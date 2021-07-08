package com.oup.eac.admin.validators;

import java.util.Locale;

import org.apache.commons.lang.StringUtils;
import org.springframework.validation.Errors;

public final class LocaleValidatorHelper {
	
	private final static String[] ISO_COUNTRIES = Locale.getISOCountries();
	private final static String[] ISO_LANGUAGES = Locale.getISOLanguages();
	
	private LocaleValidatorHelper() {
	}

	public static void validateISOLanguage(final String language, final String field, final Errors errors) {
		boolean validLang = false;
		for (String isoLang : ISO_LANGUAGES) {
			if (StringUtils.equals(isoLang, language)) {
				validLang = true;
				break;
			}
		}
		if (!validLang) {
			errors.rejectValue(field, "error.locale.language.notValid");
		}
	}
	
	public static void validateISOCountry(final String country, final String field, final Errors errors) {
		boolean validCountry = false;
		for (String isoCountry : ISO_COUNTRIES) {
			if (StringUtils.equals(isoCountry, country)) {
				validCountry = true;
				break;
			}
		}
		if (!validCountry) {
			errors.rejectValue(field, "error.locale.country.notValid");
		}
	}
}
