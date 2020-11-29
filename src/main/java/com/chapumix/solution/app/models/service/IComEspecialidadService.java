package com.chapumix.solution.app.models.service;

import java.util.List;

import com.chapumix.solution.app.models.entity.ComEspecialidad;

public interface IComEspecialidadService {
	
	public List<ComEspecialidad> findAll();
	
	public List<ComEspecialidad> findAllAsc();

	public void save(ComEspecialidad comEspecialidad);
	
	public ComEspecialidad findById(Long id);
	
	public void delete(Long id);

}
