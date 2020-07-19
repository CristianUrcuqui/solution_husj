package com.chapumix.solution.app.models.service;

import java.util.List;

import com.chapumix.solution.app.models.entity.ComTipoDocumentoMipres;

public interface IComTipoDocumentoMipresService {
	
	public List<ComTipoDocumentoMipres> findAll();

	public void save(ComTipoDocumentoMipres comTipoDocumentoMipres);
	
	public ComTipoDocumentoMipres findById(Long id);
	
	public ComTipoDocumentoMipres tipoDocumento(String tipo);
	
	public void delete(Long id);

}
