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

import com.oup.eac.admin.beans.ComponentBean;
import com.oup.eac.admin.binding.ElementTypePropertyEditor;
import com.oup.eac.admin.validators.ComponentBeanValidator;
import com.oup.eac.domain.Component;
import com.oup.eac.domain.Element;
import com.oup.eac.domain.utils.audit.AuditLogger;
import com.oup.eac.service.ComponentService;
import com.oup.eac.service.ElementService;

@Controller
@RequestMapping("/mvc/component/manage.htm")
public class ManageComponentController {
	
	private static final String NEW = "new";
	private static final String MANAGE_COMPONENT_VIEW = "manageComponent";
	private static final String MANAGE_COMPONENT_FORM_VIEW = "manageComponentForm";
	private static final String MODEL = "componentBean";
	
	private final ComponentService componentService;
	private final ElementService elementService;
	private final ComponentBeanValidator validator;
	
	@Autowired
	public ManageComponentController(
			final ComponentService componentService,
			final ElementService elementService,
			final ComponentBeanValidator validator) {
		this.componentService = componentService;
		this.elementService = elementService;
		this.validator = validator;
	}

	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView showComponentList() {
		return new ModelAndView(MANAGE_COMPONENT_VIEW);
	}

	@RequestMapping(method = RequestMethod.GET, params = { "id", "deep" })
	public ModelAndView showAll() {
		return new ModelAndView(MANAGE_COMPONENT_VIEW);
	}

	@RequestMapping(method = RequestMethod.GET, params = "id")
	public ModelAndView showForm() {
		return new ModelAndView(MANAGE_COMPONENT_FORM_VIEW);
	}
	
	@ModelAttribute(MODEL)
	public ComponentBean createModel(@RequestParam(value = "id", required = false) final String id) {
		ComponentBean componentBean = new ComponentBean(
				componentService.getComponentsOrderedByName(), 
				elementService.getElementsOrderedByName());

		if (StringUtils.isNotBlank(id)) {
			if (NEW.equals(id)) {
				componentBean.setNewComponent(true);
			} else {
				componentBean.setSelectedComponent(componentService.getComponentById(id));
			}
		}

		return componentBean;
	}
	
	@RequestMapping(method = RequestMethod.POST)
	public ModelAndView saveComponent(@Valid @ModelAttribute(MODEL) final ComponentBean componentBean, final BindingResult bindingResult) {
		
		if (bindingResult.hasErrors()) {
			return new ModelAndView(MANAGE_COMPONENT_VIEW);
		}
		
		Component component = componentBean.getUpdatedComponent();
		componentService.saveComponent(component);
		if (componentBean.isNewComponent()) {
			AuditLogger.logEvent("New component '" + component.getName() + "' created");
		} else {
			AuditLogger.logEvent("Component '" + component.getName() + "' updated");
		}
		return new ModelAndView("redirect:/mvc/component/manage.htm?statusMessageKey=status.component.save.success&deep=1&id=" + component.getId());
	}
	
	@RequestMapping(method=RequestMethod.POST, params="delete")
	public ModelAndView deleteComponent(@ModelAttribute(MODEL) final ComponentBean componentBean) {
		Component component = componentBean.getSelectedComponent();
		componentService.deleteComponent(component);
		AuditLogger.logEvent("Component '" + component.getName() + "' deleted");
		return new ModelAndView("redirect:/mvc/component/manage.htm?statusMessageKey=status.component.delete.success");
	}
	
	@InitBinder
	public void initBinder(final WebDataBinder webDataBinder) {
		webDataBinder.setValidator(validator);
		webDataBinder.registerCustomEditor(Element.class, new ElementTypePropertyEditor(elementService));
	}
}
