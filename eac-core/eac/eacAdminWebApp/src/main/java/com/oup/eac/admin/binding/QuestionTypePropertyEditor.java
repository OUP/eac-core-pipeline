package com.oup.eac.admin.binding;

import java.beans.PropertyEditorSupport;

import com.oup.eac.domain.Question;
import com.oup.eac.service.QuestionService;

public class QuestionTypePropertyEditor extends PropertyEditorSupport {
	
	private final QuestionService questionService;
	
	public QuestionTypePropertyEditor(final QuestionService questionService) {
		this.questionService = questionService;
	}

	@Override
	public String getAsText() {
		Object value = getValue();
		return (value != null ? ((Question) value).getId() : "");
	}

	@Override
	public void setAsText(final String text) throws IllegalArgumentException {
		if (text == null) {
			setValue(null);
		} else {
			Question question = questionService.getQuestionById(text);
			setValue(question);
		}
	}
}