package com.oup.eac.domain;

import java.util.Locale;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;


@Entity
public class ElementCountryRestriction extends BaseDomainObject {

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "element_id", nullable = false)
	private Element element;
	
	private Locale locale;

	/**
	 * @return the element
	 */
	public Element getElement() {
		return element;
	}

	/**
	 * @param element the element to set
	 */
	public void setElement(final Element element) {
		this.element = element;
	}

	/**
	 * @return the locale
	 */
	public Locale getLocale() {
		return locale;
	}

	/**
	 * @param locale the locale to set
	 */
	public void setLocale(final Locale locale) {
		this.locale = locale;
	}
	
	@Override
	public String toString() {
		return "ElementCountryRestriction [locale=" + locale + "]";
	}
	
}
