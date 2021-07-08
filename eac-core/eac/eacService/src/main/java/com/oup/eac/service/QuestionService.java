package com.oup.eac.service;

import java.util.List;

import com.oup.eac.domain.Question;

public interface QuestionService {
	
	Question getQuestionById(final String id);

	List<Question> getQuestionsOrderedByElementText();
	
	boolean isExportNameInUse(final String exportName, final String... ignoreIds);
	
	void saveQuestion(final Question question);
	
	void deleteQuestion(final Question question);
}
