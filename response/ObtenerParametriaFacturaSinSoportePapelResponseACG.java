
package com.americacg.cargavirtual.gcsi.model.acg.response;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for obtenerParametriaFacturaSinSoportePapelResponseACG complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="obtenerParametriaFacturaSinSoportePapelResponseACG">
 *   &lt;complexContent>
 *     &lt;extension base="{com.americacg.cargavirtual.gcsi.model.acg.response}gatewayMessage">
 *       &lt;sequence>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "obtenerParametriaFacturaSinSoportePapelResponseACG")
@XmlSeeAlso({
    ObtenerParamFacSinSopPapelGIREResponse.class
})
public abstract class ObtenerParametriaFacturaSinSoportePapelResponseACG
    extends GatewayMessage
{


}
