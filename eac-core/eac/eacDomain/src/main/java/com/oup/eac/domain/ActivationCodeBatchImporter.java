package com.oup.eac.domain;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Type;
import org.joda.time.DateTime;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name="activation_code_batch_importer")
public class ActivationCodeBatchImporter extends UpdatedAudit
{
    @Column(nullable = true, name="code")
    private String code;
    
    @Column(nullable = true, name="code_batch_name")
    private String codeBatchName;
    
    @Column(nullable = true, name="activation_code_format")
    private String activationCodeFormat;
    
    @Column(nullable = true, name="allowed_usage")
    private Integer allowedUsage;
    
    @Column(nullable = true, name="actual_usage")
    private Integer actualUsage;
    
    @Type(type = "org.joda.time.contrib.hibernate.PersistentDateTime")
    @DateTimeFormat (pattern="dd/MM/yyyy")
    @Column(nullable = true, name="token_validity_start_date")
    private DateTime tokenValidityStartDate;
    
    @Type(type = "org.joda.time.contrib.hibernate.PersistentDateTime")
    @DateTimeFormat (pattern="dd/MM/yyyy")
    @Column(nullable = true, name="token_validity_end_date")
    private DateTime tokenValidityEndDate;
    
    @Column(nullable = true, name="product_name")
    private String productName;
    
    @Column(nullable = true, name="licence_type")
    private String licenceType;
    
    @Type(type = "org.joda.time.contrib.hibernate.PersistentDateTime")
    @DateTimeFormat (pattern="dd/MM/yyyy")
    @Column(nullable = true, name="start_date")
    private DateTime startDate; 
    
    @Type(type = "org.joda.time.contrib.hibernate.PersistentDateTime")
    @DateTimeFormat (pattern="dd/MM/yyyy")
    @Column(nullable = true, name="end_date")
    private DateTime endDate; 
    
    @Column(nullable = true, name="total_concurrency")
    private Integer totalConcurrency;
    
    @Column(nullable = true, name="user_concurrency")
    private Integer userConcurrency;
    
    @Column(nullable = true, name="allowed_usages")
    private Integer allowedUsages;
    
    @Column(nullable = true, name="time_period")
    private Integer timePeriod;
    
    @Column(nullable = true, name="unit_type")
    private String unitType;
    
    @Column(nullable = true, name="begin_on")
    private String beginOn;
    
    @OneToOne(mappedBy="activationCodeBatchImporter", cascade = CascadeType.ALL)
    private ActivationCodeBatchImporterStatus activationCodeBatchImporterStatus;

    /**
     * @return the code
     */
    public String getCode() {
        return code;
    }

    /**
     * @param code the code to set
     */
    public void setCode(final String code) {
        this.code = code;
    }

    /**
     * @return the codeBatchName
     */
    public String getCodeBatchName() {
        return codeBatchName;
    }

    /**
     * @param codeBatchName the codeBatchName to set
     */
    public void setCodeBatchName(final String codeBatchName) {
        this.codeBatchName = codeBatchName;
    }

    /**
     * @return the activationCodeFormat
     */
    public String getActivationCodeFormat() {
        return activationCodeFormat;
    }

    /**
     * @param activationCodeFormat the activationCodeFormat to set
     */
    public void setActivationCodeFormat(final String activationCodeFormat) {
        this.activationCodeFormat = activationCodeFormat;
    }

    /**
     * @return the allowedUsage
     */
    public Integer getAllowedUsage() {
        return allowedUsage;
    }

    /**
     * @param allowedUsage the allowedUsage to set
     */
    public void setAllowedUsage(final Integer allowedUsage) {
        this.allowedUsage = allowedUsage;
    }

    /**
     * @return the actualUsage
     */
    public Integer getActualUsage() {
        return actualUsage;
    }

    /**
     * @param actualUsage the actualUsage to set
     */
    public void setActualUsage(final Integer actualUsage) {
        this.actualUsage = actualUsage;
    }

    /**
     * @return the tokenValidityStartDate
     */
    public DateTime getTokenValidityStartDate() {
        return tokenValidityStartDate;
    }

    /**
     * @param tokenValidityStartDate the tokenValidityStartDate to set
     */
    public void setTokenValidityStartDate(final DateTime tokenValidityStartDate) {
        this.tokenValidityStartDate = tokenValidityStartDate;
    }

    /**
     * @return the tokenValidityEndDate
     */
    public DateTime getTokenValidityEndDate() {
        return tokenValidityEndDate;
    }

    /**
     * @param tokenValidityEndDate the tokenValidityEndDate to set
     */
    public void setTokenValidityEndDate(final DateTime tokenValidityEndDate) {
        this.tokenValidityEndDate = tokenValidityEndDate;
    }

    /**
     * @return the productName
     */
    public String getProductName() {
        return productName;
    }

    /**
     * @param productName the productName to set
     */
    public void setProductName(final String productName) {
        this.productName = productName;
    }

    /**
     * @return the licenceType
     */
    public String getLicenceType() {
        return licenceType;
    }

    /**
     * @param licenceType the licenceType to set
     */
    public void setLicenceType(final String licenceType) {
        this.licenceType = licenceType;
    }

    /**
     * @return the startDate
     */
    public DateTime getStartDate() {
        return startDate;
    }

    /**
     * @param startDate the startDate to set
     */
    public void setStartDate(final DateTime startDate) {
        this.startDate = startDate;
    }

    /**
     * @return the endDate
     */
    public DateTime getEndDate() {
        return endDate;
    }

    /**
     * @param endDate the endDate to set
     */
    public void setEndDate(final DateTime endDate) {
        this.endDate = endDate;
    }

    /**
     * @return the totalConcurrency
     */
    public Integer getTotalConcurrency() {
        return totalConcurrency;
    }

    /**
     * @param totalConcurrency the totalConcurrency to set
     */
    public void setTotalConcurrency(final Integer totalConcurrency) {
        this.totalConcurrency = totalConcurrency;
    }

    /**
     * @return the userConcurrency
     */
    public Integer getUserConcurrency() {
        return userConcurrency;
    }

    /**
     * @param userConcurrency the userConcurrency to set
     */
    public void setUserConcurrency(final Integer userConcurrency) {
        this.userConcurrency = userConcurrency;
    }

    /**
     * @return the allowedUsages
     */
    public Integer getAllowedUsages() {
        return allowedUsages;
    }

    /**
     * @param allowedUsages the allowedUsages to set
     */
    public void setAllowedUsages(final Integer allowedUsages) {
        this.allowedUsages = allowedUsages;
    }

    /**
     * @return the timePeriod
     */
    public Integer getTimePeriod() {
        return timePeriod;
    }

    /**
     * @param timePeriod the timePeriod to set
     */
    public void setTimePeriod(final Integer timePeriod) {
        this.timePeriod = timePeriod;
    }

    /**
     * @return the unitType
     */
    public String getUnitType() {
        return unitType;
    }

    /**
     * @param unitType the unitType to set
     */
    public void setUnitType(final String unitType) {
        this.unitType = unitType;
    }

    /**
     * @return the beginOn
     */
    public String getBeginOn() {
        return beginOn;
    }

    /**
     * @param beginOn the beginOn to set
     */
    public void setBeginOn(final String beginOn) {
        this.beginOn = beginOn;
    }

    /**
     * @return the activationCodeBatchImporterStatus
     */
    public ActivationCodeBatchImporterStatus getActivationCodeBatchImporterStatus() {
        return activationCodeBatchImporterStatus;
    }

    /**
     * @param activationCodeBatchImporterStatus the activationCodeBatchImporterStatus to set
     */
    public void setActivationCodeBatchImporterStatus(
            ActivationCodeBatchImporterStatus activationCodeBatchImporterStatus) {
        this.activationCodeBatchImporterStatus = activationCodeBatchImporterStatus;
    }

}
