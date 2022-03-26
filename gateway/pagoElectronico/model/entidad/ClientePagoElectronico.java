package com.americacg.cargavirtual.gateway.pagoElectronico.model.entidad;

import com.americacg.cargavirtual.gateway.pagoElectronico.model.ClaseBase;

public class ClientePagoElectronico extends ClaseBase{
	/**
	 * 
	 */
	private static final long serialVersionUID = -6339755807522241192L;
	private Long id;
	private String razonSocial;
	private String nombreFantasia;
	private String CUIT;
	private String tipoCliente;
	private Long idDistribuidor1;
	private String razonSocialDist1;
	private Long idDistribuidor2;
	private String razonSocialDist2;
	private Long idDistribuidor3;
	private String razonSocialDist3;
	private Long idDistribuidor4;
	private String razonSocialDist4;
	private Long idDistribuidor5;
	private String razonSocialDist5;
	
	public ClientePagoElectronico() {
	}
	
	public ClientePagoElectronico(Long id) {
		this.id = id;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getRazonSocial() {
		return razonSocial;
	}

	public void setRazonSocial(String razonSocial) {
		this.razonSocial = razonSocial;
	}

	public String getCUIT() {
		return CUIT;
	}

	public void setCUIT(String cUIT) {
		CUIT = cUIT;
	}

	public String getTipoCliente() {
		return tipoCliente;
	}

	public void setTipoCliente(String tipoCliente) {
		this.tipoCliente = tipoCliente;
	}

	public Long getIdDistribuidor1() {
		return idDistribuidor1;
	}

	public void setIdDistribuidor1(Long idDistribuidor1) {
		this.idDistribuidor1 = idDistribuidor1;
	}

	public String getRazonSocialDist1() {
		return razonSocialDist1;
	}

	public void setRazonSocialDist1(String razonSocialDist1) {
		this.razonSocialDist1 = razonSocialDist1;
	}

	public Long getIdDistribuidor2() {
		return idDistribuidor2;
	}

	public void setIdDistribuidor2(Long idDistribuidor2) {
		this.idDistribuidor2 = idDistribuidor2;
	}

	public String getRazonSocialDist2() {
		return razonSocialDist2;
	}

	public void setRazonSocialDist2(String razonSocialDist2) {
		this.razonSocialDist2 = razonSocialDist2;
	}

	public Long getIdDistribuidor3() {
		return idDistribuidor3;
	}

	public void setIdDistribuidor3(Long idDistribuidor3) {
		this.idDistribuidor3 = idDistribuidor3;
	}

	public String getRazonSocialDist3() {
		return razonSocialDist3;
	}

	public void setRazonSocialDist3(String razonSocialDist3) {
		this.razonSocialDist3 = razonSocialDist3;
	}

	public Long getIdDistribuidor4() {
		return idDistribuidor4;
	}

	public void setIdDistribuidor4(Long idDistribuidor4) {
		this.idDistribuidor4 = idDistribuidor4;
	}

	public String getRazonSocialDist4() {
		return razonSocialDist4;
	}

	public void setRazonSocialDist4(String razonSocialDist4) {
		this.razonSocialDist4 = razonSocialDist4;
	}

	public Long getIdDistribuidor5() {
		return idDistribuidor5;
	}

	public void setIdDistribuidor5(Long idDistribuidor5) {
		this.idDistribuidor5 = idDistribuidor5;
	}

	public String getRazonSocialDist5() {
		return razonSocialDist5;
	}

	public void setRazonSocialDist5(String razonSocialDist5) {
		this.razonSocialDist5 = razonSocialDist5;
	}

	public final String getNombreFantasia() {
		return nombreFantasia;
	}

	public final void setNombreFantasia(String nombreFantasia) {
		this.nombreFantasia = nombreFantasia;
	}

}
