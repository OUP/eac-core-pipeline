package com.oup.eac.dto;

import java.io.Serializable;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import com.oup.eac.domain.RollingLicenceTemplate.RollingBeginOn;
import com.oup.eac.domain.RollingLicenceTemplate.RollingUnitType;

public class ActivationCodeDto implements Serializable {

    private String batchId;
    
    private String batchName;
    
    private DateTime batchCreatedDate;
    
    private Long count;
    
    private Long activationCodeWithNoUsages;
    
    private Long activationCodeWithAvailableUsages;
    
    private Long totalAllowedUsages;
    
    private Long totalActualUsages;
    
    private String productGroupName;
    
    private String productId;
    
    private DateTime batchStartDate;
    
    private DateTime batchEndDate;
    
    private String licenceType;
    
    private LocalDate licenceStartDate;

    private LocalDate licenceEndDate;

    private Integer licenceTotalConcurrency;
    
    private RollingUnitType licenceUnitType;
    
    private RollingBeginOn licenceBeginOn;
    
    private Integer licenceTimePeriod;
    
    private Integer licenceAllowedUsages;

    
    public ActivationCodeDto() {
        super();
    }

    public ActivationCodeDto(String batchId, String batchName, DateTime batchCreatedDate, Long count,
            Long activationCodeWithNoUsages,
            Long activationCodeWithAvailableUsages, Long totalAllowedUsages,
            Long totalActualUsages, DateTime batchStartDate,
            DateTime batchEndDate, String productId, 
            String licenceType, LocalDate licenceStartDate,
            LocalDate licenceEndDate, Integer licenceTotalConcurrency,
            RollingUnitType licenceUnitType, RollingBeginOn licenceBeginOn,
            Integer licenceTimePeriod, Integer licenceAllowedUsages) {
        super();
        this.batchId = batchId;
        this.batchName = batchName;
        this.batchCreatedDate = batchCreatedDate;
        this.count = count;
        this.activationCodeWithNoUsages = activationCodeWithNoUsages;
        this.activationCodeWithAvailableUsages = activationCodeWithAvailableUsages;
        this.totalAllowedUsages = totalAllowedUsages;
        this.totalActualUsages = totalActualUsages;
        this.batchStartDate = batchStartDate;
        this.batchEndDate = batchEndDate;
        this.productId = productId;
        this.licenceType = licenceType;
        this.licenceStartDate = licenceStartDate;
        this.licenceEndDate = licenceEndDate;
        this.licenceTotalConcurrency = licenceTotalConcurrency;
        this.licenceUnitType = licenceUnitType;
        this.licenceBeginOn = licenceBeginOn;
        this.licenceTimePeriod = licenceTimePeriod;
        this.licenceAllowedUsages = licenceAllowedUsages;
    }

    public ActivationCodeDto(String batchId, String batchName, DateTime batchCreatedDate, Long count,
            Long activationCodeWithNoUsages,
            Long activationCodeWithAvailableUsages, Long totalAllowedUsages,
            Long totalActualUsages, DateTime batchStartDate,
            DateTime batchEndDate, String productId, String productGroupName,
            String licenceType, LocalDate licenceStartDate,
            LocalDate licenceEndDate, Integer licenceTotalConcurrency,
            RollingUnitType licenceUnitType, RollingBeginOn licenceBeginOn,
            Integer licenceTimePeriod, Integer licenceAllowedUsages) {
        super();
        this.batchId = batchId;
        this.batchName = batchName;
        this.batchCreatedDate = batchCreatedDate;
        this.count = count;
        this.activationCodeWithNoUsages = activationCodeWithNoUsages;
        this.activationCodeWithAvailableUsages = activationCodeWithAvailableUsages;
        this.totalAllowedUsages = totalAllowedUsages;
        this.totalActualUsages = totalActualUsages;
        this.productGroupName = productGroupName;
        this.batchStartDate = batchStartDate;
        this.batchEndDate = batchEndDate;
        this.productId = productId;
        this.licenceType = licenceType;
        this.licenceStartDate = licenceStartDate;
        this.licenceEndDate = licenceEndDate;
        this.licenceTotalConcurrency = licenceTotalConcurrency;
        this.licenceUnitType = licenceUnitType;
        this.licenceBeginOn = licenceBeginOn;
        this.licenceTimePeriod = licenceTimePeriod;
        this.licenceAllowedUsages = licenceAllowedUsages;
    } 
    /**
     * @return the batchCreatedDate
     */
    public DateTime getBatchCreatedDate() {
        return batchCreatedDate;
    }
   
    /**
     * @return the batchId
     */
    public String getBatchId() {
        return batchId;
    }

    /**
     * @return the batchName
     */
    public String getBatchName() {
        return batchName;
    }

    /**
     * @return the count
     */
    public Long getCount() {
        return count;
    }

    /**
     * @return the activationCodeWithNoUsages
     */
    public Long getActivationCodeWithNoUsages() {
        return activationCodeWithNoUsages;
    }

    /**
     * @return the activationCodeWithUsages
     */
    public Long getActivationCodeWithAvailableUsages() {
        return activationCodeWithAvailableUsages;
    }

    /**
     * @return the totalAllowedUsages
     */
    public Long getTotalAllowedUsages() {
        return totalAllowedUsages;
    }

    /**
     * @return the totalActualUsages
     */
    public Long getTotalActualUsages() {
        return totalActualUsages;
    }

    /**
     * @return the batchStartDate
     */
    public DateTime getBatchStartDate() {
        return batchStartDate;
    }

    /**
     * @return the batchEndDate
     */
    public DateTime getBatchEndDate() {
        return batchEndDate;
    }

    /**
     * 
     * @return the productGroupName
     */
    public String getProductGroupName(){
        return productGroupName;
    }
    /**
     * @return the productId
     */
    public String getProductId() {
        return productId;
    }

    /**
     * @return the licenceType
     */
    public String getLicenceType() {
        return licenceType;
    }

    /**
     * @return the licenceStartDate
     */
    public LocalDate getLicenceStartDate() {
        return licenceStartDate;
    }

    /**
     * @return the licenceEndDate
     */
    public LocalDate getLicenceEndDate() {
        return licenceEndDate;
    }

    /**
     * @return the licenceTotalConcurrency
     */
    public Integer getLicenceTotalConcurrency() {
        return licenceTotalConcurrency;
    }

    /**
     * @return the licenceUnitType
     */
    public RollingUnitType getLicenceUnitType() {
        return licenceUnitType;
    }

    /**
     * @return the licenceBeginOn
     */
    public RollingBeginOn getLicenceBeginOn() {
        return licenceBeginOn;
    }

    /**
     * @return the licenceTimePeriod
     */
    public Integer getLicenceTimePeriod() {
        return licenceTimePeriod;
    }

    /**
     * @return the licenceAllowedUsages
     */
    public Integer getLicenceAllowedUsages() {
        return licenceAllowedUsages;
    }

}
