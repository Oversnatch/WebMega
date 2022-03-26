
package com.americacg.cargavirtual.gcsi.model.acg.request;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for descargaReporteACG complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="descargaReporteACG">
 *   &lt;complexContent>
 *     &lt;extension base="{com.americacg.cargavirtual.gcsi.model.acg.request}gatewayMessage">
 *       &lt;sequence>
 *         &lt;element name="filtroReporte" type="{com.americacg.cargavirtual.gcsi.model.acg.request}filtroListadoRequest"/>
 *         &lt;element name="separadorCampo" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="separadorDecimales" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "descargaReporteACG", propOrder = {
    "filtroReporte",
    "separadorCampo",
    "separadorDecimales"
})
public class DescargaReporteACG
    extends GatewayMessage
{

    @XmlElement(required = true)
    protected FiltroListadoRequest filtroReporte;
    @XmlElement(required = true)
    protected String separadorCampo;
    @XmlElement(required = true)
    protected String separadorDecimales;

    /**
     * Gets the value of the filtroReporte property.
     * 
     * @return
     *     possible object is
     *     {@link FiltroListadoRequest }
     *     
     */
    public FiltroListadoRequest getFiltroReporte() {
        return filtroReporte;
    }

    /**
     * Sets the value of the filtroReporte property.
     * 
     * @param value
     *     allowed object is
     *     {@link FiltroListadoRequest }
     *     
     */
    public void setFiltroReporte(FiltroListadoRequest value) {
        this.filtroReporte = value;
    }

    /**
     * Gets the value of the separadorCampo property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSeparadorCampo() {
        return separadorCampo;
    }

    /**
     * Sets the value of the separadorCampo property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSeparadorCampo(String value) {
        this.separadorCampo = value;
    }

    /**
     * Gets the value of the separadorDecimales property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSeparadorDecimales() {
        return separadorDecimales;
    }

    /**
     * Sets the value of the separadorDecimales property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSeparadorDecimales(String value) {
        this.separadorDecimales = value;
    }

}
