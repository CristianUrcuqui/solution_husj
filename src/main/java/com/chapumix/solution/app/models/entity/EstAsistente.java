package com.chapumix.solution.app.models.entity;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "est_asistente")
public class EstAsistente implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;
	private String nombre;
	private String actividad;
	private EstMortalidad estMortalidad;
	
	public EstAsistente() {		
	}

	@Id
	@Column(name = "id_asistente")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "nombre", length = 100)
	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	@Column(name = "actividad", length = 100)
	public String getActividad() {
		return actividad;
	}

	public void setActividad(String actividad) {
		this.actividad = actividad;
	}
	
	@ManyToOne(fetch = FetchType.LAZY, optional = false, cascade = CascadeType.ALL)	
	@JoinColumn(name = "id_mortalidad")
	public EstMortalidad getEstMortalidad() {
		return estMortalidad;
	}

	public void setEstMortalidad(EstMortalidad estMortalidad) {
		this.estMortalidad = estMortalidad;
	}
	

}
