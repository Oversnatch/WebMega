
package com.americacg.cargavirtual.core.model;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for Error complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Error">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="codigoError" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="hayError" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="msgError" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Error", propOrder = {
    "codigoError",
    "hayError",
    "msgError"
})
public class Error {

    @XmlElementRef(name = "codigoError", namespace = "http://model.core.cargavirtual.americacg.com", type = JAXBElement.class)
    protected JAXBElement<String> codigoError;
    @XmlElementRef(name = "hayError", namespace = "http://model.core.cargavirtual.americacg.com", type = JAXBElement.class)
    protected JAXBElement<Boolean> hayError;
    @XmlElementRef(name = "msgError", namespace = "http://model.core.cargavirtual.americacg.com", type = JAXBElement.class)
    protected JAXBElement<String> msgError;

    /**
     * Gets the value of the codigoError property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getCodigoError() {
        return codigoError;
    }

    /**
     * Sets the value of the codigoError property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setCodigoError(JAXBElement<String> value) {
        this.codigoError = ((JAXBElement<String> ) value);
    }

    /**
     * Gets the value of the hayError property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link Boolean }{@code >}
     *     
     */
    public JAXBElement<Boolean> getHayError() {
        return hayError;
    }

    /**
     * Sets the value of the hayError property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link Boolean }{@code >}
     *     
     */
    public void setHayError(JAXBElement<Boolean> value) {
        this.hayError = ((JAXBElement<Boolean> ) value);
    }

    /**
     * Gets the value of the msgError property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getMsgError() {
        return msgError;
    }

    /**
     * Sets the value of the msgError property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setMsgError(JAXBElement<String> value) {
        this.msgError = ((JAXBElement<String> ) value);
    }

}
