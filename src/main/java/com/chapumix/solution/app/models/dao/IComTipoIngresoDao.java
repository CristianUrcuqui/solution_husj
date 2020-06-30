package com.chapumix.solution.app.models.dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.chapumix.solution.app.models.entity.ComTipoIngreso;

public interface IComTipoIngresoDao extends CrudRepository<ComTipoIngreso, Long>{
	
	
	// query personalizado para consultar por codigo
	@Query("SELECT t FROM ComTipoIngreso t WHERE t.codigo = ?1")
	ComTipoIngreso findByCodigo(Integer codigo);
	
	//query personalizado para obtener el paciente en la solicitud procesada
	@Query("SELECT t FROM ComTipoIngreso t WHERE t.nombre = ?1")
	ComTipoIngreso tipoIngresoNombre(String nombre);
		
}
