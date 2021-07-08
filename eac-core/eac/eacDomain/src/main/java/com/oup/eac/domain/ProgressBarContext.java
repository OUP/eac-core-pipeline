package com.oup.eac.domain;

import java.util.Locale;

public class ProgressBarContext {

	private final String page;
	private final Customer customer;
	private final ActivationCode activationCode;
	private final RegisterableProduct registerableProduct;
	private final String registrationId;
	private final Locale locale;

	public ProgressBarContext(
			final String page, 
			final Customer customer, 
			final ActivationCode activationCode, 
			final RegisterableProduct registerableProduct, 
			final String registrationId,
			final Locale locale) {
		this.page = page;
		this.customer = customer;
		this.activationCode = activationCode;
		this.registerableProduct = registerableProduct;
		this.registrationId = registrationId;
		this.locale = locale;
	}

	public String getPage() {
		return page;
	}

	public Customer getCustomer() {
		return customer;
	}

	public ActivationCode getActivationCode() {
		return activationCode;
	}

	public RegisterableProduct getRegisterableProduct() {
		return registerableProduct;
	}

	public String getRegistrationId() {
		return registrationId;
	}

	public Locale getLocale() {
		return locale;
	}

	@Override
	public String toString() {
		return "ProgressBarContext [page=" + page + ", customer=" + customer + ", activationCode=" + activationCode + ", registerableProduct="
				+ registerableProduct + ", registrationId=" + registrationId + ", locale=" + locale + "]";
	}
	
}
