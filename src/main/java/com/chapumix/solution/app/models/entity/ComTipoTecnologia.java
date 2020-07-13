package com.chapumix.solution.app.models.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "com_tipo_tecnologia")
public class ComTipoTecnologia implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;
	private String tipo;
	private String descripcion;
	
	public ComTipoTecnologia() {		
	}

	@Id
	@Column(name = "id_tipo_tecnologia")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "tipo", length = 4)
	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	@Column(name = "descripcion", length = 40)
	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

}
