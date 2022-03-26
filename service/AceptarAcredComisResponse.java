
package com.americacg.cargavirtual.gestion.service;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import com.americacg.cargavirtual.gestion.model.RespAcreditComisionesContainer;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="out" type="{http://model.core.cargavirtual.americacg.com}RespAcreditComisionesContainer"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "out"
})
@XmlRootElement(name = "aceptarAcredComisResponse")
public class AceptarAcredComisResponse {

    @XmlElement(required = true, nillable = true)
    protected RespAcreditComisionesContainer out;

    /**
     * Gets the value of the out property.
     * 
     * @return
     *     possible object is
     *     {@link RespAcreditComisionesContainer }
     *     
     */
    public RespAcreditComisionesContainer getOut() {
        return out;
    }

    /**
     * Sets the value of the out property.
     * 
     * @param value
     *     allowed object is
     *     {@link RespAcreditComisionesContainer }
     *     
     */
    public void setOut(RespAcreditComisionesContainer value) {
        this.out = value;
    }

}
