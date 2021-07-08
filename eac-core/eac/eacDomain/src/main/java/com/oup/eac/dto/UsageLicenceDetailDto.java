package com.oup.eac.dto;


/**
 * Detail of a usage licence.
 * 
 * @author Ian Packard
 *
 */
public class UsageLicenceDetailDto extends LicenceDetailDto {

	private int allowedUsages;
	private final int usagesRemaining;
	
	public UsageLicenceDetailDto() {
		super();
		this.usagesRemaining = -1;
	}
	
	public UsageLicenceDetailDto(final int usagesRemaining) {
		super();
		this.usagesRemaining = usagesRemaining;
	}

	public int getAllowedUsages() {
		return allowedUsages;
	}
	
	public void setAllowedUsages(int allowedUsages) {
		this.allowedUsages = allowedUsages;
	}
	
	public int getUsagesRemaining() {
		return usagesRemaining;
	}

	@Override
	public LicenceType getLicenceType() {
		return LicenceType.USAGE;
	}
}
