
package com.americacg.cargavirtual.gestion.service;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;
import com.americacg.cargavirtual.gestion.model.HeaderSecur;


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
 *         &lt;element name="in0" type="{http://model.core.cargavirtual.americacg.com}HeaderSecur"/>
 *         &lt;element name="in1" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="in2" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="in3" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="in4" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="in5" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="in6" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="in7" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="in8" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 *         &lt;element name="in9" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="in10" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="in11" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="in12" type="{http://www.w3.org/2001/XMLSchema}string"/>
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
    "in0",
    "in1",
    "in2",
    "in3",
    "in4",
    "in5",
    "in6",
    "in7",
    "in8",
    "in9",
    "in10",
    "in11",
    "in12"
})
@XmlRootElement(name = "acreditarMovimientoCuenta")
public class AcreditarMovimientoCuenta {

    @XmlElement(required = true, nillable = true)
    protected HeaderSecur in0;
    @XmlElement(required = true, type = Long.class, nillable = true)
    protected Long in1;
    @XmlElement(required = true, type = Long.class, nillable = true)
    protected Long in2;
    @XmlElement(required = true, nillable = true)
    protected String in3;
    @XmlElement(required = true, nillable = true)
    protected String in4;
    @XmlElement(required = true, type = Long.class, nillable = true)
    protected Long in5;
    @XmlElement(required = true, nillable = true)
    protected String in6;
    @XmlElement(required = true, nillable = true)
    protected String in7;
    @XmlElement(required = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar in8;
    @XmlElement(required = true, type = Boolean.class, nillable = true)
    protected Boolean in9;
    @XmlElement(required = true, type = Boolean.class, nillable = true)
    protected Boolean in10;
    @XmlElement(required = true, type = Boolean.class, nillable = true)
    protected Boolean in11;
    @XmlElement(required = true, nillable = true)
    protected String in12;

    /**
     * Gets the value of the in0 property.
     * 
     * @return
     *     possible object is
     *     {@link HeaderSecur }
     *     
     */
    public HeaderSecur getIn0() {
        return in0;
    }

    /**
     * Sets the value of the in0 property.
     * 
     * @param value
     *     allowed object is
     *     {@link HeaderSecur }
     *     
     */
    public void setIn0(HeaderSecur value) {
        this.in0 = value;
    }

    /**
     * Gets the value of the in1 property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getIn1() {
        return in1;
    }

    /**
     * Sets the value of the in1 property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setIn1(Long value) {
        this.in1 = value;
    }

    /**
     * Gets the value of the in2 property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getIn2() {
        return in2;
    }

    /**
     * Sets the value of the in2 property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setIn2(Long value) {
        this.in2 = value;
    }

    /**
     * Gets the value of the in3 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIn3() {
        return in3;
    }

    /**
     * Sets the value of the in3 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIn3(String value) {
        this.in3 = value;
    }

    /**
     * Gets the value of the in4 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIn4() {
        return in4;
    }

    /**
     * Sets the value of the in4 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIn4(String value) {
        this.in4 = value;
    }

    /**
     * Gets the value of the in5 property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getIn5() {
        return in5;
    }

    /**
     * Sets the value of the in5 property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setIn5(Long value) {
        this.in5 = value;
    }

    /**
     * Gets the value of the in6 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIn6() {
        return in6;
    }

    /**
     * Sets the value of the in6 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIn6(String value) {
        this.in6 = value;
    }

    /**
     * Gets the value of the in7 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIn7() {
        return in7;
    }

    /**
     * Sets the value of the in7 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIn7(String value) {
        this.in7 = value;
    }

    /**
     * Gets the value of the in8 property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getIn8() {
        return in8;
    }

    /**
     * Sets the value of the in8 property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setIn8(XMLGregorianCalendar value) {
        this.in8 = value;
    }

    /**
     * Gets the value of the in9 property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIn9() {
        return in9;
    }

    /**
     * Sets the value of the in9 property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIn9(Boolean value) {
        this.in9 = value;
    }

    /**
     * Gets the value of the in10 property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIn10() {
        return in10;
    }

    /**
     * Sets the value of the in10 property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIn10(Boolean value) {
        this.in10 = value;
    }

    /**
     * Gets the value of the in11 property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIn11() {
        return in11;
    }

    /**
     * Sets the value of the in11 property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIn11(Boolean value) {
        this.in11 = value;
    }

    /**
     * Gets the value of the in12 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIn12() {
        return in12;
    }

    /**
     * Sets the value of the in12 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIn12(String value) {
        this.in12 = value;
    }

}
