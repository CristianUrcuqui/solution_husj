package com.chapumix.solution.app.models.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.chapumix.solution.app.models.dao.IComTipoDocumentoMipresDao;
import com.chapumix.solution.app.models.entity.ComTipoDocumentoMipres;

@Service
public class ComTipoDocumentoMipresServiceImpl implements IComTipoDocumentoMipresService{
	
	@Autowired
	private IComTipoDocumentoMipresDao comTipoDocumentoMipresDao;
	

	@Override
	@Transactional(readOnly = true)//El reanOnly se usa para que la consulta sea solo de lectura
	public List<ComTipoDocumentoMipres> findAll() {
		return (List<ComTipoDocumentoMipres>) comTipoDocumentoMipresDao.findAll();
	}

	@Override
	@Transactional(readOnly = true)//El reanOnly se usa para que la consulta sea solo de lectura
	public ComTipoDocumentoMipres findById(Long id) {
		return comTipoDocumentoMipresDao.findById(id).orElse(null);//.orElse es que si lo encuentra retorna el objeto y no devuelve null
	}
	
	@Override
	@Transactional(readOnly = true)//El reanOnly se usa para que la consulta sea solo de lectura
	public ComTipoDocumentoMipres tipoDocumento(String tipo) {
		return comTipoDocumentoMipresDao.tipoDocumento(tipo);
	}	
		
	
	@Override
	@Transactional
	public void save(ComTipoDocumentoMipres comTipoDocumentoMipres) {
		comTipoDocumentoMipresDao.save(comTipoDocumentoMipres);
	}


	@Override
	@Transactional
	public void delete(Long id) {
		comTipoDocumentoMipresDao.deleteById(id);	
	}

}
