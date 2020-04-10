package com.chapumix.solution.app.models.service;

import java.util.List;

import com.chapumix.solution.app.models.entity.EstTipoCertificado;

public interface IEstTipoCertificadoService {
	
	public List<EstTipoCertificado> findAll();

	public void save(EstTipoCertificado estTipoCertificado);
	
	public EstTipoCertificado findById(Long id);
	
	public void delete(Long id);

}
