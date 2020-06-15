package com.chapumix.solution.app.entity.dto;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GenPacienMortalidadDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer oid;	
	private Integer pacTipDoc;
	private String pacNumDoc;	
	private String pacPriNom;
	private String pacSegNom;
	private String pacPriApe;
	private String pacSegApe;
	private Date gpafecnac;
	private Integer gpasexpac;
	private Integer ainConsec;
	private String munNomMun;
	private Integer gdeConFac;
	private Integer ainUrgCon;
	private String diaCodigo;
	private String diaNombre;
	private String entNombre;
	private Date ainFecIng;
	private Date hcefecdef;	
	
	
	public GenPacienMortalidadDTO() {		
	}


	public GenPacienMortalidadDTO(Integer oid, Integer pacTipDoc, String pacNumDoc, String pacPriNom, String pacSegNom,
			String pacPriApe, String pacSegApe, Date gpafecnac, Integer gpasexpac, Integer ainConsec, String munNomMun,
			Integer gdeConFac, Integer ainUrgCon, String diaCodigo, String diaNombre, String entNombre, Date ainFecIng,
			Date hcefecdef) {		
		this.oid = oid;
		this.pacTipDoc = pacTipDoc;
		this.pacNumDoc = pacNumDoc;
		this.pacPriNom = pacPriNom;
		this.pacSegNom = pacSegNom;
		this.pacPriApe = pacPriApe;
		this.pacSegApe = pacSegApe;
		this.gpafecnac = gpafecnac;
		this.gpasexpac = gpasexpac;
		this.ainConsec = ainConsec;
		this.munNomMun = munNomMun;
		this.gdeConFac = gdeConFac;
		this.ainUrgCon = ainUrgCon;
		this.diaCodigo = diaCodigo;
		this.diaNombre = diaNombre;
		this.entNombre = entNombre;
		this.ainFecIng = ainFecIng;
		this.hcefecdef = hcefecdef;
	}
	

	public Integer getOid() {
		return oid;
	}

	public void setOid(Integer oid) {
		this.oid = oid;
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
	
	public Date getGpafecnac() {
		return gpafecnac;
	}

	public void setGpafecnac(Date gpafecnac) {
		this.gpafecnac = gpafecnac;
	}

	public Integer getGpasexpac() {
		return gpasexpac;
	}

	public void setGpasexpac(Integer gpasexpac) {
		this.gpasexpac = gpasexpac;
	}

	public Integer getAinConsec() {
		return ainConsec;
	}

	public void setAinConsec(Integer ainConsec) {
		this.ainConsec = ainConsec;
	}

	public String getMunNomMun() {
		return munNomMun;
	}

	public void setMunNomMun(String munNomMun) {
		this.munNomMun = munNomMun;
	}

	public Integer getGdeConFac() {
		return gdeConFac;
	}

	public void setGdeConFac(Integer gdeConFac) {
		this.gdeConFac = gdeConFac;
	}

	public Integer getAinUrgCon() {
		return ainUrgCon;
	}

	public void setAinUrgCon(Integer ainUrgCon) {
		this.ainUrgCon = ainUrgCon;
	}

	public String getDiaCodigo() {
		return diaCodigo;
	}

	public void setDiaCodigo(String diaCodigo) {
		this.diaCodigo = diaCodigo;
	}

	public String getDiaNombre() {
		return diaNombre;
	}

	public void setDiaNombre(String diaNombre) {
		this.diaNombre = diaNombre;
	}

	public String getEntNombre() {
		return entNombre;
	}

	public void setEntNombre(String entNombre) {
		this.entNombre = entNombre;
	}

	public Date getAinFecIng() {
		return ainFecIng;
	}

	public void setAinFecIng(Date ainFecIng) {
		this.ainFecIng = ainFecIng;
	}

	public Date getHcefecdef() {
		return hcefecdef;
	}

	public void setHcefecdef(Date hcefecdef) {
		this.hcefecdef = hcefecdef;
	}	
		
}