package com.chapumix.solution.app.models.dao;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.chapumix.solution.app.models.entity.EstMortalidad;

public interface IEstMortalidadDao extends CrudRepository<EstMortalidad, Long>{

	//query personalizado para obtener la mortalidad por numero de documento
	@Query("SELECT m FROM EstMortalidad m JOIN m.genPacien p  WHERE p.pacNumDoc = ?1")
	EstMortalidad pacienteMortalidad(String numDoc);
	
	//query personalizado para consultar los procedimientos realizados por fechas
	@Query("SELECT m FROM EstMortalidad m WHERE m.fechaRegistro BETWEEN ?1 AND ?2")
	List<EstMortalidad> findByStartDateBetween(Date fechaInicial, Date fechaFinal);
	
}
