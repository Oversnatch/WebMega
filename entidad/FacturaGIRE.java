
package com.americacg.cargavirtual.gcsi.entidad;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import com.americacg.cargavirtual.gcsi.service.FormasPago;


/**
 * <p>Java class for facturaGIRE complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="facturaGIRE">
 *   &lt;complexContent>
 *     &lt;extension base="{com.americacg.cargavirtual.gcsi.entidad}factura">
 *       &lt;sequence>
 *         &lt;element name="anula" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="tipoCobranza" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="formaDePago" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="codTI" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="descTI" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="idMod" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="formasDePagoHabilitadas" type="{http://gateway.gcsi.cargavirtual.americacg.com/}formasPago" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "facturaGIRE", propOrder = {
    "anula",
    "tipoCobranza",
    "formaDePago",
    "codTI",
    "descTI",
    "idMod",
    "formasDePagoHabilitadas"
})
public class FacturaGIRE
    extends Factura
{

    @XmlElement(required = true)
    protected String anula;
    @XmlElement(required = true)
    protected String tipoCobranza;
    @XmlElement(required = true)
    protected String formaDePago;
    @XmlElement(required = true)
    protected String codTI;
    @XmlElement(required = true)
    protected String descTI;
    @XmlElement(required = true)
    protected String idMod;
    protected List<FormasPago> formasDePagoHabilitadas;

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
     * Gets the value of the tipoCobranza property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTipoCobranza() {
        return tipoCobranza;
    }

    /**
     * Sets the value of the tipoCobranza property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTipoCobranza(String value) {
        this.tipoCobranza = value;
    }

    /**
     * Gets the value of the formaDePago property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFormaDePago() {
        return formaDePago;
    }

    /**
     * Sets the value of the formaDePago property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFormaDePago(String value) {
        this.formaDePago = value;
    }

    /**
     * Gets the value of the codTI property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodTI() {
        return codTI;
    }

    /**
     * Sets the value of the codTI property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodTI(String value) {
        this.codTI = value;
    }

    /**
     * Gets the value of the descTI property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescTI() {
        return descTI;
    }

    /**
     * Sets the value of the descTI property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescTI(String value) {
        this.descTI = value;
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
     * Gets the value of the formasDePagoHabilitadas property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the formasDePagoHabilitadas property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getFormasDePagoHabilitadas().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link FormasPago }
     * 
     * 
     */
    public List<FormasPago> getFormasDePagoHabilitadas() {
        if (formasDePagoHabilitadas == null) {
            formasDePagoHabilitadas = new ArrayList<FormasPago>();
        }
        return this.formasDePagoHabilitadas;
    }

}
