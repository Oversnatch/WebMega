
package com.americacg.cargavirtual.gcsi.model.acg.response;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;
import com.americacg.cargavirtual.gcsi.model.operador.rapipago.ConsultaTransaccionMensaje;


/**
 * <p>Java class for consultarTransaccionResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="consultarTransaccionResponse">
 *   &lt;complexContent>
 *     &lt;extension base="{com.americacg.cargavirtual.gcsi.model.acg.response}gatewayMessage">
 *       &lt;sequence>
 *         &lt;element name="codServicio" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="mensajeTrx" type="{com.americacg.cargavirtual.gcsi.model.operador.rapipago}consultaTransaccionMensaje" minOccurs="0"/>
 *         &lt;element name="codResul" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="descResul" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "consultarTransaccionResponse", propOrder = {
    "codServicio",
    "mensajeTrx",
    "codResul",
    "descResul"
})
public class ConsultarTransaccionResponse
    extends GatewayMessage
{

    protected Integer codServicio;
    protected ConsultaTransaccionMensaje mensajeTrx;
    protected Integer codResul;
    protected String descResul;

    /**
     * Gets the value of the codServicio property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getCodServicio() {
        return codServicio;
    }

    /**
     * Sets the value of the codServicio property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setCodServicio(Integer value) {
        this.codServicio = value;
    }

    /**
     * Gets the value of the mensajeTrx property.
     * 
     * @return
     *     possible object is
     *     {@link ConsultaTransaccionMensaje }
     *     
     */
    public ConsultaTransaccionMensaje getMensajeTrx() {
        return mensajeTrx;
    }

    /**
     * Sets the value of the mensajeTrx property.
     * 
     * @param value
     *     allowed object is
     *     {@link ConsultaTransaccionMensaje }
     *     
     */
    public void setMensajeTrx(ConsultaTransaccionMensaje value) {
        this.mensajeTrx = value;
    }

    /**
     * Gets the value of the codResul property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getCodResul() {
        return codResul;
    }

    /**
     * Sets the value of the codResul property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setCodResul(Integer value) {
        this.codResul = value;
    }

    /**
     * Gets the value of the descResul property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescResul() {
        return descResul;
    }

    /**
     * Sets the value of the descResul property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescResul(String value) {
        this.descResul = value;
    }

}
