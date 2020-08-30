package com.chapumix.solution.app.models.service;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.chapumix.solution.app.models.entity.FarMipres;

public interface IFarMipresService {
	
	public List<FarMipres> findAll();

	public Page<FarMipres> findAll(Pageable pageable);
	
	public Page<FarMipres> findAllCustomPendientes(Pageable pageable);
	
	public Page<FarMipres> findAllCustomSearchPendientes(Pageable pageable, String prescripcion);
	
	public Page<FarMipres> findAllCustomProcesados(Pageable pageable);
	
	public Page<FarMipres> findAllCustomSearchProcesados(Pageable pageable, String prescripcion);	
	
	public void save(FarMipres farMipres);
	
	public FarMipres findById(Long id);
	
	public FarMipres findByDocumentoPrescripcionTecnologiaCantidad(String numDocumento, String prescripcion, String cantidadEntregada);
	
	public FarMipres findByDocumentoPrescripcionConsecutivoTecnologiaCantidad(String numDocumento, String prescripcion, String cantidadEntregada, Integer consecutivoTecnologia);
	
	public List<FarMipres> findByPrescripcion(String prescripcion);
	
	public List<FarMipres> findByIdMipres(Long id);
	
	public List<FarMipres> findByStartDateBetween(Date fechaInicial, Date fechaFinal);
	
	public void delete(Long id);

}
