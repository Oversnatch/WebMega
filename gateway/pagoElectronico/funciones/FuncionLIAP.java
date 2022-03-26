package com.americacg.cargavirtual.gateway.pagoElectronico.funciones;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Map;
import com.americacg.cargavirtual.gateway.pagoElectronico.enums.FuncionName;
import com.americacg.cargavirtual.gateway.pagoElectronico.enums.FuncionParam;
import com.americacg.cargavirtual.gateway.pagoElectronico.model.ErrorGateway;
import com.americacg.cargavirtual.gateway.pagoElectronico.model.HeaderOutGateway;
import com.americacg.cargavirtual.gateway.pagoElectronico.model.MensajeOutboundGateway;
import com.americacg.cargavirtual.gateway.pagoElectronico.model.parametros.funciones.input.DataInputFcnLIAP;
import com.americacg.cargavirtual.gateway.pagoElectronico.model.parametros.funciones.output.DataOutputFcnLIAP;
import com.americacg.cargavirtual.web.helpers.LogACGHelper;

public class FuncionLIAP extends FuncionGateway {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1376344568613903403L;
	private static final long TIPO_TRX = 10L;

	private DataInputFcnLIAP dataIn = null;

	public FuncionLIAP(Map<FuncionParam, Object> parametros) throws Exception {
		super(parametros, FuncionName.LIAP);

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

		DataOutputFcnLIAP oDataOut = null;
		ErrorGateway oErr = null;

		MensajeOutboundGateway oMsgOut = new MensajeOutboundGateway();

		oErr = validar();

		if (!oErr.isHayError()) {
			try {



				/*
				 * oDataIn.setIdConfiguracionComercio(1L); oDataIn.setIdPago(12L); //idPago
				 * oDataIn.setIdMayorista(1L); //mayorista oDataIn.setIdCliente(10251L);
				 * //cliente oDataIn.setIdUsuario(0L); //usuario oDataIn.setFechaDesde(new
				 * Date("2020-06-22")); //Fecha desde oDataIn.setFechaHasta(new
				 * Date("2020-07-22")); //Fecha hasta oDataIn.setPageSize(6); //page size
				 * oDataIn.setPage(1); //page oDataIn.setCampoOrden("fechaDeAnulacion"); //campo
				 * orden oDataIn.setTipoOrden("desc"); //tipo Orden
				 */

				String trama = this.generarTramaConHash(this.obtenerTrama());
				LogACGHelper.escribirLog(this.getIdProceso(),
						"|" + String.format("%040x", new BigInteger(1, trama.getBytes("UTF-8"))) + "|");

				oDataOut = new DataOutputFcnLIAP(parametros, this.enviarTrama(trama));
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
		SimpleDateFormat oSDF = new SimpleDateFormat("yyyyMMddHHmm");

		oSB.append((char) 2);
		oSB.append(this.obtenerHeader());

		if (this.dataIn.getIdMayorista() != null)
			oSB.append(this.dataIn.getIdMayorista());
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
		
		if (this.dataIn.getOperador() != null) {
			if (this.dataIn.getOperador().getId() != null)
				oSB.append(this.dataIn.getOperador().getId());
			else
				oSB.append("");
		}else
			oSB.append("");
		oSB.append(this.getSeparadorTrama().getSeparadorTrama());

		if (this.dataIn.getFechaDesde() != null)
			oSB.append(oSDF.format(this.dataIn.getFechaDesde()));
		else
			oSB.append("");
		oSB.append(this.getSeparadorTrama().getSeparadorTrama());

		if (this.dataIn.getFechaHasta() != null)
			oSB.append(oSDF.format(this.dataIn.getFechaHasta()));
		else
			oSB.append("");
		oSB.append(this.getSeparadorTrama().getSeparadorTrama());
		
		if (this.dataIn.getPaymentId() != null)
			oSB.append(this.dataIn.getPaymentId().toString());
		else
			oSB.append("");
		oSB.append(this.getSeparadorTrama().getSeparadorTrama());
		
		if (this.dataIn.getIdPagoACG() != null)
			oSB.append(this.dataIn.getIdPagoACG().toString());
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

	
	public DataInputFcnLIAP getDataIn() {
		return dataIn;
	}

	public void setDataIn(DataInputFcnLIAP dataIn) {
		this.dataIn = dataIn;
	}

}
