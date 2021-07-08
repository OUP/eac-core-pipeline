package com.oup.eac.admin.validators;

import java.text.MessageFormat;
import java.util.List;
import java.util.Locale;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.oup.eac.admin.beans.LanguageBean;
import com.oup.eac.domain.Message;

@Component
public class LanguageBeanValidator implements Validator {

	private static final int MAX_MESSAGE_LENGTH = 1000;

	@Override
	public void validate(final Object target, final Errors errors) {
		LanguageBean languageBean = (LanguageBean) target;

		if (languageBean.isNewLocale()) {
			validateLanguageIsNotEmpty(languageBean, errors);
			validateMessagesPresent(errors, languageBean);
		}

		validateCountryAndVariant(languageBean, errors);
		validateValidISOLanguage(languageBean, errors);
		validateValidISOCountry(languageBean, errors);
		validateLocaleDoesNotExist(languageBean, errors);
		validateMessageKey(languageBean, errors);
		validateMessageContent(languageBean, errors);
	}

	private void validateLanguageIsNotEmpty(final LanguageBean languageBean, final Errors errors) {
		if (StringUtils.isBlank(languageBean.getLanguage())) {
			errors.rejectValue("language", "error.locale.language.empty");
		}
	}

	private void validateCountryAndVariant(final LanguageBean languageBean, final Errors errors) {
		if (StringUtils.isNotBlank(languageBean.getVariant()) && StringUtils.isBlank(languageBean.getCountry())) {
			errors.rejectValue("country", "error.locale.variantWithoutCountry");
		}
	}

	private void validateValidISOLanguage(final LanguageBean languageBean, final Errors errors) {
		if (StringUtils.isNotBlank(languageBean.getLanguage())) {
			LocaleValidatorHelper.validateISOLanguage(languageBean.getLanguage(), "language", errors);
		}
	}

	private void validateValidISOCountry(final LanguageBean languageBean, final Errors errors) {
		if (StringUtils.isNotBlank(languageBean.getCountry())) {
			LocaleValidatorHelper.validateISOCountry(languageBean.getCountry(), "country", errors);
		}
	}

	private void validateLocaleDoesNotExist(final LanguageBean languageBean, final Errors errors) {
		if (languageBean.isNewLocale()) {
			Locale newLocale = languageBean.createLocaleFromParts();
			if (languageBean.getSupportedLocales().contains(newLocale)) {
				errors.rejectValue("language", "error.locale.alreadyExists");
			}
		} else if (languageBean.getSelectedLocale() != null && StringUtils.isBlank(languageBean.getLanguage())) {
			Locale newLocale = languageBean.createLocaleFromParts();
			if (newLocale == null || languageBean.getSupportedLocales().contains(newLocale)) {
				errors.rejectValue("language", "error.locale.alreadyExists");
			}
		} else if (StringUtils.isNotBlank(languageBean.getLanguage())) {
			if (languageBean.getSelectedLocale() != null) {
				Locale newLocale = languageBean.createLocaleFromParts();
				if (!languageBean.getSelectedLocale().equals(newLocale) && languageBean.getSupportedLocales().contains(newLocale)) {
					errors.rejectValue("language", "error.locale.alreadyExists");
				}
			} else {
				errors.rejectValue("language", "error.locale.operationNotPermitted");
			}
		}
	}

	private void validateMessagesPresent(final Errors errors, LanguageBean languageBean) {
		if (languageBean.getNewMessageCount() == 0) {
			errors.rejectValue("newMessages", "error.locale.messages.empty");
		}
	}

	private void validateMessageKey(final LanguageBean languageBean, final Errors errors) {
		List<Message> messages = languageBean.getMessages();
		for (int i = 0; i < messages.size(); i++) {
			Message message = messages.get(i);
			if (StringUtils.isBlank(message.getKey()) && StringUtils.isNotBlank(message.getBasename())) {
				errors.rejectValue("messages[" + i + "].key", "error.locale.message.emptyKey");
			}
		}
		List<Message> newMessages = languageBean.getNewMessages();
		for (int i = 0; i < newMessages.size(); i++) {
			Message message = newMessages.get(i);
			if (StringUtils.isBlank(message.getKey()) && StringUtils.isNotBlank(message.getBasename())) {
				errors.rejectValue("newMessages[" + i + "].key", "error.locale.message.emptyKey");
			}
		}
	}

	private void validateMessageContent(final LanguageBean languageBean, final Errors errors) {
		for (Message message : languageBean.getNewMessages()) {
			validateMessageLength(message, errors);
			validateSpecialCharacters(message, errors);
		}
		for (Message message : languageBean.getMessages()) {
			validateMessageLength(message, errors);
			validateSpecialCharacters(message, errors);
		}
	}

	private void validateMessageLength(final Message message, final Errors errors) {
		String messageContent = message.getMessage();
		if (messageContent != null && messageContent.length() > MAX_MESSAGE_LENGTH) {
			errors.rejectValue("language", "error.locale.message.tooBig");
		}
	}

	private void validateSpecialCharacters(final Message message, final Errors errors) {
		String messageContent = message.getMessage();
		if (StringUtils.isNotBlank(messageContent)) {
			try {
				new MessageFormat(messageContent);
			} catch (final IllegalArgumentException e) {
				errors.rejectValue("messages[0].message", "error.locale.message.syntaxError", new Object[] { e.toString() }, null);
			}
		}
	}

	@Override
	public boolean supports(final Class<?> clazz) {
		final boolean supports = LanguageBean.class.isAssignableFrom(clazz);
		return supports;
	}

}
