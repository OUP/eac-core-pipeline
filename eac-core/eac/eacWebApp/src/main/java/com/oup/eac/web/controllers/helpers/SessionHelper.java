package com.oup.eac.web.controllers.helpers;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.support.RequestContextUtils;

import com.oup.eac.common.utils.EACSettings;
import com.oup.eac.common.utils.url.QueryParser;
import com.oup.eac.domain.ActivationCode;
import com.oup.eac.domain.Customer;
import com.oup.eac.domain.ProductRegistrationDefinition;
import com.oup.eac.domain.RegisterableProduct;
import com.oup.eac.domain.UrlSkin;
import com.oup.eac.dto.LicenceDto;
import com.oup.eac.web.controllers.registration.EACViews;
import com.oup.eac.web.utils.CustomerTimeoutConfig;
import com.oup.eac.web.utils.WebContentUtils;
import com.oup.eac.web.utils.impl.WebContentUtilsImpl;

/**
 * @author harlandd Session helper class
 */
public final class SessionHelper {

    private static final Logger LOG = LoggerFactory.getLogger(SessionHelper.class);

    private static final String CHANGE_PWD_TOKEN = "CHANGE_PWD_TOKEN";
    
    private static final String ERSESSION = "ERSESSION";

    private static final String FORWARD_URL = "FORWARDURL";

    public static final String URL_SKIN = "URL_SKIN";

    private static final String PRODUCT = "PRODUCT";

    private static final String PRODUCT_REGISTRATION_DEFINITION = "PRODUCT_REGISTRATION_DEFINITION";

    private static final String CUSTOMER = "CUSTOMER";

    private static final String REGISTRATION = "REGISTRATION";
    
    private static final String LICENSE = "LICNESE";

    private static final String CHANGE_CUSTOMER = "CHANGE_CUSTOMER";

    private static final String ACTIVATION_CODE = "ACTIVATION_CODE";

    private static final String RE_REGISTER = "RE_REGISTER";

    private static final String IS_ERROR = "isError";
    
    private static final String WEB_USER_NAME = "webUserName";
    
    public static final String BEAN_NAME_WEB_CONTENT_UTILS = "webContentUtils";
    
    public static final String EAC_INSYNC_TOKEN = "eacInSyncToken";
    
	public static final String CUSTOMER_SESSION_START = "CustomerSessionStart";

	public static final String AWAITING_VALIDATION = "awaitingValidation";
	
	public static final String BEAN_NAME_WEB_CUSTOMER_CONFIG = "webCustomerTimeoutConfig";
	
	public static final String COMPLETED = "completed";
	
	private static final String LICNECE_DTO = "licneceDto";
	
	private static final String URL = "url";
    
    /**
     * Private default constructor.
     */
    private SessionHelper() {

    }

    /**
     * Gets the forward url.
     *
     * @param request the request
     * @return the forward url
     */
    public static String getForwardUrl(final HttpServletRequest request) {
        return getForwardUrl(request.getSession());
    }

    /**
     * Get FORWARDURL from the session. If this is not available the setting
     * EAC_DEFAULT_FORWARD_URL will be returned. Will include the ERSESSION
     * parameter by default (if it is available).
     * 
     * @param session
     *            the session
     * @return the forwarding url
     */
    public static String getForwardUrl(final HttpSession session) {
        return getForwardUrl(session, true);
    }

    /**
     * Get FORWARDURL from the session. If this is not available the setting
     * EAC_DEFAULT_FORWARD_URL will be returned. If it is not required that
     * ERSESSION be added to the url, includeErightsSessionId should be false.
     * 
     * @param session
     *            the session
     * @param includeErightsSessionId
     *            true if ERSESSION parameter should be appended to the url
     * @return the forwarding url
     */
    public static String getForwardUrl(final HttpSession session, final boolean includeErightsSessionId) {
        String forwardUrl = (String) session.getAttribute(FORWARD_URL);
        if (forwardUrl == null) {
            forwardUrl = EACSettings.getProperty(EACSettings.EAC_DEFAULT_FORWARD_URL, EACViews.PROFILE_VIEW);
        }

        String erSession = getErightsSession(session);
        if (StringUtils.isBlank(erSession) && includeErightsSessionId) {
            LOG.info("Attempt to include ERSESSION parameter on forwarding url has failed. Perhaps the user is not logged in?");
        }
        if (StringUtils.isNotBlank(erSession) && includeErightsSessionId) {
            forwardUrl = appendErSessionToUrl(forwardUrl, erSession);
        }
        return forwardUrl;
    }

    /**
     * Appends the ERSESSION query string parameter to a url.
     * 
     * @param url the url to append to
     * @param erSession the value of the ERSESSION query string parameter
     * @return the result url
     */
    public static String appendErSessionToUrl(final String url, final String erSession) {
        QueryParser queryParser = new QueryParser(url);
        queryParser.replaceParameter(ERSESSION, erSession);
        String result = queryParser.getUrl();
        return result;
    }

    /**
     * Set ERSESSION in the session.
     * 
     * @param session
     *            httpSession
     * @param erSession
     *            erSession value
     */
    public static void setErightsSession(final HttpSession session, final String erSession) {
        session.setAttribute(ERSESSION, erSession);
    }

    /**
     * Get ERSESSION from the session.
     * 
     * @param session
     *            http session
     * @return er session
     */
    public static String getErightsSession(final HttpSession session) {
        return (String) session.getAttribute(ERSESSION);
    }

    /**
     * @param session
     *            the session
     * @param url
     *            the forwarding url
     */
    public static void setForwardUrl(final HttpSession session, final String url) {
        session.setAttribute(FORWARD_URL, url);
    }

    /**
     * Gets the registerable product.
     *
     * @param session the session
     * @return the registerable product
     */
    public static RegisterableProduct getRegisterableProduct(final HttpSession session) {
        return (RegisterableProduct) session.getAttribute(PRODUCT);
    }

    /**
     * Sets the registerable product.
     *
     * @param session the session
     * @param product the product
     */
    public static void setRegisterableProduct(final HttpSession session, final RegisterableProduct product) {
        session.setAttribute(PRODUCT, product);
    }

    /**
     * @param session
     *            the session
     * @return the product registration definition
     */
    public static ProductRegistrationDefinition getProductRegistrationDefinition(final HttpSession session) {
        return (ProductRegistrationDefinition) session.getAttribute(PRODUCT_REGISTRATION_DEFINITION);
    }

    /**
     * @param session
     *            the session
     * @param productRegistrationDefinition
     *            the product registration definition
     */
    public static void setProductRegistrationDefinition(final HttpSession session, final ProductRegistrationDefinition productRegistrationDefinition) {
        session.setAttribute(PRODUCT_REGISTRATION_DEFINITION, productRegistrationDefinition);
    }

    /**
     * @param request
     *            the request
     * @return the username
     */
    public static String getRegistrationId(final HttpServletRequest request) {
        return (String) request.getSession().getAttribute(REGISTRATION);
    }

    /**
     * Sets the registration id.
     *
     * @param request the request
     * @param registrationId the registration id
     */
    public static void setRegistrationId(final HttpServletRequest request, final String registrationId) {
        request.getSession().setAttribute(REGISTRATION, registrationId);
    }
    
    /**
     * @param request
     * @return
     */
    public static String getLicenseId(final HttpServletRequest request) {
        return (String) request.getSession().getAttribute(LICENSE);
    }

    
    /**
     * @param request
     * @param licenseId
     */
    public static void setLicenseId(final HttpServletRequest request, final String licenseId) {
        request.getSession().setAttribute(LICENSE, licenseId);
    }

    /**
     * @param request
     *            the request
     */
    public static void removeRegistration(final HttpServletRequest request) {
        request.getSession().removeAttribute(REGISTRATION);
    }

    /**
     * @param request
     *            the request
     * @return the username
     */
    public static String getChangeCustomer(final HttpServletRequest request) {
        return (String) request.getSession().getAttribute(CHANGE_CUSTOMER);
    }

    /**
     * @param request
     *            the request
     * @param username
     *            the username
     */
    public static void setChangeCustomer(final HttpServletRequest request, final String username) {
        request.getSession().setAttribute(CHANGE_CUSTOMER, username);
    }

    /**
     * @param request
     *            the request
     */
    public static void removeChangeCustomer(final HttpServletRequest request) {
        request.getSession().removeAttribute(CHANGE_CUSTOMER);
    }

    /**
     * @param request
     *            the request
     * @return the product definition
     */
    public static Customer getCustomer(final HttpServletRequest request) {
        Customer customer =  (Customer) request.getSession().getAttribute(CUSTOMER);
		//Check that customer has not been in the session for too long - EAC Caches customer in session but this should be time limited.
		if (customer != null) {
			boolean customerTimedOut = hasCustomerTimedOut(customer, request);
			if(customerTimedOut){
				setCustomer(request, null);
				customer = null;
			}
		}
        return customer;
    }
    
	private static CustomerTimeoutConfig getCustomerTimeoutConfig(HttpServletRequest request){
		CustomerTimeoutConfig timeoutConfig = null;
		try {
			WebApplicationContext context = RequestContextUtils.getWebApplicationContext(request);
			timeoutConfig = context.getBean(BEAN_NAME_WEB_CUSTOMER_CONFIG, CustomerTimeoutConfig.class);
		} catch (RuntimeException ex) {
			if (LOG.isDebugEnabled()) {
				LOG.debug("problem getting customer", ex);
			}
		}
		return timeoutConfig;
	}
	
	private static boolean hasCustomerTimedOut(Customer customer, HttpServletRequest request){
		boolean hasTimedOut = false;
		CustomerTimeoutConfig timeoutConfig = getCustomerTimeoutConfig(request);
		if(timeoutConfig != null){
			int timeoutMins = timeoutConfig.getCustomerTimeoutMins();
			long timeoutMs = TimeUnit.MINUTES.toMillis(timeoutMins);
			Long startTime = (Long) request.getSession().getAttribute(CUSTOMER_SESSION_START);
			if (startTime != null) {
				long diff = System.currentTimeMillis() - startTime;
				hasTimedOut = timeoutMs > 0 && diff > timeoutMs;
				if(hasTimedOut){
					String msg = String.format("Customer Object in HttpSession for Username[%s] has been in there greater than [%d]mins", customer.getUsername(), timeoutMins);
					LOG.info(msg);
				}
			}
		}
		return hasTimedOut;
	}

	public static Boolean isCustomerLoggedIn(final HttpServletRequest request) {
        Customer customer = getCustomer(request);
        Boolean isLoggedIn = customer != null && customer.getCreatedDate() != null;
        return isLoggedIn;
    }

    /**
     * @param request
     *            the request
     * @param customer
     *            the Customer
     */
    public static void setCustomer(final HttpServletRequest request, final Customer customer) {
        String webUserName = generateWebUserName(request, customer);
        setCustomerAndWebUserName(request,  customer, webUserName);
    }

    /**
     * Initialise web user name. Called whenever the customer is changed.
     *
     * @param request the request
     * @param customer the customer
     * @return the string
     */
    private static String generateWebUserName(final HttpServletRequest request, final Customer customer) {
        if (customer == null) {
            return null;
        }
        try {
            WebApplicationContext context = RequestContextUtils.getWebApplicationContext(request);
            WebContentUtils utils = context.getBean(BEAN_NAME_WEB_CONTENT_UTILS, WebContentUtilsImpl.class);
            String webUserName = utils.getCustomerName(customer);
            return webUserName;
        } catch (RuntimeException ex) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("problem generating webUserName", ex);
            }
            return null;
        }
    }

    /**
     * @param request
     *            the request
     * @return the product definition
     */
    public static ActivationCode getActivationCode(final HttpServletRequest request) {
        return (ActivationCode) request.getSession().getAttribute(ACTIVATION_CODE);
    }

    /**
     * Sets the activation code.
     *
     * @param request the request
     * @param activationCode the activation code
     */
    public static void setActivationCode(final HttpServletRequest request, final ActivationCode activationCode) {
        request.getSession().setAttribute(ACTIVATION_CODE, activationCode);
    }

    /**
     * @param request
     *            the request
     */
    public static void removeActivationCode(final HttpServletRequest request) {
        request.getSession().removeAttribute(ACTIVATION_CODE);
    }

    /**
     * Sets the reregister.
     *
     * @param request the new reregister
     */
    public static void setReregister(final HttpServletRequest request) {
        request.getSession().setAttribute(RE_REGISTER, Boolean.TRUE);
    }

    /**
     * @param request
     *            the request
     * @return the product definition
     */
    public static Boolean getReregister(final HttpServletRequest request) {
        return (Boolean) request.getSession().getAttribute(RE_REGISTER);
    }

    /**
     * @param request
     *            the request
     */
    public static void removeReregister(final HttpServletRequest request) {
        request.getSession().removeAttribute(RE_REGISTER);
    }

    /**
     * @param request
     *            the request
     */
    public static void removeErSession(final HttpServletRequest request) {
        request.getSession().removeAttribute(ERSESSION);
    }

    /**
     * @param request
     *            the request
     * @return the product definition
     */
    public static ProductRegistrationDefinition getProductRegistrationDefinition(final HttpServletRequest request) {
        return getProductRegistrationDefinition(request.getSession());
    }

    /**
     * @param request
     *            the request
     * @param productRegistrationDefinition
     *            the product definition
     */
    public static void setProductRegistrationDefinition(final HttpServletRequest request, final ProductRegistrationDefinition productRegistrationDefinition) {
        setProductRegistrationDefinition(request.getSession(), productRegistrationDefinition);
    }

    /**
     * @param request
     *            the request
     * @return the product definition
     */
    public static RegisterableProduct getRegisterableProduct(final HttpServletRequest request) {
        return getRegisterableProduct(request.getSession());
    }

    /**
     * Sets the registerable product.
     *
     * @param request the request
     * @param product the product
     */
    public static void setRegisterableProduct(final HttpServletRequest request, final RegisterableProduct product) {
        setRegisterableProduct(request.getSession(), product);
    }

    /**
     * Kill session.
     *
     * @param request the request
     */
    public static void killSession(final HttpServletRequest request) {
        request.getSession().invalidate();
    }

    /**
     * Gets the url skin.
     * 
     * @param request
     *            the request
     * @return the url skin
     */
    public static UrlSkin getUrlSkin(final HttpServletRequest request) {
        HttpSession session = request.getSession();
        UrlSkin result = (UrlSkin) session.getAttribute(URL_SKIN);
        return result;
    }

    /**
     * Sets the url skin.
     * 
     * @param request
     *            the request
     * @param urlSkin
     *            the url skin
     */
    public static void setUrlSkin(final HttpServletRequest request, final UrlSkin urlSkin) {
        HttpSession session = request.getSession();
        session.setAttribute(URL_SKIN, urlSkin);
    }
    
    
    /**
     * Sets the is error.
     *
     * @param request the request
     * @param value the value
     */
    public static void setIsError(final HttpServletRequest request, final Boolean value) {
        request.setAttribute(IS_ERROR, value);
    }
    
    /**
     * Gets the isError flag in the request.
     *
     * @param request the request
     * @return the checks if is error
     */
    public static Boolean getIsError(final HttpServletRequest request) {
        return (Boolean) request.getAttribute(IS_ERROR);
    }    
    
    /**
     * Gets the web user name.
     *
     * @param request the request
     * @return the web user name
     */
    public static String getWebUserName(final HttpServletRequest request) {
        HttpSession session = request.getSession();
        return (String) session.getAttribute(WEB_USER_NAME);
    }
    
    /**
     * Sets the customer and web user name.
     *
     * @param request the request
     * @param customer the customer
     * @param webUserName the web user name
     */
    public static void setCustomerAndWebUserName(final HttpServletRequest request, final Customer customer, final String webUserName) {
        HttpSession session = request.getSession();
        session.setAttribute(CUSTOMER, customer);
        session.setAttribute(WEB_USER_NAME, webUserName);
		if (customer == null) {
			session.removeAttribute(CUSTOMER_SESSION_START);
		} else {
			session.setAttribute(CUSTOMER_SESSION_START, System.currentTimeMillis());
		}
        if (LOG.isDebugEnabled()) {
            String username = customer == null ? null : customer.getUsername();
            String msg = String.format("Setting 'webUsername' of customer username[%s] to [%s]", username, webUserName);
            LOG.debug(msg);
        }
    }
    
    
    public static Locale getLocale(HttpServletRequest request){
    	Locale result = RequestContextUtils.getLocale(request);
    	return result;
    }
    
    public static void setLocale(HttpServletRequest request, HttpServletResponse response, Locale locale){
    	LocaleResolver resolver = RequestContextUtils.getLocaleResolver(request);
    	if(resolver != null){
    		resolver.setLocale(request, response, locale);
    	}else{
    		LOG.error("No LocaleResolver available");    
    	}
    }
    
    /**
     * Gets the in sync token.
     *
     * @param session the session
     * @return the in sync token
     */
    public static String getInSyncToken(final HttpSession session) {
        String result = (String) session.getAttribute(EAC_INSYNC_TOKEN);
        return result;
    }


    /**
     * Sets the eac in sync token.
     *
     * @param session the new eac in sync token
     */
    public static void setEacInSyncToken(final HttpSession session) {
        String newValue = UUID.randomUUID().toString();
        if (LOG.isDebugEnabled()) {
            String msg = String.format("Setting inSync token for [%s] to [%s]", session.getId(), newValue);
            LOG.debug(msg);
        }
        session.setAttribute(EAC_INSYNC_TOKEN, newValue);
    }

    /**
     * Gets the submitted eac in sync token.
     *
     * @param request the request
     * @return the submitted eac in sync token
     */
    public static String getSubmittedEacInSyncToken(final HttpServletRequest request) {
        String result = request.getParameter(EAC_INSYNC_TOKEN);
        return result;
    }
    
    public static String getLocaleForwardUrl(final String redirectUrl,final HttpServletRequest request){
    	System.out.println("Inside the getLocaleForwardUrl method>>>>>");
    	StringBuilder localeSpcRedirectUrl=new StringBuilder(redirectUrl);
    	Locale locale=getLocale(request);
    	if(null!=locale){
    		localeSpcRedirectUrl.append("?selLanguage=").append(locale.getLanguage()).append("&mode=hub");
    	}    	
    	return localeSpcRedirectUrl.toString();
    }
    
    
    /**
     * @param request
     * @param awaitingValidation
     */
    public static void setAwaitingValidation(final HttpSession session, final Boolean awaitingValidation) {
    	session.setAttribute(AWAITING_VALIDATION, awaitingValidation);
    }

    
    /**
     * @param session
     * @return
     */
    public static Boolean isAwaitingValidation(final HttpSession session) {
        return (Boolean) session.getAttribute(AWAITING_VALIDATION);
    }
    
    
   /**
    * @param session
    * @param completed
    */	
	public static void setCompleted(final HttpSession session, final boolean completed) {
		if (SessionHelper.getRegisterableProduct(session) != null ) {
			String productId = SessionHelper.getRegisterableProduct(session).getId() ;
			Map<String, Boolean> productRegistartionCompletedMapping = (HashMap<String,Boolean>) session.getAttribute(COMPLETED) ;
			if (productRegistartionCompletedMapping == null ) {
				productRegistartionCompletedMapping = new HashMap<String, Boolean>() ;
			}
			productRegistartionCompletedMapping.put(productId, completed) ;
			session.setAttribute(COMPLETED, productRegistartionCompletedMapping);
		}
	}
	  
	   /**
	 * @param session
	 * @return
	 */
	public static Boolean isCompleted(final HttpSession session) {
		Boolean isCompleted = null;
		if (SessionHelper.getRegisterableProduct(session) != null) {
			Map<String, Boolean> productRegistartionCompletedMapping = (HashMap<String, Boolean>) session
					.getAttribute(COMPLETED);
			if (productRegistartionCompletedMapping != null
					&& productRegistartionCompletedMapping.size() > 0) {
				String productId = SessionHelper.getRegisterableProduct(session).getId() ;
				if (productRegistartionCompletedMapping.get(productId) != null
						&& productRegistartionCompletedMapping.get(productId)) {
					isCompleted = true;
				}
			}
		}
		return isCompleted;
	   }
	
	/**
     * @param session
     * @param licenceDto
     */
    public static void setLicenceDto(final HttpSession session, final LicenceDto licenceDto) {
        session.setAttribute(LICNECE_DTO, licenceDto);
    }
    
    /**
     * @param session
     *            the session
     * @return the Licence Dto
     */
    public static LicenceDto getLicenceDto(final HttpSession session) {
        return (LicenceDto) session.getAttribute(LICNECE_DTO);
    }

	/**
	 * @param session
	 * @return
	 */
	public static String getChangePwdToken(final HttpSession session) {
		return (String) session.getAttribute(CHANGE_PWD_TOKEN);
	}
	 
	/**
	 * @param session
	 * @param changePwdToken
	 */
	public static void setChangePwdToken(final HttpSession session, final String changePwdToken ) {
		session.setAttribute(CHANGE_PWD_TOKEN, changePwdToken);
	}
	
	/**
	 * @param session
	 * @return
	 */
	public static String getURL(final HttpSession session) {
		return (String) session.getAttribute(URL);
	}
	 
	/**
	 * @param session
	 * @param changePwdToken
	 */
	public static void setURL(final HttpSession session, final String url ) {
		session.setAttribute(URL, url);
	}
	
	/**
	 * @param request
	 */
	public static void removeChangePwdToken(final HttpServletRequest request) {
        request.getSession().removeAttribute(CHANGE_PWD_TOKEN);
    }
	/**
     * @param request
     *            the request
     */
    public static void removeIsCompleted(final HttpServletRequest request) {
    	if (SessionHelper.getRegisterableProduct(request.getSession()) != null ){
	    	String productId = SessionHelper.getRegisterableProduct(request.getSession()).getId() ;
	    	Map<String, Boolean> productRegistartionCompletedMapping = (HashMap<String,Boolean>) request.getSession().getAttribute(COMPLETED) ;
	    	if (productRegistartionCompletedMapping != null && productRegistartionCompletedMapping.size() > 0 && productRegistartionCompletedMapping.containsKey(productId)) {
	    		productRegistartionCompletedMapping.remove(productId);
	    	}
	        request.getSession().removeAttribute(COMPLETED);
    	}
    }
}
