package com.chapumix.solution.app.controllers;

import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.chapumix.solution.app.entity.dto.GenMedicoDTO;
import com.chapumix.solution.app.entity.dto.HcnSolPatIntDTO;
import com.chapumix.solution.app.entity.dto.HcnSolPatDetaDosDTO;
import com.chapumix.solution.app.entity.dto.HcnSolPatDetaTresDTO;
import com.chapumix.solution.app.entity.dto.HcnSolPatDetaUnoDTO;
import com.chapumix.solution.app.entity.dto.HcnSolPatExtDTO;
import com.chapumix.solution.app.models.entity.PatProcedimiento;
import com.chapumix.solution.app.models.service.IPatProcedimientoService;

@Controller
@SessionAttributes("patProcedimiento")
@PropertySource(value = "application.properties", encoding="UTF-8")
public class HcnSolPatController {
	
	private int folio;
	private int oidpaciente;
	private int procedimiento;
	private Logger logger = LoggerFactory.getLogger(HcnSolPatIntDTO.class);	
	public static final String URLPatologiasInt = "http://localhost:9000/api/patologiasints";
	public static final String URLPatologiasExt = "http://localhost:9000/api/patologiasexts";	
	public static final String URLDescripcion = "http://localhost:9000/api/patologias/detalles/";
	public static final String URLDescripcionExt = "http://localhost:9000/api/patologias/detalles/externos/";
	public static final String URLMedicos = "http://localhost:9000/api/patologias/medicos";	
	
	
	@Autowired
	private IPatProcedimientoService iPatProcedimientoService;
	
	@Autowired
	private RestTemplate restTemplate;
	
	@Value("${app.tituloprocesarpacientesinternos}")
	private String tituloprocesarpacientesinternos;
	
	@Value("${app.tituloprocesarpacientesexternos}")
	private String tituloprocesarpacientesexternos;
	
	@Value("${app.titulopacientesinternos}")
	private String titulopacientesinternos;	
	
	
	
	
	//PACIENTES INTERNOS
	// Este metodo me permite visualizar las solicitud pendientes de patologia de pacientes internos
	@GetMapping("/procedimientopatologiainterno")
	public String listarInterno(Model model) {		
		ResponseEntity<List<HcnSolPatIntDTO>> respuesta = restTemplate.exchange(URLPatologiasInt, HttpMethod.GET, null, new ParameterizedTypeReference<List<HcnSolPatIntDTO>>() {});		
		List<HcnSolPatIntDTO> dinamica = respuesta.getBody();
		List<PatProcedimiento> patProcedimiento = iPatProcedimientoService.findAll();
		
		//esta parte me permite cruzar entre las patologias de dinamica y las patologias procesadas en Solution
		patProcedimiento.forEach(p ->{
			Predicate<HcnSolPatIntDTO> condicion = dina -> dina.getOidPaciente().equals(p.getIdPaciente()) && dina.getOidRips().equals(p.getIdProcedimiento()) && dina.getHcNumFol().equals(p.getFolio());	         
			dinamica.removeIf(condicion);
		});		
		
		model.addAttribute("titulo", utf8(this.titulopacientesinternos));
		model.addAttribute("listprocpat", dinamica);					
		return "procedimientopatologiainterno";
	}
	
	
	// Este metodo me permite visualizar o cargar la solicitud de patologia de pacientes internos para ser procesada
	@RequestMapping(value = "/procesarpatologiainterno")
	public String crearInterno(@RequestParam(value = "oidpaciente", required = false) Integer oidpaciente, @RequestParam(value = "procedimiento", required = false) Integer procedimiento, @RequestParam(value = "folio", required = false) Integer folio, Map<String, Object> model, RedirectAttributes flash) {
		PatProcedimiento patProcedimiento = new PatProcedimiento();

		//obtenemos los valores para luego guardar la solicitud
		this.folio = folio;
		this.oidpaciente = oidpaciente;
		this.procedimiento = procedimiento;
		
		// proceso API para buscar la primera descripcion de la solicitud.
		ResponseEntity<HcnSolPatDetaUnoDTO> respuestaa = restTemplate.exchange(URLDescripcion + oidpaciente + '/' + folio, HttpMethod.GET, null,new ParameterizedTypeReference<HcnSolPatDetaUnoDTO>() {});
		HcnSolPatDetaUnoDTO dinamicaa = respuestaa.getBody();

		if (dinamicaa != null) {
			int edad = calcularEdad(dinamicaa.getGpafecnac());
			String sexo = convertirSexo(dinamicaa.getGpasexpac());

			model.put("nombrecompleto", dinamicaa.getPacPriNom() + " " + dinamicaa.getPacSegNom() + " " + dinamicaa.getPacPriApe() + " " + dinamicaa.getPacSegApe());
			model.put("servicio", dinamicaa.getGasNombre());
			model.put("cama", dinamicaa.getHcaCodigo());
			model.put("fechasolicitud", dinamicaa.getHcsfecsol());
			model.put("historia", dinamicaa.getPacNumDoc());
			model.put("edad", edad);
			model.put("sexo", sexo);
			model.put("aseguradora", dinamicaa.getGdeNombre());
			model.put("ingreso", dinamicaa.getAinConsec());
			model.put("folio", dinamicaa.getHcNumFol());
			model.put("dx", dinamicaa.getDiaCodigo() + "-" + dinamicaa.getDiaNombre());			
			
		}

		// proceso API para buscar la segunda descripcion de la solicitud.
		ResponseEntity<HcnSolPatDetaDosDTO> respuestab = restTemplate.exchange(URLDescripcion + oidpaciente + '/' + procedimiento + '/' + folio, HttpMethod.GET, null,new ParameterizedTypeReference<HcnSolPatDetaDosDTO>() {});
		HcnSolPatDetaDosDTO dinamicab = respuestab.getBody();

		if (dinamicab != null) {
			String tipo = convertirTipo(dinamicab.getHcsestado());
			model.put("cups", dinamicab.getSipCodCup());
			model.put("desc1", dinamicab.getSipCodigo());
			model.put("desc2", dinamicab.getSipNombre());
			model.put("pieza", dinamicab.getHcsobserv());
			model.put("cantidad", dinamicab.getHcscanti());
			model.put("tipo", tipo);
		}
		
		
		// proceso API para select de medicos.
		ResponseEntity<List<GenMedicoDTO>> respuestac = restTemplate.exchange(URLMedicos, HttpMethod.GET, null, new ParameterizedTypeReference<List<GenMedicoDTO>>() {});
		List<GenMedicoDTO> medicos = respuestac.getBody();
		
		model.put("medicos", medicos);
		model.put("titulo", utf8(this.tituloprocesarpacientesinternos));		
		model.put("patProcedimiento", patProcedimiento);		
		return "procesarpatologiainterno";

	}
		
		
		// Este metodo me permite guardar el proceso de patologia	    
		@RequestMapping(value = "/procesarpatologiainterno", method = RequestMethod.POST)
		public String guardarPatologiaInterno(@Valid PatProcedimiento patProcedimiento, BindingResult result, Model model, Principal principal, RedirectAttributes flash, SessionStatus status) {
	    				
			//verificamos si hay errores en los campos requeridos.
			if(result.hasErrors()) {
				// proceso API para buscar la primera descripcion de la solicitud.
				ResponseEntity<HcnSolPatDetaUnoDTO> respuestaa = restTemplate.exchange(URLDescripcion + this.oidpaciente + '/' + this.folio, HttpMethod.GET, null,new ParameterizedTypeReference<HcnSolPatDetaUnoDTO>() {});
				HcnSolPatDetaUnoDTO dinamicaa = respuestaa.getBody();
				
				if (dinamicaa != null) {
				int edad = calcularEdad(dinamicaa.getGpafecnac());
				String sexo = convertirSexo(dinamicaa.getGpasexpac());

				model.addAttribute("nombrecompleto", dinamicaa.getPacPriNom() + " " + dinamicaa.getPacSegNom() + " " + dinamicaa.getPacPriApe() + " " + dinamicaa.getPacSegApe());
				model.addAttribute("servicio", dinamicaa.getGasNombre());
				model.addAttribute("cama", dinamicaa.getHcaCodigo());
				model.addAttribute("fechasolicitud", dinamicaa.getHcsfecsol());
				model.addAttribute("historia", dinamicaa.getPacNumDoc());
				model.addAttribute("edad", edad);
				model.addAttribute("sexo", sexo);
				model.addAttribute("aseguradora", dinamicaa.getGdeNombre());
				model.addAttribute("ingreso", dinamicaa.getAinConsec());
				model.addAttribute("folio", dinamicaa.getHcNumFol());
				model.addAttribute("dx", dinamicaa.getDiaCodigo() + "-" + dinamicaa.getDiaNombre());
				}
				
				// proceso API para buscar la segunda descripcion de la solicitud.
				ResponseEntity<HcnSolPatDetaDosDTO> respuestab = restTemplate.exchange(URLDescripcion + this.oidpaciente + '/' + this.procedimiento + '/' + this.folio, HttpMethod.GET, null,new ParameterizedTypeReference<HcnSolPatDetaDosDTO>() {});
				HcnSolPatDetaDosDTO dinamicab = respuestab.getBody();
				
				if (dinamicab != null) {
				String tipo = convertirTipo(dinamicab.getHcsestado());
				model.addAttribute("cups", dinamicab.getSipCodCup());
				model.addAttribute("desc1", dinamicab.getSipCodigo());
				model.addAttribute("desc2", dinamicab.getSipNombre());
				model.addAttribute("pieza", dinamicab.getHcsobserv());
				model.addAttribute("cantidad", dinamicab.getHcscanti());
				model.addAttribute("tipo", tipo);
				}
				
				// proceso API para select de medicos.
				ResponseEntity<List<GenMedicoDTO>> respuestac = restTemplate.exchange(URLMedicos, HttpMethod.GET, null, new ParameterizedTypeReference<List<GenMedicoDTO>>() {});
				List<GenMedicoDTO> medicos = respuestac.getBody();
				
				model.addAttribute("medicos", medicos);				
				model.addAttribute("titulo", utf8(this.tituloprocesarpacientesinternos));
				model.addAttribute("patProcedimiento", patProcedimiento);											
				return "procesarpatologiainterno";
			}
			
			String mensajeFlash = (patProcedimiento.getId() != null) ? "La solicitud de patología fue editada correctamente" : "La solicitud de patología fue creada correctamente";
			patProcedimiento.setIdPaciente(this.oidpaciente);
			patProcedimiento.setIdProcedimiento(this.procedimiento);
			patProcedimiento.setFolio(this.folio);
			patProcedimiento.setPacienteInternoExterno("I");
			patProcedimiento.setLoginUsr(principal.getName());
			iPatProcedimientoService.save(patProcedimiento);
			status.setComplete();
			flash.addFlashAttribute("success", mensajeFlash);		
			return "redirect:procedimientopatologiainterno";
		}
		
		
		//PACIENTES EXTERNOS
		// Este metodo me permite visualizar las solicitud pendientes de patologia de pacientes externos
		@GetMapping("/procedimientopatologiaexterno")
		public String listarExterno(Model model) {		
			ResponseEntity<List<HcnSolPatExtDTO>> respuesta = restTemplate.exchange(URLPatologiasExt, HttpMethod.GET, null, new ParameterizedTypeReference<List<HcnSolPatExtDTO>>() {});		
			List<HcnSolPatExtDTO> dinamica = respuesta.getBody();
			List<PatProcedimiento> patProcedimiento = iPatProcedimientoService.findAll();
			
			//esta parte me permite cruzar entre las patologias de dinamica y las patologias procesadas en Solution
			patProcedimiento.forEach(p ->{
				Predicate<HcnSolPatExtDTO> condicion = dina -> dina.getOidPaciente().equals(p.getIdPaciente()) && dina.getOidRips().equals(p.getIdProcedimiento());	         
				dinamica.removeIf(condicion);
			});		
			
			model.addAttribute("titulo", utf8(this.tituloprocesarpacientesexternos));
			model.addAttribute("listprocpat", dinamica);			
			return "procedimientopatologiaexterno";
		}
		
		// Este metodo me permite visualizar o cargar la solicitud de patologia de pacientes externos para ser procesada
		@RequestMapping(value = "/procesarpatologiaexterno")
		public String crearExterno(@RequestParam(value = "oidpaciente", required = false) Integer oidpaciente, @RequestParam(value = "procedimiento", required = false) Integer procedimiento, Map<String, Object> model, RedirectAttributes flash) {
			PatProcedimiento patProcedimiento = new PatProcedimiento();

			//obtenemos los valores para luego guardar la solicitud			
			this.oidpaciente = oidpaciente;
			this.procedimiento = procedimiento;
			
			// proceso API para buscar la primera descripcion de la solicitud.
			ResponseEntity<List<HcnSolPatDetaTresDTO>> respuestaa = restTemplate.exchange(URLDescripcion + oidpaciente, HttpMethod.GET, null,new ParameterizedTypeReference<List<HcnSolPatDetaTresDTO>>() {});
			List<HcnSolPatDetaTresDTO> dinamicaa = respuestaa.getBody();
			
			
			if (dinamicaa != null && dinamicaa.size() >= 1) {
				int edad = calcularEdad(dinamicaa.get(0).getGpafecnac());
				String sexo = convertirSexo(dinamicaa.get(0).getGpasexpac());

				model.put("nombrecompleto", dinamicaa.get(0).getPacPriNom() + " " + dinamicaa.get(0).getPacSegNom() + " " + dinamicaa.get(0).getPacPriApe() + " " + dinamicaa.get(0).getPacSegApe());
				model.put("servicio", dinamicaa.get(0).getGenerico());				
				model.put("fechasolicitud", dinamicaa.get(0).getHcsfecsol());
				model.put("historia", dinamicaa.get(0).getPacNumDoc());
				model.put("edad", edad);
				model.put("sexo", sexo);
				model.put("aseguradora", dinamicaa.get(0).getGdeNombre());
				model.put("ingreso", dinamicaa.get(0).getAinConsec());							
				
			}

			// proceso API para buscar la segunda descripcion de la solicitud.
			ResponseEntity<List<HcnSolPatDetaDosDTO>> respuestab = restTemplate.exchange(URLDescripcionExt + oidpaciente + '/' + procedimiento, HttpMethod.GET, null,new ParameterizedTypeReference<List<HcnSolPatDetaDosDTO>>() {});
			List<HcnSolPatDetaDosDTO> dinamicab = respuestab.getBody();

			if (dinamicab != null) {				
				model.put("detalles", dinamicab);
			}
			
			
			// proceso API para select de medicos.
			ResponseEntity<List<GenMedicoDTO>> respuestac = restTemplate.exchange(URLMedicos, HttpMethod.GET, null, new ParameterizedTypeReference<List<GenMedicoDTO>>() {});
			List<GenMedicoDTO> medicos = respuestac.getBody();
			
			model.put("medicos", medicos);
			model.put("titulo", utf8(this.tituloprocesarpacientesexternos));		
			model.put("patProcedimiento", patProcedimiento);		
			return "procesarpatologiaexterno";

		}
		
		// Este metodo me permite guardar el proceso de patologia	    
				@RequestMapping(value = "/procesarpatologiaexterno", method = RequestMethod.POST)
				public String guardarPatologiaExterno(@Valid PatProcedimiento patProcedimiento, BindingResult result, Model model, Principal principal, RedirectAttributes flash, SessionStatus status) {
			    				
					//verificamos si hay errores en los campos requeridos.
					if(result.hasErrors()) {
						// proceso API para buscar la primera descripcion de la solicitud.
						ResponseEntity<List<HcnSolPatDetaTresDTO>> respuestaa = restTemplate.exchange(URLDescripcion + oidpaciente, HttpMethod.GET, null,new ParameterizedTypeReference<List<HcnSolPatDetaTresDTO>>() {});
						List<HcnSolPatDetaTresDTO> dinamicaa = respuestaa.getBody();
						
						
						if (dinamicaa != null && dinamicaa.size() >= 1) {
							int edad = calcularEdad(dinamicaa.get(0).getGpafecnac());
							String sexo = convertirSexo(dinamicaa.get(0).getGpasexpac());

							model.addAttribute("nombrecompleto", dinamicaa.get(0).getPacPriNom() + " " + dinamicaa.get(0).getPacSegNom() + " " + dinamicaa.get(0).getPacPriApe() + " " + dinamicaa.get(0).getPacSegApe());
							model.addAttribute("servicio", dinamicaa.get(0).getGenerico());				
							model.addAttribute("fechasolicitud", dinamicaa.get(0).getHcsfecsol());
							model.addAttribute("historia", dinamicaa.get(0).getPacNumDoc());
							model.addAttribute("edad", edad);
							model.addAttribute("sexo", sexo);
							model.addAttribute("aseguradora", dinamicaa.get(0).getGdeNombre());
							model.addAttribute("ingreso", dinamicaa.get(0).getAinConsec());							
							
						}

						// proceso API para buscar la segunda descripcion de la solicitud.
						ResponseEntity<List<HcnSolPatDetaDosDTO>> respuestab = restTemplate.exchange(URLDescripcionExt + oidpaciente + '/' + procedimiento, HttpMethod.GET, null,new ParameterizedTypeReference<List<HcnSolPatDetaDosDTO>>() {});
						List<HcnSolPatDetaDosDTO> dinamicab = respuestab.getBody();

						if (dinamicab != null) {				
							model.addAttribute("detalles", dinamicab);
						}
						
						
						// proceso API para select de medicos.
						ResponseEntity<List<GenMedicoDTO>> respuestac = restTemplate.exchange(URLMedicos, HttpMethod.GET, null, new ParameterizedTypeReference<List<GenMedicoDTO>>() {});
						List<GenMedicoDTO> medicos = respuestac.getBody();
						
						model.addAttribute("medicos", medicos);
						model.addAttribute("titulo", utf8(this.tituloprocesarpacientesexternos));		
						model.addAttribute("patProcedimiento", patProcedimiento);		
						return "procesarpatologiaexterno";
					}
					
					String mensajeFlash = (patProcedimiento.getId() != null) ? "La solicitud de patología fue editada correctamente" : "La solicitud de patología fue creada correctamente";
					patProcedimiento.setIdPaciente(this.oidpaciente);
					patProcedimiento.setIdProcedimiento(this.procedimiento);					
					patProcedimiento.setPacienteInternoExterno("E");
					patProcedimiento.setLoginUsr(principal.getName());
					iPatProcedimientoService.save(patProcedimiento);
					status.setComplete();
					flash.addFlashAttribute("success", mensajeFlash);		
					return "redirect:procedimientopatologiaexterno";
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
