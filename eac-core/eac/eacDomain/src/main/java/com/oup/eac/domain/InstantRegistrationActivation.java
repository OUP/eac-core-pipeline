package com.oup.eac.domain;

import java.util.Locale;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue(value = "INSTANT")
public class InstantRegistrationActivation extends RegistrationActivation {

	@Override
	public ActivationStrategy getActivationStrategy(final Locale... locale) {
		return ActivationStrategy.INSTANT;
	}

	@Override
	public String getName() {
		return "InstantRegistrationActivation";
	}
	
}
