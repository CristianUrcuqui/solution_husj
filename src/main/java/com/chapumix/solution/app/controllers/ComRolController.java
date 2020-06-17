package com.chapumix.solution.app.controllers;

import java.io.UnsupportedEncodingException;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.chapumix.solution.app.models.entity.ComRole;
import com.chapumix.solution.app.models.service.IComRoleService;

@Controller
@SessionAttributes("comRole")
@PropertySource(value = "application.properties", encoding="UTF-8")
public class ComRolController {
	
	
	@Autowired
	private IComRoleService iComRoleService;
	
	@Value("${app.titulorol}")
	private String titulorol;
	
	@Value("${app.enlaceprincipalajustes}")
	private String enlaceprincipalajustes;
	
	@Value("${app.enlace3}")
	private String enlace3;
	
	
	/* ----------------------------------------------------------
     * ROLES SISTEMA
     * ---------------------------------------------------------- */
	
	
	// Este metodo me permite listar los roles del sistema
	@GetMapping("/rollistado")
	public String listarRol(Model model) {					
		model.addAttribute("titulo", utf8(this.titulorol));
		model.addAttribute("listrol", iComRoleService.findAll());
		model.addAttribute("ajustes", enlaceprincipalajustes);
		model.addAttribute("enlace3", enlace3);		
		return "rollistado";
	}
	
	// Este metodo me permite visualizar o cargar el formulario del rol
	@GetMapping("/rolform")
	public String crear(Map<String, Object> model) {
		ComRole comRole = new ComRole();		
		model.put("titulo", utf8(this.titulorol));
		model.put("comRole", comRole);		
		model.put("ajustes", enlaceprincipalajustes);
		model.put("enlace3", enlace3);
		return "rolform";
	}
	
	// Este metodo me permite guardar el rol del sistema
	@RequestMapping(value = "/rolform", method = RequestMethod.POST)
	public String guardarRol(@Valid ComRole comRole, BindingResult result, Model model, RedirectAttributes flash, SessionStatus status) {
		if(result.hasErrors()) {
			model.addAttribute("titulo", utf8(this.titulorol));
			model.addAttribute("comRole", comRole);				
			model.addAttribute("ajustes", enlaceprincipalajustes);
			model.addAttribute("enlace3", enlace3);
			return "rollistado";
		}
		
		String mensajeFlash = (comRole.getId() != null) ? "El rol fue editado correctamente" : "El rol fue creado correctamente";
			
		iComRoleService.save(comRole);
		status.setComplete();
		flash.addFlashAttribute("success", mensajeFlash);		
		return "redirect:rollistado";
		}
	
	// Este metodo me permite cargar los datos para editar el rol del sistema
	@RequestMapping(value = "/rolformf")
	public String editarRol(@RequestParam(value = "id", required = false) Long id, Map<String, Object> model, RedirectAttributes flash) {
		
				
		ComRole comRole = null;
							
		if(id > 0) {			
			comRole = iComRoleService.findById(id);
				if(comRole == null) {
					flash.addFlashAttribute("error", "El ID del rol no existe en la base de datos");
					return "redirect:/rollistado";
				}
				}else {
					flash.addFlashAttribute("error", "El ID del rol no puede ser 0");
					return "redirect:/rollistado";
				}		
			
		model.put("titulo", utf8(this.titulorol));	
		model.put("comRole", comRole);		
		model.put("ajustes", enlaceprincipalajustes);
		model.put("enlace3", enlace3);
		return "rolform";
	}
	
	// Este metodo me permite eliminar el rol
	@RequestMapping(value = "/eliminarrol/{id}")
	public String eliminar(@PathVariable(value = "id") Long id, RedirectAttributes flash) {
		if(id > 0) {
			iComRoleService.delete(id);			
			flash.addFlashAttribute("success","El rol fue eliminado correctamente");
		}
			return "redirect:/rollistado";
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
