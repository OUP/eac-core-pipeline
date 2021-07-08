package com.oup.eac.admin.validators;

import java.util.ArrayList;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;
import org.springframework.validation.Errors;

import com.oup.eac.admin.beans.QuestionBean;
import com.oup.eac.domain.ExportName;
import com.oup.eac.domain.ExportType;
import com.oup.eac.domain.Question;
import com.oup.eac.service.QuestionService;

public class QuestionBeanValidatorTest {
	
	private QuestionBeanValidator validator;
	private QuestionService mockQuestionService;
	
	@Before
	public void setUp() {
		mockQuestionService = EasyMock.createMock(QuestionService.class);
		validator = new QuestionBeanValidator(mockQuestionService);
	}
	

	@Test
	public void shouldRejectWhenNameEmpty() {
		Question question = new Question();
		question.setDescription("");
		question.setElementText("element text");
		QuestionBean questionBean = new QuestionBean(new ArrayList<Question>());
		questionBean.setSelectedQuestion(question);
		
		Errors mockErrors = EasyMock.createMock(Errors.class);
		mockErrors.rejectValue("selectedQuestion.description", "error.question.name.empty");

		EasyMock.replay(mockErrors);
		
		validator.validate(questionBean, mockErrors);
		
		EasyMock.verify(mockErrors);
	}
	
	@Test
	public void shouldRejectWhenElementMessageEmpty() {
		Question question = new Question();
		question.setDescription("the name");
		question.setElementText("");
		QuestionBean questionBean = new QuestionBean(new ArrayList<Question>());
		questionBean.setSelectedQuestion(question);
		
		Errors mockErrors = EasyMock.createMock(Errors.class);
		mockErrors.rejectValue("selectedQuestion.elementText", "error.question.elementMessage.empty");
		
		EasyMock.replay(mockErrors);
		
		validator.validate(questionBean, mockErrors);
		
		EasyMock.verify(mockErrors);
	}
	
	@Test
	public void shouldRejectWhenExportNameEmpty() {
		Question question = new Question();
		question.setDescription("the name");
		question.setElementText("element text");
		QuestionBean questionBean = new QuestionBean(new ArrayList<Question>());
		questionBean.setSelectedQuestion(question);
		ExportName exportName = new ExportName();
		exportName.setExportType(ExportType.CMDP);
		questionBean.getNewExportNames().add(exportName);
		
		Errors mockErrors = EasyMock.createMock(Errors.class);
		mockErrors.rejectValue("newExportNames[0].name", "error.question.exportName.name.empty");
		
		EasyMock.replay(mockErrors);
		
		validator.validate(questionBean, mockErrors);
		
		EasyMock.verify(mockErrors);
	}
	
	@Test
	public void shouldRejectWhenExportNameInUse() {
		Question question = new Question();
		question.setDescription("the name");
		question.setElementText("element text");
		QuestionBean questionBean = new QuestionBean(new ArrayList<Question>());
		questionBean.setSelectedQuestion(question);
		ExportName exportName = new ExportName();
		exportName.setExportType(ExportType.CMDP);
		exportName.setName("export_name");
		questionBean.getNewExportNames().add(exportName);
		
		EasyMock.expect(mockQuestionService.isExportNameInUse("export_name")).andReturn(true);
		
		Errors mockErrors = EasyMock.createMock(Errors.class);
		mockErrors.rejectValue("newExportNames[0].name", "error.question.exportName.name.inUse");
		
		EasyMock.replay(mockErrors, mockQuestionService);
		
		validator.validate(questionBean, mockErrors);
		
		EasyMock.verify(mockErrors, mockQuestionService);
	}
}
