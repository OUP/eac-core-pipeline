package com.oup.eac.service.impl;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;

import com.oup.eac.data.MessageDao;
import com.oup.eac.domain.Message;
import com.oup.eac.service.MessageService;

public class MessageServiceImplTest {

	private MessageDao mockMessageDao;
	
	private MessageService messageService;
	
	@Before
	public void setUp() {
		mockMessageDao = EasyMock.createMock(MessageDao.class);
		messageService = new MessageServiceImpl(mockMessageDao);
	}
	
	@Test
	public void shouldReturnListOfSupportedLocales() throws Exception {
		List<Message> messages = new ArrayList<Message>();
		Message message = new Message();
		message.setBasename("base");
		message.setLanguage("en");
		message.setKey("key");
		message.setCountry("IT");
		message.setMessage("message");
		messages.add(message);
		message = new Message();
		message.setBasename("base");
		message.setLanguage("en");
		message.setKey("key2");
		message.setCountry("IT");
		message.setMessage("another message");
		messages.add(message);
		message = new Message();
		message.setBasename("base");
		message.setLanguage("zh");
		message.setKey("key");
		message.setCountry("TW");
		message.setMessage("message");
		messages.add(message);
		message = new Message();
		message.setBasename("base");
		message.setLanguage("en");
		message.setKey("key");
		message.setMessage("message");
		messages.add(message);
		message = new Message();
		message.setBasename("base");
		message.setLanguage("uk");
		message.setKey("key");
		message.setMessage("message");
		messages.add(message);
		
		EasyMock.expect(mockMessageDao.findAll()).andReturn(messages);
		EasyMock.replay(mockMessageDao);
		
		List<Locale> supportedLocales = messageService.getSupportedLocalesOrderedByLanguageAndCountry();
		
		assertThat(supportedLocales.size(), equalTo(4));
		assertThat(supportedLocales.get(0), equalTo(new Locale("en")));
		assertThat(supportedLocales.get(1), equalTo(new Locale("en", "IT")));
		assertThat(supportedLocales.get(2), equalTo(new Locale("uk")));
		assertThat(supportedLocales.get(3), equalTo(new Locale("zh", "TW")));
	}
}
