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
@Table(name = "ter_persona_juridica")
public class TerPersonaJuridica implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private Long idPersonaJuridica;
	private String razonSocial;
	private String registroMercantil;
	private Date fechaAct;
	private Date fechaAlta;
	private String loginUsr;
	//private TerTercero terTercero;
	//private List<TerClientePersJuridica> terClientePersJuridicas;
	//private List<TerProveedorPersJuridica> terProveedorPersJuridicas;
	
	@PrePersist
	public void prePersist() {
		fechaAct = new Date();
	}
	
	@Id
	@Column(name = "id_persona_juridica")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getIdPersonaJuridica() {
		return idPersonaJuridica;
	}

	public void setIdPersonaJuridica(Long idPersonaJuridica) {
		this.idPersonaJuridica = idPersonaJuridica;
	}

	@NotEmpty
	@Column(name = "razon_social")
	public String getRazonSocial() {
		return razonSocial;
	}

	public void setRazonSocial(String razonSocial) {
		this.razonSocial = razonSocial;
	}

	@Column(name = "registro_mercantil")
	public String getRegistroMercantil() {
		return registroMercantil;
	}

	public void setRegistroMercantil(String registroMercantil) {
		this.registroMercantil = registroMercantil;
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

	/*@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)//@ManyToOne SIGNIFICA MUCHAS PERSONAS JURIDICAS UN TERCERO
	@JoinColumn(name = "idTercero")
	public TerTercero getTerTercero() {
		return terTercero;
	}

	public void setTerTercero(TerTercero terTercero) {
		this.terTercero = terTercero;
	}*/	

}
