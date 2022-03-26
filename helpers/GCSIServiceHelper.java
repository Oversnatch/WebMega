package com.americacg.cargavirtual.web.helpers;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.Properties;

import javax.xml.namespace.QName;
import javax.xml.ws.BindingProvider;
import org.springframework.stereotype.Component;

import com.americacg.cargavirtual.core.service.CoreService;
import com.americacg.cargavirtual.gcsi.service.CargaVirtualGatewayGCSIService;
import com.americacg.cargavirtual.gcsi.service.ICargaVirtualGatewayGCSI;
import com.americacg.cargavirtual.web.enums.CfgTimeout;
import com.sun.xml.ws.client.BindingProviderProperties;

@Component
public class GCSIServiceHelper {
	private static String urlWebService;
	private static ICargaVirtualGatewayGCSI  gatewayGCSI = null;
	private static Properties timeouts = null;
	
	public GCSIServiceHelper() {
	}

	public static String getUrlWebService() {
		return urlWebService;
	}

	public static void setUrlWebService(String urlWebService) {
		GCSIServiceHelper.urlWebService = urlWebService;
	}

	public static ICargaVirtualGatewayGCSI getGatewayGCSI(CfgTimeout cfgTimeout) {
		Integer tmsConn = 0;
		Integer tmsRW = 0;

		if(gatewayGCSI == null) {
			URL url = null;
			try {
				url = new URL(CargaVirtualGatewayGCSIService.class.getResource("."), urlWebService);
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			CargaVirtualGatewayGCSIService gs = new CargaVirtualGatewayGCSIService(url,
					new QName("http://gateway.gcsi.cargavirtual.americacg.com/", "CargaVirtualGatewayGCSIService"));
			
			gatewayGCSI = gs.getCargaVirtualGatewayGCSIPort();
			// SETEO EL TIME OUT DE CONEXION (ms)
			((BindingProvider) gatewayGCSI).getRequestContext().put("javax.xml.ws.client.connectionTimeout", "3000");
	
			// SETEO EL TIME DE LECTURA (ms)
			((BindingProvider) gatewayGCSI).getRequestContext().put("javax.xml.ws.client.receiveTimeout", "10000");
	
			// Configuro el Parametro set-jaxb-validation-event-handler para
			// que no pinche el WS si cambia la definicion del WSDL
			((BindingProvider) gatewayGCSI).getRequestContext().put("set-jaxb-validation-event-handler", "false");
			
			loadProperties();
		}
		
		if(gatewayGCSI != null) {
			tmsConn = 0;
			tmsRW = 0;
			
			switch(cfgTimeout) {
				case VENTA:
					tmsConn = Integer.parseInt((String) timeouts.get("web.gcsi.timeout.conn.CONSULTA"));
					tmsRW = Integer.parseInt((String) timeouts.get("web.gcsi.timeout.rw.CONSULTA"));
					break;
	
				case CONSULTA:
						tmsConn = Integer.parseInt((String) timeouts.get("web.gcsi.timeout.conn.CONSULTA"));
						tmsRW = Integer.parseInt((String) timeouts.get("web.gcsi.timeout.rw.CONSULTA"));
						break;

				case REPORTE:
					tmsConn = Integer.parseInt((String) timeouts.get("web.gcsi.timeout.conn.REPORTE"));
					tmsRW = Integer.parseInt((String) timeouts.get("web.gcsi.timeout.rw.REPORTE"));
					break;
				case DEFAULT: 
					tmsConn = Integer.parseInt((String) timeouts.get("web.gcsi.timeout.conn.DEFAULT"));
					tmsRW = Integer.parseInt((String) timeouts.get("web.gcsi.timeout.rw.DEFAULT"));
					break;

				default:
					tmsConn = Integer.parseInt((String) timeouts.get("web.gcsi.timeout.conn.DEFAULT"));
					tmsRW = Integer.parseInt((String) timeouts.get("web.gcsi.timeout.rw.DEFAULT"));
					break;
			}
			
			Map<String, Object> requestCtx = ((BindingProvider) gatewayGCSI).getRequestContext();
			requestCtx.put(BindingProviderProperties.REQUEST_TIMEOUT, tmsRW);
			requestCtx.put(BindingProviderProperties.CONNECT_TIMEOUT, tmsConn);
			
		}

		return gatewayGCSI;
	}
	
	public static void setGatewayGCSI(ICargaVirtualGatewayGCSI gatewayGCSI) {
		GCSIServiceHelper.gatewayGCSI = gatewayGCSI;
	};

	private static void loadProperties() {
		   try (InputStream input = CoreService.class.getClassLoader().getResourceAsStream("web.properties")) {

	            timeouts = new Properties();

	            // load a properties file
	            timeouts.load(input);
	        } catch (IOException ex) {
	            ex.printStackTrace();
	        }

	}
}
