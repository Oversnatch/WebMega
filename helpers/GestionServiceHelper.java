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
import com.americacg.cargavirtual.gestion.service.GestionService;
import com.americacg.cargavirtual.gestion.service.GestionServicePortType;
import com.americacg.cargavirtual.web.enums.CfgTimeout;
import com.sun.xml.ws.client.BindingProviderProperties;

@Component
public class GestionServiceHelper {
	private static String urlWebService;
	private static GestionServicePortType gestionService = null;
	private static Properties timeouts = null;
	
	public GestionServiceHelper() {
	}

	public static String getUrlWebService() {
		return urlWebService;
	}

	public static void setUrlWebService(String urlWebService) {
		GestionServiceHelper.urlWebService = urlWebService;
	}

	public static GestionServicePortType getGestionService(CfgTimeout cfgTimeout) {
		Integer tmsConn = 0;
		Integer tmsRW = 0;
		
		if(gestionService == null) {
			URL url = null;
			try {
				url = new URL(GestionService.class.getResource("."), urlWebService);
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			GestionService gs = new GestionService(url,
					new QName("http://service.core.cargavirtual.americacg.com", "GestionService"));
			
			gestionService = gs.getGestionServiceHttpPort();
			// SETEO EL TIME OUT DE CONEXION (ms)
			((BindingProvider) gestionService).getRequestContext().put("javax.xml.ws.client.connectionTimeout", "3000");
	
			// SETEO EL TIME DE LECTURA (ms)
			((BindingProvider) gestionService).getRequestContext().put("javax.xml.ws.client.receiveTimeout", "10000");
	
			// Configuro el Parametro set-jaxb-validation-event-handler para
			// que no pinche el WS si cambia la definicion del WSDL
			((BindingProvider) gestionService).getRequestContext().put("set-jaxb-validation-event-handler", "false");

			loadProperties();
		}
		
		if(gestionService != null) {
			tmsConn = 0;
			tmsRW = 0;

			switch(cfgTimeout) {
				case CONSULTA:
					tmsConn = Integer.parseInt((String) timeouts.get("web.gestion.timeout.conn.CONSULTA"));
					tmsRW = Integer.parseInt((String) timeouts.get("web.gestion.timeout.rw.CONSULTA"));
					break;

				case REPORTE:
					tmsConn = Integer.parseInt((String) timeouts.get("web.gestion.timeout.conn.REPORTE"));
					tmsRW = Integer.parseInt((String) timeouts.get("web.gestion.timeout.rw.REPORTE"));
					break;
				case DEFAULT: 
					tmsConn = Integer.parseInt((String) timeouts.get("web.gestion.timeout.conn.DEFAULT"));
					tmsRW = Integer.parseInt((String) timeouts.get("web.gestion.timeout.rw.DEFAULT"));
					break;

				default:
					tmsConn = Integer.parseInt((String) timeouts.get("web.gestion.timeout.conn.DEFAULT"));
					tmsRW = Integer.parseInt((String) timeouts.get("web.gestion.timeout.rw.DEFAULT"));
					break;
			}
			
			Map<String, Object> requestCtx = ((BindingProvider) gestionService).getRequestContext();
			requestCtx.put(BindingProviderProperties.REQUEST_TIMEOUT, tmsRW);
			requestCtx.put(BindingProviderProperties.CONNECT_TIMEOUT, tmsConn);
			
		}

		return gestionService;
	}

	public static void setGestionService(GestionServicePortType gestionService) {
		GestionServiceHelper.gestionService = gestionService;
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
