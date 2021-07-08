package com.oup.eac.admin.controllers;

import javax.validation.Valid;

import org.apache.commons.lang.StringUtils;
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

import com.oup.eac.admin.beans.QuestionBean;
import com.oup.eac.admin.binding.ExportTypePropertyEditor;
import com.oup.eac.admin.validators.QuestionBeanValidator;
import com.oup.eac.domain.ExportType;
import com.oup.eac.domain.Question;
import com.oup.eac.domain.utils.audit.AuditLogger;
import com.oup.eac.service.QuestionService;

@Controller
@RequestMapping("/mvc/question/manage.htm")
public class ManageQuestionController {

	private static final String NEW = "new";
	private static final String MANAGE_QUESTION_VIEW = "manageQuestion";
	private static final String MANAGE_QUESTION_FORM_VIEW = "manageQuestionForm";
	private static final String MODEL = "questionBean";
	private static final String EXPORT_TYPES = "exportTypes";
	
	private final QuestionService questionService;
	private final QuestionBeanValidator validator;
	
	@Autowired
	public ManageQuestionController(
			final QuestionService questionService,
			final QuestionBeanValidator validator) {
		this.questionService = questionService;
		this.validator = validator;
	}

	@RequestMapping(method=RequestMethod.GET)
	public ModelAndView showQuestionList() {
		return new ModelAndView(MANAGE_QUESTION_VIEW);
	}
	
	@RequestMapping(method = RequestMethod.GET, params = { "id", "deep" })
	public ModelAndView showAll() {
		return new ModelAndView(MANAGE_QUESTION_VIEW);
	}
	
	@RequestMapping(method=RequestMethod.GET, params="id")
	public ModelAndView showForm() {
		return new ModelAndView(MANAGE_QUESTION_FORM_VIEW);
	}
	
	@RequestMapping(method = RequestMethod.POST)
	public ModelAndView saveQuestion(@Valid @ModelAttribute(MODEL) final QuestionBean questionBean, final BindingResult bindingResult) {
		
		if (bindingResult.hasErrors()) {
			return new ModelAndView(MANAGE_QUESTION_VIEW);
		}
		
		Question question = questionBean.getUpdatedQuestion();
		questionService.saveQuestion(question);
		if (questionBean.isNewQuestion()) {
			AuditLogger.logEvent("New question '" + question.getElementText() + "' created");
		} else {
			AuditLogger.logEvent("Question '" + question.getElementText() + "' updated");
		}
		return new ModelAndView("redirect:/mvc/question/manage.htm?statusMessageKey=status.question.save.success&deep=1&id=" + question.getId());
	}
	
	@RequestMapping(method=RequestMethod.POST, params="delete")
	public ModelAndView deleteQuestion(@ModelAttribute(MODEL) final QuestionBean questionBean) {
		Question question = questionBean.getSelectedQuestion();
		questionService.deleteQuestion(question);
		AuditLogger.logEvent("Question '" + question.getElementText() + "' deleted");
		return new ModelAndView("redirect:/mvc/question/manage.htm?statusMessageKey=status.question.delete.success");
	}
	
	@ModelAttribute(EXPORT_TYPES)
	public ExportType[] getExportTypes() {
		return ExportType.values();
	}
	
	@ModelAttribute(MODEL)
	public QuestionBean createModel(@RequestParam(value = "id", required = false) final String id) {
		QuestionBean questionBean = new QuestionBean(questionService.getQuestionsOrderedByElementText());
		
		if (StringUtils.isNotBlank(id)) {
			if (NEW.equals(id)) {
				questionBean.setNewQuestion(true);
			} else {
				questionBean.setSelectedQuestion(questionService.getQuestionById(id));
			}
		}
		
		return questionBean;
	}
	
	@InitBinder
	public void initBinder(final WebDataBinder webDataBinder) {
		webDataBinder.setValidator(validator);
		webDataBinder.registerCustomEditor(ExportType.class, new ExportTypePropertyEditor());
	}
}
