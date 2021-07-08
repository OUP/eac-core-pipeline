package com.oup.eac.domain;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "registration_definition_type", discriminatorType = DiscriminatorType.STRING, length = 50)
@Table(uniqueConstraints = {
		@UniqueConstraint( columnNames = 
		{"product_id", "registration_definition_type"})
	}
)
public abstract class RegistrationDefinition extends BaseDomainObject {

	public static enum RegistrationDefinitionType {
		ACTIVATION_CODE_REGISTRATION,
		PRODUCT_REGISTRATION,
		ACCOUNT_REGISTRATION;
	}
	@Column(name="product_id")
	private String productId;
	
	@Transient
	private Product product;
	
	@Transient
	private EacGroups eacGroup;

	@Transient
	private RegistrationActivation registrationActivation;
	
	public abstract RegistrationDefinitionType getRegistrationDefinitionType();

	/**
	 * @return the registrationActivation
	 */
	public RegistrationActivation getRegistrationActivation() {
		return registrationActivation;
	}

	/**
	 * @param registrationActivation the registrationActivation to set
	 */
	public void setRegistrationActivation(final RegistrationActivation registrationActivation) {
		this.registrationActivation = registrationActivation;
	}
	
	/**
	 * @return the product
	 */
	public Product getProduct() {
		return product;
	}

	/**
	 * @param product the product to set
	 */
	public void setProduct(final Product product) {
		this.product = product;
	}

	public EacGroups getEacGroup() {
		return eacGroup;
	}

	public void setEacGroup(EacGroups eacGroup) {
		this.eacGroup = eacGroup;
	}

	/**
	 * @return the productId
	 */
	public String getProductId() {
		return productId;
	}

	/**
	 * @param productId the productId to set
	 */
	public void setProductId(String productId) {
		this.productId = productId;
	}

}
