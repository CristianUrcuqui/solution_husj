package com.chapumix.solution.app.models.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.chapumix.solution.app.models.entity.EstCertificado;
import com.chapumix.solution.app.models.entity.FarMipres;

public interface IEstCertificadoDao extends PagingAndSortingRepository<EstCertificado, Long>{
	
	//query personalizado para consultar la cantidad de certificados por documento identificacion del paciente
	@Query("SELECT c FROM EstCertificado c JOIN c.estSerial s JOIN s.estTipoCertificado t WHERE c.genPacien.pacNumDoc = ?1 AND t.tipoCertificado = ?2")
	EstCertificado findByNameTipo(String docPaciente, String tipoCertificado);
	
	
	//query personalizado para consultar certificados realizados con paginacion
	@Query("SELECT c FROM EstCertificado c ORDER BY c.fechaRegistro DESC")
	Page<EstCertificado> findAllByFechaRegistroDesc(Pageable pageable);
	
	//query personalizado para consultar certificados con paginacion y busqueda por historia del paciente, serial y medico registra
	@Query("SELECT c FROM EstCertificado c JOIN c.estSerial s JOIN c.genPacien p JOIN c.comUsuario u WHERE CONCAT(s.serial, p.pacNumDoc, u.usuario) LIKE %?1% ORDER BY c.fechaRegistro DESC")
	Page<EstCertificado> findAllCustomSearchRealizados(Pageable pageable, String busqueda);
	
	//query personalizado para consultar la cantidad de certificados por documento identificacion del paciente
	@Query(value = "SELECT * FROM est_certificado ORDER BY fecha_registro DESC LIMIT 25", nativeQuery = true)
	List<EstCertificado> findAllByFechaRegistroAscLimit();
		
}
