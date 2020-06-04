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

import com.chapumix.solution.app.entity.dto.GenUsuarioDTO;
import com.chapumix.solution.app.models.entity.ComRole;
import com.chapumix.solution.app.models.entity.ComUsuario;
import com.chapumix.solution.app.models.service.IComRoleService;
import com.chapumix.solution.app.models.service.IComUsuarioService;

@Controller
@SessionAttributes("comUsuario")
@PropertySource(value = "application.properties", encoding="UTF-8")
public class ComUsuarioController {
	
	public static final String URLUsuario = "http://localhost:9000/api/usuarios/username"; //se obtuvo de API REST de GenUsuarioRestController	
	
	@Autowired
	private IComUsuarioService iComUsuarioService;
	
	@Autowired
	private IComRoleService iComRoleService;
	
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
	@GetMapping("/sincronizaform")
	public String sincroniza(Map<String, Object> model) {
		ComUsuario comUsuario = new ComUsuario();
		model.put("titulo", utf8(this.titulosincronizarusuario));
		model.put("comUsuario", comUsuario);
		model.put("enlace8", enlace8);
		return "sincronizaform";
	}
	
	
	// Este metodo me permite sincronizar los usuarios
	@RequestMapping(value = "/sincronizaform", method = RequestMethod.POST)
	public String sincronizaUsuario(@RequestParam(value = "documento", required = false) String documento, Model model, RedirectAttributes flash, SessionStatus status) {
		
		if(documento != "") {
			
			String sinespaciosdocumento = documento.replace(" ", "");
			List<String> list = Stream.of(sinespaciosdocumento.trim().split(",")).collect(Collectors.toList());			
						
			list.forEach(doc -> {				
			
				ComUsuario usuarioBuscar = iComUsuarioService.findByUsuario(doc);				
				
				if(usuarioBuscar == null) {
					// proceso API para consultar el medico.			
					ResponseEntity<GenUsuarioDTO> respuestam = restTemplate.exchange(URLUsuario + '/' + doc, HttpMethod.GET, null, new ParameterizedTypeReference<GenUsuarioDTO>() {});
					GenUsuarioDTO usuario = respuestam.getBody();
					
					ComUsuario guardar = usuarioAgregar(usuario);					
					iComUsuarioService.save(guardar);
					flash.addFlashAttribute("success", "Usuario(s) sincronizado correctamente");					
				}				
				
			});
			
			
			
		}else {
			flash.addFlashAttribute("error", "El documento del usuario es requerido.");
			return "redirect:sincronizaform";
		}
		//String mensajeFlash = (comRole.getId() != null) ? "El rol fue editado correctamente" : "El rol fue creado correctamente";

		//iComRoleService.save(comRole);
		model.addAttribute("titulo", utf8(this.titulousuarios));
		model.addAttribute("enlace8", enlace8);
		status.setComplete();
		flash.addFlashAttribute("success", "Usuario sincronizado correctamente");
		return "redirect:sincronizaform";
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
