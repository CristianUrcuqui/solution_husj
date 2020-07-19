package com.chapumix.solution.app.models.dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.chapumix.solution.app.models.entity.ComTipoDocumentoMipres;

public interface IComTipoDocumentoMipresDao extends CrudRepository<ComTipoDocumentoMipres, Long>{
	
	//query personalizado para obtener el tipo de documento
	@Query("SELECT t FROM ComTipoDocumentoMipres t WHERE t.tipo = ?1")
	ComTipoDocumentoMipres tipoDocumento(String tipo);
		
}
