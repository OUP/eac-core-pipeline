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
@DiscriminatorValue(value = "STANDARD")
public class StandardLicenceTemplate extends LicenceTemplate {

	public StandardLicenceTemplate() {
		super();
	}	
	
    @Override
	public LicenceType getLicenceType() {
        return LicenceType.STANDARD;
    }
}
