package com.chapumix.solution.app.models.service;

import java.util.List;

import com.chapumix.solution.app.models.entity.ComProfesion;

public interface IComProfesionService {
	
	public List<ComProfesion> findAll();

	public void save(ComProfesion comProfesion);
	
	public ComProfesion findById(Long id);
	
	public void delete(Long id);

}
