package com.oup.eac.web.interceptors;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.oup.eac.domain.EacLang;

/**
 * An interceptor that exposes the languages available for the request.
 * 
 * @author packardi
 * 
 */
public class AvailableLanguagesInterceptor extends HandlerInterceptorAdapter {

    private final List<EacLang> availableLanguages;

    /**
     * Instantiates a new available languages interceptor.
     * 
     * @param availableLanguagesP
     *            the available languages
     */
    public AvailableLanguagesInterceptor(final List<EacLang> availableLanguagesP) {
        this.availableLanguages = availableLanguagesP;
    }

    @Override
    public final void postHandle(final HttpServletRequest request, final HttpServletResponse response, final Object handler, final ModelAndView modelAndView)
            throws Exception {
        request.setAttribute("availableLanguages", availableLanguages);
        super.postHandle(request, response, handler, modelAndView);
    }
}
