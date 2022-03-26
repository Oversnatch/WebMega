package com.americacg.cargavirtual.gateway.pagoElectronico.model.entidad;

import java.util.Date;

import com.americacg.cargavirtual.gateway.pagoElectronico.model.ClaseBase;

public class MedioDePago extends ClaseBase {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8744312168436306818L;
	private Long id;
	private Operador operador;
	private Date fechaAlta;
	private Integer idMedioDePagoOperador;
	private String marca;
	private Integer longitudMinPAN;
	private Integer longitudMaxPAN;
	private String formatoCVV;
	private String tipoMedioDePago;
	private String imagen;
	private Integer estado;

	public MedioDePago() {

	}

	public MedioDePago(Long id) {
		this.id = id;
	}

	public MedioDePago(Long id, Operador operador, Date fechaAlta, Integer idMedioDePagoOperador, String marca,
			Integer longitudMinPAN, Integer longitudMaxPAN, String formatoCVV, String tipoMedioDePago, Integer estado) {
		super();
		this.id = id;
		this.operador = operador;
		this.fechaAlta = fechaAlta;
		this.idMedioDePagoOperador = idMedioDePagoOperador;
		this.marca = marca;
		this.longitudMinPAN = longitudMinPAN;
		this.longitudMaxPAN = longitudMaxPAN;
		this.formatoCVV = formatoCVV;
		this.tipoMedioDePago = tipoMedioDePago;
		this.estado = estado;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getFechaAlta() {
		return fechaAlta;
	}

	public void setFechaAlta(Date fechaAlta) {
		this.fechaAlta = fechaAlta;
	}

	public Integer getIdMedioDePagoOperador() {
		return idMedioDePagoOperador;
	}

	public void setIdMedioDePagoOperador(Integer idMedioDePagoOperador) {
		this.idMedioDePagoOperador = idMedioDePagoOperador;
	}

	public String getMarca() {
		return marca;
	}

	public void setMarca(String marca) {
		this.marca = marca;
	}

	public Integer getLongitudMinPAN() {
		return longitudMinPAN;
	}

	public void setLongitudMinPAN(Integer longitudMinPAN) {
		this.longitudMinPAN = longitudMinPAN;
	}

	public Integer getLongitudMaxPAN() {
		return longitudMaxPAN;
	}

	public void setLongitudMaxPAN(Integer longitudMaxPAN) {
		this.longitudMaxPAN = longitudMaxPAN;
	}

	public String getFormatoCVV() {
		return formatoCVV;
	}

	public void setFormatoCVV(String formatoCVV) {
		this.formatoCVV = formatoCVV;
	}

	public String getTipoMedioDePago() {
		return tipoMedioDePago;
	}

	public void setTipoMedioDePago(String tipoMedioDePago) {
		this.tipoMedioDePago = tipoMedioDePago;
	}

	public Integer getEstado() {
		return estado;
	}

	public void setEstado(Integer estado) {
		this.estado = estado;
	}

	public String getImagen() {
		return imagen;
	}

	public void setImagen(String imagen) {
		this.imagen = imagen;
	}

	public Operador getOperador() {
		return operador;
	}

	public void setOperador(Operador operador) {
		this.operador = operador;
	}
}
