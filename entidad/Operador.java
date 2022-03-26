
package com.americacg.cargavirtual.gcsi.entidad;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;
import com.americacg.cargavirtual.gcsi.service.BaseModel;


/**
 * <p>Java class for operador complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="operador">
 *   &lt;complexContent>
 *     &lt;extension base="{http://gateway.gcsi.cargavirtual.americacg.com/}baseModel">
 *       &lt;sequence>
 *         &lt;element name="id" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="descripcion" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="codMnemonico" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="estado" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
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
@XmlType(name = "operador", propOrder = {
    "id",
    "descripcion",
    "codMnemonico",
    "estado",
    "usuarioUltimaModificacion",
    "fechaUltMod"
})
public class Operador
    extends BaseModel
{

    protected Long id;
    protected String descripcion;
    protected String codMnemonico;
    protected Integer estado;
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
     * Gets the value of the descripcion property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescripcion() {
        return descripcion;
    }

    /**
     * Sets the value of the descripcion property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescripcion(String value) {
        this.descripcion = value;
    }

    /**
     * Gets the value of the codMnemonico property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodMnemonico() {
        return codMnemonico;
    }

    /**
     * Sets the value of the codMnemonico property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodMnemonico(String value) {
        this.codMnemonico = value;
    }

    /**
     * Gets the value of the estado property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getEstado() {
        return estado;
    }

    /**
     * Sets the value of the estado property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setEstado(Integer value) {
        this.estado = value;
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
