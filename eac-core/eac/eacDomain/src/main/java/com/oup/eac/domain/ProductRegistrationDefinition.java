package com.oup.eac.domain;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

@Entity
@DiscriminatorValue(value = "PRODUCT_REGISTRATION")
public class ProductRegistrationDefinition extends RegistrationDefinition {


	//@ManyToOne(fetch = FetchType.LAZY)
	//@JoinColumn(name = "licence_template_id")
	@Transient
	private LicenceTemplate licenceTemplate;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "page_definition_id")
	private ProductPageDefinition pageDefinition;
	
	@Transient
	private Boolean confirmationEmailEnabled = Boolean.TRUE;

	@Override
	public RegistrationDefinitionType getRegistrationDefinitionType() {
		return RegistrationDefinitionType.PRODUCT_REGISTRATION;
	}	
	
	/**
	 * @return the pageDefinition
	 */
	public ProductPageDefinition getPageDefinition() {
		return pageDefinition;
	}

	/**
	 * @param pageDefinition the pageDefinition to set
	 */
	public void setPageDefinition(ProductPageDefinition pageDefinition) {
		this.pageDefinition = pageDefinition;
	}
	
	/**
	 * @return the licenceTemplate
	 */
	public LicenceTemplate getLicenceTemplate() {
		return licenceTemplate;
	}

	/**
	 * @param licenceTemplate the licenceTemplate to set
	 */
	public void setLicenceTemplate(final LicenceTemplate licenceTemplate) {
		this.licenceTemplate = licenceTemplate;
	}

    /**
     * @return the confirmationEmailEnabled
     */
    public Boolean isConfirmationEmailEnabled() {
        return confirmationEmailEnabled;
    }

    /**
     * @param confirmationEmailEnabled the confirmationEmailEnabled to set
     */
    public void setConfirmationEmailEnabled(Boolean confirmationEmailEnabled) {
        this.confirmationEmailEnabled = confirmationEmailEnabled;
    }
}
