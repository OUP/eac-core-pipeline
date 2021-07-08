package com.oup.eac.web.controllers.profile;

import javax.naming.NamingException;
import javax.servlet.http.HttpSession;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.view.RedirectView;

import com.oup.eac.domain.Customer;
import com.oup.eac.web.controllers.helpers.SessionHelper;
import com.oup.eac.web.controllers.registration.EACViews;

public class BasicProfileChangePasswordControllerTest {

    private static final String USERNAME = "bobbuilder";

    private BasicProfileChangePasswordController controller;
    private Customer customer;
    private MockHttpServletRequest request;
    private MockHttpServletResponse response;
    private HttpSession session;

    public BasicProfileChangePasswordControllerTest() throws NamingException {
        super();
    }

    @Before
    public void setup() {
        controller = new BasicProfileChangePasswordController();
        customer = new Customer();
        request = new MockHttpServletRequest();
        session = new MockHttpSession();
        response = new MockHttpServletResponse();
        request.setSession(session);
        customer.setUsername(USERNAME);
        SessionHelper.setCustomer(request, customer);
    }

    @Test
    public void testController() {
        ModelAndView result = controller.setupChangePassword(request, response, session);

        Assert.assertEquals(USERNAME, SessionHelper.getChangeCustomer(request));
        Assert.assertEquals(SessionHelper.getForwardUrl(request), "profile.htm");
        View view = result.getView();
        Assert.assertTrue(view instanceof RedirectView);
        RedirectView rv = (RedirectView) view;
        Assert.assertEquals(EACViews.CHANGE_PASSWORD_VIEW, rv.getUrl());

    }

}
