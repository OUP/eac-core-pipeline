package com.oup.eac.service.util;

import java.util.Map;
import java.util.Set;

import com.oup.eac.domain.Field;
import com.oup.eac.dto.RegistrationDto;
import com.oup.eac.service.ServiceLayerException;

/**
 * @author David Hay
 */
public  interface ProductRegistrationHelper {

	Map<String, Field> getRegistrationPageConfig(RegistrationDto registrationDto);

	Map<Field, String> getValidatedRegistrationPageData(
			Map<String, Field> registrationPageConfig,
			Map<String, String> registrationPageData, boolean strict) throws ServiceLayerException;
	
    Map<Field, String> getValidatedRegistrationPageData(
	            Map<String, Field> registrationPageConfig,
	            Map<String, String> registrationPageData, boolean strict, Set<String> ignoreReqdFields) throws ServiceLayerException;


	void populateRegistrationDtoWithValidatedData(
			Map<Field, String> validatedRegData, RegistrationDto registrationDto);
	
}
