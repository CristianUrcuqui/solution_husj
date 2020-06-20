package com.chapumix.solution.app.models.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;


@Entity
@Table(name = "est_serial", uniqueConstraints = {@UniqueConstraint(columnNames = {"serial"})})

public class EstSerial implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;	
	private String serial;
	private Boolean estado;
	private String loginUsrAlta;
	private Date fechaAlta;
	private String loginUsrAct;
	private Date fechaAltaAct;
	private String loginUsrDel;
	private Date fechaDelAct;
	private Integer serialInicial;
	private Integer serialFinal;
	private EstTipoCertificado estTipoCertificado;
	private List<EstCertificado> estCertificado;
	
	
	
	@PrePersist
	public void prePersist() {		
		this.fechaAlta = new Date();
	}

	@Id
	@Column(name = "id_serial")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}	

	@Column(name = "serial", unique = true)
	public String getSerial() {
		return serial;
	}

	public void setSerial(String serial) {
		this.serial = serial;
	}	

	@Column(name = "estado", columnDefinition="BIT")
	public Boolean getEstado() {
		return estado;
	}

	public void setEstado(Boolean estado) {
		this.estado = estado;
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
	
	@Column(name = "login_usr_del")
	public String getLoginUsrDel() {
		return loginUsrDel;
	}

	public void setLoginUsrDel(String loginUsrDel) {
		this.loginUsrDel = loginUsrDel;
	}

	@Column(name = "fecha_del")
	public Date getFechaDelAct() {
		return fechaDelAct;
	}

	public void setFechaDelAct(Date fechaDelAct) {
		this.fechaDelAct = fechaDelAct;
	}

	@NotNull
	@Transient
	public Integer getSerialInicial() {
		return serialInicial;
	}

	public void setSerialInicial(Integer serialInicial) {
		this.serialInicial = serialInicial;
	}

	@NotNull
	@Transient
	public Integer getSerialFinal() {
		return serialFinal;
	}

	public void setSerialFinal(Integer serialFinal) {
		this.serialFinal = serialFinal;
	}

	@NotNull
	@ManyToOne
	@JoinColumn(name = "id_tipo_certificado", nullable = false)
	public EstTipoCertificado getEstTipoCertificado() {
		return estTipoCertificado;
	}

	public void setEstTipoCertificado(EstTipoCertificado estTipoCertificado) {
		this.estTipoCertificado = estTipoCertificado;
	}		
	
	@NotNull
	@OneToMany(mappedBy = "estSerial", fetch = FetchType.LAZY)
	public List<EstCertificado> getEstCertificado() {
		return estCertificado;
	}

	public void setEstCertificado(List<EstCertificado> estCertificado) {
		this.estCertificado = estCertificado;
	}	
	
	
	
}
