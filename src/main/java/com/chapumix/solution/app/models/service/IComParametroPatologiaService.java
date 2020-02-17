package com.chapumix.solution.app.models.service;

import java.util.List;

import com.chapumix.solution.app.models.entity.ComParametroPatologia;

public interface IComParametroPatologiaService {
	
	public List<ComParametroPatologia> findAll();

	public void save(ComParametroPatologia comParametro);
	
	public ComParametroPatologia findById(Long id);
	
	public ComParametroPatologia findByName(String seccion);
	
	public void delete(Long id);

}
