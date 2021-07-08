package com.oup.eac.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.LocaleUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

@Entity
@DiscriminatorValue(value = "COUNTRY_MATCH")
public class CountryMatchRegistrationActivation extends RegistrationActivation {
	
	private static final Logger LOGGER = Logger.getLogger(CountryMatchRegistrationActivation.class);

	@OneToOne(fetch = FetchType.EAGER, optional = true)
	private RegistrationActivation matchedActivation;

	@OneToOne(fetch = FetchType.EAGER, optional = true)
	private RegistrationActivation unmatchedActivation;

	@Column(nullable = true)
	private String description;

	@Column(nullable = true)
	private String localeList;

	@Override
	public ActivationStrategy getActivationStrategy(final Locale... locale) {
		if (isMatched(locale)) {
			return matchedActivation.getActivationStrategy(locale);
		}

		return unmatchedActivation.getActivationStrategy(locale);
	}
	
	public boolean isMatched(final Locale... locale) {
		boolean matched = false;
		if (ArrayUtils.isNotEmpty(locale)) {
			if (StringUtils.isNotBlank(localeList)) {
				String country = locale[0].getCountry();
				if (StringUtils.isNotBlank(country)) {
					matched = isValidForCountry(country);
				} else {
					matched = isValidForLanguage(locale[0].getLanguage());
				}
			}
			LOGGER.info("Matching supplied locale '" + locale[0].toString() + "' against '" + localeList + "': match=" + matched);
		}
		return matched;
	}
	
	private boolean isValidForCountry(String country) {
		boolean isValid = false;
		for (Locale locale : getLocaleListAsLocales()) {
			if (locale.getCountry().equals(country)) {
				isValid = true;
				break;
			}
		}
		return isValid;
	}
	
	private boolean isValidForLanguage(String language) {
		boolean isValid = false;
		for (Locale locale : getLocaleListAsLocales()) {
			if (locale.getLanguage().equals(language)) {
				isValid = true;
				break;
			}
		}
		return isValid;
	}
	
	private List<Locale> getLocaleListAsLocales() {
		List<Locale> locales = new ArrayList<Locale>();
		String[] localeArray = localeList.split(",");
		for (String locale : localeArray) {
			locales.add(LocaleUtils.toLocale(locale.trim()));
		}
		return locales;
	}
	
	@Override
	public String getProperty(final String name, final Locale... locale) {
		if (isMatched(locale)) {
			return matchedActivation.getProperty(name, locale);
		}
		return unmatchedActivation.getProperty(name, locale);
	}

	public RegistrationActivation getMatchedActivation() {
		return matchedActivation;
	}

	public void setMatchedActivation(final RegistrationActivation matchedActivation) {
		this.matchedActivation = matchedActivation;
	}

	public RegistrationActivation getUnmatchedActivation() {
		return unmatchedActivation;
	}

	public void setUnmatchedActivation(final RegistrationActivation unmatchedActivation) {
		this.unmatchedActivation = unmatchedActivation;
	}

	public String getLocaleList() {
		return localeList;
	}

	public void setLocaleList(final String localeList) {
		List<String> sanitisedList = new ArrayList<String>();
		if (localeList != null) {
			String[] locales = localeList.split(",");
			for (int i = 0; i < locales.length; i++) {
				String locale = locales[i].trim();
				if (StringUtils.isNotBlank(locale)) {
					if (!sanitisedList.contains(locale)) {
						sanitisedList.add(locale);
					}
				}
			}
			Collections.sort(sanitisedList);
			this.localeList = StringUtils.join(sanitisedList, ",");
		} else {
			this.localeList = null;
		}
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(final String description) {
		this.description = description;
	}

	public void checkCircularReferences(final CountryMatchRegistrationActivationVisitor visitor) throws CircularCountryMatchException {
		visitor.visit(this);
	}

	@Override
	public String getName() {
		return "CountryMatchRegistrationActivation";
	}

	@Override
	public String toString() {
		return "CountryMatchRegistrationActivation [description=" + description + ", locale_list=" + localeList + "]";
	}
	
}
