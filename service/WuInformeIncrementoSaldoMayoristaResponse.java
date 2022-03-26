
package com.americacg.cargavirtual.gestion.service;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import com.americacg.cargavirtual.gestion.model.ArrayOfInformeIncrementoSaldoMayoristaWU;


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
 *         &lt;element name="out" type="{http://model.core.cargavirtual.americacg.com}ArrayOfInformeIncrementoSaldoMayoristaWU"/>
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
@XmlRootElement(name = "wu_informeIncrementoSaldoMayoristaResponse")
public class WuInformeIncrementoSaldoMayoristaResponse {

    @XmlElement(required = true, nillable = true)
    protected ArrayOfInformeIncrementoSaldoMayoristaWU out;

    /**
     * Gets the value of the out property.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfInformeIncrementoSaldoMayoristaWU }
     *     
     */
    public ArrayOfInformeIncrementoSaldoMayoristaWU getOut() {
        return out;
    }

    /**
     * Sets the value of the out property.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfInformeIncrementoSaldoMayoristaWU }
     *     
     */
    public void setOut(ArrayOfInformeIncrementoSaldoMayoristaWU value) {
        this.out = value;
    }

}
