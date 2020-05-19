package com.chapumix.solution.app.models.dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.chapumix.solution.app.models.entity.GenAreSer;

public interface IGenAreSerDao extends CrudRepository<GenAreSer, Long>{
	
	//query personalizado para consultar los procedimientos realizados por fechas
		@Query("SELECT g FROM GenAreSer g WHERE g.oid = ?1")
		GenAreSer findByOid(Integer oid);
		
}
