package com.oup.eac.ws.filter;

import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

/**
 * Times all requests into the web service layer. The filter 
 * is configured in web.xml of eacWebService.
 * 
 * Only outputs data when debug is enabled.
 * 
 * @author Ian Packard
 *
 */
public class StopWatchFilter implements Filter {
    
    private static final Logger LOGGER = Logger.getLogger(StopWatchFilter.class);

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        long start = System.currentTimeMillis();
        chain.doFilter(request, response);
        if(LOGGER.isDebugEnabled()) {
            LOGGER.debug("Running Time (millis): " + (System.currentTimeMillis() - start));
            outputParameters((HttpServletRequest)request);
        }
    }
    
    @SuppressWarnings("unchecked")
    private void outputParameters(HttpServletRequest httpRequest) {
        StringBuilder stringBuilder = new StringBuilder();
        Enumeration<String> headers = httpRequest.getHeaderNames();
        while (headers.hasMoreElements()) {
            String header = headers.nextElement();
            stringBuilder.append("\nHeader: ").append(header).append(": ").append(httpRequest.getHeader(header));
        }
        Enumeration<String> params = httpRequest.getParameterNames();
        while (params.hasMoreElements()) {
            String param = params.nextElement();
            stringBuilder.append("\nParam: ").append(param).append(": ").append(httpRequest.getParameter(param));
        }
        stringBuilder.append("\nAuth type: ").append(httpRequest.getAuthType());
        stringBuilder.append("\nCharacter encoding: ").append(httpRequest.getCharacterEncoding());
        stringBuilder.append("\nContent length: ").append(httpRequest.getContentLength());
        stringBuilder.append("\nContentType: ").append(httpRequest.getContentType());
        stringBuilder.append("\nContextPath: ").append(httpRequest.getContextPath());
        stringBuilder.append("\nLocalAddr: ").append(httpRequest.getLocalAddr());
        stringBuilder.append("\nLocalName: ").append(httpRequest.getLocalName());
        stringBuilder.append("\nLocalPort: ").append(httpRequest.getLocalPort());
        stringBuilder.append("\nMethod: ").append(httpRequest.getMethod());
        stringBuilder.append("\nPathInfo: ").append(httpRequest.getPathInfo());
        stringBuilder.append("\nProtocol: ").append(httpRequest.getProtocol());
        stringBuilder.append("\nQueryString: ").append(httpRequest.getQueryString());
        stringBuilder.append("\nRemoteAddr: ").append(httpRequest.getRemoteAddr());
        stringBuilder.append("\nRemoteHost: ").append(httpRequest.getRemoteHost());
        stringBuilder.append("\nRemotePort: ").append(httpRequest.getRemotePort());
        stringBuilder.append("\nRequestURI: ").append(httpRequest.getRequestURI());
        stringBuilder.append("\nRequestURL: ").append(httpRequest.getRequestURL());
        stringBuilder.append("\nServerName: ").append(httpRequest.getServerName());
        stringBuilder.append("\nServerPort: ").append(httpRequest.getServerPort());
        stringBuilder.append("\nServletPath: ").append(httpRequest.getServletPath());  
        LOGGER.debug(stringBuilder.toString());
    }

    @Override
    public void init(FilterConfig arg0) throws ServletException {

    }
    
    @Override
    public void destroy() {

    }

}
