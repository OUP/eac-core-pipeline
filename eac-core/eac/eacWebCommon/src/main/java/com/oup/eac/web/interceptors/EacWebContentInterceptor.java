package com.oup.eac.web.interceptors;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.web.servlet.mvc.WebContentInterceptor;

public class EacWebContentInterceptor extends WebContentInterceptor {

    private static final Logger LOG = Logger.getLogger(EacWebContentInterceptor.class);

    protected static final String USER_AGENT = "user-agent";
    protected static final String CACHE_CONTROL = "Cache-Control";
    protected static final String CACHE_CONTROL_MSIE_VALUE = "no-cache,private, max-age=0";
    protected static final String INTERNET_EXPLORER_CODE = "MSIE";

    /**
     * {@inheritDoc}
     */

    @Override
    public final boolean preHandle(final HttpServletRequest request, final HttpServletResponse response, final Object handler) throws ServletException {
        String userAgent = request.getHeader(USER_AGENT);
        
        if (StringUtils.isNotBlank(userAgent) && userAgent.contains(INTERNET_EXPLORER_CODE)) {
            
            response.addHeader(CACHE_CONTROL, CACHE_CONTROL_MSIE_VALUE);
            
            if (LOG.isDebugEnabled()) {
                String msg = String.format("request for MSIE - setting [%s] to [%s]", CACHE_CONTROL, CACHE_CONTROL_MSIE_VALUE);
                LOG.debug(msg);
            }
            return true;
        } else {
            return super.preHandle(request, response, handler);
        }

    }

}
