package com.americacg.cargavirtual.gateway.pagoElectronico.model.entidad;

import java.io.Serializable;
import java.util.Date;

import com.americacg.cargavirtual.gateway.pagoElectronico.model.ClaseBase;

public class ConfiguracionSite extends ClaseBase implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6233608018765463066L;
	private Long id;
	private Long idMayorista;
	private String nombre;
	private String email;
	private String siteId;
	private Operador operador;
	private String nroLocacion;
	private String iataCode;
	private String publicApiKey;
	private String privateApiKey;
	private Date fechaAlta;
	private Integer estado;
	// Esta estructura esta armada para retornar si el cliente tiene un merchant
	// configurado
	// Tener en cuenta que el modelo de datos refleja N merchant para un mayorista.
	private ConfiguracionMerchant configuracionMerchant;

	public ConfiguracionSite() {
	}

	public ConfiguracionSite(Long id, Long idMayorista, String nombre, String email, String siteId, Operador operador, String nroLocacion,
			String iataCode, String publicApiKey, String privateApiKey, Date fechaAlta, Integer estado,
			ConfiguracionMerchant configuracionMerchant) {
		super();
		this.id = id;
		this.idMayorista = idMayorista;
		this.nombre = nombre;
		this.email = email;
		this.siteId = siteId;
		this.operador = operador;
		this.nroLocacion = nroLocacion;
		this.iataCode = iataCode;
		this.publicApiKey = publicApiKey;
		this.privateApiKey = privateApiKey;
		this.fechaAlta = fechaAlta;
		this.estado = estado;
		this.configuracionMerchant = configuracionMerchant;
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

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getSiteId() {
		return siteId;
	}

	public void setSiteId(String siteId) {
		this.siteId = siteId;
	}

	public String getNroLocacion() {
		return nroLocacion;
	}

	public void setNroLocacion(String nroLocacion) {
		this.nroLocacion = nroLocacion;
	}

	public String getIataCode() {
		return iataCode;
	}

	public void setIataCode(String iataCode) {
		this.iataCode = iataCode;
	}

	public String getPublicApiKey() {
		return publicApiKey;
	}

	public void setPublicApiKey(String publicApiKey) {
		this.publicApiKey = publicApiKey;
	}

	public String getPrivateApiKey() {
		return privateApiKey;
	}

	public void setPrivateApiKey(String privateApiKey) {
		this.privateApiKey = privateApiKey;
	}

	public Date getFechaAlta() {
		return fechaAlta;
	}

	public void setFechaAlta(Date fechaAlta) {
		this.fechaAlta = fechaAlta;
	}

	public Integer getEstado() {
		return estado;
	}

	public void setEstado(Integer estado) {
		this.estado = estado;
	}

	public ConfiguracionMerchant getConfiguracionMerchant() {
		return configuracionMerchant;
	}

	public void setConfiguracionMerchant(ConfiguracionMerchant configuracionMerchant) {
		this.configuracionMerchant = configuracionMerchant;
	}

	public Operador getOperador() {
		return operador;
	}

	public void setOperador(Operador operador) {
		this.operador = operador;
	}

}
