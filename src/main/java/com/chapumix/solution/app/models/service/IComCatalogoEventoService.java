package com.chapumix.solution.app.models.service;

import java.util.List;

import com.chapumix.solution.app.models.entity.ComCatalogoEvento;
import com.chapumix.solution.app.models.entity.ComTipoEvento;

public interface IComCatalogoEventoService {
	
	public List<ComCatalogoEvento> findAll();
	
	public List<ComCatalogoEvento> findByTipoEvento(ComTipoEvento comTipoEvento);

	public void save(ComCatalogoEvento comCatalogoEvento);
	
	public ComCatalogoEvento findById(Long id);
	
	public void delete(Long id);

}
