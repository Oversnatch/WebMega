
package com.americacg.cargavirtual.gcsi.model.acg.response;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for consultaPagoSinFacturaResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="consultaPagoSinFacturaResponse">
 *   &lt;complexContent>
 *     &lt;extension base="{com.americacg.cargavirtual.gcsi.model.acg.response}gatewayMessage">
 *       &lt;sequence>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "consultaPagoSinFacturaResponse")
@XmlSeeAlso({
    ConsultaPagoSinFacturaResponseWU.class,
    ConsultaPagoSinFacturaResponseGIRE.class,
    ConsultaPagoSinFacturaResponsePNET.class
})
public abstract class ConsultaPagoSinFacturaResponse
    extends GatewayMessage
{


}
