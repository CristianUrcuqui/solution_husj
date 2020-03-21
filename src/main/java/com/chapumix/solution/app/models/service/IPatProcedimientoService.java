package com.chapumix.solution.app.models.service;

import java.util.Date;
import java.util.List;

import com.chapumix.solution.app.models.entity.PatProcedimiento;

public interface IPatProcedimientoService {
	
	public List<PatProcedimiento> findAll();
	
	public List<PatProcedimiento> findAllProcedimientos();
	
	public List<PatProcedimiento> findByStartDateBetween(Date fechaInicial, Date FechaFinal);
	
	public List<PatProcedimiento> findByIdPatologo(Integer patologoAsigna, Integer patologoReAsigna);
	
	public List<PatProcedimiento> findByStartDateBetweenIdPatologo(Date fechaInicial, Date fechaFinal, Integer patologoAsigna, Integer patologoReAsigna); 

	public void save(PatProcedimiento patProcedimiento);
	
	public PatProcedimiento findById(Long id);	
	
	public void delete(Long id);	

}
