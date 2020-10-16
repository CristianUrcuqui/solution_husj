package com.chapumix.solution.app.models.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.chapumix.solution.app.models.entity.EstCertificado;

public interface IEstCertificadoService {
	
	public List<EstCertificado> findAll();
	
	public Page<EstCertificado> findAllByFechaRegistroDesc(Pageable pageable);
	
	public Page<EstCertificado> findAllCustomSearchRealizados(Pageable pageable, String busqueda);
	
	//public List<EstCertificado> findByName(String docPaciente);
	public List<EstCertificado> findAllByFechaRegistroAscLimit();
		
	public EstCertificado findByNameTipo(String docPaciente, String tipoCertificado);
	
	public void save(EstCertificado estCertificado);
	
	public EstCertificado findById(Long id);
	
	public void delete(Long id);

	

}
