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
 * <p>Java class for adminManufacturerInfo complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="adminManufacturerInfo">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="languageId" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="lastClick" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="manufacturerId" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="url" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="urlClicked" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "adminManufacturerInfo", propOrder = {
    "languageId",
    "lastClick",
    "manufacturerId",
    "url",
    "urlClicked"
})
public class AdminManufacturerInfo {

    protected int languageId;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar lastClick;
    protected int manufacturerId;
    protected String url;
    protected int urlClicked;

    /**
     * Gets the value of the languageId property.
     * 
     */
    public int getLanguageId() {
        return languageId;
    }

    /**
     * Sets the value of the languageId property.
     * 
     */
    public void setLanguageId(int value) {
        this.languageId = value;
    }

    /**
     * Gets the value of the lastClick property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getLastClick() {
        return lastClick;
    }

    /**
     * Sets the value of the lastClick property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setLastClick(XMLGregorianCalendar value) {
        this.lastClick = value;
    }

    /**
     * Gets the value of the manufacturerId property.
     * 
     */
    public int getManufacturerId() {
        return manufacturerId;
    }

    /**
     * Sets the value of the manufacturerId property.
     * 
     */
    public void setManufacturerId(int value) {
        this.manufacturerId = value;
    }

    /**
     * Gets the value of the url property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUrl() {
        return url;
    }

    /**
     * Sets the value of the url property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUrl(String value) {
        this.url = value;
    }

    /**
     * Gets the value of the urlClicked property.
     * 
     */
    public int getUrlClicked() {
        return urlClicked;
    }

    /**
     * Sets the value of the urlClicked property.
     * 
     */
    public void setUrlClicked(int value) {
        this.urlClicked = value;
    }

}
