package com.oup.eac.domain;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
@DiscriminatorValue(value = "ACCOUNT_REGISTRATION")
public class AccountRegistrationDefinition extends RegistrationDefinition {

	@Override
	public RegistrationDefinitionType getRegistrationDefinitionType() {
		return RegistrationDefinitionType.ACCOUNT_REGISTRATION;
	}	
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "page_definition_id")
	private AccountPageDefinition pageDefinition;
	
	private Boolean validationRequired;

	/**
	 * @return the pageDefinition
	 */
	public AccountPageDefinition getPageDefinition() {
		return pageDefinition;
	}

	/**
	 * @param pageDefinition the pageDefinition to set
	 */
	public void setPageDefinition(AccountPageDefinition pageDefinition) {
		this.pageDefinition = pageDefinition;
	}

    /**
     * @return the validationRequired
     */
    public final Boolean isValidationRequired() {
        return validationRequired;
    }

    /**
     * @param validationRequired the validationRequired to set
     */
    public final void setValidationRequired(final Boolean validationRequired) {
        this.validationRequired = validationRequired;
    }
	
}
