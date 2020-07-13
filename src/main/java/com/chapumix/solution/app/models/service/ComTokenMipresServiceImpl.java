package com.chapumix.solution.app.models.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.chapumix.solution.app.models.dao.IComTokenMipresDao;
import com.chapumix.solution.app.models.entity.ComTokenMipres;

@Service
public class ComTokenMipresServiceImpl implements IComTokenMipresService{
	
	@Autowired
	private IComTokenMipresDao comTokenMipresDao;
	

	@Override
	@Transactional(readOnly = true)//El reanOnly se usa para que la consulta sea solo de lectura
	public List<ComTokenMipres> findAll() {
		return (List<ComTokenMipres>) comTokenMipresDao.findAll();
	}

	@Override
	@Transactional(readOnly = true)//El reanOnly se usa para que la consulta sea solo de lectura
	public ComTokenMipres findById(Long id) {
		return comTokenMipresDao.findById(id).orElse(null);//.orElse es que si lo encuentra retorna el objeto y no devuelve null
	}	
		
	
	@Override
	@Transactional
	public void save(ComTokenMipres comTokenMipres) {
		comTokenMipresDao.save(comTokenMipres);
	}


	@Override
	@Transactional
	public void delete(Long id) {
		comTokenMipresDao.deleteById(id);	
	}	

}
