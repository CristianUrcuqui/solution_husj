package com.chapumix.solution.app.models.service;

import java.util.List;

import com.chapumix.solution.app.models.entity.ComRetraso;

public interface IComRetrasoService {
	
	public List<ComRetraso> findAll();

	public void save(ComRetraso comRetraso);
	
	public ComRetraso findById(Long id);
	
	public void delete(Long id);

}
