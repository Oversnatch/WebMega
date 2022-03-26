
package com.americacg.cargavirtual.gcsi.model.acg.response;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;
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
 *         &lt;element name="fechaServidor" type="{http://www.w3.org/2001/XMLSchema}anySimpleType"/>
 *         &lt;element name="codigo" type="{http://gateway.gcsi.cargavirtual.americacg.com/}codigoRetorno"/>
 *         &lt;element name="mensaje" type="{http://www.w3.org/2001/XMLSchema}string"/>
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
    "fechaServidor",
    "codigo",
    "mensaje",
    "cantTimeOutConn"
})
@XmlSeeAlso({
    ObtenerFacturaSinSoportePapelResponseACG.class,
    ConsultarTransaccionResponse.class,
    ReimprimirTicketResponse.class,
    CerrarTurnoResponse.class,
    ConfirmarResponseACG.class,
    ObtenerEstadosResponse.class,
    ConsultarOperacionesResponse.class,
    ReporteACGResponse.class,
    AbrirTurnoResponse.class,
    ConsultaPagoSinFacturaResponse.class,
    ConsultarBarraResponse.class,
    ObtenerParametriaFacturaSinSoportePapelResponseACG.class,
    ConsultarEmpresaResponseACG.class,
    ConsultarUltimaTransaccionResponse.class,
    ConsultarTurnoCajaAsignadoResponse.class
})
public class GatewayMessage {

    @XmlElement(required = true)
    @XmlSchemaType(name = "anySimpleType")
    protected Object fechaServidor;
    @XmlElement(required = true)
    protected CodigoRetorno codigo;
    @XmlElement(required = true)
    protected String mensaje;
    protected int cantTimeOutConn;

    /**
     * Gets the value of the fechaServidor property.
     * 
     * @return
     *     possible object is
     *     {@link Object }
     *     
     */
    public Object getFechaServidor() {
        return fechaServidor;
    }

    /**
     * Sets the value of the fechaServidor property.
     * 
     * @param value
     *     allowed object is
     *     {@link Object }
     *     
     */
    public void setFechaServidor(Object value) {
        this.fechaServidor = value;
    }

    /**
     * Gets the value of the codigo property.
     * 
     * @return
     *     possible object is
     *     {@link CodigoRetorno }
     *     
     */
    public CodigoRetorno getCodigo() {
        return codigo;
    }

    /**
     * Sets the value of the codigo property.
     * 
     * @param value
     *     allowed object is
     *     {@link CodigoRetorno }
     *     
     */
    public void setCodigo(CodigoRetorno value) {
        this.codigo = value;
    }

    /**
     * Gets the value of the mensaje property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMensaje() {
        return mensaje;
    }

    /**
     * Sets the value of the mensaje property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMensaje(String value) {
        this.mensaje = value;
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
