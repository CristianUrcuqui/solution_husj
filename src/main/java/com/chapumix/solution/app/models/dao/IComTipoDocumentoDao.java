package com.chapumix.solution.app.models.dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.chapumix.solution.app.models.entity.ComTipoDocumento;

public interface IComTipoDocumentoDao extends CrudRepository<ComTipoDocumento, Long>{
	
	//query personalizado para obtener el tipo de documento
	@Query("SELECT t FROM ComTipoDocumento t WHERE t.tipo = ?1")
	ComTipoDocumento tipoDocumento(String tipo);
		
}
