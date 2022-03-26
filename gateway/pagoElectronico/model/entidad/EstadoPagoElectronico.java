package com.americacg.cargavirtual.gateway.pagoElectronico.model.entidad;

import java.io.Serializable;

import com.americacg.cargavirtual.gateway.pagoElectronico.model.ClaseBase;

public class EstadoPagoElectronico extends ClaseBase implements Serializable  {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8617189318118876963L;
	private Long id;
	private String descripcion;
	
	public EstadoPagoElectronico(){}

	public EstadoPagoElectronico(Long idEstado){
		this.id = idEstado;
	}

	public EstadoPagoElectronico(Long idEstado, String descripcion){
		this.id = idEstado;
		this.descripcion = descripcion;
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
}
