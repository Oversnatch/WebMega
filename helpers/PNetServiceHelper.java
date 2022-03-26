package com.americacg.cargavirtual.web.helpers;

import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.BindingProvider;
import org.springframework.stereotype.Component;

import com.americacg.cargavirtual.pnet.gateway.ICargaVirtualGatewayPNet;
import com.americacg.cargavirtual.pnet.service.CargaVirtualGatewayPNet;

@Component
public class PNetServiceHelper {
	private static String urlWebService;
	private static ICargaVirtualGatewayPNet  gatewayPNet = null;

	public PNetServiceHelper() {
	}

	public static String getUrlWebService() {
		return urlWebService;
	}

	public static void setUrlWebService(String urlWebService) {
		PNetServiceHelper.urlWebService = urlWebService;
	}

	public static ICargaVirtualGatewayPNet getGatewayPNet() {
		if(gatewayPNet == null) {
			URL url = null;
			try {
				url = new URL(CargaVirtualGatewayPNet.class.getResource("."), urlWebService);
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			CargaVirtualGatewayPNet gs = new CargaVirtualGatewayPNet(url,
					new QName("http://service.pnet.cargavirtual.americacg.com/", "CargaVirtualGatewayPNet"));
			
			gatewayPNet = gs.getCargaVirtualGatewayPNetPort();
			// SETEO EL TIME OUT DE CONEXION (ms)
			((BindingProvider) gatewayPNet).getRequestContext().put("javax.xml.ws.client.connectionTimeout", "3000");
	
			// SETEO EL TIME DE LECTURA (ms)
			((BindingProvider) gatewayPNet).getRequestContext().put("javax.xml.ws.client.receiveTimeout", "10000");
	
			// Configuro el Parametro set-jaxb-validation-event-handler para
			// que no pinche el WS si cambia la definicion del WSDL
			((BindingProvider) gatewayPNet).getRequestContext().put("set-jaxb-validation-event-handler", "false");
		}
		return gatewayPNet;
	}
	
	public static ICargaVirtualGatewayPNet getGatewayPNet(String urlWebService) {
		PNetServiceHelper.urlWebService = urlWebService;
		return getGatewayPNet();
	}
	

	public static void setGatewayPNet(ICargaVirtualGatewayPNet gatewayPNet) {
		PNetServiceHelper.gatewayPNet = gatewayPNet;
	};

}
