package com.americacg.cargavirtual.gateway.pagoElectronico.model;

import java.io.Serializable;
import java.util.Date;

import com.americacg.cargavirtual.gateway.pagoElectronico.enums.FuncionName;

public class HeaderInGateway implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8695458945440088998L;

	private String hashSecurity = null;
	private String proveedor = null; 
	private String version = null; 
	private String canal = null;
	private String origen = null;
	private FuncionName funcion = null;
	private String marcaPOS = null; 
	private String nroSeriePos = null;
	private String imeiPOS = null; 
	private String imeiSIM = null;
	private String medioConexion = null;
	private String nroTelefonoDelSIM = null;
	private String modelo = null; 
	private Long idMayorista = null; 
	private Long idCliente = null; 
	private Long idUsuario = null;
	private String usuario = null; 
	private String clave = null; 
	private Date fechaCliente = null;
	private String idTransaccionCliente = null; 
	private String tipoCliente = null;
	private String versionPOS = null;
	private String tokenAuth = null;
	private String idConfiguracionComercio = null;
	
	public HeaderInGateway(){
		
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getCanal() {
		return canal;
	}

	public void setCanal(String canal) {
		this.canal = canal;
	}

	public FuncionName getFuncion() {
		return funcion;
	}

	public void setFuncion(FuncionName funcion) {
		this.funcion = funcion;
	}

	public String getMarcaPOS() {
		return marcaPOS;
	}

	public void setMarcaPOS(String marcaPOS) {
		this.marcaPOS = marcaPOS;
	}

	public String getNroSeriePos() {
		return nroSeriePos;
	}

	public void setNroSeriePos(String nroSeriePos) {
		this.nroSeriePos = nroSeriePos;
	}

	public String getImeiPOS() {
		return imeiPOS;
	}

	public void setImeiPOS(String imeiPOS) {
		this.imeiPOS = imeiPOS;
	}

	public String getImeiSIM() {
		return imeiSIM;
	}

	public void setImeiSIM(String imeiSIM) {
		this.imeiSIM = imeiSIM;
	}

	public String getMedioConexion() {
		return medioConexion;
	}

	public void setMedioConexion(String medioConexion) {
		this.medioConexion = medioConexion;
	}

	public String getNroTelefonoDelSIM() {
		return nroTelefonoDelSIM;
	}

	public void setNroTelefonoDelSIM(String nroTelefonoDelSIM) {
		this.nroTelefonoDelSIM = nroTelefonoDelSIM;
	}

	public String getModelo() {
		return modelo;
	}

	public void setModelo(String modelo) {
		this.modelo = modelo;
	}

	public String getProveedor() {
		return proveedor;
	}

	public void setProveedor(String proveedor) {
		this.proveedor = proveedor;
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

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public String getClave() {
		return clave;
	}

	public void setClave(String clave) {
		this.clave = clave;
	}

	public Date getFechaCliente() {
		return fechaCliente;
	}

	public void setFechaCliente(Date fechaCliente) {
		this.fechaCliente = fechaCliente;
	}

	public String getIdTransaccionCliente() {
		if(idTransaccionCliente == null || idTransaccionCliente.isEmpty())
			idTransaccionCliente = " ";
		
		return idTransaccionCliente;
	}

	public void setIdTransaccionCliente(String idTransaccionCliente) {
		this.idTransaccionCliente = idTransaccionCliente;
	}

	public String getTipoCliente() {
		return tipoCliente;
	}

	public void setTipoCliente(String tipoCliente) {
		this.tipoCliente = tipoCliente;
	}

	public String getHashSecurity() {
		return hashSecurity;
	}

	public void setHashSecurity(String hashSecurity) {
		this.hashSecurity = hashSecurity;
	}

	public String getVersionPOS() {
		return versionPOS;
	}

	public void setVersionPOS(String versionPOS) {
		this.versionPOS = versionPOS;
	}

	public String getTokenAuth() {
		return tokenAuth;
	}

	public void setTokenAuth(String tokenAuth) {
		this.tokenAuth = tokenAuth;
	}

	public String getIdConfiguracionComercio() {
		return idConfiguracionComercio;
	}

	public void setIdConfiguracionComercio(String idConfiguracionComercio) {
		this.idConfiguracionComercio = idConfiguracionComercio;
	}

	public String getOrigen() {
		return origen;
	}

	public void setOrigen(String origen) {
		this.origen = origen;
	}
}
