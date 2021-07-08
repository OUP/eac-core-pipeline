package com.oup.eac.dto;

import java.util.Locale;

import com.oup.eac.domain.Customer;
import com.oup.eac.domain.ProductRegistrationDefinition;
import com.oup.eac.domain.Registration;

public class RegistrationActivationDto {

	private final Customer customer;
	private final Locale locale;
	private final String originalUrl;
	private final Registration<? extends ProductRegistrationDefinition> registration;
	private final ProductRegistrationDefinition registrationDef;
	private Boolean isAdmin=false;
	
	public RegistrationActivationDto(final Customer customer, final Locale locale, final String originalUrl,
			final Registration<? extends ProductRegistrationDefinition> registration, final ProductRegistrationDefinition registrationDef) {
		this.customer = customer;
		this.locale = locale;
		this.originalUrl = originalUrl;
		this.registration = registration;
		this.registrationDef = registrationDef;
	}

	public Customer getCustomer() {
		return customer;
	}

	public Locale getLocale() {
		return locale;
	}

	public String getOriginalUrl() {
		return originalUrl;
	}

	public Registration<? extends ProductRegistrationDefinition> getRegistration() {
		return registration;
	}

	public ProductRegistrationDefinition getRegistrationDef() {
		return registrationDef;
	}

	public Boolean getIsAdmin() {
		return isAdmin;
	}

	public void setIsAdmin(Boolean isAdmin) {
		this.isAdmin = isAdmin;
	}
	
	@Override
	public String toString() {
		return "RegistrationActivationDto [customer=" + customer + ", locale=" + locale + ", orignalUrl=" + originalUrl + ", registration=" + registration
				+ ", registrationDef=" + registrationDef + "]";
	}

}
