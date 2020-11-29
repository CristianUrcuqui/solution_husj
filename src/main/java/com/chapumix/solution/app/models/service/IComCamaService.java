package com.chapumix.solution.app.models.service;

import java.util.List;

import com.chapumix.solution.app.models.entity.ComCama;

public interface IComCamaService {
	
	public List<ComCama> findAll();
	
	public ComCama findByCodigo(String codigo);

	public void save(ComCama comCama);
	
	public ComCama findById(Long id);
	
	public void delete(Long id);

}
