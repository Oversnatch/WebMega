
package com.americacg.cargavirtual.gcsi.model.acg.miscelaneo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ordenBBDD complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ordenBBDD">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="campoOrden" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="tipoOrden" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ordenBBDD", propOrder = {
    "campoOrden",
    "tipoOrden"
})
public class OrdenBBDD {

    protected String campoOrden;
    protected String tipoOrden;

    /**
     * Gets the value of the campoOrden property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCampoOrden() {
        return campoOrden;
    }

    /**
     * Sets the value of the campoOrden property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCampoOrden(String value) {
        this.campoOrden = value;
    }

    /**
     * Gets the value of the tipoOrden property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTipoOrden() {
        return tipoOrden;
    }

    /**
     * Sets the value of the tipoOrden property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTipoOrden(String value) {
        this.tipoOrden = value;
    }

}
