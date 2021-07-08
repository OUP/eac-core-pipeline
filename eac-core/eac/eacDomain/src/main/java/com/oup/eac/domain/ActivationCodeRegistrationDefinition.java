package com.oup.eac.domain;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Transient;

@Entity
@DiscriminatorValue(value = "ACTIVATION_CODE_REGISTRATION")
public class ActivationCodeRegistrationDefinition extends ProductRegistrationDefinition {

	//@OneToMany(fetch = FetchType.LAZY, mappedBy = "activationCodeRegistrationDefinition", cascade = CascadeType.ALL)
	@Transient
	private Set<ActivationCodeBatch> activationCodeBatchs = new HashSet<ActivationCodeBatch>();

	//@Override
	@Transient
	public RegistrationDefinitionType getRegistrationDefinitionType() {
		return RegistrationDefinitionType.ACTIVATION_CODE_REGISTRATION;
	}	
	
	/**
	 * @return the activationCodeBatchs
	 */
	public Set<ActivationCodeBatch> getActivationCodeBatchs() {
		return activationCodeBatchs;
	}

	/**
	 * @param activationCodeBatchs the activationCodeBatchs to set
	 */
	public void setActivationCodeBatchs(final Set<ActivationCodeBatch> activationCodeBatchs) {
		this.activationCodeBatchs = activationCodeBatchs;
	}
	
	public void addActivationCodeBatch(final ActivationCodeBatch activationCodeBatch) {
		activationCodeBatchs.add(activationCodeBatch);
		activationCodeBatch.setActivationCodeRegistrationDefinition(this);
	}
}
