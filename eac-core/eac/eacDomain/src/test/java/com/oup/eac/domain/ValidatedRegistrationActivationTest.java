package com.oup.eac.domain;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class ValidatedRegistrationActivationTest {

	@Test
	public void shouldReturnValidatorEmailAddressFromValidatedRegistrationActivation() {
		ValidatedRegistrationActivation validatedRegistrationActivation = new ValidatedRegistrationActivation();
		validatedRegistrationActivation.setValidatorEmail("somebody@somewhere.com");
		
		String validatorEmail = validatedRegistrationActivation.getProperty("validatorEmail");
		
		assertThat(validatorEmail, equalTo("somebody@somewhere.com"));
	}
}
