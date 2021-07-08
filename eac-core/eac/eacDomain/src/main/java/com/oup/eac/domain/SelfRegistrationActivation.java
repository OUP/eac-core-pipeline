package com.oup.eac.domain;

import java.util.Locale;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue(value = "SELF")
public class SelfRegistrationActivation extends RegistrationActivation {

	@Override
	public ActivationStrategy getActivationStrategy(final Locale...locale) {
		return ActivationStrategy.SELF;
	}

	@Override
	public String getName() {
		return "SelfRegistrationActivation";
	}
	
}
