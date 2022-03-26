package com.americacg.cargavirtual.web.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

@Component
public class ACGAccessDeniedHandler implements AccessDeniedHandler {
	protected final Log logger = LogFactory.getLog(getClass());
	private String errorPage;

	public ACGAccessDeniedHandler() {

	}

	public ACGAccessDeniedHandler(String errorPage) {
		super();
		this.errorPage = errorPage;
	}

	public String getErrorPage() {
		return errorPage;
	}

	public void setErrorPage(String errorPage) {
		this.errorPage = errorPage;
	}

	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response,
			AccessDeniedException accessDeniedException) throws IOException, ServletException {
		// TODO Auto-generated method stub
		final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (logger.isDebugEnabled()) {
			logger.debug(String.format("El usuario [%s] intento acceder a la url protegida [%s]!",
					authentication.getName(), request.getRequestURI()));
		}

		boolean ajaxRedirect = request.getHeader("faces-request") != null
				&& request.getHeader("faces-request").toLowerCase().indexOf("ajax") > -1;

		if (ajaxRedirect) {
			String ajaxRedirectXml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + "<partial-response><redirect url=\""
					+ request.getContextPath() + this.getErrorPage() + "\"></redirect></partial-response>";
			response.setContentType("text/xml");
			response.getWriter().write(ajaxRedirectXml);
		} else {
			response.sendRedirect(request.getContextPath() + this.getErrorPage());
		}

	}

}
