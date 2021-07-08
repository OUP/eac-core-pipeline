package com.oup.eac.admin.validators;

import org.easymock.EasyMock;
import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;
import org.springframework.validation.Errors;

import com.oup.eac.dto.LicenceDto;
import com.oup.eac.dto.StandardConcurrentLicenceDetailDto;

public class LicencePropertyValidatorHelperTest {
	
	private Errors mockErrors;
	
	@Before
	public void setUp() {
		mockErrors = EasyMock.createMock(Errors.class);
	}
	
	@Test
	public void shouldNotValidateWhenEndDateIsBeforeStartDate() {
		LicenceDto licence = new LicenceDto();
		licence.setStartDate(new LocalDate());
		licence.setEndDate(new LocalDate(2012, 1, 1));
		licence.setLicenceDetail(new StandardConcurrentLicenceDetailDto(1, 1));
		
		mockErrors.reject("error.licence.endDate.beforeStartDate");
		EasyMock.replay(mockErrors);
		
		LicencePropertyValidatorHelper.validateLicenceDto(licence, mockErrors);

		EasyMock.verify(mockErrors);
	}
}
