package com.chapumix.solution.app.models.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.chapumix.solution.app.models.dao.ICalEventoDao;
import com.chapumix.solution.app.models.entity.CalEvento;

@Service
public class CalEventoServiceImpl implements ICalEventoService{
	
	@Autowired
	private ICalEventoDao calEventoDao;
	

	@Override
	@Transactional(readOnly = true)//El reanOnly se usa para que la consulta sea solo de lectura
	public List<CalEvento> findAll() {
		return (List<CalEvento>) calEventoDao.findAll();
	}

	@Override
	@Transactional(readOnly = true)//El reanOnly se usa para que la consulta sea solo de lectura
	public CalEvento findById(Long id) {
		return calEventoDao.findById(id).orElse(null);//.orElse es que si lo encuentra retorna el objeto y no devuelve null
	}	
		
	
	@Override
	@Transactional
	public void save(CalEvento calEvento) {
		calEventoDao.save(calEvento);
	}


	@Override
	@Transactional
	public void delete(Long id) {
		calEventoDao.deleteById(id);	
	}	

}
