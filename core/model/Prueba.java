
package com.americacg.cargavirtual.core.model;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for Prueba complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Prueba">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="error" type="{http://model.core.cargavirtual.americacg.com}Error" minOccurs="0"/>
 *         &lt;element name="flt" type="{http://www.w3.org/2001/XMLSchema}float" minOccurs="0"/>
 *         &lt;element name="fltN" type="{http://www.w3.org/2001/XMLSchema}float" minOccurs="0"/>
 *         &lt;element name="intN" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="inte" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="lng" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="lngN" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="str" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="strN" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Prueba", propOrder = {
    "error",
    "flt",
    "fltN",
    "intN",
    "inte",
    "lng",
    "lngN",
    "str",
    "strN"
})
public class Prueba {

    @XmlElementRef(name = "error", namespace = "http://model.core.cargavirtual.americacg.com", type = JAXBElement.class)
    protected JAXBElement<Error> error;
    @XmlElementRef(name = "flt", namespace = "http://model.core.cargavirtual.americacg.com", type = JAXBElement.class)
    protected JAXBElement<Float> flt;
    @XmlElementRef(name = "fltN", namespace = "http://model.core.cargavirtual.americacg.com", type = JAXBElement.class)
    protected JAXBElement<Float> fltN;
    @XmlElementRef(name = "intN", namespace = "http://model.core.cargavirtual.americacg.com", type = JAXBElement.class)
    protected JAXBElement<Integer> intN;
    @XmlElementRef(name = "inte", namespace = "http://model.core.cargavirtual.americacg.com", type = JAXBElement.class)
    protected JAXBElement<Integer> inte;
    @XmlElementRef(name = "lng", namespace = "http://model.core.cargavirtual.americacg.com", type = JAXBElement.class)
    protected JAXBElement<Long> lng;
    @XmlElementRef(name = "lngN", namespace = "http://model.core.cargavirtual.americacg.com", type = JAXBElement.class)
    protected JAXBElement<Long> lngN;
    @XmlElementRef(name = "str", namespace = "http://model.core.cargavirtual.americacg.com", type = JAXBElement.class)
    protected JAXBElement<String> str;
    @XmlElementRef(name = "strN", namespace = "http://model.core.cargavirtual.americacg.com", type = JAXBElement.class)
    protected JAXBElement<String> strN;

    /**
     * Gets the value of the error property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link Error }{@code >}
     *     
     */
    public JAXBElement<Error> getError() {
        return error;
    }

    /**
     * Sets the value of the error property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link Error }{@code >}
     *     
     */
    public void setError(JAXBElement<Error> value) {
        this.error = ((JAXBElement<Error> ) value);
    }

    /**
     * Gets the value of the flt property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link Float }{@code >}
     *     
     */
    public JAXBElement<Float> getFlt() {
        return flt;
    }

    /**
     * Sets the value of the flt property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link Float }{@code >}
     *     
     */
    public void setFlt(JAXBElement<Float> value) {
        this.flt = ((JAXBElement<Float> ) value);
    }

    /**
     * Gets the value of the fltN property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link Float }{@code >}
     *     
     */
    public JAXBElement<Float> getFltN() {
        return fltN;
    }

    /**
     * Sets the value of the fltN property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link Float }{@code >}
     *     
     */
    public void setFltN(JAXBElement<Float> value) {
        this.fltN = ((JAXBElement<Float> ) value);
    }

    /**
     * Gets the value of the intN property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link Integer }{@code >}
     *     
     */
    public JAXBElement<Integer> getIntN() {
        return intN;
    }

    /**
     * Sets the value of the intN property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link Integer }{@code >}
     *     
     */
    public void setIntN(JAXBElement<Integer> value) {
        this.intN = ((JAXBElement<Integer> ) value);
    }

    /**
     * Gets the value of the inte property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link Integer }{@code >}
     *     
     */
    public JAXBElement<Integer> getInte() {
        return inte;
    }

    /**
     * Sets the value of the inte property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link Integer }{@code >}
     *     
     */
    public void setInte(JAXBElement<Integer> value) {
        this.inte = ((JAXBElement<Integer> ) value);
    }

    /**
     * Gets the value of the lng property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link Long }{@code >}
     *     
     */
    public JAXBElement<Long> getLng() {
        return lng;
    }

    /**
     * Sets the value of the lng property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link Long }{@code >}
     *     
     */
    public void setLng(JAXBElement<Long> value) {
        this.lng = ((JAXBElement<Long> ) value);
    }

    /**
     * Gets the value of the lngN property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link Long }{@code >}
     *     
     */
    public JAXBElement<Long> getLngN() {
        return lngN;
    }

    /**
     * Sets the value of the lngN property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link Long }{@code >}
     *     
     */
    public void setLngN(JAXBElement<Long> value) {
        this.lngN = ((JAXBElement<Long> ) value);
    }

    /**
     * Gets the value of the str property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getStr() {
        return str;
    }

    /**
     * Sets the value of the str property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setStr(JAXBElement<String> value) {
        this.str = ((JAXBElement<String> ) value);
    }

    /**
     * Gets the value of the strN property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getStrN() {
        return strN;
    }

    /**
     * Sets the value of the strN property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setStrN(JAXBElement<String> value) {
        this.strN = ((JAXBElement<String> ) value);
    }

}
