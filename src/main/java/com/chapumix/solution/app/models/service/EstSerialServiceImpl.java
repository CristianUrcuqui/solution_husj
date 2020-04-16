package com.chapumix.solution.app.models.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.chapumix.solution.app.models.dao.IEstSerialDao;
import com.chapumix.solution.app.models.entity.EstSerial;

@Service
public class EstSerialServiceImpl implements IEstSerialService{
	
	@Autowired	
	private IEstSerialDao estSerialDao;

	@Override
	@Transactional(readOnly = true)//El reanOnly se usa para que la consulta sea solo de lectura
	public List<EstSerial> findAll() {
		return (List<EstSerial>) estSerialDao.findAll();
	}
	
	@Override
	@Transactional(readOnly = true)//El reanOnly se usa para que la consulta sea solo de lectura
	public EstSerial findSerialByTipo(Long id) {
		return estSerialDao.findSerialByTipo(id);
	}
	
	@Override
	@Transactional(readOnly = true)//El reanOnly se usa para que la consulta sea solo de lectura
	public EstSerial findSerialBySerialAndTipo(String serial, String tipoCertificado) {
		return estSerialDao.findSerialBySerialAndTipo(serial, tipoCertificado);
	}	

	@Override
	@Transactional(readOnly = true)//El reanOnly se usa para que la consulta sea solo de lectura
	public EstSerial findById(Long id) {
		return estSerialDao.findById(id).orElse(null);//.orElse es que si lo encuentra retorna el objeto y no devuelve null
	}	
		
	
	@Override
	@Transactional
	public void save(EstSerial estSerial) {
		estSerialDao.save(estSerial);
	}


	@Override
	@Transactional
	public void delete(Long id) {
		estSerialDao.deleteById(id);	
	}
		

}
