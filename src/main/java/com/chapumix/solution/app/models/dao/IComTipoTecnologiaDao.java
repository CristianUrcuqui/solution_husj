package com.chapumix.solution.app.models.dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.chapumix.solution.app.models.entity.ComTipoTecnologia;

public interface IComTipoTecnologiaDao extends CrudRepository<ComTipoTecnologia, Long>{
	
	//query personalizado para obtener el tipo de tecnologia
	@Query("SELECT t FROM ComTipoTecnologia t WHERE t.tipo = ?1")
	ComTipoTecnologia tipoTecnologia(String tipo);
		
}
