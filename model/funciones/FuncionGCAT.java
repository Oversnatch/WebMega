package com.americacg.cargavirtual.sube.model.funciones;

import com.americacg.cargavirtual.web.helpers.LogACGHelper;
import com.americacg.cargavirtual.sube.enums.FuncionName;
import com.americacg.cargavirtual.sube.model.ErrorGateway;
import com.americacg.cargavirtual.sube.model.HeaderInGateway;
import com.americacg.cargavirtual.sube.model.HeaderOutGateway;
import com.americacg.cargavirtual.sube.model.MensajeOutboundGateway;
import com.americacg.cargavirtual.sube.model.parametros.SeparadorTrama;
import com.americacg.cargavirtual.sube.model.parametros.funciones.output.DataOutputFcnGCAT;

public class FuncionGCAT extends FuncionGateway {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2486438993079128966L;

	public FuncionGCAT(Long idProceso, HeaderInGateway headerIn,
			SeparadorTrama separadorTrama, String data) throws Exception {
		super(idProceso, headerIn, separadorTrama, FuncionName.GCAT, data);

		this.setDataIn(null);
	}

	@Override
	public ErrorGateway validar() {
		ErrorGateway oErr = new ErrorGateway();

		try {
			// --- Invoca al metodo validador de la clase padre ---
			oErr = super.validar();

			if (oErr.isHayError()) {
				return oErr;
			}

		} catch (Exception ex) {
			LogACGHelper.escribirLog(
					this.getIdProceso(),
					"Error en validador de datos de entrada. ("
							+ this.getClass().getSimpleName() + ") Err: |"
							+ ex.getMessage() + "|");
						
			oErr.setError("M0001", "Error en validador de datos de entrada.");
			return oErr;
		}

		return oErr;
	}

	@Override
	public MensajeOutboundGateway ejecutar() {
		DataOutputFcnGCAT oDataOut = null;
		HeaderOutGateway oHPO = null;
		ErrorGateway oErr = null;
		/*TODO: ver logs
		Log oLogRequest = null;
		*/
		MensajeOutboundGateway rtaTrama;
		
		MensajeOutboundGateway oMsgOut = new MensajeOutboundGateway(
				this.getIdProceso(), this.getSeparadorTrama(), null);

		// ---------- Ejecuta los validadores para la funcion GTER
		// ----------------
		oErr = validar();

		oHPO = new HeaderOutGateway();

		if (!oErr.isHayError()) {
			try {

				rtaTrama = this.enviarTrama(this.armarTrama(
						this.getIdProceso(),
						null));

				
				oDataOut = new DataOutputFcnGCAT(this.getSeparadorTrama(), rtaTrama.getDataOutGatewaySUBE());

				oMsgOut.setHeaderPOSOut(rtaTrama.getHeaderPOSOut());
				oMsgOut.setDataOutGatewaySUBE(oDataOut.getClaveDeAutorizacion());
/*
				oHPO.setCodigoRetorno("M0000");
				oHPO.setMensajeRetorno("");
				*/
				
				return oMsgOut;
				
			} catch (Exception e) {

				LogACGHelper.escribirLog(
						this.getIdProceso(),
						"Error ejecucion de funcion ("
								+ this.getClass().getSimpleName() + "): |"
								+ e.getMessage() + "|");

				oMsgOut.setDataOutputFcnPOS(null);
				oHPO.setCodigoRetorno("E0001");
				oHPO.setMensajeRetorno("Error en ejecucion de funcion. Error: |" + e.getMessage() + "|");
			}
		} else {
			oMsgOut.setDataOutputFcnPOS(null);
			oHPO.setCodigoRetorno(oErr.getCodigoError());
			oHPO.setMensajeRetorno(oErr.getMsgError());
		}

		oMsgOut.setHeaderPOSOut(oHPO);

		return oMsgOut;
	}
	
	@Override
	protected String armarTramaHeaderExtend(){
		
		return null;
	}
	
}
