package com.oup.eac.ws.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.Filter;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

/**
 * Filter to catch any uncaught exceptions and return soap fault.
 * 
 * Configured in web.xml of eacWebService. 
 * 
 */
public class ExceptionFaultFilter implements Filter {
    private static final Logger LOG = Logger.getLogger(ExceptionFaultFilter.class);

    /**
     * @param request
     *            request
     * @param response
     *            response
     * @param chain
     *            chain
     * @throws IOException
     *             IOException
     * @throws ServletException
     *             ServletException
     * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest, javax.servlet.ServletResponse, javax.servlet.FilterChain)
     */
    @Override
    public final void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain chain) throws IOException, ServletException {
        try {
            chain.doFilter(request, response);
        } catch (Exception e) {
            // Exceptions should be caught by Exception Resolvers
            // However exceptions can occur earlier in the marshaling so need to
            // be caught
            LOG.error("Error occured handing web service request", e);
            ((HttpServletResponse) response).setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            ServletOutputStream out = response.getOutputStream();
            StringBuilder sb = new StringBuilder();
            sb.append("<SOAP-ENV:Envelope xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\">");
            sb.append("<SOAP-ENV:Header/>");
            sb.append("<SOAP-ENV:Body>");
            sb.append("<SOAP-ENV:Fault>");
            sb.append("<faultcode>SOAP-ENV:Client</faultcode>");
            sb.append("<faultstring xml:lang=\"en\">");
            sb.append(e.getMessage());
            sb.append("</faultstring>");
            sb.append("</SOAP-ENV:Fault>");
            sb.append("</SOAP-ENV:Body>");
            sb.append("</SOAP-ENV:Envelope>");
            out.println(sb.toString());
            out.close();
        }
    }

    @Override
    public void destroy() {

    }

    @Override
    public void init(final FilterConfig arg0) throws ServletException {

    }
}
