package com.chapumix.solution.app.models.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.chapumix.solution.app.models.dao.IComTipoDocumentoDao;
import com.chapumix.solution.app.models.entity.ComTipoDocumento;

@Service
public class ComTipoDocumentoServiceImpl implements IComTipoDocumentoService{
	
	@Autowired
	private IComTipoDocumentoDao comTipoDocumentoDao;
	

	@Override
	@Transactional(readOnly = true)//El reanOnly se usa para que la consulta sea solo de lectura
	public List<ComTipoDocumento> findAll() {
		return (List<ComTipoDocumento>) comTipoDocumentoDao.findAll();
	}

	@Override
	@Transactional(readOnly = true)//El reanOnly se usa para que la consulta sea solo de lectura
	public ComTipoDocumento findById(Long id) {
		return comTipoDocumentoDao.findById(id).orElse(null);//.orElse es que si lo encuentra retorna el objeto y no devuelve null
	}	
		
	
	@Override
	@Transactional
	public void save(ComTipoDocumento comTipoDocumento) {
		comTipoDocumentoDao.save(comTipoDocumento);
	}


	@Override
	@Transactional
	public void delete(Long id) {
		comTipoDocumentoDao.deleteById(id);	
	}	

}
