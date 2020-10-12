package com.chapumix.solution.app.entity.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SlnSerProDTO implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private Integer oid;
	private Integer ainConsec;
	private String gpaNomCom;
	private Integer slnOrdser1;
	private String serDesSer;	

	public SlnSerProDTO() {		
	}

	public SlnSerProDTO(Integer oid, Integer ainConsec, String gpaNomCom, Integer slnOrdser1, String serDesSer) {		
		this.oid = oid;
		this.ainConsec = ainConsec;
		this.gpaNomCom = gpaNomCom;
		this.slnOrdser1 = slnOrdser1;
		this.serDesSer = serDesSer;
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

	public String getGpaNomCom() {
		return gpaNomCom;
	}

	public void setGpaNomCom(String gpaNomCom) {
		this.gpaNomCom = gpaNomCom;
	}

	public Integer getSlnOrdser1() {
		return slnOrdser1;
	}

	public void setSlnOrdser1(Integer slnOrdser1) {
		this.slnOrdser1 = slnOrdser1;
	}

	public String getSerDesSer() {
		return serDesSer;
	}

	public void setSerDesSer(String serDesSer) {
		this.serDesSer = serDesSer;
	}

}