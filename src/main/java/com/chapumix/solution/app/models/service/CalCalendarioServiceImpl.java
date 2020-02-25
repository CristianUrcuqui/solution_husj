package com.chapumix.solution.app.models.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.chapumix.solution.app.models.dao.ICalCalendarioDao;
import com.chapumix.solution.app.models.entity.CalCalendario;

@Service
public class CalCalendarioServiceImpl implements ICalCalendarioService{
	
	@Autowired
	private ICalCalendarioDao calCalendarioDao;
	

	@Override
	@Transactional(readOnly = true)//El reanOnly se usa para que la consulta sea solo de lectura
	public List<CalCalendario> findAll() {
		return (List<CalCalendario>) calCalendarioDao.findAll();
	}
	
	@Override
	public List<CalCalendario> findUserByDate() {
		return calCalendarioDao.findUserByDate();
	}	

	@Override
	@Transactional(readOnly = true)//El reanOnly se usa para que la consulta sea solo de lectura
	public CalCalendario findById(Long id) {
		return calCalendarioDao.findById(id).orElse(null);//.orElse es que si lo encuentra retorna el objeto y no devuelve null
	}	
	
	@Override
	@Transactional(readOnly = true)//El reanOnly se usa para que la consulta sea solo de lectura
	public CalCalendario findByNumeroIdentificacion(String identificacion) {
		return calCalendarioDao.findByNumeroIdentificacion(identificacion);
	}	
	
	@Override
	@Transactional
	public void save(CalCalendario calCalendario) {
		calCalendarioDao.save(calCalendario);
	}


	@Override
	@Transactional
	public void delete(Long id) {
		calCalendarioDao.deleteById(id);	
	}

}
