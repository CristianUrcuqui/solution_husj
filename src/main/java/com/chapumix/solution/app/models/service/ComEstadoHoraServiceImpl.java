package com.chapumix.solution.app.models.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.chapumix.solution.app.models.dao.IComEstadoHoraDao;
import com.chapumix.solution.app.models.entity.ComEstadoHora;

@Service
public class ComEstadoHoraServiceImpl implements IComEstadoHoraService{
	
	@Autowired
	private IComEstadoHoraDao comEstadoHoraDao;
	

	@Override
	@Transactional(readOnly = true)//El reanOnly se usa para que la consulta sea solo de lectura
	public List<ComEstadoHora> findAll() {
		return (List<ComEstadoHora>) comEstadoHoraDao.findAll();
	}

	@Override
	@Transactional(readOnly = true)//El reanOnly se usa para que la consulta sea solo de lectura
	public ComEstadoHora findById(Long id) {
		return comEstadoHoraDao.findById(id).orElse(null);//.orElse es que si lo encuentra retorna el objeto y no devuelve null
	}	
		
	
	@Override
	@Transactional
	public void save(ComEstadoHora comEstadoHora) {
		comEstadoHoraDao.save(comEstadoHora);
	}


	@Override
	@Transactional
	public void delete(Long id) {
		comEstadoHoraDao.deleteById(id);	
	}	

}
