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

import org.apache.commons.io.input.BOMInputStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.multipart.MultipartFile;

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
	
	// Este metodo me permite visualizar o cargar el formulario del empleado para cargar el archivo
	@GetMapping("/cumplecarform")
	public String crear(Map<String, Object> model) {
		CalCalendario calCalendario = new CalCalendario();	
		//model.put("titulo", utf8(this.utf8(this.titulorol)));
		model.put("calCalendario", calCalendario);		
		//model.put("ajustes", enlaceprincipalajustes);
		//model.put("enlace4", enlace4);
		return "cumplecarform";
	}
	
	
	@PostMapping("/uploadcsv")
    public String uploadCSVFile(@RequestParam("archivo") MultipartFile file, Model model) {

		// validamos el archivo si esta vacio
        if (file.isEmpty()) {
        	String mensajeFlash = "Debes seleccionar un archivo CSV";            
            model.addAttribute("error", mensajeFlash);
        } else {           
        	
            // analizar el archivo CSV para crear una lista de objetos Empleado
            try (Reader reader = new BufferedReader(new InputStreamReader(new BOMInputStream(file.getInputStream()), StandardCharsets.UTF_8))) {
        	
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
                		if(calCalendario == null) {
                			try {
                				CalCalendario calCalendarioGuardar = new CalCalendario();
                				calCalendarioGuardar.setNombreCompleto(emp.getNombreCompleto());
                				calCalendarioGuardar.setNumeroIdentificacion(emp.getNumeroIdentificacion());
                				calCalendarioGuardar.setFechaNacimiento(convertirfecha(emp.getFechaNacimiento()));
                				calCalendarioGuardar.setCorreo(emp.getCorreo());
                				calCalendarioGuardar.setEnviado(emp.getEnviado());
    	                		iCalCalendarioService.save(calCalendarioGuardar);
    						} catch (ParseException e) {							
    							e.printStackTrace();
    						}                			
                		}              		
                	});
                }
                
                // muestra mensaje despues de guardar correctamente
                String mensajeFlash = "El archivo CSV se proceso correctamente";                
                model.addAttribute("success", mensajeFlash);

            } catch (Exception ex) {
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