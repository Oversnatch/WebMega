package com.americacg.cargavirtual.gateway.pagoElectronico.funciones;

import java.math.BigInteger;
import java.util.Map;
import com.americacg.cargavirtual.gateway.pagoElectronico.enums.FuncionName;
import com.americacg.cargavirtual.gateway.pagoElectronico.enums.FuncionParam;
import com.americacg.cargavirtual.gateway.pagoElectronico.model.ErrorGateway;
import com.americacg.cargavirtual.gateway.pagoElectronico.model.HeaderOutGateway;
import com.americacg.cargavirtual.gateway.pagoElectronico.model.MensajeOutboundGateway;
import com.americacg.cargavirtual.gateway.pagoElectronico.model.parametros.funciones.input.DataInputFcnPAGO;
import com.americacg.cargavirtual.gateway.pagoElectronico.model.parametros.funciones.output.DataOutputFcnPAGO;
import com.americacg.cargavirtual.web.helpers.LogACGHelper;

public class FuncionPAGO extends FuncionGateway {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1376344568613903403L;
	private static final long TIPO_TRX = 9L;

	private DataInputFcnPAGO dataIn = null;

	public FuncionPAGO(Map<FuncionParam, Object> parametros) throws Exception {
		super(parametros, FuncionName.PAGO);
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

		// DataInputFcnPAGO oDataIn = null;
		DataOutputFcnPAGO oDataOut = null;
		ErrorGateway oErr = null;

		MensajeOutboundGateway oMsgOut = new MensajeOutboundGateway();

		oErr = validar();

		if (!oErr.isHayError()) {
			try {

				String trama = this.generarTramaConHash(this.obtenerTrama());
				LogACGHelper.escribirLog(this.getIdProceso(),
						"|" + String.format("%040x", new BigInteger(1, trama.getBytes("UTF-8"))) + "|");

				oDataOut = new DataOutputFcnPAGO(parametros, this.enviarTrama(trama));
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

		oSB.append((char) this.getSeparadorTrama().getInicioTrama());
		oSB.append(this.obtenerHeader());

		if (this.dataIn.getNumeroTicketACG() != null)
			oSB.append(this.dataIn.getNumeroTicketACG());
		else
			oSB.append("");
		oSB.append(this.getSeparadorTrama().getSeparadorTrama());

		
		if (this.dataIn.getIdTerminal() != null)
			oSB.append(this.dataIn.getIdTerminal());
		else
			oSB.append("");
		oSB.append(this.getSeparadorTrama().getSeparadorTrama());
		

		if (this.dataIn.getTarjetaNumero() != null)
			oSB.append(this.dataIn.getTarjetaNumero());
		else
			oSB.append("");
		oSB.append(this.getSeparadorTrama().getSeparadorTrama());

		if (this.dataIn.getTarjetaExpiracionMes() != null)
			oSB.append(this.dataIn.getTarjetaExpiracionMes());
		else
			oSB.append("");
		oSB.append(this.getSeparadorTrama().getSeparadorTrama());

		if (this.dataIn.getTarjetaExpiracionAnio() != null)
			oSB.append(this.dataIn.getTarjetaExpiracionAnio());
		else
			oSB.append("");
		oSB.append(this.getSeparadorTrama().getSeparadorTrama());

		if (this.dataIn.getCodigoSeguridad() != null)
			oSB.append(this.dataIn.getCodigoSeguridad());
		else
			oSB.append("");
		oSB.append(this.getSeparadorTrama().getSeparadorTrama());

		if (this.dataIn.getTarjetaTitularNombre() != null)
			oSB.append(this.dataIn.getTarjetaTitularNombre());
		else
			oSB.append("");
		oSB.append(this.getSeparadorTrama().getSeparadorTrama());

		if (this.dataIn.getTarjetaTitularIdentificacion() != null) {
			if (this.dataIn.getTarjetaTitularIdentificacion().getTipo() != null)
				oSB.append(this.dataIn.getTarjetaTitularIdentificacion().getTipo());
			else
				oSB.append("");
			oSB.append(this.getSeparadorTrama().getSeparadorTrama());

			if (this.dataIn.getTarjetaTitularIdentificacion().getNumero() != null)
				oSB.append(this.dataIn.getTarjetaTitularIdentificacion().getNumero());
			else
				oSB.append("");
			oSB.append(this.getSeparadorTrama().getSeparadorTrama());

		}

		if (this.dataIn.getIdMedioPago() != null)
			oSB.append(this.dataIn.getIdMedioPago());
		else
			oSB.append("");
		oSB.append(this.getSeparadorTrama().getSeparadorTrama());

		if (this.dataIn.getImporte() != null)
			oSB.append(this.dataIn.getImporte());
		else
			oSB.append("");
		oSB.append(this.getSeparadorTrama().getSeparadorTrama());

		if (this.dataIn.getMoneda() != null)
			oSB.append(this.dataIn.getMoneda());
		else
			oSB.append("");
		oSB.append(this.getSeparadorTrama().getSeparadorTrama());

		if (this.dataIn.getIdPlanDeCuota() != null)
			oSB.append(this.dataIn.getIdPlanDeCuota());
		else
			oSB.append("");
		oSB.append(this.getSeparadorTrama().getSeparadorTrama());

		if (this.dataIn.getTelefonoCliente() != null)
			oSB.append(this.dataIn.getTelefonoCliente());
		else
			oSB.append("");
		oSB.append(this.getSeparadorTrama().getSeparadorTrama());
		
		if (this.dataIn.getEmailCliente() != null)
			oSB.append(this.dataIn.getEmailCliente());
		else
			oSB.append("");
		oSB.append(this.getSeparadorTrama().getSeparadorTrama());

		if (this.dataIn.getIdBanco() != null)
			oSB.append(this.dataIn.getIdBanco());
		else
			oSB.append("");
		oSB.append(this.getSeparadorTrama().getSeparadorTrama());

		if (this.dataIn.getIdConceptoDePago() != null)
			oSB.append(this.dataIn.getIdConceptoDePago());
		else
			oSB.append("");
		oSB.append(this.getSeparadorTrama().getSeparadorTrama());

		if (this.dataIn.getDatosAdicionalesPago() != null)
			oSB.append(this.dataIn.getDatosAdicionalesPago());
		else
			oSB.append("");
		oSB.append(this.getSeparadorTrama().getSeparadorTrama());

		oSB.append(this.dataIn.getTipoCargaTarjeta());
		//oSB.append("M");
		oSB.append(this.getSeparadorTrama().getSeparadorTrama());
		
		oSB.append((char) 3);

		return oSB.toString();
	}

	public DataInputFcnPAGO getDataIn() {
		return dataIn;
	}

	public void setDataIn(DataInputFcnPAGO dataIn) {
		this.dataIn = dataIn;
	}

}
