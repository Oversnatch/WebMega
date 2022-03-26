package com.americacg.cargavirtual.gateway.pagoElectronico.model.entidad;

import java.math.BigDecimal;
import java.util.Date;

import com.americacg.cargavirtual.gateway.pagoElectronico.model.ClaseBase;

public class Anulacion extends ClaseBase {
	/**
	 * 
	 */
	private static final long serialVersionUID = -938128684328525231L;
	private Long id;
	private Operador operador;	
	private Date fechaDeAnulacion;
	private String numeroTicketACG;
	private String numeroTicketOperador;
	private Integer idAnulacionOperador;
	private BigDecimal importe;
	private String estadoOperador;
	private String estadoDescripcion;
	private ConfiguracionSite configuracionSite;

	public Anulacion() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getFechaDeAnulacion() {
		return fechaDeAnulacion;
	}

	public void setFechaDeAnulacion(Date fechaDeAnulacion) {
		this.fechaDeAnulacion = fechaDeAnulacion;
	}

	public String getNumeroTicketACG() {
		return numeroTicketACG;
	}

	public void setNumeroTicketACG(String numeroTicketACG) {
		this.numeroTicketACG = numeroTicketACG;
	}

	public String getNumeroTicketOperador() {
		return numeroTicketOperador;
	}

	public void setNumeroTicketOperador(String numeroTicketOperador) {
		this.numeroTicketOperador = numeroTicketOperador;
	}

	public Integer getIdAnulacionOperador() {
		return idAnulacionOperador;
	}

	public void setIdAnulacionOperador(Integer idAnulacionOperador) {
		this.idAnulacionOperador = idAnulacionOperador;
	}

	public BigDecimal getImporte() {
		return importe;
	}

	public void setImporte(BigDecimal importe) {
		this.importe = importe;
	}

	public String getEstadoOperador() {
		return estadoOperador;
	}

	public void setEstadoOperador(String estadoOperador) {
		this.estadoOperador = estadoOperador;
	}

	public String getEstadoDescripcion() {
		return estadoDescripcion;
	}

	public void setEstadoDescripcion(String estadoDescripcion) {
		this.estadoDescripcion = estadoDescripcion;
	}

	public Operador getOperador() {
		return operador;
	}

	public void setOperador(Operador operador) {
		this.operador = operador;
	}

	public ConfiguracionSite getConfiguracionSite() {
		return configuracionSite;
	}

	public void setConfiguracionSite(ConfiguracionSite configuracionSite) {
		this.configuracionSite = configuracionSite;
	}
}
