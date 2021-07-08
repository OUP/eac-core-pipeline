package com.oup.eac.admin.beans;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import org.apache.commons.lang.ArrayUtils;
import org.junit.Test;

public class CustomerSearchCriteriaBeanTest {

	private final CustomerSearchCriteriaBean bean = new CustomerSearchCriteriaBean();

	@Test
	public void shouldReturnListOfKeywords() {
		String registrationData = "keyword1, keyword2 ,keyword3";
		bean.setRegistrationData(registrationData);
		String[] keywords = bean.getRegistrationDataAsKeywordList();
		assertThat(keywords.length, equalTo(3));
		assertThat(ArrayUtils.contains(keywords, "keyword1"), equalTo(true));
		assertThat(ArrayUtils.contains(keywords, "keyword2"), equalTo(true));
		assertThat(ArrayUtils.contains(keywords, "keyword3"), equalTo(true));
	}

	@Test
	public void shouldReturnListOfOneKeyword() {
		String registrationData = "keyword1";
		bean.setRegistrationData(registrationData);
		String[] keywords = bean.getRegistrationDataAsKeywordList();
		assertThat(keywords.length, equalTo(1));
		assertThat(keywords[0], equalTo("keyword1"));
	}

	@Test
	public void shouldReturnEmptyListWhenNoRegistrationData() {
		String registrationData = "    ";
		bean.setRegistrationData(registrationData);
		String[] keywords = bean.getRegistrationDataAsKeywordList();
		assertThat(keywords.length, equalTo(0));
	}

	@Test
	public void shouldReturnEmptyListWhenSingleComma() {
		String registrationData = ",";
		bean.setRegistrationData(registrationData);
		String[] keywords = bean.getRegistrationDataAsKeywordList();
		assertThat(keywords.length, equalTo(0));
	}

	@Test
	public void shouldReturnEmptyListWhenMultipleCommas() {
		String registrationData = ",,";
		bean.setRegistrationData(registrationData);
		String[] keywords = bean.getRegistrationDataAsKeywordList();
		assertThat(keywords.length, equalTo(0));
	}

	@Test
	public void shouldReturnListKeywordsWithoutDuplicates() {
		String registrationData = "keyword1, keyword2 ,keyword2";
		bean.setRegistrationData(registrationData);
		String[] keywords = bean.getRegistrationDataAsKeywordList();
		assertThat(keywords.length, equalTo(2));
		assertThat(keywords[0], equalTo("keyword1"));
		assertThat(keywords[1], equalTo("keyword2"));
	}
}
