package com.oup.eac.domain;

import java.util.Locale;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;

@Entity
@Table(name = "message")
public class Message extends BaseDomainObject {

	private String basename;

	private String language;

	private String country;

	private String variant;

	@Column(name="`key`")
	private String key;

	private String message;

	public String getBasename() {
		return basename;
	}

	public void setBasename(final String basename) {
		this.basename = basename;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(final String language) {
		this.language = language == null ? null : language.toLowerCase();
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(final String country) {
		this.country = country == null ? null : country.toUpperCase();
	}

	public String getVariant() {
		return variant;
	}

	public void setVariant(final String variant) {
		this.variant = variant;
	}

	public String getKey() {
		return key;
	}

	public void setKey(final String key) {
		this.key = key;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(final String message) {
		this.message = message;
	}
	
	public Locale getLocale() {
		Locale locale = null;
		if (StringUtils.isNotBlank(language) && StringUtils.isNotBlank(country) && StringUtils.isNotBlank(variant)) {
			locale = new Locale(language, country, variant);
		} else if (StringUtils.isNotBlank(language) && StringUtils.isNotBlank(country)) {
			locale = new Locale(language, country);
		} else if (StringUtils.isNotBlank(language)) {
			locale = new Locale(language);
		}
		return locale;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((basename == null) ? 0 : basename.hashCode());
		result = prime * result + ((country == null) ? 0 : country.hashCode());
		result = prime * result + ((key == null) ? 0 : key.hashCode());
		result = prime * result + ((language == null) ? 0 : language.hashCode());
		result = prime * result + ((message == null) ? 0 : message.hashCode());
		result = prime * result + ((variant == null) ? 0 : variant.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Message other = (Message) obj;
		if (basename == null) {
			if (other.basename != null)
				return false;
		} else if (!basename.equals(other.basename))
			return false;
		if (country == null) {
			if (other.country != null)
				return false;
		} else if (!country.equals(other.country))
			return false;
		if (key == null) {
			if (other.key != null)
				return false;
		} else if (!key.equals(other.key))
			return false;
		if (language == null) {
			if (other.language != null)
				return false;
		} else if (!language.equals(other.language))
			return false;
		if (message == null) {
			if (other.message != null)
				return false;
		} else if (!message.equals(other.message))
			return false;
		if (variant == null) {
			if (other.variant != null)
				return false;
		} else if (!variant.equals(other.variant))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Message [basename=" + basename + ", language=" + language + ", country=" + country + ", variant=" + variant + ", key=" + key + ", message="
				+ message + "]";
	}

}
