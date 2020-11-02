package com.chapumix.solution.app.models.dao;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.chapumix.solution.app.models.entity.FarMipres;

public interface IFarMipresDao extends PagingAndSortingRepository<FarMipres, Long>{
	
	//query personalizado para consultar mipres por documento, prescripcion y cantidad entregada
	@Query("SELECT f FROM FarMipres f JOIN f.genPacien g JOIN f.comTipoTecnologia t WHERE g.pacNumDoc = ?1 AND f.numeroPrescripcion = ?2 AND f.cantidadEntregada = ?3")
	FarMipres findByDocumentoPrescripcionTecnologiaCantidad(String numDocumento, String prescripcion, String cantidadEntregada);
	
	//query personalizado para consultar mipres por documento, prescripcion, cantidad entregada y consecutivo tecnologia
	@Query("SELECT f FROM FarMipres f JOIN f.genPacien g JOIN f.comTipoTecnologia t WHERE g.pacNumDoc = ?1 AND f.numeroPrescripcion = ?2 AND f.cantidadEntregada = ?3 AND f.consecutivoTecnologia = ?4")
	FarMipres findByDocumentoPrescripcionConsecutivoTecnologiaCantidad(String numDocumento, String prescripcion, String cantidadEntregada, Integer consecutivoTecnologia);
	
	//query personalizado para consultar mipres por numero de prescripcion
	@Query("SELECT f FROM FarMipres f WHERE f.numeroPrescripcion = ?1")
	List<FarMipres> findByPrescripcion(String prescripcion);
	
	//query personalizado para consultar mipres no procesados hospitalarios con paginacion
	@Query("SELECT f FROM FarMipres f JOIN f.comAmbito a WHERE (f.procesadoEntrega = 0 OR f.procesadoReporteEntrega = 0 OR f.procesadoFacturacion = 0) AND a.codigo IN (22,30)  ORDER BY f.fechaEntrega ASC")
	Page<FarMipres> findAllCustomPendientesHosp(Pageable pageable);
	
	//query personalizado para consultar mipres no procesados hospitalarios con paginacion y busqueda por documento y prescripcion
	@Query("SELECT f FROM FarMipres f JOIN f.genPacien g JOIN f.comAmbito a WHERE (f.procesadoEntrega = 0 OR f.procesadoReporteEntrega = 0 OR f.procesadoFacturacion = 0) AND a.codigo IN (22,30) AND  f.numeroPrescripcion LIKE %?1% OR g.pacNumDoc LIKE %?1% ORDER BY f.fechaEntrega ASC")
	Page<FarMipres> findAllCustomSearchPendientesHosp(Pageable pageable, String prescripcion);	
	
	//query personalizado para consultar mipres procesados hospitalarios con paginacion
	@Query("SELECT f FROM FarMipres f JOIN f.comAmbito a WHERE (f.procesadoEntrega = 1 AND f.procesadoReporteEntrega = 1 AND f.procesadoFacturacion = 1) AND a.codigo IN (22,30) ORDER BY f.fechaEntrega ASC")
	Page<FarMipres> findAllCustomProcesadosHosp(Pageable pageable);
	
	//query personalizado para consultar mipres procesados hospitalarios con paginacion y busqueda por documento y prescripcion
	@Query("SELECT f FROM FarMipres f JOIN f.genPacien g JOIN f.comAmbito a WHERE (f.procesadoEntrega = 1 AND f.procesadoReporteEntrega = 1 AND f.procesadoFacturacion = 1) AND a.codigo IN (22,30) AND f.numeroPrescripcion LIKE %?1% OR g.pacNumDoc LIKE %?1% ORDER BY f.fechaEntrega ASC")
	Page<FarMipres> findAllCustomSearchProcesadosHosp(Pageable pageable, String prescripcion);
	
	//query personalizado para consultar mipres no procesados ambulatorio con paginacion
	@Query("SELECT f FROM FarMipres f JOIN f.comAmbito a WHERE (f.procesadoEntrega = 0 OR f.procesadoReporteEntrega = 0 OR f.procesadoFacturacion = 0) AND a.codigo IN (11,12,21)  ORDER BY f.fechaEntrega ASC")
	Page<FarMipres> findAllCustomPendientesAmb(Pageable pageable);
	
	//query personalizado para consultar mipres no procesados ambulatorio con paginacion y busqueda por documento y prescripcion
	@Query("SELECT f FROM FarMipres f JOIN f.genPacien g JOIN f.comAmbito a WHERE (f.procesadoEntrega = 0 OR f.procesadoReporteEntrega = 0 OR f.procesadoFacturacion = 0) AND a.codigo IN (11,12,21) AND  f.numeroPrescripcion LIKE %?1% OR g.pacNumDoc LIKE %?1% ORDER BY f.fechaEntrega ASC")
	Page<FarMipres> findAllCustomSearchPendientesAmb(Pageable pageable, String prescripcion);
	
	//query personalizado para consultar mipres procesados ambulatorio con paginacion
	@Query("SELECT f FROM FarMipres f JOIN f.comAmbito a WHERE (f.procesadoEntrega = 1 AND f.procesadoReporteEntrega = 1 AND f.procesadoFacturacion = 1) AND a.codigo IN (11,12,21) ORDER BY f.fechaEntrega ASC")
	Page<FarMipres> findAllCustomProcesadosAmb(Pageable pageable);	
	
	
	//query personalizado para consultar mipres procesados ambulatorios con paginacion y busqueda por documento y prescripcion
	@Query("SELECT f FROM FarMipres f JOIN f.genPacien g JOIN f.comAmbito a WHERE (f.procesadoEntrega = 1 AND f.procesadoReporteEntrega = 1 AND f.procesadoFacturacion = 1) AND a.codigo IN (11,12,21) AND f.numeroPrescripcion LIKE %?1% OR g.pacNumDoc LIKE %?1% ORDER BY f.fechaEntrega ASC")
	Page<FarMipres> findAllCustomSearchProcesadosAmb(Pageable pageable, String prescripcion);
	
	
	//query personalizado para listar mipres por id
	@Query("SELECT f FROM FarMipres f WHERE f.id = ?1")
	List<FarMipres> findByIdMipres(Long prescripcion);
	
	//query personalizado para consultar mipres por fecha de entrega
	@Query("SELECT f FROM FarMipres f WHERE f.procesadoEntrega = 1 AND f.procesadoReporteEntrega = 1 AND f.procesadoFacturacion = 1 AND f.fechaEntrega BETWEEN ?1 AND ?2")
	List<FarMipres> findByStartDateBetween(Date fechaInicial, Date fechaFinal);
	
}
