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
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;
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
	private Integer numeroEntrega;
	private String codigoServicio;
	private String cantidadEntregada;
	private Integer entregaTotal;
	private Integer causaNoEntrega;
	private String valorEntregado;
	private String valorUnitario;
	private String valorTotal;
	private String cuotaModeradora;
	private String copago;
	private Date fechaEntrega;
	private String loteEntregado;
	private String codEps;
	private String idTraza;
	private String idEntregaMipress;
	private String idReporteEntregaMipress;
	private String idFacturacionMipress;
	private String idReporteFacturacionMipress;
	private Boolean procesadoEntrega;
	private Boolean procesadoReporteEntrega;
	private Boolean procesadoFacturacion;
	private String numeroFactura;
	private String nombreMedicamento;
	private Date fechaRegistro;
	private String loginUsrAlta;
	private Date fechaAlta;
	private String loginUsrAct;
	private Date fechaAltaAct;	
	private ComTipoTecnologia comTipoTecnologia;
	private ComTipoDocumentoMipres comTipoDocumentoMipres;
	private GenPacien genPacien;	
	
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
	

	@Column(name = "valor_unitario")
	public String getValorUnitario() {
		return valorUnitario;
	}


	public void setValorUnitario(String valorUnitario) {
		this.valorUnitario = valorUnitario;
	}


	@Column(name = "valor_total")
	public String getValorTotal() {
		return valorTotal;
	}


	public void setValorTotal(String valorTotal) {
		this.valorTotal = valorTotal;
	}

	@Column(name = "cuota_moderadora")
	public String getCuotaModeradora() {
		return cuotaModeradora;
	}


	public void setCuotaModeradora(String cuotaModeradora) {
		this.cuotaModeradora = cuotaModeradora;
	}
	

	@Column(name = "copago")
	public String getCopago() {
		return copago;
	}


	public void setCopago(String copago) {
		this.copago = copago;
	}


	@Column(name = "valor_entregado")
	public String getValorEntregado() {
		return valorEntregado;
	}

	public void setValorEntregado(String valorEntregado) {
		this.valorEntregado = valorEntregado;
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

	@Column(name = "codigo_eps", length = 10)
	public String getCodEps() {
		return codEps;
	}


	public void setCodEps(String codEps) {
		this.codEps = codEps;
	}


	@Column(name = "id_traza")
	public String getIdTraza() {
		return idTraza;
	}

	public void setIdTraza(String idTraza) {
		this.idTraza = idTraza;
	}	

	@Column(name = "id_entrega_mipres")
	public String getIdEntregaMipress() {
		return idEntregaMipress;
	}

	public void setIdEntregaMipress(String idEntregaMipress) {
		this.idEntregaMipress = idEntregaMipress;
	}	

	@Column(name = "id_reporte_entrega_mipres")
	public String getIdReporteEntregaMipress() {
		return idReporteEntregaMipress;
	}


	public void setIdReporteEntregaMipress(String idReporteEntregaMipress) {
		this.idReporteEntregaMipress = idReporteEntregaMipress;
	}


	@Column(name = "id_facturacion_mipres")
	public String getIdFacturacionMipress() {
		return idFacturacionMipress;
	}


	public void setIdFacturacionMipress(String idFacturacionMipress) {
		this.idFacturacionMipress = idFacturacionMipress;
	}


	@Column(name = "id_reporte_facturacion_mipres")
	public String getIdReporteFacturacionMipress() {
		return idReporteFacturacionMipress;
	}


	public void setIdReporteFacturacionMipress(String idReporteFacturacionMipress) {
		this.idReporteFacturacionMipress = idReporteFacturacionMipress;
	}


	@Column(name = "procesado_entrega", columnDefinition="BIT")
	public Boolean getProcesadoEntrega() {
		return procesadoEntrega;
	}

	public void setProcesadoEntrega(Boolean procesadoEntrega) {
		this.procesadoEntrega = procesadoEntrega;
	}	
	
	@Column(name = "procesado_reporte_entrega", columnDefinition="BIT")
	public Boolean getProcesadoReporteEntrega() {
		return procesadoReporteEntrega;
	}

	public void setProcesadoReporteEntrega(Boolean procesadoReporteEntrega) {
		this.procesadoReporteEntrega = procesadoReporteEntrega;
	}

	@Column(name = "procesado_facturacion", columnDefinition="BIT")
	public Boolean getProcesadoFacturacion() {
		return procesadoFacturacion;
	}

	public void setProcesadoFacturacion(Boolean procesadoFacturacion) {
		this.procesadoFacturacion = procesadoFacturacion;
	}

	@Column(name = "numero_factura")	
	public String getNumeroFactura() {
		return numeroFactura;
	}

	public void setNumeroFactura(String numeroFactura) {
		this.numeroFactura = numeroFactura;
	}	

	@Lob
	@Column(name = "nombre_medicamento")
	public String getNombreMedicamento() {
		return nombreMedicamento;
	}

	public void setNombreMedicamento(String nombreMedicamento) {
		this.nombreMedicamento = nombreMedicamento;
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

	@NotNull
	@Valid
	@ManyToOne(fetch = FetchType.LAZY, optional = false)	
	@JoinColumn(name = "id_paciente")
	public GenPacien getGenPacien() {
		return genPacien;
	}

	public void setGenPacien(GenPacien genPacien) {
		this.genPacien = genPacien;
	}
}
