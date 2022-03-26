
package com.americacg.cargavirtual.gcsi.model.acg.request;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;
import com.americacg.cargavirtual.gcsi.entidad.Terminal;


/**
 * <p>Java class for cerrarTurnoRequest complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="cerrarTurnoRequest">
 *   &lt;complexContent>
 *     &lt;extension base="{com.americacg.cargavirtual.gcsi.model.acg.request}gatewayMessage">
 *       &lt;sequence>
 *         &lt;element name="fechaCierreCaja" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="terminal" type="{com.americacg.cargavirtual.gcsi.entidad}terminal" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "cerrarTurnoRequest", propOrder = {
    "fechaCierreCaja",
    "terminal"
})
public class CerrarTurnoRequest
    extends GatewayMessage
{

    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar fechaCierreCaja;
    protected Terminal terminal;

    /**
     * Gets the value of the fechaCierreCaja property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getFechaCierreCaja() {
        return fechaCierreCaja;
    }

    /**
     * Sets the value of the fechaCierreCaja property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setFechaCierreCaja(XMLGregorianCalendar value) {
        this.fechaCierreCaja = value;
    }

    /**
     * Gets the value of the terminal property.
     * 
     * @return
     *     possible object is
     *     {@link Terminal }
     *     
     */
    public Terminal getTerminal() {
        return terminal;
    }

    /**
     * Sets the value of the terminal property.
     * 
     * @param value
     *     allowed object is
     *     {@link Terminal }
     *     
     */
    public void setTerminal(Terminal value) {
        this.terminal = value;
    }

}
