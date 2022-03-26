
package com.americacg.cargavirtual.gcsi.entidad;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;
import com.americacg.cargavirtual.gcsi.service.BaseModel;


/**
 * <p>Java class for turnoCaja complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="turnoCaja">
 *   &lt;complexContent>
 *     &lt;extension base="{http://gateway.gcsi.cargavirtual.americacg.com/}baseModel">
 *       &lt;sequence>
 *         &lt;element name="id" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="operador" type="{com.americacg.cargavirtual.gcsi.entidad}operador" minOccurs="0"/>
 *         &lt;element name="mayorista" type="{com.americacg.cargavirtual.gcsi.entidad}mayorista" minOccurs="0"/>
 *         &lt;element name="cliente" type="{com.americacg.cargavirtual.gcsi.entidad}cliente" minOccurs="0"/>
 *         &lt;element name="usuario" type="{com.americacg.cargavirtual.gcsi.entidad}usuario" minOccurs="0"/>
 *         &lt;element name="fechaAlta" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="fechaAperturaCaja" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="fechaCierreCaja" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="estadoId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="estadoDescripcion" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="codigo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="entidad" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="agencia" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="terminal" type="{com.americacg.cargavirtual.gcsi.entidad}terminal" minOccurs="0"/>
 *         &lt;element name="lote" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="usuarioUltimaModificacion" type="{com.americacg.cargavirtual.gcsi.entidad}usuario" minOccurs="0"/>
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
@XmlType(name = "turnoCaja", propOrder = {
    "id",
    "operador",
    "mayorista",
    "cliente",
    "usuario",
    "fechaAlta",
    "fechaAperturaCaja",
    "fechaCierreCaja",
    "estadoId",
    "estadoDescripcion",
    "codigo",
    "entidad",
    "agencia",
    "terminal",
    "lote",
    "usuarioUltimaModificacion",
    "fechaUltMod"
})
public class TurnoCaja
    extends BaseModel
{

    protected Long id;
    @XmlElement(nillable = true)
    protected Operador operador;
    @XmlElement(nillable = true)
    protected Mayorista mayorista;
    @XmlElement(nillable = true)
    protected Cliente cliente;
    @XmlElement(nillable = true)
    protected Usuario usuario;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar fechaAlta;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar fechaAperturaCaja;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar fechaCierreCaja;
    protected String estadoId;
    protected String estadoDescripcion;
    protected String codigo;
    protected String entidad;
    protected String agencia;
    @XmlElement(nillable = true)
    protected Terminal terminal;
    protected String lote;
    @XmlElement(nillable = true)
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
     * Gets the value of the fechaAlta property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getFechaAlta() {
        return fechaAlta;
    }

    /**
     * Sets the value of the fechaAlta property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setFechaAlta(XMLGregorianCalendar value) {
        this.fechaAlta = value;
    }

    /**
     * Gets the value of the fechaAperturaCaja property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getFechaAperturaCaja() {
        return fechaAperturaCaja;
    }

    /**
     * Sets the value of the fechaAperturaCaja property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setFechaAperturaCaja(XMLGregorianCalendar value) {
        this.fechaAperturaCaja = value;
    }

    /**
     * Gets the value of the fechaCierreCaja property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getFechaCierreCaja() {
        return fechaCierreCaja;
    }

    /**
     * Sets the value of the fechaCierreCaja property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setFechaCierreCaja(XMLGregorianCalendar value) {
        this.fechaCierreCaja = value;
    }

    /**
     * Gets the value of the estadoId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEstadoId() {
        return estadoId;
    }

    /**
     * Sets the value of the estadoId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEstadoId(String value) {
        this.estadoId = value;
    }

    /**
     * Gets the value of the estadoDescripcion property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEstadoDescripcion() {
        return estadoDescripcion;
    }

    /**
     * Sets the value of the estadoDescripcion property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEstadoDescripcion(String value) {
        this.estadoDescripcion = value;
    }

    /**
     * Gets the value of the codigo property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodigo() {
        return codigo;
    }

    /**
     * Sets the value of the codigo property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodigo(String value) {
        this.codigo = value;
    }

    /**
     * Gets the value of the entidad property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEntidad() {
        return entidad;
    }

    /**
     * Sets the value of the entidad property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEntidad(String value) {
        this.entidad = value;
    }

    /**
     * Gets the value of the agencia property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAgencia() {
        return agencia;
    }

    /**
     * Sets the value of the agencia property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAgencia(String value) {
        this.agencia = value;
    }

    /**
     * Gets the value of the terminal property.
     * 
     * @return
     *     possible object is
     *     {@link Terminal }
     *     
     */
    public Terminal getTerminal() {
        return terminal;
    }

    /**
     * Sets the value of the terminal property.
     * 
     * @param value
     *     allowed object is
     *     {@link Terminal }
     *     
     */
    public void setTerminal(Terminal value) {
        this.terminal = value;
    }

    /**
     * Gets the value of the lote property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLote() {
        return lote;
    }

    /**
     * Sets the value of the lote property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLote(String value) {
        this.lote = value;
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
