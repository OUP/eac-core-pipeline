//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2013.04.29 at 05:07:04 PM BST 
//


package com.oup.eac.domain.migrationtool.beans;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for adminAddressFormat complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="adminAddressFormat">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="addressFormat" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="addressFormatId" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="addressSummary" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="id" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "adminAddressFormat", propOrder = {
    "addressFormat",
    "addressFormatId",
    "addressSummary",
    "id"
})
public class AdminAddressFormat {

    protected String addressFormat;
    protected int addressFormatId;
    protected String addressSummary;
    protected int id;

    /**
     * Gets the value of the addressFormat property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAddressFormat() {
        return addressFormat;
    }

    /**
     * Sets the value of the addressFormat property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAddressFormat(String value) {
        this.addressFormat = value;
    }

    /**
     * Gets the value of the addressFormatId property.
     * 
     */
    public int getAddressFormatId() {
        return addressFormatId;
    }

    /**
     * Sets the value of the addressFormatId property.
     * 
     */
    public void setAddressFormatId(int value) {
        this.addressFormatId = value;
    }

    /**
     * Gets the value of the addressSummary property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAddressSummary() {
        return addressSummary;
    }

    /**
     * Sets the value of the addressSummary property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAddressSummary(String value) {
        this.addressSummary = value;
    }

    /**
     * Gets the value of the id property.
     * 
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the value of the id property.
     * 
     */
    public void setId(int value) {
        this.id = value;
    }

}