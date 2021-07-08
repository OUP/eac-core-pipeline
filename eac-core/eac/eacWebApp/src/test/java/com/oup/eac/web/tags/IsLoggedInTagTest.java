package com.oup.eac.web.tags;

import java.util.UUID;

import javax.servlet.jsp.JspException;

import org.easymock.EasyMock;
import org.joda.time.DateTime;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockPageContext;
import org.springframework.mock.web.MockServletContext;

import com.oup.eac.domain.Customer;
import com.oup.eac.web.controllers.helpers.SessionHelper;

@RunWith(PowerMockRunner.class)
//@PrepareForTest(Customer.class)
public class IsLoggedInTagTest {

    private static final String VAR = "varname";

    private IsLoggedInTag tag;
    private MockServletContext sCtx;
    private MockHttpServletRequest request;
    private MockHttpServletResponse response;
    private MockPageContext ctx;
    private Customer mockCustomer;

    @Before
    public void setup() {
        tag = new IsLoggedInTag();
        tag.setVar(VAR);
        sCtx = new MockServletContext();
        request = new MockHttpServletRequest();

        response = new MockHttpServletResponse();
        ctx = new MockPageContext(sCtx, request, response);
        tag.setJspContext(ctx);

        mockCustomer = new Customer();
        mockCustomer.setId(UUID.randomUUID().toString());
        mockCustomer.setCreatedDate(new DateTime());
        //PowerMock.createPartialMock(Customer.class, "getCreatedDate");
        Assert.assertEquals(VAR, tag.getVar());
    }

    @Test
    public void isLoggedInNoVarSet() throws JspException {
        tag.setVar(null);
        tag.doTag();
        Assert.assertNull(request.getAttribute(VAR));
    }

    @Test
    public void isLoggedInNo1() throws JspException {
        SessionHelper.setCustomer(request, null);
        tag.doTag();
        Assert.assertEquals(Boolean.FALSE, request.getAttribute(VAR));
    }

    @Test
    public void isLoggedInNo2() throws JspException {
        Customer customer = new Customer();
        SessionHelper.setCustomer(request, customer);
        tag.doTag();
        Assert.assertEquals(Boolean.FALSE, request.getAttribute(VAR));
    }

    @Test
    public void isLoggedInYes() throws JspException {
        Customer customer = new Customer();
        customer.setCreatedDate(new DateTime());
        SessionHelper.setCustomer(request, customer);
        tag.doTag();
        Assert.assertEquals(Boolean.TRUE, request.getAttribute(VAR));
    }

    @Test(expected = RuntimeException.class)
    public void isLoggedInError() throws JspException {
        try {
            SessionHelper.setCustomer(request, mockCustomer);
            EasyMock.expect(mockCustomer.getCreatedDate()).andThrow(new RuntimeException("oops"));
            PowerMock.replay(mockCustomer);
            tag.doTag();
        } finally {            
            PowerMock.verify(mockCustomer);
        }
    }

}
