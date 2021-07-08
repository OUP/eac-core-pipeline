package com.oup.eac.admin.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.oup.eac.domain.ExportName;
import com.oup.eac.domain.Question;

public class QuestionBean implements Serializable {

	private List<Question> questions;
	private Question selectedQuestion;
	private boolean newQuestion;
	private List<ExportName> newExportNames = new ArrayList<ExportName>();
	private String exportNamesToRemove;

	public QuestionBean(final List<Question> questions) {
		this.questions = questions;
	}

	public List<Question> getQuestions() {
		return questions;
	}

	public void setQuestions(final List<Question> questions) {
		this.questions = questions;
	}

	public Question getSelectedQuestion() {
		return selectedQuestion;
	}

	public void setSelectedQuestion(final Question selectedQuestion) {
		this.selectedQuestion = selectedQuestion;
	}
	
	public boolean isSelectedQuestionDeletable() {
		boolean deletable = false;
		if (selectedQuestion != null) {
			deletable = selectedQuestion.getElements().isEmpty() /*&& selectedQuestion.getAnswers().isEmpty()*/;
		}
		return deletable;
	}

	public boolean isNewQuestion() {
		return newQuestion;
	}

	public void setNewQuestion(final boolean newQuestion) {
		this.newQuestion = newQuestion;
	}

	public Question getUpdatedQuestion() {
		for (ExportName exportName : newExportNames) {
			selectedQuestion.addExportName(exportName);
		}
		if (StringUtils.isNotBlank(exportNamesToRemove)) {
			for (String id : exportNamesToRemove.split(",")) {
				if (StringUtils.isNotBlank(id)) {
					selectedQuestion.removeExportName(id.trim());
				}
			}
		}
		return selectedQuestion;
	}
	
	public void clearSelectedQuestion() {
		this.selectedQuestion = null;
	}

	public String getExportNamesToRemove() {
		return exportNamesToRemove;
	}

	public void setExportNamesToRemove(final String exportNamesToRemove) {
		this.exportNamesToRemove = exportNamesToRemove;
	}

	public List<ExportName> getNewExportNames() {
		return newExportNames;
	}

	public void setNewExportNames(final List<ExportName> newExportNames) {
		this.newExportNames = newExportNames;
	}

	public boolean isSelectedQuestionSet() {
		return selectedQuestion != null;
	}
}
