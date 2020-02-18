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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.chapumix.solution.app.models.entity.ComUsuario;
import com.chapumix.solution.app.models.service.IComRoleService;
import com.chapumix.solution.app.models.service.IComUsuarioService;

@Controller
@SessionAttributes("comUsuario")
@PropertySource(value = "application.properties", encoding="UTF-8")
public class ComUsuarioController {
	
	
	@Autowired
	private IComUsuarioService iComUsuarioService;
	
	@Autowired
	private IComRoleService iComRoleService;
	
	@Value("${app.titulousuarios}")
	private String titulousuarios;
	
	@Value("${app.enlaceprincipalajustes}")
	private String enlaceprincipalajustes;
	
	@Value("${app.enlace3}")
	private String enlace3;
	
	
	/* ----------------------------------------------------------
     * USUARIOS SISTEMA
     * ---------------------------------------------------------- */
	
	
	// Este metodo me permite listar los usuarios del sistema
	@GetMapping("/usuariolistado")
	public String listarUsuario(Model model) {					
		model.addAttribute("titulo", utf8(this.titulousuarios));
		model.addAttribute("listusuario", iComUsuarioService.findAll());
		model.addAttribute("ajustes", enlaceprincipalajustes);
		model.addAttribute("enlace3", enlace3);		
		return "usuariolistado";
	}
	
	// Este metodo me permite guardar el usuario del sistema
		@RequestMapping(value = "/usuarioform", method = RequestMethod.POST)
		public String guardarUsuario(@Valid ComUsuario comUsuario, BindingResult result, Model model, RedirectAttributes flash, SessionStatus status) {
			if(result.hasErrors()) {
				model.addAttribute("titulo", utf8(this.titulousuarios));
				model.addAttribute("comUsuario", comUsuario);				
				model.addAttribute("ajustes", enlaceprincipalajustes);
				model.addAttribute("enlace3", enlace3);
				return "usuariolistado";
			}
			
			String mensajeFlash = (comUsuario.getId() != null) ? "El usuario fue editado correctamente" : "El usuario fue creado correctamente";
			
			iComUsuarioService.save(comUsuario);
			status.setComplete();
			flash.addFlashAttribute("success", mensajeFlash);		
			return "redirect:usuariolistado";
		}
	
	// Este metodo me permite cargar los datos para editar el usuario del sistema
	@RequestMapping(value = "/usuarioform")
	public String editarUsuario(@RequestParam(value = "id", required = false) Long id, Map<String, Object> model, RedirectAttributes flash) {		
				
		ComUsuario comUsuario = null;
							
		if(id > 0) {			
			comUsuario = iComUsuarioService.findById(id);
				if(comUsuario == null) {
					flash.addFlashAttribute("error", "El ID del usuario no existe en la base de datos");
					return "redirect:/usuariolistado";
				}
				}else {
					flash.addFlashAttribute("error", "El ID del usuario no puede ser 0");
					return "redirect:/usuariolistado";
				}		
			
		model.put("titulo", utf8(this.titulousuarios));	
		model.put("comUsuario", comUsuario);
		model.put("roles",iComRoleService.findAll());
		model.put("ajustes", enlaceprincipalajustes);
		model.put("enlace3", enlace3);
		return "usuarioform";
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
	

}
