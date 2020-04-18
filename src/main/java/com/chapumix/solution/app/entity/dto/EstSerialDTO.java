package com.chapumix.solution.app.entity.dto;

import com.opencsv.bean.CsvBindByName;

public class EstSerialDTO {

	@CsvBindByName
	private String serial;
	@CsvBindByName
	private Long idTipoCertificado;
	@CsvBindByName
	private Boolean estado;

	public EstSerialDTO() {
	}

	public String getSerial() {
		return serial;
	}

	public void setSerial(String serial) {
		this.serial = serial;
	}

	public Long getIdTipoCertificado() {
		return idTipoCertificado;
	}

	public void setIdTipoCertificado(Long idTipoCertificado) {
		this.idTipoCertificado = idTipoCertificado;
	}

	public Boolean getEstado() {
		return estado;
	}

	public void setEstado(Boolean estado) {
		this.estado = estado;
	}

}
