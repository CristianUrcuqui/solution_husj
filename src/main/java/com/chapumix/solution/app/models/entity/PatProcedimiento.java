package com.chapumix.solution.app.models.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "pat_procedimiento")
public class PatProcedimiento implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;
	private Date fechaRegistro;
	private Integer idPaciente;
	private Integer idProcedimiento;
	private Integer folio;
	private Integer idPatologo;
	private Integer idPatologoReasigando;
	private String correccion;
	private String observacion;
	private String pacienteInternoExterno;

	@Id
	@Column(name = "id_patologia_procedimiento")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "fecha_registro")
	public Date getFechaRegistro() {
		return fechaRegistro;
	}

	public void setFechaRegistro(Date fechaRegistro) {
		this.fechaRegistro = fechaRegistro;
	}

	@Column(name = "id_paciente")
	public Integer getIdPaciente() {
		return idPaciente;
	}

	public void setIdPaciente(Integer idPaciente) {
		this.idPaciente = idPaciente;
	}

	@Column(name = "id_procedimiento")
	public Integer getIdProcedimiento() {
		return idProcedimiento;
	}

	public void setIdProcedimiento(Integer idProcedimiento) {
		this.idProcedimiento = idProcedimiento;
	}

	@Column(name = "folio")
	public Integer getFolio() {
		return folio;
	}

	public void setFolio(Integer folio) {
		this.folio = folio;
	}

	@Column(name = "id_patologia")
	public Integer getIdPatologo() {
		return idPatologo;
	}

	public void setIdPatologo(Integer idPatologo) {
		this.idPatologo = idPatologo;
	}

	@Column(name = "id_patologo_reasignado")
	public Integer getIdPatologoReasigando() {
		return idPatologoReasigando;
	}

	public void setIdPatologoReasigando(Integer idPatologoReasigando) {
		this.idPatologoReasigando = idPatologoReasigando;
	}

	@Column(name = "correccion")
	public String getCorreccion() {
		return correccion;
	}

	public void setCorreccion(String correccion) {
		this.correccion = correccion;
	}

	@Column(name = "observacion")
	public String getObservacion() {
		return observacion;
	}

	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}

	@Column(name = "paciente_interno_externo")
	public String getPacienteInternoExterno() {
		return pacienteInternoExterno;
	}

	public void setPacienteInternoExterno(String pacienteInternoExterno) {
		this.pacienteInternoExterno = pacienteInternoExterno;
	}
}
