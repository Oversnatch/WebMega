
package com.americacg.cargavirtual.gcsi.model.acg.request;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for obtenerFacturaSinSoportePapelGIRERequest complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="obtenerFacturaSinSoportePapelGIRERequest">
 *   &lt;complexContent>
 *     &lt;extension base="{com.americacg.cargavirtual.gcsi.model.acg.request}obtenerFacturaSinSoportePapelRequestACG">
 *       &lt;sequence>
 *         &lt;element name="idMod" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="codEmp" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="datosFormulario">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="entry" maxOccurs="unbounded" minOccurs="0">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;sequence>
 *                             &lt;element name="key" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                             &lt;element name="value" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                           &lt;/sequence>
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="idTrxAnterior" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="descEmp" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
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
@XmlType(name = "obtenerFacturaSinSoportePapelGIRERequest", propOrder = {
    "idMod",
    "codEmp",
    "datosFormulario",
    "idTrxAnterior",
    "descEmp",
    "idFormaDePago",
    "impMinNeg",
    "impMaxNeg",
    "impMinPos",
    "impMaxPos",
    "anula"
})
public class ObtenerFacturaSinSoportePapelGIRERequest
    extends ObtenerFacturaSinSoportePapelRequestACG
{

    @XmlElement(required = true)
    protected String idMod;
    protected Long codEmp;
    @XmlElement(required = true)
    protected ObtenerFacturaSinSoportePapelGIRERequest.DatosFormulario datosFormulario;
    protected String idTrxAnterior;
    protected String descEmp;
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
     * Gets the value of the codEmp property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getCodEmp() {
        return codEmp;
    }

    /**
     * Sets the value of the codEmp property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setCodEmp(Long value) {
        this.codEmp = value;
    }

    /**
     * Gets the value of the datosFormulario property.
     * 
     * @return
     *     possible object is
     *     {@link ObtenerFacturaSinSoportePapelGIRERequest.DatosFormulario }
     *     
     */
    public ObtenerFacturaSinSoportePapelGIRERequest.DatosFormulario getDatosFormulario() {
        return datosFormulario;
    }

    /**
     * Sets the value of the datosFormulario property.
     * 
     * @param value
     *     allowed object is
     *     {@link ObtenerFacturaSinSoportePapelGIRERequest.DatosFormulario }
     *     
     */
    public void setDatosFormulario(ObtenerFacturaSinSoportePapelGIRERequest.DatosFormulario value) {
        this.datosFormulario = value;
    }

    /**
     * Gets the value of the idTrxAnterior property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdTrxAnterior() {
        return idTrxAnterior;
    }

    /**
     * Sets the value of the idTrxAnterior property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdTrxAnterior(String value) {
        this.idTrxAnterior = value;
    }

    /**
     * Gets the value of the descEmp property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescEmp() {
        return descEmp;
    }

    /**
     * Sets the value of the descEmp property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescEmp(String value) {
        this.descEmp = value;
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


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="entry" maxOccurs="unbounded" minOccurs="0">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;sequence>
     *                   &lt;element name="key" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *                   &lt;element name="value" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *                 &lt;/sequence>
     *               &lt;/restriction>
     *             &lt;/complexContent>
     *           &lt;/complexType>
     *         &lt;/element>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "entry"
    })
    public static class DatosFormulario {

        protected List<ObtenerFacturaSinSoportePapelGIRERequest.DatosFormulario.Entry> entry;

        /**
         * Gets the value of the entry property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the entry property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getEntry().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link ObtenerFacturaSinSoportePapelGIRERequest.DatosFormulario.Entry }
         * 
         * 
         */
        public List<ObtenerFacturaSinSoportePapelGIRERequest.DatosFormulario.Entry> getEntry() {
            if (entry == null) {
                entry = new ArrayList<ObtenerFacturaSinSoportePapelGIRERequest.DatosFormulario.Entry>();
            }
            return this.entry;
        }


        /**
         * <p>Java class for anonymous complex type.
         * 
         * <p>The following schema fragment specifies the expected content contained within this class.
         * 
         * <pre>
         * &lt;complexType>
         *   &lt;complexContent>
         *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *       &lt;sequence>
         *         &lt;element name="key" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
         *         &lt;element name="value" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
         *       &lt;/sequence>
         *     &lt;/restriction>
         *   &lt;/complexContent>
         * &lt;/complexType>
         * </pre>
         * 
         * 
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "", propOrder = {
            "key",
            "value"
        })
        public static class Entry {

            protected String key;
            protected String value;

            /**
             * Gets the value of the key property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getKey() {
                return key;
            }

            /**
             * Sets the value of the key property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setKey(String value) {
                this.key = value;
            }

            /**
             * Gets the value of the value property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getValue() {
                return value;
            }

            /**
             * Sets the value of the value property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setValue(String value) {
                this.value = value;
            }

        }

    }

}
