package com.chapumix.solution.app.models.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.chapumix.solution.app.models.entity.FarMipres;

public interface IFarMipresDao extends PagingAndSortingRepository<FarMipres, Long>{
	
	//query personalizado para consultar los procedimientos realizados por fechas
	@Query("SELECT f FROM FarMipres f JOIN f.genPacien g JOIN f.comTipoTecnologia t WHERE g.pacNumDoc = ?1 AND f.numeroPrescripcion = ?2 AND f.cantidadEntregada = ?3")
	FarMipres findByDocumentoPrescripcionTecnologiaCantidad(String numDocumento, String prescripcion, String cantidadEntregada);
	
	//query personalizado para consultar los procedimientos realizados por fechas
	@Query("SELECT f FROM FarMipres f JOIN f.genPacien g JOIN f.comTipoTecnologia t WHERE g.pacNumDoc = ?1 AND f.numeroPrescripcion = ?2 AND f.cantidadEntregada = ?3 AND f.consecutivoTecnologia = ?4")
	FarMipres findByDocumentoPrescripcionConsecutivoTecnologiaCantidad(String numDocumento, String prescripcion, String cantidadEntregada, Integer consecutivoTecnologia);
	
	//query personalizado para consultar los procedimientos realizados por fechas
	@Query("SELECT f FROM FarMipres f WHERE f.numeroPrescripcion = ?1")
	List<FarMipres> findByPrescripcion(String prescripcion);
	
	
	@Query("SELECT f FROM FarMipres f WHERE f.procesadoEntrega = 0 OR f.procesadoReporteEntrega = 0 OR f.procesadoFacturacion = 0 ORDER BY fechaEntrega ASC")
	Page<FarMipres> findAllCustomPendientes(Pageable pageable);
	
	@Query("SELECT f FROM FarMipres f JOIN f.genPacien g WHERE (f.procesadoEntrega = 0 OR f.procesadoReporteEntrega = 0 OR f.procesadoFacturacion = 0) AND f.numeroPrescripcion LIKE %?1% OR g.pacNumDoc LIKE %?1% ORDER BY f.fechaEntrega ASC")
	Page<FarMipres> findAllCustomSearchPendientes(Pageable pageable, String prescripcion);	
	
	@Query("SELECT f FROM FarMipres f WHERE f.procesadoEntrega = 1 AND f.procesadoReporteEntrega = 1 AND f.procesadoFacturacion = 1 ORDER BY fechaEntrega ASC")
	Page<FarMipres> findAllCustomProcesados(Pageable pageable);
	
	@Query("SELECT f FROM FarMipres f JOIN f.genPacien g WHERE (f.procesadoEntrega = 1 AND f.procesadoReporteEntrega = 1 AND f.procesadoFacturacion = 1) AND f.numeroPrescripcion LIKE %?1% OR g.pacNumDoc LIKE %?1% ORDER BY f.fechaEntrega ASC")
	Page<FarMipres> findAllCustomSearchProcesados(Pageable pageable, String prescripcion);
}
