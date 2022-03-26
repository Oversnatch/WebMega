package com.americacg.cargavirtual.sube.model.entidad;

import java.util.Date;

public class Log {
	private Long id;
	private Log logRequest;
	private Date fecha;
	private Long idMayorista;
	private Long idCliente;
	private String lgSerie;
	private String samId;
	private String posId;
	private Integer sentidoTRX;
	private String servidorConexion;
	private String logData;
	private String logDataEncriptado;
	private String datosAdicionales;
	
	public Log() {
		super();
	}

	public Log(Long id, Log logRequest, Date fecha, Long idMayorista, Long idCliente,
			String terna, String lgSerie, String samId, String posId,
			String tipoTransaccion,
			Integer sentidoTRX, String servidorConexion, String logData, String logDataEncriptado,
			String estadoTransaccion, String datosAdicionales) {
		super();
		this.id = id;
		this.logRequest = logRequest;
		this.fecha = fecha;
		this.idMayorista = idMayorista;
		this.idCliente = idCliente;
		this.lgSerie = lgSerie;
		this.samId = samId;
		this.posId = posId;
		this.sentidoTRX = sentidoTRX;
		this.servidorConexion = servidorConexion;
		this.logData = logData;
		this.logDataEncriptado = logDataEncriptado;
		this.datosAdicionales = datosAdicionales;
	}



	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public Log getLogRequest() {
		return logRequest;
	}

	public void setLogRequest(Log logRequest) {
		this.logRequest = logRequest;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
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

	public String getLgSerie() {
		return lgSerie;
	}

	public void setLgSerie(String lgSerie) {
		this.lgSerie = lgSerie;
	}

	public String getSamId() {
		return samId;
	}

	public void setSamId(String samId) {
		this.samId = samId;
	}

	public String getPosId() {
		return posId;
	}

	public void setPosId(String posId) {
		this.posId = posId;
	}

	public Integer getSentidoTRX() {
		return sentidoTRX;
	}

	public void setSentidoTRX(Integer sentidoTRX) {
		this.sentidoTRX = sentidoTRX;
	}

	public String getServidorConexion() {
		return servidorConexion;
	}

	public void setServidorConexion(String servidorConexion) {
		this.servidorConexion = servidorConexion;
	}

	public String getLogData() {
		return logData;
	}

	public void setLogData(String logData) {
		this.logData = logData;
	}

	public String getLogDataEncriptado() {
		return logDataEncriptado;
	}

	public void setLogDataEncriptado(String logDataEncriptado) {
		this.logDataEncriptado = logDataEncriptado;
	}

	public String getDatosAdicionales() {
		return datosAdicionales;
	}

	public void setDatosAdicionales(String datosAdicionales) {
		this.datosAdicionales = datosAdicionales;
	}
}
