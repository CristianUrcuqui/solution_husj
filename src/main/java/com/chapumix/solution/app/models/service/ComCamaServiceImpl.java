package com.chapumix.solution.app.models.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.chapumix.solution.app.models.dao.IComCamaDao;
import com.chapumix.solution.app.models.entity.ComCama;

@Service
public class ComCamaServiceImpl implements IComCamaService{
	
	@Autowired
	private IComCamaDao comCamaDao;
	

	@Override
	@Transactional(readOnly = true)//El reanOnly se usa para que la consulta sea solo de lectura
	public List<ComCama> findAll() {
		return (List<ComCama>) comCamaDao.findAll();
	}
	
	@Override
	@Transactional(readOnly = true)//El reanOnly se usa para que la consulta sea solo de lectura
	public ComCama findByCodigo(String codigo) {
		return comCamaDao.findByCodigo(codigo);
	}

	@Override
	@Transactional(readOnly = true)//El reanOnly se usa para que la consulta sea solo de lectura
	public ComCama findById(Long id) {
		return comCamaDao.findById(id).orElse(null);//.orElse es que si lo encuentra retorna el objeto y no devuelve null
	}	
		
	
	@Override
	@Transactional
	public void save(ComCama comCama) {
		comCamaDao.save(comCama);
	}


	@Override
	@Transactional
	public void delete(Long id) {
		comCamaDao.deleteById(id);	
	}	

}
