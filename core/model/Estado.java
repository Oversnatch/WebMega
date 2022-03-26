
package com.americacg.cargavirtual.core.model;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for Estado complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Estado">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="cantAnul" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="cantAnulSim" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="cantConfirm" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="cantConfirmSim" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="cantConsSal" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="cantConsSalSim" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="cantConsTrx" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="cantConsTrxSim" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="cantTRXS" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="cantVentas" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="cantVentasSim" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="error" type="{http://model.core.cargavirtual.americacg.com}Error" minOccurs="0"/>
 *         &lt;element name="estado" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="modulo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Estado", propOrder = {
    "cantAnul",
    "cantAnulSim",
    "cantConfirm",
    "cantConfirmSim",
    "cantConsSal",
    "cantConsSalSim",
    "cantConsTrx",
    "cantConsTrxSim",
    "cantTRXS",
    "cantVentas",
    "cantVentasSim",
    "error",
    "estado",
    "modulo"
})
public class Estado {

    @XmlElementRef(name = "cantAnul", namespace = "http://model.core.cargavirtual.americacg.com", type = JAXBElement.class)
    protected JAXBElement<Long> cantAnul;
    @XmlElementRef(name = "cantAnulSim", namespace = "http://model.core.cargavirtual.americacg.com", type = JAXBElement.class)
    protected JAXBElement<Long> cantAnulSim;
    @XmlElementRef(name = "cantConfirm", namespace = "http://model.core.cargavirtual.americacg.com", type = JAXBElement.class)
    protected JAXBElement<Long> cantConfirm;
    @XmlElementRef(name = "cantConfirmSim", namespace = "http://model.core.cargavirtual.americacg.com", type = JAXBElement.class)
    protected JAXBElement<Long> cantConfirmSim;
    @XmlElementRef(name = "cantConsSal", namespace = "http://model.core.cargavirtual.americacg.com", type = JAXBElement.class)
    protected JAXBElement<Long> cantConsSal;
    @XmlElementRef(name = "cantConsSalSim", namespace = "http://model.core.cargavirtual.americacg.com", type = JAXBElement.class)
    protected JAXBElement<Long> cantConsSalSim;
    @XmlElementRef(name = "cantConsTrx", namespace = "http://model.core.cargavirtual.americacg.com", type = JAXBElement.class)
    protected JAXBElement<Long> cantConsTrx;
    @XmlElementRef(name = "cantConsTrxSim", namespace = "http://model.core.cargavirtual.americacg.com", type = JAXBElement.class)
    protected JAXBElement<Long> cantConsTrxSim;
    @XmlElementRef(name = "cantTRXS", namespace = "http://model.core.cargavirtual.americacg.com", type = JAXBElement.class)
    protected JAXBElement<Long> cantTRXS;
    @XmlElementRef(name = "cantVentas", namespace = "http://model.core.cargavirtual.americacg.com", type = JAXBElement.class)
    protected JAXBElement<Long> cantVentas;
    @XmlElementRef(name = "cantVentasSim", namespace = "http://model.core.cargavirtual.americacg.com", type = JAXBElement.class)
    protected JAXBElement<Long> cantVentasSim;
    @XmlElementRef(name = "error", namespace = "http://model.core.cargavirtual.americacg.com", type = JAXBElement.class)
    protected JAXBElement<Error> error;
    @XmlElementRef(name = "estado", namespace = "http://model.core.cargavirtual.americacg.com", type = JAXBElement.class)
    protected JAXBElement<String> estado;
    @XmlElementRef(name = "modulo", namespace = "http://model.core.cargavirtual.americacg.com", type = JAXBElement.class)
    protected JAXBElement<String> modulo;

    /**
     * Gets the value of the cantAnul property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link Long }{@code >}
     *     
     */
    public JAXBElement<Long> getCantAnul() {
        return cantAnul;
    }

    /**
     * Sets the value of the cantAnul property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link Long }{@code >}
     *     
     */
    public void setCantAnul(JAXBElement<Long> value) {
        this.cantAnul = ((JAXBElement<Long> ) value);
    }

    /**
     * Gets the value of the cantAnulSim property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link Long }{@code >}
     *     
     */
    public JAXBElement<Long> getCantAnulSim() {
        return cantAnulSim;
    }

    /**
     * Sets the value of the cantAnulSim property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link Long }{@code >}
     *     
     */
    public void setCantAnulSim(JAXBElement<Long> value) {
        this.cantAnulSim = ((JAXBElement<Long> ) value);
    }

    /**
     * Gets the value of the cantConfirm property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link Long }{@code >}
     *     
     */
    public JAXBElement<Long> getCantConfirm() {
        return cantConfirm;
    }

    /**
     * Sets the value of the cantConfirm property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link Long }{@code >}
     *     
     */
    public void setCantConfirm(JAXBElement<Long> value) {
        this.cantConfirm = ((JAXBElement<Long> ) value);
    }

    /**
     * Gets the value of the cantConfirmSim property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link Long }{@code >}
     *     
     */
    public JAXBElement<Long> getCantConfirmSim() {
        return cantConfirmSim;
    }

    /**
     * Sets the value of the cantConfirmSim property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link Long }{@code >}
     *     
     */
    public void setCantConfirmSim(JAXBElement<Long> value) {
        this.cantConfirmSim = ((JAXBElement<Long> ) value);
    }

    /**
     * Gets the value of the cantConsSal property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link Long }{@code >}
     *     
     */
    public JAXBElement<Long> getCantConsSal() {
        return cantConsSal;
    }

    /**
     * Sets the value of the cantConsSal property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link Long }{@code >}
     *     
     */
    public void setCantConsSal(JAXBElement<Long> value) {
        this.cantConsSal = ((JAXBElement<Long> ) value);
    }

    /**
     * Gets the value of the cantConsSalSim property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link Long }{@code >}
     *     
     */
    public JAXBElement<Long> getCantConsSalSim() {
        return cantConsSalSim;
    }

    /**
     * Sets the value of the cantConsSalSim property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link Long }{@code >}
     *     
     */
    public void setCantConsSalSim(JAXBElement<Long> value) {
        this.cantConsSalSim = ((JAXBElement<Long> ) value);
    }

    /**
     * Gets the value of the cantConsTrx property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link Long }{@code >}
     *     
     */
    public JAXBElement<Long> getCantConsTrx() {
        return cantConsTrx;
    }

    /**
     * Sets the value of the cantConsTrx property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link Long }{@code >}
     *     
     */
    public void setCantConsTrx(JAXBElement<Long> value) {
        this.cantConsTrx = ((JAXBElement<Long> ) value);
    }

    /**
     * Gets the value of the cantConsTrxSim property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link Long }{@code >}
     *     
     */
    public JAXBElement<Long> getCantConsTrxSim() {
        return cantConsTrxSim;
    }

    /**
     * Sets the value of the cantConsTrxSim property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link Long }{@code >}
     *     
     */
    public void setCantConsTrxSim(JAXBElement<Long> value) {
        this.cantConsTrxSim = ((JAXBElement<Long> ) value);
    }

    /**
     * Gets the value of the cantTRXS property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link Long }{@code >}
     *     
     */
    public JAXBElement<Long> getCantTRXS() {
        return cantTRXS;
    }

    /**
     * Sets the value of the cantTRXS property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link Long }{@code >}
     *     
     */
    public void setCantTRXS(JAXBElement<Long> value) {
        this.cantTRXS = ((JAXBElement<Long> ) value);
    }

    /**
     * Gets the value of the cantVentas property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link Long }{@code >}
     *     
     */
    public JAXBElement<Long> getCantVentas() {
        return cantVentas;
    }

    /**
     * Sets the value of the cantVentas property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link Long }{@code >}
     *     
     */
    public void setCantVentas(JAXBElement<Long> value) {
        this.cantVentas = ((JAXBElement<Long> ) value);
    }

    /**
     * Gets the value of the cantVentasSim property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link Long }{@code >}
     *     
     */
    public JAXBElement<Long> getCantVentasSim() {
        return cantVentasSim;
    }

    /**
     * Sets the value of the cantVentasSim property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link Long }{@code >}
     *     
     */
    public void setCantVentasSim(JAXBElement<Long> value) {
        this.cantVentasSim = ((JAXBElement<Long> ) value);
    }

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
     * Gets the value of the estado property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getEstado() {
        return estado;
    }

    /**
     * Sets the value of the estado property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setEstado(JAXBElement<String> value) {
        this.estado = ((JAXBElement<String> ) value);
    }

    /**
     * Gets the value of the modulo property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getModulo() {
        return modulo;
    }

    /**
     * Sets the value of the modulo property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setModulo(JAXBElement<String> value) {
        this.modulo = ((JAXBElement<String> ) value);
    }

}
