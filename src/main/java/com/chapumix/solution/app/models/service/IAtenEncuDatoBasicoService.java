package com.chapumix.solution.app.models.service;

import java.util.Date;
import java.util.List;

import com.chapumix.solution.app.models.entity.AtenEncuDatoBasico;

public interface IAtenEncuDatoBasicoService {
	
	public List<AtenEncuDatoBasico> findAll();
	
	public List<AtenEncuDatoBasico> findByStartDateBetween(Date fechaInicial, Date fechaFinal);

	public List<AtenEncuDatoBasico> findByNegativasOnceStartDateBetween(Date fechaInicial, Date fechaFinal);
	
	public List<AtenEncuDatoBasico> findByNegativasDoceStartDateBetween(Date fechaInicial, Date fechaFinal);
	
	public void save(AtenEncuDatoBasico atenEncuDatoBasico);
	
	public AtenEncuDatoBasico findById(Long id);
	
	public void delete(Long id);	

}
