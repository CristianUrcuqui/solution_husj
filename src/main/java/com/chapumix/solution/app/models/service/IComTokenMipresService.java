package com.chapumix.solution.app.models.service;

import java.util.List;

import com.chapumix.solution.app.models.entity.ComTokenMipres;

public interface IComTokenMipresService {
	
	public List<ComTokenMipres> findAll();

	public void save(ComTokenMipres comTokenMipres);
	
	public ComTokenMipres findById(Long id);
	
	public void delete(Long id);

}
