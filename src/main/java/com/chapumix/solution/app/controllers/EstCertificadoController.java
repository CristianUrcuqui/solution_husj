package com.chapumix.solution.app.controllers;

import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
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
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.chapumix.solution.app.models.entity.EstSerial;
import com.chapumix.solution.app.models.service.IEstSerialService;
import com.chapumix.solution.app.models.service.IEstTipoCertificadoService;


@Controller
@PropertySource(value = "application.properties", encoding="UTF-8")
public class EstCertificadoController {
	
	//private static final Charset UTF_8 = Charset.forName("UTF-8");
	
	@Autowired
	private IEstTipoCertificadoService iEstTipoCertificadoService;
	
	@Autowired
	private IEstSerialService iEstSerialService;
	
	@Value("${app.tituloestadistica}")
	private String tituloestadistica;
	
	@Value("${app.enlaceprincipalestadistica}")
	private String enlaceprincipalestadistica;
	
	@Value("${app.enlace7}")
	private String enlace7;
	
	
	/* ----------------------------------------------------------
     * INDEX ESTADISTICA
     * ---------------------------------------------------------- */
	
	//INDEX ESTADISTICA
	@GetMapping("/indexcertificado")
	public String index(Model model) {
		model.addAttribute("titulo", utf8(this.tituloestadistica));
		model.addAttribute("calidad", enlaceprincipalestadistica);
		model.addAttribute("enlace7", enlace7);
		return "indexcertificado";
	}
	
	
	// Este metodo me permite visualizar o cargar el formulario del consecutivo
	@GetMapping("/parametrocertificado")
	public String crearParametroCertificado(Map<String, Object> model) {
		EstSerial estSerial = new EstSerial();
		model.put("titulo", utf8(this.tituloestadistica));
		model.put("tipos", iEstTipoCertificadoService.findAll());
		model.put("estSerial", estSerial);
		model.put("enlace7", enlace7);
		return "parametrocertificado";
	}
	
	
	// Este metodo me permite guardar el consecutivo
	@RequestMapping(value = "/parametrocertificado", method = RequestMethod.POST)
	public String guardarEmpleado(@Valid EstSerial estSerial, BindingResult result, Model model, Principal principal, RedirectAttributes flash, SessionStatus status) {
		if (result.hasErrors()) {
			model.addAttribute("titulo", utf8(this.tituloestadistica));
			model.addAttribute("tipos", iEstTipoCertificadoService.findAll());
			model.addAttribute("estSerial", estSerial);
			model.addAttribute("enlace7", enlace7);
			return "parametrocertificado";
		}
		
		List<String> errores = new ArrayList<>();		
		if(estSerial.getSerialInicial() < estSerial.getSerialFinal() && estSerial.getSerialInicial() instanceof Integer && estSerial.getSerialFinal() instanceof Integer) {
			List<EstSerial> estSerialGuardado = iEstSerialService.findAll();
			
						
			for(Integer i = estSerial.getSerialInicial(); i<=estSerial.getSerialFinal(); i++) {
				
				//encapsulamos la variable i para poder usarla en el predicate
				final Integer p = new Integer(i);
				//Este se usa para buscar por medio de un predicate un valor y me devuelve o falso o verdadero
				boolean encontradoDato = estSerialGuardado.stream().anyMatch(action -> action.getSerial().equals(p.toString().trim()));				
					
					if (!encontradoDato) {
						EstSerial estSerialGuardar = new EstSerial();
						estSerialGuardar.setSerial(p.toString().trim());
						estSerialGuardar.setEstado(false);
						estSerialGuardar.setLoginUsrAlta(principal.getName());
						estSerialGuardar.setEstTipoCertificado(estSerial.getEstTipoCertificado());
						estSerialGuardar.setSerialInicial(estSerial.getSerialInicial());
						estSerialGuardar.setSerialFinal(estSerial.getSerialFinal());
						iEstSerialService.save(estSerialGuardar);
					}else {						
						//agregamos a una lista los seriales no ingresados
						errores.add(i.toString());											
					}
			}			
			
		}else {
			flash.addFlashAttribute("error", "El rango de los seriales no son validos");
			return "redirect:parametrocertificado";
		}
		
		
		String mensajeFlash = (estSerial.getId() != null) ? "El consecutivo fue editado correctamente" : "El consecutivo fue creado correctamente";		
		status.setComplete();
		//me muestra los errores si hay contenido en el listado
		if(errores.size() > 0) {
			flash.addFlashAttribute("error", "Seriales duplicados no ingresados: "+errores);
		}		
		flash.addFlashAttribute("success", mensajeFlash);
		return "redirect:indexcertificado";
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

}