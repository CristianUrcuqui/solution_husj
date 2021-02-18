package com.chapumix.solution.app.models.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.chapumix.solution.app.models.entity.ComCatalogoEvento;
import com.chapumix.solution.app.models.entity.ComTipoEvento;

public interface IComCatalogoEventoDao extends CrudRepository<ComCatalogoEvento, Long>{
	
	//query personalizado para obtener el catalogo de eventos
	@Query("select c from ComCatalogoEvento c where c.comTipoEvento = ?1")
	public List<ComCatalogoEvento> findByTipoEvento(ComTipoEvento comTipoEvento);
		
}
