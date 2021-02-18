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

@Entity
@Table(name = "com_catalogo_evento")
public class ComCatalogoEvento implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;
	private String nombre;	
	private ComTipoEvento  comTipoEvento;
	private ComCategoriaEvento comCategoriaEvento;
	
	
	
	public ComCatalogoEvento() {		
	}

	@Id
	@Column(name = "id_catalogo_evento")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "nombre")
	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	@ManyToOne(fetch = FetchType.LAZY, optional = false)	
	@JoinColumn(name = "id_tipo_evento")
	public ComTipoEvento getComTipoEvento() {
		return comTipoEvento;
	}

	public void setComTipoEvento(ComTipoEvento comTipoEvento) {
		this.comTipoEvento = comTipoEvento;
	}

	@ManyToOne(fetch = FetchType.LAZY, optional = false)	
	@JoinColumn(name = "id_categoria_evento")
	public ComCategoriaEvento getComCategoriaEvento() {
		return comCategoriaEvento;
	}

	public void setComCategoriaEvento(ComCategoriaEvento comCategoriaEvento) {
		this.comCategoriaEvento = comCategoriaEvento;
	}
	

}
