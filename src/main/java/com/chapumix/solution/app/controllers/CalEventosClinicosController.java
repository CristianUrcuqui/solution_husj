package com.chapumix.solution.app.controllers;

import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.chapumix.solution.app.models.entity.CalEvento;
import com.chapumix.solution.app.models.entity.ComCatalogoEvento;
import com.chapumix.solution.app.models.entity.ComTipoEvento;
import com.chapumix.solution.app.models.service.ICalEventoService;
import com.chapumix.solution.app.models.service.IComCatalogoEventoService;
import com.chapumix.solution.app.models.service.IComProfesionService;
import com.chapumix.solution.app.models.service.IComTipoEventoService;
import com.chapumix.solution.app.models.service.IComUsuarioService;
import com.chapumix.solution.app.utils.IncorrectSaveException;


@Controller
@SessionAttributes("calEvento")
@PropertySource(value = "application.properties", encoding="UTF-8")
public class CalEventosClinicosController {
	
	@Autowired
	private ICalEventoService iCalEventoService;
	
	@Autowired
	private IComProfesionService iComProfesionService;
	
	@Autowired
	private IComUsuarioService iComUsuarioService;
	
	@Autowired
	private IComTipoEventoService iComTipoEventoService;
	
	@Autowired
	private IComCatalogoEventoService iComCatalogoEventoService;
	
	@Autowired
	private RestTemplate restTemplate;
	
	@Value("${app.tituloregingresohops}")
	private String tituloregingresohops;
	
	@Value("${app.tituloreingresohosp}")
	private String tituloreingresohosp;
	
	@Value("${app.tituloeventosclinicos}")
	private String tituloeventosclinicos;
	
	@Value("${app.enlaceprincipalcalidad}")
	private String enlaceprincipalcalidad;
	
	@Value("${app.titulonuevoeventoclinico}")
	private String titulonuevoeventoclinico;	
	
	@Value("${app.enlace5}")
	private String enlace5;
	
	
	/* ----------------------------------------------------------
     * INDEX CALIDAD EVENTOS CLINICOS
     * ---------------------------------------------------------- */
	
	
	//INDEX CALIDAD EVENTOS CLINICOS
	@GetMapping("/indexeventosclinicos")
	public String index(Model model) {
		model.addAttribute("titulo", utf8(this.tituloeventosclinicos));
		model.addAttribute("calidad", enlaceprincipalcalidad);
		model.addAttribute("enlace5", enlace5);
		return "indexeventosclinicos";
	}
	
	//INDEX REINGRESO HOSPITALARIO
		@GetMapping("/indexreingresohosp")
		public String reingresohops(Model model) {
			model.addAttribute("titulo", utf8(this.tituloreingresohosp));
			model.addAttribute("calidad", enlaceprincipalcalidad);
			model.addAttribute("enlace5", enlace5);
			return "indexreingresohosp";
		}
	

	
	
	// Este metodo me permite visualizar o cargar el formulario para mortalidad
	@GetMapping("/eventoclinicoregform")
	public String crearRegistroEventoClinico(Map<String, Object> model) {	
			
	CalEvento calEvento = new CalEvento();

		model.put("titulo", utf8(this.titulonuevoeventoclinico));
		model.put("calEvento", calEvento);
		model.put("profesion", iComProfesionService.findAll());
		model.put("tipoevento", iComTipoEventoService.findAll());
		model.put("enlace5", enlace5);
		return "eventoclinicoregform";
	}
	
	// Este metodo me permite visualizar registro de reingreso hospitalario
	@GetMapping("/regreingresohospfrom")
	public String crearRegistroReingresoHospitalario(Map<String, Object> model) {	
			
	CalEvento calEvento = new CalEvento();

		model.put("titulo", utf8(this.tituloregingresohops));
		model.put("calEvento", calEvento);
		model.put("profesion", iComProfesionService.findAll());
		model.put("tipoevento", iComTipoEventoService.findAll());
		model.put("enlace5", enlace5);
		return "regreingresohospfrom";
	}
	
	
	
	
	// Este metodo me permite guardar el analisis de mortalidad
	@RequestMapping(value = "/eventoclinicoregform", method = RequestMethod.POST)
	public String guardarEvento(@Valid CalEvento calEvento, BindingResult result, Model model, @RequestParam(name = "anonimo") String anonimo, Principal principal, RedirectAttributes flash, SessionStatus status) throws IncorrectSaveException {
		
		if (result.hasErrors()) {
			model.addAttribute("titulo", utf8(this.titulonuevoeventoclinico));
			model.addAttribute("profesion", iComProfesionService.findAll());		
			model.addAttribute("enlace5", enlace5);
			return "eventoclinicoregform";
		}		
		
		//condicional en caso de que sea anonimo y traemos el usuario anonimo de la tabla com_usuario
		if(anonimo.equals("312")) {
			calEvento.setComUsuario(iComUsuarioService.findByUsuario(anonimo));
		}else {
			calEvento.setComUsuario(iComUsuarioService.findByUsuario(principal.getName()));
		}
		
		String mensajeFlash = (calEvento.getId() != null) ? "El evento fue editado correctamente" : "El evento fue creado correctamente";
		
		try {
			iCalEventoService.save(calEvento);
			status.setComplete();
			flash.addFlashAttribute("success", mensajeFlash);		
			return "redirect:eventoclinicoregform";
		} catch (Exception e) {
			throw new IncorrectSaveException("Lo sentimos ha ocurrido un error");
		}
		
		
		
	}
	
	
	
	/* ----------------------------------------------------------
     * METODOS ADICIONALES 
     * ---------------------------------------------------------- */	
	
	
	//Este metodo me permite cargar los catalogos de un determinado tipo de evento
	@RequestMapping(value = "/cargarCatalogoEvento")
	@ResponseBody
	public HashMap<Long, String> cargarCatalogoEvento(@RequestParam Long id) {
			
		HashMap<Long, String> catalogos = new HashMap<Long, String>();
		//LinkedHashMap<Object, Object> test = new LinkedHashMap<Object, Object>();
		
		ComTipoEvento comTipoEvento = iComTipoEventoService.findById(id);
		List<ComCatalogoEvento> comCatalogoEventos = iComCatalogoEventoService.findByTipoEvento(comTipoEvento);
		for(ComCatalogoEvento ComCatalogoEvento : comCatalogoEventos) {
			catalogos.put(ComCatalogoEvento.getId(), ComCatalogoEvento.getNombre());
			//test.putAll(m);
		
		}
		
		return catalogos;		
		
	}	
	
	
	
	// Se usa para codificacion ISO-8859-1 a UTF-8
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