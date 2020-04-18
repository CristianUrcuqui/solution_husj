package com.chapumix.solution.app.entity.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CtnCenCosDTO implements Serializable {	

	private static final long serialVersionUID = 1L;

	private Integer oid;
	private String ccCodigo;
	private String ccNombre;
	private Boolean ccActivo;
	
	

	public CtnCenCosDTO() {		
	}

	public CtnCenCosDTO(Integer oid, String ccCodigo, String ccNombre, Boolean ccActivo) {		
		this.oid = oid;
		this.ccCodigo = ccCodigo;
		this.ccNombre = ccNombre;
		this.ccActivo = ccActivo;
	}

	public Integer getOid() {
		return oid;
	}

	public void setOid(Integer oid) {
		this.oid = oid;
	}
	
	public String getCcCodigo() {
		return ccCodigo;
	}

	public void setCcCodigo(String ccCodigo) {
		this.ccCodigo = ccCodigo;
	}
	
	public String getCcNombre() {
		return ccNombre;
	}

	public void setCcNombre(String ccNombre) {
		this.ccNombre = ccNombre;
	}

	public Boolean getCcActivo() {
		return ccActivo;
	}

	public void setCcActivo(Boolean ccActivo) {
		this.ccActivo = ccActivo;
	}	
	
}