package com.oup.eac.admin.controllers;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.List;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.oup.eac.admin.beans.TrustedSystemBean;
import com.oup.eac.admin.validators.TrustedSystemBeanValidator;
import com.oup.eac.common.utils.EACSettings;
import com.oup.eac.common.utils.crypto.SimpleCipher;
import com.oup.eac.common.utils.exception.ExceptionUtil;
import com.oup.eac.domain.TrustedSystem;
import com.oup.eac.integration.facade.exceptions.ErightsException;
import com.oup.eac.service.CustomerService;
import com.oup.eac.service.ServiceLayerException;
import com.oup.eac.service.TrustedSystemService;

@Controller
@RequestMapping("/mvc/trustedSystems/manage.htm")
public class ManageTrustedSystemController {
	
	private static final Logger LOG = Logger.getLogger(ManageTrustedSystemController.class);
	private static final String CREATE_SUCCESS = "status.trusted.user.save.success";
	private static final String UPDATE_SUCCESS = "status.trusted.user.update.success";
	private static final String DELETE_SUCCESS = "status.trusted.user.delete.success";
	private static final String PASSWORD_ENCRYPTED_MSG = "msg.trusted.user.password.encrypt";
	private static final String STATUS_PASSWORD_ENCRYPTED_MSG = "passwordEncryptMsg";
	private static final String STATUS_PASSWORD_ENCRYPTED = "passwordEncrypt";
	private static final String STATUS_MSG_KEY = "statusMessageKey";
	
	private static final String MODEL = "trustedSystemBean";
	private static final String MANAGE_TRUSTED_SYSTEM_VIEW = "manageTrustedSystem";
	private final CustomerService customerService ;
	private final TrustedSystemService trustedSystemService;
	private final TrustedSystemBeanValidator validator;
	
	@Autowired
	public ManageTrustedSystemController(
			final TrustedSystemService trustedSystemService,
			final CustomerService customerService,
			final TrustedSystemBeanValidator validator) {
		this.trustedSystemService = trustedSystemService;
		this.customerService = customerService ;
		this.validator = validator;
	}
	
	@RequestMapping(method=RequestMethod.GET)
	public ModelAndView showForm() {
		return new ModelAndView(MANAGE_TRUSTED_SYSTEM_VIEW);
	}
	
	@RequestMapping(method = RequestMethod.POST)
	public ModelAndView saveTrustedUser(@Valid @ModelAttribute(MODEL) TrustedSystemBean trustedSystemBean, final BindingResult bindingResult) throws ServiceLayerException {

		ModelAndView modelAndView = new ModelAndView(MANAGE_TRUSTED_SYSTEM_VIEW);
		

		if (!bindingResult.hasErrors()) {

			TrustedSystem trustedSystem = trustedSystemBean.getUpdatedTrustedSystems();
			String encryptedPassword = getEncryptedPassword(trustedSystem.getPassword()) ;
			if (encryptedPassword == null) {
				bindingResult.rejectValue(null, "", new String[1], "");
			} else {
				if(!trustedSystemBean.isEditFlag()){
					List<String> errorMessages = new ArrayList<String>();
					try {
						errorMessages =  trustedSystemService.createTrustedSystem(trustedSystem);
						if(errorMessages.isEmpty()){
							refreshData(trustedSystemBean);
							modelAndView.addObject(STATUS_MSG_KEY, CREATE_SUCCESS);
							modelAndView.addObject(STATUS_PASSWORD_ENCRYPTED_MSG, PASSWORD_ENCRYPTED_MSG);
							modelAndView.addObject(STATUS_PASSWORD_ENCRYPTED, encryptedPassword);
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
						LOG.error(ExceptionUtil.getStackTrace(e));
					}
				}
				else{
					List<String> errorMessages = new ArrayList<String>();
					try {
						errorMessages = trustedSystemService.updateTrustedSystem(trustedSystem);
						if(errorMessages.isEmpty()){
							refreshData(trustedSystemBean);
							modelAndView.addObject(STATUS_MSG_KEY, UPDATE_SUCCESS);
							modelAndView.addObject(STATUS_PASSWORD_ENCRYPTED_MSG, PASSWORD_ENCRYPTED_MSG);
							modelAndView.addObject(STATUS_PASSWORD_ENCRYPTED, encryptedPassword);
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
						LOG.error(ExceptionUtil.getStackTrace(e));
					}
				}
			}
		}
		return modelAndView;

	}
	
	@RequestMapping(method=RequestMethod.POST, params="delete")
	public ModelAndView deleteTrustedUser(
			@ModelAttribute(MODEL) final TrustedSystemBean trustedSystemBean,
			final BindingResult bindingResult) {
		ModelAndView modelAndView = new ModelAndView(MANAGE_TRUSTED_SYSTEM_VIEW);
		try {
			customerService.deleteTrustedSystem(trustedSystemBean.getSelectedTrustedSystemId());
			modelAndView.addObject(STATUS_MSG_KEY, DELETE_SUCCESS);
		} catch (ServiceLayerException e) {
			bindingResult.rejectValue(null, e.getMessage());
			LOG.error(ExceptionUtil.getStackTrace(e));
		}
		return modelAndView ;
		
	}
	
	@ModelAttribute(MODEL)
	public TrustedSystemBean createModel() throws Exception {
		
		List<TrustedSystem> trustedSystemList = null;
		try {
			trustedSystemList = trustedSystemService.fetchAllTrustedSystems();
		} catch (Exception e) {
			LOG.error(ExceptionUtil.getStackTrace(e));
		} 
		
		TrustedSystemBean trustedSystemBean = new TrustedSystemBean(trustedSystemList);
		
		return trustedSystemBean;
	}
	
	@InitBinder
	public void initBinder(final WebDataBinder binder) {
		binder.setValidator(validator);
	}
	
	/**
	 * refreshData used to refresh and compare data with db
	 * @param trustedSystemBean
	 */
	public void refreshData(TrustedSystemBean trustedSystemBean){
		try{
			List<TrustedSystem> trustedSystem = trustedSystemService.fetchAllTrustedSystems() ; //call search
			trustedSystemBean.getTrustedSystems().clear();
			trustedSystemBean.setNewUserName(null);
			trustedSystemBean.setNewPassword(null);
			trustedSystemBean.setNewConfirmPassword(null);
			trustedSystemBean.setTrustedSystems(trustedSystem);
		}catch(Exception e){
			LOG.error(ExceptionUtil.getStackTrace(e));
		}
	}
	public String getEncryptedPassword(final String plainPassword) {
		String encryptedText = null ;
		try {
			encryptedText = SimpleCipher.encryptTextRSA(plainPassword, EACSettings.getProperty(EACSettings.PASSWORD_ENCRYPTION_PUBLIC_KEY));
		} catch (InvalidKeyException e) {
			LOG.error(ExceptionUtil.getStackTrace(e));
		} catch (NoSuchAlgorithmException e) {
			LOG.error(ExceptionUtil.getStackTrace(e));
		} catch (InvalidKeySpecException e) {
			LOG.error(ExceptionUtil.getStackTrace(e));
		} catch (UnsupportedEncodingException e) {
			LOG.error(ExceptionUtil.getStackTrace(e));
		} catch (IllegalBlockSizeException e) {
			LOG.error(ExceptionUtil.getStackTrace(e));
		} catch (BadPaddingException e) {
			LOG.error(ExceptionUtil.getStackTrace(e));
		}
		return encryptedText;
	}
}
