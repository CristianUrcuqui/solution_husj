package com.chapumix.solution.app.models.service;

import java.util.List;

import com.chapumix.solution.app.models.entity.ComRegimen;

public interface IComRegimenService {
	
	public List<ComRegimen> findAll();

	public void save(ComRegimen comRegimen);
	
	public ComRegimen findById(Long id);
	
	public ComRegimen regimenNombre(String nombre);
	
	public void delete(Long id);

}
