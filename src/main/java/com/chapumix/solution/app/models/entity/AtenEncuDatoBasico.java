package com.chapumix.solution.app.models.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "aten_encu_dato_basico")
public class AtenEncuDatoBasico implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;
	private GenAreSer genAreSer;
	private ComGenero comGenero;
	private String edad;	
	private String respuesta1;	
	private String respuesta2;	
	private String respuesta3;	
	private String respuesta4;	
	private String respuesta5;	
	private String respuesta6;	
	private String respuesta7;	
	private String respuesta8;	
	private String respuesta9;	
	private String respuesta10;	
	private String respuesta11;
	private String justificacion11;	
	private String respuesta12;
	private String justificacion12;
	private Date fechaRegistro;
	private String loginUsrAlta;
	private Date fechaAlta;
	private String loginUsrAct;
	private Date fechaAltaAct;
	
	
	public AtenEncuDatoBasico() {
		this.fechaAlta = new Date();		
	}


	@Id
	@Column(name = "id_dato_basico")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
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
	@JoinColumn(name = "id_genero", nullable = false)
	public ComGenero getComGenero() {
		return comGenero;
	}


	public void setComGenero(ComGenero comGenero) {
		this.comGenero = comGenero;
	}
	
	@NotEmpty
	@Column(name = "edad", length = 10, nullable = false)
	public String getEdad() {
		return edad;
	}


	public void setEdad(String edad) {
		this.edad = edad;
	}

	@NotEmpty
	@Column(name = "respuesta1", nullable = false)
	public String getRespuesta1() {
		return respuesta1;
	}


	public void setRespuesta1(String respuesta1) {
		this.respuesta1 = respuesta1;
	}	


	@NotEmpty
	@Column(name = "respuesta2", nullable = false)
	public String getRespuesta2() {
		return respuesta2;
	}


	public void setRespuesta2(String respuesta2) {
		this.respuesta2 = respuesta2;
	}	


	@NotEmpty
	@Column(name = "respuesta3", nullable = false)
	public String getRespuesta3() {
		return respuesta3;
	}


	public void setRespuesta3(String respuesta3) {
		this.respuesta3 = respuesta3;
	}
	

	@NotEmpty
	@Column(name = "respuesta4", nullable = false)
	public String getRespuesta4() {
		return respuesta4;
	}


	public void setRespuesta4(String respuesta4) {
		this.respuesta4 = respuesta4;
	}	


	@NotEmpty
	@Column(name = "respuesta5", nullable = false)
	public String getRespuesta5() {
		return respuesta5;
	}


	public void setRespuesta5(String respuesta5) {
		this.respuesta5 = respuesta5;
	}	


	@NotEmpty
	@Column(name = "respuesta6", nullable = false)
	public String getRespuesta6() {
		return respuesta6;
	}


	public void setRespuesta6(String respuesta6) {
		this.respuesta6 = respuesta6;
	}


	@NotEmpty
	@Column(name = "respuesta7", nullable = false)
	public String getRespuesta7() {
		return respuesta7;
	}


	public void setRespuesta7(String respuesta7) {
		this.respuesta7 = respuesta7;
	}

	@NotEmpty
	@Column(name = "respuesta8", nullable = false)
	public String getRespuesta8() {
		return respuesta8;
	}


	public void setRespuesta8(String respuesta8) {
		this.respuesta8 = respuesta8;
	}	


	@NotEmpty
	@Column(name = "respuesta9", nullable = false)
	public String getRespuesta9() {
		return respuesta9;
	}


	public void setRespuesta9(String respuesta9) {
		this.respuesta9 = respuesta9;
	}
	

	@NotEmpty
	@Column(name = "respuesta10", nullable = false)
	public String getRespuesta10() {
		return respuesta10;
	}


	public void setRespuesta10(String respuesta10) {
		this.respuesta10 = respuesta10;
	}	


	@NotEmpty
	@Column(name = "respuesta11", nullable = false)
	public String getRespuesta11() {
		return respuesta11;
	}


	public void setRespuesta11(String respuesta11) {
		this.respuesta11 = respuesta11;
	}

	@NotEmpty
	@Lob
	@Column(name = "justificacion11", nullable = false)
	public String getJustificacion11() {
		return justificacion11;
	}


	public void setJustificacion11(String justificacion11) {
		this.justificacion11 = justificacion11;
	}
	

	@NotEmpty
	@Column(name = "respuesta12", nullable = false)
	public String getRespuesta12() {
		return respuesta12;
	}


	public void setRespuesta12(String respuesta12) {
		this.respuesta12 = respuesta12;
	}	
	
	@NotEmpty
	@Lob
	@Column(name = "justificacion12", nullable = false)
	public String getJustificacion12() {
		return justificacion12;
	}

	public void setJustificacion12(String justificacion12) {
		this.justificacion12 = justificacion12;
	}

	
	@NotNull
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "dd-MM-yyyy")
	@Column(name = "fecha_registro", nullable = false)
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
	
}
