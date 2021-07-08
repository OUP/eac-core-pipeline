package com.oup.eac.admin.controllers;

import java.io.IOException;
import java.io.OutputStream;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.io.IOUtils;
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

import com.oup.eac.admin.beans.ElementBean;
import com.oup.eac.admin.binding.QuestionTypePropertyEditor;
import com.oup.eac.admin.binding.TagTypePropertyEditor;
import com.oup.eac.admin.validators.ElementBeanValidator;
import com.oup.eac.domain.Element;
import com.oup.eac.domain.Question;
import com.oup.eac.domain.Tag;
import com.oup.eac.domain.Tag.TagType;
import com.oup.eac.domain.utils.audit.AuditLogger;
import com.oup.eac.service.ElementService;
import com.oup.eac.service.QuestionService;

@Controller
@RequestMapping("/mvc/element/manage.htm")
public class ManageElementController {
	
	private static final Logger LOGGER = Logger.getLogger(ManageElementController.class);

	private static final String NEW = "new";
	private static final String MANAGE_ELEMENT_VIEW = "manageElement";
	private static final String MANAGE_ELEMENT_FORM_VIEW = "manageElementForm";
	private static final String MODEL = "elementBean";
	private static final String DISPLAY_TYPES = "displayTypes";

	private final ElementService elementService;
	private final QuestionService questionService;
	private final ElementBeanValidator validator;

	@Autowired
	public ManageElementController(
			final ElementService elementService,
			final QuestionService questionService,
			final ElementBeanValidator validator) {
		this.elementService = elementService;
		this.questionService = questionService;
		this.validator = validator;
	}

	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView showElementList() {
		return new ModelAndView(MANAGE_ELEMENT_VIEW);
	}
	
	@RequestMapping(method = RequestMethod.GET, params = { "id", "deep" })
	public ModelAndView showAll() {
		return new ModelAndView(MANAGE_ELEMENT_VIEW);
	}

	@RequestMapping(method = RequestMethod.GET, params = "id")
	public ModelAndView showForm() {
		return new ModelAndView(MANAGE_ELEMENT_FORM_VIEW);
	}

	@RequestMapping(method = RequestMethod.POST, params = "regex")
	public void validateRegex(
			@RequestParam(value = "regex") final String regex, 
			final HttpServletResponse response) throws IOException {
		boolean valid = true;
		
		try {
			Pattern.compile(regex);
		} catch (final Exception e) {
			LOGGER.debug("Invalid regular expression: " + regex + ": " + StringUtils.trimToEmpty(e.getMessage()));
			valid = false;
		}
		
		response.setContentType("application/json");
		OutputStream out = response.getOutputStream();
		IOUtils.write("{\"valid\": \"" + valid + "\"}", out);
	}
	
	@RequestMapping(method = RequestMethod.POST)
	public ModelAndView saveElement(@Valid @ModelAttribute(MODEL) final ElementBean elementBean, final BindingResult bindingResult) {
		
		if (bindingResult.hasErrors()) {
			return new ModelAndView(MANAGE_ELEMENT_VIEW);
		}
		
		Element element = elementBean.getUpdatedElement();
		elementService.saveElement(element);
		if (elementBean.isNewElement()) {
			AuditLogger.logEvent("New element '" + element.getName() + "' created");
		} else {
			AuditLogger.logEvent("Element '" + element.getName() + "' updated");
		}
		return new ModelAndView("redirect:/mvc/element/manage.htm?statusMessageKey=status.element.save.success&deep=1&id=" + element.getId());
	}
	
	@RequestMapping(method=RequestMethod.POST, params="delete")
	public ModelAndView deleteElement(@ModelAttribute(MODEL) final ElementBean elementBean) {
		Element element = elementBean.getSelectedElement();
		elementService.deleteElement(element);
		AuditLogger.logEvent("Element '" + element.getName() + "' deleted");
		return new ModelAndView("redirect:/mvc/element/manage.htm?statusMessageKey=status.element.delete.success");
	}
	
	@ModelAttribute(DISPLAY_TYPES)
	public TagType[] getDisplayTypes() {
		return Tag.TagType.values();
	}

	@ModelAttribute(MODEL)
	public ElementBean createModel(@RequestParam(value = "id", required = false) final String id) {
		ElementBean elementBean = new ElementBean(
				elementService.getElementsOrderedByName(), 
				questionService.getQuestionsOrderedByElementText());

		if (StringUtils.isNotBlank(id)) {
			if (NEW.equals(id)) {
				elementBean.setNewElement(true);
			} else {
				elementBean.setSelectedElement(elementService.getElementById(id));
			}
		}

		return elementBean;
	}
	
	@InitBinder
	public void initBinder(final WebDataBinder webDataBinder) {
		webDataBinder.setValidator(validator);
		webDataBinder.registerCustomEditor(TagType.class, new TagTypePropertyEditor());
		webDataBinder.registerCustomEditor(Question.class, new QuestionTypePropertyEditor(questionService));
	}
}
