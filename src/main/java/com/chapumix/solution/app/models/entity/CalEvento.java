package com.chapumix.solution.app.models.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "cal_evento")
public class CalEvento implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;
	private ComUsuario comUsuario;
	private ComProfesion comProfesion;		
	private ComCatalogoEvento comCatalogoEvento;
	
	public CalEvento() {		
	}

	@Id
	@Column(name = "id_evento")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}		
	
	@ManyToOne
	@JoinColumn(name = "id_usuario_realiza")
	public ComUsuario getComUsuario() {
		return comUsuario;
	}

	public void setComUsuario(ComUsuario comUsuario) {
		this.comUsuario = comUsuario;
	}

	@NotNull
	@ManyToOne
	@JoinColumn(name = "id_profesion_realiza", nullable = false)
	public ComProfesion getComProfesion() {
		return comProfesion;
	}

	public void setComProfesion(ComProfesion comProfesion) {
		this.comProfesion = comProfesion;
	}

	@ManyToOne(fetch = FetchType.LAZY, optional = false)	
	@JoinColumn(name = "id_catalogo_evento")
	public ComCatalogoEvento getComCatalogoEvento() {
		return comCatalogoEvento;
	}

	public void setComCatalogoEvento(ComCatalogoEvento comCatalogoEvento) {
		this.comCatalogoEvento = comCatalogoEvento;
	}		

}
