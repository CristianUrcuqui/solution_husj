package com.chapumix.solution.app.entity.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GenMedicoDTO implements Serializable{

	private static final long serialVersionUID = 1L;

	
	private Integer oid;
	private String gmeCodigo;
	private String gmeNomCod;
	
	public GenMedicoDTO() {
	}

	public GenMedicoDTO(Integer oid, String gmeCodigo, String gmeNomCod) {
		this.oid = oid;
		this.gmeCodigo = gmeCodigo;
		this.gmeNomCod = gmeNomCod;
	}

	public Integer getOid() {
		return oid;
	}

	public void setOid(Integer oid) {
		this.oid = oid;
	}

	public String getGmeCodigo() {
		return gmeCodigo;
	}

	public void setGmeCodigo(String gmeCodigo) {
		this.gmeCodigo = gmeCodigo;
	}

	public String getGmeNomCod() {
		return gmeNomCod;
	}

	public void setGmeNomCod(String gmeNomCod) {
		this.gmeNomCod = gmeNomCod;
	}

}
