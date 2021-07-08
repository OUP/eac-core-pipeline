package com.oup.eac.web.interceptors;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.oup.eac.domain.Customer;
import com.oup.eac.service.CustomerService;
import com.oup.eac.service.exceptions.CustomerNotFoundServiceLayerException;
import com.oup.eac.web.controllers.helpers.CookieHelper;
import com.oup.eac.web.controllers.helpers.SessionHelper;
import com.oup.eac.web.controllers.registration.EACViews;

public class LoggedInCustomerInterceptor extends HandlerInterceptorAdapter {

    private static final String SLASH = "/";

    private static final Logger LOG = Logger.getLogger(LoggedInCustomerInterceptor.class);

    private final CustomerService customerService;

    /**
     * Instantiates a new basic profile customer interceptor.
     * 
     * @param customerServiceP
     *            the customer service
     */
    public LoggedInCustomerInterceptor(final CustomerService customerServiceP) {
        this.customerService = customerServiceP;
    }

    /**
     * Checks that a customer can be found in session or from EAC cookie
     * otherwise redirects to login screen.
     * 
     * @param request
     *            the request
     * @param response
     *            the response
     * @param handler
     *            the handler
     * @return true, if successful
     * @throws Exception
     *             the exception
     */
    @Override
    public final boolean preHandle(final HttpServletRequest request, final HttpServletResponse response, final Object handler) throws Exception {
        Customer customer = getCustomer(request, response);

        if (customer == null) {
            String forwardURL = request.getServletPath();
            if (forwardURL.startsWith(SLASH)) {
                forwardURL = forwardURL.substring(1);
            }
            SessionHelper.setForwardUrl(request.getSession(), forwardURL);
            String redirect = createRedirectUrl(request);
            if (LOG.isDebugEnabled()) {
                String msg = String.format("No Customer found : redirecting to Login [%s], forwardUrl [%s]", redirect, forwardURL);
                LOG.debug(msg);
            }
            response.sendRedirect(redirect);
            return false;
        }
        return true;
    }

    /**
     * Gets the customer.
     *
     * @param request the request
     * @param response the response
     * @return the customer
     */
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
        String resource = EACViews.LOGIN_VIEW;
        String result = String.format("%s/%s", request.getContextPath(), resource);
        return result;
    }

}
