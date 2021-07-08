package com.oup.eac.admin.controllers;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.oup.eac.admin.beans.PlatformBean;
import com.oup.eac.admin.validators.PlatformBeanValidator;
import com.oup.eac.domain.Platform;
import com.oup.eac.integration.facade.exceptions.ErightsException;
import com.oup.eac.integration.facade.exceptions.PlatformNotFoundException;
import com.oup.eac.service.PlatformService;
import com.oup.eac.service.ServiceLayerException;

@Controller
@RequestMapping("/mvc/PlatformMaster/platformMaster.htm")
public class ManagePlatformController {
	
	private static final String CREATE_SUCCESS = "status.platform.save.success";
	private static final String UPDATE_SUCCESS = "status.platform.update.success";
	private static final String DELETE_SUCCESS = "status.platform.delete.success";
	private static final String STATUS_MSG_KEY = "statusMessageKey";
	
	private static final String MODEL = "platformBean";
	private static final String MANAGE_PLATFORM_VIEW = "managePlatform";
	
	private final PlatformService platformService;
	private final PlatformBeanValidator validator;
	
	@Autowired
	public ManagePlatformController(
			final PlatformService platformService,
			final PlatformBeanValidator validator) {
		this.platformService = platformService;
		this.validator = validator;
	}
	
	@RequestMapping(method=RequestMethod.GET)
	public ModelAndView showForm() {
		return new ModelAndView(MANAGE_PLATFORM_VIEW);
	}
	
	@RequestMapping(method = RequestMethod.POST)
	public ModelAndView savePlatform(@Valid @ModelAttribute(MODEL) final PlatformBean platformBean, final BindingResult bindingResult) throws ServiceLayerException {

		ModelAndView modelAndView = new ModelAndView(MANAGE_PLATFORM_VIEW);
		

		if (!bindingResult.hasErrors()) {

			Platform platform = platformBean.getUpdatedPlatform();
			if(!platformBean.isEditFlag()){
				List<String> errorMessages = new ArrayList<String>();
				try {
					errorMessages =  platformService.createPlatform(platform);
					if(errorMessages.isEmpty()){
						refreshData(platformBean);
						modelAndView.addObject(STATUS_MSG_KEY, CREATE_SUCCESS);
					}
					else{
						Object[] errorMessagesArray = new String[errorMessages.size()];
						errorMessagesArray = errorMessages.toArray(errorMessagesArray);
						for(int i=0;i<errorMessages.size();i++){
							bindingResult.rejectValue(null, "", errorMessagesArray, errorMessages.get(i));
						}
					}
					errorMessages.clear();
				} catch (ErightsException e) {
					e.printStackTrace();
				}
			}
			else{
				List<String> errorMessages = new ArrayList<String>();
				try {
					errorMessages = platformService.updatePlatform(platform);
					if(errorMessages.isEmpty()){
						refreshData(platformBean);
						modelAndView.addObject(STATUS_MSG_KEY, UPDATE_SUCCESS);
					}
					else{
						Object[] errorMessagesArray = new String[errorMessages.size()];
						errorMessagesArray = errorMessages.toArray(errorMessagesArray);
						for(int i=0;i<errorMessages.size();i++){
							bindingResult.rejectValue(null, "", errorMessagesArray, errorMessages.get(i));
						}
					}
					errorMessages.clear();
				} catch (ErightsException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return modelAndView;

	}
	
	@RequestMapping(method=RequestMethod.POST, params="delete")
	public ModelAndView deletePlatform(
			@ModelAttribute(MODEL) final PlatformBean platformBean,
			final BindingResult bindingResult) {
		Platform request = new Platform();
		List<Platform> platformList = platformBean.getPlatforms() ;
		for ( Platform platform : platformList) {
			if (platform.getPlatformId().equals(platformBean.getSelectedPlatformGuid())) {
				request.setCode(platform.getCode());
				break ;
			}
		}
		
		ModelAndView modelAndView = new ModelAndView(MANAGE_PLATFORM_VIEW);
		List<String> errorMessages = new ArrayList<String>();
		// Delete platform
		try {
			errorMessages = platformService.deletePlatform(request);
			if (errorMessages.isEmpty()) {
				refreshData(platformBean);
				modelAndView.addObject(STATUS_MSG_KEY,
						DELETE_SUCCESS);
			} else {
				Object[] errorMessagesArray = new String[errorMessages.size()];
				errorMessagesArray = errorMessages.toArray(errorMessagesArray);
				for (int i = 0; i < errorMessages.size(); i++) {
					bindingResult.rejectValue(null, "", errorMessagesArray,
							errorMessages.get(i));
				}
			}
			errorMessages.clear();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return modelAndView;
	}
	
	@ModelAttribute(MODEL)
	public PlatformBean createModel() throws PlatformNotFoundException, AccessDeniedException, ErightsException {
		
		List<Platform> platformList = platformService.getAllPlatforms();
		//updatePlatformListWithEscapeCharacter(platformList) ;
		PlatformBean platformBean = new PlatformBean(platformList);
		
		return platformBean;
	}
	
	@InitBinder
	public void initBinder(final WebDataBinder binder) {
		binder.setValidator(validator);
	}
	
	/**
	 * refreshData used to refresh and compare data with db
	 * @param platformBean
	 */
	public void refreshData(PlatformBean platformBean){
		try{
			List<Platform> newPlatforms = platformService.getAllPlatforms();
			//updatePlatformListWithEscapeCharacter(newPlatforms);
			platformBean.getPlatforms().clear();
			platformBean.setNewPlatformCode(null);
			platformBean.setNewPlatformDescription(null);
			platformBean.setNewPlatformName(null);
			platformBean.setSelectedPlatformGuid(null);
			platformBean.setPlatforms(newPlatforms);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/*private void updatePlatformListWithEscapeCharacter( List<Platform> platfroms){
		for (Platform platform : platfroms ) {
			platform.setCode(JavaScriptUtils.javaScriptEscape(HtmlUtils.htmlEscape(platform.getCode())));
			platform.setName(JavaScriptUtils.javaScriptEscape(HtmlUtils.htmlEscape(platform.getName())));
			platform.setDescription(JavaScriptUtils.javaScriptEscape(HtmlUtils.htmlEscape(platform.getDescription())));
		}
	}*/
}
