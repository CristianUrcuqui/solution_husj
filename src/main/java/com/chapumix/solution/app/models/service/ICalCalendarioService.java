package com.chapumix.solution.app.models.service;

import java.util.List;

import com.chapumix.solution.app.models.entity.CalCalendario;

public interface ICalCalendarioService {
	
	public List<CalCalendario> findAll();
	
	public List<CalCalendario> findUserByDate();
	
	public CalCalendario findByNumeroIdentificacion(String identificacion);

	public void save(CalCalendario calCalendario);
	
	public CalCalendario findById(Long id);
	
	public void delete(Long id);

}
