package com.chapumix.solution.app.models.service;

import java.util.List;

import com.chapumix.solution.app.models.entity.ComUsuario;

public interface IComUsuarioService {
	
	public List<ComUsuario> findAll();

	public void save(ComUsuario comUsuario);
	
	public ComUsuario findById(Long id);
	
	public void delete(Long id);

}
