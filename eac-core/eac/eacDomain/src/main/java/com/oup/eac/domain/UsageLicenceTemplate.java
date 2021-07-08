/**
 * 
 */
package com.oup.eac.domain;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * @author harlandd
 * 
 */
@Entity
@DiscriminatorValue(value = "USAGE")
public class UsageLicenceTemplate extends LicenceTemplate {

    private int allowedUsages;

    public UsageLicenceTemplate() {
		super();
	}

	public UsageLicenceTemplate(final UsageLicenceTemplate usageLicenceTemplate) {
    	super(usageLicenceTemplate);
    	this.allowedUsages = usageLicenceTemplate.getAllowedUsages();
    }
    
    /**
     * @return the allowedUsages
     */
	public int getAllowedUsages() {
        return allowedUsages;
    }

    /**
     * @param allowedUsages
     *            the allowedUsages to set
     */
	public void setAllowedUsages(final int allowedUsages) {
        this.allowedUsages = allowedUsages;
    }

    @Override
	public LicenceType getLicenceType() {
        return LicenceType.USAGE;
    }
}
