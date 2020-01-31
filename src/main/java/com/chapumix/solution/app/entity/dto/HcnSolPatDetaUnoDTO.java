package com.chapumix.solution.app.entity.dto;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class HcnSolPatDetaUnoDTO implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	private String pacPriNom;
	private String pacSegNom;
	private String pacPriApe;
	private String pacSegApe;
	private String gasNombre;
	private String hcaCodigo;
	private Date hcsfecsol;
	private String pacNumDoc;
	private Date gpafecnac;
	private Integer gpasexpac;
	private String gdeNombre;
	private Integer ainConsec;
	private Integer hcNumFol;
	private String diaCodigo;
	private String diaNombre;
	
	public HcnSolPatDetaUnoDTO() {		
	}

	public HcnSolPatDetaUnoDTO(String pacPriNom, String pacSegNom, String pacPriApe, String pacSegApe, String gasNombre,
			String hcaCodigo, Date hcsfecsol, String pacNumDoc, Date gpafecnac, Integer gpasexpac, String gdeNombre,
			Integer ainConsec, Integer hcNumFol, String diaCodigo, String diaNombre) {		
		this.pacPriNom = pacPriNom;
		this.pacSegNom = pacSegNom;
		this.pacPriApe = pacPriApe;
		this.pacSegApe = pacSegApe;
		this.gasNombre = gasNombre;
		this.hcaCodigo = hcaCodigo;
		this.hcsfecsol = hcsfecsol;
		this.pacNumDoc = pacNumDoc;
		this.gpafecnac = gpafecnac;
		this.gpasexpac = gpasexpac;
		this.gdeNombre = gdeNombre;
		this.ainConsec = ainConsec;
		this.hcNumFol = hcNumFol;
		this.diaCodigo = diaCodigo;
		this.diaNombre = diaNombre;
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

	public String getGasNombre() {
		return gasNombre;
	}

	public void setGasNombre(String gasNombre) {
		this.gasNombre = gasNombre;
	}

	public String getHcaCodigo() {
		return hcaCodigo;
	}

	public void setHcaCodigo(String hcaCodigo) {
		this.hcaCodigo = hcaCodigo;
	}

	@Temporal(TemporalType.TIMESTAMP)
	public Date getHcsfecsol() {
		return hcsfecsol;
	}

	public void setHcsfecsol(Date hcsfecsol) {
		this.hcsfecsol = hcsfecsol;
	}

	public String getPacNumDoc() {
		return pacNumDoc;
	}

	public void setPacNumDoc(String pacNumDoc) {
		this.pacNumDoc = pacNumDoc;
	}

	@Temporal(TemporalType.DATE)
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

	public String getGdeNombre() {
		return gdeNombre;
	}

	public void setGdeNombre(String gdeNombre) {
		this.gdeNombre = gdeNombre;
	}

	public Integer getAinConsec() {
		return ainConsec;
	}

	public void setAinConsec(Integer ainConsec) {
		this.ainConsec = ainConsec;
	}

	public Integer getHcNumFol() {
		return hcNumFol;
	}

	public void setHcNumFol(Integer hcNumFol) {
		this.hcNumFol = hcNumFol;
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

}
