package com.oup.eac.admin.beans;

import java.io.Serializable;

import com.oup.eac.dto.EacGroupsSearchCriteria;

public class EacGroupsSearchBean extends PageTrackingBean implements Serializable {

	private String searchTerm;
	private String searchBy;
	public String getSearchTerm() {
		return searchTerm;
	}
	public void setSearchTerm(String searchTerm) {
		this.searchTerm = searchTerm;
	}
	public String getSearchBy() {
		return searchBy;
	}
	public void setSearchBy(String searchBy) {
		this.searchBy = searchBy;
	}
	
	public EacGroupsSearchCriteria toProductGroupSearchCriteria(){
		EacGroupsSearchCriteria searchCriteria= new EacGroupsSearchCriteria();
		
		if ("groupName".equals(searchBy)) {
			searchCriteria.setGroupName(searchTerm);
		}
		
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
}
