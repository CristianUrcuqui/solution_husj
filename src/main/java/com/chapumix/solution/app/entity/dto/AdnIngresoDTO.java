package com.chapumix.solution.app.entity.dto;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AdnIngresoDTO implements Serializable{

	private static final long serialVersionUID = 1L;

	private Integer oid;
	private Integer ainConsec;
	private Date ainFecIng;
	private Integer pacTipDoc;
	private String pacNumDoc;
	private String pacPriNom;
	private String pacSegNom;
	private String pacPriApe;
	private String pacSegApe;
	private Integer ainEstado;
	private String ainmotanu;
	private Date adfecanula;
	private Integer adusuanula;	
	
	public AdnIngresoDTO() {		
	}


	public AdnIngresoDTO(Integer oid, Integer ainConsec, Date ainFecIng, Integer pacTipDoc, String pacNumDoc,
			String pacPriNom, String pacSegNom, String pacPriApe, String pacSegApe, Integer ainEstado,
			String ainmotanu) {		
		this.oid = oid;
		this.ainConsec = ainConsec;
		this.ainFecIng = ainFecIng;
		this.pacTipDoc = pacTipDoc;
		this.pacNumDoc = pacNumDoc;
		this.pacPriNom = pacPriNom;
		this.pacSegNom = pacSegNom;
		this.pacPriApe = pacPriApe;
		this.pacSegApe = pacSegApe;
		this.ainEstado = ainEstado;
		this.ainmotanu = ainmotanu;
	}	


	public Integer getOid() {
		return oid;
	}
	

	public void setOid(Integer oid) {
		this.oid = oid;
	}

	public Integer getAinConsec() {
		return ainConsec;
	}

	public void setAinConsec(Integer ainConsec) {
		this.ainConsec = ainConsec;
	}

	public Date getAinFecIng() {
		return ainFecIng;
	}

	public void setAinFecIng(Date ainFecIng) {
		this.ainFecIng = ainFecIng;
	}

	public Integer getPacTipDoc() {
		return pacTipDoc;
	}

	public void setPacTipDoc(Integer pacTipDoc) {
		this.pacTipDoc = pacTipDoc;
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

	public Integer getAinEstado() {
		return ainEstado;
	}

	public void setAinEstado(Integer ainEstado) {
		this.ainEstado = ainEstado;
	}

	public String getAinmotanu() {
		return ainmotanu;
	}

	public void setAinmotanu(String ainmotanu) {
		this.ainmotanu = ainmotanu;
	}

	public Date getAdfecanula() {
		return adfecanula;
	}

	public void setAdfecanula(Date adfecanula) {
		this.adfecanula = adfecanula;
	}

	public Integer getAdusuanula() {
		return adusuanula;
	}

	public void setAdusuanula(Integer adusuanula) {
		this.adusuanula = adusuanula;
	}	

}