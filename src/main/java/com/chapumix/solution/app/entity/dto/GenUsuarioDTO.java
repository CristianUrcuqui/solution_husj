package com.chapumix.solution.app.entity.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GenUsuarioDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer oid;
	private Integer genRol;
	private String usuNombre;
	private String usuClave;
	private String usuDescri;
	private String usuEstado;
	

	public GenUsuarioDTO() {		
	}	

	public GenUsuarioDTO(Integer oid, String usuNombre, String usuDescri, String usuEstado) {		
		this.oid = oid;
		this.usuNombre = usuNombre;
		this.usuDescri = usuDescri;
		this.usuEstado = usuEstado;
	}

	public GenUsuarioDTO(Integer oid, Integer genRol, String usuNombre, String usuClave, String usuDescri,
			String usuEstado) {		
		this.oid = oid;
		this.genRol = genRol;
		this.usuNombre = usuNombre;
		this.usuClave = usuClave;
		this.usuDescri = usuDescri;
		this.usuEstado = usuEstado;
	}

	public Integer getOid() {
		return oid;
	}

	public void setOid(Integer oid) {
		this.oid = oid;
	}
	

	public Integer getGenRol() {
		return genRol;
	}

	public void setGenRol(Integer genRol) {
		this.genRol = genRol;
	}

	public String getUsuNombre() {
		return usuNombre;
	}

	public void setUsuNombre(String usuNombre) {
		this.usuNombre = usuNombre;
	}	

	public String getUsuClave() {
		return usuClave;
	}

	public void setUsuClave(String usuClave) {
		this.usuClave = usuClave;
	}

	public String getUsuDescri() {
		return usuDescri;
	}

	public void setUsuDescri(String usuDescri) {
		this.usuDescri = usuDescri;
	}

	public String getUsuEstado() {
		return usuEstado;
	}

	public void setUsuEstado(String usuEstado) {
		this.usuEstado = usuEstado;
	}	

}
