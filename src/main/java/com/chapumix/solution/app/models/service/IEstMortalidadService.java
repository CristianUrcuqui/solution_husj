package com.chapumix.solution.app.models.service;

import java.util.Date;
import java.util.List;

import com.chapumix.solution.app.models.entity.EstMortalidad;

public interface IEstMortalidadService {
	
	public List<EstMortalidad> findAll();
	
	public List<EstMortalidad> findByStartDateBetween(Date fechaI, Date fechaF);

	public void save(EstMortalidad estMortalidad);
	
	public EstMortalidad findById(Long id);
	
	public EstMortalidad pacienteMortalidad(String numDoc);
	
	public List<EstMortalidad> pacienteMortalidadPDF(String numDoc);
	
	public void delete(Long id);	

}
