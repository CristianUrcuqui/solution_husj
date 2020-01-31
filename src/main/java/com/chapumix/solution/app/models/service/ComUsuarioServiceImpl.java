package com.chapumix.solution.app.models.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.chapumix.solution.app.models.dao.IComUsuarioDao;
import com.chapumix.solution.app.models.entity.ComUsuario;

@Service
public class ComUsuarioServiceImpl implements IComUsuarioService{
	
	@Autowired
	private IComUsuarioDao comUsuarioDao;
	

	@Override
	@Transactional(readOnly = true)//El reanOnly se usa para que la consulta sea solo de lectura
	public List<ComUsuario> findAll() {
		return (List<ComUsuario>) comUsuarioDao.findAll();
	}

	@Override
	@Transactional(readOnly = true)//El reanOnly se usa para que la consulta sea solo de lectura
	public ComUsuario findById(Long id) {
		return comUsuarioDao.findById(id).orElse(null);//.orElse es que si lo encuentra retorna el objeto y no devuelve null
	}	
		
	
	@Override
	@Transactional
	public void save(ComUsuario comUsuario) {
		comUsuarioDao.save(comUsuario);
	}


	@Override
	@Transactional
	public void delete(Long id) {
		comUsuarioDao.deleteById(id);	
	}	

}
