package com.americacg.cargavirtual.sube.model.parametros.funciones.input;

import com.americacg.cargavirtual.sube.model.ClaseBase;
import com.americacg.cargavirtual.sube.model.parametros.SeparadorTrama;

public abstract class DataInputFcnGateway extends ClaseBase {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4006886474407428086L;
	private SeparadorTrama separadorTrama;
	private String data;
	private String lgSerie;
	private String samId;
	private String posId;
	private Long idTerna;
	private Long idProveedorLG;
	private String idUbicacionNacion;

	public DataInputFcnGateway(Long idProceso, boolean debug, SeparadorTrama separadorTrama, String socketData){
		super();
		this.setData(socketData);
		this.setSeparadorTrama(separadorTrama);
		this.setIdProceso(idProceso);
		this.setDebug(debug);
	}
	
	public String getData() {
		return data;
	}
	
	public void setData(String data) {
		this.data = data;
	}
	
	public SeparadorTrama getSeparadorTrama() {
		return separadorTrama;
	}

	public void setSeparadorTrama(SeparadorTrama separadorTrama) {
		this.separadorTrama = separadorTrama;
	}

	public Long getIdTerna() {
		return idTerna;
	}

	public void setIdTerna(Long idTerna) {
		this.idTerna = idTerna;
	}

	public Long getIdProveedorLG() {
		return idProveedorLG;
	}

	public void setIdProveedorLG(Long idProveedorLG) {
		this.idProveedorLG = idProveedorLG;
	}

	public String getIdUbicacionNacion() {
		return idUbicacionNacion;
	}

	public void setIdUbicacionNacion(String idUbicacionNacion) {
		this.idUbicacionNacion = idUbicacionNacion;
	}

	public String getLgSerie() {
		return lgSerie;
	}

	public void setLgSerie(String lgSerie) {
		this.lgSerie = lgSerie;
	}

	public String getSamId() {
		return samId;
	}

	public void setSamId(String samId) {
		this.samId = samId;
	}

	public String getPosId() {
		return posId;
	}

	public void setPosId(String posId) {
		this.posId = posId;
	}
	
	public boolean parseData() {
		return true;
	}
	
}
