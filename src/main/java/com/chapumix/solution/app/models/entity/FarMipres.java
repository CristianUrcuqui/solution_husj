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
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "far_mipres")
public class FarMipres implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;	
	private String numeroPrescripcion;
	private Integer consecutivoTecnologia;	
	private String numeroDocumentoPaciente;
	private Integer numeroEntrega;
	private String codigoServicio;
	private String cantidadEntregada;
	private Integer entregaTotal;
	private Integer causaNoEntrega;
	private Date fechaEntrega;
	private String loteEntregado;
	private String idMipress;
	private String idEntregaMipress;
	private Boolean enviado;
	private Date fechaRegistro;
	private String loginUsrAlta;
	private Date fechaAlta;
	private String loginUsrAct;
	private Date fechaAltaAct;	
	private ComTipoTecnologia comTipoTecnologia;
	private ComTipoDocumentoMipres comTipoDocumentoMipres;
	
	public FarMipres() {		
	}
	
	@PrePersist
	public void prePersist() {		
		this.fechaAlta = new Date();
		this.fechaRegistro = new Date();
	}
	

	@Id
	@Column(name = "id_entrega")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@NotEmpty
	@Column(name = "numero_prescripcion", length = 20)
	public String getNumeroPrescripcion() {
		return numeroPrescripcion;
	}

	public void setNumeroPrescripcion(String numeroPrescripcion) {
		this.numeroPrescripcion = numeroPrescripcion;
	}

	@NotNull
	@Column(name = "consecutivo_tecnologia", length = 5)
	public Integer getConsecutivoTecnologia() {
		return consecutivoTecnologia;
	}

	public void setConsecutivoTecnologia(Integer consecutivoTecnologia) {
		this.consecutivoTecnologia = consecutivoTecnologia;
	}	

	@NotEmpty
	@Column(name = "numero_documento_paciente", length = 50)
	public String getNumeroDocumentoPaciente() {
		return numeroDocumentoPaciente;
	}

	public void setNumeroDocumentoPaciente(String numeroDocumentoPaciente) {
		this.numeroDocumentoPaciente = numeroDocumentoPaciente;
	}

	@NotNull
	@Column(name = "numero_entrega")
	public Integer getNumeroEntrega() {
		return numeroEntrega;
	}

	public void setNumeroEntrega(Integer numeroEntrega) {
		this.numeroEntrega = numeroEntrega;
	}

	@NotEmpty
	@Column(name = "codigo_servicio", length = 20)
	public String getCodigoServicio() {
		return codigoServicio;
	}

	public void setCodigoServicio(String codigoServicio) {
		this.codigoServicio = codigoServicio;
	}

	@NotEmpty
	@Column(name = "cantidad_entregada", length = 20)
	public String getCantidadEntregada() {
		return cantidadEntregada;
	}

	public void setCantidadEntregada(String cantidadEntregada) {
		this.cantidadEntregada = cantidadEntregada;
	}

	@NotNull
	@Column(name = "entrega_total")
	public Integer getEntregaTotal() {
		return entregaTotal;
	}

	public void setEntregaTotal(Integer entregaTotal) {
		this.entregaTotal = entregaTotal;
	}
	
	@Column(name = "causa_no_entrega")
	public Integer getCausaNoEntrega() {
		return causaNoEntrega;
	}

	public void setCausaNoEntrega(Integer causaNoEntrega) {
		this.causaNoEntrega = causaNoEntrega;
	}

	@NotNull
	@Column(name = "fecha_entrega")
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "dd-MM-yyyy")
	public Date getFechaEntrega() {
		return fechaEntrega;
	}

	public void setFechaEntrega(Date fechaEntrega) {
		this.fechaEntrega = fechaEntrega;
	}

	@Column(name = "lote_entregado", length = 20)
	public String getLoteEntregado() {
		return loteEntregado;
	}

	public void setLoteEntregado(String loteEntregado) {
		this.loteEntregado = loteEntregado;
	}

	@Column(name = "id_mipres")
	public String getIdMipress() {
		return idMipress;
	}

	public void setIdMipress(String idMipress) {
		this.idMipress = idMipress;
	}

	@Column(name = "id_entrega_mipres")
	public String getIdEntregaMipress() {
		return idEntregaMipress;
	}

	public void setIdEntregaMipress(String idEntregaMipress) {
		this.idEntregaMipress = idEntregaMipress;
	}	

	@Column(name = "enviado", columnDefinition="BIT")
	public Boolean getEnviado() {
		return enviado;
	}

	public void setEnviado(Boolean enviado) {
		this.enviado = enviado;
	}
	
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "dd-MM-yyyy")
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

	@NotNull
	@ManyToOne(fetch = FetchType.LAZY, optional = false)	
	@JoinColumn(name = "id_tipo_tecnologia")
	public ComTipoTecnologia getComTipoTecnologia() {
		return comTipoTecnologia;
	}

	public void setComTipoTecnologia(ComTipoTecnologia comTipoTecnologia) {
		this.comTipoTecnologia = comTipoTecnologia;
	}

	@NotNull
	@ManyToOne(fetch = FetchType.LAZY, optional = false)	
	@JoinColumn(name = "id_tipo_documento")
	public ComTipoDocumentoMipres getComTipoDocumentoMipres() {
		return comTipoDocumentoMipres;
	}

	public void setComTipoDocumentoMipres(ComTipoDocumentoMipres comTipoDocumentoMipres) {
		this.comTipoDocumentoMipres = comTipoDocumentoMipres;
	}
	
	

}
