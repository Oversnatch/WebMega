
package com.americacg.cargavirtual.gcsi.model.acg.request;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for consultarOperacionesRequest complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="consultarOperacionesRequest">
 *   &lt;complexContent>
 *     &lt;extension base="{com.americacg.cargavirtual.gcsi.model.acg.request}gatewayMessage">
 *       &lt;sequence>
 *         &lt;element name="filtro" type="{com.americacg.cargavirtual.gcsi.model.acg.request}filtroListadoOperacionesRequest"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "consultarOperacionesRequest", propOrder = {
    "filtro"
})
public class ConsultarOperacionesRequest
    extends GatewayMessage
{

    @XmlElement(required = true)
    protected FiltroListadoOperacionesRequest filtro;

    /**
     * Gets the value of the filtro property.
     * 
     * @return
     *     possible object is
     *     {@link FiltroListadoOperacionesRequest }
     *     
     */
    public FiltroListadoOperacionesRequest getFiltro() {
        return filtro;
    }

    /**
     * Sets the value of the filtro property.
     * 
     * @param value
     *     allowed object is
     *     {@link FiltroListadoOperacionesRequest }
     *     
     */
    public void setFiltro(FiltroListadoOperacionesRequest value) {
        this.filtro = value;
    }

}
