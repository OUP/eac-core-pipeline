package com.oup.eac.accounts.controllers;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.oup.eac.domain.UrlSkin;
import com.oup.eac.service.CustomerService;
import com.oup.eac.service.ServiceLayerException;
import com.oup.eac.web.controllers.helpers.CookieHelper;
import com.oup.eac.web.controllers.helpers.SessionHelper;

/**
 * Spring MVC Controller for Logout.  
 * @author Gaurav Soni
 */

@Controller
public class AccountsLogoutController {

    private static final Logger LOG = Logger.getLogger(AccountsLogoutController.class);
    private final CustomerService customerService;
    private final String SKIN_URL = "SKIN_URL";

    @Autowired
    public AccountsLogoutController(CustomerService customerService) {
        super();
        this.customerService = customerService;
    }
    
    @RequestMapping(value = { "/logout" }, method=RequestMethod.GET)
    public ModelAndView showForm(final HttpServletRequest request, final HttpServletResponse response) {
        UrlSkin skin = SessionHelper.getUrlSkin(request);
        String requestedSiteUrl = null;
        if(skin != null){
            requestedSiteUrl = skin.getUrl();
            SessionHelper.setUrlSkin(request, null);
        }
        
        Cookie cookie = CookieHelper.getErightsCookie(request);
        if (cookie != null) {
            String session = cookie.getValue();
            LOG.debug("Cookie found for logout. The erights session to logout is = " + session);
            if (StringUtils.isNotBlank(session)) {
                try {
                    customerService.logout(SessionHelper.getCustomer(request), session);
                } catch (ServiceLayerException e) {
                    LOG.debug(e.getMessage());
                }
            }
            CookieHelper.invalidateErightsCookie(response);
            SessionHelper.killSession(request);
        }
        
        if(!StringUtils.isBlank(requestedSiteUrl)){
            return new ModelAndView("redirect:/login.htm?"+SKIN_URL+"="+requestedSiteUrl);
        }
        
        return new ModelAndView("redirect:/login.htm");
    }
}
