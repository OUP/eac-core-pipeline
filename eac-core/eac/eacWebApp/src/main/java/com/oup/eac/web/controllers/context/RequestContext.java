package com.oup.eac.web.controllers.context;

import com.oup.eac.domain.AccountRegistrationDefinition;
import com.oup.eac.domain.Customer;
import com.oup.eac.domain.ProductRegistrationDefinition;
import com.oup.eac.domain.RegisterableProduct;
import com.oup.eac.domain.Registration;

public class RequestContext {

	private String cookieValue;
	private Customer customer;
	private String userRequestedUrl;
	private RegisterableProduct registerableProduct;
	private ProductRegistrationDefinition productRegistrationDefinition;
	private AccountRegistrationDefinition accountRegistrationDefinition;
	private Registration<ProductRegistrationDefinition> productRegistration;
	
	/**
	 * @return the cookieValue
	 */
	public final String getCookieValue() {
		return cookieValue;
	}
	/**
	 * @param cookieValue the cookieValue to set
	 */
	public final void setCookieValue(String cookieValue) {
		this.cookieValue = cookieValue;
	}
	/**
	 * @return the customer
	 */
	public final Customer getCustomer() {
		return customer;
	}
	/**
	 * @param customer the customer to set
	 */
	public final void setCustomer(Customer customer) {
		this.customer = customer;
	}
	/**
	 * @return the userRequestedUrl
	 */
	public final String getUserRequestedUrl() {
		return userRequestedUrl;
	}
	/**
	 * @param userRequestedUrl the userRequestedUrl to set
	 */
	public final void setUserRequestedUrl(String userRequestedUrl) {
		this.userRequestedUrl = userRequestedUrl;
	}
	/**
	 * @return the productRegistrationDefinition
	 */
	public final ProductRegistrationDefinition getProductRegistrationDefinition() {
		return productRegistrationDefinition;
	}
	/**
	 * @param registrationDefinition the registrationDefinition to set
	 */
	public final void setProductRegistrationDefinition(
			ProductRegistrationDefinition productRegistrationDefinition) {
		this.productRegistrationDefinition = productRegistrationDefinition;
	}
	
	/**
	 * @return the accountRegistrationDefinition
	 */
	public final AccountRegistrationDefinition getAccountRegistrationDefinition() {
		return accountRegistrationDefinition;
	}
	/**
	 * @param accountRegistrationDefinition the accountRegistrationDefinition to set
	 */
	public final void setAccountRegistrationDefinition(
			AccountRegistrationDefinition accountRegistrationDefinition) {
		this.accountRegistrationDefinition = accountRegistrationDefinition;
	}
	/**
	 * @return the productRegistration
	 */
	public final Registration<ProductRegistrationDefinition> getProductRegistration() {
		return productRegistration;
	}
	/**
	 * @param productRegistration the registration to set
	 */
	public final void setProductRegistration(Registration<ProductRegistrationDefinition> productRegistration) {
		this.productRegistration = productRegistration;
	}
	
	/**
	 * @return the registerableProduct
	 */
	public final RegisterableProduct getRegisterableProduct() {
		return registerableProduct;
	}
	
	/**
	 * @param registerableProduct the registerableProduct to set
	 */
	public final void setRegisterableProduct(RegisterableProduct registerableProduct) {
		this.registerableProduct = registerableProduct;
	}
}
