package com.chapumix.solution.app.models.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.chapumix.solution.app.models.entity.GenAreSer;

public interface IGenAreSerDao extends CrudRepository<GenAreSer, Long>{
	
		//query personalizado para consultar todos los servicios por oid
		@Query("SELECT g FROM GenAreSer g WHERE g.oid = ?1")
		GenAreSer findByOid(Integer oid);
	
	
		//query personalizado para consultar todos los servicio de forma ordenada ascendentemente
		@Query("SELECT g FROM GenAreSer g ORDER BY g.gasNombre ASC")
		List<GenAreSer> findByOrderNombre();
		
}
