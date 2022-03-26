package com.americacg.cargavirtual.sube.model.parametros.funciones.input;

import com.americacg.cargavirtual.web.helpers.LogACGHelper;
import com.americacg.cargavirtual.sube.model.parametros.SeparadorTrama;

public class DataInputFcn extends DataInputFcnGateway {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8378588969468698423L;
	private String tipo;
	private String version;
	private String lenData;
	private String idRed;
	
	public DataInputFcn(Long idProceso, boolean debug,
			SeparadorTrama separadorTrama, String socketData) {

		super(idProceso, debug, separadorTrama, socketData);
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getLenData() {
		return lenData;
	}

	public void setLenData(String lenData) {
		this.lenData = lenData;
	}

	public String getIdRed() {
		return idRed;
	}

	public void setIdRed(String idRed) {
		this.idRed = idRed;
	}

	//@Override
	public boolean parseData() {
		try{

			
		}
		catch(Exception e){
			LogACGHelper.escribirLog(this.getIdProceso(),
					"Error en parseo de datos de entrada (" + this.getClass().getSimpleName() + "): |" + e.getMessage() + "|");
			return false;
		}
		return true;
	}
	
	public String serializar(){
		StringBuilder oSB = new StringBuilder();
		
		oSB.append(this.getTipo());
		oSB.append(this.getVersion());
		oSB.append(this.getLenData());
		oSB.append(this.getIdRed());

		return oSB.toString();
	}

	public String serializarSinIDRed(){
		StringBuilder oSB = new StringBuilder();
		
		oSB.append(this.getTipo());
		oSB.append(this.getVersion());
		oSB.append(this.getLenData());

		return oSB.toString();
	}
}
