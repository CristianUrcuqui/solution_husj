package com.chapumix.solution.app.models.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.chapumix.solution.app.models.dao.IComTipoIngresoDao;
import com.chapumix.solution.app.models.entity.ComTipoIngreso;

@Service
public class ComTipoIngresoServiceImpl implements IComTipoIngresoService{
	
	
	@Autowired
	private IComTipoIngresoDao comTipoIngresoDao;
	

	@Override
	@Transactional(readOnly = true)//El reanOnly se usa para que la consulta sea solo de lectura
	public List<ComTipoIngreso> findAll() {
		return (List<ComTipoIngreso>) comTipoIngresoDao.findAll();
	}
	
	@Override
	@Transactional(readOnly = true)//El reanOnly se usa para que la consulta sea solo de lectura
	public ComTipoIngreso findByCodigo(Integer codigo) {
		return comTipoIngresoDao.findByCodigo(codigo);
	}
	
	@Override
	@Transactional(readOnly = true)//El reanOnly se usa para que la consulta sea solo de lectura
	public ComTipoIngreso tipoIngresoNombre(String nombre) {
		return comTipoIngresoDao.tipoIngresoNombre(nombre);
	}

	@Override
	@Transactional(readOnly = true)//El reanOnly se usa para que la consulta sea solo de lectura
	public ComTipoIngreso findById(Long id) {
		return comTipoIngresoDao.findById(id).orElse(null);//.orElse es que si lo encuentra retorna el objeto y no devuelve null
	}	
		
	
	@Override
	@Transactional
	public void save(ComTipoIngreso comRegimen) {
		comTipoIngresoDao.save(comRegimen);
	}


	@Override
	@Transactional
	public void delete(Long id) {
		comTipoIngresoDao.deleteById(id);	
	}		

}
