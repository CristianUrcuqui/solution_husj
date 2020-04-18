package com.chapumix.solution.app.entity.dto;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class EstCertificadoDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;
	private String serial;
	private String docPaciente;
	private String nombreCompletoPaciente;
	private String tipoCertificado;
	private String nombreMedico;
	private String servicio;
	private Date fechaRegistro;

	public EstCertificadoDTO() {
	}
	


	public EstCertificadoDTO(Long id, String serial, String docPaciente, String nombreCompletoPaciente,
			String tipoCertificado, String nombreMedico, String servicio, Date fechaRegistro) {
		this.id = id;
		this.serial = serial;
		this.docPaciente = docPaciente;
		this.nombreCompletoPaciente = nombreCompletoPaciente;
		this.tipoCertificado = tipoCertificado;
		this.nombreMedico = nombreMedico;
		this.servicio = servicio;
		this.fechaRegistro = fechaRegistro;
	}


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getSerial() {
		return serial;
	}

	public void setSerial(String serial) {
		this.serial = serial;
	}

	public String getDocPaciente() {
		return docPaciente;
	}

	public void setDocPaciente(String docPaciente) {
		this.docPaciente = docPaciente;
	}

	public String getNombreCompletoPaciente() {
		return nombreCompletoPaciente;
	}

	public void setNombreCompletoPaciente(String nombreCompletoPaciente) {
		this.nombreCompletoPaciente = nombreCompletoPaciente;
	}

	public String getTipoCertificado() {
		return tipoCertificado;
	}

	public void setTipoCertificado(String tipoCertificado) {
		this.tipoCertificado = tipoCertificado;
	}

	public String getNombreMedico() {
		return nombreMedico;
	}

	public void setNombreMedico(String nombreMedico) {
		this.nombreMedico = nombreMedico;
	}	

	public String getServicio() {
		return servicio;
	}

	public void setServicio(String servicio) {
		this.servicio = servicio;
	}

	@Temporal(TemporalType.TIMESTAMP)
	public Date getFechaRegistro() {
		return fechaRegistro;
	}

	public void setFechaRegistro(Date fechaRegistro) {
		this.fechaRegistro = fechaRegistro;
	}

}
