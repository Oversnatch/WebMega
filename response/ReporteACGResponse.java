
package com.americacg.cargavirtual.gcsi.model.acg.response;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import com.americacg.cargavirtual.gcsi.entidad.Lote;


/**
 * <p>Java class for reporteACGResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="reporteACGResponse">
 *   &lt;complexContent>
 *     &lt;extension base="{com.americacg.cargavirtual.gcsi.model.acg.response}gatewayMessage">
 *       &lt;sequence>
 *         &lt;element name="listaLotes" type="{com.americacg.cargavirtual.gcsi.entidad}lote" maxOccurs="unbounded"/>
 *         &lt;element name="cantidadRegistros" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="importeTotalAbonado" type="{http://www.w3.org/2001/XMLSchema}float"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "reporteACGResponse", propOrder = {
    "listaLotes",
    "cantidadRegistros",
    "importeTotalAbonado"
})
public class ReporteACGResponse
    extends GatewayMessage
{

    @XmlElement(required = true)
    protected List<Lote> listaLotes;
    protected int cantidadRegistros;
    protected float importeTotalAbonado;

    /**
     * Gets the value of the listaLotes property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the listaLotes property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getListaLotes().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Lote }
     * 
     * 
     */
    public List<Lote> getListaLotes() {
        if (listaLotes == null) {
            listaLotes = new ArrayList<Lote>();
        }
        return this.listaLotes;
    }

    /**
     * Gets the value of the cantidadRegistros property.
     * 
     */
    public int getCantidadRegistros() {
        return cantidadRegistros;
    }

    /**
     * Sets the value of the cantidadRegistros property.
     * 
     */
    public void setCantidadRegistros(int value) {
        this.cantidadRegistros = value;
    }

    /**
     * Gets the value of the importeTotalAbonado property.
     * 
     */
    public float getImporteTotalAbonado() {
        return importeTotalAbonado;
    }

    /**
     * Sets the value of the importeTotalAbonado property.
     * 
     */
    public void setImporteTotalAbonado(float value) {
        this.importeTotalAbonado = value;
    }

}
