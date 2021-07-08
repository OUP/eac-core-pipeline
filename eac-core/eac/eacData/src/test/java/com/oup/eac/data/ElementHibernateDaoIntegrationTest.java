package com.oup.eac.data;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import java.util.List;
import java.util.Locale;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.oup.eac.domain.Element;
import com.oup.eac.domain.Question;

public class ElementHibernateDaoIntegrationTest extends AbstractDBTest {

	@Autowired
	private ElementDao elementDao;
	
	@Before
	public void setUp() throws Exception {
		Question question = getSampleDataCreator().createQuestion();
		Element el = getSampleDataCreator().createElement(question);
		getSampleDataCreator().createElementCountryRestriction(el, new Locale("ja", "JP"));
		getSampleDataCreator().createElementCountryRestriction(el, new Locale("tr", "TR"));
		getSampleDataCreator().createElementCountryRestriction(el, new Locale("en", "IN"));
		loadAllDataSets();
	}
	
	@Test
	public void shouldReturnedOrderedListOfLocales() {
		List<Locale> locales = elementDao.getOrderedElementCountryRestrictionLocales();
		
		assertThat(locales.size(), equalTo(3));
		assertThat(locales.get(0), equalTo(new Locale("en", "IN")));
		assertThat(locales.get(1), equalTo(new Locale("ja", "JP")));
	}
}
