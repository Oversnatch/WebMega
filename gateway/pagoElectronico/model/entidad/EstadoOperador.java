package com.americacg.cargavirtual.gateway.pagoElectronico.model.entidad;

import com.americacg.cargavirtual.gateway.pagoElectronico.model.ClaseBase;

public class EstadoOperador extends ClaseBase {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long id;
	private Operador operador;
	private String codigoOperador;
	private String descripcionOperador;
	private String codigoACG;
	private String descripcionCortaACG;
	private String descripcionLargaACG;
	private Integer estado;
	
	public EstadoOperador() {
	}

	public EstadoOperador(Long id, String descripcionLarga) {
		this.id = id;
		this.descripcionLargaACG = descripcionLarga;
	}

	public EstadoOperador(Long id) {
		this.id = id;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Operador getOperador() {
		return operador;
	}

	public void setOperador(Operador operador) {
		this.operador = operador;
	}

	public String getCodigoOperador() {
		return codigoOperador;
	}

	public void setCodigoOperador(String codigoOperador) {
		this.codigoOperador = codigoOperador;
	}

	public String getDescripcionOperador() {
		return descripcionOperador;
	}

	public void setDescripcionOperador(String descripcionOperador) {
		this.descripcionOperador = descripcionOperador;
	}

	public String getCodigoACG() {
		return codigoACG;
	}

	public void setCodigoACG(String codigoACG) {
		this.codigoACG = codigoACG;
	}

	public String getDescripcionCortaACG() {
		return descripcionCortaACG;
	}

	public void setDescripcionCortaACG(String descripcionCortaACG) {
		this.descripcionCortaACG = descripcionCortaACG;
	}

	public String getDescripcionLargaACG() {
		return descripcionLargaACG;
	}

	public void setDescripcionLargaACG(String descripcionLargaACG) {
		this.descripcionLargaACG = descripcionLargaACG;
	}

	public Integer getEstado() {
		return estado;
	}

	public void setEstado(Integer estado) {
		this.estado = estado;
	}
}

