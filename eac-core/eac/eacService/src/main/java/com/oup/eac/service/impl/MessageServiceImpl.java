package com.oup.eac.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.oup.eac.data.MessageDao;
import com.oup.eac.domain.Message;
import com.oup.eac.domain.utils.audit.AuditLogger;
import com.oup.eac.service.MessageService;

@Service("messageService")
public class MessageServiceImpl implements MessageService {
	
	private static final int MAX_MATCH_LIMIT = 10;

	private final MessageDao messageDao;

	@Autowired
	public MessageServiceImpl(final MessageDao messageDao) {
		this.messageDao = messageDao;
	}

	@Override
	public Message getMessageById(final String id) {
		return messageDao.findById(id, true);
	}

	@Override
	public List<Message> findMessagesWithKeyOrTextContaining(final String text) {
		return messageDao.findMessagesWithKeyOrTextContaining(text, MAX_MATCH_LIMIT);
	}

	@Override
	public List<Locale> getSupportedLocalesOrderedByLanguageAndCountry() {
		Set<Locale> supportedLocales = new HashSet<Locale>();
		List<Message> messages = messageDao.findAll();
		
		for (Message message : messages) {
			Locale locale = message.getLocale();
			if (locale != null) {
				supportedLocales.add(locale);
			}
		}
		
		List<Locale> supportedLocalesOrdered = new ArrayList<Locale>(supportedLocales);
		Collections.sort(supportedLocalesOrdered, new Comparator<Locale>() {
			@Override
			public int compare(final Locale o1, final Locale o2) {
				return o1.toString().compareTo(o2.toString());
			}
		});
		
		return supportedLocalesOrdered;
	}

	@Override
	public List<Message> getMessagesByLocaleOrderedByKey(final Locale locale) {
		return messageDao.getMessagesByLocaleOrderedByKey(locale);
	}

	@Override
	public List<Message> getDefaultMessagesOrderedByKey() {
		return messageDao.getDefaultMessagesOrderedByKey();
	}

	@Override
	public void saveMessage(final Message message) {
		messageDao.saveOrUpdate(message);
		AuditLogger.logEvent("Saved Message", "Id:"+message.getId(), AuditLogger.message(message));
	}

	@Override
	public void deleteMessage(final Message message) {
		messageDao.delete(message);
		AuditLogger.logEvent("Deleted Message", "Id:"+message.getId(), AuditLogger.message(message));
	}
}