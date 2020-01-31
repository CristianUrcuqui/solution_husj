package com.chapumix.solution.app.controllers;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
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
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.chapumix.solution.app.entity.dto.HcnSolPatDTO;
import com.chapumix.solution.app.entity.dto.HcnSolPatDetaDosDTO;
import com.chapumix.solution.app.entity.dto.HcnSolPatDetaUnoDTO;
import com.chapumix.solution.app.models.entity.PatProcedimiento;
import com.chapumix.solution.app.models.entity.TerPersonaJuridica;
import com.chapumix.solution.app.models.service.IPersonaJuridicaService;

@Controller
@SessionAttributes("patProcedimiento")
@PropertySource(value = "application.properties", encoding="UTF-8")
public class HcnSolPatController {
	
	private int edad;
	private String sexo;
	private Logger logger = LoggerFactory.getLogger(HcnSolPatDTO.class);	
	public static final String URLPatologias = "http://localhost:9000/api/patologias";
	public static final String URLDescripcionUno = "http://localhost:9000/api/patologias/detalles/";
	public static final String URLDescripcionDos = "http://localhost:9000/api/patologias/detalles/";
	
	
	@Autowired
	private IPersonaJuridicaService personaJuridicaService;
	
	@Autowired
	private RestTemplate restTemplate;
	
	@Value("${app.tituloprocesarpacientesinternos}")
	private String tituloprocesarpacientesinternos;
	
	@Value("${app.enlaceprincipal}")
	private String enlaceprincipal;
	
	
	
	// Este metodo me permite visualizar las solicitud pendientes de patologia
	@GetMapping("/procedimientopatologiainterno")
	public String listar(Model model) {		
		ResponseEntity<List<HcnSolPatDTO>> response = restTemplate.exchange(URLPatologias, HttpMethod.GET, null, new ParameterizedTypeReference<List<HcnSolPatDTO>>() {});
		List<HcnSolPatDTO> data = response.getBody();						
		model.addAttribute("titulo", utf8(this.tituloprocesarpacientesinternos));
		model.addAttribute("listprocpat", data);
		model.addAttribute("principal", enlaceprincipal);			
		return "procedimientopatologiainterno";
	}
	
	
	// Este metodo me permite procesar la solicitud de patologia
		@RequestMapping(value = "/procesarpatologia")	
		public String editarPatologia(@RequestParam(value = "oidpaciente", required = false) Integer oidpaciente, @RequestParam(value = "procedimiento", required = false) Integer procedimiento, @RequestParam(value = "folio", required = false) Integer folio, Map<String, Object> model, RedirectAttributes flash) { 	
			PatProcedimiento patProcedimiento = null;						
			
			//proceso API para buscar la primera descripcion de la solicitud.
			ResponseEntity<HcnSolPatDetaUnoDTO> response = restTemplate.exchange(URLDescripcionUno+oidpaciente+'/'+folio, HttpMethod.GET, null, new ParameterizedTypeReference<HcnSolPatDetaUnoDTO>() {});
			HcnSolPatDetaUnoDTO data = response.getBody();			
			
			if(data != null) {
				this.edad = calcularEdad(data.getGpafecnac());
				this.sexo = convertirSexo(data.getGpasexpac());
				
				model.put("nombrecompleto", data.getPacPriNom()+"-"+data.getPacSegNom()+"-"+data.getPacPriApe()+"-"+data.getPacSegApe());
				/*model.put("nombreuno", data.getPacPriNom());
				model.put("nombredos", data.getPacSegNom());
				model.put("apellidouno", data.getPacPriApe());
				model.put("apellidodos", data.getPacSegApe());*/
				model.put("servicio", data.getGasNombre());
				model.put("cama", data.getHcaCodigo());
				model.put("fechasolicitud", data.getHcsfecsol());
				model.put("historia", data.getPacNumDoc());
				model.put("edad", this.edad);
				model.put("sexo", this.sexo);
				model.put("aseguradora", data.getGdeNombre());
				model.put("ingreso", data.getAinConsec());
				model.put("folio", data.getHcNumFol());
				model.put("dx", data.getDiaCodigo()+"-"+data.getDiaNombre());				
			}
			
			//proceso API para buscar la segunda descripcion de la solicitud.
			ResponseEntity<HcnSolPatDetaDosDTO> responser = restTemplate.exchange(URLDescripcionDos+oidpaciente+'/'+procedimiento+'/'+folio, HttpMethod.GET, null, new ParameterizedTypeReference<HcnSolPatDetaDosDTO>() {});
			HcnSolPatDetaDosDTO datas = responser.getBody();
			
			if(datas != null) {
				String tipo = convertirTipo(datas.getHcsestado());
				model.put("cups", datas.getSipcodcup());
				model.put("desc1", datas.getSipCodigo());
				model.put("desc2", datas.getSipNombre());
				model.put("pieza", datas.getHcsobserv());
				model.put("cantidad", datas.getHcscanti());
				model.put("tipo", tipo);			
			}			
			
			model.put("titulo", utf8(this.tituloprocesarpacientesinternos));
			model.put("patProcedimiento", patProcedimiento);
			model.put("principal", enlaceprincipal);		
			return "procesarpatologia";			
			
		}
	


	// Este metodo me permite visualizar o cargar el formulario de la persona juridica
	@GetMapping("/formpersonajuridica")
	public String crear(Map<String, Object> model) {
		TerPersonaJuridica terPersonaJuridica = new TerPersonaJuridica();		
		model.put("titulo", utf8(this.tituloprocesarpacientesinternos));
		model.put("terPersonaJuridica", terPersonaJuridica);		
		model.put("principal", enlaceprincipal);		
		return "formpersonajuridica";
	}		
			
	// Este metodo me permite guardar la persona juridica
	@RequestMapping(value = "/formpersonajuridica", method = RequestMethod.POST)
	public String guardar(@Valid TerPersonaJuridica terPersonaJuridica, BindingResult result, Model model, RedirectAttributes flash, SessionStatus status) {
		if(result.hasErrors()) {
			model.addAttribute("titulo", utf8(this.tituloprocesarpacientesinternos));
			model.addAttribute("terPersonaJuridica", terPersonaJuridica);
			//model.addAttribute("terTercero", terTercero);
			model.addAttribute("principal", enlaceprincipal);			
			return "formpersonajuridica";
		}
		
		String mensajeFlash = (terPersonaJuridica.getIdPersonaJuridica() != null) ? "La persona jurídica fue editada correctamente" : "La persona jurídica fue creada correctamente";
		
		personaJuridicaService.save(terPersonaJuridica);
		status.setComplete();
		flash.addFlashAttribute("success", mensajeFlash);		
		return "redirect:listapersonajuridica";
	}
	
	// Este metodo me permite editar la persona juridica
	@RequestMapping(value = "/formpersonajuridica/{id}")
	public String editar(@PathVariable(value = "id") Long id, Map<String, Object> model, RedirectAttributes flash) {
		//TerPersonaJuridica terPersonaJuridica = null;
		TerPersonaJuridica terPersonaJuridica = null;
		if(id > 0) {			
			terPersonaJuridica = personaJuridicaService.findById(id);
			if(terPersonaJuridica == null) {
				flash.addFlashAttribute("error", "El ID de la persona jurídica no existe en la base de datos");
				return "redirect:/listapersonajuridica";
			}
		}else {
			flash.addFlashAttribute("error", "El ID de la persona jurídica no puede ser 0");
			return "redirect:/listapersonajuridica";
		}
		model.put("titulo", utf8(this.tituloprocesarpacientesinternos));
		model.put("terPersonaJuridica", terPersonaJuridica);
		model.put("principal", enlaceprincipal);		
		return "formpersonajuridica";
	}
	
	// Este metodo me permite eliminar la persona juridica
	@RequestMapping(value = "/eliminar/{id}")
	public String eliminar(@PathVariable(value = "id") Long id, RedirectAttributes flash) {
		if(id > 0) {
			personaJuridicaService.delete(id);			
			flash.addFlashAttribute("success","La persona jurídica fue elimina correctamente");
		}
		return "redirect:/listapersonajuridica";
	}
	
	
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
	
	private void cadenasVacias(HcnSolPatDetaUnoDTO data) {
		data.setAinConsec(null);
		data.setDiaCodigo("");
		data.setDiaNombre("");
		data.setGasNombre("");
		data.setGdeNombre("");
		data.setGpafecnac(null);
		data.setGpasexpac(null);
		data.setHcaCodigo("");
		data.setHcNumFol(null);
		data.setHcsfecsol(null);
		data.setPacNumDoc("");
		data.setPacPriApe("");
		data.setPacPriNom("");
		data.setPacSegApe("");
		data.setPacSegNom("");		
	}
	
	
	private int calcularEdad(Date fechaNacimiento) {
		
		//convierto la fecha que entra en texto
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		String fechaNacimientoTexto = sdf.format(fechaNacimiento);
		
		//hago el calculo de la fecha de nacimiento
		DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd-MM-yyyy");
		LocalDate fechaNac = LocalDate.parse(fechaNacimientoTexto, fmt);
		LocalDate ahora = LocalDate.now();

		Period periodo = Period.between(fechaNac, ahora);
		//System.out.printf("Tu edad es: %s años, %s meses y %s días", periodo.getYears(), periodo.getMonths(), periodo.getDays());
		return periodo.getYears();		
		
	}
	
	private String convertirSexo(Integer gpasexpac) {
		if(gpasexpac == 1)
		return "M";{			
		}
		return "F";
	}
	
	private String convertirTipo(Integer hcsestado) {
		if(hcsestado == 0) {
			return "URGENTE";
		}
		return "RUTINARIO";
	}

}
