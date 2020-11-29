package com.chapumix.solution.app.models.service;

import java.util.List;

import com.chapumix.solution.app.models.entity.ComEstadoTramite;

public interface IComEstadoTramiteService {
	
	public List<ComEstadoTramite> findAll();

	public void save(ComEstadoTramite comEstadoTramite);
	
	public ComEstadoTramite findById(Long id);
	
	public void delete(Long id);

}
