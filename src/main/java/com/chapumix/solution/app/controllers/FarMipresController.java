package com.chapumix.solution.app.controllers;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.chapumix.solution.app.entity.dto.GenPacienDTO;
import com.chapumix.solution.app.models.entity.ComAmbito;
import com.chapumix.solution.app.models.entity.ComGenero;
import com.chapumix.solution.app.models.entity.ComTipoDocumento;
import com.chapumix.solution.app.models.entity.ComTipoDocumentoMipres;
import com.chapumix.solution.app.models.entity.ComTipoTecnologia;
import com.chapumix.solution.app.models.entity.ComTokenMipres;
import com.chapumix.solution.app.models.entity.FarMipres;
import com.chapumix.solution.app.models.entity.GenPacien;
import com.chapumix.solution.app.models.service.IComAmbitoService;
import com.chapumix.solution.app.models.service.IComGeneroService;
import com.chapumix.solution.app.models.service.IComTipoDocumentoMipresService;
import com.chapumix.solution.app.models.service.IComTipoDocumentoService;
import com.chapumix.solution.app.models.service.IComTipoTecnologiaService;
import com.chapumix.solution.app.models.service.IComTokenMipresService;
import com.chapumix.solution.app.models.service.IFarMipresService;
import com.chapumix.solution.app.models.service.IGenPacienService;
import com.chapumix.solution.app.utils.ExcelUtils;
import com.chapumix.solution.app.utils.PacienteDinamica;
import com.chapumix.solution.app.utils.PageRender;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;


@Controller
@SessionAttributes({"farMipres","comTokenMipres"})
@PropertySource(value = "application.properties", encoding="UTF-8")
public class FarMipresController {
	
	public static final String MetodoPutEntrega = "https://wsmipres.sispro.gov.co/WSSUMMIPRESNOPBS/api/EntregaAmbito/"; //url mipres metodo para put entrega
	
	public static final String MetodoPutAnulaEntrega = "https://wsmipres.sispro.gov.co/WSSUMMIPRESNOPBS/api/AnularEntrega/"; //url mipres metodo para put anulacion entrega
	
	public static final String MetodoPutReporteEntrega = "https://wsmipres.sispro.gov.co/WSSUMMIPRESNOPBS/api/ReporteEntrega/"; //url mipres metodo para put reporte entrega
	
	public static final String MetodoPutAnulaReporteEntrega = "https://wsmipres.sispro.gov.co/WSSUMMIPRESNOPBS/api/AnularReporteEntrega/"; //url mipres metodo para put anulacion reporte entrega
	
	public static final String MetodoGetReporteEntrega = "https://wsmipres.sispro.gov.co/WSSUMMIPRESNOPBS/api/ReporteEntregaXPrescripcion/"; //url mipres metodo para get reporte entrega
	
	public static final String MetodoPutReporteFacturacion = "https://wsmipres.sispro.gov.co/WSFACMIPRESNOPBS/api/Facturacion/"; //url mipres metodo para put reporte facturacion
	
	public static final String MetodoPutAnulacionReporteFacturacion = "https://wsmipres.sispro.gov.co/WSFACMIPRESNOPBS/api/FacturacionAnular/"; //url mipres metodo para put anulacion reporte facturacion
	
	public static final String MetodoGetReporteFacturacion = "https://wsmipres.sispro.gov.co/WSFACMIPRESNOPBS/api/FacturacionXPrescripcion/"; //url mipres metodo para get reporte facturacion
	
	public static final String MetodoGetConsultaFecha = "https://wsmipres.sispro.gov.co/wsmipresnopbs/api/Prescripcion/"; //url mipres metodo para get consulta por fecha 
	
	public static final String MetodoGetConsultaFechaNumeroPrescripcion = "https://wsmipres.sispro.gov.co/wsmipresnopbs/api/PrescripcionXNumero/"; //url mipres metodo para get consulta por numero de prescipcion
	
	public static final String MetodoGetPrescripcion = "https://wsmipres.sispro.gov.co/WSSUMMIPRESNOPBS/api/EntregaXPrescripcion/"; //url mipres metodo para get consulta
	
	public static final String MetodoGetDireccionamiento = "https://wsmipres.sispro.gov.co/WSSUMMIPRESNOPBS/api/DireccionamientoXPrescripcion/"; //url mipres metodo para get direccionamiento ambulatorio
	
	public static final String MetodoPutProgramacion = "https://wsmipres.sispro.gov.co/WSSUMMIPRESNOPBS/api/Programacion/"; //url mipres metodo para put programacion ambulatorio
	
	public static final String MetodoPutEntregaAmbulatorio = "https://wsmipres.sispro.gov.co/WSSUMMIPRESNOPBS/api/Entrega/"; //url mipres metodo para put entrega ambulatorio
	
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
	private IComAmbitoService iComAmbitoService;
	
	@Autowired
	private PacienteDinamica pacienteDinamica;
	
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
	
	@Value("${app.tituloprogramacion}")
	private String tituloprogramacion;	
	
	@Value("${app.tituloreporteentrega}")
	private String tituloreporteentrega;
	
	@Value("${app.tituloreportefacturacion}")
	private String tituloreportefacturacion;	
	
	@Value("${app.tituloentregarealizadahosp}")
	private String tituloentregarealizadahosp;
	
	@Value("${app.tituloentregaprocesadahosp}")
	private String tituloentregaprocesadahosp;
	
	@Value("${app.tituloentregarealizadaamb}")
	private String tituloentregarealizadaamb;
	
	@Value("${app.tituloentregaprocesadaamb}")
	private String tituloentregaprocesadaamb;
	
	
	@Value("${app.enlaceprincipalfarmacia}")
	private String enlaceprincipalfarmacia;
	
	@Value("${app.titulosincroniza}")
	private String titulosincroniza;
	
	@Value("${app.reporterips}")
	private String reportemipres;
	
	@Value("${app.anularprescripcion}")
	private String anularmipres;	
	
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
	@GetMapping("/pendientesmipreshosp")
	public String listarPendientesHosp(Model model, @RequestParam(name = "page", defaultValue = "0") int page, @RequestParam(name = "buscando", required = false) String buscando) {
		
		if(buscando == null || buscando == "") {
			Pageable pageRequest = PageRequest.of(page, 100);			
			Page<FarMipres> farMipres = iFarMipresService.findAllCustomPendientesHosp(pageRequest);
			PageRender<FarMipres> pageRender = new PageRender<>("/pendientesmipreshosp", farMipres);
			model.addAttribute("listentrega", farMipres);
			model.addAttribute("totalItems", farMipres.getTotalElements());			
			model.addAttribute("page", pageRender);
		}else {
			Pageable pageRequest = PageRequest.of(page, 100);			
			Page<FarMipres> farMipres = iFarMipresService.findAllCustomSearchPendientesHosp(pageRequest, buscando);
			PageRender<FarMipres> pageRender = new PageRender<>("/pendientesmipreshosp", farMipres);
			model.addAttribute("listentrega", farMipres);
			model.addAttribute("totalItems", farMipres.getTotalElements());			
			model.addAttribute("page", pageRender);
		}		
								
		model.addAttribute("titulo", utf8(this.tituloentregarealizadahosp));			
		model.addAttribute("farmacia", enlaceprincipalfarmacia);
		model.addAttribute("enlace10", enlace10);		
		return "pendientesmipreshosp";
	}
	
	
	// Este metodo me permite listar todas las entregas pendientes en el web service del ministerio
	@GetMapping("/pendientesmipresamb")
	public String listarPendientesAmb(Model model, @RequestParam(name = "page", defaultValue = "0") int page, @RequestParam(name = "buscando", required = false) String buscando) {
			
		if(buscando == null || buscando == "") {
			Pageable pageRequest = PageRequest.of(page, 100);			
			Page<FarMipres> farMipres = iFarMipresService.findAllCustomPendientesAmb(pageRequest);
			PageRender<FarMipres> pageRender = new PageRender<>("/pendientesmipresamb", farMipres);
			model.addAttribute("listentrega", farMipres);
			model.addAttribute("totalItems", farMipres.getTotalElements());			
			model.addAttribute("page", pageRender);
		}else {
			Pageable pageRequest = PageRequest.of(page, 100);			
			Page<FarMipres> farMipres = iFarMipresService.findAllCustomSearchPendientesAmb(pageRequest, buscando);
			PageRender<FarMipres> pageRender = new PageRender<>("/pendientesmipresamb", farMipres);
			model.addAttribute("listentrega", farMipres);
			model.addAttribute("totalItems", farMipres.getTotalElements());			
			model.addAttribute("page", pageRender);
		}		
								
		model.addAttribute("titulo", utf8(this.tituloentregarealizadaamb));			
		model.addAttribute("farmacia", enlaceprincipalfarmacia);
		model.addAttribute("enlace10", enlace10);		
		return "pendientesmipresamb";
	}
	
	
	// Este metodo me permite listar todas las entregas procesadas en el web service del ministerio
	@GetMapping("/procesadosmipreshosp")
	public String listarProcesadosHosp(Model model, @RequestParam(name = "page", defaultValue = "0") int page, @RequestParam(name = "buscando", required = false) String buscando) {
			
		if(buscando == null || buscando == "") {
			Pageable pageRequest = PageRequest.of(page, 150);			
			Page<FarMipres> farMipres = iFarMipresService.findAllCustomProcesadosHosp(pageRequest);
			PageRender<FarMipres> pageRender = new PageRender<>("/procesadosmipreshosp", farMipres);
			model.addAttribute("listentrega", farMipres);
			model.addAttribute("page", pageRender);
		}else {
			Pageable pageRequest = PageRequest.of(page, 150);			
			Page<FarMipres> farMipres = iFarMipresService.findAllCustomSearchProcesadosHosp(pageRequest, buscando);
			PageRender<FarMipres> pageRender = new PageRender<>("/procesadosmipreshosp", farMipres);
			model.addAttribute("listentrega", farMipres);
			model.addAttribute("page", pageRender);
		}		
									
		model.addAttribute("titulo", utf8(this.tituloentregaprocesadahosp));			
		model.addAttribute("farmacia", enlaceprincipalfarmacia);
		model.addAttribute("enlace10", enlace10);		
		return "procesadosmipreshosp";
	}
	
	// Este metodo me permite listar todas las entregas procesadas en el web service del ministerio
	@GetMapping("/procesadosmipresamb")
	public String listarProcesadosAmb(Model model, @RequestParam(name = "page", defaultValue = "0") int page, @RequestParam(name = "buscando", required = false) String buscando) {
				
		if(buscando == null || buscando == "") {
			Pageable pageRequest = PageRequest.of(page, 150);			
			Page<FarMipres> farMipres = iFarMipresService.findAllCustomProcesadosAmb(pageRequest);
			PageRender<FarMipres> pageRender = new PageRender<>("/procesadosmipresamb", farMipres);
			model.addAttribute("listentrega", farMipres);
			model.addAttribute("page", pageRender);
		}else {
			Pageable pageRequest = PageRequest.of(page, 150);			
			Page<FarMipres> farMipres = iFarMipresService.findAllCustomSearchProcesadosAmb(pageRequest, buscando);
			PageRender<FarMipres> pageRender = new PageRender<>("/procesadosmipresamb", farMipres);
			model.addAttribute("listentrega", farMipres);
			model.addAttribute("page", pageRender);
		}		
										
		model.addAttribute("titulo", utf8(this.tituloentregaprocesadaamb));			
		model.addAttribute("farmacia", enlaceprincipalfarmacia);
		model.addAttribute("enlace10", enlace10);		
		return "procesadosmipresamb";
	}
	
	// Este metodo me permite cargar el consolidad de la prescripcion hospitalario
	@RequestMapping(value = "/consolidadomipresformhosp")
	public String cargarConsolidadoHosp(@RequestParam(value = "id", required = false) Long id, Map<String, Object> model, RedirectAttributes flash, Principal principal) throws ParseException {
			
		FarMipres farMipres = null;
		if(id > 0) {			
			farMipres = iFarMipresService.findById(id);
			if(farMipres == null) {
				flash.addFlashAttribute("error", "El ID del consolidado no existe en la base de datos");
				return "redirect:/procesadosmipreshosp";
			}
		}else {
			flash.addFlashAttribute("error", "El ID del consolidado no puede ser 0");
				return "redirect:/procesadosmipreshosp";
		}			
		
		model.put("titulo", utf8(this.tituloentrega));
		model.put("farMipres", farMipres);
		model.put("tecnologia", iComTipoTecnologiaService.findAll());
		model.put("tipodocumento", iComTipoDocumentoMipresService.findAll());
		model.put("farmacia", enlaceprincipalfarmacia);
		model.put("enlace10", enlace10);
		return "consolidadomipresformhosp";
	}
	
	// Este metodo me permite cargar el consolidad de la prescripcion ambulatorio
		@RequestMapping(value = "/consolidadomipresformamb")
		public String cargarConsolidadoAmb(@RequestParam(value = "id", required = false) Long id, Map<String, Object> model, RedirectAttributes flash, Principal principal) throws ParseException {
				
			FarMipres farMipres = null;
			if(id > 0) {			
				farMipres = iFarMipresService.findById(id);
				if(farMipres == null) {
					flash.addFlashAttribute("error", "El ID del consolidado no existe en la base de datos");
					return "redirect:/procesadosmipresamb";
				}
			}else {
				flash.addFlashAttribute("error", "El ID del consolidado no puede ser 0");
					return "redirect:/procesadosmipresamb";
			}			
			
			model.put("titulo", utf8(this.tituloentrega));
			model.put("farMipres", farMipres);
			model.put("tecnologia", iComTipoTecnologiaService.findAll());
			model.put("tipodocumento", iComTipoDocumentoMipresService.findAll());
			model.put("farmacia", enlaceprincipalfarmacia);
			model.put("enlace10", enlace10);
			return "consolidadomipresformamb";
		}
	
	// Este metodo me permite generar el certificado hospitalario
	@RequestMapping(value = "/certificadomipreshosp")
	public String certificadoMipresHosp(@RequestParam(value = "id", required = false) Long id, Map<String, Object> model, RedirectAttributes flash, Principal principal, HttpServletResponse response) throws ParseException, JRException, IOException {
				
		List<FarMipres> farMipres = null;
		if(id > 0) {			
			farMipres = iFarMipresService.findByIdMipres(id);
			crearPDFHosp(farMipres, response);			
		}
		return null;
	}
	
	// Este metodo me permite generar el certificado ambulatorio
	@RequestMapping(value = "/certificadomipresamb")
	public String certificadoMipresAmb(@RequestParam(value = "id", required = false) Long id, Map<String, Object> model, RedirectAttributes flash, Principal principal, HttpServletResponse response) throws ParseException, JRException, IOException {
					
		List<FarMipres> farMipres = null;
		if(id > 0) {			
			farMipres = iFarMipresService.findByIdMipres(id);
			crearPDFAmb(farMipres, response);			
		}
		return null;
	}
	
	// Este metodo me permite cargar los datos para editar la programacion y guardar
	@RequestMapping(value = "/programacionambform")
	public String crearProgramacionAmb(@RequestParam(value = "id", required = false) Long id, Map<String, Object> model, RedirectAttributes flash, Principal principal) throws ParseException {
				
		FarMipres farMipres = null;
		if(id > 0) {			
			farMipres = iFarMipresService.findById(id);
			if(farMipres == null) {
				flash.addFlashAttribute("error", "El ID del reporte no existe en la base de datos");
				return "redirect:/pendientesmipresamb";
			}
		}else {
			flash.addFlashAttribute("error", "El ID del reporte no puede ser 0");
			return "redirect:/pendientesmipreshosp";
		}
						
		model.put("titulo", utf8(this.tituloprogramacion));
		model.put("farMipres", farMipres);
		model.put("tecnologia", iComTipoTecnologiaService.findAll());
		model.put("tipodocumento", iComTipoDocumentoMipresService.findAll());
		model.put("farmacia", enlaceprincipalfarmacia);
		model.put("enlace10", enlace10);
		return "programacionambform";
	}
	
	
	// Este metodo me permite guardar la programacion
	@RequestMapping(value = "/programacionambform", method = RequestMethod.POST)
	public String guardarProgramacionAmb(@Valid FarMipres farMipres, BindingResult result, Model model, RedirectAttributes flash, SessionStatus status, Principal principal) throws Exception {
		if(result.hasErrors()) {
			model.addAttribute("titulo", utf8(this.tituloentrega));
			model.addAttribute("farMipres", farMipres);			
			model.addAttribute("farmacia", enlaceprincipalfarmacia);
			model.addAttribute("enlace10", enlace10);
			return "programacionambform";
		}		
			
		Map<String, String> webServiceInfo =  guardarWebServiceMipresProgramacion(farMipres);
			
			
		if(StringUtils.equals(webServiceInfo.get("success"), "200")) {
				
			//sincronizo paciente de dinamica a solution
			GenPacien genPacien = iGenPacienService.findByNumberDoc(farMipres.getGenPacien().getPacNumDoc());		
			GenPacien obtengoPaciente = pacienteDinamica.SincronizarPaciente(genPacien, farMipres.getGenPacien().getPacNumDoc());
				
			String mensajeFlash = (farMipres.getId() != null) ? "La programación fue editada correctamente"+"IdProgramacion: "+webServiceInfo.get("IdProgramacion") : "La programación fue creada correctamente "+"IdProgramacion: "+webServiceInfo.get("IdProgramacion");
			farMipres.setIdProgramacionMipress(webServiceInfo.get("IdProgramacion"));
			farMipres.setProcesadoProgramacion(true);
			farMipres.setLoginUsrAlta(principal.getName());
			farMipres.setFechaAltaAct(new Date());
			if(genPacien == null) {
				farMipres.setGenPacien(obtengoPaciente);
			}else {
				farMipres.setGenPacien(genPacien);
			}			
			iFarMipresService.save(farMipres);
			status.setComplete();
			flash.addFlashAttribute("success", mensajeFlash);
			return "redirect:pendientesmipresamb";
			}else {
				flash.addFlashAttribute("error", webServiceInfo.get("error"));
				return "redirect:pendientesmipresamb";
		}		
			
	}
	
	
	// Este metodo me permite cargar los datos para editar la entrega ambulatorio y guardar
	@RequestMapping(value = "/entregaambform")
	public String crearEntregaAmb(@RequestParam(value = "id", required = false) Long id, Map<String, Object> model, RedirectAttributes flash, Principal principal) throws ParseException {
			
		FarMipres farMipres = null;
		if(id > 0) {			
			farMipres = iFarMipresService.findById(id);
			if(farMipres == null) {
				flash.addFlashAttribute("error", "El ID del reporte no existe en la base de datos");
				return "redirect:/pendientesmipresamb";
			}
		}else {
			flash.addFlashAttribute("error", "El ID del reporte no puede ser 0");
			return "redirect:/pendientesmipresamb";
		}
					
		model.put("titulo", utf8(this.tituloentrega));
		model.put("farMipres", farMipres);
		model.put("tecnologia", iComTipoTecnologiaService.findAll());
		model.put("tipodocumento", iComTipoDocumentoMipresService.findAll());
		model.put("farmacia", enlaceprincipalfarmacia);
		model.put("enlace10", enlace10);
		return "entregaambform";
	}
	
	// Este metodo me permite guardar la entrega ambulatoria
	@RequestMapping(value = "/entregaambform", method = RequestMethod.POST)
	public String guardarEntregaAmb(@Valid FarMipres farMipres, BindingResult result, Model model, RedirectAttributes flash, SessionStatus status, Principal principal) throws Exception {
		if(result.hasErrors()) {
			model.addAttribute("titulo", utf8(this.tituloentrega));
			model.addAttribute("farMipres", farMipres);
			model.addAttribute("tecnologia", iComTipoTecnologiaService.findAll());
			model.addAttribute("tipodocumento", iComTipoDocumentoMipresService.findAll());
			model.addAttribute("farmacia", enlaceprincipalfarmacia);
			model.addAttribute("enlace10", enlace10);
			return "entregaambform";
		}		
		
		Map<String, String> webServiceInfo =  guardarWebServiceMipresEntregaAmb(farMipres);
			
			
		if(StringUtils.equals(webServiceInfo.get("success"), "200")) {
				
			//sincronizo paciente de dinamica a solution
			GenPacien genPacien = iGenPacienService.findByNumberDoc(farMipres.getGenPacien().getPacNumDoc());		
			GenPacien obtengoPaciente = pacienteDinamica.SincronizarPaciente(genPacien, farMipres.getGenPacien().getPacNumDoc());
			
			String mensajeFlash = (farMipres.getId() != null) ? "La entrega fue editada correctamente"+"IdEntrega: "+webServiceInfo.get("IdEntrega") : "La entrega fue creada correctamente "+"IdEntrega: "+webServiceInfo.get("IdEntrega");
			farMipres.setIdTraza(webServiceInfo.get("Id"));
			farMipres.setIdEntregaMipress(webServiceInfo.get("IdEntrega"));
			farMipres.setProcesadoEntrega(true);
			farMipres.setLoginUsrAlta(principal.getName());
			farMipres.setFechaAltaAct(new Date());
			if(genPacien == null) {
				farMipres.setGenPacien(obtengoPaciente);
			}else {
				farMipres.setGenPacien(genPacien);
			}			
			iFarMipresService.save(farMipres);
			status.setComplete();
			flash.addFlashAttribute("success", mensajeFlash);
			return "redirect:pendientesmipresamb";
		}else {
			flash.addFlashAttribute("error", webServiceInfo.get("error"));
			return "redirect:pendientesmipresamb";
		}		
		
	}
	
	// Este metodo me permite cargar los datos para editar el reporte de entrega ambulatorio y guardar
	@RequestMapping(value = "/reporteentregaambform")
	public String crearReporteEntregaAmb(@RequestParam(value = "id", required = false) Long id, Map<String, Object> model, RedirectAttributes flash, Principal principal) throws ParseException {
				
		FarMipres farMipres = null;
		if(id > 0) {			
			farMipres = iFarMipresService.findById(id);
			if(farMipres == null) {
				flash.addFlashAttribute("error", "El ID del reporte no existe en la base de datos");
				return "redirect:/pendientesmipresamb";
			}
		}else {
			flash.addFlashAttribute("error", "El ID del reporte no puede ser 0");
			return "redirect:/pendientesmipresamb";
		}				
				
		model.put("titulo", utf8(this.tituloreporteentrega));
		model.put("farMipres", farMipres);
		model.put("farmacia", enlaceprincipalfarmacia);
		model.put("enlace10", enlace10);
		return "reporteentregaambform";
	}
	
	
	// Este metodo me permite guardar el reporte de entrega ambulatorio
	@RequestMapping(value = "/reporteentregaambform", method = RequestMethod.POST)
	public String guardarReporteEntregaAmb(@Valid FarMipres farMipres, @RequestParam(value = "estadoentrega", required = false) Integer estadoentrega, BindingResult result, Model model, RedirectAttributes flash, SessionStatus status, Principal principal) throws Exception {
		if(result.hasErrors()) {
			model.addAttribute("titulo", utf8(this.tituloreporteentrega));
			model.addAttribute("farMipres", farMipres);			
			model.addAttribute("farmacia", enlaceprincipalfarmacia);
			model.addAttribute("enlace10", enlace10);
			return "reporteentregaambform";
		}
			
		if(estadoentrega == null) {			
			model.addAttribute("error", "El estado de entrega es requerido");
			return "reporteentregaambform";
		}
			
		if(farMipres.getValorEntregado().isEmpty()) {			
			model.addAttribute("error", "El valor es requerido");
			return "reporteentregaambform";
		}
			
		if(farMipres.getNumeroFactura().isEmpty()) {			
			model.addAttribute("error", "La factura es requerida");
			return "reporteentregaambform";
		}
			
			
		Map<String, String> webServiceInfo =  guardarWebServiceMipresReporteEntrega(farMipres, estadoentrega);
		
		if(StringUtils.equals(webServiceInfo.get("success"), "200")) {
						
			String mensajeFlash = (farMipres.getId() != null) ? "El reporte de entrega fue creado correctamente "+"IdReporteEntrega: "+webServiceInfo.get("IdReporteEntrega") : "El reporte de entrega fue creado correctamente "+"IdReporteEntrega: "+webServiceInfo.get("IdReporteEntrega");
			farMipres.setIdReporteEntregaMipress(webServiceInfo.get("IdReporteEntrega"));			
			farMipres.setProcesadoReporteEntrega(true);
			farMipres.setLoginUsrAct(principal.getName());
			farMipres.setFechaAltaAct(new Date());
			iFarMipresService.save(farMipres);
			status.setComplete();
			flash.addFlashAttribute("success", mensajeFlash);
			return "redirect:reporteentregaambform";
			}else {
				flash.addFlashAttribute("error", webServiceInfo.get("error"));
				return "redirect:reporteentregaambform";
			}	
	}
	
	
	// Este metodo me permite cargar los datos para editar el reporte de facturacion ambulatorio
	@RequestMapping(value = "/reportefacturacionamb")
	public String crearReporteFacturacionAmb(@RequestParam(value = "id", required = false) Long id, Map<String, Object> model, RedirectAttributes flash, Principal principal) throws ParseException {
					
		FarMipres farMipres = null;
		if(id > 0) {			
			farMipres = iFarMipresService.findById(id);
			if(farMipres == null) {
				flash.addFlashAttribute("error", "El ID del empleado no existe en la base de datos");
				return "redirect:/pendientesmipresamb";
			}
		}else {
			flash.addFlashAttribute("error", "El ID del empleado no puede ser 0");
			return "redirect:/pendientesmipresamb";
		}				
				
		farMipres.setCuotaModeradora("0");
		farMipres.setCopago("0");
		model.put("titulo", utf8(this.tituloreportefacturacion));
		model.put("farMipres", farMipres);
		model.put("farmacia", enlaceprincipalfarmacia);
		model.put("enlace10", enlace10);
		return "reportefacturacionamb";
	}
	
	// Este metodo me permite guardar el reporte de facturacion ambulatorio
	@RequestMapping(value = "/reportefacturacionamb", method = RequestMethod.POST)
	public String guardarReporteFacturacionAmb(@Valid FarMipres farMipres, @RequestParam(value = "cdispensacion", required = false) String cdispensacion, BindingResult result, Model model, RedirectAttributes flash, SessionStatus status, Principal principal) throws Exception {
		if(result.hasErrors()) {
			model.addAttribute("titulo", utf8(this.tituloreporteentrega));
			model.addAttribute("farMipres", farMipres);			
			model.addAttribute("farmacia", enlaceprincipalfarmacia);
			model.addAttribute("enlace10", enlace10);
			return "reportefacturacionamb";
		}
				
		if(farMipres.getNitEps().isEmpty()) {			
			model.addAttribute("error", "El nit es requerido");
			model.addAttribute("farmacia", enlaceprincipalfarmacia);
			model.addAttribute("enlace10", enlace10);
			return "reportefacturacionamb";
		}		
		if(cdispensacion.isEmpty()) {			
			model.addAttribute("error", "La cantidad de procedimientos es requerido");
			model.addAttribute("farmacia", enlaceprincipalfarmacia);
			model.addAttribute("enlace10", enlace10);
			return "reportefacturacionamb";
		}
		if(farMipres.getValorUnitario().isEmpty()) {			
			model.addAttribute("error", "El valor unitario es requerido");
			model.addAttribute("farmacia", enlaceprincipalfarmacia);
			model.addAttribute("enlace10", enlace10);
			return "reportefacturacionamb";
		}
		if(farMipres.getValorTotal().isEmpty()) {			
			model.addAttribute("error", "El valor total es requerido");
			model.addAttribute("farmacia", enlaceprincipalfarmacia);
			model.addAttribute("enlace10", enlace10);
			return "reportefacturacionamb";
		}
		if(farMipres.getCuotaModeradora().isEmpty()) {			
			model.addAttribute("error", "La cuota moderadora es requerida");
			model.addAttribute("farmacia", enlaceprincipalfarmacia);
			model.addAttribute("enlace10", enlace10);
			return "reportefacturacionamb";
		}
		if(farMipres.getCopago().isEmpty()) {			
			model.addAttribute("error", "El copago es requerido");
			model.addAttribute("farmacia", enlaceprincipalfarmacia);
			model.addAttribute("enlace10", enlace10);
			return "reportefacturacionamb";
		}
				
		Map<String, String> webServiceInfo =  guardarWebServiceMipresReporteFacturacion(farMipres, cdispensacion);
			
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
			return "redirect:pendientesmipresamb";
		}else {
			flash.addFlashAttribute("error", webServiceInfo.get("error"));
			return "redirect:pendientesmipresamb";
		}	
	}	
	

	// Este metodo me permite cargar los datos para editar la entrega hospitalario y guardar
	@RequestMapping(value = "/entregahospform")
	public String crearEntregaHosp(@RequestParam(value = "id", required = false) Long id, Map<String, Object> model, RedirectAttributes flash, Principal principal) throws ParseException {
		
		FarMipres farMipres = null;
		if(id > 0) {			
			farMipres = iFarMipresService.findById(id);
			if(farMipres == null) {
				flash.addFlashAttribute("error", "El ID del reporte no existe en la base de datos");
				return "redirect:/pendientesmipreshosp";
			}
		}else {
			flash.addFlashAttribute("error", "El ID del reporte no puede ser 0");
			return "redirect:/pendientesmipreshosp";
		}
				
		model.put("titulo", utf8(this.tituloentrega));
		model.put("farMipres", farMipres);
		model.put("tecnologia", iComTipoTecnologiaService.findAll());
		model.put("tipodocumento", iComTipoDocumentoMipresService.findAll());
		model.put("farmacia", enlaceprincipalfarmacia);
		model.put("enlace10", enlace10);
		return "entregahospform";
	}	
	
	// Este metodo me permite guardar la entrega hospitalario
	@RequestMapping(value = "/entregahospform", method = RequestMethod.POST)
	public String guardarEntregaHosp(@Valid FarMipres farMipres, BindingResult result, Model model, RedirectAttributes flash, SessionStatus status, Principal principal) throws Exception {
		if(result.hasErrors()) {
			model.addAttribute("titulo", utf8(this.tituloentrega));
			model.addAttribute("farMipres", farMipres);
			model.addAttribute("tecnologia", iComTipoTecnologiaService.findAll());
			model.addAttribute("tipodocumento", iComTipoDocumentoMipresService.findAll());
			model.addAttribute("farmacia", enlaceprincipalfarmacia);
			model.addAttribute("enlace10", enlace10);
			return "entregahospform";
		}		
		
		Map<String, String> webServiceInfo =  guardarWebServiceMipresEntregaHosp(farMipres);
		
		
		if(StringUtils.equals(webServiceInfo.get("success"), "200")) {
			
			//sincronizo paciente de dinamica a solution
			GenPacien genPacien = iGenPacienService.findByNumberDoc(farMipres.getGenPacien().getPacNumDoc());		
			GenPacien obtengoPaciente = pacienteDinamica.SincronizarPaciente(genPacien, farMipres.getGenPacien().getPacNumDoc());
			
			String mensajeFlash = (farMipres.getId() != null) ? "La entrega fue editada correctamente"+"IdEntrega: "+webServiceInfo.get("IdEntrega") : "La entrega fue creada correctamente "+"IdEntrega: "+webServiceInfo.get("IdEntrega");
			farMipres.setIdTraza(webServiceInfo.get("Id"));
			farMipres.setIdEntregaMipress(webServiceInfo.get("IdEntrega"));
			farMipres.setProcesadoEntrega(true);
			farMipres.setLoginUsrAlta(principal.getName());
			farMipres.setFechaAltaAct(new Date());
			if(genPacien == null) {
				farMipres.setGenPacien(obtengoPaciente);
			}else {
				farMipres.setGenPacien(genPacien);
			}			
			iFarMipresService.save(farMipres);
			status.setComplete();
			flash.addFlashAttribute("success", mensajeFlash);
			return "redirect:pendientesmipreshosp";
		}else {
			flash.addFlashAttribute("error", webServiceInfo.get("error"));
			return "redirect:pendientesmipreshosp";
		}		
		
	}
	
	
	// Este metodo me permite cargar los datos para editar el reporte de entrega hospitalario y guardar
	@RequestMapping(value = "/reporteentregahospform")
	public String crearReporteEntregaHosp(@RequestParam(value = "id", required = false) Long id, Map<String, Object> model, RedirectAttributes flash, Principal principal) throws ParseException {
			
		FarMipres farMipres = null;
		if(id > 0) {			
			farMipres = iFarMipresService.findById(id);
			if(farMipres == null) {
				flash.addFlashAttribute("error", "El ID del reporte no existe en la base de datos");
				return "redirect:/pendientesmipreshosp";
			}
		}else {
			flash.addFlashAttribute("error", "El ID del reporte no puede ser 0");
			return "redirect:/pendientesmipreshosp";
		}				
			
		model.put("titulo", utf8(this.tituloreporteentrega));
		model.put("farMipres", farMipres);
		model.put("farmacia", enlaceprincipalfarmacia);
		model.put("enlace10", enlace10);
		return "reporteentregahospform";
	}
	
	
	// Este metodo me permite guardar el reporte de entrega hospitalario
	@RequestMapping(value = "/reporteentregahospform", method = RequestMethod.POST)
	public String guardarReporteEntrega(@Valid FarMipres farMipres, @RequestParam(value = "estadoentrega", required = false) Integer estadoentrega, BindingResult result, Model model, RedirectAttributes flash, SessionStatus status, Principal principal) throws Exception {
		if(result.hasErrors()) {
			model.addAttribute("titulo", utf8(this.tituloreporteentrega));
			model.addAttribute("farMipres", farMipres);			
			model.addAttribute("farmacia", enlaceprincipalfarmacia);
			model.addAttribute("enlace10", enlace10);
			return "reporteentregahospform";
		}
		
		if(estadoentrega == null) {			
			model.addAttribute("error", "El estado de entrega es requerido");
			return "reporteentregahospform";
		}
		
		if(farMipres.getValorEntregado().isEmpty()) {			
			model.addAttribute("error", "El valor es requerido");
			return "reporteentregahospform";
		}
		
		if(farMipres.getNumeroFactura().isEmpty()) {			
			model.addAttribute("error", "La factura es requerida");
			return "reporteentregahospform";
		}
		
		
		Map<String, String> webServiceInfo =  guardarWebServiceMipresReporteEntrega(farMipres, estadoentrega);
		
		if(StringUtils.equals(webServiceInfo.get("success"), "200")) {
					
					String mensajeFlash = (farMipres.getId() != null) ? "El reporte de entrega fue creado correctamente "+"IdReporteEntrega: "+webServiceInfo.get("IdReporteEntrega") : "El reporte de entrega fue creado correctamente "+"IdReporteEntrega: "+webServiceInfo.get("IdReporteEntrega");
					farMipres.setIdReporteEntregaMipress(webServiceInfo.get("IdReporteEntrega"));			
					farMipres.setProcesadoReporteEntrega(true);
					farMipres.setLoginUsrAct(principal.getName());
					farMipres.setFechaAltaAct(new Date());
					iFarMipresService.save(farMipres);
					status.setComplete();
					flash.addFlashAttribute("success", mensajeFlash);
					return "redirect:pendientesmipreshosp";
				}else {
					flash.addFlashAttribute("error", webServiceInfo.get("error"));
					return "redirect:pendientesmipreshosp";
				}	
	}
	
	// Este metodo me permite cargar los datos para editar el reporte de facturacion hospitalario
	@RequestMapping(value = "/reportefacturacionhosp")
	public String crearReporteFacturacionHosp(@RequestParam(value = "id", required = false) Long id, Map<String, Object> model, RedirectAttributes flash, Principal principal) throws ParseException {
				
		FarMipres farMipres = null;
		if(id > 0) {			
			farMipres = iFarMipresService.findById(id);
			if(farMipres == null) {
				flash.addFlashAttribute("error", "El ID del empleado no existe en la base de datos");
				return "redirect:/pendientesmipreshosp";
			}
		}else {
			flash.addFlashAttribute("error", "El ID del empleado no puede ser 0");
			return "redirect:/pendientesmipreshosp";
		}				
			
		farMipres.setCuotaModeradora("0");
		farMipres.setCopago("0");
		model.put("titulo", utf8(this.tituloreportefacturacion));
		model.put("farMipres", farMipres);
		model.put("farmacia", enlaceprincipalfarmacia);
		model.put("enlace10", enlace10);
		return "reportefacturacionhosp";
	}
	
	// Este metodo me permite guardar el reporte de facturacion hospitalario
	@RequestMapping(value = "/reportefacturacionhosp", method = RequestMethod.POST)
	public String guardarReporteFacturacionHosp(@Valid FarMipres farMipres, @RequestParam(value = "cdispensacion", required = false) String cdispensacion, BindingResult result, Model model, RedirectAttributes flash, SessionStatus status, Principal principal) throws Exception {
		if(result.hasErrors()) {
			model.addAttribute("titulo", utf8(this.tituloreporteentrega));
			model.addAttribute("farMipres", farMipres);			
			model.addAttribute("farmacia", enlaceprincipalfarmacia);
			model.addAttribute("enlace10", enlace10);
			return "reportefacturacionhosp";
		}
			
		if(farMipres.getNitEps().isEmpty()) {			
			model.addAttribute("error", "El nit es requerido");
			model.addAttribute("farmacia", enlaceprincipalfarmacia);
			model.addAttribute("enlace10", enlace10);
			return "reportefacturacionhosp";
		}		
		if(cdispensacion.isEmpty()) {			
			model.addAttribute("error", "La cantidad de procedimientos es requerido");
			model.addAttribute("farmacia", enlaceprincipalfarmacia);
			model.addAttribute("enlace10", enlace10);
			return "reportefacturacionhosp";
		}
		if(farMipres.getValorUnitario().isEmpty()) {			
			model.addAttribute("error", "El valor unitario es requerido");
			model.addAttribute("farmacia", enlaceprincipalfarmacia);
			model.addAttribute("enlace10", enlace10);
			return "reportefacturacionhosp";
		}
		if(farMipres.getValorTotal().isEmpty()) {			
			model.addAttribute("error", "El valor total es requerido");
			model.addAttribute("farmacia", enlaceprincipalfarmacia);
			model.addAttribute("enlace10", enlace10);
			return "reportefacturacionhosp";
		}
		if(farMipres.getCuotaModeradora().isEmpty()) {			
			model.addAttribute("error", "La cuota moderadora es requerida");
			model.addAttribute("farmacia", enlaceprincipalfarmacia);
			model.addAttribute("enlace10", enlace10);
			return "reportefacturacionhosp";
		}
		if(farMipres.getCopago().isEmpty()) {			
			model.addAttribute("error", "El copago es requerido");
			model.addAttribute("farmacia", enlaceprincipalfarmacia);
			model.addAttribute("enlace10", enlace10);
			return "reportefacturacionhosp";
		}
			
		Map<String, String> webServiceInfo =  guardarWebServiceMipresReporteFacturacion(farMipres, cdispensacion);
			
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
					return "redirect:pendientesmipreshosp";
				}else {
					flash.addFlashAttribute("error", webServiceInfo.get("error"));
					return "redirect:pendientesmipreshosp";
				}	
	}
	

	// Este metodo me permite visualizar o cargar el formulario para sincronizar las prescripciones hospitalario
	@GetMapping("/sincronizaprescripcionhospform")
	public String crearSincronizacionHosp(Map<String, Object> model, RedirectAttributes flash) {		
		FarMipres farMipres =  new FarMipres();
		model.put("titulo", utf8(this.titulosincroniza));
		model.put("farmacia", enlaceprincipalfarmacia);
		model.put("farMipres", farMipres);		
		model.put("enlace10", enlace10);		
		return "sincronizaprescripcionhospform";
	}
	
	
	// Este metodo me permite guardar e sincronizar las prescripciones hospitalario por fecha que no estan en solution obtenidas del web service mipres
	@RequestMapping(value = "/sincronizaprescripcionporfecha")
	public String guardarSincronizacionPorFechaHosp(@RequestParam(value = "fechaInicial", required = false) String fechaInicial, @RequestParam(value = "fechaFinal", required = false) String fechaFinal, Map<String, Object> model, RedirectAttributes flash, SessionStatus status, Principal principal) throws ParseException, IOException {
		if(!fechaInicial.isEmpty() && !fechaFinal.isEmpty()) {
			sincronizoPrescripcion(fechaInicial, fechaFinal, principal);
		}else {
			flash.addFlashAttribute("error", "Las fechas son requeridas");
			return "redirect:sincronizaprescripcionhospform";
		}				
		model.put("titulo", utf8(this.titulosincroniza));				
		model.put("enlace10", enlace10);		
		flash.addFlashAttribute("success", "Sincronizacion correcta");		
		status.setComplete();
		return "redirect:sincronizaprescripcionhospform";
	}
	
	// Este metodo me permite guardar e sincronizar la prescripcion hospitalario por numero, que no estan en solution obtenidas del web service mipres
	@RequestMapping(value = "/sincronizaprescripcionhosppornumero")
	public String guardarSincronizacionPorNumeroDePrescripcionHosp(@RequestParam(value = "numeroPrescripcion", required = false) String numeroPrescripcion, Map<String, Object> model, RedirectAttributes flash, SessionStatus status, Principal principal) throws ParseException, IOException {
		if(!numeroPrescripcion.isEmpty() ) {
			String mensaje = sincronizoPrescripcionHosp(numeroPrescripcion, principal, flash);
			if(mensaje == "success") {
				flash.addFlashAttribute("success", "Sincronizacion correcta");
			}else if(mensaje == "error") {
				flash.addFlashAttribute("error", "Prescripción no encontrada");
			}else {
				flash.addFlashAttribute("error", "Prescripción ya existe");
			}
			
		}else {
			flash.addFlashAttribute("error", "El número de prescipción es requerida");
			return "redirect:sincronizaprescripcionhospform";
		}				
		model.put("titulo", utf8(this.titulosincroniza));				
		model.put("enlace10", enlace10);			
		status.setComplete();
		return "redirect:sincronizaprescripcionhospform";
	}
	
	// Este metodo me permite visualizar o cargar el formulario para sincronizar las prescripciones ambulatoria 
	@GetMapping("/sincronizaprescripcionambform")
	public String crearSincronizacion(Map<String, Object> model, RedirectAttributes flash) {		
		FarMipres farMipres =  new FarMipres();
		model.put("titulo", utf8(this.titulosincroniza));
		model.put("farmacia", enlaceprincipalfarmacia);
		model.put("farMipres", farMipres);		
		model.put("enlace10", enlace10);		
		return "sincronizaprescripcionambform";
	}
	
	// Este metodo me permite guardar e sincronizar la prescripcion ambulatorio por numero, que no estan en solution obtenidas del web service mipres
	@RequestMapping(value = "/sincronizaprescripcionambpornumero")
	public String guardarSincronizacionPorNumeroDePrescripcionAmb(@RequestParam(value = "numeroPrescripcion", required = false) String numeroPrescripcion, Map<String, Object> model, RedirectAttributes flash, SessionStatus status, Principal principal) throws ParseException, IOException {
		if(!numeroPrescripcion.isEmpty() ) {
			String mensaje = sincronizoPrescripcionAmb(numeroPrescripcion, principal, flash);
			if(mensaje == "success") {
				flash.addFlashAttribute("success", "Sincronizacion correcta");
			}else if(mensaje == "error") {
					flash.addFlashAttribute("error", "Prescripción no encontrada");
			}else {
				flash.addFlashAttribute("error", "Prescripción ya existe");
			}
				
		}else {
			flash.addFlashAttribute("error", "El número de prescipción es requerida");
			return "redirect:sincronizaprescripcionhospform";
		}				
		model.put("titulo", utf8(this.titulosincroniza));				
		model.put("enlace10", enlace10);			
		status.setComplete();
		return "redirect:sincronizaprescripcionambform";
		}
	
	
	// Este metodo me permite visualizar o cargar el formulario para generar el excel para rips
	@GetMapping("/ripsmipres")
	public String crearconsolidadomortalidad(Map<String, Object> model) {		
		model.put("titulo", utf8(this.reportemipres));		
		model.put("farmacia", enlaceprincipalfarmacia);
		model.put("enlace10", enlace10);
		return "ripsmipres";	
			
	}
	
	
	// Este metodo me permite generar el consolidad de mortalidades
	@RequestMapping("/generarreporterips")
	public String generarconsolidadomortalidad(Model model, @RequestParam(value = "fechaInicial", required = false) String fechaInicial, @RequestParam(value = "fechaFinal", required = false) String fechaFinal, RedirectAttributes flash, HttpServletResponse response) throws ParseException {
			
		String errorFechas = "";
			
		// valida si la fecha inicial y la fecha final no estan vacios
		if (fechaInicial.equals("") && fechaFinal.equals("")) {
			errorFechas = "Debes establecer una fecha inicial y fecha final";
			model.addAttribute("error", errorFechas);
			model.addAttribute("farmacia", enlaceprincipalfarmacia);
			model.addAttribute("enlace10", enlace10);
			return "ripsmipres";
		}

		// valida si la fecha inicial y la fecha final no estan vacios
		if (fechaInicial.equals("") || fechaFinal.equals("")) {
			errorFechas = "Debes establecer una fecha inicial y fecha final";
			model.addAttribute("error", errorFechas);
			model.addAttribute("farmacia", enlaceprincipalfarmacia);
			model.addAttribute("enlace10", enlace10);
			return "ripsmipres";
		}
			
		// consulta por la fecha inicial y la fecha final
		if (!fechaInicial.equals("") && !fechaFinal.equals("")) {
			Date fechaI = convertirFecha(fechaInicial);
			Date fechaF = convertirFecha(fechaFinal);
				
			List<FarMipres> listadoMipres = iFarMipresService.findByStartDateBetween(fechaI, fechaF);
			
			if(!listadoMipres.isEmpty()) {
				//creamos el reporte en EXCEL
				crearExcel(listadoMipres, response);							
											
				model.addAttribute("farmacia", enlaceprincipalfarmacia);
				model.addAttribute("enlace10", enlace10);	
			}else {
				model.addAttribute("error", "No hay información disponible para este rango de fechas");
				return "ripsmipres";
			}									
		}					
		return null;			
	}
	
	// Este metodo me permite visualizar o cargar el formulario para anular el reporte de facturacion, el reporte de entrega y la entrega
	@GetMapping("/anulaprescripcionhospform")
	public String anularSincronizacion(Map<String, Object> model, RedirectAttributes flash) {		
		
		FarMipres farMipres =  new FarMipres();		
		boolean listado = false;
		
		model.put("titulo", utf8(this.anularmipres));
		model.put("farMipres", farMipres);		
		model.put("enlace10", enlace10);
		model.put("farmacia", enlaceprincipalfarmacia);
		model.put("listado", listado);		
		return "anulaprescripcionhospform";
	}
	
	// Este metodo me permite consultar cual proceso se puede anular para la prescripcion por ID
	@RequestMapping("/procesaranulacion")	
	public String procesarAnulacion(Model model, @RequestParam(value = "idprescripcion", required = false) String idprescripcion, RedirectAttributes flash, HttpServletResponse response) throws ParseException, JRException, IOException {
		
		boolean listado = false;
			
		// valida el ID de la prescripcion que no este vacia
		if (idprescripcion.isEmpty()) {		
			model.addAttribute("titulo", utf8(this.anularmipres));
			model.addAttribute("farmacia", enlaceprincipalfarmacia);
			model.addAttribute("enlace10", enlace10);
			model.addAttribute("error", "El número de prescripción es requerida");
			return "anulaprescripcionform";
		}		
		
		try {
			Long ID = Long.parseLong(idprescripcion);
			List<FarMipres> prescripcionObtenida = iFarMipresService.findByIdMipres(ID);
			listado = true;
			model.addAttribute("titulo", utf8(this.anularmipres));
			model.addAttribute("farmacia", enlaceprincipalfarmacia);
			model.addAttribute("enlace10", enlace10);
			model.addAttribute("prescipcion", prescripcionObtenida);
			model.addAttribute("listado", listado);
		} catch (NumberFormatException ex) {
			model.addAttribute("error", "ID no valido");
		}		
		return  "anulaprescripcionform";			
	}
	
	// Este metodo me permite anular la entrega
	@RequestMapping(value = "/anularentrega/{id}/{condicion}")
	public String anularEntrega(@PathVariable(value = "id") Long id, @PathVariable(value = "condicion") String condicion, SessionStatus status, RedirectAttributes flash, Principal principal) throws Exception {
		
		if (id > 0) {			
			FarMipres farMipres = iFarMipresService.findById(id);			

			Map<String, String> webServiceInfo = anulacionPrescripcion(farMipres, condicion);

			if (StringUtils.equals(webServiceInfo.get("success"), "200") && webServiceInfo.get("condicion").equals("entrega")) {				
				
				farMipres.setIdTraza(null);
				farMipres.setIdEntregaMipress(null);
				farMipres.setProcesadoEntrega(false);
				farMipres.setCodigoServicio("SIN INFORMACION");
				farMipres.setLoginUsrAct(principal.getName());
				farMipres.setFechaAltaAct(new Date());
				
				iFarMipresService.save(farMipres);
				status.setComplete();
				flash.addFlashAttribute("success", "La entrega fue anulada correctamente");
				return "redirect:/anulaprescripcionform";
			}

			if (StringUtils.equals(webServiceInfo.get("success"), "200") && webServiceInfo.get("condicion").equals("reporteentrega")) {				
				
				
				farMipres.setIdReporteEntregaMipress(null);	
				farMipres.setNumeroFactura(null);
				farMipres.setValorEntregado(null);
				farMipres.setProcesadoReporteEntrega(false);
				farMipres.setLoginUsrAct(principal.getName());
				farMipres.setFechaAltaAct(new Date());
				
				iFarMipresService.save(farMipres);
				status.setComplete();
				flash.addFlashAttribute("success", "El reporte de entrega fue anulada correctamente");
				return "redirect:/anulaprescripcionform";
			}
			
			if (StringUtils.equals(webServiceInfo.get("success"), "200") && webServiceInfo.get("condicion").equals("reportefacturacion")) {				
				
				farMipres.setIdReporteFacturacionMipress(null);
				farMipres.setIdFacturacionMipress(null);
				farMipres.setCopago(null);
				farMipres.setCuotaModeradora(null);
				farMipres.setValorTotal(null);
				farMipres.setValorUnitario(null);
				farMipres.setProcesadoFacturacion(false);
				farMipres.setLoginUsrAct(principal.getName());
				farMipres.setFechaAltaAct(new Date());
				
				iFarMipresService.save(farMipres);
				status.setComplete();
				flash.addFlashAttribute("success", "El reporte de facturación fue anulada correctamente");
				return "redirect:/anulaprescripcionform";
			}
			
			else {
				flash.addFlashAttribute("error", webServiceInfo.get("error"));
				return "redirect:/anulaprescripcionform";
			}

		}
		return "redirect:/anulaprescripcionform";
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
	
	
	//Se usa para hacer put en el web service de mipres PROGRAMACION
	private Map<String, String> guardarWebServiceMipresProgramacion(FarMipres farMipres) throws Exception {
			
		Map<String, String> map = new HashMap<>();
			
		//obtengo los datos del token secundario guardados en solution
		ComTokenMipres comTokenMipres = iComTokenMipresService.findById(1L); 
			
		//genero la url para consultar
		String urlEncadenada = MetodoPutProgramacion+comTokenMipres.getNit()+'/'+comTokenMipres.getTokenSecundario();
				
		//convierto la fecha de entrega en string y formato YYYY-MM-DD
		String fechaMaxEntrega = formatoFecha(farMipres.getFechaMaxEntrega());		
			
	    //Especificamos la URL y configuro el objeto CloseableHttpClient
		CloseableHttpClient httpclient = HttpClients.createDefault();
			
		//Se crea una solicitud PUT (si es post HttpPost y si es get HttpGet) y pasamos el URL del recurso y también asigne encabezados a este objeto de colocación
		HttpPut httpPut = new HttpPut(urlEncadenada);
	    httpPut.setHeader("Accept", "application/json");
	    httpPut.setHeader("Content-type", "application/json");
	        
	    //creo el json que sera pasado al objeto StringEntity
	    JSONObject parametros = new JSONObject();
	    parametros.put("ID", farMipres.getIdDireccionamientoTrazaMipress());
	    parametros.put("FecMaxEnt", fechaMaxEntrega);
	    parametros.put("TipoIDSedeProv", "NI"); //TIPO DE IDENTIFICACION PROVEEDOR 
	    parametros.put("NoIDSedeProv", "891580002"); //NUMERO DE IDENTIFICACION PROVEEDOR
	    parametros.put("CodSedeProv", "190010003101"); //CODIGO HABILITACION
	    parametros.put("CodSerTecAEntregar", farMipres.getCodigoServicio());
	    parametros.put("CantTotAEntregar", farMipres.getCantidadEntregada());	    
	   
			
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
	        map.put("IdProgramacion", jsonContent.get("IdProgramacion").toString());
	        return map;
	        	
	    }else if (response.getStatusLine().getStatusCode() == 422) {        	
	    	map.put("error", jsonContent.get("Message").toString() +",  "+ jsonContent.get("Errors").toString().replaceAll("[^ a-zA-Záéíóú]", ""));
	        return map;
	    }else {
	    	map.put("error", jsonContent.get("Message").toString());
	        return map;
	    }
	}
	
	
	
	
	//Se usa para hacer put en el web service de mipres ENTREGA HOSPITALARIO
	private Map<String, String> guardarWebServiceMipresEntregaHosp(FarMipres farMipres) throws Exception {
		
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
	
	
	//Se usa para hacer put en el web service de mipres ENTREGA AMBULATORIO
	private Map<String, String> guardarWebServiceMipresEntregaAmb(FarMipres farMipres) throws Exception {
			
		Map<String, String> map = new HashMap<>();
			
		//obtengo los datos del token secundario guardados en solution
		ComTokenMipres comTokenMipres = iComTokenMipresService.findById(1L); 
			
		//genero la url para consultar
		String urlEncadenada = MetodoPutEntregaAmbulatorio+comTokenMipres.getNit()+'/'+comTokenMipres.getTokenSecundario();
					
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
	    parametros.put("ID", farMipres.getIdDireccionamientoTrazaMipress());
	    parametros.put("CodSerTecEntregado", farMipres.getCodigoServicio()); 
	    parametros.put("CantTotEntregada", farMipres.getCantidadEntregada());
	    parametros.put("EntTotal", farMipres.getEntregaTotal());
	    parametros.put("CausaNoEntrega", farMipres.getCausaNoEntrega());
	    parametros.put("FecEntrega", fechaEntrega);
	    parametros.put("NoLote", farMipres.getLoteEntregado());
	    parametros.put("TipoIDRecibe", farMipres.getComTipoDocumentoMipres().getTipo());
	    parametros.put("NoIDRecibe", farMipres.getGenPacien().getPacNumDoc());    
	          
			
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
	private Map<String, String> guardarWebServiceMipresReporteEntrega(FarMipres farMipres, Integer estadoentrega) throws IOException {
		
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
		parametros.put("ValorEntregado", farMipres.getValorEntregado());
		
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
	private Map<String, String> guardarWebServiceMipresReporteFacturacion(FarMipres farMipres, String cdispensacion) throws IOException {
		
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
		parametros.put("NoEntrega", farMipres.getNumeroEntrega());
		parametros.put("NoFactura", farMipres.getNumeroFactura());
		parametros.put("NoIDEPS", farMipres.getNitEps());
		parametros.put("CodEPS", farMipres.getCodEps());
		parametros.put("CodSerTecAEntregado", farMipres.getCodigoServicio());
		parametros.put("CantUnMinDis", cdispensacion);
		parametros.put("ValorUnitFacturado", farMipres.getValorUnitario());
		parametros.put("ValorTotFacturado", farMipres.getValorTotal());
		parametros.put("CuotaModer", farMipres.getCuotaModeradora());
		parametros.put("Copago", farMipres.getCopago());
				
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
	
	//Se usa para hacer get en el web service de mipres y sincronizar por rango de fechas
	private void sincronizoPrescripcion(String fechaInicial, String fechaFinal, Principal principal) throws ParseException, IOException, IOException {
				
		//convierto String a Date 
		Date fInicialDate = convertirFecha(fechaInicial);
		Date fFinalDate = convertirFecha(fechaFinal);
		
		//convierto Date a LocalDate
		LocalDate fInicialLocalDate = fInicialDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		LocalDate fFinalLocalDate = fFinalDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		
		//Me sirve para guardar los tipos de tecnologias
		Map<String, Object> map = new HashMap<>();
		
		
		for (LocalDate fecha = fInicialLocalDate; !fecha.isAfter(fFinalLocalDate); fecha = fecha.plusDays(1)) {
	        
			//obtengo los datos del token primario guardados en solution
			ComTokenMipres comTokenMipres = iComTokenMipresService.findById(2L);
			
			//genero la url para consultar
			String urlEncadenada = MetodoGetConsultaFecha+comTokenMipres.getNit()+'/'+fecha+'/'+comTokenMipres.getTokenPrincipal();
			
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
            			map.put("nombreMedicamento"+m, arrayMedicamentos.getJSONObject(m).getString("DescMedPrinAct"));
	                    map.put("tipoTecnologia"+m, "M");	
	                    map.put("consecutivoTecnologia"+m, arrayMedicamentos.getJSONObject(m).getInt("ConOrden"));
	            	}
            	} 
            	
            	//contamos los procedimientos dados
            	JSONArray arrayProcedimientos = objetoJSON.getJSONArray("procedimientos");            	
            	if(arrayProcedimientos.length() >= 1) {
            		
            		for(int p=0; p < arrayProcedimientos.length(); p++) {           			
            			map.put("cantidadEntregada"+p, arrayProcedimientos.getJSONObject(p).getString("CantTotal"));
	                    map.put("tipoTecnologia"+p, "P");
	                    map.put("consecutivoTecnologia"+p, arrayProcedimientos.getJSONObject(p).getInt("ConOrden"));
	            	}
            	}
            	
            	//contamos los productos nutricionales dados
            	JSONArray arrayProductosNutricionales = objetoJSON.getJSONArray("productosnutricionales");            	
            	if(arrayProductosNutricionales.length() >= 1) {
            		
            		for(int n=0; n < arrayProductosNutricionales.length(); n++) {            			
            			map.put("cantidadEntregada"+n, arrayProductosNutricionales.getJSONObject(n).getString("CantTotalF"));
	                    map.put("tipoTecnologia"+n, "N");
	                    map.put("consecutivoTecnologia"+n, arrayProductosNutricionales.getJSONObject(n).getInt("ConOrden"));
	            	}           		
            	}
            	
            	//contamos los servicios complementarios dados
            	JSONArray arrayServicioComplementario = objetoJSON.getJSONArray("serviciosComplementarios");            	
            	if(arrayServicioComplementario.length() >= 1) {
            		
            		for(int c=0; c < arrayServicioComplementario.length(); c++) {           			
            			map.put("cantidadEntregada"+c, arrayServicioComplementario.getJSONObject(c).getString("CantTotal"));
	                    map.put("tipoTecnologia"+c, "S");
	                    map.put("consecutivoTecnologia"+c, arrayServicioComplementario.getJSONObject(c).getInt("ConOrden"));
	            	}          		
            	}        
                
            	//este if es cuando es solo una sola tecnologia
            	if(map.size() == 3 || map.size() == 4) {
            		
            		FarMipres buscarSolution = iFarMipresService.findByDocumentoPrescripcionConsecutivoTecnologiaCantidad(numDocumento, prescripcion, map.get("cantidadEntregada0").toString(), (Integer) map.get("consecutivoTecnologia0"));
                    
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
                    	ComTipoTecnologia comTipoTecnologia = iComTipoTecnologiaService.tipoTecnologia(map.get("tipoTecnologia0").toString());  
                    	
                    	//buscamos el ambito para guardar                	  
                    	ComAmbito comAmbito = iComAmbitoService.ambitoCodigo(codigoAmbitoAtencion.toString());
                    	
                    	//validamos si el medicamento es vacio
                    	if(map.containsKey("nombreMedicamento0")) {
                    		farMipres.setNombreMedicamento(map.get("nombreMedicamento0").toString());
                    	}else {
                    		farMipres.setNombreMedicamento("SIN INFORMACION");
                    	}
                    	
                    	farMipres.setNumeroPrescripcion(prescripcion);
                        farMipres.setCantidadEntregada(map.get("cantidadEntregada0").toString());
                        farMipres.setProcesadoEntrega(false);
                        farMipres.setProcesadoReporteEntrega(false);
                        farMipres.setProcesadoFacturacion(false);                    
                        farMipres.setComTipoDocumentoMipres(comTipoDocumentoMipres);                    
                        farMipres.setFechaEntrega(fechaEntregaCustomDate);
                        farMipres.setConsecutivoTecnologia(Integer.parseInt(map.get("consecutivoTecnologia0").toString()));
                        farMipres.setComTipoTecnologia(comTipoTecnologia);
                        farMipres.setComAmbito(comAmbito);
                        farMipres.setCodigoServicio("SIN INFORMACION");
                        farMipres.setNumeroEntrega(1);
                        farMipres.setEntregaTotal(1);
                        farMipres.setCausaNoEntrega(0);
                        farMipres.setLoginUsrAlta(principal.getName());
                        farMipres.setCodEps(codEps);                
                        
                    	iFarMipresService.save(farMipres);            	
                    }   
                //este else es cuando son varias tecnologias
            	}else {
            		if(map.size() == 6 || map.size() == 8) {
            			int hasta = 2;
            			procesarGuardarHosp(hasta, map, numDocumento, prescripcion, tipoDocumento, primerNombre, segundoNombre, primerApellido, segundoApellido, fechaEntregaCustomDate, principal.getName(), codEps, codigoAmbitoAtencion);
            		}
            		if(map.size() == 9 || map.size() == 12) {
            			int hasta = 3;
            			procesarGuardarHosp(hasta, map, numDocumento, prescripcion, tipoDocumento, primerNombre, segundoNombre, primerApellido, segundoApellido, fechaEntregaCustomDate, principal.getName(), codEps, codigoAmbitoAtencion);
            		}
            	}
            }	
            	 	
            }//fin for
	    }	
	}	

	//Se usa para gurdar la prescripcion en la la base de solution
	private void procesarGuardarHosp(int hasta, Map<String, Object> map, String numDocumento, String prescripcion, String tipoDocumento, String primerNombre, String segundoNombre, String primerApellido,
			String segundoApellido, Date fechaEntregaCustomDate, String usuario, String codEps, Integer codigoAmbitoAtencion) {
		for(int z=0; z<hasta; z++) {
			
			String cantidadEntregada = map.get("cantidadEntregada"+z).toString();                    	
        	String tipoTecnologia = map.get("tipoTecnologia"+z).toString();
        	Integer consecutivoTecnologia = Integer.parseInt(map.get("consecutivoTecnologia"+z).toString());
			
			FarMipres buscarSolution = iFarMipresService.findByDocumentoPrescripcionConsecutivoTecnologiaCantidad(numDocumento, prescripcion, cantidadEntregada, consecutivoTecnologia);
            
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
        	
        	//buscamos el ambito para guardar                	  
        	ComAmbito comAmbito = iComAmbitoService.ambitoCodigo(codigoAmbitoAtencion.toString());
        	
        	//validamos si el medicamento es vacio
        	if(map.containsKey("nombreMedicamento"+z)) {
        		farMipres.setNombreMedicamento(map.get("nombreMedicamento"+z).toString());
        	}else {
        		farMipres.setNombreMedicamento("SIN INFORMACION");
        	}
        	
        	farMipres.setNumeroPrescripcion(prescripcion);
            farMipres.setCantidadEntregada(cantidadEntregada);
            farMipres.setProcesadoEntrega(false);
            farMipres.setProcesadoReporteEntrega(false);
            farMipres.setProcesadoFacturacion(false);                    
            farMipres.setComTipoDocumentoMipres(comTipoDocumentoMipres);                    
            farMipres.setFechaEntrega(fechaEntregaCustomDate);
            farMipres.setConsecutivoTecnologia(Integer.parseInt(map.get("consecutivoTecnologia"+z).toString()));                        
            farMipres.setComTipoTecnologia(comTipoTecnologia);
            farMipres.setComAmbito(comAmbito);
            farMipres.setCodigoServicio("SIN INFORMACION");
            farMipres.setNumeroEntrega(1);
            farMipres.setEntregaTotal(0);
            farMipres.setCausaNoEntrega(0);
            farMipres.setLoginUsrAlta(usuario);
            farMipres.setCodEps(codEps);                        
        	iFarMipresService.save(farMipres);
        	
            	}
			}		
	}
	
	
	//Se usa para gurdar la prescripcion en la la base de solution
	private void procesarGuardarAmb(int hasta, Map<String, Object> map, String numDocumento, String prescripcion, String tipoDocumento, String primerNombre, String segundoNombre, String primerApellido,
			String segundoApellido, Date fechaEntregaCustomDate, String usuario, String codEps, Integer codigoAmbitoAtencion, Map<String, Object> direccionamiento) throws ParseException {
	for(int z=0; z<hasta; z++) {
				
			String cantidadEntregada = map.get("cantidadEntregada"+z).toString();                    	
	       	String tipoTecnologia = map.get("tipoTecnologia"+z).toString();
	       	Integer consecutivoTecnologia = Integer.parseInt(map.get("consecutivoTecnologia"+z).toString());
				
			FarMipres buscarSolution = iFarMipresService.findByDocumentoPrescripcionConsecutivoTecnologiaCantidad(numDocumento, prescripcion, cantidadEntregada, consecutivoTecnologia);
	            
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
	        	
	        	//buscamos el ambito para guardar                	  
	        	ComAmbito comAmbito = iComAmbitoService.ambitoCodigo(codigoAmbitoAtencion.toString());
	        	
	        	//validamos si el medicamento es vacio
	        	if(map.containsKey("nombreMedicamento"+z)) {
	        		farMipres.setNombreMedicamento(map.get("nombreMedicamento"+z).toString());
	        	}else {
	        		farMipres.setNombreMedicamento("SIN INFORMACION");
	        	}
	        	
	        	farMipres.setNumeroPrescripcion(prescripcion);
	            farMipres.setCantidadEntregada(cantidadEntregada);
	            farMipres.setProcesadoProgramacion(false);
	            farMipres.setProcesadoEntrega(false);
	            farMipres.setProcesadoReporteEntrega(false);
	            farMipres.setProcesadoFacturacion(false);                    
	            farMipres.setComTipoDocumentoMipres(comTipoDocumentoMipres);                    
	            farMipres.setFechaEntrega(fechaEntregaCustomDate);
	            farMipres.setConsecutivoTecnologia(Integer.parseInt(map.get("consecutivoTecnologia"+z).toString()));                        
	            farMipres.setComTipoTecnologia(comTipoTecnologia);
	            farMipres.setComAmbito(comAmbito);
	            farMipres.setCodigoServicio("SIN INFORMACION");
	            farMipres.setNumeroEntrega(1);
	            farMipres.setEntregaTotal(0);
	            farMipres.setCausaNoEntrega(0);
	            farMipres.setLoginUsrAlta(usuario);
	            farMipres.setCodEps(codEps);	            
	            farMipres.setIdDireccionamientoTrazaMipress(direccionamiento.get("ID").toString());
                farMipres.setIdDireccionamientoMipress(direccionamiento.get("IDDireccionamiento").toString());
                farMipres.setFechaMaxEntrega(convertirFechaParametro(direccionamiento.get("FecMaxEnt").toString()));
	        	iFarMipresService.save(farMipres);
	        	
	           	}
		}		
	}
	
	//Se usa para hacer get en el web service de mipres y sincronizar por numero de prescripcion hospitalizacion
	private String sincronizoPrescripcionHosp(String numeroPrescripcion, Principal principal, RedirectAttributes flash) throws ClientProtocolException, IOException, ParseException {
		
		String success = "success"; 
		String error = "error";
		String existe = "existe";
		
		//Me sirve para guardar los tipos de tecnologias
		Map<String, Object> map = new HashMap<>();		
		
		//obtengo los datos del token primario guardados en solution
		ComTokenMipres comTokenMipres = iComTokenMipresService.findById(2L);
		
		//genero la url para consultar
		String urlEncadenada = MetodoGetConsultaFechaNumeroPrescripcion+comTokenMipres.getNit()+'/'+comTokenMipres.getTokenPrincipal()+'/'+numeroPrescripcion;		
		
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
        			map.put("nombreMedicamento"+m, arrayMedicamentos.getJSONObject(m).getString("DescMedPrinAct"));
                    map.put("tipoTecnologia"+m, "M");	
                    map.put("consecutivoTecnologia"+m, arrayMedicamentos.getJSONObject(m).getInt("ConOrden"));
            	}
        	} 
        	
        	//contamos los procedimientos dados
        	JSONArray arrayProcedimientos = objetoJSON.getJSONArray("procedimientos");            	
        	if(arrayProcedimientos.length() >= 1) {
        		
        		for(int p=0; p < arrayProcedimientos.length(); p++) {           			
        			map.put("cantidadEntregada"+p, arrayProcedimientos.getJSONObject(p).getString("CantTotal"));
                    map.put("tipoTecnologia"+p, "P");
                    map.put("consecutivoTecnologia"+p, arrayProcedimientos.getJSONObject(p).getInt("ConOrden"));
            	}
        	}
        	
        	//contamos los productos nutricionales dados
        	JSONArray arrayProductosNutricionales = objetoJSON.getJSONArray("productosnutricionales");            	
        	if(arrayProductosNutricionales.length() >= 1) {
        		
        		for(int n=0; n < arrayProductosNutricionales.length(); n++) {            			
        			map.put("cantidadEntregada"+n, arrayProductosNutricionales.getJSONObject(n).getString("CantTotalF"));
                    map.put("tipoTecnologia"+n, "N");
                    map.put("consecutivoTecnologia"+n, arrayProductosNutricionales.getJSONObject(n).getInt("ConOrden"));
            	}           		
        	}
        	
        	//contamos los servicios complementarios dados
        	JSONArray arrayServicioComplementario = objetoJSON.getJSONArray("serviciosComplementarios");            	
        	if(arrayServicioComplementario.length() >= 1) {
        		
        		for(int c=0; c < arrayServicioComplementario.length(); c++) {           			
        			map.put("cantidadEntregada"+c, arrayServicioComplementario.getJSONObject(c).getString("CantTotal"));
                    map.put("tipoTecnologia"+c, "S");
                    map.put("consecutivoTecnologia"+c, arrayServicioComplementario.getJSONObject(c).getInt("ConOrden"));
            	}          		
        	}
        	
        	//este if es cuando es solo una sola tecnologia
        	if(map.size() == 3 || map.size() == 4) {
        		
        		FarMipres buscarSolution = iFarMipresService.findByDocumentoPrescripcionConsecutivoTecnologiaCantidad(numDocumento, prescripcion, map.get("cantidadEntregada0").toString(), (Integer) map.get("consecutivoTecnologia0"));
                
                if(buscarSolution == null) {         	
                	
                	FarMipres farMipres = new FarMipres();
                    
                	//buscamos el tipo documento paciente para guardar
                	ComTipoDocumentoMipres comTipoDocumentoMipres = iComTipoDocumentoMipresService.tipoDocumento(tipoDocumento);
                	
                	//buscamos el ambito para guardar                	  
                	ComAmbito comAmbito = iComAmbitoService.ambitoCodigo(codigoAmbitoAtencion.toString());
                	
                	//buscamos el numero de documento del paciente para guardar
                	GenPacien genPacien = iGenPacienService.findByNumberDoc(numDocumento);
                	if(genPacien == null) {
                		GenPacien obtengoPaciente =  SicronizarPacientePorDocumento(tipoDocumento, numDocumento, primerNombre, segundoNombre, primerApellido, segundoApellido);
                		farMipres.setGenPacien(obtengoPaciente);
                	}  else {
                		farMipres.setGenPacien(genPacien);
                	}
                	
                	//buscamos la tecnologia para guardar                	  
                	ComTipoTecnologia comTipoTecnologia = iComTipoTecnologiaService.tipoTecnologia(map.get("tipoTecnologia0").toString());  
                	
                	//validamos si el medicamento es vacio
                	if(map.containsKey("nombreMedicamento0")) {
                		farMipres.setNombreMedicamento(map.get("nombreMedicamento0").toString());
                	}else {
                		farMipres.setNombreMedicamento("SIN INFORMACION");
                	}
                	
                	farMipres.setNumeroPrescripcion(prescripcion);
                    farMipres.setCantidadEntregada(map.get("cantidadEntregada0").toString());
                    farMipres.setProcesadoEntrega(false);
                    farMipres.setProcesadoReporteEntrega(false);
                    farMipres.setProcesadoFacturacion(false);                    
                    farMipres.setComTipoDocumentoMipres(comTipoDocumentoMipres);                    
                    farMipres.setFechaEntrega(fechaEntregaCustomDate);
                    farMipres.setConsecutivoTecnologia(Integer.parseInt(map.get("consecutivoTecnologia0").toString()));
                    farMipres.setComTipoTecnologia(comTipoTecnologia);
                    farMipres.setComAmbito(comAmbito);
                    farMipres.setCodigoServicio("SIN INFORMACION");
                    farMipres.setNumeroEntrega(1);
                    farMipres.setEntregaTotal(1);
                    farMipres.setCausaNoEntrega(0);
                    farMipres.setLoginUsrAlta(principal.getName());
                    farMipres.setCodEps(codEps);                    
                	iFarMipresService.save(farMipres);
                	return success;                		
                }
                else {
                	return existe;
                }
            //este else es cuando son varias tecnologias
        	}else {
        		if(map.size() == 6 || map.size() == 8) {
        			int hasta = 2;
        			procesarGuardarHosp(hasta, map, numDocumento, prescripcion, tipoDocumento, primerNombre, segundoNombre, primerApellido, segundoApellido, fechaEntregaCustomDate, principal.getName(), codEps, codigoAmbitoAtencion);
        			return success;	
        		}
        		if(map.size() == 9 || map.size() == 12) {
        			int hasta = 3;
        			procesarGuardarHosp(hasta, map, numDocumento, prescripcion, tipoDocumento, primerNombre, segundoNombre, primerApellido, segundoApellido, fechaEntregaCustomDate, principal.getName(), codEps, codigoAmbitoAtencion);
        			return success;	
        		}
        	}
        
        	}
        	//si prescripcion no cumple con el codigo ambito atencion
        	else {        		
        		return error;
        	}
    	 	
        }//fin for		
        //si prescripcion esta vacia o nula
        return error;
	}
	
	
	//Se usa para hacer get en el web service de mipres y sincronizar por numero de prescripcion ambulatorio
	private String sincronizoPrescripcionAmb(String numeroPrescripcion, Principal principal, RedirectAttributes flash) throws ClientProtocolException, IOException, ParseException {
			
		String success = "success"; 
		String error = "error";
		String existe = "existe";		
			
		//Me sirve para guardar los tipos de tecnologias
		Map<String, Object> map = new HashMap<>();		
		
		//obtengo los datos del token primario guardados en solution
		ComTokenMipres comTokenMipresPrimario = iComTokenMipresService.findById(2L);
		
		//obtengo los datos del token secundario guardados en solution
		ComTokenMipres comTokenMipresSecundario = iComTokenMipresService.findById(1L);
		
		Map<String, Object> direccionamiento = obtenerDireccionamiento(numeroPrescripcion, comTokenMipresSecundario.getNit(), comTokenMipresSecundario.getTokenSecundario());
		
		//map.get("tipoTecnologia0").toString()
		if(direccionamiento.get("ID") != null) {
			//genero la url para consultar
			String urlEncadenada = MetodoGetConsultaFechaNumeroPrescripcion+comTokenMipresPrimario.getNit()+'/'+comTokenMipresPrimario.getTokenPrincipal()+'/'+numeroPrescripcion;		
				
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
	        	if(codigoAmbitoAtencion == 11 || codigoAmbitoAtencion == 12 || codigoAmbitoAtencion == 21) {            	
		        	
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
		        			map.put("nombreMedicamento"+m, arrayMedicamentos.getJSONObject(m).getString("DescMedPrinAct"));
		                    map.put("tipoTecnologia"+m, "M");	
		                    map.put("consecutivoTecnologia"+m, arrayMedicamentos.getJSONObject(m).getInt("ConOrden"));
		            	}
		        	} 
		        	
		        	//contamos los procedimientos dados
		        	JSONArray arrayProcedimientos = objetoJSON.getJSONArray("procedimientos");            	
		        	if(arrayProcedimientos.length() >= 1) {
		        		
		        		for(int p=0; p < arrayProcedimientos.length(); p++) {           			
		        			map.put("cantidadEntregada"+p, arrayProcedimientos.getJSONObject(p).getString("CantTotal"));
		                    map.put("tipoTecnologia"+p, "P");
		                    map.put("consecutivoTecnologia"+p, arrayProcedimientos.getJSONObject(p).getInt("ConOrden"));
		            	}
		        	}
		        	
		        	//contamos los productos nutricionales dados
		        	JSONArray arrayProductosNutricionales = objetoJSON.getJSONArray("productosnutricionales");            	
		        	if(arrayProductosNutricionales.length() >= 1) {
		        		
		        		for(int n=0; n < arrayProductosNutricionales.length(); n++) {            			
		        			map.put("cantidadEntregada"+n, arrayProductosNutricionales.getJSONObject(n).getString("CantTotalF"));
		                    map.put("tipoTecnologia"+n, "N");
		                    map.put("consecutivoTecnologia"+n, arrayProductosNutricionales.getJSONObject(n).getInt("ConOrden"));
		            	}           		
		        	}
		        	
		        	//contamos los servicios complementarios dados
		        	JSONArray arrayServicioComplementario = objetoJSON.getJSONArray("serviciosComplementarios");            	
		        	if(arrayServicioComplementario.length() >= 1) {
		        		
		        		for(int c=0; c < arrayServicioComplementario.length(); c++) {           			
		        			map.put("cantidadEntregada"+c, arrayServicioComplementario.getJSONObject(c).getString("CantTotal"));
		                    map.put("tipoTecnologia"+c, "S");
		                    map.put("consecutivoTecnologia"+c, arrayServicioComplementario.getJSONObject(c).getInt("ConOrden"));
		            	}          		
		        	}
		        	
		        	//este if es cuando es solo una sola tecnologia
		        	if(map.size() == 3 || map.size() == 4) {
		        		
		        		FarMipres buscarSolution = iFarMipresService.findByDocumentoPrescripcionConsecutivoTecnologiaCantidad(numDocumento, prescripcion, map.get("cantidadEntregada0").toString(), (Integer) map.get("consecutivoTecnologia0"));
		                
		                if(buscarSolution == null) {         	
		                	
		                	FarMipres farMipres = new FarMipres();
		                    
		                	//buscamos el tipo documento paciente para guardar
		                	ComTipoDocumentoMipres comTipoDocumentoMipres = iComTipoDocumentoMipresService.tipoDocumento(tipoDocumento);
		                	
		                	//buscamos el ambito para guardar                	  
		                	ComAmbito comAmbito = iComAmbitoService.ambitoCodigo(codigoAmbitoAtencion.toString());
		                	
		                	//buscamos el numero de documento del paciente para guardar
		                	GenPacien genPacien = iGenPacienService.findByNumberDoc(numDocumento);
		                	if(genPacien == null) {
		                		GenPacien obtengoPaciente =  SicronizarPacientePorDocumento(tipoDocumento, numDocumento, primerNombre, segundoNombre, primerApellido, segundoApellido);
		                		farMipres.setGenPacien(obtengoPaciente);
		                	}  else {
		                		farMipres.setGenPacien(genPacien);
		                	}
		                	
		                	//buscamos la tecnologia para guardar                	  
		                	ComTipoTecnologia comTipoTecnologia = iComTipoTecnologiaService.tipoTecnologia(map.get("tipoTecnologia0").toString());  
		                	
		                	//validamos si el medicamento es vacio
		                	if(map.containsKey("nombreMedicamento0")) {
		                		farMipres.setNombreMedicamento(map.get("nombreMedicamento0").toString());
		                	}else {
		                		farMipres.setNombreMedicamento("SIN INFORMACION");
		                	}
		                	
		                	farMipres.setNumeroPrescripcion(prescripcion);
		                    farMipres.setCantidadEntregada(map.get("cantidadEntregada0").toString());
		                    farMipres.setProcesadoProgramacion(false);
		                    farMipres.setProcesadoEntrega(false);
		                    farMipres.setProcesadoReporteEntrega(false);
		                    farMipres.setProcesadoFacturacion(false);                    
		                    farMipres.setComTipoDocumentoMipres(comTipoDocumentoMipres);                    
		                    farMipres.setFechaEntrega(fechaEntregaCustomDate);
		                    farMipres.setConsecutivoTecnologia(Integer.parseInt(map.get("consecutivoTecnologia0").toString()));
		                    farMipres.setComTipoTecnologia(comTipoTecnologia);
		                    farMipres.setComAmbito(comAmbito);
		                    farMipres.setCodigoServicio("SIN INFORMACION");
		                    farMipres.setNumeroEntrega(1);
		                    farMipres.setEntregaTotal(1);
		                    farMipres.setCausaNoEntrega(0);
		                    farMipres.setLoginUsrAlta(principal.getName());
		                    farMipres.setCodEps(codEps);		                    
		                    farMipres.setIdDireccionamientoTrazaMipress(direccionamiento.get("ID").toString());
		                    farMipres.setIdDireccionamientoMipress(direccionamiento.get("IDDireccionamiento").toString());
		                    farMipres.setFechaMaxEntrega(convertirFechaParametro(direccionamiento.get("FecMaxEnt").toString()));
		                	iFarMipresService.save(farMipres);
		                	return success;                		
		                }
		                else {
		                	return existe;
		                }
		            //este else es cuando son varias tecnologias
		        	}else {
		        		if(map.size() == 6 || map.size() == 8) {
		        			int hasta = 2;
		        			procesarGuardarAmb(hasta, map, numDocumento, prescripcion, tipoDocumento, primerNombre, segundoNombre, primerApellido, segundoApellido, fechaEntregaCustomDate, principal.getName(), codEps, codigoAmbitoAtencion, direccionamiento);
		        			return success;	
		        		}
		        		if(map.size() == 9 || map.size() == 12) {
		        			int hasta = 3;
		        			procesarGuardarAmb(hasta, map, numDocumento, prescripcion, tipoDocumento, primerNombre, segundoNombre, primerApellido, segundoApellido, fechaEntregaCustomDate, principal.getName(), codEps, codigoAmbitoAtencion, direccionamiento);
		        			return success;	
		        		}
		        	}
		        
		        	}
		        	//si prescripcion no cumple con el codigo ambito atencion
		        	else {        		
		        		return error;
		        	}
		    	 	
		        }//fin for		
		}else {
			return error;
		}
		
	      //si prescripcion esta vacia o nula
		  return error;	        
	}
	
	
	//Se usa para hacer get en el web service de mipres DIRECCIONAMIENTO
	private Map<String, Object> obtenerDireccionamiento(String numeroPrescripcion, String nit, String tokenSecundario) throws ClientProtocolException, IOException, ParseException {
		
		//Me sirve para guardar los tipos de tecnologias
		Map<String, Object> map = new HashMap<>();	
		
		//genero la url para consultar
		String urlEncadenada = MetodoGetDireccionamiento+nit+'/'+tokenSecundario+'/'+numeroPrescripcion;		
					
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
	    
	    if(!arregloJSON.isEmpty()) {
	    	for (int i = 0; i < arregloJSON.length(); i++) {	        	
	        	JSONObject objetoJSON = arregloJSON.getJSONObject(i);   	
	        	map.put("ID", objetoJSON.getInt("ID"));
	        	map.put("IDDireccionamiento", objetoJSON.getInt("IDDireccionamiento"));
	        	map.put("FecMaxEnt", objetoJSON.getString("FecMaxEnt"));      	
		    }
	    }else {
	    	map.put("error", "error");
	    }    
		
		return map;
	}

	//Se usa para hacer put anulacion en el web service de mipres ENTREGA
	private Map<String, String> anulacionPrescripcion(FarMipres farMipres, String condicion) throws Exception {
			
		Map<String, String> map = new HashMap<>();
		
		String urlEncadenada = null;
			
		//obtengo los datos del token secundario guardados en solution
		ComTokenMipres comTokenMipres = iComTokenMipresService.findById(1L); 
			
		//genero la url para consultar
		//urlEncadenada = MetodoPutEntrega+comTokenMipres.getNit()+'/'+comTokenMipres.getTokenSecundario();
		
		switch (condicion) {
		case "entrega":
			urlEncadenada = MetodoPutAnulaEntrega+comTokenMipres.getNit()+'/'+comTokenMipres.getTokenSecundario()+'/'+farMipres.getIdEntregaMipress();			
			break;
			
		case "reporteentrega":
			urlEncadenada = MetodoPutAnulaReporteEntrega+comTokenMipres.getNit()+'/'+comTokenMipres.getTokenSecundario()+'/'+farMipres.getIdReporteEntregaMipress();			
			break;
			
		case "reportefacturacion":
			urlEncadenada = MetodoPutAnulacionReporteFacturacion+comTokenMipres.getNit()+'/'+comTokenMipres.getTokenSecundario()+'/'+farMipres.getIdReporteFacturacionMipress();			
			break;	
		}				
		
	    //Especificamos la URL y configuro el objeto CloseableHttpClient
		CloseableHttpClient httpclient = HttpClients.createDefault();
			
		//Se crea una solicitud PUT (si es post HttpPost y si es get HttpGet) y pasamos el URL del recurso y también asigne encabezados a este objeto de colocación
		HttpPut httpPut = new HttpPut(urlEncadenada);
	    httpPut.setHeader("Accept", "application/json");
	    httpPut.setHeader("Content-type", "application/json");  

		// Envio la solicitud usando HttpPut -> Método de ejecución PUT
		HttpResponse response = httpclient.execute(httpPut);

		// creo un objeto HttpEntity para obtener el resultado en String de la peticion
		HttpEntity entity = response.getEntity();
		String content = EntityUtils.toString(entity);

		// System.out.println(content);

		// convierto la respuesta de la peticion String a Json para mensajes personalizados
		JSONObject jsonContent = new JSONObject();
		if (response.getStatusLine().getStatusCode() == 200) {
			jsonContent = new JSONObject(content.replaceAll("\\[", "").replaceAll("\\]", ""));// busca "[" y "]" y los reemplaza por espacios en blanco
		} else {
			jsonContent = new JSONObject(content);
		}
	        
	    //System.out.println(jsonContent.get("Errors").toString().replaceAll("[^ a-zA-Záéíóú]", ""));
	    //System.out.println(jsonContent.get("Message")); 
	         
		// verificamos que la respuesta o estado sea 200
		if (response.getStatusLine().getStatusCode() == 200) {
			map.put("success", Integer.toString(response.getStatusLine().getStatusCode()));
			map.put("condicion", condicion);
			return map;

		} else if (response.getStatusLine().getStatusCode() == 422) {
			map.put("error", jsonContent.get("Message").toString() + ",  "
					+ jsonContent.get("Errors").toString().replaceAll("[^ a-zA-Záéíóú]", ""));
			return map;
		} else {
			map.put("error", jsonContent.get("Message").toString());
			return map;
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
	
	//Se usa para convertir parametro fecha solicitud de String a fecha Date con formato
	private Date convertirFechaWebService(String fecha) throws ParseException {
		Date fechaTranformada = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(fecha);
		/*System.out.println(fechaTranformada);*/
		//Date fechaTranformada = DateUtils.parseDate(fecha, new String[] { "yyyy-MM-dd HH:mm", "dd-MM-yyyy hh:mm" });
		//System.out.println(fechaTranformada);
		return fechaTranformada;
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
	
	//se usa para crear el certificado hospitalario en PDF
	private void crearPDFHosp(List<FarMipres> farMipres, HttpServletResponse response) throws JRException, IOException, ParseException {
		
		//parametros adicionales para el PDF
		Map<String, Object> parameters = new HashMap<>();
		
		String entregadoTotal = entrega(farMipres.get(0).getEntregaTotal());
		String causaNoEntrega = causa(farMipres.get(0).getCausaNoEntrega());
		
		// Obteniendo el archivo .jrxml de la carpeta de recursos.
		InputStream jrxmlInput = this.getClass().getResourceAsStream("/reports/certificadomipreshosp.jrxml");
		
		// Compilo el informe Jasper de .jrxml a .jasper
		JasperReport jasperReport = JasperCompileManager.compileReport(jrxmlInput);
		
		// Obteniendo a las prescipciones de la fuente de datos.
		JRBeanCollectionDataSource source = new JRBeanCollectionDataSource(farMipres);
		
		//Obtengo datos adicionales del web service Reporte Entrega
		Map<String, Object> webServiceInfoEntrega =  obtenerReporteEntrega(farMipres.get(0).getNumeroPrescripcion());
		
		//Obtengo datos adicionales del web service Reporte Facturacion
		Map<String, Object> webServiceInfoFacturacion =  obtenerReporteFacturacion(farMipres.get(0).getNumeroPrescripcion());
				
		//obtengo el estado del reporte de entrega desde el web service
		String estadoReporteEntrega = estadoReporte(webServiceInfoEntrega.get("EstRepEntrega"));
		//obtengo la fecha del reporte de entrega desde el web service
		Date fecRepEntrega = convertirFechaWebService(webServiceInfoEntrega.get("FecRepEntrega").toString());
		//obtengo el estado del reporte de facturacion desde el web service
		String estadoReporteFacturacion = estadoReporte(webServiceInfoFacturacion.get("EstFacturacion"));
		//obtengo la fecha del reporte de facturacion desde el web service
		Date fecRepFacturacion = convertirFechaWebService(webServiceInfoFacturacion.get("FecFacturacion").toString());
		
		//
		
		// Agregar los parámetros adicionales al pdf.
		parameters.put("logo", this.getClass().getResourceAsStream("/static/dist/img/logohusj.png"));
		parameters.put("entregaTotal", entregadoTotal);
		parameters.put("causaNoEntrega", causaNoEntrega);
		parameters.put("estadoReporteEntrega", estadoReporteEntrega);
		parameters.put("fecRepEntrega", fecRepEntrega);
		parameters.put("valorTotalEntrega", Integer.parseInt(farMipres.get(0).getValorEntregado()));
		parameters.put("cantidadUnidadesMinimas", String.valueOf(webServiceInfoFacturacion.get("CantUnMinDis")).replaceAll(".0", ""));
		parameters.put("estadoReporteFacturacion", estadoReporteFacturacion);
		parameters.put("fecRepFacturacion", fecRepFacturacion);
		parameters.put("valorUnitario", Integer.parseInt(farMipres.get(0).getValorUnitario()));
		parameters.put("cuotaModeradora", Integer.parseInt(farMipres.get(0).getCuotaModeradora()));
		parameters.put("copago", Integer.parseInt(farMipres.get(0).getCopago()));
		parameters.put("valorTotal", Integer.parseInt(farMipres.get(0).getValorTotal()));	
		
		
		// Rellenar el informe con los datos de la prescripcion y la información de parámetros adicionales.
		JasperPrint jasperPrint  = JasperFillManager.fillReport(jasperReport, parameters, source);
		
		
		//este me permite exportar y abrir dialogo para guardar el archivo
		String fileName = farMipres.get(0).getNumeroPrescripcion()+".pdf";
		response.addHeader("Content-Disposition", "attachment;filename=\"" + fileName + "\"");
		response.setContentType("application/pdf");
		ServletOutputStream servletOutputStream = response.getOutputStream();
	    JasperExportManager.exportReportToPdfStream(jasperPrint,servletOutputStream);
	    servletOutputStream.flush();
	    servletOutputStream.close();
	    
	}
	
	
	//se usa para crear el certificado ambulatorio en PDF
	private void crearPDFAmb(List<FarMipres> farMipres, HttpServletResponse response) throws JRException, IOException, ParseException {
			
		//parametros adicionales para el PDF
		Map<String, Object> parameters = new HashMap<>();
		
		String entregadoTotal = entrega(farMipres.get(0).getEntregaTotal());
		String causaNoEntrega = causa(farMipres.get(0).getCausaNoEntrega());
			
		// Obteniendo el archivo .jrxml de la carpeta de recursos.
		InputStream jrxmlInput = this.getClass().getResourceAsStream("/reports/certificadomipresamb.jrxml");
			
		// Compilo el informe Jasper de .jrxml a .jasper
		JasperReport jasperReport = JasperCompileManager.compileReport(jrxmlInput);
			
		// Obteniendo a las prescipciones de la fuente de datos.
		JRBeanCollectionDataSource source = new JRBeanCollectionDataSource(farMipres);
			
		//Obtengo datos adicionales del web service Reporte Entrega
		Map<String, Object> webServiceInfoEntrega =  obtenerReporteEntrega(farMipres.get(0).getNumeroPrescripcion());
			
		//Obtengo datos adicionales del web service Reporte Facturacion
		Map<String, Object> webServiceInfoFacturacion =  obtenerReporteFacturacion(farMipres.get(0).getNumeroPrescripcion());
					
		//obtengo el estado del reporte de entrega desde el web service
		String estadoReporteEntrega = estadoReporte(webServiceInfoEntrega.get("EstRepEntrega"));
		//obtengo la fecha del reporte de entrega desde el web service
		Date fecRepEntrega = convertirFechaWebService(webServiceInfoEntrega.get("FecRepEntrega").toString());
		//obtengo el estado del reporte de facturacion desde el web service
		String estadoReporteFacturacion = estadoReporte(webServiceInfoFacturacion.get("EstFacturacion"));
		//obtengo la fecha del reporte de facturacion desde el web service
		Date fecRepFacturacion = convertirFechaWebService(webServiceInfoFacturacion.get("FecFacturacion").toString());
			
		// Agregar los parámetros adicionales al pdf.
		parameters.put("logo", this.getClass().getResourceAsStream("/static/dist/img/logohusj.png"));
		parameters.put("entregaTotal", entregadoTotal);
		parameters.put("causaNoEntrega", causaNoEntrega);
		parameters.put("estadoReporteEntrega", estadoReporteEntrega);
		parameters.put("fecRepEntrega", fecRepEntrega);
		parameters.put("valorTotalEntrega", Integer.parseInt(farMipres.get(0).getValorEntregado()));
		parameters.put("cantidadUnidadesMinimas", String.valueOf(webServiceInfoFacturacion.get("CantUnMinDis")).replaceAll(".0", ""));
		parameters.put("estadoReporteFacturacion", estadoReporteFacturacion);
		parameters.put("fecRepFacturacion", fecRepFacturacion);
		parameters.put("valorUnitario", Integer.parseInt(farMipres.get(0).getValorUnitario()));
		parameters.put("cuotaModeradora", Integer.parseInt(farMipres.get(0).getCuotaModeradora()));
		parameters.put("copago", Integer.parseInt(farMipres.get(0).getCopago()));
		parameters.put("valorTotal", Integer.parseInt(farMipres.get(0).getValorTotal()));	
			
			
		// Rellenar el informe con los datos de la prescripcion y la información de parámetros adicionales.
		JasperPrint jasperPrint  = JasperFillManager.fillReport(jasperReport, parameters, source);
			
			
		//este me permite exportar y abrir dialogo para guardar el archivo
		String fileName = farMipres.get(0).getNumeroPrescripcion()+".pdf";
		response.addHeader("Content-Disposition", "attachment;filename=\"" + fileName + "\"");
		response.setContentType("application/pdf");
		ServletOutputStream servletOutputStream = response.getOutputStream();
		JasperExportManager.exportReportToPdfStream(jasperPrint,servletOutputStream);
		servletOutputStream.flush();
		servletOutputStream.close();
		    
	}

	//se usa para transformar (entero) de la entrega en String 
	private String entrega(Integer entregaTotal) {
		if(entregaTotal == 1) {
			return "SI";
		}else {
			return "NO";
		}
	}
	
	
	//se usa para transformar (entero) de la entrega en String 
	private String causa(Integer causaNoEntrega) {
		if(causaNoEntrega == 0) {
			return "NINGUNA";
		}
		else if(causaNoEntrega == 7) {
			return "NO FUE POSIBLE CONTACTAR AL PACIENTE";
		}
		else if(causaNoEntrega == 8) {
			return "PACIENTE FALLECIDO";
		}
		else {
			return "PACIENTE SE NIEGA A RECIBIR EL SUMINISTRO";
		}
	}
	
	//se usa para transformar (entero) de estado reporte entrega en String 
	private String estadoReporte(Object estado) {
		if(estado.equals(0)) {
			return "Anulado";
		}
		else if(estado.equals(1)) {
			return "Activo";
		}
		else {
			return "Procesado";
		}
		
	}
	
	//se usa para obtener la informacion del reporte de entrega del web service de mipres
	private Map<String, Object> obtenerReporteEntrega(String prescripcion) throws IOException, IOException{
		
		Map<String, Object> datosAdicionales = new HashMap<>();
		
		//obtengo los datos del token primario guardados en solution
		ComTokenMipres comTokenMipres = iComTokenMipresService.findById(1L);
		
		//genero la url para consultar
		String urlEncadenada = MetodoGetReporteEntrega+comTokenMipres.getNit()+'/'+comTokenMipres.getTokenSecundario()+'/'+prescripcion;
		
		//Especificamos la URL y configuro el objeto HttpClient			
		HttpClient httpclient = HttpClientBuilder.create().build();			
		HttpResponse response = null;
		
		//Se crea una solicitud GET (si es post HttpPost y si es es put) y pasamos el URL del recurso y también asigne encabezados a este objeto de colocación
		HttpGet httpGet = new HttpGet(urlEncadenada);			
		httpGet.setHeader("Accept", "application/json");
		httpGet.setHeader("Content-type", "application/json");
		
		response = httpclient.execute(httpGet);
		
		//creo un String para guardar la respuesta y convertir en un arreglo JSON	        
        String content;
		
        content = EntityUtils.toString(response.getEntity());
		JSONArray arregloJSON = new JSONArray(content);
		
		for (int i = 0; i < arregloJSON.length(); i++) {
			
			JSONObject objetoJSON = arregloJSON.getJSONObject(i);
        	
			datosAdicionales.put("ID", objetoJSON.get("ID"));
			datosAdicionales.put("IDReporteEntrega", objetoJSON.get("IDReporteEntrega"));
			datosAdicionales.put("NoPrescripcion", objetoJSON.get("NoPrescripcion"));
			datosAdicionales.put("TipoTec", objetoJSON.get("TipoTec"));
			datosAdicionales.put("ConTec", objetoJSON.get("ConTec"));
			datosAdicionales.put("TipoIDPaciente", objetoJSON.get("TipoIDPaciente"));
			datosAdicionales.put("NoIDPaciente", objetoJSON.get("NoIDPaciente"));
			datosAdicionales.put("NoEntrega", objetoJSON.get("NoEntrega"));
			datosAdicionales.put("EstadoEntrega", objetoJSON.get("EstadoEntrega"));
			datosAdicionales.put("CausaNoEntrega", objetoJSON.get("CausaNoEntrega"));
			datosAdicionales.put("ValorEntregado", objetoJSON.get("ValorEntregado"));
			datosAdicionales.put("CodTecEntregado", objetoJSON.get("CodTecEntregado"));
			datosAdicionales.put("CantTotEntregada", objetoJSON.get("CantTotEntregada"));
			datosAdicionales.put("NoLote", objetoJSON.get("NoLote"));
			datosAdicionales.put("FecEntrega", objetoJSON.get("FecEntrega"));
			datosAdicionales.put("FecRepEntrega", objetoJSON.get("FecRepEntrega"));
			datosAdicionales.put("EstRepEntrega", objetoJSON.get("EstRepEntrega"));
			datosAdicionales.put("FecAnulacion", objetoJSON.get("FecAnulacion"));			
		}
		
		return datosAdicionales;
		
	}
	
	//se usa para obtener la informacion del reporte de facturacion del web service de mipres
	private Map<String, Object> obtenerReporteFacturacion(String prescripcion) throws IOException, IOException{
			
		Map<String, Object> datosAdicionales = new HashMap<>();
			
		//obtengo los datos del token primario guardados en solution
		ComTokenMipres comTokenMipres = iComTokenMipresService.findById(1L);
			
		//genero la url para consultar
		String urlEncadenada = MetodoGetReporteFacturacion+comTokenMipres.getNit()+'/'+comTokenMipres.getTokenSecundario()+'/'+prescripcion;
			
		//Especificamos la URL y configuro el objeto HttpClient			
		HttpClient httpclient = HttpClientBuilder.create().build();			
		HttpResponse response = null;
			
		//Se crea una solicitud GET (si es post HttpPost y si es es put) y pasamos el URL del recurso y también asigne encabezados a este objeto de colocación
		HttpGet httpGet = new HttpGet(urlEncadenada);			
		httpGet.setHeader("Accept", "application/json");
		httpGet.setHeader("Content-type", "application/json");
			
		response = httpclient.execute(httpGet);
			
		//creo un String para guardar la respuesta y convertir en un arreglo JSON	        
	    String content;
			
	    content = EntityUtils.toString(response.getEntity());
	    JSONArray arregloJSON = new JSONArray(content);
			
	    for (int i = 0; i < arregloJSON.length(); i++) {
				
			JSONObject objetoJSON = arregloJSON.getJSONObject(i);
	        	
			datosAdicionales.put("ID", objetoJSON.get("ID"));
			datosAdicionales.put("IDFacturacion", objetoJSON.get("IDFacturacion"));
			datosAdicionales.put("NoPrescripcion", objetoJSON.get("NoPrescripcion"));
			datosAdicionales.put("TipoTec", objetoJSON.get("TipoTec"));
			datosAdicionales.put("ConTec", objetoJSON.get("ConTec"));
			datosAdicionales.put("TipoIDPaciente", objetoJSON.get("TipoIDPaciente"));
			datosAdicionales.put("NoIDPaciente", objetoJSON.get("NoIDPaciente"));
			datosAdicionales.put("NoEntrega", objetoJSON.get("NoEntrega"));
			datosAdicionales.put("NoFactura", objetoJSON.get("NoFactura"));
			datosAdicionales.put("NoIDEPS", objetoJSON.get("NoIDEPS"));
			datosAdicionales.put("CodEPS", objetoJSON.get("CodEPS"));
			datosAdicionales.put("CodSerTecAEntregado", objetoJSON.get("CodSerTecAEntregado"));
			datosAdicionales.put("CantUnMinDis", objetoJSON.get("CantUnMinDis"));
			datosAdicionales.put("ValorUnitFacturado", objetoJSON.get("ValorUnitFacturado"));
			datosAdicionales.put("ValorTotFacturado", objetoJSON.get("ValorTotFacturado"));
			datosAdicionales.put("CuotaModer", objetoJSON.get("CuotaModer"));
			datosAdicionales.put("Copago", objetoJSON.get("Copago"));
			datosAdicionales.put("FecFacturacion", objetoJSON.get("FecFacturacion"));
			datosAdicionales.put("EstFacturacion", objetoJSON.get("EstFacturacion"));
			datosAdicionales.put("FecAnulacion", objetoJSON.get("FecAnulacion"));
		}
			
		return datosAdicionales;
			
	}

	
	//Se usa para crear el archivo en EXCEL
	private void crearExcel(List<FarMipres> listadoMipres, HttpServletResponse response) {
		//EJEMPLO DE RANGOS EN FILAS Y COLUMNAS
		//sheet.addMergedRegion(new CellRangeAddress(fila_inicial, fila_final, columna_inicial, columna_final))		
					
		//Se crea variable para el nombre del archivo de EXCEL
		String fileName = "reporte_rips.xlsx";
					
					
		// 1. Se crea el libro XLSX
		// Umbral, el número máximo de objetos en la memoria, más allá del cual se genera y almacena un archivo temporal en el disco duro
		SXSSFWorkbook workbook = new SXSSFWorkbook(); //new HSSFWorkbook() para generar archivos `.xls`
					
		//OPCIONAL
		//CreationHelper nos ayuda a crear instancias o utilerias para formatos especiales como DataFormat, Hyperlink, RichTextString, etc., en un formato (HSSF, XSSF) de forma independiente
		CreationHelper createHelper = workbook.getCreationHelper();
				
		//2.Se crea una hoja dentro del libro asignando un nombre
		SXSSFSheet sheet = workbook.createSheet("Reporte_Rips");	
		
		//se usa para complementar el metodo autoSizeColumn, para ajustar el texto en la columna
		sheet.trackAllColumnsForAutoSizing();
						
		//3. Establecer el estilo y el estilo de fuente		
		CellStyle headerStyle = ExcelUtils.createHeadCellStyle(workbook);
		CellStyle contentStyle = ExcelUtils.createContentCellStyleMiPres(workbook);
			        
			        
		//4. Crear primera fila encabezados
		//Número de línea
		int rowNum = 0;
		
		//Primer elemento       
	    Row row1 = sheet.createRow(rowNum++);
	    //sheet.setColumnWidth(3, 25 * 256);
	    row1.setHeight((short)500); //me permite poner alto a la celda
	    String[] row_first = {"PACIENTE","TIPO DOCUMENTO","NUMERO DOCUMENTO","NUMERO FACTURA","NIT","CODIGO EPS","COD TECNOLOGIA","CANTIDAD ENTREGADA","VR UNITARIO","VR TOTAL","NUMERO PRESCRIPCION","ID ENTREGA","ID TRAZA","ID REPORTE ENTREGA","ID FACTURACION","ID REPORTE FACTURACION"};
	    for (int i = 0; i < row_first.length; i++) {
	    Cell tempCell = row1.createCell(i);
	    	tempCell.setCellValue(row_first[i]);
	        tempCell.setCellStyle(headerStyle);	        
	    }
	        
	    //5. Agrego el contenido desde un arraylist al EXCEL
	    
	    //Segundo elemento
	    //for(EstMortalidad mortalidad: listadoMortalidad) {
	        
	    for(int i=0; i<listadoMipres.size(); i++) {       
	        	
	       	Row tempRow = sheet.createRow(rowNum++);
	        //tempRow.setHeight((short) 1250);
	        // Recorrido para relleno de celdas
	        for (int j = 0; j < 16; j++) {
	            	
	            Cell tempCell = tempRow.createCell(j);
	            tempCell.setCellStyle(contentStyle);	            
	            String tempValue = "";
	            if (j == 0) {
	            	// Paciente
	            	String nombreCompleto = nombreCompleto(listadoMipres.get(i).getGenPacien().getPacPriNom(), listadoMipres.get(i).getGenPacien().getPacSegNom(), listadoMipres.get(i).getGenPacien().getPacPriApe(), listadoMipres.get(i).getGenPacien().getPacSegApe());
	            	tempValue = nombreCompleto;                    
	            }
	            else if(j == 1) {
	            	// Tipo Documento Paciente
	                tempValue = listadoMipres.get(i).getGenPacien().getComTipoDocumento().getTipo();
	            }
	            else if(j == 2) {
	               	// Numero Documento Paciente
	               	tempValue = listadoMipres.get(i).getGenPacien().getPacNumDoc();
	            }
	            else if(j == 3) {
	               	// Numero Factura
	                tempValue = listadoMipres.get(i).getNumeroFactura();
	            }
	            else if(j == 4) {
	               	// Nit Eps
	                tempValue = listadoMipres.get(i).getNitEps();
	            }
	            else if(j == 5) {
	               	// Codigo Eps
	                tempValue = listadoMipres.get(i).getCodEps();
	            }
	            else if(j == 6) {
	               	// Codigo Tecnologia
	                tempValue = listadoMipres.get(i).getCodigoServicio();
	            }
	            else if(j == 7) {
	               	// Cantidad Entregada
	                tempValue = listadoMipres.get(i).getCantidadEntregada();
	            }
	            else if(j == 8) {
	               	// Valor Unitario
	                tempValue = listadoMipres.get(i).getValorUnitario();
	            }
	            else if(j == 9) {
	               	// Valor Total
	                tempValue = listadoMipres.get(i).getValorTotal();
	            }
	            else if(j == 10) {
	               	// Numero Prescripcion
	                tempValue = listadoMipres.get(i).getNumeroPrescripcion();
	            }
	            else if(j == 11) {
	               	// ID Entrega
	                tempValue = listadoMipres.get(i).getIdEntregaMipress();
	            }
	            else if(j == 12) {
	               	// ID Traza
	                tempValue = listadoMipres.get(i).getIdTraza();
	            }
	            else if(j == 13) {
	               	// ID Reporte Entrega
	                tempValue = listadoMipres.get(i).getIdReporteEntregaMipress();
	            }
	            else if(j == 14) {
	               	// ID Facturacion
	                tempValue = listadoMipres.get(i).getIdFacturacionMipress();
	            }
	            else if(j == 15) {
	            	// ID Reporte Facturacion
	                tempValue = listadoMipres.get(i).getIdReporteFacturacionMipress();
	            }
	            
	            // Creamos la celda con el contenido
	            tempCell.setCellValue(tempValue);
	            sheet.autoSizeColumn(j);          	
	          }
	      }      
	        
		//este me permite exportar y abrir dialogo para guardar el archivo
	    try {
	    	fileName = new String(fileName.getBytes("UTF-8"),"ISO-8859-1");
	        response.setHeader("Content-disposition", "attachment;filename=\"" + fileName + "\"");            
	        OutputStream stream = response.getOutputStream();
	        if(null != workbook && null != stream){
	        	workbook.write(stream);
	            workbook.close();
	            stream.close();
	        	}
	        }catch (Exception e){
	        	e.printStackTrace();
	        }		
	}

	//Se usa para generar el nombre completo del paciente quitando espacios para que en la celda de excel quede mejor visualmente
	private String nombreCompleto(String pacPriNom, String pacSegNom, String pacPriApe, String pacSegApe) {
		
		//esta clase me permite el uso de concatenacion
		StringBuilder nombreCompleto = new StringBuilder(100);		
		
		if(!StringUtils.isBlank(pacPriNom)) {			
			nombreCompleto.append(pacPriNom.replaceAll("\\s+","")); //a los textos les quitamos los espacios que vienen de la base
		}
		if(!StringUtils.isBlank(pacSegNom)) {
			nombreCompleto.append(" "+pacSegNom.replaceAll("\\s+","")); //a los textos les quitamos los espacios que vienen de la base
		}
		if(!StringUtils.isBlank(pacPriApe)) {
			nombreCompleto.append(" "+pacPriApe.replaceAll("\\s+","")); //a los textos les quitamos los espacios que vienen de la base
		}
		if(!StringUtils.isBlank(pacSegApe)) {
			nombreCompleto.append(" "+pacSegApe.replaceAll("\\s+","")); //a los textos les quitamos los espacios que vienen de la base
		}		
		return nombreCompleto.toString();
	}	
}