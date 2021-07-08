package com.oup.eac.admin.controllers;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.oup.eac.admin.beans.ExternalSystemBean;
import com.oup.eac.admin.validators.ExternalSystemBeanValidator;
import com.oup.eac.domain.ExternalSystem;
import com.oup.eac.dto.Message;
import com.oup.eac.integration.facade.exceptions.ErightsException;
import com.oup.eac.service.ExternalIdService;
import com.oup.eac.service.ServiceLayerException;

@Controller
@RequestMapping("/mvc/externalsystem/manage.htm")
public class ManageExternalSystemController {	
	
	private static final String MODEL = "externalSystemBean";
	private static final String VIEW = "manageExternalSystem";
	
	private final ExternalIdService externalIdService;
	private final ExternalSystemBeanValidator validator;
	
	@Autowired
	public ManageExternalSystemController(final ExternalIdService externalIdService, final ExternalSystemBeanValidator validator) {
		this.externalIdService = externalIdService;
		this.validator = validator;
	}

	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView displayForm() {
		return new ModelAndView(VIEW);
	}
	
	@RequestMapping(method = RequestMethod.POST)
	public ModelAndView saveExternalSystem(@Valid @ModelAttribute(MODEL) final ExternalSystemBean externalSystemBean, final BindingResult bindingResult) throws ServiceLayerException {
		
		ModelAndView modelAndView = new ModelAndView(VIEW);

		if (!bindingResult.hasErrors()) {
			
			//Added to recreate UAT behavior. This check can be moved to Validator with proper validation message.
			if(externalSystemBean.getNewExternalSystemName()!=null && externalSystemBean.getNewExternalSystemName().length() > 255){
				throw new ServiceLayerException("Unexpected error.", new  Message( "error.unknown", "Unexpected error."));  
			}
			
			ExternalSystem externalSystem = externalSystemBean.getUpdatedExternalSystem();
			//externalIdService.saveOrUpdate(externalSystem);
			
			String oldSystemName=externalSystemBean.getOldSystemName();
			//Create new external system  or update existing  external system in Atypon based in edit flag
			if(!externalSystemBean.isEditFlag())
				externalIdService.createExternalSystem(externalSystem);
			else
				externalIdService.updateExternalSystem(externalSystem,oldSystemName);
			
			//Delete ExternalSystemTypes from Atypon
			if(externalSystemBean.getSystemTypesToDelete()!=null && externalSystemBean.getSystemTypesToDelete().size()>0)
			{
				externalIdService.deleteExternalSystemTypes(externalSystem, externalSystemBean.getSystemTypesToDelete());
			}
					
			
			try {
				externalSystemBean.refreshWith(externalIdService.getAllExternalSystemsOrderedByName());
			} catch (ErightsException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			externalSystemBean.setSelectedExternalSystemGuid(externalSystem.getId());
			modelAndView.addObject("statusMessageKey", "status.externalSystem.save.success");
		}
		
		return modelAndView;
	}
	
	@RequestMapping(method=RequestMethod.POST, params="delete")
	public ModelAndView deleteExternalSystem(@ModelAttribute(MODEL) final ExternalSystemBean externalSystemBean) {
		
		
		//Delete External System from Atypon
		externalIdService.deleteExternalSystem(externalSystemBean.getSelectedExternalSystem());
		try {
			externalSystemBean.refreshWith(externalIdService.getAllExternalSystemsOrderedByName());
		} catch (ErightsException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ModelAndView modelAndView = new ModelAndView(VIEW);
		modelAndView.addObject("statusMessageKey", "status.externalSystem.delete.success");
		return modelAndView;
	}
	
	@ModelAttribute(MODEL)
	public ExternalSystemBean createModel() {
		ExternalSystemBean externalSystemBean = null;
		try {
			externalSystemBean = new ExternalSystemBean(externalIdService.getAllExternalSystemsOrderedByName());
		} catch (ErightsException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return externalSystemBean;
	}
	
	@InitBinder
	public void initBinder(final WebDataBinder binder) {
		binder.setValidator(validator);
	}
}
