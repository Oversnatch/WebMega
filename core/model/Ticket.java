
package com.americacg.cargavirtual.core.model;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for Ticket complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Ticket">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="callcenter1" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="callcenter2" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="caracteristicaTelefono" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="cat" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="descEstadoTransaccion" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="descripcionProducto" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="encabezado" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="error" type="{http://model.core.cargavirtual.americacg.com}Error" minOccurs="0"/>
 *         &lt;element name="fecha" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="fechaVencimientoPin" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="finTicket" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="idCliente" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="idEstadoTransaccion" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="idLlotePin" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="idProducto" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="idTransaccion" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="importe" type="{http://www.w3.org/2001/XMLSchema}float" minOccurs="0"/>
 *         &lt;element name="instrucciones" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="moneda" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="numeroTelefono" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="pie1" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="pie2" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="pie3" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="pie4" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="pin" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="razonSocialCliente" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="saldoDisponibleDelCliente" type="{http://www.w3.org/2001/XMLSchema}float" minOccurs="0"/>
 *         &lt;element name="serie" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="tipoOperacion" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="usuario" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="vigenciaPin" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Ticket", propOrder = {
    "callcenter1",
    "callcenter2",
    "caracteristicaTelefono",
    "cat",
    "descEstadoTransaccion",
    "descripcionProducto",
    "encabezado",
    "error",
    "fecha",
    "fechaVencimientoPin",
    "finTicket",
    "idCliente",
    "idEstadoTransaccion",
    "idLlotePin",
    "idProducto",
    "idTransaccion",
    "importe",
    "instrucciones",
    "moneda",
    "numeroTelefono",
    "pie1",
    "pie2",
    "pie3",
    "pie4",
    "pin",
    "razonSocialCliente",
    "saldoDisponibleDelCliente",
    "serie",
    "tipoOperacion",
    "usuario",
    "vigenciaPin"
})
public class Ticket {

    @XmlElementRef(name = "callcenter1", namespace = "http://model.core.cargavirtual.americacg.com", type = JAXBElement.class)
    protected JAXBElement<String> callcenter1;
    @XmlElementRef(name = "callcenter2", namespace = "http://model.core.cargavirtual.americacg.com", type = JAXBElement.class)
    protected JAXBElement<String> callcenter2;
    @XmlElementRef(name = "caracteristicaTelefono", namespace = "http://model.core.cargavirtual.americacg.com", type = JAXBElement.class)
    protected JAXBElement<Integer> caracteristicaTelefono;
    @XmlElementRef(name = "cat", namespace = "http://model.core.cargavirtual.americacg.com", type = JAXBElement.class)
    protected JAXBElement<String> cat;
    @XmlElementRef(name = "descEstadoTransaccion", namespace = "http://model.core.cargavirtual.americacg.com", type = JAXBElement.class)
    protected JAXBElement<String> descEstadoTransaccion;
    @XmlElementRef(name = "descripcionProducto", namespace = "http://model.core.cargavirtual.americacg.com", type = JAXBElement.class)
    protected JAXBElement<String> descripcionProducto;
    @XmlElementRef(name = "encabezado", namespace = "http://model.core.cargavirtual.americacg.com", type = JAXBElement.class)
    protected JAXBElement<String> encabezado;
    @XmlElementRef(name = "error", namespace = "http://model.core.cargavirtual.americacg.com", type = JAXBElement.class)
    protected JAXBElement<Error> error;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar fecha;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar fechaVencimientoPin;
    @XmlElementRef(name = "finTicket", namespace = "http://model.core.cargavirtual.americacg.com", type = JAXBElement.class)
    protected JAXBElement<String> finTicket;
    @XmlElementRef(name = "idCliente", namespace = "http://model.core.cargavirtual.americacg.com", type = JAXBElement.class)
    protected JAXBElement<Long> idCliente;
    @XmlElementRef(name = "idEstadoTransaccion", namespace = "http://model.core.cargavirtual.americacg.com", type = JAXBElement.class)
    protected JAXBElement<String> idEstadoTransaccion;
    @XmlElementRef(name = "idLlotePin", namespace = "http://model.core.cargavirtual.americacg.com", type = JAXBElement.class)
    protected JAXBElement<String> idLlotePin;
    @XmlElementRef(name = "idProducto", namespace = "http://model.core.cargavirtual.americacg.com", type = JAXBElement.class)
    protected JAXBElement<Long> idProducto;
    @XmlElementRef(name = "idTransaccion", namespace = "http://model.core.cargavirtual.americacg.com", type = JAXBElement.class)
    protected JAXBElement<Long> idTransaccion;
    @XmlElementRef(name = "importe", namespace = "http://model.core.cargavirtual.americacg.com", type = JAXBElement.class)
    protected JAXBElement<Float> importe;
    @XmlElementRef(name = "instrucciones", namespace = "http://model.core.cargavirtual.americacg.com", type = JAXBElement.class)
    protected JAXBElement<String> instrucciones;
    @XmlElementRef(name = "moneda", namespace = "http://model.core.cargavirtual.americacg.com", type = JAXBElement.class)
    protected JAXBElement<String> moneda;
    @XmlElementRef(name = "numeroTelefono", namespace = "http://model.core.cargavirtual.americacg.com", type = JAXBElement.class)
    protected JAXBElement<Integer> numeroTelefono;
    @XmlElementRef(name = "pie1", namespace = "http://model.core.cargavirtual.americacg.com", type = JAXBElement.class)
    protected JAXBElement<String> pie1;
    @XmlElementRef(name = "pie2", namespace = "http://model.core.cargavirtual.americacg.com", type = JAXBElement.class)
    protected JAXBElement<String> pie2;
    @XmlElementRef(name = "pie3", namespace = "http://model.core.cargavirtual.americacg.com", type = JAXBElement.class)
    protected JAXBElement<String> pie3;
    @XmlElementRef(name = "pie4", namespace = "http://model.core.cargavirtual.americacg.com", type = JAXBElement.class)
    protected JAXBElement<String> pie4;
    @XmlElementRef(name = "pin", namespace = "http://model.core.cargavirtual.americacg.com", type = JAXBElement.class)
    protected JAXBElement<String> pin;
    @XmlElementRef(name = "razonSocialCliente", namespace = "http://model.core.cargavirtual.americacg.com", type = JAXBElement.class)
    protected JAXBElement<String> razonSocialCliente;
    @XmlElementRef(name = "saldoDisponibleDelCliente", namespace = "http://model.core.cargavirtual.americacg.com", type = JAXBElement.class)
    protected JAXBElement<Float> saldoDisponibleDelCliente;
    @XmlElementRef(name = "serie", namespace = "http://model.core.cargavirtual.americacg.com", type = JAXBElement.class)
    protected JAXBElement<String> serie;
    @XmlElementRef(name = "tipoOperacion", namespace = "http://model.core.cargavirtual.americacg.com", type = JAXBElement.class)
    protected JAXBElement<String> tipoOperacion;
    @XmlElementRef(name = "usuario", namespace = "http://model.core.cargavirtual.americacg.com", type = JAXBElement.class)
    protected JAXBElement<String> usuario;
    @XmlElementRef(name = "vigenciaPin", namespace = "http://model.core.cargavirtual.americacg.com", type = JAXBElement.class)
    protected JAXBElement<String> vigenciaPin;

    /**
     * Gets the value of the callcenter1 property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getCallcenter1() {
        return callcenter1;
    }

    /**
     * Sets the value of the callcenter1 property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setCallcenter1(JAXBElement<String> value) {
        this.callcenter1 = ((JAXBElement<String> ) value);
    }

    /**
     * Gets the value of the callcenter2 property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getCallcenter2() {
        return callcenter2;
    }

    /**
     * Sets the value of the callcenter2 property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setCallcenter2(JAXBElement<String> value) {
        this.callcenter2 = ((JAXBElement<String> ) value);
    }

    /**
     * Gets the value of the caracteristicaTelefono property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link Integer }{@code >}
     *     
     */
    public JAXBElement<Integer> getCaracteristicaTelefono() {
        return caracteristicaTelefono;
    }

    /**
     * Sets the value of the caracteristicaTelefono property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link Integer }{@code >}
     *     
     */
    public void setCaracteristicaTelefono(JAXBElement<Integer> value) {
        this.caracteristicaTelefono = ((JAXBElement<Integer> ) value);
    }

    /**
     * Gets the value of the cat property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getCat() {
        return cat;
    }

    /**
     * Sets the value of the cat property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setCat(JAXBElement<String> value) {
        this.cat = ((JAXBElement<String> ) value);
    }

    /**
     * Gets the value of the descEstadoTransaccion property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getDescEstadoTransaccion() {
        return descEstadoTransaccion;
    }

    /**
     * Sets the value of the descEstadoTransaccion property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setDescEstadoTransaccion(JAXBElement<String> value) {
        this.descEstadoTransaccion = ((JAXBElement<String> ) value);
    }

    /**
     * Gets the value of the descripcionProducto property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getDescripcionProducto() {
        return descripcionProducto;
    }

    /**
     * Sets the value of the descripcionProducto property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setDescripcionProducto(JAXBElement<String> value) {
        this.descripcionProducto = ((JAXBElement<String> ) value);
    }

    /**
     * Gets the value of the encabezado property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getEncabezado() {
        return encabezado;
    }

    /**
     * Sets the value of the encabezado property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setEncabezado(JAXBElement<String> value) {
        this.encabezado = ((JAXBElement<String> ) value);
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
     * Gets the value of the fecha property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getFecha() {
        return fecha;
    }

    /**
     * Sets the value of the fecha property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setFecha(XMLGregorianCalendar value) {
        this.fecha = value;
    }

    /**
     * Gets the value of the fechaVencimientoPin property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getFechaVencimientoPin() {
        return fechaVencimientoPin;
    }

    /**
     * Sets the value of the fechaVencimientoPin property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setFechaVencimientoPin(XMLGregorianCalendar value) {
        this.fechaVencimientoPin = value;
    }

    /**
     * Gets the value of the finTicket property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getFinTicket() {
        return finTicket;
    }

    /**
     * Sets the value of the finTicket property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setFinTicket(JAXBElement<String> value) {
        this.finTicket = ((JAXBElement<String> ) value);
    }

    /**
     * Gets the value of the idCliente property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link Long }{@code >}
     *     
     */
    public JAXBElement<Long> getIdCliente() {
        return idCliente;
    }

    /**
     * Sets the value of the idCliente property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link Long }{@code >}
     *     
     */
    public void setIdCliente(JAXBElement<Long> value) {
        this.idCliente = ((JAXBElement<Long> ) value);
    }

    /**
     * Gets the value of the idEstadoTransaccion property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getIdEstadoTransaccion() {
        return idEstadoTransaccion;
    }

    /**
     * Sets the value of the idEstadoTransaccion property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setIdEstadoTransaccion(JAXBElement<String> value) {
        this.idEstadoTransaccion = ((JAXBElement<String> ) value);
    }

    /**
     * Gets the value of the idLlotePin property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getIdLlotePin() {
        return idLlotePin;
    }

    /**
     * Sets the value of the idLlotePin property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setIdLlotePin(JAXBElement<String> value) {
        this.idLlotePin = ((JAXBElement<String> ) value);
    }

    /**
     * Gets the value of the idProducto property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link Long }{@code >}
     *     
     */
    public JAXBElement<Long> getIdProducto() {
        return idProducto;
    }

    /**
     * Sets the value of the idProducto property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link Long }{@code >}
     *     
     */
    public void setIdProducto(JAXBElement<Long> value) {
        this.idProducto = ((JAXBElement<Long> ) value);
    }

    /**
     * Gets the value of the idTransaccion property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link Long }{@code >}
     *     
     */
    public JAXBElement<Long> getIdTransaccion() {
        return idTransaccion;
    }

    /**
     * Sets the value of the idTransaccion property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link Long }{@code >}
     *     
     */
    public void setIdTransaccion(JAXBElement<Long> value) {
        this.idTransaccion = ((JAXBElement<Long> ) value);
    }

    /**
     * Gets the value of the importe property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link Float }{@code >}
     *     
     */
    public JAXBElement<Float> getImporte() {
        return importe;
    }

    /**
     * Sets the value of the importe property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link Float }{@code >}
     *     
     */
    public void setImporte(JAXBElement<Float> value) {
        this.importe = ((JAXBElement<Float> ) value);
    }

    /**
     * Gets the value of the instrucciones property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getInstrucciones() {
        return instrucciones;
    }

    /**
     * Sets the value of the instrucciones property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setInstrucciones(JAXBElement<String> value) {
        this.instrucciones = ((JAXBElement<String> ) value);
    }

    /**
     * Gets the value of the moneda property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getMoneda() {
        return moneda;
    }

    /**
     * Sets the value of the moneda property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setMoneda(JAXBElement<String> value) {
        this.moneda = ((JAXBElement<String> ) value);
    }

    /**
     * Gets the value of the numeroTelefono property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link Integer }{@code >}
     *     
     */
    public JAXBElement<Integer> getNumeroTelefono() {
        return numeroTelefono;
    }

    /**
     * Sets the value of the numeroTelefono property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link Integer }{@code >}
     *     
     */
    public void setNumeroTelefono(JAXBElement<Integer> value) {
        this.numeroTelefono = ((JAXBElement<Integer> ) value);
    }

    /**
     * Gets the value of the pie1 property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getPie1() {
        return pie1;
    }

    /**
     * Sets the value of the pie1 property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setPie1(JAXBElement<String> value) {
        this.pie1 = ((JAXBElement<String> ) value);
    }

    /**
     * Gets the value of the pie2 property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getPie2() {
        return pie2;
    }

    /**
     * Sets the value of the pie2 property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setPie2(JAXBElement<String> value) {
        this.pie2 = ((JAXBElement<String> ) value);
    }

    /**
     * Gets the value of the pie3 property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getPie3() {
        return pie3;
    }

    /**
     * Sets the value of the pie3 property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setPie3(JAXBElement<String> value) {
        this.pie3 = ((JAXBElement<String> ) value);
    }

    /**
     * Gets the value of the pie4 property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getPie4() {
        return pie4;
    }

    /**
     * Sets the value of the pie4 property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setPie4(JAXBElement<String> value) {
        this.pie4 = ((JAXBElement<String> ) value);
    }

    /**
     * Gets the value of the pin property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getPin() {
        return pin;
    }

    /**
     * Sets the value of the pin property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setPin(JAXBElement<String> value) {
        this.pin = ((JAXBElement<String> ) value);
    }

    /**
     * Gets the value of the razonSocialCliente property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getRazonSocialCliente() {
        return razonSocialCliente;
    }

    /**
     * Sets the value of the razonSocialCliente property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setRazonSocialCliente(JAXBElement<String> value) {
        this.razonSocialCliente = ((JAXBElement<String> ) value);
    }

    /**
     * Gets the value of the saldoDisponibleDelCliente property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link Float }{@code >}
     *     
     */
    public JAXBElement<Float> getSaldoDisponibleDelCliente() {
        return saldoDisponibleDelCliente;
    }

    /**
     * Sets the value of the saldoDisponibleDelCliente property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link Float }{@code >}
     *     
     */
    public void setSaldoDisponibleDelCliente(JAXBElement<Float> value) {
        this.saldoDisponibleDelCliente = ((JAXBElement<Float> ) value);
    }

    /**
     * Gets the value of the serie property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getSerie() {
        return serie;
    }

    /**
     * Sets the value of the serie property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setSerie(JAXBElement<String> value) {
        this.serie = ((JAXBElement<String> ) value);
    }

    /**
     * Gets the value of the tipoOperacion property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getTipoOperacion() {
        return tipoOperacion;
    }

    /**
     * Sets the value of the tipoOperacion property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setTipoOperacion(JAXBElement<String> value) {
        this.tipoOperacion = ((JAXBElement<String> ) value);
    }

    /**
     * Gets the value of the usuario property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getUsuario() {
        return usuario;
    }

    /**
     * Sets the value of the usuario property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setUsuario(JAXBElement<String> value) {
        this.usuario = ((JAXBElement<String> ) value);
    }

    /**
     * Gets the value of the vigenciaPin property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getVigenciaPin() {
        return vigenciaPin;
    }

    /**
     * Sets the value of the vigenciaPin property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setVigenciaPin(JAXBElement<String> value) {
        this.vigenciaPin = ((JAXBElement<String> ) value);
    }

}
