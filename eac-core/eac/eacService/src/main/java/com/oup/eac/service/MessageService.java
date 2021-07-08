package com.oup.eac.service;

import java.util.List;
import java.util.Locale;

import com.oup.eac.domain.Message;

public interface MessageService {

	Message getMessageById(final String id);
	
	List<Message> findMessagesWithKeyOrTextContaining(final String text);
	
	List<Locale> getSupportedLocalesOrderedByLanguageAndCountry();
	
	List<Message> getMessagesByLocaleOrderedByKey(final Locale locale);
	
	List<Message> getDefaultMessagesOrderedByKey();
	
	void saveMessage(final Message message);
	
	void deleteMessage(final Message message);
}
