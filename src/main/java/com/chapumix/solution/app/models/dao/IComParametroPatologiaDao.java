package com.chapumix.solution.app.models.dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.chapumix.solution.app.models.entity.ComParametroPatologia;

public interface IComParametroPatologiaDao extends CrudRepository<ComParametroPatologia, Long>{	
	
	//query personalizado para consultar las procedimientos realizados por fechas
	@Query("SELECT p FROM ComParametroPatologia p WHERE p.nombre = ?1")
	ComParametroPatologia findByName(String seccion);
	
}
