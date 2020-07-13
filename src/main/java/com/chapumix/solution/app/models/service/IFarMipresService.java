package com.chapumix.solution.app.models.service;

import java.util.List;

import com.chapumix.solution.app.models.entity.FarMipres;

public interface IFarMipresService {
	
	public List<FarMipres> findAll();

	public void save(FarMipres farMipres);
	
	public FarMipres findById(Long id);
	
	public void delete(Long id);

}
