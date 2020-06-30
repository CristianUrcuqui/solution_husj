package com.chapumix.solution.app.models.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.chapumix.solution.app.models.dao.IComRegimenDao;
import com.chapumix.solution.app.models.entity.ComRegimen;

@Service
public class ComRegimenServiceImpl implements IComRegimenService{
	
	
	@Autowired
	private IComRegimenDao comRegimenDao;
	

	@Override
	@Transactional(readOnly = true)//El reanOnly se usa para que la consulta sea solo de lectura
	public List<ComRegimen> findAll() {
		return (List<ComRegimen>) comRegimenDao.findAll();
	}

	@Override
	@Transactional(readOnly = true)//El reanOnly se usa para que la consulta sea solo de lectura
	public ComRegimen findById(Long id) {
		return comRegimenDao.findById(id).orElse(null);//.orElse es que si lo encuentra retorna el objeto y no devuelve null
	}	
		
	
	@Override
	@Transactional
	public void save(ComRegimen comRegimen) {
		comRegimenDao.save(comRegimen);
	}


	@Override
	@Transactional
	public void delete(Long id) {
		comRegimenDao.deleteById(id);	
	}

	@Override
	@Transactional(readOnly = true)//El reanOnly se usa para que la consulta sea solo de lectura
	public ComRegimen regimenNombre(String nombre) {
		return comRegimenDao.regimenNombre(nombre);
	}

}
