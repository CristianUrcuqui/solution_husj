package com.chapumix.solution.app.models.service;

import java.util.List;

import com.chapumix.solution.app.models.entity.ComRole;

public interface IComRoleService {
	
	public List<ComRole> findAll();

	public void save(ComRole comRole);
	
	public ComRole findById(Long id);
	
	public void delete(Long id);

}
