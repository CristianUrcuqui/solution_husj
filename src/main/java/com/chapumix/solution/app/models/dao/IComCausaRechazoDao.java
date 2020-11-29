package com.chapumix.solution.app.models.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.chapumix.solution.app.models.entity.ComCausaRechazo;

public interface IComCausaRechazoDao extends CrudRepository<ComCausaRechazo, Long>{
	
	//query personalizado para obtener las causas del rechaso ordenadas
	@Query("SELECT c FROM ComCausaRechazo c ORDER BY c.nombre ASC")
	List<ComCausaRechazo> findAllAsc();
		
}
