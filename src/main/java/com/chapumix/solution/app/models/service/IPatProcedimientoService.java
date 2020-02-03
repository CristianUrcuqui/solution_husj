package com.chapumix.solution.app.models.service;

import java.util.List;

import com.chapumix.solution.app.models.entity.PatProcedimiento;

public interface IPatProcedimientoService {
	
	public List<PatProcedimiento> findAll();

	public void save(PatProcedimiento patProcedimiento);
	
	public PatProcedimiento findById(Long id);
	
	public void delete(Long id);

}
