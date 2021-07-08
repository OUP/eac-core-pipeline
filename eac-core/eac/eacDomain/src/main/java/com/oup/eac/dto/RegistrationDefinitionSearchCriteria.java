package com.oup.eac.dto;

import java.util.Set;

import com.oup.eac.domain.AdminUser;
import com.oup.eac.domain.Division;
import com.oup.eac.domain.Product.ProductState;
import com.oup.eac.domain.RegistrationDefinition.RegistrationDefinitionType;

public class RegistrationDefinitionSearchCriteria {

	private String productName;
	private String productId;
	private String externalId;
	private String url;
	private String platformCode;
	private Division division;
	private AdminUser adminUser;
	private RegistrationDefinitionType registrationDefinitionType;
	private Set<ProductState> productStates;
	
	public String getProductName() {
		return productName;
	}

	public void setProductName(final String productName) {
		this.productName = productName;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(final String productId) {
		this.productId = productId;
	}

	public String getExternalId() {
		return externalId;
	}

	public void setExternalId(final String externalId) {
		this.externalId = externalId;
	}


	public Division getDivision() {
		return division;
	}

	public void setDivision(final Division division) {
		this.division = division;
	}

	public AdminUser getAdminUser() {
		return adminUser;
	}

	public void setAdminUser(final AdminUser adminUser) {
		this.adminUser = adminUser;
	}

	public RegistrationDefinitionType getRegistrationDefinitionType() {
		return registrationDefinitionType;
	}

	public void setRegistrationDefinitionType(final RegistrationDefinitionType registrationDefinitionType) {
		this.registrationDefinitionType = registrationDefinitionType;
	}

	public RegistrationDefinitionSearchCriteria productName(final String productName) {
		this.productName = productName;
		return this;
	}

	public RegistrationDefinitionSearchCriteria productId(final String productId) {
		this.productId = productId;
		return this;
	}

	public RegistrationDefinitionSearchCriteria externalId(final String externalId) {
		this.externalId = externalId;
		return this;
	}

	public RegistrationDefinitionSearchCriteria division(final Division division) {
		this.division = division;
		return this;
	}

	public RegistrationDefinitionSearchCriteria adminUser(final AdminUser adminUser) {
		this.adminUser = adminUser;
		return this;
	}

	public RegistrationDefinitionSearchCriteria registrationDefinitionType(final RegistrationDefinitionType registrationDefinitionType) {
		this.registrationDefinitionType = registrationDefinitionType;
		return this;
	}

	/**
	 * @return the productStates
	 */
	public Set<ProductState> getProductStates() {
		return productStates;
	}

	/**
	 * @param productStates the productStates to set
	 */
	public void setProductStates(Set<ProductState> productStates) {
		this.productStates = productStates;
	}

	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * @param url the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * @return the platformCode
	 */
	public String getPlatformCode() {
		return platformCode;
	}

	/**
	 * @param platformCode the PlatformCode to set
	 */
	public void setPlatformCode(String platformCode) {
		this.platformCode = platformCode;
	}
	
}
