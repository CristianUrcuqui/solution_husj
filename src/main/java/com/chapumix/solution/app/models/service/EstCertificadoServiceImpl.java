package com.chapumix.solution.app.models.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.chapumix.solution.app.models.dao.IEstCertificadoDao;
import com.chapumix.solution.app.models.entity.EstCertificado;
import com.chapumix.solution.app.models.entity.FarMipres;

@Service
public class EstCertificadoServiceImpl implements IEstCertificadoService{
	
	@Autowired	
	private IEstCertificadoDao estCertificadoDao;
	

	@Override
	@Transactional(readOnly = true)//El reanOnly se usa para que la consulta sea solo de lectura
	public List<EstCertificado> findAll() {
		return (List<EstCertificado>) estCertificadoDao.findAll();
	}
	
	
	@Override
	@Transactional(readOnly = true)//El reanOnly se usa para que la consulta sea solo de lectura
	public Page<EstCertificado> findAllByFechaRegistroDesc(Pageable pageable) {
		return estCertificadoDao.findAllByFechaRegistroDesc(pageable);
	}
	
	@Override
	@Transactional(readOnly = true)//El reanOnly se usa para que la consulta sea solo de lectura
	public Page<EstCertificado> findAllCustomSearchRealizados(Pageable pageable, String busqueda) {
		return estCertificadoDao.findAllCustomSearchRealizados(pageable, busqueda);
	}	
	
	
	@Override
	@Transactional(readOnly = true)//El reanOnly se usa para que la consulta sea solo de lectura
	public List<EstCertificado> findAllByFechaRegistroAscLimit() {
		return estCertificadoDao.findAllByFechaRegistroAscLimit();
	}

	
	@Override
	@Transactional(readOnly = true)//El reanOnly se usa para que la consulta sea solo de lectura
	public EstCertificado findByNameTipo(String docPaciente, String tipoCertificado) {		
		return (EstCertificado) estCertificadoDao.findByNameTipo(docPaciente, tipoCertificado);
	}

	@Override
	@Transactional(readOnly = true)//El reanOnly se usa para que la consulta sea solo de lectura
	public EstCertificado findById(Long id) {
		return estCertificadoDao.findById(id).orElse(null);//.orElse es que si lo encuentra retorna el objeto y no devuelve null
	}	
		
	
	@Override
	@Transactional
	public void save(EstCertificado estCertificado) {
		estCertificadoDao.save(estCertificado);
	}


	@Override
	@Transactional
	public void delete(Long id) {
		estCertificadoDao.deleteById(id);	
	}
	
}
