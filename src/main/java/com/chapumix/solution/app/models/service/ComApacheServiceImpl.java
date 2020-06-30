package com.chapumix.solution.app.models.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.chapumix.solution.app.models.dao.IComApacheDao;
import com.chapumix.solution.app.models.entity.ComApache;

@Service
public class ComApacheServiceImpl implements IComApacheService{
	
	@Autowired
	private IComApacheDao comApacheDao;
	

	@Override
	@Transactional(readOnly = true)//El reanOnly se usa para que la consulta sea solo de lectura
	public List<ComApache> findAll() {
		return (List<ComApache>) comApacheDao.findAll();
	}

	@Override
	@Transactional(readOnly = true)//El reanOnly se usa para que la consulta sea solo de lectura
	public ComApache findById(Long id) {
		return comApacheDao.findById(id).orElse(null);//.orElse es que si lo encuentra retorna el objeto y no devuelve null
	}	
		
	
	@Override
	@Transactional
	public void save(ComApache comApache) {
		comApacheDao.save(comApache);
	}


	@Override
	@Transactional
	public void delete(Long id) {
		comApacheDao.deleteById(id);	
	}	

}
