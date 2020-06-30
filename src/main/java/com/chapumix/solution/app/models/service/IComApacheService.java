package com.chapumix.solution.app.models.service;

import java.util.List;

import com.chapumix.solution.app.models.entity.ComApache;

public interface IComApacheService {
	
	public List<ComApache> findAll();

	public void save(ComApache comApache);
	
	public ComApache findById(Long id);
	
	public void delete(Long id);

}
