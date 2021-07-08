package com.oup.eac.domain;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

/**
 * @author harlandd
 * 
 */
@Entity
@DiscriminatorValue(value = "ROLLING")
public class RollingLicenceTemplate extends LicenceTemplate {

    public enum RollingUnitType {
        YEAR,
        MONTH,
        WEEK,
        DAY,
        HOUR,
        MINUTE,
        SECOND,
        MILLISECOND;
    }
    
    public enum RollingBeginOn {
        FIRST_USE,
        CREATION;
    }
    
    @Enumerated(EnumType.STRING)
    private RollingUnitType unitType;
    
    @Enumerated(EnumType.STRING)
    private RollingBeginOn beginOn;
    
    private int timePeriod;

    public RollingLicenceTemplate() {
		super();
	}

	public RollingLicenceTemplate(final RollingLicenceTemplate rollingLicenceTemplate) {
    	super(rollingLicenceTemplate);
    	this.timePeriod = rollingLicenceTemplate.getTimePeriod();
    }
    
    /**
     * @return the timePeriod
     */
	public int getTimePeriod() {
        return timePeriod;
    }

    /**
     * Sets the time period for this licence.
     * 
     * @see com.oup.eac.integration.facade.impl.ErightsFacadeImpl#getOupRollingLicenseData()
     * 
     * @param timePeriod
     *            the timePeriod to set
     */
	public void setTimePeriod(final int timePeriod) {
        this.timePeriod = timePeriod;
    }

    @Override
	public LicenceType getLicenceType() {
        return LicenceType.ROLLING;
    }

    public RollingUnitType getUnitType() {
        return unitType;
    }

    public void setUnitType(RollingUnitType unitType) {
        this.unitType = unitType;
    }

    public RollingBeginOn getBeginOn() {
        return beginOn;
    }

    public void setBeginOn(RollingBeginOn beginOn) {
        this.beginOn = beginOn;
    }
}
