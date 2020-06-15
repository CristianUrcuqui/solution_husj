package com.chapumix.solution.app.controllers;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

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
import com.chapumix.solution.app.models.entity.AtenEncuDatoBasico;
import com.chapumix.solution.app.models.entity.GenAreSer;
import com.chapumix.solution.app.models.service.IAtenEncuDatoBasicoService;
import com.chapumix.solution.app.models.service.IComGeneroService;
import com.chapumix.solution.app.models.service.IGenAreSerService;
import com.chapumix.solution.app.utils.ExcelUtils;


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
	private IGenAreSerService iGenAreSerService;
	
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
				
		model.put("titulo", utf8(this.tituloencuesta));
		model.put("servicio", iGenAreSerService.findAll());
		model.put("genero", iComGeneroService.findAll());		
		model.put("atenEncuDatoBasico", atenEncuDatoBasico);
		model.put("siau", enlaceprincipalsiau);
		model.put("enlace9", enlace9);
		return "encuestaform";
	}
	
	
	// Este metodo me permite guardar la encuesta
	@RequestMapping(value = "/encuestaform", method = RequestMethod.POST)
	public String guardarRol(@Valid AtenEncuDatoBasico atenEncuDatoBasico, BindingResult result, Model model, Principal principal, RedirectAttributes flash, SessionStatus status) {
		
		if (result.hasErrors()) {
			model.addAttribute("titulo", utf8(this.tituloencuestasatisfaccion));
			model.addAttribute("servicio", iGenAreSerService.findAll());
			model.addAttribute("genero", iComGeneroService.findAll());			
			model.addAttribute("atenEncuDatoBasico", atenEncuDatoBasico);			
			model.addAttribute("enlace9", enlace9);
			return "encuestaform";
		}		

		String mensajeFlash = (atenEncuDatoBasico.getId() != null) ? "La encuesta fue editada correctamente" : "La encuesta fue creada correctamente";

		atenEncuDatoBasico.setLoginUsrAlta(principal.getName());
		iAtenEncuDatoBasicoService.save(atenEncuDatoBasico);
		status.setComplete();
		flash.addFlashAttribute("success", mensajeFlash);
		return "redirect:encuestaform";
	}
	
	
	
	  // Este metodo me permite generar el consolidad de encuestas
	  
	  @RequestMapping("/buscarconsolidadosiau") public String listarConsolidado(Model model, @RequestParam(value = "fechaInicial", required = false) String fechaInicial, @RequestParam(value = "fechaFinal", required = false) String fechaFinal, RedirectAttributes flash, HttpServletResponse response) throws ParseException {
	  
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
				
				List<GenAreSer> servicio = iGenAreSerService.findAll();
				
				servicio.forEach(s -> {
					
					List<AtenEncuDatoBasico> atenEncuDatoBasico = iAtenEncuDatoBasicoService.findByStartDateBetween(fechaI, fechaF);			
					
					//obtenemos el numero de encuentas
					long numeroEncuestas = atenEncuDatoBasico.stream().filter(a -> s.getId().equals(a.getGenAreSer().getId())).count();
					
					//obtenemos consolidado pregunta 1
					long totalPregunta1Si = atenEncuDatoBasico.stream().filter(a -> s.getId().equals(a.getGenAreSer().getId()) && "1".equals(a.getRespuesta1())).count();
					long totalPregunta1No = atenEncuDatoBasico.stream().filter(a -> s.getId().equals(a.getGenAreSer().getId()) && "0".equals(a.getRespuesta1())).count();
					
					//obtenemos consolidado pregunta 2
					long totalPregunta2MuyMala = atenEncuDatoBasico.stream().filter(a -> s.getId().equals(a.getGenAreSer().getId()) && "3".equals(a.getRespuesta2())).count();
					long totalPregunta2Mala = atenEncuDatoBasico.stream().filter(a -> s.getId().equals(a.getGenAreSer().getId()) && "4".equals(a.getRespuesta2())).count();
					long totalPregunta2Regular = atenEncuDatoBasico.stream().filter(a -> s.getId().equals(a.getGenAreSer().getId()) && "5".equals(a.getRespuesta2())).count();
					long totalPregunta2Buena = atenEncuDatoBasico.stream().filter(a -> s.getId().equals(a.getGenAreSer().getId()) && "6".equals(a.getRespuesta2())).count();
					long totalPregunta2MuyBuena = atenEncuDatoBasico.stream().filter(a -> s.getId().equals(a.getGenAreSer().getId()) && "7".equals(a.getRespuesta2())).count();
					long totalPregunta2NoAplica = atenEncuDatoBasico.stream().filter(a -> s.getId().equals(a.getGenAreSer().getId()) && "2".equals(a.getRespuesta2())).count();
					
					//obtenemos consolidado pregunta 3
					long totalPregunta3MuyMala = atenEncuDatoBasico.stream().filter(a -> s.getId().equals(a.getGenAreSer().getId()) && "3".equals(a.getRespuesta3())).count();
					long totalPregunta3Mala = atenEncuDatoBasico.stream().filter(a -> s.getId().equals(a.getGenAreSer().getId()) && "4".equals(a.getRespuesta3())).count();
					long totalPregunta3Regular = atenEncuDatoBasico.stream().filter(a -> s.getId().equals(a.getGenAreSer().getId()) && "5".equals(a.getRespuesta3())).count();
					long totalPregunta3Buena = atenEncuDatoBasico.stream().filter(a -> s.getId().equals(a.getGenAreSer().getId()) && "6".equals(a.getRespuesta3())).count();
					long totalPregunta3MuyBuena = atenEncuDatoBasico.stream().filter(a -> s.getId().equals(a.getGenAreSer().getId()) && "7".equals(a.getRespuesta3())).count();
					long totalPregunta3NoAplica = atenEncuDatoBasico.stream().filter(a -> s.getId().equals(a.getGenAreSer().getId()) && "2".equals(a.getRespuesta3())).count();
					
					//obtenemos consolidado pregunta 4
					long totalPregunta4MuyMala = atenEncuDatoBasico.stream().filter(a -> s.getId().equals(a.getGenAreSer().getId()) && "3".equals(a.getRespuesta4())).count();
					long totalPregunta4Mala = atenEncuDatoBasico.stream().filter(a -> s.getId().equals(a.getGenAreSer().getId()) && "4".equals(a.getRespuesta4())).count();
					long totalPregunta4Regular = atenEncuDatoBasico.stream().filter(a -> s.getId().equals(a.getGenAreSer().getId()) && "5".equals(a.getRespuesta4())).count();
					long totalPregunta4Buena = atenEncuDatoBasico.stream().filter(a -> s.getId().equals(a.getGenAreSer().getId()) && "6".equals(a.getRespuesta4())).count();
					long totalPregunta4MuyBuena = atenEncuDatoBasico.stream().filter(a -> s.getId().equals(a.getGenAreSer().getId()) && "7".equals(a.getRespuesta4())).count();
					long totalPregunta4NoAplica = atenEncuDatoBasico.stream().filter(a -> s.getId().equals(a.getGenAreSer().getId()) && "2".equals(a.getRespuesta4())).count();
					
					//obtenemos consolidado pregunta 5
					long totalPregunta5MuyMala = atenEncuDatoBasico.stream().filter(a -> s.getId().equals(a.getGenAreSer().getId()) && "3".equals(a.getRespuesta5())).count();
					long totalPregunta5Mala = atenEncuDatoBasico.stream().filter(a -> s.getId().equals(a.getGenAreSer().getId()) && "4".equals(a.getRespuesta5())).count();
					long totalPregunta5Regular = atenEncuDatoBasico.stream().filter(a -> s.getId().equals(a.getGenAreSer().getId()) && "5".equals(a.getRespuesta5())).count();
					long totalPregunta5Buena = atenEncuDatoBasico.stream().filter(a -> s.getId().equals(a.getGenAreSer().getId()) && "6".equals(a.getRespuesta5())).count();
					long totalPregunta5MuyBuena = atenEncuDatoBasico.stream().filter(a -> s.getId().equals(a.getGenAreSer().getId()) && "7".equals(a.getRespuesta5())).count();
					long totalPregunta5NoAplica = atenEncuDatoBasico.stream().filter(a -> s.getId().equals(a.getGenAreSer().getId()) && "2".equals(a.getRespuesta5())).count();
					
					//obtenemos consolidado pregunta 6
					long totalPregunta6MuyMala = atenEncuDatoBasico.stream().filter(a -> s.getId().equals(a.getGenAreSer().getId()) && "3".equals(a.getRespuesta6())).count();
					long totalPregunta6Mala = atenEncuDatoBasico.stream().filter(a -> s.getId().equals(a.getGenAreSer().getId()) && "4".equals(a.getRespuesta6())).count();
					long totalPregunta6Regular = atenEncuDatoBasico.stream().filter(a -> s.getId().equals(a.getGenAreSer().getId()) && "5".equals(a.getRespuesta6())).count();
					long totalPregunta6Buena = atenEncuDatoBasico.stream().filter(a -> s.getId().equals(a.getGenAreSer().getId()) && "6".equals(a.getRespuesta6())).count();
					long totalPregunta6MuyBuena = atenEncuDatoBasico.stream().filter(a -> s.getId().equals(a.getGenAreSer().getId()) && "7".equals(a.getRespuesta6())).count();
					
					//obtenemos consolidado pregunta 7
					long totalPregunta7MuyMala = atenEncuDatoBasico.stream().filter(a -> s.getId().equals(a.getGenAreSer().getId()) && "3".equals(a.getRespuesta7())).count();
					long totalPregunta7Mala = atenEncuDatoBasico.stream().filter(a -> s.getId().equals(a.getGenAreSer().getId()) && "4".equals(a.getRespuesta7())).count();
					long totalPregunta7Regular = atenEncuDatoBasico.stream().filter(a -> s.getId().equals(a.getGenAreSer().getId()) && "5".equals(a.getRespuesta7())).count();
					long totalPregunta7Buena = atenEncuDatoBasico.stream().filter(a -> s.getId().equals(a.getGenAreSer().getId()) && "6".equals(a.getRespuesta7())).count();
					long totalPregunta7MuyBuena = atenEncuDatoBasico.stream().filter(a -> s.getId().equals(a.getGenAreSer().getId()) && "7".equals(a.getRespuesta7())).count();
					
					//obtenemos consolidado pregunta 8
					long totalPregunta8MuyMala = atenEncuDatoBasico.stream().filter(a -> s.getId().equals(a.getGenAreSer().getId()) && "3".equals(a.getRespuesta8())).count();
					long totalPregunta8Mala = atenEncuDatoBasico.stream().filter(a -> s.getId().equals(a.getGenAreSer().getId()) && "4".equals(a.getRespuesta8())).count();
					long totalPregunta8Regular = atenEncuDatoBasico.stream().filter(a -> s.getId().equals(a.getGenAreSer().getId()) && "5".equals(a.getRespuesta8())).count();
					long totalPregunta8Buena = atenEncuDatoBasico.stream().filter(a -> s.getId().equals(a.getGenAreSer().getId()) && "6".equals(a.getRespuesta8())).count();
					long totalPregunta8MuyBuena = atenEncuDatoBasico.stream().filter(a -> s.getId().equals(a.getGenAreSer().getId()) && "7".equals(a.getRespuesta8())).count();
					long totalPregunta8NoAplica = atenEncuDatoBasico.stream().filter(a -> s.getId().equals(a.getGenAreSer().getId()) && "2".equals(a.getRespuesta8())).count();
					
					//obtenemos consolidado pregunta 9
					long totalPregunta9MuyMala = atenEncuDatoBasico.stream().filter(a -> s.getId().equals(a.getGenAreSer().getId()) && "3".equals(a.getRespuesta9())).count();
					long totalPregunta9Mala = atenEncuDatoBasico.stream().filter(a -> s.getId().equals(a.getGenAreSer().getId()) && "4".equals(a.getRespuesta9())).count();
					long totalPregunta9Regular = atenEncuDatoBasico.stream().filter(a -> s.getId().equals(a.getGenAreSer().getId()) && "5".equals(a.getRespuesta9())).count();
					long totalPregunta9Buena = atenEncuDatoBasico.stream().filter(a -> s.getId().equals(a.getGenAreSer().getId()) && "6".equals(a.getRespuesta9())).count();
					long totalPregunta9MuyBuena = atenEncuDatoBasico.stream().filter(a -> s.getId().equals(a.getGenAreSer().getId()) && "7".equals(a.getRespuesta9())).count();
					long totalPregunta9NoAplica = atenEncuDatoBasico.stream().filter(a -> s.getId().equals(a.getGenAreSer().getId()) && "2".equals(a.getRespuesta9())).count();
					
					//obtenemos consolidado pregunta 10
					long totalPregunta10Si = atenEncuDatoBasico.stream().filter(a -> s.getId().equals(a.getGenAreSer().getId()) && "1".equals(a.getRespuesta10())).count();
					long totalPregunta10No = atenEncuDatoBasico.stream().filter(a -> s.getId().equals(a.getGenAreSer().getId()) && "0".equals(a.getRespuesta10())).count();
					long totalPregunta10NoAplica = atenEncuDatoBasico.stream().filter(a -> s.getId().equals(a.getGenAreSer().getId()) && "2".equals(a.getRespuesta10())).count();
					
					//obtenemos consolidado pregunta 11
					long totalPregunta11MuyMala = atenEncuDatoBasico.stream().filter(a -> s.getId().equals(a.getGenAreSer().getId()) && "3".equals(a.getRespuesta11())).count();
					long totalPregunta11Mala = atenEncuDatoBasico.stream().filter(a -> s.getId().equals(a.getGenAreSer().getId()) && "4".equals(a.getRespuesta11())).count();
					long totalPregunta11Regular = atenEncuDatoBasico.stream().filter(a -> s.getId().equals(a.getGenAreSer().getId()) && "5".equals(a.getRespuesta11())).count();
					long totalPregunta11Buena = atenEncuDatoBasico.stream().filter(a -> s.getId().equals(a.getGenAreSer().getId()) && "6".equals(a.getRespuesta11())).count();
					long totalPregunta11MuyBuena = atenEncuDatoBasico.stream().filter(a -> s.getId().equals(a.getGenAreSer().getId()) && "7".equals(a.getRespuesta11())).count();
					
					//obtenemos consolidado pregunta 12
					long totalDefinitivamenteNo12 = atenEncuDatoBasico.stream().filter(a -> s.getId().equals(a.getGenAreSer().getId()) && "8".equals(a.getRespuesta12())).count();
					long totalProbablementeNo12 = atenEncuDatoBasico.stream().filter(a -> s.getId().equals(a.getGenAreSer().getId()) && "9".equals(a.getRespuesta12())).count();
					long totalProbablementeSi12 = atenEncuDatoBasico.stream().filter(a -> s.getId().equals(a.getGenAreSer().getId()) && "10".equals(a.getRespuesta12())).count();
					long totalDefinitivamenteSi12 = atenEncuDatoBasico.stream().filter(a -> s.getId().equals(a.getGenAreSer().getId()) && "11".equals(a.getRespuesta12())).count();
					
					
					//creamos un arreglo nuevo con la informacion consolidada
					if(numeroEncuestas > 0) {
						atenEncuConsolidadoDTO.add(new AtenEncuConsolidadoDTO(s.getGasNombre(), Long.toString(numeroEncuestas), Long.toString(totalPregunta1Si), Long.toString(totalPregunta1No), Long.toString(totalPregunta2MuyMala), Long.toString(totalPregunta2Mala), Long.toString(totalPregunta2Regular), Long.toString(totalPregunta2Buena), Long.toString(totalPregunta2MuyBuena), Long.toString(totalPregunta2NoAplica), 
								Long.toString(totalPregunta3MuyMala), Long.toString(totalPregunta3Mala), Long.toString(totalPregunta3Regular), Long.toString(totalPregunta3Buena), Long.toString(totalPregunta3MuyBuena), Long.toString(totalPregunta3NoAplica), Long.toString(totalPregunta4MuyMala), Long.toString(totalPregunta4Mala), Long.toString(totalPregunta4Regular), Long.toString(totalPregunta4Buena), Long.toString(totalPregunta4MuyBuena), Long.toString(totalPregunta4NoAplica),
								Long.toString(totalPregunta5MuyMala), Long.toString(totalPregunta5Mala), Long.toString(totalPregunta5Regular), Long.toString(totalPregunta5Buena), Long.toString(totalPregunta5MuyBuena), Long.toString(totalPregunta5NoAplica), Long.toString(totalPregunta6MuyMala), Long.toString(totalPregunta6Mala), Long.toString(totalPregunta6Regular), Long.toString(totalPregunta6Buena), Long.toString(totalPregunta6MuyBuena),
								Long.toString(totalPregunta7MuyMala), Long.toString(totalPregunta7Mala), Long.toString(totalPregunta7Regular), Long.toString(totalPregunta7Buena), Long.toString(totalPregunta7MuyBuena), Long.toString(totalPregunta8MuyMala), Long.toString(totalPregunta8Mala), Long.toString(totalPregunta8Regular), Long.toString(totalPregunta8Buena), Long.toString(totalPregunta8MuyBuena), Long.toString(totalPregunta8NoAplica),
								Long.toString(totalPregunta9MuyMala), Long.toString(totalPregunta9Mala), Long.toString(totalPregunta9Regular), Long.toString(totalPregunta9Buena), Long.toString(totalPregunta9MuyBuena), Long.toString(totalPregunta9NoAplica), Long.toString(totalPregunta10Si), Long.toString(totalPregunta10No), Long.toString(totalPregunta10NoAplica),
								Long.toString(totalPregunta11MuyMala), Long.toString(totalPregunta11Mala), Long.toString(totalPregunta11Regular), Long.toString(totalPregunta11Buena), Long.toString(totalPregunta11MuyBuena), Long.toString(totalDefinitivamenteNo12), Long.toString(totalProbablementeNo12), Long.toString(totalProbablementeSi12), Long.toString(totalDefinitivamenteSi12)));						
					}					
					
				});		
				
			}
			
			try {
				//creamos el reporte en EXCEL
				crearExcel(atenEncuConsolidadoDTO, response);						
				
			} catch (IOException e) {						
				e.printStackTrace();
			}
			
			String mensajeReporte = "Reporte generado correctamente en el escritorio";
			
			//model.addAttribute("medicos", medicos);
			model.addAttribute("titulo", utf8(this.tituloencuestasatisfaccion));
			model.addAttribute("listprocpat", atenEncuConsolidadoDTO);
			model.addAttribute("enlace9", enlace9);
			model.addAttribute("success", mensajeReporte);
			return  null;
			
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
		
		
	// Se usa para convertir una cadena String a fecha Date con formato
	private Date convertirfecha(String fecha) throws ParseException {
		Date fechaTranformada = new SimpleDateFormat("dd-MM-yyyy").parse(fecha);
		return fechaTranformada;
	}	
	

	
	//Se usa para crear el archivo en EXCEL
	private void crearExcel(List<AtenEncuConsolidadoDTO> atenEncuConsolidadoDTO, HttpServletResponse response) throws IOException {		
		
		//EJEMPLO DE RANGOS EN FILAS Y COLUMNAS
		//sheet.addMergedRegion(new CellRangeAddress(fila_inicial, fila_final, columna_inicial, columna_final))		
		
		//Se crea variable para el nombre del archivo de EXCEL
        String fileName = "consolidado_encuestas.xlsx";
		
		
		// 1. Se crea el libro XLSX
        // Umbral, el número máximo de objetos en la memoria, más allá del cual se genera y almacena un archivo temporal en el disco duro
		SXSSFWorkbook workbook = new SXSSFWorkbook(); //new HSSFWorkbook() para generar archivos `.xls`
		
		//OPCIONAL
		//CreationHelper nos ayuda a crear instancias o utilerias para formatos especiales como DataFormat, Hyperlink, RichTextString, etc., en un formato (HSSF, XSSF) de forma independiente
		CreationHelper createHelper = workbook.getCreationHelper();
		
		//2.Se crea una hoja dentro del libro asignando un nombre
        Sheet sheet = workbook.createSheet("Consolidado_Encuestas");				
		
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
        String title = "CONSOLIDADO ENCUESTAS";
        Cell c00 = row0.createCell(0);
        c00.setCellValue(title);
        c00.setCellStyle(titleStyle);
        // Combinar celdas. Los parámetros son fila inicial, fila final, columna inicial y columna final (comienza el índice 0)
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 61));//Operación celda de combinación en encabezado, el número total de columnas es 16       
         
        
        //Segundo elemento       
        Row row1 = sheet.createRow(rowNum++);
        row1.setHeight((short)350);        
        String[] row_first = {"SERVICIO","NUMERO DE ENCUESTAS","PREGUNTA1","","PREGUNTA2","","","","","","PREGUNTA3","","","","","","PREGUNTA4","","","","","","PREGUNTA5","","","","","","PREGUNTA6","","","","","PREGUNTA7","","","","","PREGUNTA8","","","","","","PREGUNTA9","","","","","","PREGUNTA10","","","PREGUNTA11","","","","","PREGUNTA12"};
        for (int i = 0; i < row_first.length; i++) {
            Cell tempCell = row1.createCell(i);
            tempCell.setCellValue(row_first[i]);
            tempCell.setCellStyle(headerStyle);            
        }
        
        sheet.addMergedRegion(new CellRangeAddress(1, 2, 0, 0));//SERVICIO
        sheet.addMergedRegion(new CellRangeAddress(1, 2, 1, 1));//ENCUESTAS
        sheet.addMergedRegion(new CellRangeAddress(1, 1, 2, 3));//PREGUNTA1
        sheet.addMergedRegion(new CellRangeAddress(1, 1, 4, 9));//PREGUNTA2
        sheet.addMergedRegion(new CellRangeAddress(1, 1, 10, 15));//PREGUNTA3
        sheet.addMergedRegion(new CellRangeAddress(1, 1, 16, 21));//PREGUNTA4
        sheet.addMergedRegion(new CellRangeAddress(1, 1, 22, 27));//PREGUNTA5
        sheet.addMergedRegion(new CellRangeAddress(1, 1, 28, 32));//PREGUNTA6
        sheet.addMergedRegion(new CellRangeAddress(1, 1, 33, 37));//PREGUNTA7
        sheet.addMergedRegion(new CellRangeAddress(1, 1, 38, 43));//PREGUNTA8
        sheet.addMergedRegion(new CellRangeAddress(1, 1, 44, 49));//PREGUNTA9
        sheet.addMergedRegion(new CellRangeAddress(1, 1, 50, 52));//PREGUNTA10
        sheet.addMergedRegion(new CellRangeAddress(1, 1, 53, 57));//PREGUNTA11
        sheet.addMergedRegion(new CellRangeAddress(1, 1, 58, 61));//PREGUNTA12
       
        
        //Tercer elemento        
        Row row2 = sheet.createRow(rowNum++);
        row2.setHeight((short)350);
        String[] row_second = {"","","Si","No","Muy Mala","Mala","Regular","Buena","Muy Buena","NO APLICA","Muy Mala","Mala","Regular","Buena","Muy Buena","NO APLICA","Muy Mala","Mala","Regular","Buena","Muy Buena","NO APLICA","Muy Mala","Mala","Regular","Buena","Muy Buena","NO APLICA","Muy Mala","Mala","Regular","Buena","Muy Buena","Muy Mala","Mala","Regular","Buena","Muy Buena","Muy Mala","Mala","Regular","Buena","Muy Buena","NO APLICA","Muy Mala","Mala","Regular","Buena","Muy Buena","NO APLICA","Si","No","NO APLICA","Muy Mala","Mala","Regular","Buena","Muy Buena","Definitivamente NO","Probablemente NO","Probablemente SI","Definitivamente SI"};
        for (int i = 0; i < row_second.length; i++) {
            Cell tempCell = row2.createCell(i);
            tempCell.setCellValue(row_second[i]);
            tempCell.setCellStyle(headerStyle);
        }
        
        //5. Agrego el contenido desde un arraylist al EXCEL   
        
        for(AtenEncuConsolidadoDTO consolidado: atenEncuConsolidadoDTO) {
			
        	Row tempRow = sheet.createRow(rowNum++);
            tempRow.setHeight((short) 800);
            // Recorrido para relleno de celdas
            for (int j = 0; j < 62; j++) {
                Cell tempCell = tempRow.createCell(j);
                tempCell.setCellStyle(contentStyle);
                String tempValue = "";
                if (j == 0) {
                    // Servicio
                    tempValue = consolidado.getServicio();                 
                    //esto me sirve para calcular ajustar el ancho de la columna
                    int currentColumnWidth = sheet.getColumnWidth(j);                    
                    sheet.setColumnWidth(j, (currentColumnWidth + 550));
                }
                else if(j == 1) {
                	// Numero de encuestas
                    tempValue = consolidado.getNumeroEncuestas();             
                }
                else if(j == 2) {
                	// Pregunta 1
                    tempValue = consolidado.getTotalPregunta1Si();             
                }
                else if(j == 3) {
                	// Pregunta 1
                    tempValue = consolidado.getTotalPregunta1No();             
                }
                else if(j == 4) {
                	// Pregunta 1
                    tempValue = consolidado.getTotalPregunta2MuyMala();             
                }
                else if(j == 5) {
                	// Pregunta 2
                    tempValue = consolidado.getTotalPregunta2Mala();             
                }
                else if(j == 6) {
                	// Pregunta 2
                    tempValue = consolidado.getTotalPregunta2Regular();             
                }
                else if(j == 7) {
                	// Pregunta 2
                    tempValue = consolidado.getTotalPregunta2Buena();             
                }
                else if(j == 8) {
                	// Pregunta 2
                    tempValue = consolidado.getTotalPregunta2MuyBuena();             
                }
                else if(j == 9) {
                	// Pregunta 2
                    tempValue = consolidado.getTotalPregunta2NoAplica();             
                }
                else if(j == 10) {
                	// Pregunta 3
                    tempValue = consolidado.getTotalPregunta3MuyMala();             
                }
                else if(j == 11) {
                	// Pregunta 3
                    tempValue = consolidado.getTotalPregunta3Mala();             
                }
                else if(j == 12) {
                	// Pregunta 3
                    tempValue = consolidado.getTotalPregunta3Regular();            
                }
                else if(j == 13) {
                	// Pregunta 3
                    tempValue = consolidado.getTotalPregunta3Buena();             
                }
                else if(j == 14) {
                	// Pregunta 3
                    tempValue = consolidado.getTotalPregunta3MuyBuena();             
                }
                else if(j == 15) {
                	// Pregunta 3
                    tempValue = consolidado.getTotalPregunta3NoAplica();             
                }
                else if(j == 16) {
                	// Pregunta 4
                    tempValue = consolidado.getTotalPregunta4MuyMala();             
                }
                else if(j == 17) {
                	// Pregunta 4
                    tempValue = consolidado.getTotalPregunta4Mala();             
                }
                else if(j == 18) {
                	// Pregunta 4
                    tempValue = consolidado.getTotalPregunta4Regular();            
                }
                else if(j == 19) {
                	// Pregunta 4
                    tempValue = consolidado.getTotalPregunta4Buena();             
                }
                else if(j == 20) {
                	// Pregunta 4
                    tempValue = consolidado.getTotalPregunta4MuyBuena();             
                }
                else if(j == 21) {
                	// Pregunta 4
                    tempValue = consolidado.getTotalPregunta4NoAplica();             
                }
                else if(j == 22) {
                	// Pregunta 5
                    tempValue = consolidado.getTotalPregunta5MuyMala();             
                }
                else if(j == 23) {
                	// Pregunta 5
                    tempValue = consolidado.getTotalPregunta5Mala();             
                }
                else if(j == 24) {
                	// Pregunta 5
                    tempValue = consolidado.getTotalPregunta5Regular();            
                }
                else if(j == 25) {
                	// Pregunta 5
                    tempValue = consolidado.getTotalPregunta5Buena();             
                }
                else if(j == 26) {
                	// Pregunta 5
                    tempValue = consolidado.getTotalPregunta5MuyBuena();             
                }
                else if(j == 27) {
                	// Pregunta 5
                    tempValue = consolidado.getTotalPregunta5NoAplica();             
                }
                else if(j == 28) {
                	// Pregunta 6
                    tempValue = consolidado.getTotalPregunta6MuyMala();             
                }
                else if(j == 29) {
                	// Pregunta 6
                    tempValue = consolidado.getTotalPregunta6Mala();             
                }
                else if(j == 30) {
                	// Pregunta 6
                    tempValue = consolidado.getTotalPregunta6Regular();            
                }
                else if(j == 31) {
                	// Pregunta 6
                    tempValue = consolidado.getTotalPregunta6Buena();             
                }
                else if(j == 32) {
                	// Pregunta 6
                    tempValue = consolidado.getTotalPregunta6MuyBuena();             
                }
                else if(j == 33) {
                	// Pregunta 7
                    tempValue = consolidado.getTotalPregunta7MuyMala();             
                }
                else if(j == 34) {
                	// Pregunta 7
                    tempValue = consolidado.getTotalPregunta7Mala();             
                }
                else if(j == 35) {
                	// Pregunta 7
                    tempValue = consolidado.getTotalPregunta7Regular();            
                }
                else if(j == 36) {
                	// Pregunta 7
                    tempValue = consolidado.getTotalPregunta7Buena();             
                }
                else if(j == 37) {
                	// Pregunta 7
                    tempValue = consolidado.getTotalPregunta7MuyBuena();             
                }
                else if(j == 38) {
                	// Pregunta 8
                    tempValue = consolidado.getTotalPregunta8MuyMala();             
                }
                else if(j == 39) {
                	// Pregunta 8
                    tempValue = consolidado.getTotalPregunta8Mala();             
                }
                else if(j == 40) {
                	// Pregunta 8
                    tempValue = consolidado.getTotalPregunta8Regular();            
                }
                else if(j == 41) {
                	// Pregunta 8
                    tempValue = consolidado.getTotalPregunta8Buena();             
                }
                else if(j == 42) {
                	// Pregunta 8
                    tempValue = consolidado.getTotalPregunta8MuyBuena();             
                }
                else if(j == 43) {
                	// Pregunta 8
                    tempValue = consolidado.getTotalPregunta8NoAplica();             
                }
                else if(j == 44) {
                	// Pregunta 9
                    tempValue = consolidado.getTotalPregunta9MuyMala();             
                }
                else if(j == 45) {
                	// Pregunta 9
                    tempValue = consolidado.getTotalPregunta9Mala();             
                }
                else if(j == 46) {
                	// Pregunta 9
                    tempValue = consolidado.getTotalPregunta9Regular();            
                }
                else if(j == 47) {
                	// Pregunta 9
                    tempValue = consolidado.getTotalPregunta9Buena();             
                }
                else if(j == 48) {
                	// Pregunta 9
                    tempValue = consolidado.getTotalPregunta9MuyBuena();             
                }
                else if(j == 49) {
                	// Pregunta 9
                    tempValue = consolidado.getTotalPregunta9NoAplica();             
                }
                else if(j == 50) {
                	// Pregunta 10
                    tempValue = consolidado.getTotalPregunta10Si();             
                }
                else if(j == 51) {
                	// Pregunta 10
                    tempValue = consolidado.getTotalPregunta10No();             
                }
                else if(j == 52) {
                	// Pregunta 10
                    tempValue = consolidado.getTotalPregunta10NoAplica();             
                }
                else if(j == 53) {
                	// Pregunta 11
                    tempValue = consolidado.getTotalPregunta11MuyMala();             
                }
                else if(j == 54) {
                	// Pregunta 11
                    tempValue = consolidado.getTotalPregunta11Mala();             
                }
                else if(j == 55) {
                	// Pregunta 11
                    tempValue = consolidado.getTotalPregunta11Regular();            
                }
                else if(j == 56) {
                	// Pregunta 11
                    tempValue = consolidado.getTotalPregunta11Buena();             
                }
                else if(j == 57) {
                	// Pregunta 11
                    tempValue = consolidado.getTotalPregunta11MuyBuena();             
                }
                else if(j == 58) {
                	// Pregunta 12
                    tempValue = consolidado.getTotalPregunta12DefinitivamenteNo();             
                }
                else if(j == 59) {
                	// Pregunta 12
                    tempValue = consolidado.getTotalPregunta12ProbablementeNo();            
                }
                else if(j == 60) {
                	// Pregunta 12
                    tempValue = consolidado.getTotalPregunta12ProbablementeSi();             
                }
                else if(j == 61) {
                	// Pregunta 12
                    tempValue = consolidado.getTotalPregunta12DefinitivamenteSi();             
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

}