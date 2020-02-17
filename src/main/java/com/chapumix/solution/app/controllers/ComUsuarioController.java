package com.chapumix.solution.app.controllers;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import com.chapumix.solution.app.models.service.IComUsuarioService;

@Controller
@SessionAttributes("comUsuario")
@PropertySource(value = "application.properties", encoding="UTF-8")
public class ComUsuarioController {
	
	
	@Autowired
	private IComUsuarioService iComUsuarioService;
	
	@Value("${app.titulousuarios}")
	private String titulousuarios;
	
	@Value("${app.enlaceprincipalajustes}")
	private String enlaceprincipalajustes;
	
	@Value("${app.enlace3}")
	private String enlace3;
	
	
	@GetMapping("/usuariolistado")
	public String listar(Model model) {					
		model.addAttribute("titulo", utf8(this.titulousuarios));
		model.addAttribute("listusuario", iComUsuarioService.findAll());
		model.addAttribute("ajustes", enlaceprincipalajustes);
		model.addAttribute("enlace3", enlace3);		
		return "usuariolistado";
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
	
	private Date convertirfecha(String fecha) throws ParseException {
		Date fechaTranformada = new SimpleDateFormat("dd-MM-yyyy").parse(fecha);  
		return fechaTranformada;
	}

}
