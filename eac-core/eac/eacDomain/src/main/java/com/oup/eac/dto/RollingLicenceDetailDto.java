package com.oup.eac.dto;

import org.joda.time.DateTime;

import com.oup.eac.domain.RollingLicenceTemplate.RollingBeginOn;
import com.oup.eac.domain.RollingLicenceTemplate.RollingUnitType;

/**
 * Details of rolling lience in erights. Data calculated by erights is immutable.
 * 
 * @author Ian Packard
 *
 */
public class RollingLicenceDetailDto extends LicenceDetailDto {

	private RollingBeginOn beginOn;
	
	private RollingUnitType unitType;
	
	private int timePeriod;
	
	private final DateTime firstUse;
	
	public RollingLicenceDetailDto(final RollingBeginOn beginOn,
			final RollingUnitType unitType, final int timePeriod, final DateTime firstUse) {
		super();
		this.beginOn = beginOn;
		this.unitType = unitType;
		this.timePeriod = timePeriod;
		this.firstUse = firstUse;
	}

	public RollingBeginOn getBeginOn() {
		return beginOn;
	}

	public void setBeginOn(RollingBeginOn beginOn) {
		this.beginOn = beginOn;
	}

	public RollingUnitType getUnitType() {
		return unitType;
	}

	public void setUnitType(RollingUnitType unitType) {
		this.unitType = unitType;
	}

	public int getTimePeriod() {
		return timePeriod;
	}

	public void setTimePeriod(int timePeriod) {
		this.timePeriod = timePeriod;
	}

	@Override
	public LicenceType getLicenceType() {
		return LicenceType.ROLLING;
	}

	public DateTime getFirstUse() {
		return firstUse;
	}
}
