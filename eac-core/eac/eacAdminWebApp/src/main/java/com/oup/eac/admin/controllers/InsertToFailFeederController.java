package com.oup.eac.admin.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.oup.eac.admin.beans.InsertIntoCloudFailFeederBean;
import com.oup.eac.common.utils.exception.ExceptionUtil;
import com.oup.eac.dto.Message;
import com.oup.eac.service.InsertIntoCloudFailFeeder;
import com.oup.eac.service.ServiceLayerException;

@Controller
@RequestMapping("/mvc/support/insertToFailFeeder.htm")
public class InsertToFailFeederController {

	
	private static final Logger LOGGER = Logger.getLogger(InsertToFailFeederController.class);
	private static final String GENERATE_SUCCESS = "Entry successful, verify your data after feeder interval";
	private static final String GENERATE_ERROR = "Error inserting values";
	private static final String STATUS_MSG_KEY = "statusMessageKey";
	
	private static final String FAIL_FEEDER_VIEW = "insertToFailFeeder";
	
	private final InsertIntoCloudFailFeeder insertIntoCloudFailFeeder;
	
	@Autowired
	public InsertToFailFeederController(
			final InsertIntoCloudFailFeeder insertIntoCloudFailFeeder) {
		this.insertIntoCloudFailFeeder = insertIntoCloudFailFeeder;
	}
	
	@RequestMapping(method=RequestMethod.GET)
	public ModelAndView showForm() {
		return new ModelAndView(FAIL_FEEDER_VIEW);
	}
	
	@RequestMapping(method = RequestMethod.POST)
	public ModelAndView evictCache(@Valid @ModelAttribute("insertIntoCloudFailFeederBean") final InsertIntoCloudFailFeederBean cloudFeederBean, final BindingResult bindingResult) throws ServiceLayerException {

		ModelAndView modelAndView = new ModelAndView(FAIL_FEEDER_VIEW);
		
		try {
			if (!bindingResult.hasErrors()) {
				Map<String, String> request = new HashMap<String, String>();
				request.put("oupId", cloudFeederBean.getOupId());
				request.put("erightsId", cloudFeederBean.getErightsId());
				request.put("entity", cloudFeederBean.getEntity());
				request.put("searchDomainName", cloudFeederBean.getSearchDomainName());
				List<String> responseMessage = insertIntoCloudFailFeeder.insertToFailFeeder(request);
				if (responseMessage.size() > 0 && !responseMessage.get(0).equalsIgnoreCase("SUCCESS")) {
					Object[] errorMessagesArray = new String[responseMessage.size()];
					errorMessagesArray = responseMessage.toArray(errorMessagesArray);
					for(int i=0;i<responseMessage.size();i++){
						bindingResult.rejectValue(null, "", errorMessagesArray, responseMessage.get(i));
					}
				} else {
					modelAndView.addObject(STATUS_MSG_KEY, GENERATE_SUCCESS );
				}
			}
		} catch (Exception e) {
			LOGGER.error(ExceptionUtil.getStackTrace(e));
			throw new ServiceLayerException("Error while Cloudsearch Insertion.", e, new Message(
                    "", "There was a problem in api ")) ;
		}
		return modelAndView;

	}
	
	
	
	@ModelAttribute("insertIntoCloudFailFeederBean")
	public InsertIntoCloudFailFeederBean createModel() {
		
		InsertIntoCloudFailFeederBean bean = new InsertIntoCloudFailFeederBean();
		
		return bean;
	}
	
	

}
