package com.chapumix.solution.app.models.service;

import java.util.List;

import com.chapumix.solution.app.models.entity.EstCertificado;

public interface IEstCertificadoService {
	
	public List<EstCertificado> findAll();
	
	//public List<EstCertificado> findByName(String docPaciente);
	public List<EstCertificado> findAllByFechaRegistroAsc();
		
	public EstCertificado findByNameTipo(String docPaciente, String tipoCertificado);
	
	public void save(EstCertificado estCertificado);
	
	public EstCertificado findById(Long id);
	
	public void delete(Long id);

	

}
