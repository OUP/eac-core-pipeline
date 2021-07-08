package com.oup.eac.web.controllers.helpers;

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.util.Assert;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;

/**
 * @author harlandd Request helper class
 */
public final class RequestHelper {

    private static final Logger LOGGER = Logger.getLogger(RequestHelper.class);

    private static final int DEFAULT_PORT = 80;
    private static final int SECURE_PORT = 443;
    private static final String SHOWING_PRODUCT_REGISTRATION_PAGE_NAME = "showingProductRegistrationPage";
    private static final String PRODUCT_REGISTRATION_FORM = "productRegistrationForm";
    
    /**
     * Private default constructor.
     */
    private RequestHelper() {

    }

    /**
     * @param request
     *            the request
     */
    public static void logParams(final HttpServletRequest request) {
        String params = getParams(request);
        LOGGER.info(params);
    }

    /**
     * Gets the params.
     *
     * @param request the request
     * @return the params
     */
    public static String getParams(final HttpServletRequest request) {
        StringBuilder stringBuilder = new StringBuilder();
        Enumeration<String> headers = request.getHeaderNames();
        while (headers.hasMoreElements()) {
            if (stringBuilder.length() > 0) {
                stringBuilder.append("\n");
            }
            String header = headers.nextElement();
            stringBuilder.append("Header: " + header + ": " + request.getHeader(header));
        }
        @SuppressWarnings("unchecked")
        Enumeration<String> params = request.getHeaderNames();
        while (params.hasMoreElements()) {
            if (stringBuilder.length() > 0) {
                stringBuilder.append("\n");
            }
            String param = params.nextElement();
            stringBuilder.append("Param: " + param + ": " + request.getHeader(param));
        }
        stringBuilder.append("\nAuth type: " + request.getAuthType());
        stringBuilder.append("\nCharacter encoding: " + request.getCharacterEncoding());
        stringBuilder.append("\nContent length: " + request.getContentLength());
        stringBuilder.append("\ncontentType: " + request.getContentType());
        stringBuilder.append("\ncontextPath: " + request.getContextPath());
        stringBuilder.append("\nlocalAddr: " + request.getLocalAddr());
        stringBuilder.append("\nlocalName: " + request.getLocalName());
        stringBuilder.append("\nlocalPort: " + request.getLocalPort());
        stringBuilder.append("\nmethod: " + request.getMethod());
        stringBuilder.append("\npathInfo: " + request.getPathInfo());
        stringBuilder.append("\nprotocol: " + request.getProtocol());
        stringBuilder.append("\nqueryString: " + request.getQueryString());
        stringBuilder.append("\nremoteAddr: " + request.getRemoteAddr());
        stringBuilder.append("\nremoteHost: " + request.getRemoteHost());
        stringBuilder.append("\nremotePort: " + request.getRemotePort());
        stringBuilder.append("\nrequestURI: " + request.getRequestURI());
        stringBuilder.append("\nrequestURL: " + request.getRequestURL());
        stringBuilder.append("\nserverName: " + request.getServerName());
        stringBuilder.append("\nserverPort: " + request.getServerPort());
        stringBuilder.append("\nservletPath: " + request.getServletPath());
        return stringBuilder.toString();
    }

    /**
     * Gets the url prefix.
     *
     * @param request the request
     * @return the url prefix
     */
    public static String getUrlPrefix(final HttpServletRequest request) {
        int port = request.getServerPort();
        String serverName = request.getServerName();
        String protocol = request.getScheme();
        String portSuffix = String.format(":%d", port);
        if (port == DEFAULT_PORT) {
            portSuffix = "";
        } else if (port == SECURE_PORT && "https".equals(protocol)) {
            portSuffix = "";
        }

        String contextRoot = request.getContextPath();
        if ("/".equals(contextRoot)) {
            contextRoot = "";
        }
        String prefix = String.format("%s://%s%s%s", protocol, serverName, portSuffix, contextRoot);
        Assert.isTrue(!prefix.endsWith("/"), "Problem with urlPrefix generation");
        return prefix;
    }

    /**
     * Checks if the request is a GET request.
     * 
     * @param request
     *            the request
     * @return true, if the request is a GET request
     */
    public static boolean isGetRequest(final HttpServletRequest request) {
        return "GET".equals(request.getMethod());
    }

    /**
     * Checks if the request is a POST request.
     * 
     * @param request
     *            the request
     * @return true, if the request is a POST request
     */
    public static boolean isPostRequest(final HttpServletRequest request) {
        return "POST".equals(request.getMethod());
    }
    
    /**
     * Checks if is showing product registration page.
     *
     * @param request the request
     * @return true, if is showing product registration page
     */
    public static boolean isShowingProductRegistrationPage(final HttpServletRequest request){
        boolean result = (Boolean) request.getAttribute(SHOWING_PRODUCT_REGISTRATION_PAGE_NAME);
        return result;
    }
    
    /**
     * Sets the showing product registration page.
     *
     * @param modelAndView the model and view
     * @param request the request
     */
    public static void setShowingProductRegistrationPage(final ModelAndView modelAndView, final HttpServletRequest request) {
        String viewName;
       
        View view = modelAndView.getView();
        if (view != null) {
            viewName = view.getContentType();
        } else {
            viewName = modelAndView.getViewName();            
        }
        if (PRODUCT_REGISTRATION_FORM.equals(viewName)) {
            request.setAttribute(SHOWING_PRODUCT_REGISTRATION_PAGE_NAME, Boolean.valueOf(true));        
        }
    }
}
