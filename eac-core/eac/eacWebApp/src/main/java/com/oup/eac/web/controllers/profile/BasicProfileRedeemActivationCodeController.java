package com.oup.eac.web.controllers.profile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.oup.eac.web.controllers.helpers.SessionHelper;
import com.oup.eac.web.controllers.registration.EACViews;

@Controller("basicProfileRedeemActivationCode")
@RequestMapping("profileRedeemActivationCode.htm")
public class BasicProfileRedeemActivationCodeController {

    /**
     * Setup change password.
     *
     * @param request the request
     * @param response the response
     * @param session the session
     * @return the model and view
     */
    @RequestMapping(method = RequestMethod.GET)
    public final ModelAndView setupRedeemActivationCode(final HttpServletRequest request, final HttpServletResponse response, final HttpSession session) {               
        SessionHelper.setRegisterableProduct(session, null);
        SessionHelper.setForwardUrl(session, "profile.htm");
        SessionHelper.setProductRegistrationDefinition(request, null);
        SessionHelper.setUrlSkin(request, null);
        return new ModelAndView(new RedirectView(EACViews.DIRECT_ACTIVATION_CODE_VIEW));
    }
}
