/**
 * 
 */
package com.oup.eac.web.controllers.authentication;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import com.oup.eac.web.controllers.helpers.SessionHelper;

/**
 * @author harlandd Cookie error controller
 */
public class ReregisterController implements Controller {

    /**
     * @param request
     *            the HttpServletRequest
     * @param response
     *            the HttpServletResponse
     * @return the ModelAndView
     * @throws Exception
     *             any checked exception
     */
    @Override
    public final ModelAndView handleRequest(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
    	SessionHelper.setReregister(request);
        return AuthenticationWorkFlow.getSuccessfulEndState(request);
    }

}
