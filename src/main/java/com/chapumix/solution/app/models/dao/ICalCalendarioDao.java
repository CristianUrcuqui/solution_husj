package com.chapumix.solution.app.models.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.chapumix.solution.app.models.entity.CalCalendario;

public interface ICalCalendarioDao extends CrudRepository<CalCalendario, Long>{
	
	public CalCalendario findByNumeroIdentificacion(String identificacion);
	
	@Query("SELECT e FROM CalCalendario e WHERE day(e.fechaNacimiento)=day(NOW()) AND month(e.fechaNacimiento)=month(NOW())")
	List<CalCalendario> findUserByDate();
		
}
