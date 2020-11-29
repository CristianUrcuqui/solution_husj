package com.chapumix.solution.app.models.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "med_int_cama")
public class MedIntCama implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;
	private String ingreso;
	private GenPacien genPacien;
	private ComEspecialidad comEspecialidad;
	private ComCama comCama;
	private Date fechaSolicitud;
	private Date fechaAsignacion;
	private Date fechaRechazo;
	private Date fechaAnulacionAsignacion;
	private ComEstadoTramite comEstadoTramite;
	private ComCausaRechazo comCausaRechazo;
	private Integer extensionTelefono;
	private String tipoAislamiento;
	private ComUsuario loginUsrTramita;
	
	
	@Id
	@Column(name = "id_med_int_cama")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	@NotNull
	@Column(name = "ingreso")
	public String getIngreso() {
		return ingreso;
	}

	public void setIngreso(String ingreso) {
		this.ingreso = ingreso;
	}	
	
	@ManyToOne(fetch = FetchType.LAZY, optional = false)	
	@JoinColumn(name = "id_paciente")
	public GenPacien getGenPacien() {
		return genPacien;
	}

	public void setGenPacien(GenPacien genPacien) {
		this.genPacien = genPacien;
	}	
		
	@ManyToOne(fetch = FetchType.LAZY)	
	@JoinColumn(name = "id_especialidad")
	public ComEspecialidad getComEspecialidad() {
		return comEspecialidad;
	}

	public void setComEspecialidad(ComEspecialidad comEspecialidad) {
		this.comEspecialidad = comEspecialidad;
	}	
	
	@ManyToOne(fetch = FetchType.LAZY)	
	@JoinColumn(name = "id_cama")
	public ComCama getComCama() {
		return comCama;
	}

	public void setComCama(ComCama comCama) {
		this.comCama = comCama;
	}

	@Column(name = "fecha_solicitud")
	public Date getFechaSolicitud() {
		return fechaSolicitud;
	}

	public void setFechaSolicitud(Date fechaSolicitud) {
		this.fechaSolicitud = fechaSolicitud;
	}

	@Column(name = "fecha_asignacion")
	public Date getFechaAsignacion() {
		return fechaAsignacion;
	}

	public void setFechaAsignacion(Date fechaAsignacion) {
		this.fechaAsignacion = fechaAsignacion;
	}	
	
	@Column(name = "fecha_rechazo")
	public Date getFechaRechazo() {
		return fechaRechazo;
	}

	public void setFechaRechazo(Date fechaRechazo) {
		this.fechaRechazo = fechaRechazo;
	}

	@Column(name = "fecha_anulacion_asignacion")
	public Date getFechaAnulacionAsignacion() {
		return fechaAnulacionAsignacion;
	}

	public void setFechaAnulacionAsignacion(Date fechaAnulacionAsignacion) {
		this.fechaAnulacionAsignacion = fechaAnulacionAsignacion;
	}	

	@ManyToOne(fetch = FetchType.LAZY, optional = false)	
	@JoinColumn(name = "id_estado_tramite")
	public ComEstadoTramite getComEstadoTramite() {
		return comEstadoTramite;
	}

	public void setComEstadoTramite(ComEstadoTramite comEstadoTramite) {
		this.comEstadoTramite = comEstadoTramite;
	}	

	@ManyToOne(fetch = FetchType.LAZY)	
	@JoinColumn(name = "id_causa_rechazo")	
	public ComCausaRechazo getComCausaRechazo() {
		return comCausaRechazo;
	}

	public void setComCausaRechazo(ComCausaRechazo comCausaRechazo) {
		this.comCausaRechazo = comCausaRechazo;
	}	
	
	@Column(name = "extension_telefono")
	public Integer getExtensionTelefono() {
		return extensionTelefono;
	}

	public void setExtensionTelefono(Integer extensionTelefono) {
		this.extensionTelefono = extensionTelefono;
	}

	@Column(name = "tipo_aislamiento")
	public String getTipoAislamiento() {
		return tipoAislamiento;
	}
	
	public void setTipoAislamiento(String tipoAislamiento) {
		this.tipoAislamiento = tipoAislamiento;
	}

	@ManyToOne(fetch = FetchType.LAZY)	
	@JoinColumn(name = "login_usr_tramita")
	public ComUsuario getLoginUsrTramita() {
		return loginUsrTramita;
	}

	public void setLoginUsrTramita(ComUsuario loginUsrTramita) {
		this.loginUsrTramita = loginUsrTramita;
	}	
	
}
