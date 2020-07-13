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
	
	
	//query personalizado para consultar las respuestas de la pregunta 11 negativas por fecha
	@Query("SELECT a FROM AtenEncuDatoBasico a JOIN a.genAreSer g WHERE a.respuesta11 IN (3,4,5) AND a.fechaRegistro BETWEEN ?1 AND ?2")
	List<AtenEncuDatoBasico> findByNegativasOnceStartDateBetween(Date fechaInicial, Date fechaFinal);
	
	//query personalizado para consultar las respuestas de la pregunta 12 negativas por fecha
	@Query("SELECT a FROM AtenEncuDatoBasico a JOIN a.genAreSer g WHERE a.respuesta12 IN (8,9) AND a.fechaRegistro BETWEEN ?1 AND ?2")
	List<AtenEncuDatoBasico> findByNegativasDoceStartDateBetween(Date fechaInicial, Date fechaFinal);
	
}
