package com.americacg.cargavirtual.sube.model.parametros.funciones.output;

import com.americacg.cargavirtual.sube.model.parametros.SeparadorTrama;

public class DataOutputFcnVCAT extends DataOutputFcnGateway {	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3237959380645026129L;

	public DataOutputFcnVCAT() {
	};

	public DataOutputFcnVCAT(SeparadorTrama separadorTrama, String dataLG) throws Exception{
		//String codRetSUBE = "";
		this.setSeparadorTrama(separadorTrama);
		
		try{
			/*
			if(dataLG.length() > 4){
				codRetSUBE = dataLG.substring(0, 4);
				codRetSUBE = GatewayHelper.strHexToLittleEndian(codRetSUBE, 4);
				
				this.setCodRetornoSUBE(Integer.decode("0x"+codRetSUBE));
			}
			else
				throw new Exception("Error en retorno funcion GCAT.");
				*/
		}
		catch(Exception e){
			throw new Exception("Error en codigo retorno SUBE");
		}
	};

	@Override
	public String obtenerTramaPOS() {
		StringBuilder oSB = new StringBuilder();

		oSB.append(this.getCodRetornoSUBE());
		oSB.append(this.getSeparadorTrama().getSeparadorTrama());
		return oSB.toString();
	}
}
