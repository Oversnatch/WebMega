package com.americacg.cargavirtual.gateway.pagoElectronico.model.entidad;

import java.sql.Time;
import java.util.Date;

import com.americacg.cargavirtual.gateway.pagoElectronico.model.ClaseBase;

public class Operador extends ClaseBase {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7984490557597180258L;
	private Long id;
	private Date fechaAlta;
	private String descripcion;
	private Time horaInicioOperacion;
	private Time horaFinOperacion;
	private String mensajeInhabilitacion;
	private Integer estado;
	
	public Operador() {		
	}

	public Operador(Long id) {
		this.id = id;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getFechaAlta() {
		return fechaAlta;
	}

	public void setFechaAlta(Date fechaAlta) {
		this.fechaAlta = fechaAlta;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public Time getHoraInicioOperacion() {
		return horaInicioOperacion;
	}

	public void setHoraInicioOperacion(Time horaInicioOperacion) {
		this.horaInicioOperacion = horaInicioOperacion;
	}

	public Time getHoraFinOperacion() {
		return horaFinOperacion;
	}

	public void setHoraFinOperacion(Time horaFinOperacion) {
		this.horaFinOperacion = horaFinOperacion;
	}

	public String getMensajeInhabilitacion() {
		return mensajeInhabilitacion;
	}

	public void setMensajeInhabilitacion(String mensajeInhabilitacion) {
		this.mensajeInhabilitacion = mensajeInhabilitacion;
	}

	public Integer getEstado() {
		return estado;
	}

	public void setEstado(Integer estado) {
		this.estado = estado;
	}
}
