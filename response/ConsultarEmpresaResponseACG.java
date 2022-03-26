
package com.americacg.cargavirtual.gcsi.model.acg.response;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import com.americacg.cargavirtual.gcsi.service.EmpresaRapipago;


/**
 * <p>Java class for consultarEmpresaResponseACG complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="consultarEmpresaResponseACG">
 *   &lt;complexContent>
 *     &lt;extension base="{com.americacg.cargavirtual.gcsi.model.acg.response}gatewayMessage">
 *       &lt;sequence>
 *         &lt;element name="listaEmpresas" type="{http://gateway.gcsi.cargavirtual.americacg.com/}empresaRapipago" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "consultarEmpresaResponseACG", propOrder = {
    "listaEmpresas"
})
public class ConsultarEmpresaResponseACG
    extends GatewayMessage
{

    @XmlElement(nillable = true)
    protected List<EmpresaRapipago> listaEmpresas;

    /**
     * Gets the value of the listaEmpresas property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the listaEmpresas property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getListaEmpresas().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link EmpresaRapipago }
     * 
     * 
     */
    public List<EmpresaRapipago> getListaEmpresas() {
        if (listaEmpresas == null) {
            listaEmpresas = new ArrayList<EmpresaRapipago>();
        }
        return this.listaEmpresas;
    }

}
