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
 * <p>Java class for adminPaymentSchedule complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="adminPaymentSchedule">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="custom1" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="custom2" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="custom3" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="custom4" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="custom5" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="dateAdded" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="dayOfMonth" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="description" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="id" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="name" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="numPayments" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="numTrialPayments" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="timeLength" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="timeUnit" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "adminPaymentSchedule", propOrder = {
    "custom1",
    "custom2",
    "custom3",
    "custom4",
    "custom5",
    "dateAdded",
    "dayOfMonth",
    "description",
    "id",
    "name",
    "numPayments",
    "numTrialPayments",
    "timeLength",
    "timeUnit"
})
public class AdminPaymentSchedule {

    protected String custom1;
    protected String custom2;
    protected String custom3;
    protected String custom4;
    protected String custom5;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar dateAdded;
    protected int dayOfMonth;
    protected String description;
    protected int id;
    protected String name;
    protected int numPayments;
    protected int numTrialPayments;
    protected int timeLength;
    protected int timeUnit;

    /**
     * Gets the value of the custom1 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCustom1() {
        return custom1;
    }

    /**
     * Sets the value of the custom1 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCustom1(String value) {
        this.custom1 = value;
    }

    /**
     * Gets the value of the custom2 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCustom2() {
        return custom2;
    }

    /**
     * Sets the value of the custom2 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCustom2(String value) {
        this.custom2 = value;
    }

    /**
     * Gets the value of the custom3 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCustom3() {
        return custom3;
    }

    /**
     * Sets the value of the custom3 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCustom3(String value) {
        this.custom3 = value;
    }

    /**
     * Gets the value of the custom4 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCustom4() {
        return custom4;
    }

    /**
     * Sets the value of the custom4 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCustom4(String value) {
        this.custom4 = value;
    }

    /**
     * Gets the value of the custom5 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCustom5() {
        return custom5;
    }

    /**
     * Sets the value of the custom5 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCustom5(String value) {
        this.custom5 = value;
    }

    /**
     * Gets the value of the dateAdded property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDateAdded() {
        return dateAdded;
    }

    /**
     * Sets the value of the dateAdded property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDateAdded(XMLGregorianCalendar value) {
        this.dateAdded = value;
    }

    /**
     * Gets the value of the dayOfMonth property.
     * 
     */
    public int getDayOfMonth() {
        return dayOfMonth;
    }

    /**
     * Sets the value of the dayOfMonth property.
     * 
     */
    public void setDayOfMonth(int value) {
        this.dayOfMonth = value;
    }

    /**
     * Gets the value of the description property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the value of the description property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescription(String value) {
        this.description = value;
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

    /**
     * Gets the value of the name property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the value of the name property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setName(String value) {
        this.name = value;
    }

    /**
     * Gets the value of the numPayments property.
     * 
     */
    public int getNumPayments() {
        return numPayments;
    }

    /**
     * Sets the value of the numPayments property.
     * 
     */
    public void setNumPayments(int value) {
        this.numPayments = value;
    }

    /**
     * Gets the value of the numTrialPayments property.
     * 
     */
    public int getNumTrialPayments() {
        return numTrialPayments;
    }

    /**
     * Sets the value of the numTrialPayments property.
     * 
     */
    public void setNumTrialPayments(int value) {
        this.numTrialPayments = value;
    }

    /**
     * Gets the value of the timeLength property.
     * 
     */
    public int getTimeLength() {
        return timeLength;
    }

    /**
     * Sets the value of the timeLength property.
     * 
     */
    public void setTimeLength(int value) {
        this.timeLength = value;
    }

    /**
     * Gets the value of the timeUnit property.
     * 
     */
    public int getTimeUnit() {
        return timeUnit;
    }

    /**
     * Sets the value of the timeUnit property.
     * 
     */
    public void setTimeUnit(int value) {
        this.timeUnit = value;
    }

}
