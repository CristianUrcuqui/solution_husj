package com.chapumix.solution.app.models.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.chapumix.solution.app.models.dao.IFarMipresDao;
import com.chapumix.solution.app.models.entity.FarMipres;

@Service
public class FarMipresServiceImpl implements IFarMipresService{
	
	@Autowired
	private IFarMipresDao farMipresDao;
	

	@Override
	@Transactional(readOnly = true)//El reanOnly se usa para que la consulta sea solo de lectura
	public List<FarMipres> findAll() {
		return (List<FarMipres>) farMipresDao.findAll();
	}

	@Override
	@Transactional(readOnly = true)//El reanOnly se usa para que la consulta sea solo de lectura
	public FarMipres findById(Long id) {
		return farMipresDao.findById(id).orElse(null);//.orElse es que si lo encuentra retorna el objeto y no devuelve null
	}	
		
	
	@Override
	@Transactional
	public void save(FarMipres farMipres) {
		farMipresDao.save(farMipres);
	}


	@Override
	@Transactional
	public void delete(Long id) {
		farMipresDao.deleteById(id);	
	}	

}
