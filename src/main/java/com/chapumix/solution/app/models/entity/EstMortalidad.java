package com.chapumix.solution.app.models.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "est_mortalidad")
public class EstMortalidad implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;
	private Integer ingreso;
	private Date fechaIngreso;
	private Date fechaDefuncion;
	private String resumenCaso;
	private boolean codigoLila;
	private boolean codigoBlanco;
	private String escala;
	private String analisis;
	private String planMejora;
	private String accion;
	private String tiempo;
	private String responsable;
	private Date fechaRegistro;
	private String loginUsrAlta;
	private Date fechaAlta;
	private String loginUsrAct;
	private Date fechaAltaAct;	
	private GenPacien genPacien;
	private ComCie10 comCie10;
	private ComRegimen comRegimen;
	private ComTipoIngreso comTipoIngreso;
	private GenAreSer genAreSer;
	private ComEstadoHora comEstadoHora; 
	private ComApache comApache;
	private ComPrism comPrism;
	private List<EstAsistente> estAsistentes;
	private List<EstRetraso> estRetrasos;
	private List<EstCausa> estCausas;
	
	
	public EstMortalidad() {
		estAsistentes = new ArrayList<>();
	}
	
	@PrePersist
	public void prePersist() {		
		this.fechaAlta = new Date();
		this.fechaRegistro = new Date();
	}


	@Id
	@Column(name = "id_mortalidad")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}	

	@Column(name = "ingreso")
	public Integer getIngreso() {
		return ingreso;
	}

	public void setIngreso(Integer ingreso) {
		this.ingreso = ingreso;
	}
	
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss")
	@Column(name = "fecha_ingreso")
	public Date getFechaIngreso() {
		return fechaIngreso;
	}

	public void setFechaIngreso(Date fechaIngreso) {
		this.fechaIngreso = fechaIngreso;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss")
	@Column(name = "fecha_defuncion")	
	public Date getFechaDefuncion() {
		return fechaDefuncion;
	}

	public void setFechaDefuncion(Date fechaDefuncion) {
		this.fechaDefuncion = fechaDefuncion;
	}

	@Lob
	@Column(name = "resumen_caso")
	public String getResumenCaso() {
		return resumenCaso;
	}

	public void setResumenCaso(String resumenCaso) {
		this.resumenCaso = resumenCaso;
	}

	@Column(name = "codigo_lila", columnDefinition="BIT")
	public boolean isCodigoLila() {
		return codigoLila;
	}

	public void setCodigoLila(boolean codigoLila) {
		this.codigoLila = codigoLila;
	}	
	
	@Column(name = "codigo_blanco", columnDefinition="BIT")
	public boolean isCodigoBlanco() {
		return codigoBlanco;
	}

	public void setCodigoBlanco(boolean codigoBlanco) {
		this.codigoBlanco = codigoBlanco;
	}

	@NotEmpty
	@Column(name = "escala", length = 10)
	public String getEscala() {
		return escala;
	}

	public void setEscala(String escala) {
		this.escala = escala;
	}

	@Lob
	@Column(name = "analisis")
	public String getAnalisis() {
		return analisis;
	}

	public void setAnalisis(String analisis) {
		this.analisis = analisis;
	}

	@NotEmpty
	@Column(name = "plan_mejora", length = 10)
	public String getPlanMejora() {
		return planMejora;
	}

	public void setPlanMejora(String planMejora) {
		this.planMejora = planMejora;
	}	

	@Column(name = "accion")
	public String getAccion() {
		return accion;
	}

	public void setAccion(String accion) {
		this.accion = accion;
	}

	@Column(name = "tiempo")
	public String getTiempo() {
		return tiempo;
	}

	public void setTiempo(String tiempo) {
		this.tiempo = tiempo;
	}

	@Column(name = "responsable")
	public String getResponsable() {
		return responsable;
	}

	public void setResponsable(String responsable) {
		this.responsable = responsable;
	}
	
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "dd-MM-yyyy")
	@Column(name = "fecha_registro")
	public Date getFechaRegistro() {
		return fechaRegistro;
	}

	public void setFechaRegistro(Date fechaRegistro) {
		this.fechaRegistro = fechaRegistro;
	}

	@Column(name = "login_usr_alta")
	public String getLoginUsrAlta() {
		return loginUsrAlta;
	}

	public void setLoginUsrAlta(String loginUsrAlta) {
		this.loginUsrAlta = loginUsrAlta;
	}

	@Column(name = "fecha_alta")
	public Date getFechaAlta() {
		return fechaAlta;
	}

	public void setFechaAlta(Date fechaAlta) {
		this.fechaAlta = fechaAlta;
	}

	@Column(name = "login_usr_act")
	public String getLoginUsrAct() {
		return loginUsrAct;
	}

	public void setLoginUsrAct(String loginUsrAct) {
		this.loginUsrAct = loginUsrAct;
	}

	@Column(name = "fecha_act")
	public Date getFechaAltaAct() {
		return fechaAltaAct;
	}

	public void setFechaAltaAct(Date fechaAltaAct) {
		this.fechaAltaAct = fechaAltaAct;
	}

	@NotNull
	@Valid
	@ManyToOne
	@JoinColumn(name = "id_paciente", nullable = false, unique = true)
	public GenPacien getGenPacien() {
		return genPacien;
	}

	public void setGenPacien(GenPacien genPacien) {
		this.genPacien = genPacien;
	}	

	@ManyToOne
	@JoinColumn(name = "id_cie10", nullable = false)
	public ComCie10 getComCie10() {
		return comCie10;
	}

	public void setComCie10(ComCie10 comCie10) {
		this.comCie10 = comCie10;
	}

	@ManyToOne
	@JoinColumn(name = "id_regimen", nullable = false)
	public ComRegimen getComRegimen() {
		return comRegimen;
	}

	public void setComRegimen(ComRegimen comRegimen) {
		this.comRegimen = comRegimen;
	}

	@ManyToOne
	@JoinColumn(name = "id_tipo_ingreso", nullable = false)
	public ComTipoIngreso getComTipoIngreso() {
		return comTipoIngreso;
	}

	public void setComTipoIngreso(ComTipoIngreso comTipoIngreso) {
		this.comTipoIngreso = comTipoIngreso;
	}

	@NotNull
	@ManyToOne
	@JoinColumn(name = "id_servicio", nullable = false)
	public GenAreSer getGenAreSer() {
		return genAreSer;
	}

	public void setGenAreSer(GenAreSer genAreSer) {
		this.genAreSer = genAreSer;
	}

	@NotNull
	@ManyToOne
	@JoinColumn(name = "id_estado_hora", nullable = false)
	public ComEstadoHora getComEstadoHora() {
		return comEstadoHora;
	}

	public void setComEstadoHora(ComEstadoHora comEstadoHora) {
		this.comEstadoHora = comEstadoHora;
	}

	@ManyToOne
	@JoinColumn(name = "id_apache")
	public ComApache getComApache() {
		return comApache;
	}

	public void setComApache(ComApache comApache) {
		this.comApache = comApache;
	}	
	

	@ManyToOne
	@JoinColumn(name = "id_prism")	
	public ComPrism getComPrism() {
		return comPrism;
	}

	public void setComPrism(ComPrism comPrism) {
		this.comPrism = comPrism;
	}

	@OneToMany(mappedBy = "estMortalidad", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	public List<EstAsistente> getEstAsistentes() {
		return estAsistentes;
	}

	public void setEstAsistentes(List<EstAsistente> estAsistentes) {
		this.estAsistentes = estAsistentes;
	}

	@OneToMany(mappedBy = "estMortalidad", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	public List<EstRetraso> getEstRetrasos() {
		return estRetrasos;
	}

	public void setEstRetrasos(List<EstRetraso> estRetrasos) {
		this.estRetrasos = estRetrasos;
	}

	@OneToMany(mappedBy = "estMortalidad", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	public List<EstCausa> getEstCausas() {
		return estCausas;
	}

	public void setEstCausas(List<EstCausa> estCausas) {
		this.estCausas = estCausas;
	}
	
}
