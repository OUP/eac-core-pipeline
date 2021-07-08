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
@DiscriminatorValue(value = "PRODUCT_PAGE_DEFINITION")
public class ProductPageDefinition extends PageDefinition {

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "pageDefinition")
	private Set<ProductRegistrationDefinition> registrationDefinitions = new HashSet<ProductRegistrationDefinition>();
	
	@Override
	public PageDefinitionType getPageDefinitionType() {
		return PageDefinitionType.PRODUCT_PAGE_DEFINITION;
	}

	/**
	 * @return the registrationDefinitions
	 */
	public Set<ProductRegistrationDefinition> getRegistrationDefinitions() {
		return registrationDefinitions;
	}

	/**
	 * @param registrationDefinitions the registrationDefinitions to set
	 */
	public void setRegistrationDefinitions(
			Set<ProductRegistrationDefinition> registrationDefinitions) {
		this.registrationDefinitions = registrationDefinitions;
	}

}
