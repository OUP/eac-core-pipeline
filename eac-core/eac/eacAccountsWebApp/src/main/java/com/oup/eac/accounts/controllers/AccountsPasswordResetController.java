package com.oup.eac.accounts.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

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

import com.oup.eac.accounts.validators.AccountsResetPasswordFormValidator;
import com.oup.eac.common.utils.EACSettings;
import com.oup.eac.common.utils.email.InternationalEmailAddress;
import com.oup.eac.domain.Customer;
import com.oup.eac.domain.Customer.CustomerType;
import com.oup.eac.dto.PasswordResetDto;
import com.oup.eac.integration.facade.exceptions.UserNotFoundException;
import com.oup.eac.service.CustomerService;
import com.oup.eac.service.ServiceLayerException;
import com.oup.eac.service.exceptions.PasswordPolicyViolatedServiceLayerException;
import com.oup.eac.web.controllers.helpers.SessionHelper;

/**
 * Spring MVC Controller for reset password.  
 * @author Gaurav Soni
 */

@Controller
public class AccountsPasswordResetController {

    private static final Logger LOG = Logger.getLogger(AccountsPasswordResetController.class);
    private final String PASSWORD_RESET_FORM = "passwordResetForm";
    private final String PASSWORD_RESET_SUCCESS = "passwordResetSuccess";
    private final CustomerService customerService;
    private final AccountsResetPasswordFormValidator resetPasswordFormValidator;
    
    @Autowired
    public AccountsPasswordResetController(CustomerService customerService,
            AccountsResetPasswordFormValidator resetPasswordFormValidator) {
        Assert.notNull(customerService);
        Assert.notNull(resetPasswordFormValidator);
        this.customerService = customerService;
        this.resetPasswordFormValidator = resetPasswordFormValidator;
    }

    @ModelAttribute("passwordResetDto")
    public PasswordResetDto createModel(){
        return new PasswordResetDto();
    }
    
    @InitBinder
    public void initBinder(final WebDataBinder webDataBinder) {
        webDataBinder.setValidator(resetPasswordFormValidator);
    }
    
    @RequestMapping(value = { "/passwordReset" }, method=RequestMethod.GET)
    public ModelAndView showForm() {
        return new ModelAndView(PASSWORD_RESET_FORM);
    }

    @RequestMapping(value = { "/passwordReset" }, method=RequestMethod.POST)
    public ModelAndView resetPassword(@Valid @ModelAttribute("passwordResetDto") PasswordResetDto passwordResetDto, final BindingResult bindingResult,
            final HttpServletRequest request, final HttpServletResponse response) {
        
        if (bindingResult.hasErrors()) {
            return new ModelAndView(PASSWORD_RESET_FORM);
        }
        final String requestUrl=EACSettings.getProperty(EACSettings.EAC_HOST_URL) + "/eacAccounts/" ;
        try{
            final String username = passwordResetDto.getUsername();
            final Customer user = customerService.getCustomerByUsername(username);
            if (user == null) {
                if (InternationalEmailAddress.isValid(username)) {
                    customerService.notifyCustomerAccountNotFound(username, SessionHelper.getLocale(request));
                }
                return new ModelAndView(PASSWORD_RESET_SUCCESS);
            }
            if (user.getCustomerType() == CustomerType.SHARED) {
                customerService.passwordResetAttemptDenied(user, SessionHelper.getLocale(request));
                return new ModelAndView(PASSWORD_RESET_SUCCESS);
            }
            customerService.updateResetCustomerPassword(user, SessionHelper.getLocale(request), requestUrl, null);
        } catch(PasswordPolicyViolatedServiceLayerException e){
            LOG.debug(e.getMessage());
            AccountsBindingErrorController.convertMessages(e, bindingResult);
            return new ModelAndView(PASSWORD_RESET_FORM);
        } catch (UserNotFoundException e){
        	ServiceLayerException ser = new ServiceLayerException(e.getMessage());
        	AccountsBindingErrorController.convertMessages(ser, bindingResult);
             }
        catch(ServiceLayerException e){
            LOG.debug(e.getMessage());
            AccountsBindingErrorController.convertMessages(e, bindingResult);
            return new ModelAndView(PASSWORD_RESET_FORM);
        } catch(Exception e){
            LOG.error(e.getMessage());
            bindingResult.reject("error.unknown", "Unexpected error.");
            return new ModelAndView(PASSWORD_RESET_FORM);
        }
        return new ModelAndView(PASSWORD_RESET_SUCCESS);
    }
}