
package com.americacg.cargavirtual.gcsi.entidad;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;
import com.americacg.cargavirtual.gcsi.service.BaseModel;


/**
 * <p>Java class for cliente complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="cliente">
 *   &lt;complexContent>
 *     &lt;extension base="{http://gateway.gcsi.cargavirtual.americacg.com/}baseModel">
 *       &lt;sequence>
 *         &lt;element name="id" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="idPais" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="idProvincia" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="idlocalidad" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="idComercial" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="idMayorista" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="idDistribuidor1" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="idDistribuidor2" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="idDistribuidor3" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="idDistribuidor4" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="idDistribuidor5" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="tipoIva" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="tipoCliente" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="nivelDistribuidorSuperior" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="nroContrato" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="estado" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="nombre" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="apellido" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="razonSocial" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="nombreFantasia" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="CUIT" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="facturable" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="acredComisionConIVA" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="telefono1" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="telefono2" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="telefono3" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="celular" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="fax" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="mail" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="fechaAlta" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="fechaBaja" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="comentario" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="calle" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="altura" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="idDistribuidorSuperior" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="razonSocialDistribuidorSuperior" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="idTipoTerminal" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="descripcionTipoTerminal" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="idLogoChico" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="logoChico" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="idFooter" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="footer" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="idTicket" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="ticket" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="mostrarPin" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="habilitarPrestamos" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="distPermitirLogPos" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="validarIdPos" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="idPos" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="habilitarVirtual" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="idEntidad" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="descripcionEntidad" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="idRubro" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="rubro" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="permitirDepositos" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="importeMaxRepartoMaquinas" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="calculoAutomComisiones" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="dist_asignar_ventas_a_cliente_inf" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="clienteValidadoOK" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="permitirVentasPorWeb" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="valorParaIncrementoAutomaticoDeSaldo" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="valorLimiteDescubierto" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="solicActualizacionDatosAlCliente" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="geo_latitud" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="0"/>
 *         &lt;element name="geo_longitud" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="0"/>
 *         &lt;element name="imagen_del_local" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="cantMinTrxParaCobrarCostoAdminist" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="costoAdminist" type="{http://www.w3.org/2001/XMLSchema}float" minOccurs="0"/>
 *         &lt;element name="wu_idLocation" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="partido" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="barrio" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="piso" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="depto" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="entreCalle1" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="entreCalle2" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="local" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="tipoDeCentro" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ubicacion" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="tipoUbicacion" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="comuna" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="jurisdiccion" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="snSUBEcargaDif" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="idClienteExterno" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="idUnicoCliente" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="id_movistar" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="id_claro" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="id_personal" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="id_nextel" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="casa" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="lote" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="manzana" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="pnet_serieEquipo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="idsUnifCli_sube_cd_locationid" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="idsUnifCli_sube_cd_posid" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="idsUnifCli_sube_cd_terminalid" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="idSegmentoCliente" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="descSegmentoCliente" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="idTipoDocumento" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="descDocumento" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="numeroDocumento" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "cliente", propOrder = {
    "id",
    "idPais",
    "idProvincia",
    "idlocalidad",
    "idComercial",
    "idMayorista",
    "idDistribuidor1",
    "idDistribuidor2",
    "idDistribuidor3",
    "idDistribuidor4",
    "idDistribuidor5",
    "tipoIva",
    "tipoCliente",
    "nivelDistribuidorSuperior",
    "nroContrato",
    "estado",
    "nombre",
    "apellido",
    "razonSocial",
    "nombreFantasia",
    "cuit",
    "facturable",
    "acredComisionConIVA",
    "telefono1",
    "telefono2",
    "telefono3",
    "celular",
    "fax",
    "mail",
    "fechaAlta",
    "fechaBaja",
    "comentario",
    "calle",
    "altura",
    "idDistribuidorSuperior",
    "razonSocialDistribuidorSuperior",
    "idTipoTerminal",
    "descripcionTipoTerminal",
    "idLogoChico",
    "logoChico",
    "idFooter",
    "footer",
    "idTicket",
    "ticket",
    "mostrarPin",
    "habilitarPrestamos",
    "distPermitirLogPos",
    "validarIdPos",
    "idPos",
    "habilitarVirtual",
    "idEntidad",
    "descripcionEntidad",
    "idRubro",
    "rubro",
    "permitirDepositos",
    "importeMaxRepartoMaquinas",
    "calculoAutomComisiones",
    "distAsignarVentasAClienteInf",
    "clienteValidadoOK",
    "permitirVentasPorWeb",
    "valorParaIncrementoAutomaticoDeSaldo",
    "valorLimiteDescubierto",
    "solicActualizacionDatosAlCliente",
    "geoLatitud",
    "geoLongitud",
    "imagenDelLocal",
    "cantMinTrxParaCobrarCostoAdminist",
    "costoAdminist",
    "wuIdLocation",
    "partido",
    "barrio",
    "piso",
    "depto",
    "entreCalle1",
    "entreCalle2",
    "local",
    "tipoDeCentro",
    "ubicacion",
    "tipoUbicacion",
    "comuna",
    "jurisdiccion",
    "snSUBEcargaDif",
    "idClienteExterno",
    "idUnicoCliente",
    "idMovistar",
    "idClaro",
    "idPersonal",
    "idNextel",
    "casa",
    "lote",
    "manzana",
    "pnetSerieEquipo",
    "idsUnifCliSubeCdLocationid",
    "idsUnifCliSubeCdPosid",
    "idsUnifCliSubeCdTerminalid",
    "idSegmentoCliente",
    "descSegmentoCliente",
    "idTipoDocumento",
    "descDocumento",
    "numeroDocumento"
})
public class Cliente
    extends BaseModel
{

    protected Long id;
    protected Long idPais;
    protected Long idProvincia;
    protected Long idlocalidad;
    protected Long idComercial;
    protected Long idMayorista;
    protected Long idDistribuidor1;
    protected Long idDistribuidor2;
    protected Long idDistribuidor3;
    protected Long idDistribuidor4;
    protected Long idDistribuidor5;
    protected String tipoIva;
    protected String tipoCliente;
    protected Integer nivelDistribuidorSuperior;
    protected Long nroContrato;
    protected String estado;
    protected String nombre;
    protected String apellido;
    protected String razonSocial;
    protected String nombreFantasia;
    @XmlElement(name = "CUIT")
    protected Long cuit;
    protected Boolean facturable;
    protected Boolean acredComisionConIVA;
    protected String telefono1;
    protected String telefono2;
    protected String telefono3;
    protected String celular;
    protected String fax;
    protected String mail;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar fechaAlta;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar fechaBaja;
    protected String comentario;
    protected String calle;
    protected Long altura;
    protected Long idDistribuidorSuperior;
    protected String razonSocialDistribuidorSuperior;
    protected Long idTipoTerminal;
    protected String descripcionTipoTerminal;
    protected Long idLogoChico;
    protected String logoChico;
    protected Long idFooter;
    protected String footer;
    protected Long idTicket;
    protected String ticket;
    protected Boolean mostrarPin;
    protected Boolean habilitarPrestamos;
    protected Boolean distPermitirLogPos;
    protected Boolean validarIdPos;
    protected String idPos;
    protected Boolean habilitarVirtual;
    protected Long idEntidad;
    protected String descripcionEntidad;
    protected Long idRubro;
    protected String rubro;
    protected Boolean permitirDepositos;
    protected Integer importeMaxRepartoMaquinas;
    protected Boolean calculoAutomComisiones;
    @XmlElement(name = "dist_asignar_ventas_a_cliente_inf")
    protected Boolean distAsignarVentasAClienteInf;
    protected Boolean clienteValidadoOK;
    protected Boolean permitirVentasPorWeb;
    protected Integer valorParaIncrementoAutomaticoDeSaldo;
    protected Integer valorLimiteDescubierto;
    protected Boolean solicActualizacionDatosAlCliente;
    @XmlElement(name = "geo_latitud")
    protected Double geoLatitud;
    @XmlElement(name = "geo_longitud")
    protected Double geoLongitud;
    @XmlElement(name = "imagen_del_local")
    protected String imagenDelLocal;
    protected Integer cantMinTrxParaCobrarCostoAdminist;
    protected Float costoAdminist;
    @XmlElement(name = "wu_idLocation")
    protected String wuIdLocation;
    protected String partido;
    protected String barrio;
    protected String piso;
    protected String depto;
    protected String entreCalle1;
    protected String entreCalle2;
    protected String local;
    protected String tipoDeCentro;
    protected String ubicacion;
    protected String tipoUbicacion;
    protected String comuna;
    protected String jurisdiccion;
    protected String snSUBEcargaDif;
    protected String idClienteExterno;
    protected Long idUnicoCliente;
    @XmlElement(name = "id_movistar")
    protected String idMovistar;
    @XmlElement(name = "id_claro")
    protected String idClaro;
    @XmlElement(name = "id_personal")
    protected String idPersonal;
    @XmlElement(name = "id_nextel")
    protected String idNextel;
    protected String casa;
    protected String lote;
    protected String manzana;
    @XmlElement(name = "pnet_serieEquipo")
    protected String pnetSerieEquipo;
    @XmlElement(name = "idsUnifCli_sube_cd_locationid")
    protected String idsUnifCliSubeCdLocationid;
    @XmlElement(name = "idsUnifCli_sube_cd_posid")
    protected String idsUnifCliSubeCdPosid;
    @XmlElement(name = "idsUnifCli_sube_cd_terminalid")
    protected String idsUnifCliSubeCdTerminalid;
    protected Long idSegmentoCliente;
    protected String descSegmentoCliente;
    protected Long idTipoDocumento;
    protected String descDocumento;
    protected String numeroDocumento;

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
     * Gets the value of the idPais property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getIdPais() {
        return idPais;
    }

    /**
     * Sets the value of the idPais property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setIdPais(Long value) {
        this.idPais = value;
    }

    /**
     * Gets the value of the idProvincia property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getIdProvincia() {
        return idProvincia;
    }

    /**
     * Sets the value of the idProvincia property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setIdProvincia(Long value) {
        this.idProvincia = value;
    }

    /**
     * Gets the value of the idlocalidad property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getIdlocalidad() {
        return idlocalidad;
    }

    /**
     * Sets the value of the idlocalidad property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setIdlocalidad(Long value) {
        this.idlocalidad = value;
    }

    /**
     * Gets the value of the idComercial property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getIdComercial() {
        return idComercial;
    }

    /**
     * Sets the value of the idComercial property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setIdComercial(Long value) {
        this.idComercial = value;
    }

    /**
     * Gets the value of the idMayorista property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getIdMayorista() {
        return idMayorista;
    }

    /**
     * Sets the value of the idMayorista property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setIdMayorista(Long value) {
        this.idMayorista = value;
    }

    /**
     * Gets the value of the idDistribuidor1 property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getIdDistribuidor1() {
        return idDistribuidor1;
    }

    /**
     * Sets the value of the idDistribuidor1 property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setIdDistribuidor1(Long value) {
        this.idDistribuidor1 = value;
    }

    /**
     * Gets the value of the idDistribuidor2 property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getIdDistribuidor2() {
        return idDistribuidor2;
    }

    /**
     * Sets the value of the idDistribuidor2 property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setIdDistribuidor2(Long value) {
        this.idDistribuidor2 = value;
    }

    /**
     * Gets the value of the idDistribuidor3 property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getIdDistribuidor3() {
        return idDistribuidor3;
    }

    /**
     * Sets the value of the idDistribuidor3 property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setIdDistribuidor3(Long value) {
        this.idDistribuidor3 = value;
    }

    /**
     * Gets the value of the idDistribuidor4 property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getIdDistribuidor4() {
        return idDistribuidor4;
    }

    /**
     * Sets the value of the idDistribuidor4 property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setIdDistribuidor4(Long value) {
        this.idDistribuidor4 = value;
    }

    /**
     * Gets the value of the idDistribuidor5 property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getIdDistribuidor5() {
        return idDistribuidor5;
    }

    /**
     * Sets the value of the idDistribuidor5 property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setIdDistribuidor5(Long value) {
        this.idDistribuidor5 = value;
    }

    /**
     * Gets the value of the tipoIva property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTipoIva() {
        return tipoIva;
    }

    /**
     * Sets the value of the tipoIva property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTipoIva(String value) {
        this.tipoIva = value;
    }

    /**
     * Gets the value of the tipoCliente property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTipoCliente() {
        return tipoCliente;
    }

    /**
     * Sets the value of the tipoCliente property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTipoCliente(String value) {
        this.tipoCliente = value;
    }

    /**
     * Gets the value of the nivelDistribuidorSuperior property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getNivelDistribuidorSuperior() {
        return nivelDistribuidorSuperior;
    }

    /**
     * Sets the value of the nivelDistribuidorSuperior property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setNivelDistribuidorSuperior(Integer value) {
        this.nivelDistribuidorSuperior = value;
    }

    /**
     * Gets the value of the nroContrato property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getNroContrato() {
        return nroContrato;
    }

    /**
     * Sets the value of the nroContrato property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setNroContrato(Long value) {
        this.nroContrato = value;
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
     * Gets the value of the razonSocial property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRazonSocial() {
        return razonSocial;
    }

    /**
     * Sets the value of the razonSocial property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRazonSocial(String value) {
        this.razonSocial = value;
    }

    /**
     * Gets the value of the nombreFantasia property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNombreFantasia() {
        return nombreFantasia;
    }

    /**
     * Sets the value of the nombreFantasia property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNombreFantasia(String value) {
        this.nombreFantasia = value;
    }

    /**
     * Gets the value of the cuit property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getCUIT() {
        return cuit;
    }

    /**
     * Sets the value of the cuit property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setCUIT(Long value) {
        this.cuit = value;
    }

    /**
     * Gets the value of the facturable property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isFacturable() {
        return facturable;
    }

    /**
     * Sets the value of the facturable property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setFacturable(Boolean value) {
        this.facturable = value;
    }

    /**
     * Gets the value of the acredComisionConIVA property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isAcredComisionConIVA() {
        return acredComisionConIVA;
    }

    /**
     * Sets the value of the acredComisionConIVA property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setAcredComisionConIVA(Boolean value) {
        this.acredComisionConIVA = value;
    }

    /**
     * Gets the value of the telefono1 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTelefono1() {
        return telefono1;
    }

    /**
     * Sets the value of the telefono1 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTelefono1(String value) {
        this.telefono1 = value;
    }

    /**
     * Gets the value of the telefono2 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTelefono2() {
        return telefono2;
    }

    /**
     * Sets the value of the telefono2 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTelefono2(String value) {
        this.telefono2 = value;
    }

    /**
     * Gets the value of the telefono3 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTelefono3() {
        return telefono3;
    }

    /**
     * Sets the value of the telefono3 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTelefono3(String value) {
        this.telefono3 = value;
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
     * Gets the value of the fax property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFax() {
        return fax;
    }

    /**
     * Sets the value of the fax property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFax(String value) {
        this.fax = value;
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
     * Gets the value of the comentario property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getComentario() {
        return comentario;
    }

    /**
     * Sets the value of the comentario property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setComentario(String value) {
        this.comentario = value;
    }

    /**
     * Gets the value of the calle property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCalle() {
        return calle;
    }

    /**
     * Sets the value of the calle property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCalle(String value) {
        this.calle = value;
    }

    /**
     * Gets the value of the altura property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getAltura() {
        return altura;
    }

    /**
     * Sets the value of the altura property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setAltura(Long value) {
        this.altura = value;
    }

    /**
     * Gets the value of the idDistribuidorSuperior property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getIdDistribuidorSuperior() {
        return idDistribuidorSuperior;
    }

    /**
     * Sets the value of the idDistribuidorSuperior property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setIdDistribuidorSuperior(Long value) {
        this.idDistribuidorSuperior = value;
    }

    /**
     * Gets the value of the razonSocialDistribuidorSuperior property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRazonSocialDistribuidorSuperior() {
        return razonSocialDistribuidorSuperior;
    }

    /**
     * Sets the value of the razonSocialDistribuidorSuperior property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRazonSocialDistribuidorSuperior(String value) {
        this.razonSocialDistribuidorSuperior = value;
    }

    /**
     * Gets the value of the idTipoTerminal property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getIdTipoTerminal() {
        return idTipoTerminal;
    }

    /**
     * Sets the value of the idTipoTerminal property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setIdTipoTerminal(Long value) {
        this.idTipoTerminal = value;
    }

    /**
     * Gets the value of the descripcionTipoTerminal property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescripcionTipoTerminal() {
        return descripcionTipoTerminal;
    }

    /**
     * Sets the value of the descripcionTipoTerminal property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescripcionTipoTerminal(String value) {
        this.descripcionTipoTerminal = value;
    }

    /**
     * Gets the value of the idLogoChico property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getIdLogoChico() {
        return idLogoChico;
    }

    /**
     * Sets the value of the idLogoChico property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setIdLogoChico(Long value) {
        this.idLogoChico = value;
    }

    /**
     * Gets the value of the logoChico property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLogoChico() {
        return logoChico;
    }

    /**
     * Sets the value of the logoChico property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLogoChico(String value) {
        this.logoChico = value;
    }

    /**
     * Gets the value of the idFooter property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getIdFooter() {
        return idFooter;
    }

    /**
     * Sets the value of the idFooter property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setIdFooter(Long value) {
        this.idFooter = value;
    }

    /**
     * Gets the value of the footer property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFooter() {
        return footer;
    }

    /**
     * Sets the value of the footer property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFooter(String value) {
        this.footer = value;
    }

    /**
     * Gets the value of the idTicket property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getIdTicket() {
        return idTicket;
    }

    /**
     * Sets the value of the idTicket property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setIdTicket(Long value) {
        this.idTicket = value;
    }

    /**
     * Gets the value of the ticket property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTicket() {
        return ticket;
    }

    /**
     * Sets the value of the ticket property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTicket(String value) {
        this.ticket = value;
    }

    /**
     * Gets the value of the mostrarPin property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isMostrarPin() {
        return mostrarPin;
    }

    /**
     * Sets the value of the mostrarPin property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setMostrarPin(Boolean value) {
        this.mostrarPin = value;
    }

    /**
     * Gets the value of the habilitarPrestamos property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isHabilitarPrestamos() {
        return habilitarPrestamos;
    }

    /**
     * Sets the value of the habilitarPrestamos property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setHabilitarPrestamos(Boolean value) {
        this.habilitarPrestamos = value;
    }

    /**
     * Gets the value of the distPermitirLogPos property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isDistPermitirLogPos() {
        return distPermitirLogPos;
    }

    /**
     * Sets the value of the distPermitirLogPos property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setDistPermitirLogPos(Boolean value) {
        this.distPermitirLogPos = value;
    }

    /**
     * Gets the value of the validarIdPos property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isValidarIdPos() {
        return validarIdPos;
    }

    /**
     * Sets the value of the validarIdPos property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setValidarIdPos(Boolean value) {
        this.validarIdPos = value;
    }

    /**
     * Gets the value of the idPos property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdPos() {
        return idPos;
    }

    /**
     * Sets the value of the idPos property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdPos(String value) {
        this.idPos = value;
    }

    /**
     * Gets the value of the habilitarVirtual property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isHabilitarVirtual() {
        return habilitarVirtual;
    }

    /**
     * Sets the value of the habilitarVirtual property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setHabilitarVirtual(Boolean value) {
        this.habilitarVirtual = value;
    }

    /**
     * Gets the value of the idEntidad property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getIdEntidad() {
        return idEntidad;
    }

    /**
     * Sets the value of the idEntidad property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setIdEntidad(Long value) {
        this.idEntidad = value;
    }

    /**
     * Gets the value of the descripcionEntidad property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescripcionEntidad() {
        return descripcionEntidad;
    }

    /**
     * Sets the value of the descripcionEntidad property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescripcionEntidad(String value) {
        this.descripcionEntidad = value;
    }

    /**
     * Gets the value of the idRubro property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getIdRubro() {
        return idRubro;
    }

    /**
     * Sets the value of the idRubro property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setIdRubro(Long value) {
        this.idRubro = value;
    }

    /**
     * Gets the value of the rubro property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRubro() {
        return rubro;
    }

    /**
     * Sets the value of the rubro property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRubro(String value) {
        this.rubro = value;
    }

    /**
     * Gets the value of the permitirDepositos property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isPermitirDepositos() {
        return permitirDepositos;
    }

    /**
     * Sets the value of the permitirDepositos property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setPermitirDepositos(Boolean value) {
        this.permitirDepositos = value;
    }

    /**
     * Gets the value of the importeMaxRepartoMaquinas property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getImporteMaxRepartoMaquinas() {
        return importeMaxRepartoMaquinas;
    }

    /**
     * Sets the value of the importeMaxRepartoMaquinas property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setImporteMaxRepartoMaquinas(Integer value) {
        this.importeMaxRepartoMaquinas = value;
    }

    /**
     * Gets the value of the calculoAutomComisiones property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isCalculoAutomComisiones() {
        return calculoAutomComisiones;
    }

    /**
     * Sets the value of the calculoAutomComisiones property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setCalculoAutomComisiones(Boolean value) {
        this.calculoAutomComisiones = value;
    }

    /**
     * Gets the value of the distAsignarVentasAClienteInf property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isDistAsignarVentasAClienteInf() {
        return distAsignarVentasAClienteInf;
    }

    /**
     * Sets the value of the distAsignarVentasAClienteInf property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setDistAsignarVentasAClienteInf(Boolean value) {
        this.distAsignarVentasAClienteInf = value;
    }

    /**
     * Gets the value of the clienteValidadoOK property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isClienteValidadoOK() {
        return clienteValidadoOK;
    }

    /**
     * Sets the value of the clienteValidadoOK property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setClienteValidadoOK(Boolean value) {
        this.clienteValidadoOK = value;
    }

    /**
     * Gets the value of the permitirVentasPorWeb property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isPermitirVentasPorWeb() {
        return permitirVentasPorWeb;
    }

    /**
     * Sets the value of the permitirVentasPorWeb property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setPermitirVentasPorWeb(Boolean value) {
        this.permitirVentasPorWeb = value;
    }

    /**
     * Gets the value of the valorParaIncrementoAutomaticoDeSaldo property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getValorParaIncrementoAutomaticoDeSaldo() {
        return valorParaIncrementoAutomaticoDeSaldo;
    }

    /**
     * Sets the value of the valorParaIncrementoAutomaticoDeSaldo property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setValorParaIncrementoAutomaticoDeSaldo(Integer value) {
        this.valorParaIncrementoAutomaticoDeSaldo = value;
    }

    /**
     * Gets the value of the valorLimiteDescubierto property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getValorLimiteDescubierto() {
        return valorLimiteDescubierto;
    }

    /**
     * Sets the value of the valorLimiteDescubierto property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setValorLimiteDescubierto(Integer value) {
        this.valorLimiteDescubierto = value;
    }

    /**
     * Gets the value of the solicActualizacionDatosAlCliente property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isSolicActualizacionDatosAlCliente() {
        return solicActualizacionDatosAlCliente;
    }

    /**
     * Sets the value of the solicActualizacionDatosAlCliente property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setSolicActualizacionDatosAlCliente(Boolean value) {
        this.solicActualizacionDatosAlCliente = value;
    }

    /**
     * Gets the value of the geoLatitud property.
     * 
     * @return
     *     possible object is
     *     {@link Double }
     *     
     */
    public Double getGeoLatitud() {
        return geoLatitud;
    }

    /**
     * Sets the value of the geoLatitud property.
     * 
     * @param value
     *     allowed object is
     *     {@link Double }
     *     
     */
    public void setGeoLatitud(Double value) {
        this.geoLatitud = value;
    }

    /**
     * Gets the value of the geoLongitud property.
     * 
     * @return
     *     possible object is
     *     {@link Double }
     *     
     */
    public Double getGeoLongitud() {
        return geoLongitud;
    }

    /**
     * Sets the value of the geoLongitud property.
     * 
     * @param value
     *     allowed object is
     *     {@link Double }
     *     
     */
    public void setGeoLongitud(Double value) {
        this.geoLongitud = value;
    }

    /**
     * Gets the value of the imagenDelLocal property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getImagenDelLocal() {
        return imagenDelLocal;
    }

    /**
     * Sets the value of the imagenDelLocal property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setImagenDelLocal(String value) {
        this.imagenDelLocal = value;
    }

    /**
     * Gets the value of the cantMinTrxParaCobrarCostoAdminist property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getCantMinTrxParaCobrarCostoAdminist() {
        return cantMinTrxParaCobrarCostoAdminist;
    }

    /**
     * Sets the value of the cantMinTrxParaCobrarCostoAdminist property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setCantMinTrxParaCobrarCostoAdminist(Integer value) {
        this.cantMinTrxParaCobrarCostoAdminist = value;
    }

    /**
     * Gets the value of the costoAdminist property.
     * 
     * @return
     *     possible object is
     *     {@link Float }
     *     
     */
    public Float getCostoAdminist() {
        return costoAdminist;
    }

    /**
     * Sets the value of the costoAdminist property.
     * 
     * @param value
     *     allowed object is
     *     {@link Float }
     *     
     */
    public void setCostoAdminist(Float value) {
        this.costoAdminist = value;
    }

    /**
     * Gets the value of the wuIdLocation property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getWuIdLocation() {
        return wuIdLocation;
    }

    /**
     * Sets the value of the wuIdLocation property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setWuIdLocation(String value) {
        this.wuIdLocation = value;
    }

    /**
     * Gets the value of the partido property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPartido() {
        return partido;
    }

    /**
     * Sets the value of the partido property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPartido(String value) {
        this.partido = value;
    }

    /**
     * Gets the value of the barrio property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBarrio() {
        return barrio;
    }

    /**
     * Sets the value of the barrio property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBarrio(String value) {
        this.barrio = value;
    }

    /**
     * Gets the value of the piso property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPiso() {
        return piso;
    }

    /**
     * Sets the value of the piso property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPiso(String value) {
        this.piso = value;
    }

    /**
     * Gets the value of the depto property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDepto() {
        return depto;
    }

    /**
     * Sets the value of the depto property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDepto(String value) {
        this.depto = value;
    }

    /**
     * Gets the value of the entreCalle1 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEntreCalle1() {
        return entreCalle1;
    }

    /**
     * Sets the value of the entreCalle1 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEntreCalle1(String value) {
        this.entreCalle1 = value;
    }

    /**
     * Gets the value of the entreCalle2 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEntreCalle2() {
        return entreCalle2;
    }

    /**
     * Sets the value of the entreCalle2 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEntreCalle2(String value) {
        this.entreCalle2 = value;
    }

    /**
     * Gets the value of the local property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLocal() {
        return local;
    }

    /**
     * Sets the value of the local property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLocal(String value) {
        this.local = value;
    }

    /**
     * Gets the value of the tipoDeCentro property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTipoDeCentro() {
        return tipoDeCentro;
    }

    /**
     * Sets the value of the tipoDeCentro property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTipoDeCentro(String value) {
        this.tipoDeCentro = value;
    }

    /**
     * Gets the value of the ubicacion property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUbicacion() {
        return ubicacion;
    }

    /**
     * Sets the value of the ubicacion property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUbicacion(String value) {
        this.ubicacion = value;
    }

    /**
     * Gets the value of the tipoUbicacion property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTipoUbicacion() {
        return tipoUbicacion;
    }

    /**
     * Sets the value of the tipoUbicacion property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTipoUbicacion(String value) {
        this.tipoUbicacion = value;
    }

    /**
     * Gets the value of the comuna property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getComuna() {
        return comuna;
    }

    /**
     * Sets the value of the comuna property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setComuna(String value) {
        this.comuna = value;
    }

    /**
     * Gets the value of the jurisdiccion property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getJurisdiccion() {
        return jurisdiccion;
    }

    /**
     * Sets the value of the jurisdiccion property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setJurisdiccion(String value) {
        this.jurisdiccion = value;
    }

    /**
     * Gets the value of the snSUBEcargaDif property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSnSUBEcargaDif() {
        return snSUBEcargaDif;
    }

    /**
     * Sets the value of the snSUBEcargaDif property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSnSUBEcargaDif(String value) {
        this.snSUBEcargaDif = value;
    }

    /**
     * Gets the value of the idClienteExterno property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdClienteExterno() {
        return idClienteExterno;
    }

    /**
     * Sets the value of the idClienteExterno property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdClienteExterno(String value) {
        this.idClienteExterno = value;
    }

    /**
     * Gets the value of the idUnicoCliente property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getIdUnicoCliente() {
        return idUnicoCliente;
    }

    /**
     * Sets the value of the idUnicoCliente property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setIdUnicoCliente(Long value) {
        this.idUnicoCliente = value;
    }

    /**
     * Gets the value of the idMovistar property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdMovistar() {
        return idMovistar;
    }

    /**
     * Sets the value of the idMovistar property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdMovistar(String value) {
        this.idMovistar = value;
    }

    /**
     * Gets the value of the idClaro property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdClaro() {
        return idClaro;
    }

    /**
     * Sets the value of the idClaro property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdClaro(String value) {
        this.idClaro = value;
    }

    /**
     * Gets the value of the idPersonal property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdPersonal() {
        return idPersonal;
    }

    /**
     * Sets the value of the idPersonal property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdPersonal(String value) {
        this.idPersonal = value;
    }

    /**
     * Gets the value of the idNextel property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdNextel() {
        return idNextel;
    }

    /**
     * Sets the value of the idNextel property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdNextel(String value) {
        this.idNextel = value;
    }

    /**
     * Gets the value of the casa property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCasa() {
        return casa;
    }

    /**
     * Sets the value of the casa property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCasa(String value) {
        this.casa = value;
    }

    /**
     * Gets the value of the lote property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLote() {
        return lote;
    }

    /**
     * Sets the value of the lote property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLote(String value) {
        this.lote = value;
    }

    /**
     * Gets the value of the manzana property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getManzana() {
        return manzana;
    }

    /**
     * Sets the value of the manzana property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setManzana(String value) {
        this.manzana = value;
    }

    /**
     * Gets the value of the pnetSerieEquipo property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPnetSerieEquipo() {
        return pnetSerieEquipo;
    }

    /**
     * Sets the value of the pnetSerieEquipo property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPnetSerieEquipo(String value) {
        this.pnetSerieEquipo = value;
    }

    /**
     * Gets the value of the idsUnifCliSubeCdLocationid property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdsUnifCliSubeCdLocationid() {
        return idsUnifCliSubeCdLocationid;
    }

    /**
     * Sets the value of the idsUnifCliSubeCdLocationid property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdsUnifCliSubeCdLocationid(String value) {
        this.idsUnifCliSubeCdLocationid = value;
    }

    /**
     * Gets the value of the idsUnifCliSubeCdPosid property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdsUnifCliSubeCdPosid() {
        return idsUnifCliSubeCdPosid;
    }

    /**
     * Sets the value of the idsUnifCliSubeCdPosid property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdsUnifCliSubeCdPosid(String value) {
        this.idsUnifCliSubeCdPosid = value;
    }

    /**
     * Gets the value of the idsUnifCliSubeCdTerminalid property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdsUnifCliSubeCdTerminalid() {
        return idsUnifCliSubeCdTerminalid;
    }

    /**
     * Sets the value of the idsUnifCliSubeCdTerminalid property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdsUnifCliSubeCdTerminalid(String value) {
        this.idsUnifCliSubeCdTerminalid = value;
    }

    /**
     * Gets the value of the idSegmentoCliente property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getIdSegmentoCliente() {
        return idSegmentoCliente;
    }

    /**
     * Sets the value of the idSegmentoCliente property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setIdSegmentoCliente(Long value) {
        this.idSegmentoCliente = value;
    }

    /**
     * Gets the value of the descSegmentoCliente property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescSegmentoCliente() {
        return descSegmentoCliente;
    }

    /**
     * Sets the value of the descSegmentoCliente property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescSegmentoCliente(String value) {
        this.descSegmentoCliente = value;
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
     * Gets the value of the descDocumento property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescDocumento() {
        return descDocumento;
    }

    /**
     * Sets the value of the descDocumento property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescDocumento(String value) {
        this.descDocumento = value;
    }

    /**
     * Gets the value of the numeroDocumento property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNumeroDocumento() {
        return numeroDocumento;
    }

    /**
     * Sets the value of the numeroDocumento property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNumeroDocumento(String value) {
        this.numeroDocumento = value;
    }

}
