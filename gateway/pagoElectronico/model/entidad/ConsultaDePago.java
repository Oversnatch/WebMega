package com.americacg.cargavirtual.gateway.pagoElectronico.model.entidad;

import com.americacg.cargavirtual.gateway.pagoElectronico.model.ClaseBase;

public class ConsultaDePago extends ClaseBase {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Integer id;
	private String site_transaction_id;
	private String status;

	public ConsultaDePago() {
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getSite_transaction_id() {
		return site_transaction_id;
	}

	public void setSite_transaction_id(String site_transaction_id) {
		this.site_transaction_id = site_transaction_id;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
