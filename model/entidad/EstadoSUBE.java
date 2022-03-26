package com.americacg.cargavirtual.sube.model.entidad;

import java.io.Serializable;

public abstract class EstadoSUBE implements Serializable  {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8617189318118876963L;
	private Long id;
	private String descripcion;
	
	public EstadoSUBE(){};
	
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
