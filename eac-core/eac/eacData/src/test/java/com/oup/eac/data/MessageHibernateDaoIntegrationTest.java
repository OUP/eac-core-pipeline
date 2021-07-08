package com.oup.eac.data;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import java.util.List;
import java.util.Locale;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.oup.eac.domain.Message;

public class MessageHibernateDaoIntegrationTest extends AbstractDBTest {

	@Autowired
	private MessageDao messageDao;

	private Message message1;
	private Message message2;
	private Message message3;
	private Message message4;
	private Message message5;
	
	private Message defaultMessage;
	private Message defaultMessage1;
	private Message defaultMessage2;
	private Message defaultMessage3;
	private Message defaultMessage4;
	private Message defaultMessage5;

	@Before
	public void setUp() throws Exception {
		message1 = getSampleDataCreator().createMessage("base", new Locale("zh", "TW"), "some.key.a", "The first message");
		message2 = getSampleDataCreator().createMessage("base", new Locale("zh", "TW"), "some.key.b", "The second message");
		message3 = getSampleDataCreator().createMessage("base", new Locale("zh"), "some.key.c", "Another message");
		message4 = getSampleDataCreator().createMessage("base", new Locale("uk"), "some.key.c", "Another message");
		message5 = getSampleDataCreator().createMessage("base", new Locale("uk", "en", "a"), "some.key.d", "Another message");
		
		defaultMessage = getSampleDataCreator().createMessage("base", "some.key.d", "zzz");
		defaultMessage1 = getSampleDataCreator().createMessage("base", "title.registration.teachers.club.tandc.link", "Read our Terms and Conditions and Privacy Policy");
		defaultMessage2 = getSampleDataCreator().createMessage("base", "label.pageNotFound", "The page you requested could not be found. Please try a different link.");
		defaultMessage3 = getSampleDataCreator().createMessage("base", "title.registration.customer.dob", "Date of Birth");
		defaultMessage4 = getSampleDataCreator().createMessage("base", "basic.login.error.success.url.invalid", "The success_url is invalid.");
		defaultMessage5 = getSampleDataCreator().createMessage("base", "session.token.error.url.invalid", "The successUrl is invalid.");
		
		loadAllDataSets();
	}
	
	@Test
	public void shouldGetMessagesByLocaleWhenLanguageSpecified() throws Exception {
		List<Message> messages = messageDao.getMessagesByLocaleOrderedByKey(new Locale("zh"));
		
		assertThat(messages.size(), equalTo(1));
		assertThat(messages.get(0), equalTo(message3));
		
		messages = messageDao.getMessagesByLocaleOrderedByKey(new Locale("uk"));
		
		assertThat(messages.size(), equalTo(1));
		assertThat(messages.get(0), equalTo(message4));
	}

	@Test
	public void shouldGetMessagesByLocaleWhenLanguageAndCountrySpecified() throws Exception {
		List<Message> messages = messageDao.getMessagesByLocaleOrderedByKey(new Locale("zh", "TW"));

		assertThat(messages.size(), equalTo(2));
		assertThat(messages.get(0), equalTo(message1));
		assertThat(messages.get(1), equalTo(message2));
	}
	
	@Test
	public void shouldGetMessagesByLocaleWhenCountryAndLanguageAndVariantSpecified() throws Exception {
		List<Message> messages = messageDao.getMessagesByLocaleOrderedByKey(new Locale("uk", "en", "a"));

		assertThat(messages.size(), equalTo(1));
		assertThat(messages.get(0), equalTo(message5));
	}
	
	@Test
	public void shouldGetDefaultMessages() throws Exception {
		List<Message> messages = messageDao.getDefaultMessagesOrderedByKey();

		assertThat(messages.size(), equalTo(6));
	}
	
	@Test
	public void shouldFindMessagesContaining() throws Exception {
		List<Message> messages = messageDao.findMessagesWithKeyOrTextContaining("a", 10);
		assertThat(messages.size(), equalTo(5));
		assertThat(messages.get(0), equalTo(defaultMessage4));
		assertThat(messages.get(1), equalTo(defaultMessage2));
		assertThat(messages.get(2), equalTo(defaultMessage5));
		assertThat(messages.get(3), equalTo(defaultMessage3));
		assertThat(messages.get(4), equalTo(defaultMessage1));
		
		messages = messageDao.findMessagesWithKeyOrTextContaining("a", 2);
		assertThat(messages.size(), equalTo(2));
		assertThat(messages.get(0), equalTo(defaultMessage4));
		assertThat(messages.get(1), equalTo(defaultMessage2));
		
		messages = messageDao.findMessagesWithKeyOrTextContaining("please", 10);
		assertThat(messages.size(), equalTo(1));
		assertThat(messages.get(0), equalTo(defaultMessage2));
		
		messages = messageDao.findMessagesWithKeyOrTextContaining("title.registration.teachers.club.tandc.link", 10);
		assertThat(messages.size(), equalTo(1));
		assertThat(messages.get(0), equalTo(defaultMessage1));
	}
}
