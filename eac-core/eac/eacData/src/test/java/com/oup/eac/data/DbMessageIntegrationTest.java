package com.oup.eac.data;

import java.util.Locale;
import java.util.UUID;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.synyx.messagesource.InitializableMessageSource;

import com.oup.eac.domain.Message;

public class DbMessageIntegrationTest /* extends AbstractDbMessageTest */ {
	/*
	 * 
	 * @Autowired
	 * 
	 * @Qualifier("messageSource") private InitializableMessageSource messageSource;
	 * 
	 * private String messageText; private String messageCode;
	 * 
	 * private Locale locale;
	 * 
	 * @Before public final void setUp() throws Exception { this.locale =
	 * Locale.getDefault(); this.messageCode = UUID.randomUUID().toString();
	 * this.messageText = "This is the message for " + messageCode;
	 * 
	 * @SuppressWarnings("unused") Message message1 =
	 * this.createDbMessage("messages",messageCode, messageText);
	 * 
	 * int before = countRowsInTable("message"); loadAllDataSets(); int after =
	 * countRowsInTable("message"); Assert.assertEquals(before+1,after);
	 * System.out.printf("before=%d after=%d%n",before,after);
	 * 
	 * }
	 * 
	 * @Test public void testMessageSource(){ String actual =
	 * this.messageSource.getMessage(messageCode, new Object[0], this.locale);
	 * Assert.assertEquals(messageText,actual); }
	 * 
	 */}
