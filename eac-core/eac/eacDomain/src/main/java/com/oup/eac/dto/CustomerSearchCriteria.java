package com.oup.eac.dto;

import java.util.Arrays;

import org.joda.time.DateTime;

public class CustomerSearchCriteria {

	private String username;
	private String email;
	private String firstName;
	private String familyName;
	private String externalId;
	private DateTime createdDateFrom;
	private DateTime createdDateTo;
	private String[] registrationDataKeywords;

	public String getUsername() {
		return username;
	}

	public void setUsername(final String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(final String email) {
		this.email = email;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(final String firstName) {
		this.firstName = firstName;
	}

	public String getFamilyName() {
		return familyName;
	}

	public void setFamilyName(final String familyName) {
		this.familyName = familyName;
	}
	
	public String getExternalId() {
		return externalId;
	}

	public void setExternalId(final String externalId) {
		this.externalId = externalId;
	}

	public DateTime getCreatedDateFrom() {
		return createdDateFrom;
	}

	public void setCreatedDateFrom(final DateTime createdDateFrom) {
		this.createdDateFrom = createdDateFrom;
	}

	public DateTime getCreatedDateTo() {
		return createdDateTo;
	}

	public void setCreatedDateTo(final DateTime createdDateTo) {
		this.createdDateTo = createdDateTo;
	}

	public String[] getRegistrationDataKeywords() {
		return registrationDataKeywords;
	}

	public void setRegistrationDataKeywords(final String[] registrationDataKeywords) {
		this.registrationDataKeywords = registrationDataKeywords;
	}

	public CustomerSearchCriteria username(final String username) {
		this.username = username;
		return this;
	}

	public CustomerSearchCriteria email(final String email) {
		this.email = email;
		return this;
	}

	public CustomerSearchCriteria firstName(final String firstName) {
		this.firstName = firstName;
		return this;
	}

	public CustomerSearchCriteria familyName(final String familyName) {
		this.familyName = familyName;
		return this;
	}
	
	public CustomerSearchCriteria externalId(final String externalId) {
		this.externalId = externalId;
		return this;
	}

	public CustomerSearchCriteria createdDateFrom(final DateTime createdDateFrom) {
		this.createdDateFrom = createdDateFrom;
		return this;
	}

	public CustomerSearchCriteria createdDateTo(final DateTime createdDateTo) {
		this.createdDateTo = createdDateTo;
		return this;
	}

	public CustomerSearchCriteria registrationDataKeywords(String[] registrationDataKeywords) {
		this.registrationDataKeywords = registrationDataKeywords;
		return this;
	}

	@Override
	public String toString() {
		return "CustomerSearchCriteria [username=" + username + ", email=" + email + ", firstName=" + firstName + ", familyName=" + familyName
				+ ", externalId=" + externalId + ", createdDateFrom=" + createdDateFrom + ", createdDateTo=" + createdDateTo + ", registrationDataKeywords="
				+ Arrays.toString(registrationDataKeywords) + "]";
	}

}
