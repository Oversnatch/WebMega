package com.americacg.cargavirtual.gateway.pagoElectronico.model.entidad;

import java.io.Serializable;

public class Identificacion implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -3855765034263696921L;
	private String tipo;
	private String numero;
	
	public Identificacion() {
	}

	public Identificacion(String tipo, String numero) {
		super();
		this.tipo = tipo;
		this.numero = numero;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}
}
