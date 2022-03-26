
package com.americacg.cargavirtual.gcsi.entidad;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;
import com.americacg.cargavirtual.gcsi.service.BaseModel;


/**
 * <p>Java class for mayorista complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="mayorista">
 *   &lt;complexContent>
 *     &lt;extension base="{http://gateway.gcsi.cargavirtual.americacg.com/}baseModel">
 *       &lt;sequence>
 *         &lt;element name="id" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="estado" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="fechaAlta" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="fechaBaja" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="comentario" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="razonSocial" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="logoChico" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="logoGrande" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="footer" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="banner" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="tituloSistema" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="colorBanner" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="bannerIVR" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="bannerWap" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "mayorista", propOrder = {
    "id",
    "estado",
    "fechaAlta",
    "fechaBaja",
    "comentario",
    "razonSocial",
    "logoChico",
    "logoGrande",
    "footer",
    "banner",
    "tituloSistema",
    "colorBanner",
    "bannerIVR",
    "bannerWap"
})
public class Mayorista
    extends BaseModel
{

    protected Long id;
    protected String estado;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar fechaAlta;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar fechaBaja;
    protected String comentario;
    protected String razonSocial;
    protected String logoChico;
    protected String logoGrande;
    protected String footer;
    protected String banner;
    protected String tituloSistema;
    protected String colorBanner;
    protected String bannerIVR;
    protected String bannerWap;

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
     * Gets the value of the logoGrande property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLogoGrande() {
        return logoGrande;
    }

    /**
     * Sets the value of the logoGrande property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLogoGrande(String value) {
        this.logoGrande = value;
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
     * Gets the value of the banner property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBanner() {
        return banner;
    }

    /**
     * Sets the value of the banner property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBanner(String value) {
        this.banner = value;
    }

    /**
     * Gets the value of the tituloSistema property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTituloSistema() {
        return tituloSistema;
    }

    /**
     * Sets the value of the tituloSistema property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTituloSistema(String value) {
        this.tituloSistema = value;
    }

    /**
     * Gets the value of the colorBanner property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getColorBanner() {
        return colorBanner;
    }

    /**
     * Sets the value of the colorBanner property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setColorBanner(String value) {
        this.colorBanner = value;
    }

    /**
     * Gets the value of the bannerIVR property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBannerIVR() {
        return bannerIVR;
    }

    /**
     * Sets the value of the bannerIVR property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBannerIVR(String value) {
        this.bannerIVR = value;
    }

    /**
     * Gets the value of the bannerWap property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBannerWap() {
        return bannerWap;
    }

    /**
     * Sets the value of the bannerWap property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBannerWap(String value) {
        this.bannerWap = value;
    }

}
