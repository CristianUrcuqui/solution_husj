package com.chapumix.solution.app.controllers;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.chapumix.solution.app.entity.dto.GenPacienDTO;
import com.chapumix.solution.app.entity.dto.GenUsuarioDTO;
import com.chapumix.solution.app.models.entity.ComGenero;
import com.chapumix.solution.app.models.entity.ComRole;
import com.chapumix.solution.app.models.entity.ComTipoDocumento;
import com.chapumix.solution.app.models.entity.ComUsuario;
import com.chapumix.solution.app.models.entity.GenPacien;
import com.chapumix.solution.app.models.service.IComGeneroService;
import com.chapumix.solution.app.models.service.IComRoleService;
import com.chapumix.solution.app.models.service.IComTipoDocumentoService;
import com.chapumix.solution.app.models.service.IComUsuarioService;
import com.chapumix.solution.app.models.service.IGenPacienService;

@Controller
@SessionAttributes({"comUsuario", "genPacien"})
@PropertySource(value = "application.properties", encoding="UTF-8")
public class ComUsuarioController {
	
	public static final String URLUsuario = "http://localhost:9000/api/usuarios/username"; //se obtuvo de API REST de GenUsuarioRestController
	
	public static final String URLPaciente = "http://localhost:9000/api/pacientegeneral"; //se obtuvo de API REST de GenPacienRestController
	
	@Autowired
	private IComUsuarioService iComUsuarioService;
	
	@Autowired
	private IComRoleService iComRoleService;
	
	@Autowired
	private IGenPacienService iGenPacienService;
	
	@Autowired
	private IComGeneroService iComGeneroService;
	
	@Autowired
	private IComTipoDocumentoService iComTipoDocumentoService;
	
	@Autowired
	private RestTemplate restTemplate;
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	@Value("${app.titulousuarios}")
	private String titulousuarios;
	
	@Value("${app.enlaceprincipalajustes}")
	private String enlaceprincipalajustes;
	
	@Value("${app.titulosincronizarusuario}")
	private String titulosincronizarusuario;
	
	@Value("${app.titulosincronizarpaciente}")
	private String titulosincronizarpaciente;	
	
	@Value("${app.enlace3}")
	private String enlace3;
	
	@Value("${app.enlace8}")
	private String enlace8;
	
	
	/* ----------------------------------------------------------
     * USUARIOS SISTEMA
     * ---------------------------------------------------------- */
	
	
	// Este metodo me permite listar los usuarios del sistema
	@GetMapping("/usuariolistado")
	public String listarUsuario(Model model) {					
		model.addAttribute("titulo", utf8(this.titulousuarios));
		model.addAttribute("listusuario", iComUsuarioService.findAll());
		model.addAttribute("ajustes", enlaceprincipalajustes);
		model.addAttribute("enlace3", enlace3);		
		return "usuariolistado";
	}
	
	// Este metodo me permite guardar el usuario del sistema
		@RequestMapping(value = "/usuarioform", method = RequestMethod.POST)
		public String guardarUsuario(@Valid ComUsuario comUsuario, BindingResult result, Model model, RedirectAttributes flash, SessionStatus status) {
			if(result.hasErrors()) {
				model.addAttribute("titulo", utf8(this.titulousuarios));
				model.addAttribute("comUsuario", comUsuario);				
				model.addAttribute("ajustes", enlaceprincipalajustes);
				model.addAttribute("enlace3", enlace3);
				return "usuariolistado";
			}
			
			String mensajeFlash = (comUsuario.getId() != null) ? "El usuario fue editado correctamente" : "El usuario fue creado correctamente";
			
			iComUsuarioService.save(comUsuario);
			status.setComplete();
			flash.addFlashAttribute("success", mensajeFlash);		
			return "redirect:usuariolistado";
		}
	
	// Este metodo me permite cargar los datos para editar el usuario del sistema
	@RequestMapping(value = "/usuarioform")
	public String editarUsuario(@RequestParam(value = "id", required = false) Long id, Map<String, Object> model, RedirectAttributes flash) {		
				
		ComUsuario comUsuario = null;
							
		if(id > 0) {			
			comUsuario = iComUsuarioService.findById(id);
				if(comUsuario == null) {
					flash.addFlashAttribute("error", "El ID del usuario no existe en la base de datos");
					return "redirect:/usuariolistado";
				}
				}else {
					flash.addFlashAttribute("error", "El ID del usuario no puede ser 0");
					return "redirect:/usuariolistado";
				}		
			
		model.put("titulo", utf8(this.titulousuarios));	
		model.put("comUsuario", comUsuario);
		model.put("roles",iComRoleService.findAll());
		model.put("ajustes", enlaceprincipalajustes);
		model.put("enlace3", enlace3);
		return "usuarioform";
	}
	
	
	// Este metodo me permite visualizar o cargar el formulario de la sincronizacion de usuarios
	@GetMapping("/sincronizausuarioform")
	public String crearUsuario(Map<String, Object> model) {
		ComUsuario comUsuario = new ComUsuario();
		model.put("titulo", utf8(this.titulosincronizarusuario));
		model.put("ajustes", enlaceprincipalajustes);
		model.put("comUsuario", comUsuario);
		model.put("enlace3", enlace3);
		return "sincronizausuarioform";
	}
	
	
	// Este metodo me permite sincronizar los usuarios
	@RequestMapping(value = "/sincronizausuarioform", method = RequestMethod.POST)
	public String guardarUsuario(@RequestParam(value = "documento", required = false) String documento, Model model, RedirectAttributes flash, SessionStatus status) {
		
		if(documento != "") {
			
			String sinespaciosdocumento = documento.replace(" ", "");
			List<String> list = Stream.of(sinespaciosdocumento.trim().split(",")).collect(Collectors.toList());			
						
			list.forEach(doc -> {				
			
				ComUsuario usuarioBuscar = iComUsuarioService.findByUsuario(doc);				
				
				if(usuarioBuscar == null) {
					// proceso API para consultar el usuario en dinamica.			
					ResponseEntity<GenUsuarioDTO> respuestam = restTemplate.exchange(URLUsuario + '/' + doc, HttpMethod.GET, null, new ParameterizedTypeReference<GenUsuarioDTO>() {});
					GenUsuarioDTO usuario = respuestam.getBody();
					
					if(usuario != null) {
						ComUsuario guardar = usuarioAgregar(usuario);					
						iComUsuarioService.save(guardar);
						flash.addFlashAttribute("success", "Usuario(s) sincronizado correctamente");
					}else {
						flash.addFlashAttribute("error", "El usuario no fue encontrado en dinamica");
					}
										
				}				
				
			});
			
			
			
		}else {
			flash.addFlashAttribute("error", "El documento del usuario es requerido.");
			model.addAttribute("ajustes", enlaceprincipalajustes);
			model.addAttribute("enlace3", enlace3);
			return "redirect:sincronizausuarioform";
		}
		
		model.addAttribute("titulo", utf8(this.titulousuarios));
		model.addAttribute("ajustes", enlaceprincipalajustes);
		model.addAttribute("enlace3", enlace3);
		status.setComplete();		
		return "redirect:sincronizausuarioform";
	}
	
	
	// Este metodo me permite visualizar o cargar el formulario de la sincronizacion de pacientes
	@GetMapping("/sincronizapacienteform")
	public String crearPaciente(Map<String, Object> model) {		
		GenPacien genPacien = new GenPacien();
		model.put("titulo", utf8(this.titulosincronizarpaciente));
		model.put("ajustes", enlaceprincipalajustes);
		model.put("genPacien", genPacien);
		model.put("enlace3", enlace3);
		return "sincronizapacienteform";
	}
	
	// Este metodo me permite sincronizar los pacientes
	@RequestMapping(value = "/sincronizapacienteform", method = RequestMethod.POST)
	public String guardarPaciente(@RequestParam(value = "documento", required = false) String documento, Model model, RedirectAttributes flash, SessionStatus status) {
		
		if(documento != "") {
			
			String sinespaciosdocumento = documento.replace(" ", "");
			List<String> list = Stream.of(sinespaciosdocumento.trim().split(",")).collect(Collectors.toList());	
			
			list.forEach(doc ->{
				
				GenPacien genPacien = iGenPacienService.findByNumberDoc(doc);
				
				if(genPacien == null) {					
					
					// proceso API para buscar el paciente
					ResponseEntity<List<GenPacienDTO>> respuestaa = restTemplate.exchange(URLPaciente + '/' + doc, HttpMethod.GET, null,new ParameterizedTypeReference<List<GenPacienDTO>>() {});
					List<GenPacienDTO> dinamica = respuestaa.getBody();
					
					if(!dinamica.isEmpty()) {
						GenPacien guardarPaciente = pacienteAgregar(dinamica);					
						iGenPacienService.save(guardarPaciente);					
						flash.addFlashAttribute("success", "Paciente(s) sincronizado correctamente");
					}else {
						flash.addFlashAttribute("error", "El paciente no fue encontrado en dinamica");						
					}
										
				}				
			});			
		}
		else {
			flash.addFlashAttribute("error", "El documento del paciente es requerido.");
			model.addAttribute("ajustes", enlaceprincipalajustes);
			model.addAttribute("enlace3", enlace3);
			return "redirect:sincronizapacienteform";
		}	
		
		model.addAttribute("titulo", utf8(this.titulosincronizarpaciente));
		model.addAttribute("ajustes", enlaceprincipalajustes);
		model.addAttribute("enlace3", enlace3);
		status.setComplete();
		//flash.addFlashAttribute("success", "Paciente sincronizado correctamente");
		return "redirect:sincronizapacienteform";		
	}	

	

	

	/* ----------------------------------------------------------
     * METODOS ADICIONALES 
     * ---------------------------------------------------------- */
							
	//Se usa para codificacion ISO-8859-1 a UTF-8  
	private String utf8(String input) {
		String output = "";
		if(input != null) {
		try {
			/* From ISO-8859-1 to UTF-8 */
			output = new String(input.getBytes("ISO-8859-1"), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}}else {
			return "";
		}
		return output;
	}
	
	//Se usa para obtener el usuario a guardar
	private ComUsuario usuarioAgregar(GenUsuarioDTO usuario) {
		
		ComUsuario nuevo = new ComUsuario();
		//obtengo el rol temporal
		List<ComRole> listRoles = obtenerRoles(3L);
		nuevo.setUsuario(usuario.getUsuNombre());
		nuevo.setContrasena(passwordEncoder.encode(usuario.getUsuClave()));
		nuevo.setNombreCompleto(usuario.getUsuDescri());
		nuevo.setNombreCorto(recortar(usuario.getUsuDescri()));
		nuevo.setEstado(true);
		nuevo.setRoles(listRoles);	
		return nuevo;
	}
	
	//Se usa para obtener el paciente a guardar
	private GenPacien pacienteAgregar(List<GenPacienDTO> dinamica) {
		GenPacien agregarPaciente = new GenPacien();
		//buscamos el sexo del paciente			
		ComGenero sexoPaciente = iComGeneroService.findById(dinamica.get(0).getGpasexpac().longValue());
		
		//buscamos el tipo de documento del paciente			
		ComTipoDocumento tipoDocumento = iComTipoDocumentoService.findById(dinamica.get(0).getPacTipDoc().longValue());		
		
		agregarPaciente.setOid(dinamica.get(0).getOid());
		agregarPaciente.setPacNumDoc(dinamica.get(0).getPacNumDoc());
		agregarPaciente.setPacPriNom(dinamica.get(0).getPacPriNom());
		agregarPaciente.setPacSegNom(dinamica.get(0).getPacSegNom());
		agregarPaciente.setPacPriApe(dinamica.get(0).getPacPriApe());
		agregarPaciente.setPacSegApe(dinamica.get(0).getPacSegApe());
		agregarPaciente.setGpafecnac(dinamica.get(0).getGpafecnac());
		agregarPaciente.setComGenero(sexoPaciente);
		agregarPaciente.setComTipoDocumento(tipoDocumento);
		return agregarPaciente;
	}

	// metodo para obtener el primer nombre del funcionario
	private String recortar(String cadena) {
		String datos[] = cadena.split(" ");
		return datos[0];
	}
	
	// metodo para obtener el rol TMP
	private List<ComRole> obtenerRoles(long l) {
		ComRole role = iComRoleService.findById(l);
		List<ComRole> roles = new ArrayList<>();
		role.setNombre(role.getNombre());
		roles.add(role);
		return roles;
	}	

}
