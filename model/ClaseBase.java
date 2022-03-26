package com.americacg.cargavirtual.sube.model;

import java.io.Serializable;

public abstract class ClaseBase implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2278146180784093737L;
	private Long idProceso = 0L;
	private boolean debug = false;
	private Integer campoAParsear;

	public ClaseBase() {
		campoAParsear = 0;
	}

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

	public Integer getCampoAParsear() {
		Integer campTmp = campoAParsear;
		campoAParsear++;
		return campTmp;
	}

	public Integer getCampoActual() {
		return this.campoAParsear;
	}

	public Integer getCampoParseado() {
		return this.campoAParsear - 1;
	}
}
