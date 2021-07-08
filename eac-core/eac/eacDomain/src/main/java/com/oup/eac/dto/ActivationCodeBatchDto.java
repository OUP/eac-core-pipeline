package com.oup.eac.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.joda.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;

import com.oup.eac.domain.ActivationCodeBatch;
import com.oup.eac.domain.ActivationCodeRegistration;
import com.oup.eac.domain.ConcurrentLicenceTemplate;
import com.oup.eac.domain.RollingLicenceTemplate;
import com.oup.eac.domain.StandardLicenceTemplate;
import com.oup.eac.domain.UsageLicenceTemplate;
import com.oup.eac.dto.LicenceDetailDto.LicenceType;

public class ActivationCodeBatchDto implements Serializable {

    private String batchId;
    
    private String currentBatchId;
    
    private String codeFormat ;
    
    private Integer allowedUsages;
    
    private Integer actualUsage;
    
	private Integer numberOfTokens ;
    
    private String productGroupId;
    
    private String productId;
    
    private String addedInPool;
    
    @DateTimeFormat (pattern="dd/MM/yyyy")
	private LocalDate validFrom;
	
	@DateTimeFormat (pattern="dd/MM/yyyy")
	private LocalDate validTo;
	
	@DateTimeFormat (pattern="dd/MM/yyyy")
	private LocalDate updatedDate;
	
	@DateTimeFormat (pattern="dd/MM/yyyy")
	private LocalDate createdDate;
	
	private LicenceDetailDto licenceDetailDto;
	
	private List<String> activationCodes = new ArrayList<String>();
	
	private ActivationCodeRegistration activationCodeRegistration;
	
	private List<ActivationCodeRegistration> activationCodeRegistrations;
    
    public ActivationCodeBatchDto(List<String> activationCodes) {
        super();
        this.activationCodes = activationCodes ;
    }
    
    public String codePrefix;
    
    public long version;
    
	public ActivationCodeBatchDto() {
        super();
    }
    
    public ActivationCodeBatchDto(ActivationCodeBatch activationCodeBatch, final int noOfTokens,final int allowedUsage, final String productId, final String productGroupId){
    	this.batchId = activationCodeBatch.getBatchId() ;
    	this.allowedUsages = allowedUsage ;
    	this.numberOfTokens = noOfTokens ;
    	this.codeFormat = activationCodeBatch.getActivationCodeFormat().toString();
    	if(activationCodeBatch.getStartDate() != null)
    		this.validFrom = activationCodeBatch.getStartDate().toLocalDate() ;
    	if(activationCodeBatch.getEndDate() != null)
    		this.validTo = activationCodeBatch.getEndDate().toLocalDate() ;
    	
    	if(productId != null)
    		this.productId = productId ;
    	else
    		this.productGroupId = productGroupId;
    	
    	LicenceDetailDto licenceDetailDto = null ;
		
    	if(activationCodeBatch.getLicenceTemplate().getLicenceType().toString().equals(LicenceType.ROLLING.toString())) {
			RollingLicenceTemplate obj = (RollingLicenceTemplate)activationCodeBatch.getLicenceTemplate();
			RollingLicenceDetailDto dtoObj = new RollingLicenceDetailDto(obj.getBeginOn(), obj.getUnitType(), obj.getTimePeriod(), null);
			licenceDetailDto = dtoObj;
		} else if(activationCodeBatch.getLicenceTemplate().getLicenceType().toString().equals(LicenceType.CONCURRENT.toString())) {
			ConcurrentLicenceTemplate obj = (ConcurrentLicenceTemplate)activationCodeBatch.getLicenceTemplate();
			StandardConcurrentLicenceDetailDto dtoObj = new StandardConcurrentLicenceDetailDto(obj.getTotalConcurrency(), obj.getUserConcurrency());
			licenceDetailDto = dtoObj;
		} else if(activationCodeBatch.getLicenceTemplate().getLicenceType().toString().equals(LicenceType.USAGE.toString())) {
			UsageLicenceTemplate obj = (UsageLicenceTemplate)activationCodeBatch.getLicenceTemplate();
			UsageLicenceDetailDto dtoObj = new UsageLicenceDetailDto();
			dtoObj.setAllowedUsages(obj.getAllowedUsages());;
			licenceDetailDto = dtoObj;
		} else if(activationCodeBatch.getLicenceTemplate().getLicenceType().toString().equals(LicenceType.STANDARD.toString())){
			StandardLicenceTemplate obj = (StandardLicenceTemplate)activationCodeBatch.getLicenceTemplate();
			StandardLicenceDetailDto dtoObj = new StandardLicenceDetailDto();
			dtoObj.setStartDate(obj.getStartDate());
			dtoObj.setEndDate(obj.getEndDate());
			licenceDetailDto = dtoObj;
		}
    	if(activationCodeBatch.getLicenceTemplate().getStartDate() != null){
    		licenceDetailDto.setStartDate(activationCodeBatch.getLicenceTemplate().getStartDate());
    	}
    	if(activationCodeBatch.getLicenceTemplate().getEndDate() != null)
    		licenceDetailDto.setEndDate(activationCodeBatch.getLicenceTemplate().getEndDate());
		
		
    	this.setLicenceDetailDto(licenceDetailDto);
    }
    
    public ActivationCodeBatchDto(ActivationCodeBatch activationCodeBatch, final int noOfTokens,
    		final int allowedUsage, final int actualUsage, final String productId, final String productGroupId, 
    		final ActivationCodeRegistration activationCodeRegistration, final LocalDate createdDate, final LocalDate updatedDate,
    		final String codePrefix, final long version){
    	this.batchId = activationCodeBatch.getBatchId() ;
    	this.allowedUsages = allowedUsage ;
    	this.numberOfTokens = noOfTokens ;
    	this.codeFormat = activationCodeBatch.getActivationCodeFormat().toString();
    	if(activationCodeBatch.getStartDate() != null)
    		this.validFrom = activationCodeBatch.getStartDate().toLocalDate() ;
    	if(activationCodeBatch.getEndDate() != null)
    		this.validTo = activationCodeBatch.getEndDate().toLocalDate() ;
    	
    	if(productId != null)
    		this.productId = productId ;
    	else
    		this.productGroupId = productGroupId;
    	
    	LicenceDetailDto licenceDetailDto = null ;
		
    	if(activationCodeBatch.getLicenceTemplate().getLicenceType().toString().equals(LicenceType.ROLLING.toString())) {
			RollingLicenceTemplate obj = (RollingLicenceTemplate)activationCodeBatch.getLicenceTemplate();
			RollingLicenceDetailDto dtoObj = new RollingLicenceDetailDto(obj.getBeginOn(), obj.getUnitType(), obj.getTimePeriod(), null);
			licenceDetailDto = dtoObj;
		} else if(activationCodeBatch.getLicenceTemplate().getLicenceType().toString().equals(LicenceType.CONCURRENT.toString())) {
			ConcurrentLicenceTemplate obj = (ConcurrentLicenceTemplate)activationCodeBatch.getLicenceTemplate();
			StandardConcurrentLicenceDetailDto dtoObj = new StandardConcurrentLicenceDetailDto(obj.getTotalConcurrency(), obj.getUserConcurrency());
			licenceDetailDto = dtoObj;
		} else if(activationCodeBatch.getLicenceTemplate().getLicenceType().toString().equals(LicenceType.USAGE.toString())) {
			UsageLicenceTemplate obj = (UsageLicenceTemplate)activationCodeBatch.getLicenceTemplate();
			UsageLicenceDetailDto dtoObj = new UsageLicenceDetailDto();
			dtoObj.setAllowedUsages(obj.getAllowedUsages());;
			licenceDetailDto = dtoObj;
		} else if(activationCodeBatch.getLicenceTemplate().getLicenceType().toString().equals(LicenceType.STANDARD.toString())){
			StandardLicenceTemplate obj = (StandardLicenceTemplate)activationCodeBatch.getLicenceTemplate();
			StandardLicenceDetailDto dtoObj = new StandardLicenceDetailDto();
			dtoObj.setStartDate(obj.getStartDate());
			dtoObj.setEndDate(obj.getEndDate());
			licenceDetailDto = dtoObj;
		}
    	if(activationCodeBatch.getLicenceTemplate().getStartDate() != null){
    		licenceDetailDto.setStartDate(activationCodeBatch.getLicenceTemplate().getStartDate());
    	}
    	if(activationCodeBatch.getLicenceTemplate().getEndDate() != null)
    		licenceDetailDto.setEndDate(activationCodeBatch.getLicenceTemplate().getEndDate());
				
    	this.setLicenceDetailDto(licenceDetailDto);
    	this.actualUsage = actualUsage;
    	this.activationCodeRegistration = activationCodeRegistration;
    	this.createdDate = createdDate;
    	this.updatedDate = updatedDate;
    	this.codePrefix = codePrefix;
    	this.version = version;
    	
    }
    
	public String getBatchId() {
		return batchId;
	}

	

	public String getCurrentBatchId() {
		return currentBatchId;
	}

	public void setCurrentBatchId(String currentBatchId) {
		this.currentBatchId = currentBatchId;
	}

	public void setBatchId(String batchId) {
		this.batchId = batchId;
	}


	public String getCodeFormat() {
		return codeFormat;
	}


	public void setCodeFormat(String codeFormat) {
		this.codeFormat = codeFormat;
	}


	public Integer getAllowedUsages() {
		return allowedUsages;
	}


	public void setAllowedUsages(Integer allowedUsages) {
		this.allowedUsages = allowedUsages;
	}


	public Integer getNumberOfTokens() {
		return numberOfTokens;
	}


	public void setNumberOfTokens(Integer numberOfTokens) {
		this.numberOfTokens = numberOfTokens;
	}


	public String getProductGroupId() {
		return productGroupId;
	}


	public void setProductGroupId(String productGroupId) {
		this.productGroupId = productGroupId;
	}


	public String getProductId() {
		return productId;
	}


	public void setProductId(String productId) {
		this.productId = productId;
	}


	public LocalDate getValidFrom() {
		return validFrom;
	}


	public void setValidFrom(LocalDate validFrom) {
		this.validFrom = validFrom;
	}


	public LocalDate getValidTo() {
		return validTo;
	}


	public void setValidTo(LocalDate validTo) {
		this.validTo = validTo;
	}


	public LicenceDetailDto getLicenceDetailDto() {
		return licenceDetailDto;
	}


	public void setLicenceDetailDto(LicenceDetailDto licenceDetailDto) {
		this.licenceDetailDto = licenceDetailDto;
	}


	public List<String> getActivationCodes() {
		return activationCodes;
	}


	public void setActivationCodes(List<String> activationCodes) {
		this.activationCodes = activationCodes;
	}

    public Integer getActualUsage() {
		return actualUsage;
	}

	public void setActualUsage(Integer actualUsage) {
		this.actualUsage = actualUsage;
	}	

	public ActivationCodeRegistration getActivationCodeRegistration() {
		return activationCodeRegistration;
	}

	public void setActivationCodeRegistration(
			ActivationCodeRegistration activationCodeRegistration) {
		this.activationCodeRegistration = activationCodeRegistration;
	}
	
    public LocalDate getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(LocalDate updatedDate) {
		this.updatedDate = updatedDate;
	}

	public LocalDate getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(LocalDate createdDate) {
		this.createdDate = createdDate;
	}

	public String getCodePrefix() {
		return codePrefix;
	}

	public void setCodePrefix(String codePrefix) {
		this.codePrefix = codePrefix;
	}

	public long getVersion() {
		return version;
	}

	public void setVersion(long version) {
		this.version = version;
	}
	
	public List<ActivationCodeRegistration> getActivationCodeRegistrations() {
		return activationCodeRegistrations;
	}

	public void setActivationCodeRegistrations(
			List<ActivationCodeRegistration> activationCodeRegistrations) {
		this.activationCodeRegistrations = activationCodeRegistrations;
	}
	
	
    public void mergeActivationCodebatchChanges(ActivationCodeBatch activationCodeBatch){
    	if(activationCodeBatch.getActivationCodeRegistrationDefinition().getProduct() != null) {
    		this.productId = activationCodeBatch.getActivationCodeRegistrationDefinition().getProduct().getId();
    	} else {
    		this.productId = null ;
    	}
    	
    	this.currentBatchId = this.batchId ;
    	this.batchId = activationCodeBatch.getBatchId() ;
    	if ( activationCodeBatch.getStartDate() != null) {
    		this.validFrom = activationCodeBatch.getStartDate().toLocalDate() ;
    	}
    	if (activationCodeBatch.getEndDate()!= null ) {
    		this.validTo = activationCodeBatch.getEndDate().toLocalDate() ;
    	}
    	
    	LicenceDetailDto licenceDetailDto = null ;
		
    	if(activationCodeBatch.getLicenceTemplate().getLicenceType().toString().equals(LicenceType.ROLLING.toString())) {
			RollingLicenceTemplate obj = (RollingLicenceTemplate)activationCodeBatch.getLicenceTemplate();
			RollingLicenceDetailDto dtoObj = new RollingLicenceDetailDto(obj.getBeginOn(), obj.getUnitType(), obj.getTimePeriod(), null);
			licenceDetailDto = dtoObj;
		} else if(activationCodeBatch.getLicenceTemplate().getLicenceType().toString().equals(LicenceType.CONCURRENT.toString())) {
			ConcurrentLicenceTemplate obj = (ConcurrentLicenceTemplate)activationCodeBatch.getLicenceTemplate();
			StandardConcurrentLicenceDetailDto dtoObj = new StandardConcurrentLicenceDetailDto(obj.getTotalConcurrency(), obj.getUserConcurrency());
			licenceDetailDto = dtoObj;
		} else if(activationCodeBatch.getLicenceTemplate().getLicenceType().toString().equals(LicenceType.USAGE.toString())) {
			UsageLicenceTemplate obj = (UsageLicenceTemplate)activationCodeBatch.getLicenceTemplate();
			UsageLicenceDetailDto dtoObj = new UsageLicenceDetailDto();
			dtoObj.setAllowedUsages(obj.getAllowedUsages());;
			licenceDetailDto = dtoObj;
		} else if(activationCodeBatch.getLicenceTemplate().getLicenceType().toString().equals(LicenceType.STANDARD.toString())){
			StandardLicenceTemplate obj = (StandardLicenceTemplate)activationCodeBatch.getLicenceTemplate();
			StandardLicenceDetailDto dtoObj = new StandardLicenceDetailDto();
			dtoObj.setStartDate(obj.getStartDate());
			dtoObj.setEndDate(obj.getEndDate());
		}
    	if(activationCodeBatch.getLicenceTemplate().getStartDate() != null){
    		licenceDetailDto.setStartDate(activationCodeBatch.getLicenceTemplate().getStartDate());
    	}
    	if(activationCodeBatch.getLicenceTemplate().getEndDate() != null)
    		licenceDetailDto.setEndDate(activationCodeBatch.getLicenceTemplate().getEndDate());
    	
    	this.licenceDetailDto = licenceDetailDto ;
    }

	public String getAddedInPool() {
		return addedInPool;
	}

	public void setAddedInPool(String addedInPool) {
		this.addedInPool = addedInPool;
	}

    
}
