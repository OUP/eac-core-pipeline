package com.oup.eac.admin.validators;

import static org.easymock.EasyMock.anyObject;
import static org.easymock.EasyMock.eq;

import java.util.Arrays;
import java.util.Collections;
import java.util.Locale;

import org.easymock.EasyMock;
import org.junit.Test;
import org.springframework.validation.Errors;

import com.oup.eac.admin.beans.LanguageBean;
import com.oup.eac.domain.Message;

public class LanguageBeanValidatorTest {

	private LanguageBeanValidator validator = new LanguageBeanValidator();
	
	@Test
	public void shouldFailWhenLanguageNotSetWhenAddingNewLocale() {
		LanguageBean languageBean = new LanguageBean(Collections.<Locale>emptyList());
		languageBean.setNewLocale(true);
		languageBean.setNewMessages(Arrays.asList(createNewMessage("some.key", "some.message")));
		
		Errors mockErrors = EasyMock.createMock(Errors.class);
		mockErrors.rejectValue("language", "error.locale.language.empty");

		EasyMock.replay(mockErrors);

		validator.validate(languageBean, mockErrors);

		EasyMock.verify(mockErrors);
	}
	
	private Message createNewMessage(final String key, final String data) {
		Message message = new Message();
		message.setKey(key);
		message.setMessage(data);
		message.setBasename("messages");
		return message;
	}
	
	@Test
	public void shouldFailWhenLanguageNotValidISOLanguageCodeWhenAddingNewLocale() {
		LanguageBean languageBean = new LanguageBean(Collections.<Locale>emptyList());
		languageBean.setNewLocale(true);
		languageBean.setNewMessages(Arrays.asList(createNewMessage("some.key", "some.message")));
		languageBean.setLanguage("k9");
		
		Errors mockErrors = EasyMock.createMock(Errors.class);
		mockErrors.rejectValue("language", "error.locale.language.notValid");

		EasyMock.replay(mockErrors);

		validator.validate(languageBean, mockErrors);

		EasyMock.verify(mockErrors);
	}
	
	@Test
	public void shouldFailWhenLanguageNotValidISOLanguageCodeWhenUpdatingExistingLocale() {
		LanguageBean languageBean = new LanguageBean(Arrays.asList(new Locale("en", "IT")));
		languageBean.setNewLocale(false);
		languageBean.setNewMessages(Arrays.asList(createNewMessage("some.key", "some.message")));
		languageBean.setSelectedLocale(new Locale("en", "IT"));
		languageBean.setLanguage("k9");
		
		Errors mockErrors = EasyMock.createMock(Errors.class);
		mockErrors.rejectValue("language", "error.locale.language.notValid");
		
		EasyMock.replay(mockErrors);
		
		validator.validate(languageBean, mockErrors);
		
		EasyMock.verify(mockErrors);
	}
	
	@Test
	public void shouldFailWhenCountryNotValidISOCountryCodeWhenAddingNewLocale() {
		LanguageBean languageBean = new LanguageBean(Collections.<Locale>emptyList());
		languageBean.setNewLocale(true);
		languageBean.setNewMessages(Arrays.asList(createNewMessage("some.key", "some.message")));
		languageBean.setLanguage("en");
		languageBean.setCountry("U8");
		
		Errors mockErrors = EasyMock.createMock(Errors.class);
		mockErrors.rejectValue("country", "error.locale.country.notValid");

		EasyMock.replay(mockErrors);

		validator.validate(languageBean, mockErrors);

		EasyMock.verify(mockErrors);
	}
	
	@Test
	public void shouldNotFailWhenCountryNotSetWhenAddingNewLocale() {
		LanguageBean languageBean = new LanguageBean(Collections.<Locale>emptyList());
		languageBean.setNewLocale(true);
		languageBean.setNewMessages(Arrays.asList(createNewMessage("some.key", "some.message")));
		languageBean.setLanguage("en");
		
		Errors mockErrors = EasyMock.createMock(Errors.class);
		
		EasyMock.replay(mockErrors);
		
		validator.validate(languageBean, mockErrors);
		
		EasyMock.verify(mockErrors);
	}
	
	@Test
	public void shouldFailWhenCountryNotSetButVariantSetWhenAddingNewLocale() {
		LanguageBean languageBean = new LanguageBean(Collections.<Locale>emptyList());
		languageBean.setNewLocale(true);
		languageBean.setNewMessages(Arrays.asList(createNewMessage("some.key", "some.message")));
		languageBean.setLanguage("en");
		languageBean.setVariant("rr");
		
		Errors mockErrors = EasyMock.createMock(Errors.class);
		mockErrors.rejectValue("country", "error.locale.variantWithoutCountry");

		EasyMock.replay(mockErrors);

		validator.validate(languageBean, mockErrors);

		EasyMock.verify(mockErrors);
	}
	
	@Test
	public void shouldFailWhenCountryNotSetButVariantSetWhenUpdatingExistingLocale() {
		LanguageBean languageBean = new LanguageBean(Arrays.asList(new Locale("en", "IT")));
		languageBean.setNewLocale(false);
		languageBean.setNewMessages(Arrays.asList(createNewMessage("some.key", "some.message")));
		languageBean.setSelectedLocale(new Locale("en", "IT"));
		languageBean.setLanguage("en");
		languageBean.setCountry("");
		languageBean.setVariant("rr");
		
		Errors mockErrors = EasyMock.createMock(Errors.class);
		mockErrors.rejectValue("country", "error.locale.variantWithoutCountry");
		
		EasyMock.replay(mockErrors);
		
		validator.validate(languageBean, mockErrors);
		
		EasyMock.verify(mockErrors);
	}
	
	@Test
	public void shouldFailWhenLocaleAlreadyExistsWhenAddingNewLocale() {
		LanguageBean languageBean = new LanguageBean(Arrays.asList(new Locale("en", "IT"), new Locale("fr"), new Locale("zh", "TW")));
		languageBean.setNewLocale(true);
		languageBean.setNewMessages(Arrays.asList(createNewMessage("some.key", "some.message")));
		languageBean.setLanguage("zh");
		languageBean.setCountry("TW");
		
		Errors mockErrors = EasyMock.createMock(Errors.class);
		mockErrors.rejectValue("language", "error.locale.alreadyExists");

		EasyMock.replay(mockErrors);

		validator.validate(languageBean, mockErrors);

		EasyMock.verify(mockErrors);
	}
	
	@Test
	public void shouldFailWhenLocaleAlreadyExistsWhenUpdatingExistingLocale() {
		LanguageBean languageBean = new LanguageBean(Arrays.asList(new Locale("en", "IT"), new Locale("fr"), new Locale("zh", "TW")));
		languageBean.setNewLocale(false);
		languageBean.setNewMessages(Arrays.asList(createNewMessage("some.key", "some.message")));
		languageBean.setSelectedLocale(new Locale("en", "IT"));
		languageBean.setLanguage("zh");
		languageBean.setCountry("TW");
		
		Errors mockErrors = EasyMock.createMock(Errors.class);
		mockErrors.rejectValue("language", "error.locale.alreadyExists");
		
		EasyMock.replay(mockErrors);
		
		validator.validate(languageBean, mockErrors);
		
		EasyMock.verify(mockErrors);
	}
	
	@Test
	public void shouldFailWhenLocaleAlreadyExistsWhenUpdatingExistingLocaleToDefault() {
		LanguageBean languageBean = new LanguageBean(Arrays.asList(new Locale("en", "IT"), new Locale("fr"), new Locale("zh", "TW")));
		languageBean.setNewLocale(false);
		languageBean.setNewMessages(Arrays.asList(createNewMessage("some.key", "some.message")));
		languageBean.setSelectedLocale(new Locale("en", "IT"));
		languageBean.setLanguage(null);
		languageBean.setCountry(null);
		
		Errors mockErrors = EasyMock.createMock(Errors.class);
		mockErrors.rejectValue("language", "error.locale.alreadyExists");
		
		EasyMock.replay(mockErrors);
		
		validator.validate(languageBean, mockErrors);
		
		EasyMock.verify(mockErrors);
	}
	
	@Test
	public void shouldFailWhenUpdatingLanguageForDefaultLocaleWhenAlreadyExists() {
		LanguageBean languageBean = new LanguageBean(Arrays.asList(new Locale("en", "IT"), new Locale("fr"), new Locale("zh", "TW")));
		languageBean.setNewLocale(false);
		languageBean.setNewMessages(Arrays.asList(createNewMessage("some.key", "some.message")));
		languageBean.setLanguage("zh");
		languageBean.setCountry("TW");
		
		Errors mockErrors = EasyMock.createMock(Errors.class);
		mockErrors.rejectValue("language", "error.locale.operationNotPermitted");
		
		EasyMock.replay(mockErrors);
		
		validator.validate(languageBean, mockErrors);
		
		EasyMock.verify(mockErrors);
	}
	
	@Test
	public void shouldFailWhenUpdatingLanguageForDefaultLocaleWhenDoesNotAlreadyExist() {
		LanguageBean languageBean = new LanguageBean(Arrays.asList(new Locale("en", "IT"), new Locale("fr")));
		languageBean.setNewLocale(false);
		languageBean.setNewMessages(Arrays.asList(createNewMessage("some.key", "some.message")));
		languageBean.setLanguage("zh");
		languageBean.setCountry("TW");
		
		Errors mockErrors = EasyMock.createMock(Errors.class);
		mockErrors.rejectValue("language", "error.locale.operationNotPermitted");
		
		EasyMock.replay(mockErrors);
		
		validator.validate(languageBean, mockErrors);
		
		EasyMock.verify(mockErrors);
	}
	
	@Test
	public void shouldNotFailWhenWhenUpdatingExistingLocale() {
		LanguageBean languageBean = new LanguageBean(Arrays.asList(new Locale("en", "IT"), new Locale("fr"), new Locale("zh", "TW")));
		languageBean.setNewLocale(false);
		languageBean.setNewMessages(Arrays.asList(createNewMessage("some.key", "some.message")));
		languageBean.setSelectedLocale(new Locale("zh", "TW"));
		languageBean.setLanguage("zh");
		languageBean.setCountry("TW");
		
		Errors mockErrors = EasyMock.createMock(Errors.class);
		
		EasyMock.replay(mockErrors);
		
		validator.validate(languageBean, mockErrors);
		
		EasyMock.verify(mockErrors);
	}
	
	@Test
	public void shouldFailWhenMessageKeyEmpty() {
		LanguageBean languageBean = new LanguageBean(Collections.<Locale>emptyList());
		languageBean.setNewLocale(true);
		languageBean.setLanguage("en");
		languageBean.setNewMessages(Arrays.asList(createNewMessage("", "some.message"), new Message(), new Message()));
		
		Errors mockErrors = EasyMock.createMock(Errors.class);
		mockErrors.rejectValue("newMessages[0].key", "error.locale.message.emptyKey");

		EasyMock.replay(mockErrors);

		validator.validate(languageBean, mockErrors);

		EasyMock.verify(mockErrors);
	}
	
	@Test
	public void shouldFailWhenNoMessagesPresentWhenAddingNewLocale() {
		LanguageBean languageBean = new LanguageBean(Arrays.asList(new Locale("en", "IT"), new Locale("fr"), new Locale("zh", "TW")));
		languageBean.setNewLocale(true);
		languageBean.setLanguage("zh");
		
		Errors mockErrors = EasyMock.createMock(Errors.class);
		mockErrors.rejectValue("newMessages", "error.locale.messages.empty");

		EasyMock.replay(mockErrors);

		validator.validate(languageBean, mockErrors);

		EasyMock.verify(mockErrors);
	}
	
	@Test
	public void shouldFailWhenMessageLongerthan255Characters() {
		LanguageBean languageBean = new LanguageBean(Arrays.asList(new Locale("en", "IT"), new Locale("fr"), new Locale("zh", "TW")));
		languageBean.setNewLocale(false);
		languageBean.setSelectedLocale(new Locale("fr"));
		languageBean.setLanguage("fr");
		languageBean.setNewMessages(Arrays.asList(createNewMessage("some.key", "some.message sdjkfhksdjfh sdjkfh sdjkfh sdjkfh sdf sdf sdf sdjkfhsdjkf sdjkfh sdjkfh sdjkfh sdjkfh sdjkfh sdjkfh jksd fh sdksjdfh sdjkfh  jksdf jksd fhjksd fhjksdfh jksdfh jksdf jksdfh jksdfh sjkadh sjkadh sjkadh sjkadh sad    jksadhsjkadh  sdjkafhf dsjfh sdkf sdjkfh s dsdjkfh sdjkfh sdf sdf sdf sdjkfhsdjkf sdjkfh sdjkfh sdjkfh sdjkfh sdjkfh sdjkfh jksd fh sdksjdfh sdjkfhsdjkfh sdjkfh sdf sdf sdf sdjkfhsdjkf sdjkfh sdjkfh sdjkfh sdjkfh sdjkfh sdjkfh jksd fh sdksjdfh sdjkfhsdjkfh sdjkfh sdf sdf sdf sdjkfhsdjkf sdjkfh sdjkfh sdjkfh sdjkfh sdjkfh sdjkfh jksd fh sdksjdfh sdjkfhsdjkfh sdjkfh sdf sdf sdf sdjkfhsdjkf sdjkfh sdjkfh sdjkfh sdjkfh sdjkfh sdjkfh jksd fh sdksjdfh sdjkfhsdjkfh sdjkfh sdf sdf sdf sdjkfhsdjkf sdjkfh sdjkfh sdjkfh sdjkfh sdjkfh sdjkfh jksd fh sdksjdfh sdjkfhsdjkfh sdjkfh sdf sdf sdf sdjkfhsdjkf sdjkfh sdjkfh sdjkfh sdjkfh sdjkfh sdjkfh jksd fh sdksjdfh sdjkfhsdjkfh sdjkfh sdf sdf sdf sdjkfhsdjkf sdjkfh sdjkfh sdjkfh sdjkfh sdjkfh sdjkfh jksd fh sdksjdfh sdjkfhsdjkfh sdjkfh sdf sdf sdf sdjkfhsdjkf sdjkfh sdjkfh sdjkfh sdjkfh sdjkfh sdjkfh jksd fh sdksjdfh sdjkfhsdjkfh sdjkfh sdf sdf sdf sdjkfhsdjkf sdjkfh sdjkfh sdjkfh sdjkfh sdjkfh sdjkfh jksd fh sdksjdfh sdjkfhsdjkfh sdjkfh sdf sdf sdf sdjkfhsdjkf sdjkfh sdjkfh sdjkfh sdjkfh sdjkfh sdjkfh jksd fh sdksjdfh sdjkfhsdjkfh sdjkfh sdf sdf sdf sdjkfhsdjkf sdjkfh sdjkfh sdjkfh sdjkfh sdjkfh sdjkfh jksd fh sdksjdfh sdjkfhsdjkfh sdjkfh sdf sdf sdf sdjkfhsdjkf sdjkfh sdjkfh sdjkfh sdjkfh sdjkfh sdjkfh jksd fh sdksjdfh sdjkfhsdjkfh sdjkfh sdf sdf sdf sdjkfhsdjkf sdjkfh sdjkfh sdjkfh sdjkfh sdjkfh sdjkfh jksd fh sdksjdfh sdjkfhsdjkfh sdjkfh sdf sdf sdf sdjkfhsdjkf sdjkfh sdjkfh sdjkfh sdjkfh sdjkfh sdjkfh jksd fh sdksjdfh sdjkfh")));
		
		Errors mockErrors = EasyMock.createMock(Errors.class);
		mockErrors.rejectValue("language", "error.locale.message.tooBig");

		EasyMock.replay(mockErrors);

		validator.validate(languageBean, mockErrors);

		EasyMock.verify(mockErrors);
	}
	
	@Test
	public void shouldFailWhenNewMessageTextContainsUnmatchedCurlyBraces() {
		LanguageBean languageBean = new LanguageBean(Collections.<Locale>emptyList());
		languageBean.setNewLocale(true);
		languageBean.setLanguage("en");
		languageBean.setNewMessages(Arrays.asList(createNewMessage("some.key", "some message {")));
		
		Errors mockErrors = EasyMock.createMock(Errors.class);
		mockErrors.rejectValue(eq("messages[0].message"), eq("error.locale.message.syntaxError"), anyObject(Object[].class), anyObject(String.class));

		EasyMock.replay(mockErrors);

		validator.validate(languageBean, mockErrors);

		EasyMock.verify(mockErrors);
	}
	
	@Test
	public void shouldFailWhenExistingMessageTextContainsUnmatchedCurlyBraces() {
		LanguageBean languageBean = new LanguageBean(Arrays.asList(new Locale("en", "IT")));
		languageBean.setNewLocale(false);
		languageBean.setLanguage("en");
		languageBean.setSelectedLocale(new Locale("en", "IT"));
		languageBean.setMessages(Arrays.asList(createNewMessage("some.key", "some message {")));
		
		Errors mockErrors = EasyMock.createMock(Errors.class);
		mockErrors.rejectValue(eq("messages[0].message"), eq("error.locale.message.syntaxError"), anyObject(Object[].class), anyObject(String.class));
		
		EasyMock.replay(mockErrors);
		
		validator.validate(languageBean, mockErrors);
		
		EasyMock.verify(mockErrors);
	}
}
