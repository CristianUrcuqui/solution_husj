package com.chapumix.solution.app.entity.dto;

import com.opencsv.bean.CsvBindByName;

public class CalCalendarioDTO {

	@CsvBindByName
	private String nombreCompleto;
	@CsvBindByName
	private String numeroIdentificacion;
	@CsvBindByName
	private String fechaNacimiento;
	@CsvBindByName
	private String correo;
	@CsvBindByName
	private Boolean enviado;
		
	public CalCalendarioDTO() {		
	}
	
	
	public String getNombreCompleto() {
		return nombreCompleto;
	}


	public void setNombreCompleto(String nombreCompleto) {
		this.nombreCompleto = nombreCompleto;
	}	

	public String getNumeroIdentificacion() {
		return numeroIdentificacion;
	}

	public void setNumeroIdentificacion(String numeroIdentificacion) {
		this.numeroIdentificacion = numeroIdentificacion;
	}


	public String getFechaNacimiento() {
		return fechaNacimiento;
	}


	public void setFechaNacimiento(String fechaNacimiento) {
		this.fechaNacimiento = fechaNacimiento;
	}

	public String getCorreo() {
		return correo;
	}


	public void setCorreo(String correo) {
		this.correo = correo;
	}

	public Boolean getEnviado() {
		return enviado;
	}


	public void setEnviado(Boolean enviado) {
		this.enviado = enviado;
	}	
	

}
