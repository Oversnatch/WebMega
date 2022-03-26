
package com.americacg.cargavirtual.gcsi.entidad;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;
import com.americacg.cargavirtual.gcsi.service.BaseModel;


/**
 * <p>Java class for comprobante complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="comprobante">
 *   &lt;complexContent>
 *     &lt;extension base="{http://gateway.gcsi.cargavirtual.americacg.com/}baseModel">
 *       &lt;sequence>
 *         &lt;element name="id" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="mayorista" type="{com.americacg.cargavirtual.gcsi.entidad}mayorista"/>
 *         &lt;element name="usuario" type="{com.americacg.cargavirtual.gcsi.entidad}usuario"/>
 *         &lt;element name="secuencia" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="detalleComprobante" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="cliente" type="{com.americacg.cargavirtual.gcsi.entidad}cliente"/>
 *         &lt;element name="usuarioUltimaModificacion" type="{com.americacg.cargavirtual.gcsi.entidad}usuario"/>
 *         &lt;element name="fechaUltMod" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "comprobante", propOrder = {
    "id",
    "mayorista",
    "usuario",
    "secuencia",
    "detalleComprobante",
    "cliente",
    "usuarioUltimaModificacion",
    "fechaUltMod"
})
public class Comprobante
    extends BaseModel
{

    protected Long id;
    @XmlElement(required = true, nillable = true)
    protected Mayorista mayorista;
    @XmlElement(required = true, nillable = true)
    protected Usuario usuario;
    protected Long secuencia;
    protected String detalleComprobante;
    @XmlElement(required = true, nillable = true)
    protected Cliente cliente;
    @XmlElement(required = true, nillable = true)
    protected Usuario usuarioUltimaModificacion;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar fechaUltMod;

    /**
     * Gets the value of the id property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the value of the id property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setId(Long value) {
        this.id = value;
    }

    /**
     * Gets the value of the mayorista property.
     * 
     * @return
     *     possible object is
     *     {@link Mayorista }
     *     
     */
    public Mayorista getMayorista() {
        return mayorista;
    }

    /**
     * Sets the value of the mayorista property.
     * 
     * @param value
     *     allowed object is
     *     {@link Mayorista }
     *     
     */
    public void setMayorista(Mayorista value) {
        this.mayorista = value;
    }

    /**
     * Gets the value of the usuario property.
     * 
     * @return
     *     possible object is
     *     {@link Usuario }
     *     
     */
    public Usuario getUsuario() {
        return usuario;
    }

    /**
     * Sets the value of the usuario property.
     * 
     * @param value
     *     allowed object is
     *     {@link Usuario }
     *     
     */
    public void setUsuario(Usuario value) {
        this.usuario = value;
    }

    /**
     * Gets the value of the secuencia property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getSecuencia() {
        return secuencia;
    }

    /**
     * Sets the value of the secuencia property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setSecuencia(Long value) {
        this.secuencia = value;
    }

    /**
     * Gets the value of the detalleComprobante property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDetalleComprobante() {
        return detalleComprobante;
    }

    /**
     * Sets the value of the detalleComprobante property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDetalleComprobante(String value) {
        this.detalleComprobante = value;
    }

    /**
     * Gets the value of the cliente property.
     * 
     * @return
     *     possible object is
     *     {@link Cliente }
     *     
     */
    public Cliente getCliente() {
        return cliente;
    }

    /**
     * Sets the value of the cliente property.
     * 
     * @param value
     *     allowed object is
     *     {@link Cliente }
     *     
     */
    public void setCliente(Cliente value) {
        this.cliente = value;
    }

    /**
     * Gets the value of the usuarioUltimaModificacion property.
     * 
     * @return
     *     possible object is
     *     {@link Usuario }
     *     
     */
    public Usuario getUsuarioUltimaModificacion() {
        return usuarioUltimaModificacion;
    }

    /**
     * Sets the value of the usuarioUltimaModificacion property.
     * 
     * @param value
     *     allowed object is
     *     {@link Usuario }
     *     
     */
    public void setUsuarioUltimaModificacion(Usuario value) {
        this.usuarioUltimaModificacion = value;
    }

    /**
     * Gets the value of the fechaUltMod property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getFechaUltMod() {
        return fechaUltMod;
    }

    /**
     * Sets the value of the fechaUltMod property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setFechaUltMod(XMLGregorianCalendar value) {
        this.fechaUltMod = value;
    }

}
