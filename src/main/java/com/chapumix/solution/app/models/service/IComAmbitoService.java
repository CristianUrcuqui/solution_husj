package com.chapumix.solution.app.models.service;

import java.util.List;

import com.chapumix.solution.app.models.entity.ComAmbito;

public interface IComAmbitoService {
	
	public List<ComAmbito> findAll();

	public void save(ComAmbito comAmbito);
	
	public ComAmbito findById(Long id);
	
	public ComAmbito ambitoCodigo(String codigo);
	
	public void delete(Long id);

}
