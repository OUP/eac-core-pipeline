package com.oup.eac.web.controllers.thirdpartyui;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.oup.eac.common.utils.url.InvalidURLException;
import com.oup.eac.common.utils.url.URLUtils;
import com.oup.eac.domain.Customer;
import com.oup.eac.web.controllers.helpers.CookieHelper;
import com.oup.eac.web.controllers.helpers.RequestHelper;

/**
 * The Class LoginWidgetController.
 * 
 * @author David Hay
 * 
 */

public abstract class BaseLoginWidgetController {

    private static final Logger LOG = Logger.getLogger(LoginWidgetController.class);

    public static final String ATTR_URL_PREFIX = "urlPrefix";
    public static final String ATTR_SUCCESS_URL = "successUrl";
    public static final String ATTR_ERROR_URL = "errorUrl";
    public static final String ATTR_SESSION_KEY = "erightsSessionKey";
    public static final String ATTR_USER_NAME = "userName";
    public static final String ATTR_FIRST_NAME = "firstName";
    public static final String ATTR_LAST_NAME = "lastName";
    public static final String ATTR_LOGOUT_URL = "logoutUrl";
    public static final String ATTR_ERROR_MSG = "errorMsg";

    public static final String ATTR_USER_ID = "userId";
    public static final String ATTR_USER_TYPE = "userType";
    public static final String VIEW_WELCOME = "js/loginWidgetWelcome";
    public static final String VIEW_LOGIN_FORM = "js/loginWidgetLoginForm";
    public static final String VIEW_ERROR_FORM = "js/loginWidgetErrorForm";

    /**
     * Gets the login or welcome javascript.
     *
     * @param successUrl the success url
     * @param errorUrl the error url
     * @param logoutUrl the logout url
     * @param request the request
     * @param response the response
     * @return the login or welcome javascript
     */
    @RequestMapping(method = RequestMethod.GET, value = "/loginWidget.js")
    public final ModelAndView getLoginOrWelcomeJavascript(
            @RequestParam(value = "success_url", required = false) final String successUrl,
            @RequestParam(value = "error_url", required = false) final String errorUrl,
            @RequestParam(value = "logout_url", required = false) final String logoutUrl,
            final HttpServletRequest request,
            final HttpServletResponse response) {
    	
        ModelAndView result;
        
        try {
        	validateUrls(successUrl, errorUrl, logoutUrl);
        } catch (InvalidURLException e) {
            if (LOG.isDebugEnabled()) {
                LOG.debug(e.getMessage(), e);
            }
    		result = getJavascriptForErrorPage(request, response, successUrl, errorUrl, logoutUrl, e.getMessage());
    		return result;
		}

        Cookie eacCookie = CookieHelper.getErightsCookie(request);

        String erightsSessionKey = eacCookie == null ? null : eacCookie.getValue();
        
        Customer customer = getCustomer(erightsSessionKey, request);
        
        if (customer == null) {
            result = getJavascriptForLoginPage(request, response, successUrl, errorUrl);
            if (LOG.isDebugEnabled()) {
                LOG.debug("Serving up Javascript for Login Page");
            }
        } else {
            result = getJavascriptForWelcomePage(customer, erightsSessionKey, request, response, logoutUrl);
            if (LOG.isDebugEnabled()) {
                String msg = String.format("Serving up Javascript for Welcome Page for %s", customer.getUsername());
                LOG.debug(msg);
            }
        }
        return result;
    }
    
    private void validateUrls(final String successUrl, final String errorUrl, final String logoutUrl) throws InvalidURLException {
    	URLUtils.validateUrl(successUrl);
    	URLUtils.validateUrl(errorUrl);
    	URLUtils.validateUrl(logoutUrl);
    }
    
    /**
     * Gets the javascript for welcome page.
     *
     * @param user the user
     * @param erightsSessionKey the session key
     * @param request the request
     * @param response the response
     * @param logoutUrl the logout url
     * @return the javascript for welcome page
     */
    private ModelAndView getJavascriptForWelcomePage(final Customer user, final String erightsSessionKey,
            final HttpServletRequest request, final HttpServletResponse response, final String logoutUrl) {
        String urlPrefix = RequestHelper.getUrlPrefix(request);
        request.setAttribute(ATTR_USER_NAME, user.getUsername());
        request.setAttribute(ATTR_FIRST_NAME, user.getFirstName());
        request.setAttribute(ATTR_LAST_NAME, user.getFamilyName());
        request.setAttribute(ATTR_SESSION_KEY, erightsSessionKey);
        request.setAttribute(ATTR_USER_ID, user.getId());
        request.setAttribute(ATTR_URL_PREFIX, urlPrefix);
        request.setAttribute(ATTR_LOGOUT_URL, logoutUrl);
        request.setAttribute(ATTR_USER_TYPE, user.getCustomerType().name());
        ModelAndView result = new ModelAndView(VIEW_WELCOME);
        return result;
    }

    /**
     * Gets the JavaScript for login page.
     *
     * @param request the request
     * @param response the response
     * @param successUrl the success url
     * @param errorUrl the error url
     * @return the javascript for login page
     */
    private ModelAndView getJavascriptForLoginPage(
            final HttpServletRequest request, final HttpServletResponse response, final String successUrl, final String errorUrl) {
        String urlPrefix = RequestHelper.getUrlPrefix(request);
        request.setAttribute(ATTR_URL_PREFIX, urlPrefix);
        request.setAttribute(ATTR_SUCCESS_URL, successUrl);
        request.setAttribute(ATTR_ERROR_URL, errorUrl);
        ModelAndView result = new ModelAndView(VIEW_LOGIN_FORM);
        return result;
    }
    
    /**
     * Gets the JavaScript for login page.
     *
     * @param request the request
     * @param response the response
     * @param successUrl the success url
     * @param errorUrl the error url
     * @return the javascript for login page
     */
    private ModelAndView getJavascriptForErrorPage(
            final HttpServletRequest request, final HttpServletResponse response, final String successUrl, final String errorUrl, final String logoutUrl, final String errorMsg) {
        String urlPrefix = RequestHelper.getUrlPrefix(request);
        request.setAttribute(ATTR_URL_PREFIX, urlPrefix);
        request.setAttribute(ATTR_SUCCESS_URL, successUrl);
        request.setAttribute(ATTR_ERROR_URL, errorUrl);
        request.setAttribute(ATTR_LOGOUT_URL, errorUrl);
        request.setAttribute(ATTR_ERROR_MSG, errorMsg);
        ModelAndView result = new ModelAndView(VIEW_ERROR_FORM);
        return result;
    }

    /**
     * Gets the customer.
     *
     * @param erightsSessionKey the erights session key
     * @param request the request
     * @return the customer
     */
    public abstract Customer getCustomer(final String erightsSessionKey, HttpServletRequest request);

}
