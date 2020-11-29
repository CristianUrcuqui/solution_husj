package com.chapumix.solution.app.models.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.chapumix.solution.app.models.dao.IComCausaRechazoDao;
import com.chapumix.solution.app.models.entity.ComCausaRechazo;

@Service
public class ComCausaRechazoServiceImpl implements IComCausaRechazoService{
	
	@Autowired
	private IComCausaRechazoDao comCausaRechadoDao;
	

	@Override
	@Transactional(readOnly = true)//El reanOnly se usa para que la consulta sea solo de lectura
	public List<ComCausaRechazo> findAll() {
		return (List<ComCausaRechazo>) comCausaRechadoDao.findAll();
	}
	
	@Override
	@Transactional(readOnly = true)//El reanOnly se usa para que la consulta sea solo de lectura
	public List<ComCausaRechazo> findAllAsc() {
		return comCausaRechadoDao.findAllAsc();
	}

	@Override
	@Transactional(readOnly = true)//El reanOnly se usa para que la consulta sea solo de lectura
	public ComCausaRechazo findById(Long id) {
		return comCausaRechadoDao.findById(id).orElse(null);//.orElse es que si lo encuentra retorna el objeto y no devuelve null
	}	
		
	
	@Override
	@Transactional
	public void save(ComCausaRechazo comCausaRechazo) {
		comCausaRechadoDao.save(comCausaRechazo);
	}


	@Override
	@Transactional
	public void delete(Long id) {
		comCausaRechadoDao.deleteById(id);	
	}

	

}
