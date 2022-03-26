package com.americacg.cargavirtual.gateway.pagoElectronico.model.entidad;

import java.math.BigDecimal;
import java.util.Date;

import com.americacg.cargavirtual.gateway.pagoElectronico.model.ClaseBase;

public class Transaccion extends ClaseBase {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4857613256911557431L;
	private Long id;
	private Date fechaServidor;
	private Long idMayorista;
	private Operador operador;	
	private ConfiguracionComercio configuracionComercio;
	private Long idCliente;
	private String canal;
	private String identifTerminal;
	private TipoTransaccion tipoTransaccion;
	private String idTransaccionCliente;
	private Date fechaCliente;
	private MedioDePago medioDePago;
	private String binTarjeta;
	private String numeroTarjeta;
	private Moneda moneda;
	private BigDecimal importe;
	private Integer cantidadCuotas;
	private String numeroTicketACG;
	private String datosAdicionales;
	private Long idUsuario;
	private EstadoPagoElectronico estado;

	public Transaccion() {
	}

	public Transaccion(Long id){
		this.id = id;
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getFechaServidor() {
		return fechaServidor;
	}

	public void setFechaServidor(Date fechaServidor) {
		this.fechaServidor = fechaServidor;
	}

	public Long getIdMayorista() {
		return idMayorista;
	}

	public void setIdMayorista(Long idMayorista) {
		this.idMayorista = idMayorista;
	}

	public ConfiguracionComercio getConfiguracionComercio() {
		return configuracionComercio;
	}

	public void setConfiguracionComercio(ConfiguracionComercio configuracionComercio) {
		this.configuracionComercio = configuracionComercio;
	}

	public Long getIdCliente() {
		return idCliente;
	}

	public void setIdCliente(Long idCliente) {
		this.idCliente = idCliente;
	}

	public String getCanal() {
		return canal;
	}

	public void setCanal(String canal) {
		this.canal = canal;
	}

	public String getIdentifTerminal() {
		return identifTerminal;
	}

	public void setIdentifTerminal(String identifTerminal) {
		this.identifTerminal = identifTerminal;
	}

	public String getIdTransaccionCliente() {
		return idTransaccionCliente;
	}

	public void setIdTransaccionCliente(String idTransaccionCliente) {
		this.idTransaccionCliente = idTransaccionCliente;
	}

	public Date getFechaCliente() {
		return fechaCliente;
	}

	public void setFechaCliente(Date fechaCliente) {
		this.fechaCliente = fechaCliente;
	}

	public TipoTransaccion getTipoTransaccion() {
		return tipoTransaccion;
	}

	public void setTipoTransaccion(TipoTransaccion tipoTransaccion) {
		this.tipoTransaccion = tipoTransaccion;
	}

	public MedioDePago getMedioDePago() {
		return medioDePago;
	}

	public void setMedioDePago(MedioDePago medioDePago) {
		this.medioDePago = medioDePago;
	}

	public String getBinTarjeta() {
		return binTarjeta;
	}

	public void setBinTarjeta(String binTarjeta) {
		this.binTarjeta = binTarjeta;
	}

	public Moneda getMoneda() {
		return moneda;
	}

	public void setMoneda(Moneda moneda) {
		this.moneda = moneda;
	}

	public Integer getCantidadCuotas() {
		return cantidadCuotas;
	}

	public void setCantidadCuotas(Integer cantidadCuotas) {
		this.cantidadCuotas = cantidadCuotas;
	}

	public String getNumeroTicketACG() {
		return numeroTicketACG;
	}

	public void setNumeroTicketACG(String numeroTicketACG) {
		this.numeroTicketACG = numeroTicketACG;
	}

	public String getNumeroTarjeta() {
		return numeroTarjeta;
	}

	public void setNumeroTarjeta(String numeroTarjeta) {
		this.numeroTarjeta = numeroTarjeta;
	}

	public BigDecimal getImporte() {
		return importe;
	}

	public void setImporte(BigDecimal importe) {
		this.importe = importe;
	}

	public String getDatosAdicionales() {
		return datosAdicionales;
	}

	public void setDatosAdicionales(String datosAdicionales) {
		if (datosAdicionales != null && datosAdicionales.length() > 500) {
			datosAdicionales = datosAdicionales.substring(0, 499);
		}

		this.datosAdicionales = datosAdicionales;
	}

	public Long getIdUsuario() {
		return idUsuario;
	}

	public void setIdUsuario(Long idUsuario) {
		this.idUsuario = idUsuario;
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
