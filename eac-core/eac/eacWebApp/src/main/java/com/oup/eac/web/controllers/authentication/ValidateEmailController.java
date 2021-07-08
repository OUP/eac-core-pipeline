package com.oup.eac.web.controllers.authentication;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;
import org.springframework.web.servlet.view.RedirectView;

import com.oup.eac.service.CustomerService;
import com.oup.eac.web.controllers.registration.EACViews;

public class ValidateEmailController implements Controller {

    private CustomerService customerService;
    
    /**
     * Construct new RegistrationConfirmationController.
     * 
     * @param customerService
     *            The service this controller should use.
     */
    public ValidateEmailController(final CustomerService customerService) {
        this.customerService = customerService;
    }

    @Override
    public ModelAndView handleRequest(HttpServletRequest request,HttpServletResponse response) throws Exception {
        try {
            String tokenString = request.getParameter("token");
            String url = customerService.updateValidationEmail(tokenString);
            return new ModelAndView(new RedirectView(url, false));
        } catch (Exception e) {
            return new ModelAndView(EACViews.ACTIVATION_ERROR_PAGE);
        }
    }

}
