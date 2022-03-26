
package com.americacg.cargavirtual.gcsi.model.acg.request;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;
import com.americacg.cargavirtual.gcsi.entidad.FacturaGIRE;
import com.americacg.cargavirtual.gcsi.entidad.FacturaPNET;
import com.americacg.cargavirtual.gcsi.entidad.FacturaWU;


/**
 * <p>Java class for reimprimirTicketRequest complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="reimprimirTicketRequest">
 *   &lt;complexContent>
 *     &lt;extension base="{com.americacg.cargavirtual.gcsi.model.acg.request}gatewayMessage">
 *       &lt;sequence>
 *         &lt;element name="facturaGIRE" type="{com.americacg.cargavirtual.gcsi.entidad}facturaGIRE" minOccurs="0"/>
 *         &lt;element name="facturaPNET" type="{com.americacg.cargavirtual.gcsi.entidad}facturaPNET" minOccurs="0"/>
 *         &lt;element name="facturaWU" type="{com.americacg.cargavirtual.gcsi.entidad}facturaWU" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "reimprimirTicketRequest", propOrder = {
    "facturaGIRE",
    "facturaPNET",
    "facturaWU"
})
public class ReimprimirTicketRequest
    extends GatewayMessage
{

    protected FacturaGIRE facturaGIRE;
    protected FacturaPNET facturaPNET;
    protected FacturaWU facturaWU;

    /**
     * Gets the value of the facturaGIRE property.
     * 
     * @return
     *     possible object is
     *     {@link FacturaGIRE }
     *     
     */
    public FacturaGIRE getFacturaGIRE() {
        return facturaGIRE;
    }

    /**
     * Sets the value of the facturaGIRE property.
     * 
     * @param value
     *     allowed object is
     *     {@link FacturaGIRE }
     *     
     */
    public void setFacturaGIRE(FacturaGIRE value) {
        this.facturaGIRE = value;
    }

    /**
     * Gets the value of the facturaPNET property.
     * 
     * @return
     *     possible object is
     *     {@link FacturaPNET }
     *     
     */
    public FacturaPNET getFacturaPNET() {
        return facturaPNET;
    }

    /**
     * Sets the value of the facturaPNET property.
     * 
     * @param value
     *     allowed object is
     *     {@link FacturaPNET }
     *     
     */
    public void setFacturaPNET(FacturaPNET value) {
        this.facturaPNET = value;
    }

    /**
     * Gets the value of the facturaWU property.
     * 
     * @return
     *     possible object is
     *     {@link FacturaWU }
     *     
     */
    public FacturaWU getFacturaWU() {
        return facturaWU;
    }

    /**
     * Sets the value of the facturaWU property.
     * 
     * @param value
     *     allowed object is
     *     {@link FacturaWU }
     *     
     */
    public void setFacturaWU(FacturaWU value) {
        this.facturaWU = value;
    }

}
