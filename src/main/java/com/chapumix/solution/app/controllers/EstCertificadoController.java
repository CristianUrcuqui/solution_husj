package com.chapumix.solution.app.controllers;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;

import org.apache.commons.io.input.BOMInputStream;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.chapumix.solution.app.entity.dto.EstSerialDTO;
import com.chapumix.solution.app.entity.dto.GenPacienDTO;
import com.chapumix.solution.app.models.entity.ComGenero;
import com.chapumix.solution.app.models.entity.ComTipoDocumento;
import com.chapumix.solution.app.models.entity.ComUsuario;
import com.chapumix.solution.app.models.entity.EstCertificado;
import com.chapumix.solution.app.models.entity.EstSerial;
import com.chapumix.solution.app.models.entity.EstTipoCertificado;
import com.chapumix.solution.app.models.entity.GenAreSer;
import com.chapumix.solution.app.models.entity.GenPacien;
import com.chapumix.solution.app.models.service.IComGeneroService;
import com.chapumix.solution.app.models.service.IComTipoDocumentoService;
import com.chapumix.solution.app.models.service.IComUsuarioService;
import com.chapumix.solution.app.models.service.IEstCertificadoService;
import com.chapumix.solution.app.models.service.IEstSerialService;
import com.chapumix.solution.app.models.service.IEstTipoCertificadoService;
import com.chapumix.solution.app.models.service.IGenAreSerService;
import com.chapumix.solution.app.models.service.IGenPacienService;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;


@Controller
@SessionAttributes("estCertificado")
@PropertySource(value = "application.properties", encoding="UTF-8")
public class EstCertificadoController {
	
	public static final String URLPaciente = "http://localhost:9000/api/pacientegeneral"; //se obtuvo de API REST de GenPacienRestController	
	
	@Autowired
	private IEstTipoCertificadoService iEstTipoCertificadoService;
	
	@Autowired
	private IEstCertificadoService iEstCertificadoService;
	
	@Autowired
	private IEstSerialService iEstSerialService;
	
	@Autowired
	private IGenAreSerService iGenAreSerService;
	
	@Autowired
	private IGenPacienService iGenPacienService;
	
	@Autowired
	private IComGeneroService iComGeneroService;
	
	@Autowired
	private IComUsuarioService iComUsuarioService;
	
	@Autowired
	private IComTipoDocumentoService iComTipoDocumentoService;
	
	@Autowired
	private RestTemplate restTemplate;
	
	@Value("${app.tituloestadistica}")
	private String tituloestadistica;
	
	@Value("${app.titulocertificados}")
	private String titulocertificados;
	
	@Value("${app.titulocertificado}")
	private String titulocertificado;
	
	@Value("${app.titulocargars}")
	private String titulocargars;
	
	@Value("${app.enlaceprincipalestadistica}")
	private String enlaceprincipalestadistica;
	
	@Value("${app.enlace7}")
	private String enlace7;
	
	
	/* ----------------------------------------------------------
     * INDEX ESTADISTICA CERTIFICADO DF Y NV
     * ---------------------------------------------------------- */
	
	//INDEX ESTADISTICA CERTIFICADO DF Y NV
	@GetMapping("/indexcertificado")
	public String index(Model model) {
		
		List<EstSerial> serialesDefuncion = iEstSerialService.countSerialDefuncion();
		List<EstSerial> serialesNacidoVivo = iEstSerialService.countSerialNacidoVivo();		
		
		model.addAttribute("titulo", utf8(this.titulocertificados));
		model.addAttribute("estadistica", enlaceprincipalestadistica);
		model.addAttribute("defuncion", serialesDefuncion.size());
		model.addAttribute("nacidovivo", serialesNacidoVivo.size());
		model.addAttribute("enlace7", enlace7);
		return "indexcertificado";
	}
	
	
	/* ----------------------------------------------------------
     * AJUSTES ESTADISTICA
     * ---------------------------------------------------------- */
	
	// Este metodo me permite visualizar o cargar el formulario para el cargue del consecutivo por medio del CSV
	@GetMapping("/certificadocsvform")
	public String crearParametroCertificado(Map<String, Object> model) {
		EstSerial estSerial = new EstSerial();
		model.put("titulo", utf8(this.titulocargars));
		model.put("estadistica", enlaceprincipalestadistica);		
		model.put("estSerial", estSerial);
		model.put("enlace7", enlace7);
		return "certificadocsvform";
	}
	
	// Este metodo me permite cargar los seriales por medio de un archivo CSV
	@PostMapping("/uploadcsvserial")
    public String uploadCSVFileSerial(@RequestParam("archivo") MultipartFile file, Model model, Principal principal, RedirectAttributes flash, SessionStatus status) {

		// validamos el archivo si esta vacio
        if (file.isEmpty()) {
        	String mensajeFlash = "Debes seleccionar un archivo CSV";            
            model.addAttribute("error", mensajeFlash);
        } else {           
        	
            // analizar el archivo CSV para crear una lista de objetos Serial
            try (Reader reader = new BufferedReader(new InputStreamReader(new BOMInputStream(file.getInputStream()), StandardCharsets.ISO_8859_1))) {
        	
                // create csv bean reader                
				CsvToBean<EstSerialDTO> csvToBean = new CsvToBeanBuilder<EstSerialDTO>(reader)
                        .withType(EstSerialDTO.class)
                        .withSeparator(';')
                        .withIgnoreLeadingWhiteSpace(true)
                        .build();

                // convierte el objeto CsvToBean en una lista de seriales
                List<EstSerialDTO> seriales = csvToBean.parse();
                
                //creamos una lista para guardar los seriales no ingresados
                List<String> errores = new ArrayList<>();
             
                // recorremos la lista de seriales para ser guardada en la base de datos   
                if(!seriales.isEmpty()) {
                	seriales.forEach(s -> {
                		
                		//verificamos la lista y la base para no ingresar duplicados por serial y tipo de identificacion
                		EstSerial estSerial = iEstSerialService.findSerialBySerialAndTipoCertificado(s.getSerial(), s.getIdTipoCertificado());
                		
                		
                		if(estSerial == null) {
                			//buscamos el tipo de certificado
                    		EstTipoCertificado tipoCertificado = iEstTipoCertificadoService.findById(s.getIdTipoCertificado());
                    		
                			EstSerial estSerialGuardar = new EstSerial();
                			estSerialGuardar.setSerial(s.getSerial());
                			estSerialGuardar.setEstado(s.getEstado());
                			estSerialGuardar.setFechaAlta(new Date());
                			estSerialGuardar.setLoginUsrAlta(principal.getName());
                			estSerialGuardar.setSerialInicial(0);
                			estSerialGuardar.setSerialFinal(0);
                			estSerialGuardar.setEstTipoCertificado(tipoCertificado);
                			iEstSerialService.save(estSerialGuardar);
                		}else {
                			errores.add(s.getSerial().toString());
                		}       		
                	});
                }
                
                status.setComplete();
                if(errores.size() > 0) {
                	String errorSeriales = "Seriales duplicados no ingresados: "+errores;            
                    model.addAttribute("error", errorSeriales);        			
        		}         
                 
                // muestra mensaje despues de guardar correctamente
                String mensajeFlash = "El archivo CSV se proceso correctamente";                
                model.addAttribute("success", mensajeFlash);

            } catch (Exception ex) {            	
            	ex.printStackTrace();
            	model.addAttribute("error", "Se produjo un error al procesar el archivo CSV.");                
            }
        }                
        return "certificadocsvform";
    }
	
	
	/* ----------------------------------------------------------
     * CERTIFICADO ESTADISTICA
     * ---------------------------------------------------------- */
	
	// Este metodo me permite listar todos los certificados realizados
	@GetMapping("/certificadoestadistica")
	public String listar(Model model) {		
				
		List<EstCertificado> certificados = iEstCertificadoService.findAllByFechaRegistroDesc();
		model.addAttribute("titulo", utf8(this.titulocertificado));		
		model.addAttribute("estadistica", enlaceprincipalestadistica);
		model.addAttribute("listcertificado", certificados);		
		model.addAttribute("enlace7", enlace7);
		return "certificadoestadistica";
	}
	
	
	// Este metodo me permite visualizar o cargar el formulario del certificado
	@GetMapping("/certificadoform")
	public String crearCertificado(Map<String, Object> model) {		
		
		EstCertificado estCertificado = new EstCertificado();			
		
		model.put("titulo", utf8(this.titulocertificado));
		model.put("tipos", iEstTipoCertificadoService.findAll());
		model.put("servicio", iGenAreSerService.findByOrderNombre());
		model.put("estCertificado", estCertificado);
		model.put("enlace7", enlace7);
		return "certificadoform";
	}
	
	// Este metodo me permite guardar el certificado
	@RequestMapping(value = "/certificadoform", method = RequestMethod.POST)
	public String guardarCertificado(@Valid EstCertificado estCertificado, BindingResult result, Model model, Principal principal, RedirectAttributes flash, SessionStatus status) {
		
		if (result.hasErrors()) {			
						
			model.addAttribute("titulo", utf8(this.tituloestadistica));
			model.addAttribute("tipos", iEstTipoCertificadoService.findAll());
			model.addAttribute("servicio", iGenAreSerService.findByOrderNombre());			
			model.addAttribute("estCerticado", estCertificado);
			model.addAttribute("enlace7", enlace7);
			return "certificadoform";
		}
		
		if(estCertificado.getEstSerial().getEstTipoCertificado() == null) {
			//obtengo el listado de servicios
			List<GenAreSer> servicio = iGenAreSerService.findByOrderNombre();
			
			model.addAttribute("titulo", utf8(this.tituloestadistica));
			model.addAttribute("tipos", iEstTipoCertificadoService.findAll());
			model.addAttribute("servicio", servicio);			
			model.addAttribute("estCerticado", estCertificado);
			model.addAttribute("enlace7", enlace7);
			model.addAttribute("error", "El tipo de certificado es requerido.");
			return "certificadoform";
		}
		else if(estCertificado.getGenPacien().getPacNumDoc() == "") {
			//obtengo el listado de servicios
			List<GenAreSer> servicio = iGenAreSerService.findByOrderNombre();
			
			model.addAttribute("titulo", utf8(this.tituloestadistica));
			model.addAttribute("tipos", iEstTipoCertificadoService.findAll());
			model.addAttribute("servicio", servicio);			
			model.addAttribute("estCerticado", estCertificado);
			model.addAttribute("enlace7", enlace7);
			model.addAttribute("error", "El paciente es requerido");
			return "certificadoform";
		}
	
		String mensajeFlash = (estCertificado.getId() != null) ? "El certificado fue editado correctamente" : "El certificado fue creado correctamente";

		
		//valido si el paciente existe en solution para ir alimentando tabla de pacientes en solution
		GenPacien validarPaciente = iGenPacienService.findByNumberDoc(estCertificado.getGenPacien().getPacNumDoc());
		
		
		// sincronizo paciente de dinamica a solution en caso de que no exista
		sincronizarPaciente(validarPaciente, estCertificado.getGenPacien().getPacNumDoc());		
		
		
		//busco el paciente para agregarlo al certificado
		GenPacien paciente = iGenPacienService.findByNumberDoc(estCertificado.getGenPacien().getPacNumDoc());
		
		//busco el usuario para agregarlo al certificado		
		ComUsuario usuario = iComUsuarioService.findByUsuario(principal.getName());
		
				
		//valido si el paciente tiene varios certificados
		EstCertificado validarCertificado = iEstCertificadoService.findByNameTipo(estCertificado.getGenPacien().getPacNumDoc(), estCertificado.getEstSerial().getEstTipoCertificado().getTipoCertificado());
				
		if(validarCertificado == null ) {
			EstSerial serial = iEstSerialService.findSerialBySerialAndTipo(estCertificado.getEstSerial().getSerial(), estCertificado.getEstSerial().getEstTipoCertificado().getTipoCertificado());
			estCertificado.setLoginUsrAlta(principal.getName());
			estCertificado.setEstSerial(serial);
			estCertificado.setGenPacien(paciente);
			estCertificado.setComUsuario(usuario);
			//guardo el certificado
			iEstCertificadoService.save(estCertificado);			
			
			//actualizo el estado del serial			
			serial.setEstado(true);
			serial.setLoginUsrAct(principal.getName());
			serial.setFechaAltaAct(new Date());
			serial.setSerialInicial(0);
			serial.setSerialFinal(0);
			iEstSerialService.save(serial);
			status.setComplete();
			flash.addFlashAttribute("success", mensajeFlash);
			return "redirect:certificadoestadistica";			
		}else {
			flash.addFlashAttribute("error", "El paciente ya posee un certificado de " + estCertificado.getEstSerial().getEstTipoCertificado().getTipoCertificado());
			return "redirect:certificadoform";			
		}		
	}	
	

	// Este metodo me permite eliminar el certificado
	@RequestMapping(value = "/eliminarcertificado/{id}")
	public String eliminar(@PathVariable(value = "id") Long id, RedirectAttributes flash, Principal principal) {
		
		EstCertificado estCertificado = iEstCertificadoService.findById(id);
		EstSerial serial = iEstSerialService.findById(estCertificado.getEstSerial().getId());		
		
		if (id > 0) {
			iEstCertificadoService.delete(id);
			serial.setEstado(false);
			serial.setFechaDelAct(new Date());
			serial.setLoginUsrDel(principal.getName());
			serial.setSerialInicial(0);
			serial.setSerialFinal(0);
			iEstSerialService.save(serial);
			flash.addFlashAttribute("success", "El certificado fue eliminado correctamente");
		}
		return "redirect:/certificadoestadistica";
	}
		
	
	//Este metodo me permite cargar por FETCH API el serial por tipo de certificado		
	@RequestMapping(value = "/cargarSerial")
	@ResponseBody
	public String cargarSerial(@RequestParam Long id) {
		String serial = "";
		EstSerial estSerial = iEstSerialService.findSerialByTipo(id);
		if(estSerial != null) {
			serial = estSerial.getSerial();
		}else {
			serial = "0";
		}				
		return serial;
	}	
	
	//Este metodo me permite cargar por AJAX el dato del paciente	
	@GetMapping(value = "/cargarPaciente/{term}", produces = { "application/json" })
	@ResponseBody
	public  List<GenPacienDTO> cargarPacientes(@PathVariable String term) {
		// proceso API para buscar el paciente 
		ResponseEntity<List<GenPacienDTO>> respuestaa = restTemplate.exchange(URLPaciente + '/' + term, HttpMethod.GET, null,new ParameterizedTypeReference<List<GenPacienDTO>>() {});
		List<GenPacienDTO> dinamica = respuestaa.getBody();		
		return dinamica;
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
		
	
	// Se usa para sincronizar el paciente de dinamica a solution en caso de que no exista
	private void sincronizarPaciente(GenPacien validarPaciente, @NotEmpty String pacNumDoc) {
		if(validarPaciente == null) {
			
			// proceso API para buscar el paciente
			ResponseEntity<List<GenPacienDTO>> respuestaa = restTemplate.exchange(URLPaciente + '/' + pacNumDoc, HttpMethod.GET, null,new ParameterizedTypeReference<List<GenPacienDTO>>() {});
			List<GenPacienDTO> dinamica = respuestaa.getBody();
			
			//buscamos el sexo del paciente			
			ComGenero sexoPaciente = iComGeneroService.findById(dinamica.get(0).getGpasexpac().longValue());
			
			//buscamos el tipo de documento del paciente			
			ComTipoDocumento tipoDocumento = iComTipoDocumentoService.findById(dinamica.get(0).getPacTipDoc().longValue());
			
			GenPacien agregarPaciente = new GenPacien();
			agregarPaciente.setOid(dinamica.get(0).getOid());
			agregarPaciente.setPacNumDoc(dinamica.get(0).getPacNumDoc());
			agregarPaciente.setPacPriNom(dinamica.get(0).getPacPriNom());
			agregarPaciente.setPacSegNom(dinamica.get(0).getPacSegNom());
			agregarPaciente.setPacPriApe(dinamica.get(0).getPacPriApe());
			agregarPaciente.setPacSegApe(dinamica.get(0).getPacSegApe());
			agregarPaciente.setGpafecnac(dinamica.get(0).getGpafecnac());
			agregarPaciente.setComGenero(sexoPaciente);
			agregarPaciente.setComTipoDocumento(tipoDocumento);
			iGenPacienService.save(agregarPaciente);
			
		}
		
	}

		

}