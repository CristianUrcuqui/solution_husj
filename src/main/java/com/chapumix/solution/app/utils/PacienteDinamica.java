package com.chapumix.solution.app.utils;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.chapumix.solution.app.entity.dto.GenPacienDTO;
import com.chapumix.solution.app.models.entity.ComGenero;
import com.chapumix.solution.app.models.entity.ComTipoDocumento;
import com.chapumix.solution.app.models.entity.GenPacien;
import com.chapumix.solution.app.models.service.IComGeneroService;
import com.chapumix.solution.app.models.service.IComTipoDocumentoService;
import com.chapumix.solution.app.models.service.IGenPacienService;

public class PacienteDinamica {
	
	public static final String URLPaciente = "http://localhost:9000/api/pacientegeneral"; //se obtuvo de API REST de GenPacienRestController
	
	@Autowired
	private IGenPacienService iGenPacienService;
	
	@Autowired
	private IComGeneroService iComGeneroService;
	
	@Autowired
	private IComTipoDocumentoService iComTipoDocumentoService;
	
	@Autowired
	private RestTemplate restTemplate;
	
	//Se usa para sincronizar los pacientes de dinamica a solution
	public GenPacien SincronizarPaciente(GenPacien genPacien, String pacNumDoc) {
		if(genPacien == null) {
			// proceso API para buscar el paciente
			ResponseEntity<List<GenPacienDTO>> respuestaa = restTemplate.exchange(URLPaciente + '/' + pacNumDoc, HttpMethod.GET, null,new ParameterizedTypeReference<List<GenPacienDTO>>() {});
			List<GenPacienDTO> dinamica = respuestaa.getBody();
			
			List<GenPacienDTO> dinamicaFiltro = dinamica.stream().filter(g -> g.getPacNumDoc().equals(pacNumDoc)).collect(Collectors.toList());
			
			GenPacien guardarPaciente = pacienteAgregar(dinamicaFiltro);					
			iGenPacienService.save(guardarPaciente);
			GenPacien obtengoPaciente = iGenPacienService.findByNumberDoc(pacNumDoc);		
			return obtengoPaciente;
		}
			
		return genPacien;		
	}
		
	
	//Se usa para obtener el paciente a guardar
	public GenPacien pacienteAgregar(List<GenPacienDTO> dinamica) {
		GenPacien agregarPaciente = new GenPacien();
		//buscamos el sexo del paciente			
		ComGenero sexoPaciente = iComGeneroService.findById(dinamica.get(0).getGpasexpac().longValue());
			
		//buscamos el tipo de documento del paciente			
		ComTipoDocumento tipoDocumento = iComTipoDocumentoService.findById(dinamica.get(0).getPacTipDoc().longValue());		
				
		agregarPaciente.setOid(dinamica.get(0).getOid());
		agregarPaciente.setPacNumDoc(dinamica.get(0).getPacNumDoc());
		agregarPaciente.setPacPriNom(dinamica.get(0).getPacPriNom());
		agregarPaciente.setPacSegNom(dinamica.get(0).getPacSegNom());
		agregarPaciente.setPacPriApe(dinamica.get(0).getPacPriApe());
		agregarPaciente.setPacSegApe(dinamica.get(0).getPacSegApe());
		agregarPaciente.setGpafecnac(dinamica.get(0).getGpafecnac());
		agregarPaciente.setComGenero(sexoPaciente);
		agregarPaciente.setComTipoDocumento(tipoDocumento);
		return agregarPaciente;
	}		

}
