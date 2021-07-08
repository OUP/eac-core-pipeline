package com.oup.eac.web.tags;

import javax.servlet.jsp.JspException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockPageContext;
import org.springframework.mock.web.MockServletContext;


public class RequestMethodTagTest {

    private static final String VAR = "varname";

    private RequestMethodTag tag;
    private MockServletContext sCtx;
    private MockHttpServletRequest request;
    private MockHttpServletResponse response;
    private MockPageContext ctx;

    @Before
    public void setup() {
        tag = new RequestMethodTag();
        tag.setVar(VAR);
        sCtx = new MockServletContext();
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
        ctx = new MockPageContext(sCtx, request, response);
        tag.setJspContext(ctx);

        Assert.assertEquals(VAR, tag.getVar());
    }

    @Test
    public void isErrorNoVarSet() throws JspException {
        tag.setVar(null);
        tag.doTag();
        Assert.assertNull(ctx.getAttribute(VAR));
    }

    @Test
    public void isGet() throws JspException {
        request.setMethod("GET");
        tag.setVar(VAR);        
        tag.doTag();
        Assert.assertEquals("GET", ctx.getAttribute(VAR));
    }

    @Test
    public void isPost() throws JspException {
        request.setMethod("POST");
        tag.setVar(VAR);        
        tag.doTag();
        Assert.assertEquals("POST", ctx.getAttribute(VAR));
    }


}
