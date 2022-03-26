
package com.americacg.cargavirtual.gcsi.model.acg.request;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for obtenerParamFacSinSopPapelGIRERequest complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="obtenerParamFacSinSopPapelGIRERequest">
 *   &lt;complexContent>
 *     &lt;extension base="{com.americacg.cargavirtual.gcsi.model.acg.request}obtenerParametriaFacturaSinSoportePapelRequestACG">
 *       &lt;sequence>
 *         &lt;element name="idMod" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="idFormaDePago" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="impMinNeg" type="{http://www.w3.org/2001/XMLSchema}float"/>
 *         &lt;element name="impMaxNeg" type="{http://www.w3.org/2001/XMLSchema}float"/>
 *         &lt;element name="impMinPos" type="{http://www.w3.org/2001/XMLSchema}float"/>
 *         &lt;element name="impMaxPos" type="{http://www.w3.org/2001/XMLSchema}float"/>
 *         &lt;element name="anula" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "obtenerParamFacSinSopPapelGIRERequest", propOrder = {
    "idMod",
    "idFormaDePago",
    "impMinNeg",
    "impMaxNeg",
    "impMinPos",
    "impMaxPos",
    "anula"
})
public class ObtenerParamFacSinSopPapelGIRERequest
    extends ObtenerParametriaFacturaSinSoportePapelRequestACG
{

    @XmlElement(required = true)
    protected String idMod;
    @XmlElement(required = true)
    protected String idFormaDePago;
    protected float impMinNeg;
    protected float impMaxNeg;
    protected float impMinPos;
    protected float impMaxPos;
    @XmlElement(required = true)
    protected String anula;

    /**
     * Gets the value of the idMod property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdMod() {
        return idMod;
    }

    /**
     * Sets the value of the idMod property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdMod(String value) {
        this.idMod = value;
    }

    /**
     * Gets the value of the idFormaDePago property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdFormaDePago() {
        return idFormaDePago;
    }

    /**
     * Sets the value of the idFormaDePago property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdFormaDePago(String value) {
        this.idFormaDePago = value;
    }

    /**
     * Gets the value of the impMinNeg property.
     * 
     */
    public float getImpMinNeg() {
        return impMinNeg;
    }

    /**
     * Sets the value of the impMinNeg property.
     * 
     */
    public void setImpMinNeg(float value) {
        this.impMinNeg = value;
    }

    /**
     * Gets the value of the impMaxNeg property.
     * 
     */
    public float getImpMaxNeg() {
        return impMaxNeg;
    }

    /**
     * Sets the value of the impMaxNeg property.
     * 
     */
    public void setImpMaxNeg(float value) {
        this.impMaxNeg = value;
    }

    /**
     * Gets the value of the impMinPos property.
     * 
     */
    public float getImpMinPos() {
        return impMinPos;
    }

    /**
     * Sets the value of the impMinPos property.
     * 
     */
    public void setImpMinPos(float value) {
        this.impMinPos = value;
    }

    /**
     * Gets the value of the impMaxPos property.
     * 
     */
    public float getImpMaxPos() {
        return impMaxPos;
    }

    /**
     * Sets the value of the impMaxPos property.
     * 
     */
    public void setImpMaxPos(float value) {
        this.impMaxPos = value;
    }

    /**
     * Gets the value of the anula property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAnula() {
        return anula;
    }

    /**
     * Sets the value of the anula property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAnula(String value) {
        this.anula = value;
    }

}
