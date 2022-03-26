package com.americacg.cargavirtual.gateway.pagoElectronico.model.entidad;

import com.americacg.cargavirtual.gateway.pagoElectronico.model.ClaseBase;

public class UsuarioPagoElectronico extends ClaseBase {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3485374316565144482L;
	private Long id;
	private String usuario;
	
	public UsuarioPagoElectronico(Long id) {
		this.id = id;
	}

	public UsuarioPagoElectronico() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}
}
