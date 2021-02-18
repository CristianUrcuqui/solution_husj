package com.chapumix.solution.app.models.service;

import java.util.List;

import com.chapumix.solution.app.models.entity.ComTipoEvento;

public interface IComTipoEventoService {
	
	public List<ComTipoEvento> findAll();

	public void save(ComTipoEvento comTipoEvento);
	
	public ComTipoEvento findById(Long id);
	
	public void delete(Long id);

}
