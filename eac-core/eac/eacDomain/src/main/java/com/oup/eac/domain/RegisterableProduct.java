package com.oup.eac.domain;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Transient;

import com.oup.eac.dto.EnforceableProductDto;

@Entity
@DiscriminatorValue(value = "REGISTERABLE")
public class RegisterableProduct extends Product {
	
	public static enum RegisterableType {
		SELF_REGISTERABLE,
		ADMIN_REGISTERABLE;
	}
	
	@Transient
	private RegisterableType registerableType;

	/*@Transient
	private Set<LinkedProduct> linkedProducts = new HashSet<LinkedProduct>();
	*/
	
	public RegisterableProduct() {
		
	}
	public RegisterableProduct(EnforceableProductDto enforcebleProduct) {
		if (enforcebleProduct.getRegisterableType().equals(RegisterableProduct.RegisterableType.ADMIN_REGISTERABLE.toString()))
			registerableType = RegisterableType.ADMIN_REGISTERABLE ; 
	}

	/**
	 * @return the linkedProducts
	 */
	/*public Set<LinkedProduct> getLinkedProducts() {
		return linkedProducts;
	}

	*//**
	 * @param linkedProducts the linkedProducts to set
	 *//*
	public void setLinkedProducts(final Set<LinkedProduct> linkedProducts) {
		this.linkedProducts = linkedProducts;
	}*/

	@Override
	public ProductType getProductType() {
		return ProductType.REGISTERABLE;
	}

	/**
	 * @return the registerableType
	 */
	public RegisterableType getRegisterableType() {
		return registerableType;
	}

	/**
	 * @param registerableType the registerableType to set
	 */
	public void setRegisterableType(RegisterableType registerableType) {
		this.registerableType = registerableType;
	}
	
	public boolean isSelfRegisterable() {
		return getRegisterableType() == RegisterableType.SELF_REGISTERABLE;
	}
	
	
}
