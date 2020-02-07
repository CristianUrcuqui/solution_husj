package com.chapumix.solution.app.entity.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GenPacienDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer oid;
	private String pacNumDoc;
	private String pacPriNom;
	private String pacSegNom;
	private String pacPriApe;
	private String pacSegApe;
	
	public GenPacienDTO() {		
	}

	public GenPacienDTO(Integer oid, String pacNumDoc, String pacPriNom, String pacSegNom, String pacPriApe,
			String pacSegApe) {		
		this.oid = oid;
		this.pacNumDoc = pacNumDoc;
		this.pacPriNom = pacPriNom;
		this.pacSegNom = pacSegNom;
		this.pacPriApe = pacPriApe;
		this.pacSegApe = pacSegApe;
	}

	public Integer getOid() {
		return oid;
	}

	public void setOid(Integer oid) {
		this.oid = oid;
	}	

	public String getPacNumDoc() {
		return pacNumDoc;
	}

	public void setPacNumDoc(String pacNumDoc) {
		this.pacNumDoc = pacNumDoc;
	}

	public String getPacPriNom() {
		return pacPriNom;
	}

	public void setPacPriNom(String pacPriNom) {
		this.pacPriNom = pacPriNom;
	}

	public String getPacSegNom() {
		return pacSegNom;
	}

	public void setPacSegNom(String pacSegNom) {
		this.pacSegNom = pacSegNom;
	}

	public String getPacPriApe() {
		return pacPriApe;
	}

	public void setPacPriApe(String pacPriApe) {
		this.pacPriApe = pacPriApe;
	}

	public String getPacSegApe() {
		return pacSegApe;
	}

	public void setPacSegApe(String pacSegApe) {
		this.pacSegApe = pacSegApe;
	}
		
}





