package com.oup.eac.domain;

import java.io.UnsupportedEncodingException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.mail.internet.InternetAddress;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Transient;

import org.hibernate.annotations.Index;

import com.oup.eac.dto.EnforceableProductUrlDto;

/**
 * A product definition.
 * 
 * @author harlandd
 */
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "product_type", discriminatorType = DiscriminatorType.STRING)
public abstract class Product extends OUPBaseDomainObject {

	public static enum ProductState {

		ACTIVE(false),
		SUSPENDED(true),
		RETIRED(true),
		REMOVED(true);

		private boolean eRightsSuspend;

		ProductState(boolean erightsSuspend) {
			this.eRightsSuspend = erightsSuspend;
		}

		public boolean getErightsSuspend() {
			return this.eRightsSuspend;
		}
	}

	public static enum ProductType {
		REGISTERABLE,
		LINKED;
	}
	
	//@Index (name = "erights_id_idx")
	@Transient
	private Integer erightsId;	
	
	@Transient
	private String landingPage;

	@Transient
	private String homePage;

	@Transient
	private String email;

	@Transient
	private String serviceLevelAgreement;

	@Transient
    private Division division;

	@Transient
	private Set<ExternalProductId> externalIds = new HashSet<ExternalProductId>();

	@Transient
	public abstract ProductType getProductType();

	@Transient
	Set<EacGroups> eacGroups= new HashSet<EacGroups>();

	@Transient
	private ProductState state;

	@Transient
	private String productName;
	
	@Transient
	private String activationStrategy;
	
	@Transient
	private Product linkedProduct;	
	
	@Transient
	private List<Product> linkedProducts;	

	@Transient
	private List<EnforceableProductUrlDto> productUrls ;
	
	@Transient
	private String validatorEmail ;
	
	
	public Product getLinkedProduct() {
		return linkedProduct;
	}

	public void setLinkedProduct(Product linkedProduct) {
		this.linkedProduct = linkedProduct;
	}

	public Set<EacGroups> getEacGroups() {
		return eacGroups;
	}

	public void setEacGroups(Set<EacGroups> eacGroups) {
		this.eacGroups = eacGroups;
	}
	
	public String getActivationStrategy() {
		return activationStrategy;
	}

	public void setActivationStrategy(String activationStrategy) {
		this.activationStrategy = activationStrategy;
	}

	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	public InternetAddress getEmailInternetAddress() throws UnsupportedEncodingException {
		return new InternetAddress(getEmail(), getEmail());
	}

	/**
	 * @return the serviceLevelAgreement
	 */
	public String getServiceLevelAgreement() {
		return serviceLevelAgreement;
	}

	/**
	 * @param serviceLevelAgreement the serviceLevelAgreement to set
	 */
	public void setServiceLevelAgreement(String serviceLevelAgreement) {
		this.serviceLevelAgreement = serviceLevelAgreement;
	}

	/**
	 * @return the landing page
	 */
	public String getLandingPage() {
		return landingPage;
	}

	/**
	 * @param landingPage
	 *            the landing page
	 */
	public void setLandingPage(final String landingPage) {
		this.landingPage = landingPage;
	}

	public String getHomePage() {
		return homePage;
	}

	public void setHomePage(String homePage) {
		this.homePage = homePage;
	}

	/**
	 * Gets the external ids.
	 * 
	 * @return the external ids
	 */
	public Set<ExternalProductId> getExternalIds() {
		return externalIds;
	}

	/**
	 * Sets the external ids.
	 * 
	 * @param externalIds1
	 *            the new external ids
	 */
	public void setExternalIds(final Set<ExternalProductId> externalIds1) {
		this.externalIds = externalIds1;
	}

	public ProductState getState() {
		return state;
	}

	public void setState(ProductState state) {
		this.state = state;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}
	
	/**
     * @return the erights id
     */
	public Integer getErightsId() {
        return erightsId;
    }

    /**
     * @param erightsId
     *            the erights id
     */
	public void setErightsId(final Integer erightsId) {
        this.erightsId = erightsId;
    }
	

	public Division getDivision() {
		return division;
	}

	public void setDivision(Division division) {
		this.division = division;
	}

	public List<Product> getLinkedProducts() {
		return linkedProducts;
	}

	public void setLinkedProducts(List<Product> linkedProducts) {
		this.linkedProducts = linkedProducts;
	}

	public List<EnforceableProductUrlDto> getProductUrls() {
		return productUrls;
	}

	public void setProductUrls(List<EnforceableProductUrlDto> productUrls) {
		this.productUrls = productUrls;
	}

	public String getValidatorEmail() {
		return validatorEmail;
	}

	public void setValidatorEmail(String validatorEmail) {
		this.validatorEmail = validatorEmail;
	}

	
}
