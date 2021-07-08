package com.oup.eac.dto;

import java.io.Serializable;
import java.util.List;

import org.joda.time.DateTime;

/**
 * Contains licence information exchanged between eac and erights. 
 * 
 * Also contains some immutable values calculated by erights.
 * 
 * @author Ian Packard
 *
 */
public abstract class BaseLicenceDto implements Serializable {

	private String licenseId;
	
	private boolean enabled;
	
	private Integer licenceTemplateId;
	
	private List<String> productIds;
	
	private EnforceableProductDto products;
	
	private List<String> linkedProductIds;
	
	private LicenceDetailDto licenceDetail;
	
	private final boolean active; 
	
	private final boolean expired;
	
	private final DateTime expiryDateAndTime;
	
	protected final boolean hasInfo;
	
	private boolean awaitingValidation;
	
	private boolean denied;
	
	private boolean completed;
	
	public BaseLicenceDto(final String licenseId) {
		super();
		this.licenseId = licenseId;
	
		this.active = false;
		this.expired = false;
		this.expiryDateAndTime = null;

		this.hasInfo = false;		
	}
	
	/**
	 * Licence used for sending data to atypon.
	 */
	public BaseLicenceDto() {
		super();
		this.active = false;
		this.expired = false;
		this.expiryDateAndTime = null;
		
		this.hasInfo = false;
	}
	
	/**
	 * Creates licence based on immutable licence information provided by erights.
	 * 
	 * @param daysRemaining
	 * @param expired
	 * @param active
	 */
	public BaseLicenceDto(final String licenseId, final DateTime expiryDateAndTime, final boolean expired, final boolean active, 
			final boolean completed, final boolean awaitingValidation,  final boolean denied) {
		super();
		if (active && expired) throw new IllegalStateException("Licence cannot be active if it has expired");
		this.licenseId = licenseId;
		this.active = active;
		this.expired = expired;
		this.expiryDateAndTime = expiryDateAndTime;
		this.setAwaitingValidation(awaitingValidation);
		this.setDenied(denied);
		this.setCompleted(completed);
		this.hasInfo = true;
	}
	
	public String getLicenseId() {
		return licenseId;
	}

	public void setLicenseId(String licenseId) {
		this.licenseId = licenseId;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public Integer getLicenceTemplateId() {
		return licenceTemplateId;
	}

	public void setLicenceTemplateId(Integer licenceTemplateId) {
		this.licenceTemplateId = licenceTemplateId;
	}

	public List<String> getProductIds() {
		return productIds;
	}

	public void setProductIds(List<String> productIds) {
		this.productIds = productIds;
	}

	public LicenceDetailDto getLicenceDetail() {
		return licenceDetail;
	}
	
	public void setLicenceDetail(LicenceDetailDto licenceDetail) {
		this.licenceDetail = licenceDetail;
	}
	
	/**
	 * Is the license enabled and has it not expired. This 
	 * value is calculated by erights. For a local calculation 
	 * use isLocallyActive().
	 * 
	 * @return
	 */
	public boolean isActive() {
		if (!hasInfo) throw new IllegalStateException("This dto does not contain licence info");
		return active;
	}

	/**
	 * Erights calculated value. Shows the date and time this
	 * licence is due to expire according to erights.
	 * 
	 * @return
	 */
	public DateTime getExpiryDateAndTime() {
		if (!hasInfo) throw new IllegalStateException("This dto does not contain licence info");
		return expiryDateAndTime;
	}

	/**
	 * Expired if the end date of the licence has passed or the number of usages has been
	 * exceeded. Value calculated by erights.
	 * 
	 * @return
	 */
	public boolean isExpired() {
		if (!hasInfo) throw new IllegalStateException("This dto does not contain licence info");
		return expired;
	}

	/**
	 * @return the awaitingValidation
	 */
	public boolean isAwaitingValidation() {
		return awaitingValidation;
	}

	/**
	 * @param awaitingValidation the awaitingValidation to set
	 */
	public void setAwaitingValidation(boolean awaitingValidation) {
		this.awaitingValidation = awaitingValidation;
	}

	/**
	 * @return the denied
	 */
	public boolean isDenied() {
		return denied;
	}

	/**
	 * @param denied the denied to set
	 */
	public void setDenied(boolean denied) {
		this.denied = denied;
	}

	/**
	 * @return the completed
	 */
	public boolean isCompleted() {
		return completed;
	}

	/**
	 * @param completed the completed to set
	 */
	public void setCompleted(boolean completed) {
		this.completed = completed;
	}

	/**
	 * @return the linkedProductIds
	 */
	public List<String> getLinkedProductIds() {
		return linkedProductIds;
	}

	/**
	 * @param linkedProductIds the linkedProductIds to set
	 */
	public void setLinkedProductIds(List<String> linkedProductIds) {
		this.linkedProductIds = linkedProductIds;
	}

	public EnforceableProductDto getProducts() {
		return products;
	}

	public void setProducts(EnforceableProductDto products) {
		this.products = products;
	}
	
	
}
