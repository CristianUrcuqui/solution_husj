package com.chapumix.solution.app.models.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.chapumix.solution.app.models.dao.IPersonaJuridicaDao;
import com.chapumix.solution.app.models.entity.TerPersonaJuridica;

@Service
public class PersonaJuridicaServiceImpl implements IPersonaJuridicaService{
	
	
	@Autowired
	private IPersonaJuridicaDao personaJuridicaDao;

	@Override
	@Transactional(readOnly = true)//El reanOnly se usa para que la consulta sea solo de lectura
	public List<TerPersonaJuridica> findAll() {
		return (List<TerPersonaJuridica>) personaJuridicaDao.findAll();
	}
	
	@Override
	@Transactional(readOnly = true)//El reanOnly se usa para que la consulta sea solo de lectura
	public TerPersonaJuridica findById(Long id) {
		return personaJuridicaDao.findById(id).orElse(null);//.orElse es que si lo encuentra retorna el objeto y no devuelve null
	}
	
	@Override
	@Transactional
	public void save(TerPersonaJuridica terPersonaJuridica) {
		personaJuridicaDao.save(terPersonaJuridica);		
	}	

	@Override
	@Transactional
	public void delete(Long id) {
		personaJuridicaDao.deleteById(id);
		
	}

}
