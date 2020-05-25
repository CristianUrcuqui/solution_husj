package com.chapumix.solution.app.models.service;

import java.util.List;

import com.chapumix.solution.app.models.entity.ComTipoDocumento;

public interface IComTipoDocumentoService {
	
	public List<ComTipoDocumento> findAll();

	public void save(ComTipoDocumento comTipoDocumento);
	
	public ComTipoDocumento findById(Long id);
	
	public void delete(Long id);

}
