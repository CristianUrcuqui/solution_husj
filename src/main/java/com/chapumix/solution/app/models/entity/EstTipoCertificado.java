package com.chapumix.solution.app.models.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.Table;

@Entity
@Table(name = "est_tipo_certificado")
//@Table(name = "est_tipo_certificado", uniqueConstraints = {@UniqueConstraint(columnNames = {"id_serial"})})

public class EstTipoCertificado implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;
	private String tipoCertificado;
	private String loginUsrAlta;
	private Date fechaAlta;
	private String loginUsrAct;
	private Date fechaAltaAct;
	private List<EstSerial> estSerial;
	
	
	
	@PrePersist
	public void prePersist() {		
		this.fechaAlta = new Date();
	}

	@Id
	@Column(name = "id_tipo_certificado")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	@Column(name = "tipo_certificado")
	public String getTipoCertificado() {
		return tipoCertificado;
	}

	public void setTipoCertificado(String tipoCertificado) {
		this.tipoCertificado = tipoCertificado;
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

	@OneToMany(mappedBy = "estTipoCertificado")
	public List<EstSerial> getEstSerial() {
		return estSerial;
	}

	public void setEstSerial(List<EstSerial> estSerial) {
		this.estSerial = estSerial;
	}	
	
}
