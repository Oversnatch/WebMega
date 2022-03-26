package com.americacg.cargavirtual.gateway.pagoElectronico.funciones;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Map;
import com.americacg.cargavirtual.gateway.pagoElectronico.enums.FuncionName;
import com.americacg.cargavirtual.gateway.pagoElectronico.enums.FuncionParam;
import com.americacg.cargavirtual.gateway.pagoElectronico.model.ErrorGateway;
import com.americacg.cargavirtual.gateway.pagoElectronico.model.HeaderOutGateway;
import com.americacg.cargavirtual.gateway.pagoElectronico.model.MensajeOutboundGateway;
import com.americacg.cargavirtual.gateway.pagoElectronico.model.parametros.funciones.input.DataInputFcnLICP;
import com.americacg.cargavirtual.gateway.pagoElectronico.model.parametros.funciones.output.DataOutputFcnLICP;
import com.americacg.cargavirtual.web.helpers.LogACGHelper;

public class FuncionLICP extends FuncionGateway {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7150712232171811065L;

	private static final long TIPO_TRX = 11L;

	private DataInputFcnLICP dataIn = null;

	public FuncionLICP(Map<FuncionParam, Object> parametros) throws Exception {
		super(parametros, FuncionName.LICP);

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

		DataOutputFcnLICP oDataOut = null;
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

				oDataOut = new DataOutputFcnLICP(parametros, this.enviarTrama(trama));
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
		SimpleDateFormat oSDF = new SimpleDateFormat("yyyyMMdd");

		oSB.append((char) 2);
		oSB.append(this.obtenerHeader());

		oSB.append("").append(this.getSeparadorTrama().getSeparadorTrama());
		oSB.append("").append(this.getSeparadorTrama().getSeparadorTrama());
		oSB.append("").append(this.getSeparadorTrama().getSeparadorTrama());
		oSB.append("").append(this.getSeparadorTrama().getSeparadorTrama());
		oSB.append("").append(this.getSeparadorTrama().getSeparadorTrama());
		
		/*
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
*/
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

		/*
		if (this.dataIn.getTipoOrden() != null)
			oSB.append(this.dataIn.getTipoOrden());
		else
			oSB.append("");
		oSB.append(this.getSeparadorTrama().getSeparadorTrama());
		 */
		oSB.append((char) 3);

		return oSB.toString();
	}

	public DataInputFcnLICP getDataIn() {
		return dataIn;
	}

	public void setDataIn(DataInputFcnLICP dataIn) {
		this.dataIn = dataIn;
	}

}
