package com.chapumix.solution.app.entity.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AtenEncuConsolidadoDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer id;
	private String servicio;
	private String numeroEncuestas;
	private String totalSi;
	private String totalNo;
	private String totalMuyMala;
	private String totalMala;
	private String totalRegular;
	private String totalBuena;
	private String totalMuyBuena;
	private String totalNoAplica;
	private String totalDefinitivamenteNo;
	private String totalProbablementeNo;
	private String totalProbablementeSi;
	private String totalDefinitivamenteSi;

	public AtenEncuConsolidadoDTO() {
	}	


	public AtenEncuConsolidadoDTO(String servicio, String numeroEncuestas, String totalSi, String totalNo, String totalMuyMala, String totalMala, String totalRegular, String totalBuena, String totalMuyBuena, String totalNoAplica, String totalDefinitivamenteNo, String totalProbablementeNo, String totalProbablementeSi, String totalDefinitivamenteSi) {		
		this.servicio = servicio;
		this.numeroEncuestas = numeroEncuestas;
		this.totalSi = totalSi;
		this.totalNo = totalNo;
		this.totalMuyMala = totalMuyMala;
		this.totalMala = totalMala;
		this.totalRegular = totalRegular;
		this.totalBuena = totalBuena;
		this.totalMuyBuena = totalMuyBuena;
		this.totalNoAplica = totalNoAplica;
		this.totalDefinitivamenteNo = totalDefinitivamenteNo;
		this.totalProbablementeNo = totalProbablementeNo;
		this.totalProbablementeSi = totalProbablementeSi;
		this.totalDefinitivamenteSi = totalDefinitivamenteSi;
	}


	public AtenEncuConsolidadoDTO(Integer id, String servicio, String numeroEncuestas, String totalSi, String totalNo,
			String totalMuyMala, String totalMala, String totalRegular, String totalBuena, String totalMuyBuena,
			String totalNoAplica, String totalDefinitivamenteNo, String totalProbablementeNo,
			String totalProbablementeSi, String totalDefinitivamenteSi) {		
		this.id = id;
		this.servicio = servicio;
		this.numeroEncuestas = numeroEncuestas;
		this.totalSi = totalSi;
		this.totalNo = totalNo;
		this.totalMuyMala = totalMuyMala;
		this.totalMala = totalMala;
		this.totalRegular = totalRegular;
		this.totalBuena = totalBuena;
		this.totalMuyBuena = totalMuyBuena;
		this.totalNoAplica = totalNoAplica;
		this.totalDefinitivamenteNo = totalDefinitivamenteNo;
		this.totalProbablementeNo = totalProbablementeNo;
		this.totalProbablementeSi = totalProbablementeSi;
		this.totalDefinitivamenteSi = totalDefinitivamenteSi;
	}



	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getServicio() {
		return servicio;
	}

	public void setServicio(String servicio) {
		this.servicio = servicio;
	}

	public String getNumeroEncuestas() {
		return numeroEncuestas;
	}

	public void setNumeroEncuestas(String numeroEncuestas) {
		this.numeroEncuestas = numeroEncuestas;
	}

	public String getTotalSi() {
		return totalSi;
	}

	public void setTotalSi(String totalSi) {
		this.totalSi = totalSi;
	}

	public String getTotalNo() {
		return totalNo;
	}

	public void setTotalNo(String totalNo) {
		this.totalNo = totalNo;
	}

	public String getTotalMuyMala() {
		return totalMuyMala;
	}

	public void setTotalMuyMala(String totalMuyMala) {
		this.totalMuyMala = totalMuyMala;
	}

	public String getTotalMala() {
		return totalMala;
	}

	public void setTotalMala(String totalMala) {
		this.totalMala = totalMala;
	}

	public String getTotalRegular() {
		return totalRegular;
	}

	public void setTotalRegular(String totalRegular) {
		this.totalRegular = totalRegular;
	}

	public String getTotalBuena() {
		return totalBuena;
	}

	public void setTotalBuena(String totalBuena) {
		this.totalBuena = totalBuena;
	}

	public String getTotalMuyBuena() {
		return totalMuyBuena;
	}

	public void setTotalMuyBuena(String totalMuyBuena) {
		this.totalMuyBuena = totalMuyBuena;
	}

	public String getTotalNoAplica() {
		return totalNoAplica;
	}

	public void setTotalNoAplica(String totalNoAplica) {
		this.totalNoAplica = totalNoAplica;
	}

	public String getTotalDefinitivamenteNo() {
		return totalDefinitivamenteNo;
	}

	public void setTotalDefinitivamenteNo(String totalDefinitivamenteNo) {
		this.totalDefinitivamenteNo = totalDefinitivamenteNo;
	}

	public String getTotalProbablementeNo() {
		return totalProbablementeNo;
	}

	public void setTotalProbablementeNo(String totalProbablementeNo) {
		this.totalProbablementeNo = totalProbablementeNo;
	}

	public String getTotalProbablementeSi() {
		return totalProbablementeSi;
	}

	public void setTotalProbablementeSi(String totalProbablementeSi) {
		this.totalProbablementeSi = totalProbablementeSi;
	}

	public String getTotalDefinitivamenteSi() {
		return totalDefinitivamenteSi;
	}

	public void setTotalDefinitivamenteSi(String totalDefinitivamenteSi) {
		this.totalDefinitivamenteSi = totalDefinitivamenteSi;
	}
}
