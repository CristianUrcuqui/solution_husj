package com.chapumix.solution.app.controllers;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.client.RestTemplate;

import com.chapumix.solution.app.entity.dto.HcnOrdHospCustomDTO;
import com.chapumix.solution.app.entity.dto.HcnOrdHospDTO;
import com.chapumix.solution.app.entity.dto.HcnSolPatDTO;
import com.chapumix.solution.app.models.service.IMedIntCamaService;


@Controller
@PropertySource(value = "application.properties", encoding="UTF-8")
@SessionAttributes("medIntCama")
public class MedIntCamaController {
	
	public static final String URLSolicitudCama = "http://localhost:9000/api/camasmedico";
	
	@Autowired
	private IMedIntCamaService iMedIntCamaService;
	
	@Autowired
	private RestTemplate restTemplate;
	
	@Value("${app.titulomedicinainterna}")
	private String titulomedicinainterna;
	
	@Value("${app.enlaceprincipalmedicinainterna}")
	private String enlaceprincipalmedicinainterna;
	
	@Value("${app.tituloasignacioncamas}")
	private String tituloasignacioncamas;
	
	@Value("${app.enlace6}")
	private String enlace6;
	
	
	/* ----------------------------------------------------------
     * INDEX MEDICINA INTERNA
     * ---------------------------------------------------------- */
	
	//INDEX MEDICINA INTERNA
	@GetMapping("/indexmedicinainterna")
	public String index(Model model) {
		model.addAttribute("titulo", utf8(this.titulomedicinainterna));
		model.addAttribute("medicinainterna", enlaceprincipalmedicinainterna);
		model.addAttribute("enlace6", enlace6);
		return "indexmedicinainterna";
	}
	
	
	//INDEX MEDICINA INTERNA
	@GetMapping("/indexasignacioncamas")
	public String indexCamas(Model model) {
		model.addAttribute("titulo", utf8(this.tituloasignacioncamas));
		model.addAttribute("medicinainterna", enlaceprincipalmedicinainterna);
		model.addAttribute("enlace6", enlace6);
		return "indexasignacioncamas";
	}
	
	/* ----------------------------------------------------------
     * SOLICITUDES CAMA POR MEDICO
     * ---------------------------------------------------------- */

	
	// Este metodo me permite listar todas las solicitudes pendientes de patologia
	@GetMapping("/camasolicitud")
	public String listarSolicitudCama(Model model) {

		// obtengo todos los procedimientos procesados en solution
		/*List<PatProcedimiento> patProcedimiento = iPatProcedimientoService.findAll();*/

		// obtengo el numero de pacientes por procesar
		ResponseEntity<List<HcnOrdHospDTO>> respuesta = restTemplate.exchange(URLSolicitudCama, HttpMethod.GET, null, new ParameterizedTypeReference<List<HcnOrdHospDTO>>() {});
		List<HcnOrdHospDTO> dinamica = respuesta.getBody();		
		
		List<HcnOrdHospCustomDTO> prueba = listaModificada(dinamica);
		

		// esta parte me permite cruzar entre las patologias de pacientes de dinamica y
		// las patologias procesadas en Solution
		/*patProcedimiento.forEach(p -> {
			// Clono la lista para hacer el recorrido del listado de dinamica para que me
			// pueda dejar usar el metodo removeIf y no genere excepcion
			List<HcnSolPatDTO> cloneList = new ArrayList<HcnSolPatDTO>(dinamica);
			cloneList.forEach(c -> {
				Predicate<HcnSolPatDTO> condicion = s -> s.getOidPaciente().equals(p.getIdPaciente())
						&& s.getOidRips().equals(p.getIdProcedimiento());
				dinamica.removeIf(condicion);
			});
		});*/

		model.addAttribute("titulo", utf8(this.tituloasignacioncamas));
		
		model.addAttribute("listprocpat", prueba);
		model.addAttribute("medicinainterna", enlaceprincipalmedicinainterna);
		model.addAttribute("enlace6", enlace6);
		return "camasolicitud";
	}


	// Se usa para codificacion ISO-8859-1 a UTF-8
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
	
	private List<HcnOrdHospCustomDTO> listaModificada(List<HcnOrdHospDTO> dinamica) {
		List<HcnOrdHospCustomDTO> customArray = new ArrayList<HcnOrdHospCustomDTO>();
		dinamica.forEach(d ->{
			int edad = calcularEdad(d.getGpafecnac());
			HcnOrdHospCustomDTO custom = new HcnOrdHospCustomDTO(d.getAinConsec(), d.getPacNumDoc(), d.getPacPriNom(), d.getPacSegNom(), d.getPacPriApe(), d.getPacSegApe(), d.getGpasexpac(), edad, d.getGdeNombre(), d.getHcaCodigo(), d.getHcaNombre(), d.getHcoTipAisl(), d.getHcoTipHosp(), d.getDiaCodigo(), d.getDiaNombre(), d.getHcofecdoc(), d.getHcoObserv(), d.getHcoMotivo(), d.getGmeNomCom());
			customArray.add(custom);
		});
		return customArray;
	}
	
	// Se usa para calcular la edad de una fecha
	private int calcularEdad(Date fechaNacimiento) {

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
		return periodo.getYears();

	}	

}