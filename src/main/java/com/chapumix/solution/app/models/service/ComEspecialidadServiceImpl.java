package com.chapumix.solution.app.models.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.chapumix.solution.app.models.dao.IComEspecialidadDao;
import com.chapumix.solution.app.models.entity.ComEspecialidad;

@Service
public class ComEspecialidadServiceImpl implements IComEspecialidadService{
	
	@Autowired
	private IComEspecialidadDao comEspecialidadDao;
	

	@Override
	@Transactional(readOnly = true)//El reanOnly se usa para que la consulta sea solo de lectura
	public List<ComEspecialidad> findAll() {
		return (List<ComEspecialidad>) comEspecialidadDao.findAll();
	}
	
	@Override
	@Transactional(readOnly = true)//El reanOnly se usa para que la consulta sea solo de lectura
	public List<ComEspecialidad> findAllAsc() {
		return comEspecialidadDao.findAllAsc();
	}		

	@Override
	@Transactional(readOnly = true)//El reanOnly se usa para que la consulta sea solo de lectura
	public ComEspecialidad findById(Long id) {
		return comEspecialidadDao.findById(id).orElse(null);//.orElse es que si lo encuentra retorna el objeto y no devuelve null
	}	
		
	
	@Override
	@Transactional
	public void save(ComEspecialidad comEspecialidad) {
		comEspecialidadDao.save(comEspecialidad);
	}


	@Override
	@Transactional
	public void delete(Long id) {
		comEspecialidadDao.deleteById(id);	
	}	

}
