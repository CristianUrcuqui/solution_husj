package com.chapumix.solution.app.entity.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GenUsuarioDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer oid;
	private String usuNombre;
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



	public Integer getOid() {
		return oid;
	}

	public void setOid(Integer oid) {
		this.oid = oid;
	}

	public String getUsuNombre() {
		return usuNombre;
	}

	public void setUsuNombre(String usuNombre) {
		this.usuNombre = usuNombre;
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
