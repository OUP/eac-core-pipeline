package com.oup.eac.domain;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;

/**
 * The page definition.
 * 
 * @author harlandd
 */
@Entity
@DiscriminatorValue(value = "ACCOUNT_PAGE_DEFINITION")
public class AccountPageDefinition extends PageDefinition {

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "pageDefinition")
	private Set<AccountRegistrationDefinition> registrationDefinitions = new HashSet<AccountRegistrationDefinition>();
	
	@Override
	public PageDefinitionType getPageDefinitionType() {
		return PageDefinitionType.ACCOUNT_PAGE_DEFINITION;
	}

	/**
	 * @return the registrationDefinitions
	 */
	public Set<AccountRegistrationDefinition> getRegistrationDefinitions() {
		return registrationDefinitions;
	}

	/**
	 * @param registrationDefinitions the registrationDefinitions to set
	 */
	public void setRegistrationDefinitions(
			Set<AccountRegistrationDefinition> registrationDefinitions) {
		this.registrationDefinitions = registrationDefinitions;
	}
	
}
