package com.chapumix.solution.app.controllers;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.chapumix.solution.app.models.entity.ComParametroPatologia;
import com.chapumix.solution.app.models.service.IComParametroPatologiaService;

@Controller
@SessionAttributes("comParametroPatologia")
@PropertySource(value = "application.properties", encoding="UTF-8")
public class ComParametroPatologiaController {
	
	@Autowired
	private IComParametroPatologiaService iComParametroPatologiaService;
	
	@Value("${app.tituloparametrospatologia}")
	private String tituloparametrospatologia;	
	
	@Value("${app.enlaceprincipalpatologia}")
	private String enlaceprincipalpatologia;	
	
	@Value("${app.enlace1}")
	private String enlace1;
	
	
	
	// Este metodo me permite visualizar o cargar la solicitud de patologia de pacientes externos para ser procesada
	@RequestMapping(value = "/parametropatologia")
	public String crearExterno(@RequestParam(value = "seccion", required = false) String seccion, Map<String, Object> model,SessionStatus status, RedirectAttributes flash) {
		ComParametroPatologia comParametroPatologia =  null;		
		comParametroPatologia = iComParametroPatologiaService.findByName(seccion);	
		model.put("titulo", utf8(this.tituloparametrospatologia));		
		model.put("comParametroPatologia", comParametroPatologia);
		model.put("patologia", enlaceprincipalpatologia);
		model.put("enlace1", enlace1);	
		status.setComplete();
		return "parametropatologia";

	}
	
	// Este metodo me permite guardar y editar la persona juridica
	@RequestMapping(value = "/parametropatologia", method = RequestMethod.POST)
	public String guardar(@RequestParam(value = "fechaSolicitudInterno", required = false) String fechaSolicitudInterno, @RequestParam(value = "fechaSolicitudExterno", required = false) String fechaSolicitudExterno, @Valid ComParametroPatologia comParametroPatologia, BindingResult result, Model model, RedirectAttributes flash, SessionStatus status) throws ParseException {
		comParametroPatologia = iComParametroPatologiaService.findByName("patologia");
		
		Date fechaI = convertirfecha(fechaSolicitudInterno);
		Date fechaE = convertirfecha(fechaSolicitudExterno);
		comParametroPatologia.setFechaSolicitudInterno(fechaI);
		comParametroPatologia.setFechaSolicitudExterno(fechaE);
		
		
		if (result.hasErrors()) {
			model.addAttribute("titulo", utf8(this.tituloparametrospatologia));			
			model.addAttribute("comParametroPatologia", comParametroPatologia);
			model.addAttribute("patologia", enlaceprincipalpatologia);
			model.addAttribute("enlace1", enlace1);			
			return "parametropatologia";
		}

		String mensajeFlash = (comParametroPatologia.getId() != null) ? "El parámetro para la patología fue editada correctamente" : "El parámetro para la patología fue creada correctamente";

		iComParametroPatologiaService.save(comParametroPatologia);
		status.setComplete();
		flash.addFlashAttribute("success", mensajeFlash);
		return "redirect:parametropatologia?seccion=patologia";
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
