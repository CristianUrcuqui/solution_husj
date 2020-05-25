package com.chapumix.solution.app.models.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.chapumix.solution.app.models.dao.IGenAreSerDao;
import com.chapumix.solution.app.models.entity.GenAreSer;

@Service
public class GenAreSerServiceImpl implements IGenAreSerService{
	
	@Autowired
	private IGenAreSerDao genAreSerDao;
	

	@Override
	@Transactional(readOnly = true)//El reanOnly se usa para que la consulta sea solo de lectura
	public List<GenAreSer> findAll() {
		return (List<GenAreSer>) genAreSerDao.findAll();
	}
	
	@Override
	@Transactional(readOnly = true)//El reanOnly se usa para que la consulta sea solo de lectura
	public List<GenAreSer> findByOrderNombre() {
		return genAreSerDao.findByOrderNombre();
	}

	@Override
	@Transactional(readOnly = true)//El reanOnly se usa para que la consulta sea solo de lectura
	public GenAreSer findById(Long id) {
		return genAreSerDao.findById(id).orElse(null);//.orElse es que si lo encuentra retorna el objeto y no devuelve null
	}
	
	@Override
	@Transactional(readOnly = true)//El reanOnly se usa para que la consulta sea solo de lectura
	public GenAreSer findByOid(Integer oid) {		
		return genAreSerDao.findByOid(oid);
	}	
		
	
	@Override
	@Transactional
	public void save(GenAreSer genAreSer) {
		genAreSerDao.save(genAreSer);
	}


	@Override
	@Transactional
	public void delete(Long id) {
		genAreSerDao.deleteById(id);	
	}	

}
