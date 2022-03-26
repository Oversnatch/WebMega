
package com.americacg.cargavirtual.gcsi.entidad;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import com.americacg.cargavirtual.gcsi.service.BaseModel;


/**
 * <p>Java class for estadoTerminal complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="estadoTerminal">
 *   &lt;complexContent>
 *     &lt;extension base="{http://gateway.gcsi.cargavirtual.americacg.com/}baseModel">
 *       &lt;sequence>
 *         &lt;element name="id" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="operador" type="{com.americacg.cargavirtual.gcsi.entidad}operador" minOccurs="0"/>
 *         &lt;element name="codMnemonico" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="descripcion" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "estadoTerminal", propOrder = {
    "id",
    "operador",
    "codMnemonico",
    "descripcion"
})
public class EstadoTerminal
    extends BaseModel
{

    protected Long id;
    @XmlElement(nillable = true)
    protected Operador operador;
    protected String codMnemonico;
    protected String descripcion;

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

}
