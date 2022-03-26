package com.americacg.cargavirtual.gateway.pagoElectronico.model;

import com.americacg.cargavirtual.gateway.pagoElectronico.model.parametros.funciones.output.DataOutputFcnGateway;

public class MensajeOutboundGateway extends ClaseBase {
	/**
	 * 
	 */
	private static final long serialVersionUID = -450784061695462935L;
	private HeaderOutGateway headerOut = null;
	private DataOutputFcnGateway dataOutputFcn;

	public MensajeOutboundGateway() {
	}

	public HeaderOutGateway getHeaderOut() {
		return headerOut;
	}

	public void setHeaderOut(HeaderOutGateway headerOut) {
		this.headerOut = headerOut;
	}

	public DataOutputFcnGateway getDataOutputFcn() {
		return dataOutputFcn;
	}

	public void setDataOutputFcn(DataOutputFcnGateway dataOutputFcn) {
		this.dataOutputFcn = dataOutputFcn;
	}
}