package com.chapumix.solution.app.controllers;

import java.io.UnsupportedEncodingException;
import java.security.Principal;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.chapumix.solution.app.entity.dto.GenAreSerDTO;
import com.chapumix.solution.app.models.entity.AtenEncuDatoBasico;
import com.chapumix.solution.app.models.service.IAtenEncuDatoBasicoService;
import com.chapumix.solution.app.models.service.IComGeneroService;


@Controller
@SessionAttributes("calCalendario")
@PropertySource(value = "application.properties", encoding="UTF-8")
public class AtenEncuestaController {
	
	public static final String URLServicio = "http://localhost:9000/api/camasmedico/servicio"; //se obtuvo de API REST de HcnOrdHospRestController
		
	@Autowired
	private IAtenEncuDatoBasicoService iAtenEncuDatoBasicoService;	
	
	@Autowired
	private IComGeneroService iComGeneroService;
	
	
	@Autowired
	private RestTemplate restTemplate;
	 
	@Value("${app.tituloencuesta}")
	private String tituloencuesta;	
	
	@Value("${app.enlaceprincipalsiau}")
	private String enlaceprincipalsiau;
	
	@Value("${app.enlace9}")
	private String enlace9;
	
	
	
	/* ----------------------------------------------------------
     * INDEX ENCUESTA DE SATISFACCION SIAU
     * ---------------------------------------------------------- */
	
	@GetMapping("/indexencuestasat")
	public String index(Model model) {
		model.addAttribute("titulo", utf8(this.tituloencuesta));
		model.addAttribute("siau", enlaceprincipalsiau);
		model.addAttribute("enlace9", enlace9);
		return "indexencuestasat";
	}
	
	
	/* ----------------------------------------------------------
     * ENCUESTA
     * ---------------------------------------------------------- */
	
	// Este metodo me permite visualizar o cargar el formulario de la encuesta
	@GetMapping("/encuestaform")
	public String crearEncuesta(Map<String, Object> model) {
		AtenEncuDatoBasico atenEncuDatoBasico = new AtenEncuDatoBasico();
		boolean myBooleanVariable = true;
		
		// proceso API para consultar el servicio.			
		ResponseEntity<List<GenAreSerDTO>> respuestas = restTemplate.exchange(URLServicio, HttpMethod.GET, null, new ParameterizedTypeReference<List<GenAreSerDTO>>() {});
		List<GenAreSerDTO> servicio = respuestas.getBody();		
		
		model.put("titulo", utf8(this.tituloencuesta));
		model.put("servicio", servicio);
		model.put("genero", iComGeneroService.findAll());
		model.put("myBooleanVariable", myBooleanVariable);
		model.put("atenEncuDatoBasico", atenEncuDatoBasico);
		model.put("enlace9", enlace9);
		return "encuestaform";
	}
	
	
	// Este metodo me permite guardar la encuesta
	@RequestMapping(value = "/encuestaform", method = RequestMethod.POST)
	public String guardarRol(@Valid AtenEncuDatoBasico atenEncuDatoBasico, BindingResult result, Model model, Principal principal, RedirectAttributes flash, SessionStatus status) {
		
		// proceso API para consultar el servicio.			
		ResponseEntity<List<GenAreSerDTO>> respuestas = restTemplate.exchange(URLServicio, HttpMethod.GET, null, new ParameterizedTypeReference<List<GenAreSerDTO>>() {});
		List<GenAreSerDTO> servicio = respuestas.getBody();	
		
		
		if (result.hasErrors()) {
			model.addAttribute("titulo", utf8(this.tituloencuesta));
			model.addAttribute("servicio", servicio);
			model.addAttribute("genero", iComGeneroService.findAll());			
			model.addAttribute("atenEncuDatoBasico", atenEncuDatoBasico);			
			model.addAttribute("enlace9", enlace9);
			return "encuestaform";
		}
		
		// obtengo el nombre del servicio por el oid de dinamica
		GenAreSerDTO nombreServicio = servicio.stream()
				.filter(s -> atenEncuDatoBasico.getIdServicio().equals(s.getOid().toString())).findAny().orElse(null);

		// establezco el nombre del servicio para guardar.
		atenEncuDatoBasico.setServicio(nombreServicio.getGasNombre());
				
		

		String mensajeFlash = (atenEncuDatoBasico.getId() != null) ? "La encuesta fue editada correctamente" : "La encuesta fue creada correctamente";

		atenEncuDatoBasico.setLoginUsrAlta(principal.getName());
		iAtenEncuDatoBasicoService.save(atenEncuDatoBasico);
		status.setComplete();
		flash.addFlashAttribute("success", mensajeFlash);
		return "redirect:encuestaform";
	}
	
	
	
	
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