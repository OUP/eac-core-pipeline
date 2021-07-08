package com.oup.eac.accounts.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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

import com.oup.eac.accounts.validators.AccountsRegistrationFormValidator;
import com.oup.eac.dto.AccountRegistrationDto;
import com.oup.eac.dto.CustomerSessionDto;
import com.oup.eac.service.CustomerService;
import com.oup.eac.service.ServiceLayerException;
import com.oup.eac.web.controllers.helpers.CookieHelper;
import com.oup.eac.web.controllers.helpers.SessionHelper;

/**
 * Spring MVC Controller for Account Registration.  
 * @author Gaurav Soni
 */

@Controller
public class AccountsRegistrationController {

    private static final Logger LOG = Logger.getLogger(AccountsRegistrationController.class);
    private final String ACCOUNT_REGISTRATION_FORM_VIEW = "accountsRegistrationForm";
    private final CustomerService customerService;
    private final AccountsRegistrationFormValidator accountsRegistrationFormValidator;
    
    @Autowired
    public AccountsRegistrationController(CustomerService customerService, AccountsRegistrationFormValidator accountsRegistrationFormValidator) {
        this.customerService = customerService;
        this.accountsRegistrationFormValidator = accountsRegistrationFormValidator;
    }

    @ModelAttribute("accountRegistrationDto")
    public AccountRegistrationDto createModel() {
        AccountRegistrationDto accRegDto = new AccountRegistrationDto();
        return accRegDto;
    }

    @RequestMapping(value = { "/registration" }, method = RequestMethod.GET)
    public ModelAndView showForm() {
        return new ModelAndView(ACCOUNT_REGISTRATION_FORM_VIEW);
    }
    
    @InitBinder
    public void initBinder(final WebDataBinder webDataBinder) {
        webDataBinder.setValidator(accountsRegistrationFormValidator);
    }
    
    @RequestMapping(value = { "/registration" }, method = RequestMethod.POST)
    public ModelAndView createRegistration(@Valid @ModelAttribute("accountRegistrationDto") final AccountRegistrationDto accountRegistrationDto, final BindingResult bindingResult,
            final HttpServletRequest request,
            final HttpServletResponse response) {
        if (bindingResult.hasErrors()) {
            return new ModelAndView(ACCOUNT_REGISTRATION_FORM_VIEW);
        }
        try {
            accountRegistrationDto.setUserLocale(SessionHelper.getLocale(request));
            CustomerSessionDto customerSessionDto = customerService.saveCustomerRegistration(accountRegistrationDto);
            LOG.debug("Customer session key: " + customerSessionDto.getSession());
            initErSession(request, response, customerSessionDto);
        } catch (ServiceLayerException e) {
            LOG.debug(e.getMessage());
            AccountsBindingErrorController.convertMessages(e, bindingResult);
            return new ModelAndView(ACCOUNT_REGISTRATION_FORM_VIEW);
        } catch(Exception e){
            LOG.error(e.getMessage());
            bindingResult.reject("error.unknown", "Unexpected error.");
            return new ModelAndView(ACCOUNT_REGISTRATION_FORM_VIEW);
        }
        return new ModelAndView("redirect:/activationCode.htm");
    }
    
    private void initErSession(final HttpServletRequest request,
            final HttpServletResponse response,
            final CustomerSessionDto customerSessionDto) {
        String erSession = customerSessionDto.getSession();
        SessionHelper.setCustomer(request, customerSessionDto.getCustomer());
        SessionHelper.setErightsSession(request.getSession(), erSession);
        CookieHelper.setErightsCookie(response, erSession);
        LOG.debug("Customer session key: " + erSession);  
    }
}
