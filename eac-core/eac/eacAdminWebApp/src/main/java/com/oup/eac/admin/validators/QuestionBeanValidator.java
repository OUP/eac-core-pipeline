package com.oup.eac.admin.validators;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.oup.eac.admin.beans.QuestionBean;
import com.oup.eac.domain.ExportName;
import com.oup.eac.service.QuestionService;

@Component
public class QuestionBeanValidator implements Validator {
	
	private final QuestionService questionService;
	
	@Autowired
	public QuestionBeanValidator(final QuestionService questionService) {
		this.questionService = questionService;
	}

	@Override
	public void validate(final Object target, final Errors errors) {
		QuestionBean questionBean = (QuestionBean) target;

		validateName(questionBean, errors);
		validateElementMessage(questionBean, errors);
		validateExportNames(questionBean, errors);
	}

	private void validateName(final QuestionBean questionBean, final Errors errors) {
		if (StringUtils.isBlank(questionBean.getSelectedQuestion().getDescription())) {
			errors.rejectValue("selectedQuestion.description", "error.question.name.empty");
		}
	}

	private void validateElementMessage(final QuestionBean questionBean, final Errors errors) {
		if (StringUtils.isBlank(questionBean.getSelectedQuestion().getElementText())) {
			errors.rejectValue("selectedQuestion.elementText", "error.question.elementMessage.empty");
		}
	}

	private void validateExportNames(final QuestionBean questionBean, final Errors errors) {
		List<ExportName> newExportNames = questionBean.getNewExportNames();
		for (int i = 0; i < newExportNames.size(); i++) {
			ExportName exportName = newExportNames.get(i);
			if (StringUtils.isBlank(exportName.getName())) {
				errors.rejectValue("newExportNames[" + i + "].name", "error.question.exportName.name.empty");
			} else if (questionService.isExportNameInUse(exportName.getName())) {
				errors.rejectValue("newExportNames[" + i + "].name", "error.question.exportName.name.inUse");
			}
		}
		if (questionBean.isSelectedQuestionSet()) {
			List<ExportName> existingExportNames = questionBean.getSelectedQuestion().getExportNamesAsList();
			for (int i = 0; i < existingExportNames.size(); i++) {
				ExportName exportName = existingExportNames.get(i);
				if (StringUtils.isBlank(exportName.getName())) {
					errors.rejectValue("selectedQuestion.exportNamesAsList[" + i + "].name", "error.question.exportName.name.empty");
				} else if (questionService.isExportNameInUse(exportName.getName(), exportName.getId())) {
					errors.rejectValue("selectedQuestion.exportNamesAsList[" + i + "].name", "error.question.exportName.name.inUse");
				}
			}
		}
	}
	
	@Override
	public boolean supports(final Class<?> clazz) {
		final boolean supports = QuestionBean.class.isAssignableFrom(clazz);
		return supports;
	}
}
