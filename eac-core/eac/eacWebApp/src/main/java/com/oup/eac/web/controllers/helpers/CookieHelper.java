package com.oup.eac.web.controllers.helpers;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

/**
 * @author harlandd Cookie helper class
 */
public final class CookieHelper {
    @SuppressWarnings("unused")
    private static final Logger LOG = Logger.getLogger(CookieHelper.class);

    private static final String ERIGHTS_COOKIE = "EAC";
    private static final String TEST_COOKIE = "TEST_COOKIE";
    private static final int SESCONDS_IN_DAY = 60 * 60 * 24;

    /**
     * Private default constructor.
     */
    private CookieHelper() {

    }

    /**
     * @param req
     *            the request
     * @return the erights cookie if it exists otherwise null
     */
    public static Cookie getErightsCookie(final HttpServletRequest req) {
        Cookie cookie = getCookie(req, ERIGHTS_COOKIE);
        return cookie;
    }
    
    /**
     * @param req
     *            the request
     * @return the erights cookie if it exists otherwise null
     */
    private static Cookie getCookie(final HttpServletRequest req, final String cookieName) {
        final Cookie[] cookies = req.getCookies();
        if (cookies != null && cookies.length > 0) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(cookieName)) {
                    return cookie;
                }
            }
        }
        return null;
    }

    /**
     * @param response
     *            the response
     */
    public static void invalidateErightsCookie(final HttpServletResponse response) {
        final Cookie cookie = new Cookie(ERIGHTS_COOKIE, "");
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    /**
     * @param response
     *            the response
     * @param value
     *            the value to be set in the erights cookie. The age is made to be longer than the session
     */
    public static void setTestCookie(final HttpServletResponse response, final String value) {
        final Cookie cookie = new Cookie(TEST_COOKIE, value);
        cookie.setPath("/");
        cookie.setMaxAge(SESCONDS_IN_DAY);
        response.addCookie(cookie);
    }    
    
    /**
     * @param response
     *            the response
     * @param value
     *            the value to be set in the erights cookie. The age is made to be longer than the session
     */
    public static Cookie getTestCookie(final HttpServletRequest req) {
        return getCookie(req, TEST_COOKIE);
    }   
    
    /**
     * @param response
     *            the response
     * @param value
     *            the value to be set in the erights cookie. The age is made to be longer than the session
     */
    public static void setErightsCookie(final HttpServletResponse response, final String value) {
        response.addCookie(createErightsCookie(value));
        
    }
    
    public static Cookie createErightsCookie(final String value) {
    	final Cookie cookie = new Cookie(ERIGHTS_COOKIE, value);
        cookie.setPath("/");
        cookie.setHttpOnly(true) ;
        cookie.setMaxAge(SESCONDS_IN_DAY);
        cookie.setSecure(true);
        return cookie;
    }

    /**
     * Reset cookie age on response.
     * 
     * @param response
     *            response
     * @param cookie
     *            cookie
     */
    public static void resetErightsCookieMaxAge(final HttpServletResponse response, final Cookie cookie) {
        cookie.setMaxAge(SESCONDS_IN_DAY);
        response.addCookie(cookie);
    }
}
