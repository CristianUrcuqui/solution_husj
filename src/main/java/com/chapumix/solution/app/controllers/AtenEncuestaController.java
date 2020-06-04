package com.chapumix.solution.app.controllers;

import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.chapumix.solution.app.entity.dto.AtenEncuConsolidadoDTO;
import com.chapumix.solution.app.entity.dto.GenAreSerDTO;
import com.chapumix.solution.app.models.entity.AtenEncuDatoBasico;
import com.chapumix.solution.app.models.service.IAtenEncuDatoBasicoService;
import com.chapumix.solution.app.models.service.IComGeneroService;


@Controller
@SessionAttributes("atenEncuDatoBasico")
@PropertySource(value = "application.properties", encoding="UTF-8")
public class AtenEncuestaController {
	
	public static final String URLServicio = "http://localhost:9000/api/camasmedico/servicio"; //se obtuvo de API REST de HcnOrdHospRestController
		
	@Autowired
	private IAtenEncuDatoBasicoService iAtenEncuDatoBasicoService;	
	
	@Autowired
	private IComGeneroService iComGeneroService;
	
	@Autowired
	private RestTemplate restTemplate;
	 
	@Value("${app.tituloencuestasatisfaccion}")
	private String tituloencuestasatisfaccion;
	
	@Value("${app.tituloencuesta}")
	private String tituloencuesta;
	
	@Value("${app.tituloconsolidado}")
	private String tituloconsolidado;
	
	@Value("${app.titulonegativa}")
	private String titulonegativa;
	
	
	@Value("${app.enlaceprincipalsiau}")
	private String enlaceprincipalsiau;
	
	@Value("${app.enlace9}")
	private String enlace9;
	
	
	
	/* ----------------------------------------------------------
     * INDEX ENCUESTA DE SATISFACCION SIAU
     * ---------------------------------------------------------- */
	
	@GetMapping("/indexencuestasat")
	public String index(Model model) {
		model.addAttribute("titulo", utf8(this.tituloencuestasatisfaccion));
		model.addAttribute("siau", enlaceprincipalsiau);
		model.addAttribute("enlace9", enlace9);
		return "indexencuestasat";
	}
	
	
	/* ----------------------------------------------------------
     * ENCUESTA
     * ---------------------------------------------------------- */
	
	
	// Este metodo me permite visualizar o cargar el formulario para consultar consolidado de encuestas
	@GetMapping("/consolidadoencuesta")
	public String crearListaConsolidado(Map<String, Object> model) {		
		AtenEncuDatoBasico atenEncuDatoBasico = new AtenEncuDatoBasico();
		model.put("titulo", utf8(this.tituloconsolidado));				
		model.put("atenEncuDatoBasico", atenEncuDatoBasico);
		model.put("siau", enlaceprincipalsiau);
		model.put("enlace9", enlace9);		
		return "consolidadoencuesta";
	}
	
	// Este metodo me permite visualizar o cargar el formulario de la encuesta
	@GetMapping("/encuestaform")
	public String crearEncuesta(Map<String, Object> model) {
		AtenEncuDatoBasico atenEncuDatoBasico = new AtenEncuDatoBasico();
				
		// proceso API para consultar el servicio.			
		ResponseEntity<List<GenAreSerDTO>> respuestas = restTemplate.exchange(URLServicio, HttpMethod.GET, null, new ParameterizedTypeReference<List<GenAreSerDTO>>() {});
		List<GenAreSerDTO> servicio = respuestas.getBody();		
		
		model.put("titulo", utf8(this.tituloencuesta));
		model.put("servicio", servicio);
		model.put("genero", iComGeneroService.findAll());		
		model.put("atenEncuDatoBasico", atenEncuDatoBasico);
		model.put("siau", enlaceprincipalsiau);
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
			model.addAttribute("titulo", utf8(this.tituloencuestasatisfaccion));
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
	
	
	// Este metodo me permite generar el consolidad de encuestas
	@RequestMapping("/buscarconsolidadosiau")
	public String listarConsolidado(Model model, @RequestParam(value = "fechaInicial", required = false) String fechaInicial, @RequestParam(value = "fechaFinal", required = false) String fechaFinal, RedirectAttributes flash) throws ParseException {

		//List<AtenEncuDatoBasico> atenEncuDatoBasico = new ArrayList<>();
		List<AtenEncuConsolidadoDTO> atenEncuConsolidadoDTO = new ArrayList<>();
		String errorFechas = "";

		// valida si la fecha inicial y la fecha final no estan vacios
		if (fechaInicial.equals("") && fechaFinal.equals("")) {
			errorFechas = "Debes establecer una fecha inicial y fecha final";
			model.addAttribute("error", errorFechas);
			return "consolidadoencuesta";
		}
		
		// valida si la fecha inicial y la fecha final no estan vacios
		if (fechaInicial.equals("") || fechaFinal.equals("")) {
			errorFechas = "Debes establecer una fecha inicial y fecha final";
			model.addAttribute("error", errorFechas);
			return "consolidadoencuesta";
		}
		
		// consulta por la fecha inicial y la fecha final
		if (!fechaInicial.equals("") && !fechaFinal.equals("")) {
			Date fechaI = convertirfecha(fechaInicial);
			Date fechaF = convertirfecha(fechaFinal);			
			
			// proceso API para consultar el servicio.			
			ResponseEntity<List<GenAreSerDTO>> respuestas = restTemplate.exchange(URLServicio, HttpMethod.GET, null, new ParameterizedTypeReference<List<GenAreSerDTO>>() {});
			List<GenAreSerDTO> servicio = respuestas.getBody();				
			
		servicio.forEach(s -> {
			List<AtenEncuDatoBasico> atenEncuDatoBasico = new ArrayList<>();
			atenEncuDatoBasico = iAtenEncuDatoBasicoService.findByStartDateBetween(fechaI, fechaF);
			
			//obtenemos el numero de encuentas
			long numeroEncuestas = atenEncuDatoBasico.stream().filter(a -> s.getOid().toString().equals(a.getIdServicio())).count();
			
			//obtenemos cantidad de si y no preguntas 1 y 10
			long totalSi1 = atenEncuDatoBasico.stream().filter(a -> s.getOid().toString().equals(a.getIdServicio()) && "1".equals(a.getRespuesta1())).count();
			long totalNo1 = atenEncuDatoBasico.stream().filter(a -> s.getOid().toString().equals(a.getIdServicio()) && "0".equals(a.getRespuesta1())).count();
			long totalSi10 = atenEncuDatoBasico.stream().filter(a -> s.getOid().toString().equals(a.getIdServicio()) && "1".equals(a.getRespuesta10())).count();
			long totalNo10 = atenEncuDatoBasico.stream().filter(a -> s.getOid().toString().equals(a.getIdServicio()) && "0".equals(a.getRespuesta10())).count();
			long totalSi = totalSi1 + totalSi10;
			long totalNo = totalNo1 + totalNo10;
			
			//obtenemos cantidad de Muy Mala preguntas 2, 3, 4, 5, 6, 7, 8, 9 y 11
			long totalMuyMala2 = atenEncuDatoBasico.stream().filter(a -> s.getOid().toString().equals(a.getIdServicio()) && "3".equals(a.getRespuesta2())).count();
			long totalMuyMala3 = atenEncuDatoBasico.stream().filter(a -> s.getOid().toString().equals(a.getIdServicio()) && "3".equals(a.getRespuesta3())).count();
			long totalMuyMala4 = atenEncuDatoBasico.stream().filter(a -> s.getOid().toString().equals(a.getIdServicio()) && "3".equals(a.getRespuesta4())).count();
			long totalMuyMala5 = atenEncuDatoBasico.stream().filter(a -> s.getOid().toString().equals(a.getIdServicio()) && "3".equals(a.getRespuesta5())).count();
			long totalMuyMala6 = atenEncuDatoBasico.stream().filter(a -> s.getOid().toString().equals(a.getIdServicio()) && "3".equals(a.getRespuesta6())).count();
			long totalMuyMala7 = atenEncuDatoBasico.stream().filter(a -> s.getOid().toString().equals(a.getIdServicio()) && "3".equals(a.getRespuesta7())).count();
			long totalMuyMala8 = atenEncuDatoBasico.stream().filter(a -> s.getOid().toString().equals(a.getIdServicio()) && "3".equals(a.getRespuesta8())).count();
			long totalMuyMala9 = atenEncuDatoBasico.stream().filter(a -> s.getOid().toString().equals(a.getIdServicio()) && "3".equals(a.getRespuesta9())).count();
			long totalMuyMala11 = atenEncuDatoBasico.stream().filter(a -> s.getOid().toString().equals(a.getIdServicio()) && "3".equals(a.getRespuesta11())).count();
			long totalMuyMala = totalMuyMala2 + totalMuyMala3 + totalMuyMala4 + totalMuyMala5 + totalMuyMala6 + totalMuyMala7 + totalMuyMala8 + totalMuyMala9 + totalMuyMala11; 
			
			//obtenemos cantidad de Mala preguntas 2, 3, 4, 5, 6, 7, 8, 9 y 11
			long totalMala2 = atenEncuDatoBasico.stream().filter(a -> s.getOid().toString().equals(a.getIdServicio()) && "4".equals(a.getRespuesta2())).count();
			long totalMala3 = atenEncuDatoBasico.stream().filter(a -> s.getOid().toString().equals(a.getIdServicio()) && "4".equals(a.getRespuesta3())).count();
			long totalMala4 = atenEncuDatoBasico.stream().filter(a -> s.getOid().toString().equals(a.getIdServicio()) && "4".equals(a.getRespuesta4())).count();
			long totalMala5 = atenEncuDatoBasico.stream().filter(a -> s.getOid().toString().equals(a.getIdServicio()) && "4".equals(a.getRespuesta5())).count();
			long totalMala6 = atenEncuDatoBasico.stream().filter(a -> s.getOid().toString().equals(a.getIdServicio()) && "4".equals(a.getRespuesta6())).count();
			long totalMala7 = atenEncuDatoBasico.stream().filter(a -> s.getOid().toString().equals(a.getIdServicio()) && "4".equals(a.getRespuesta7())).count();
			long totalMala8 = atenEncuDatoBasico.stream().filter(a -> s.getOid().toString().equals(a.getIdServicio()) && "4".equals(a.getRespuesta8())).count();
			long totalMala9 = atenEncuDatoBasico.stream().filter(a -> s.getOid().toString().equals(a.getIdServicio()) && "4".equals(a.getRespuesta9())).count();
			long totalMala11 = atenEncuDatoBasico.stream().filter(a -> s.getOid().toString().equals(a.getIdServicio()) && "4".equals(a.getRespuesta11())).count();
			long totalMala = totalMala2 + totalMala3 + totalMala4 + totalMala5 + totalMala6 + totalMala7 + totalMala8 + totalMala9 + totalMala11;
			
			//obtenemos cantidad de Regular preguntas 2, 3, 4, 5, 6, 7, 8, 9 y 11
			long totalRegular2 = atenEncuDatoBasico.stream().filter(a -> s.getOid().toString().equals(a.getIdServicio()) && "5".equals(a.getRespuesta2())).count();
			long totalRegular3 = atenEncuDatoBasico.stream().filter(a -> s.getOid().toString().equals(a.getIdServicio()) && "5".equals(a.getRespuesta3())).count();
			long totalRegular4 = atenEncuDatoBasico.stream().filter(a -> s.getOid().toString().equals(a.getIdServicio()) && "5".equals(a.getRespuesta4())).count();
			long totalRegular5 = atenEncuDatoBasico.stream().filter(a -> s.getOid().toString().equals(a.getIdServicio()) && "5".equals(a.getRespuesta5())).count();
			long totalRegular6 = atenEncuDatoBasico.stream().filter(a -> s.getOid().toString().equals(a.getIdServicio()) && "5".equals(a.getRespuesta6())).count();
			long totalRegular7 = atenEncuDatoBasico.stream().filter(a -> s.getOid().toString().equals(a.getIdServicio()) && "5".equals(a.getRespuesta7())).count();
			long totalRegular8 = atenEncuDatoBasico.stream().filter(a -> s.getOid().toString().equals(a.getIdServicio()) && "5".equals(a.getRespuesta8())).count();
			long totalRegular9 = atenEncuDatoBasico.stream().filter(a -> s.getOid().toString().equals(a.getIdServicio()) && "5".equals(a.getRespuesta9())).count();
			long totalRegular11 = atenEncuDatoBasico.stream().filter(a -> s.getOid().toString().equals(a.getIdServicio()) && "5".equals(a.getRespuesta11())).count();
			long totalRegular = totalRegular2 + totalRegular3 + totalRegular4 + totalRegular5 + totalRegular6 + totalRegular7 + totalRegular8 + totalRegular9 + totalRegular11;
			
			//obtenemos cantidad de Buena preguntas 2, 3, 4, 5, 6, 7, 8, 9 y 11
			long totalBuena2 = atenEncuDatoBasico.stream().filter(a -> s.getOid().toString().equals(a.getIdServicio()) && "6".equals(a.getRespuesta2())).count();
			long totalBuena3 = atenEncuDatoBasico.stream().filter(a -> s.getOid().toString().equals(a.getIdServicio()) && "6".equals(a.getRespuesta3())).count();
			long totalBuena4 = atenEncuDatoBasico.stream().filter(a -> s.getOid().toString().equals(a.getIdServicio()) && "6".equals(a.getRespuesta4())).count();
			long totalBuena5 = atenEncuDatoBasico.stream().filter(a -> s.getOid().toString().equals(a.getIdServicio()) && "6".equals(a.getRespuesta5())).count();
			long totalBuena6 = atenEncuDatoBasico.stream().filter(a -> s.getOid().toString().equals(a.getIdServicio()) && "6".equals(a.getRespuesta6())).count();
			long totalBuena7 = atenEncuDatoBasico.stream().filter(a -> s.getOid().toString().equals(a.getIdServicio()) && "6".equals(a.getRespuesta7())).count();
			long totalBuena8 = atenEncuDatoBasico.stream().filter(a -> s.getOid().toString().equals(a.getIdServicio()) && "6".equals(a.getRespuesta8())).count();
			long totalBuena9 = atenEncuDatoBasico.stream().filter(a -> s.getOid().toString().equals(a.getIdServicio()) && "6".equals(a.getRespuesta9())).count();
			long totalBuena11 = atenEncuDatoBasico.stream().filter(a -> s.getOid().toString().equals(a.getIdServicio()) && "6".equals(a.getRespuesta11())).count();
			long totalBuena = totalBuena2 + totalBuena3 + totalBuena4 + totalBuena5 + totalBuena6 + totalBuena7 + totalBuena8 + totalBuena9 + totalBuena11;
			
			//obtenemos cantidad de Muy Buena preguntas 2, 3, 4, 5, 6, 7, 8, 9 y 11
			long totalMuyBuena2 = atenEncuDatoBasico.stream().filter(a -> s.getOid().toString().equals(a.getIdServicio()) && "7".equals(a.getRespuesta2())).count();
			long totalMuyBuena3 = atenEncuDatoBasico.stream().filter(a -> s.getOid().toString().equals(a.getIdServicio()) && "7".equals(a.getRespuesta3())).count();
			long totalMuyBuena4 = atenEncuDatoBasico.stream().filter(a -> s.getOid().toString().equals(a.getIdServicio()) && "7".equals(a.getRespuesta4())).count();
			long totalMuyBuena5 = atenEncuDatoBasico.stream().filter(a -> s.getOid().toString().equals(a.getIdServicio()) && "7".equals(a.getRespuesta5())).count();
			long totalMuyBuena6 = atenEncuDatoBasico.stream().filter(a -> s.getOid().toString().equals(a.getIdServicio()) && "7".equals(a.getRespuesta6())).count();
			long totalMuyBuena7 = atenEncuDatoBasico.stream().filter(a -> s.getOid().toString().equals(a.getIdServicio()) && "7".equals(a.getRespuesta7())).count();
			long totalMuyBuena8 = atenEncuDatoBasico.stream().filter(a -> s.getOid().toString().equals(a.getIdServicio()) && "7".equals(a.getRespuesta8())).count();
			long totalMuyBuena9 = atenEncuDatoBasico.stream().filter(a -> s.getOid().toString().equals(a.getIdServicio()) && "7".equals(a.getRespuesta9())).count();
			long totalMuyBuena11 = atenEncuDatoBasico.stream().filter(a -> s.getOid().toString().equals(a.getIdServicio()) && "7".equals(a.getRespuesta11())).count();
			long totalMuyBuena = totalMuyBuena2 + totalMuyBuena3 + totalMuyBuena4 + totalMuyBuena5 + totalMuyBuena6 + totalMuyBuena7 + totalMuyBuena8 + totalMuyBuena9 + totalMuyBuena11;
			
			//obtenemos cantidad de NO APLICA preguntas 2,3,4,5,8,9,10
			long totalNoAplica2 = atenEncuDatoBasico.stream().filter(a -> s.getOid().toString().equals(a.getIdServicio()) && "2".equals(a.getRespuesta2())).count();
			long totalNoAplica3 = atenEncuDatoBasico.stream().filter(a -> s.getOid().toString().equals(a.getIdServicio()) && "2".equals(a.getRespuesta3())).count();
			long totalNoAplica4 = atenEncuDatoBasico.stream().filter(a -> s.getOid().toString().equals(a.getIdServicio()) && "2".equals(a.getRespuesta4())).count();
			long totalNoAplica5 = atenEncuDatoBasico.stream().filter(a -> s.getOid().toString().equals(a.getIdServicio()) && "2".equals(a.getRespuesta5())).count();
			long totalNoAplica8 = atenEncuDatoBasico.stream().filter(a -> s.getOid().toString().equals(a.getIdServicio()) && "2".equals(a.getRespuesta8())).count();
			long totalNoAplica9 = atenEncuDatoBasico.stream().filter(a -> s.getOid().toString().equals(a.getIdServicio()) && "2".equals(a.getRespuesta9())).count();
			long totalNoAplica10 = atenEncuDatoBasico.stream().filter(a -> s.getOid().toString().equals(a.getIdServicio()) && "2".equals(a.getRespuesta10())).count();
			long totalNoAplica = totalNoAplica2 + totalNoAplica3 + totalNoAplica4 + totalNoAplica5 + totalNoAplica8 + totalNoAplica9 + totalNoAplica10;
			
			//obtenemos cantidad de Definitivamente NO preguntas 12			
			long totalDefinitivamenteNo12 = atenEncuDatoBasico.stream().filter(a -> s.getOid().toString().equals(a.getIdServicio()) && "8".equals(a.getRespuesta12())).count();
			long totalDefinitivamenteNo = totalDefinitivamenteNo12;
			
			//obtenemos cantidad de Probablemente NO preguntas 12			
			long totalProbablementeNo12 = atenEncuDatoBasico.stream().filter(a -> s.getOid().toString().equals(a.getIdServicio()) && "9".equals(a.getRespuesta12())).count();
			long totalProbablementeNo = totalProbablementeNo12;
			
			//obtenemos cantidad de Probablemente SI preguntas 12			
			long totalProbablementeSi12 = atenEncuDatoBasico.stream().filter(a -> s.getOid().toString().equals(a.getIdServicio()) && "10".equals(a.getRespuesta12())).count();
			long totalProbablementeSi = totalProbablementeSi12;
			
			//obtenemos cantidad de Definitivamente SI preguntas 12			
			long totalDefinitivamenteSi12 = atenEncuDatoBasico.stream().filter(a -> s.getOid().toString().equals(a.getIdServicio()) && "11".equals(a.getRespuesta12())).count();
			long totalDefinitivamenteSi = totalDefinitivamenteSi12;		
			
			if(numeroEncuestas > 0) {
				atenEncuConsolidadoDTO.add(new AtenEncuConsolidadoDTO(s.getGasNombre(), Long.toString(numeroEncuestas), Long.toString(totalSi), Long.toString(totalNo), Long.toString(totalMuyMala), Long.toString(totalMala), Long.toString(totalRegular), Long.toString(totalBuena), Long.toString(totalMuyBuena), Long.toString(totalNoAplica), Long.toString(totalDefinitivamenteNo), Long.toString(totalProbablementeNo), Long.toString(totalProbablementeSi), Long.toString(totalDefinitivamenteSi)));	
			}					
			
		});			
			
		}
		
		//model.addAttribute("medicos", medicos);
		model.addAttribute("titulo", utf8(this.tituloencuestasatisfaccion));
		model.addAttribute("listprocpat", atenEncuConsolidadoDTO);
		model.addAttribute("enlace9", enlace9);		
		return "consolidadoencuesta";
	}
	
	
	// Este metodo me permite visualizar o cargar el formulario para consultar consolidado de encuestas negativas
	@GetMapping("/negativaencuesta")
	public String crearListaConsolidadoNegativo(Map<String, Object> model) {
		AtenEncuDatoBasico atenEncuDatoBasico = new AtenEncuDatoBasico();
		model.put("titulo", utf8(this.titulonegativa));
		model.put("atenEncuDatoBasico", atenEncuDatoBasico);
		model.put("siau", enlaceprincipalsiau);
		model.put("enlace9", enlace9);
		return "negativaencuesta";
	}
	
	
	// Este metodo me permite generar el consolidad de encuestas negativas	
	@RequestMapping("/buscarnegativosiau")
	public String listarConsolidadoNegativo(Model model, @RequestParam(value = "fechaInicial", required = false) String fechaInicial, @RequestParam(value = "fechaFinal", required = false) String fechaFinal, RedirectAttributes flash) throws ParseException {
		
		// List<AtenEncuDatoBasico> atenEncuDatoBasico = new ArrayList<>();
		List<AtenEncuConsolidadoDTO> atenEncuConsolidadoDTO = new ArrayList<>();
		String errorFechas = "";

		// valida si la fecha inicial y la fecha final no estan vacios
		if (fechaInicial.equals("") && fechaFinal.equals("")) {
			errorFechas = "Debes establecer una fecha inicial y fecha final";
			model.addAttribute("error", errorFechas);
			return "negativaencuesta";
		}

		// valida si la fecha inicial y la fecha final no estan vacios
		if (fechaInicial.equals("") || fechaFinal.equals("")) {
			errorFechas = "Debes establecer una fecha inicial y fecha final";
			model.addAttribute("error", errorFechas);
			return "negativaencuesta";
		}
		
		// consulta por la fecha inicial y la fecha final
		if (!fechaInicial.equals("") && !fechaFinal.equals("")) {
			
			Date fechaI = convertirfecha(fechaInicial);
			Date fechaF = convertirfecha(fechaFinal);	
			
			List<AtenEncuDatoBasico> atenEncuDatoBasico = new ArrayList<>();
			List<AtenEncuDatoBasico> finalDatos = new ArrayList<>();
			
			atenEncuDatoBasico = iAtenEncuDatoBasicoService.findByStartDateBetween(fechaI, fechaF);
			
			List<AtenEncuDatoBasico> tmp11 = atenEncuDatoBasico.stream().filter(c -> c.getRespuesta11().equals("3")).collect(Collectors.toList());
			
			finalDatos.addAll(tmp11);
			
			model.addAttribute("listprocpat", finalDatos);	
		}
		
		model.addAttribute("titulo", utf8(this.tituloencuestasatisfaccion));		
		model.addAttribute("enlace9", enlace9);
		return "negativaencuesta";				
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
		
		
	// Se usa para convertir una cadena String a fecha Date con formato
	private Date convertirfecha(String fecha) throws ParseException {
		Date fechaTranformada = new SimpleDateFormat("dd-MM-yyyy").parse(fecha);
		return fechaTranformada;
	}

}