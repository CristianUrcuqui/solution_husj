package com.chapumix.solution.app.models.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.chapumix.solution.app.models.entity.ComCie10;

public interface IComCie10Dao extends CrudRepository<ComCie10, Long>{
	
	//query personalizado para consultar los procedimientos realizados por fechas
	@Query("SELECT c FROM ComCie10 c WHERE c.codigo = ?1")
	ComCie10 findByCodigo(String codigo);
	
	
	//query personalizado para consultar los procedimientos ordenados ascendentemente
	@Query("SELECT c FROM ComCie10 c Order By c.nombre ASC")
	List<ComCie10> findAllAsc();
		
		
}
