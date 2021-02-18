package com.chapumix.solution.app.models.service;

import java.util.List;

import com.chapumix.solution.app.models.entity.CalEvento;

public interface ICalEventoService {
	
	public List<CalEvento> findAll();

	public void save(CalEvento calEvento);
	
	public CalEvento findById(Long id);
	
	public void delete(Long id);

}
