package com.chapumix.solution.app.models.service;

import java.util.List;

import com.chapumix.solution.app.models.entity.AtenEncuDatoBasico;

public interface IAtenEncuDatoBasicoService {
	
	public List<AtenEncuDatoBasico> findAll();

	public void save(AtenEncuDatoBasico atenEncuDatoBasico);
	
	public AtenEncuDatoBasico findById(Long id);
	
	public void delete(Long id);

}
