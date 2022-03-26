package com.americacg.cargavirtual.gateway.pagoElectronico.model.parametros.funciones.input;

public class DataInputFcnCOPA extends DataInputFcnGateway {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4735876094887942557L;
	protected Integer paymentId = 0;
	protected String idPagoACG = null;
	protected Integer tarjetaDatosAdicionales = 0;
	protected Integer omitirRequestOperador = 0;
	protected Integer dataObjectReturnType = 0;

	public DataInputFcnCOPA() throws Exception {
		super();
	}

	public Integer getPaymentId() {
		return paymentId;
	}

	public void setPaymentId(Integer paymentId) {
		this.paymentId = paymentId;
	}

	public String getIdPagoACG() {
		return idPagoACG;
	}

	public void setIdPagoACG(String idPagoACG) {
		this.idPagoACG = idPagoACG;
	}

	public Integer getTarjetaDatosAdicionales() {
		return tarjetaDatosAdicionales;
	}

	public void setTarjetaDatosAdicionales(Integer tarjetaDatosAdicionales) {
		this.tarjetaDatosAdicionales = tarjetaDatosAdicionales;
	}

	public Integer getOmitirRequestOperador() {
		return omitirRequestOperador;
	}

	public void setOmitirRequestOperador(Integer omitirRequestOperador) {
		this.omitirRequestOperador = omitirRequestOperador;
	}

	public Integer getDataObjectReturnType() {
		return dataObjectReturnType;
	}

	public void setDataObjectReturnType(Integer dataObjectReturnType) {
		this.dataObjectReturnType = dataObjectReturnType;
	}
}
