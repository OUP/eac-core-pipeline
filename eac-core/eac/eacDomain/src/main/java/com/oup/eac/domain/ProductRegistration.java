package com.oup.eac.domain;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 * @author harlandd A user registration.
 */
@Entity
@DiscriminatorValue(value = "PRODUCT")
public class ProductRegistration extends Registration<ProductRegistrationDefinition> {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "registration_definition_id", nullable = false)
    private ProductRegistrationDefinition registrationDefinition;
    
	@Override
	public RegistrationType getRegistrationType() {
		return RegistrationType.PRODUCT;
	}

    @Override
    public ProductRegistrationDefinition getRegistrationDefinition() {
        return registrationDefinition;
    }

    @Override
    public void setRegistrationDefinition(ProductRegistrationDefinition registrationDefinition) {
        this.registrationDefinition = registrationDefinition;
    }
}
