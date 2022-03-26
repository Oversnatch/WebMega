
package com.americacg.cargavirtual.gestion.service;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import com.americacg.cargavirtual.gestion.model.HeaderSecur;
import com.americacg.cargavirtual.gestion.model.PlatTipoIdentifCta;


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
 *         &lt;element name="in0" type="{http://model.core.cargavirtual.americacg.com}HeaderSecur"/>
 *         &lt;element name="in1" type="{http://model.core.cargavirtual.americacg.com}PlatTipoIdentifCta"/>
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
    "in0",
    "in1"
})
@XmlRootElement(name = "agregarPlatTipoIdentifCta")
public class AgregarPlatTipoIdentifCta {

    @XmlElement(required = true, nillable = true)
    protected HeaderSecur in0;
    @XmlElement(required = true, nillable = true)
    protected PlatTipoIdentifCta in1;

    /**
     * Gets the value of the in0 property.
     * 
     * @return
     *     possible object is
     *     {@link HeaderSecur }
     *     
     */
    public HeaderSecur getIn0() {
        return in0;
    }

    /**
     * Sets the value of the in0 property.
     * 
     * @param value
     *     allowed object is
     *     {@link HeaderSecur }
     *     
     */
    public void setIn0(HeaderSecur value) {
        this.in0 = value;
    }

    /**
     * Gets the value of the in1 property.
     * 
     * @return
     *     possible object is
     *     {@link PlatTipoIdentifCta }
     *     
     */
    public PlatTipoIdentifCta getIn1() {
        return in1;
    }

    /**
     * Sets the value of the in1 property.
     * 
     * @param value
     *     allowed object is
     *     {@link PlatTipoIdentifCta }
     *     
     */
    public void setIn1(PlatTipoIdentifCta value) {
        this.in1 = value;
    }

}
