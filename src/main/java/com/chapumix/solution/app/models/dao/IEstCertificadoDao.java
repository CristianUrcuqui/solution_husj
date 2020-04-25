package com.chapumix.solution.app.models.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.chapumix.solution.app.models.entity.EstCertificado;

public interface IEstCertificadoDao extends CrudRepository<EstCertificado, Long>{
	
	//query personalizado para consultar la cantidad de certificados por documento identificacion del paciente
	/*@Query("SELECT c FROM EstCertificado c WHERE c.docPaciente = ?1")
	List<EstCertificado> findByName(String docPaciente);*/
	
	//query personalizado para consultar la cantidad de certificados por documento identificacion del paciente
	@Query("SELECT c FROM EstCertificado c JOIN c.estSerial s JOIN s.estTipoCertificado t WHERE c.docPaciente = ?1 AND t.tipoCertificado = ?2")
	EstCertificado findByNameTipo(String docPaciente, String tipoCertificado);
		
	//query personalizado para consultar la cantidad de certificados por documento identificacion del paciente
	@Query("SELECT c FROM EstCertificado c ORDER BY c.fechaRegistro DESC")
	List<EstCertificado> findAllByFechaRegistroAsc();
		
}
