package com.chapumix.solution.app.models.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.chapumix.solution.app.models.dao.IComTipoTecnologiaDao;
import com.chapumix.solution.app.models.entity.ComTipoTecnologia;

@Service
public class ComTipoTecnologiaServiceImpl implements IComTipoTecnologiaService{	
	
	@Autowired
	private IComTipoTecnologiaDao ComTipoTecnologiaDao; 
	

	@Override
	@Transactional(readOnly = true)//El reanOnly se usa para que la consulta sea solo de lectura
	public List<ComTipoTecnologia> findAll() {
		return (List<ComTipoTecnologia>) ComTipoTecnologiaDao.findAll();
	}

	@Override
	@Transactional(readOnly = true)//El reanOnly se usa para que la consulta sea solo de lectura
	public ComTipoTecnologia findById(Long id) {
		return ComTipoTecnologiaDao.findById(id).orElse(null);//.orElse es que si lo encuentra retorna el objeto y no devuelve null
	}	
		
	
	@Override
	@Transactional
	public void save(ComTipoTecnologia comTipoTecnologia) {
		ComTipoTecnologiaDao.save(comTipoTecnologia);
	}


	@Override
	@Transactional
	public void delete(Long id) {
		ComTipoTecnologiaDao.deleteById(id);	
	}	

}
