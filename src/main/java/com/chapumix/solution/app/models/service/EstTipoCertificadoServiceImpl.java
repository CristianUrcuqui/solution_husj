package com.chapumix.solution.app.models.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.chapumix.solution.app.models.dao.IEstTipoCertificadoDao;
import com.chapumix.solution.app.models.entity.EstTipoCertificado;

@Service
public class EstTipoCertificadoServiceImpl implements IEstTipoCertificadoService{
	
	@Autowired	
	private IEstTipoCertificadoDao estTipoCertificadoDao;
	

	@Override
	@Transactional(readOnly = true)//El reanOnly se usa para que la consulta sea solo de lectura
	public List<EstTipoCertificado> findAll() {
		return (List<EstTipoCertificado>) estTipoCertificadoDao.findAll();
	}

	@Override
	@Transactional(readOnly = true)//El reanOnly se usa para que la consulta sea solo de lectura
	public EstTipoCertificado findById(Long id) {
		return estTipoCertificadoDao.findById(id).orElse(null);//.orElse es que si lo encuentra retorna el objeto y no devuelve null
	}	
		
	
	@Override
	@Transactional
	public void save(EstTipoCertificado estTipoCertificado) {
		estTipoCertificadoDao.save(estTipoCertificado);
	}


	@Override
	@Transactional
	public void delete(Long id) {
		estTipoCertificadoDao.deleteById(id);	
	}	

}
