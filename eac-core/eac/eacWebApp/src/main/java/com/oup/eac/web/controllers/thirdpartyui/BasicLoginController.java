package com.oup.eac.web.controllers.thirdpartyui;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Locale;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.oup.eac.domain.Customer;
import com.oup.eac.dto.CustomerSessionDto;
import com.oup.eac.integration.facade.exceptions.ErightsException;
import com.oup.eac.integration.facade.exceptions.UserNotFoundException;
import com.oup.eac.service.CustomerService;
import com.oup.eac.service.InvalidCredentialsServiceLayerException;
import com.oup.eac.service.ServiceLayerException;
import com.oup.eac.service.exceptions.AccountLockedServiceLayerException;
import com.oup.eac.service.exceptions.CustomerNotFoundServiceLayerException;
import com.oup.eac.web.controllers.helpers.CookieHelper;
import com.oup.eac.web.controllers.helpers.SessionHelper;
import com.oup.eac.web.controllers.registration.EACViews;

/**
 * The Class BasicLoginController.
 * 
 * @author David Hay
 * 
 */
@Controller("basicLoginController")
public class BasicLoginController {

    private static final Logger LOG = Logger.getLogger(BasicLoginController.class);

    // The MSG_* constants are for debug messages only - we don't want to tell
    // the client exactly why the username/password is invalid
    private static final String MSG_INVALID_SUCCESS_URL = "invalid success url";
    private static final String MSG_BLANK_PASSWORD = "blank password";
    private static final String MSG_BLANK_USER_NAME = "blank user name";
    private static final String MSG_WRONG_PASSSWORD = "wrong passsword";
    private static final String MSG_ACCOUNT_LOCKED = "account locked";
    private static final String MSG_UNKNOWN = "unknown";
    private static final String MSG_USERNAME_DOES_NOT_EXIST = "username does not exist";

    private static final String MSG_ERIGHTS_AUTHENTICATION_FAILURE = "eRights Authentication failure";
    private static final String FMT_FAILED = "Basic Login Failed for username[%s] : reason [%s]";
    private static final String FMT_SUCCESS = "Basic Login Succeeed for username[%s] : sessionToken[%s] freshCookie?[%b]";

    public static final String REDIRECT = "redirect:";
    public static final String BASIC_LOGIN_ERROR_PAGE = "basicLoginError";
    public static final String ERROR_MSG_CODE = "errorCode";

    public static final String ERR_CODE_USER_NAME_BLANK = "basic.login.error.user.name.blank";
    public static final String ERR_CODE_PASSWORD_BLANK = "basic.login.error.password.blank";
    public static final String ERR_CODE_SUCCESS_URL_INVALID = "basic.login.error.success.url.invalid";
    public static final String ERR_CODE_LOGIN_FAILED = "basic.login.error.login.failed";

    private final CustomerService customerService;

    /**
     * Instantiates a new basic login controller.
     * 
     * @param customerServiceP
     *            the customer service p
     */
    @Autowired
    public BasicLoginController(final CustomerService customerServiceP) {
        this.customerService = customerServiceP;
    }

    /**
     * Basic login.
     * 
     * @param userName
     *            the user name
     * @param password
     *            the password
     * @param errorUrl
     *            the error url
     * @param successUrl
     *            the success url
     * @param request
     *            the request
     * @param response
     *            the response
     * @return the model and view
     */
    @RequestMapping(method = RequestMethod.POST, value = "/basicLogin.htm")
    public final ModelAndView basicLogin(
            @RequestParam("username") final String userName,
            @RequestParam("password") final String password,
            @RequestParam("error_url") final String errorUrl,
            @RequestParam("success_url") final String successUrl,
            final HttpServletRequest request,
            final HttpServletResponse response) {
        ModelAndView result;
        Context context = new Context(userName, request, response);
        context.performBasicLogin(password, errorUrl, successUrl);
        if (context.isError()) {
            result = getErrorResult(errorUrl, context, request);
        } else {
            // set up EAC cookie in the response
            String sessionToken = context.getSessionToken();
            CookieHelper.setErightsCookie(response, sessionToken);
            
            if (context.isResetPassword()) {
            	result = new ModelAndView(new RedirectView(EACViews.CHANGE_PASSWORD_VIEW));
            } else {
	            result = getSuccessResult(successUrl, sessionToken);
	            if (LOG.isDebugEnabled()) {
	                String msg = String.format(FMT_SUCCESS, userName, sessionToken, context.isFreshCookie());
	                LOG.debug(msg);
	            }
            }
        }
        return result;
    }






    /**
     * Terminates the eRightsSession of the existingEacCookie if it's for a
     * different customer than the authenticated customer.
     * 
     * @param authenticatedCustomer
     *            the authenticated customer
     * @param existingEacCookie
     *            the existing eac cookie
     * @return the sessionToken of the existingEacCookie if it is associated
     *         with the authenticatedCustomer
     */
    private String processExistingCookie(final Customer authenticatedCustomer, final Cookie existingEacCookie) {
        final String oldSessionToken = existingEacCookie.getValue();
        String sessionToken = null;
        try {
            final Customer cookieCustomer = customerService.getCustomerFromSession(oldSessionToken);
            if (cookieCustomer != null) {
                if (cookieCustomer.getId().equals(authenticatedCustomer.getId())) {
                    sessionToken = existingEacCookie.getValue();
                } else {
                    try {
                        customerService.logout(oldSessionToken);
                    } catch (ServiceLayerException e) {
                        LOG.trace("Problem logging out session associated with existing EAC Cookie", e);
                    }
                }
            }
        } catch (CustomerNotFoundServiceLayerException sle) {
            sessionToken = null;
        }
        return sessionToken;
    }



    /**
     * Gets the success result.
     *
     * @param successUrl the success url
     * @param sessionToken the session token
     * @return the success result
     */
    private ModelAndView getSuccessResult(final String successUrl, final String sessionToken) {
        // result enhancedSuccessUrl = <successUrl>?ERSESSION=<sessionToken>
        String enhancedSuccessUrl = SessionHelper.appendErSessionToUrl(successUrl, sessionToken);
        String successView = getRedirectURL(enhancedSuccessUrl);
        ModelAndView result = new ModelAndView(successView);
        return result;
    }

    /**
     * Gets the redirect url.
     *
     * @param baseUrl the base url
     * @return the redirect url
     */
    private String getRedirectURL(final String baseUrl) {
        String redirectUrlView = String.format("%s%s", REDIRECT, baseUrl);
        return redirectUrlView;
    }

    /**
     * Gets the error result.
     *
     * @param errorUrl the error url
     * @param context the context
     * @param request the request
     * @return the error result
     */
    private ModelAndView getErrorResult(final String errorUrl, final Context context, final HttpServletRequest request) {
        ModelAndView result;
        if (context.isErrorUrlValid()) {
            String redirectUrlView = getRedirectURL(errorUrl);
            result = new ModelAndView(redirectUrlView);
        } else {
            String errorCode = context.getErrorCode();
            if (errorCode != null) {
                request.setAttribute(ERROR_MSG_CODE, errorCode);
            }
            result = new ModelAndView(BASIC_LOGIN_ERROR_PAGE);
        }
        return result;
    }

    private class Context {
        private boolean freshCookie = false;
        private boolean error = false;
        private String errorCode;
        private boolean errorUrlValid;
        private String sessionToken;
        private boolean resetPassword;

        private final String userName;
        private final HttpServletRequest request;
        private final HttpServletResponse response;

        /**
         * Instantiates a new context.
         *
         * @param userNameP the user name
         * @param requestP the request
         * @param responseP the response
         */
        public Context(final String userNameP, final HttpServletRequest requestP, final HttpServletResponse responseP) {
            this.userName = userNameP;
            this.request = requestP;
            this.response = responseP;
        }

        /**
         * Checks if is error url valid.
         *
         * @return true, if is error url valid
         */
        public boolean isErrorUrlValid() {
            return errorUrlValid;
        }


        /**
         * Checks if is error.
         *
         * @return true, if is error
         */
        public boolean isError() {
            return error;
        }
        
		/**
		 * Checks if the authenticated user needs to reset their password.
		 * 
		 * @return true if the password must be reset. False otherwise.
		 */
        public boolean isResetPassword() {
        	return resetPassword;
        }

        /**
         * Gets the error code.
         *
         * @return the error code
         */
        public String getErrorCode() {
            return errorCode;
        }

        /**
         * Gets the session token.
         *
         * @return the session token
         */
        public String getSessionToken() {
            return sessionToken;
        }

        /**
         * Checks if is fresh cookie.
         *
         * @return true, if is fresh cookie
         */
        public boolean isFreshCookie() {
            return freshCookie;
        }

        /**
         * Sets the error url valid.
         *
         * @param errorUrlValidP the new error url valid
         */
        private void setErrorUrlValid(final boolean errorUrlValidP) {
            this.errorUrlValid = errorUrlValidP;
        }

        /**
         * Sets the error code.
         *
         * @param errorCodeP the new error code
         */
        private void setErrorCode(final String errorCodeP) {
            this.errorCode = errorCodeP;
            error = true;
        }

        /**
         * Perform basic login.
         *
         * @param password the password
         * @param errorUrl the error url
         * @param successUrl the success url
         */
        public void performBasicLogin(final String password, final String errorUrl, final String successUrl) {
            validateParameters(password, errorUrl, successUrl);
            if (isError()) {
                return;
            }
            Customer customer = getCustomerByUserName();
            if (isError()) {
                return;
            }
            
            if (customer.isResetPassword()) {
            	resetPassword = true;
            	SessionHelper.setChangeCustomer(request, customer.getUsername());
            }
            
            Cookie existingEacCookie = CookieHelper.getErightsCookie(this.request);
            String sessionTokenValue = null;

            boolean freshCookieValue = false;
            if (existingEacCookie != null) {
                sessionTokenValue = processExistingCookie(customer, existingEacCookie);
            }

            if (sessionTokenValue == null) {

                // authenticate
                sessionTokenValue = authenticate(password);
                if (isError()) {
                    return;
                }
                freshCookieValue = true;
            }

            // at this point we should have the session token to allow us to create
            // (or refresh ) the EacCookie
            setSessionToken(sessionTokenValue, freshCookieValue);
        }
        
        /**
         * Authenticate.
         *
         * @param password the password
         * @return the string
         */
        private String authenticate(final String password) {
            String result = null;
            try {
                boolean checkEacDb = false;
                boolean checkAtypon = true;
                CustomerSessionDto customerSession = customerService.getCustomerByUsernameAndPassword(this.userName, password, checkEacDb, checkAtypon);
                result = customerSession.getSession();
                Customer customer = customerSession.getCustomer();
                SessionHelper.setCustomer(this.request, customer);
                Locale locale = customer.getLocale();
                SessionHelper.setLocale(request, response, locale);
            }catch(CustomerNotFoundServiceLayerException ex){
        		loginFailure(MSG_USERNAME_DOES_NOT_EXIST, ERR_CODE_LOGIN_FAILED);
        	}catch(InvalidCredentialsServiceLayerException ex){
                loginFailure(MSG_WRONG_PASSSWORD, ERR_CODE_LOGIN_FAILED);
        	}catch(AccountLockedServiceLayerException ex){
        		loginFailure(MSG_ACCOUNT_LOCKED, ERR_CODE_LOGIN_FAILED);
            }catch(ServiceLayerException ex){
            	//shouldn't get to here if 
            	loginFailure(MSG_UNKNOWN, ERR_CODE_LOGIN_FAILED);
            }
            /*} catch (ServiceLayerException sle) {

                if (LOG.isDebugEnabled()) {
                    String msg = String.format("Auth Failed for username[%s] reason [%s]", this.userName, sle.getMessage());
                    LOG.debug(msg);
                }
                loginFailure(MSG_ERIGHTS_AUTHENTICATION_FAILURE, ERR_CODE_LOGIN_FAILED);
                result = null;
            }*/
            return result;
        }
        
        /**
         * Validates the username and password against the EAC Database only
         * returning the associated Customer object.
         * 
         * @param password
         *            the password ( plain text )
         * @return the Customer corresponding to the username/password
         */
        private Customer validateUserAndPassword(final String password) {
        	Customer result = null;
        	try{
        		boolean checkEacDb = true;
        		boolean checkAtypon = false;
        		CustomerSessionDto dto = customerService.getCustomerByUsernameAndPassword(this.userName, password, checkEacDb, checkAtypon);
        		result = dto.getCustomer();
        	}catch(CustomerNotFoundServiceLayerException ex){
        		loginFailure(MSG_USERNAME_DOES_NOT_EXIST, ERR_CODE_LOGIN_FAILED);
        	}catch(InvalidCredentialsServiceLayerException ex){
                loginFailure(MSG_WRONG_PASSSWORD, ERR_CODE_LOGIN_FAILED);
        	}catch(AccountLockedServiceLayerException ex){
        		loginFailure(MSG_ACCOUNT_LOCKED, ERR_CODE_LOGIN_FAILED);
            }catch(ServiceLayerException ex){
            	//shouldn't get to here if 
            	loginFailure(MSG_UNKNOWN, ERR_CODE_LOGIN_FAILED);
            }
            return result;
        }
        
        /**
         * Get user from Rightsuit database
         * returning the associated Customer object.
         * 
         * 
         * @return the Customer corresponding to the username
         */
        private Customer getCustomerByUserName() {
        	Customer result = null;
        	try{
        		result = customerService.getCustomerByUsername(this.userName) ;
        	}catch(UserNotFoundException ex){
        		loginFailure(MSG_USERNAME_DOES_NOT_EXIST, ERR_CODE_LOGIN_FAILED);
        	}catch(ErightsException ex){
            	//shouldn't get to here if 
            	loginFailure(MSG_UNKNOWN, ERR_CODE_LOGIN_FAILED);
            }
            return result;
        }
        
        /**
         * Validates the four input parameters. An invalid errorUrl is not an error.
         * 
         * @param password
         *            the password
         * @param errorUrl
         *            the error url
         * @param successUrl
         *            the success url
         */
        private void validateParameters(final String password, final String errorUrl, final String successUrl) {

            // an invalid error url is not an error - we will just use an eac error
            // page instead.
            boolean errorUrlValidValue = isUrlValid(errorUrl);
            setErrorUrlValid(errorUrlValidValue);

            if (StringUtils.isBlank(this.userName)) {
                loginFailure(MSG_BLANK_USER_NAME, ERR_CODE_USER_NAME_BLANK);
            } else if (StringUtils.isBlank(password)) {
                loginFailure(MSG_BLANK_PASSWORD, ERR_CODE_PASSWORD_BLANK);
            } else if (!isUrlValid(successUrl)) {
                loginFailure(MSG_INVALID_SUCCESS_URL, ERR_CODE_SUCCESS_URL_INVALID);
            }
        }
        
        /**
         * Checks if is url valid.
         *
         * @param url the url
         * @return true, if is url valid
         */
        private boolean isUrlValid(final String url) {
            try {
                new URL(url);
                return true;
            } catch (MalformedURLException ex) {
                return false;
            }
        }
        
        /**
         * Sets the session token.
         *
         * @param sessionTokenP the session token
         * @param freshCookieP the fresh cookie
         */
        private void setSessionToken(final String sessionTokenP, final boolean freshCookieP) {
            this.sessionToken = sessionTokenP;
            this.freshCookie = freshCookieP;
        }

        /**
         * Login failure.
         *
         * @param debugMessage the debug message
         * @param code the code
         */
        private void loginFailure(final String debugMessage, final String code) {
            setErrorCode(code);
            if (LOG.isDebugEnabled()) {
                String msg = String.format(FMT_FAILED, userName, debugMessage);
                LOG.debug(msg);
            }
        }

    }
}
