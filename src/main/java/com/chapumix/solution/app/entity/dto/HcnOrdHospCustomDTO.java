package com.chapumix.solution.app.entity.dto;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class HcnOrdHospCustomDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer ainConsec;
	private Integer oidPaciente;
	private String pacNumDoc;
	private String pacPriNom;
	private String pacSegNom;
	private String pacPriApe;
	private String pacSegApe;
	private Integer gpasexpac;
	private Integer gpafecnac;
	private String hcaCodigo;
	private String hcaNombre;
	private Integer hcoTipAisl;
	private Integer hcoTipHosp;
	private String diaCodigo;
	private String diaNombre;
	private Date hcofecdoc;
	private String ccCodigo;
	private String ccNombre;
	private String hcoObserv;
	private String hcoMotivo;
	private String gdeNombre;
	private String gmeNomCom;
	private String gasNombre;	
	

	public HcnOrdHospCustomDTO() {
		
	}	
	
	
	public HcnOrdHospCustomDTO(Integer ainConsec, String pacNumDoc, String pacPriNom, String pacSegNom,
			String pacPriApe, String pacSegApe, Integer gpasexpac, Integer gpafecnac, String gdeNombre, String hcaCodigo, 
			String hcaNombre, Integer hcoTipAisl, Integer hcoTipHosp, String diaCodigo,	String diaNombre, Date hcofecdoc,
			String ccCodigo, String ccNombre, String hcoObserv, String hcoMotivo, String gmeNomCom, String gasNombre) {		
		this.ainConsec = ainConsec;
		this.pacNumDoc = pacNumDoc;
		this.pacPriNom = pacPriNom;
		this.pacSegNom = pacSegNom;
		this.pacPriApe = pacPriApe;
		this.pacSegApe = pacSegApe;
		this.gpasexpac = gpasexpac;
		this.gpafecnac = gpafecnac;
		this.gdeNombre = gdeNombre;
		this.hcaCodigo = hcaCodigo;
		this.hcaNombre = hcaNombre;
		this.hcoTipAisl = hcoTipAisl;
		this.hcoTipHosp = hcoTipHosp;
		this.diaCodigo = diaCodigo;
		this.diaNombre = diaNombre;
		this.hcofecdoc = hcofecdoc;
		this.ccCodigo = ccCodigo;
		this.ccNombre = ccNombre;
		this.hcoObserv = hcoObserv;
		this.hcoMotivo = hcoMotivo;
		this.gmeNomCom = gmeNomCom;
		this.gasNombre = gasNombre;
	}


	public HcnOrdHospCustomDTO(Integer ainConsec, Integer oidPaciente, String pacNumDoc, String pacPriNom,
			String pacSegNom, String pacPriApe, String pacSegApe, Integer gpasexpac, Integer gpafecnac,
			String hcaCodigo, String hcaNombre, Integer hcoTipAisl, Integer hcoTipHosp, String diaCodigo,
			String diaNombre, Date hcofecdoc, String ccCodigo, String ccNombre, String hcoObserv, String hcoMotivo,
			String gdeNombre, String gmeNomCom) {		
		this.ainConsec = ainConsec;
		this.oidPaciente = oidPaciente;
		this.pacNumDoc = pacNumDoc;
		this.pacPriNom = pacPriNom;
		this.pacSegNom = pacSegNom;
		this.pacPriApe = pacPriApe;
		this.pacSegApe = pacSegApe;
		this.gpasexpac = gpasexpac;
		this.gpafecnac = gpafecnac;
		this.hcaCodigo = hcaCodigo;
		this.hcaNombre = hcaNombre;
		this.hcoTipAisl = hcoTipAisl;
		this.hcoTipHosp = hcoTipHosp;
		this.diaCodigo = diaCodigo;
		this.diaNombre = diaNombre;
		this.hcofecdoc = hcofecdoc;
		this.ccCodigo = ccCodigo;
		this.ccNombre = ccNombre;
		this.hcoObserv = hcoObserv;
		this.hcoMotivo = hcoMotivo;
		this.gdeNombre = gdeNombre;
		this.gmeNomCom = gmeNomCom;
	}

	public Integer getAinConsec() {
		return ainConsec;
	}

	public void setAinConsec(Integer ainConsec) {
		this.ainConsec = ainConsec;
	}

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

	public Integer getGpasexpac() {
		return gpasexpac;
	}

	public void setGpasexpac(Integer gpasexpac) {
		this.gpasexpac = gpasexpac;
	}
	
	public Integer getGpafecnac() {
		return gpafecnac;
	}

	public void setGpafecnac(Integer gpafecnac) {
		this.gpafecnac = gpafecnac;
	}

	public String getHcaCodigo() {
		return hcaCodigo;
	}

	public void setHcaCodigo(String hcaCodigo) {
		this.hcaCodigo = hcaCodigo;
	}

	public String getHcaNombre() {
		return hcaNombre;
	}

	public void setHcaNombre(String hcaNombre) {
		this.hcaNombre = hcaNombre;
	}

	public Integer getHcoTipAisl() {
		return hcoTipAisl;
	}

	public void setHcoTipAisl(Integer hcoTipAisl) {
		this.hcoTipAisl = hcoTipAisl;
	}

	public Integer getHcoTipHosp() {
		return hcoTipHosp;
	}

	public void setHcoTipHosp(Integer hcoTipHosp) {
		this.hcoTipHosp = hcoTipHosp;
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

	@Temporal(TemporalType.TIMESTAMP)
	public Date getHcofecdoc() {
		return hcofecdoc;
	}

	public void setHcofecdoc(Date hcofecdoc) {
		this.hcofecdoc = hcofecdoc;
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

	public String getHcoObserv() {
		return hcoObserv;
	}

	public void setHcoObserv(String hcoObserv) {
		this.hcoObserv = hcoObserv;
	}

	public String getHcoMotivo() {
		return hcoMotivo;
	}

	public void setHcoMotivo(String hcoMotivo) {
		this.hcoMotivo = hcoMotivo;
	}

	public String getGdeNombre() {
		return gdeNombre;
	}

	public void setGdeNombre(String gdeNombre) {
		this.gdeNombre = gdeNombre;
	}

	public String getGmeNomCom() {
		return gmeNomCom;
	}

	public void setGmeNomCom(String gmeNomCom) {
		this.gmeNomCom = gmeNomCom;
	}


	public String getGasNombre() {
		return gasNombre;
	}


	public void setGasNombre(String gasNombre) {
		this.gasNombre = gasNombre;
	}
	
}
