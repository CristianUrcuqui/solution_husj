package com.chapumix.solution.app.models.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "est_causa")
public class EstCausa implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;
	private ComCie10 causaDirecta;
	private ComCie10 causaAntedecente;
	private String otraCausaA;
	private String otraCausaB;
	private String otros;
	private EstMortalidad estMortalidad;	
	
	public EstCausa() {		
	}

	@Id
	@Column(name = "id_causa")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	

	@ManyToOne
	@JoinColumn(name = "id_mortalidad", nullable = false)
	public EstMortalidad getEstMortalidad() {
		return estMortalidad;
	}

	public void setEstMortalidad(EstMortalidad estMortalidad) {
		this.estMortalidad = estMortalidad;
	}

	@ManyToOne
	@JoinColumn(name = "causa_directa", nullable = false)
	public ComCie10 getCausaDirecta() {
		return causaDirecta;
	}

	public void setCausaDirecta(ComCie10 causaDirecta) {
		this.causaDirecta = causaDirecta;
	}

	@ManyToOne
	@JoinColumn(name = "causa_antecedente")
	public ComCie10 getCausaAntedecente() {
		return causaAntedecente;
	}

	public void setCausaAntedecente(ComCie10 causaAntedecente) {
		this.causaAntedecente = causaAntedecente;
	}

	@Column(name = "otra_causa_a")
	public String getOtraCausaA() {
		return otraCausaA;
	}

	public void setOtraCausaA(String otraCausaA) {
		this.otraCausaA = otraCausaA;
	}

	@Column(name = "otra_causa_b")
	public String getOtraCausaB() {
		return otraCausaB;
	}

	public void setOtraCausaB(String otraCausaB) {
		this.otraCausaB = otraCausaB;
	}

	@Column(name = "otros")
	public String getOtros() {
		return otros;
	}

	public void setOtros(String otros) {
		this.otros = otros;
	}

}
