package com.chapumix.solution.app.entity.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ComCamaDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private String ccCodigo;
	private String ccNombre;
	
	public ComCamaDTO() {		
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

	

}
