package com.chapumix.solution.app.models.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "gen_pacien")
public class GenPacien implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;
	private Integer oid;	
	private String pacNumDoc;
	private String pacPriNom;
	private String pacSegNom;
	private String pacPriApe;
	private String pacSegApe;
	private Date gpafecnac;
	private ComTipoDocumento comTipoDocumento;
	private ComGenero comGenero;
	
	public GenPacien() {		
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_paciente")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	
	@Column(name = "oid")
	public Integer getOid() {
		return oid;
	}

	public void setOid(Integer oid) {
		this.oid = oid;
	}	

	@Column(name = "pac_num_doc", length = 20)
	public String getPacNumDoc() {
		return pacNumDoc;
	}

	public void setPacNumDoc(String pacNumDoc) {
		this.pacNumDoc = pacNumDoc;
	}

	@Column(name = "pac_pri_nom", length = 30)
	public String getPacPriNom() {
		return pacPriNom;
	}

	public void setPacPriNom(String pacPriNom) {
		this.pacPriNom = pacPriNom;
	}

	@Column(name = "pac_seg_nom", length = 30)
	public String getPacSegNom() {
		return pacSegNom;
	}

	public void setPacSegNom(String pacSegNom) {
		this.pacSegNom = pacSegNom;
	}

	@Column(name = "pac_pri_ape", length = 30)
	public String getPacPriApe() {
		return pacPriApe;
	}

	public void setPacPriApe(String pacPriApe) {
		this.pacPriApe = pacPriApe;
	}

	@Column(name = "pac_seg_ape", length = 30)
	public String getPacSegApe() {
		return pacSegApe;
	}

	public void setPacSegApe(String pacSegApe) {
		this.pacSegApe = pacSegApe;
	}	

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="gpa_fec_nac")
	public Date getGpafecnac() {
		return gpafecnac;
	}

	public void setGpafecnac(Date gpafecnac) {
		this.gpafecnac = gpafecnac;
	}	

	@ManyToOne
	@JoinColumn(name = "id_tipo_documento", nullable = false)
	public ComTipoDocumento getComTipoDocumento() {
		return comTipoDocumento;
	}

	public void setComTipoDocumento(ComTipoDocumento comTipoDocumento) {
		this.comTipoDocumento = comTipoDocumento;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_genero", nullable = false)
	public ComGenero getComGenero() {
		return comGenero;
	}

	public void setComGenero(ComGenero comGenero) {
		this.comGenero = comGenero;
	}
	
}





