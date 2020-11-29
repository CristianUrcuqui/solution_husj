package com.chapumix.solution.app.models.dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.chapumix.solution.app.models.entity.ComCama;

public interface IComCamaDao extends CrudRepository<ComCama, Long>{
	
	
	//query personalizado para consultar todos las camas por codigo
	@Query("SELECT c FROM ComCama c WHERE c.codigo = ?1")
	ComCama findByCodigo(String codigo);
		
}
