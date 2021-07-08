package com.oup.eac.accounts.interceptors;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.oup.eac.domain.Customer;
import com.oup.eac.service.CustomerService;
import com.oup.eac.service.exceptions.CustomerNotFoundServiceLayerException;
import com.oup.eac.web.controllers.helpers.CookieHelper;
import com.oup.eac.web.controllers.helpers.SessionHelper;

/**
 * Spring interceptor to whether customer is logged in or not.  
 * @author Gaurav Soni
 */

@Component
public class AccountsLoggedInCustomerInterceptors extends HandlerInterceptorAdapter {
    
    private final String LOGIN_VIEW="login.htm";
    private static final Logger LOG = Logger.getLogger(AccountsLoggedInCustomerInterceptors.class);
    private final CustomerService customerService;
    
    @Autowired
    public AccountsLoggedInCustomerInterceptors(CustomerService customerService) {
        this.customerService = customerService;
    }

    @Override
    public final boolean preHandle(final HttpServletRequest request, final HttpServletResponse response, final Object handler) throws Exception{
        
        Customer customer = getCustomer(request, response);
        if(customer == null){
            response.sendRedirect(createRedirectUrl(request));
            return false;
        }
        return true;
    }

    private Customer getCustomer(final HttpServletRequest request, final HttpServletResponse response) {

        Customer customer = SessionHelper.getCustomer(request);
        if (customer == null) {
            Cookie cookie = CookieHelper.getErightsCookie(request);
            if (cookie != null) {
                String sessionKey = cookie.getValue();
                try {
                    customer = customerService.getCustomerFromSession(sessionKey);
                    SessionHelper.setCustomer(request, customer);
                    if (customer != null) {
                        SessionHelper.setLocale(request, response, customer.getLocale());
                        if (customer.isResetPassword()) {
                            SessionHelper.setChangeCustomer(request, customer.getUsername());
                        }
                    } else {
                        SessionHelper.setLocale(request, response, null);
                    }
                } catch (CustomerNotFoundServiceLayerException e) {
                    LOG.debug("No Customer Available from EAC Cookie", e);
                }
            } else {
                LOG.debug("Cannot get Customer : No EAC Cookie");
            }
        }
        return customer;
    }
    
    /**
     * Creates the redirect url.
     *
     * @param request the request
     * @return the string
     */
    private String createRedirectUrl(final HttpServletRequest request) {
        String resource = LOGIN_VIEW;
        String result = String.format("%s/%s", request.getContextPath(), resource);
        return result;
    }

}
