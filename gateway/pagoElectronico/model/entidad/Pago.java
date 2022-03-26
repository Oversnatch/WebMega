package com.americacg.cargavirtual.gateway.pagoElectronico.model.entidad;

import java.math.BigDecimal;
import java.util.Date;

import com.americacg.cargavirtual.gateway.pagoElectronico.model.ClaseBase;



public class Pago extends ClaseBase {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6811421272128938939L;

	private Long id;
	private Operador operador;	
	private Long idMayorista;
	private ClientePagoElectronico clientePagoElectronico;
	private UsuarioPagoElectronico usuarioPagoElectronico;	
	//private Long idCliente;
	//private Long idUsuario;
	private Transaccion transaccion;
	private TokenOperador tokenOperador;
	private String siteTransactionId;
	private MedioDePago metodoDePago;
	private Moneda moneda;
	private BigDecimal importeBase;
	private BigDecimal porcentajeInteres;
	private BigDecimal importeCalculado;
	private Integer cantidadCuotas;
	private Integer cantidadCuotasOperador;
	private String etiqueta;
	private String nombreComercio;
	private String emailComercio;
	private String emailCliente;
	private String telefonoCliente;	
	private String formaDePago;
	private String merchantId;
	private ConceptoDePago conceptoDePago;
	private String descripcionAdicionalPago;
	private Integer idPagoOperador;
	private EstadoOperador estadoOperador;
	private String numeroTicketOperador;
	private String numeroTicketACG;
	private Long idBanco;
	private String idBancoBCRA;
	private String bancoDenominacion;
	private String cardAuthCode;
	private String addressValCode;
	private String cardData;
	private Date fechaDePago;
	private boolean conAnulacion;
	private Integer codRetornoOperador;
	private Integer codRetornoACG;
	private Long tiempoRespuestaACGms;
	private Long tiempoRespuestaOperadorms;
	private String msgError;
	private Integer cantidadReversas;
	private String observacion;
	private EstadoPagoElectronico estado;
	private String idTerminal;
	private String nombreTitularTarjeta;
	private Long idTipoDocumento;
	private String descripcionTipoDocumento;
	private String nroDocumento;
	private String tipoCarga;
	private String idOperadorAnulacion;
	private String numeroTicketOperadorAnulacion;
	private String numeroTicketACGAnulacion;
	private Date fechaDeServidor;
	private ConfiguracionSite configuracionSite;
	private Date fechaAcreditacionCuenta;
	private Long idMovimientoDeCuenta;
	private boolean acreditadoEnCuenta;

	public Pago() {
		
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

	public Transaccion getTransaccion() {
		return transaccion;
	}

	public void setTransaccion(Transaccion transaccion) {
		this.transaccion = transaccion;
	}

	public TokenOperador getTokenOperador() {
		return tokenOperador;
	}

	public void setTokenOperador(TokenOperador tokenOperador) {
		this.tokenOperador = tokenOperador;
	}

	public String getSiteTransactionId() {
		return siteTransactionId;
	}

	public void setSiteTransactionId(String siteTransactionId) {
		this.siteTransactionId = siteTransactionId;
	}

	public MedioDePago getMetodoDePago() {
		return metodoDePago;
	}

	public void setMetodoDePago(MedioDePago metodoDePago) {
		this.metodoDePago = metodoDePago;
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

	public String getNombreComercio() {
		return nombreComercio;
	}

	public void setNombreComercio(String nombreComercio) {
		this.nombreComercio = nombreComercio;
	}

	public String getEmailComercio() {
		return emailComercio;
	}

	public void setEmailComercio(String emailComercio) {
		this.emailComercio = emailComercio;
	}

	public String getEmailCliente() {
		return emailCliente;
	}

	public void setEmailCliente(String emailCliente) {
		this.emailCliente = emailCliente;
	}

	public String getFormaDePago() {
		return formaDePago;
	}

	public void setFormaDePago(String formaDePago) {
		this.formaDePago = formaDePago;
	}

	public String getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId;
	}

	public ConceptoDePago getConceptoDePago() {
		return conceptoDePago;
	}

	public void setConceptoDePago(ConceptoDePago conceptoDePago) {
		this.conceptoDePago = conceptoDePago;
	}

	public String getDescripcionAdicionalPago() {
		return descripcionAdicionalPago;
	}

	public void setDescripcionAdicionalPago(String descripcionAdicionalPago) {
		this.descripcionAdicionalPago = descripcionAdicionalPago;
	}

	public Integer getIdPagoOperador() {
		return idPagoOperador;
	}

	public void setIdPagoOperador(Integer idPagoOperador) {
		this.idPagoOperador = idPagoOperador;
	}

	public EstadoOperador getEstadoOperador() {
		return estadoOperador;
	}

	public void setEstadoOperador(EstadoOperador estadoOperador) {
		this.estadoOperador = estadoOperador;
	}

	public String getNumeroTicketOperador() {
		return numeroTicketOperador;
	}

	public void setNumeroTicketOperador(String numeroTicketOperador) {
		this.numeroTicketOperador = numeroTicketOperador;
	}

	public String getNumeroTicketACG() {
		return numeroTicketACG;
	}

	public void setNumeroTicketACG(String numeroTicketACG) {
		this.numeroTicketACG = numeroTicketACG;
	}

	public String getCardAuthCode() {
		return cardAuthCode;
	}

	public void setCardAuthCode(String cardAuthCode) {
		this.cardAuthCode = cardAuthCode;
	}

	public String getAddressValCode() {
		return addressValCode;
	}

	public void setAddressValCode(String addressValCode) {
		this.addressValCode = addressValCode;
	}

	public String getCardData() {
		return cardData;
	}

	public void setCardData(String cardData) {
		this.cardData = cardData;
	}

	public Date getFechaDePago() {
		return fechaDePago;
	}

	public void setFechaDePago(Date fechaDePago) {
		this.fechaDePago = fechaDePago;
	}

	public boolean isConAnulacion() {
		return conAnulacion;
	}

	public void setConAnulacion(boolean conAnulacion) {
		this.conAnulacion = conAnulacion;
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

	public Integer getCantidadReversas() {
		return cantidadReversas;
	}

	public void setCantidadReversas(Integer cantidadReversas) {
		this.cantidadReversas = cantidadReversas;
	}

	public String getObservacion() {
		return observacion;
	}

	public void setObservacion(String observacion) {
		this.observacion = observacion;
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

	public Long getIdBanco() {
		return idBanco;
	}

	public void setIdBanco(Long idBanco) {
		this.idBanco = idBanco;
	}

	public String getIdBancoBCRA() {
		return idBancoBCRA;
	}

	public void setIdBancoBCRA(String idBancoBCRA) {
		this.idBancoBCRA = idBancoBCRA;
	}

	public String getBancoDenominacion() {
		return bancoDenominacion;
	}

	public void setBancoDenominacion(String bancoDenominacion) {
		this.bancoDenominacion = bancoDenominacion;
	}

	public BigDecimal getImporteBase() {
		return importeBase;
	}

	public void setImporteBase(BigDecimal importeBase) {
		this.importeBase = importeBase;
	}

	public BigDecimal getPorcentajeInteres() {
		return porcentajeInteres;
	}

	public void setPorcentajeInteres(BigDecimal porcentajeInteres) {
		this.porcentajeInteres = porcentajeInteres;
	}

	public BigDecimal getImporteCalculado() {
		return importeCalculado;
	}

	public void setImporteCalculado(BigDecimal importeCalculado) {
		this.importeCalculado = importeCalculado;
	}

	public String getIdTerminal() {
		return idTerminal;
	}

	public void setIdTerminal(String idTerminal) {
		this.idTerminal = idTerminal;
	}

	public ClientePagoElectronico getClientePagoElectronico() {
		return clientePagoElectronico;
	}

	public void setClientePagoElectronico(ClientePagoElectronico clientePagoElectronico) {
		this.clientePagoElectronico = clientePagoElectronico;
	}

	public UsuarioPagoElectronico getUsuarioPagoElectronico() {
		return usuarioPagoElectronico;
	}

	public void setUsuarioPagoElectronico(UsuarioPagoElectronico usuarioPagoElectronico) {
		this.usuarioPagoElectronico = usuarioPagoElectronico;
	}

	public String getTelefonoCliente() {
		return telefonoCliente;
	}

	public void setTelefonoCliente(String telefonoCliente) {
		this.telefonoCliente = telefonoCliente;
	}

	public String getNombreTitularTarjeta() {
		return nombreTitularTarjeta;
	}

	public void setNombreTitularTarjeta(String nombreTitularTarjeta) {
		this.nombreTitularTarjeta = nombreTitularTarjeta;
	}

	public Long getIdTipoDocumento() {
		return idTipoDocumento;
	}

	public void setIdTipoDocumento(Long idTipoDocumento) {
		this.idTipoDocumento = idTipoDocumento;
	}

	public String getDescripcionTipoDocumento() {
		return descripcionTipoDocumento;
	}

	public void setDescripcionTipoDocumento(String descripcionTipoDocumento) {
		this.descripcionTipoDocumento = descripcionTipoDocumento;
	}

	public String getNroDocumento() {
		return nroDocumento;
	}

	public void setNroDocumento(String nroDocumento) {
		this.nroDocumento = nroDocumento;
	}

	public String getTipoCarga() {
		return tipoCarga;
	}

	public void setTipoCarga(String tipoCarga) {
		this.tipoCarga = tipoCarga;
	}

	public String getIdOperadorAnulacion() {
		return idOperadorAnulacion;
	}

	public void setIdOperadorAnulacion(String idOperadorAnulacion) {
		this.idOperadorAnulacion = idOperadorAnulacion;
	}

	public String getNumeroTicketOperadorAnulacion() {
		return numeroTicketOperadorAnulacion;
	}

	public void setNumeroTicketOperadorAnulacion(String numeroTicketOperadorAnulacion) {
		this.numeroTicketOperadorAnulacion = numeroTicketOperadorAnulacion;
	}

	public String getNumeroTicketACGAnulacion() {
		return numeroTicketACGAnulacion;
	}

	public void setNumeroTicketACGAnulacion(String numeroTicketACGAnulacion) {
		this.numeroTicketACGAnulacion = numeroTicketACGAnulacion;
	}

	public Date getFechaDeServidor() {
		return fechaDeServidor;
	}

	public void setFechaDeServidor(Date fechaDeServidor) {
		this.fechaDeServidor = fechaDeServidor;
	}

	public ConfiguracionSite getConfiguracionSite() {
		return configuracionSite;
	}

	public void setConfiguracionSite(ConfiguracionSite configuracionSite) {
		this.configuracionSite = configuracionSite;
	}

	public final Date getFechaAcreditacionCuenta() {
		return fechaAcreditacionCuenta;
	}

	public final void setFechaAcreditacionCuenta(Date fechaAcreditacionCuenta) {
		this.fechaAcreditacionCuenta = fechaAcreditacionCuenta;
	}

	public final boolean isAcreditadoEnCuenta() {
		return acreditadoEnCuenta;
	}

	public final void setAcreditadoEnCuenta(boolean acreditadoEnCuenta) {
		this.acreditadoEnCuenta = acreditadoEnCuenta;
	}

	public final Long getIdMovimientoDeCuenta() {
		return idMovimientoDeCuenta;
	}

	public final void setIdMovimientoDeCuenta(Long idMovimientoDeCuenta) {
		this.idMovimientoDeCuenta = idMovimientoDeCuenta;
	}

	public Integer getCantidadCuotasOperador() {
		return cantidadCuotasOperador;
	}

	public void setCantidadCuotasOperador(Integer cantidadCuotasOperador) {
		this.cantidadCuotasOperador = cantidadCuotasOperador;
	}

	public String getEtiqueta() {
		return etiqueta;
	}

	public void setEtiqueta(String etiqueta) {
		this.etiqueta = etiqueta;
	}

	
}
