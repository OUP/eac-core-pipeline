package com.oup.eac.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.oup.eac.domain.Division;
import com.oup.eac.domain.ExternalProductId;
import com.oup.eac.domain.LinkedProductNew;
import com.oup.eac.domain.Platform;
import com.oup.eac.domain.RegisterableProduct;

public class EnforceableProductDto implements Serializable {
	
	private String productId;
	
	private int divisionId ;
	
	private Division division;
	
	private String name;
	
	private List<EnforceableProductUrlDto> urls = new ArrayList<EnforceableProductUrlDto>();
	
	private List<LinkedProductNew> linkedProducts = new ArrayList<LinkedProductNew>();
	
	private List<String> parentIds;

	private boolean suspended = false;
	
	private String state;	
	
	public static enum RegisterableType {
		SELF_REGISTERABLE,
		ADMIN_REGISTERABLE;
	}
	
	public static enum ActivationStrategy {
		INSTANT,
		SELF,
		VALIDATED;
	}

	private String activationStrategy;
	
	private String validatorEmail;
	
	//protected String eacId;

	protected String landingPage;

	protected String homePage;

	protected String adminEmail;

	protected String sla;

	protected String registerableType;
	
	protected String registrationDefinitionType;
	
	protected LicenceDto licenceDetail;
	
	/*Added for externalId with getters and setters*/
	private List<ExternalProductId> externalIds = new ArrayList<ExternalProductId>();
	
	private List<Platform> platformList = new ArrayList<Platform>();
	
	private Boolean confirmationEmailEnabled = Boolean.TRUE;
	
	public List<ExternalProductId> getExternalIds() {
		return externalIds;
	}

	/**
	 * @param externalIds the externalIds to set
	 */
	public void setExternalIds(List<ExternalProductId> externalIds) {
		this.externalIds = externalIds;
	}
	
	
	/**
	 * @return the externalIds
	 */
	
	public List<LinkedProductNew> getLinkedProducts() {
		return linkedProducts;
	}

	public void setLinkedProducts(List<LinkedProductNew> linkedProducts) {
		this.linkedProducts = linkedProducts;
	}
	
	public LicenceDto getLicenceDetail() {
		return licenceDetail;
	}

	public void setLicenceDetail(LicenceDto licenceDetail) {
		this.licenceDetail = licenceDetail;
	}

	/**
	 * @return the state
	 */
	public String getState() {
		return state;
	}

	/**
	 * @param state the state to set
	 */
	public void setState(String state) {
		this.state = state;
	}

	public EnforceableProductDto(final String productId, final EnforceableProductDto enforceableProductDto) {
		this.productId = productId;
		this.name = enforceableProductDto.name;
		this.urls = new ArrayList<EnforceableProductUrlDto>(enforceableProductDto.urls.size());
		for (EnforceableProductUrlDto url : enforceableProductDto.urls) {
			urls.add(new EnforceableProductUrlDto(url));
		}
		this.platformList = enforceableProductDto.getPlatformList() ;
		this.parentIds = enforceableProductDto.parentIds;
	}
	
	public EnforceableProductDto(final String productId, final String name, final List<String> parentIds, final boolean suspended, final List<EnforceableProductUrlDto> urls) {
		this.productId = productId;
		this.name = name;
		this.urls = urls;	
		this.parentIds = parentIds;
		this.suspended = suspended;
	}
	
	public EnforceableProductDto(final String productId, final String name, final List<String> parentIds, final boolean suspended,final String state, final List<EnforceableProductUrlDto> urls, List<ExternalProductId> externalIds) {
		this.productId = productId;
		this.name = name;
		this.urls = urls;	
		this.parentIds = parentIds;
		this.suspended = suspended;
		this.state = state;
		this.externalIds=externalIds;
		
	}
	
	public EnforceableProductDto(final String productId, final String name, final boolean suspended,final String state, final List<EnforceableProductUrlDto> urls, List<ExternalProductId> externalIds) {
		this.productId = productId;
		this.name = name;
		this.urls = urls;	
		this.suspended = suspended;
		this.state = state;
		this.externalIds=externalIds;
	}
	
	public EnforceableProductDto(final String name, final List<String> parentIds, final List<EnforceableProductUrlDto> urls) {
		this.name = name;
		this.urls = urls;	
		this.parentIds = parentIds;
	}
	
	public EnforceableProductDto(final String name, final List<EnforceableProductUrlDto> urls) {
		this.name = name;
		this.urls = urls;			
	}
	
	public EnforceableProductDto(final String name) {
		this.name = name;
	}
	
	public EnforceableProductDto() {
		
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

	public List<EnforceableProductUrlDto> getUrls() {
		return urls;
	}

	public void setUrls(List<EnforceableProductUrlDto> urls) {
		this.urls = urls;
	}

	
	
	public List<String> getParentIds() {
		return parentIds;
	}

	public void setParentIds(List<String> parentIds) {
		this.parentIds = parentIds;
	}

	public void addEnforceableProductUrl(final EnforceableProductUrlDto url) {
		this.getUrls().add(url);
	}

	public boolean isSuspended() {
		return suspended ;
	}

	public void setSuspended(boolean suspended) {
		this.suspended = suspended;
	}
	
	public void suspend() {
		setSuspended(true);
	}
	
	public void unsuspend() {
		setSuspended(false);
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

	public String getAdminEmail() {
		return adminEmail;
	}

	public void setAdminEmail(String adminEmail) {
		this.adminEmail = adminEmail;
	}

	public String getSla() {
		return sla;
	}

	public void setSla(String sla) {
		this.sla = sla;
	}

	public String getRegisterableType() {
		return registerableType;
	}

	public void setRegisterableType(String registerableType) {
		this.registerableType = registerableType;
	}

	public String getActivationStrategy() {
		return activationStrategy;
	}

	public void setActivationStrategy(String activationStrategy) {
		this.activationStrategy = activationStrategy;
	}

	public String getValidatorEmail() {
		return validatorEmail;
	}

	public void setValidatorEmail(String validatorEmail) {
		this.validatorEmail = validatorEmail;
	}
	

	public Boolean getConfirmationEmailEnabled() {
		return confirmationEmailEnabled;
	}

	public void setConfirmationEmailEnabled(Boolean confirmationEmailEnabled) {
		this.confirmationEmailEnabled = confirmationEmailEnabled;
	}
	
	

	public String getRegistrationDefinitionType() {
		return registrationDefinitionType;
	}

	public void setRegistrationDefinitionType(String registrationDefitionType) {
		this.registrationDefinitionType = registrationDefitionType;
	}

	
	public int getDivisionId() {
		return divisionId;
	}

	public void setDivisionId(int divisionId) {
		this.divisionId = divisionId;
	}
		
	public Division getDivision() {
		return division;
	}

	public void setDivision(Division division) {
		this.division = division;
	}

	public void mergeProductChanges(RegisterableProduct product) {
		this.productId = product.getId();
		this.externalIds= new ArrayList<ExternalProductId>(product.getExternalIds());
		//product de-duplication
		/*this.landingPage=product.getLandingPage();
		this.homePage=product.getHomePage();
		this.adminEmail=product.getEmail();
		this.sla=product.getServiceLevelAgreement();
		this.state=product.getState().toString();
		this.registerableType=product.getRegisterableType().toString();
		this.externalIds= product.getExternalIds();
		this.name = product.getProductName() ;
		this.suspended = product.getState().getErightsSuspend() ;*/
	}

	public List<Platform> getPlatformList() {
		return platformList;
	}

	public void setPlatformList(List<Platform> platformList) {
		this.platformList = platformList;
	}
	
	
}
