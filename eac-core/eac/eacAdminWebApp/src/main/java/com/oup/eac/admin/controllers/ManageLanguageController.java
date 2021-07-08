package com.oup.eac.admin.controllers;

import java.util.List;
import java.util.Locale;

import javax.validation.Valid;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.oup.eac.admin.beans.LanguageBean;
import com.oup.eac.admin.validators.LanguageBeanValidator;
import com.oup.eac.data.message.DbMessageSourceReloader;
import com.oup.eac.domain.Message;
import com.oup.eac.service.MessageService;

@Controller
@RequestMapping("/mvc/language/manage.htm")
public class ManageLanguageController {
	
	private static final Logger LOGGER = Logger.getLogger(ManageLanguageController.class);
	
	private static final String NEW = "new";
	private static final String MANAGE_LANGUAGE_VIEW = "manageLanguage";
	private static final String MANAGE_LANGUAGE_FORM_VIEW = "manageLanguageForm";
	private static final String MODEL = "languageBean";
	
	private final MessageService messageService;
	private final DbMessageSourceReloader messageReloader;
	private final LanguageBeanValidator validator;
	
	@Autowired
	public ManageLanguageController(
			final MessageService messageService, 
			final DbMessageSourceReloader messageReloader,
			final LanguageBeanValidator validator) {
		this.messageService = messageService;
		this.messageReloader = messageReloader;
		this.validator = validator;
	}

	@RequestMapping(method=RequestMethod.GET)
	public ModelAndView showSupportedLocaleList() {
		return new ModelAndView(MANAGE_LANGUAGE_VIEW);
	}
	
	@RequestMapping(method=RequestMethod.GET, params="showForm")
	public ModelAndView showForm() {
		return new ModelAndView(MANAGE_LANGUAGE_FORM_VIEW);
	}
	
	@RequestMapping(method=RequestMethod.POST)
	public ModelAndView saveLanguage(@Valid @ModelAttribute(MODEL) final LanguageBean languageBean, final BindingResult bindingResult) {
		ModelAndView modelAndView = new ModelAndView(MANAGE_LANGUAGE_VIEW);
		
		if (!bindingResult.hasErrors()) {
			List<Message> messagesToRemove = languageBean.getMessagesToRemove();
			for (Message message : messagesToRemove) {
				messageService.deleteMessage(message);
			}
			
			List<Message> updatedMessages = languageBean.getUpdatedMessages();
			for (Message message : updatedMessages) {
				if (!messagesToRemove.contains(message)) {
					messageService.saveMessage(message);
				}
			}
			
			reinitialiseModel(languageBean);
			modelAndView.addObject("statusMessageKey", "status.language.save.success");
			reloadMessageSource();
		}
		
		return modelAndView;
	}
	
	@RequestMapping(method=RequestMethod.POST, params="delete")
	public ModelAndView deleteLanguage(@ModelAttribute(MODEL) final LanguageBean languageBean) {
		ModelAndView modelAndView = new ModelAndView(MANAGE_LANGUAGE_VIEW);
		
		if (languageBean.getSelectedLocale() != null) {
			for (Message message : languageBean.getMessages()) {
				messageService.deleteMessage(message);
			}
			
			languageBean.clearMessages();
			
			reinitialiseModel(languageBean);
			modelAndView.addObject("statusMessageKey", "status.language.delete.success");
			reloadMessageSource();
		}
		
		return modelAndView;
	}
	
	private void reloadMessageSource() {
		try {
			messageReloader.onMessageSourceReload();
		} catch (final Exception e) {
			LOGGER.error("Unable to reload messages: " + e, e);
		}
	}

	private void reinitialiseModel(final LanguageBean languageBean) {
		languageBean.reinitialiseSelectedLocale();
		languageBean.setNewLocale(false);
		
		if (languageBean.getSelectedLocale() != null) {
			languageBean.setMessages(messageService.getMessagesByLocaleOrderedByKey(languageBean.getSelectedLocale()));
		} else {
			languageBean.setMessages(messageService.getDefaultMessagesOrderedByKey());
		}
		
		languageBean.setSupportedLocales(messageService.getSupportedLocalesOrderedByLanguageAndCountry());
	}
	
	@ModelAttribute(MODEL)
	public LanguageBean createModel(@RequestParam(value = "locale", required = false) final String localeStr) {
		List<Locale> supportedLocales = messageService.getSupportedLocalesOrderedByLanguageAndCountry();
		LanguageBean languageBean = new LanguageBean(supportedLocales);
		
		if (StringUtils.isNotBlank(localeStr)) {
			if (NEW.equals(localeStr)) {
				languageBean.setNewLocale(true);
			} else {
				Locale locale = org.springframework.util.StringUtils.parseLocaleString(localeStr);
				languageBean.setSelectedLocale(locale);
				languageBean.setMessages(messageService.getMessagesByLocaleOrderedByKey(locale));
			}
		} else {
			languageBean.setMessages(messageService.getDefaultMessagesOrderedByKey());
		}
		
		return languageBean;
	}
	
	@InitBinder
	public void initBinder(final WebDataBinder binder) {
		binder.setValidator(validator);
	}
}
