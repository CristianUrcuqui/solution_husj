package com.chapumix.solution.app.models.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotEmpty;

@Entity
@Table(name = "ter_tercero")
public class TerTercero implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private Long idTercero;	
	private String numeroIdentificacion;	
	private String direccion;	
	private String codigoPrestador;
	private String nombreComercial;
	private String codigoPostal;
	private String observaciones;
	private String sitioWeb;	
	private String dv;
	private Date fechaAct;
	private Date fechaAlta;
	private String loginUsr;
	//private List<TerPersonaJuridica> terPersonaJuridicas;
	//private ComTipoIdentificacion comTipoIdentificacion; //funcionando
	//private ComMunicipio comMunicipio; //funcionando
	/*private List<ConContrato> conContratos;
	private List<ConPortafolio> conPortafolios;
	private List<FacFacturaCompra> facFacturaCompras;
	private List<FacFacturaVenta> facFacturaVentas1;
	private List<FacFacturaVenta> facFacturaVentas2;
	private List<GdcRepositorioImagen> gdcRepositorioImagens;
	private List<InvOrdenPedido> invOrdenPedidos;
	private List<InvOrdenServicio> invOrdenServicios;*/
	//private List<TerCuentaBancaria> terCuentaBancarias; //funcionando
	//private List<TerEmail> terEmails; //funcionando 
	//private List<TerEmpresa> terEmpresas; //funcionando
	//private List<TerPersonaContacto> terPersonaContactos; //funcionando
	//private List<TerPersonaJuridica> terPersonaJuridicas; //funcionando
	//private List<TerPersonaNatural> terPersonaNaturals; //funcionando
	/*private List<TerSucursal> terSucursals;*/
	//private List<TerTelefonoTercero> terTelefonoTerceros; //funcionando
	/*private List<ImpExcepcionesTributaria> impExcepcionesTributarias;
	private List<TerTerceroContratoAdm> terTerceroContratoAdms;
	private List<TerTerceroGuiaRetencion> terTerceroGuiaRetencions1;
	private List<TerTerceroGuiaRetencion> terTerceroGuiaRetencions2;*/
	
	public TerTercero() {
		//terPersonaJuridicas = new ArrayList<TerPersonaJuridica>();
	}

	
	@PrePersist
	public void prePersist() {
		fechaAct = new Date();
	}
	
	
	@Id
	@Column(name = "id_tercero")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getIdTercero() {
		return idTercero;
	}

	public void setIdTercero(Long idTercero) {
		this.idTercero = idTercero;
	}

	@NotEmpty
	@Column(name = "numero_identificacion")
	public String getNumeroIdentificacion() {
		return numeroIdentificacion;
	}

	public void setNumeroIdentificacion(String numeroIdentificacion) {
		this.numeroIdentificacion = numeroIdentificacion;
	}

	@NotEmpty
	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	@Column(name = "codigo_prestador")
	public String getCodigoPrestador() {
		return codigoPrestador;
	}

	public void setCodigoPrestador(String codigoPrestador) {
		this.codigoPrestador = codigoPrestador;
	}

	@Column(name = "nombre_comercial")
	public String getNombreComercial() {
		return nombreComercial;
	}

	public void setNombreComercial(String nombreComercial) {
		this.nombreComercial = nombreComercial;
	}

	@Column(name = "codigo_postal")
	public String getCodigoPostal() {
		return codigoPostal;
	}

	public void setCodigoPostal(String codigoPostal) {
		this.codigoPostal = codigoPostal;
	}

	public String getObservaciones() {
		return observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

	@Column(name = "sitio_web")
	public String getSitioWeb() {
		return sitioWeb;
	}

	public void setSitioWeb(String sitioWeb) {
		this.sitioWeb = sitioWeb;
	}

	public String getDv() {
		return dv;
	}

	public void setDv(String dv) {
		this.dv = dv;
	}

	@Temporal(TemporalType.DATE)
	@Column(name="fecha_act")
	public Date getFechaAct() {
		return fechaAct;
	}

	public void setFechaAct(Date fechaAct) {
		this.fechaAct = fechaAct;
	}

	@Temporal(TemporalType.DATE)
	@Column(name="fecha_alta")
	public Date getFechaAlta() {
		return fechaAlta;
	}

	public void setFechaAlta(Date fechaAlta) {
		this.fechaAlta = fechaAlta;
	}

	@Column(name="login_usr")
	public String getLoginUsr() {
		return loginUsr;
	}

	public void setLoginUsr(String loginUsr) {
		this.loginUsr = loginUsr;
	}

	/*@OneToMany(mappedBy = "terTercero", fetch = FetchType.LAZY, cascade = CascadeType.ALL)//@OneToMany SIGNIFICA UNA PERSONA JURIDICA MUCHOS TERCEROS	
	public List<TerPersonaJuridica> getTerPersonaJuridicas() {
		return terPersonaJuridicas;
	}

	public void setTerPersonaJuridicas(List<TerPersonaJuridica> terPersonaJuridicas) {
		this.terPersonaJuridicas = terPersonaJuridicas;
	}
	
	public void addTerPersonaJuridica(TerPersonaJuridica terPersonaJuridica) {
		terPersonaJuridicas.add(terPersonaJuridica);
	}*/

}
