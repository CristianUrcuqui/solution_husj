package com.chapumix.solution.app.models.dao;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.chapumix.solution.app.models.entity.AtenEncuDatoBasico;

public interface IAtenEncuDatoBasicoDao extends CrudRepository<AtenEncuDatoBasico, Long>{
	
	
	//query personalizado para consultar los procedimientos realizados por fechas
	@Query("SELECT a FROM AtenEncuDatoBasico a WHERE a.fechaRegistro BETWEEN ?1 AND ?2")
	List<AtenEncuDatoBasico> findByStartDateBetween(Date fechaInicial, Date fechaFinal);	
		
}
