
package com.americacg.cargavirtual.gcsi.entidad;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;
import com.americacg.cargavirtual.gcsi.service.BaseModel;
import com.americacg.cargavirtual.gcsi.service.PlatCanal;


/**
 * <p>Java class for lote complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="lote">
 *   &lt;complexContent>
 *     &lt;extension base="{http://gateway.gcsi.cargavirtual.americacg.com/}baseModel">
 *       &lt;sequence>
 *         &lt;element name="id" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="operador" type="{com.americacg.cargavirtual.gcsi.entidad}operador" minOccurs="0"/>
 *         &lt;element name="mayorista" type="{com.americacg.cargavirtual.gcsi.entidad}mayorista" minOccurs="0"/>
 *         &lt;element name="cliente" type="{com.americacg.cargavirtual.gcsi.entidad}cliente" minOccurs="0"/>
 *         &lt;element name="usuario" type="{com.americacg.cargavirtual.gcsi.entidad}usuario" minOccurs="0"/>
 *         &lt;element name="tiempoRespWSACGms" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="facturas" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;choice maxOccurs="unbounded" minOccurs="0">
 *                     &lt;element name="facturaGIRE" type="{com.americacg.cargavirtual.gcsi.entidad}facturaGIRE"/>
 *                     &lt;element name="facturaPNET" type="{com.americacg.cargavirtual.gcsi.entidad}facturaPNET"/>
 *                     &lt;element name="facturaWU" type="{com.americacg.cargavirtual.gcsi.entidad}facturaWU"/>
 *                   &lt;/choice>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="idAutogeneradoFrontEnd" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="idLoteExterno" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="turnoCaja" type="{com.americacg.cargavirtual.gcsi.entidad}turnoCaja" minOccurs="0"/>
 *         &lt;element name="canal" type="{http://gateway.gcsi.cargavirtual.americacg.com/}PlatCanal" minOccurs="0"/>
 *         &lt;element name="fechaAlta" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="usuarioUltimaModificacion" type="{com.americacg.cargavirtual.gcsi.entidad}usuario" minOccurs="0"/>
 *         &lt;element name="fechaUltMod" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "lote", propOrder = {
    "id",
    "operador",
    "mayorista",
    "cliente",
    "usuario",
    "tiempoRespWSACGms",
    "facturas",
    "idAutogeneradoFrontEnd",
    "idLoteExterno",
    "turnoCaja",
    "canal",
    "fechaAlta",
    "usuarioUltimaModificacion",
    "fechaUltMod"
})
public class Lote
    extends BaseModel
{

    protected Long id;
    @XmlElement(nillable = true)
    protected Operador operador;
    @XmlElement(nillable = true)
    protected Mayorista mayorista;
    @XmlElement(nillable = true)
    protected Cliente cliente;
    @XmlElement(nillable = true)
    protected Usuario usuario;
    protected Long tiempoRespWSACGms;
    protected Lote.Facturas facturas;
    protected String idAutogeneradoFrontEnd;
    protected String idLoteExterno;
    protected TurnoCaja turnoCaja;
    protected PlatCanal canal;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar fechaAlta;
    protected Usuario usuarioUltimaModificacion;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar fechaUltMod;

    /**
     * Gets the value of the id property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the value of the id property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setId(Long value) {
        this.id = value;
    }

    /**
     * Gets the value of the operador property.
     * 
     * @return
     *     possible object is
     *     {@link Operador }
     *     
     */
    public Operador getOperador() {
        return operador;
    }

    /**
     * Sets the value of the operador property.
     * 
     * @param value
     *     allowed object is
     *     {@link Operador }
     *     
     */
    public void setOperador(Operador value) {
        this.operador = value;
    }

    /**
     * Gets the value of the mayorista property.
     * 
     * @return
     *     possible object is
     *     {@link Mayorista }
     *     
     */
    public Mayorista getMayorista() {
        return mayorista;
    }

    /**
     * Sets the value of the mayorista property.
     * 
     * @param value
     *     allowed object is
     *     {@link Mayorista }
     *     
     */
    public void setMayorista(Mayorista value) {
        this.mayorista = value;
    }

    /**
     * Gets the value of the cliente property.
     * 
     * @return
     *     possible object is
     *     {@link Cliente }
     *     
     */
    public Cliente getCliente() {
        return cliente;
    }

    /**
     * Sets the value of the cliente property.
     * 
     * @param value
     *     allowed object is
     *     {@link Cliente }
     *     
     */
    public void setCliente(Cliente value) {
        this.cliente = value;
    }

    /**
     * Gets the value of the usuario property.
     * 
     * @return
     *     possible object is
     *     {@link Usuario }
     *     
     */
    public Usuario getUsuario() {
        return usuario;
    }

    /**
     * Sets the value of the usuario property.
     * 
     * @param value
     *     allowed object is
     *     {@link Usuario }
     *     
     */
    public void setUsuario(Usuario value) {
        this.usuario = value;
    }

    /**
     * Gets the value of the tiempoRespWSACGms property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getTiempoRespWSACGms() {
        return tiempoRespWSACGms;
    }

    /**
     * Sets the value of the tiempoRespWSACGms property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setTiempoRespWSACGms(Long value) {
        this.tiempoRespWSACGms = value;
    }

    /**
     * Gets the value of the facturas property.
     * 
     * @return
     *     possible object is
     *     {@link Lote.Facturas }
     *     
     */
    public Lote.Facturas getFacturas() {
        return facturas;
    }

    /**
     * Sets the value of the facturas property.
     * 
     * @param value
     *     allowed object is
     *     {@link Lote.Facturas }
     *     
     */
    public void setFacturas(Lote.Facturas value) {
        this.facturas = value;
    }

    /**
     * Gets the value of the idAutogeneradoFrontEnd property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdAutogeneradoFrontEnd() {
        return idAutogeneradoFrontEnd;
    }

    /**
     * Sets the value of the idAutogeneradoFrontEnd property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdAutogeneradoFrontEnd(String value) {
        this.idAutogeneradoFrontEnd = value;
    }

    /**
     * Gets the value of the idLoteExterno property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdLoteExterno() {
        return idLoteExterno;
    }

    /**
     * Sets the value of the idLoteExterno property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdLoteExterno(String value) {
        this.idLoteExterno = value;
    }

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

    /**
     * Gets the value of the canal property.
     * 
     * @return
     *     possible object is
     *     {@link PlatCanal }
     *     
     */
    public PlatCanal getCanal() {
        return canal;
    }

    /**
     * Sets the value of the canal property.
     * 
     * @param value
     *     allowed object is
     *     {@link PlatCanal }
     *     
     */
    public void setCanal(PlatCanal value) {
        this.canal = value;
    }

    /**
     * Gets the value of the fechaAlta property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getFechaAlta() {
        return fechaAlta;
    }

    /**
     * Sets the value of the fechaAlta property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setFechaAlta(XMLGregorianCalendar value) {
        this.fechaAlta = value;
    }

    /**
     * Gets the value of the usuarioUltimaModificacion property.
     * 
     * @return
     *     possible object is
     *     {@link Usuario }
     *     
     */
    public Usuario getUsuarioUltimaModificacion() {
        return usuarioUltimaModificacion;
    }

    /**
     * Sets the value of the usuarioUltimaModificacion property.
     * 
     * @param value
     *     allowed object is
     *     {@link Usuario }
     *     
     */
    public void setUsuarioUltimaModificacion(Usuario value) {
        this.usuarioUltimaModificacion = value;
    }

    /**
     * Gets the value of the fechaUltMod property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getFechaUltMod() {
        return fechaUltMod;
    }

    /**
     * Sets the value of the fechaUltMod property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setFechaUltMod(XMLGregorianCalendar value) {
        this.fechaUltMod = value;
    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;choice maxOccurs="unbounded" minOccurs="0">
     *           &lt;element name="facturaGIRE" type="{com.americacg.cargavirtual.gcsi.entidad}facturaGIRE"/>
     *           &lt;element name="facturaPNET" type="{com.americacg.cargavirtual.gcsi.entidad}facturaPNET"/>
     *           &lt;element name="facturaWU" type="{com.americacg.cargavirtual.gcsi.entidad}facturaWU"/>
     *         &lt;/choice>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "facturaGIREOrFacturaPNETOrFacturaWU"
    })
    public static class Facturas {

        @XmlElements({
            @XmlElement(name = "facturaWU", type = FacturaWU.class),
            @XmlElement(name = "facturaGIRE", type = FacturaGIRE.class),
            @XmlElement(name = "facturaPNET", type = FacturaPNET.class)
        })
        protected List<Factura> facturaGIREOrFacturaPNETOrFacturaWU;

        /**
         * Gets the value of the facturaGIREOrFacturaPNETOrFacturaWU property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the facturaGIREOrFacturaPNETOrFacturaWU property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getFacturaGIREOrFacturaPNETOrFacturaWU().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link FacturaWU }
         * {@link FacturaGIRE }
         * {@link FacturaPNET }
         * 
         * 
         */
        public List<Factura> getFacturaGIREOrFacturaPNETOrFacturaWU() {
            if (facturaGIREOrFacturaPNETOrFacturaWU == null) {
                facturaGIREOrFacturaPNETOrFacturaWU = new ArrayList<Factura>();
            }
            return this.facturaGIREOrFacturaPNETOrFacturaWU;
        }

    }

}
