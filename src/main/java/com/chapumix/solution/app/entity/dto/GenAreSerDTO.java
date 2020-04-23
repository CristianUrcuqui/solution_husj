package com.chapumix.solution.app.entity.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GenAreSerDTO implements Serializable{
	

	private static final long serialVersionUID = 1L;
	
	
	private Integer oid;
	private String gasCodigo;
	private String gasNombre;	
	
	

	public GenAreSerDTO() {		
	}

	public GenAreSerDTO(Integer oid, String gasCodigo, String gasNombre) {		
		this.oid = oid;
		this.gasCodigo = gasCodigo;
		this.gasNombre = gasNombre;
	}

	public Integer getOid() {
		return oid;
	}

	public void setOid(Integer oid) {
		this.oid = oid;
	}	

	public String getGasCodigo() {
		return gasCodigo;
	}

	public void setGasCodigo(String gasCodigo) {
		this.gasCodigo = gasCodigo;
	}

	public String getGasNombre() {
		return gasNombre;
	}

	public void setGasNombre(String gasNombre) {
		this.gasNombre = gasNombre;
	}

}