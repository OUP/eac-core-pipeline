package com.oup.eac.web.controllers.profile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.oup.eac.domain.Customer;
import com.oup.eac.web.controllers.helpers.SessionHelper;
import com.oup.eac.web.controllers.registration.EACViews;

@Controller("basicProfileChangePasswordController")
@RequestMapping("profileChangePassword.htm")
public class BasicProfileChangePasswordController {

    /**
     * Setup change password.
     *
     * @param request the request
     * @param response the response
     * @param session the session
     * @return the model and view
     */
    @RequestMapping(method = RequestMethod.GET)
    public final ModelAndView setupChangePassword(final HttpServletRequest request, final HttpServletResponse response, final HttpSession session) {
        Customer customer = SessionHelper.getCustomer(request);
        SessionHelper.setChangeCustomer(request, customer.getUsername());
        SessionHelper.setForwardUrl(session, "profile.htm");
        SessionHelper.setUrlSkin(request, null);
        return new ModelAndView(new RedirectView(EACViews.CHANGE_PASSWORD_VIEW));
    }
}
