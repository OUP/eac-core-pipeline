package com.oup.eac.web.interceptors;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.LocaleUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

import com.oup.eac.web.controllers.helpers.RequestHelper;
import com.oup.eac.web.controllers.helpers.SessionHelper;

/**
 * Similar to Spring's LocaleChangeInterceptor but with a few improvements.
 * 
 * Only considers GET requests.
 * 
 * Better locale validation - ignoring clearly invalid locales.
 * 
 * Also has the ability to reset locale to that of browser.
 * 
 * @see LocaleChangeInterceptor
 * @author David Hay
 * 
 */
public class LocaleInterceptor extends HandlerInterceptorAdapter {

    private static final Logger LOG = Logger.getLogger(LocaleInterceptor.class);

    public static final String LOCALE_EL_ATTRIBUTE_NAME = "locale";
    
    public static final String DEFAULT_PARAM_NAME = "locale";

    private String paramName = DEFAULT_PARAM_NAME;

    /**
     * {@inheritDoc}
     */
    @Override
    public final boolean preHandle(final HttpServletRequest request, final HttpServletResponse response, final Object handler) throws Exception {
        if (RequestHelper.isGetRequest(request)) {
            String localeParam = request.getParameter(paramName);
            if (StringUtils.isNotBlank(localeParam)) {
                try {
                    Locale locale = LocaleUtils.toLocale(localeParam);
                    boolean isValid = com.oup.eac.common.utils.lang.LocaleUtils.isValid(locale);
                    if (isValid) {
                        SessionHelper.setLocale(request, response, locale);
                    } else {
                        String msg = String.format("The Locale  [%s] is not valid", locale.toString());
                        LOG.warn(msg);
                    }
                } catch (IllegalArgumentException ex) {
                    String msg = String.format("Cannot create Locale from [%s]", localeParam);
                    LOG.warn(msg);
                } 
            } else {
              	// reset the locale to that supplied by browser
              	if (request.getParameterMap().containsKey(paramName)) {
                   	SessionHelper.setLocale(request, response, null);
               	}
            }
        }
        return true;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public final void postHandle(
            final HttpServletRequest request, final HttpServletResponse response, final Object handler, final ModelAndView modelAndView)
            throws Exception {
        Locale locale = SessionHelper.getLocale(request);
        request.setAttribute(LOCALE_EL_ATTRIBUTE_NAME, locale);
    }


    /**
     * Gets the param name.
     * 
     * @return the param name
     */
    public final String getParamName() {
        return paramName;
    }

    /**
     * Sets the param name.
     * 
     * @param paramNameP
     *            the new param name
     */
    public final void setParamName(final String paramNameP) {
        this.paramName = paramNameP;
    }

}
