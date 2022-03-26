package com.americacg.cargavirtual.core.service;

import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceFeature;
import javax.xml.ws.Service;

/**
 * This class was generated by Apache CXF 2.4.2
 * 2020-03-02T12:04:43.396-03:00
 * Generated source version: 2.4.2
 * 
 */
@WebServiceClient(name = "CoreService", 
                  wsdlLocation = "file:/C:/Java/workspaceCargaVirtual/wsdl2java/src/main/wsdl/CoreService.wsdl",
                  targetNamespace = "http://service.core.cargavirtual.americacg.com") 
public class CoreService extends Service {

    public final static URL WSDL_LOCATION;

    public final static QName SERVICE = new QName("http://service.core.cargavirtual.americacg.com", "CoreService");
    public final static QName CoreServiceHttpPort = new QName("http://service.core.cargavirtual.americacg.com", "CoreServiceHttpPort");
    static {
        URL url = null;
        try {
            url = new URL("file:/C:/Java/workspaceCargaVirtual/wsdl2java/src/main/wsdl/CoreService.wsdl");
        } catch (MalformedURLException e) {
            java.util.logging.Logger.getLogger(CoreService.class.getName())
                .log(java.util.logging.Level.INFO, 
                     "Can not initialize the default wsdl from {0}", "file:/C:/Java/workspaceCargaVirtual/wsdl2java/src/main/wsdl/CoreService.wsdl");
        }
        WSDL_LOCATION = url;
    }

    public CoreService(URL wsdlLocation) {
        super(wsdlLocation, SERVICE);
    }

    public CoreService(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public CoreService() {
        super(WSDL_LOCATION, SERVICE);
    }
    
    //This constructor requires JAX-WS API 2.2. You will need to endorse the 2.2
    //API jar or re-run wsdl2java with "-frontend jaxws21" to generate JAX-WS 2.1
    //compliant code instead.
    public CoreService(WebServiceFeature ... features) {
        super(WSDL_LOCATION, SERVICE, features);
    }

    //This constructor requires JAX-WS API 2.2. You will need to endorse the 2.2
    //API jar or re-run wsdl2java with "-frontend jaxws21" to generate JAX-WS 2.1
    //compliant code instead.
    public CoreService(URL wsdlLocation, WebServiceFeature ... features) {
        super(wsdlLocation, SERVICE, features);
    }

    //This constructor requires JAX-WS API 2.2. You will need to endorse the 2.2
    //API jar or re-run wsdl2java with "-frontend jaxws21" to generate JAX-WS 2.1
    //compliant code instead.
    public CoreService(URL wsdlLocation, QName serviceName, WebServiceFeature ... features) {
        super(wsdlLocation, serviceName, features);
    }

    /**
     *
     * @return
     *     returns CoreServicePortType
     */
    @WebEndpoint(name = "CoreServiceHttpPort")
    public CoreServicePortType getCoreServiceHttpPort() {
        return super.getPort(CoreServiceHttpPort, CoreServicePortType.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns CoreServicePortType
     */
    @WebEndpoint(name = "CoreServiceHttpPort")
    public CoreServicePortType getCoreServiceHttpPort(WebServiceFeature... features) {
        return super.getPort(CoreServiceHttpPort, CoreServicePortType.class, features);
    }

}
