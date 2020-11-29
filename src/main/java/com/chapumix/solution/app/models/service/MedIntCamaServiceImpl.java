package com.chapumix.solution.app.models.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.chapumix.solution.app.models.dao.IMedIntCamaDao;
import com.chapumix.solution.app.models.entity.MedIntCama;

@Service
public class MedIntCamaServiceImpl implements IMedIntCamaService{
	
	@Autowired
	private IMedIntCamaDao medIntCamaDao;
	

	@Override
	@Transactional(readOnly = true)//El reanOnly se usa para que la consulta sea solo de lectura
	public List<MedIntCama> findAll() {
		return (List<MedIntCama>) medIntCamaDao.findAll();
	}

	@Override
	@Transactional(readOnly = true)//El reanOnly se usa para que la consulta sea solo de lectura
	public MedIntCama findById(Long id) {
		return medIntCamaDao.findById(id).orElse(null);//.orElse es que si lo encuentra retorna el objeto y no devuelve null
	}
	
	@Override
	@Transactional(readOnly = true)//El reanOnly se usa para que la consulta sea solo de lectura
	public MedIntCama findByIngreso(String ingreso) {		
		return medIntCamaDao.findByIngreso(ingreso);
	}
	
	@Override
	@Transactional(readOnly = true)//El reanOnly se usa para que la consulta sea solo de lectura
	public List<MedIntCama> findByDocumentoIngreso(String keyword) {
		return medIntCamaDao.findByDocumentoIngreso(keyword);
	}
		
	
	@Override
	@Transactional
	public void save(MedIntCama medIntCama) {
		medIntCamaDao.save(medIntCama);
	}


	@Override
	@Transactional
	public void delete(Long id) {
		medIntCamaDao.deleteById(id);	
	}	

}
