package com.chapumix.solution.app.controllers;

import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.text.ParseException;
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
import com.chapumix.solution.app.entity.dto.HcnSolPatDTO;
import com.chapumix.solution.app.entity.dto.PatProcedimientoDTO;
import com.chapumix.solution.app.entity.dto.HcnSolPatDetaDosDTO;
import com.chapumix.solution.app.entity.dto.HcnSolPatDetaUnoDTO;
import com.chapumix.solution.app.models.entity.PatProcedimiento;
import com.chapumix.solution.app.models.service.IPatProcedimientoService;

@Controller
@SessionAttributes("patProcedimiento")
@PropertySource(value = "application.properties", encoding="UTF-8")
public class HcnSolPatController {
	
	private Integer folio;
	private int oidpaciente;
	private int procedimiento;	
	private Date fechasolicitud;	
	private Logger logger = LoggerFactory.getLogger(HcnSolPatDTO.class);	
	public static final String URLPatologias = "http://localhost:9000/api/patologias";	
	public static final String URLDescripcion = "http://localhost:9000/api/patologias/detalles/";
	public static final String URLDescripcionNull = "http://localhost:9000/api/patologias/detalles/null/";
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
	
	@Value("${app.titulopacientes}")
	private String titulopacientes;
	
	@Value("${app.titulopatologia}")
	private String titulopatologia;
	
	@Value("${app.tituloprocedimiento}")
	private String tituloprocedimiento;
	
	
	@Value("${app.tituloeditarprocedimientosprocesados}")
	private String tituloeditarprocedimientosprocesados;
	
	@Value("${app.tituloprocedimientosprocesados}")
	private String tituloprocedimientosprocesados;	
	
	@Value("${app.enlaceprincipalpatologia}")
	private String enlaceprincipalpatologia;	
	
	@Value("${app.enlace1}")
	private String enlace1;
	
	
	
	/* ----------------------------------------------------------
     * INDEX PATOLOGIA
     * ---------------------------------------------------------- */
	
	//INDEX PATOLOGIA
	@GetMapping("/indexpatologia")
	public String indexPatologia(Model model) throws ParseException {	  
	  model.addAttribute("titulo", utf8(this.titulopatologia));	 
	  model.addAttribute("patologia", enlaceprincipalpatologia);
	  model.addAttribute("enlace1", enlace1);
	  return "indexpatologia";
	}
	

	//INDEX PPROCESAR
		@GetMapping("/indexprocesar")
		public String indexProcesar(Model model) throws ParseException {					
			
		  
		  //obtengo todos los procedimientos procesados en  solution	
		  List<PatProcedimiento> patProcedimiento = iPatProcedimientoService.findAll();
		  
		  // obtengo el numero de pacientes internos por procesar	
		  ResponseEntity<List<HcnSolPatDTO>> respuesta = restTemplate.exchange(URLPatologias, HttpMethod.GET, null, new ParameterizedTypeReference<List<HcnSolPatDTO>>() {});		
		  List<HcnSolPatDTO> dinamica = respuesta.getBody();	  
		  
		  
		// esta parte me permite cruzar entre las patologias de pacientes de dinamica y las patologias procesadas en Solution
			patProcedimiento.forEach(p -> {			
				//Clono la lista para hacer el recorrido del listado de dinamica para que me pueda dejar usar el metodo removeIf y no genere expecion
				List<HcnSolPatDTO> cloneList = new ArrayList<HcnSolPatDTO>(dinamica);
				cloneList.forEach(c -> {									
					Predicate<HcnSolPatDTO> condicion = s -> s.getOidPaciente().equals(p.getIdPaciente()) && s.getOidRips().equals(p.getIdProcedimiento()) && s.getHcNumFol() == p.getFolio();
					dinamica.removeIf(condicion);
				});
			});	  
		  
		  
		  model.addAttribute("titulo", utf8(this.tituloprocedimiento));
		  model.addAttribute("procesar", dinamica.size());	  
		  model.addAttribute("procesados", patProcedimiento.size());
		  model.addAttribute("patologia", enlaceprincipalpatologia);
		  model.addAttribute("enlace1", enlace1);
		  return "indexprocesar";
		}


	/* ----------------------------------------------------------
     * PACIENTES PATOLOGIA
     * ---------------------------------------------------------- */

	
	// Este metodo me permite listar todas las solicitudes pendientes de patologia
	@GetMapping("/procedimientopatologia")
	public String listarProcedimientos(Model model) {						
			  
		//obtengo todos los procedimientos procesados en  solution	
		  List<PatProcedimiento> patProcedimiento = iPatProcedimientoService.findAll();		  
		  		
		  // obtengo el numero de pacientes por procesar	
		  ResponseEntity<List<HcnSolPatDTO>> respuesta = restTemplate.exchange(URLPatologias, HttpMethod.GET, null, new ParameterizedTypeReference<List<HcnSolPatDTO>>() {});		
		  List<HcnSolPatDTO> dinamica = respuesta.getBody();		 
				  
		// esta parte me permite cruzar entre las patologias de pacientes de dinamica y las patologias procesadas en Solution
		patProcedimiento.forEach(p -> {			
			//Clono la lista para hacer el recorrido del listado de dinamica para que me pueda dejar usar el metodo removeIf y no genere expecion
			List<HcnSolPatDTO> cloneList = new ArrayList<HcnSolPatDTO>(dinamica);
			cloneList.forEach(c -> {								
				Predicate<HcnSolPatDTO> condicion = s -> s.getOidPaciente().equals(p.getIdPaciente()) && s.getOidRips().equals(p.getIdProcedimiento()) && s.getHcNumFol() == p.getFolio();		
				dinamica.removeIf(condicion);
			});
		});
		
		model.addAttribute("titulo", utf8(this.titulopacientes));
		model.addAttribute("listprocpat", dinamica);
		model.addAttribute("patologia", enlaceprincipalpatologia);
		model.addAttribute("enlace1", enlace1);
		return "procedimientopatologia";
	}
	
	
	// Este metodo me permite visualizar o cargar la solicitud de patologia de los pacientes para ser procesada
	@RequestMapping(value = "/procesarpatologia")
	public String crearInterno(@RequestParam(value = "oidpaciente", required = false) Integer oidpaciente, @RequestParam(value = "procedimiento", required = false) Integer procedimiento, @RequestParam(value = "folio", required = false) Integer folio, @RequestParam(value = "fechasolicitud", required = false) String fechasolicitud, Map<String, Object> model, RedirectAttributes flash) throws ParseException {
		PatProcedimiento patProcedimiento = new PatProcedimiento();

		//obtenemos los valores para luego guardar la solicitud			
		this.oidpaciente = oidpaciente;
		this.procedimiento = procedimiento;
		this.fechasolicitud = convertirFechaParametro(fechasolicitud); 
		if(folio != null) {
			this.folio = folio;
			// proceso API para buscar la primera descripcion de la solicitud.
			ResponseEntity<HcnSolPatDetaUnoDTO> respuestaa = restTemplate.exchange(URLDescripcion + oidpaciente + '/' + folio, HttpMethod.GET, null,new ParameterizedTypeReference<HcnSolPatDetaUnoDTO>() {});
			HcnSolPatDetaUnoDTO dinamicaa = respuestaa.getBody();
			
			if(dinamicaa != null) {
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
				
				descripcionDosPatologia(model, oidpaciente, procedimiento, folio);
				
				
			}else {				
				descripcionUnoPatologia(model, oidpaciente);
				
				descripcionDosPatologia(model, oidpaciente, procedimiento, folio);
				
			}						
		}else {
			this.folio = 0;
			descripcionUnoPatologia(model, oidpaciente);
			
			// proceso API para buscar la segunda descripcion de la solicitud.
			ResponseEntity<List<HcnSolPatDetaDosDTO>> respuestab = restTemplate.exchange(URLDescripcionNull + oidpaciente + '/' + procedimiento, HttpMethod.GET, null,new ParameterizedTypeReference<List<HcnSolPatDetaDosDTO>>() {});
			List<HcnSolPatDetaDosDTO> dinamicab = respuestab.getBody();

			if (dinamicab != null) {
				model.put("detalles", dinamicab);
				model.put("patologia", enlaceprincipalpatologia);
				model.put("enlace1", enlace1);
			}
		}	
		
		
		// proceso API para select de medicos.
		ResponseEntity<List<GenMedicoDTO>> respuestac = restTemplate.exchange(URLMedicos, HttpMethod.GET, null, new ParameterizedTypeReference<List<GenMedicoDTO>>() {});
		List<GenMedicoDTO> medicos = respuestac.getBody();		
		
		model.put("medicos", medicos);
		model.put("titulo", utf8(this.titulopacientes));		
		model.put("patProcedimiento", patProcedimiento);
		model.put("patologia", enlaceprincipalpatologia);
		model.put("enlace1", enlace1);
		return "procesarpatologia";

	}		



		//Este metodo me permite guardarla solicitud de patologia de pacientes interno para ser procesada    
		@RequestMapping(value = "/procesarpatologia", method = RequestMethod.POST)
		public String guardarPatologiaInterno(@Valid PatProcedimiento patProcedimiento, BindingResult result, Model model, Principal principal, RedirectAttributes flash, SessionStatus status) {
			//verificamos si hay errores en los campos requeridos.
			if(result.hasErrors()) {				
											
				Integer f = this.folio;
				//obtenemos los valores para luego guardar la solicitud			
				if(!f.equals(null)) {					
					// proceso API para buscar la primera descripcion de la solicitud.
					ResponseEntity<HcnSolPatDetaUnoDTO> respuestaa = restTemplate.exchange(URLDescripcion + this.oidpaciente + '/' + this.folio, HttpMethod.GET, null,new ParameterizedTypeReference<HcnSolPatDetaUnoDTO>() {});
					HcnSolPatDetaUnoDTO dinamicaa = respuestaa.getBody();					
					
					if(dinamicaa != null) {
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
						
						descripcionDosPatologia(model, this.oidpaciente, this.procedimiento, this.folio);
						
						
					}else {				
						descripcionUnoPatologia(model, this.oidpaciente);
						
						descripcionDosPatologia(model, this.oidpaciente, this.procedimiento, this.folio);
						
					}						
				}else {
					descripcionUnoPatologia(model, this.oidpaciente);
					
					// proceso API para buscar la segunda descripcion de la solicitud.
					ResponseEntity<List<HcnSolPatDetaDosDTO>> respuestab = restTemplate.exchange(URLDescripcionNull + this.oidpaciente + '/' + this.procedimiento, HttpMethod.GET, null,new ParameterizedTypeReference<List<HcnSolPatDetaDosDTO>>() {});
					List<HcnSolPatDetaDosDTO> dinamicab = respuestab.getBody();

					if (dinamicab != null) {
						model.addAttribute("detalles", dinamicab);
						model.addAttribute("patologia", enlaceprincipalpatologia);
						model.addAttribute("enlace1", enlace1);
					}
				}				
				
				// proceso API para select de medicos.
				ResponseEntity<List<GenMedicoDTO>> respuestac = restTemplate.exchange(URLMedicos, HttpMethod.GET, null, new ParameterizedTypeReference<List<GenMedicoDTO>>() {});
				List<GenMedicoDTO> medicos = respuestac.getBody();
				
				
				model.addAttribute("medicos", medicos);				
				model.addAttribute("titulo", utf8(this.titulopacientes));
				model.addAttribute("patProcedimiento", patProcedimiento);
				model.addAttribute("patologia", enlaceprincipalpatologia);
				model.addAttribute("enlace1", enlace1);
				return "procesarpatologia";
			}				
			
			
			String mensajeFlash = (patProcedimiento.getId() != null) ? "La solicitud de patología fue editada correctamente" : "La solicitud de patología fue creada correctamente";
			patProcedimiento.setIdPaciente(this.oidpaciente);
			patProcedimiento.setIdProcedimiento(this.procedimiento);			
			patProcedimiento.setFechaSolicitud(this.fechasolicitud);
			if(this.folio == 0) {
				patProcedimiento.setPacienteInternoExterno("E");
			}else {
				patProcedimiento.setPacienteInternoExterno("I");
				patProcedimiento.setFolio(this.folio);
			}			
			patProcedimiento.setLoginUsrAlta(principal.getName());
			iPatProcedimientoService.save(patProcedimiento);
			this.folio = 0;
			status.setComplete();
			flash.addFlashAttribute("success", mensajeFlash);		
			return "redirect:procedimientopatologia";
		}		



		/* ----------------------------------------------------------
	     * PROCEDIMIENTOS PROCESADOS
	     * ---------------------------------------------------------- */


	// Este metodo me permite listar todos los procedimientos procesados
	@GetMapping("/procedimientopatologiageneral")
	public String listarGeneral(Model model) {		
		
		//List<PatProcedimiento> patProcedimiento = iPatProcedimientoService.findAll();
		List<PatProcedimiento> patProcedimiento = iPatProcedimientoService.findAllProcedimientos();
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
			PatProcedimientoDTO dto = new PatProcedimientoDTO(pat.getId(), anoActual, pat.getFechaRegistro(), paciente.getPacNumDoc(), paciente.getPacPriNom().trim()+" "+paciente.getPacSegNom().trim()+" "+paciente.getPacPriApe().trim()+" "+paciente.getPacSegApe().trim(), procedimiento.getSipCodigo()+"-"+procedimiento.getSipNombre(), pat.getTipoMuestra(), medico.getGmeCodigo()+" "+medico.getGmeNomCod(), this.medicoa.getGmeCodigo()+" "+this.medicoa.getGmeNomCod(), pat.getCorreccion(), pat.getObservacion(), pacienteInternoExterno);
			newPatProcedimiento.add(dto);
			
		});
		
		// proceso API para select de medicos.
		ResponseEntity<List<GenMedicoDTO>> respuestac = restTemplate.exchange(URLMedicos, HttpMethod.GET, null, new ParameterizedTypeReference<List<GenMedicoDTO>>() {});
		List<GenMedicoDTO> medicos = respuestac.getBody();
				
		model.addAttribute("medicos", medicos);			
		model.addAttribute("titulo", utf8(this.tituloprocedimientosprocesados));
		model.addAttribute("listprocpat", newPatProcedimiento);
		model.addAttribute("patologia", enlaceprincipalpatologia);
		model.addAttribute("enlace1", enlace1);
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
					return "redirect:/procedimientopatologiageneral";
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
			model.put("patologia", enlaceprincipalpatologia);
			model.put("enlace1", enlace1);
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
				model.addAttribute("patologia", enlaceprincipalpatologia);
				model.addAttribute("enlace1", enlace1);
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
		@RequestMapping(value = "/eliminarpatologiaprocesada/{id}")
		public String eliminar(@PathVariable(value = "id") Long id, RedirectAttributes flash) {
			if(id > 0) {
				iPatProcedimientoService.delete(id);			
				flash.addFlashAttribute("success","La procedimiento fue eliminado correctamente");
			}
			return "redirect:/procedimientopatologiageneral";
		}
		
		
		
	// Este metodo me permite listar por fechas y por el patologo todos los procedimientos procesados
	@RequestMapping("/buscarpatologiageneral")
	public String listarGeneralFiltro(Model model, @RequestParam(value = "fechaInicial", required = false) String fechaInicial, @RequestParam(value = "fechaFinal", required = false) String fechaFinal, @RequestParam(value = "especialista", required = false) String especialista, RedirectAttributes flash) throws ParseException {		
			
		List<PatProcedimiento> patProcedimiento = new ArrayList<>();
		List<PatProcedimientoDTO> newPatProcedimiento = new ArrayList<>();
		String errorFechas  = "";
			
		//valida si la fecha inicial, la fecha final y el especialista no estan vacios
		if(fechaInicial.equals("") && fechaFinal.equals("") && especialista.equals("")) {				
			errorFechas = "Debes establecer una fecha inicial y fecha final";
			model.addAttribute("error", errorFechas);
		}
		
		//valida si la fecha inicial no esta vacio
		if(fechaInicial.equals("") && !fechaFinal.equals("")) {				
			errorFechas = "Debes establecer una fecha inicial y fecha final";
			model.addAttribute("error", errorFechas);
		}
		
		//valida si la fecha final no esta vacio
		if(!fechaInicial.equals("") && fechaFinal.equals("")) {				
			errorFechas = "Debes establecer una fecha inicial y fecha final";
			model.addAttribute("error", errorFechas);
		}
			
		//consulta por la fecha inicial y la fecha final sin contar con el especialista
		if(!fechaInicial.equals("") && !fechaFinal.equals("") && especialista.equals("")) {
			Date fechaI = convertirfecha(fechaInicial);
			Date fechaF = convertirfecha(fechaFinal);				
			patProcedimiento = iPatProcedimientoService.findByStartDateBetween(fechaI, fechaF);
		}
		
		//consulta por el especialista sin contar con la fecha inicial y la fecha final
		if(fechaInicial.equals("") && fechaFinal.equals("") && !especialista.equals("")) {
			patProcedimiento = iPatProcedimientoService.findByIdPatologo(Integer.parseInt(especialista), Integer.parseInt(especialista));
		}
		
		//consulta la fecha inicial, la fecha final y el especialista. 
		if(!fechaInicial.equals("") && !fechaFinal.equals("") && !especialista.equals("")) {
			Date fechaI = convertirfecha(fechaInicial);
			Date fechaF = convertirfecha(fechaFinal);	
			patProcedimiento = iPatProcedimientoService.findByStartDateBetweenIdPatologo(fechaI, fechaF, Integer.parseInt(especialista), Integer.parseInt(especialista));
		}		
			
			
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
				
			PatProcedimientoDTO dto = new PatProcedimientoDTO(pat.getId(), anoActual, pat.getFechaRegistro(), paciente.getPacNumDoc(), paciente.getPacPriNom().trim()+" "+paciente.getPacSegNom().trim()+" "+paciente.getPacPriApe().trim()+" "+paciente.getPacSegApe().trim(), procedimiento.getSipCodigo()+"-"+procedimiento.getSipNombre(), pat.getTipoMuestra(), medico.getGmeCodigo()+" "+medico.getGmeNomCod(), this.medicoa.getGmeCodigo()+" "+this.medicoa.getGmeNomCod(), pat.getCorreccion(), pat.getObservacion(), pacienteInternoExterno);
			newPatProcedimiento.add(dto);
				
		});
			
		// proceso API para select de medicos.
		ResponseEntity<List<GenMedicoDTO>> respuestac = restTemplate.exchange(URLMedicos, HttpMethod.GET, null, new ParameterizedTypeReference<List<GenMedicoDTO>>() {});
		List<GenMedicoDTO> medicos = respuestac.getBody();
					
		model.addAttribute("medicos", medicos);			
		model.addAttribute("titulo", utf8(this.tituloprocedimientosprocesados));
		model.addAttribute("listprocpat", newPatProcedimiento);		
		//flash.addFlashAttribute("error", errorFechas);
		return "procedimientopatologiageneral";
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
	
	//Se usa para convertir una cadena a INTERNO o EXTERNO
	private String pacienteIntExt(String pacienteInternoExterno) {
		if(pacienteInternoExterno.equals("I")) {
			return "INTERNO";
		}
		return "EXTERNO";
	}

	
	//Se usa para convertir una cadena String a fecha Date con formato
	private Date convertirfecha(String fecha) throws ParseException {
		Date fechaTranformada = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").parse(fecha);  
		return fechaTranformada;
	}
	
	//Se usa para convertir parametro fecha solicitud de String a fecha Date con formato
	private Date convertirFechaParametro(String fecha) throws ParseException {
		Date fechaTranformada = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss aa").parse(fecha);		
		return fechaTranformada;
	}	
	
	
	//Se usa para consultar en la API REST la primera descripcion en caso de que la primera consulta de la descripcion sea null de la solicitud.
	private void descripcionUnoPatologia(Map<String, Object> model, Integer oidpaciente) {
		// proceso API para buscar la primera descripcion en caso de que la primera consulta de la descripcion sea null de la solicitud.
		ResponseEntity<List<HcnSolPatDetaUnoDTO>> respuestanull = restTemplate.exchange(URLDescripcionNull + oidpaciente, HttpMethod.GET, null,new ParameterizedTypeReference<List<HcnSolPatDetaUnoDTO>>() {});
		List<HcnSolPatDetaUnoDTO> dinamicanull = respuestanull.getBody();
		
		int edad = calcularEdad(dinamicanull.get(0).getGpafecnac());
		String sexo = convertirSexo(dinamicanull.get(0).getGpasexpac());

		model.put("nombrecompleto", dinamicanull.get(0).getPacPriNom() + " " + dinamicanull.get(0).getPacSegNom() + " " + dinamicanull.get(0).getPacPriApe() + " " + dinamicanull.get(0).getPacSegApe());
		model.put("servicio", "");
		model.put("cama", "");
		model.put("fechasolicitud", this.fechasolicitud);
		model.put("historia", dinamicanull.get(0).getPacNumDoc());
		model.put("edad", edad);
		model.put("sexo", sexo);
		model.put("aseguradora", dinamicanull.get(0).getGdeNombre());
		model.put("ingreso", dinamicanull.get(0).getAinConsec());
	}
	
	//Se usa para consultar en la API REST la primera descripcion en caso de que la primera consulta de la descripcion sea null de la solicitud.
	private void descripcionUnoPatologia(Model model, int oidpaciente2) {
		// proceso API para buscar la primera descripcion en caso de que la primera consulta de la descripcion sea null de la solicitud.
		ResponseEntity<List<HcnSolPatDetaUnoDTO>> respuestanull = restTemplate.exchange(URLDescripcionNull + oidpaciente, HttpMethod.GET, null,new ParameterizedTypeReference<List<HcnSolPatDetaUnoDTO>>() {});
		List<HcnSolPatDetaUnoDTO> dinamicanull = respuestanull.getBody();

		
		int edad = calcularEdad(dinamicanull.get(0).getGpafecnac());
		String sexo = convertirSexo(dinamicanull.get(0).getGpasexpac());

		model.addAttribute("nombrecompleto", dinamicanull.get(0).getPacPriNom() + " " + dinamicanull.get(0).getPacSegNom() + " " + dinamicanull.get(0).getPacPriApe() + " " + dinamicanull.get(0).getPacSegApe());
		model.addAttribute("servicio", "");
		model.addAttribute("cama", "");
		model.addAttribute("fechasolicitud", this.fechasolicitud);
		model.addAttribute("historia", dinamicanull.get(0).getPacNumDoc());
		model.addAttribute("edad", edad);
		model.addAttribute("sexo", sexo);
		model.addAttribute("aseguradora", dinamicanull.get(0).getGdeNombre());
		model.addAttribute("ingreso", dinamicanull.get(0).getAinConsec());
		
	}
	
	
	//Se usa para consultar en la API REST la segunda descripcion en caso de que el folio sea null de la solicitud.
	private void descripcionDosPatologia(Map<String, Object> model, Integer oidpaciente, Integer procedimiento, Integer folio) {
		// proceso API para buscar la segunda descripcion de la solicitud.
		ResponseEntity<List<HcnSolPatDetaDosDTO>> respuestab = restTemplate.exchange(URLDescripcion + oidpaciente + '/' + procedimiento + '/' + folio, HttpMethod.GET, null,new ParameterizedTypeReference<List<HcnSolPatDetaDosDTO>>() {});
		List<HcnSolPatDetaDosDTO> dinamicab = respuestab.getBody();

		if (dinamicab != null) {
			model.put("detalles", dinamicab);
			model.put("patologia", enlaceprincipalpatologia);
			model.put("enlace1", enlace1);
		}
		
	}	

	//Se usa para consultar en la API REST la segunda descripcion en caso de que el folio sea null de la solicitud.
	private void descripcionDosPatologia(Model model, int oidpaciente2, int procedimiento2, int folio2) {
		// proceso API para buscar la segunda descripcion de la solicitud.
		ResponseEntity<List<HcnSolPatDetaDosDTO>> respuestab = restTemplate.exchange(URLDescripcion + oidpaciente + '/' + procedimiento + '/' + folio, HttpMethod.GET, null,new ParameterizedTypeReference<List<HcnSolPatDetaDosDTO>>() {});
		List<HcnSolPatDetaDosDTO> dinamicab = respuestab.getBody();

		if (dinamicab != null) {
				model.addAttribute("detalles", dinamicab);
				model.addAttribute("patologia", enlaceprincipalpatologia);
				model.addAttribute("enlace1", enlace1);
			}		
	}

}
