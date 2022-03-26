
package com.americacg.cargavirtual.gcsi.model.acg.response;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import com.americacg.cargavirtual.gcsi.service.Campo;


/**
 * <p>Java class for obtenerParamFacSinSopPapelGIREResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="obtenerParamFacSinSopPapelGIREResponse">
 *   &lt;complexContent>
 *     &lt;extension base="{com.americacg.cargavirtual.gcsi.model.acg.response}obtenerParametriaFacturaSinSoportePapelResponseACG">
 *       &lt;sequence>
 *         &lt;element name="titulo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="idMod" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="idFormaDePago" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="impMinNeg" type="{http://www.w3.org/2001/XMLSchema}float" minOccurs="0"/>
 *         &lt;element name="impMaxNeg" type="{http://www.w3.org/2001/XMLSchema}float" minOccurs="0"/>
 *         &lt;element name="impMinPos" type="{http://www.w3.org/2001/XMLSchema}float" minOccurs="0"/>
 *         &lt;element name="impMaxPos" type="{http://www.w3.org/2001/XMLSchema}float" minOccurs="0"/>
 *         &lt;element name="anula" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="campos" type="{http://gateway.gcsi.cargavirtual.americacg.com/}campo" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "obtenerParamFacSinSopPapelGIREResponse", propOrder = {
    "titulo",
    "idMod",
    "idFormaDePago",
    "impMinNeg",
    "impMaxNeg",
    "impMinPos",
    "impMaxPos",
    "anula",
    "campos"
})
public class ObtenerParamFacSinSopPapelGIREResponse
    extends ObtenerParametriaFacturaSinSoportePapelResponseACG
{

    protected String titulo;
    protected String idMod;
    protected String idFormaDePago;
    protected Float impMinNeg;
    protected Float impMaxNeg;
    protected Float impMinPos;
    protected Float impMaxPos;
    protected String anula;
    @XmlElement(nillable = true)
    protected List<Campo> campos;

    /**
     * Gets the value of the titulo property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTitulo() {
        return titulo;
    }

    /**
     * Sets the value of the titulo property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTitulo(String value) {
        this.titulo = value;
    }

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
     * @return
     *     possible object is
     *     {@link Float }
     *     
     */
    public Float getImpMinNeg() {
        return impMinNeg;
    }

    /**
     * Sets the value of the impMinNeg property.
     * 
     * @param value
     *     allowed object is
     *     {@link Float }
     *     
     */
    public void setImpMinNeg(Float value) {
        this.impMinNeg = value;
    }

    /**
     * Gets the value of the impMaxNeg property.
     * 
     * @return
     *     possible object is
     *     {@link Float }
     *     
     */
    public Float getImpMaxNeg() {
        return impMaxNeg;
    }

    /**
     * Sets the value of the impMaxNeg property.
     * 
     * @param value
     *     allowed object is
     *     {@link Float }
     *     
     */
    public void setImpMaxNeg(Float value) {
        this.impMaxNeg = value;
    }

    /**
     * Gets the value of the impMinPos property.
     * 
     * @return
     *     possible object is
     *     {@link Float }
     *     
     */
    public Float getImpMinPos() {
        return impMinPos;
    }

    /**
     * Sets the value of the impMinPos property.
     * 
     * @param value
     *     allowed object is
     *     {@link Float }
     *     
     */
    public void setImpMinPos(Float value) {
        this.impMinPos = value;
    }

    /**
     * Gets the value of the impMaxPos property.
     * 
     * @return
     *     possible object is
     *     {@link Float }
     *     
     */
    public Float getImpMaxPos() {
        return impMaxPos;
    }

    /**
     * Sets the value of the impMaxPos property.
     * 
     * @param value
     *     allowed object is
     *     {@link Float }
     *     
     */
    public void setImpMaxPos(Float value) {
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

    /**
     * Gets the value of the campos property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the campos property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCampos().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Campo }
     * 
     * 
     */
    public List<Campo> getCampos() {
        if (campos == null) {
            campos = new ArrayList<Campo>();
        }
        return this.campos;
    }

}
