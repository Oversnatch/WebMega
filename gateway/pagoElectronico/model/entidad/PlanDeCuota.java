package com.americacg.cargavirtual.gateway.pagoElectronico.model.entidad;

import java.math.BigDecimal;
import java.util.Date;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import com.americacg.cargavirtual.gateway.pagoElectronico.model.ClaseBase;
import com.americacg.cargavirtual.web.model.superPago.Cuenta;

public class PlanDeCuota extends ClaseBase {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7655090748821004682L;
	private Long id;
	private Operador operador;
	private Date fechaAlta;
	private BancoPagoElectronico banco;
	private MedioDePago medioDePago;
	private Integer cantidadDeCuotas;
	private BigDecimal porcentajeInteres;
	private BigDecimal importeCuota;
	private Date fechaInicioVigencia;
	private Date fechaFinVigencia;
	private String etiqueta;
	private Integer estado;

	public PlanDeCuota() {

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

	public BancoPagoElectronico getBanco() {
		return banco;
	}

	public void setBanco(BancoPagoElectronico banco) {
		this.banco = banco;
	}

	public MedioDePago getMedioDePago() {
		return medioDePago;
	}

	public void setMedioDePago(MedioDePago medioDePago) {
		this.medioDePago = medioDePago;
	}

	public Integer getCantidadDeCuotas() {
		return cantidadDeCuotas;
	}

	public void setCantidadDeCuotas(Integer cantidadDeCuotas) {
		this.cantidadDeCuotas = cantidadDeCuotas;
	}

	public BigDecimal getPorcentajeInteres() {
		return porcentajeInteres;
	}

	public void setPorcentajeInteres(BigDecimal porcentajeInteres) {
		this.porcentajeInteres = porcentajeInteres;
	}

	public Date getFechaInicioVigencia() {
		return fechaInicioVigencia;
	}

	public void setFechaInicioVigencia(Date fechaInicioVigencia) {
		this.fechaInicioVigencia = fechaInicioVigencia;
	}

	public Date getFechaFinVigencia() {
		return fechaFinVigencia;
	}

	public void setFechaFinVigencia(Date fechaFinVigencia) {
		this.fechaFinVigencia = fechaFinVigencia;
	}

	public String getEtiqueta() {
		return etiqueta;
	}

	public void setEtiqueta(String etiqueta) {
		this.etiqueta = etiqueta;
	}

	public Integer getEstado() {
		return estado;
	}

	public void setEstado(Integer estado) {
		this.estado = estado;
	}

	public Operador getOperador() {
		return operador;
	}

	public void setOperador(Operador operador) {
		this.operador = operador;
	}

	public BigDecimal getImporteCuota() {
		return importeCuota;
	}

	public void setImporteCuota(BigDecimal importeCuota) {
		this.importeCuota = importeCuota;
	}

	@Override
	public int hashCode() {
		HashCodeBuilder hcb = new HashCodeBuilder();
		
		hcb.append(this.getId());
		hcb.append(this.getBanco());
		hcb.append(this.getCantidadDeCuotas());

		return hcb.toHashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof PlanDeCuota))
			return false;
		PlanDeCuota other = (PlanDeCuota) obj;

		EqualsBuilder eb = new EqualsBuilder();
		
		if(this.getId() == null || other.getId() == null){
			eb.append(this.getBanco(), other.getBanco());
			eb.append(this.getCantidadDeCuotas(), other.getCantidadDeCuotas());
		}
		else
			eb.append(getId(), other.getId());
		
		return eb.isEquals();
	}

}
