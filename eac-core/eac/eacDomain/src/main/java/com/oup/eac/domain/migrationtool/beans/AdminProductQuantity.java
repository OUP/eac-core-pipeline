//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2013.04.29 at 05:07:04 PM BST 
//


package com.oup.eac.domain.migrationtool.beans;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for adminProductQuantity complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="adminProductQuantity">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="dateAvailable" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="encodedOptionValues" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="extQuantity" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="productId" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="quantity" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="sku" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "adminProductQuantity", propOrder = {
    "dateAvailable",
    "encodedOptionValues",
    "extQuantity",
    "productId",
    "quantity",
    "sku"
})
public class AdminProductQuantity {

    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar dateAvailable;
    protected String encodedOptionValues;
    protected Integer extQuantity;
    protected int productId;
    protected int quantity;
    protected String sku;

    /**
     * Gets the value of the dateAvailable property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDateAvailable() {
        return dateAvailable;
    }

    /**
     * Sets the value of the dateAvailable property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDateAvailable(XMLGregorianCalendar value) {
        this.dateAvailable = value;
    }

    /**
     * Gets the value of the encodedOptionValues property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEncodedOptionValues() {
        return encodedOptionValues;
    }

    /**
     * Sets the value of the encodedOptionValues property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEncodedOptionValues(String value) {
        this.encodedOptionValues = value;
    }

    /**
     * Gets the value of the extQuantity property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getExtQuantity() {
        return extQuantity;
    }

    /**
     * Sets the value of the extQuantity property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setExtQuantity(Integer value) {
        this.extQuantity = value;
    }

    /**
     * Gets the value of the productId property.
     * 
     */
    public int getProductId() {
        return productId;
    }

    /**
     * Sets the value of the productId property.
     * 
     */
    public void setProductId(int value) {
        this.productId = value;
    }

    /**
     * Gets the value of the quantity property.
     * 
     */
    public int getQuantity() {
        return quantity;
    }

    /**
     * Sets the value of the quantity property.
     * 
     */
    public void setQuantity(int value) {
        this.quantity = value;
    }

    /**
     * Gets the value of the sku property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSku() {
        return sku;
    }

    /**
     * Sets the value of the sku property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSku(String value) {
        this.sku = value;
    }

}