package com.americacg.cargavirtual.gateway.pagoElectronico.model.entidad;

import java.io.Serializable;

import com.americacg.cargavirtual.gateway.pagoElectronico.model.ClaseBase;

public class Moneda extends ClaseBase implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5969550365830627645L;
	private String id;
	private String descripcion;
	private String pais;
	private String codNumISO;
	private String simbolo;

	public Moneda() {

	}

	public Moneda(String idMoneda) {
		this.id = idMoneda;
	}

	public Moneda(String idMoneda, String descripcion, String pais, String codNumISO) {
		super();
		this.id = idMoneda;
		this.descripcion = descripcion;
		this.pais = pais;
		this.codNumISO = codNumISO;
	}

	public String getId() {
		return id;
	}

	public void setId(String idMoneda) {
		this.id = idMoneda;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getPais() {
		return pais;
	}

	public void setPais(String pais) {
		this.pais = pais;
	}

	public String getCodNumISO() {
		return codNumISO;
	}

	public void setCodNumISO(String codNumISO) {
		this.codNumISO = codNumISO;
	}

	public String getSimbolo() {
		return simbolo;
	}

	public void setSimbolo(String simbolo) {
		this.simbolo = simbolo;
	}
}
