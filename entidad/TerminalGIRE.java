
package com.americacg.cargavirtual.gcsi.entidad;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for terminalGIRE complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="terminalGIRE">
 *   &lt;complexContent>
 *     &lt;extension base="{com.americacg.cargavirtual.gcsi.entidad}terminal">
 *       &lt;sequence>
 *         &lt;element name="codPuesto" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "terminalGIRE", propOrder = {
    "codPuesto"
})
public class TerminalGIRE
    extends Terminal
{

    protected String codPuesto;

    /**
     * Gets the value of the codPuesto property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodPuesto() {
        return codPuesto;
    }

    /**
     * Sets the value of the codPuesto property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodPuesto(String value) {
        this.codPuesto = value;
    }

}
