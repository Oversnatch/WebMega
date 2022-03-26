package com.americacg.cargavirtual.sube.model;

import java.io.Serializable;

public class ErrorGateway implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7256683117455144589L;
	private boolean hayError = false;
	private String codigoError;
	private String msgError;

	public ErrorGateway() {

	}

	public ErrorGateway(boolean hayError, String codigoError, String msgError) {
		super();
		this.hayError = hayError;
		this.codigoError = codigoError;
		this.msgError = msgError;
	}

	public boolean isHayError() {
		return hayError;
	}

	public void setHayError(boolean hayError) {
		this.hayError = hayError;
	}

	public String getCodigoError() {
		return codigoError;
	}

	public void setCodigoError(String codigoError) {
		this.codigoError = codigoError;
	}

	public String getMsgError() {
		return msgError;
	}

	public void setMsgError(String msgError) {
		this.msgError = msgError;
	}
	
	public void setError(String codigoError, String msgError){
		this.setCodigoError(codigoError);
		this.setMsgError(msgError);
		this.setHayError(true);
	}
	
	
}