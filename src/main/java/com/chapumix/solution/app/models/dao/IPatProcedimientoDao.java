package com.chapumix.solution.app.models.dao;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.chapumix.solution.app.models.entity.PatProcedimiento;

public interface IPatProcedimientoDao extends CrudRepository<PatProcedimiento, Long>{
	
		//query personalizado para consultar las procedimientos realizados por fechas
		@Query("SELECT p FROM PatProcedimiento p WHERE p.fechaRegistro BETWEEN ?1 AND ?2")
		List<PatProcedimiento> findByStartDateBetween(Date fechaInicial, Date fechaFinal);
		
		//query personalizado para consultar las procedimientos por medico o especialista
		@Query("SELECT p FROM PatProcedimiento p WHERE p.idPatologo = ?1 OR p.idPatologoReasigando = ?2")
		List<PatProcedimiento> findByIdPatologo(Integer patologoAsigna, Integer patologoReAsigna);
		
		//query personalizado para consultar por fecha inicial, fecha final y por medico o especialista
		@Query("SELECT p FROM PatProcedimiento p WHERE p.fechaRegistro BETWEEN ?1 AND ?2 AND p.idPatologo = ?3 OR p.idPatologoReasigando = ?4")
		List<PatProcedimiento> findByStartDateBetweenIdPatologo(Date fechaInicial, Date fechaFinal, Integer patologoAsigna, Integer patologoReAsigna);

}
