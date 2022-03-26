
package com.americacg.cargavirtual.gcsi.model.acg.response;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import com.americacg.cargavirtual.gcsi.entidad.Lote;


/**
 * <p>Java class for consultarOperacionesResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="consultarOperacionesResponse">
 *   &lt;complexContent>
 *     &lt;extension base="{com.americacg.cargavirtual.gcsi.model.acg.response}gatewayMessage">
 *       &lt;sequence>
 *         &lt;element name="pageSize" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="offset" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="totalRegistros" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="lotes" type="{com.americacg.cargavirtual.gcsi.entidad}lote" maxOccurs="unbounded"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "consultarOperacionesResponse", propOrder = {
    "pageSize",
    "offset",
    "totalRegistros",
    "lotes"
})
public class ConsultarOperacionesResponse
    extends GatewayMessage
{

    protected int pageSize;
    protected int offset;
    protected long totalRegistros;
    @XmlElement(required = true)
    protected List<Lote> lotes;

    /**
     * Gets the value of the pageSize property.
     * 
     */
    public int getPageSize() {
        return pageSize;
    }

    /**
     * Sets the value of the pageSize property.
     * 
     */
    public void setPageSize(int value) {
        this.pageSize = value;
    }

    /**
     * Gets the value of the offset property.
     * 
     */
    public int getOffset() {
        return offset;
    }

    /**
     * Sets the value of the offset property.
     * 
     */
    public void setOffset(int value) {
        this.offset = value;
    }

    /**
     * Gets the value of the totalRegistros property.
     * 
     */
    public long getTotalRegistros() {
        return totalRegistros;
    }

    /**
     * Sets the value of the totalRegistros property.
     * 
     */
    public void setTotalRegistros(long value) {
        this.totalRegistros = value;
    }

    /**
     * Gets the value of the lotes property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the lotes property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getLotes().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Lote }
     * 
     * 
     */
    public List<Lote> getLotes() {
        if (lotes == null) {
            lotes = new ArrayList<Lote>();
        }
        return this.lotes;
    }

}
