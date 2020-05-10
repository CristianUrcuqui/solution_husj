package com.chapumix.solution.app.models.service;

import java.util.List;

import com.chapumix.solution.app.models.entity.ComGenero;

public interface IComGeneroService {
	
	public List<ComGenero> findAll();

	public void save(ComGenero comGenero);
	
	public ComGenero findById(Long id);
	
	public void delete(Long id);

}
