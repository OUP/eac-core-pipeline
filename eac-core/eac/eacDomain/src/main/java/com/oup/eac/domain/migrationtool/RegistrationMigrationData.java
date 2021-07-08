package com.oup.eac.domain.migrationtool;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import com.oup.eac.domain.BaseDomainObject;
import com.oup.eac.domain.ProductRegistrationDefinition;

@Entity
public class RegistrationMigrationData extends BaseDomainObject{

    
    
	public enum Status {
        CURRENT,
        EXTRA,
        ADMIN,
        MIGRATE,
        CHILD,
        NO_REG_DEF
    }
    
    @ManyToOne(cascade = {CascadeType.ALL}, fetch=FetchType.LAZY)
    @JoinColumn(name = "customer_migration_data_id", nullable = true)    
    private CustomerMigrationData customerMigrationData;

    @Type(type = "org.joda.time.contrib.hibernate.PersistentDateTime")
    @Column(nullable = true, updatable = true, unique=false)
    private DateTime createdDate;

  
   
    
    @Column
    private String institutionName; 
    
    @Column
    private String departmentName; 

    @Column
    private String jobTitle; 

    @Column
    private String marketing; 

    @Column(name="address_line_2")
    private String addressLine2;
    
    @Column(name="address_line_3")
    private String addressLine3;
    
    @Column(name="street")
    private String street;
    
    @Column(name="city")
    private String city;


    @Column(name="zip")
    private String zip;

    @Column(name="Country_code")
    private String CountryCode;

    @Column
    private String telephone;
    
    @Column
    private String moduleTitle;
    
    
    
    @Column
    private String numberOfStudents;
    
    @Column
    private String textBook;
    
    
    @Column
    private String referral;
    
    @Column
    private String referralOther;
    
    @Column
    private String enrolmentYear;

    @Column(nullable=false)
    private String productId;

    @Column
    private String productName;
    
    @Column
    private String productClassification;
    
    @Column
    private String productClassificationParameter;
    
    @Column(name="licence_status",nullable=true)
    private String licenceStatus;
    
    @Column(name="old_licence_id")
    private String oldOrcsLicenceId;

    @Type(type = "org.joda.time.contrib.hibernate.PersistentDateTime")
    @Column(nullable = true, updatable = false, unique=false, name="licence_start_date")
    private DateTime licenceStartDate;
    
    @Type(type = "org.joda.time.contrib.hibernate.PersistentDateTime")
    @Column(nullable = true, updatable = false, unique=false, name="licence_end_date")
    private DateTime licenceEndDate;
    
    @Enumerated(EnumType.STRING)
    @Column
    private Status status;

   /* @ManyToOne(cascade = {CascadeType.ALL}, fetch=FetchType.LAZY)
    @JoinColumn(name = "registration_definition_id", nullable = true)   */ 
    
    @ManyToOne(cascade = {CascadeType.ALL}, fetch=FetchType.LAZY)
    @JoinColumn(name = "registration_definition_id", nullable = true) 
    private ProductRegistrationDefinition productRegistrationDefinition;

    public CustomerMigrationData getCustomerMigrationData() {
        return customerMigrationData;
    }

    public void setCustomerMigrationData(CustomerMigrationData customerMigrationData) {
        this.customerMigrationData = customerMigrationData;
    }

    public DateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(DateTime createdDate) {
        this.createdDate = createdDate;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    
    public String getInstitutionName() {
        return institutionName;
    }

    public void setInstitutionName(String institutionName) {
        this.institutionName = institutionName;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getMarketing() {
        return marketing;
    }

    public void setMarketing(String marketing) {
        this.marketing = marketing;
    }

   

    public String getAddressLine2() {
        return addressLine2;
    }

    public void setAddressLine2(String addressLine2) {
        this.addressLine2 = addressLine2;
    }

    public String getZip() {
        return zip;
    }

    public void setAddressLine3(String zip) {
        this.zip = zip;
    }

    public String getCountryCode() {
        return CountryCode;
    }

    public void setCountryCode(String CountryCode) {
        this.CountryCode = CountryCode;
    }

  
    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getReferral() {
        return referral;
    }

    public void setReferral(String referral) {
        this.referral = referral;
    }

    public String getModuleTitle() {
        return moduleTitle;
    }

    public void setModuleTitle(String moduleTitle) {
        this.moduleTitle = moduleTitle;
    }



    public String getNumberOfStudents() {
        return numberOfStudents;
    }

    public void setNumberOfStudents(String numberOfStudents) {
        this.numberOfStudents = numberOfStudents;
    }

    public String getTextBook() {
        return textBook;
    }

    public void setTextBook(String textBook) {
        this.textBook = textBook;
    }

    public String getEnrolmentYear() {
        return enrolmentYear;
    }

    public void setEnrolmentYear(String enrolmentYear) {
        this.enrolmentYear = enrolmentYear;
    }

    public String isLicenceStatus() {
        return licenceStatus;
    }

    public void setLicenceStatus(String licenceStatus) {
        this.licenceStatus = licenceStatus;
    }

    public String getOldOrcsLicenceId() {
        return oldOrcsLicenceId;
    }

    public void setOldOrcsLicenceId(String oldOrcsLicenceId) {
        this.oldOrcsLicenceId = oldOrcsLicenceId;
    }

    public DateTime getLicenceStartDate() {
        return licenceStartDate;
    }

    public void setLicenceStartDate(DateTime licenceStartDate) {
        this.licenceStartDate = licenceStartDate;
    }

    public DateTime getLicenceEndDate() {
        return licenceEndDate;
    }

    public void setLicenceEndDate(DateTime licenceEndDate) {
        this.licenceEndDate = licenceEndDate;
    }

    

    public String getLicenceStatus() {
        return licenceStatus;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public ProductRegistrationDefinition getProductRegistrationDefinition() {
        return productRegistrationDefinition;
    }

    public void setProductRegistrationDefinition(ProductRegistrationDefinition productRegistrationDefinition) {
        this.productRegistrationDefinition = productRegistrationDefinition;
    }

    public String getReferralOther() {
        return referralOther;
    }

    public void setReferralOther(String referralOther) {
        this.referralOther = referralOther;
    }

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public void setZip(String zip) {
		this.zip = zip;
	}

	public String getProductClassification() {
		return productClassification;
	}

	public void setProductClassification(String productClassification) {
		this.productClassification = productClassification;
	}


    
}
