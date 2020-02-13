package com.chapumix.solution.app.entity.dto;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;

public class PatProcedimientoDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;
	private String codigo;
	private Date fechaAsignacion;
	private String historia;
	private String paciente;
	private String procedimiento;
	private Integer folio;
	private String patologo;
	private String patologoReasignado;
	private String correccion;
	private String observacion;
	private String pacienteInternoExterno;
	
	public PatProcedimientoDTO() {		
	}

	public PatProcedimientoDTO(Long id, String codigo, Date fechaAsignacion, String historia, String paciente,
			String procedimiento, Integer folio, String patologo, String patologoReasignado, String correccion,
			String observacion, String pacienteInternoExterno) {		
		this.id = id;
		this.codigo = codigo;
		this.fechaAsignacion = fechaAsignacion;
		this.historia = historia;
		this.paciente = paciente;
		this.procedimiento = procedimiento;
		this.folio = folio;
		this.patologo = patologo;
		this.patologoReasignado = patologoReasignado;
		this.correccion = correccion;
		this.observacion = observacion;
		this.pacienteInternoExterno = pacienteInternoExterno;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	@Temporal(TemporalType.TIMESTAMP)
	public Date getFechaAsignacion() {
		return fechaAsignacion;
	}

	public void setFechaAsignacion(Date fechaAsignacion) {
		this.fechaAsignacion = fechaAsignacion;
	}

	public String getHistoria() {
		return historia;
	}

	public void setHistoria(String historia) {
		this.historia = historia;
	}

	public String getPaciente() {
		return paciente;
	}

	public void setPaciente(String paciente) {
		this.paciente = paciente;
	}

	public String getProcedimiento() {
		return procedimiento;
	}

	public void setProcedimiento(String procedimiento) {
		this.procedimiento = procedimiento;
	}

	public Integer getFolio() {
		return folio;
	}

	public void setFolio(Integer folio) {
		this.folio = folio;
	}

	public String getPatologo() {
		return patologo;
	}

	public void setPatologo(String patologo) {
		this.patologo = patologo;
	}

	public String getPatologoReasignado() {
		return patologoReasignado;
	}

	public void setPatologoReasignado(String patologoReasignado) {
		this.patologoReasignado = patologoReasignado;
	}

	public String getCorreccion() {
		return correccion;
	}

	public void setCorreccion(String correccion) {
		this.correccion = correccion;
	}

	public String getObservacion() {
		return observacion;
	}

	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}

	public String getPacienteInternoExterno() {
		return pacienteInternoExterno;
	}

	public void setPacienteInternoExterno(String pacienteInternoExterno) {
		this.pacienteInternoExterno = pacienteInternoExterno;
	}

}

