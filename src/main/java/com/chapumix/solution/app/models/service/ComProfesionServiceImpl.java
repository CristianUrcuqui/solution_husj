package com.chapumix.solution.app.models.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.chapumix.solution.app.models.dao.IComProfesionDao;
import com.chapumix.solution.app.models.entity.ComProfesion;

@Service
public class ComProfesionServiceImpl implements IComProfesionService{
	
	@Autowired
	private IComProfesionDao comProfesionDao;
	

	@Override
	@Transactional(readOnly = true)//El reanOnly se usa para que la consulta sea solo de lectura
	public List<ComProfesion> findAll() {
		return (List<ComProfesion>) comProfesionDao.findAll();
	}

	@Override
	@Transactional(readOnly = true)//El reanOnly se usa para que la consulta sea solo de lectura
	public ComProfesion findById(Long id) {
		return comProfesionDao.findById(id).orElse(null);//.orElse es que si lo encuentra retorna el objeto y no devuelve null
	}	
		
	
	@Override
	@Transactional
	public void save(ComProfesion comProfesion) {
		comProfesionDao.save(comProfesion);
	}


	@Override
	@Transactional
	public void delete(Long id) {
		comProfesionDao.deleteById(id);	
	}	

}
