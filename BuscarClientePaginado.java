
package com.americacg.cargavirtual.gestion.service;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


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
 *         &lt;element name="in3" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="in4" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="in5" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="in6" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="in7" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="in8" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="in9" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="in10" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="in11" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="in12" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="in13" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="in14" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="in15" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="in16" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="in17" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="in18" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="in19" type="{http://www.w3.org/2001/XMLSchema}int"/>
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
    "in12",
    "in13",
    "in14",
    "in15",
    "in16",
    "in17",
    "in18",
    "in19"
})
@XmlRootElement(name = "buscarClientePaginado")
public class BuscarClientePaginado {

    @XmlElement(required = true, type = Long.class, nillable = true)
    protected Long in0;
    @XmlElement(required = true, type = Long.class, nillable = true)
    protected Long in1;
    @XmlElement(required = true, type = Integer.class, nillable = true)
    protected Integer in2;
    @XmlElement(required = true, type = Long.class, nillable = true)
    protected Long in3;
    @XmlElement(required = true, nillable = true)
    protected String in4;
    @XmlElement(required = true, nillable = true)
    protected String in5;
    @XmlElement(required = true, type = Long.class, nillable = true)
    protected Long in6;
    @XmlElement(required = true, nillable = true)
    protected String in7;
    @XmlElement(required = true, type = Long.class, nillable = true)
    protected Long in8;
    @XmlElement(required = true, nillable = true)
    protected String in9;
    @XmlElement(required = true, nillable = true)
    protected String in10;
    @XmlElement(required = true, nillable = true)
    protected String in11;
    @XmlElement(required = true, type = Long.class, nillable = true)
    protected Long in12;
    @XmlElement(required = true, type = Long.class, nillable = true)
    protected Long in13;
    @XmlElement(required = true, type = Long.class, nillable = true)
    protected Long in14;
    @XmlElement(required = true, nillable = true)
    protected String in15;
    @XmlElement(required = true, nillable = true)
    protected String in16;
    @XmlElement(required = true, nillable = true)
    protected String in17;
    @XmlElement(required = true, type = Integer.class, nillable = true)
    protected Integer in18;
    @XmlElement(required = true, type = Integer.class, nillable = true)
    protected Integer in19;

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
     *     {@link Long }
     *     
     */
    public Long getIn3() {
        return in3;
    }

    /**
     * Sets the value of the in3 property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setIn3(Long value) {
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
     *     {@link String }
     *     
     */
    public String getIn5() {
        return in5;
    }

    /**
     * Sets the value of the in5 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIn5(String value) {
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
     *     {@link Long }
     *     
     */
    public Long getIn8() {
        return in8;
    }

    /**
     * Sets the value of the in8 property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setIn8(Long value) {
        this.in8 = value;
    }

    /**
     * Gets the value of the in9 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIn9() {
        return in9;
    }

    /**
     * Sets the value of the in9 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIn9(String value) {
        this.in9 = value;
    }

    /**
     * Gets the value of the in10 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIn10() {
        return in10;
    }

    /**
     * Sets the value of the in10 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIn10(String value) {
        this.in10 = value;
    }

    /**
     * Gets the value of the in11 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIn11() {
        return in11;
    }

    /**
     * Sets the value of the in11 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIn11(String value) {
        this.in11 = value;
    }

    /**
     * Gets the value of the in12 property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getIn12() {
        return in12;
    }

    /**
     * Sets the value of the in12 property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setIn12(Long value) {
        this.in12 = value;
    }

    /**
     * Gets the value of the in13 property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getIn13() {
        return in13;
    }

    /**
     * Sets the value of the in13 property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setIn13(Long value) {
        this.in13 = value;
    }

    /**
     * Gets the value of the in14 property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getIn14() {
        return in14;
    }

    /**
     * Sets the value of the in14 property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setIn14(Long value) {
        this.in14 = value;
    }

    /**
     * Gets the value of the in15 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIn15() {
        return in15;
    }

    /**
     * Sets the value of the in15 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIn15(String value) {
        this.in15 = value;
    }

    /**
     * Gets the value of the in16 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIn16() {
        return in16;
    }

    /**
     * Sets the value of the in16 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIn16(String value) {
        this.in16 = value;
    }

    /**
     * Gets the value of the in17 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIn17() {
        return in17;
    }

    /**
     * Sets the value of the in17 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIn17(String value) {
        this.in17 = value;
    }

    /**
     * Gets the value of the in18 property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getIn18() {
        return in18;
    }

    /**
     * Sets the value of the in18 property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setIn18(Integer value) {
        this.in18 = value;
    }

    /**
     * Gets the value of the in19 property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getIn19() {
        return in19;
    }

    /**
     * Sets the value of the in19 property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setIn19(Integer value) {
        this.in19 = value;
    }

}
