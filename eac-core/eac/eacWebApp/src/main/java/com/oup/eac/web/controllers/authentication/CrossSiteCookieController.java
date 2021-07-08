package com.oup.eac.web.controllers.authentication;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import com.oup.eac.web.controllers.helpers.CookieHelper;
import com.oup.eac.web.controllers.helpers.ResponseHelper;

/**
 * Simple controller to return javascript to write an eRights cookie to a secondary domain
 * using the value of the EAC cookie in the primary domain. 
 * 
 * Add the following to your html page to fetch the erights cookie
 * <script type="text/javascript" language="JavaScript" src="http://<host>/eac/crossSiteCookie.htm"></script> 
 * 
 * @author douglass
 *
 */
public class CrossSiteCookieController implements Controller {

    private static final int DEFAULT_CACHE_SECS = 60;
    private static final String DEFAULT_COOKIE_NAME = "EAC_ERIGHTS";
    private int cacheSeconds = DEFAULT_CACHE_SECS;
    private String cookieName = DEFAULT_COOKIE_NAME; 


    /**
     * Constructor
     */
    public CrossSiteCookieController() {
        super();
    }

    /**
     * Constructor
     * @param cacheSeconds
     * @param cookieName
     */
    public CrossSiteCookieController(final int cacheSeconds, final String cookieName) {
        super();
        this.cacheSeconds = cacheSeconds;
        this.cookieName = cookieName;
    }

    /**
     * Get the EAC cookie from the request and return javascript to write an eRights cookie with the session key value
     * 
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
        final Cookie cookie = CookieHelper.getErightsCookie(request);
        String jScript = "document.cookie=\"" + cookieName + "=; expires=Thu, 01-Jan-70 00:00:01 GMT;\n";
        boolean cacheResponse = false;
        if (cookie != null) {
            final String session = cookie.getValue();
            if (StringUtils.isNotBlank(session)) {
                // Send javascript to create an eRights cookie in the local domain
                // the javascript will check and not over write if one already exists
                // containing the same value. Cookies created by the adapter are not over written.                
                jScript = "function eRightsCookie() {\n"
                + " var nameEQ = \"" + cookieName + "=\";\n"
                + " var ca = document.cookie.split(';');\n"
                + " for(var i=0;i < ca.length;i++) {\n"
                + "     var c = ca[i];\n"
                + "     while (c.charAt(0)==' ') c = c.substring(1,c.length);\n"
                + "     if (c.indexOf(nameEQ) == 0) return c.substring(nameEQ.length,c.length);\n"
                + " }\n"
                + " return null;\n"
                + "}\n"
                + "if (!(eRightsCookie() == '" + session + "')) {\n"
                + "    document.cookie=\"" + cookieName + "=" + session + "\";\n"
                + "}\n";
                cacheResponse = true;
            }
        } 
        
        if (cacheResponse) {
            setCaching(response, cacheSeconds);
        } else {
            ResponseHelper.setNoCaching(response);
        }
        response.getWriter().println(jScript);
        return null;
    }

    /**
     * Set caching for configurable time period 
     * @param response http response
     * @param secs seconds to cache
     */
    private void setCaching(final HttpServletResponse response, final int secs) {
        response.setHeader("Cache-Control", "public, max-age=" + secs + ", must-revalidate");
        response.setDateHeader("Expires", System.currentTimeMillis() + (secs * 1000));
        response.setHeader("Pragma", "public");        
        response.setHeader("Last-Modified", "Tue, 19 May 2009 00:05:54 GMT");
    }

    /**
     * Get cache seconds
     * @return
     */
    public final int getCacheSeconds() {
        return cacheSeconds;
    }

    /**
     * Set cache seconds
     * @param cacheSeconds
     */
    public final void setCacheSeconds(final int cacheSeconds) {
        this.cacheSeconds = cacheSeconds;
    }

    /**
     * Get cookie name
     * @return
     */
    public final String getCookieName() {
        return cookieName;
    }

    /**
     * Set cookie name
     * @param cookieName
     */
    public final void setCookieName(final String cookieName) {
        this.cookieName = cookieName;
    }
    
}
