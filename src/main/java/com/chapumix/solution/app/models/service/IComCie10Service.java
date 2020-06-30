package com.chapumix.solution.app.models.service;

import java.util.List;

import com.chapumix.solution.app.models.entity.ComCie10;

public interface IComCie10Service {
	
	public List<ComCie10> findAll();
	
	public List<ComCie10> findAllAsc();

	public void save(ComCie10 comCie10);
	
	public ComCie10 findById(Long id);
	
	public ComCie10 findByCodigo(String codigo);
	
	public void delete(Long id);

	

}
