package com.oup.eac.web.controllers.thirdpartyui;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.oup.eac.service.CustomerService;
import com.oup.eac.service.exceptions.CustomerNotFoundServiceLayerException;
import com.oup.eac.web.controllers.helpers.CookieHelper;
import com.oup.eac.web.controllers.helpers.SessionHelper;

/**
 * The Class EacValidateCookieController.
 * 
 * Used to check whether the EAC Cookie is valid or not.
 * 
 * If valid it will redirect to the supplied success url.
 * 
 * If not valid it will redirect to the supplied failure url.
 * 
 * If an error occurs, 
 * If a valid error_url has been provided it will redirect to that
 * otherwise it will display an error page.
 *  
 * @author David Hay
 * 
 */
@Controller("eacValidateCookieController")
public class EacValidateCookieController {

    private static final Logger LOG = Logger.getLogger(EacValidateCookieController.class);

    public static final String ERROR_CODE_SUCCESS_URL_BLANK = "validate.cookie.success.url.blank";
    public static final String ERROR_CODE_SUCCESS_URL_INVALID = "validate.cookie.success.url.invalid";

    public static final String ERROR_CODE_FAILURE_URL_BLANK = "validate.cookie.failure.url.blank";
    public static final String ERROR_CODE_FAILURE_URL_INVALID = "validate.cookie.failure.url.invalid";

    public static final String ERROR_CODE_ERROR_URL_INVALID = "validate.cookie.error.url.invalid";

    public static final String ERROR_CODE_UNEXPECTED_PROBLEM_VALIDATING_COOKIE = "validate.cookie.problem.validating.cookie";

    public static final String ERROR_MSG_CODE = "errorCode";
    public static final String ERROR_MSG_PARAMS = "errorParams";

    public static final String ERROR_JSP = "validateCookieError";

    public static final String PARAM_SUCCESS_URL = "authN_url";

    public static final String PARAM_FAILURE_URL = "noAuth_url";

    public static final String PARAM_ERROR_URL = "error_url";

    private final CustomerService customerService;

    /**
     * Instantiates a new eac validate cookie controller.
     * 
     * @param customerService
     *            the customer service
     */
    @Autowired
    public EacValidateCookieController(final CustomerService customerService) {
        this.customerService = customerService;
    }

    /**
     * Validate eac cookie.
     * 
     * @param request
     *            the request
     * @param response
     *            the response
     * @param successUrl
     *            the success url
     * @param failureUrl
     *            the failure url
     * @param errorUrl
     *            the error url
     * @return the model and view
     */
    @RequestMapping(method = RequestMethod.GET, value = "cookieValidate.htm")
    public final ModelAndView validateEacCookie(final HttpServletRequest request,
            final HttpServletResponse response,
            @RequestParam(PARAM_SUCCESS_URL) final String successUrl,
            @RequestParam(PARAM_FAILURE_URL) final String failureUrl,
            @RequestParam(PARAM_ERROR_URL) final String errorUrl) {
        ModelAndView result;

        Context context = new Context(successUrl, failureUrl, errorUrl);
        if (context.isError()) {
            result = context.getJspResult(request);
        } else {
            context.validateSessionKey(request);
            if (context.isError()) {
                result = context.getErrorResult(request);
            } else if (context.isSessionValid()) {
                result = context.getSuccessResult(request);
            } else {
                result = context.getFailureResult(request);
            }
        }
        return result;
    }

    class Context {

        private final String sessionValidUrl;
        private final String sessionInvalidUrl;
        private final String errorUrl;

        private String errorCode;
        private String sessionKey;
        private boolean error;
        private boolean sessionValid;
        private String errorParam;

        /**
         * Instantiates a new context.
         * 
         * @param successUrl
         *            the success url
         * @param failureUrl
         *            the failure url
         * @param errorUrl
         *            the error url
         */
        public Context(final String successUrl, final String failureUrl, final String errorUrl) {
            this.sessionValidUrl = successUrl;
            this.sessionInvalidUrl = failureUrl;
            this.errorUrl = errorUrl;

            checkUrl(successUrl, "successUrl", PARAM_SUCCESS_URL, ERROR_CODE_SUCCESS_URL_BLANK, ERROR_CODE_SUCCESS_URL_INVALID);

            if (!isError()) {
                checkUrl(failureUrl, "failureUrl", PARAM_FAILURE_URL, ERROR_CODE_FAILURE_URL_BLANK, ERROR_CODE_FAILURE_URL_INVALID);
            }
            if (!isError()) {
                if (StringUtils.isNotBlank(errorUrl)) {
                    checkUrlInvalid(errorUrl, "errorUrl", PARAM_ERROR_URL, ERROR_CODE_ERROR_URL_INVALID);
                }
            }
        }

        /**
         * Validate session key.
         * 
         * @param request
         *            the request
         */
        public void validateSessionKey(final HttpServletRequest request) {
            Cookie cookie = CookieHelper.getErightsCookie(request);
            if (cookie == null) {
                sessionValid = false;
                return;
            }
            sessionKey = cookie.getValue();
            if (StringUtils.isBlank(sessionKey)) {
                sessionValid = false;
                return;
            }
            try {
                Customer customer = customerService.getCustomerFromSession(sessionKey);
                sessionValid = customer != null;
            } catch (CustomerNotFoundServiceLayerException e) {
                sessionValid = false;
            } catch (Exception ex) {
                String msg = String.format("Problem trying to validate session token [%s]", sessionKey);
                LOG.error(msg, ex);
                error = true;
                setErrorCode(ERROR_CODE_UNEXPECTED_PROBLEM_VALIDATING_COOKIE);
                this.errorParam = sessionKey;
            }
        }

        /**
         * Checks if is error.
         * 
         * @return true, if is error
         */
        public final boolean isError() {
            return error;
        }

        /**
         * Checks if is session valid.
         * 
         * @return true, if is session valid
         */
        public final boolean isSessionValid() {
            return sessionValid;
        }

        /**
         * Gets the failure result.
         * 
         * @param request
         *            the request
         * @return the failure result
         */
        public final ModelAndView getFailureResult(final HttpServletRequest request) {
            ModelAndView result = new ModelAndView(new RedirectView(sessionInvalidUrl));
            if (LOG.isDebugEnabled()) {
                String msg = String.format("The sessionKey [%s] is NOT valid : redirecting to [%s]", sessionKey, sessionInvalidUrl);
                LOG.debug(msg);
            }
            return result;
        }

        /**
         * Gets the success result.
         * 
         * @param request
         *            the request
         * @return the success result
         */
        public final ModelAndView getSuccessResult(final HttpServletRequest request) {
            // redirectUrl = <redirectSuccessUrl>?ERSESSION=<sessionToken>
            String redirectUrl = SessionHelper.appendErSessionToUrl(sessionValidUrl, sessionKey);
            ModelAndView result = new ModelAndView(new RedirectView(redirectUrl));
            if (LOG.isDebugEnabled()) {
                String msg = String.format("The sessionKey [%s] is valid : redirecting to [%s]", sessionKey, redirectUrl);
                LOG.debug(msg);
            }
            return result;
        }

        /**
         * Gets the error map.
         * 
         * @return the error map
         */
        private Map<String, Object> getErrorMap() {

            Map<String, Object> result = new LocalHashMap<String, Object>();

            List<Object> params = new ArrayList<Object>();
            if (StringUtils.isNotBlank(errorParam)) {
                params.add(errorParam);
            }

            result.put(ERROR_MSG_PARAMS, params.toArray());

            result.put(ERROR_MSG_CODE, errorCode);

            if (LOG.isDebugEnabled()) {
                LOG.debug("error data " + result);
            }
            return result;
        }

        /**
         * Gets the jsp result.
         * 
         * @param request
         *            the request
         * @return the jsp result
         */
        ModelAndView getJspResult(final HttpServletRequest request) {
            Map<String, Object> errorData = getErrorMap();
            ModelAndView result = getJspResult(request, errorData);
            return result;
        }

        /**
         * Gets the jsp result.
         * 
         * @param request
         *            the request
         * @param errorData
         *            the error data
         * @return the jsp result
         */
        private ModelAndView getJspResult(final HttpServletRequest request, final Map<String, Object> errorData) {
            ModelAndView result = new ModelAndView(ERROR_JSP);
            result.getModelMap().addAllAttributes(errorData);
            return result;
        }

        /**
         * Gets the error result.
         * 
         * @param request
         *            the request
         * @return the error result
         */
        public ModelAndView getErrorResult(final HttpServletRequest request) {
            ModelAndView result;
            Map<String, Object> errorData = getErrorMap();
            if (errorUrl == null) {
                result = getJspResult(request, errorData);
            } else {
                result = new ModelAndView(new RedirectView(errorUrl));
            }
            return result;
        }

        /**
         * Sets the error code.
         * 
         * @param errorCode
         *            the new error code
         */
        private void setErrorCode(final String errorCode) {
            this.errorCode = errorCode;
            this.error = true;
        }

        /**
         * Check url.
         * 
         * @param url
         *            the url
         * @param label
         *            the label
         * @param paramName
         *            the param name
         * @param errorCodeBlank
         *            the error code blank
         * @param errorCodeInvalid
         *            the error code invalid
         */
        private void checkUrl(final String url, final String label, final String paramName, final String errorCodeBlank, final String errorCodeInvalid) {
            boolean isBlank = checkUrlBlank(url, label, paramName, errorCodeBlank);
            if (!isBlank) {
                checkUrlInvalid(url, label, paramName, errorCodeInvalid);
            }
        }

        /**
         * Check url blank.
         * 
         * @param url
         *            the url
         * @param label
         *            the label
         * @param paramName
         *            the param name
         * @param errorCodeBlank
         *            the error code blank
         * @return true, if successful
         */
        private boolean checkUrlBlank(final String url, final String label, final String paramName, final String errorCodeBlank) {
            boolean isBlank = StringUtils.isBlank(url);
            if (isBlank) {
                setErrorCode(errorCodeBlank);
                this.errorParam = paramName;
            }
            return isBlank;
        }

        /**
         * Check url invalid.
         * 
         * @param url
         *            the url
         * @param label
         *            the label
         * @param paramName
         *            the param name
         * @param errorCodeInvalid
         *            the error code invalid
         */
        private void checkUrlInvalid(final String url, final String label, final String paramName, final String errorCodeInvalid) {
            if (!isValidUrl(url)) {
                setErrorCode(errorCodeInvalid);
                this.errorParam = paramName;
            }
        }

        /**
         * Checks if is valid url.
         * 
         * @param url
         *            the url
         * @return true, if is valid url
         */
        private boolean isValidUrl(final String url) {
            try {
                new URL(url);
                return true;
            } catch (MalformedURLException ex) {
                return false;
            }
        }
    } // Context

    private static class LocalHashMap<K, V> extends HashMap<K, V> {

        @Override
        public final String toString() {
            boolean first = true;
            StringBuffer sb = new StringBuffer();
            sb.append("{");
            for (Map.Entry<K, V> entry : this.entrySet()) {
                if (!first) {
                    sb.append(", ");
                }
                first = false;
                sb.append(entry.getKey());
                sb.append("=");
                Object value = entry.getValue();
                if (value instanceof Object[]) {
                    sb.append(Arrays.toString((Object[]) value));
                } else {
                    sb.append(value);
                }
            }
            sb.append("}");
            return sb.toString();
        }
    }
}
