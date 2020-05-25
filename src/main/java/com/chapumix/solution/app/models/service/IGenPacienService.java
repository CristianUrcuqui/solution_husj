package com.chapumix.solution.app.models.service;

import java.util.List;

import com.chapumix.solution.app.models.entity.GenPacien;

public interface IGenPacienService {
	
	public List<GenPacien> findAll();

	public void save(GenPacien genPacien);
	
	public GenPacien findById(Long id);
	
	public GenPacien findByNumberDoc(String numDoc);
	
	public void delete(Long id);

}
