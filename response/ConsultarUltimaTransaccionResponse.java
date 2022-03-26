
package com.americacg.cargavirtual.gcsi.model.acg.response;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for consultarUltimaTransaccionResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="consultarUltimaTransaccionResponse">
 *   &lt;complexContent>
 *     &lt;extension base="{com.americacg.cargavirtual.gcsi.model.acg.response}gatewayMessage">
 *       &lt;sequence>
 *         &lt;element name="idUltTrxPendiente" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="idUltTrxConfirmada" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="existeTrxEnProceso" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
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
@XmlType(name = "consultarUltimaTransaccionResponse", propOrder = {
    "idUltTrxPendiente",
    "idUltTrxConfirmada",
    "existeTrxEnProceso",
    "codResul",
    "descResul"
})
public class ConsultarUltimaTransaccionResponse
    extends GatewayMessage
{

    protected String idUltTrxPendiente;
    protected String idUltTrxConfirmada;
    protected Boolean existeTrxEnProceso;
    protected Integer codResul;
    protected String descResul;

    /**
     * Gets the value of the idUltTrxPendiente property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdUltTrxPendiente() {
        return idUltTrxPendiente;
    }

    /**
     * Sets the value of the idUltTrxPendiente property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdUltTrxPendiente(String value) {
        this.idUltTrxPendiente = value;
    }

    /**
     * Gets the value of the idUltTrxConfirmada property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdUltTrxConfirmada() {
        return idUltTrxConfirmada;
    }

    /**
     * Sets the value of the idUltTrxConfirmada property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdUltTrxConfirmada(String value) {
        this.idUltTrxConfirmada = value;
    }

    /**
     * Gets the value of the existeTrxEnProceso property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isExisteTrxEnProceso() {
        return existeTrxEnProceso;
    }

    /**
     * Sets the value of the existeTrxEnProceso property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setExisteTrxEnProceso(Boolean value) {
        this.existeTrxEnProceso = value;
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
