
package com.americacg.cargavirtual.gcsi.model.acg.request;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for consultarBarraRequest complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="consultarBarraRequest">
 *   &lt;complexContent>
 *     &lt;extension base="{com.americacg.cargavirtual.gcsi.model.acg.request}gatewayMessage">
 *       &lt;sequence>
 *         &lt;element name="barra" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "consultarBarraRequest", propOrder = {
    "barra"
})
public class ConsultarBarraRequest
    extends GatewayMessage
{

    @XmlElement(required = true)
    protected String barra;

    /**
     * Gets the value of the barra property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBarra() {
        return barra;
    }

    /**
     * Sets the value of the barra property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBarra(String value) {
        this.barra = value;
    }

}
