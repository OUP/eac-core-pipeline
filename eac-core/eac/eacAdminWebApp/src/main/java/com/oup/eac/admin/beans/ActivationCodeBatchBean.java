package com.oup.eac.admin.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.joda.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;

import com.oup.eac.domain.ActivationCodeBatch;
import com.oup.eac.domain.ActivationCodeRegistrationDefinition;
import com.oup.eac.domain.ConcurrentLicenceTemplate;
import com.oup.eac.domain.ExternalProductId;
import com.oup.eac.domain.LicenceTemplate;
import com.oup.eac.domain.LicenceTemplate.LicenceType;
import com.oup.eac.domain.RollingLicenceTemplate;
import com.oup.eac.domain.RollingLicenceTemplate.RollingBeginOn;
import com.oup.eac.domain.RollingLicenceTemplate.RollingUnitType;
import com.oup.eac.domain.StandardLicenceTemplate;
import com.oup.eac.domain.UsageLicenceTemplate;
import com.oup.eac.dto.EnforceableProductDto;

public class ActivationCodeBatchBean implements Serializable {

	private String currentBatchId = "" ;
	
	private final ActivationCodeBatch activationCodeBatch;

	private String tokenAllowedUsages = "1";

	private String numberOfTokens = "";

	private LicenceType licenceType = LicenceType.ROLLING;

	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private LocalDate licenceStartDate;

	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private LocalDate licenceEndDate;

	private ActivationCodeRegistrationDefinition activationCodeRegistrationDefinition;

	private String totalConcurrency = "1";

	private String userConcurrency = "1";

	private String timePeriod = "1";

	private RollingBeginOn rollingBeginOn;

	private RollingUnitType rollingUnitType;

	private String licenceAllowedUsages = "1";

	private boolean editMode;
	
	private String productId;
	
	private String eacGroupId;
	
	private EnforceableProductDto product ;
	
	private String addedInPool;
	
	public ActivationCodeBatchBean(ActivationCodeBatch activationCodeBatch) {
		super();
		this.activationCodeBatch = activationCodeBatch;
		editMode = this.activationCodeBatch.getBatchId() != null;
		if(editMode){
		    this.activationCodeRegistrationDefinition = activationCodeBatch.getActivationCodeRegistrationDefinition();
		    LicenceTemplate lt = activationCodeBatch.getLicenceTemplate();
		    this.licenceType = lt.getLicenceType();
		    this.licenceStartDate = lt.getStartDate();
		    this.licenceEndDate = lt.getEndDate();
		    switch(this.licenceType){
		    case ROLLING:
		        RollingLicenceTemplate rolling = (RollingLicenceTemplate)lt;
		        this.timePeriod = String.valueOf(rolling.getTimePeriod());
		        this.rollingUnitType = rolling.getUnitType();
		        this.rollingBeginOn = rolling.getBeginOn();
		        break;
		    case CONCURRENT:
		        ConcurrentLicenceTemplate concurrent = (ConcurrentLicenceTemplate)lt;
		        this.userConcurrency = String.valueOf(concurrent.getUserConcurrency());
		        this.totalConcurrency = String.valueOf(concurrent.getTotalConcurrency());
		        break;
            case USAGE:
                UsageLicenceTemplate usage = (UsageLicenceTemplate)lt;
                this.licenceAllowedUsages = String.valueOf(usage.getAllowedUsages());
		    case STANDARD:
		    default:
		    }
		}
	}

	/**
	 * @return the tokenAllowedUsages
	 */
	public String getTokenAllowedUsages() {
		return tokenAllowedUsages;
	}

	/**
	 * @param tokenAllowedUsages
	 *            the tokenAllowedUsages to set
	 */
	public void setTokenAllowedUsages(final String tokenAllowedUsages) {
		this.tokenAllowedUsages = tokenAllowedUsages;
	}
	
	public int getTokenAllowedUsagesAsInt() {
		return Integer.parseInt(tokenAllowedUsages);
	}

	/**
	 * @return the numberOfTokens
	 */
	public String getNumberOfTokens() {
		return numberOfTokens;
	}

	/**
	 * @param numberOfTokens
	 *            the numberOfTokens to set
	 */
	public void setNumberOfTokens(String numberOfTokens) {
		this.numberOfTokens = numberOfTokens;
	}

	public int getNumberOfTokensAsInt() {
		return Integer.parseInt(numberOfTokens);
	}
	
	/**
	 * @return the totalConcurrency
	 */
	public String getTotalConcurrency() {
		return totalConcurrency;
	}

	/**
	 * @param totalConcurrency
	 *            the totalConcurrency to set
	 */
	public void setTotalConcurrency(String totalConcurrency) {
		this.totalConcurrency = totalConcurrency;
	}

	public int getTotalConcurrencyAsInt() {
		return Integer.parseInt(totalConcurrency);
	}

	/**
	 * @return the userConcurrency
	 */
	public String getUserConcurrency() {
		return userConcurrency;
	}

	/**
	 * @param userConcurrency
	 *            the userConcurrency to set
	 */
	public void setUserConcurrency(String userConcurrency) {
		this.userConcurrency = userConcurrency;
	}

	public int getUserConcurrencyAsInt() {
		return Integer.parseInt(userConcurrency);
	}

	/**
	 * @return the timePeriod
	 */
	public String getTimePeriod() {
		return timePeriod;
	}

	/**
	 * @param timePeriod
	 *            the timePeriod to set
	 */
	public void setTimePeriod(String timePeriod) {
		this.timePeriod = timePeriod;
	}

	public int getTimePeriodAsInt() {
		return Integer.parseInt(timePeriod);
	}

	/**
	 * @return the licenceAllowedUsages
	 */
	public String getLicenceAllowedUsages() {
		return licenceAllowedUsages;
	}

	/**
	 * @param licenceAllowedUsages
	 *            the licenceAllowedUsages to set
	 */
	public void setLicenceAllowedUsages(String licenceAllowedUsages) {
		this.licenceAllowedUsages = licenceAllowedUsages;
	}

	public int getLicenceAllowedUsagesAsInt() {
		return Integer.parseInt(licenceAllowedUsages);
	}

	/**
	 * @return the activationCodeBatch
	 */
	public ActivationCodeBatch getActivationCodeBatch() {
		return activationCodeBatch;
	}

	public void setLicenceType(final LicenceType licenceType) {
		this.licenceType = licenceType;
	}

	public LicenceType getLicenceType() {
		return licenceType;
	}
	
	public static List<LicenceType> getAllowedLicenceTypes() {
		List<LicenceType> licenceTypes = new ArrayList<LicenceType>();
		licenceTypes.addAll(Arrays.asList(LicenceType.values()));
		licenceTypes.remove(LicenceType.STANDARD);
		return licenceTypes;
	}
	
	public LocalDate getLicenceStartDate() {
		return licenceStartDate;
	}

	public void setLicenceStartDate(LocalDate licenceStartDate) {
		this.licenceStartDate = licenceStartDate;
	}

	public LocalDate getLicenceEndDate() {
		return licenceEndDate;
	}

	public void setLicenceEndDate(LocalDate licenceEndDate) {
		this.licenceEndDate = licenceEndDate;
	}

	/**
	 * @return the activationCodeRegistrationDefinition
	 */
	public ActivationCodeRegistrationDefinition getActivationCodeRegistrationDefinition() {
		return activationCodeRegistrationDefinition;
	}

	/**
	 * @param activationCodeRegistrationDefinition
	 *            the activationCodeRegistrationDefinition to set
	 */
	public void setActivationCodeRegistrationDefinition(ActivationCodeRegistrationDefinition activationCodeRegistrationDefinition) {
		this.activationCodeRegistrationDefinition = activationCodeRegistrationDefinition;
	}

	public RollingBeginOn getRollingBeginOn() {
		return rollingBeginOn;
	}

	public void setRollingBeginOn(RollingBeginOn rollingBeginOn) {
		this.rollingBeginOn = rollingBeginOn;
	}

	public RollingUnitType getRollingUnitType() {
		return rollingUnitType;
	}
	
	public static List<RollingUnitType> getAllowedRollingUnitTypes() {
		List<RollingUnitType> rollingUnitTypes = new ArrayList<RollingUnitType>();
		rollingUnitTypes.addAll(Arrays.asList(RollingUnitType.values()));
		rollingUnitTypes.remove(RollingUnitType.SECOND);
		rollingUnitTypes.remove(RollingUnitType.MILLISECOND);
		return rollingUnitTypes;
	}

	public void setRollingUnitType(RollingUnitType rollingUnitType) {
		this.rollingUnitType = rollingUnitType;
	}

	public List<String> getExternalIdsForDisplay() {
		List<String> externalIds = new ArrayList<String>();
		if (activationCodeRegistrationDefinition != null) {
			if(activationCodeRegistrationDefinition.getProduct() != null){
				for (ExternalProductId extId : activationCodeRegistrationDefinition.getProduct().getExternalIds()) {
					StringBuilder builder = new StringBuilder();
					builder.append(extId.getExternalSystemIdType().getExternalSystem().getName());
					builder.append("/");
					builder.append(extId.getExternalSystemIdType().getName());
					builder.append(": ");
					builder.append(extId.getExternalId());
					externalIds.add(builder.toString());
				}
			}
		}
		Collections.sort(externalIds);
		return externalIds;
	}

	public LicenceTemplate getLicenceTemplate() {
		switch (getLicenceType()) {
		case STANDARD:
			return getStandardLicenceTemplate();
		case CONCURRENT:
			return getConcurrentLicenceTemplate();
		case ROLLING:
			return getRollingLicenceTemplate();
		case USAGE:
			return getUsageLicenceTemplate();
		default:
			throw new IllegalArgumentException("Unknown licence type :" + getLicenceType());
		}
	}

	private StandardLicenceTemplate getStandardLicenceTemplate() {
		StandardLicenceTemplate standardLicenceTemplate = new StandardLicenceTemplate();
		populateLicenceTemplate(standardLicenceTemplate);
		return standardLicenceTemplate;
	}

	private ConcurrentLicenceTemplate getConcurrentLicenceTemplate() {
		ConcurrentLicenceTemplate concurrentLicenceTemplate = new ConcurrentLicenceTemplate();
		concurrentLicenceTemplate.setTotalConcurrency(getTotalConcurrencyAsInt());
		concurrentLicenceTemplate.setUserConcurrency(getUserConcurrencyAsInt());
		populateLicenceTemplate(concurrentLicenceTemplate);
		return concurrentLicenceTemplate;
	}

	private UsageLicenceTemplate getUsageLicenceTemplate() {
		UsageLicenceTemplate usageLicenceTemplate = new UsageLicenceTemplate();
		usageLicenceTemplate.setAllowedUsages(getLicenceAllowedUsagesAsInt());
		populateLicenceTemplate(usageLicenceTemplate);
		return usageLicenceTemplate;
	}

	private RollingLicenceTemplate getRollingLicenceTemplate() {
		RollingLicenceTemplate rollingLicenceTemplate = new RollingLicenceTemplate();
		rollingLicenceTemplate.setTimePeriod(getTimePeriodAsInt());
		rollingLicenceTemplate.setBeginOn(getRollingBeginOn());
		rollingLicenceTemplate.setUnitType(getRollingUnitType());
		populateLicenceTemplate(rollingLicenceTemplate);
		return rollingLicenceTemplate;
	}

	private void populateLicenceTemplate(LicenceTemplate licenceTemplate) {
		licenceTemplate.setStartDate(getLicenceStartDate());
		licenceTemplate.setEndDate(getLicenceEndDate());
	}

    public boolean isEditMode() {
        return editMode;
    }

    public void setEditMode(boolean editMode) {
        this.editMode = editMode;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }
    
	public String getEacGroupId() {
		return eacGroupId;
	}

	public void setEacGroupId(String eacGroupId) {
		this.eacGroupId = eacGroupId;
	}

	public String getCurrentBatchId() {
		return currentBatchId;
	}

	public void setCurrentBatchId(String currentBatchId) {
		this.currentBatchId = currentBatchId;
	}

	public EnforceableProductDto getProduct() {
		return product;
	}

	public void setProduct(EnforceableProductDto product) {
		this.product = product;
	}

	public String getAddedInPool() {
		return addedInPool;
	}

	public void setAddedInPool(String addedInPool) {
		this.addedInPool = addedInPool;
	}
	
	
}
