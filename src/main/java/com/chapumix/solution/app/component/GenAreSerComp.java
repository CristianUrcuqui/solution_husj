package com.chapumix.solution.app.component;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.chapumix.solution.app.entity.dto.GenAreSerDTO;
import com.chapumix.solution.app.models.entity.GenAreSer;
import com.chapumix.solution.app.models.service.IGenAreSerService;

@Component
public class GenAreSerComp {
	
	public static final String URLServicio = "http://localhost:9000/api/camasmedico/servicio/"; //se obtuvo de API REST de HcnOrdHospRestController
	
	
	@Autowired
	private IGenAreSerService iGenAreSerService;
	
	@Autowired
	private RestTemplate restTemplate;
		
	@Autowired
	private ResourceLoader loader;
	
	//metodo que se ejecuta de forma automatica todos los dias a la 8:00 AM y 8:00 PM formato de 24 horas
    @Scheduled(cron = "00 00 08,20 * * *", zone="America/Bogota")
	public void cronSincronizaServicio() {    	
    	
    	// proceso API para consultar el servicio.			
    	ResponseEntity<List<GenAreSerDTO>> respuestas = restTemplate.exchange(URLServicio, HttpMethod.GET, null, new ParameterizedTypeReference<List<GenAreSerDTO>>() {});
    	List<GenAreSerDTO> servicio = respuestas.getBody();
    	
    	servicio.forEach(s -> {
    		GenAreSer genAreSer = iGenAreSerService.findByOid(s.getOid());
    		
    		if(genAreSer == null) {
    			GenAreSer agregarServicio = new GenAreSer();
    			agregarServicio.setOid(s.getOid());
    			agregarServicio.setGasCodigo(s.getGasCodigo());
    			agregarServicio.setGasNombre(s.getGasNombre());
    			iGenAreSerService.save(agregarServicio);    			
    			
    		}
    		
    	});
	   }
}
