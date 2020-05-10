package com.chapumix.solution.app.models.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.chapumix.solution.app.models.dao.IAtenEncuDatoBasicoDao;
import com.chapumix.solution.app.models.entity.AtenEncuDatoBasico;

@Service
public class AtenEncuDatoBasicoServiceImpl implements IAtenEncuDatoBasicoService{
	
	@Autowired
	private IAtenEncuDatoBasicoDao atenEncuDatoBasicoDao;
	

	@Override
	@Transactional(readOnly = true)//El reanOnly se usa para que la consulta sea solo de lectura
	public List<AtenEncuDatoBasico> findAll() {
		return (List<AtenEncuDatoBasico>) atenEncuDatoBasicoDao.findAll();
	}

	@Override
	@Transactional(readOnly = true)//El reanOnly se usa para que la consulta sea solo de lectura
	public AtenEncuDatoBasico findById(Long id) {
		return atenEncuDatoBasicoDao.findById(id).orElse(null);//.orElse es que si lo encuentra retorna el objeto y no devuelve null
	}	
		
	
	@Override
	@Transactional
	public void save(AtenEncuDatoBasico atenEncuDatoBasico) {
		atenEncuDatoBasicoDao.save(atenEncuDatoBasico);
	}


	@Override
	@Transactional
	public void delete(Long id) {
		atenEncuDatoBasicoDao.deleteById(id);	
	}	

}
