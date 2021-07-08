//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2013.04.29 at 05:07:04 PM BST 
//


package com.oup.eac.domain.migrationtool.beans;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for adminOrderReturn complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="adminOrderReturn">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="dateAdded" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="id" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="lastModified" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="order" type="{}adminOrder" minOccurs="0"/>
 *         &lt;element name="orderId" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="orderNumber" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="retOrderProducts" type="{}adminOrderReturnProduct" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="returnReason" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="rmaCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "adminOrderReturn", propOrder = {
    "dateAdded",
    "id",
    "lastModified",
    "order",
    "orderId",
    "orderNumber",
    "retOrderProducts",
    "returnReason",
    "rmaCode"
})
public class AdminOrderReturn {

    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar dateAdded;
    protected int id;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar lastModified;
    protected AdminOrder order;
    protected int orderId;
    protected String orderNumber;
    @XmlElement(nillable = true)
    protected List<AdminOrderReturnProduct> retOrderProducts;
    protected String returnReason;
    protected String rmaCode;

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
     * Gets the value of the lastModified property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getLastModified() {
        return lastModified;
    }

    /**
     * Sets the value of the lastModified property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setLastModified(XMLGregorianCalendar value) {
        this.lastModified = value;
    }

    /**
     * Gets the value of the order property.
     * 
     * @return
     *     possible object is
     *     {@link AdminOrder }
     *     
     */
    public AdminOrder getOrder() {
        return order;
    }

    /**
     * Sets the value of the order property.
     * 
     * @param value
     *     allowed object is
     *     {@link AdminOrder }
     *     
     */
    public void setOrder(AdminOrder value) {
        this.order = value;
    }

    /**
     * Gets the value of the orderId property.
     * 
     */
    public int getOrderId() {
        return orderId;
    }

    /**
     * Sets the value of the orderId property.
     * 
     */
    public void setOrderId(int value) {
        this.orderId = value;
    }

    /**
     * Gets the value of the orderNumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOrderNumber() {
        return orderNumber;
    }

    /**
     * Sets the value of the orderNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOrderNumber(String value) {
        this.orderNumber = value;
    }

    /**
     * Gets the value of the retOrderProducts property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the retOrderProducts property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getRetOrderProducts().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link AdminOrderReturnProduct }
     * 
     * 
     */
    public List<AdminOrderReturnProduct> getRetOrderProducts() {
        if (retOrderProducts == null) {
            retOrderProducts = new ArrayList<AdminOrderReturnProduct>();
        }
        return this.retOrderProducts;
    }

    /**
     * Gets the value of the returnReason property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getReturnReason() {
        return returnReason;
    }

    /**
     * Sets the value of the returnReason property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setReturnReason(String value) {
        this.returnReason = value;
    }

    /**
     * Gets the value of the rmaCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRmaCode() {
        return rmaCode;
    }

    /**
     * Sets the value of the rmaCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRmaCode(String value) {
        this.rmaCode = value;
    }

}
