/**
 * 
 */
package com.oup.eac.web.controllers.authentication;

import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.util.Assert;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;
import org.springframework.web.servlet.view.RedirectView;

import com.oup.eac.common.utils.EACSettings;
import com.oup.eac.common.utils.url.URLUtils;
import com.oup.eac.service.CustomerService;
import com.oup.eac.service.ServiceLayerException;
import com.oup.eac.service.WhiteListUrlService;
import com.oup.eac.web.controllers.helpers.CookieHelper;
import com.oup.eac.web.controllers.helpers.SessionHelper;

/**
 * @author harlandd Logout controller. SSO logout for erights users.
 */
public class LogoutController implements Controller {

    private static final Logger LOG = Logger.getLogger(LogoutController.class);

    private final CustomerService customerService; 
    private final WhiteListUrlService whiteListUrlService;

    /**
     * @param customerService
     *            the customerService
     */
    public LogoutController(final CustomerService customerService,
    		final WhiteListUrlService whiteListUrlService) {
        super();
        Assert.notNull(customerService);
        this.customerService = customerService;
        this.whiteListUrlService = whiteListUrlService;
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
    @Override
    public final ModelAndView handleRequest(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        String userRequestedUrl = request.getParameter(EACSettings.getProperty(EACSettings.ERIGHTS_URL_PARAMETER_NAME));
        List<String> urls = whiteListUrlService.getUrls();
        
        if (StringUtils.isNotBlank(userRequestedUrl) 
        		&& URLUtils.checkValidUrl(userRequestedUrl, urls)) { 
        	SessionHelper.setForwardUrl(request.getSession(), userRequestedUrl);
        }

        String redirectUrl = SessionHelper.getForwardUrl(request.getSession(), false);
        LOG.debug("Executing logout using redirectUrl = " + redirectUrl);
        String redirectLocaleUrl=SessionHelper.getLocaleForwardUrl(redirectUrl, request);
        LOG.debug("Executing logout using redirectLocalUrl = " + redirectLocaleUrl);

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

        return new ModelAndView(new RedirectView(redirectLocaleUrl));
    }

}
