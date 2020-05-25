package com.chapumix.solution.app.models.dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.chapumix.solution.app.models.entity.GenPacien;

public interface IGenPacienDao extends CrudRepository<GenPacien, Long>{
	
		//query personalizado para consultar el paciente por el numero de documento
		@Query("SELECT p FROM GenPacien p WHERE p.pacNumDoc = ?1")
		GenPacien findByNumberDoc(String numDoc);
		
}
