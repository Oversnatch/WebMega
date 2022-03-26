
package com.americacg.cargavirtual.gcsi.model.acg.request;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;
import com.americacg.cargavirtual.gcsi.service.CodigoRetorno;


/**
 * <p>Java class for gatewayMessage complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="gatewayMessage">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="codPuesto" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="codMnemonicoOperador" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="idMayorista" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="usuario" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="pwd" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="idCliente" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="idTerminal" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="fechaCliente" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 *         &lt;element name="idTurnoCaja" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="idCanal" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="codRetornoAnterior" type="{http://gateway.gcsi.cargavirtual.americacg.com/}codigoRetorno"/>
 *         &lt;element name="cantTimeOutConn" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "gatewayMessage", propOrder = {
    "codPuesto",
    "codMnemonicoOperador",
    "idMayorista",
    "usuario",
    "pwd",
    "idCliente",
    "idTerminal",
    "fechaCliente",
    "idTurnoCaja",
    "idCanal",
    "codRetornoAnterior",
    "cantTimeOutConn"
})
@XmlSeeAlso({
    CerrarTurnoRequest.class,
    ObtenerEstadosRequest.class,
    ConsultarEmpresaRequestACG.class,
    ConsultarTurnoCajaAsignadoRequest.class,
    ConsultarOperacionesRequest.class,
    ReimprimirTicketRequest.class,
    DescargaReporteACG.class,
    ConsultarBarraRequest.class,
    ObtenerParametriaFacturaSinSoportePapelRequestACG.class,
    ObtenerFacturaSinSoportePapelRequestACG.class,
    ConsultarTransaccionRequest.class,
    AbrirTurnoRequest.class,
    ConsultarUltimaTransaccionRequest.class,
    ConfirmarRequestACG.class
})
public abstract class GatewayMessage {

    protected String codPuesto;
    protected String codMnemonicoOperador;
    protected long idMayorista;
    @XmlElement(required = true)
    protected String usuario;
    @XmlElement(required = true)
    protected String pwd;
    protected long idCliente;
    protected Long idTerminal;
    @XmlElement(required = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar fechaCliente;
    protected Long idTurnoCaja;
    @XmlElement(required = true)
    protected String idCanal;
    @XmlElement(required = true)
    protected CodigoRetorno codRetornoAnterior;
    protected int cantTimeOutConn;

    /**
     * Gets the value of the codPuesto property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodPuesto() {
        return codPuesto;
    }

    /**
     * Sets the value of the codPuesto property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodPuesto(String value) {
        this.codPuesto = value;
    }

    /**
     * Gets the value of the codMnemonicoOperador property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodMnemonicoOperador() {
        return codMnemonicoOperador;
    }

    /**
     * Sets the value of the codMnemonicoOperador property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodMnemonicoOperador(String value) {
        this.codMnemonicoOperador = value;
    }

    /**
     * Gets the value of the idMayorista property.
     * 
     */
    public long getIdMayorista() {
        return idMayorista;
    }

    /**
     * Sets the value of the idMayorista property.
     * 
     */
    public void setIdMayorista(long value) {
        this.idMayorista = value;
    }

    /**
     * Gets the value of the usuario property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUsuario() {
        return usuario;
    }

    /**
     * Sets the value of the usuario property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUsuario(String value) {
        this.usuario = value;
    }

    /**
     * Gets the value of the pwd property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPwd() {
        return pwd;
    }

    /**
     * Sets the value of the pwd property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPwd(String value) {
        this.pwd = value;
    }

    /**
     * Gets the value of the idCliente property.
     * 
     */
    public long getIdCliente() {
        return idCliente;
    }

    /**
     * Sets the value of the idCliente property.
     * 
     */
    public void setIdCliente(long value) {
        this.idCliente = value;
    }

    /**
     * Gets the value of the idTerminal property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getIdTerminal() {
        return idTerminal;
    }

    /**
     * Sets the value of the idTerminal property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setIdTerminal(Long value) {
        this.idTerminal = value;
    }

    /**
     * Gets the value of the fechaCliente property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getFechaCliente() {
        return fechaCliente;
    }

    /**
     * Sets the value of the fechaCliente property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setFechaCliente(XMLGregorianCalendar value) {
        this.fechaCliente = value;
    }

    /**
     * Gets the value of the idTurnoCaja property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getIdTurnoCaja() {
        return idTurnoCaja;
    }

    /**
     * Sets the value of the idTurnoCaja property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setIdTurnoCaja(Long value) {
        this.idTurnoCaja = value;
    }

    /**
     * Gets the value of the idCanal property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdCanal() {
        return idCanal;
    }

    /**
     * Sets the value of the idCanal property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdCanal(String value) {
        this.idCanal = value;
    }

    /**
     * Gets the value of the codRetornoAnterior property.
     * 
     * @return
     *     possible object is
     *     {@link CodigoRetorno }
     *     
     */
    public CodigoRetorno getCodRetornoAnterior() {
        return codRetornoAnterior;
    }

    /**
     * Sets the value of the codRetornoAnterior property.
     * 
     * @param value
     *     allowed object is
     *     {@link CodigoRetorno }
     *     
     */
    public void setCodRetornoAnterior(CodigoRetorno value) {
        this.codRetornoAnterior = value;
    }

    /**
     * Gets the value of the cantTimeOutConn property.
     * 
     */
    public int getCantTimeOutConn() {
        return cantTimeOutConn;
    }

    /**
     * Sets the value of the cantTimeOutConn property.
     * 
     */
    public void setCantTimeOutConn(int value) {
        this.cantTimeOutConn = value;
    }

}
