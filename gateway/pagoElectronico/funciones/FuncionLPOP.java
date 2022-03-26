package com.americacg.cargavirtual.gateway.pagoElectronico.funciones;

import java.math.BigInteger;
import java.util.Map;
import com.americacg.cargavirtual.gateway.pagoElectronico.enums.FuncionName;
import com.americacg.cargavirtual.gateway.pagoElectronico.enums.FuncionParam;
import com.americacg.cargavirtual.gateway.pagoElectronico.model.ErrorGateway;
import com.americacg.cargavirtual.gateway.pagoElectronico.model.HeaderInGateway;
import com.americacg.cargavirtual.gateway.pagoElectronico.model.HeaderOutGateway;
import com.americacg.cargavirtual.gateway.pagoElectronico.model.MensajeOutboundGateway;
import com.americacg.cargavirtual.gateway.pagoElectronico.model.parametros.funciones.input.DataInputFcnLPOP;
import com.americacg.cargavirtual.gateway.pagoElectronico.model.parametros.funciones.output.DataOutputFcnLPOP;
import com.americacg.cargavirtual.web.helpers.LogACGHelper;

public class FuncionLPOP extends FuncionGateway {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1376344568613903403L;
	private static final long TIPO_TRX = 6L;

	private DataInputFcnLPOP dataIn = null;

	public FuncionLPOP(Map<FuncionParam, Object> parametros) throws Exception {
		super(parametros, FuncionName.LPOP);

		/*
		 * if (!this.getParametrosServicio().getParametros().containsKey("ID_RED")) {
		 * throw new ConfiguracionSUBEException( "Parametro ID_RED no configurado."); }
		 */
	}

	@Override
	public ErrorGateway validar() {
		ErrorGateway oErr = new ErrorGateway();

		try {
			// --- Invoca al metodo validador de la clase padre ---
			oErr = super.validar(true);

			if (oErr.isHayError()) {
				return oErr;
			}
		} catch (Exception ex) {
			LogACGHelper.escribirLog(this.getIdProceso(), "Error en validador de datos de entrada. ("
					+ this.getClass().getSimpleName() + ") Err: |" + ex.getMessage() + "|");
			oErr.setError("M0001", "Error en validador de datos de entrada.");
			return oErr;
		}

		return oErr;
	}

	@Override
	public MensajeOutboundGateway ejecutar() {

		DataOutputFcnLPOP oDataOut = null;
		HeaderInGateway oHeader = null;
		ErrorGateway oErr = null;

		MensajeOutboundGateway oMsgOut = new MensajeOutboundGateway();

		oErr = validar();

		if (!oErr.isHayError()) {
			try {

				String trama = this.generarTramaConHash(this.obtenerTrama());
				LogACGHelper.escribirLog(this.getIdProceso(),
						"|" + String.format("%040x", new BigInteger(1, trama.getBytes("UTF-8"))) + "|");

				oDataOut = new DataOutputFcnLPOP(parametros, this.enviarTrama(trama));
				oDataOut.parseData();
				oMsgOut.setDataOutputFcn(oDataOut);

			} catch (Exception e) {

				LogACGHelper.escribirLog(this.getIdProceso(), "Error ejecucion de funcion ("
						+ this.getClass().getSimpleName() + "): |" + e.getMessage() + "|");
				HeaderOutGateway hog = new HeaderOutGateway();
				hog.setCodigoRetorno("E0002");
				hog.setMensajeRetorno(e.getMessage());
				oMsgOut.setHeaderOut(hog);

			}

		} else {
			HeaderOutGateway hog = new HeaderOutGateway();
			hog.setCodigoRetorno(oErr.getCodigoError());
			hog.setMensajeRetorno(oErr.getMsgError());
			oMsgOut.setHeaderOut(hog);
		}

		return oMsgOut;
	}
	
	
	@Override
	public String obtenerTrama() {
		StringBuilder oSB = new StringBuilder();

		oSB.append((char) 2);
		oSB.append(this.obtenerHeader());
		
		if (this.dataIn.getIdMayorista() != null)
			oSB.append(this.dataIn.getIdMayorista().toString());
		else
			oSB.append("");
		oSB.append(this.getSeparadorTrama().getSeparadorTrama());

		oSB.append("");
		oSB.append(this.getSeparadorTrama().getSeparadorTrama());
		
		oSB.append("");
		oSB.append(this.getSeparadorTrama().getSeparadorTrama());
		
		oSB.append("");
		oSB.append(this.getSeparadorTrama().getSeparadorTrama());
		
		oSB.append("");
		oSB.append(this.getSeparadorTrama().getSeparadorTrama());		

		if (this.dataIn.getOperador() != null)
			oSB.append(this.dataIn.getOperador().toString());
		else
			oSB.append("");
		oSB.append(this.getSeparadorTrama().getSeparadorTrama());

		if (this.dataIn.getEstado() != null)
			oSB.append(this.dataIn.getEstado());
		else
			oSB.append("");
		oSB.append(this.getSeparadorTrama().getSeparadorTrama());
		
		if (this.dataIn.getSiteTransactionId() != null)
			oSB.append(this.dataIn.getSiteTransactionId().toString());
		else
			oSB.append("");
		oSB.append(this.getSeparadorTrama().getSeparadorTrama());

		if (this.dataIn.getSiteId() != null)
			oSB.append(this.dataIn.getSiteId().toString());
		else
			oSB.append("");
		oSB.append(this.getSeparadorTrama().getSeparadorTrama());

		if (this.dataIn.getMerchantId() != null)
			oSB.append(this.dataIn.getMerchantId().toString());
		else
			oSB.append("");
		oSB.append(this.getSeparadorTrama().getSeparadorTrama());

		if (this.dataIn.getFechaDesde() != null)
			oSB.append(this.dataIn.getFechaDesde().toString());
		else
			oSB.append("");
		oSB.append(this.getSeparadorTrama().getSeparadorTrama());

		if (this.dataIn.getFechaHasta() != null)
			oSB.append(this.dataIn.getFechaHasta().toString());
		else
			oSB.append("");
		oSB.append(this.getSeparadorTrama().getSeparadorTrama());
		
		
		if (this.dataIn.getPageSize() != null)
			oSB.append(this.dataIn.getPageSize().toString());
		else
			oSB.append("0");
		oSB.append(this.getSeparadorTrama().getSeparadorTrama());		

		if (this.dataIn.getPage() != null)
			oSB.append(this.dataIn.getPage().toString());
		else
			oSB.append("0");
		oSB.append(this.getSeparadorTrama().getSeparadorTrama());		

		if (this.dataIn.getCampoOrden() != null)
			oSB.append(this.dataIn.getCampoOrden().toString());
		else
			oSB.append("");
		oSB.append(this.getSeparadorTrama().getSeparadorTrama());	
		
		if (this.dataIn.getTipoOrden() != null)
			oSB.append(this.dataIn.getTipoOrden().toString());
		else
			oSB.append("");
		oSB.append(this.getSeparadorTrama().getSeparadorTrama());	
		
		oSB.append((char) 3);

		return oSB.toString();
	}

	public DataInputFcnLPOP getDataIn() {
		return dataIn;
	}

	public void setDataIn(DataInputFcnLPOP dataIn) {
		this.dataIn = dataIn;
	}

}
