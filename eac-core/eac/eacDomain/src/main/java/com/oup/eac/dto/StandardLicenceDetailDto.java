package com.oup.eac.dto;

public class StandardLicenceDetailDto extends LicenceDetailDto {

	@Override
	public LicenceType getLicenceType() {
		return LicenceType.STANDARD;
	}

}
