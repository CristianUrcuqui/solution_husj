package com.chapumix.solution.app.models.dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.chapumix.solution.app.models.entity.ComAmbito;

public interface IComAmbitoDao extends CrudRepository<ComAmbito, Long>{
	
	//query personalizado para obtener el paciente en la solicitud procesada
	@Query("SELECT a FROM ComAmbito a WHERE a.codigo = ?1")
	ComAmbito ambitoCodigo(String codigo);
		
}
