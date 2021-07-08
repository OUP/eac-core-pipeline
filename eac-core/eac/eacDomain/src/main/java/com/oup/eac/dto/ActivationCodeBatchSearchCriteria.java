package com.oup.eac.dto;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;

import com.oup.eac.domain.AdminUser;
import com.oup.eac.domain.LicenceTemplate;
import com.oup.eac.domain.RegisterableProduct;

public class ActivationCodeBatchSearchCriteria implements Serializable {

	private String batchId;
	private DateTime batchDate;
	private String code;
	private Class<? extends LicenceTemplate> licenceTemplate;
	private AdminUser adminUser;
	private RegisterableProduct registerableProduct;
	
	private String eacGroupId;
	
	public String getEacGroupId() {
		return eacGroupId;
	}

	public void setEacGroupId(String eacGroupId) {
		this.eacGroupId = eacGroupId;
	}

	public String getBatchId() {
		return batchId;
	}

	public void setBatchId(String batchId) {
		this.batchId = batchId;
	}

	public DateTime getBatchDate() {
		return batchDate;
	}

	public void setBatchDate(DateTime batchDate) {
		this.batchDate = batchDate;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
    	String santisedCode = code;
    	if (StringUtils.contains(santisedCode, "-")) {
    		santisedCode = StringUtils.replace(santisedCode, "-", "");
    	}
        this.code = santisedCode;
	}

	public Class<? extends LicenceTemplate> getLicenceTemplate() {
		return licenceTemplate;
	}

	public void setLicenceTemplate(Class<? extends LicenceTemplate> licenceTemplate) {
		this.licenceTemplate = licenceTemplate;
	}

	public AdminUser getAdminUser() {
		return adminUser;
	}

	public void setAdminUser(AdminUser adminUser) {
		this.adminUser = adminUser;
	}

	public RegisterableProduct getRegisterableProduct() {
		return registerableProduct;
	}

	public void setRegisterableProduct(RegisterableProduct registerableProduct) {
		this.registerableProduct = registerableProduct;
	}
	
	public ActivationCodeBatchSearchCriteria adminUser(final AdminUser adminUser) {
		this.adminUser = adminUser;
		return this;
	}
	
	public ActivationCodeBatchSearchCriteria batchId(final String batchId) {
		this.batchId = batchId;
		return this;
	}
	
	public ActivationCodeBatchSearchCriteria batchDate(final DateTime batchDate) {
		this.batchDate = batchDate;
		return this;
	}
	
	public ActivationCodeBatchSearchCriteria code(final String code) {
		this.code = code;
		return this;
	}
	
	public ActivationCodeBatchSearchCriteria licenceTemplate(final Class<? extends LicenceTemplate> licenceTemplate) {
		this.licenceTemplate = licenceTemplate;
		return this;
	}
	
	public ActivationCodeBatchSearchCriteria registerableProduct(final RegisterableProduct registerableProduct) {
		this.registerableProduct = registerableProduct;
		return this;
	}

	public ActivationCodeBatchSearchCriteria eacGroupId(final String eacGroupId) {
		this.eacGroupId = eacGroupId;
		return this;
	}
	
	@Override
	public String toString() {
		return "ActivationCodeBatchSearchCriteria [batchId=" + batchId + ", batchDate=" + batchDate + ", code=" + code + ", licenceTemplate=" + licenceTemplate
				+ ", adminUser=" + adminUser + ", registerableProduct=" + registerableProduct + "]";
	}

}
