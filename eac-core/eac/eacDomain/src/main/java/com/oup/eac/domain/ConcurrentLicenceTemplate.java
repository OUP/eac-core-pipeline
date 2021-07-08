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
@DiscriminatorValue(value = "CONCURRENT")
public class ConcurrentLicenceTemplate extends LicenceTemplate {

    private int totalConcurrency;
    
    private int userConcurrency;

    public ConcurrentLicenceTemplate() {
		super();
	}
	public ConcurrentLicenceTemplate(final ConcurrentLicenceTemplate concurrentLicenceTemplate) {
    	super(concurrentLicenceTemplate);
    	setTotalConcurrency(concurrentLicenceTemplate.getTotalConcurrency());
    	setUserConcurrency(concurrentLicenceTemplate.getUserConcurrency());
    }
    /**
     * @return the concurrency
     */
	public int getTotalConcurrency() {
        return totalConcurrency;
    }

    /**
     * @param totalConcurrency
     *            the concurrency to set
     */
	public void setTotalConcurrency(final int totalConcurrency) {
        this.totalConcurrency = totalConcurrency;
    }

    @Override
	public LicenceType getLicenceType() {
        return LicenceType.CONCURRENT;
    }

	/**
	 * @return the userConcurrency
	 */
	public int getUserConcurrency() {
		return userConcurrency;
	}

	/**
	 * @param userConcurrency the userConcurrency to set
	 */
	public void setUserConcurrency(int userConcurrency) {
		this.userConcurrency = userConcurrency;
	}

}
