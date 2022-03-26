package com.americacg.cargavirtual.web.helpers;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.Properties;

import javax.xml.namespace.QName;
import javax.xml.ws.BindingProvider;
import com.sun.xml.ws.client.BindingProviderProperties;
import org.springframework.stereotype.Component;

import com.americacg.cargavirtual.core.service.CoreService;
import com.americacg.cargavirtual.core.service.CoreServicePortType;
import com.americacg.cargavirtual.web.enums.CfgTimeout;

@Component
public class CoreServiceHelper {
	private static String urlWebService;
	private static CoreServicePortType coreService = null;
	private static Properties timeouts = null;
	
	public CoreServiceHelper() {
	}

	public static String getUrlWebService() {
		return urlWebService;
	}

	public static void setUrlWebService(String urlWebService) {
		CoreServiceHelper.urlWebService = urlWebService;
	}

	public static CoreServicePortType getCoreService(CfgTimeout cfgTimeout) {
		Integer tmsConn = 0;
		Integer tmsRW = 0;
		
		if(coreService == null) {
			URL url = null;
			try {
				url = new URL(CoreService.class.getResource("."), urlWebService);
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			CoreService gs = new CoreService(url,
					new QName("http://service.core.cargavirtual.americacg.com", "CoreService"));
			
			coreService = gs.getCoreServiceHttpPort();
			// SETEO EL TIME OUT DE CONEXION (ms)
			//((BindingProvider) coreService).getRequestContext().put("javax.xml.ws.client.connectionTimeout", "3000");
	
			// SETEO EL TIME DE LECTURA (ms)
			//((BindingProvider) coreService).getRequestContext().put("javax.xml.ws.client.receiveTimeout", "10000");
	
			// Configuro el Parametro set-jaxb-validation-event-handler para
			// que no pinche el WS si cambia la definicion del WSDL
			((BindingProvider) coreService).getRequestContext().put("set-jaxb-validation-event-handler", "false");
			
			loadProperties();
		}
		
		if(coreService != null) {
			tmsConn = 0;
			tmsRW = 0;

			switch(cfgTimeout) {
				case CONSULTA:
					tmsConn = Integer.parseInt((String) timeouts.get("web.core.timeout.conn.CONSULTA"));
					tmsRW = Integer.parseInt((String) timeouts.get("web.core.timeout.rw.CONSULTA"));
					break;

				case VENTA:
					tmsConn = Integer.parseInt((String) timeouts.get("web.core.timeout.conn.VENTA"));
					tmsRW = Integer.parseInt((String) timeouts.get("web.core.timeout.rw.VENTA"));
					break;
				case DEFAULT: 
					tmsConn = Integer.parseInt((String) timeouts.get("web.core.timeout.conn.DEFAULT"));
					tmsRW = Integer.parseInt((String) timeouts.get("web.core.timeout.rw.DEFAULT"));
					break;

				default:
					tmsConn = Integer.parseInt((String) timeouts.get("web.core.timeout.conn.DEFAULT"));
					tmsRW = Integer.parseInt((String) timeouts.get("web.core.timeout.rw.DEFAULT"));
					break;
			}
			
			Map<String, Object> requestCtx = ((BindingProvider) coreService).getRequestContext();
			requestCtx.put(BindingProviderProperties.REQUEST_TIMEOUT, tmsRW);
			requestCtx.put(BindingProviderProperties.CONNECT_TIMEOUT, tmsConn);
			
		}
		
		return coreService;
	}

	public static void setCoreService(CoreServicePortType coreService) {
		CoreServiceHelper.coreService = coreService;
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
