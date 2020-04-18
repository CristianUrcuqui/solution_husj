package com.chapumix.solution.app.controllers;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.apache.commons.io.input.BOMInputStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.chapumix.solution.app.entity.dto.CalCalendarioDTO;
import com.chapumix.solution.app.models.entity.CalCalendario;
import com.chapumix.solution.app.models.service.ICalCalendarioService;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;


@Controller
@SessionAttributes("calCalendario")
@PropertySource(value = "application.properties", encoding="UTF-8")
public class CalCalendarioController {	
	
	
	@Autowired
	private ICalCalendarioService iCalCalendarioService;
	 
	@Value("${app.titulocumpleanosempleados}")
	private String titulocumpleanosempleados;
	
	@Value("${app.tituloempleados}")
	private String tituloempleados;
	
	@Value("${app.titulohoy}")
	private String titulohoy;
	
	@Value("${app.enlaceprincipalcalidad}")
	private String enlaceprincipalcalidad;
	
	@Value("${app.enlace5}")
	private String enlace5;
	
	
	
	/* ----------------------------------------------------------
     * INDEX CALENDARIO
     * ---------------------------------------------------------- */
	
	@GetMapping("/indexcalendario")
	public String index(Model model) {
		model.addAttribute("titulo", utf8(this.titulocumpleanosempleados));
		model.addAttribute("calidad", enlaceprincipalcalidad);
		model.addAttribute("enlace5", enlace5);
		return "indexcalendario";
	}
	
	/* ----------------------------------------------------------
     * CUMPLEAÑOS EMPLEADOS
     * ---------------------------------------------------------- */
	
	// Este metodo me permite listar todos los empleados a los cuales se les ha enviado tarjeta de cumpleaños
	@GetMapping("/empleadocumple")
	public String listar(Model model) {					
		model.addAttribute("titulo", utf8(this.tituloempleados));
		model.addAttribute("listempleado", iCalCalendarioService.findAll());
		model.addAttribute("calidad", enlaceprincipalcalidad);
		model.addAttribute("enlace5", enlace5);		
		return "empleadocumple";
	}
	
	// Este metodo me permite visualizar o cargar el formulario del empleado
	@GetMapping("/empleadoform")
	public String crearEmpleado(Map<String, Object> model) {
		CalCalendario calCalendario = new CalCalendario();		
		model.put("titulo", utf8(this.tituloempleados));
		model.put("calCalendario", calCalendario);		
		model.put("enlace5", enlace5);
		return "empleadoform";
	}
	
	
	// Este metodo me permite guardar el empleado
	@RequestMapping(value = "/empleadoform", method = RequestMethod.POST)
	public String guardarEmpleado(@Valid CalCalendario calCalendario, BindingResult result, Model model, RedirectAttributes flash, SessionStatus status) {
		if(result.hasErrors()) {
			model.addAttribute("titulo", utf8(this.tituloempleados));
			model.addAttribute("calCalendario", calCalendario);		
			model.addAttribute("enlace5", enlace5);
			return "empleadoform";
		}
			
	String mensajeFlash = (calCalendario.getId() != null) ? "El empleado fue editado correctamente" : "El empleado fue creado correctamente";
				
	iCalCalendarioService.save(calCalendario);
	status.setComplete();
	flash.addFlashAttribute("success", mensajeFlash);		
	return "redirect:empleadocumple";
	}
	
	
	
	// Este metodo me permite cargar los datos para editar el empleado
	@RequestMapping(value = "/empleadoforme")
	public String editarEmpleado(@RequestParam(value = "id", required = false) Long id, Map<String, Object> model, RedirectAttributes flash) throws ParseException {
		CalCalendario calCalendario = null;
		if(id > 0) {			
			calCalendario = iCalCalendarioService.findById(id);
			if(calCalendario == null) {
				flash.addFlashAttribute("error", "El ID del empleado no existe en la base de datos");
				return "redirect:/empleadocumple";
			}
		}else {
			flash.addFlashAttribute("error", "El ID del empleado no puede ser 0");
			return "redirect:/empleadocumple";
		}
		//Date fechaCumpleanos = convertirfechaEmpleado(calCalendario.getFechaNacimiento().toString()); 
		//calCalendario.setFechaNacimiento(fechaCumpleanos);
		model.put("titulo", utf8(this.tituloempleados));
		model.put("calCalendario", calCalendario);		
		model.put("enlace5", enlace5);
		return "empleadoform";
	}
	
	// Este metodo me permite eliminar el empleado
	@RequestMapping(value = "/eliminarempleado/{id}")
	public String eliminar(@PathVariable(value = "id") Long id, RedirectAttributes flash) {
		if(id > 0) {
			iCalCalendarioService.delete(id);			
			flash.addFlashAttribute("success","El empleado fue eliminado correctamente");
		}
			return "redirect:/empleadocumple";
	}
	
	
	// Este metodo me permite listar todos los empleados que cumplen años hoy
	@GetMapping("/empleadohoy")
	public String listarHoy(Model model) {					
		model.addAttribute("titulo", utf8(this.titulohoy));
		model.addAttribute("listempleado", iCalCalendarioService.findUserByDate());
		model.addAttribute("calidad", enlaceprincipalcalidad);
		model.addAttribute("enlace5", enlace5);		
		return "empleadohoy";
	}
	
	// Este metodo me permite visualizar o cargar el formulario del empleado para cargar el archivo CSV
	@GetMapping("/cumplecarform")
	public String crear(Map<String, Object> model) {
		CalCalendario calCalendario = new CalCalendario();			
		model.put("calCalendario", calCalendario);				
		return "cumplecarform";
	}
	
	
	@PostMapping("/uploadcsvempleado")
    public String uploadCSVFileEmpleado(@RequestParam("archivo") MultipartFile file, Model model, SessionStatus status) {

		// validamos el archivo si esta vacio
        if (file.isEmpty()) {
        	String mensajeFlash = "Debes seleccionar un archivo CSV";            
            model.addAttribute("error", mensajeFlash);
        } else {           
        	
            // analizar el archivo CSV para crear una lista de objetos Empleado
            try (Reader reader = new BufferedReader(new InputStreamReader(new BOMInputStream(file.getInputStream()), StandardCharsets.ISO_8859_1))) {
        	
                // create csv bean reader                
				CsvToBean<CalCalendarioDTO> csvToBean = new CsvToBeanBuilder<CalCalendarioDTO>(reader)
                        .withType(CalCalendarioDTO.class)
                        .withSeparator(';')
                        .withIgnoreLeadingWhiteSpace(true)
                        .build();

                // convierte el objeto CsvToBean en una lista de empleados
                List<CalCalendarioDTO> empleados = csvToBean.parse();

                
             // recorremos la lista de empleados para ser guardada en la base de datos
                if(!empleados.isEmpty()) {
                	empleados.forEach(emp ->{              		
                		
                		//verificamos la lista y la base para no ingresar duplicados por numero de identificacion
                		CalCalendario calCalendario = iCalCalendarioService.findByNumeroIdentificacion(emp.getNumeroIdentificacion());
                		if(calCalendario == null || calCalendario.getNumeroIdentificacion() == "" || calCalendario.getNumeroIdentificacion() == null ) {
                			try {
                				CalCalendario calCalendarioGuardar = new CalCalendario();
                				calCalendarioGuardar.setNombreCompleto(emp.getNombreCompleto());
                				calCalendarioGuardar.setNumeroIdentificacion(emp.getNumeroIdentificacion());
                				calCalendarioGuardar.setFechaNacimiento(convertirfecha(emp.getFechaNacimiento()));
                				calCalendarioGuardar.setCorreo(emp.getCorreo().toString());
                				calCalendarioGuardar.setEnviado(emp.getEnviado());
    	                		iCalCalendarioService.save(calCalendarioGuardar);
    						} catch (ParseException e) {							
    							e.printStackTrace();
    						}                			
                		}              		
                	});
                }
                
                // muestra mensaje despues de guardar correctamente
                status.setComplete();
                String mensajeFlash = "El archivo CSV se proceso correctamente";                
                model.addAttribute("success", mensajeFlash);

            } catch (Exception ex) {
            	System.out.println(ex);
                model.addAttribute("error", "Se produjo un error al procesar el archivo CSV.");                
            }
        }

        return "cumplecarform";
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
		
	//Se usa para convertir una string cadena a fecha Date con formato
	private Date convertirfecha(String fecha) throws ParseException {
		Date fechaTranformada = new SimpleDateFormat("dd-MM-yyyy").parse(fecha);  
		return fechaTranformada;
	}	

}