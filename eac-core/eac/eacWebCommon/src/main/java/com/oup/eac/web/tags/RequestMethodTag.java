package com.oup.eac.web.tags;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import org.apache.log4j.Logger;

/**
 * A tag to put the request method into a scripting variable.
 * 
 * @author David Hay
 * 
 */
public class RequestMethodTag extends SimpleTagSupport {

    private static final Logger LOG = Logger.getLogger(RequestMethodTag.class);

    private String var;

    /**
     * @return the var
     */
    public final String getVar() {
        return var;
    }

    /**
     * @param varP
     *            the var to set
     */
    public final void setVar(final String varP) {
        this.var = varP;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void doTag() throws JspException {

        PageContext pageContext = (PageContext) getJspContext();

        try {
            HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
            String method = request.getMethod();
            if (LOG.isDebugEnabled()) {
                String msg = String.format("Setting pageContext variable [%s] to [%s]", var, method);
                LOG.debug(msg);
            }
            pageContext.setAttribute(var, request.getMethod());

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
