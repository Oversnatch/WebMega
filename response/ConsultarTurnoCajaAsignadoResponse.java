
package com.americacg.cargavirtual.gcsi.model.acg.response;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;
import com.americacg.cargavirtual.gcsi.entidad.TurnoCaja;


/**
 * <p>Java class for consultarTurnoCajaAsignadoResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="consultarTurnoCajaAsignadoResponse">
 *   &lt;complexContent>
 *     &lt;extension base="{com.americacg.cargavirtual.gcsi.model.acg.response}gatewayMessage">
 *       &lt;sequence>
 *         &lt;element name="turnoCaja" type="{com.americacg.cargavirtual.gcsi.entidad}turnoCaja" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "consultarTurnoCajaAsignadoResponse", propOrder = {
    "turnoCaja"
})
public class ConsultarTurnoCajaAsignadoResponse
    extends GatewayMessage
{

    protected TurnoCaja turnoCaja;

    /**
     * Gets the value of the turnoCaja property.
     * 
     * @return
     *     possible object is
     *     {@link TurnoCaja }
     *     
     */
    public TurnoCaja getTurnoCaja() {
        return turnoCaja;
    }

    /**
     * Sets the value of the turnoCaja property.
     * 
     * @param value
     *     allowed object is
     *     {@link TurnoCaja }
     *     
     */
    public void setTurnoCaja(TurnoCaja value) {
        this.turnoCaja = value;
    }

}
