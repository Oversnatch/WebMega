package com.americacg.cargavirtual.gateway.pagoElectronico.model.entidad;

import java.util.Date;

import com.americacg.cargavirtual.gateway.pagoElectronico.model.ClaseBase;

public class TokenOperador extends ClaseBase {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3142153244003693251L;
	private Long id;
	private Long idMayorista;
	private Long idCliente;
	private Long idUsuario;
	private Transaccion transaccion;
	private Operador operador;	
	private String binTarjeta;
	private String numeroTarjeta;
	private Integer longitudNumeroTarjeta;
	private String token;
	private Date fechaCreacion;
	private Date fechaVencimiento;
	private String estadoOperador;
	private Integer estadoACG;
	private Long tiempoRespuestaACGms;
	private Long tiempoRespuestaOperadorms;
	private String msgError;
	private Integer codRetornoOperador;
	private Integer codRetornoACG;
	private EstadoPagoElectronico estado;

	public TokenOperador() {
	}

	public TokenOperador(Long id, Long idMayorista, Long idCliente, Long idUsuario, Transaccion transaccion, Operador operador,
			String binTarjeta, String numeroTarjeta, Integer longitudNumeroTarjeta, String token, Date fechaCreacion,
			Date fechaVencimiento, String estadoOperador, Integer estadoACG, Long tiempoRespuestaACGms,
			Long tiempoRespuestaOperadorms, String msgError, Integer codRetornoOperador, Integer codRetornoACG,
			EstadoPagoElectronico estado) {
		super();
		this.id = id;
		this.idMayorista = idMayorista;
		this.idCliente = idCliente;
		this.idUsuario = idUsuario;
		this.transaccion = transaccion;
		this.operador  = operador;
		this.binTarjeta = binTarjeta;
		this.numeroTarjeta = numeroTarjeta;
		this.longitudNumeroTarjeta = longitudNumeroTarjeta;
		this.token = token;
		this.fechaCreacion = fechaCreacion;
		this.fechaVencimiento = fechaVencimiento;
		this.estadoOperador = estadoOperador;
		this.estadoACG = estadoACG;
		this.tiempoRespuestaACGms = tiempoRespuestaACGms;
		this.tiempoRespuestaOperadorms = tiempoRespuestaOperadorms;
		this.msgError = msgError;
		this.codRetornoOperador = codRetornoOperador;
		this.codRetornoACG = codRetornoACG;
		this.estado = estado;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public Transaccion getTransaccion() {
		return transaccion;
	}

	public void setTransaccion(Transaccion transaccion) {
		this.transaccion = transaccion;
	}

	public String getBinTarjeta() {
		return binTarjeta;
	}

	public void setBinTarjeta(String binTarjeta) {
		this.binTarjeta = binTarjeta;
	}

	public String getNumeroTarjeta() {
		return numeroTarjeta;
	}

	public void setNumeroTarjeta(String numeroTarjeta) {
		this.numeroTarjeta = numeroTarjeta;
	}

	public Integer getLongitudNumeroTarjeta() {
		return longitudNumeroTarjeta;
	}

	public void setLongitudNumeroTarjeta(Integer longitudNumeroTarjeta) {
		this.longitudNumeroTarjeta = longitudNumeroTarjeta;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public Date getFechaCreacion() {
		return fechaCreacion;
	}

	public void setFechaCreacion(Date fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}

	public Date getFechaVencimiento() {
		return fechaVencimiento;
	}

	public void setFechaVencimiento(Date fechaVencimiento) {
		this.fechaVencimiento = fechaVencimiento;
	}

	public String getEstadoOperador() {
		return estadoOperador;
	}

	public void setEstadoOperador(String estadoOperador) {
		this.estadoOperador = estadoOperador;
	}

	public Integer getEstadoACG() {
		return estadoACG;
	}

	public void setEstadoACG(Integer estadoACG) {
		this.estadoACG = estadoACG;
	}

	public Long getTiempoRespuestaACGms() {
		return tiempoRespuestaACGms;
	}

	public void setTiempoRespuestaACGms(Long tiempoRespuestaACGms) {
		this.tiempoRespuestaACGms = tiempoRespuestaACGms;
	}

	public Long getTiempoRespuestaOperadorms() {
		return tiempoRespuestaOperadorms;
	}

	public void setTiempoRespuestaOperadorms(Long tiempoRespuestaOperadorms) {
		this.tiempoRespuestaOperadorms = tiempoRespuestaOperadorms;
	}

	public String getMsgError() {
		return msgError;
	}

	public void setMsgError(String msgError) {
		this.msgError = msgError;
	}

	public Integer getCodRetornoOperador() {
		return codRetornoOperador;
	}

	public void setCodRetornoOperador(Integer codRetornoOperador) {
		this.codRetornoOperador = codRetornoOperador;
	}

	public Integer getCodRetornoACG() {
		return codRetornoACG;
	}

	public void setCodRetornoACG(Integer codRetornoACG) {
		this.codRetornoACG = codRetornoACG;
	}

	public EstadoPagoElectronico getEstado() {
		return estado;
	}

	public void setEstado(EstadoPagoElectronico estado) {
		this.estado = estado;
	}

	public Operador getOperador() {
		return operador;
	}

	public void setOperador(Operador operador) {
		this.operador = operador;
	}

}
