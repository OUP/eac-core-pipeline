package com.oup.eac.web.tags;

import javax.servlet.jsp.tagext.BodyContent;
import javax.servlet.jsp.tagext.BodyTagSupport;

import org.apache.log4j.Logger;

/**
 * A tag to put the body of the tag into a scripting variable.
 * 
 * @author David Hay
 * 
 */
public class BodyCaptureTag extends BodyTagSupport {

    @SuppressWarnings("unused")
    private static final Logger LOG = Logger.getLogger(BodyCaptureTag.class);

    protected BodyContent bodyContent;

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

    public int doStartTag() {
        return EVAL_BODY_BUFFERED;
    }
    
    public int doEndTag() {
        try {
            bodyContent = getBodyContent();
            String body = bodyContent.getString();            
            pageContext.setAttribute(var, body);
            pageContext.getRequest().setAttribute(var, body);
        } catch (Exception e) {
            LOG.error("unexpected exception",e);
        }
        return EVAL_PAGE;
    }
    
}
