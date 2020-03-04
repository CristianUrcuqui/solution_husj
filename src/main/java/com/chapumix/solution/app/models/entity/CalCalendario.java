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
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "cal_calendario")
public class CalCalendario implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;
	private String nombreCompleto;
	private String numeroIdentificacion;
	private Date fechaNacimiento;
	private String correo;
	private Boolean enviado;
	
	public CalCalendario() {		
	}
	
	@PrePersist
	public void prePersist() {
		this.enviado = false;		
	}
	

	@Id
	@Column(name = "id_usuario")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	@NotEmpty
	@Column(name = "nombre_completo")
	public String getNombreCompleto() {
		return nombreCompleto;
	}


	public void setNombreCompleto(String nombreCompleto) {
		this.nombreCompleto = nombreCompleto;
	}
	
	@NotEmpty
	@Column(name = "numero_identificacion", unique = true)
	public String getNumeroIdentificacion() {
		return numeroIdentificacion;
	}


	public void setNumeroIdentificacion(String numeroIdentificacion) {
		this.numeroIdentificacion = numeroIdentificacion;
	}	


	@NotNull
	@DateTimeFormat(pattern = "dd-mm-yyyy")
	@Temporal(TemporalType.DATE)
	@Column(name = "fecha_nacimiento")
	public Date getFechaNacimiento() {
		return fechaNacimiento;
	}


	public void setFechaNacimiento(Date fechaNacimiento) {
		this.fechaNacimiento = fechaNacimiento;
	}

	@NotEmpty
	@Email
	@Column(name = "correo")
	public String getCorreo() {
		return correo;
	}


	public void setCorreo(String correo) {
		this.correo = correo;
	}

	
	@Column(name = "enviado")
	public Boolean getEnviado() {
		return enviado;
	}


	public void setEnviado(Boolean enviado) {
		this.enviado = enviado;
	}	
	

}
