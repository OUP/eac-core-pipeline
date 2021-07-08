package com.oup.eac.accounts.controllers;

import java.util.Locale;

import javax.servlet.http.Cookie;
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

import com.oup.eac.accounts.validators.AccountsLoginFormValidator;
import com.oup.eac.domain.Customer;
import com.oup.eac.domain.UrlSkin;
import com.oup.eac.dto.CustomerSessionDto;
import com.oup.eac.dto.LoginDto;
import com.oup.eac.service.CustomerService;
import com.oup.eac.service.DomainSkinResolverService;
import com.oup.eac.service.ServiceLayerException;
import com.oup.eac.service.exceptions.CustomerNotFoundServiceLayerException;
import com.oup.eac.web.controllers.helpers.CookieHelper;
import com.oup.eac.web.controllers.helpers.SessionHelper;

/**
 * Spring MVC Controller for Login and SSO.  
 * @author Gaurav Soni
 */


@Controller
public class AccountsLoginController {

    private static final Logger LOG = Logger.getLogger(AccountsLoginController.class);
    private final String LOGIN_FORM_VIEW = "loginForm";
    private final String SKIN_URL = "SKIN_URL";
    private final CustomerService customerService;
    private final AccountsLoginFormValidator loginFormValidator;
    private final DomainSkinResolverService domainSkinResolverService;
    
    
    @Autowired
    public AccountsLoginController(final CustomerService customerService, final AccountsLoginFormValidator loginFormValidator, final DomainSkinResolverService domainSkinResolverService) {
        Assert.notNull(customerService);
        Assert.notNull(loginFormValidator);
        Assert.notNull(domainSkinResolverService);
        this.customerService = customerService;
        this.loginFormValidator = loginFormValidator;
        this.domainSkinResolverService = domainSkinResolverService;
    }
    
    @ModelAttribute("loginDto")
    public LoginDto createModel(){
        LoginDto lDto= new LoginDto();
        return lDto;
    }
    
    @InitBinder
    public void initBinder(final WebDataBinder webDataBinder) {
        webDataBinder.setValidator(loginFormValidator);
    }
    
    @RequestMapping(value = { "/login" }, method=RequestMethod.GET)
    public ModelAndView showForm(final HttpServletRequest request, final HttpServletResponse response) {
        
        cleanSession(request);
        resolveSkin(request);
        
        if(!isPrimaryDomainSessionAvailable(request)){
            LOG.debug("primary domain session is not available.");
            return new ModelAndView(LOGIN_FORM_VIEW);
        }
        if(!isPrimaryDomainSessionValid(request)){
            LOG.debug("primary domain session is invalid.");
          //Erights session is invalid so remove erights cookie.
            CookieHelper.invalidateErightsCookie(response);
            return new ModelAndView(LOGIN_FORM_VIEW);
        }
        return new ModelAndView("redirect:/activationCode.htm");
    }
    
    private void resolveSkin(final HttpServletRequest request){
        final String requestedSiteUrl = request.getParameter(SKIN_URL);
        if(!StringUtils.isBlank(requestedSiteUrl)){
            LOG.debug("Requested skin site url: "+requestedSiteUrl);
            UrlSkin skin = domainSkinResolverService.getSkinFromDomain(requestedSiteUrl);
            SessionHelper.setUrlSkin(request, skin);
        }
    }
    
    private boolean isPrimaryDomainSessionAvailable(final HttpServletRequest request)  {
        final String cookieValue = getCookieValue(request);
        if(StringUtils.isBlank(cookieValue)) {
            return false;
        }
        return true;
    }
    
    private boolean isPrimaryDomainSessionValid(final HttpServletRequest request){
        String cookieValue = getCookieValue(request);
        Customer customer = getCustomer(cookieValue);
        if(customer == null) {
            return false;
        }
        //Make customer available in the session
        SessionHelper.setCustomer(request, customer);

        //It is possible that the cookie value is correct but that we are in a new jsession. In this
        //instance we must make sure that we always set ERSESSION back in the current session
        //once we have determined that the cookie is valid.
        SessionHelper.setErightsSession(request.getSession(), cookieValue);

        return true;
    }
    
    private String getCookieValue(final HttpServletRequest request) {
        final Cookie cookie = CookieHelper.getErightsCookie(request);
        LOG.debug("Cookie is null: " + (cookie == null));
        final String cookieValue = cookie != null ? cookie.getValue() : null; 
        if (StringUtils.isBlank(cookieValue)) {
            return null;
        }
        LOG.debug("Cookie value: " + cookieValue);
        return cookieValue;       
    }
    
    private Customer getCustomer(final String cookieValue) {
        try {
            Customer customer = customerService.getCustomerFromSession(cookieValue);
            LOG.debug("Customer found for session key: " + cookieValue);
            return customer;
        } catch (CustomerNotFoundServiceLayerException e) {
            // No valid customer for session
            LOG.debug("Customer not found for session key: " + cookieValue);
            return null;
        }
    }
    
    private void cleanSession(final HttpServletRequest request) {
        final String requestedSiteUrl = request.getParameter(SKIN_URL);
        if(StringUtils.isBlank(requestedSiteUrl)){
            SessionHelper.setUrlSkin(request, null);
        }
        SessionHelper.setRegisterableProduct(request.getSession(), null);
        SessionHelper.removeErSession(request);
        SessionHelper.setChangeCustomer(request, null);
        SessionHelper.setCustomerAndWebUserName(request, null, null);
    }
    
    @RequestMapping(value = { "/login" }, method=RequestMethod.POST)
    public ModelAndView authenticate(@Valid @ModelAttribute("loginDto") final LoginDto loginDto, final BindingResult bindingResult, 
            final HttpServletRequest request, final HttpServletResponse response){
        
        if (bindingResult.hasErrors()) {
            return new ModelAndView(LOGIN_FORM_VIEW);
        }
        try {
            CustomerSessionDto customerSessionDto = customerService.getCustomerByUsernameAndPassword(loginDto.getUsername(), loginDto.getPassword());
            initErSession(request, response, customerSessionDto);
            Customer customer = customerSessionDto.getCustomer();
            Locale locale = customer.getLocale();
            
            //set locale according to customer's locale
            SessionHelper.setLocale(request, response, locale);
            SessionHelper.setCustomer(request, customer);
            
            //check if customer logged in after password reset.
            if (customer.isResetPassword()) {
                return new ModelAndView("redirect:/changePassword.htm");
            }
            
        } catch (ServiceLayerException e) {
            LOG.debug(e.getMessage());
            AccountsBindingErrorController.convertMessages(e, bindingResult);
            /*bindingResult.reject("error.servicelayer", e.getMessage());*/
            return new ModelAndView(LOGIN_FORM_VIEW);
        } catch(Exception e){
            LOG.error(e.getMessage());
            bindingResult.reject("error.unknown", "Unexpected error.");
            return new ModelAndView(LOGIN_FORM_VIEW);
        }
        return new ModelAndView("redirect:/activationCode.htm");
    }
    
    private void initErSession(final HttpServletRequest request, final HttpServletResponse response,  final CustomerSessionDto customerSessionDto) {
        String erSession = customerSessionDto.getSession();
        SessionHelper.setErightsSession(request.getSession(), erSession);
        CookieHelper.setErightsCookie(response, erSession);
        LOG.debug("Customer session key: " + erSession);
    }
    
    /*private class RequestContext{
        private String cookieValue;
        private Customer customer;
        
        public String getCookieValue() {
            return cookieValue;
        }
        public void setCookieValue(String cookieValue) {
            this.cookieValue = cookieValue;
        }
        @SuppressWarnings("unused")
        public Customer getCustomer() {
            return customer;
        }
        public void setCustomer(Customer customer) {
            this.customer = customer;
        }
    }*/
}
