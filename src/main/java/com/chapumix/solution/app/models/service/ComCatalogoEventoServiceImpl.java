package com.chapumix.solution.app.models.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.chapumix.solution.app.models.dao.IComCatalogoEventoDao;
import com.chapumix.solution.app.models.entity.ComCatalogoEvento;
import com.chapumix.solution.app.models.entity.ComTipoEvento;

@Service
public class ComCatalogoEventoServiceImpl implements IComCatalogoEventoService{
	
	@Autowired
	private IComCatalogoEventoDao comCatalogoEventoDao;
	

	@Override
	@Transactional(readOnly = true)//El reanOnly se usa para que la consulta sea solo de lectura
	public List<ComCatalogoEvento> findAll() {
		return (List<ComCatalogoEvento>) comCatalogoEventoDao.findAll();
	}	
	
	@Override
	@Transactional(readOnly = true)//El reanOnly se usa para que la consulta sea solo de lectura
	public List<ComCatalogoEvento> findByTipoEvento(ComTipoEvento comTipoEvento) {
		return comCatalogoEventoDao.findByTipoEvento(comTipoEvento);
	}

	@Override
	@Transactional(readOnly = true)//El reanOnly se usa para que la consulta sea solo de lectura
	public ComCatalogoEvento findById(Long id) {
		return comCatalogoEventoDao.findById(id).orElse(null);//.orElse es que si lo encuentra retorna el objeto y no devuelve null
	}	
		
	
	@Override
	@Transactional
	public void save(ComCatalogoEvento comCatalogoEvento) {
		comCatalogoEventoDao.save(comCatalogoEvento);
	}


	@Override
	@Transactional
	public void delete(Long id) {
		comCatalogoEventoDao.deleteById(id);	
	}	

}
