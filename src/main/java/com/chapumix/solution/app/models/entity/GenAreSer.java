package com.chapumix.solution.app.models.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "gen_are_ser")
public class GenAreSer implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;
	private Integer oid;
	private String gasCodigo;
	private String gasNombre;

	public GenAreSer() {
	}

	@Id
	@Column(name = "id_gen_are_ser")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@NotNull
	@Column(name = "oid", unique = true, nullable = false)
	public Integer getOid() {
		return oid;
	}

	public void setOid(Integer oid) {
		this.oid = oid;
	}

	@NotEmpty
	@Column(name = "gas_codigo", length = 20, nullable = false)
	public String getGasCodigo() {
		return gasCodigo;
	}

	public void setGasCodigo(String gasCodigo) {
		this.gasCodigo = gasCodigo;
	}

	@NotEmpty
	@Column(name = "gas_nombre", nullable = false)
	public String getGasNombre() {
		return gasNombre;
	}

	public void setGasNombre(String gasNombre) {
		this.gasNombre = gasNombre;
	}
}
