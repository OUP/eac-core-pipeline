package com.oup.eac.admin.beans;

import java.io.Serializable;

import com.oup.eac.dto.RegistrationDefinitionSearchCriteria;

public class ProductSearchBean extends PageTrackingBean implements Serializable {

	private String searchTerm;
	private String searchBy;
	private String divisionType;
	
	private String exampleUrl;
	
	private String encodedExampleUrl;
	
	private String platformCode;
	
	public String getSearchTerm() {
		return searchTerm;
	}

	public void setSearchTerm(final String searchTerm) {
		this.searchTerm = searchTerm;
	}

	public String getSearchBy() {
		return searchBy;
	}

	public void setSearchBy(final String searchBy) {
		this.searchBy = searchBy;
	}

	
	public String getDivisionType() {
		return divisionType;
	}

	public void setDivisionType(final String divisionType) {
		this.divisionType = divisionType;
	}
	
	public RegistrationDefinitionSearchCriteria toRegistrationDefinitionSearchCriteria() {
		RegistrationDefinitionSearchCriteria searchCriteria = new RegistrationDefinitionSearchCriteria();
		
		if ("productName".equals(searchBy)) {
			searchCriteria.setProductName(searchTerm);
		}
		
		if ("productId".equals(searchBy)) {
			searchCriteria.setProductId(searchTerm);
		}
		
		if ("externalId".equals(searchBy)) {
			searchCriteria.setExternalId(searchTerm);
		}
		
		return searchCriteria;
	}

	public String getExampleUrl() {
		return exampleUrl;
	}

	public void setExampleUrl(String exampleUrl) {
		this.exampleUrl = exampleUrl;
	}

	public String getEncodedExampleUrl() {
		return encodedExampleUrl;
	}

	public void setEncodedExampleUrl(String encodedExampleUrl) {
		this.encodedExampleUrl = encodedExampleUrl;
	}

	public String getPlatformCode() {
		return platformCode;
	}

	public void setPlatformCode(String platformCode) {
		this.platformCode = platformCode;
	}

	

}
