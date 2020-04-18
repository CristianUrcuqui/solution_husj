package com.chapumix.solution.app.models.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "est_certificado", uniqueConstraints = {@UniqueConstraint(columnNames = {"id_serial"})})
public class EstCertificado implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;
	private String docPaciente;
	private Integer idServicio;
	private Date fechaRegistro;
	private String loginUsrAlta;
	private Date fechaAlta;
	private String loginUsrAct;
	private Date fechaAltaAct;	
	private EstSerial estSerial; 
	
	
	@PrePersist
	public void prePersist() {		
		this.fechaAlta = new Date();
		this.fechaRegistro = new Date();
	}

	@Id
	@Column(name = "id_certificado")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	@NotEmpty
	@Column(name = "doc_paciente", length = 50)
	public String getDocPaciente() {
		return docPaciente;
	}

	public void setDocPaciente(String docPaciente) {
		this.docPaciente = docPaciente;
	}	
	
	@NotNull
	@Column(name = "id_servicio")
	public Integer getIdServicio() {
		return idServicio;
	}

	public void setIdServicio(Integer idServicio) {
		this.idServicio = idServicio;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "fecha_registro")
	public Date getFechaRegistro() {
		return fechaRegistro;
	}

	public void setFechaRegistro(Date fechaRegistro) {
		this.fechaRegistro = fechaRegistro;
	}

	@Column(name = "login_usr_alta")
	public String getLoginUsrAlta() {
		return loginUsrAlta;
	}

	public void setLoginUsrAlta(String loginUsrAlta) {
		this.loginUsrAlta = loginUsrAlta;
	}

	@Column(name = "fecha_alta")
	public Date getFechaAlta() {
		return fechaAlta;
	}

	public void setFechaAlta(Date fechaAlta) {
		this.fechaAlta = fechaAlta;
	}

	@Column(name = "login_usr_act")
	public String getLoginUsrAct() {
		return loginUsrAct;
	}

	public void setLoginUsrAct(String loginUsrAct) {
		this.loginUsrAct = loginUsrAct;
	}

	@Column(name = "fecha_act")
	public Date getFechaAltaAct() {
		return fechaAltaAct;
	}

	public void setFechaAltaAct(Date fechaAltaAct) {
		this.fechaAltaAct = fechaAltaAct;
	}	

	@ManyToOne	
	@JoinColumn(name = "id_serial")
	public EstSerial getEstSerial() {
		return estSerial;
	}

	public void setEstSerial(EstSerial estSerial) {
		this.estSerial = estSerial;
	}	
	
	
	
}
