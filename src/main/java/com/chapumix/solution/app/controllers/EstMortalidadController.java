package com.chapumix.solution.app.controllers;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
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
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.chapumix.solution.app.entity.dto.GenPacienDTO;
import com.chapumix.solution.app.entity.dto.GenPacienMortalidadDTO;
import com.chapumix.solution.app.entity.dto.GenPacienMortalidadTMPDTO;
import com.chapumix.solution.app.models.entity.ComCie10;
import com.chapumix.solution.app.models.entity.ComGenero;
import com.chapumix.solution.app.models.entity.ComRegimen;
import com.chapumix.solution.app.models.entity.ComTipoDocumento;
import com.chapumix.solution.app.models.entity.ComTipoIngreso;
import com.chapumix.solution.app.models.entity.ComUsuario;
import com.chapumix.solution.app.models.entity.EstAsistente;
import com.chapumix.solution.app.models.entity.EstCausa;
import com.chapumix.solution.app.models.entity.EstMortalidad;
import com.chapumix.solution.app.models.entity.EstRetraso;
import com.chapumix.solution.app.models.entity.GenPacien;
import com.chapumix.solution.app.models.service.IComApacheService;
import com.chapumix.solution.app.models.service.IComCie10Service;
import com.chapumix.solution.app.models.service.IComEstadoHoraService;
import com.chapumix.solution.app.models.service.IComGeneroService;
import com.chapumix.solution.app.models.service.IComPrismService;
import com.chapumix.solution.app.models.service.IComRegimenService;
import com.chapumix.solution.app.models.service.IComRetrasoService;
import com.chapumix.solution.app.models.service.IComTipoDocumentoService;
import com.chapumix.solution.app.models.service.IComTipoIngresoService;
import com.chapumix.solution.app.models.service.IComUsuarioService;
import com.chapumix.solution.app.models.service.IEstMortalidadService;
import com.chapumix.solution.app.models.service.IGenAreSerService;
import com.chapumix.solution.app.models.service.IGenPacienService;
import com.chapumix.solution.app.utils.ExcelUtils;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;


@Controller
@SessionAttributes("estMortalidad")
@PropertySource(value = "application.properties", encoding="UTF-8")
public class EstMortalidadController {
	
	public static final String URLPaciente = "http://localhost:9000/api/pacientemortalidad"; //se obtuvo de API REST de GenPacienRestController
	
	@Autowired
	private IEstMortalidadService iEstMortalidadService;
	
	@Autowired
	private IComGeneroService iComGeneroService;
	
	@Autowired
	private IComRegimenService iComRegimenService;
	
	@Autowired
	private IComTipoIngresoService iComTipoIngresoService;
	
	@Autowired
	private IComCie10Service iComCie10Service;
	
	@Autowired
	private IGenAreSerService iGenAreSerService;
	
	@Autowired
	private IGenPacienService iGenPacienService;
	
	@Autowired
	private IComTipoDocumentoService iComTipoDocumentoService;
	
	@Autowired
	private IComEstadoHoraService iComEstadoHoraService;
	
	@Autowired
	private IComRetrasoService iComRetrasoService;
	
	@Autowired
	private IComApacheService iComApacheService;
	
	@Autowired
	private IComPrismService iComPrismService;
	
	@Autowired
	private IComUsuarioService iComUsuarioService;
		
	@Autowired
	private RestTemplate restTemplate;
	
	@Value("${app.titulomortalidad}")
	private String titulomortalidad;
	
	@Value("${app.titulomortalidadconsolidado}")
	private String tituloconsolidado;
	
	@Value("${app.titulomortalidadreporte}")
	private String tituloreporte;
	
	
	
	@Value("${app.enlaceprincipalestadistica}")
	private String enlaceprincipalestadistica;
	
	@Value("${app.enlace7}")
	private String enlace7;
	
	
	/* ----------------------------------------------------------
     * INDEX ESTADISTICA MORTALIDAD
     * ---------------------------------------------------------- */
	
	//INDEX ESTADISTICA MORTALIDAD
	@GetMapping("/indexmortalidad")
	public String index(Model model) {
		model.addAttribute("titulo", utf8(this.titulomortalidad));
		model.addAttribute("estadistica", enlaceprincipalestadistica);
		model.addAttribute("enlace7", enlace7);
		return "indexmortalidad";
	}
	
	
	
	// Este metodo me permite visualizar o cargar el formulario para mortalidad
	@GetMapping("/mortalidadform")
	public String crearMortalidad(Map<String, Object> model) {	
		
		EstMortalidad estMortalidad = new EstMortalidad();		

		model.put("titulo", utf8(this.titulomortalidad));
		model.put("estMortalidad", estMortalidad);		
		model.put("servicio", iGenAreSerService.findByOrderNombre());
		model.put("retraso", iComRetrasoService.findAll());
		model.put("estadoHora", iComEstadoHoraService.findAll());
		model.put("apache", iComApacheService.findAll());
		model.put("prism", iComPrismService.findAll());
		model.put("cie10", iComCie10Service.findAllAsc());
		model.put("estadistica", enlaceprincipalestadistica);
		model.put("enlace7", enlace7);
		return "mortalidadform";
	}
	
	
	// Este metodo me permite guardar el analisis de mortalidad
	@RequestMapping(value = "/mortalidadform", method = RequestMethod.POST)
	public String guardarMortalidad(@Valid EstMortalidad estMortalidad, BindingResult result, Model model, @RequestParam(name = "pacNombre") String pacNombre, 
			@RequestParam(name = "pacApellido") String pacApellido, @RequestParam(name = "ingreso") String ingreso, 
			@RequestParam(name = "edad") String edad, @RequestParam(name = "genero") String genero, 
			@RequestParam(name = "municipio") String municipio, @RequestParam(name = "regimen") String regimen,
			@RequestParam(name = "tipoIngreso") String tipoIngreso, @RequestParam(name = "diagnostico") String diagnostico,
			@RequestParam(name = "entidad") String entidad, @RequestParam(name = "fechaIngreso") String fechaIngreso,
			@RequestParam(name = "fechaDefuncion") String fechaDefuncion, Principal principal, RedirectAttributes flash, SessionStatus status) throws ParseException {	
		
		
		if (result.hasErrors()) {
			
			model.addAttribute("titulo", utf8(this.titulomortalidad));	
			model.addAttribute("servicio", iGenAreSerService.findByOrderNombre());
			model.addAttribute("retraso", iComRetrasoService.findAll());
			model.addAttribute("estadoHora", iComEstadoHoraService.findAll());
			model.addAttribute("apache", iComApacheService.findAll());
			model.addAttribute("prism", iComPrismService.findAll());
			model.addAttribute("cie10", iComCie10Service.findAllAsc());
			model.addAttribute("pacNombre", pacNombre);
			model.addAttribute("pacApellido", pacApellido);
			model.addAttribute("ingreso", ingreso);
			model.addAttribute("edad", edad);
			model.addAttribute("genero", genero);
			model.addAttribute("municipio", municipio);
			model.addAttribute("regimen", regimen);
			model.addAttribute("tipoIngreso", tipoIngreso);
			model.addAttribute("diagnostico", diagnostico);
			model.addAttribute("entidad", entidad);
			model.addAttribute("fechaIngreso", fechaIngreso);
			model.addAttribute("fechaDefuncion", fechaDefuncion);
			model.addAttribute("estadistica", enlaceprincipalestadistica);
			model.addAttribute("enlace7", enlace7);
			return "mortalidadform";
		}	
		

		// valido si el paciente existe en solution para ir alimentando tabla de pacientes en solution
		GenPacien validarPaciente = iGenPacienService.findByNumberDoc(estMortalidad.getGenPacien().getPacNumDoc());
		
		if(validarPaciente == null) {
			// sincronizo paciente de dinamica a solution en caso de que no exista
			sincronizarPaciente(validarPaciente, estMortalidad.getGenPacien().getPacNumDoc());
		}				
		
		//busco el analisis para verificar que solo se encuentre tan solo un registro
		EstMortalidad mortalidad = iEstMortalidadService.pacienteMortalidad(estMortalidad.getGenPacien().getPacNumDoc());
		
		if(mortalidad == null) {
			
			// busco el paciente para agregarlo al analisis de mortalidad
			GenPacien paciente = iGenPacienService.findByNumberDoc(estMortalidad.getGenPacien().getPacNumDoc());
						
			String mensajeFlash = (estMortalidad.getId() != null) ? "El análisis fue editado correctamente" : "El análisis fue creado correctamente";		
			estMortalidad.setGenPacien(paciente);
			
			//sincronizo y le asigno el id de la mortalidad al objeto EstAsistentes
			sincronizarDetalleAsistente(estMortalidad.getEstAsistentes(), estMortalidad);
			
			//sincronizo y le asigno el id de la mortalidad al objeto EstRetraso
			sincronizarDetalleRetraso(estMortalidad.getEstRetrasos(), estMortalidad);
		
			//sincronizo y le asigno el id de la mortalidad al objeto EstCausa
			sincronizarDetalleCausa(estMortalidad.getEstCausas(), estMortalidad);		
			
			//guardo el usuario en base al logueo
			estMortalidad.setLoginUsrAlta(principal.getName());			
			
			//busco el regimen para ser seteado en estMortalidad
			estMortalidad.setComRegimen(iComRegimenService.regimenNombre(regimen));
			
			//busco el tipo de ingreso para ser seteado en estMortalidad
			estMortalidad.setComTipoIngreso(iComTipoIngresoService.tipoIngresoNombre(tipoIngreso));
			
			//busco el diagnostico segun rips para ser seteado en estMortalidad
			String codigoDiagnostico = recortarDiagnostico(diagnostico);
			estMortalidad.setComCie10(iComCie10Service.findByCodigo(codigoDiagnostico));
			
			
			//filtro para guardar el predictor seleccionado
			filtroPredictor(estMortalidad.getEscala(), estMortalidad);
			
			
			estMortalidad.setFechaIngreso(convertirFecha(fechaIngreso));
			estMortalidad.setFechaDefuncion(convertirFecha(fechaDefuncion));
			iEstMortalidadService.save(estMortalidad);
			status.setComplete();
			flash.addFlashAttribute("success", mensajeFlash);		
			
		}else {
			flash.addFlashAttribute("error", "Proceso no realizado, ya existe un analisis para el paciente");			
		}
		
		return "redirect:mortalidadform";
		
	}


	// Este metodo me permite visualizar o cargar el formulario para el consolidado de mortalidades
	@GetMapping("/consolidadomortalidad")
	public String crearconsolidadomortalidad(Map<String, Object> model) {		
		model.put("titulo", utf8(this.tituloconsolidado));
		model.put("estadistica", enlaceprincipalestadistica);
		model.put("enlace7", enlace7);
		return "consolidadomortalidad";	
		
	}
	
	
	// Este metodo me permite generar el consolidad de mortalidades
	@RequestMapping("/generarconsolidadomortalidad")
	public String generarconsolidadomortalidad(Model model, @RequestParam(value = "fechaInicial", required = false) String fechaInicial, @RequestParam(value = "fechaFinal", required = false) String fechaFinal, RedirectAttributes flash, HttpServletResponse response) throws ParseException {
		
		String errorFechas = "";
		
		// valida si la fecha inicial y la fecha final no estan vacios
		if (fechaInicial.equals("") && fechaFinal.equals("")) {
			errorFechas = "Debes establecer una fecha inicial y fecha final";
			model.addAttribute("error", errorFechas);
			model.addAttribute("estadistica", enlaceprincipalestadistica);
			model.addAttribute("enlace7", enlace7);
			return "consolidadomortalidad";
		}

		// valida si la fecha inicial y la fecha final no estan vacios
		if (fechaInicial.equals("") || fechaFinal.equals("")) {
			errorFechas = "Debes establecer una fecha inicial y fecha final";
			model.addAttribute("error", errorFechas);
			model.addAttribute("estadistica", enlaceprincipalestadistica);
			model.addAttribute("enlace7", enlace7);
			return "consolidadomortalidad";
		}
		
		// consulta por la fecha inicial y la fecha final
		if (!fechaInicial.equals("") && !fechaFinal.equals("")) {
			Date fechaI = convertirFechaReporte(fechaInicial);
			Date fechaF = convertirFechaReporte(fechaFinal);
			
			List<EstMortalidad> listadoMortalidad = iEstMortalidadService.findByStartDateBetween(fechaI, fechaF);
			
			//creamos el reporte en EXCEL
			crearExcel(listadoMortalidad, response);
						
									
			model.addAttribute("estadistica", enlaceprincipalestadistica);
			model.addAttribute("enlace7", enlace7);						
		}		
		
		return  null;
		
	}
	
	// Este metodo me permite visualizar o cargar el formulario para el reporte de mortalidades
	@GetMapping("/reportemortalidad")
	public String crearreportemortalidad(Map<String, Object> model) {		
		model.put("titulo", utf8(this.tituloreporte));
		model.put("estadistica", enlaceprincipalestadistica);
		model.put("enlace7", enlace7);
		return "reportemortalidad";	
			
	}
	
	
	// Este metodo me permite generar el consolidad de mortalidades
	@RequestMapping("/generarreportemortalidad")
	public String generarreportemortalidad(Model model, @RequestParam(value = "documento", required = false) String documento, RedirectAttributes flash, HttpServletResponse response) throws ParseException, JRException, IOException {
			
		// valida si la fecha inicial y la fecha final no estan vacios
		if (documento.isEmpty()) {			
			model.addAttribute("error", "El documento es requerido.");
			model.addAttribute("estadistica", enlaceprincipalestadistica);
			model.addAttribute("enlace7", enlace7);
			return "reportemortalidad";
		}		
		else {
			List<EstMortalidad> mortalidad = iEstMortalidadService.pacienteMortalidadPDF(documento);
			
			if(!mortalidad.isEmpty()) {
				//creamos el reporte en PDF
				crearPDF(mortalidad, response);
				model.addAttribute("estadistica", enlaceprincipalestadistica);
				model.addAttribute("enlace7", enlace7);
			}else {
				model.addAttribute("error", "No hay datos del paciente.");
				model.addAttribute("estadistica", enlaceprincipalestadistica);
				model.addAttribute("enlace7", enlace7);
				return "reportemortalidad";
			}			
		}			
		return  null;			
	}	


	// Este metodo me permite cargar por AJAX el dato del paciente
	@GetMapping(value = "/pacientemortalidad/{term}", produces = { "application/json" })
	@ResponseBody
	public List<GenPacienMortalidadTMPDTO> cargarPaciente(@PathVariable String term) {
		// proceso API para buscar el paciente
		ResponseEntity<List<GenPacienMortalidadDTO>> respuesta = restTemplate.exchange(URLPaciente + '/' + term, HttpMethod.GET, null, new ParameterizedTypeReference<List<GenPacienMortalidadDTO>>() {});
		List<GenPacienMortalidadDTO> dinamica = respuesta.getBody();
		
		//creo un nuevo arreglo para guardar el resultado final de la consulta con los datos transformados
		List<GenPacienMortalidadTMPDTO> finalValores = new ArrayList<>();		
		
		if(dinamica != null) {
			dinamica.forEach(d -> {
				
				//obtengo la edad completa
				String edad = calcularEdad(d.getGpafecnac());			
				//obtengo el genero			
				ComGenero genero = iComGeneroService.findById(new Long(d.getGpasexpac()));
				//obtengo el regimen			
				ComRegimen regimen = iComRegimenService.findById(new Long(d.getGdeConFac()));
				//obtengo el tipo de ingreso			
				ComTipoIngreso  tipoIngreso = iComTipoIngresoService.findByCodigo(d.getAinUrgCon());
				if(tipoIngreso == null) {
					tipoIngreso = new ComTipoIngreso();
					tipoIngreso.setNombre("");
				}
				//obtengo el diagnostico principal			
				ComCie10  diagnostico = iComCie10Service.findByCodigo(d.getDiaCodigo());
				//convierto la fecha de ingreso
				String fechaIngreso = formatoFecha(d.getAinFecIng());
				//convierto la fecha de defuncion
				String fechaDefuncion = formatoFecha(d.getHcefecdef());
				
				finalValores.add(new GenPacienMortalidadTMPDTO(d.getPacNumDoc(), d.getPacPriNom().trim()+" "+d.getPacSegNom().trim(), d.getPacPriApe().trim()+" "+d.getPacSegApe().trim(), d.getAinConsec().toString().trim(), edad, genero.getNombre(), d.getMunNomMun().trim(), regimen.getNombre(), tipoIngreso.getNombre(), diagnostico.getCodigo()+" - "+diagnostico.getNombre(), d.getEntNombre(), fechaIngreso, fechaDefuncion));
				
				
				});		
		}		
		
		return finalValores;
			
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
	
	
	// Se usa para calcular la edad de una fecha
	private String calcularEdad(Date fechaNacimiento) {

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
		String fecha = String.format("%s AÑOS, %s MESES, %s DIAS", periodo.getYears(), periodo.getMonths(), periodo.getDays());
		return fecha;

	}
	
	// Se usa para dar formato a fechas de dinamica
	private String formatoFecha(Date fecha) {

		// convierto la fecha que entra en formato Date
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss aa");
		String fechaConversion = sdf.format(fecha);		
		return fechaConversion;

	}
	
	//Se usa para convertir una cadena String a fecha Date con formato
	private Date convertirFecha(String fecha) throws ParseException {
		Date fechaTranformada = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss aa").parse(fecha); 		
		return fechaTranformada;
	}
	
	
	//Se usa para convertir una cadena String a fecha Date con formato
	private Date convertirFechaReporte(String fecha) throws ParseException {
		Date fechaTranformada = new SimpleDateFormat("dd-MM-yyyy").parse(fecha); 		
		return fechaTranformada;
	}
	
	//Se usa para extraer el codigo del diagnostico cie10
	private String recortarDiagnostico(String diagnostico) {
		String[] parts = diagnostico.split("-");
		String part1 = parts[0]; 
		//String part2 = parts[1]; 
		return part1.trim();
	}

	
	// Se usa para sincronizar el paciente de dinamica a solution en caso de que no exista
	private void sincronizarPaciente(GenPacien validarPaciente, String pacNumDoc) {		
		
			// proceso API para buscar el paciente
			ResponseEntity<List<GenPacienDTO>> respuesta = restTemplate.exchange(URLPaciente + '/' + pacNumDoc, HttpMethod.GET, null, new ParameterizedTypeReference<List<GenPacienDTO>>() {});
			List<GenPacienDTO> dinamica = respuesta.getBody();

			// buscamos el sexo del paciente
			ComGenero sexoPaciente = iComGeneroService.findById(dinamica.get(0).getGpasexpac().longValue());

			// buscamos el tipo de documento del paciente
			ComTipoDocumento tipoDocumento = iComTipoDocumentoService.findById(dinamica.get(0).getPacTipDoc().longValue());

			GenPacien agregarPaciente = new GenPacien();
			agregarPaciente.setOid(dinamica.get(0).getOid());
			agregarPaciente.setPacNumDoc(dinamica.get(0).getPacNumDoc());
			agregarPaciente.setPacPriNom(dinamica.get(0).getPacPriNom());
			agregarPaciente.setPacSegNom(dinamica.get(0).getPacSegNom());
			agregarPaciente.setPacPriApe(dinamica.get(0).getPacPriApe());
			agregarPaciente.setPacSegApe(dinamica.get(0).getPacSegApe());
			agregarPaciente.setGpafecnac(dinamica.get(0).getGpafecnac());
			agregarPaciente.setComGenero(sexoPaciente);
			agregarPaciente.setComTipoDocumento(tipoDocumento);
			iGenPacienService.save(agregarPaciente);
			
	}
	
	// Se usa para sincronizar y asignar el id de la mortalidad al objeto EstAsistentes
	private void sincronizarDetalleAsistente(List<EstAsistente> estAsistentes, EstMortalidad estMortalidad) {
		
		//elimino posiciones vacias
		estAsistentes.removeIf(e -> e.getNombre().isEmpty() && e.getActividad().isEmpty());		
		
		for (EstAsistente estAsistenteDetail : estAsistentes) {			
			estAsistenteDetail.setEstMortalidad(estMortalidad);			
		}	
	}
	
	// Se usa para sincronizar y asignar el id de la mortalidad al objeto EstRetraso
	private void sincronizarDetalleRetraso(List<EstRetraso> estRetrasos, EstMortalidad estMortalidad) {
		
		//elimino posiciones vacias
		estRetrasos.removeIf(e -> e.getComRetraso() == null && e.getObservacion() == null);				
		
		for (EstRetraso estRetrasoDetail : estRetrasos) {			
			estRetrasoDetail.setEstMortalidad(estMortalidad);			
		}			
				
	}
	
	// Se usa para sincronizar y asignar el id de la mortalidad al objeto EstCausa
	private void sincronizarDetalleCausa(List<EstCausa> estCausas, EstMortalidad estMortalidad) {
		
		//elimino posiciones vacias
		estCausas.removeIf(e -> e.getCausaDirecta() == null && e.getCausaAntedecente() == null);	
		
		for (EstCausa estCausaDetail : estCausas) {			
			estCausaDetail.setEstMortalidad(estMortalidad);			
		}
		
	}
	
	//filtro para guardar el predictor seleccionado
	private void filtroPredictor(String escala, EstMortalidad estMortalidad) {
		
		if(escala.equals("0")) {
			estMortalidad.setComPrism(null);				
		}
		if(escala.equals("1")) {
			estMortalidad.setComApache(null);
		}
			
	}
	
	//Se usa para crear el archivo en EXCEL
	private void crearExcel(List<EstMortalidad> listadoMortalidad, HttpServletResponse response) {
		//EJEMPLO DE RANGOS EN FILAS Y COLUMNAS
		//sheet.addMergedRegion(new CellRangeAddress(fila_inicial, fila_final, columna_inicial, columna_final))		
				
		//Se crea variable para el nombre del archivo de EXCEL
		String fileName = "consolidado_mortalidad.xlsx";
				
				
		// 1. Se crea el libro XLSX
		// Umbral, el número máximo de objetos en la memoria, más allá del cual se genera y almacena un archivo temporal en el disco duro
		SXSSFWorkbook workbook = new SXSSFWorkbook(); //new HSSFWorkbook() para generar archivos `.xls`
				
		//OPCIONAL
		//CreationHelper nos ayuda a crear instancias o utilerias para formatos especiales como DataFormat, Hyperlink, RichTextString, etc., en un formato (HSSF, XSSF) de forma independiente
		CreationHelper createHelper = workbook.getCreationHelper();
			
		//2.Se crea una hoja dentro del libro asignando un nombre
		Sheet sheet = workbook.createSheet("Reporte_Mortalidad");				
				
		//3. Establecer el estilo y el estilo de fuente
		CellStyle titleStyle = ExcelUtils.createTitleCellStyle(workbook);
		CellStyle headerStyle = ExcelUtils.createHeadCellStyle(workbook);
		CellStyle contentStyle = ExcelUtils.createContentCellStyle(workbook);
		        
		        
		//4. Crear título y combinar las celdas para el título
		//Número de línea
		int rowNum = 0;
		        
		//Primer elemento
		//Crear primera fila con índice a partir de 0 (fila de encabezado)
		Row row0 = sheet.createRow(rowNum++);
		row0.setHeight((short) 500);// Establecer altura de fila
		String title = "CONSOLIDADO MORTALIDAD";
		Cell c00 = row0.createCell(0);
		c00.setCellValue(title);
		c00.setCellStyle(titleStyle);
		// Combinar celdas. Los parámetros son fila inicial, fila final, columna inicial y columna final (comienza el índice 0)
		sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 32));//Operación celda de combinación en encabezado, el número total de columnas es 28
		
		
		//Segundo elemento       
        Row row1 = sheet.createRow(rowNum++);
        //sheet.setColumnWidth(3, 25 * 256);
        row1.setHeight((short)450);        
        String[] row_first = {"","","","","","","DIAGNOSTICO SEGUN RIPS","","CAUSA DIRECTA","","CAUSA ANTECEDENTES","","","","","","RETRASO","","","","OBSERVACION","","","","","APLICA PLAN DE MEJORA","","","","","","",""};
        for (int i = 0; i < row_first.length; i++) {
            Cell tempCell = row1.createCell(i);
            tempCell.setCellValue(row_first[i]);
            tempCell.setCellStyle(headerStyle);            
        }
        
        sheet.addMergedRegion(new CellRangeAddress(1, 1, 6, 7));//DIAGNOSTICO SEGUN RIPS
        sheet.addMergedRegion(new CellRangeAddress(1, 1, 8, 9));//CAUSA DIRECTA
        sheet.addMergedRegion(new CellRangeAddress(1, 1, 10, 14));//CAUSA ANTECEDENTES
        sheet.addMergedRegion(new CellRangeAddress(1, 1, 16, 19));//RETRASO
        sheet.addMergedRegion(new CellRangeAddress(1, 1, 20, 23));//OBSERVACION
        sheet.addMergedRegion(new CellRangeAddress(1, 1, 25, 28));//APLICA PLAN DE MEJORA        
        
        
      //Tercer elemento        
        Row row2 = sheet.createRow(rowNum++);
        row2.setHeight((short)1250);
        
        String[] row_second = {"SERVICIO","HISTORIA","EDAD","SEXO","FECHA DE INGRESO","FECHA DEFUNCION","CODIGO","DESCRIPCION","CODIGO","DESCRIPCION","CODIGO","DESCRIPCION","OTRO ANTECEDENTE","OTRO ANTECEDENTE","OTROS ESTADOS PATOLÓGICOS","RESUMEN DEL CASO","1","2","3","4","1","2","3","4","ANALISIS","APLICA","TIEMPO","ACCION","RESPONSABLE","CODIGO LILA","CODIGO BLANCO","APACHE","PRISM"};
        for (int i = 0; i < row_second.length; i++) {
            Cell tempCell = row2.createCell(i);
            tempCell.setCellValue(row_second[i]);
            tempCell.setCellStyle(headerStyle);
            //esta linea me permite ajustar el ancho
            sheet.setColumnWidth(i, sheet.getColumnWidth(i) * 20 / 10);            
            
        }
        
        
       //5. Agrego el contenido desde un arraylist al EXCEL
        
        //for(EstMortalidad mortalidad: listadoMortalidad) {
        
        for(int i=0; i<listadoMortalidad.size(); i++) {       
        	
        	Row tempRow = sheet.createRow(rowNum++);
            tempRow.setHeight((short) 800);
            // Recorrido para relleno de celdas
            for (int j = 0; j < 33; j++) {
            	
            	Cell tempCell = tempRow.createCell(j);
                tempCell.setCellStyle(contentStyle);
                String tempValue = "";
                if (j == 0) {
                    // Servicio
                    tempValue = listadoMortalidad.get(i).getGenAreSer().getGasNombre();                    
                }
                else if(j == 1) {
                	// Historia
                    tempValue = listadoMortalidad.get(i).getGenPacien().getPacNumDoc();
                }
                else if(j == 2) {
                	// Edad
                	String edad = calcularEdad(listadoMortalidad.get(i).getGenPacien().getGpafecnac());
                    tempValue = edad;
                }
                else if(j == 3) {
                	// Sexo
                    tempValue = listadoMortalidad.get(i).getGenPacien().getComGenero().getNombre();
                }
                else if(j == 4) {
                	// Fecha Ingreso
                	String fechaIngreso = formatoFecha(listadoMortalidad.get(i).getFechaIngreso());
                    tempValue = fechaIngreso;
                }
                else if(j == 5) {
                	// Fecha Defuncion
                	String fechaDefuncion = formatoFecha(listadoMortalidad.get(i).getFechaDefuncion());
                    tempValue = fechaDefuncion;
                }
                else if(j == 6) {
                	// Codigo Diagnostico Segun Rips
                	String codigo = listadoMortalidad.get(i).getComCie10().getCodigo();
                    tempValue = codigo;
                }
                else if(j == 7) {
                	// Nombre Diagnostico Segun Rips
                	String nombre = listadoMortalidad.get(i).getComCie10().getNombre();
                    tempValue = nombre;
                }
                else if(j == 8) {
                	// Codigo Diagnostico Causa Directa
                	if(listadoMortalidad.get(i).getEstCausas().isEmpty()) {
                		tempValue = "";
                	}else {
                		if(listadoMortalidad.get(i).getEstCausas().get(0).getCausaDirecta() != null) {
                			String codigo = listadoMortalidad.get(i).getEstCausas().get(0).getCausaDirecta().getCodigo();
                			tempValue = codigo;
                		}else {
                			tempValue = "";
                		}                        
                	}                	                   
                }
                else if(j == 9) {
                	// Nombre Diagnostico Causa Directa
                	if(listadoMortalidad.get(i).getEstCausas().isEmpty()) {
                		tempValue = "";
                	}else {
                		if(listadoMortalidad.get(i).getEstCausas().get(0).getCausaDirecta() != null) {
                			String nombre = listadoMortalidad.get(i).getEstCausas().get(0).getCausaDirecta().getNombre();
                			tempValue = nombre;
                		}else {
                			tempValue = "";
                		}              		
                	}              	
                }
                else if(j == 10) {
                	// Codigo Diagnostico Causa Antecedentes Principal
                	if(listadoMortalidad.get(i).getEstCausas().isEmpty()) {
                		tempValue = "";
                	}else {
                		if(listadoMortalidad.get(i).getEstCausas().get(0).getCausaAntedecente() != null) {
                			String codigo = listadoMortalidad.get(i).getEstCausas().get(0).getCausaAntedecente().getCodigo();
                			tempValue = codigo;
                		}          		
                	}                	
                }
                else if(j == 11) {
                	// Nombre Diagnostico Causa Antecedentes Principal
                	if(listadoMortalidad.get(i).getEstCausas().isEmpty()) {
                		tempValue = "";
                	}else {
                		if(listadoMortalidad.get(i).getEstCausas().get(0).getCausaAntedecente() != null) {
                			String nombre = listadoMortalidad.get(i).getEstCausas().get(0).getCausaAntedecente().getNombre();
                			tempValue = nombre;
                		}else {
                			tempValue = "";
                		}          		
                	}           	
                }
                else if(j == 12) {
                	// Nombre Diagnostico Causa Antecedentes B
                	if(listadoMortalidad.get(i).getEstCausas().isEmpty()) {
                		tempValue = "";
                	}else {
                		if(!listadoMortalidad.get(i).getEstCausas().get(0).getOtraCausaA().isEmpty()) {
                			String nombre = listadoMortalidad.get(i).getEstCausas().get(0).getOtraCausaA();
                			tempValue = nombre;
                		}else {
                			tempValue = "";
                		}
                	}               	
                }
                else if(j == 13) {
                	// Nombre Diagnostico Causa Antecedentes C
                	if(listadoMortalidad.get(i).getEstCausas().isEmpty()) {
                		tempValue = "";
                	}else {
                		if(!listadoMortalidad.get(i).getEstCausas().get(0).getOtraCausaB().isEmpty()) {
                			String nombre = listadoMortalidad.get(i).getEstCausas().get(0).getOtraCausaB();
                			tempValue = nombre;
                		}else {
                			tempValue = "";
                		}
                	}            	
                }
                else if(j == 14) {
                	// Nombre Diagnostico Causa Antecedentes D
                	if(listadoMortalidad.get(i).getEstCausas().isEmpty()) {
                		tempValue = "";
                	}else {                		
                		if(!listadoMortalidad.get(i).getEstCausas().get(0).getOtros().isEmpty()) {
                			String nombre = listadoMortalidad.get(i).getEstCausas().get(0).getOtros();
                			tempValue = nombre;
                		}else {
                			tempValue = "";
                		}
                	}           	
                }
                else if(j == 15) {
                	// Resumen Del Caso
                	String resumen = listadoMortalidad.get(i).getResumenCaso();
                    tempValue = resumen;
                }
                else if(j == 16) {
                	// Retraso 1
                	if(listadoMortalidad.get(i).getEstRetrasos().isEmpty()) {
                		tempValue = "";
                	}else {              		
                		for(int k=0; k<listadoMortalidad.get(i).getEstRetrasos().size(); k++) {
                			if(k==0) {
                				String retrasouno = listadoMortalidad.get(i).getEstRetrasos().get(k).getComRetraso().getNombre();
                    			tempValue = retrasouno;
                			}                			
                		}                		               		
                	}             	
                }
                
                else if(j == 17) {
                	// Retraso 2
                	if(listadoMortalidad.get(i).getEstRetrasos().isEmpty()) {
                		tempValue = "";
                	}else {              		
                		for(int k=0; k<listadoMortalidad.get(i).getEstRetrasos().size(); k++) {
                			if(k==1) {
                				String retrasodos = listadoMortalidad.get(i).getEstRetrasos().get(k).getComRetraso().getNombre();
                    			tempValue = retrasodos;
                			}                			
                		}                		               		
                	}             	
                }
                
                else if(j == 18) {
                	// Retraso 3
                	if(listadoMortalidad.get(i).getEstRetrasos().isEmpty()) {
                		tempValue = "";
                	}else {              		
                		for(int k=0; k<listadoMortalidad.get(i).getEstRetrasos().size(); k++) {
                			if(k==2) {
                				String retrasotres = listadoMortalidad.get(i).getEstRetrasos().get(k).getComRetraso().getNombre();
                    			tempValue = retrasotres;
                			}                			
                		}                		               		
                	}             	
                }
                
                else if(j == 19) {
                	// Retraso 4
                	if(listadoMortalidad.get(i).getEstRetrasos().isEmpty()) {
                		tempValue = "";
                	}else {              		
                		for(int k=0; k<listadoMortalidad.get(i).getEstRetrasos().size(); k++) {
                			if(k==3) {
                				String retrasocuatro = listadoMortalidad.get(i).getEstRetrasos().get(k).getComRetraso().getNombre();
                    			tempValue = retrasocuatro;
                			}                			
                		}                		               		
                	}             	
                }
                
                else if(j == 20) {
                	// Observacion Retraso 1
                	if(listadoMortalidad.get(i).getEstRetrasos().isEmpty()) {
                		tempValue = "";
                	}else {              		
                		for(int k=0; k<listadoMortalidad.get(i).getEstRetrasos().size(); k++) {
                			if(k==0) {
                				String retrasoobservacionuno = listadoMortalidad.get(i).getEstRetrasos().get(k).getObservacion();
                    			tempValue = retrasoobservacionuno;
                			}                			
                		}                		               		
                	}             	
                }
               
                
                else if(j == 21) {
                	// Observacion Retraso 2
                	if(listadoMortalidad.get(i).getEstRetrasos().isEmpty()) {
                		tempValue = "";
                	}else {              		
                		for(int k=0; k<listadoMortalidad.get(i).getEstRetrasos().size(); k++) {
                			if(k==1) {
                				String retrasoobservaciondos = listadoMortalidad.get(i).getEstRetrasos().get(k).getObservacion();
                    			tempValue = retrasoobservaciondos;
                			}                			
                		}                		               		
                	}             	
                }
                
                else if(j == 22) {
                	// Observacion Retraso 3
                	if(listadoMortalidad.get(i).getEstRetrasos().isEmpty()) {
                		tempValue = "";
                	}else {              		
                		for(int k=0; k<listadoMortalidad.get(i).getEstRetrasos().size(); k++) {
                			if(k==2) {
                				String retrasoobservaciontres = listadoMortalidad.get(i).getEstRetrasos().get(k).getObservacion();
                    			tempValue = retrasoobservaciontres;
                			}                			
                		}                		               		
                	}             	
                }
                
                else if(j == 23) {
                	// Observacion Retraso 4
                	if(listadoMortalidad.get(i).getEstRetrasos().isEmpty()) {
                		tempValue = "";
                	}else {              		
                		for(int k=0; k<listadoMortalidad.get(i).getEstRetrasos().size(); k++) {
                			if(k==3) {
                				String retrasoobservacioncuatro = listadoMortalidad.get(i).getEstRetrasos().get(k).getObservacion();
                    			tempValue = retrasoobservacioncuatro;
                			}                			
                		}                		               		
                	}             	
                }                        
                
                else if(j == 24) {
                	// Analisis
                	String analisis = listadoMortalidad.get(i).getAnalisis();
                    tempValue = analisis;
                }
                else if(j == 25) {
                	// Plan De Mejora
                	String planmejora = listadoMortalidad.get(i).getPlanMejora();
                    tempValue = planmejora;
                }
                else if(j == 26) {
                	// Plan De Mejora Tiempo
                	String planmejoratiempo = listadoMortalidad.get(i).getTiempo();
                    tempValue = planmejoratiempo;
                }
                else if(j == 27) {
                	// Plan De Mejora Accion
                	String planmejoraaccion = listadoMortalidad.get(i).getAccion();
                    tempValue = planmejoraaccion;
                }
                else if(j == 28) {
                	// Plan De Mejora Responsable
                	String planmejoraresponsable = listadoMortalidad.get(i).getResponsable();
                    tempValue = planmejoraresponsable;
                }
                else if(j == 29) {
                	// Codigo Lila
                	String codigolila = validarBooleano(listadoMortalidad.get(i).isCodigoLila());            	
                    tempValue = codigolila;
                }
                else if(j == 30) {
                	// Codigo Blanco
                	String codigoblanco = validarBooleano(listadoMortalidad.get(i).isCodigoBlanco());            	
                    tempValue = codigoblanco;
                }
                else if(j == 31) {
                	// Apache
                	String apache = listadoMortalidad.get(i).getComApache().getNombre();            	
                    tempValue = apache;
                }
                else if(j == 32) {
                	// Prism
                	String prism = listadoMortalidad.get(i).getComPrism().getNombre();            	
                    tempValue = prism;
                }
                
                // Creamos la celda con el contenido
                tempCell.setCellValue(tempValue);
            	
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
	
	//Se usa para crear el reporte en PDF
	private void crearPDF(List<EstMortalidad> mortalidad, HttpServletResponse response) throws JRException, IOException {
		
		//parametros adicionales para el PDF
		Map<String, Object> parameters = new HashMap<>();
		
		// Obteniendo el archivo .jrxml de la carpeta de recursos.
		InputStream jrxmlInput = this.getClass().getResourceAsStream("/reports/reportemortalidad.jrxml");
				
		// Compilo el informe Jasper de .jrxml a .jasper
		JasperReport jasperReport = JasperCompileManager.compileReport(jrxmlInput);
				
		// Obteniendo a las prescipciones de la fuente de datos.
		JRBeanCollectionDataSource source = new JRBeanCollectionDataSource(mortalidad);
		
		//obtengo la edad completa
		String edad = calcularEdad(mortalidad.get(0).getGenPacien().getGpafecnac());
		
		//obtengo rutas de imagenes para convertir
		BufferedImage logo = ImageIO.read(getClass().getResource("/static/dist/img/logohusj.png"));
		BufferedImage check = ImageIO.read(getClass().getResource("/static/dist/img/check.png"));
				
		//obtengo nombre de quien diligencio el analisis		
		ComUsuario usuario =  iComUsuarioService.findByUsuario(mortalidad.get(0).getLoginUsrAlta());	
		
		// Agregar los parámetros adicionales al pdf.
		parameters.put("logo", logo);
		parameters.put("check", check);		
		parameters.put("edad", edad);
		parameters.put("estAsistentes", mortalidad.get(0).getEstAsistentes());
		parameters.put("estRetrasos", mortalidad.get(0).getEstRetrasos());
		parameters.put("estCausas", mortalidad.get(0).getEstCausas());
		parameters.put("nombreDiligencio", usuario.getNombreCompleto());
		
		// Rellenar el informe con los datos de la prescripcion y la información de parámetros adicionales.
		JasperPrint jasperPrint  = JasperFillManager.fillReport(jasperReport, parameters, source);
				
				
		//este me permite exportar y abrir dialogo para guardar el archivo
		String fileName = mortalidad.get(0).getGenPacien().getPacNumDoc()+".pdf";
		response.addHeader("Content-Disposition", "attachment;filename=\"" + fileName + "\"");
		response.setContentType("application/pdf");
		ServletOutputStream servletOutputStream = response.getOutputStream();
		JasperExportManager.exportReportToPdfStream(jasperPrint,servletOutputStream);
		servletOutputStream.flush();
		servletOutputStream.close();
		
	}

	//me sirve para convertir el booleano en un string SI o NO
	private String validarBooleano(boolean codigo) {
		if(String.valueOf(codigo).equals("false")) {
			return "NO";
		}else {
			return "SI";
		}		
	}
	
}