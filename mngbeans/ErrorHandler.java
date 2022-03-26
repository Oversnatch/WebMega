package com.americacg.cargavirtual.web.mngbeans;

import javax.faces.context.FacesContext;
import javax.inject.Named;
import org.springframework.web.context.annotation.RequestScope;

@Named("errorHandler")
@RequestScope
public class ErrorHandler {

	public Integer getStatusCode() {
		Integer val = -1;
		if (FacesContext.getCurrentInstance().getExternalContext().getRequestMap()
				.get("javax.servlet.error.status_code") != null) {
			val = (Integer) (FacesContext.getCurrentInstance().getExternalContext().getRequestMap()
					.get("javax.servlet.error.status_code"));
		}
		return val;
	}

	public String getMessage() {
		String resp = "S/I";
		Integer val = null;
		try {

			if (FacesContext.getCurrentInstance().getExternalContext().getRequestMap()
					.get("javax.servlet.error.status_code") != null) {

				val = (Integer) (FacesContext.getCurrentInstance().getExternalContext().getRequestMap()
						.get("javax.servlet.error.status_code"));

				switch (val) {
				case 404:
					resp = "Recurso no disponible o no encontrado.";
					break;
				case 500:
					resp = "Error del servicio.";
					break;
				default:
					resp = String.valueOf(FacesContext.getCurrentInstance().getExternalContext().

							getRequestMap().get("javax.servlet.error.message"));
					break;
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			resp = e.getMessage();

		}

		return resp;
	}

	public String getExceptionType() {
		String val = "";
		if (FacesContext.getCurrentInstance().getExternalContext().getRequestMap()
				.get("javax.servlet.error.exception_type") != null) {
			val = String.valueOf(FacesContext.getCurrentInstance().getExternalContext().getRequestMap()
					.get("javax.servlet.error.exception_type"));
		}
		return val;
	}

	public String getException() {

		String val = "";

		if (FacesContext.getCurrentInstance().getExternalContext().getRequestMap()
				.get("javax.servlet.error.exception") != null) {
			val = String.valueOf((Exception) FacesContext.getCurrentInstance().getExternalContext().getRequestMap()
					.get("javax.servlet.error.exception"));
		}
		return val;
	}

	public String getRequestURI() {
		return (String) FacesContext.getCurrentInstance().getExternalContext().getRequestMap()
				.get("javax.servlet.error.request_uri");
	}

	public String getServletName() {
		return (String) FacesContext.getCurrentInstance().getExternalContext().getRequestMap()
				.get("javax.servlet.error.servlet_name");
	}

}