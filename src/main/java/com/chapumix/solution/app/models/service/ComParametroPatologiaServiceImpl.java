package com.chapumix.solution.app.models.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.chapumix.solution.app.models.dao.IComParametroPatologiaDao;
import com.chapumix.solution.app.models.entity.ComParametroPatologia;

@Service
public class ComParametroPatologiaServiceImpl implements IComParametroPatologiaService{
	
	@Autowired
	private IComParametroPatologiaDao comParametroDao;

	@Override
	@Transactional(readOnly = true)//El reanOnly se usa para que la consulta sea solo de lectura
	public List<ComParametroPatologia> findAll() {
		return (List<ComParametroPatologia>) comParametroDao.findAll();
	}
	
	@Override
	@Transactional(readOnly = true)//El reanOnly se usa para que la consulta sea solo de lectura
	public ComParametroPatologia findById(Long id) {
		return comParametroDao.findById(id).orElse(null);//.orElse es que si lo encuentra retorna el objeto y no devuelve null
	}
	
	@Override
	@Transactional(readOnly = true)//El reanOnly se usa para que la consulta sea solo de lectura
	public ComParametroPatologia findByName(String seccion) {
		return comParametroDao.findByName(seccion);
	}

	@Override
	@Transactional
	public void save(ComParametroPatologia comParametro) {
		comParametroDao.save(comParametro);		
	}	

	@Override
	@Transactional
	public void delete(Long id) {		
		comParametroDao.deleteById(id);
	}	

}
