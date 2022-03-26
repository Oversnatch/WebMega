package com.americacg.cargavirtual.sube.model;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.americacg.cargavirtual.web.helpers.LogACGHelper;
import com.americacg.cargavirtual.sube.model.entidad.EstadoTerna;
import com.americacg.cargavirtual.sube.model.parametros.SeparadorTrama;
import com.americacg.cargavirtual.sube.model.parametros.funciones.output.DataOutputFcnGateway;

public class MensajeOutboundGateway extends ClaseBase {
	/**
	 * 
	 */
	private static final long serialVersionUID = -450784061695462935L;
	private HeaderOutGateway headerOutGatewaySUBE = null;
	private SeparadorTrama separadorTrama;
	private DataOutputFcnGateway dataOutputFcnGatewaySUBE;
	private boolean omitirSeparadores = false;
	private String dataSocket = "";
	private String dataOutGatewaySUBE = "";

	public MensajeOutboundGateway(Long idProceso, SeparadorTrama separadorTrama, String dataSocket) {
		this.separadorTrama = separadorTrama;
		this.setIdProceso(idProceso);

		this.headerOutGatewaySUBE = new HeaderOutGateway();
		this.omitirSeparadores = false;
		this.dataSocket = dataSocket;
	}

	public HeaderOutGateway getHeaderPOSOut() {
		return headerOutGatewaySUBE;
	}

	public void setHeaderPOSOut(HeaderOutGateway headerPOSOut) {
		this.headerOutGatewaySUBE = headerPOSOut;
	}

	public SeparadorTrama getSeparadorTrama() {
		return separadorTrama;
	}

	public void setSeparadorTrama(SeparadorTrama separadorTrama) {
		this.separadorTrama = separadorTrama;
	}

	public DataOutputFcnGateway getDataOutputFcnPOS() {
		return dataOutputFcnGatewaySUBE;
	}

	public void setDataOutputFcnPOS(DataOutputFcnGateway dataOutputFcnPOS) {
		this.dataOutputFcnGatewaySUBE = dataOutputFcnPOS;
	}

	public boolean isOmitirSeparadores() {
		return omitirSeparadores;
	}

	public void setOmitirSeparadores(boolean omitirSeparadores) {
		this.omitirSeparadores = omitirSeparadores;
	}

	// ------------------------------------------------------------------------------------------------------
	// DEFINICION DE METODOS
	// ------------------------------------------------------------------------------------------------------
	public String getTramaParaSocket() {
		StringBuilder oSB = new StringBuilder();
		SimpleDateFormat trxDateFmt;
		trxDateFmt = new SimpleDateFormat("yyyyMMddhhmmss");
		this.getHeaderPOSOut().setFechaServidor(
				trxDateFmt.format(Calendar.getInstance().getTime()));

		if (!omitirSeparadores) {
			oSB.append(this.getSeparadorTrama().getInicioTrama());
			oSB.append(this.getHeaderPOSOut().getCodigoRetorno());
			oSB.append(this.getSeparadorTrama().getSeparadorTrama());
			oSB.append(this.getHeaderPOSOut().getMensajeRetorno());
			oSB.append(this.getSeparadorTrama().getSeparadorTrama());
			oSB.append(this.getHeaderPOSOut().getFechaServidor());
			oSB.append(this.getSeparadorTrama().getSeparadorTrama());
			oSB.append(this.getHeaderPOSOut().getIdTransaccionCliente());
			oSB.append(this.getSeparadorTrama().getSeparadorTrama());
			oSB.append(this.getSeparadorTrama().getSeparadorHeaderData());
		}

		if (dataOutputFcnGatewaySUBE != null) {
			oSB.append(dataOutputFcnGatewaySUBE.obtenerTramaPOS());
		} else {
			oSB.append("");
		}

		if (!omitirSeparadores)
			oSB.append(this.getSeparadorTrama().getFinTrama());

		return oSB.toString();
	}

	public String getDataSocket() {
		return dataSocket;
	}

	public void setDataSocket(String dataSocket) {
		this.dataSocket = dataSocket;
	}
	
	/*TODO: ver que devuelva el mensaje de error*/
	public ErrorGateway parseData() {
		ErrorGateway errorGateway = new ErrorGateway();
		
		try{
			
			if (dataSocket == null || (dataSocket != null && dataSocket.isEmpty())) {
				errorGateway.setError("M0001", "La peticion recibida del POS es nula o vacia.");
				LogACGHelper.escribirLog(this.getIdProceso(), "La peticion recibida del POS es nula o vacia.");
				return errorGateway;
			}

			//dataSocket = dataSocket.replaceAll("\r\n", "");
			
			if (!(this.dataSocket.charAt(0) == this.separadorTrama.getInicioTrama()) && this.dataSocket.charAt(this.dataSocket.length() - 1 ) == this.separadorTrama.getFinTrama()){ 
				errorGateway.setError("M0001", "Trama de Peticion sin caracteres de inicio y/o fin.");
				LogACGHelper.escribirLog(this.getIdProceso(), "Trama de Peticion sin caracteres de inicio y/o fin.");
				return errorGateway;
			}

			int indIniTrama = this.dataSocket.indexOf(getSeparadorTrama().getInicioTrama(), 0);
			int indFinTrama = this.dataSocket.indexOf(getSeparadorTrama().getFinTrama(), 0);
			
			
			// Hace split del header y data
			String[] dataSocketSplit = dataSocket.substring(indIniTrama + 1, indFinTrama)
					.split(Character.toString(getSeparadorTrama().getSeparadorHeaderData()), -1);
		
			if (dataSocketSplit == null || (dataSocketSplit != null && dataSocketSplit.length <= 0)) {
				errorGateway.setError("M0001", "Header invalido.");
				LogACGHelper.escribirLog(this.getIdProceso(), "Header invalido.");
				return errorGateway;
			}
			
			String[] camposHeader =GatewayHelper.splitData(null, false, this.getSeparadorTrama().getSeparadorTrama(), dataSocketSplit[0]);
			
			if(!(camposHeader.length > 0)){
				errorGateway.setError("M0001", "El header no contiene los campos requeridos.");
				LogACGHelper.escribirLog(this.getIdProceso(), "Header invalido.");
				return errorGateway;
			}
			
			try {
				this.headerOutGatewaySUBE.setCodigoRetorno(camposHeader[this.getCampoAParsear()]);
				if("M0000".equals(this.headerOutGatewaySUBE.getCodigoRetorno())){
					this.headerOutGatewaySUBE.setMensajeRetorno(camposHeader[this.getCampoAParsear()]);
					this.headerOutGatewaySUBE.setFechaServidor(camposHeader[this.getCampoAParsear()]);
					this.headerOutGatewaySUBE.setIdTransaccionCliente(camposHeader[this.getCampoAParsear()]);
					EstadoTerna et = new EstadoTerna();
					et.setId(Long.parseLong(camposHeader[this.getCampoAParsear()]));
					this.headerOutGatewaySUBE.setEstadoTerna(et);
				}else{
					this.headerOutGatewaySUBE.setMensajeRetorno(camposHeader[this.getCampoAParsear()]);
					errorGateway.setError(this.headerOutGatewaySUBE.getCodigoRetorno(), this.headerOutGatewaySUBE.getMensajeRetorno());
					LogACGHelper.escribirLog(this.getIdProceso(), "Header invalido.");
					return errorGateway;
					
				}
				
				
				

				
				
			} catch (Exception e) {
				errorGateway.setError("M0001", "El header no contiene los campos requeridos.");
				LogACGHelper.escribirLog(this.getIdProceso(), "Header invalido.");
				return errorGateway;
			}
			
			if(dataSocketSplit.length > 1){
				dataOutGatewaySUBE = dataSocketSplit[1];
			}else{
				dataOutGatewaySUBE = "";
			}
			
		}
		catch(Exception e){
			errorGateway.setError("M0001", "Error en parseo de datos de salida (" + this.getClass().getSimpleName() + "): Campo Nro: |" + this.getCampoParseado() + "|, |" + e.getMessage() + "|");
			LogACGHelper.escribirLog(this.getIdProceso(),
					"Error en parseo de datos de salida (" + this.getClass().getSimpleName() + "): Campo Nro: |" + this.getCampoParseado() + "|, |" + e.getMessage() + "|");
			return errorGateway;
		}
		return errorGateway;
	}

	public String getDataOutGatewaySUBE() {
		return dataOutGatewaySUBE;
	}

	public void setDataOutGatewaySUBE(String dataOutGatewaySUBE) {
		this.dataOutGatewaySUBE = dataOutGatewaySUBE;
	}
	

}
