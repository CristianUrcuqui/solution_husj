package com.chapumix.solution.app.models.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.chapumix.solution.app.models.entity.FarMipres;

public interface IFarMipresDao extends CrudRepository<FarMipres, Long>{
	
	//query personalizado para consultar los procedimientos realizados por fechas
	@Query("SELECT f FROM FarMipres f JOIN f.genPacien g JOIN f.comTipoTecnologia t WHERE g.pacNumDoc = ?1 AND f.numeroPrescripcion = ?2 AND f.cantidadEntregada = ?3")
	FarMipres findByDocumentoPrescripcionTecnologiaCantidad(String numDocumento, String prescripcion, String cantidadEntregada);
	
	//query personalizado para consultar los procedimientos realizados por fechas
	@Query("SELECT f FROM FarMipres f WHERE f.numeroPrescripcion = ?1")
	List<FarMipres> findByPrescripcion(String prescripcion);
		
}
