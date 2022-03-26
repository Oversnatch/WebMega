package com.americacg.cargavirtual.gateway.pagoElectronico.model;

import java.io.Serializable;

public abstract class ClaseBase implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2278146180784093737L;
	protected Long idProceso = 0L;
	protected boolean debug = false;
	
	public Long getIdProceso() {
		return idProceso;
	}

	public void setIdProceso(Long idProceso) {
		this.idProceso = idProceso;
	}

	public boolean isDebug() {
		return debug;
	}

	public void setDebug(boolean debug) {
		this.debug = debug;
	}
}
