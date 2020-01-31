package com.chapumix.solution.app.models.dao;

import org.springframework.data.repository.CrudRepository;

import com.chapumix.solution.app.models.entity.TerPersonaJuridica;

public interface IPersonaJuridicaDao extends CrudRepository<TerPersonaJuridica, Long>{
	
	/*@Query("SELECT u FROM GenUsuario u WHERE u.usunombre = ?1")
	GenUsuario findUserByName(String name);*/	

}
