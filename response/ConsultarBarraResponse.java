
package com.americacg.cargavirtual.gcsi.model.acg.response;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import com.americacg.cargavirtual.gcsi.entidad.Factura;


/**
 * <p>Java class for consultarBarraResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="consultarBarraResponse">
 *   &lt;complexContent>
 *     &lt;extension base="{com.americacg.cargavirtual.gcsi.model.acg.response}gatewayMessage">
 *       &lt;sequence>
 *         &lt;element name="listaFactura" type="{com.americacg.cargavirtual.gcsi.entidad}factura" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "consultarBarraResponse", propOrder = {
    "listaFactura"
})
public class ConsultarBarraResponse
    extends GatewayMessage
{

    @XmlElement(nillable = true)
    protected List<Factura> listaFactura;

    /**
     * Gets the value of the listaFactura property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the listaFactura property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getListaFactura().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Factura }
     * 
     * 
     */
    public List<Factura> getListaFactura() {
        if (listaFactura == null) {
            listaFactura = new ArrayList<Factura>();
        }
        return this.listaFactura;
    }

}
