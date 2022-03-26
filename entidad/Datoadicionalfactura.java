
package com.americacg.cargavirtual.gcsi.entidad;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;
import com.americacg.cargavirtual.gcsi.service.BaseModel;


/**
 * <p>Java class for datoadicionalfactura complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="datoadicionalfactura">
 *   &lt;complexContent>
 *     &lt;extension base="{http://gateway.gcsi.cargavirtual.americacg.com/}baseModel">
 *       &lt;sequence>
 *         &lt;element name="id" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="operador" type="{http://www.w3.org/2001/XMLSchema}anyType"/>
 *         &lt;element name="mayorista" type="{http://www.w3.org/2001/XMLSchema}anyType"/>
 *         &lt;element name="cliente" type="{http://www.w3.org/2001/XMLSchema}anyType"/>
 *         &lt;element name="usuario" type="{http://www.w3.org/2001/XMLSchema}anyType"/>
 *         &lt;element name="usuarioUltimaModificacion" type="{http://www.w3.org/2001/XMLSchema}anyType"/>
 *         &lt;element name="ordinal" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="valorDato" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="codDato" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="descripcionDato" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
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
@XmlType(name = "datoadicionalfactura", propOrder = {
    "id",
    "operador",
    "mayorista",
    "cliente",
    "usuario",
    "usuarioUltimaModificacion",
    "ordinal",
    "valorDato",
    "codDato",
    "descripcionDato",
    "fechaUltMod"
})
public class Datoadicionalfactura
    extends BaseModel
{

    protected Long id;
    @XmlElement(required = true, nillable = true)
    protected Object operador;
    @XmlElement(required = true, nillable = true)
    protected Object mayorista;
    @XmlElement(required = true, nillable = true)
    protected Object cliente;
    @XmlElement(required = true, nillable = true)
    protected Object usuario;
    @XmlElement(required = true, nillable = true)
    protected Object usuarioUltimaModificacion;
    protected Long ordinal;
    protected String valorDato;
    protected String codDato;
    protected String descripcionDato;
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
     * Gets the value of the operador property.
     * 
     * @return
     *     possible object is
     *     {@link Object }
     *     
     */
    public Object getOperador() {
        return operador;
    }

    /**
     * Sets the value of the operador property.
     * 
     * @param value
     *     allowed object is
     *     {@link Object }
     *     
     */
    public void setOperador(Object value) {
        this.operador = value;
    }

    /**
     * Gets the value of the mayorista property.
     * 
     * @return
     *     possible object is
     *     {@link Object }
     *     
     */
    public Object getMayorista() {
        return mayorista;
    }

    /**
     * Sets the value of the mayorista property.
     * 
     * @param value
     *     allowed object is
     *     {@link Object }
     *     
     */
    public void setMayorista(Object value) {
        this.mayorista = value;
    }

    /**
     * Gets the value of the cliente property.
     * 
     * @return
     *     possible object is
     *     {@link Object }
     *     
     */
    public Object getCliente() {
        return cliente;
    }

    /**
     * Sets the value of the cliente property.
     * 
     * @param value
     *     allowed object is
     *     {@link Object }
     *     
     */
    public void setCliente(Object value) {
        this.cliente = value;
    }

    /**
     * Gets the value of the usuario property.
     * 
     * @return
     *     possible object is
     *     {@link Object }
     *     
     */
    public Object getUsuario() {
        return usuario;
    }

    /**
     * Sets the value of the usuario property.
     * 
     * @param value
     *     allowed object is
     *     {@link Object }
     *     
     */
    public void setUsuario(Object value) {
        this.usuario = value;
    }

    /**
     * Gets the value of the usuarioUltimaModificacion property.
     * 
     * @return
     *     possible object is
     *     {@link Object }
     *     
     */
    public Object getUsuarioUltimaModificacion() {
        return usuarioUltimaModificacion;
    }

    /**
     * Sets the value of the usuarioUltimaModificacion property.
     * 
     * @param value
     *     allowed object is
     *     {@link Object }
     *     
     */
    public void setUsuarioUltimaModificacion(Object value) {
        this.usuarioUltimaModificacion = value;
    }

    /**
     * Gets the value of the ordinal property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getOrdinal() {
        return ordinal;
    }

    /**
     * Sets the value of the ordinal property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setOrdinal(Long value) {
        this.ordinal = value;
    }

    /**
     * Gets the value of the valorDato property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getValorDato() {
        return valorDato;
    }

    /**
     * Sets the value of the valorDato property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setValorDato(String value) {
        this.valorDato = value;
    }

    /**
     * Gets the value of the codDato property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodDato() {
        return codDato;
    }

    /**
     * Sets the value of the codDato property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodDato(String value) {
        this.codDato = value;
    }

    /**
     * Gets the value of the descripcionDato property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescripcionDato() {
        return descripcionDato;
    }

    /**
     * Sets the value of the descripcionDato property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescripcionDato(String value) {
        this.descripcionDato = value;
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
