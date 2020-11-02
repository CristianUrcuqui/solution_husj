package com.chapumix.solution.app.models.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;

@Entity
@Table(name = "com_token_mipres")
public class ComTokenMipres implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;
	private String url;
	private String nit;
	private String codigoHabilitacion;
	private String tokenPrincipal;
	private String tokenSecundario;
	private Date fechaAlta;	
	
	public ComTokenMipres() {		
	}

	@Id
	@Column(name = "id_token_mipres")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@NotEmpty
	@Column(name = "url")
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}	

	@NotEmpty
	@Column(name = "nit", length = 10)
	public String getNit() {
		return nit;
	}

	public void setNit(String nit) {
		this.nit = nit;
	}	

	@NotEmpty
	@Column(name = "codigo_habilitacion", length = 12)
	public String getCodigoHabilitacion() {
		return codigoHabilitacion;
	}

	public void setCodigoHabilitacion(String codigoHabilitacion) {
		this.codigoHabilitacion = codigoHabilitacion;
	}

	@NotEmpty
	@Column(name = "token_principal", length = 40)
	public String getTokenPrincipal() {
		return tokenPrincipal;
	}

	public void setTokenPrincipal(String tokenPrincipal) {
		this.tokenPrincipal = tokenPrincipal;
	}

	@Column(name = "token_secundario")
	public String getTokenSecundario() {
		return tokenSecundario;
	}

	public void setTokenSecundario(String tokenSecundario) {
		this.tokenSecundario = tokenSecundario;
	}

	@Column(name = "fecha_alta")
	public Date getFechaAlta() {
		return fechaAlta;
	}

	public void setFechaAlta(Date fechaAlta) {
		this.fechaAlta = fechaAlta;
	}	

}
