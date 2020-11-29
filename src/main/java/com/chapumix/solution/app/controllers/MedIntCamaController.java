package com.chapumix.solution.app.controllers;

import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
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

import com.chapumix.solution.app.entity.dto.ComCamaDTO;
import com.chapumix.solution.app.entity.dto.HcnOrdHospCustomDTO;
import com.chapumix.solution.app.entity.dto.HcnOrdHospDTO;
import com.chapumix.solution.app.models.entity.CalCalendario;
import com.chapumix.solution.app.models.entity.ComCama;
import com.chapumix.solution.app.models.entity.ComEstadoTramite;
import com.chapumix.solution.app.models.entity.ComUsuario;
import com.chapumix.solution.app.models.entity.GenPacien;
import com.chapumix.solution.app.models.entity.MedIntCama;
import com.chapumix.solution.app.models.service.IComCamaService;
import com.chapumix.solution.app.models.service.IComCausaRechazoService;
import com.chapumix.solution.app.models.service.IComEspecialidadService;
import com.chapumix.solution.app.models.service.IComEstadoTramiteService;
import com.chapumix.solution.app.models.service.IComUsuarioService;
import com.chapumix.solution.app.models.service.IGenAreSerService;
import com.chapumix.solution.app.models.service.IGenPacienService;
import com.chapumix.solution.app.models.service.IMedIntCamaService;
import com.chapumix.solution.app.utils.PacienteDinamica;


@Controller
@PropertySource(value = "application.properties", encoding="UTF-8")
@SessionAttributes("medIntCama")
public class MedIntCamaController {
	
	public static final String URLSolicitudCama = "http://localhost:9000/api/camasmedico";
	
	public static final String URLCamaDisponible = "http://localhost:9000/api/camasdisponibles";
	
	@Autowired
	private IMedIntCamaService iMedIntCamaService;
	
	@Autowired
	private IGenAreSerService iGenAreSerService;
	
	@Autowired
	private IGenPacienService iGenPacienService;
	
	@Autowired
	private IComEspecialidadService iComEspecialidadService;
	
	@Autowired
	private IComEstadoTramiteService iComEstadoTramiteService;
	
	@Autowired
	private IComCamaService iComCamaService;
	
	@Autowired
	private IComCausaRechazoService iComCausaRechazoService;
	
	@Autowired
	private IComUsuarioService iComUsuarioService;
	
	@Autowired
	private RestTemplate restTemplate;
	
	@Autowired
	private PacienteDinamica pacienteDinamica;
	
	@Value("${app.titulomedicinainterna}")
	private String titulomedicinainterna;
	
	@Value("${app.enlaceprincipalmedicinainterna}")
	private String enlaceprincipalmedicinainterna;
	
	@Value("${app.tituloasignacioncamas}")
	private String tituloasignacioncamas;
	
	@Value("${app.titulotramitadascamas}")
	private String titulotramitadascamas;
	
	
	@Value("${app.titulotramitarcamas}")
	private String titulotramitarcamas;
	
	@Value("${app.titulorechazarcamas}")
	private String titulorechazarcamas;
	
	
	
	@Value("${app.enlace6}")
	private String enlace6;
	
	
	/* ----------------------------------------------------------
     * INDEX MEDICINA INTERNA
     * ---------------------------------------------------------- */
	
	//INDEX MEDICINA INTERNA
	@GetMapping("/indexmedicinainterna")
	public String index(Model model) {
		model.addAttribute("titulo", utf8(this.titulomedicinainterna));
		model.addAttribute("medicinainterna", enlaceprincipalmedicinainterna);
		model.addAttribute("enlace6", enlace6);
		return "indexmedicinainterna";
	}
	
	
	//INDEX ASIGNACION DE CAMAS
	@GetMapping("/indexasignacioncamas")
	public String indexCamas(Model model) {
		model.addAttribute("titulo", utf8(this.tituloasignacioncamas));
		model.addAttribute("medicinainterna", enlaceprincipalmedicinainterna);
		model.addAttribute("enlace6", enlace6);
		return "indexasignacioncamas";
	}
	
	/* ----------------------------------------------------------
     * SOLICITUDES CAMA POR MEDICO
     * ---------------------------------------------------------- */

	
	// Este metodo me permite listar todas las solicitudes de camas pendientes de asignar
	@GetMapping("/camasolicitud")	
	public String listarSolicitudCama(Model model, @RequestParam(value = "servicio", required = false) String servicio) {

		// obtengo el numero de solicitudes por procesar desde dinamica
		ResponseEntity<List<HcnOrdHospDTO>> respuesta = restTemplate.exchange(URLSolicitudCama, HttpMethod.GET, null, new ParameterizedTypeReference<List<HcnOrdHospDTO>>() {});
		List<HcnOrdHospDTO> dinamica = respuesta.getBody();		
		
		
		if(servicio == null || servicio.equals("TODOS") ) {
			List<HcnOrdHospCustomDTO> finalListado = listaModificada(dinamica);
			model.addAttribute("listsolic", finalListado);
			model.addAttribute("cantidad", finalListado.size());
		}else {
			List<HcnOrdHospCustomDTO> customSolicitud = listaModificada(dinamica);
			List<HcnOrdHospCustomDTO> porServicio = customSolicitud.stream().filter(s -> s.getGasNombre().equals(servicio.trim())).collect(Collectors.toList());			
			model.addAttribute("nombreservicio", servicio);
			model.addAttribute("listsolic", porServicio);				
		}		
		
		model.addAttribute("servicio", iGenAreSerService.findByOrderNombreIntenacion());
		model.addAttribute("titulo", utf8(this.tituloasignacioncamas));		
		model.addAttribute("medicinainterna", enlaceprincipalmedicinainterna);
		model.addAttribute("enlace6", enlace6);
		return "camasolicitud";
	}
	

	// Este metodo me permite cargar los datos para editar las solicitudes de camas pendientes y guardar
	@RequestMapping(value = "/camatramitarform")
	public String crearSolicitudCama(@RequestParam(value = "ingreso", required = false) String ingreso, @RequestParam(value = "hc", required = false) String hc, @RequestParam(value = "fecha", required = false) String fecha, 
						@RequestParam(value = "aislamiento", required = false) String aislamiento, Map<String, Object> model, RedirectAttributes flash, Principal principal) throws Exception {
			
		//sincronizo paciente de dinamica a solution
		GenPacien genPacien = iGenPacienService.findByNumberDoc(hc);
		GenPacien obtengoPaciente =  pacienteDinamica.SincronizarPaciente(genPacien, hc);
		
		// obtengo las camas disponibles desde dinamica
		ResponseEntity<List<ComCamaDTO>> respuesta = restTemplate.exchange(URLCamaDisponible, HttpMethod.GET, null, new ParameterizedTypeReference<List<ComCamaDTO>>() {});
		List<ComCamaDTO> camasDisponibles = respuesta.getBody();		
		
		//creo una lista de tipo ComCama apartir de camas disponibles desde dinamica
		List<ComCama> comCama = obtenerListaCamas(camasDisponibles);
		
		//obtengo a partir de un numero el tipo de aislamiento
		String tipoAislamiento = tipoAislamiento(aislamiento);
		
		MedIntCama medIntCama = new MedIntCama();		
		medIntCama.setIngreso(ingreso);
		medIntCama.setTipoAislamiento(tipoAislamiento);
		model.put("titulo", utf8(this.titulotramitarcamas));
		model.put("medIntCama", medIntCama);
		model.put("ingreso", ingreso);
		model.put("hc", hc);
		model.put("fecha", fecha);
		model.put("paciente", obtengoPaciente.getPacPriNom().trim()+' '+obtengoPaciente.getPacSegNom().trim()+' '+obtengoPaciente.getPacPriApe().trim()+' '+obtengoPaciente.getPacSegApe().trim());
		model.put("camasDisponibles", comCama);
		model.put("especialidad", iComEspecialidadService.findAllAsc());
		model.put("medicinainterna", enlaceprincipalmedicinainterna);
		model.put("enlace6", enlace6);
		return "camatramitarform";
	}	


	// Este metodo me permite guardar las solicitudes de camas aceptadas
	@RequestMapping(value = "/camatramitarform", method = RequestMethod.POST)
	public String guardarSolicitudCama(@Valid MedIntCama medIntCama, BindingResult result, Model model, @RequestParam(name = "hc") String hc,  
									  @RequestParam(name = "fecha") String fecha, RedirectAttributes flash, SessionStatus status, Principal principal) throws Exception {
		
		if(medIntCama.getComEspecialidad() == null || medIntCama.getComCama() == null || medIntCama.getExtensionTelefono() == null) {
			// obtengo las camas disponibles
			ResponseEntity<List<ComCamaDTO>> respuesta = restTemplate.exchange(URLCamaDisponible, HttpMethod.GET, null, new ParameterizedTypeReference<List<ComCamaDTO>>() {});
			List<ComCamaDTO> camasDisponibles = respuesta.getBody();
						
			//creo una lista de tipo ComCama apartir de camas disponibles desde dinamica
			List<ComCama> comCama = obtenerListaCamas(camasDisponibles);
						
			//obtengo paciente de solution
			GenPacien genPacien = iGenPacienService.findByNumberDoc(hc);		
			
			model.addAttribute("titulo", utf8(this.titulotramitarcamas));
			model.addAttribute("medIntCama", medIntCama);			
			model.addAttribute("hc", hc);
			model.addAttribute("fecha", fecha);
			model.addAttribute("paciente", genPacien.getPacPriNom().trim()+' '+genPacien.getPacSegNom().trim()+' '+genPacien.getPacPriApe().trim()+' '+genPacien.getPacSegApe().trim());
			model.addAttribute("camasDisponibles", comCama);
			model.addAttribute("especialidad", iComEspecialidadService.findAllAsc());
			model.addAttribute("medicinainterna", enlaceprincipalmedicinainterna);
			model.addAttribute("enlace6", enlace6);
			model.addAttribute("error", "Hay campos requeridos sin diligenciar");			
			return "camatramitarform";
		}
		
		//obtengo paciente de solution
		GenPacien genPacien = iGenPacienService.findByNumberDoc(hc);
		
		//obtengo el estado de tramite desde solution
		ComEstadoTramite comEstadoTramite = iComEstadoTramiteService.findById(1L);
		
		//obtengo el usuario logueado desde solution
		ComUsuario comUsuario = iComUsuarioService.findByUsuario(principal.getName());
				
		String mensajeFlash = (medIntCama.getId() != null) ? "La solicitud fue editada correctamente" : "La solicitud fue procesada correctamente";			
		medIntCama.setGenPacien(genPacien);
		medIntCama.setFechaSolicitud(convertirFecha(fecha));
		medIntCama.setFechaAsignacion(new Date());
		medIntCama.setComEstadoTramite(comEstadoTramite);
		medIntCama.setLoginUsrTramita(comUsuario);		
		iMedIntCamaService.save(medIntCama);
		status.setComplete();
		flash.addFlashAttribute("success", mensajeFlash);
		return "redirect:camasolicitud";		
	}
	
	// Este metodo me permite cargar los datos para editar las solicitudes de camas rechazadas y guardar
	@RequestMapping(value = "/camarechazarform")
	public String rechazarSolicitudCama(@RequestParam(value = "ingreso", required = false) String ingreso, @RequestParam(value = "hc", required = false) String hc, @RequestParam(value = "fecha", required = false) String fecha, 
						@RequestParam(value = "aislamiento", required = false) String aislamiento, Map<String, Object> model, RedirectAttributes flash, Principal principal) throws Exception {
				
		//sincronizo paciente de dinamica a solution
		GenPacien genPacien = iGenPacienService.findByNumberDoc(hc);
		GenPacien obtengoPaciente =  pacienteDinamica.SincronizarPaciente(genPacien, hc);	
			
		//obtengo a partir de un numero el tipo de aislamiento
		String tipoAislamiento = tipoAislamiento(aislamiento);
			
		MedIntCama medIntCama = new MedIntCama();		
		medIntCama.setIngreso(ingreso);
		medIntCama.setTipoAislamiento(tipoAislamiento);
		model.put("titulo", utf8(this.titulorechazarcamas));
		model.put("medIntCama", medIntCama);
		model.put("ingreso", ingreso);
		model.put("hc", hc);
		model.put("fecha", fecha);
		model.put("paciente", obtengoPaciente.getPacPriNom().trim()+' '+obtengoPaciente.getPacSegNom().trim()+' '+obtengoPaciente.getPacPriApe().trim()+' '+obtengoPaciente.getPacSegApe().trim());
		model.put("rechazo", iComCausaRechazoService.findAllAsc());		
		model.put("medicinainterna", enlaceprincipalmedicinainterna);
		model.put("enlace6", enlace6);
		return "camarechazarform";
	}
	
	// Este metodo me permite guardar las solicitudes de camas rechazadas
	@RequestMapping(value = "/camarechazarform", method = RequestMethod.POST)
	public String guardarRechazarCama(@Valid MedIntCama medIntCama, BindingResult result, Model model, @RequestParam(name = "hc") String hc,  
									  @RequestParam(name = "fecha") String fecha, RedirectAttributes flash, SessionStatus status, Principal principal) throws Exception {
				
		
		if(medIntCama.getComCausaRechazo() == null || medIntCama.getExtensionTelefono() == null) {
			//obtengo paciente de solution
			GenPacien genPacien = iGenPacienService.findByNumberDoc(hc);			
				
			model.addAttribute("titulo", utf8(this.titulorechazarcamas));
			model.addAttribute("medIntCama", medIntCama);			
			model.addAttribute("hc", hc);
			model.addAttribute("fecha", fecha);
			model.addAttribute("paciente", genPacien.getPacPriNom().trim()+' '+genPacien.getPacSegNom().trim()+' '+genPacien.getPacPriApe().trim()+' '+genPacien.getPacSegApe().trim());			
			model.addAttribute("rechazo", iComCausaRechazoService.findAllAsc());		
			model.addAttribute("medicinainterna", enlaceprincipalmedicinainterna);
			model.addAttribute("enlace6", enlace6);
			model.addAttribute("error", "Hay campos requeridos sin diligenciar");
			return "camarechazarform";
		}
			
		//obtengo paciente de solution
		GenPacien genPacien = iGenPacienService.findByNumberDoc(hc);
			
		//obtengo el estado de tramite desde solution
		ComEstadoTramite comEstadoTramite = iComEstadoTramiteService.findById(2L);
		
		//obtengo el usuario logueado desde solution
		ComUsuario comUsuario = iComUsuarioService.findByUsuario(principal.getName());
					
		String mensajeFlash = (medIntCama.getId() != null) ? "La solicitud fue editada correctamente" : "La solicitud fue procesada correctamente";			
		medIntCama.setGenPacien(genPacien);
		medIntCama.setFechaSolicitud(convertirFecha(fecha));
		medIntCama.setFechaRechazo(new Date());
		medIntCama.setComEstadoTramite(comEstadoTramite);
		medIntCama.setLoginUsrTramita(comUsuario);		
		iMedIntCamaService.save(medIntCama);
		status.setComplete();
		flash.addFlashAttribute("success", mensajeFlash);
		return "redirect:camasolicitud";		
	}
	
	// Este metodo me permite visualizar o cargar el formulario para consultar el tramite de la cama solicitada
	@GetMapping("/camatramitada")
	public String tramiteCamaConsultar(Map<String, Object> model, RedirectAttributes flash) {		
			
		MedIntCama medIntCama =  new MedIntCama();		
		boolean listado = false;
			
		model.put("titulo", utf8(this.titulotramitadascamas));
		model.put("medIntCama", medIntCama);		
		model.put("enlace6", enlace6);		
		model.put("medicinainterna", enlaceprincipalmedicinainterna);
		model.put("listado", listado);		
		return "camatramitada";
	}
	
	// Este metodo me permite consultar el tramite de la cama solicitada o rechazada
	@RequestMapping("/camarealizada")	
	public String tramiteCamaRealizado(Model model, @RequestParam(value = "keyword", required = false) String keyword, RedirectAttributes flash) {
			
		boolean listado = false;
				
		// valida el ID de la prescripcion que no este vacia
		if (keyword.isEmpty()) {		
			model.addAttribute("titulo", utf8(this.titulotramitadascamas));			
			model.addAttribute("enlace6", enlace6);
			model.addAttribute("medicinainterna", enlaceprincipalmedicinainterna);
			model.addAttribute("error", "El documento del paciente o el ingreso es requerido");
			return "camatramitada";
		}		
		
		List<MedIntCama> listadoTramitados = iMedIntCamaService.findByDocumentoIngreso(keyword);
		if(!listadoTramitados.isEmpty()) {
			listado = true;
			model.addAttribute("titulo", utf8(this.titulotramitadascamas));			
			model.addAttribute("enlace6", enlace6);
			model.addAttribute("medicinainterna", enlaceprincipalmedicinainterna);
			model.addAttribute("listadoTramitados", listadoTramitados);
			model.addAttribute("listado", listado);
			return  "camatramitada";
		}else {
			model.addAttribute("error", "El documento del paciente o el ingreso no existen en la base de datos");
			return "camatramitada";
		}					
	}
	
	// Este metodo me permite cargar la solicitud de la cama despues de que ha sido asignada para rechazar
	@RequestMapping(value = "/camarechazarformpost")
	public String editarSolicitud(@RequestParam(value = "id", required = false) Long id, Map<String, Object> model, RedirectAttributes flash) throws ParseException {
		MedIntCama medIntCama = null;
		if(id > 0) {			
			medIntCama = iMedIntCamaService.findById(id);
			if(medIntCama == null) {
				flash.addFlashAttribute("error", "El ID de la solicitud no existe en la base de datos");
				return "redirect:/camatramitada";
			}
		}else {
			flash.addFlashAttribute("error", "El ID de la solicitud no puede ser 0");
			return "redirect:/camatramitada";
		}
		//Date fechaCumpleanos = convertirfechaEmpleado(calCalendario.getFechaNacimiento().toString()); 
		//calCalendario.setFechaNacimiento(fechaCumpleanos);
		model.put("titulo", utf8(this.titulorechazarcamas));		
		model.put("enlace6", enlace6);
		model.put("medicinainterna", enlaceprincipalmedicinainterna);
		model.put("paciente", medIntCama.getGenPacien().getPacPriNom().trim()+' '+medIntCama.getGenPacien().getPacSegNom().trim()+' '+medIntCama.getGenPacien().getPacPriApe().trim()+' '+medIntCama.getGenPacien().getPacSegApe().trim());
		model.put("rechazo", iComCausaRechazoService.findAllAsc());
		model.put("medIntCama", medIntCama);
		return "camarechazarformpost";
	}
	
	
	// Este metodo me permite guardar las solicitudes de camas ya asignadas y cambiarlas a rechazadas
	@RequestMapping(value = "/camarechazarformpost", method = RequestMethod.POST)
	public String guardarRechazarCamaPost(@Valid MedIntCama medIntCama, BindingResult result, Model model, RedirectAttributes flash, SessionStatus status, Principal principal) throws Exception {
					
			
		if(medIntCama.getComCausaRechazo() == null || medIntCama.getExtensionTelefono() == null) {
			model.addAttribute("titulo", utf8(this.titulorechazarcamas));
			model.addAttribute("medIntCama", medIntCama);			
			model.addAttribute("paciente", medIntCama.getGenPacien().getPacPriNom().trim()+' '+medIntCama.getGenPacien().getPacSegNom().trim()+' '+medIntCama.getGenPacien().getPacPriApe().trim()+' '+medIntCama.getGenPacien().getPacSegApe().trim());			
			model.addAttribute("rechazo", iComCausaRechazoService.findAllAsc());		
			model.addAttribute("medicinainterna", enlaceprincipalmedicinainterna);
			model.addAttribute("enlace6", enlace6);
			model.addAttribute("error", "Hay campos requeridos sin diligenciar");
			return "camarechazarformpost";
		}
		
		//obtengo el estado de tramite desde solution
		ComEstadoTramite comEstadoTramite = iComEstadoTramiteService.findById(3L);
				
		//obtengo el usuario logueado desde solution
		ComUsuario comUsuario = iComUsuarioService.findByUsuario(principal.getName());
					
		String mensajeFlash = (medIntCama.getId() != null) ? "La solicitud fue editada correctamente" : "La solicitud fue procesada correctamente";			
		medIntCama.setComEspecialidad(null);
		medIntCama.setComCama(null);
		medIntCama.setFechaAnulacionAsignacion(new Date());
		medIntCama.setComEstadoTramite(comEstadoTramite);
		medIntCama.setLoginUsrTramita(comUsuario);		
		iMedIntCamaService.save(medIntCama);
		status.setComplete();
		flash.addFlashAttribute("success", mensajeFlash);
		return "redirect:camatramitada";		
	}
	
	
	
	/* ----------------------------------------------------------
     * METODOS ADICIONALES 
     * ---------------------------------------------------------- */


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
	
	private List<HcnOrdHospCustomDTO> listaModificada(List<HcnOrdHospDTO> dinamica) {
		List<HcnOrdHospCustomDTO> customSolicitud = new ArrayList<HcnOrdHospCustomDTO>();
		dinamica.forEach(d ->{
			int edad = calcularEdad(d.getGpafecnac());
			HcnOrdHospCustomDTO custom = new HcnOrdHospCustomDTO(d.getAinConsec(), d.getPacNumDoc(), d.getPacPriNom(), d.getPacSegNom(), d.getPacPriApe(), d.getPacSegApe(), d.getGpasexpac(), edad, d.getGdeNombre(), d.getHcaCodigo(), d.getHcaNombre(), d.getHcoTipAisl(), d.getHcoTipHosp(), d.getDiaCodigo(), d.getDiaNombre(), d.getHcofecdoc(), d.getCcCodigo(), d.getCcNombre(), d.getHcoObserv(), d.getHcoMotivo(), d.getGmeNomCom(), d.getGasNombre().trim());
			customSolicitud.add(custom);
		});
		List<HcnOrdHospCustomDTO> filtrarTextosCortos = customSolicitud.stream().filter(s -> (s.getHcoObserv().length() > 6) || (s.getHcoMotivo().length() > 6)).collect(Collectors.toList());						
		List<HcnOrdHospCustomDTO> finalListado = finalLista(filtrarTextosCortos);
		return finalListado;
	}
	
	// Se usa para calcular la edad de una fecha
	private int calcularEdad(Date fechaNacimiento) {

		// convierto la fecha que entra en texto
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		String fechaNacimientoTexto = sdf.format(fechaNacimiento);

		// hago el calculo de la fecha de nacimiento
		DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd-MM-yyyy");
		LocalDate fechaNac = LocalDate.parse(fechaNacimientoTexto, fmt);
		LocalDate ahora = LocalDate.now();

		Period periodo = Period.between(fechaNac, ahora);
		// System.out.printf("Tu edad es: %s años, %s meses y %s días",
		// periodo.getYears(), periodo.getMonths(), periodo.getDays());
		return periodo.getYears();

	}
	
	//Se usa para crear la lista de camas disponibles desde dinamica
	private List<ComCama> obtenerListaCamas(List<ComCamaDTO> camasDisponibles) {
		List<ComCama> listaCamas = new ArrayList<>();
		camasDisponibles.forEach(c ->{
			//obtengo la cama de solution
			ComCama comCama = iComCamaService.findByCodigo(c.getCcCodigo());
			listaCamas.add(comCama);
		});
		return listaCamas;
	}
	
	//Se usa para convertir parametro fecha solicitud de String a fecha Date con formato
	private Date convertirFecha(String fecha) throws ParseException {		
		Date fechaTranformada = new SimpleDateFormat("dd-MM-yyy hh:mm a").parse(fecha);		
		return fechaTranformada;
	}
	
	//Se usa para obtener el tipo de aislamiento a partir de un codigo
	private String tipoAislamiento(String aislamiento) {		
		
		String tipoAislamiento = "";
		
		switch (aislamiento) {
		case "0":
			tipoAislamiento = "PRECAUCION_ESTANDAR";
			break;

		case "1":
			tipoAislamiento = "PRECAUCION_VIAS_AEREAS";
			break;
			
		case "2":
			tipoAislamiento = "PRECAUCION_POR_GOTAS";
			break;
			
		case "3":
			tipoAislamiento = "PRECAUCION_POR_CONTACTO";
			break;
			
		case "4":
			tipoAislamiento = "AISLAMIENTO_EN_COHORTE";
			break;
			
		case "5":
			tipoAislamiento = "AMBIENTE_PROTECTOR";
			break;		
		}
		
		return tipoAislamiento;
	}
	
	//Se usa para que las solicitudes despues de procesadas se comparen en dinamica y solution para que desaparezcan   
	private List<HcnOrdHospCustomDTO> finalLista(List<HcnOrdHospCustomDTO> filtrarTextosCortos) {		
		
		List<HcnOrdHospCustomDTO> original = filtrarTextosCortos;
		//clono la lista para poder filtrar y no generar excepcion
		List<HcnOrdHospCustomDTO> listaClonada = new ArrayList<HcnOrdHospCustomDTO>(original);		
		
		
		original.forEach(t ->{
			
			MedIntCama medIntCama =  iMedIntCamaService.findByIngreso(t.getAinConsec().toString());
			if(medIntCama != null) {
				Predicate<HcnOrdHospCustomDTO> condicion = s -> s.getAinConsec().toString().equals(medIntCama.getIngreso());				
				listaClonada.removeIf(condicion);
				
			}			
		});
		
		return listaClonada;
	}


}