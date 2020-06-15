package com.chapumix.solution.app.entity.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AtenEncuConsolidadoDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private String servicio;
	private String numeroEncuestas;
	private String totalPregunta1Si;
	private String totalPregunta1No;
	private String totalPregunta2MuyMala;
	private String totalPregunta2Mala;
	private String totalPregunta2Regular;
	private String totalPregunta2Buena;
	private String totalPregunta2MuyBuena;
	private String totalPregunta2NoAplica;
	private String totalPregunta3MuyMala;
	private String totalPregunta3Mala;
	private String totalPregunta3Regular;
	private String totalPregunta3Buena;
	private String totalPregunta3MuyBuena;
	private String totalPregunta3NoAplica;
	private String totalPregunta4MuyMala;
	private String totalPregunta4Mala;
	private String totalPregunta4Regular;
	private String totalPregunta4Buena;
	private String totalPregunta4MuyBuena;
	private String totalPregunta4NoAplica;
	private String totalPregunta5MuyMala;
	private String totalPregunta5Mala;
	private String totalPregunta5Regular;
	private String totalPregunta5Buena;
	private String totalPregunta5MuyBuena;
	private String totalPregunta5NoAplica;
	private String totalPregunta6MuyMala;
	private String totalPregunta6Mala;
	private String totalPregunta6Regular;
	private String totalPregunta6Buena;
	private String totalPregunta6MuyBuena;
	private String totalPregunta7MuyMala;
	private String totalPregunta7Mala;
	private String totalPregunta7Regular;
	private String totalPregunta7Buena;
	private String totalPregunta7MuyBuena;
	private String totalPregunta8MuyMala;
	private String totalPregunta8Mala;
	private String totalPregunta8Regular;
	private String totalPregunta8Buena;
	private String totalPregunta8MuyBuena;
	private String totalPregunta8NoAplica;
	private String totalPregunta9MuyMala;
	private String totalPregunta9Mala;
	private String totalPregunta9Regular;
	private String totalPregunta9Buena;
	private String totalPregunta9MuyBuena;
	private String totalPregunta9NoAplica;
	private String totalPregunta10Si;
	private String totalPregunta10No;
	private String totalPregunta10NoAplica;
	private String totalPregunta11MuyMala;
	private String totalPregunta11Mala;
	private String totalPregunta11Regular;
	private String totalPregunta11Buena;
	private String totalPregunta11MuyBuena;
	private String totalPregunta12DefinitivamenteNo; 
	private String totalPregunta12ProbablementeNo;
	private String totalPregunta12ProbablementeSi;
	private String totalPregunta12DefinitivamenteSi;	
	

	public AtenEncuConsolidadoDTO() {
	}
	
	public AtenEncuConsolidadoDTO(String servicio, String numeroEncuestas, String totalPregunta1Si,
			String totalPregunta1No, String totalPregunta2MuyMala, String totalPregunta2Mala,
			String totalPregunta2Regular, String totalPregunta2Buena, String totalPregunta2MuyBuena,
			String totalPregunta2NoAplica, String totalPregunta3MuyMala, String totalPregunta3Mala,
			String totalPregunta3Regular, String totalPregunta3Buena, String totalPregunta3MuyBuena,
			String totalPregunta3NoAplica, String totalPregunta4MuyMala, String totalPregunta4Mala,
			String totalPregunta4Regular, String totalPregunta4Buena, String totalPregunta4MuyBuena,
			String totalPregunta4NoAplica, String totalPregunta5MuyMala, String totalPregunta5Mala,
			String totalPregunta5Regular, String totalPregunta5Buena, String totalPregunta5MuyBuena,
			String totalPregunta5NoAplica, String totalPregunta6MuyMala, String totalPregunta6Mala,
			String totalPregunta6Regular, String totalPregunta6Buena, String totalPregunta6MuyBuena,
			String totalPregunta7MuyMala, String totalPregunta7Mala, String totalPregunta7Regular,
			String totalPregunta7Buena, String totalPregunta7MuyBuena, String totalPregunta8MuyMala,
			String totalPregunta8Mala, String totalPregunta8Regular, String totalPregunta8Buena,
			String totalPregunta8MuyBuena, String totalPregunta8NoAplica, String totalPregunta9MuyMala,
			String totalPregunta9Mala, String totalPregunta9Regular, String totalPregunta9Buena,
			String totalPregunta9MuyBuena, String totalPregunta9NoAplica, String totalPregunta10Si,
			String totalPregunta10No, String totalPregunta10NoAplica, String totalPregunta11MuyMala,
			String totalPregunta11Mala, String totalPregunta11Regular, String totalPregunta11Buena,
			String totalPregunta11MuyBuena, String totalPregunta12DefinitivamenteNo,
			String totalPregunta12ProbablementeNo, String totalPregunta12ProbablementeSi,
			String totalPregunta12DefinitivamenteSi) {		
		this.servicio = servicio;
		this.numeroEncuestas = numeroEncuestas;
		this.totalPregunta1Si = totalPregunta1Si;
		this.totalPregunta1No = totalPregunta1No;
		this.totalPregunta2MuyMala = totalPregunta2MuyMala;
		this.totalPregunta2Mala = totalPregunta2Mala;
		this.totalPregunta2Regular = totalPregunta2Regular;
		this.totalPregunta2Buena = totalPregunta2Buena;
		this.totalPregunta2MuyBuena = totalPregunta2MuyBuena;
		this.totalPregunta2NoAplica = totalPregunta2NoAplica;
		this.totalPregunta3MuyMala = totalPregunta3MuyMala;
		this.totalPregunta3Mala = totalPregunta3Mala;
		this.totalPregunta3Regular = totalPregunta3Regular;
		this.totalPregunta3Buena = totalPregunta3Buena;
		this.totalPregunta3MuyBuena = totalPregunta3MuyBuena;
		this.totalPregunta3NoAplica = totalPregunta3NoAplica;
		this.totalPregunta4MuyMala = totalPregunta4MuyMala;
		this.totalPregunta4Mala = totalPregunta4Mala;
		this.totalPregunta4Regular = totalPregunta4Regular;
		this.totalPregunta4Buena = totalPregunta4Buena;
		this.totalPregunta4MuyBuena = totalPregunta4MuyBuena;
		this.totalPregunta4NoAplica = totalPregunta4NoAplica;
		this.totalPregunta5MuyMala = totalPregunta5MuyMala;
		this.totalPregunta5Mala = totalPregunta5Mala;
		this.totalPregunta5Regular = totalPregunta5Regular;
		this.totalPregunta5Buena = totalPregunta5Buena;
		this.totalPregunta5MuyBuena = totalPregunta5MuyBuena;
		this.totalPregunta5NoAplica = totalPregunta5NoAplica;
		this.totalPregunta6MuyMala = totalPregunta6MuyMala;
		this.totalPregunta6Mala = totalPregunta6Mala;
		this.totalPregunta6Regular = totalPregunta6Regular;
		this.totalPregunta6Buena = totalPregunta6Buena;
		this.totalPregunta6MuyBuena = totalPregunta6MuyBuena;
		this.totalPregunta7MuyMala = totalPregunta7MuyMala;
		this.totalPregunta7Mala = totalPregunta7Mala;
		this.totalPregunta7Regular = totalPregunta7Regular;
		this.totalPregunta7Buena = totalPregunta7Buena;
		this.totalPregunta7MuyBuena = totalPregunta7MuyBuena;
		this.totalPregunta8MuyMala = totalPregunta8MuyMala;
		this.totalPregunta8Mala = totalPregunta8Mala;
		this.totalPregunta8Regular = totalPregunta8Regular;
		this.totalPregunta8Buena = totalPregunta8Buena;
		this.totalPregunta8MuyBuena = totalPregunta8MuyBuena;
		this.totalPregunta8NoAplica = totalPregunta8NoAplica;
		this.totalPregunta9MuyMala = totalPregunta9MuyMala;
		this.totalPregunta9Mala = totalPregunta9Mala;
		this.totalPregunta9Regular = totalPregunta9Regular;
		this.totalPregunta9Buena = totalPregunta9Buena;
		this.totalPregunta9MuyBuena = totalPregunta9MuyBuena;
		this.totalPregunta9NoAplica = totalPregunta9NoAplica;
		this.totalPregunta10Si = totalPregunta10Si;
		this.totalPregunta10No = totalPregunta10No;
		this.totalPregunta10NoAplica = totalPregunta10NoAplica;
		this.totalPregunta11MuyMala = totalPregunta11MuyMala;
		this.totalPregunta11Mala = totalPregunta11Mala;
		this.totalPregunta11Regular = totalPregunta11Regular;
		this.totalPregunta11Buena = totalPregunta11Buena;
		this.totalPregunta11MuyBuena = totalPregunta11MuyBuena;
		this.totalPregunta12DefinitivamenteNo = totalPregunta12DefinitivamenteNo;
		this.totalPregunta12ProbablementeNo = totalPregunta12ProbablementeNo;
		this.totalPregunta12ProbablementeSi = totalPregunta12ProbablementeSi;
		this.totalPregunta12DefinitivamenteSi = totalPregunta12DefinitivamenteSi;
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

	public String getTotalPregunta1Si() {
		return totalPregunta1Si;
	}

	public void setTotalPregunta1Si(String totalPregunta1Si) {
		this.totalPregunta1Si = totalPregunta1Si;
	}

	public String getTotalPregunta1No() {
		return totalPregunta1No;
	}

	public void setTotalPregunta1No(String totalPregunta1No) {
		this.totalPregunta1No = totalPregunta1No;
	}

	public String getTotalPregunta2MuyMala() {
		return totalPregunta2MuyMala;
	}

	public void setTotalPregunta2MuyMala(String totalPregunta2MuyMala) {
		this.totalPregunta2MuyMala = totalPregunta2MuyMala;
	}

	public String getTotalPregunta2Mala() {
		return totalPregunta2Mala;
	}

	public void setTotalPregunta2Mala(String totalPregunta2Mala) {
		this.totalPregunta2Mala = totalPregunta2Mala;
	}

	public String getTotalPregunta2Regular() {
		return totalPregunta2Regular;
	}

	public void setTotalPregunta2Regular(String totalPregunta2Regular) {
		this.totalPregunta2Regular = totalPregunta2Regular;
	}

	public String getTotalPregunta2Buena() {
		return totalPregunta2Buena;
	}

	public void setTotalPregunta2Buena(String totalPregunta2Buena) {
		this.totalPregunta2Buena = totalPregunta2Buena;
	}

	public String getTotalPregunta2MuyBuena() {
		return totalPregunta2MuyBuena;
	}

	public void setTotalPregunta2MuyBuena(String totalPregunta2MuyBuena) {
		this.totalPregunta2MuyBuena = totalPregunta2MuyBuena;
	}

	public String getTotalPregunta2NoAplica() {
		return totalPregunta2NoAplica;
	}

	public void setTotalPregunta2NoAplica(String totalPregunta2NoAplica) {
		this.totalPregunta2NoAplica = totalPregunta2NoAplica;
	}

	public String getTotalPregunta3MuyMala() {
		return totalPregunta3MuyMala;
	}

	public void setTotalPregunta3MuyMala(String totalPregunta3MuyMala) {
		this.totalPregunta3MuyMala = totalPregunta3MuyMala;
	}

	public String getTotalPregunta3Mala() {
		return totalPregunta3Mala;
	}

	public void setTotalPregunta3Mala(String totalPregunta3Mala) {
		this.totalPregunta3Mala = totalPregunta3Mala;
	}

	public String getTotalPregunta3Regular() {
		return totalPregunta3Regular;
	}

	public void setTotalPregunta3Regular(String totalPregunta3Regular) {
		this.totalPregunta3Regular = totalPregunta3Regular;
	}

	public String getTotalPregunta3Buena() {
		return totalPregunta3Buena;
	}

	public void setTotalPregunta3Buena(String totalPregunta3Buena) {
		this.totalPregunta3Buena = totalPregunta3Buena;
	}

	public String getTotalPregunta3MuyBuena() {
		return totalPregunta3MuyBuena;
	}

	public void setTotalPregunta3MuyBuena(String totalPregunta3MuyBuena) {
		this.totalPregunta3MuyBuena = totalPregunta3MuyBuena;
	}

	public String getTotalPregunta3NoAplica() {
		return totalPregunta3NoAplica;
	}

	public void setTotalPregunta3NoAplica(String totalPregunta3NoAplica) {
		this.totalPregunta3NoAplica = totalPregunta3NoAplica;
	}

	public String getTotalPregunta4MuyMala() {
		return totalPregunta4MuyMala;
	}

	public void setTotalPregunta4MuyMala(String totalPregunta4MuyMala) {
		this.totalPregunta4MuyMala = totalPregunta4MuyMala;
	}

	public String getTotalPregunta4Mala() {
		return totalPregunta4Mala;
	}

	public void setTotalPregunta4Mala(String totalPregunta4Mala) {
		this.totalPregunta4Mala = totalPregunta4Mala;
	}

	public String getTotalPregunta4Regular() {
		return totalPregunta4Regular;
	}

	public void setTotalPregunta4Regular(String totalPregunta4Regular) {
		this.totalPregunta4Regular = totalPregunta4Regular;
	}

	public String getTotalPregunta4Buena() {
		return totalPregunta4Buena;
	}

	public void setTotalPregunta4Buena(String totalPregunta4Buena) {
		this.totalPregunta4Buena = totalPregunta4Buena;
	}

	public String getTotalPregunta4MuyBuena() {
		return totalPregunta4MuyBuena;
	}

	public void setTotalPregunta4MuyBuena(String totalPregunta4MuyBuena) {
		this.totalPregunta4MuyBuena = totalPregunta4MuyBuena;
	}

	public String getTotalPregunta4NoAplica() {
		return totalPregunta4NoAplica;
	}

	public void setTotalPregunta4NoAplica(String totalPregunta4NoAplica) {
		this.totalPregunta4NoAplica = totalPregunta4NoAplica;
	}

	public String getTotalPregunta5MuyMala() {
		return totalPregunta5MuyMala;
	}

	public void setTotalPregunta5MuyMala(String totalPregunta5MuyMala) {
		this.totalPregunta5MuyMala = totalPregunta5MuyMala;
	}

	public String getTotalPregunta5Mala() {
		return totalPregunta5Mala;
	}

	public void setTotalPregunta5Mala(String totalPregunta5Mala) {
		this.totalPregunta5Mala = totalPregunta5Mala;
	}

	public String getTotalPregunta5Regular() {
		return totalPregunta5Regular;
	}

	public void setTotalPregunta5Regular(String totalPregunta5Regular) {
		this.totalPregunta5Regular = totalPregunta5Regular;
	}

	public String getTotalPregunta5Buena() {
		return totalPregunta5Buena;
	}

	public void setTotalPregunta5Buena(String totalPregunta5Buena) {
		this.totalPregunta5Buena = totalPregunta5Buena;
	}

	public String getTotalPregunta5MuyBuena() {
		return totalPregunta5MuyBuena;
	}

	public void setTotalPregunta5MuyBuena(String totalPregunta5MuyBuena) {
		this.totalPregunta5MuyBuena = totalPregunta5MuyBuena;
	}

	public String getTotalPregunta5NoAplica() {
		return totalPregunta5NoAplica;
	}

	public void setTotalPregunta5NoAplica(String totalPregunta5NoAplica) {
		this.totalPregunta5NoAplica = totalPregunta5NoAplica;
	}

	public String getTotalPregunta6MuyMala() {
		return totalPregunta6MuyMala;
	}

	public void setTotalPregunta6MuyMala(String totalPregunta6MuyMala) {
		this.totalPregunta6MuyMala = totalPregunta6MuyMala;
	}

	public String getTotalPregunta6Mala() {
		return totalPregunta6Mala;
	}

	public void setTotalPregunta6Mala(String totalPregunta6Mala) {
		this.totalPregunta6Mala = totalPregunta6Mala;
	}

	public String getTotalPregunta6Regular() {
		return totalPregunta6Regular;
	}

	public void setTotalPregunta6Regular(String totalPregunta6Regular) {
		this.totalPregunta6Regular = totalPregunta6Regular;
	}

	public String getTotalPregunta6Buena() {
		return totalPregunta6Buena;
	}

	public void setTotalPregunta6Buena(String totalPregunta6Buena) {
		this.totalPregunta6Buena = totalPregunta6Buena;
	}

	public String getTotalPregunta6MuyBuena() {
		return totalPregunta6MuyBuena;
	}

	public void setTotalPregunta6MuyBuena(String totalPregunta6MuyBuena) {
		this.totalPregunta6MuyBuena = totalPregunta6MuyBuena;
	}

	public String getTotalPregunta7MuyMala() {
		return totalPregunta7MuyMala;
	}

	public void setTotalPregunta7MuyMala(String totalPregunta7MuyMala) {
		this.totalPregunta7MuyMala = totalPregunta7MuyMala;
	}

	public String getTotalPregunta7Mala() {
		return totalPregunta7Mala;
	}

	public void setTotalPregunta7Mala(String totalPregunta7Mala) {
		this.totalPregunta7Mala = totalPregunta7Mala;
	}

	public String getTotalPregunta7Regular() {
		return totalPregunta7Regular;
	}

	public void setTotalPregunta7Regular(String totalPregunta7Regular) {
		this.totalPregunta7Regular = totalPregunta7Regular;
	}

	public String getTotalPregunta7Buena() {
		return totalPregunta7Buena;
	}

	public void setTotalPregunta7Buena(String totalPregunta7Buena) {
		this.totalPregunta7Buena = totalPregunta7Buena;
	}

	public String getTotalPregunta7MuyBuena() {
		return totalPregunta7MuyBuena;
	}

	public void setTotalPregunta7MuyBuena(String totalPregunta7MuyBuena) {
		this.totalPregunta7MuyBuena = totalPregunta7MuyBuena;
	}

	public String getTotalPregunta8MuyMala() {
		return totalPregunta8MuyMala;
	}

	public void setTotalPregunta8MuyMala(String totalPregunta8MuyMala) {
		this.totalPregunta8MuyMala = totalPregunta8MuyMala;
	}

	public String getTotalPregunta8Mala() {
		return totalPregunta8Mala;
	}

	public void setTotalPregunta8Mala(String totalPregunta8Mala) {
		this.totalPregunta8Mala = totalPregunta8Mala;
	}

	public String getTotalPregunta8Regular() {
		return totalPregunta8Regular;
	}

	public void setTotalPregunta8Regular(String totalPregunta8Regular) {
		this.totalPregunta8Regular = totalPregunta8Regular;
	}

	public String getTotalPregunta8Buena() {
		return totalPregunta8Buena;
	}

	public void setTotalPregunta8Buena(String totalPregunta8Buena) {
		this.totalPregunta8Buena = totalPregunta8Buena;
	}

	public String getTotalPregunta8MuyBuena() {
		return totalPregunta8MuyBuena;
	}

	public void setTotalPregunta8MuyBuena(String totalPregunta8MuyBuena) {
		this.totalPregunta8MuyBuena = totalPregunta8MuyBuena;
	}

	public String getTotalPregunta8NoAplica() {
		return totalPregunta8NoAplica;
	}

	public void setTotalPregunta8NoAplica(String totalPregunta8NoAplica) {
		this.totalPregunta8NoAplica = totalPregunta8NoAplica;
	}

	public String getTotalPregunta9MuyMala() {
		return totalPregunta9MuyMala;
	}

	public void setTotalPregunta9MuyMala(String totalPregunta9MuyMala) {
		this.totalPregunta9MuyMala = totalPregunta9MuyMala;
	}

	public String getTotalPregunta9Mala() {
		return totalPregunta9Mala;
	}

	public void setTotalPregunta9Mala(String totalPregunta9Mala) {
		this.totalPregunta9Mala = totalPregunta9Mala;
	}

	public String getTotalPregunta9Regular() {
		return totalPregunta9Regular;
	}

	public void setTotalPregunta9Regular(String totalPregunta9Regular) {
		this.totalPregunta9Regular = totalPregunta9Regular;
	}

	public String getTotalPregunta9Buena() {
		return totalPregunta9Buena;
	}

	public void setTotalPregunta9Buena(String totalPregunta9Buena) {
		this.totalPregunta9Buena = totalPregunta9Buena;
	}

	public String getTotalPregunta9MuyBuena() {
		return totalPregunta9MuyBuena;
	}

	public void setTotalPregunta9MuyBuena(String totalPregunta9MuyBuena) {
		this.totalPregunta9MuyBuena = totalPregunta9MuyBuena;
	}

	public String getTotalPregunta9NoAplica() {
		return totalPregunta9NoAplica;
	}

	public void setTotalPregunta9NoAplica(String totalPregunta9NoAplica) {
		this.totalPregunta9NoAplica = totalPregunta9NoAplica;
	}

	public String getTotalPregunta10Si() {
		return totalPregunta10Si;
	}

	public void setTotalPregunta10Si(String totalPregunta10Si) {
		this.totalPregunta10Si = totalPregunta10Si;
	}

	public String getTotalPregunta10No() {
		return totalPregunta10No;
	}

	public void setTotalPregunta10No(String totalPregunta10No) {
		this.totalPregunta10No = totalPregunta10No;
	}

	public String getTotalPregunta10NoAplica() {
		return totalPregunta10NoAplica;
	}

	public void setTotalPregunta10NoAplica(String totalPregunta10NoAplica) {
		this.totalPregunta10NoAplica = totalPregunta10NoAplica;
	}

	public String getTotalPregunta11MuyMala() {
		return totalPregunta11MuyMala;
	}

	public void setTotalPregunta11MuyMala(String totalPregunta11MuyMala) {
		this.totalPregunta11MuyMala = totalPregunta11MuyMala;
	}

	public String getTotalPregunta11Mala() {
		return totalPregunta11Mala;
	}

	public void setTotalPregunta11Mala(String totalPregunta11Mala) {
		this.totalPregunta11Mala = totalPregunta11Mala;
	}

	public String getTotalPregunta11Regular() {
		return totalPregunta11Regular;
	}

	public void setTotalPregunta11Regular(String totalPregunta11Regular) {
		this.totalPregunta11Regular = totalPregunta11Regular;
	}

	public String getTotalPregunta11Buena() {
		return totalPregunta11Buena;
	}

	public void setTotalPregunta11Buena(String totalPregunta11Buena) {
		this.totalPregunta11Buena = totalPregunta11Buena;
	}

	public String getTotalPregunta11MuyBuena() {
		return totalPregunta11MuyBuena;
	}

	public void setTotalPregunta11MuyBuena(String totalPregunta11MuyBuena) {
		this.totalPregunta11MuyBuena = totalPregunta11MuyBuena;
	}

	public String getTotalPregunta12DefinitivamenteNo() {
		return totalPregunta12DefinitivamenteNo;
	}

	public void setTotalPregunta12DefinitivamenteNo(String totalPregunta12DefinitivamenteNo) {
		this.totalPregunta12DefinitivamenteNo = totalPregunta12DefinitivamenteNo;
	}

	public String getTotalPregunta12ProbablementeNo() {
		return totalPregunta12ProbablementeNo;
	}

	public void setTotalPregunta12ProbablementeNo(String totalPregunta12ProbablementeNo) {
		this.totalPregunta12ProbablementeNo = totalPregunta12ProbablementeNo;
	}

	public String getTotalPregunta12ProbablementeSi() {
		return totalPregunta12ProbablementeSi;
	}

	public void setTotalPregunta12ProbablementeSi(String totalPregunta12ProbablementeSi) {
		this.totalPregunta12ProbablementeSi = totalPregunta12ProbablementeSi;
	}

	public String getTotalPregunta12DefinitivamenteSi() {
		return totalPregunta12DefinitivamenteSi;
	}

	public void setTotalPregunta12DefinitivamenteSi(String totalPregunta12DefinitivamenteSi) {
		this.totalPregunta12DefinitivamenteSi = totalPregunta12DefinitivamenteSi;
	}	
}
