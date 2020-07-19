package com.chapumix.solution.app.component;

import java.io.IOException;
import java.net.URI;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.chapumix.solution.app.models.entity.ComTokenMipres;
import com.chapumix.solution.app.models.service.IComTokenMipresService;

@Component
public class ComTokenMipresComp {
	
	@Autowired
	private IComTokenMipresService iComTokenMipresService ;
	
	@Autowired
	private RestTemplate restTemplate;
	
	
	//metodo que se ejecuta de forma automatica cada 10 horas formato de 24 horas    
	@Scheduled(cron = "00 00 00/10 * * *", zone="America/Bogota")	
	public void cronSincronizaToken() throws Exception {    	
    	
    	// proceso para consultar token en solution.		
		ComTokenMipres comTokenMipres =  iComTokenMipresService.findById(1L);
		
		//obtengo el token secundario para guardar en solution
		String tokenSecundario = obtenerTokenSecundario(comTokenMipres.getUrl(), comTokenMipres.getNit(), comTokenMipres.getTokenPrincipal());
		
		//establezco el valor del token secundario
		comTokenMipres.setTokenSecundario(tokenSecundario);
		comTokenMipres.setFechaAlta(new Date());
		iComTokenMipresService.save(comTokenMipres);   	
    	
	   }

	
	
	/* ----------------------------------------------------------
     * METODOS ADICIONALES 
     * ---------------------------------------------------------- */	
	

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
		
	}
}
