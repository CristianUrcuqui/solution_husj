package com.chapumix.solution.app.models.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "com_estado_tramite")
public class ComEstadoTramite implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;	
	private String descripcion;
	
	public ComEstadoTramite() {		
	}

	@Id
	@Column(name = "id_estado_tramite")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}	

	@Column(name = "descripcion")
	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}	

}
