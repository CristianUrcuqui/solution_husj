package com.chapumix.solution.app.models.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.chapumix.solution.app.models.dao.IComPrismDao;
import com.chapumix.solution.app.models.entity.ComPrism;

@Service
public class ComPrismServiceImpl implements IComPrismService{
	
	@Autowired
	private IComPrismDao comPrismDao;
	

	@Override
	@Transactional(readOnly = true)//El reanOnly se usa para que la consulta sea solo de lectura
	public List<ComPrism> findAll() {
		return (List<ComPrism>) comPrismDao.findAll();
	}

	@Override
	@Transactional(readOnly = true)//El reanOnly se usa para que la consulta sea solo de lectura
	public ComPrism findById(Long id) {
		return comPrismDao.findById(id).orElse(null);//.orElse es que si lo encuentra retorna el objeto y no devuelve null
	}	
		
	
	@Override
	@Transactional
	public void save(ComPrism comPrism) {
		comPrismDao.save(comPrism);
	}


	@Override
	@Transactional
	public void delete(Long id) {
		comPrismDao.deleteById(id);	
	}	

}
