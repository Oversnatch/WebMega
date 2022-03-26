package com.americacg.cargavirtual.sube.model.parametros.funciones.output;

import com.americacg.cargavirtual.sube.model.GatewayHelper;
import com.americacg.cargavirtual.sube.model.parametros.SeparadorTrama;

public class DataOutputFcnGCAT extends DataOutputFcnGateway {	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3237959380645026129L;
	
	private String claveDeAutorizacion = null;
	

	public DataOutputFcnGCAT() {
		super();
	};

	public DataOutputFcnGCAT(SeparadorTrama separadorTrama, String data) throws Exception{
		this.setSeparadorTrama(separadorTrama);
		
		try{
			
			String[] camposData =GatewayHelper.splitData(null, false, this.getSeparadorTrama().getSeparadorTrama(), data);
			
			if(!(camposData.length > 0)){
				throw new Exception("El data no contiene el campo Clave De Autorizacion requerido.");
			}
			
			try {
				this.setClaveDeAutorizacion(camposData[this.getCampoAParsear()]);
			} catch (Exception e) {
				throw new Exception("El data no contiene los campo requeridos.");
			}
		}
		catch(Exception e){
			throw new Exception("Error en codigo retorno para obtener Clave de Autorización");
		}
	};

	@Override
	public String obtenerTramaPOS() {
		StringBuilder oSB = new StringBuilder();

		oSB.append(this.getCodRetornoSUBE());
		oSB.append(this.getSeparadorTrama().getSeparadorTrama());
		return oSB.toString();
	}

	public String getClaveDeAutorizacion() {
		return claveDeAutorizacion;
	}

	public void setClaveDeAutorizacion(String claveDeAutorizacion) {
		this.claveDeAutorizacion = claveDeAutorizacion;
	}
}
