package com.chapumix.solution.app.entity.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GenSeRipsDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer oid;
	private String sipCodigo;
	private String sipNombre;
	private String sipCodCup;
	
		
	public GenSeRipsDTO() {		
	}

	public GenSeRipsDTO(Integer oid, String sipCodigo, String sipNombre, String sipCodCup) {
		this.oid = oid;
		this.sipCodigo = sipCodigo;
		this.sipNombre = sipNombre;
		this.sipCodCup = sipCodCup;
	}

	public Integer getOid() {
		return oid;
	}

	public void setOid(Integer oid) {
		this.oid = oid;
	}	

	public String getSipCodigo() {
		return sipCodigo;
	}

	public void setSipCodigo(String sipCodigo) {
		this.sipCodigo = sipCodigo;
	}

	public String getSipNombre() {
		return sipNombre;
	}

	public void setSipNombre(String sipNombre) {
		this.sipNombre = sipNombre;
	}
	
	public String getSipCodCup() {
		return sipCodCup;
	}

	public void setSipCodCup(String sipCodCup) {
		this.sipCodCup = sipCodCup;
	}		
}





