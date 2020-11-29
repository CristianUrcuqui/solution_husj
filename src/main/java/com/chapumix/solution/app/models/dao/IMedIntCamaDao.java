package com.chapumix.solution.app.models.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.chapumix.solution.app.models.entity.MedIntCama;

public interface IMedIntCamaDao extends CrudRepository<MedIntCama, Long>{
	
	
	//query personalizado para consultar por ingreso
	@Query("SELECT m FROM MedIntCama m WHERE m.ingreso = ?1")
	MedIntCama findByIngreso(String ingreso);
	
	//query personalizado para consultar por documento del paciente o por el ingreso
	@Query("SELECT m FROM MedIntCama m JOIN m.genPacien p WHERE CONCAT(m.ingreso, p.pacNumDoc) LIKE %?1% ORDER BY m.id DESC")
	List<MedIntCama> findByDocumentoIngreso(String keyword);
		
}
