package com.chapumix.solution.app.models.service;

import java.util.List;

import com.chapumix.solution.app.models.entity.GenAreSer;

public interface IGenAreSerService {
	
	public List<GenAreSer> findAll();

	public void save(GenAreSer genAreSer);
	
	public GenAreSer findById(Long id);
	
	public GenAreSer findByOid(Integer oid);
	
	public List<GenAreSer> findByOrderNombre();	
	
	public List<GenAreSer> findByOrderNombreIntenacion();
	
	public void delete(Long id);

}
