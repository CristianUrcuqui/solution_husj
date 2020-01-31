package com.chapumix.solution.app.controllers;

import java.security.Principal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;



@Controller
public class LoginControlller {
	
	
	
	@GetMapping({"/","/login"})
	public String login(@RequestParam(value = "error", required = false) String error, @RequestParam(value = "logout", required = false) String logout, Model model, Principal principal, RedirectAttributes flash) {
		
		if(principal != null) { //principal me garantiza validar que si esta logueado me redirecciona a la pagina que queramos, evita doble inicio de sesion
			flash.addFlashAttribute("info", "Ya ha iniciado sesión anteriormente");
			return "redirect:/index";			
		}
		
		if(error != null) {
			model.addAttribute("error", "Nombre de usuario o contraseña incorrecta, por favor vuelva a intentarlo!");
		}
		
		if(logout != null) {
			model.addAttribute("success", "Te has desconectado de Solution.");
		}
		
		return "login";
	}
	

}
