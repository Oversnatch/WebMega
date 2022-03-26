
package com.americacg.cargavirtual.gcsi.model.acg.request;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for consultarEmpresaRequestACG complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="consultarEmpresaRequestACG">
 *   &lt;complexContent>
 *     &lt;extension base="{com.americacg.cargavirtual.gcsi.model.acg.request}gatewayMessage">
 *       &lt;sequence>
 *         &lt;element name="descEmp" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="codEmp" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="idTrxAnterior" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="tiposCobranza" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "consultarEmpresaRequestACG", propOrder = {
    "descEmp",
    "codEmp",
    "idTrxAnterior",
    "tiposCobranza"
})
public class ConsultarEmpresaRequestACG
    extends GatewayMessage
{

    protected String descEmp;
    protected Long codEmp;
    protected String idTrxAnterior;
    protected List<String> tiposCobranza;

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
     * Gets the value of the tiposCobranza property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the tiposCobranza property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getTiposCobranza().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getTiposCobranza() {
        if (tiposCobranza == null) {
            tiposCobranza = new ArrayList<String>();
        }
        return this.tiposCobranza;
    }

}
