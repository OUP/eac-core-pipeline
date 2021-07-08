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
 * <p>Java class for adminCustomer complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="adminCustomer">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="accountCreated" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="accountLastModified" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="addresses" type="{}adminAddress" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="birthDate" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="custom1" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="custom2" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="custom3" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="custom4" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="custom5" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="defaultAddr" type="{}adminAddress" minOccurs="0"/>
 *         &lt;element name="defaultAddrId" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="emailAddr" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="enabled" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="faxNumber" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="firstName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="gender" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="globalProdNotifier" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="groupId" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="id" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="invisible" type="{http://www.w3.org/2001/XMLSchema}byte"/>
 *         &lt;element name="lastLogon" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="lastName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="locale" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="newsletter" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="numberOfLogons" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="orders" type="{}adminOrder" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="password" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="prodNotifications" type="{}adminProductNotification" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="productNotifications" type="{}adminProduct" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="promotionMaxUse" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="promotionTimesUsed" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="roles" type="{}adminRole" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="telephoneNumber" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="telephoneNumber1" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="type" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "adminCustomer", propOrder = {
    "accountCreated",
    "accountLastModified",
    "addresses",
    "birthDate",
    "custom1",
    "custom2",
    "custom3",
    "custom4",
    "custom5",
    "defaultAddr",
    "defaultAddrId",
    "emailAddr",
    "enabled",
    "faxNumber",
    "firstName",
    "gender",
    "globalProdNotifier",
    "groupId",
    "id",
    "invisible",
    "lastLogon",
    "lastName",
    "locale",
    "newsletter",
    "numberOfLogons",
    "orders",
    "password",
    "prodNotifications",
    "productNotifications",
    "promotionMaxUse",
    "promotionTimesUsed",
    "roles",
    "telephoneNumber",
    "telephoneNumber1",
    "type"
})
public class AdminCustomer {

    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar accountCreated;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar accountLastModified;
    @XmlElement(nillable = true)
    protected List<AdminAddress> addresses;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar birthDate;
    protected String custom1;
    protected String custom2;
    protected String custom3;
    protected String custom4;
    protected String custom5;
    protected AdminAddress defaultAddr;
    protected int defaultAddrId;
    protected String emailAddr;
    protected boolean enabled;
    protected String faxNumber;
    protected String firstName;
    protected String gender;
    protected int globalProdNotifier;
    protected int groupId;
    protected int id;
    protected byte invisible;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar lastLogon;
    protected String lastName;
    protected String locale;
    protected String newsletter;
    protected int numberOfLogons;
    @XmlElement(nillable = true)
    protected List<AdminOrder> orders;
    protected String password;
    @XmlElement(nillable = true)
    protected List<AdminProductNotification> prodNotifications;
    @XmlElement(nillable = true)
    protected List<AdminProduct> productNotifications;
    protected int promotionMaxUse;
    protected int promotionTimesUsed;
    @XmlElement(nillable = true)
    protected List<AdminRole> roles;
    protected String telephoneNumber;
    protected String telephoneNumber1;
    protected int type;

    /**
     * Gets the value of the accountCreated property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getAccountCreated() {
        return accountCreated;
    }

    /**
     * Sets the value of the accountCreated property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setAccountCreated(XMLGregorianCalendar value) {
        this.accountCreated = value;
    }

    /**
     * Gets the value of the accountLastModified property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getAccountLastModified() {
        return accountLastModified;
    }

    /**
     * Sets the value of the accountLastModified property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setAccountLastModified(XMLGregorianCalendar value) {
        this.accountLastModified = value;
    }

    /**
     * Gets the value of the addresses property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the addresses property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAddresses().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link AdminAddress }
     * 
     * 
     */
    public List<AdminAddress> getAddresses() {
        if (addresses == null) {
            addresses = new ArrayList<AdminAddress>();
        }
        return this.addresses;
    }

    /**
     * Gets the value of the birthDate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getBirthDate() {
        return birthDate;
    }

    /**
     * Sets the value of the birthDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setBirthDate(XMLGregorianCalendar value) {
        this.birthDate = value;
    }

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
     * Gets the value of the defaultAddr property.
     * 
     * @return
     *     possible object is
     *     {@link AdminAddress }
     *     
     */
    public AdminAddress getDefaultAddr() {
        return defaultAddr;
    }

    /**
     * Sets the value of the defaultAddr property.
     * 
     * @param value
     *     allowed object is
     *     {@link AdminAddress }
     *     
     */
    public void setDefaultAddr(AdminAddress value) {
        this.defaultAddr = value;
    }

    /**
     * Gets the value of the defaultAddrId property.
     * 
     */
    public int getDefaultAddrId() {
        return defaultAddrId;
    }

    /**
     * Sets the value of the defaultAddrId property.
     * 
     */
    public void setDefaultAddrId(int value) {
        this.defaultAddrId = value;
    }

    /**
     * Gets the value of the emailAddr property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEmailAddr() {
        return emailAddr;
    }

    /**
     * Sets the value of the emailAddr property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEmailAddr(String value) {
        this.emailAddr = value;
    }

    /**
     * Gets the value of the enabled property.
     * 
     */
    public boolean isEnabled() {
        return enabled;
    }

    /**
     * Sets the value of the enabled property.
     * 
     */
    public void setEnabled(boolean value) {
        this.enabled = value;
    }

    /**
     * Gets the value of the faxNumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFaxNumber() {
        return faxNumber;
    }

    /**
     * Sets the value of the faxNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFaxNumber(String value) {
        this.faxNumber = value;
    }

    /**
     * Gets the value of the firstName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Sets the value of the firstName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFirstName(String value) {
        this.firstName = value;
    }

    /**
     * Gets the value of the gender property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGender() {
        return gender;
    }

    /**
     * Sets the value of the gender property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGender(String value) {
        this.gender = value;
    }

    /**
     * Gets the value of the globalProdNotifier property.
     * 
     */
    public int getGlobalProdNotifier() {
        return globalProdNotifier;
    }

    /**
     * Sets the value of the globalProdNotifier property.
     * 
     */
    public void setGlobalProdNotifier(int value) {
        this.globalProdNotifier = value;
    }

    /**
     * Gets the value of the groupId property.
     * 
     */
    public int getGroupId() {
        return groupId;
    }

    /**
     * Sets the value of the groupId property.
     * 
     */
    public void setGroupId(int value) {
        this.groupId = value;
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
     * Gets the value of the invisible property.
     * 
     */
    public byte getInvisible() {
        return invisible;
    }

    /**
     * Sets the value of the invisible property.
     * 
     */
    public void setInvisible(byte value) {
        this.invisible = value;
    }

    /**
     * Gets the value of the lastLogon property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getLastLogon() {
        return lastLogon;
    }

    /**
     * Sets the value of the lastLogon property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setLastLogon(XMLGregorianCalendar value) {
        this.lastLogon = value;
    }

    /**
     * Gets the value of the lastName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Sets the value of the lastName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLastName(String value) {
        this.lastName = value;
    }

    /**
     * Gets the value of the locale property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLocale() {
        return locale;
    }

    /**
     * Sets the value of the locale property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLocale(String value) {
        this.locale = value;
    }

    /**
     * Gets the value of the newsletter property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNewsletter() {
        return newsletter;
    }

    /**
     * Sets the value of the newsletter property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNewsletter(String value) {
        this.newsletter = value;
    }

    /**
     * Gets the value of the numberOfLogons property.
     * 
     */
    public int getNumberOfLogons() {
        return numberOfLogons;
    }

    /**
     * Sets the value of the numberOfLogons property.
     * 
     */
    public void setNumberOfLogons(int value) {
        this.numberOfLogons = value;
    }

    /**
     * Gets the value of the orders property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the orders property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getOrders().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link AdminOrder }
     * 
     * 
     */
    public List<AdminOrder> getOrders() {
        if (orders == null) {
            orders = new ArrayList<AdminOrder>();
        }
        return this.orders;
    }

    /**
     * Gets the value of the password property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the value of the password property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPassword(String value) {
        this.password = value;
    }

    /**
     * Gets the value of the prodNotifications property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the prodNotifications property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getProdNotifications().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link AdminProductNotification }
     * 
     * 
     */
    public List<AdminProductNotification> getProdNotifications() {
        if (prodNotifications == null) {
            prodNotifications = new ArrayList<AdminProductNotification>();
        }
        return this.prodNotifications;
    }

    /**
     * Gets the value of the productNotifications property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the productNotifications property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getProductNotifications().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link AdminProduct }
     * 
     * 
     */
    public List<AdminProduct> getProductNotifications() {
        if (productNotifications == null) {
            productNotifications = new ArrayList<AdminProduct>();
        }
        return this.productNotifications;
    }

    /**
     * Gets the value of the promotionMaxUse property.
     * 
     */
    public int getPromotionMaxUse() {
        return promotionMaxUse;
    }

    /**
     * Sets the value of the promotionMaxUse property.
     * 
     */
    public void setPromotionMaxUse(int value) {
        this.promotionMaxUse = value;
    }

    /**
     * Gets the value of the promotionTimesUsed property.
     * 
     */
    public int getPromotionTimesUsed() {
        return promotionTimesUsed;
    }

    /**
     * Sets the value of the promotionTimesUsed property.
     * 
     */
    public void setPromotionTimesUsed(int value) {
        this.promotionTimesUsed = value;
    }

    /**
     * Gets the value of the roles property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the roles property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getRoles().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link AdminRole }
     * 
     * 
     */
    public List<AdminRole> getRoles() {
        if (roles == null) {
            roles = new ArrayList<AdminRole>();
        }
        return this.roles;
    }

    /**
     * Gets the value of the telephoneNumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTelephoneNumber() {
        return telephoneNumber;
    }

    /**
     * Sets the value of the telephoneNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTelephoneNumber(String value) {
        this.telephoneNumber = value;
    }

    /**
     * Gets the value of the telephoneNumber1 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTelephoneNumber1() {
        return telephoneNumber1;
    }

    /**
     * Sets the value of the telephoneNumber1 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTelephoneNumber1(String value) {
        this.telephoneNumber1 = value;
    }

    /**
     * Gets the value of the type property.
     * 
     */
    public int getType() {
        return type;
    }

    /**
     * Sets the value of the type property.
     * 
     */
    public void setType(int value) {
        this.type = value;
    }

}
