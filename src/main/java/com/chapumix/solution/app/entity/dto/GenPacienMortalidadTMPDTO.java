package com.chapumix.solution.app.entity.dto;

import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GenPacienMortalidadTMPDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private String pacNumDoc;	
	private String pacNombre;
	private String pacApellido;
	private String ingreso;
	private String edad;
	private String genero;
	private String municipio;
	private String regimen;
	private String tipoIngreso;
	private String diagnostico;
	private String entidad;
	private String fechaIngreso;
	private String fechaDefuncion;
	
	
	
	public GenPacienMortalidadTMPDTO() {		
	}

	

	public GenPacienMortalidadTMPDTO(String pacNumDoc, String pacNombre, String pacApellido, String ingreso,
			String edad, String genero, String municipio, String regimen, String tipoIngreso, String diagnostico,
			String entidad, String fechaIngreso, String fechaDefuncion) {		
		this.pacNumDoc = pacNumDoc;
		this.pacNombre = pacNombre;
		this.pacApellido = pacApellido;
		this.ingreso = ingreso;
		this.edad = edad;
		this.genero = genero;
		this.municipio = municipio;
		this.regimen = regimen;
		this.tipoIngreso = tipoIngreso;
		this.diagnostico = diagnostico;
		this.entidad = entidad;
		this.fechaIngreso = fechaIngreso;
		this.fechaDefuncion = fechaDefuncion;
	}



	public String getPacNumDoc() {
		return pacNumDoc;
	}


	public void setPacNumDoc(String pacNumDoc) {
		this.pacNumDoc = pacNumDoc;
	}


	public String getPacNombre() {
		return pacNombre;
	}


	public void setPacNombre(String pacNombre) {
		this.pacNombre = pacNombre;
	}


	public String getPacApellido() {
		return pacApellido;
	}


	public void setPacApellido(String pacApellido) {
		this.pacApellido = pacApellido;
	}


	public String getIngreso() {
		return ingreso;
	}


	public void setIngreso(String ingreso) {
		this.ingreso = ingreso;
	}


	public String getEdad() {
		return edad;
	}


	public void setEdad(String edad) {
		this.edad = edad;
	}


	public String getGenero() {
		return genero;
	}


	public void setGenero(String genero) {
		this.genero = genero;
	}


	public String getMunicipio() {
		return municipio;
	}


	public void setMunicipio(String municipio) {
		this.municipio = municipio;
	}


	public String getRegimen() {
		return regimen;
	}


	public void setRegimen(String regimen) {
		this.regimen = regimen;
	}


	public String getTipoIngreso() {
		return tipoIngreso;
	}


	public void setTipoIngreso(String tipoIngreso) {
		this.tipoIngreso = tipoIngreso;
	}


	public String getDiagnostico() {
		return diagnostico;
	}


	public void setDiagnostico(String diagnostico) {
		this.diagnostico = diagnostico;
	}


	public String getEntidad() {
		return entidad;
	}


	public void setEntidad(String entidad) {
		this.entidad = entidad;
	}



	public String getFechaIngreso() {
		return fechaIngreso;
	}



	public void setFechaIngreso(String fechaIngreso) {
		this.fechaIngreso = fechaIngreso;
	}



	public String getFechaDefuncion() {
		return fechaDefuncion;
	}



	public void setFechaDefuncion(String fechaDefuncion) {
		this.fechaDefuncion = fechaDefuncion;
	}
			
}