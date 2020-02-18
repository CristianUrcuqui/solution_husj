package com.chapumix.solution.app.models.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.chapumix.solution.app.models.dao.IComRoleDao;
import com.chapumix.solution.app.models.entity.ComRole;

@Service
public class ComRoleServiceImpl implements IComRoleService{
	
	@Autowired
	private IComRoleDao comRoleDao;
	

	@Override
	@Transactional(readOnly = true)//El reanOnly se usa para que la consulta sea solo de lectura
	public List<ComRole> findAll() {
		return (List<ComRole>) comRoleDao.findAll();
	}

	@Override
	@Transactional(readOnly = true)//El reanOnly se usa para que la consulta sea solo de lectura
	public ComRole findById(Long id) {
		return comRoleDao.findById(id).orElse(null);//.orElse es que si lo encuentra retorna el objeto y no devuelve null
	}	
		
	
	@Override
	@Transactional
	public void save(ComRole ComRole) {
		comRoleDao.save(ComRole);
	}


	@Override
	@Transactional
	public void delete(Long id) {
		comRoleDao.deleteById(id);	
	}	

}
