package com.oup.eac.web.controllers.authentication;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import com.oup.eac.web.controllers.registration.EACViews;

/**
 * Password Reset Failure controller.
 * @author Jainam Shah
 */
public class PasswordResetFailureController implements Controller {

    private static final Logger LOGGER = Logger.getLogger(PasswordResetFailureController.class);

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

        return new ModelAndView(EACViews.PASSWORD_RESET_FAILURE_PAGE);

    }	
    
}
