package com.americacg.cargavirtual.gateway.pagoElectronico.model.entidad;

import java.util.Date;

import com.americacg.cargavirtual.gateway.pagoElectronico.model.ClaseBase;

public class ConfiguracionComercio extends ClaseBase {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8679353444426503854L;
	private Long id;
	private Long idMayorista;
	private Long idCliente;
	private ConfiguracionSite configuracionSite;
	private String idMerchant;
	private String nombre;
	private String email;
	private Date fechaAlta;
	private Integer estado;

	public ConfiguracionComercio() {
	}

	public ConfiguracionComercio(Long id, Long idMayorista, Long idCliente, ConfiguracionSite configuracionSite,
			String idMerchant, String nombre, String email, Date fechaAlta, Integer estado) {
		super();
		this.id = id;
		this.idMayorista = idMayorista;
		this.idCliente = idCliente;
		this.configuracionSite = configuracionSite;
		this.idMerchant = idMerchant;
		this.nombre = nombre;
		this.email = email;
		this.fechaAlta = fechaAlta;
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

	public ConfiguracionSite getConfiguracionSite() {
		return configuracionSite;
	}

	public void setConfiguracionSite(ConfiguracionSite configuracionSite) {
		this.configuracionSite = configuracionSite;
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

	public String getIdMerchant() {
		return idMerchant;
	}

	public void setIdMerchant(String idMerchant) {
		this.idMerchant = idMerchant;
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
}
