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
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = "est_certificado", uniqueConstraints = {@UniqueConstraint(columnNames = {"id_serial"})})
public class EstCertificado implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;
	private Integer idPaciente;	
	private String loginUsrAlta;
	private Date fechaAlta;
	private String loginUsrAct;
	private Date fechaAltaAct;	
	private EstSerial estSerial; 
	
	
	@PrePersist
	public void prePersist() {		
		this.fechaAlta = new Date();
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
	
	@Column(name = "id_paciente")
	public Integer getIdPaciente() {
		return idPaciente;
	}

	public void setIdPaciente(Integer idPaciente) {
		this.idPaciente = idPaciente;
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
	@JoinColumn(name = "id_serial", nullable=false)
	public EstSerial getEstSerial() {
		return estSerial;
	}

	public void setEstSerial(EstSerial estSerial) {
		this.estSerial = estSerial;
	}	
	
	
	
}
