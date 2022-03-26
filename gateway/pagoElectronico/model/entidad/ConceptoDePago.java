package com.americacg.cargavirtual.gateway.pagoElectronico.model.entidad;

import java.util.Date;

import com.americacg.cargavirtual.gateway.pagoElectronico.model.ClaseBase;

public class ConceptoDePago extends ClaseBase {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2921857861219848060L;
	private Long id;
	private Date fechaDeAlta;
	private String descripcion;
	private String codMnemonico;
	private Integer estado;

	public ConceptoDePago() {

	}

	public ConceptoDePago(Long id) {
		this.id = id;
	}

	public ConceptoDePago(Long id, Date fechaDeAlta, String codMnemonico, String descripcion, Integer estado) {
		super();
		this.id = id;
		this.fechaDeAlta = fechaDeAlta;
		this.descripcion = descripcion;
		this.codMnemonico = codMnemonico;
		this.estado = estado;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getFechaDeAlta() {
		return fechaDeAlta;
	}

	public void setFechaDeAlta(Date fechaDeAlta) {
		this.fechaDeAlta = fechaDeAlta;
	}

	public String getCodMnemonico() {
		return codMnemonico;
	}

	public void setCodMnemonico(String codMnemonico) {
		this.codMnemonico = codMnemonico;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public Integer getEstado() {
		return estado;
	}

	public void setEstado(Integer estado) {
		this.estado = estado;
	}
}
