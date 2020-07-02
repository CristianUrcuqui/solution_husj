package com.chapumix.solution.app.models.service;

import java.util.List;

import com.chapumix.solution.app.models.entity.ComPrism;

public interface IComPrismService {
	
	public List<ComPrism> findAll();

	public void save(ComPrism comPrism);
	
	public ComPrism findById(Long id);
	
	public void delete(Long id);

}
