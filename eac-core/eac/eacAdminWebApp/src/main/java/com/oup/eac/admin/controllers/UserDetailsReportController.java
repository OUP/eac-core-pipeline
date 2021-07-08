package com.oup.eac.admin.controllers;

import java.util.List;

import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.oup.eac.admin.beans.UserDetailsBean;
import com.oup.eac.admin.validators.UserDetailsBeanValidator;
import com.oup.eac.common.utils.exception.ExceptionUtil;
import com.oup.eac.domain.AdminUser;
import com.oup.eac.domain.UserDetailsReport;
import com.oup.eac.dto.CustomerSearchCriteria;
import com.oup.eac.dto.Message;
import com.oup.eac.service.ServiceLayerException;
import com.oup.eac.service.UserDetailsReportService;

@Controller
@RequestMapping("/mvc/reports/userDetails.htm")
public class UserDetailsReportController {
	
	private static final Logger LOGGER = Logger.getLogger(UserDetailsReportController.class);
	private static final String GENERATE_SUCCESS = "status.userdetails.submit.success";
	private static final String USERNAME_INVALID = "Invalid username.";
	private static final String STATUS_MSG_KEY = "statusMessageKey";
	
	private static final String USER_DETAILS_REPORT_VIEW = "userDetailsReport";
	
	private final UserDetailsReportService userDetailsReportService;
	private final UserDetailsBeanValidator validator;
	
	@Autowired
	public UserDetailsReportController(
			final UserDetailsReportService userDetailsReportService,
			final UserDetailsBeanValidator validator) {
		this.userDetailsReportService = userDetailsReportService;
		this.validator = validator;
	}
	
	@RequestMapping(method=RequestMethod.GET)
	public ModelAndView showForm() {
		return new ModelAndView(USER_DETAILS_REPORT_VIEW);
	}
	
	@RequestMapping(method = RequestMethod.POST)
	public ModelAndView generateUserDetailsReport(@Valid @ModelAttribute("userDetailsBean") final UserDetailsBean userDetailsBean, final BindingResult bindingResult) throws ServiceLayerException {

		ModelAndView modelAndView = new ModelAndView(USER_DETAILS_REPORT_VIEW);
		
		try {
			if (!bindingResult.hasErrors()) {
				CustomerSearchCriteria customerSearchCriteria = new CustomerSearchCriteria() ;
				customerSearchCriteria.setUsername(userDetailsBean.getSearchUserName()) ;
				String userId = userDetailsReportService.getUserIdFromUsername(customerSearchCriteria) ;
				if (userId == null || userId.isEmpty() ) {
					LOGGER.info("User is not found with username : " + userDetailsBean.getSearchUserName());
					Object[] errorMessagesArray = new String[1];
					errorMessagesArray[0] = USERNAME_INVALID ;
					bindingResult.rejectValue(null, "", errorMessagesArray, USERNAME_INVALID);
					return modelAndView ;
				}
				AdminUser adminUser = (AdminUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
				UserDetailsReport userDetailsReport = new UserDetailsReport() ;
				userDetailsReport.setAdminEmailAddress(adminUser.getEmailAddress());
				userDetailsReport.setUserId(userId);
				List<String> responseMessage = userDetailsReportService.generateUserDetailsReport(userDetailsReport) ;
				
				if (responseMessage.size() > 0 && !responseMessage.get(0).equalsIgnoreCase("SCHEDULED")) {
					Object[] errorMessagesArray = new String[responseMessage.size()];
					errorMessagesArray = responseMessage.toArray(errorMessagesArray);
					for(int i=0;i<responseMessage.size();i++){
						bindingResult.rejectValue(null, "", errorMessagesArray, responseMessage.get(i));
					}
				} else {
					modelAndView.addObject(STATUS_MSG_KEY, GENERATE_SUCCESS );
					//modelAndView.addObject("adminEmail", adminUser.getEmailAddress()) ;
				}
				
			}
		} catch (Exception e) {
			LOGGER.error(ExceptionUtil.getStackTrace(e));
			throw new ServiceLayerException("UserDetailsReport error.", e, new Message(
                    "", "There was a problem to generate user details report: " + userDetailsBean.getSearchUserName())) ;
		}
		return modelAndView;

	}
	
	
	
	@ModelAttribute("userDetailsBean")
	public UserDetailsBean createModel() {
		
		UserDetailsBean userDetailsBean = new UserDetailsBean();
		
		return userDetailsBean;
	}
	
	@InitBinder
	public void initBinder(final WebDataBinder binder) {
		binder.setValidator(validator);
	}
	
	
}
