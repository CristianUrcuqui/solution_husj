package com.chapumix.solution.app.models.service;

import java.util.List;

import com.chapumix.solution.app.models.entity.MedIntCama;

public interface IMedIntCamaService {
	
	public List<MedIntCama> findAll();

	public void save(MedIntCama medIntCama);
	
	public MedIntCama findById(Long id);
	
	public MedIntCama findByIngreso(String ingreso);
	
	public List<MedIntCama> findByDocumentoIngreso(String keyword);
	
	public void delete(Long id);

}
