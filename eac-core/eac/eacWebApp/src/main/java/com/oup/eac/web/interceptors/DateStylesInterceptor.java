package com.oup.eac.web.interceptors;

import java.beans.ConstructorProperties;
import java.util.TimeZone;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.servlet.view.RedirectView;

import com.oup.eac.domain.Customer;
import com.oup.eac.web.controllers.helpers.SessionHelper;

/**
 * Makes date and dateTime Style Styles in JSPs for joda time jsp tags.
 * 
 * @author David Hay
 * 
 */
public class DateStylesInterceptor extends HandlerInterceptorAdapter {

    private static final Logger LOG = Logger.getLogger(DateStylesInterceptor.class);

    public static final String KEY_DATE_STYLE = "dateStyle";
    public static final String KEY_DATE_TIME_STYLE = "dateTimeStyle";
    public static final String KEY_TIME_ZONE = "timeZone";
    
    private final String dateStyle;
    
    private final String dateTimeStyle;
    
   
    /**
     * Instantiates a new date Styles interceptor.
     *
     * @param dateTimeStyle the date time Style
     * @param dateStyle the date Style
     */
    @ConstructorProperties({"dateTimeStyle", "dateStyle" })
    public DateStylesInterceptor(final String dateTimeStyle, final String dateStyle) {
        this.dateStyle = dateStyle;
        this.dateTimeStyle = dateTimeStyle;        
        LOG.info("DateTime Style : " + dateTimeStyle);
        LOG.info("Date Style : " + dateStyle);
    }

    
    /**
     * {@inheritDoc}
     */
    @Override
    public final void postHandle(
            final HttpServletRequest request, final HttpServletResponse response, final Object handler, final ModelAndView modelAndView)
			throws Exception {
		boolean isRedirect = false;
		if (modelAndView != null) {
			View view = modelAndView.getView();
			if (view != null) {
				if (view instanceof RedirectView) {
					isRedirect = true;
				}
			} else {
				String viewName = modelAndView.getViewName();
				if (StringUtils.isNotBlank(viewName)) {
					isRedirect = viewName.startsWith("redirect:");
				}
			}
		}
		if (!isRedirect) {
			request.setAttribute(KEY_DATE_STYLE, dateStyle);
			request.setAttribute(KEY_DATE_TIME_STYLE, dateTimeStyle);
			
			//timezone
			String tz = TimeZone.getDefault().getDisplayName();
			Customer customer = SessionHelper.getCustomer(request);
			if(customer != null){
				String temp = customer.getTimeZone();
				if(StringUtils.isNotBlank(temp)){
					tz = temp;
				}
			}
			request.setAttribute(KEY_TIME_ZONE, tz);
			LOG.debug("Setting TimeZone for JSPS to " + tz);
		}
	}

}
