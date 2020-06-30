package com.chapumix.solution.app.models.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.chapumix.solution.app.models.dao.IComRetrasoDao;
import com.chapumix.solution.app.models.entity.ComRetraso;

@Service
public class ComRetrasoServiceImpl implements IComRetrasoService{
	
	@Autowired
	private IComRetrasoDao comRetrasoDao;
	

	@Override
	@Transactional(readOnly = true)//El reanOnly se usa para que la consulta sea solo de lectura
	public List<ComRetraso> findAll() {
		return (List<ComRetraso>) comRetrasoDao.findAll();
	}

	@Override
	@Transactional(readOnly = true)//El reanOnly se usa para que la consulta sea solo de lectura
	public ComRetraso findById(Long id) {
		return comRetrasoDao.findById(id).orElse(null);//.orElse es que si lo encuentra retorna el objeto y no devuelve null
	}	
		
	
	@Override
	@Transactional
	public void save(ComRetraso comRetraso) {
		comRetrasoDao.save(comRetraso);
	}


	@Override
	@Transactional
	public void delete(Long id) {
		comRetrasoDao.deleteById(id);	
	}	

}
