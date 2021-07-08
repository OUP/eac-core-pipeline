package com.oup.eac.web.tags;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;

/**
 * A tag to create a boolean scripting variable depending on whether a date is in the future
 * 
 * @author David Hay
 * 
 */
public class DateTimeInFutureTag extends SimpleTagSupport {

    @SuppressWarnings("unused")
    private static final Logger LOG = Logger.getLogger(DateTimeInFutureTag.class);

    private DateTime dateTime;

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

    public DateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(DateTime dateTime) {
        this.dateTime = dateTime;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void doTag() throws JspException {

        PageContext pageContext = (PageContext) getJspContext();
        try {
            boolean inFuture = this.dateTime != null && this.dateTime.isAfter(new DateTime());
            pageContext.setAttribute(var, inFuture);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    
}
