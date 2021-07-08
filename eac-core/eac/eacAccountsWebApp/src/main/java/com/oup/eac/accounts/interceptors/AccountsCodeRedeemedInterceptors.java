package com.oup.eac.accounts.interceptors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.oup.eac.domain.ActivationCode;
import com.oup.eac.domain.RegisterableProduct;
import com.oup.eac.web.controllers.helpers.SessionHelper;

/**
 * Spring Interceptor to check availability of code and product after code redemption.  
 * @author Gaurav Soni
 */

@Component
public class AccountsCodeRedeemedInterceptors extends HandlerInterceptorAdapter {

    private final String ACTIVATION_CODE_FORM_VIEW = "activationCode.htm";
    
    @Override
    public final boolean preHandle(final HttpServletRequest request, final HttpServletResponse response, final Object handler) throws Exception{
        
        ActivationCode activationCode = SessionHelper.getActivationCode(request);
        if(activationCode == null){
            response.sendRedirect(createRedirectUrl(request));
            return false;
        }
        RegisterableProduct regProduct = SessionHelper.getRegisterableProduct(request);
        if(regProduct == null){
            response.sendRedirect(createRedirectUrl(request));
            return false;
        }
        return true;
    }
    
    /**
     * Creates the redirect url.
     *
     * @param request the request
     * @return the string
     */
    private String createRedirectUrl(final HttpServletRequest request) {
        String resource = ACTIVATION_CODE_FORM_VIEW;
        String result = String.format("%s/%s", request.getContextPath(), resource);
        return result;
    }
}
