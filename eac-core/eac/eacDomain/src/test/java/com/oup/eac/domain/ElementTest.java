package com.oup.eac.domain;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

public class ElementTest {

	private Element japaneseElement = new Element();
	private Element koreanElement = new Element();

	@Before
	public void setUp() {
		japaneseElement.setElementCountryRestrictions(createElementCountryRestrictions(new Locale("ja", "JP")));
		Set<ElementCountryRestriction> koreanRestrictions = new HashSet<ElementCountryRestriction>();
		koreanRestrictions.addAll(createElementCountryRestrictions(new Locale("ko", "KR")));
		koreanRestrictions.addAll(createElementCountryRestrictions(new Locale("uk", "UA")));
		koreanElement.setElementCountryRestrictions(koreanRestrictions);
	}

	@Test
	public void testValidForLocaleCombinations() {
		assertThat(japaneseElement.isValidForLocale(new Locale("ja")), equalTo(true));
		assertThat(japaneseElement.isValidForLocale(new Locale("ja", "JP")), equalTo(true));
		assertThat(japaneseElement.isValidForLocale(new Locale("ja", "JP", "JP")), equalTo(true));
		assertThat(japaneseElement.isValidForLocale(new Locale("en", "JP")), equalTo(true));
		assertThat(japaneseElement.isValidForLocale(new Locale("ja", "GB")), equalTo(false));
		assertThat(japaneseElement.isValidForLocale(new Locale("en")), equalTo(false));
		assertThat(japaneseElement.isValidForLocale(new Locale("en", "GB")), equalTo(false));
		assertThat(japaneseElement.isValidForLocale(new Locale("ko")), equalTo(false));
		assertThat(japaneseElement.isValidForLocale(new Locale("ko", "KR")), equalTo(false));
		assertThat(japaneseElement.isValidForLocale(new Locale("en", "KR")), equalTo(false));
		assertThat(japaneseElement.isValidForLocale(new Locale("", "JP")), equalTo(true));
		assertThat(japaneseElement.isValidForLocale(new Locale("", "KR")), equalTo(false));
		assertThat(japaneseElement.isValidForLocale(new Locale("", "")), equalTo(false));
		assertThat(koreanElement.isValidForLocale(new Locale("ja")), equalTo(false));
		assertThat(koreanElement.isValidForLocale(new Locale("ja", "JP")), equalTo(false));
		assertThat(koreanElement.isValidForLocale(new Locale("ja", "JP", "JP")), equalTo(false));
		assertThat(koreanElement.isValidForLocale(new Locale("en", "JP")), equalTo(false));
		assertThat(koreanElement.isValidForLocale(new Locale("ja", "GB")), equalTo(false));
		assertThat(koreanElement.isValidForLocale(new Locale("en")), equalTo(false));
		assertThat(koreanElement.isValidForLocale(new Locale("en", "GB")), equalTo(false));
		assertThat(koreanElement.isValidForLocale(new Locale("ko")), equalTo(true));
		assertThat(koreanElement.isValidForLocale(new Locale("ko", "KR")), equalTo(true));
		assertThat(koreanElement.isValidForLocale(new Locale("en", "KR")), equalTo(true));
	}

	private Set<ElementCountryRestriction> createElementCountryRestrictions(Locale... locales) {
		Set<ElementCountryRestriction> elementCountryRestrictions = new HashSet<ElementCountryRestriction>();
		for (Locale locale : locales) {
			ElementCountryRestriction elementCountryRestriction = new ElementCountryRestriction();
			elementCountryRestriction.setLocale(locale);
			elementCountryRestrictions.add(elementCountryRestriction);
		}
		return elementCountryRestrictions;
	}
}
