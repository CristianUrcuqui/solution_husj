package com.chapumix.solution.app.models.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.chapumix.solution.app.models.dao.IEstMortalidadDao;
import com.chapumix.solution.app.models.entity.EstMortalidad;

@Service
public class EstMortalidadServiceImpl implements IEstMortalidadService{
	
	@Autowired
	private IEstMortalidadDao estMortalidadDao;
	

	@Override
	@Transactional(readOnly = true)//El reanOnly se usa para que la consulta sea solo de lectura
	public List<EstMortalidad> findAll() {
		return (List<EstMortalidad>) estMortalidadDao.findAll();
	}
	
	@Override
	@Transactional(readOnly = true)//El reanOnly se usa para que la consulta sea solo de lectura
	public List<EstMortalidad> findByStartDateBetween(Date fechaInicial, Date fechaFinal) {
		return estMortalidadDao.findByStartDateBetween(fechaInicial, fechaFinal);
	}

	@Override
	@Transactional(readOnly = true)//El reanOnly se usa para que la consulta sea solo de lectura
	public EstMortalidad findById(Long id) {
		return estMortalidadDao.findById(id).orElse(null);//.orElse es que si lo encuentra retorna el objeto y no devuelve null
	}
	
	@Override
	@Transactional(readOnly = true)//El reanOnly se usa para que la consulta sea solo de lectura
	public EstMortalidad pacienteMortalidad(String numDoc) {		
		return estMortalidadDao.pacienteMortalidad(numDoc);
	}	
		
	
	@Override
	@Transactional
	public void save(EstMortalidad comRole) {
		estMortalidadDao.save(comRole);
	}


	@Override
	@Transactional
	public void delete(Long id) {
		estMortalidadDao.deleteById(id);	
	}	

}
