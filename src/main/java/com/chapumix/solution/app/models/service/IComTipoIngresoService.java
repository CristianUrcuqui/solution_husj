package com.chapumix.solution.app.models.service;

import java.util.List;

import com.chapumix.solution.app.models.entity.ComTipoIngreso;

public interface IComTipoIngresoService {
	
	public List<ComTipoIngreso> findAll();
	
	public ComTipoIngreso findByCodigo(Integer codigo);

	public void save(ComTipoIngreso comTipoIngreso);
	
	public ComTipoIngreso findById(Long id);
	
	public ComTipoIngreso tipoIngresoNombre(String nombre);
	
	public void delete(Long id);

}
