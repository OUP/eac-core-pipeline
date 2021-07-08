package com.oup.eac.dto;



public class StandardConcurrentLicenceDetailDto extends LicenceDetailDto {

	private int totalConcurrency;
	
	private int userConcurrency;

	public StandardConcurrentLicenceDetailDto(int totalConcurrency,
			int userConcurrency) {
		super();
		this.totalConcurrency = totalConcurrency;
		this.userConcurrency = userConcurrency;
	}

	public int getTotalConcurrency() {
		return totalConcurrency;
	}

	public void setTotalConcurrency(int totalConcurrency) {
		this.totalConcurrency = totalConcurrency;
	}

	public int getUserConcurrency() {
		return userConcurrency;
	}

	public void setUserConcurrency(int userConcurrency) {
		this.userConcurrency = userConcurrency;
	}
	
	@Override
	public LicenceType getLicenceType() {
		return LicenceType.CONCURRENT;
	}
}
