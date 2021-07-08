package com.oup.eac.service.impl;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import com.oup.eac.data.QuestionDao;
import com.oup.eac.domain.Question;
import com.oup.eac.service.QuestionService;

@Service("questionService")
public class QuestionServiceImpl implements QuestionService {

	private final QuestionDao questionDao;

	@Autowired
	public QuestionServiceImpl(final QuestionDao questionDao) {
		this.questionDao = questionDao;
	}

	@Override
	public Question getQuestionById(final String id) {
		return questionDao.findById(id, true);
	}

	@Override
	public List<Question> getQuestionsOrderedByElementText() {
		List<Question> questions = questionDao.findAll();
		Collections.sort(questions, new Comparator<Question>() {
			@Override
			public int compare(final Question arg0, final Question arg1) {
				return arg0.getElementText().compareTo(arg1.getElementText());
			}
		});
		return questions;
	}

	@Override
	public boolean isExportNameInUse(final String exportName, final String... ignoreIds) {
		return questionDao.isExportNameInUse(exportName, ignoreIds);
	}

	@Override
	public void saveQuestion(final Question question) {
		questionDao.saveOrUpdate(question);
	}

	@Override
	public void deleteQuestion(final Question question) {
		questionDao.delete(question);
	}

}
