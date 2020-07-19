package com.chapumix.solution.app.models.service;

import java.util.List;

import com.chapumix.solution.app.models.entity.ComTipoTecnologia;

public interface IComTipoTecnologiaService {
	
	public List<ComTipoTecnologia> findAll();

	public void save(ComTipoTecnologia comTipoTecnologia);
	
	public ComTipoTecnologia findById(Long id);
	
	public ComTipoTecnologia tipoTecnologia(String tipo);
	
	public void delete(Long id);

}
