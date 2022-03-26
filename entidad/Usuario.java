
package com.americacg.cargavirtual.gcsi.entidad;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;
import com.americacg.cargavirtual.gcsi.service.BaseModel;


/**
 * <p>Java class for usuario complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="usuario">
 *   &lt;complexContent>
 *     &lt;extension base="{http://gateway.gcsi.cargavirtual.americacg.com/}baseModel">
 *       &lt;sequence>
 *         &lt;element name="id" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="mayorista" type="{com.americacg.cargavirtual.gcsi.entidad}mayorista" minOccurs="0"/>
 *         &lt;element name="cliente" type="{com.americacg.cargavirtual.gcsi.entidad}cliente" minOccurs="0"/>
 *         &lt;element name="tipoUsuario" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="nombre" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="apellido" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="usuario" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="clave" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="telefono" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="celular" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="mail" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="fechaAlta" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="fechaBaja" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="estado" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="fechaUltimoAcceso" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="fechaCambioClave" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="soloInformes" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="cambiarClaveEnProximoIngreso" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="fechaUltimoAccesoFallido" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="cantAccesosOK" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="cantIntentosAccesosFallidos" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="cantAccesosFallidosConsec" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="coord1proxlogin" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="coord2proxlogin" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="superUsuario" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="permitirRealizarRepartosExrternos" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="idUnicoCambioDeClave" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="autoIncrementoSaldoEnDistribuidoresSuperiores" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="permitirModificarPermisos" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="idTipoDocumento" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="tipoDocumento" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="numeroDocumento" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="claveBloqueada" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="usarTarjetaCoordenadas" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="tarjetaCoordenada" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "usuario", propOrder = {
    "id",
    "mayorista",
    "cliente",
    "tipoUsuario",
    "nombre",
    "apellido",
    "usuario",
    "clave",
    "telefono",
    "celular",
    "mail",
    "fechaAlta",
    "fechaBaja",
    "estado",
    "fechaUltimoAcceso",
    "fechaCambioClave",
    "soloInformes",
    "cambiarClaveEnProximoIngreso",
    "fechaUltimoAccesoFallido",
    "cantAccesosOK",
    "cantIntentosAccesosFallidos",
    "cantAccesosFallidosConsec",
    "coord1Proxlogin",
    "coord2Proxlogin",
    "superUsuario",
    "permitirRealizarRepartosExrternos",
    "idUnicoCambioDeClave",
    "autoIncrementoSaldoEnDistribuidoresSuperiores",
    "permitirModificarPermisos",
    "idTipoDocumento",
    "tipoDocumento",
    "numeroDocumento",
    "claveBloqueada",
    "usarTarjetaCoordenadas",
    "tarjetaCoordenada"
})
public class Usuario
    extends BaseModel
{

    protected Long id;
    @XmlElement(nillable = true)
    protected Mayorista mayorista;
    @XmlElement(nillable = true)
    protected Cliente cliente;
    protected String tipoUsuario;
    protected String nombre;
    protected String apellido;
    protected String usuario;
    protected String clave;
    protected String telefono;
    protected String celular;
    protected String mail;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar fechaAlta;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar fechaBaja;
    protected String estado;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar fechaUltimoAcceso;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar fechaCambioClave;
    protected Boolean soloInformes;
    protected Boolean cambiarClaveEnProximoIngreso;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar fechaUltimoAccesoFallido;
    protected Long cantAccesosOK;
    protected Long cantIntentosAccesosFallidos;
    protected Long cantAccesosFallidosConsec;
    @XmlElement(name = "coord1proxlogin")
    protected Integer coord1Proxlogin;
    @XmlElement(name = "coord2proxlogin")
    protected Integer coord2Proxlogin;
    protected Integer superUsuario;
    protected Integer permitirRealizarRepartosExrternos;
    protected String idUnicoCambioDeClave;
    protected Integer autoIncrementoSaldoEnDistribuidoresSuperiores;
    protected Integer permitirModificarPermisos;
    protected Long idTipoDocumento;
    protected String tipoDocumento;
    protected Integer numeroDocumento;
    protected Boolean claveBloqueada;
    protected Boolean usarTarjetaCoordenadas;
    protected String tarjetaCoordenada;

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
     * Gets the value of the tipoUsuario property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTipoUsuario() {
        return tipoUsuario;
    }

    /**
     * Sets the value of the tipoUsuario property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTipoUsuario(String value) {
        this.tipoUsuario = value;
    }

    /**
     * Gets the value of the nombre property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Sets the value of the nombre property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNombre(String value) {
        this.nombre = value;
    }

    /**
     * Gets the value of the apellido property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getApellido() {
        return apellido;
    }

    /**
     * Sets the value of the apellido property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setApellido(String value) {
        this.apellido = value;
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
     * Gets the value of the clave property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getClave() {
        return clave;
    }

    /**
     * Sets the value of the clave property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setClave(String value) {
        this.clave = value;
    }

    /**
     * Gets the value of the telefono property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTelefono() {
        return telefono;
    }

    /**
     * Sets the value of the telefono property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTelefono(String value) {
        this.telefono = value;
    }

    /**
     * Gets the value of the celular property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCelular() {
        return celular;
    }

    /**
     * Sets the value of the celular property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCelular(String value) {
        this.celular = value;
    }

    /**
     * Gets the value of the mail property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMail() {
        return mail;
    }

    /**
     * Sets the value of the mail property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMail(String value) {
        this.mail = value;
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
     * Gets the value of the fechaBaja property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getFechaBaja() {
        return fechaBaja;
    }

    /**
     * Sets the value of the fechaBaja property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setFechaBaja(XMLGregorianCalendar value) {
        this.fechaBaja = value;
    }

    /**
     * Gets the value of the estado property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEstado() {
        return estado;
    }

    /**
     * Sets the value of the estado property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEstado(String value) {
        this.estado = value;
    }

    /**
     * Gets the value of the fechaUltimoAcceso property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getFechaUltimoAcceso() {
        return fechaUltimoAcceso;
    }

    /**
     * Sets the value of the fechaUltimoAcceso property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setFechaUltimoAcceso(XMLGregorianCalendar value) {
        this.fechaUltimoAcceso = value;
    }

    /**
     * Gets the value of the fechaCambioClave property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getFechaCambioClave() {
        return fechaCambioClave;
    }

    /**
     * Sets the value of the fechaCambioClave property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setFechaCambioClave(XMLGregorianCalendar value) {
        this.fechaCambioClave = value;
    }

    /**
     * Gets the value of the soloInformes property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isSoloInformes() {
        return soloInformes;
    }

    /**
     * Sets the value of the soloInformes property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setSoloInformes(Boolean value) {
        this.soloInformes = value;
    }

    /**
     * Gets the value of the cambiarClaveEnProximoIngreso property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isCambiarClaveEnProximoIngreso() {
        return cambiarClaveEnProximoIngreso;
    }

    /**
     * Sets the value of the cambiarClaveEnProximoIngreso property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setCambiarClaveEnProximoIngreso(Boolean value) {
        this.cambiarClaveEnProximoIngreso = value;
    }

    /**
     * Gets the value of the fechaUltimoAccesoFallido property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getFechaUltimoAccesoFallido() {
        return fechaUltimoAccesoFallido;
    }

    /**
     * Sets the value of the fechaUltimoAccesoFallido property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setFechaUltimoAccesoFallido(XMLGregorianCalendar value) {
        this.fechaUltimoAccesoFallido = value;
    }

    /**
     * Gets the value of the cantAccesosOK property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getCantAccesosOK() {
        return cantAccesosOK;
    }

    /**
     * Sets the value of the cantAccesosOK property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setCantAccesosOK(Long value) {
        this.cantAccesosOK = value;
    }

    /**
     * Gets the value of the cantIntentosAccesosFallidos property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getCantIntentosAccesosFallidos() {
        return cantIntentosAccesosFallidos;
    }

    /**
     * Sets the value of the cantIntentosAccesosFallidos property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setCantIntentosAccesosFallidos(Long value) {
        this.cantIntentosAccesosFallidos = value;
    }

    /**
     * Gets the value of the cantAccesosFallidosConsec property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getCantAccesosFallidosConsec() {
        return cantAccesosFallidosConsec;
    }

    /**
     * Sets the value of the cantAccesosFallidosConsec property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setCantAccesosFallidosConsec(Long value) {
        this.cantAccesosFallidosConsec = value;
    }

    /**
     * Gets the value of the coord1Proxlogin property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getCoord1Proxlogin() {
        return coord1Proxlogin;
    }

    /**
     * Sets the value of the coord1Proxlogin property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setCoord1Proxlogin(Integer value) {
        this.coord1Proxlogin = value;
    }

    /**
     * Gets the value of the coord2Proxlogin property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getCoord2Proxlogin() {
        return coord2Proxlogin;
    }

    /**
     * Sets the value of the coord2Proxlogin property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setCoord2Proxlogin(Integer value) {
        this.coord2Proxlogin = value;
    }

    /**
     * Gets the value of the superUsuario property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getSuperUsuario() {
        return superUsuario;
    }

    /**
     * Sets the value of the superUsuario property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setSuperUsuario(Integer value) {
        this.superUsuario = value;
    }

    /**
     * Gets the value of the permitirRealizarRepartosExrternos property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getPermitirRealizarRepartosExrternos() {
        return permitirRealizarRepartosExrternos;
    }

    /**
     * Sets the value of the permitirRealizarRepartosExrternos property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setPermitirRealizarRepartosExrternos(Integer value) {
        this.permitirRealizarRepartosExrternos = value;
    }

    /**
     * Gets the value of the idUnicoCambioDeClave property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdUnicoCambioDeClave() {
        return idUnicoCambioDeClave;
    }

    /**
     * Sets the value of the idUnicoCambioDeClave property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdUnicoCambioDeClave(String value) {
        this.idUnicoCambioDeClave = value;
    }

    /**
     * Gets the value of the autoIncrementoSaldoEnDistribuidoresSuperiores property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getAutoIncrementoSaldoEnDistribuidoresSuperiores() {
        return autoIncrementoSaldoEnDistribuidoresSuperiores;
    }

    /**
     * Sets the value of the autoIncrementoSaldoEnDistribuidoresSuperiores property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setAutoIncrementoSaldoEnDistribuidoresSuperiores(Integer value) {
        this.autoIncrementoSaldoEnDistribuidoresSuperiores = value;
    }

    /**
     * Gets the value of the permitirModificarPermisos property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getPermitirModificarPermisos() {
        return permitirModificarPermisos;
    }

    /**
     * Sets the value of the permitirModificarPermisos property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setPermitirModificarPermisos(Integer value) {
        this.permitirModificarPermisos = value;
    }

    /**
     * Gets the value of the idTipoDocumento property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getIdTipoDocumento() {
        return idTipoDocumento;
    }

    /**
     * Sets the value of the idTipoDocumento property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setIdTipoDocumento(Long value) {
        this.idTipoDocumento = value;
    }

    /**
     * Gets the value of the tipoDocumento property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTipoDocumento() {
        return tipoDocumento;
    }

    /**
     * Sets the value of the tipoDocumento property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTipoDocumento(String value) {
        this.tipoDocumento = value;
    }

    /**
     * Gets the value of the numeroDocumento property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getNumeroDocumento() {
        return numeroDocumento;
    }

    /**
     * Sets the value of the numeroDocumento property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setNumeroDocumento(Integer value) {
        this.numeroDocumento = value;
    }

    /**
     * Gets the value of the claveBloqueada property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isClaveBloqueada() {
        return claveBloqueada;
    }

    /**
     * Sets the value of the claveBloqueada property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setClaveBloqueada(Boolean value) {
        this.claveBloqueada = value;
    }

    /**
     * Gets the value of the usarTarjetaCoordenadas property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isUsarTarjetaCoordenadas() {
        return usarTarjetaCoordenadas;
    }

    /**
     * Sets the value of the usarTarjetaCoordenadas property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setUsarTarjetaCoordenadas(Boolean value) {
        this.usarTarjetaCoordenadas = value;
    }

    /**
     * Gets the value of the tarjetaCoordenada property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTarjetaCoordenada() {
        return tarjetaCoordenada;
    }

    /**
     * Sets the value of the tarjetaCoordenada property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTarjetaCoordenada(String value) {
        this.tarjetaCoordenada = value;
    }

}
