package com.oup.eac.web.tags;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;

/**
 * A tag to put the earliest of two Date Times into a scripting variable.
 * 
 * @author David Hay
 * 
 */
public class MinDateTimeTag extends SimpleTagSupport {

    @SuppressWarnings("unused")
    private static final Logger LOG = Logger.getLogger(MinDateTimeTag.class);

    private String var;

    private DateTime dateTime1;
    
    private DateTime dateTime2;
    
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


    public DateTime getDateTime1() {
        return dateTime1;
    }

    public void setDateTime1(DateTime dateTime1) {
        this.dateTime1 = dateTime1;
    }

    public DateTime getDateTime2() {
        return dateTime2;
    }

    public void setDateTime2(DateTime dateTime2) {
        this.dateTime2 = dateTime2;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void doTag() throws JspException {

        PageContext pageContext = (PageContext) getJspContext();
        try {
            DateTime min = this.dateTime1.isBefore(dateTime1) ? dateTime1 : dateTime2;
            pageContext.setAttribute(var, min);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
