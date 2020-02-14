package com.chapumix.solution.app.controllers;

import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.chapumix.solution.app.entity.dto.GenMedicoDTO;
import com.chapumix.solution.app.entity.dto.GenPacienDTO;
import com.chapumix.solution.app.entity.dto.GenSeRipsDTO;
import com.chapumix.solution.app.entity.dto.HcnSolPatIntDTO;
import com.chapumix.solution.app.entity.dto.PatProcedimientoDTO;
import com.chapumix.solution.app.entity.dto.HcnSolPatDetaDosDTO;
import com.chapumix.solution.app.entity.dto.HcnSolPatDetaTresDTO;
import com.chapumix.solution.app.entity.dto.HcnSolPatDetaUnoDTO;
import com.chapumix.solution.app.entity.dto.HcnSolPatExtDTO;
import com.chapumix.solution.app.models.entity.PatProcedimiento;
import com.chapumix.solution.app.models.service.IPatProcedimientoService;

@Controller
@SessionAttributes("patProcedimiento")
@PropertySource(value = "application.properties", encoding="UTF-8")
public class HcnSolPatController {
	
	private int folio;
	private int oidpaciente;
	private int procedimiento;
	private Logger logger = LoggerFactory.getLogger(HcnSolPatIntDTO.class);	
	public static final String URLPatologiasInt = "http://localhost:9000/api/patologiasints";
	public static final String URLPatologiasExt = "http://localhost:9000/api/patologiasexts";	
	public static final String URLDescripcion = "http://localhost:9000/api/patologias/detalles/";
	public static final String URLDescripcionExt = "http://localhost:9000/api/patologias/detalles/externos/";
	public static final String URLMedicos = "http://localhost:9000/api/patologias/medicos";	
	public static final String URLMedico = "http://localhost:9000/api/patologias/medicos/";	
	public static final String URLPaciente = "http://localhost:9000/api/patologias/pacientes/";	
	public static final String URLProcedimiento = "http://localhost:9000/api/patologias/procedimientos/";
	private GenMedicoDTO medicoa = new GenMedicoDTO();
	
	
	@Autowired
	private IPatProcedimientoService iPatProcedimientoService;
	
	@Autowired
	private RestTemplate restTemplate;
	
	@Value("${app.tituloprocesarpacientesinternos}")
	private String tituloprocesarpacientesinternos;
	
	@Value("${app.tituloprocesarpacientesexternos}")
	private String tituloprocesarpacientesexternos;
	
	@Value("${app.titulopacientesinternos}")
	private String titulopacientesinternos;	
	
	@Value("${app.tituloprocedimintopatologia}")
	private String tituloprocedimintopatologia;
	
	@Value("${app.tituloeditarprocedimientosprocesados}")
	private String tituloeditarprocedimientosprocesados;
	
	@Value("${app.tituloprocedimientosprocesados}")
	private String tituloprocedimientosprocesados;
	
	
	
	@Value("${app.enlaceprincipal}")
	private String enlaceprincipal;
	
	//////////////*********************************************************//////////////////////////
	//////////////*********************************************************//////////////////////////
	//////////////*********************************************************//////////////////////////
	
	//INDEX PATOLOGIA
	@GetMapping("/indexpatologia")
	public String index(Model model) {
	  model.addAttribute("titulo", utf8(this.tituloprocedimintopatologia));
	  model.addAttribute("principal", enlaceprincipal);
	  return "indexpatologia";
	}
	
	
	//////////////*********************************************************//////////////////////////
	//////////////*********************************************************//////////////////////////
	//////////////*********************************************************//////////////////////////

	//PACIENTES INTERNOS
	// Este metodo me permite listar todas las solicitudes pendientes de patologia de pacientes internos
	@GetMapping("/procedimientopatologiainterno")
	public String listarInterno(Model model) {		
		ResponseEntity<List<HcnSolPatIntDTO>> respuesta = restTemplate.exchange(URLPatologiasInt, HttpMethod.GET, null, new ParameterizedTypeReference<List<HcnSolPatIntDTO>>() {});		
		List<HcnSolPatIntDTO> dinamica = respuesta.getBody();
		List<PatProcedimiento> patProcedimiento = iPatProcedimientoService.findAll();
		
		//esta parte me permite cruzar entre las patologias de dinamica y las patologias procesadas en Solution
		patProcedimiento.forEach(p ->{
			Predicate<HcnSolPatIntDTO> condicion = dina -> dina.getOidPaciente().equals(p.getIdPaciente()) && dina.getOidRips().equals(p.getIdProcedimiento()) && dina.getHcNumFol().equals(p.getFolio());	         
			dinamica.removeIf(condicion);
		});		
		
		model.addAttribute("titulo", utf8(this.titulopacientesinternos));
		model.addAttribute("listprocpat", dinamica);					
		return "procedimientopatologiainterno";
	}
	
	
	// Este metodo me permite visualizar o cargar la solicitud de patologia de pacientes internos para ser procesada
	@RequestMapping(value = "/procesarpatologiainterno")
	public String crearInterno(@RequestParam(value = "oidpaciente", required = false) Integer oidpaciente, @RequestParam(value = "procedimiento", required = false) Integer procedimiento, @RequestParam(value = "folio", required = false) Integer folio, Map<String, Object> model, RedirectAttributes flash) {
		PatProcedimiento patProcedimiento = new PatProcedimiento();

		//obtenemos los valores para luego guardar la solicitud
		this.folio = folio;
		this.oidpaciente = oidpaciente;
		this.procedimiento = procedimiento;
		
		// proceso API para buscar la primera descripcion de la solicitud.
		ResponseEntity<HcnSolPatDetaUnoDTO> respuestaa = restTemplate.exchange(URLDescripcion + oidpaciente + '/' + folio, HttpMethod.GET, null,new ParameterizedTypeReference<HcnSolPatDetaUnoDTO>() {});
		HcnSolPatDetaUnoDTO dinamicaa = respuestaa.getBody();

		if (dinamicaa != null) {
			int edad = calcularEdad(dinamicaa.getGpafecnac());
			String sexo = convertirSexo(dinamicaa.getGpasexpac());

			model.put("nombrecompleto", dinamicaa.getPacPriNom() + " " + dinamicaa.getPacSegNom() + " " + dinamicaa.getPacPriApe() + " " + dinamicaa.getPacSegApe());
			model.put("servicio", dinamicaa.getGasNombre());
			model.put("cama", dinamicaa.getHcaCodigo());
			model.put("fechasolicitud", dinamicaa.getHcsfecsol());
			model.put("historia", dinamicaa.getPacNumDoc());
			model.put("edad", edad);
			model.put("sexo", sexo);
			model.put("aseguradora", dinamicaa.getGdeNombre());
			model.put("ingreso", dinamicaa.getAinConsec());
			model.put("folio", dinamicaa.getHcNumFol());
			model.put("dx", dinamicaa.getDiaCodigo() + "-" + dinamicaa.getDiaNombre());			
			
		}

		// proceso API para buscar la segunda descripcion de la solicitud.
		ResponseEntity<List<HcnSolPatDetaDosDTO>> respuestab = restTemplate.exchange(URLDescripcion + oidpaciente + '/' + procedimiento + '/' + folio, HttpMethod.GET, null,new ParameterizedTypeReference<List<HcnSolPatDetaDosDTO>>() {});
		List<HcnSolPatDetaDosDTO> dinamicab = respuestab.getBody();

		if (dinamicab != null) {
			model.put("detalles", dinamicab);			
		}
		
		
		// proceso API para select de medicos.
		ResponseEntity<List<GenMedicoDTO>> respuestac = restTemplate.exchange(URLMedicos, HttpMethod.GET, null, new ParameterizedTypeReference<List<GenMedicoDTO>>() {});
		List<GenMedicoDTO> medicos = respuestac.getBody();
		
		model.put("medicos", medicos);
		model.put("titulo", utf8(this.tituloprocesarpacientesinternos));		
		model.put("patProcedimiento", patProcedimiento);		
		return "procesarpatologiainterno";

	}
		
		
		//Este metodo me permite guardarla solicitud de patologia de pacientes interno para ser procesada    
		@RequestMapping(value = "/procesarpatologiainterno", method = RequestMethod.POST)
		public String guardarPatologiaInterno(@Valid PatProcedimiento patProcedimiento, BindingResult result, Model model, Principal principal, RedirectAttributes flash, SessionStatus status) {
	    				
			//verificamos si hay errores en los campos requeridos.
			if(result.hasErrors()) {
				// proceso API para buscar la primera descripcion de la solicitud.
				ResponseEntity<HcnSolPatDetaUnoDTO> respuestaa = restTemplate.exchange(URLDescripcion + this.oidpaciente + '/' + this.folio, HttpMethod.GET, null,new ParameterizedTypeReference<HcnSolPatDetaUnoDTO>() {});
				HcnSolPatDetaUnoDTO dinamicaa = respuestaa.getBody();
				
				if (dinamicaa != null) {
				int edad = calcularEdad(dinamicaa.getGpafecnac());
				String sexo = convertirSexo(dinamicaa.getGpasexpac());

				model.addAttribute("nombrecompleto", dinamicaa.getPacPriNom() + " " + dinamicaa.getPacSegNom() + " " + dinamicaa.getPacPriApe() + " " + dinamicaa.getPacSegApe());
				model.addAttribute("servicio", dinamicaa.getGasNombre());
				model.addAttribute("cama", dinamicaa.getHcaCodigo());
				model.addAttribute("fechasolicitud", dinamicaa.getHcsfecsol());
				model.addAttribute("historia", dinamicaa.getPacNumDoc());
				model.addAttribute("edad", edad);
				model.addAttribute("sexo", sexo);
				model.addAttribute("aseguradora", dinamicaa.getGdeNombre());
				model.addAttribute("ingreso", dinamicaa.getAinConsec());
				model.addAttribute("folio", dinamicaa.getHcNumFol());
				model.addAttribute("dx", dinamicaa.getDiaCodigo() + "-" + dinamicaa.getDiaNombre());
				}
				
				// proceso API para buscar la segunda descripcion de la solicitud.
				System.out.println(URLDescripcion + this.oidpaciente + '/' + this.procedimiento + '/' + this.folio);
				ResponseEntity<List<HcnSolPatDetaDosDTO>> respuestab = restTemplate.exchange(URLDescripcion + this.oidpaciente + '/' + this.procedimiento + '/' + this.folio, HttpMethod.GET, null,new ParameterizedTypeReference<List<HcnSolPatDetaDosDTO>>() {});
				List<HcnSolPatDetaDosDTO> dinamicab = respuestab.getBody();
				
				if (dinamicab != null) {		
					model.addAttribute("detalles", dinamicab);			
					
				}
				
				// proceso API para select de medicos.
				ResponseEntity<List<GenMedicoDTO>> respuestac = restTemplate.exchange(URLMedicos, HttpMethod.GET, null, new ParameterizedTypeReference<List<GenMedicoDTO>>() {});
				List<GenMedicoDTO> medicos = respuestac.getBody();
				
				model.addAttribute("medicos", medicos);				
				model.addAttribute("titulo", utf8(this.tituloprocesarpacientesinternos));
				model.addAttribute("patProcedimiento", patProcedimiento);											
				return "procesarpatologiainterno";
			}
			
			String mensajeFlash = (patProcedimiento.getId() != null) ? "La solicitud de patología fue editada correctamente" : "La solicitud de patología fue creada correctamente";
			patProcedimiento.setIdPaciente(this.oidpaciente);
			patProcedimiento.setIdProcedimiento(this.procedimiento);
			patProcedimiento.setFolio(this.folio);
			patProcedimiento.setPacienteInternoExterno("I");
			patProcedimiento.setLoginUsrAlta(principal.getName());
			iPatProcedimientoService.save(patProcedimiento);
			status.setComplete();
			flash.addFlashAttribute("success", mensajeFlash);		
			return "redirect:procedimientopatologiainterno";
		}
		
		
		//////////////*********************************************************//////////////////////////
		//////////////*********************************************************//////////////////////////
		//////////////*********************************************************//////////////////////////
		
		//PACIENTES EXTERNOS
		// Este metodo me permite listar todas las solicitudes de patologia de pacientes externos para ser procesada
		@GetMapping("/procedimientopatologiaexterno")
		public String listarExterno(Model model) {		
			ResponseEntity<List<HcnSolPatExtDTO>> respuesta = restTemplate.exchange(URLPatologiasExt, HttpMethod.GET, null, new ParameterizedTypeReference<List<HcnSolPatExtDTO>>() {});		
			List<HcnSolPatExtDTO> dinamica = respuesta.getBody();
			List<PatProcedimiento> patProcedimiento = iPatProcedimientoService.findAll();
			
			//esta parte me permite cruzar entre las patologias de dinamica y las patologias procesadas en Solution
			patProcedimiento.forEach(p ->{
				Predicate<HcnSolPatExtDTO> condicion = dina -> dina.getOidPaciente().equals(p.getIdPaciente()) && dina.getOidRips().equals(p.getIdProcedimiento());	         
				dinamica.removeIf(condicion);
			});		
			
			model.addAttribute("titulo", utf8(this.tituloprocesarpacientesexternos));
			model.addAttribute("listprocpat", dinamica);			
			return "procedimientopatologiaexterno";
		}
		
		// Este metodo me permite visualizar o cargar la solicitud de patologia de pacientes externos para ser procesada
		@RequestMapping(value = "/procesarpatologiaexterno")
		public String crearExterno(@RequestParam(value = "oidpaciente", required = false) Integer oidpaciente, @RequestParam(value = "procedimiento", required = false) Integer procedimiento, Map<String, Object> model, RedirectAttributes flash) {
			PatProcedimiento patProcedimiento = new PatProcedimiento();

			//obtenemos los valores para luego guardar la solicitud			
			this.oidpaciente = oidpaciente;
			this.procedimiento = procedimiento;
			
			// proceso API para buscar la primera descripcion de la solicitud.
			ResponseEntity<List<HcnSolPatDetaTresDTO>> respuestaa = restTemplate.exchange(URLDescripcion + oidpaciente, HttpMethod.GET, null,new ParameterizedTypeReference<List<HcnSolPatDetaTresDTO>>() {});
			List<HcnSolPatDetaTresDTO> dinamicaa = respuestaa.getBody();
			
			
			if (dinamicaa != null && dinamicaa.size() >= 1) {
				int edad = calcularEdad(dinamicaa.get(0).getGpafecnac());
				String sexo = convertirSexo(dinamicaa.get(0).getGpasexpac());

				model.put("nombrecompleto", dinamicaa.get(0).getPacPriNom() + " " + dinamicaa.get(0).getPacSegNom() + " " + dinamicaa.get(0).getPacPriApe() + " " + dinamicaa.get(0).getPacSegApe());
				model.put("servicio", dinamicaa.get(0).getGenerico());				
				model.put("fechasolicitud", dinamicaa.get(0).getHcsfecsol());
				model.put("historia", dinamicaa.get(0).getPacNumDoc());
				model.put("edad", edad);
				model.put("sexo", sexo);
				model.put("aseguradora", dinamicaa.get(0).getGdeNombre());
				model.put("ingreso", dinamicaa.get(0).getAinConsec());							
				
			}

			// proceso API para buscar la segunda descripcion de la solicitud.
			ResponseEntity<List<HcnSolPatDetaDosDTO>> respuestab = restTemplate.exchange(URLDescripcionExt + oidpaciente + '/' + procedimiento, HttpMethod.GET, null,new ParameterizedTypeReference<List<HcnSolPatDetaDosDTO>>() {});
			List<HcnSolPatDetaDosDTO> dinamicab = respuestab.getBody();

			if (dinamicab != null) {				
				model.put("detalles", dinamicab);
			}
			
			
			// proceso API para select de medicos.
			ResponseEntity<List<GenMedicoDTO>> respuestac = restTemplate.exchange(URLMedicos, HttpMethod.GET, null, new ParameterizedTypeReference<List<GenMedicoDTO>>() {});
			List<GenMedicoDTO> medicos = respuestac.getBody();
			
			model.put("medicos", medicos);
			model.put("titulo", utf8(this.tituloprocesarpacientesexternos));		
			model.put("patProcedimiento", patProcedimiento);		
			return "procesarpatologiaexterno";

		}
		
		// Este metodo me permite guardar el proceso de patologia	    
		@RequestMapping(value = "/procesarpatologiaexterno", method = RequestMethod.POST)
		public String guardarPatologiaExterno(@Valid PatProcedimiento patProcedimiento, BindingResult result, Model model, Principal principal, RedirectAttributes flash, SessionStatus status) {
			    				
			//verificamos si hay errores en los campos requeridos.
			if(result.hasErrors()) {
				// proceso API para buscar la primera descripcion de la solicitud.
				ResponseEntity<List<HcnSolPatDetaTresDTO>> respuestaa = restTemplate.exchange(URLDescripcion + oidpaciente, HttpMethod.GET, null,new ParameterizedTypeReference<List<HcnSolPatDetaTresDTO>>() {});
				List<HcnSolPatDetaTresDTO> dinamicaa = respuestaa.getBody();
							
							
				if (dinamicaa != null && dinamicaa.size() >= 1) {
					int edad = calcularEdad(dinamicaa.get(0).getGpafecnac());
					String sexo = convertirSexo(dinamicaa.get(0).getGpasexpac());
	
					model.addAttribute("nombrecompleto", dinamicaa.get(0).getPacPriNom() + " " + dinamicaa.get(0).getPacSegNom() + " " + dinamicaa.get(0).getPacPriApe() + " " + dinamicaa.get(0).getPacSegApe());
					model.addAttribute("servicio", dinamicaa.get(0).getGenerico());				
					model.addAttribute("fechasolicitud", dinamicaa.get(0).getHcsfecsol());
					model.addAttribute("historia", dinamicaa.get(0).getPacNumDoc());
					model.addAttribute("edad", edad);
					model.addAttribute("sexo", sexo);
					model.addAttribute("aseguradora", dinamicaa.get(0).getGdeNombre());
					model.addAttribute("ingreso", dinamicaa.get(0).getAinConsec());							
								
				}
	
			// proceso API para buscar la segunda descripcion de la solicitud.			
			ResponseEntity<List<HcnSolPatDetaDosDTO>> respuestab = restTemplate.exchange(URLDescripcionExt + oidpaciente + '/' + procedimiento, HttpMethod.GET, null,new ParameterizedTypeReference<List<HcnSolPatDetaDosDTO>>() {});
			List<HcnSolPatDetaDosDTO> dinamicab = respuestab.getBody();
	
			if (dinamicab != null) {				
				model.addAttribute("detalles", dinamicab);
			}
							
							
			// proceso API para select de medicos.
			ResponseEntity<List<GenMedicoDTO>> respuestac = restTemplate.exchange(URLMedicos, HttpMethod.GET, null, new ParameterizedTypeReference<List<GenMedicoDTO>>() {});
			List<GenMedicoDTO> medicos = respuestac.getBody();
							
			model.addAttribute("medicos", medicos);
			model.addAttribute("titulo", utf8(this.tituloprocesarpacientesexternos));		
			model.addAttribute("patProcedimiento", patProcedimiento);		
			return "procesarpatologiaexterno";
			}
						
			String mensajeFlash = (patProcedimiento.getId() != null) ? "La solicitud de patología fue editada correctamente" : "La solicitud de patología fue creada correctamente";
			patProcedimiento.setIdPaciente(this.oidpaciente);
			patProcedimiento.setIdProcedimiento(this.procedimiento);					
			patProcedimiento.setPacienteInternoExterno("E");
			patProcedimiento.setLoginUsrAlta(principal.getName());
			iPatProcedimientoService.save(patProcedimiento);
			status.setComplete();
			flash.addFlashAttribute("success", mensajeFlash);		
			return "redirect:procedimientopatologiaexterno";
		}
		
	
	
	//PROCEDIMIENTOS PROCESADOS
	// Este metodo me permite listar todos los procedimientos procesados
	@GetMapping("/procedimientopatologiageneral")
	public String listarGeneral(Model model) {		
		
		List<PatProcedimiento> patProcedimiento = iPatProcedimientoService.findAll();
		List<PatProcedimientoDTO> newPatProcedimiento = new ArrayList<>();
		
		
		patProcedimiento.forEach(pat ->{			
			// proceso API para consultar el paciente.			
			ResponseEntity<GenPacienDTO> respuestap = restTemplate.exchange(URLPaciente+pat.getIdPaciente(), HttpMethod.GET, null, new ParameterizedTypeReference<GenPacienDTO>() {});
			GenPacienDTO paciente = respuestap.getBody();
			
			// proceso API para consultar el procedimiento.			
			ResponseEntity<GenSeRipsDTO> respuestapr = restTemplate.exchange(URLProcedimiento+pat.getIdProcedimiento(), HttpMethod.GET, null, new ParameterizedTypeReference<GenSeRipsDTO>() {});
			GenSeRipsDTO procedimiento = respuestapr.getBody();
			
			// proceso API para consultar el medico patologo.			
			ResponseEntity<GenMedicoDTO> respuestam = restTemplate.exchange(URLMedico+pat.getIdPatologo(), HttpMethod.GET, null, new ParameterizedTypeReference<GenMedicoDTO>() {});
			GenMedicoDTO medico = respuestam.getBody();
			
			// proceso API para consultar el medico patologo reasignado.
			
			if(pat.getIdPatologoReasigando() != null) {
			ResponseEntity<GenMedicoDTO> respuestama = restTemplate.exchange(URLMedico+pat.getIdPatologoReasigando(), HttpMethod.GET, null, new ParameterizedTypeReference<GenMedicoDTO>() {});
			this.medicoa = respuestama.getBody();
			}else {
				this.medicoa.setGmeCodigo("");
				this.medicoa.setGmeNomCod("");
			}
			
			String anoActual = obtenerAno(pat.getFechaRegistro(), pat.getId());
			String pacienteInternoExterno = pacienteIntExt(pat.getPacienteInternoExterno());
			
			PatProcedimientoDTO dto = new PatProcedimientoDTO(pat.getId(), anoActual, pat.getFechaRegistro(), paciente.getPacNumDoc(), paciente.getPacPriNom().trim()+" "+paciente.getPacSegNom().trim()+" "+paciente.getPacPriApe().trim()+" "+paciente.getPacSegApe().trim(), procedimiento.getSipCodigo()+"-"+procedimiento.getSipNombre(), pat.getFolio(), medico.getGmeCodigo()+" "+medico.getGmeNomCod(), this.medicoa.getGmeCodigo()+" "+this.medicoa.getGmeNomCod(), pat.getCorreccion(), pat.getObservacion(), pacienteInternoExterno);
			newPatProcedimiento.add(dto);
			
		});
		
		// proceso API para select de medicos.
		ResponseEntity<List<GenMedicoDTO>> respuestac = restTemplate.exchange(URLMedicos, HttpMethod.GET, null, new ParameterizedTypeReference<List<GenMedicoDTO>>() {});
		List<GenMedicoDTO> medicos = respuestac.getBody();
				
		model.addAttribute("medicos", medicos);			
		model.addAttribute("titulo", utf8(this.tituloprocedimientosprocesados));
		model.addAttribute("listprocpat", newPatProcedimiento);					
		return "procedimientopatologiageneral";
	}
	
	// Este metodo me permite cargar los datos para editar el procedimiento procesado
		@RequestMapping(value = "/editarpatologiaprocesada")
		public String editarGeneral(@RequestParam(value = "id", required = false) Long id, Map<String, Object> model, RedirectAttributes flash) {		
			
			PatProcedimiento patProcedimiento = null;
						
			if(id > 0) {			
				patProcedimiento = iPatProcedimientoService.findById(id);
				if(patProcedimiento == null) {
					flash.addFlashAttribute("error", "El ID del procedimiento no existe en la base de datos");
					return "redirect:/listapersonajuridica";
				}
			}else {
				flash.addFlashAttribute("error", "El ID del procedimiento no puede ser 0");
				return "redirect:/procedimientopatologiageneral";
			}		
			
			//obtengo el consecutivo
			String codigo = obtenerAno(patProcedimiento.getFechaRegistro(), patProcedimiento.getId());
			
			// proceso API para consultar el paciente.			
			ResponseEntity<GenPacienDTO> respuestap = restTemplate.exchange(URLPaciente+patProcedimiento.getIdPaciente(), HttpMethod.GET, null, new ParameterizedTypeReference<GenPacienDTO>() {});
			GenPacienDTO paciente = respuestap.getBody();
			
			// proceso API para consultar el procedimiento.			
			ResponseEntity<GenSeRipsDTO> respuestapr = restTemplate.exchange(URLProcedimiento+patProcedimiento.getIdProcedimiento(), HttpMethod.GET, null, new ParameterizedTypeReference<GenSeRipsDTO>() {});
			GenSeRipsDTO procedimiento = respuestapr.getBody();
			
			// proceso API para select de medicos.
			ResponseEntity<List<GenMedicoDTO>> respuestac = restTemplate.exchange(URLMedicos, HttpMethod.GET, null, new ParameterizedTypeReference<List<GenMedicoDTO>>() {});
			List<GenMedicoDTO> medicos = respuestac.getBody();
						
			
			model.put("titulo", utf8(this.tituloeditarprocedimientosprocesados));			
			model.put("codigo", codigo);
			model.put("historia", paciente.getPacNumDoc());
			model.put("paciente", paciente.getPacPriNom().trim()+" "+paciente.getPacSegNom().trim()+" "+paciente.getPacPriApe().trim()+" "+paciente.getPacSegApe().trim());
			model.put("procedimiento", procedimiento.getSipCodigo()+"-"+procedimiento.getSipNombre());
			model.put("medicos", medicos);
			model.put("patProcedimiento", patProcedimiento);	
			return "editarpatologiaprocesada";
		}
		
		// Este metodo me permite guardar o editar el procedimiento procesado	    
		@RequestMapping(value = "/editarpatologiaprocesada", method = RequestMethod.POST)
		public String guardarGeneral(@Valid PatProcedimiento patProcedimiento, BindingResult result, Model model, Principal principal, RedirectAttributes flash, SessionStatus status) {
				
			//obtengo el consecutivo
			String codigo = obtenerAno(patProcedimiento.getFechaRegistro(), patProcedimiento.getId());
			
			// proceso API para consultar el paciente.			
			ResponseEntity<GenPacienDTO> respuestap = restTemplate.exchange(URLPaciente+patProcedimiento.getIdPaciente(), HttpMethod.GET, null, new ParameterizedTypeReference<GenPacienDTO>() {});
			GenPacienDTO paciente = respuestap.getBody();
			
			// proceso API para consultar el procedimiento.			
			ResponseEntity<GenSeRipsDTO> respuestapr = restTemplate.exchange(URLProcedimiento+patProcedimiento.getIdProcedimiento(), HttpMethod.GET, null, new ParameterizedTypeReference<GenSeRipsDTO>() {});
			GenSeRipsDTO procedimiento = respuestapr.getBody();
			
			// proceso API para select de medicos.
			ResponseEntity<List<GenMedicoDTO>> respuestac = restTemplate.exchange(URLMedicos, HttpMethod.GET, null, new ParameterizedTypeReference<List<GenMedicoDTO>>() {});
			List<GenMedicoDTO> medicos = respuestac.getBody();
			
			
			//verificamos si hay errores en los campos requeridos.
			if(result.hasErrors()) {
				model.addAttribute("titulo", utf8(this.tituloeditarprocedimientosprocesados));			
				model.addAttribute("codigo", codigo);
				model.addAttribute("historia", paciente.getPacNumDoc());
				model.addAttribute("paciente", paciente.getPacPriNom().trim()+" "+paciente.getPacSegNom().trim()+" "+paciente.getPacPriApe().trim()+" "+paciente.getPacSegApe().trim());
				model.addAttribute("procedimiento", procedimiento.getSipCodigo()+"-"+procedimiento.getSipNombre());
				model.addAttribute("medicos", medicos);	
				return "editarpatologiaprocesada";
			}
								
			String mensajeFlash = (patProcedimiento.getId() != null) ? "El procedimiento procesado fue editado correctamente" : "El procedimiento procesado fue creado correctamente";
			patProcedimiento.setLoginUsrAct(principal.getName());
			patProcedimiento.setFechaAltaAct(new Date());
			iPatProcedimientoService.save(patProcedimiento);
			status.setComplete();
			flash.addFlashAttribute("success", mensajeFlash);		
			return "redirect:procedimientopatologiageneral";
			}
	
	
	
	// Este metodo me permite eliminar el procedimiento procesado
		@RequestMapping(value = "/eliminar/{id}")
		public String eliminar(@PathVariable(value = "id") Long id, RedirectAttributes flash) {
			if(id > 0) {
				iPatProcedimientoService.delete(id);			
				flash.addFlashAttribute("success","La persona fue eliminado correctamente");
			}
			return "redirect:/procedimientopatologiageneral";
		}
		
		
	
				
	


	//////////////*********************************************************//////////////////////////
	//////////////*********************************************************//////////////////////////
	//////////////*********************************************************//////////////////////////
								
	//METODOS ADICIONALES 

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
	
	//Se usa para calcular la edad de una fecha  
	private int calcularEdad(Date fechaNacimiento) {
		
		//convierto la fecha que entra en texto
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		String fechaNacimientoTexto = sdf.format(fechaNacimiento);
		
		//hago el calculo de la fecha de nacimiento
		DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd-MM-yyyy");
		LocalDate fechaNac = LocalDate.parse(fechaNacimientoTexto, fmt);
		LocalDate ahora = LocalDate.now();

		Period periodo = Period.between(fechaNac, ahora);
		//System.out.printf("Tu edad es: %s años, %s meses y %s días", periodo.getYears(), periodo.getMonths(), periodo.getDays());
		return periodo.getYears();		
		
	}
	
	//Se usa para convertir el codigo del sexo a M o F
	private String convertirSexo(Integer gpasexpac) {
		if(gpasexpac == 1)
		return "M";{			
		}
		return "F";
	}
	
	private String convertirTipo(Integer hcsestado) {
		if(hcsestado == 0) {
			return "URGENTE";
		}
		return "RUTINARIO";
	}	
	
	
	//Se usa para obtener el consecutivo formado por el año de la fecha de registro y el id
	private String obtenerAno(Date fechaRegistro, Long id) {
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(fechaRegistro);
		String anoString = Integer.toString(calendar.get(Calendar.YEAR));				
		String anoparte = anoString.substring(2);
		String idLong = Long.toString(id);
		String ano = anoparte+"-"+idLong;
		
		//int mes = calendar.get(Calendar.MONTH) + 1;
		//int dia = calendar.get(Calendar.DAY_OF_MONTH);		
		return ano;
	}
	
	private String pacienteIntExt(String pacienteInternoExterno) {
		if(pacienteInternoExterno.equals("I")) {
			return "INTERNO";
		}
		return "EXTERNO";
	}


}
