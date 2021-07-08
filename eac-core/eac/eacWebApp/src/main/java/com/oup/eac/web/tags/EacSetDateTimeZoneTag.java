package com.oup.eac.web.tags;

import javax.servlet.jsp.JspTagException;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.joda.time.contrib.jsptag.SetDateTimeZoneTag;

import com.oup.eac.common.date.utils.DateUtils;

public class EacSetDateTimeZoneTag extends SetDateTimeZoneTag {

    private static final Logger LOG = Logger.getLogger(EacSetDateTimeZoneTag.class);
    private static final String UTC_TIMEZONE_ID = null;//null means UTC see code for org.joda.time.contrib.jsptag.SetDateTimeZoneSupport
    /**
     * Sets the value attribute.
     * 
     * @param value  the value
     */
    public void setValue(Object value) throws JspTagException {
        String tzId = (String) value;
        if (StringUtils.isNotBlank(tzId)) {
            if (DateUtils.isProblemWithTimeZoneResourceFile(tzId)) {
                String msg = String.format("Problem loading resources for tzId[%s] will use UTC instead.", tzId);
                LOG.info(msg);
                tzId = UTC_TIMEZONE_ID;
            }
        }
        super.setValue(tzId);
    }
    
 }
