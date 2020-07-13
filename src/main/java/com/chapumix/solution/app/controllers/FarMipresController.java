package com.chapumix.solution.app.controllers;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.security.Principal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
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

import com.chapumix.solution.app.models.entity.ComTokenMipres;
import com.chapumix.solution.app.models.entity.FarMipres;
import com.chapumix.solution.app.models.service.IComTipoDocumentoMipresService;
import com.chapumix.solution.app.models.service.IComTipoTecnologiaService;
import com.chapumix.solution.app.models.service.IComTokenMipresService;
import com.chapumix.solution.app.models.service.IFarMipresService;


@Controller
@SessionAttributes({"farMipres","comTokenMipres"})
@PropertySource(value = "application.properties", encoding="UTF-8")
public class FarMipresController {
	
	public static final String MetodoPutEntrega = "https://wsmipres.sispro.gov.co/WSSUMMIPRESNOPBS/api/EntregaAmbito/"; //url mipres metodo para put entrega
	
	@Autowired
	private IComTokenMipresService iComTokenMipresService;
	
	@Autowired
	private IComTipoTecnologiaService iComTipoTecnologiaService;
	
	@Autowired
	private IComTipoDocumentoMipresService iComTipoDocumentoMipresService;
	
	@Autowired
	private IFarMipresService iFarMipresService; 
	
	@Autowired
	private RestTemplate restTemplate;
	
	@Value("${app.titulomipres}")
	private String titulomipres;
	
	@Value("${app.titulotoken}")
	private String titulotoken;
	
	@Value("${app.tituloentrega}")
	private String tituloentrega;
	
	
	
	@Value("${app.enlaceprincipalfarmacia}")
	private String enlaceprincipalfarmacia;
	
	@Value("${app.enlace10}")
	private String enlace10;
	
	
	/* ----------------------------------------------------------
     * INDEX ESTADISTICA MORTALIDAD
     * ---------------------------------------------------------- */
	
	//INDEX ESTADISTICA MORTALIDAD
	@GetMapping("/indexmipres")
	public String index(Model model) {
		model.addAttribute("titulo", utf8(this.titulomipres));
		model.addAttribute("farmacia", enlaceprincipalfarmacia);
		model.addAttribute("enlace10", enlace10);
		return "indexmipres";
	}
	
	// Este metodo me permite guardar el token
	@RequestMapping(value = "/tokenform", method = RequestMethod.POST)
	public String guardarToken(@Valid ComTokenMipres comTokenMipres, BindingResult result, Model model, RedirectAttributes flash, SessionStatus status) throws Exception {
		if(result.hasErrors()) {
			model.addAttribute("titulo", utf8(this.titulomipres));							
			model.addAttribute("comTokenMipres", comTokenMipres);
			model.addAttribute("farmacia", enlaceprincipalfarmacia);
			model.addAttribute("enlace10", enlace10);
			return "tokenform";
		}
			
		String mensajeFlash = (comTokenMipres.getId() != null) ? "El token fue editado correctamente" : "El token fue creado correctamente";
		
		//obtengo el token secundario para guardar en solution
		String tokenSecundario = obtenerTokenSecundario(comTokenMipres.getUrl(), comTokenMipres.getNit(), comTokenMipres.getTokenPrincipal());
		//establezco el valor del token secundario
		comTokenMipres.setTokenSecundario(tokenSecundario);
		comTokenMipres.setFechaAlta(new Date());
		iComTokenMipresService.save(comTokenMipres);
		status.setComplete();
		flash.addFlashAttribute("success", mensajeFlash);		
		return "redirect:indexmipres";
	}	
	
	

	// Este metodo me permite cargar los datos para editar el token
	@RequestMapping(value = "/tokenform")
	public String editarToken(@RequestParam(value = "id", required = false) Long id, Map<String, Object> model, RedirectAttributes flash) throws ParseException {
		ComTokenMipres comTokenMipres = null;
		if(id > 0) {			
			comTokenMipres = iComTokenMipresService.findById(id);
			if(comTokenMipres == null) {
				flash.addFlashAttribute("error", "El ID del token no existe en la base de datos");
				return "redirect:/indexmipres";
			}
		}else {
			flash.addFlashAttribute("error", "El ID del token no puede ser 0");
			return "redirect:/indexmipres";
		}
		model.put("titulo", utf8(this.titulotoken));
		model.put("comTokenMipres", comTokenMipres);
		model.put("farmacia", enlaceprincipalfarmacia);
		model.put("enlace10", enlace10);
		return "tokenform";
	}
	
	
	// Este metodo me permite visualizar o cargar el formulario de la entrega
	@GetMapping("/entregaform")
	public String crear(Map<String, Object> model) {		
		FarMipres farMipres = new FarMipres();
		model.put("titulo", utf8(this.tituloentrega));
		model.put("farMipres", farMipres);
		model.put("tecnologia", iComTipoTecnologiaService.findAll());
		model.put("tipodocumento", iComTipoDocumentoMipresService.findAll());
		model.put("farmacia", enlaceprincipalfarmacia);
		model.put("enlace10", enlace10);
		return "entregaform";
	}
	
	// Este metodo me permite guardar la entrega
	@RequestMapping(value = "/entregaform", method = RequestMethod.POST)
	public String guardarEntrega(@Valid FarMipres farMipres, BindingResult result, Model model, RedirectAttributes flash, SessionStatus status, Principal principal) throws Exception {
		if(result.hasErrors()) {
			model.addAttribute("titulo", utf8(this.tituloentrega));
			model.addAttribute("farMipres", farMipres);
			model.addAttribute("tecnologia", iComTipoTecnologiaService.findAll());
			model.addAttribute("tipodocumento", iComTipoDocumentoMipresService.findAll());
			model.addAttribute("farmacia", enlaceprincipalfarmacia);
			model.addAttribute("enlace10", enlace10);
			return "entregaform";
		}
		
		//String webServiceInfo = guardarWebServiceMipres(farMipres);
		Map<String, String> webServiceInfo =  guardarWebServiceMipres(farMipres);
		
		
		if(StringUtils.equals(webServiceInfo.get("success"), "200")) {
			String mensajeFlash = (farMipres.getId() != null) ? "La entrega fue editada correctamente" : "La entrega fue creada correctamente "+"IdEntrega: "+webServiceInfo.get("IdEntrega");
			farMipres.setIdMipress(webServiceInfo.get("Id"));
			farMipres.setIdEntregaMipress(webServiceInfo.get("IdEntrega"));
			farMipres.setEnviado(true);
			farMipres.setLoginUsrAlta(principal.getName());
			iFarMipresService.save(farMipres);
			status.setComplete();
			flash.addFlashAttribute("success", mensajeFlash);
			return "redirect:entregaform";
		}else {
			flash.addFlashAttribute("error", webServiceInfo.get("error"));
			return "redirect:entregaform";
		}
		
		/*if(webServiceInfo.get("success").equals("200")) {
			String mensajeFlash = (farMipres.getId() != null) ? "La entrega fue editada correctamente" : "La entrega fue creada correctamente "+"IdEntrega: "+webServiceInfo.get("IdEntrega");
			farMipres.setIdMipress(webServiceInfo.get("Id"));
			farMipres.setIdEntregaMipress(webServiceInfo.get("IdEntrega"));
			farMipres.setEnviado(true);
			farMipres.setLoginUsrAlta(principal.getName());
			iFarMipresService.save(farMipres);
			status.setComplete();
			flash.addFlashAttribute("success", mensajeFlash);
			return "redirect:entregaform";
		}else {
			flash.addFlashAttribute("error", webServiceInfo.get("error"));
			return "redirect:entregaform";
		}*/
			
			
		
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
	
	//Se usa para obtener token secundario
	private String obtenerTokenSecundario(String url, String nit, String tokenPrincipal) throws IOException, Exception {		
		
		String urlEncadenada = url+nit+'/'+tokenPrincipal;
	    URI uri = new URI(urlEncadenada);
	 
	    ResponseEntity<String> resultado = restTemplate.getForEntity(uri, String.class);
	    
	    if(resultado.getStatusCodeValue() == 200) {
	    	//me permite eliminar las comillas al inicio y al final de la cadena
	    	String tokenSecundario = resultado.getBody().toString().replaceAll("^\\\"+|\\\"+$", "");
	    	return tokenSecundario;
	    }else {
	    	return "La solicitud GET no funcionó";	    	
	    }		
		
		
		/*URL urlEncadenada = new URL(url+nit+'/'+tokenPrincipal);
		HttpURLConnection con = (HttpURLConnection) urlEncadenada.openConnection();
		con.setRequestMethod("GET");		
		con.setRequestProperty("Content-Type", "application/json");
		con.setConnectTimeout(10000); //10 segundos de espera para la conexion
		con.setReadTimeout(10000); //10 segundos de lectura para la conexion	
		int responseCode = con.getResponseCode();		
		if (responseCode == HttpURLConnection.HTTP_OK) { // verifico respuesta 200 sucess
			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();			
			String tokenSecundario = response.toString().replaceAll("^\\\"+|\\\"+$", "");			
			return tokenSecundario;
		} else {			
			return "La solicitud GET no funcionó";
		}*/		
	}
	
	//Se usa para hacer put en el web service de mipres
	private Map<String, String> guardarWebServiceMipres(FarMipres farMipres) throws Exception {
		//https://wsmipres.sispro.gov.co/WSSUMMIPRESNOPBS/api/EntregaAmbito/{nit}/{token}
		
		Map<String, String> map = new HashMap<>();
		
		//obtengo los datos del token secundario guardados en solution
		ComTokenMipres comTokenMipres = iComTokenMipresService.findById(1L); 
		
		String urlEncadenada = MetodoPutEntrega+comTokenMipres.getNit()+'/'+comTokenMipres.getTokenSecundario();
				
		//convierto la fecha de entrega en string y formato YYYY-MM-DD
		String fechaEntrega = formatoFecha(farMipres.getFechaEntrega());		
		
	    //Especificamos la URL y configuro el objeto CloseableHttpClient
		CloseableHttpClient httpclient = HttpClients.createDefault();
		
		//Se crea una solicitud PUT (si es post HttpPost) y pasamos el URL del recurso y también asigne encabezados a este objeto de colocación
		HttpPut httpPut = new HttpPut(urlEncadenada);
        httpPut.setHeader("Accept", "application/json");
        httpPut.setHeader("Content-type", "application/json");
        
        //creo el json que sera pasado al objeto StringEntity
        JSONObject parametros = new JSONObject();
        parametros.put("NoPrescripcion", farMipres.getNumeroPrescripcion());
        parametros.put("TipoTec", farMipres.getComTipoTecnologia().getTipo());
        parametros.put("ConTec", farMipres.getConsecutivoTecnologia());
        parametros.put("TipoIDPaciente", farMipres.getComTipoDocumentoMipres().getTipo());
        parametros.put("NoIDPaciente", farMipres.getNumeroDocumentoPaciente());
        parametros.put("NoEntrega", farMipres.getNumeroEntrega());
        parametros.put("CodSerTecEntregado", farMipres.getCodigoServicio());
        parametros.put("CantTotEntregada", farMipres.getCantidadEntregada());
        parametros.put("EntTotal", farMipres.getEntregaTotal());
        parametros.put("CausaNoEntrega", farMipres.getCausaNoEntrega());
        parametros.put("FecEntrega", fechaEntrega);
        parametros.put("NoLote", farMipres.getLoteEntregado());        
		
        //Proporciono la solicitud json en el objeto StringEntity y asígnela al objeto puesto.
        StringEntity stringEntity = new StringEntity(parametros.toString());
        stringEntity.setContentEncoding("UTF-8");        
        httpPut.setEntity(stringEntity);     
 
        //Envio la solicitud usando HttpPut -> Método de ejecución PUT
        HttpResponse response = httpclient.execute(httpPut);    
        
        
        //creo un objeto HttpEntity para obtener el resultado en String de la peticion
        HttpEntity entity = response.getEntity();
        String content = EntityUtils.toString(entity);
        
        //System.out.println(content);        
        
        //convierto la respuesta de la peticion String a Json para mensajes personalizados
        JSONObject jsonContent = new JSONObject();
        if(response.getStatusLine().getStatusCode() == 200) {
        	jsonContent = new JSONObject(content.replaceAll("\\[", "").replaceAll("\\]", ""));//busca "[" y "]" y los reemplaza por espacios en blanco  
        }else {
        	jsonContent = new JSONObject(content); 
        }
        
        //System.out.println(jsonContent.get("Errors").toString().replaceAll("[^ a-zA-Záéíóú]", ""));
        //System.out.println(jsonContent.get("Message")); 
         
        //verificamos que la respuesta o estado sea 200
        if (response.getStatusLine().getStatusCode() == 200) {        	
            map.put("success", Integer.toString(response.getStatusLine().getStatusCode()));
            map.put("Id", jsonContent.get("Id").toString());
            map.put("IdEntrega", jsonContent.get("IdEntrega").toString());
            return map;
        	
        }else if (response.getStatusLine().getStatusCode() == 422) {        	
        	map.put("error", jsonContent.get("Message").toString() +",  "+ jsonContent.get("Errors").toString().replaceAll("[^ a-zA-Záéíóú]", ""));
        	return map;
        }else {
        	map.put("error", jsonContent.get("Message").toString());
        	return map;
        }
    }
	
	
	// Se usa para dar formato a fechas de dinamica
	private String formatoFecha(Date fecha) {

		// convierto la fecha que entra en formato Date
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String fechaConversion = sdf.format(fecha);		
		return fechaConversion;

	}
		
}