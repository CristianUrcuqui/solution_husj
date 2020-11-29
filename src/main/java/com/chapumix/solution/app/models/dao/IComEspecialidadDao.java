package com.chapumix.solution.app.models.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.chapumix.solution.app.models.entity.ComEspecialidad;

public interface IComEspecialidadDao extends CrudRepository<ComEspecialidad, Long>{
	
	//query personalizado para obtener las especialidades ordenadas
	@Query("SELECT e FROM ComEspecialidad e ORDER BY e.nombre ASC")
	List<ComEspecialidad> findAllAsc();
		
}
