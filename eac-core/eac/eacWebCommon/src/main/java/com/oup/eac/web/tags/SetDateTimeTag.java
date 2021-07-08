package com.oup.eac.web.tags;

import java.util.Locale;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.util.Assert;

/**
 * @author David Hay
 * Used for testing licenceDto.tag to allow testing of all logic paths.
 * 
 */
public class SetDateTimeTag extends SimpleTagSupport {

    private static final String DATE_TIME_FORMAT ="ddMMyyyyHHmmSS";
    
    private static final Logger LOG = Logger.getLogger(SetDateTimeTag.class);

    private String var;

    private String date;
    
    private String time;
    
    protected static final DateTimeFormatter FMT = DateTimeFormat.forPattern(DATE_TIME_FORMAT).withZone(DateTimeZone.UTC).withLocale(Locale.UK);
    
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

    
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void doTag() throws JspException {

        PageContext pageContext = (PageContext) getJspContext();
        DateTime dateTime = getDateTime(this.date, this.time);
        if(dateTime != null){
            pageContext.setAttribute(var, dateTime);
        }
    }
    
    protected static DateTime getDateTime(String date, String time) {
        
        Assert.isTrue(date != null);
        Assert.isTrue(time != null);
        String dateTime = date+time;
        DateTime result = null;
        try {
            
            result = FMT.parseDateTime(dateTime);
        } catch (Exception e) {
            LOG.warn(e.getMessage());
        }
        return result;
    }
    

}
