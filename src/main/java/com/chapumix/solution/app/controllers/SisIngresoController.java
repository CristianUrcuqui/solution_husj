package com.chapumix.solution.app.controllers;

import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.chapumix.solution.app.entity.dto.AdnIngresoDTO;
import com.chapumix.solution.app.entity.dto.GenUsuarioDTO;


@Controller
@PropertySource(value = "application.properties", encoding="UTF-8")
@SessionAttributes("sisIngreso")
public class SisIngresoController {
	
	
	public static final String URLGetIngreso = "http://localhost:9000/api/ingresoporpaciente"; //url para obtener el ingreso desde dinamica
	
	public static final String URLPutIngreso = "http://localhost:9000/api/actualizaringreso"; //url para actualizar el ingreso en dinamica
	
	public static final String URLGetUsuarioOID = "http://localhost:9000/api/usuarios/username"; //url para obtener el usuario de dinamica
	
	
	@Autowired
	private RestTemplate restTemplate;
	
	@Value("${app.titulosistemas}")
	private String titulosistemas;
	
	@Value("${app.enlaceprincipalsistemas}")
	private String enlaceprincipalsistemas;
	
	@Value("${app.tituloanulacioningreso}")
	private String tituloanulacioningreso;
	
	@Value("${app.anularingreso}")
	private String anularingreso;
	
	@Value("${app.enlace11}")
	private String enlace11;
	
	
	/* ----------------------------------------------------------
     * INDEX SISTEMAS
     * ---------------------------------------------------------- */
	
	//INDEX SISTEMAS
	@GetMapping("/indexsistemas")
	public String index(Model model) {
		model.addAttribute("titulo", utf8(this.titulosistemas));
		model.addAttribute("sistemas", enlaceprincipalsistemas);
		model.addAttribute("enlace11", enlace11);
		return "indexsistemas";
	}
	
	
	//INDEX SISTEMAS
	@GetMapping("/indexanulacioningreso")
	public String indexAnulacion(Model model) {
		model.addAttribute("titulo", utf8(this.tituloanulacioningreso));
		model.addAttribute("sistemas", enlaceprincipalsistemas);
		model.addAttribute("enlace11", enlace11);
		return "indexanulacioningreso";
	}
	
	
	// Este metodo me permite visualizar o cargar el formulario para anular el ingreso
	@GetMapping("/anulaingreso")
	public String anularIngreso(Map<String, Object> model, RedirectAttributes flash) {
		
		boolean listado = false;
			
		model.put("titulo", utf8(this.anularingreso));
		model.put("sistemas", enlaceprincipalsistemas);
		model.put("listado", listado);
		model.put("enlace11", enlace11);		
		return "anulaingreso";
	}
	
	
	// Este metodo me permite consultar el ingreso desde dinamica para ser anulado
	@RequestMapping("/procesaranulacioningreso")	
	public String procesarAnulacion(Model model, @RequestParam(value = "ingreso", required = false) String ingreso, RedirectAttributes flash, HttpServletResponse response) {
			
		boolean listado = false;
				
		// valida que el ingreso digitado en el formulario no este vacio
		if (ingreso.isEmpty()) {		
			model.addAttribute("titulo", utf8(this.anularingreso));
			model.addAttribute("sistemas", enlaceprincipalsistemas);
			model.addAttribute("enlace11", enlace11);
			model.addAttribute("error", "El número de ingreso es requerido");
			return "anulaingreso";
		}		
			
		
			
		// proceso API para buscar el paciente 
		ResponseEntity<List<AdnIngresoDTO>> respuesta = restTemplate.exchange(URLGetIngreso + '/' + ingreso, HttpMethod.GET, null,new ParameterizedTypeReference<List<AdnIngresoDTO>>() {});
		List<AdnIngresoDTO> ingresoDinamica = respuesta.getBody();
		
		//valido si el ingreso existe en dinamica
		if(!ingresoDinamica.isEmpty()) {
			listado = true;
			model.addAttribute("titulo", utf8(this.anularingreso));
			model.addAttribute("sistemas", enlaceprincipalsistemas);
			model.addAttribute("enlace11", enlace11);
			model.addAttribute("listado", listado);
			model.addAttribute("ingresoDinamica", ingresoDinamica);
		}else {
			model.addAttribute("titulo", utf8(this.anularingreso));
			model.addAttribute("sistemas", enlaceprincipalsistemas);
			model.addAttribute("enlace11", enlace11);
			model.addAttribute("listado", listado);
			model.addAttribute("error", "Ingreso no encontrado");
		}
		
		return  "anulaingreso";			
	}
	
	
	// Este metodo me permite anular el ingreso en dinamica
	@RequestMapping(value = "/anularingreso")
	public String anularEntrega(Model model, @RequestParam(name = "oid") Integer oid, @RequestParam(name = "motivo") String motivo, @RequestParam(name = "ingreso") String ingreso, RedirectAttributes flash, Principal principal) throws Exception {
		if (motivo.isEmpty()) {		
			model.addAttribute("titulo", utf8(this.anularingreso));
			model.addAttribute("sistemas", enlaceprincipalsistemas);
			model.addAttribute("enlace11", enlace11);			
			flash.addFlashAttribute("error", "El motivo de la anulación es requerida");		
			return "redirect:procesaranulacioningreso"+"?"+"ingreso"+"="+ingreso;			
		}
		
		Map<String, String> webServiceInfo =  guardarAPIRestIngreso(oid, motivo, principal);
		
		if (StringUtils.equals(webServiceInfo.get("success"), "200")) {
			model.addAttribute("titulo", utf8(this.anularingreso));
			model.addAttribute("sistemas", enlaceprincipalsistemas);
			model.addAttribute("enlace11", enlace11);
			model.addAttribute("success", "Ingreso anulado correctamente");
			return  "anulaingreso";
		}else {
			model.addAttribute("titulo", utf8(this.anularingreso));
			model.addAttribute("sistemas", enlaceprincipalsistemas);
			model.addAttribute("enlace11", enlace11);
			model.addAttribute("error", "Ha ocurrido un error en el proceso");
			return  "anulaingreso";
		}	
		
	}		
		


	/* ----------------------------------------------------------
     * METODOS ADICIONALES 
     * ---------------------------------------------------------- */


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
	
	
	// Se usa para actualizar el ingreso en dinamica
	private Map<String, String> guardarAPIRestIngreso(Integer oid, String motivo, Principal principal) throws Exception {
		
		Map<String, String> map = new HashMap<>();		
		
		//genero la url para consultar
		String urlEncadenada = URLPutIngreso+'/'+oid;
				
		//convierto la fecha de entrega en string y formato YYYY-MM-DD
		//String fechaEntrega = formatoFecha(farMipres.getFechaEntrega());		
		
		// proceso API para buscar el usuario
		ResponseEntity<GenUsuarioDTO> respuesta = restTemplate.exchange(URLGetUsuarioOID + '/' + principal.getName(), HttpMethod.GET, null,new ParameterizedTypeReference<GenUsuarioDTO>() {});
		GenUsuarioDTO usuario = respuesta.getBody();	
	
		
	    //Especificamos la URL y configuro el objeto CloseableHttpClient
		CloseableHttpClient httpclient = HttpClients.createDefault();
		
		//Se crea una solicitud PUT (si es post HttpPost y si es get HttpGet) y pasamos el URL del recurso y también asigne encabezados a este objeto de colocación
		HttpPut httpPut = new HttpPut(urlEncadenada);
        httpPut.setHeader("Accept", "application/json");
        httpPut.setHeader("Content-type", "application/json");        
        
        
        //creo el json que sera pasado al objeto StringEntity
        JSONObject parametros = new JSONObject();
        parametros.put("oid", oid);
        parametros.put("ainEstado", 2);
        parametros.put("ainmotanu", motivo);
        parametros.put("adfecanula", formatoFecha(new Date()));
        parametros.put("adusuanula", usuario.getOid());
         
        
		
        //Proporciono la solicitud json en el objeto StringEntity y asígnela al objeto puesto.
        StringEntity stringEntity = new StringEntity(parametros.toString());
        stringEntity.setContentEncoding("UTF-8");        
        httpPut.setEntity(stringEntity);     
 
        //Envio la solicitud usando HttpPut -> Método de ejecución PUT
        HttpResponse response = httpclient.execute(httpPut); 
        
            
        // verificamos que la respuesta o estado sea 200
     	if (response.getStatusLine().getStatusCode() == 200) {
     		map.put("success", Integer.toString(response.getStatusLine().getStatusCode()));     			
     		return map;
   		}else {
   			map.put("error", "Ha ocurrido un error en el proceso");
   			return map;
   		}		
	}
	
	
	// Se usa para dar formato a fechas de dinamica
	private String formatoFecha(Date date) throws ParseException {
		// convierto la fecha que entra en formato Date
		Locale locale = Locale.getDefault();        
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
		String fechaConversion = sdf.format(date);		
		return fechaConversion;		
	}
	
}