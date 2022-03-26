package com.americacg.cargavirtual.gateway.pagoElectronico.model.entidad;

import java.util.Date;

import com.americacg.cargavirtual.gateway.pagoElectronico.model.ClaseBase;

public class BancoPagoElectronico extends ClaseBase {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2991669600980744454L;
	private Long id;
	private Date fechaAlta;
	private String idBancoBCRA;
	private String denominacion;
	private Integer estado;

	public BancoPagoElectronico() {

	}

	public BancoPagoElectronico(Long id) {
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

	public String getIdBancoBCRA() {
		return idBancoBCRA;
	}

	public void setIdBancoBCRA(String idBancoBCRA) {
		this.idBancoBCRA = idBancoBCRA;
	}

	public String getDenominacion() {
		return denominacion;
	}

	public void setDenominacion(String denominacion) {
		this.denominacion = denominacion;
	}

	public Integer getEstado() {
		return estado;
	}

	public void setEstado(Integer estado) {
		this.estado = estado;
	}

}
