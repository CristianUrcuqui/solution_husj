package com.chapumix.solution.app.models.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "com_categoria_evento")
public class ComCategoriaEvento implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;
	private String nombre;
	private List<ComCatalogoEvento> comCatalogoEventos;
	
	
	public ComCategoriaEvento() {		
	}

	@Id
	@Column(name = "id_categoria_evento")
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
	
	
	@OneToMany(mappedBy = "comCategoriaEvento", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	public List<ComCatalogoEvento> getComCatalogoEventos() {
		return comCatalogoEventos;
	}

	public void setComCatalogoEventos(List<ComCatalogoEvento> comCatalogoEventos) {
		this.comCatalogoEventos = comCatalogoEventos;
	}
	

}
