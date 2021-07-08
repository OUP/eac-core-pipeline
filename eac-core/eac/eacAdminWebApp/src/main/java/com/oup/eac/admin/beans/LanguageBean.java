package com.oup.eac.admin.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.commons.lang.StringUtils;

import com.oup.eac.domain.Message;

public class LanguageBean implements Serializable {

	private Locale selectedLocale;
	private List<Locale> supportedLocales;
	private String language;
	private String country;
	private String variant;
	private List<Message> messages = new ArrayList<Message>();
	private List<Message> newMessages = new ArrayList<Message>();
	private String messageIdsToRemove;
	private boolean newLocale;

	public LanguageBean(final List<Locale> supportedLocales) {
		this.supportedLocales = supportedLocales;
		for (int i = 0; i < 100; i++) {
			newMessages.add(new Message());
		}
	}

	public Locale getSelectedLocale() {
		return selectedLocale;
	}

	public void setSelectedLocale(final Locale selectedLocale) {
		this.selectedLocale = selectedLocale;
		language = selectedLocale.getLanguage();
		country = selectedLocale.getCountry();
		variant = selectedLocale.getVariant();
	}
	
	public void reinitialiseSelectedLocale() {
		if (messages.size() == 0 || getMessagesToRemove().size() == messages.size()) {
			selectedLocale = null;
			language = null;
			country = null;
			variant = null;
		} else if (selectedLocale != null || newLocale) {
			selectedLocale = createLocaleFromParts();
		}
	}
	
	public Locale createLocaleFromParts() {
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

	public List<Locale> getSupportedLocales() {
		return supportedLocales;
	}
	
	public void setSupportedLocales(final List<Locale> supportedLocales) {
		this.supportedLocales = supportedLocales;
	}
	
	public List<Message> getUpdatedMessages() {
		for (Message newMessage : newMessages) {
			if (!isEmpty(newMessage)) {
				messages.add(newMessage);
			}
		}
		
		newMessages.clear();
		
		for (Message message : messages) {
			message.setLanguage(StringUtils.defaultIfEmpty(language, null));
			message.setCountry(StringUtils.defaultIfEmpty(country, null));
			message.setVariant(StringUtils.defaultIfEmpty(variant, null));
		}
		
		return messages;
	}
	
	private boolean isEmpty(final Message message) {
		return StringUtils.isBlank(message.getKey()) && StringUtils.isBlank(message.getBasename());
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(final String language) {
		this.language = language;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(final String country) {
		this.country = country;
	}

	public String getVariant() {
		return variant;
	}

	public void setVariant(final String variant) {
		this.variant = variant;
	}

	public List<Message> getMessages() {
		return messages;
	}

	public void setMessages(final List<Message> messages) {
		this.messages = messages;
	}
	
	public void clearMessages() {
		this.messages.clear();
	}

	public List<Message> getNewMessages() {
		return newMessages;
	}

	public void setNewMessages(final List<Message> newMessages) {
		this.newMessages = newMessages;
	}
	
	public int getNewMessageCount() {
		int count = 0;
		for (Message message : newMessages) {
			if (StringUtils.isNotBlank(message.getBasename())) {
				count++;
			}
		}
		return count;
	}

	public String getMessageIdsToRemove() {
		return messageIdsToRemove;
	}

	public void setMessageIdsToRemove(final String messageIdsToRemove) {
		this.messageIdsToRemove = messageIdsToRemove;
	}
	
	public List<Message> getMessagesToRemove() {
		List<Message> messagesToRemove = new ArrayList<Message>();
		for (Message message : messages) {
			if (StringUtils.isNotBlank(messageIdsToRemove) && messageIdsToRemove.contains(message.getId())) {
				messagesToRemove.add(message);
			}
		}
		return messagesToRemove;
	}

	public boolean isNewLocale() {
		return newLocale;
	}

	public void setNewLocale(final boolean newLocale) {
		this.newLocale = newLocale;
	}
	
}
