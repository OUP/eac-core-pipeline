/**
 * 
 */
package com.oup.eac.web.interceptors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.oup.eac.web.controllers.helpers.RequestHelper;
import com.oup.eac.web.controllers.helpers.SessionHelper;

/**
 * @author harlandd Interceptor to check if there is a product definition
 */
public class ProductRegistrationDefinitionInterceptor extends HandlerInterceptorAdapter {

    private static final Logger LOGGER = Logger.getLogger(ProductRegistrationDefinitionInterceptor.class);
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
        HttpSession httpSession = request.getSession(false);
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Session is null: " + (httpSession == null));
            if (httpSession != null) {
                LOGGER.debug("Product Registration Definition: " + SessionHelper.getProductRegistrationDefinition(httpSession));
            }
        }
        if (httpSession == null || SessionHelper.getProductRegistrationDefinition(httpSession) == null) {
            LOGGER.warn(buildMessage());
        	response.sendRedirect(MY_PROFILE);
        	return false;
        }
        return true;
    }

	private String buildMessage() {
		StringBuilder message = new StringBuilder();
		message.append("No product registration definition was found in session for this url.");
		message.append(" Redirecting to ");
		message.append(MY_PROFILE);
		return message.toString();
	}
    
    @Override
    public final void postHandle(
            final HttpServletRequest request, 
            final HttpServletResponse response, 
            final Object handler, 
            final ModelAndView modelAndView)
            throws Exception {
        RequestHelper.setShowingProductRegistrationPage(modelAndView, request);
    }

}
