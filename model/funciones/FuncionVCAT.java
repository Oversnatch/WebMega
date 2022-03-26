package com.americacg.cargavirtual.sube.model.funciones;

import com.americacg.cargavirtual.web.helpers.LogACGHelper;
import com.americacg.cargavirtual.sube.enums.FuncionName;
import com.americacg.cargavirtual.sube.model.ErrorGateway;
import com.americacg.cargavirtual.sube.model.HeaderInGateway;
import com.americacg.cargavirtual.sube.model.HeaderOutGateway;
import com.americacg.cargavirtual.sube.model.MensajeOutboundGateway;
/*TODO: ver logs
import com.americacg.cargavirtual.web.sube.model.entidad.Log;
*/
import com.americacg.cargavirtual.sube.model.parametros.SeparadorTrama;

public class FuncionVCAT extends FuncionGateway {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2486438993079128966L;

	public FuncionVCAT(Long idProceso, HeaderInGateway headerIn,
			SeparadorTrama separadorTrama, String data) throws Exception {
		super(idProceso, headerIn, separadorTrama, FuncionName.VCAT, data);

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
		HeaderOutGateway oHPO = null;
		ErrorGateway oErr = null;

		
		MensajeOutboundGateway oMsgOut = new MensajeOutboundGateway(
				this.getIdProceso(), this.getSeparadorTrama(), null);

		// ---------- Ejecuta los validadores para la funcion GTER
		// ----------------
		oErr = validar();

		oHPO = new HeaderOutGateway();

		if (!oErr.isHayError()) {
			try {

				this.enviarTrama(this.armarTrama(
						this.getIdProceso(),
						null));
				
			} catch (Exception e) {

				LogACGHelper.escribirLog(
						this.getIdProceso(),
						"Error ejecucion de funcion ("
								+ this.getClass().getSimpleName() + "): |"
								+ e.getMessage() + "|");

				oMsgOut.setDataOutputFcnPOS(null);
				oHPO.setCodigoRetorno("E0001");
				oHPO.setMensajeRetorno("Error en ejecucion de funcion.");
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
		StringBuilder oSB = new StringBuilder();
		
		oSB.append(this.headerIn.getIdMayorista()).append(this.separadorTrama.getSeparadorTrama());
		oSB.append(this.headerIn.getIdCliente()).append(this.separadorTrama.getSeparadorTrama());
		oSB.append(this.headerIn.getIdUsuario()).append(this.separadorTrama.getSeparadorTrama());
		oSB.append(this.headerIn.getClave()).append(this.separadorTrama.getSeparadorTrama());
		oSB.append(this.headerIn.getFechaCliente()).append(this.separadorTrama.getSeparadorTrama());
		oSB.append(this.headerIn.getIdTransaccionCliente()).append(this.separadorTrama.getSeparadorTrama());
		oSB.append(this.headerIn.getTipoCliente());
		
		return oSB.toString();
	}
	
}
