package com.chapumix.solution.app.controllers;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.security.Principal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.validation.Valid;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

import com.chapumix.solution.app.entity.dto.GenPacienDTO;
import com.chapumix.solution.app.models.entity.ComGenero;
import com.chapumix.solution.app.models.entity.ComTipoDocumento;
import com.chapumix.solution.app.models.entity.ComTipoDocumentoMipres;
import com.chapumix.solution.app.models.entity.ComTipoTecnologia;
import com.chapumix.solution.app.models.entity.ComTokenMipres;
import com.chapumix.solution.app.models.entity.FarMipres;
import com.chapumix.solution.app.models.entity.GenPacien;
import com.chapumix.solution.app.models.service.IComGeneroService;
import com.chapumix.solution.app.models.service.IComTipoDocumentoMipresService;
import com.chapumix.solution.app.models.service.IComTipoDocumentoService;
import com.chapumix.solution.app.models.service.IComTipoTecnologiaService;
import com.chapumix.solution.app.models.service.IComTokenMipresService;
import com.chapumix.solution.app.models.service.IFarMipresService;
import com.chapumix.solution.app.models.service.IGenPacienService;
import com.chapumix.solution.app.utils.PageRender;


@Controller
@SessionAttributes({"farMipres","comTokenMipres"})
@PropertySource(value = "application.properties", encoding="UTF-8")
public class FarMipresController {
	
	public static final String MetodoPutEntrega = "https://wsmipres.sispro.gov.co/WSSUMMIPRESNOPBS/api/EntregaAmbito/"; //url mipres metodo para put entrega
	
	public static final String MetodoPutReporteEntrega = "https://wsmipres.sispro.gov.co/WSSUMMIPRESNOPBS/api/ReporteEntrega/"; //url mipres metodo para put reporte entrega
	
	public static final String MetodoPutReporteFacturacion = "https://wsmipres.sispro.gov.co/WSFACMIPRESNOPBS/api/Facturacion/"; //url mipres metodo para put reporte facturacion
	
	public static final String MetodoGetConsulta = "https://wsmipres.sispro.gov.co/wsmipresnopbs/api/Prescripcion/"; //url mipres metodo para get consulta
	
	public static final String MetodoGetPrescripcion = "https://wsmipres.sispro.gov.co/WSSUMMIPRESNOPBS/api/EntregaXPrescripcion/"; //url mipres metodo para get consulta	
	
	public static final String URLPaciente = "http://localhost:9000/api/pacientegeneral"; //se obtuvo de API REST de GenPacienRestController
	
	@Autowired
	private IComTokenMipresService iComTokenMipresService;
	
	@Autowired
	private IComTipoTecnologiaService iComTipoTecnologiaService;
	
	@Autowired
	private IComTipoDocumentoMipresService iComTipoDocumentoMipresService;
	
	@Autowired
	private IFarMipresService iFarMipresService;
	
	@Autowired
	private IGenPacienService iGenPacienService;
	
	@Autowired
	private IComGeneroService iComGeneroService;
	
	@Autowired
	private IComTipoDocumentoService iComTipoDocumentoService;
	
	@Autowired
	private RestTemplate restTemplate;
	
	@Value("${app.titulomipres}")
	private String titulomipres;
	
	@Value("${app.titulotokenprimario}")
	private String titulotokenprimario;
	
	@Value("${app.titulotokensecundario}")
	private String titulotokensecundario;
	
	@Value("${app.tituloentrega}")
	private String tituloentrega;
	
	@Value("${app.tituloreporteentrega}")
	private String tituloreporteentrega;
	
	@Value("${app.tituloreportefacturacion}")
	private String tituloreportefacturacion;	
	
	@Value("${app.tituloentregarealizada}")
	private String tituloentregarealizada;
	
	@Value("${app.tituloentregaprocesada}")
	private String tituloentregaprocesada;
	
	
	@Value("${app.enlaceprincipalfarmacia}")
	private String enlaceprincipalfarmacia;
	
	@Value("${app.titulosincroniza}")
	private String titulosincroniza;
	
	
	@Value("${app.enlace10}")
	private String enlace10;
	
	
	/* ----------------------------------------------------------
     * INDEX FARMACIA MIPRES
     * ---------------------------------------------------------- */
	
	//INDEX FARMACIA MIPRES
	@GetMapping("/indexmipres")
	public String index(Model model) {
		model.addAttribute("titulo", utf8(this.titulomipres));
		model.addAttribute("farmacia", enlaceprincipalfarmacia);
		model.addAttribute("enlace10", enlace10);
		return "indexmipres";
	}
	
	// Este metodo me permite guardar el token primario
	@RequestMapping(value = "/tokenformprimario", method = RequestMethod.POST)
	public String guardarTokenPrimario(@Valid ComTokenMipres comTokenMipres, BindingResult result, Model model, RedirectAttributes flash, SessionStatus status) throws Exception {
		if(result.hasErrors()) {
			model.addAttribute("titulo", utf8(this.titulomipres));							
			model.addAttribute("comTokenMipres", comTokenMipres);
			model.addAttribute("farmacia", enlaceprincipalfarmacia);
			model.addAttribute("enlace10", enlace10);
			return "tokenformprimario";
		}
				
		String mensajeFlash = (comTokenMipres.getId() != null) ? "El token fue editado correctamente" : "El token fue creado correctamente";			
		
		comTokenMipres.setFechaAlta(new Date());
		iComTokenMipresService.save(comTokenMipres);
		status.setComplete();
		flash.addFlashAttribute("success", mensajeFlash);		
		return "redirect:indexmipres";
	}	
	
	// Este metodo me permite cargar los datos para editar el token primario
	@RequestMapping(value = "/tokenformprimario")
	public String editarToken(@RequestParam(value = "id", required = false) Long id, Map<String, Object> model, RedirectAttributes flash) throws ParseException {
		ComTokenMipres comTokenMipres = null;
		if(id > 0) {			
			comTokenMipres = iComTokenMipresService.findById(id);
			if(comTokenMipres == null) {
				flash.addFlashAttribute("error", "El ID del token no existe en la base de datos");
				return "redirect:/indexmipres";
			}
		}else {
			flash.addFlashAttribute("error", "El ID del token no puede ser 0");
			return "redirect:/indexmipres";
		}
		
		model.put("titulo", utf8(this.titulotokenprimario));
		model.put("comTokenMipres", comTokenMipres);
		model.put("farmacia", enlaceprincipalfarmacia);
		model.put("enlace10", enlace10);
		return "tokenformprimario";
	}
	
	
	// Este metodo me permite guardar el token secundario
	@RequestMapping(value = "/tokenformsecundario", method = RequestMethod.POST)
	public String guardarTokenSecundario(@Valid ComTokenMipres comTokenMipres, BindingResult result, Model model, RedirectAttributes flash, SessionStatus status) throws Exception {
		if(result.hasErrors()) {
			model.addAttribute("titulo", utf8(this.titulomipres));							
			model.addAttribute("comTokenMipres", comTokenMipres);
			model.addAttribute("farmacia", enlaceprincipalfarmacia);
			model.addAttribute("enlace10", enlace10);
			return "tokenformsecundario";
		}
			
		String mensajeFlash = (comTokenMipres.getId() != null) ? "El token fue editado correctamente" : "El token fue creado correctamente";
		
		//obtengo el token secundario para guardar en solution
		String tokenSecundario = obtenerTokenSecundario(comTokenMipres.getUrl(), comTokenMipres.getNit(), comTokenMipres.getTokenPrincipal());
		//establezco el valor del token secundario
		comTokenMipres.setTokenSecundario(tokenSecundario);
		comTokenMipres.setFechaAlta(new Date());
		iComTokenMipresService.save(comTokenMipres);
		status.setComplete();
		flash.addFlashAttribute("success", mensajeFlash);		
		return "redirect:indexmipres";
	}	
	
	

	// Este metodo me permite cargar los datos para editar el token secundario
	@RequestMapping(value = "/tokenformsecundario")
	public String editarTokenSecundario(@RequestParam(value = "id", required = false) Long id, Map<String, Object> model, RedirectAttributes flash) throws ParseException {
		ComTokenMipres comTokenMipres = null;
		if(id > 0) {			
			comTokenMipres = iComTokenMipresService.findById(id);
			if(comTokenMipres == null) {
				flash.addFlashAttribute("error", "El ID del token no existe en la base de datos");
				return "redirect:/indexmipres";
			}
		}else {
			flash.addFlashAttribute("error", "El ID del token no puede ser 0");
			return "redirect:/indexmipres";
		}
		model.put("titulo", utf8(this.titulotokensecundario));
		model.put("comTokenMipres", comTokenMipres);
		model.put("farmacia", enlaceprincipalfarmacia);
		model.put("enlace10", enlace10);
		return "tokenformsecundario";
	}
	
	// Este metodo me permite listar todas las entregas pendientes en el web service del ministerio
	@GetMapping("/pendientesmipres")
	public String listarPendientes(Model model, @RequestParam(name = "page", defaultValue = "0") int page, @RequestParam(name = "buscando", required = false) String buscando) {
			
		if(buscando == null || buscando == "") {
			Pageable pageRequest = PageRequest.of(page, 150);			
			Page<FarMipres> farMipres = iFarMipresService.findAllCustomPendientes(pageRequest);
			PageRender<FarMipres> pageRender = new PageRender<>("/pendientesmipres", farMipres);
			model.addAttribute("listentrega", farMipres);
			model.addAttribute("page", pageRender);
		}else {
			Pageable pageRequest = PageRequest.of(page, 150);			
			Page<FarMipres> farMipres = iFarMipresService.findAllCustomSearchPendientes(pageRequest, buscando);
			PageRender<FarMipres> pageRender = new PageRender<>("/pendientesmipres", farMipres);
			model.addAttribute("listentrega", farMipres);
			model.addAttribute("page", pageRender);
		}		
								
		model.addAttribute("titulo", utf8(this.tituloentregarealizada));			
		model.addAttribute("farmacia", enlaceprincipalfarmacia);
		model.addAttribute("enlace10", enlace10);		
		return "pendientesmipres";
	}
	
	
	// Este metodo me permite listar todas las entregas procesadas en el web service del ministerio
	@GetMapping("/procesadosmipres")
	public String listarProcesados(Model model, @RequestParam(name = "page", defaultValue = "0") int page, @RequestParam(name = "buscando", required = false) String buscando) {
			
		if(buscando == null || buscando == "") {
			Pageable pageRequest = PageRequest.of(page, 150);			
			Page<FarMipres> farMipres = iFarMipresService.findAllCustomProcesados(pageRequest);
			PageRender<FarMipres> pageRender = new PageRender<>("/procesadosmipres", farMipres);
			model.addAttribute("listentrega", farMipres);
			model.addAttribute("page", pageRender);
		}else {
			Pageable pageRequest = PageRequest.of(page, 150);			
			Page<FarMipres> farMipres = iFarMipresService.findAllCustomSearchProcesados(pageRequest, buscando);
			PageRender<FarMipres> pageRender = new PageRender<>("/procesadosmipres", farMipres);
			model.addAttribute("listentrega", farMipres);
			model.addAttribute("page", pageRender);
		}		
									
		model.addAttribute("titulo", utf8(this.tituloentregaprocesada));			
		model.addAttribute("farmacia", enlaceprincipalfarmacia);
		model.addAttribute("enlace10", enlace10);		
		return "procesadosmipres";
	}
	
	// Este metodo me permite cargar el consolidad de la prescripcion
	@RequestMapping(value = "/consolidadomipresform")
	public String cargarConsolidado(@RequestParam(value = "id", required = false) Long id, Map<String, Object> model, RedirectAttributes flash, Principal principal) throws ParseException {
			
		FarMipres farMipres = null;
		if(id > 0) {			
			farMipres = iFarMipresService.findById(id);
			if(farMipres == null) {
				flash.addFlashAttribute("error", "El ID del consolidado no existe en la base de datos");
				return "redirect:/procesadosmipres";
			}
		}else {
			flash.addFlashAttribute("error", "El ID del consolidado no puede ser 0");
				return "redirect:/procesadosmipres";
		}
					
		model.put("titulo", utf8(this.tituloentrega));
		model.put("farMipres", farMipres);
		model.put("tecnologia", iComTipoTecnologiaService.findAll());
		model.put("tipodocumento", iComTipoDocumentoMipresService.findAll());
		model.put("farmacia", enlaceprincipalfarmacia);
		model.put("enlace10", enlace10);
		return "consolidadomipresform";
	}	
	
	
	// Este metodo me permite cargar los datos para editar la entrega y guardar
	@RequestMapping(value = "/entregaform")
	public String crear(@RequestParam(value = "id", required = false) Long id, Map<String, Object> model, RedirectAttributes flash, Principal principal) throws ParseException {
		
		FarMipres farMipres = null;
		if(id > 0) {			
			farMipres = iFarMipresService.findById(id);
			if(farMipres == null) {
				flash.addFlashAttribute("error", "El ID del reporte no existe en la base de datos");
				return "redirect:/pendientesmipres";
			}
		}else {
			flash.addFlashAttribute("error", "El ID del reporte no puede ser 0");
			return "redirect:/pendientesmipres";
		}
				
		model.put("titulo", utf8(this.tituloentrega));
		model.put("farMipres", farMipres);
		model.put("tecnologia", iComTipoTecnologiaService.findAll());
		model.put("tipodocumento", iComTipoDocumentoMipresService.findAll());
		model.put("farmacia", enlaceprincipalfarmacia);
		model.put("enlace10", enlace10);
		return "entregaform";
	}	
	
	// Este metodo me permite guardar la entrega
	@RequestMapping(value = "/entregaform", method = RequestMethod.POST)
	public String guardarEntrega(@Valid FarMipres farMipres, BindingResult result, Model model, RedirectAttributes flash, SessionStatus status, Principal principal) throws Exception {
		if(result.hasErrors()) {
			model.addAttribute("titulo", utf8(this.tituloentrega));
			model.addAttribute("farMipres", farMipres);
			model.addAttribute("tecnologia", iComTipoTecnologiaService.findAll());
			model.addAttribute("tipodocumento", iComTipoDocumentoMipresService.findAll());
			model.addAttribute("farmacia", enlaceprincipalfarmacia);
			model.addAttribute("enlace10", enlace10);
			return "entregaform";
		}		
		
		Map<String, String> webServiceInfo =  guardarWebServiceMipresEntrega(farMipres);
		
		
		if(StringUtils.equals(webServiceInfo.get("success"), "200")) {
			
			//sincronizo paciente de dinamica a solution
			GenPacien genPacien = iGenPacienService.findByNumberDoc(farMipres.getGenPacien().getPacNumDoc());
			GenPacien obtengoPaciente =  SicronizarPaciente(genPacien, farMipres.getGenPacien().getPacNumDoc());	
			
			String mensajeFlash = (farMipres.getId() != null) ? "La entrega fue editada correctamente" : "La entrega fue creada correctamente "+"IdEntrega: "+webServiceInfo.get("IdEntrega");
			farMipres.setIdTraza(webServiceInfo.get("Id"));
			farMipres.setIdEntregaMipress(webServiceInfo.get("IdEntrega"));
			farMipres.setProcesadoEntrega(true);
			farMipres.setLoginUsrAlta(principal.getName());
			if(genPacien == null) {
				farMipres.setGenPacien(obtengoPaciente);
			}else {
				farMipres.setGenPacien(genPacien);
			}			
			iFarMipresService.save(farMipres);
			status.setComplete();
			flash.addFlashAttribute("success", mensajeFlash);
			return "redirect:pendientesmipres";
		}else {
			flash.addFlashAttribute("error", webServiceInfo.get("error"));
			return "redirect:pendientesmipres";
		}		
		
	}
	
	
	// Este metodo me permite cargar los datos para editar el reporte de entrega y guardar
	@RequestMapping(value = "/reporteentregaform")
	public String crearReporteEntrega(@RequestParam(value = "id", required = false) Long id, Map<String, Object> model, RedirectAttributes flash, Principal principal) throws ParseException {
			
		FarMipres farMipres = null;
		if(id > 0) {			
			farMipres = iFarMipresService.findById(id);
			if(farMipres == null) {
				flash.addFlashAttribute("error", "El ID del reporte no existe en la base de datos");
				return "redirect:/pendientesmipres";
			}
		}else {
			flash.addFlashAttribute("error", "El ID del reporte no puede ser 0");
			return "redirect:/pendientesmipres";
		}				
			
		model.put("titulo", utf8(this.tituloreporteentrega));
		model.put("farMipres", farMipres);
		model.put("farmacia", enlaceprincipalfarmacia);
		model.put("enlace10", enlace10);
		return "reporteentregaform";
	}
	
	
	// Este metodo me permite guardar el reporte de entrega
	@RequestMapping(value = "/reporteentregaform", method = RequestMethod.POST)
	public String guardarReporteEntrega(@Valid FarMipres farMipres, @RequestParam(value = "estadoentrega", required = false) Integer estadoentrega, @RequestParam(value = "valor", required = false) String valor, BindingResult result, Model model, RedirectAttributes flash, SessionStatus status, Principal principal) throws Exception {
		if(result.hasErrors()) {
			model.addAttribute("titulo", utf8(this.tituloreporteentrega));
			model.addAttribute("farMipres", farMipres);			
			model.addAttribute("farmacia", enlaceprincipalfarmacia);
			model.addAttribute("enlace10", enlace10);
			return "reporteentregaform";
		}
		
		if(estadoentrega == null) {			
			model.addAttribute("error", "El estado de entrega es requerido");
			return "reporteentregaform";
		}
		
		if(valor.isEmpty()) {			
			model.addAttribute("error", "El valor es requerido");
			return "reporteentregaform";
		}
		
		if(farMipres.getNumeroFactura().isEmpty()) {			
			model.addAttribute("error", "La factura es requerida");
			return "reporteentregaform";
		}
		
		
		Map<String, String> webServiceInfo =  guardarWebServiceMipresReporteEntrega(farMipres, estadoentrega, valor);
		
		if(StringUtils.equals(webServiceInfo.get("success"), "200")) {
					
					String mensajeFlash = (farMipres.getId() != null) ? "El reporte de entrega fue creado correctamente "+"IdReporteEntrega: "+webServiceInfo.get("IdReporteEntrega") : "El reporte de entrega fue creado correctamente "+"IdReporteEntrega: "+webServiceInfo.get("IdReporteEntrega");
					farMipres.setIdReporteEntregaMipress(webServiceInfo.get("IdReporteEntrega"));			
					farMipres.setProcesadoReporteEntrega(true);
					farMipres.setLoginUsrAct(principal.getName());
					farMipres.setFechaAltaAct(new Date());
					iFarMipresService.save(farMipres);
					status.setComplete();
					flash.addFlashAttribute("success", mensajeFlash);
					return "redirect:pendientesmipres";
				}else {
					flash.addFlashAttribute("error", webServiceInfo.get("error"));
					return "redirect:pendientesmipres";
				}	
	}
	
	// Este metodo me permite cargar los datos para editar el reporte de facturacion
	@RequestMapping(value = "/reportefacturacion")
	public String crearReporteFacturacion(@RequestParam(value = "id", required = false) Long id, Map<String, Object> model, RedirectAttributes flash, Principal principal) throws ParseException {
				
		FarMipres farMipres = null;
		if(id > 0) {			
			farMipres = iFarMipresService.findById(id);
			if(farMipres == null) {
				flash.addFlashAttribute("error", "El ID del empleado no existe en la base de datos");
				return "redirect:/pendientesmipres";
			}
		}else {
			flash.addFlashAttribute("error", "El ID del empleado no puede ser 0");
			return "redirect:/pendientesmipres";
		}				
				
		model.put("titulo", utf8(this.tituloreportefacturacion));
		model.put("farMipres", farMipres);
		model.put("farmacia", enlaceprincipalfarmacia);
		model.put("enlace10", enlace10);
		return "reportefacturacion";
	}
	
	// Este metodo me permite guardar el reporte de facturacion
	@RequestMapping(value = "/reportefacturacion", method = RequestMethod.POST)
	public String guardarReporteFacturacion(@Valid FarMipres farMipres, @RequestParam(value = "nit", required = false) String nit, @RequestParam(value = "cdispensacion", required = false) String cdispensacion, 
											@RequestParam(value = "vunitario", required = false) String vunitario, @RequestParam(value = "vtotal", required = false) String vtotal, 
											@RequestParam(value = "cuota", required = false) String cuota, @RequestParam(value = "copago", required = false) String copago, BindingResult result, Model model, RedirectAttributes flash, SessionStatus status, Principal principal) throws Exception {
		if(result.hasErrors()) {
			model.addAttribute("titulo", utf8(this.tituloreporteentrega));
			model.addAttribute("farMipres", farMipres);			
			model.addAttribute("farmacia", enlaceprincipalfarmacia);
			model.addAttribute("enlace10", enlace10);
			return "reporteentregaform";
		}
			
		if(nit.isEmpty()) {			
			model.addAttribute("error", "El nit es requerido");
			return "reportefacturacion";
		}		
		if(cdispensacion.isEmpty()) {			
			model.addAttribute("error", "La cantidad en unidades minimas dispensación es requerido");
			return "reportefacturacion";
		}
		if(vunitario.isEmpty()) {			
			model.addAttribute("error", "El valor unitario es requerido");
			return "reportefacturacion";
		}
		if(vtotal.isEmpty()) {			
			model.addAttribute("error", "El valor total es requerido");
			return "reportefacturacion";
		}
		if(cuota.isEmpty()) {			
			model.addAttribute("error", "La cuota moderadora es requerida");
			return "reportefacturacion";
		}
		if(copago.isEmpty()) {			
			model.addAttribute("error", "El copago es requerido");
			return "reportefacturacion";
		}
			
		Map<String, String> webServiceInfo =  guardarWebServiceMipresReporteFacturacion(farMipres, nit, cdispensacion, vunitario, vtotal, cuota, copago);
			
		if(StringUtils.equals(webServiceInfo.get("success"), "200")) {
						
					String mensajeFlash = (farMipres.getId() != null) ? "El reporte de facturación fue creado correctamente "+"IdFacturacion: "+webServiceInfo.get("IdFacturacion") : "El reporte de facturación fue creado correctamente "+"IdFacturacion: "+webServiceInfo.get("IdFacturacion");
					farMipres.setIdReporteFacturacionMipress(webServiceInfo.get("IdFacturacion"));
					farMipres.setIdFacturacionMipress(webServiceInfo.get("Id"));
					farMipres.setProcesadoFacturacion(true);
					farMipres.setLoginUsrAct(principal.getName());
					farMipres.setFechaAltaAct(new Date());
					iFarMipresService.save(farMipres);
					status.setComplete();
					flash.addFlashAttribute("success", mensajeFlash);
					return "redirect:pendientesmipres";
				}else {
					flash.addFlashAttribute("error", webServiceInfo.get("error"));
					return "redirect:pendientesmipres";
				}	
	}
	

	// Este metodo me permite visualizar o cargar el formulario para sincronizar las prescripciones por fecha
	@GetMapping("/sincronizaprescripcionform")
	public String crearSincronizacion(Map<String, Object> model, RedirectAttributes flash) {		
		FarMipres farMipres =  new FarMipres();
		model.put("titulo", utf8(this.titulosincroniza));
		model.put("farMipres", farMipres);		
		model.put("enlace10", enlace10);		
		return "sincronizaprescripcionform";
	}
	
	
	// Este metodo me permite guardar e sincronizar las prescripciones por fecha que no estan en solution obtenidas del web service mipres
	@RequestMapping(value = "/sincronizaprescripcionform")
	public String guardarSincronizacion(@RequestParam(value = "fechaInicial", required = false) String fechaInicial, @RequestParam(value = "fechaFinal", required = false) String fechaFinal, Map<String, Object> model, RedirectAttributes flash, SessionStatus status, Principal principal) throws ParseException, IOException {
		sincronizoPrescripcion(fechaInicial, fechaFinal, principal);		
		model.put("titulo", utf8(this.titulosincroniza));				
		model.put("enlace10", enlace10);		
		flash.addFlashAttribute("success", "Sincronizacion correcta");		
		status.setComplete();
		return "redirect:sincronizaprescripcionform";
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
	
	//Se usa para obtener token secundario
	private String obtenerTokenSecundario(String url, String nit, String tokenPrincipal) throws IOException, Exception {		
		
		String urlEncadenada = url+nit+'/'+tokenPrincipal;
	    URI uri = new URI(urlEncadenada);
	 
	    ResponseEntity<String> resultado = restTemplate.getForEntity(uri, String.class);
	    
	    if(resultado.getStatusCodeValue() == 200) {
	    	//me permite eliminar las comillas al inicio y al final de la cadena
	    	String tokenSecundario = resultado.getBody().toString().replaceAll("^\\\"+|\\\"+$", "");
	    	return tokenSecundario;
	    }else {
	    	return "La solicitud GET no funcionó";	    	
	    }
	}
	
	//Se usa para hacer put en el web service de mipres ENTREGA
	private Map<String, String> guardarWebServiceMipresEntrega(FarMipres farMipres) throws Exception {
		
		Map<String, String> map = new HashMap<>();
		
		//obtengo los datos del token secundario guardados en solution
		ComTokenMipres comTokenMipres = iComTokenMipresService.findById(1L); 
		
		//genero la url para consultar
		String urlEncadenada = MetodoPutEntrega+comTokenMipres.getNit()+'/'+comTokenMipres.getTokenSecundario();
				
		//convierto la fecha de entrega en string y formato YYYY-MM-DD
		String fechaEntrega = formatoFecha(farMipres.getFechaEntrega());		
		
	    //Especificamos la URL y configuro el objeto CloseableHttpClient
		CloseableHttpClient httpclient = HttpClients.createDefault();
		
		//Se crea una solicitud PUT (si es post HttpPost y si es get HttpGet) y pasamos el URL del recurso y también asigne encabezados a este objeto de colocación
		HttpPut httpPut = new HttpPut(urlEncadenada);
        httpPut.setHeader("Accept", "application/json");
        httpPut.setHeader("Content-type", "application/json");
        
        //creo el json que sera pasado al objeto StringEntity
        JSONObject parametros = new JSONObject();
        parametros.put("NoPrescripcion", farMipres.getNumeroPrescripcion());
        parametros.put("TipoTec", farMipres.getComTipoTecnologia().getTipo());
        parametros.put("ConTec", farMipres.getConsecutivoTecnologia());
        parametros.put("TipoIDPaciente", farMipres.getComTipoDocumentoMipres().getTipo());
        parametros.put("NoIDPaciente", farMipres.getGenPacien().getPacNumDoc());
        parametros.put("NoEntrega", farMipres.getNumeroEntrega());
        parametros.put("CodSerTecEntregado", farMipres.getCodigoServicio());
        parametros.put("CantTotEntregada", farMipres.getCantidadEntregada());
        parametros.put("EntTotal", farMipres.getEntregaTotal());
        parametros.put("CausaNoEntrega", farMipres.getCausaNoEntrega());
        parametros.put("FecEntrega", fechaEntrega);
        parametros.put("NoLote", farMipres.getLoteEntregado());        
		
        //Proporciono la solicitud json en el objeto StringEntity y asígnela al objeto puesto.
        StringEntity stringEntity = new StringEntity(parametros.toString());
        stringEntity.setContentEncoding("UTF-8");        
        httpPut.setEntity(stringEntity);     
 
        //Envio la solicitud usando HttpPut -> Método de ejecución PUT
        HttpResponse response = httpclient.execute(httpPut);    
        
        
        //creo un objeto HttpEntity para obtener el resultado en String de la peticion
        HttpEntity entity = response.getEntity();
        String content = EntityUtils.toString(entity);
        
        //System.out.println(content);        
        
        //convierto la respuesta de la peticion String a Json para mensajes personalizados
        JSONObject jsonContent = new JSONObject();
        if(response.getStatusLine().getStatusCode() == 200) {
        	jsonContent = new JSONObject(content.replaceAll("\\[", "").replaceAll("\\]", ""));//busca "[" y "]" y los reemplaza por espacios en blanco  
        }else {
        	jsonContent = new JSONObject(content); 
        }
        
        //System.out.println(jsonContent.get("Errors").toString().replaceAll("[^ a-zA-Záéíóú]", ""));
        //System.out.println(jsonContent.get("Message")); 
         
        //verificamos que la respuesta o estado sea 200
        if (response.getStatusLine().getStatusCode() == 200) {        	
            map.put("success", Integer.toString(response.getStatusLine().getStatusCode()));
            map.put("Id", jsonContent.get("Id").toString());
            map.put("IdEntrega", jsonContent.get("IdEntrega").toString());
            return map;
        	
        }else if (response.getStatusLine().getStatusCode() == 422) {        	
        	map.put("error", jsonContent.get("Message").toString() +",  "+ jsonContent.get("Errors").toString().replaceAll("[^ a-zA-Záéíóú]", ""));
        	return map;
        }else {
        	map.put("error", jsonContent.get("Message").toString());
        	return map;
        }
    }
	
	//Se usa para hacer put en el web service de mipres REPORTE ENTREGA
	private Map<String, String> guardarWebServiceMipresReporteEntrega(FarMipres farMipres, Integer estadoentrega, String valor) throws IOException {
		
		Map<String, String> map = new HashMap<>();
		
		//obtengo los datos del token secundario guardados en solution
		ComTokenMipres comTokenMipres = iComTokenMipresService.findById(1L); 
		
		//genero la url para consultar
		String urlEncadenada = MetodoPutReporteEntrega+comTokenMipres.getNit()+'/'+comTokenMipres.getTokenSecundario();
		
		//Especificamos la URL y configuro el objeto CloseableHttpClient
		CloseableHttpClient httpclient = HttpClients.createDefault();
				
		//Se crea una solicitud PUT (si es post HttpPost y si es get HttpGet) y pasamos el URL del recurso y también asigne encabezados a este objeto de colocación
		HttpPut httpPut = new HttpPut(urlEncadenada);
		httpPut.setHeader("Accept", "application/json");
		httpPut.setHeader("Content-type", "application/json");
		        
		//creo el json que sera pasado al objeto StringEntity
		JSONObject parametros = new JSONObject();
		parametros.put("ID", farMipres.getIdTraza());
		parametros.put("EstadoEntrega", estadoentrega);
		parametros.put("CausaNoEntrega", farMipres.getCausaNoEntrega());
		parametros.put("ValorEntregado", valor);
		
		//Proporciono la solicitud json en el objeto StringEntity y asígnela al objeto puesto.
        StringEntity stringEntity = new StringEntity(parametros.toString());
        stringEntity.setContentEncoding("UTF-8");        
        httpPut.setEntity(stringEntity);     
 
        //Envio la solicitud usando HttpPut -> Método de ejecución PUT
        HttpResponse response = httpclient.execute(httpPut);        
        
        //creo un objeto HttpEntity para obtener el resultado en String de la peticion
        HttpEntity entity = response.getEntity();
        String content = EntityUtils.toString(entity);       
        
        //convierto la respuesta de la peticion String a Json para mensajes personalizados
        JSONObject jsonContent = new JSONObject();
        if(response.getStatusLine().getStatusCode() == 200) {
        	jsonContent = new JSONObject(content.replaceAll("\\[", "").replaceAll("\\]", ""));//busca "[" y "]" y los reemplaza por espacios en blanco  
        }else {
        	jsonContent = new JSONObject(content); 
        }
        
      //verificamos que la respuesta o estado sea 200
        if (response.getStatusLine().getStatusCode() == 200) {        	
            map.put("success", Integer.toString(response.getStatusLine().getStatusCode()));
            map.put("Id", jsonContent.get("Id").toString());
            map.put("IdReporteEntrega", jsonContent.get("IdReporteEntrega").toString());
            return map;
        	
        }else if (response.getStatusLine().getStatusCode() == 422) {        	
        	map.put("error", jsonContent.get("Message").toString() +",  "+ jsonContent.get("Errors").toString().replaceAll("[^ a-zA-Záéíóú]", ""));
        	return map;
        }else {
        	map.put("error", jsonContent.get("Message").toString());
        	return map;
        }		
	}
	
	//Se usa para hacer put en el web service de mipres REPORTE FACTURACION
	private Map<String, String> guardarWebServiceMipresReporteFacturacion(FarMipres farMipres, String nit, String cdispensacion, String vunitario, String vtotal, String cuota, String copago) throws IOException {
		
		Map<String, String> map = new HashMap<>();
		
		//obtengo los datos del token secundario guardados en solution
		ComTokenMipres comTokenMipres = iComTokenMipresService.findById(1L); 
		
		//genero la url para consultar
		String urlEncadenada = MetodoPutReporteFacturacion+comTokenMipres.getNit()+'/'+comTokenMipres.getTokenSecundario();
		
		//Especificamos la URL y configuro el objeto CloseableHttpClient
		CloseableHttpClient httpclient = HttpClients.createDefault();
						
		//Se crea una solicitud PUT (si es post HttpPost y si es get HttpGet) y pasamos el URL del recurso y también asigne encabezados a este objeto de colocación
		HttpPut httpPut = new HttpPut(urlEncadenada);
		httpPut.setHeader("Accept", "application/json");
		httpPut.setHeader("Content-type", "application/json");
				        
		//creo el json que sera pasado al objeto StringEntity
		JSONObject parametros = new JSONObject();
		parametros.put("NoPrescripcion", farMipres.getNumeroPrescripcion());
		parametros.put("TipoTec", farMipres.getComTipoTecnologia().getTipo());
		parametros.put("ConTec", farMipres.getConsecutivoTecnologia());
		parametros.put("TipoIDPaciente", farMipres.getComTipoDocumentoMipres().getTipo());
		parametros.put("NoIDPaciente", farMipres.getGenPacien().getPacNumDoc());		
		parametros.put("NoEntrega", farMipres.getEntregaTotal());
		parametros.put("NoFactura", farMipres.getNumeroFactura());
		parametros.put("NoIDEPS", nit);
		parametros.put("CodEPS", farMipres.getCodEps());
		parametros.put("CodSerTecAEntregado", farMipres.getCodigoServicio());
		parametros.put("CantUnMinDis", cdispensacion);
		parametros.put("ValorUnitFacturado", vunitario);
		parametros.put("ValorTotFacturado", vtotal);
		parametros.put("CuotaModer", cuota);
		parametros.put("Copago", copago);
				
		//Proporciono la solicitud json en el objeto StringEntity y asígnela al objeto puesto.
		StringEntity stringEntity = new StringEntity(parametros.toString());
		stringEntity.setContentEncoding("UTF-8");        
		httpPut.setEntity(stringEntity);     
		 
		//Envio la solicitud usando HttpPut -> Método de ejecución PUT
		HttpResponse response = httpclient.execute(httpPut);
		
		//creo un objeto HttpEntity para obtener el resultado en String de la peticion
        HttpEntity entity = response.getEntity();
        String content = EntityUtils.toString(entity);       
        
        //convierto la respuesta de la peticion String a Json para mensajes personalizados
        JSONObject jsonContent = new JSONObject();
        if(response.getStatusLine().getStatusCode() == 200) {
        	jsonContent = new JSONObject(content.replaceAll("\\[", "").replaceAll("\\]", ""));//busca "[" y "]" y los reemplaza por espacios en blanco  
        }else {
        	jsonContent = new JSONObject(content); 
        }
        
      //verificamos que la respuesta o estado sea 200
        if (response.getStatusLine().getStatusCode() == 200) {        	
            map.put("success", Integer.toString(response.getStatusLine().getStatusCode()));
            map.put("Id", jsonContent.get("Id").toString());
            map.put("IdFacturacion", jsonContent.get("IdFacturacion").toString());
            return map;
        	
        }else if (response.getStatusLine().getStatusCode() == 422) {        	
        	map.put("error", jsonContent.get("Message").toString() +",  "+ jsonContent.get("Errors").toString().replaceAll("[^ a-zA-Záéíóú]", ""));
        	return map;
        }else {
        	map.put("error", jsonContent.get("Message").toString());
        	return map;
        }		
	}
	
	//Se usa para hacer get en el web service de mipres
	private void sincronizoPrescripcion(String fechaInicial, String fechaFinal, Principal principal) throws ParseException, IOException, IOException {
				
		//convierto String a Date 
		Date fInicialDate = convertirFecha(fechaInicial);
		Date fFinalDate = convertirFecha(fechaFinal);
		
		//convierto Date a LocalDate
		LocalDate fInicialLocalDate = fInicialDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		LocalDate fFinalLocalDate = fFinalDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		
		//Me sirve para guardar los tipos de tecnologias
		Map<String, String> map = new HashMap<>();
		
		
		for (LocalDate fecha = fInicialLocalDate; !fecha.isAfter(fFinalLocalDate); fecha = fecha.plusDays(1)) {
	        
			//obtengo los datos del token primario guardados en solution
			ComTokenMipres comTokenMipres = iComTokenMipresService.findById(2L);
			
			//genero la url para consultar
			String urlEncadenada = MetodoGetConsulta+comTokenMipres.getNit()+'/'+fecha+'/'+comTokenMipres.getTokenPrincipal();
			
			//Especificamos la URL y configuro el objeto HttpClient			
			HttpClient httpclient = HttpClientBuilder.create().build();			
			HttpResponse response = null;
			
			//Se crea una solicitud GET (si es post HttpPost y si es es put) y pasamos el URL del recurso y también asigne encabezados a este objeto de colocación
			HttpGet httpGet = new HttpGet(urlEncadenada);			
			httpGet.setHeader("Accept", "application/json");
			httpGet.setHeader("Content-type", "application/json");			
			
			response = httpclient.execute(httpGet);		
			
			//creo un String para guardar la respuesta y convertir en un arreglo JSON	        
	        String content = EntityUtils.toString(response.getEntity());	        
	        JSONArray arregloJSON = new JSONArray(content);
	        
	        //recorro el arreglo para tranformarlo en un objeto JSON
            for (int i = 0; i < arregloJSON.length(); i++) {
            	//jsonContent = new JSONObject(content.replaceAll("\\[", "").replaceAll("\\]", ""));
            	JSONObject objetoJSON = arregloJSON.getJSONObject(i);
            	Integer codigoAmbitoAtencion = objetoJSON.getJSONObject("prescripcion").getInt("CodAmbAte");
            	if(codigoAmbitoAtencion == 22 || codigoAmbitoAtencion == 30) {            	
            	
            	String prescripcion = objetoJSON.getJSONObject("prescripcion").getString("NoPrescripcion");            	
            	String tipoDocumento = objetoJSON.getJSONObject("prescripcion").getString("TipoIDPaciente");
            	String numDocumento = objetoJSON.getJSONObject("prescripcion").getString("NroIDPaciente");
            	String primerNombre = objetoJSON.getJSONObject("prescripcion").getString("PNPaciente");
            	String segundoNombre = objetoJSON.getJSONObject("prescripcion").getString("SNPaciente");
            	String primerApellido = objetoJSON.getJSONObject("prescripcion").getString("PAPaciente");
            	String segundoApellido = objetoJSON.getJSONObject("prescripcion").getString("SAPaciente");
            	String codEps = objetoJSON.getJSONObject("prescripcion").getString("CodEPS");
            	String fechaEntrega = objetoJSON.getJSONObject("prescripcion").getString("FPrescripcion");
            	
            	
            	String fechaEntregaCustomString = fechaEntrega.replace("T00:00:00", "");
            	Date fechaEntregaCustomDate = convertirFechaParametro(fechaEntregaCustomString);        	
            	            	
            	//limpiamos el Map
            	map.clear();
            	
            	//contamos los medicamentos dados
            	JSONArray arrayMedicamentos = objetoJSON.getJSONArray("medicamentos");            	
            	if(arrayMedicamentos.length() >= 1) {
	            	
            		for(int m=0; m < arrayMedicamentos.length(); m++) {          			
            			map.put("cantidadEntregada"+m, arrayMedicamentos.getJSONObject(m).getString("CantTotalF"));
	                    map.put("tipoTecnologia"+m, "M");	            		
	            	}
            	} 
            	
            	//contamos los procedimientos dados
            	JSONArray arrayProcedimientos = objetoJSON.getJSONArray("procedimientos");            	
            	if(arrayProcedimientos.length() >= 1) {
            		
            		for(int p=0; p < arrayProcedimientos.length(); p++) {           			
            			map.put("cantidadEntregada"+p, arrayProcedimientos.getJSONObject(p).getString("CantTotal"));
	                    map.put("tipoTecnologia"+p, "P");	            		
	            	}
            	}
            	
            	//contamos los productos nutricionales dados
            	JSONArray arrayProductosNutricionales = objetoJSON.getJSONArray("productosnutricionales");            	
            	if(arrayProductosNutricionales.length() >= 1) {
            		
            		for(int n=0; n < arrayProductosNutricionales.length(); n++) {            			
            			map.put("cantidadEntregada"+n, arrayProductosNutricionales.getJSONObject(n).getString("CantTotalF"));
	                    map.put("tipoTecnologia"+n, "N");	            		
	            	}           		
            	}
            	
            	//contamos los servicios complementarios dados
            	JSONArray arrayServicioComplementario = objetoJSON.getJSONArray("serviciosComplementarios");            	
            	if(arrayServicioComplementario.length() >= 1) {
            		
            		for(int c=0; c < arrayServicioComplementario.length(); c++) {           			
            			map.put("cantidadEntregada"+c, arrayServicioComplementario.getJSONObject(c).getString("CantTotal"));
	                    map.put("tipoTecnologia"+c, "S");	            		
	            	}          		
            	}        
                
            	//este if es cuando es solo una sola tecnologia
            	if(map.size() == 2) {
            		
            		FarMipres buscarSolution = iFarMipresService.findByDocumentoPrescripcionTecnologiaCantidad(numDocumento, prescripcion, map.get("cantidadEntregada0"));
                    
                    if(buscarSolution == null) {         	
                    	
                    	FarMipres farMipres = new FarMipres();
                        
                    	//buscamos el tipo documento paciente para guardar
                    	ComTipoDocumentoMipres comTipoDocumentoMipres = iComTipoDocumentoMipresService.tipoDocumento(tipoDocumento);
                    	
                    	//buscamos el numero de documento del paciente para guardar
                    	GenPacien genPacien = iGenPacienService.findByNumberDoc(numDocumento);
                    	if(genPacien == null) {
                    		GenPacien obtengoPaciente =  SicronizarPacientePorDocumento(tipoDocumento, numDocumento, primerNombre, segundoNombre, primerApellido, segundoApellido);
                    		farMipres.setGenPacien(obtengoPaciente);
                    	}  else {
                    		farMipres.setGenPacien(genPacien);
                    	}
                    	
                    	//buscamos la tecnologia para guardar                	  
                    	ComTipoTecnologia comTipoTecnologia = iComTipoTecnologiaService.tipoTecnologia(map.get("tipoTecnologia0"));  
                    	
                    	farMipres.setNumeroPrescripcion(prescripcion);
                        farMipres.setCantidadEntregada(map.get("cantidadEntregada0"));
                        farMipres.setProcesadoEntrega(false);
                        farMipres.setProcesadoReporteEntrega(false);
                        farMipres.setProcesadoFacturacion(false);                    
                        farMipres.setComTipoDocumentoMipres(comTipoDocumentoMipres);                    
                        farMipres.setFechaEntrega(fechaEntregaCustomDate);
                        farMipres.setConsecutivoTecnologia(1);
                        farMipres.setComTipoTecnologia(comTipoTecnologia);
                        farMipres.setCodigoServicio("NULL");
                        farMipres.setNumeroEntrega(1);
                        farMipres.setEntregaTotal(1);
                        farMipres.setCausaNoEntrega(0);
                        farMipres.setLoginUsrAlta(principal.getName());
                        farMipres.setCodEps(codEps);
                    	iFarMipresService.save(farMipres);            	
                    }   
                //este else es cuando son varias tecnologias
            	}else {
            		for(int z=0; z<map.size()/2; z++) {
            			
            			String cantidadEntregada = map.get("cantidadEntregada"+z);                    	
                    	String tipoTecnologia = map.get("tipoTecnologia"+z);                    	
            			
            			FarMipres buscarSolution = iFarMipresService.findByDocumentoPrescripcionTecnologiaCantidad(numDocumento, prescripcion, cantidadEntregada);
                        
                        if(buscarSolution == null) {                
            			
            			FarMipres farMipres = new FarMipres();
                        
                    	//buscamos el tipo documento paciente para guardar
                    	ComTipoDocumentoMipres comTipoDocumentoMipres = iComTipoDocumentoMipresService.tipoDocumento(tipoDocumento);
                    	
                    	//buscamos el numero de documento del paciente para guardar
                    	GenPacien genPacien = iGenPacienService.findByNumberDoc(numDocumento);
                    	if(genPacien == null) {
                    		GenPacien obtengoPaciente =  SicronizarPacientePorDocumento(tipoDocumento, numDocumento, primerNombre, segundoNombre, primerApellido, segundoApellido);
                    		farMipres.setGenPacien(obtengoPaciente);
                    	}  else {
                    		farMipres.setGenPacien(genPacien);
                    	}                    	
                    	
                    	//buscamos la tecnologia para guardar                	  
                    	ComTipoTecnologia comTipoTecnologia = iComTipoTecnologiaService.tipoTecnologia(tipoTecnologia);  
                    	
                    	farMipres.setNumeroPrescripcion(prescripcion);
                        farMipres.setCantidadEntregada(cantidadEntregada);
                        farMipres.setProcesadoEntrega(false);
                        farMipres.setProcesadoReporteEntrega(false);
                        farMipres.setProcesadoFacturacion(false);                    
                        farMipres.setComTipoDocumentoMipres(comTipoDocumentoMipres);                    
                        farMipres.setFechaEntrega(fechaEntregaCustomDate);
                        farMipres.setConsecutivoTecnologia(1+z);
                        farMipres.setComTipoTecnologia(comTipoTecnologia);
                        farMipres.setCodigoServicio("NULL");
                        farMipres.setNumeroEntrega(1);
                        farMipres.setEntregaTotal(0);
                        farMipres.setCausaNoEntrega(0);
                        farMipres.setLoginUsrAlta(principal.getName());
                        farMipres.setCodEps(codEps);
                    	iFarMipresService.save(farMipres);
                    	
                        	}
            			}
            		}
            	}	
            	 	
            }//fin for
	    }	
	}	

	// Se usa para dar formato a fechas de dinamica
	private String formatoFecha(Date fecha) {

		// convierto la fecha que entra en formato Date
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String fechaConversion = sdf.format(fecha);		
		return fechaConversion;

	}
	
	//Se usa para convertir parametro fecha solicitud de String a fecha Date con formato
	private Date convertirFechaParametro(String fecha) throws ParseException {
		Date fechaTranformada = new SimpleDateFormat("yyyy-MM-dd").parse(fecha);		
		return fechaTranformada;
	}
	
	//Se usa para convertir parametro fecha solicitud de String a fecha Date con formato
	private Date convertirFecha(String fecha) throws ParseException {
		Date fechaTranformada = new SimpleDateFormat("dd-MM-yyyy").parse(fecha);		
		return fechaTranformada;
	}
	
	//Se usa para sincronizar los pacientes de dinamica a solution
	private GenPacien SicronizarPaciente(GenPacien genPacien, String pacNumDoc) {
		if(genPacien == null) {
			// proceso API para buscar el paciente
			ResponseEntity<List<GenPacienDTO>> respuestaa = restTemplate.exchange(URLPaciente + '/' + pacNumDoc, HttpMethod.GET, null,new ParameterizedTypeReference<List<GenPacienDTO>>() {});
			List<GenPacienDTO> dinamica = respuestaa.getBody();
			
			GenPacien guardarPaciente = pacienteAgregar(dinamica);					
			iGenPacienService.save(guardarPaciente);
			GenPacien obtengoPaciente = iGenPacienService.findByNumberDoc(pacNumDoc);		
			return obtengoPaciente;
		}
		
		return genPacien;		
	}
	
	//Se usa para sincronizar los pacientes de dinamica a solution
	private GenPacien SicronizarPacientePorDocumento(String tipoDocumento, String numDocumento, String primerNombre, String segundoNombre, String primerApellido, String segundoApellido) {
		// proceso API para buscar el paciente
		ResponseEntity<List<GenPacienDTO>> respuestaa = restTemplate.exchange(URLPaciente + '/' + numDocumento, HttpMethod.GET, null,new ParameterizedTypeReference<List<GenPacienDTO>>() {});
		List<GenPacienDTO> dinamica = respuestaa.getBody();
		
		List<GenPacienDTO> filtrarPaciente = dinamica.stream().filter(c -> c.getPacNumDoc().equals(numDocumento)).collect(Collectors.toList());
		
		if(!filtrarPaciente.isEmpty()) {
			GenPacien guardarPaciente = pacienteAgregar(filtrarPaciente);					
			iGenPacienService.save(guardarPaciente);
			GenPacien obtengoPaciente = iGenPacienService.findByNumberDoc(numDocumento);		
			return obtengoPaciente;
		}else {
			GenPacien guardarPaciente = pacienteNoExisteDinamica(tipoDocumento, numDocumento, primerNombre, segundoNombre, primerApellido, segundoApellido);
			iGenPacienService.save(guardarPaciente);
			GenPacien obtengoPaciente = iGenPacienService.findByNumberDoc(numDocumento);		
			return obtengoPaciente;
		}
		
	}
	
	//Se usa para crear un paciente no existente en dinamica
	private GenPacien pacienteNoExisteDinamica(String tipoDocumento, String numDocumento, String primerNombre, String segundoNombre, String primerApellido, String segundoApellido) {
		GenPacien agregarPaciente = new GenPacien();
		//buscamos el sexo del paciente			
		ComGenero sexoPaciente = iComGeneroService.findById(2L);
			
		//buscamos el tipo de documento del paciente			
		ComTipoDocumento tipoDocumentoMipres = iComTipoDocumentoService.tipoDocumento(tipoDocumento);
			
		agregarPaciente.setOid(null);
		agregarPaciente.setPacNumDoc(numDocumento);
		agregarPaciente.setPacPriNom(primerNombre);
		agregarPaciente.setPacSegNom(segundoNombre);
		agregarPaciente.setPacPriApe(primerApellido);
		agregarPaciente.setPacSegApe(segundoApellido);
		agregarPaciente.setGpafecnac(new Date());
		agregarPaciente.setComGenero(sexoPaciente);
		agregarPaciente.setComTipoDocumento(tipoDocumentoMipres);
		return agregarPaciente;
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
	
	//se usa para obtener los id e identrega del web service de mipres
	private void sincronizoEntrega(String prescripcion){
		List<FarMipres> farMipres = iFarMipresService.findByPrescripcion(prescripcion);
		
		farMipres.forEach(f -> {
			
			//obtengo los datos del token primario guardados en solution
			ComTokenMipres comTokenMipres = iComTokenMipresService.findById(1L);
			
			//genero la url para consultar
			String urlEncadenada = MetodoGetPrescripcion+comTokenMipres.getNit()+'/'+comTokenMipres.getTokenSecundario()+'/'+prescripcion;
			
			//Especificamos la URL y configuro el objeto HttpClient			
			HttpClient httpclient = HttpClientBuilder.create().build();			
			HttpResponse response = null;
			
			//Se crea una solicitud GET (si es post HttpPost y si es es put) y pasamos el URL del recurso y también asigne encabezados a este objeto de colocación
			HttpGet httpGet = new HttpGet(urlEncadenada);			
			httpGet.setHeader("Accept", "application/json");
			httpGet.setHeader("Content-type", "application/json");			
			
			try {
				response = httpclient.execute(httpGet);
			} catch (IOException e) {				
				e.printStackTrace();
			}			
			
			//creo un String para guardar la respuesta y convertir en un arreglo JSON	        
	        String content;
			try {
				content = EntityUtils.toString(response.getEntity());
				JSONArray arregloJSON = new JSONArray(content);
				
				for (int i = 0; i < arregloJSON.length(); i++) {
					
					JSONObject objetoJSON = arregloJSON.getJSONObject(i);
	            	String idTraza = objetoJSON.getString("ID");
	            	String idEntrega = objetoJSON.getString("IDEntrega");
	            	String tecnologia = objetoJSON.getString("TipoTec");
	            	Integer consecutivoTecnologia = objetoJSON.getInt("TipoTec");	            	
	            	String tipoDocumento = objetoJSON.getString("TipoIDPaciente");
	            	String numDocumento = objetoJSON.getString("NoIDPaciente");
	            	Integer numeroEntrega = objetoJSON.getInt("NoEntrega");
	            	String codigoServicio = objetoJSON.getString("CodSerTecEntregado");    
	            	String cantidadEntregada = objetoJSON.getString("CantTotEntregada");
	            	Integer entregaTotal = objetoJSON.getInt("EntTotal");
	            	String FechaEntrega = objetoJSON.getString("FecEntrega");
	            	
	            	f.setIdTraza(idTraza);
	            	f.setIdEntregaMipress(idEntrega);	            	
	            	//busco el tipo de tecnologia para guardar
	            	ComTipoTecnologia comTipoTecnologia = iComTipoTecnologiaService.tipoTecnologia(tecnologia);
	            	f.setComTipoTecnologia(comTipoTecnologia);	            	
	            	f.setConsecutivoTecnologia(consecutivoTecnologia);
	            	//busco el tipo de documento para guardar
	            	ComTipoDocumentoMipres comTipoDocumentoMipres = iComTipoDocumentoMipresService.tipoDocumento(tipoDocumento);
	            	f.setComTipoDocumentoMipres(comTipoDocumentoMipres);
	            	//busco el paciente para guardar
	            	GenPacien genPacien = iGenPacienService.findByNumberDoc(numDocumento);
	            	f.setGenPacien(genPacien);
	            	f.setNumeroEntrega(numeroEntrega);
	            	f.setCodigoServicio(codigoServicio);
	            	f.setCantidadEntregada(cantidadEntregada);
	            	f.setEntregaTotal(entregaTotal);
	            	//recortar String fecha
	            	String fechaAjustada = FechaEntrega.replace(" 00:00", "");
	            	f.setFechaEntrega(convertirFechaParametro(fechaAjustada));
	            	f.setFechaRegistro(new Date());
	            	f.setProcesadoEntrega(true);
	            	f.setProcesadoReporteEntrega(false);
	            	f.setProcesadoFacturacion(false);
	            	iFarMipresService.save(f);
	            	
				}
				
				
			} catch (org.apache.http.ParseException e) {				
				e.printStackTrace();
			} catch (IOException e) {				
				e.printStackTrace();
			} catch (ParseException e) {				
				e.printStackTrace();
			}		
			
		});
	}
		
}