
package com.americacg.cargavirtual.gcsi.entidad;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;
import com.americacg.cargavirtual.gcsi.service.BaseModel;


/**
 * <p>Java class for terminal complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="terminal">
 *   &lt;complexContent>
 *     &lt;extension base="{http://gateway.gcsi.cargavirtual.americacg.com/}baseModel">
 *       &lt;sequence>
 *         &lt;element name="id" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="cliente" type="{com.americacg.cargavirtual.gcsi.entidad}cliente"/>
 *         &lt;element name="operador" type="{com.americacg.cargavirtual.gcsi.entidad}operador"/>
 *         &lt;element name="mayorista" type="{com.americacg.cargavirtual.gcsi.entidad}mayorista"/>
 *         &lt;element name="codDiscriminador" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="estadoTerminal" type="{com.americacg.cargavirtual.gcsi.entidad}estadoTerminal"/>
 *         &lt;element name="usuarioUltMod" type="{com.americacg.cargavirtual.gcsi.entidad}usuario"/>
 *         &lt;element name="tmsUltMod" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "terminal", propOrder = {
    "id",
    "cliente",
    "operador",
    "mayorista",
    "codDiscriminador",
    "estadoTerminal",
    "usuarioUltMod",
    "tmsUltMod"
})
@XmlSeeAlso({
    TerminalGIRE.class
})
public abstract class Terminal
    extends BaseModel
{

    protected long id;
    @XmlElement(required = true, nillable = true)
    protected Cliente cliente;
    @XmlElement(required = true, nillable = true)
    protected Operador operador;
    @XmlElement(required = true, nillable = true)
    protected Mayorista mayorista;
    @XmlElement(required = true)
    protected String codDiscriminador;
    @XmlElement(required = true, nillable = true)
    protected EstadoTerminal estadoTerminal;
    @XmlElement(required = true, nillable = true)
    protected Usuario usuarioUltMod;
    @XmlElement(required = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar tmsUltMod;

    /**
     * Gets the value of the id property.
     * 
     */
    public long getId() {
        return id;
    }

    /**
     * Sets the value of the id property.
     * 
     */
    public void setId(long value) {
        this.id = value;
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
     * Gets the value of the operador property.
     * 
     * @return
     *     possible object is
     *     {@link Operador }
     *     
     */
    public Operador getOperador() {
        return operador;
    }

    /**
     * Sets the value of the operador property.
     * 
     * @param value
     *     allowed object is
     *     {@link Operador }
     *     
     */
    public void setOperador(Operador value) {
        this.operador = value;
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
     * Gets the value of the codDiscriminador property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodDiscriminador() {
        return codDiscriminador;
    }

    /**
     * Sets the value of the codDiscriminador property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodDiscriminador(String value) {
        this.codDiscriminador = value;
    }

    /**
     * Gets the value of the estadoTerminal property.
     * 
     * @return
     *     possible object is
     *     {@link EstadoTerminal }
     *     
     */
    public EstadoTerminal getEstadoTerminal() {
        return estadoTerminal;
    }

    /**
     * Sets the value of the estadoTerminal property.
     * 
     * @param value
     *     allowed object is
     *     {@link EstadoTerminal }
     *     
     */
    public void setEstadoTerminal(EstadoTerminal value) {
        this.estadoTerminal = value;
    }

    /**
     * Gets the value of the usuarioUltMod property.
     * 
     * @return
     *     possible object is
     *     {@link Usuario }
     *     
     */
    public Usuario getUsuarioUltMod() {
        return usuarioUltMod;
    }

    /**
     * Sets the value of the usuarioUltMod property.
     * 
     * @param value
     *     allowed object is
     *     {@link Usuario }
     *     
     */
    public void setUsuarioUltMod(Usuario value) {
        this.usuarioUltMod = value;
    }

    /**
     * Gets the value of the tmsUltMod property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getTmsUltMod() {
        return tmsUltMod;
    }

    /**
     * Sets the value of the tmsUltMod property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setTmsUltMod(XMLGregorianCalendar value) {
        this.tmsUltMod = value;
    }

}
