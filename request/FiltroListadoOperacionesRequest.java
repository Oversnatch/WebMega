
package com.americacg.cargavirtual.gcsi.model.acg.request;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import com.americacg.cargavirtual.gcsi.entidad.Lote;
import com.americacg.cargavirtual.gcsi.service.EstadoGCSI;


/**
 * <p>Java class for filtroListadoOperacionesRequest complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="filtroListadoOperacionesRequest">
 *   &lt;complexContent>
 *     &lt;extension base="{com.americacg.cargavirtual.gcsi.model.acg.request}filtroListadoRequest">
 *       &lt;sequence>
 *         &lt;element name="lote" type="{com.americacg.cargavirtual.gcsi.entidad}lote" minOccurs="0"/>
 *         &lt;element name="listaIdsCliente" type="{http://www.w3.org/2001/XMLSchema}long" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="listaCodMnemonicoEstado" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="listaResultado" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="estadoOperacionGCSI" type="{http://gateway.gcsi.cargavirtual.americacg.com/}estadoGCSI" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "filtroListadoOperacionesRequest", propOrder = {
    "lote",
    "listaIdsCliente",
    "listaCodMnemonicoEstado",
    "listaResultado",
    "estadoOperacionGCSI"
})
public class FiltroListadoOperacionesRequest
    extends FiltroListadoRequest
{

    protected Lote lote;
    @XmlElement(type = Long.class)
    protected List<Long> listaIdsCliente;
    protected List<String> listaCodMnemonicoEstado;
    protected List<String> listaResultado;
    protected EstadoGCSI estadoOperacionGCSI;

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

    /**
     * Gets the value of the listaIdsCliente property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the listaIdsCliente property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getListaIdsCliente().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Long }
     * 
     * 
     */
    public List<Long> getListaIdsCliente() {
        if (listaIdsCliente == null) {
            listaIdsCliente = new ArrayList<Long>();
        }
        return this.listaIdsCliente;
    }

    /**
     * Gets the value of the listaCodMnemonicoEstado property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the listaCodMnemonicoEstado property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getListaCodMnemonicoEstado().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getListaCodMnemonicoEstado() {
        if (listaCodMnemonicoEstado == null) {
            listaCodMnemonicoEstado = new ArrayList<String>();
        }
        return this.listaCodMnemonicoEstado;
    }

    /**
     * Gets the value of the listaResultado property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the listaResultado property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getListaResultado().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getListaResultado() {
        if (listaResultado == null) {
            listaResultado = new ArrayList<String>();
        }
        return this.listaResultado;
    }

    /**
     * Gets the value of the estadoOperacionGCSI property.
     * 
     * @return
     *     possible object is
     *     {@link EstadoGCSI }
     *     
     */
    public EstadoGCSI getEstadoOperacionGCSI() {
        return estadoOperacionGCSI;
    }

    /**
     * Sets the value of the estadoOperacionGCSI property.
     * 
     * @param value
     *     allowed object is
     *     {@link EstadoGCSI }
     *     
     */
    public void setEstadoOperacionGCSI(EstadoGCSI value) {
        this.estadoOperacionGCSI = value;
    }

}
