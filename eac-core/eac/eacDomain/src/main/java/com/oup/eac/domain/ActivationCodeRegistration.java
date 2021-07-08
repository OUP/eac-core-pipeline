package com.oup.eac.domain;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Transient;

@Entity
@DiscriminatorValue(value = "ACTIVATION_CODE")
public class ActivationCodeRegistration extends Registration<ActivationCodeRegistrationDefinition> {

	/*@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "activation_code_id")*/
	@Transient
	private ActivationCode activationCode;
	
	/*@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "registration_definition_id", nullable = false)*/
	@Transient
    private ActivationCodeRegistrationDefinition registrationDefinition;

	/**
	 * @return the activationCode
	 */
	public ActivationCode getActivationCode() {
		return activationCode;
	}

	/**
	 * @param activationCode the activationCode to set
	 */
	public void setActivationCode(final ActivationCode activationCode) {
		this.activationCode = activationCode;
	}

	@Override
	public com.oup.eac.domain.Registration.RegistrationType getRegistrationType() {
		return RegistrationType.ACTIVATION_CODE;
	}

	@Override
    public ActivationCodeRegistrationDefinition getRegistrationDefinition() {
        return registrationDefinition;
    }

    @Override
    public void setRegistrationDefinition(ActivationCodeRegistrationDefinition registrationDefinition) {
        this.registrationDefinition = registrationDefinition;
    }
    
}
