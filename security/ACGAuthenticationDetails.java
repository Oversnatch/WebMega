package com.americacg.cargavirtual.web.security;

import java.net.InetAddress;
import java.net.UnknownHostException;
import javax.servlet.http.HttpServletRequest;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

/*
 * Esta clase almacena datos usado para la autenticacion 
 */

public class ACGAuthenticationDetails extends WebAuthenticationDetails {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8463011707186466999L;
	private final String dominio;
	private final String path;
	private final String captcha;
	private final String coordenada1;
	private final String coordenada2;
	private final String ipInetAddress;

	public ACGAuthenticationDetails(final HttpServletRequest request) {
		super(request);
		
		if (request != null) {
			dominio = request.getServerName();
			String[] sArray = request.getRequestURL().toString().split("/");

			if (sArray != null && sArray.length >= 4) {
				path = sArray[3];
			} else {
				path = "";
			}

			if (request.getParameterMap().size() > 0) {
				captcha = request.getParameter("captcha");
				coordenada1 = request.getParameter("coordenada1");
				coordenada2 = request.getParameter("coordenada2");
			} else {
				captcha = "";
				coordenada1 = "";
				coordenada2 = "";
			}

			InetAddress ipCliente = null;
			try {
				ipCliente = InetAddress.getLocalHost();
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
			}
			if (ipCliente != null) {
				ipInetAddress = ipCliente.getHostAddress();
			} else {
				ipInetAddress = "";
			}
		}
		else {
			this.path = "";
			this.dominio = "";
			this.captcha = "";
			this.coordenada1 = "";
			this.coordenada2 = "";
			this.ipInetAddress = "";
		}
	}

	public String getDominio() {
		return dominio;
	}

	public String getPath() {
		return path;
	}

	public String getCaptcha() {
		return captcha;
	}

	public String getCoordenada1() {
		return coordenada1;
	}

	public String getCoordenada2() {
		return coordenada2;
	}

	public String getIpInetAddress() {
		return ipInetAddress;
	}
}