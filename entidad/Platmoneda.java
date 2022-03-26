
package com.americacg.cargavirtual.gcsi.entidad;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import com.americacg.cargavirtual.gcsi.service.BaseModel;


/**
 * <p>Java class for platmoneda complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="platmoneda">
 *   &lt;complexContent>
 *     &lt;extension base="{http://gateway.gcsi.cargavirtual.americacg.com/}baseModel">
 *       &lt;sequence>
 *         &lt;element name="id" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="descripcion" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="idPais" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="codISO4217A3" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="codISO4217Num" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="txtSimbolo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="idEstado" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="idUsuUltMod" type="{com.americacg.cargavirtual.gcsi.entidad}usuario" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "platmoneda", propOrder = {
    "id",
    "descripcion",
    "idPais",
    "codISO4217A3",
    "codISO4217Num",
    "txtSimbolo",
    "idEstado",
    "idUsuUltMod"
})
public class Platmoneda
    extends BaseModel
{

    protected Long id;
    protected String descripcion;
    protected Long idPais;
    protected String codISO4217A3;
    protected String codISO4217Num;
    protected String txtSimbolo;
    protected Long idEstado;
    @XmlElement(nillable = true)
    protected Usuario idUsuUltMod;

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
     * Gets the value of the idPais property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getIdPais() {
        return idPais;
    }

    /**
     * Sets the value of the idPais property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setIdPais(Long value) {
        this.idPais = value;
    }

    /**
     * Gets the value of the codISO4217A3 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodISO4217A3() {
        return codISO4217A3;
    }

    /**
     * Sets the value of the codISO4217A3 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodISO4217A3(String value) {
        this.codISO4217A3 = value;
    }

    /**
     * Gets the value of the codISO4217Num property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodISO4217Num() {
        return codISO4217Num;
    }

    /**
     * Sets the value of the codISO4217Num property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodISO4217Num(String value) {
        this.codISO4217Num = value;
    }

    /**
     * Gets the value of the txtSimbolo property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTxtSimbolo() {
        return txtSimbolo;
    }

    /**
     * Sets the value of the txtSimbolo property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTxtSimbolo(String value) {
        this.txtSimbolo = value;
    }

    /**
     * Gets the value of the idEstado property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getIdEstado() {
        return idEstado;
    }

    /**
     * Sets the value of the idEstado property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setIdEstado(Long value) {
        this.idEstado = value;
    }

    /**
     * Gets the value of the idUsuUltMod property.
     * 
     * @return
     *     possible object is
     *     {@link Usuario }
     *     
     */
    public Usuario getIdUsuUltMod() {
        return idUsuUltMod;
    }

    /**
     * Sets the value of the idUsuUltMod property.
     * 
     * @param value
     *     allowed object is
     *     {@link Usuario }
     *     
     */
    public void setIdUsuUltMod(Usuario value) {
        this.idUsuUltMod = value;
    }

}
