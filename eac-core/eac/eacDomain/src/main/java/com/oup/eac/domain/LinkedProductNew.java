package com.oup.eac.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class LinkedProductNew implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private String productId ;
	
	private String name ;
	
	
	private String parentId ;
	
	private Division division;

	protected String landingPage;

	protected String homePage;
	
	private List<ExternalProductId> externalIds = new ArrayList<ExternalProductId>();
	
	public String getParentId() {
		return parentId;
	}
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
	
	public String getProductId() {
		return productId;
	}
	public void setProductId(String productId) {
		this.productId = productId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Division getDivision() {
		return division;
	}
	public void setDivision(Division division) {
		this.division = division;
	}
	public String getLandingPage() {
		return landingPage;
	}
	public void setLandingPage(String landingPage) {
		this.landingPage = landingPage;
	}
	public String getHomePage() {
		return homePage;
	}
	public void setHomePage(String homePage) {
		this.homePage = homePage;
	}
	public List<ExternalProductId> getExternalIds() {
		return externalIds;
	}
	public void setExternalIds(List<ExternalProductId> externalIds) {
		this.externalIds = externalIds;
	}
	
}
