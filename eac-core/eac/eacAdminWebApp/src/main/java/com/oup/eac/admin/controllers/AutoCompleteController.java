package com.oup.eac.admin.controllers;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.oup.eac.domain.Message;
import com.oup.eac.service.MessageService;

@Controller
@RequestMapping("/mvc/autocomplete")
public class AutoCompleteController {

	private final MessageService messageService;

	@Autowired
	public AutoCompleteController(final MessageService messageService) {
		this.messageService = messageService;
	}

	@RequestMapping(value="/messages.htm", method = RequestMethod.GET, params = { "term" })
	public @ResponseBody List<AutoCompletedMessage> autoCompleteMessages(@RequestParam("term") final String term) {
		List<AutoCompletedMessage> response = new ArrayList<AutoCompletedMessage>();
		
		if (StringUtils.isNotBlank(term)) {
			List<Message> messages = messageService.findMessagesWithKeyOrTextContaining(term);
			
			for (Message message : messages) {
				response.add(new AutoCompletedMessage(getLabel(message), message.getKey()));
			}
		}
		
		return response;
	}
	
	public static class AutoCompletedMessage {

		private String label;
		private String value;
		
		public AutoCompletedMessage(final String label, final String value) {
			this.label = label;
			this.value = value;
		}

		public String getLabel() {
			return label;
		}

		public void setLabel(String label) {
			this.label = label;
		}

		public String getValue() {
			return value;
		}

		public void setValue(String value) {
			this.value = value;
		}

	}
	
	private String getLabel(final Message message) {
		StringBuilder builder = new StringBuilder();
		builder.append(message.getKey());
		builder.append(" - ");
		builder.append(message.getMessage());
		return builder.toString();
	}
	
	
}
