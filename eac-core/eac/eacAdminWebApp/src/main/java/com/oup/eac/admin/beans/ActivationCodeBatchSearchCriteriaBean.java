package com.oup.eac.admin.beans;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.joda.time.LocalDate;
import org.springframework.security.core.context.SecurityContextHolder;

import com.oup.eac.domain.AdminUser;
import com.oup.eac.domain.EacGroups;
import com.oup.eac.domain.LicenceTemplate.LicenceType;
import com.oup.eac.domain.Product;
import com.oup.eac.domain.RegisterableProduct;
import com.oup.eac.dto.ActivationCodeBatchSearchCriteria;

public class ActivationCodeBatchSearchCriteriaBean extends PageTrackingBean {

	private String activationCode;

	private String batchName;

	private LocalDate batchDateTime;

	private LicenceType licenceType;

	private boolean enabled;

	private Product product;
	
	private String eacGroupId;
	
	public String getEacGroupId() {
		return eacGroupId;
	}

	public void setEacGroupId(String eacGroupId) {
		this.eacGroupId = eacGroupId;
	}

	public String getActivationCode() {
		return activationCode;
	}

	public void setActivationCode(String activationCode) {
		this.activationCode = activationCode;
	}

	public String getBatchName() {
		return batchName;
	}

	public void setBatchName(String batchName) {
		this.batchName = batchName;
	}

	public LocalDate getBatchDateTime() {
		return batchDateTime;
	}

	public void setBatchDateTime(LocalDate batchDateTime) {
		this.batchDateTime = batchDateTime;
	}

	public LicenceType getLicenceType() {
		return licenceType;
	}

	public void setLicenceType(LicenceType licenceType) {
		this.licenceType = licenceType;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public ActivationCodeBatchSearchCriteria toActivationCodeBatchSearchCriteria() {
		ActivationCodeBatchSearchCriteria searchCriteria = new ActivationCodeBatchSearchCriteria();
		if (product != null) {
			searchCriteria.setRegisterableProduct((RegisterableProduct) product);
		}
		
		if(eacGroupId != null){
			searchCriteria.setEacGroupId(eacGroupId);
		}
		
		searchCriteria.setAdminUser((AdminUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
		if (batchDateTime != null) {
			searchCriteria.setBatchDate(batchDateTime.toDateTimeAtStartOfDay());
		}
		searchCriteria.setCode(activationCode);
		searchCriteria.setBatchId(batchName);
		if (licenceType != null) {
			searchCriteria.setLicenceTemplate(licenceType.getLicenceClass());
		}
		return searchCriteria;
	}

	public List<LicenceType> getAllowedLicenceTypes() {
		List<LicenceType> licenceTypes = new ArrayList<LicenceType>();
		licenceTypes.addAll(Arrays.asList(LicenceType.values()));
		licenceTypes.remove(LicenceType.STANDARD);
		return licenceTypes;
	}
}
