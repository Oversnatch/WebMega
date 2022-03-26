
package com.americacg.cargavirtual.gcsi.model.acg.request;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;
import com.americacg.cargavirtual.gcsi.entidad.Lote;


/**
 * <p>Java class for confirmarRequestACG complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="confirmarRequestACG">
 *   &lt;complexContent>
 *     &lt;extension base="{com.americacg.cargavirtual.gcsi.model.acg.request}gatewayMessage">
 *       &lt;sequence>
 *         &lt;element name="lote" type="{com.americacg.cargavirtual.gcsi.entidad}lote" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "confirmarRequestACG", propOrder = {
    "lote"
})
public class ConfirmarRequestACG
    extends GatewayMessage
{

    protected Lote lote;

    /**
     * Gets the value of the lote property.
     * 
     * @return
     *     possible object is
     *     {@link Lote }
     *     
     */
    public Lote getLote() {
        return lote;
    }

    /**
     * Sets the value of the lote property.
     * 
     * @param value
     *     allowed object is
     *     {@link Lote }
     *     
     */
    public void setLote(Lote value) {
        this.lote = value;
    }

}
