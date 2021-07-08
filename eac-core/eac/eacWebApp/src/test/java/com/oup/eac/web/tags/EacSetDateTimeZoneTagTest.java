package com.oup.eac.web.tags;

import java.util.Set;

import javax.servlet.jsp.JspException;

import org.joda.time.DateTimeZone;
import org.joda.time.contrib.jsptag.DateTimeZoneSupport;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockPageContext;
import org.springframework.mock.web.MockServletContext;

public class EacSetDateTimeZoneTagTest {

    private EacSetDateTimeZoneTag tag;
    private MockServletContext sCtx;
    private MockHttpServletRequest request;
    private MockHttpServletResponse response;
    private MockPageContext ctx;    
    
    
    @Before
    public void setup() {
        sCtx = new MockServletContext();
        ctx = new MockPageContext(sCtx);
        request = new MockHttpServletRequest(sCtx);
        response = new MockHttpServletResponse();
        tag = new EacSetDateTimeZoneTag();
        tag.setPageContext(ctx);        
    }
    
    @Test
    public void testUTC() throws JspException{
        checkTZ("UTC", DateTimeZone.UTC);
    }
    
    @Test
    public void testNull() throws JspException{
        checkTZ(null, DateTimeZone.UTC);
    }
    
    @Test
    public void testUnknown() throws JspException{
        checkTZ("bob", DateTimeZone.UTC);
    }
    
    @Test
    public void testMany() throws JspException{
        @SuppressWarnings("unchecked")
        Set<String> ids = DateTimeZone.getAvailableIDs();
        for(String id : ids){
            checkTZ(id, DateTimeZone.forID(id));
        }
    }
    
    public void checkTZ(String tzId, DateTimeZone tz) throws JspException {
        tag.setValue(tzId);
        tag.doStartTag();
        tag.doEndTag();
        Assert.assertEquals(tz, ctx.getAttribute(DateTimeZoneSupport.FMT_TIME_ZONE+".page"));
    }

}
