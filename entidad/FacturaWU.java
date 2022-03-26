
package com.americacg.cargavirtual.gcsi.entidad;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for facturaWU complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="facturaWU">
 *   &lt;complexContent>
 *     &lt;extension base="{com.americacg.cargavirtual.gcsi.entidad}factura">
 *       &lt;sequence>
 *         &lt;element name="idProducto" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="secuenciaLote" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="numeroCuenta" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="estadoWU" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="reimpresionTicket" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "facturaWU", propOrder = {
    "idProducto",
    "secuenciaLote",
    "numeroCuenta",
    "estadoWU",
    "reimpresionTicket"
})
public class FacturaWU
    extends Factura
{

    protected Long idProducto;
    protected Long secuenciaLote;
    protected String numeroCuenta;
    protected String estadoWU;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar reimpresionTicket;

    /**
     * Gets the value of the idProducto property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getIdProducto() {
        return idProducto;
    }

    /**
     * Sets the value of the idProducto property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setIdProducto(Long value) {
        this.idProducto = value;
    }

    /**
     * Gets the value of the secuenciaLote property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getSecuenciaLote() {
        return secuenciaLote;
    }

    /**
     * Sets the value of the secuenciaLote property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setSecuenciaLote(Long value) {
        this.secuenciaLote = value;
    }

    /**
     * Gets the value of the numeroCuenta property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNumeroCuenta() {
        return numeroCuenta;
    }

    /**
     * Sets the value of the numeroCuenta property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNumeroCuenta(String value) {
        this.numeroCuenta = value;
    }

    /**
     * Gets the value of the estadoWU property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEstadoWU() {
        return estadoWU;
    }

    /**
     * Sets the value of the estadoWU property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEstadoWU(String value) {
        this.estadoWU = value;
    }

    /**
     * Gets the value of the reimpresionTicket property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getReimpresionTicket() {
        return reimpresionTicket;
    }

    /**
     * Sets the value of the reimpresionTicket property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setReimpresionTicket(XMLGregorianCalendar value) {
        this.reimpresionTicket = value;
    }

}
