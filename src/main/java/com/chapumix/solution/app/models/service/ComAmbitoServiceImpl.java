package com.chapumix.solution.app.models.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.chapumix.solution.app.models.dao.IComAmbitoDao;
import com.chapumix.solution.app.models.entity.ComAmbito;

@Service
public class ComAmbitoServiceImpl implements IComAmbitoService{
	
	@Autowired
	private IComAmbitoDao comAmbitoDao;
	

	@Override
	@Transactional(readOnly = true)//El reanOnly se usa para que la consulta sea solo de lectura
	public List<ComAmbito> findAll() {
		return (List<ComAmbito>) comAmbitoDao.findAll();
	}

	@Override
	@Transactional(readOnly = true)//El reanOnly se usa para que la consulta sea solo de lectura
	public ComAmbito findById(Long id) {
		return comAmbitoDao.findById(id).orElse(null);//.orElse es que si lo encuentra retorna el objeto y no devuelve null
	}
	
	@Override
	@Transactional(readOnly = true)//El reanOnly se usa para que la consulta sea solo de lectura
	public ComAmbito ambitoCodigo(String codigo) {
		return comAmbitoDao.ambitoCodigo(codigo);
	}	
		
	
	@Override
	@Transactional
	public void save(ComAmbito comAmbito) {
		comAmbitoDao.save(comAmbito);
	}


	@Override
	@Transactional
	public void delete(Long id) {
		comAmbitoDao.deleteById(id);	
	}

}
