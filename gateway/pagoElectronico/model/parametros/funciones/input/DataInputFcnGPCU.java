package com.americacg.cargavirtual.gateway.pagoElectronico.model.parametros.funciones.input;

import java.util.Date;

public class DataInputFcnGPCU extends DataInputFcnGateway {

	/**
	 * 
	 */
	private static final long serialVersionUID = 842028552247920264L;
	protected Long idMayorista;
	protected Long idCliente;
	protected Long idUsuario;
	protected String tipoCliente;
	private Long idBanco;
	private Long idMedioDePago;
	protected Date fechaCobranza;
	private Long idEstado;
	protected String campoOrden;
	protected String tipoOrden;
	protected Integer pageSize;
	protected Integer page;
	protected Integer importe;	

	public DataInputFcnGPCU() throws Exception {
		super();
	}

	public Long getIdMayorista() {
		return idMayorista;
	}

	public void setIdMayorista(Long idMayorista) {
		this.idMayorista = idMayorista;
	}

	public Long getIdCliente() {
		return idCliente;
	}

	public void setIdCliente(Long idCliente) {
		this.idCliente = idCliente;
	}

	public Long getIdUsuario() {
		return idUsuario;
	}

	public void setIdUsuario(Long idUsuario) {
		this.idUsuario = idUsuario;
	}

	public String getTipoCliente() {
		return tipoCliente;
	}

	public void setTipoCliente(String tipoCliente) {
		this.tipoCliente = tipoCliente;
	}

	public String getCampoOrden() {
		return campoOrden;
	}

	public void setCampoOrden(String campoOrden) {
		this.campoOrden = campoOrden;
	}

	public String getTipoOrden() {
		return tipoOrden;
	}

	public void setTipoOrden(String tipoOrden) {
		this.tipoOrden = tipoOrden;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public Integer getPage() {
		return page;
	}

	public void setPage(Integer page) {
		this.page = page;
	}

	public Long getIdMedioDePago() {
		return idMedioDePago;
	}

	public void setIdMedioDePago(Long idMedioDePago) {
		this.idMedioDePago = idMedioDePago;
	}

	public Long getIdEstado() {
		return idEstado;
	}

	public void setIdEstado(Long idEstado) {
		this.idEstado = idEstado;
	}

	public Long getIdBanco() {
		return idBanco;
	}

	public void setIdBanco(Long idBanco) {
		this.idBanco = idBanco;
	}

	public Date getFechaCobranza() {
		return fechaCobranza;
	}

	public void setFechaCobranza(Date fechaCobranza) {
		this.fechaCobranza = fechaCobranza;
	}

	public Integer getImporte() {
		return importe;
	}

	public void setImporte(Integer importe) {
		this.importe = importe;
	}
}
