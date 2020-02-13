package com.chapumix.solution.app.models.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.chapumix.solution.app.models.dao.IPatProcedimientoDao;
import com.chapumix.solution.app.models.entity.PatProcedimiento;

@Service
public class PatProcedimientoServiceImpl implements IPatProcedimientoService{
	
	@Autowired
	private IPatProcedimientoDao iPatProcedimientoDao;

	@Override
	@Transactional(readOnly = true)//El reanOnly se usa para que la consulta sea solo de lectura
	public List<PatProcedimiento> findAll() {
		return (List<PatProcedimiento>) iPatProcedimientoDao.findAll();
	}
	
	@Override
	@Transactional(readOnly = true)//El reanOnly se usa para que la consulta sea solo de lectura
	public PatProcedimiento findById(Long id) {
		return iPatProcedimientoDao.findById(id).orElse(null);
	}
	

	@Override
	@Transactional
	public void save(PatProcedimiento patProcedimiento) {
		iPatProcedimientoDao.save(patProcedimiento);		
	}

	

	@Override
	@Transactional
	public void delete(Long id) {
		iPatProcedimientoDao.deleteById(id);
		
	}

}
