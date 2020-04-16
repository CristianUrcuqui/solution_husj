package com.chapumix.solution.app.controllers;

import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.chapumix.solution.app.entity.dto.EstCertificadoDTO;
import com.chapumix.solution.app.entity.dto.GenPacienDTO;
import com.chapumix.solution.app.entity.dto.GenUsuarioDTO;
import com.chapumix.solution.app.models.entity.EstCertificado;
import com.chapumix.solution.app.models.entity.EstSerial;
import com.chapumix.solution.app.models.service.IEstCertificadoService;
import com.chapumix.solution.app.models.service.IEstSerialService;
import com.chapumix.solution.app.models.service.IEstTipoCertificadoService;


@Controller
@PropertySource(value = "application.properties", encoding="UTF-8")
public class EstCertificadoController {
	
	public static final String URLPaciente = "http://localhost:9000/api/pacientegeneral";
	public static final String URLMedico = "http://localhost:9000/api/usuarios/username";
	
	@Autowired
	private IEstTipoCertificadoService iEstTipoCertificadoService;
	
	@Autowired
	private IEstCertificadoService iEstCertificadoService;
	
	@Autowired
	private IEstSerialService iEstSerialService;
	
	@Autowired
	private RestTemplate restTemplate;
	
	@Value("${app.tituloestadistica}")
	private String tituloestadistica;
	
	@Value("${app.enlaceprincipalestadistica}")
	private String enlaceprincipalestadistica;
	
	@Value("${app.enlace7}")
	private String enlace7;
	
	
	/* ----------------------------------------------------------
     * INDEX ESTADISTICA
     * ---------------------------------------------------------- */
	
	//INDEX ESTADISTICA
	@GetMapping("/indexcertificado")
	public String index(Model model) {
		model.addAttribute("titulo", utf8(this.tituloestadistica));
		model.addAttribute("estadistica", enlaceprincipalestadistica);
		model.addAttribute("enlace7", enlace7);
		return "indexcertificado";
	}
	
	
	/* ----------------------------------------------------------
     * AJUSTES ESTADISTICA
     * ---------------------------------------------------------- */
	
	// Este metodo me permite visualizar o cargar el formulario del consecutivo
	@GetMapping("/parametrocertificado")
	public String crearParametroCertificado(Map<String, Object> model) {
		EstSerial estSerial = new EstSerial();
		model.put("titulo", utf8(this.tituloestadistica));
		model.put("estadistica", enlaceprincipalestadistica);
		model.put("tipos", iEstTipoCertificadoService.findAll());
		model.put("estSerial", estSerial);
		model.put("enlace7", enlace7);
		return "parametrocertificado";
	}
	
	
	// Este metodo me permite guardar el consecutivo
	@RequestMapping(value = "/parametrocertificado", method = RequestMethod.POST)
	public String guardarSerial(@Valid EstSerial estSerial, BindingResult result, Model model, Principal principal, RedirectAttributes flash, SessionStatus status) {
		if (result.hasErrors()) {
			model.addAttribute("titulo", utf8(this.tituloestadistica));
			model.addAttribute("tipos", iEstTipoCertificadoService.findAll());
			model.addAttribute("estSerial", estSerial);
			model.addAttribute("enlace7", enlace7);
			return "parametrocertificado";
		}
		
		List<String> errores = new ArrayList<>();		
		if(estSerial.getSerialInicial() < estSerial.getSerialFinal() && estSerial.getSerialInicial() instanceof Integer && estSerial.getSerialFinal() instanceof Integer) {
			List<EstSerial> estSerialGuardado = iEstSerialService.findAll();
			
						
			for(Integer i = estSerial.getSerialInicial(); i<=estSerial.getSerialFinal(); i++) {
				
				//encapsulamos la variable i para poder usarla en el predicate
				final Integer p = new Integer(i);
				//Este se usa para buscar por medio de un predicate un valor y me devuelve o falso o verdadero
				boolean encontradoDato = estSerialGuardado.stream().anyMatch(action -> action.getSerial().equals(p.toString().trim()));				
					
					if (!encontradoDato) {
						EstSerial estSerialGuardar = new EstSerial();
						estSerialGuardar.setSerial(p.toString().trim());
						estSerialGuardar.setEstado(false);
						estSerialGuardar.setLoginUsrAlta(principal.getName());
						estSerialGuardar.setEstTipoCertificado(estSerial.getEstTipoCertificado());
						estSerialGuardar.setSerialInicial(estSerial.getSerialInicial());
						estSerialGuardar.setSerialFinal(estSerial.getSerialFinal());
						iEstSerialService.save(estSerialGuardar);
					}else {						
						//agregamos a una lista los seriales no ingresados
						errores.add(i.toString());											
					}
			}			
			
		}else {
			flash.addFlashAttribute("error", "El rango de los seriales no son validos");
			return "redirect:parametrocertificado";
		}
		
		
		String mensajeFlash = (estSerial.getId() != null) ? "El consecutivo fue editado correctamente" : "El consecutivo fue creado correctamente";		
		status.setComplete();
		//me muestra los errores si hay contenido en el listado
		if(errores.size() > 0) {
			flash.addFlashAttribute("error", "Seriales duplicados no ingresados: "+errores);
		}		
		flash.addFlashAttribute("success", mensajeFlash);
		return "redirect:indexcertificado";
	}
	
	
	/* ----------------------------------------------------------
     * CERTIFICADO ESTADISTICA
     * ---------------------------------------------------------- */
	
	// Este metodo me permite listar todos los certificados realizados
	@GetMapping("/certificadoestadistica")
	public String listar(Model model) {
		
		List<EstCertificado> certificados = iEstCertificadoService.findAll();
		List<EstCertificadoDTO> newCertificados = new ArrayList<>();
		
		certificados.forEach(c ->{
			
			// proceso API para consultar el paciente.
			//System.out.println(restTemplate.exchange(URLPaciente + '/' + c.getDocPaciente(), HttpMethod.GET, null, new ParameterizedTypeReference<GenPacienDTO>() {}));
			ResponseEntity<List<GenPacienDTO>> respuestap = restTemplate.exchange(URLPaciente + '/' + c.getDocPaciente(), HttpMethod.GET, null, new ParameterizedTypeReference<List<GenPacienDTO>>() {});
			List<GenPacienDTO> paciente = respuestap.getBody();	
						
			// proceso API para consultar el medico.			
			ResponseEntity<GenUsuarioDTO> respuestam = restTemplate.exchange(URLMedico + '/' + c.getLoginUsrAlta(), HttpMethod.GET, null, new ParameterizedTypeReference<GenUsuarioDTO>() {});
			GenUsuarioDTO medico = respuestam.getBody();
			
			EstCertificadoDTO dto = new EstCertificadoDTO(c.getId(), c.getEstSerial().getSerial(), paciente.get(0).getPacNumDoc(), paciente.get(0).getPacPriNom() +" "+ paciente.get(0).getPacSegNom() +" "+ paciente.get(0).getPacPriApe()+" "+ paciente.get(0).getPacSegApe(), c.getEstSerial().getEstTipoCertificado().getTipoCertificado(), medico.getUsuDescri(), c.getFechaAlta());
			newCertificados.add(dto);		
			
		});
		
		model.addAttribute("titulo", utf8(this.tituloestadistica));		
		model.addAttribute("estadistica", enlaceprincipalestadistica);
		model.addAttribute("listcertificado", newCertificados);		
		model.addAttribute("enlace7", enlace7);
		return "certificadoestadistica";
	}
	
	
	// Este metodo me permite visualizar o cargar el formulario del certificado
	@GetMapping("/certificadoform")
	public String crearCertificado(Map<String, Object> model) {		
		EstCertificado estCertificado = new EstCertificado();
		model.put("titulo", utf8(this.tituloestadistica));
		model.put("tipos", iEstTipoCertificadoService.findAll());
		model.put("estCertificado", estCertificado);
		model.put("enlace7", enlace7);
		return "certificadoform";
	}
	
	// Este metodo me permite guardar el certificado
	@RequestMapping(value = "/certificadoform", method = RequestMethod.POST)
	public String guardarCertificado(@Valid EstCertificado estCertificado, BindingResult result, Model model, Principal principal, RedirectAttributes flash, SessionStatus status) {
		
		if (result.hasErrors()) {
			model.addAttribute("titulo", utf8(this.tituloestadistica));
			model.addAttribute("tipos", iEstTipoCertificadoService.findAll());
			model.addAttribute("estCerticado", estCertificado);
			model.addAttribute("enlace7", enlace7);
			return "certificadoform";
		}
		
	
		String mensajeFlash = (estCertificado.getId() != null) ? "El certificado fue editado correctamente" : "El certificado fue creado correctamente";

		//List<EstCertificado> validar = iEstCertificadoService.findByName(estCertificado.getDocPaciente());
		EstCertificado validar = iEstCertificadoService.findByNameTipo(estCertificado.getDocPaciente(), estCertificado.getEstSerial().getEstTipoCertificado().getTipoCertificado());
				
		if(validar == null ) {
			EstSerial serial = iEstSerialService.findSerialBySerialAndTipo(estCertificado.getEstSerial().getSerial(), estCertificado.getEstSerial().getEstTipoCertificado().getTipoCertificado());
			estCertificado.setLoginUsrAlta(principal.getName());
			estCertificado.setEstSerial(serial);
			//guardo el certificado
			iEstCertificadoService.save(estCertificado);
			
			
			//actualizo el estado del serial
			//EstSerial serial = iEstSerialService.findById(estCertificado.getEstSerial().getId());
			serial.setEstado(true);
			serial.setLoginUsrAct(principal.getName());
			serial.setFechaAltaAct(new Date());
			serial.setSerialInicial(0);
			serial.setSerialFinal(0);
			iEstSerialService.save(serial);
			status.setComplete();
			flash.addFlashAttribute("success", mensajeFlash);
			return "redirect:certificadoestadistica";			
		}else {
			flash.addFlashAttribute("error", "El paciente ya posee un certificado de " + estCertificado.getEstSerial().getEstTipoCertificado().getTipoCertificado());
			return "redirect:certificadoform";			
		}		
	}
	
	// Este metodo me permite eliminar el certificado
	@RequestMapping(value = "/eliminarcertificado/{id}")
	public String eliminar(@PathVariable(value = "id") Long id, RedirectAttributes flash, Principal principal) {
		
		EstCertificado estCertificado = iEstCertificadoService.findById(id);
		EstSerial serial = iEstSerialService.findById(estCertificado.getEstSerial().getId());		
		
		if (id > 0) {
			iEstCertificadoService.delete(id);
			serial.setEstado(false);
			serial.setFechaDelAct(new Date());
			serial.setLoginUsrDel(principal.getName());
			serial.setSerialInicial(0);
			serial.setSerialFinal(0);
			iEstSerialService.save(serial);
			flash.addFlashAttribute("success", "El certificado fue eliminado correctamente");
		}
		return "redirect:/certificadoestadistica";
	}
		
	
	//Este metodo me permite cargar el serial por tipo de certificado		
	@RequestMapping(value = "/cargarSerial")
	@ResponseBody
	public String cargarSerial(@RequestParam Long id) {
		String serial = "";
		EstSerial estSerial = iEstSerialService.findSerialByTipo(id);
		if(estSerial != null) {
			serial = estSerial.getSerial();
		}else {
			serial = "0";
		}				
		return serial;
	}	
	
	@GetMapping(value = "/cargarPaciente/{term}", produces = { "application/json" })
	@ResponseBody
	public  List<GenPacienDTO> cargarPacientes(@PathVariable String term) {
		// proceso API para buscar el paciente 
		ResponseEntity<List<GenPacienDTO>> respuestaa = restTemplate.exchange(URLPaciente + '/' + term, HttpMethod.GET, null,new ParameterizedTypeReference<List<GenPacienDTO>>() {});
		List<GenPacienDTO> dinamica = respuestaa.getBody();		
		return dinamica;
	}
	
	
	
	
	
	
	
	/* ----------------------------------------------------------
     * METODOS ADICIONALES 
     * ---------------------------------------------------------- */							
	
	//Se usa para codificacion ISO-8859-1 a UTF-8  
		public String utf8(String input) {
			String output = "";
			try {
				/* From ISO-8859-1 to UTF-8 */
				output = new String(input.getBytes("ISO-8859-1"), "UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			return output;
		}

}