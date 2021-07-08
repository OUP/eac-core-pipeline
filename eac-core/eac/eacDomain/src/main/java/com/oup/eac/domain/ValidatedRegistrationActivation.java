package com.oup.eac.domain;

import java.util.Locale;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue(value = "VALIDATED")
public class ValidatedRegistrationActivation extends RegistrationActivation {
	
	private String validatorEmail;

	/**
	 * @return the validatorEmail
	 */
	public String getValidatorEmail() {
		return validatorEmail;
	}

	/**
	 * @param validatorEmail the validatorEmail to set
	 */
	public void setValidatorEmail(String validatorEmail) {
		this.validatorEmail = validatorEmail;
	}

	@Override
	public ActivationStrategy getActivationStrategy(final Locale... locale) {
		return ActivationStrategy.VALIDATED;
	}

	@Override
	public String getName() {
		return "ValidatedRegistrationActivation";
	}
	
}
