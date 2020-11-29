package com.chapumix.solution.app.component;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.chapumix.solution.app.entity.dto.ComCamaDTO;
import com.chapumix.solution.app.models.entity.ComCama;
import com.chapumix.solution.app.models.service.IComCamaService;

@Component
public class ComCamaComp {
	
	public static final String URLCamaTodas = "http://localhost:9000/api/camastodas"; //se obtuvo de API REST de HcnOrdHospRestController
	
	
	@Autowired
	private IComCamaService iComCamaService;
	
	@Autowired
	private RestTemplate restTemplate;	
	
	
	//metodo que se ejecuta de forma automatica todos los dias a la 8:00 AM y 8:00 PM formato de 24 horas
    @Scheduled(cron = "00 00 08,14,20 * * *", zone="America/Bogota")	    
	public void cronSincronizaServicio() {    	
    	
    	// proceso API para consultar el servicio.			
    	ResponseEntity<List<ComCamaDTO>> respuestas = restTemplate.exchange(URLCamaTodas, HttpMethod.GET, null, new ParameterizedTypeReference<List<ComCamaDTO>>() {});
    	List<ComCamaDTO> cama = respuestas.getBody();
    	
    	cama.forEach(c -> {
    		
    		ComCama comCama = iComCamaService.findByCodigo(c.getCcCodigo());
    		
    		if(comCama == null) {
    			ComCama agregarCama = new ComCama();
    			agregarCama.setCodigo(c.getCcCodigo());
    			agregarCama.setNombre(c.getCcNombre());    			
    			iComCamaService.save(agregarCama);    			
    			
    		}
    		
    	});
	   }
}
