package com.chapumix.solution.app.models.service;

import java.util.List;

import com.chapumix.solution.app.models.entity.EstSerial;

public interface IEstSerialService {
	
	public List<EstSerial> findAll();

	public void save(EstSerial estSerial);
	
	public EstSerial findById(Long id);
	
	public void delete(Long id);

}
