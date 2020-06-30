package com.chapumix.solution.app.models.dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.chapumix.solution.app.models.entity.ComRegimen;

public interface IComRegimenDao extends CrudRepository<ComRegimen, Long>{
	
	//query personalizado para obtener el paciente en la solicitud procesada
	@Query("SELECT r FROM ComRegimen r WHERE r.nombre = ?1")
	ComRegimen regimenNombre(String nombre);
		
}
