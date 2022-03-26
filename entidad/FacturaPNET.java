
package com.americacg.cargavirtual.gcsi.entidad;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for facturaPNET complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="facturaPNET">
 *   &lt;complexContent>
 *     &lt;extension base="{com.americacg.cargavirtual.gcsi.entidad}factura">
 *       &lt;sequence>
 *         &lt;element name="numeroUnicoSiris" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="identificadorServicio" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="identificador1" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="identificador2" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="identificador3" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="identificadorMarca" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="identificadorBuscaRepetida" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="secuencia" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="ente" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="subEnte" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="transaccionSiris" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="numeroTarjeta" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="numeroCuponTarjeta" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="transaccionPrisma" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "facturaPNET", propOrder = {
    "numeroUnicoSiris",
    "identificadorServicio",
    "identificador1",
    "identificador2",
    "identificador3",
    "identificadorMarca",
    "identificadorBuscaRepetida",
    "secuencia",
    "ente",
    "subEnte",
    "transaccionSiris",
    "numeroTarjeta",
    "numeroCuponTarjeta",
    "transaccionPrisma"
})
public class FacturaPNET
    extends Factura
{

    protected Long numeroUnicoSiris;
    protected String identificadorServicio;
    protected String identificador1;
    protected String identificador2;
    protected String identificador3;
    protected String identificadorMarca;
    protected String identificadorBuscaRepetida;
    protected Long secuencia;
    protected String ente;
    protected String subEnte;
    protected String transaccionSiris;
    protected String numeroTarjeta;
    protected String numeroCuponTarjeta;
    protected String transaccionPrisma;

    /**
     * Gets the value of the numeroUnicoSiris property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getNumeroUnicoSiris() {
        return numeroUnicoSiris;
    }

    /**
     * Sets the value of the numeroUnicoSiris property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setNumeroUnicoSiris(Long value) {
        this.numeroUnicoSiris = value;
    }

    /**
     * Gets the value of the identificadorServicio property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdentificadorServicio() {
        return identificadorServicio;
    }

    /**
     * Sets the value of the identificadorServicio property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdentificadorServicio(String value) {
        this.identificadorServicio = value;
    }

    /**
     * Gets the value of the identificador1 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdentificador1() {
        return identificador1;
    }

    /**
     * Sets the value of the identificador1 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdentificador1(String value) {
        this.identificador1 = value;
    }

    /**
     * Gets the value of the identificador2 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdentificador2() {
        return identificador2;
    }

    /**
     * Sets the value of the identificador2 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdentificador2(String value) {
        this.identificador2 = value;
    }

    /**
     * Gets the value of the identificador3 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdentificador3() {
        return identificador3;
    }

    /**
     * Sets the value of the identificador3 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdentificador3(String value) {
        this.identificador3 = value;
    }

    /**
     * Gets the value of the identificadorMarca property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdentificadorMarca() {
        return identificadorMarca;
    }

    /**
     * Sets the value of the identificadorMarca property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdentificadorMarca(String value) {
        this.identificadorMarca = value;
    }

    /**
     * Gets the value of the identificadorBuscaRepetida property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdentificadorBuscaRepetida() {
        return identificadorBuscaRepetida;
    }

    /**
     * Sets the value of the identificadorBuscaRepetida property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdentificadorBuscaRepetida(String value) {
        this.identificadorBuscaRepetida = value;
    }

    /**
     * Gets the value of the secuencia property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getSecuencia() {
        return secuencia;
    }

    /**
     * Sets the value of the secuencia property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setSecuencia(Long value) {
        this.secuencia = value;
    }

    /**
     * Gets the value of the ente property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEnte() {
        return ente;
    }

    /**
     * Sets the value of the ente property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEnte(String value) {
        this.ente = value;
    }

    /**
     * Gets the value of the subEnte property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSubEnte() {
        return subEnte;
    }

    /**
     * Sets the value of the subEnte property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSubEnte(String value) {
        this.subEnte = value;
    }

    /**
     * Gets the value of the transaccionSiris property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTransaccionSiris() {
        return transaccionSiris;
    }

    /**
     * Sets the value of the transaccionSiris property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTransaccionSiris(String value) {
        this.transaccionSiris = value;
    }

    /**
     * Gets the value of the numeroTarjeta property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNumeroTarjeta() {
        return numeroTarjeta;
    }

    /**
     * Sets the value of the numeroTarjeta property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNumeroTarjeta(String value) {
        this.numeroTarjeta = value;
    }

    /**
     * Gets the value of the numeroCuponTarjeta property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNumeroCuponTarjeta() {
        return numeroCuponTarjeta;
    }

    /**
     * Sets the value of the numeroCuponTarjeta property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNumeroCuponTarjeta(String value) {
        this.numeroCuponTarjeta = value;
    }

    /**
     * Gets the value of the transaccionPrisma property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTransaccionPrisma() {
        return transaccionPrisma;
    }

    /**
     * Sets the value of the transaccionPrisma property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTransaccionPrisma(String value) {
        this.transaccionPrisma = value;
    }

}
