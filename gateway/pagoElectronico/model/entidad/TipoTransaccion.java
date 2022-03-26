package com.americacg.cargavirtual.gateway.pagoElectronico.model.entidad;

import java.io.Serializable;

import org.springframework.http.HttpMethod;

public class TipoTransaccion implements Serializable  {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5858655197676423099L;
	private Long id;
	private Operador operador;	
	private String codigoFuncion;
	private String descripcion;
	private boolean funcionPublica;
	private boolean grabaTransaccion;
	private boolean grabaLogDatosSensibles;
	private String filtroDatosSensibles;
	private boolean requiereCampoData;
	private boolean requiereBodyREST;
	private boolean requierePrivateApiKey;
	private boolean requierePublicApiKey;
	private String funcionGtwOperador;
	private String parametroGtwOperador;
	private HttpMethod tipoRequest;
	private Integer estado;
	
	public TipoTransaccion(){}

	public TipoTransaccion(Long id, Operador operador, String codigoFuncion, String descripcion, boolean funcionPublica,
			boolean grabaTransaccion, boolean grabaLogDatosSensibles, String filtroDatosSensibles,
			boolean requiereCampoData, boolean requiereBodyREST, boolean requierePrivateApiKey,
			boolean requierePublicApiKey, String funcionGtwOperador, String parametroGtwOperador, HttpMethod tipoRequest,
			Integer estado) {
		super();
		this.id = id;
		this.operador  = operador;
		this.codigoFuncion = codigoFuncion;
		this.descripcion = descripcion;
		this.funcionPublica = funcionPublica;
		this.grabaTransaccion = grabaTransaccion;
		this.grabaLogDatosSensibles = grabaLogDatosSensibles;
		this.filtroDatosSensibles = filtroDatosSensibles;
		this.requiereCampoData = requiereCampoData;
		this.requiereBodyREST = requiereBodyREST;
		this.requierePrivateApiKey = requierePrivateApiKey;
		this.requierePublicApiKey = requierePublicApiKey;
		this.funcionGtwOperador = funcionGtwOperador;
		this.parametroGtwOperador = parametroGtwOperador;
		this.tipoRequest = tipoRequest;
		this.estado = estado;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCodigoFuncion() {
		return codigoFuncion;
	}

	public void setCodigoFuncion(String codigoFuncion) {
		this.codigoFuncion = codigoFuncion;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public boolean isFuncionPublica() {
		return funcionPublica;
	}

	public void setFuncionPublica(boolean funcionPublica) {
		this.funcionPublica = funcionPublica;
	}

	public boolean isGrabaTransaccion() {
		return grabaTransaccion;
	}

	public void setGrabaTransaccion(boolean grabaTransaccion) {
		this.grabaTransaccion = grabaTransaccion;
	}

	public boolean isGrabaLogDatosSensibles() {
		return grabaLogDatosSensibles;
	}

	public void setGrabaLogDatosSensibles(boolean grabaLogDatosSensibles) {
		this.grabaLogDatosSensibles = grabaLogDatosSensibles;
	}

	public String getFiltroDatosSensibles() {
		return filtroDatosSensibles;
	}

	public void setFiltroDatosSensibles(String filtroDatosSensibles) {
		this.filtroDatosSensibles = filtroDatosSensibles;
	}

	public boolean isRequiereCampoData() {
		return requiereCampoData;
	}

	public void setRequiereCampoData(boolean requiereCampoData) {
		this.requiereCampoData = requiereCampoData;
	}

	public boolean isRequiereBodyREST() {
		return requiereBodyREST;
	}

	public void setRequiereBodyREST(boolean requiereBodyREST) {
		this.requiereBodyREST = requiereBodyREST;
	}

	public boolean isRequierePrivateApiKey() {
		return requierePrivateApiKey;
	}

	public void setRequierePrivateApiKey(boolean requierePrivateApiKey) {
		this.requierePrivateApiKey = requierePrivateApiKey;
	}

	public boolean isRequierePublicApiKey() {
		return requierePublicApiKey;
	}

	public void setRequierePublicApiKey(boolean requierePublicApiKey) {
		this.requierePublicApiKey = requierePublicApiKey;
	}

	public String getFuncionGtwOperador() {
		return funcionGtwOperador;
	}

	public void setFuncionGtwOperador(String funcionGtwOperador) {
		this.funcionGtwOperador = funcionGtwOperador;
	}

	public String getParametroGtwOperador() {
		return parametroGtwOperador;
	}

	public void setParametroGtwOperador(String parametroGtwOperador) {
		this.parametroGtwOperador = parametroGtwOperador;
	}

	public HttpMethod getTipoRequest() {
		return tipoRequest;
	}

	public void setTipoRequest(HttpMethod tipoRequest) {
		this.tipoRequest = tipoRequest;
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

}
