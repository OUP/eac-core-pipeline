/**
 * 
 */
package com.oup.eac.web.exceptionresolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;

import com.oup.eac.web.controllers.helpers.SessionHelper;

/**
 * @author harlandd
 * 
 */
public class EACExceptionResolver extends SimpleMappingExceptionResolver {

    private static final Logger LOGGER = Logger.getLogger(EACExceptionResolver.class);

    @Override
    public final ModelAndView resolveException(final HttpServletRequest request, final HttpServletResponse response, final Object handler, final Exception ex) {
        SessionHelper.setIsError(request, true);
        LOGGER.error(ex.getMessage(), ex);
        return super.resolveException(request, response, handler, ex);
    }
}
