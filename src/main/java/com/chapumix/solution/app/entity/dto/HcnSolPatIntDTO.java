package com.chapumix.solution.app.entity.dto;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


@JsonIgnoreProperties(ignoreUnknown = true)
public class HcnSolPatIntDTO implements Serializable{

	private static final long serialVersionUID = 1L;

	private Integer oidPaciente;
	private String pacNumDoc;
	private String pacPriNom;
	private String pacSegNom;
	private String pacPriApe;
	private String pacSegApe;
	private Integer ainConsec;
	private Integer hcNumFol;
	private Integer oidRips;
	private String sipCodigo;
	private String sipNombre;
	private Date hcsfecsol;
	private String gasNombre;
		

	public Integer getOidPaciente() {
		return oidPaciente;
	}	

	public void setOidPaciente(Integer oidPaciente) {
		this.oidPaciente = oidPaciente;
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

	public Integer getOidRips() {
		return oidRips;
	}

	public void setOidRips(Integer oidRips) {
		this.oidRips = oidRips;
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

	@Temporal(TemporalType.TIMESTAMP)
	public Date getHcsfecsol() {
		return hcsfecsol;
	}

	public void setHcsfecsol(Date hcsfecsol) {
		this.hcsfecsol = hcsfecsol;
	}

	public String getGasNombre() {
		return gasNombre;
	}

	public void setGasNombre(String gasNombre) {
		this.gasNombre = gasNombre;
	}

}
