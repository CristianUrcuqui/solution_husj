package com.chapumix.solution.app.controllers;

import java.io.UnsupportedEncodingException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
@PropertySource(value = "application.properties", encoding="UTF-8")
public class FarFarmaciaController {
	
	//private static final Charset UTF_8 = Charset.forName("UTF-8");
	
	@Value("${app.titulofarmacia}")
	private String titulofarmacia;
	
	@Value("${app.enlaceprincipalfarmacia}")
	private String enlaceprincipalfarmacia;
	
	@Value("${app.enlace10}")
	private String enlace10;
	
	
	/* ----------------------------------------------------------
     * INDEX ESTADISTICA
     * ---------------------------------------------------------- */
	
	//INDEX ESTADISTICA
	@GetMapping("/indexfarmacia")
	public String index(Model model) {
		model.addAttribute("titulo", utf8(this.titulofarmacia));
		model.addAttribute("farmacia", enlaceprincipalfarmacia);
		model.addAttribute("enlace10", enlace10);
		return "indexfarmacia";
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

}