package com.oup.eac.web.interceptors;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.oup.eac.domain.Customer;
import com.oup.eac.domain.Customer.CustomerType;
import com.oup.eac.web.controllers.helpers.SessionHelper;

public class SharedUsersCannotAmendRegistrationsInterceptor extends HandlerInterceptorAdapter {

    private static final Logger LOG = Logger.getLogger(SharedUsersCannotAmendRegistrationsInterceptor.class);

    protected static final String AMEND_REGISTRATION_DENIED_ERROR_JSP = "/WEB-INF/jsp/amendRegistrationDenied.jsp";

    private static final String NEXT_URL_ATTR = "nextURL";
    private static final String NEXT_URL = "profile.htm";

    /**
     * {@inheritDoc}
     */
    @Override
    public final boolean preHandle(final HttpServletRequest request, final HttpServletResponse response, final Object handler) throws Exception {
    	Customer customer = SessionHelper.getCustomer(request);
    	if (customer != null && customer.getCustomerType() == CustomerType.SHARED) {
    		LOG.warn("Amending Registrations is denied for shared user [" + customer.getUsername() +"] - after error will invite user to go to  : [" + NEXT_URL + "]");
            request.setAttribute(NEXT_URL_ATTR, NEXT_URL);
            RequestDispatcher dispatcher = request.getRequestDispatcher(AMEND_REGISTRATION_DENIED_ERROR_JSP);
            dispatcher.forward(request, response);
            return false;
    	}else{
    		return true;
    	}
    }

}
