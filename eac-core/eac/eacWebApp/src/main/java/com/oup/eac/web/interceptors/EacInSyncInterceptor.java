package com.oup.eac.web.interceptors;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.oup.eac.web.controllers.helpers.RequestHelper;
import com.oup.eac.web.controllers.helpers.SessionHelper;

/**
 * The Class EacInSyncInterceptor.
 * 
 * @author David Hay
 * 
 */
public class EacInSyncInterceptor extends HandlerInterceptorAdapter {

    private static final Logger LOG = Logger.getLogger(EacInSyncInterceptor.class);

    protected static final String OUT_OF_SYNCH_ERROR_JSP = "/WEB-INF/jsp/outOfSyncError.jsp";

    private static final String FRESH_FORM_URL_ATTR_NAME = "freshFormURL";

    private List<String> ignoredPaths = new ArrayList<String>();
    
    private List<String> skipTokenRegenerationPaths = new ArrayList<String>();

    private boolean enabled;

    @Override
    public final boolean preHandle(final HttpServletRequest request, final HttpServletResponse response, final Object handler) throws Exception {
        String url = request.getRequestURI();
        if (!canProceedWithSynchCheck(request, url)) {
            return true;
        }
        // only get here if POSTing and this interceptor is 'enabled' and the path is not ignored.
        HttpSession session = request.getSession();

        String current = SessionHelper.getInSyncToken(session);
        String submitted = SessionHelper.getSubmittedEacInSyncToken(request);

        boolean inSync = current != null && (current.equals(submitted));

        if (LOG.isInfoEnabled()) {
            String msg = String.format("check for [%s] is [%b]", session.getId(), inSync);
            LOG.info(msg);
        }

        if (inSync) {
            return true;
        }
        
        rejectPost(request, response, url);
        return false;
    }

    @Override
    public final void postHandle(final HttpServletRequest request, final HttpServletResponse response, final Object handler, final ModelAndView modelAndView)
            throws Exception {
    	if (!shouldSkipTokenRegeneration(request.getRequestURI())) {
    		SessionHelper.setEacInSyncToken(request.getSession());
    	}
    }
    
    /**
     * Can proceed.
     * 
     * @param request
     *            the request
     * @param url
     *            the url
     * @return true, if successful
     */
    private boolean canProceedWithSynchCheck(final HttpServletRequest request, final String url) {
        if (!enabled) {
            if (LOG.isTraceEnabled()) {
                LOG.trace("EacInSyncInterceptor disabled");
            }
            return false;
        }
        if (!RequestHelper.isPostRequest(request)) {
            if (LOG.isTraceEnabled()) {
                LOG.trace("EacInSyncInterceptor does not handle [" + request.getMethod() + "] requests.");
            }
            return false;
        }
        if (ignoredPaths != null) {

            for (String ignorePath : ignoredPaths) {
                if (url.endsWith(ignorePath)) {
                    if (LOG.isTraceEnabled()) {
                        LOG.trace("EacInSyncInterceptor ignoring POST requests to  [" + url + "]");
                    }
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Reject post.
     * 
     * @param request
     *            the request
     * @param response
     *            the response
     * @param url
     *            the url
     * @throws ServletException
     *             the servlet exception
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    private void rejectPost(final HttpServletRequest request, final HttpServletResponse response, final String url) throws ServletException, IOException {
        String queryString = request.getQueryString();
        String refreshUrl = url;
        if (queryString != null && StringUtils.isNotBlank(queryString)) {
            refreshUrl = url + "?" + queryString;
        }
        LOG.warn("Form Submission is out of sync - will take user back to  : [" + refreshUrl + "]");
        request.setAttribute(FRESH_FORM_URL_ATTR_NAME, refreshUrl);
        RequestDispatcher dispatcher = request.getRequestDispatcher(OUT_OF_SYNCH_ERROR_JSP);
        dispatcher.forward(request, response);
    }
    
    private boolean shouldSkipTokenRegeneration(final String url) {
    	boolean shouldSkip = false;
    	for (String ignoredPath : skipTokenRegenerationPaths) {
    		if (url.endsWith(ignoredPath)) {
    			shouldSkip = true;
    			break;
    		}
    	}
    	return shouldSkip;
    }

    /**
     * Gets the ignored paths.
     * 
     * @return the ignored paths
     */
    public final List<String> getIgnoredPaths() {
        return ignoredPaths;
    }
    
    /**
     * Sets the ignored paths.
     * 
     * @param ignoredPaths
     *            the new ignored paths
     */
    public final void setIgnoredPaths(final List<String> ignoredPaths) {
        this.ignoredPaths = ignoredPaths;
    }

	/**
	 * Sets a {@link List} of paths that will not regenerate the insync token. If a request matches a path specified in
	 * this list, then a new token will not be generated for this request. Any previous token will continue to be used
	 * (will not be overwritten).
	 * 
	 * @param skipTokenRegenerationPaths
	 *            A List of paths which will not regenerate the insync token.
	 */
	public void setSkipTokenRegenerationPaths(List<String> skipTokenRegenerationPaths) {
		this.skipTokenRegenerationPaths = skipTokenRegenerationPaths;
	}

	/**
     * Checks if is enabled.
     * 
     * @return true, if is enabled
     */
    public final boolean isEnabled() {
        return enabled;
    }

    /**
     * Sets the enabled.
     * 
     * @param enabled
     *            the new enabled
     */
    public final void setEnabled(final boolean enabled) {
        this.enabled = enabled;
    }

}
