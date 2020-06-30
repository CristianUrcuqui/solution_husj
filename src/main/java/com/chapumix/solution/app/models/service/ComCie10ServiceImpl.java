package com.chapumix.solution.app.models.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.chapumix.solution.app.models.dao.IComCie10Dao;
import com.chapumix.solution.app.models.entity.ComCie10;

@Service
public class ComCie10ServiceImpl implements IComCie10Service{
	
	
	@Autowired
	private IComCie10Dao comCie10Dao;
	

	@Override
	@Transactional(readOnly = true)//El reanOnly se usa para que la consulta sea solo de lectura
	public List<ComCie10> findAll() {
		return (List<ComCie10>) comCie10Dao.findAll();
	}
	
	
	@Override
	@Transactional(readOnly = true)//El reanOnly se usa para que la consulta sea solo de lectura
	public List<ComCie10> findAllAsc() {
		return (List<ComCie10>) comCie10Dao.findAllAsc();
	}

	@Override
	@Transactional(readOnly = true)//El reanOnly se usa para que la consulta sea solo de lectura
	public ComCie10 findById(Long id) {
		return comCie10Dao.findById(id).orElse(null);//.orElse es que si lo encuentra retorna el objeto y no devuelve null
	}
	
	@Override
	@Transactional(readOnly = true)//El reanOnly se usa para que la consulta sea solo de lectura
	public ComCie10 findByCodigo(String codigo) {
		return comCie10Dao.findByCodigo(codigo);
	}
		
	
	@Override
	@Transactional
	public void save(ComCie10 comCie10) {
		comCie10Dao.save(comCie10);
	}


	@Override
	@Transactional
	public void delete(Long id) {
		comCie10Dao.deleteById(id);	
	}
	

}
