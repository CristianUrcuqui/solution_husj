package com.chapumix.solution.app.entity.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class HcnSolPatDetaDosDTO implements Serializable{

	private static final long serialVersionUID = 1L;

	
	private String sipCodigo;
	private String sipNombre;
	private String sipCodCup;
	private String hcsobserv;
	private Integer hcscanti;
	private Integer hcsestado;
	
	public HcnSolPatDetaDosDTO() {
	}	


	public HcnSolPatDetaDosDTO(String sipCodigo, String sipNombre, String sipCodCup, String hcsobserv, Integer hcscanti,
			Integer hcsestado) {		
		this.sipCodigo = sipCodigo;
		this.sipNombre = sipNombre;
		this.sipCodCup = sipCodCup;
		this.hcsobserv = hcsobserv;
		this.hcscanti = hcscanti;
		this.hcsestado = hcsestado;
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


	public String getHcsobserv() {
		return hcsobserv;
	}

	public void setHcsobserv(String hcsobserv) {
		this.hcsobserv = hcsobserv;
	}

	public Integer getHcscanti() {
		return hcscanti;
	}

	public void setHcscanti(Integer hcscanti) {
		this.hcscanti = hcscanti;
	}

	public Integer getHcsestado() {
		return hcsestado;
	}

	public void setHcsestado(Integer hcsestado) {
		this.hcsestado = hcsestado;
	}

}
