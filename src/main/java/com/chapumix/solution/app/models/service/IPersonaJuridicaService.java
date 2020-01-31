package com.chapumix.solution.app.models.service;

import java.util.List;

import com.chapumix.solution.app.models.entity.TerPersonaJuridica;

public interface IPersonaJuridicaService {
	
	public List<TerPersonaJuridica> findAll();

	public void save(TerPersonaJuridica terPersonaJuridica);
	
	public TerPersonaJuridica findById(Long id);
	
	public void delete(Long id);

}
