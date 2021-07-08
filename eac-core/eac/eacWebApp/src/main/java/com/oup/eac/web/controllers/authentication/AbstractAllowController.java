package com.oup.eac.web.controllers.authentication;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.util.Assert;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import com.oup.eac.domain.ProductRegistrationDefinition;
import com.oup.eac.domain.Registration;
import com.oup.eac.service.RegistrationService;
import com.oup.eac.web.controllers.registration.EACViews;

public abstract class AbstractAllowController implements Controller {

    private final RegistrationService registrationService;

    /**
     * Construct new AbstractAllowController.
     * 
     * @param registrationService
     *            The service this controller should use.
     */
    public AbstractAllowController(final RegistrationService registrationService) {
        super();
        Assert.notNull(registrationService);
        this.registrationService = registrationService;
    }

    /**
     * @param request
     *            the HttpServletRequest
     * @param response
     *            the HttpServletResponse
     * @return the ModelAndView
     * @throws Exception
     *             any checked exception
     */
	@SuppressWarnings("unchecked")
	@Override
    public final ModelAndView handleRequest(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
		HttpSession session = request.getSession();
		String tokenString = request.getParameter("token");
		
        if (StringUtils.isEmpty(tokenString)) {
        	return new ModelAndView(EACViews.ACTIVATION_ERROR_PAGE);
        }
        
        synchronized (session) {
        	// Prevent multiple clicks of the same link treading on one another
        	Object allowToken = session.getAttribute(tokenString);
        	if (allowToken == null) {
        		session.setAttribute(tokenString, Boolean.TRUE);
        	} else {
        		getLog().warn("Ignoring duplicate request");
        		return new ModelAndView("processing");
        	}
        }
	        
        try {
			Map<String, Object> map = registrationService.updateAllowRegistrationFromToken(tokenString, true);
	    	Registration<? extends ProductRegistrationDefinition> registration = (Registration<? extends ProductRegistrationDefinition>)map.get("registration");
	    	
	        if(((Boolean)map.get("activated")).booleanValue()) {
	            getLog().warn("Registration activated already so taking user to product");
	        }
	        
	        return getNotActivatedView(getParamMap(registration, (String)map.get("originalUrl")));
		} catch (Exception e) {
		    getLog().warn("unexpected exception",e);
			return new ModelAndView(EACViews.ACTIVATION_ERROR_PAGE);
		} finally {
	        synchronized (session) {
	        	session.removeAttribute(tokenString);
	        }
		}
    }

    protected abstract ModelAndView getNotActivatedView(Map<String, String> paramMap);
    
    protected Map<String, String> getParamMap(Registration<? extends ProductRegistrationDefinition> registration, String originalUrl) {
    	/*Map<String, String> paramMap = new HashMap<String, String>();
        paramMap.put("username", registration.getCustomer().getUsername());
        paramMap.put("product", registration.getRegistrationDefinition().getProduct().getProductName());
        paramMap.put("email", registration.getRegistrationDefinition().getProduct().getEmail());
		paramMap.put("productHome", registration.getRegistrationDefinition().getProduct().getHomePage());
        paramMap.put("originalUrl", originalUrl);*/
    	Map<String, String> paramMap = new HashMap<String, String>();
        paramMap.put("username", null);
        paramMap.put("product", null);
        paramMap.put("email", null);
		paramMap.put("productHome", null);
        paramMap.put("originalUrl", originalUrl);
        return paramMap;
    }
    
    public abstract Logger getLog();
}
