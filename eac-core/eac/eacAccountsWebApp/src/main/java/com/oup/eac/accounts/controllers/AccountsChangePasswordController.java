package com.oup.eac.accounts.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.oup.eac.accounts.validators.AccountsChangePasswordFormValidator;
import com.oup.eac.common.utils.token.TokenConverter;
import com.oup.eac.domain.Customer;
import com.oup.eac.domain.Customer.CustomerType;
import com.oup.eac.dto.ChangePasswordDto;
import com.oup.eac.service.CustomerService;
import com.oup.eac.service.ServiceLayerException;
import com.oup.eac.service.exceptions.PasswordPolicyViolatedServiceLayerException;
import com.oup.eac.web.controllers.helpers.SessionHelper;
import com.oup.eac.web.controllers.registration.EACViews;

/**
 * Spring MVC Controller for Change password.  
 * @author Gaurav Soni
 */

@Controller
public class AccountsChangePasswordController {

    private static final Logger LOG = Logger.getLogger(AccountsChangePasswordController.class);
    private final String CHANGE_PASSWORD_VIEW = "changePasswordForm";
    private static final String CHANGE_PASSWORD_DENIED_JSP = "changePasswordDenied";
    private final CustomerService customerService;
    private final AccountsChangePasswordFormValidator changePasswordFormValidator;
    
    @Autowired
    public AccountsChangePasswordController(CustomerService customerService,
            AccountsChangePasswordFormValidator changePasswordFormValidator) {
        Assert.notNull(customerService);
        Assert.notNull(changePasswordFormValidator);
        this.customerService = customerService;
        this.changePasswordFormValidator = changePasswordFormValidator;
    }
    
    @ModelAttribute("changePasswordDto")
    public ChangePasswordDto createModel(){
        return new ChangePasswordDto();
    }
    
    @InitBinder
    public void initBinder(final WebDataBinder webDataBinder) {
        webDataBinder.setValidator(changePasswordFormValidator);
    }
    
    @RequestMapping(value = { "/changePassword" }, method=RequestMethod.GET)
    public ModelAndView showForm(final HttpServletRequest request){
        Customer customer = null;
        String userNameToken = request.getParameter("token");
        ModelAndView modelAndView = new ModelAndView(CHANGE_PASSWORD_VIEW);
        String userName = null ;
        if (userNameToken != null) {
	        try {
	        	ChangePasswordDto tokenDto = (ChangePasswordDto) TokenConverter.decrypt(userNameToken , new ChangePasswordDto());
				
				userName = tokenDto.getUsername();
				
				customer = customerService.getCustomerByUsername(userName) ;
				SessionHelper.setChangePwdToken(request.getSession(), userNameToken);
				//modelAndView.addObject("username", userName);
			} catch (Exception e) {
				LOG.debug(e.getMessage());
	            modelAndView = new ModelAndView(CHANGE_PASSWORD_DENIED_JSP);
			}
        } else {
        	customer = SessionHelper.getCustomer(request);
    		if(customer == null)
    		{
    			 return new ModelAndView(EACViews.PASSWORD_RESET_FAILURE_PAGE);
    		}
        }
        
        if(customer == null || customer.getCustomerType() == CustomerType.SHARED) {
            LOG.warn("Change Password prohibited, after error page is shown, will invite user to return to Login Page.");
            modelAndView = new ModelAndView(CHANGE_PASSWORD_DENIED_JSP);
        }
        
                
        return modelAndView;
    }
    
    @RequestMapping(value = { "/changePassword" }, method=RequestMethod.POST)
    public ModelAndView changePassword(@Valid @ModelAttribute("changePasswordDto") ChangePasswordDto changePasswordDto,
            final BindingResult bindingResult,
            final HttpServletRequest request,
            final HttpServletResponse response) {
        
    	ModelAndView modelAndView = new ModelAndView(CHANGE_PASSWORD_VIEW);
    	/*String userName = changePasswordDto.getUsername();
    	if (userName == null || userName.isEmpty()) {
    		userName = getUsername(request);
    		changePasswordDto.setUsername(userName);
    	}*/
    	String userName = getUsername(request);
    	if (userName == null || userName.isEmpty()) {
    		String changePwdToken = SessionHelper.getChangePwdToken(request.getSession()) ;
        	ChangePasswordDto changePasswordToken;
			try {
				changePasswordToken = (ChangePasswordDto) TokenConverter.decrypt(changePwdToken,new ChangePasswordDto());
				userName = changePasswordToken.getUsername();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        	
        	
    	}
    	changePasswordDto.setUsername(userName);
    	modelAndView.addObject("username", changePasswordDto.getUsername());
    	if (bindingResult.hasErrors()) {
            return modelAndView ;
        }
        
        try {
            customerService.saveChangeCustomerPassword(changePasswordDto, null);
        } catch (PasswordPolicyViolatedServiceLayerException e){
        	LOG.debug(e.getMessage());
        	bindingResult.reject("", e.getMessage());
            return modelAndView;
        } catch (ServiceLayerException e) {
        	LOG.debug(e.getMessage());
            AccountsBindingErrorController.convertMessages(e, bindingResult);
            return modelAndView;
        } catch(Exception e){
            LOG.error(e.getMessage());
            bindingResult.reject("error.unknown", "Unexpected error.");
            return modelAndView;
        }
        //remove change_customer paramet from  session.
        modelAndView.clear();
        SessionHelper.removeChangeCustomer(request);
        SessionHelper.removeChangePwdToken(request);
        return new ModelAndView("redirect:/activationCode.htm");
    }
    
    /**
     * Get username for change password.
     * Should be set up in session, unless session recreated by interceptor
     * @param request
     * @return
     */
    private final String getUsername(final HttpServletRequest request){
        String username = SessionHelper.getChangeCustomer(request);
        if (StringUtils.isBlank(username)) {
            Customer customer = SessionHelper.getCustomer(request);
            if (customer != null) {
                username = customer.getUsername();
            }
        }
        return username;
    }
}
