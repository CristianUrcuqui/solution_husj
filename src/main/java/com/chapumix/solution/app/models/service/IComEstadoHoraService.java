package com.chapumix.solution.app.models.service;

import java.util.List;

import com.chapumix.solution.app.models.entity.ComEstadoHora;

public interface IComEstadoHoraService {
	
	public List<ComEstadoHora> findAll();

	public void save(ComEstadoHora comEstadoHora);
	
	public ComEstadoHora findById(Long id);
	
	public void delete(Long id);

}
