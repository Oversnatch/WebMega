package com.americacg.cargavirtual.gateway.pagoElectronico.funciones;

import java.math.BigInteger;
import java.util.Map;
import com.americacg.cargavirtual.gateway.pagoElectronico.enums.FuncionName;
import com.americacg.cargavirtual.gateway.pagoElectronico.enums.FuncionParam;
import com.americacg.cargavirtual.gateway.pagoElectronico.model.ErrorGateway;
import com.americacg.cargavirtual.gateway.pagoElectronico.model.HeaderOutGateway;
import com.americacg.cargavirtual.gateway.pagoElectronico.model.MensajeOutboundGateway;
import com.americacg.cargavirtual.gateway.pagoElectronico.model.parametros.funciones.input.DataInputFcnLEOP;
import com.americacg.cargavirtual.gateway.pagoElectronico.model.parametros.funciones.output.DataOutputFcnLEOP;
import com.americacg.cargavirtual.web.helpers.LogACGHelper;

public class FuncionLEOP extends FuncionGateway {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1376344568613903403L;
	private static final long TIPO_TRX = 21L;

	private DataInputFcnLEOP dataIn = null;

	public FuncionLEOP(Map<FuncionParam, Object> parametros) throws Exception {
		super(parametros, FuncionName.LEOP);

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

		DataOutputFcnLEOP oDataOut = null;
		ErrorGateway oErr = null;

		MensajeOutboundGateway oMsgOut = new MensajeOutboundGateway();

		oErr = validar();

		if (!oErr.isHayError()) {
			try {

				String trama = this.generarTramaConHash(this.obtenerTrama());
				LogACGHelper.escribirLog(this.getIdProceso(),
						"|" + String.format("%040x", new BigInteger(1, trama.getBytes("UTF-8"))) + "|");

				oDataOut = new DataOutputFcnLEOP(parametros, this.enviarTrama(trama));
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

		if (this.dataIn.getIdCliente() != null)
			oSB.append(this.dataIn.getIdCliente());
		else
			oSB.append("");
		oSB.append(this.getSeparadorTrama().getSeparadorTrama());

		if (this.dataIn.getIdUsuario() != null)
			oSB.append(this.dataIn.getIdUsuario());
		else
			oSB.append("");
		oSB.append(this.getSeparadorTrama().getSeparadorTrama());

		if (this.dataIn.getTipoCliente() != null)
			oSB.append(this.dataIn.getTipoCliente());
		else
			oSB.append("");
		oSB.append(this.getSeparadorTrama().getSeparadorTrama());

		if (this.dataIn.getIdOperador() != null)
			oSB.append(this.dataIn.getIdOperador());
		else
			oSB.append("");
		oSB.append(this.getSeparadorTrama().getSeparadorTrama());

		if (this.dataIn.getCodigoOperador() != null)
			oSB.append(this.dataIn.getCodigoOperador());
		else
			oSB.append("");
		oSB.append(this.getSeparadorTrama().getSeparadorTrama());

		if (this.dataIn.getDescripcionOperador() != null)
			oSB.append(this.dataIn.getDescripcionOperador());
		else
			oSB.append("");
		oSB.append(this.getSeparadorTrama().getSeparadorTrama());

		if (this.dataIn.getCodigoACGOperador() != null)
			oSB.append(this.dataIn.getCodigoACGOperador());
		else
			oSB.append("");
		oSB.append(this.getSeparadorTrama().getSeparadorTrama());

		if (this.dataIn.getDescripcionCortaACGOperador() != null)
			oSB.append(this.dataIn.getDescripcionCortaACGOperador());
		else
			oSB.append("");
		oSB.append(this.getSeparadorTrama().getSeparadorTrama());

		if (this.dataIn.getDescripcionLargaACGOperador() != null)
			oSB.append(this.dataIn.getDescripcionLargaACGOperador());
		else
			oSB.append("");
		oSB.append(this.getSeparadorTrama().getSeparadorTrama());
		
		if (this.dataIn.getEstadoOperador() != null)
			oSB.append(this.dataIn.getEstadoOperador());
		else
			oSB.append("");
		oSB.append(this.getSeparadorTrama().getSeparadorTrama());				
		
		if (this.dataIn.getPageSize() != null)
			oSB.append(this.dataIn.getPageSize());
		else
			oSB.append("");
		oSB.append(this.getSeparadorTrama().getSeparadorTrama());

		if (this.dataIn.getPage() != null)
			oSB.append(this.dataIn.getPage());
		else
			oSB.append("");
		oSB.append(this.getSeparadorTrama().getSeparadorTrama());

		if (this.dataIn.getCampoOrden() != null)
			oSB.append(this.dataIn.getCampoOrden());
		else
			oSB.append("");
		oSB.append(this.getSeparadorTrama().getSeparadorTrama());

		if (this.dataIn.getTipoOrden() != null)
			oSB.append(this.dataIn.getTipoOrden());
		else
			oSB.append("");
		oSB.append(this.getSeparadorTrama().getSeparadorTrama());

		oSB.append((char) 3);

		return oSB.toString();
	}

	public DataInputFcnLEOP getDataIn() {
		return dataIn;
	}

	public void setDataIn(DataInputFcnLEOP dataIn) {
		this.dataIn = dataIn;
	}

}
