package com.oup.eac.web.controllers.thirdpartyui;

import java.net.MalformedURLException;
import java.net.URL;

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

import com.oup.eac.common.utils.EACSettings;
import com.oup.eac.common.utils.crypto.SimpleCipher;
import com.oup.eac.domain.Customer;
import com.oup.eac.service.CustomerService;
import com.oup.eac.service.ServiceLayerException;
import com.oup.eac.service.exceptions.CustomerNotFoundServiceLayerException;
import com.oup.eac.web.controllers.helpers.CookieHelper;
import com.oup.eac.web.controllers.helpers.SessionHelper;

/**
 * The Class EacCookieController.
 * 
 * @author David Hay
 * 
 */
@Controller("eacCookieController")
public class EacCookieController {

    private static final Logger LOG = Logger.getLogger(EacCookieController.class);
    public static final String SESSION_TOKEN = "sessionToken";
    public static final String SUCCESS_URL = "successUrl";
    public static final String USER_NAME = "userName";
    public static final String REDIRECT = "redirect:";
    public static final String ERROR_JSP = "sessionTokenError";
    public static final String ERROR_MSG_CODE = "errorCode";

    public static final String ERROR_CODE_INVALID_SESSION = "session.token.error.invalid.session";
    public static final String ERROR_CODE_DECRYPT_PROBLEM = "session.token.error.decrypt.problem";
    public static final String ERROR_CODE_SUCCESS_URL_BLANK = "session.token.error.url.blank";
    public static final String ERROR_CODE_SUCCESS_URL_INVALID = "session.token.error.url.invalid";
    public static final String ERROR_CODE_SESSION_TOKEN_BLANK = "session.token.error.token.blank";

    private final CustomerService customerService;

    /**
     * Instantiates a new eac cookie controller.
     * 
     * @param customerService
     *            the customer service
     */
    @Autowired
    public EacCookieController(final CustomerService customerService) {
        this.customerService = customerService;
    }

    /**
     * Generate cookie from session token.
     * 
     * @param encryptedSessionToken
     *            the encrypted session token
     * @param redirectSuccessUrl
     *            the redirect success url
     * @param request
     *            the request
     * @param response
     *            the response
     * @return the model and view
     */
    @RequestMapping(method = RequestMethod.POST, value = "/cookieFromSession.htm")
    public final ModelAndView generateCookieFromSessionToken(@RequestParam(SESSION_TOKEN) final String encryptedSessionToken,
            @RequestParam(SUCCESS_URL) final String redirectSuccessUrl, final HttpServletRequest request, final HttpServletResponse response) {

        Context context = getCookieFromSessionToken(encryptedSessionToken, redirectSuccessUrl, request, response);

        ModelAndView result;

        if (context.isError()) {
            result = getErrorResult(request, context.getErrorCode());
        } else {
            result = getSuccessResult(context.getRedirectUrl());
        }

        return result;
    }

    /**
     * Gets the cookie from session token.
     *
     * @param encryptedSessionToken the encrypted session token
     * @param redirectSuccessUrl the redirect success url
     * @param request the request
     * @param response the response
     * @return the cookie from session token
     */
    private Context getCookieFromSessionToken(
            final String encryptedSessionToken, final String redirectSuccessUrl, final HttpServletRequest request, final HttpServletResponse response) {

        Context context = new Context();

        // VALIDATE THE PARAMS
        validateParams(context, encryptedSessionToken, redirectSuccessUrl);
        if (context.isError()) {
            return context;
        }

        // GET THE SESSION TOKEN
        String sessionToken = getSessionToken(context, encryptedSessionToken);
        if (context.isError()) {
            return context;
        }

        // CHECK TO SEE IF THE SESSION IS CURRENT
        Customer customer = getCustomerFromSessionToken(context, sessionToken);
        if (context.isError()) {
            return context;
        }

        // CHECK TO SEE IF WE HAVE AN EXISTING EAC COOKIE FOR A DIFFERENT
        // SESSION
        Cookie existingEacCookie = CookieHelper.getErightsCookie(request);
        if (existingEacCookie != null) {
            if (!existingEacCookie.getValue().equals(sessionToken)) {
                // the cookie and the sessionToken are for different eRights
                // sessions.
                terminateErightsSession(existingEacCookie);
            }
        }

        // set up EAC cookie in the response
        CookieHelper.setErightsCookie(response, sessionToken);

        // redirectUrl = <redirectSuccessUrl>?ERSESSION=<sessionToken>
        String redirectUrl = SessionHelper.appendErSessionToUrl(redirectSuccessUrl, sessionToken);

        if (LOG.isDebugEnabled()) {
            String msg = String.format("Created Eac Cookie from posted encrypted sessionToken [%s] for Customer id[%s] username[%s]", encryptedSessionToken,
                    customer.getId(), customer.getUsername());
            LOG.debug(msg);
        }
        context.setRedirectUrl(redirectUrl);
        return context;
    }

    /**
     * Basic validation checks on the parameters.
     *
     * @param context the context
     * @param encryptedSessionToken the encrypted session token
     * @param redirectSuccessUrl the redirect success url
     */
    @SuppressWarnings("unused")
    private void validateParams(final Context context, final String encryptedSessionToken, final String redirectSuccessUrl) {
        if (StringUtils.isBlank(encryptedSessionToken)) {
            context.setErrorCode(ERROR_CODE_SESSION_TOKEN_BLANK);
            LOG.trace("Invalid Parameter : encrypted : sesionToken cannot be blank");
        }
        if (StringUtils.isBlank(redirectSuccessUrl)) {
            context.setErrorCode(ERROR_CODE_SUCCESS_URL_BLANK);
            LOG.trace("Invalid Parameter : successUrl : cannot be blank");
        }

        try {
            new URL(redirectSuccessUrl);
        } catch (MalformedURLException e) {
            context.setErrorCode(ERROR_CODE_SUCCESS_URL_INVALID);
            LOG.trace("Invalid Parameter : successUrl : Invalid URL : " + redirectSuccessUrl);
        }
    }

    /**
     * Terminate erights session.
     *
     * @param existingEacCookie the existing eac cookie
     */
    private void terminateErightsSession(final Cookie existingEacCookie) {
        String session = existingEacCookie.getValue();
        if (LOG.isDebugEnabled()) {
            LOG.debug("Cookie found for logout. The erights session to logout is = " + session);
        }
        if (StringUtils.isNotBlank(session)) {
            try {
                customerService.logout(session);
            } catch (ServiceLayerException e) {
                LOG.debug("Problem logging out session: " + session, e);
            }
        }
    }

    /**
     * Gets the customer from session token.
     *
     * @param context the context
     * @param sessionToken the session token
     * @return the customer from session token
     */
    private Customer getCustomerFromSessionToken(final Context context, final String sessionToken) {
        Customer result = null;
        boolean error = false;
        try {
            result = customerService.getCustomerFromSession(sessionToken);
            if (result == null) {
                error = true;
            }
        } catch (CustomerNotFoundServiceLayerException ex) {
            error = true;
        }
        if (error) {
            context.setErrorCode(ERROR_CODE_INVALID_SESSION);
            if (LOG.isDebugEnabled()) {
                String msg = String.format("No customer found for plainText sessionToken[%s]", sessionToken);
                LOG.debug(msg);
            }
            result = null;
        }
        return result;
    }

    /**
     * Gets the session token.
     *
     * @param context the context
     * @param encryptedSessionToken the encrypted session token
     * @return the session token
     */
    private String getSessionToken(final Context context, final String encryptedSessionToken) {
        String result = null;
        try {
            String sessionTokenEncryptionKey = EACSettings.getRequiredProperty(EACSettings.EAC_SESSION_TOKEN_ENCRYPTION_KEY);
            result = SimpleCipher.decrypt(encryptedSessionToken, sessionTokenEncryptionKey);

            if (LOG.isTraceEnabled()) {
                String msg = String.format("Encrypted Session Token [%s] -> Plain text Session Token [%s]", encryptedSessionToken, result);
                LOG.trace(msg);
            }
        } catch (Exception ex) {

            context.setErrorCode(ERROR_CODE_DECRYPT_PROBLEM);

            if (LOG.isDebugEnabled()) {
                String msg = String.format("Problem Decrypting SessionToken [%s]", encryptedSessionToken);
                LOG.debug(msg, ex);
            }
        }
        return result;
    }

    /**
     * Gets the success result.
     *
     * @param url the url
     * @return the success result
     */
    private ModelAndView getSuccessResult(final String url) {
        StringBuilder sb = new StringBuilder(REDIRECT);
        sb.append(url);
        String viewName = sb.toString();
        if (LOG.isTraceEnabled()) {
            String msg = String.format("generateCookieFromSessionToken result view name is [%s]", viewName);
            LOG.trace(msg);
        }
        ModelAndView result = new ModelAndView(viewName);
        return result;
    }

    /**
     * Gets the error result.
     *
     * @param request the request
     * @param errorCode the error code
     * @return the error result
     */
    private ModelAndView getErrorResult(final HttpServletRequest request, final String errorCode) {
        if (errorCode != null) {
            request.setAttribute(ERROR_MSG_CODE, errorCode);
        }
        ModelAndView result = new ModelAndView(ERROR_JSP);
        return result;
    }

    private static class Context {
        private String errorCode;
        private boolean error;
        private String redirectUrl;

        /**
         * Gets the error code.
         *
         * @return the error code
         */
        public String getErrorCode() {
            return errorCode;
        }

        /**
         * Sets the error code.
         *
         * @param errorCode the new error code
         */
        public void setErrorCode(final String errorCode) {
            this.error = true;
            this.errorCode = errorCode;
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
         * Gets the redirect url.
         *
         * @return the redirect url
         */
        public String getRedirectUrl() {
            return redirectUrl;
        }

        /**
         * Sets the redirect url.
         *
         * @param redirectUrl the new redirect url
         */
        public void setRedirectUrl(final String redirectUrl) {
            this.redirectUrl = redirectUrl;
        }

    }
}
