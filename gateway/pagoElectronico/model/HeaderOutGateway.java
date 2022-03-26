package com.americacg.cargavirtual.gateway.pagoElectronico.model;

import java.io.Serializable;



public class HeaderOutGateway implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 761018714546370607L;

	private String codigoRetorno = null; 
	private String mensajeRetorno = null; 
	private String fechaServidor = null;
	private String idTransaccionCliente = null;
	private Long idConfiguracionComercio = null;
	
	public HeaderOutGateway(){
		
	}

	public String getCodigoRetorno() {
		return codigoRetorno;
	}

	public void setCodigoRetorno(String codigoRetorno) {
		this.codigoRetorno = codigoRetorno;
	}

	public String getMensajeRetorno() {
		return mensajeRetorno;
	}

	public void setMensajeRetorno(String mensajeRetorno) {
		this.mensajeRetorno = mensajeRetorno;
	}

	public String getFechaServidor() {
		return fechaServidor;
	}

	public void setFechaServidor(String fechaServidor) {
		this.fechaServidor = fechaServidor;
	}

	public String getIdTransaccionCliente() {
		return idTransaccionCliente;
	}

	public void setIdTransaccionCliente(String idTransaccionCliente) {
		this.idTransaccionCliente = idTransaccionCliente;
	}

	public Long getIdConfiguracionComercio() {
		return idConfiguracionComercio;
	}

	public void setIdConfiguracionComercio(Long idConfiguracionComercio) {
		this.idConfiguracionComercio = idConfiguracionComercio;
	}
}
