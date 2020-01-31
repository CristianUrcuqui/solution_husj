package com.chapumix.solution.app.models.dao;

import org.springframework.data.repository.CrudRepository;
import com.chapumix.solution.app.models.entity.ComUsuario;

public interface IComUsuarioDao extends CrudRepository<ComUsuario, Long>{
	
	//public ComUsuario findByUsername(String username);
	//@Query("select u from ComUsuario u where u.usuario = ?1")
	public ComUsuario findByUsuario(String username);
}
