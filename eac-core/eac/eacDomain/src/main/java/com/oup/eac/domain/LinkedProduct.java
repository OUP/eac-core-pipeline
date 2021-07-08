package com.oup.eac.domain;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
@DiscriminatorValue(value = "LINKED")
public class LinkedProduct extends Product {

	/*public static enum ActivationMethod {
		PRE_PARENT,
		POST_PARENT;
	}*/
	
	
	//product de-duplication
	/*@ManyToOne(fetch = FetchType.LAZY, optional = true)
	@JoinColumn(name = "registerable_product_id")
	private RegisterableProduct registerableProduct;*/
	/*
	@Enumerated(EnumType.STRING)
	private ActivationMethod activationMethod;
	*/
	//private int productIds ;
	/**
	 * @return the activationMethod
	 */
	/*public ActivationMethod getActivationMethod() {
		return activationMethod;
	}
*/
	/**
	 * @param activationMethod the activationMethod to set
	 */
	/*public void setActivationMethod(final ActivationMethod activationMethod) {
		this.activationMethod = activationMethod;
	}*/
//product de-duplication
//	public int getProductIds() {
//		return productId;
//	}
//
//	public void setProductId(int productId) {
//		this.productId = productId;
//	}

	/**
	 * @return the registerableProduct
	 */
	/*public RegisterableProduct getRegisterableProduct() {
		return registerableProduct;
	}

	*//**
	 * @param registerableProduct the registerableProduct to set
	 *//*
	public void setRegisterableProduct(final RegisterableProduct registerableProduct) {
		this.registerableProduct = registerableProduct;
	}*/

	@Override
	public ProductType getProductType() {
		return ProductType.LINKED;
	}
	
}
