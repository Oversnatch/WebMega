
package com.americacg.cargavirtual.gestion.service;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


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
 *         &lt;element name="in0" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="in1" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="in2" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="in3" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 *         &lt;element name="in4" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 *         &lt;element name="in5" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="in6" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="in7" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="in8" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="in9" type="{http://www.w3.org/2001/XMLSchema}int"/>
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
    "in9"
})
@XmlRootElement(name = "wu_informeRepartoSaldo")
public class WuInformeRepartoSaldo {

    @XmlElement(required = true, type = Long.class, nillable = true)
    protected Long in0;
    @XmlElement(required = true, type = Long.class, nillable = true)
    protected Long in1;
    @XmlElement(required = true, type = Integer.class, nillable = true)
    protected Integer in2;
    @XmlElement(required = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar in3;
    @XmlElement(required = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar in4;
    @XmlElement(required = true, type = Long.class, nillable = true)
    protected Long in5;
    @XmlElement(required = true, type = Long.class, nillable = true)
    protected Long in6;
    @XmlElement(required = true, type = Long.class, nillable = true)
    protected Long in7;
    @XmlElement(required = true, type = Integer.class, nillable = true)
    protected Integer in8;
    @XmlElement(required = true, type = Integer.class, nillable = true)
    protected Integer in9;

    /**
     * Gets the value of the in0 property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getIn0() {
        return in0;
    }

    /**
     * Sets the value of the in0 property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setIn0(Long value) {
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
     *     {@link Integer }
     *     
     */
    public Integer getIn2() {
        return in2;
    }

    /**
     * Sets the value of the in2 property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setIn2(Integer value) {
        this.in2 = value;
    }

    /**
     * Gets the value of the in3 property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getIn3() {
        return in3;
    }

    /**
     * Sets the value of the in3 property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setIn3(XMLGregorianCalendar value) {
        this.in3 = value;
    }

    /**
     * Gets the value of the in4 property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getIn4() {
        return in4;
    }

    /**
     * Sets the value of the in4 property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setIn4(XMLGregorianCalendar value) {
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
     *     {@link Long }
     *     
     */
    public Long getIn6() {
        return in6;
    }

    /**
     * Sets the value of the in6 property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setIn6(Long value) {
        this.in6 = value;
    }

    /**
     * Gets the value of the in7 property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getIn7() {
        return in7;
    }

    /**
     * Sets the value of the in7 property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setIn7(Long value) {
        this.in7 = value;
    }

    /**
     * Gets the value of the in8 property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getIn8() {
        return in8;
    }

    /**
     * Sets the value of the in8 property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setIn8(Integer value) {
        this.in8 = value;
    }

    /**
     * Gets the value of the in9 property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getIn9() {
        return in9;
    }

    /**
     * Sets the value of the in9 property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setIn9(Integer value) {
        this.in9 = value;
    }

}
