package com.chapumix.solution.app.models.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.chapumix.solution.app.models.dao.IFarMipresDao;
import com.chapumix.solution.app.models.entity.FarMipres;

@Service
public class FarMipresServiceImpl implements IFarMipresService{
	
	@Autowired
	private IFarMipresDao farMipresDao;
	

	@Override
	@Transactional(readOnly = true)//El reanOnly se usa para que la consulta sea solo de lectura
	public List<FarMipres> findAll() {
		return (List<FarMipres>) farMipresDao.findAll();
	}
	
	@Override
	@Transactional(readOnly = true)//El reanOnly se usa para que la consulta sea solo de lectura
	public Page<FarMipres> findAll(Pageable pageable) {
		return farMipresDao.findAll(pageable);
	}
	
	@Override
	@Transactional(readOnly = true)//El reanOnly se usa para que la consulta sea solo de lectura
	public Page<FarMipres> findAllCustomPendientes(Pageable pageable) {
		return farMipresDao.findAllCustomPendientes(pageable);
	}
	
	@Override
	@Transactional(readOnly = true)//El reanOnly se usa para que la consulta sea solo de lectura
	public Page<FarMipres> findAllCustomSearchPendientes(Pageable pageable, String prescripcion) {
		return farMipresDao.findAllCustomSearchPendientes(pageable, prescripcion)		;
	}
	
	@Override
	@Transactional(readOnly = true)//El reanOnly se usa para que la consulta sea solo de lectura
	public Page<FarMipres> findAllCustomProcesados(Pageable pageable) {
		return farMipresDao.findAllCustomProcesados(pageable);
	}
	
	@Override
	@Transactional(readOnly = true)//El reanOnly se usa para que la consulta sea solo de lectura
	public Page<FarMipres> findAllCustomSearchProcesados(Pageable pageable, String prescripcion) {
		return farMipresDao.findAllCustomSearchProcesados(pageable, prescripcion)		;
	}
	
	@Override
	@Transactional(readOnly = true)//El reanOnly se usa para que la consulta sea solo de lectura
	public List<FarMipres> findByIdMipres(Long id) {
		return farMipresDao.findByIdMipres(id);
	}

	@Override
	@Transactional(readOnly = true)//El reanOnly se usa para que la consulta sea solo de lectura
	public FarMipres findById(Long id) {
		return farMipresDao.findById(id).orElse(null);//.orElse es que si lo encuentra retorna el objeto y no devuelve null
	}
	
	@Override
	@Transactional(readOnly = true)//El reanOnly se usa para que la consulta sea solo de lectura
	public FarMipres findByDocumentoPrescripcionTecnologiaCantidad(String numDocumento, String prescripcion, String cantidadEntregada) {
		return farMipresDao.findByDocumentoPrescripcionTecnologiaCantidad(numDocumento, prescripcion, cantidadEntregada);
	}
	
	@Override
	@Transactional(readOnly = true)//El reanOnly se usa para que la consulta sea solo de lectura
	public FarMipres findByDocumentoPrescripcionConsecutivoTecnologiaCantidad(String numDocumento, String prescripcion, String cantidadEntregada, Integer consecutivoTecnologia) {
		return farMipresDao.findByDocumentoPrescripcionConsecutivoTecnologiaCantidad(numDocumento, prescripcion, cantidadEntregada, consecutivoTecnologia);
	}	
	
	@Override
	@Transactional(readOnly = true)//El reanOnly se usa para que la consulta sea solo de lectura
	public List<FarMipres> findByPrescripcion(String prescripcion) {
		return farMipresDao.findByPrescripcion(prescripcion);
	}
	
	@Override
	@Transactional(readOnly = true)//El reanOnly se usa para que la consulta sea solo de lectura
	public List<FarMipres> findByStartDateBetween(Date fechaInicial, Date fechaFinal) {
		return farMipresDao.findByStartDateBetween(fechaInicial, fechaFinal);
	}	
	
	@Override
	@Transactional
	public void save(FarMipres farMipres) {
		farMipresDao.save(farMipres);
	}

	@Override
	@Transactional
	public void delete(Long id) {
		farMipresDao.deleteById(id);	
	}	

}
