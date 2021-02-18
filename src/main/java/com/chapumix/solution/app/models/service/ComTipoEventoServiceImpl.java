package com.chapumix.solution.app.models.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.chapumix.solution.app.models.dao.IComTipoEventoDao;
import com.chapumix.solution.app.models.entity.ComTipoEvento;

@Service
public class ComTipoEventoServiceImpl implements IComTipoEventoService{
	
	@Autowired
	private IComTipoEventoDao comTipoEventoDao;
	

	@Override
	@Transactional(readOnly = true)//El reanOnly se usa para que la consulta sea solo de lectura
	public List<ComTipoEvento> findAll() {
		return (List<ComTipoEvento>) comTipoEventoDao.findAll();
	}

	@Override
	@Transactional(readOnly = true)//El reanOnly se usa para que la consulta sea solo de lectura
	public ComTipoEvento findById(Long id) {
		return comTipoEventoDao.findById(id).orElse(null);//.orElse es que si lo encuentra retorna el objeto y no devuelve null
	}	
		
	
	@Override
	@Transactional
	public void save(ComTipoEvento comTipoEvento) {
		comTipoEventoDao.save(comTipoEvento);
	}


	@Override
	@Transactional
	public void delete(Long id) {
		comTipoEventoDao.deleteById(id);	
	}	

}
