package com.chapumix.solution.app.models.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.chapumix.solution.app.models.dao.IComGeneroDao;
import com.chapumix.solution.app.models.entity.ComGenero;

@Service
public class ComGeneroServiceImpl implements IComGeneroService{
	
	@Autowired
	private IComGeneroDao comGeneroDao;
	

	@Override
	@Transactional(readOnly = true)//El reanOnly se usa para que la consulta sea solo de lectura
	public List<ComGenero> findAll() {
		return (List<ComGenero>) comGeneroDao.findAll();
	}

	@Override
	@Transactional(readOnly = true)//El reanOnly se usa para que la consulta sea solo de lectura
	public ComGenero findById(Long id) {
		return comGeneroDao.findById(id).orElse(null);//.orElse es que si lo encuentra retorna el objeto y no devuelve null
	}	
		
	
	@Override
	@Transactional
	public void save(ComGenero comGenero) {
		comGeneroDao.save(comGenero);
	}


	@Override
	@Transactional
	public void delete(Long id) {
		comGeneroDao.deleteById(id);	
	}	

}
