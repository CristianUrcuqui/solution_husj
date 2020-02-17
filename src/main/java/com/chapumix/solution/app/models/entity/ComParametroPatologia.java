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

import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "com_parametro_patologia")
public class ComParametroPatologia implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;
	private String nombre;
	private Date fechaSolicitudInterno;
	private Date fechaSolicitudExterno;;
	
	public ComParametroPatologia() {		
	}
	

	@Id
	@Column(name = "id_parametro")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}	

	@Column(name="nombre")
	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	@Temporal(TemporalType.DATE)
	@DateTimeFormat (pattern="dd-MM-YYYY")
	@Column(name="fecha_solicitud_interno")
	public Date getFechaSolicitudInterno() {
		return fechaSolicitudInterno;
	}


	public void setFechaSolicitudInterno(Date fechaSolicitudInterno) {
		this.fechaSolicitudInterno = fechaSolicitudInterno;
	}


	@Temporal(TemporalType.DATE)
	@DateTimeFormat (pattern="dd-MM-YYYY")
	@Column(name="fecha_solicitud_externo")	
	public Date getFechaSolicitudExterno() {
		return fechaSolicitudExterno;
	}


	public void setFechaSolicitudExterno(Date fechaSolicitudExterno) {
		this.fechaSolicitudExterno = fechaSolicitudExterno;
	}	
	
}
