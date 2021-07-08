/**
 * 
 */
package com.oup.eac.web.interceptors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.oup.eac.domain.Customer;
import com.oup.eac.domain.RegisterableProduct;
import com.oup.eac.domain.RegisterableProduct.RegisterableType;
import com.oup.eac.web.controllers.helpers.SessionHelper;

/**
 * Checks that there is a registerable product and that it is SELF_REGISTERABLE
 * 
 * @author David Harland
 */
public class RegistrationInterceptor extends HandlerInterceptorAdapter {
	

	private static final Logger LOGGER = Logger.getLogger(RegistrationInterceptor.class);

    private static final String NULL_PRODUCT_ERROR_MESSAGE = "USER:- %s - has attempted to register for a product URL:- %s - but there is no product in the session.";
    private static final String MY_PROFILE = "profile.htm";
    
    /**
     * @param request
     *            the request
     * @param response
     *            the response
     * @param handler
     *            the handler
     * @throws Exception
     *             any checked exception
     * @return true if the handler chain should continue
     */
    @Override
    public final boolean preHandle(final HttpServletRequest request, final HttpServletResponse response, final Object handler) throws Exception {

        RegisterableProduct regProduct = SessionHelper.getRegisterableProduct(request);
        
        if (regProduct == null) {
        	LOGGER.warn(getMessage(request) + " Redirecting to " + MY_PROFILE);
        	response.sendRedirect(MY_PROFILE);
        	return false;
        }
        
        if(regProduct.getRegisterableType() != RegisterableType.SELF_REGISTERABLE) {
        	LOGGER.warn("Registation is only allowed for self registerable products. Redirecting to " + MY_PROFILE);
        	response.sendRedirect(MY_PROFILE);
        	return false;
        }

        return true;
    }
    
    private String getMessage(final HttpServletRequest request) {
        Customer customer = SessionHelper.getCustomer(request);
        String username = customer != null ? customer.getUsername() : null;
        String url = SessionHelper.getForwardUrl(request);    
        return String.format(NULL_PRODUCT_ERROR_MESSAGE, username, url);
    }
   

}
