package com.chapumix.solution.app.models.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.chapumix.solution.app.models.dao.IGenPacienDao;
import com.chapumix.solution.app.models.entity.GenPacien;

@Service
public class GenPacienServiceImpl implements IGenPacienService{
	
	@Autowired
	private IGenPacienDao genPacienDao;
	

	@Override
	@Transactional(readOnly = true)//El reanOnly se usa para que la consulta sea solo de lectura
	public List<GenPacien> findAll() {
		return (List<GenPacien>) genPacienDao.findAll();
	}

	@Override
	@Transactional(readOnly = true)//El reanOnly se usa para que la consulta sea solo de lectura
	public GenPacien findById(Long id) {
		return genPacienDao.findById(id).orElse(null);//.orElse es que si lo encuentra retorna el objeto y no devuelve null
	}
	
	@Override
	@Transactional(readOnly = true)//El reanOnly se usa para que la consulta sea solo de lectura
	public GenPacien findByNumberDoc(String numDoc) {		
		return genPacienDao.findByNumberDoc(numDoc);
	}
		
	
	@Override
	@Transactional
	public void save(GenPacien genPacien) {
		genPacienDao.save(genPacien);
	}


	@Override
	@Transactional
	public void delete(Long id) {
		genPacienDao.deleteById(id);	
	}	

}
