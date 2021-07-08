package com.oup.eac.accounts.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 * Spring MVC Controller for Reset Password Failure.  
 * @author Jainam Shah
 */

@Controller
public class PasswordResetFailureController {

    private static final Logger LOG = Logger.getLogger(PasswordResetFailureController.class);
    private final String PASSWORD_RESET_FAILURE = "passwordResetFailure";

    
   
    
    @RequestMapping(value = { "/passwordResetFailure.htm" }, method=RequestMethod.GET)
    public ModelAndView handleRequest(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        return new ModelAndView(PASSWORD_RESET_FAILURE);
    }

    
}