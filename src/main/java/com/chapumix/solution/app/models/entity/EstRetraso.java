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
@Table(name = "est_retraso")
public class EstRetraso implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;	
	private String observacion;
	private EstMortalidad estMortalidad;	
	private ComRetraso comRetraso;
	
	public EstRetraso() {		
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
	

	@Column(name = "observacion")
	public String getObservacion() {
		return observacion;
	}

	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}

	@ManyToOne(fetch = FetchType.LAZY, optional = false, cascade = CascadeType.ALL)		
	@JoinColumn(name = "id_mortalidad")
	public EstMortalidad getEstMortalidad() {
		return estMortalidad;
	}

	public void setEstMortalidad(EstMortalidad estMortalidad) {
		this.estMortalidad = estMortalidad;
	}	

	@ManyToOne(fetch = FetchType.LAZY, optional = false)	
	@JoinColumn(name = "id_retraso")
	public ComRetraso getComRetraso() {
		return comRetraso;
	}

	public void setComRetraso(ComRetraso comRetraso) {
		this.comRetraso = comRetraso;
	}

}
