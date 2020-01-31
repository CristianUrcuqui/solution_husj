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
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "com_usuario")
public class ComUsuario implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;
	private String usuario;
	private String contrasena;
	private String nombreCompleto;
	private String nombreCorto;
	private Boolean estado;
	private List<ComRole> roles;
	
	public ComUsuario() {		
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

	@NotNull
	@Column(name = "usuario", length = 30, unique = true)	
	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	@NotNull
	@Column(name = "contrasena", length = 60)
	public String getContrasena() {
		return contrasena;
	}

	public void setContrasena(String contrasena) {
		this.contrasena = contrasena;
	}
	
	
	@NotNull
	@Column(name = "nombre_completo", length = 250)
	public String getNombreCompleto() {
		return nombreCompleto;
	}


	public void setNombreCompleto(String nombreCompleto) {
		this.nombreCompleto = nombreCompleto;
	}
	
	@NotNull
	@Column(name = "nombre_corto", length = 250)
	public String getNombreCorto() {
		return nombreCorto;
	}


	public void setNombreCorto(String nombreCorto) {
		this.nombreCorto = nombreCorto;
	}

	@NotNull	
	@Column(name = "estado", columnDefinition="BIT")
	public Boolean getEstado() {
		return estado;
	}

	public void setEstado(Boolean estado) {
		this.estado = estado;
	}

	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)//Esto significa que un usuario puede tener muchos roles
	@JoinColumn(name = "usuarios_id")
	public List<ComRole> getRoles() {
		return roles;
	}

	public void setRoles(List<ComRole> roles) {
		this.roles = roles;
	}	

}
