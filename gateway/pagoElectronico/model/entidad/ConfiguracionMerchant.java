package com.americacg.cargavirtual.gateway.pagoElectronico.model.entidad;

import java.io.Serializable;
import java.util.Date;

import com.americacg.cargavirtual.gateway.pagoElectronico.model.ClaseBase;

public class ConfiguracionMerchant extends ClaseBase implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2275757466984321962L;
	private Long id;
	private Long idCliente;
	private String idMerchant;
	private String nombre;
	private String email;
	private Date fechaAlta;
	private Integer estado;

	public ConfiguracionMerchant() {
	}

	public ConfiguracionMerchant(Long id, Long idCliente, String idMerchant, String nombre, String email,
			Date fechaAlta, Integer estado) {
		super();
		this.id = id;
		this.idCliente = idCliente;
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

	public Long getIdCliente() {
		return idCliente;
	}

	public void setIdCliente(Long idCliente) {
		this.idCliente = idCliente;
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
	};
}
