package com.chapumix.solution.app.models.dao;

import org.springframework.data.repository.CrudRepository;

import com.chapumix.solution.app.models.entity.CalCalendario;

public interface ICalCalendarioDao extends CrudRepository<CalCalendario, Long>{
	
	public CalCalendario findByNumeroIdentificacion(String identificacion);
		
}
