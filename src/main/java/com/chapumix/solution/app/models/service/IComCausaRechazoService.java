package com.chapumix.solution.app.models.service;

import java.util.List;

import com.chapumix.solution.app.models.entity.ComCausaRechazo;

public interface IComCausaRechazoService {
	
	public List<ComCausaRechazo> findAll();
	
	public List<ComCausaRechazo> findAllAsc();

	public void save(ComCausaRechazo comCausaRechazo);
	
	public ComCausaRechazo findById(Long id);
	
	public void delete(Long id);

}
