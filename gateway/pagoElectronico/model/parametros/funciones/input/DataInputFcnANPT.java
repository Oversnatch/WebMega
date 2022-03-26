package com.americacg.cargavirtual.gateway.pagoElectronico.model.parametros.funciones.input;

public class DataInputFcnANPT extends DataInputFcnGateway {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4735876094887942557L;
	private String numeroTicketACG = "";
	private Integer paymentId = 0;
	private Long idPagoACG = 0L;
	private String idTerminal = "";

	public DataInputFcnANPT() throws Exception {
		super();
	}

	public String getNumeroTicketACG() {
		return numeroTicketACG;
	}

	public void setNumeroTicketACG(String numeroTicketACG) {
		this.numeroTicketACG = numeroTicketACG;
	}

	public Integer getPaymentId() {
		return paymentId;
	}

	public void setPaymentId(Integer paymentId) {
		this.paymentId = paymentId;
	}

	public Long getIdPagoACG() {
		return idPagoACG;
	}

	public void setIdPagoACG(Long idDePagoACG) {
		this.idPagoACG = idDePagoACG;
	}

	public String getIdTerminal() {
		return idTerminal;
	}

	public void setIdTerminal(String idTerminal) {
		this.idTerminal = idTerminal;
	}

}
