
package com.americacg.cargavirtual.gcsi.entidad;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;
import com.americacg.cargavirtual.gcsi.service.BaseModel;


/**
 * <p>Java class for factura complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="factura">
 *   &lt;complexContent>
 *     &lt;extension base="{http://gateway.gcsi.cargavirtual.americacg.com/}baseModel">
 *       &lt;sequence>
 *         &lt;element name="id" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="codDiscriminador" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="operador" type="{com.americacg.cargavirtual.gcsi.entidad}operador"/>
 *         &lt;element name="mayorista" type="{com.americacg.cargavirtual.gcsi.entidad}mayorista"/>
 *         &lt;element name="cliente" type="{com.americacg.cargavirtual.gcsi.entidad}cliente"/>
 *         &lt;element name="usuario" type="{com.americacg.cargavirtual.gcsi.entidad}usuario"/>
 *         &lt;element name="moneda" type="{com.americacg.cargavirtual.gcsi.entidad}platmoneda"/>
 *         &lt;element name="estado" type="{com.americacg.cargavirtual.gcsi.entidad}estado"/>
 *         &lt;element name="comprobantes" type="{com.americacg.cargavirtual.gcsi.entidad}comprobante" maxOccurs="unbounded"/>
 *         &lt;element name="datosAdicionalesFactura" type="{com.americacg.cargavirtual.gcsi.entidad}datoadicionalfactura" maxOccurs="unbounded"/>
 *         &lt;element name="codEmp" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="descEmpresa" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="resultado" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="resultadoMsg" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="codBarra1" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="codBarra2" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="idTrxACG" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="idTrxOperador" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="impMinNeg" type="{http://www.w3.org/2001/XMLSchema}float" minOccurs="0"/>
 *         &lt;element name="impMaxNeg" type="{http://www.w3.org/2001/XMLSchema}float" minOccurs="0"/>
 *         &lt;element name="impMinPos" type="{http://www.w3.org/2001/XMLSchema}float" minOccurs="0"/>
 *         &lt;element name="impMaxPos" type="{http://www.w3.org/2001/XMLSchema}float" minOccurs="0"/>
 *         &lt;element name="importeTotal" type="{http://www.w3.org/2001/XMLSchema}float" minOccurs="0"/>
 *         &lt;element name="importeAbonado" type="{http://www.w3.org/2001/XMLSchema}float" minOccurs="0"/>
 *         &lt;element name="obs" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="cantReversas" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="fechaAlta" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="tiempoRespOperadorMs" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="mensajeError" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="cantidadIntentosConfirmacion" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="usuarioUltimaModificacion" type="{com.americacg.cargavirtual.gcsi.entidad}usuario"/>
 *         &lt;element name="fechaUltMod" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="idTemporal" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "factura", propOrder = {
    "id",
    "codDiscriminador",
    "operador",
    "mayorista",
    "cliente",
    "usuario",
    "moneda",
    "estado",
    "comprobantes",
    "datosAdicionalesFactura",
    "codEmp",
    "descEmpresa",
    "resultado",
    "resultadoMsg",
    "codBarra1",
    "codBarra2",
    "idTrxACG",
    "idTrxOperador",
    "impMinNeg",
    "impMaxNeg",
    "impMinPos",
    "impMaxPos",
    "importeTotal",
    "importeAbonado",
    "obs",
    "cantReversas",
    "fechaAlta",
    "tiempoRespOperadorMs",
    "mensajeError",
    "cantidadIntentosConfirmacion",
    "usuarioUltimaModificacion",
    "fechaUltMod",
    "idTemporal"
})
@XmlSeeAlso({
    FacturaGIRE.class,
    FacturaWU.class,
    FacturaPNET.class
})
public abstract class Factura
    extends BaseModel
{

    protected Long id;
    protected String codDiscriminador;
    @XmlElement(required = true, nillable = true)
    protected Operador operador;
    @XmlElement(required = true, nillable = true)
    protected Mayorista mayorista;
    @XmlElement(required = true, nillable = true)
    protected Cliente cliente;
    @XmlElement(required = true, nillable = true)
    protected Usuario usuario;
    @XmlElement(required = true, nillable = true)
    protected Platmoneda moneda;
    @XmlElement(required = true, nillable = true)
    protected Estado estado;
    @XmlElement(required = true, nillable = true)
    protected List<Comprobante> comprobantes;
    @XmlElement(required = true, nillable = true)
    protected List<Datoadicionalfactura> datosAdicionalesFactura;
    protected String codEmp;
    protected String descEmpresa;
    protected String resultado;
    protected String resultadoMsg;
    protected String codBarra1;
    protected String codBarra2;
    protected String idTrxACG;
    protected String idTrxOperador;
    protected Float impMinNeg;
    protected Float impMaxNeg;
    protected Float impMinPos;
    protected Float impMaxPos;
    protected Float importeTotal;
    protected Float importeAbonado;
    protected String obs;
    protected Long cantReversas;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar fechaAlta;
    protected Long tiempoRespOperadorMs;
    protected String mensajeError;
    protected Long cantidadIntentosConfirmacion;
    @XmlElement(required = true, nillable = true)
    protected Usuario usuarioUltimaModificacion;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar fechaUltMod;
    protected String idTemporal;

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
     * Gets the value of the codDiscriminador property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodDiscriminador() {
        return codDiscriminador;
    }

    /**
     * Sets the value of the codDiscriminador property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodDiscriminador(String value) {
        this.codDiscriminador = value;
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
     * Gets the value of the moneda property.
     * 
     * @return
     *     possible object is
     *     {@link Platmoneda }
     *     
     */
    public Platmoneda getMoneda() {
        return moneda;
    }

    /**
     * Sets the value of the moneda property.
     * 
     * @param value
     *     allowed object is
     *     {@link Platmoneda }
     *     
     */
    public void setMoneda(Platmoneda value) {
        this.moneda = value;
    }

    /**
     * Gets the value of the estado property.
     * 
     * @return
     *     possible object is
     *     {@link Estado }
     *     
     */
    public Estado getEstado() {
        return estado;
    }

    /**
     * Sets the value of the estado property.
     * 
     * @param value
     *     allowed object is
     *     {@link Estado }
     *     
     */
    public void setEstado(Estado value) {
        this.estado = value;
    }

    /**
     * Gets the value of the comprobantes property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the comprobantes property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getComprobantes().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Comprobante }
     * 
     * 
     */
    public List<Comprobante> getComprobantes() {
        if (comprobantes == null) {
            comprobantes = new ArrayList<Comprobante>();
        }
        return this.comprobantes;
    }

    /**
     * Gets the value of the datosAdicionalesFactura property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the datosAdicionalesFactura property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDatosAdicionalesFactura().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Datoadicionalfactura }
     * 
     * 
     */
    public List<Datoadicionalfactura> getDatosAdicionalesFactura() {
        if (datosAdicionalesFactura == null) {
            datosAdicionalesFactura = new ArrayList<Datoadicionalfactura>();
        }
        return this.datosAdicionalesFactura;
    }

    /**
     * Gets the value of the codEmp property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodEmp() {
        return codEmp;
    }

    /**
     * Sets the value of the codEmp property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodEmp(String value) {
        this.codEmp = value;
    }

    /**
     * Gets the value of the descEmpresa property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescEmpresa() {
        return descEmpresa;
    }

    /**
     * Sets the value of the descEmpresa property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescEmpresa(String value) {
        this.descEmpresa = value;
    }

    /**
     * Gets the value of the resultado property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getResultado() {
        return resultado;
    }

    /**
     * Sets the value of the resultado property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setResultado(String value) {
        this.resultado = value;
    }

    /**
     * Gets the value of the resultadoMsg property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getResultadoMsg() {
        return resultadoMsg;
    }

    /**
     * Sets the value of the resultadoMsg property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setResultadoMsg(String value) {
        this.resultadoMsg = value;
    }

    /**
     * Gets the value of the codBarra1 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodBarra1() {
        return codBarra1;
    }

    /**
     * Sets the value of the codBarra1 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodBarra1(String value) {
        this.codBarra1 = value;
    }

    /**
     * Gets the value of the codBarra2 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodBarra2() {
        return codBarra2;
    }

    /**
     * Sets the value of the codBarra2 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodBarra2(String value) {
        this.codBarra2 = value;
    }

    /**
     * Gets the value of the idTrxACG property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdTrxACG() {
        return idTrxACG;
    }

    /**
     * Sets the value of the idTrxACG property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdTrxACG(String value) {
        this.idTrxACG = value;
    }

    /**
     * Gets the value of the idTrxOperador property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdTrxOperador() {
        return idTrxOperador;
    }

    /**
     * Sets the value of the idTrxOperador property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdTrxOperador(String value) {
        this.idTrxOperador = value;
    }

    /**
     * Gets the value of the impMinNeg property.
     * 
     * @return
     *     possible object is
     *     {@link Float }
     *     
     */
    public Float getImpMinNeg() {
        return impMinNeg;
    }

    /**
     * Sets the value of the impMinNeg property.
     * 
     * @param value
     *     allowed object is
     *     {@link Float }
     *     
     */
    public void setImpMinNeg(Float value) {
        this.impMinNeg = value;
    }

    /**
     * Gets the value of the impMaxNeg property.
     * 
     * @return
     *     possible object is
     *     {@link Float }
     *     
     */
    public Float getImpMaxNeg() {
        return impMaxNeg;
    }

    /**
     * Sets the value of the impMaxNeg property.
     * 
     * @param value
     *     allowed object is
     *     {@link Float }
     *     
     */
    public void setImpMaxNeg(Float value) {
        this.impMaxNeg = value;
    }

    /**
     * Gets the value of the impMinPos property.
     * 
     * @return
     *     possible object is
     *     {@link Float }
     *     
     */
    public Float getImpMinPos() {
        return impMinPos;
    }

    /**
     * Sets the value of the impMinPos property.
     * 
     * @param value
     *     allowed object is
     *     {@link Float }
     *     
     */
    public void setImpMinPos(Float value) {
        this.impMinPos = value;
    }

    /**
     * Gets the value of the impMaxPos property.
     * 
     * @return
     *     possible object is
     *     {@link Float }
     *     
     */
    public Float getImpMaxPos() {
        return impMaxPos;
    }

    /**
     * Sets the value of the impMaxPos property.
     * 
     * @param value
     *     allowed object is
     *     {@link Float }
     *     
     */
    public void setImpMaxPos(Float value) {
        this.impMaxPos = value;
    }

    /**
     * Gets the value of the importeTotal property.
     * 
     * @return
     *     possible object is
     *     {@link Float }
     *     
     */
    public Float getImporteTotal() {
        return importeTotal;
    }

    /**
     * Sets the value of the importeTotal property.
     * 
     * @param value
     *     allowed object is
     *     {@link Float }
     *     
     */
    public void setImporteTotal(Float value) {
        this.importeTotal = value;
    }

    /**
     * Gets the value of the importeAbonado property.
     * 
     * @return
     *     possible object is
     *     {@link Float }
     *     
     */
    public Float getImporteAbonado() {
        return importeAbonado;
    }

    /**
     * Sets the value of the importeAbonado property.
     * 
     * @param value
     *     allowed object is
     *     {@link Float }
     *     
     */
    public void setImporteAbonado(Float value) {
        this.importeAbonado = value;
    }

    /**
     * Gets the value of the obs property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getObs() {
        return obs;
    }

    /**
     * Sets the value of the obs property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setObs(String value) {
        this.obs = value;
    }

    /**
     * Gets the value of the cantReversas property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getCantReversas() {
        return cantReversas;
    }

    /**
     * Sets the value of the cantReversas property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setCantReversas(Long value) {
        this.cantReversas = value;
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
     * Gets the value of the tiempoRespOperadorMs property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getTiempoRespOperadorMs() {
        return tiempoRespOperadorMs;
    }

    /**
     * Sets the value of the tiempoRespOperadorMs property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setTiempoRespOperadorMs(Long value) {
        this.tiempoRespOperadorMs = value;
    }

    /**
     * Gets the value of the mensajeError property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMensajeError() {
        return mensajeError;
    }

    /**
     * Sets the value of the mensajeError property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMensajeError(String value) {
        this.mensajeError = value;
    }

    /**
     * Gets the value of the cantidadIntentosConfirmacion property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getCantidadIntentosConfirmacion() {
        return cantidadIntentosConfirmacion;
    }

    /**
     * Sets the value of the cantidadIntentosConfirmacion property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setCantidadIntentosConfirmacion(Long value) {
        this.cantidadIntentosConfirmacion = value;
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
     * Gets the value of the idTemporal property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdTemporal() {
        return idTemporal;
    }

    /**
     * Sets the value of the idTemporal property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdTemporal(String value) {
        this.idTemporal = value;
    }

}
