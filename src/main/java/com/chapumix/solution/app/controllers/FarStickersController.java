package com.chapumix.solution.app.controllers;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.chapumix.solution.app.entity.dto.GenPacienDTO;

import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;


@Controller
@PropertySource(value = "application.properties", encoding="UTF-8")
public class FarStickersController {
	
	public static final String URLPaciente = "http://localhost:9000/api/pacientesticker"; //se obtuvo de API REST de GenPacienRestController
	
	@Value("${app.titulostickers}")
	private String titulostickers;
	
	@Value("${app.titulogenerartickers}")
	private String titulogenerartickers;
	
	
	
	@Value("${app.enlaceprincipalfarmacia}")
	private String enlaceprincipalfarmacia;
	
	@Value("${app.enlace10}")
	private String enlace10;
	
	@Autowired
	private RestTemplate restTemplate;	
	
	
	/* ----------------------------------------------------------
     * INDEX FARMACIA STICKERS
     * ---------------------------------------------------------- */
	
	//INDEX STICKERS
	@GetMapping("/indexstickers")
	public String index(Model model) {
		model.addAttribute("titulo", utf8(this.titulostickers));
		model.addAttribute("farmacia", enlaceprincipalfarmacia);
		model.addAttribute("enlace10", enlace10);
		return "indexstickers";
	}
	
	
	// Este metodo me permite visualizar o cargar el formulario para consultar el paciente para generar el sticker
	@GetMapping("/generarsticker")
	public String cargarSticker(Map<String, Object> model, RedirectAttributes flash) {						
				
		boolean listado = false;
				
		model.put("titulo", utf8(this.titulogenerartickers));			
		model.put("enlace10", enlace10);		
		model.put("farmacia", enlaceprincipalfarmacia);
		model.put("listado", listado);		
		return "generarsticker";
	}
	
	// Este metodo me permite consultar el paciente y generar el pdf para imprimir
	@RequestMapping("/crearsticker")	
	public String generarSticker(Model model, @RequestParam(value = "keyword", required = false) String keyword, RedirectAttributes flash) throws Exception{
				
		boolean listado = false;
					
		// valida el ID de la prescripcion que no este vacia
		if (keyword.isEmpty()) {		
			model.addAttribute("titulo", utf8(this.titulogenerartickers));			
			model.addAttribute("enlace10", enlace10);
			model.addAttribute("farmacia", enlaceprincipalfarmacia);
			model.addAttribute("error", "El documento del paciente es requerido");
			return "generarsticker";
		}
		
		// proceso API para buscar el paciente
		ResponseEntity<List<GenPacienDTO>> respuesta = restTemplate.exchange(URLPaciente + '/' + keyword, HttpMethod.GET, null,new ParameterizedTypeReference<List<GenPacienDTO>>() {});
		List<GenPacienDTO> dinamica = respuesta.getBody();
		
		if(!dinamica.isEmpty()) {
			
			//creamos el reporte en PDF
			crearPDF(dinamica);			
			listado = true;			
			
			model.addAttribute("titulo", utf8(this.titulogenerartickers));			
			model.addAttribute("enlace10", enlace10);
			model.addAttribute("farmacia", enlaceprincipalfarmacia);			
			model.addAttribute("listado", listado);
			model.addAttribute("docPaciente", dinamica.get(0).getPacNumDoc().concat(".pdf"));
			model.addAttribute("pacienteGenerado", dinamica.get(0).getPacPriNom().trim()+" "+dinamica.get(0).getPacSegNom().trim()+" "+dinamica.get(0).getPacPriApe().trim()+" "+dinamica.get(0).getPacSegApe().trim());
			model.addAttribute("navpanes", "#navpanes=1");
			return  "generarsticker";
			
			
		}else {
			model.addAttribute("error", "El documento del paciente no existen en la base de datos");
			return "generarsticker";
		}	
					
	}	
	


	/* ----------------------------------------------------------
     * METODOS ADICIONALES 
     * ---------------------------------------------------------- */
	
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
	
	//Se usa para crear el sticker en PDF
	private void crearPDF(List<GenPacienDTO> dinamica) throws Exception {
		
				
		//generamos el pdf con la informacion de dinamica			
		
		// Obteniendo el archivo .jrxml de la carpeta de recursos.
		InputStream jrxmlInput = this.getClass().getResourceAsStream("/reports/sticker.jrxml");
		
		// Compilo el informe Jasper de .jrxml a .jasper
		JasperReport jasperReport = JasperCompileManager.compileReport(jrxmlInput);
		
		//Para evitar compilarlo cada vez, podemos guardarlo en un archivo
		//JRSaver.saveObject(jasperReport, "sticker.jasper");
		
		//parametros adicionales para el PDF
		Map<String, Object> parameters = new HashMap<>();
		
		// Obteniendo a las prescipciones de la fuente de datos.
		JRBeanCollectionDataSource source = new JRBeanCollectionDataSource(dinamica);
		
		//obtengo la edad completa
		String edad = calcularEdad(dinamica.get(0).getGpafecnac());
		
		// Agregar los parámetros adicionales al pdf.
		parameters.put("paciente", dinamica.get(0).getPacPriNom().trim()+" "+dinamica.get(0).getPacSegNom().trim()+" "+dinamica.get(0).getPacPriApe().trim()+" "+dinamica.get(0).getPacSegApe().trim());			
		parameters.put("documento", dinamica.get(0).getPacNumDoc());		
		parameters.put("edad", edad);		
		
		// Rellenar el informe con los datos de la prescripcion y la información de parámetros adicionales.
		JasperPrint jasperPrint  = JasperFillManager.fillReport(jasperReport, parameters, source);		
		
		//este me permite armar el nombre del archivo
		String fileName = dinamica.get(0).getPacNumDoc()+".pdf";
		
		//Resource resource = loader.getResource("classpath:static/dist/img/cumpleanos.jpg"); //windows 
        //Resource resource = loader.getResource("file:/home/jar/cumpleanos.jpg"); //linux
          
        //String path = "C://tmp//pdf"; //Windows		
		String path = "/home/jar/pdf/";	//Linux		
		
		//File file = new File(path);
		//String absolutePath = file.getAbsolutePath();		
		JasperExportManager.exportReportToPdfFile(jasperPrint, path +'/'+ fileName);		
		
	}
		
		

}