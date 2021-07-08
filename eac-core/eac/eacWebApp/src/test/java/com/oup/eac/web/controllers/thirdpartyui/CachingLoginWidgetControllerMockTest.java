package com.oup.eac.web.controllers.thirdpartyui;

import javax.naming.NamingException;
import javax.servlet.http.Cookie;

import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.web.servlet.ModelAndView;

import com.oup.eac.common.mock.AbstractMockTest;
import com.oup.eac.domain.Customer;
import com.oup.eac.domain.Customer.CustomerType;
import com.oup.eac.dto.WebCustomerDto;
import com.oup.eac.service.exceptions.CustomerNotFoundServiceLayerException;
import com.oup.eac.web.controllers.helpers.SessionHelper;

public class CachingLoginWidgetControllerMockTest extends AbstractMockTest {

    private static final String CUSTOMER_USER_NAME = "bobbuilder";

    private static final String CUSTOMER_USER_ID = "customerId1";

    private static final String SUCCESS_URL = "http://www.ora.com";

    private static final String LOGOUT_URL = "http://www.oup.com";

    private static final String ERROR_URL = "http://www.google.com";

    private static final String EXPECTED_URL_PREFIX = "http://eac.mock.host/eac";

    private static final String SESSION_KEY = "sessionKey123";

    private static final String INVALID_SESSION_KEY = "sessionKey1234";

    private static final String EAC = "EAC";

    private CachingLoginWidgetController controller;

    private Customer customer;

    private LoginWidgetCustomerLookup loginWidgetCustomerLookup;

    private WebCustomerDto webCustomerDto;

    public CachingLoginWidgetControllerMockTest() throws NamingException {
        super();
    }

    @Before
    public void setup() {

        this.loginWidgetCustomerLookup = EasyMock.createMock(LoginWidgetCustomerLookup.class);
        this.controller = new CachingLoginWidgetController(loginWidgetCustomerLookup);
        this.customer = new Customer();
        this.customer.setId(CUSTOMER_USER_ID);
        this.customer.setUsername(CUSTOMER_USER_NAME);
        this.customer.setCustomerType(CustomerType.SELF_SERVICE);

        this.webCustomerDto = new WebCustomerDto(customer, "homer simpson");
        setMocks(loginWidgetCustomerLookup);
    }

    private MockHttpServletRequest getRequest(boolean newSession) {
        return getRequest(newSession, "http", 80, "eac.mock.host", "/eac");
    }

    private MockHttpServletRequest getRequest(boolean newSession, String protocol, int port, String server, String context) {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setProtocol(protocol);
        request.setServerPort(port);
        request.setServerName(server);
        request.setContextPath(context);
        MockHttpSession session = new MockHttpSession();
        request.setSession(session);
        session.setNew(newSession);
        return request;
    }

    private void checkLoginPage(MockHttpServletRequest request, MockHttpServletResponse response, ModelAndView result) {
        Assert.assertEquals(SUCCESS_URL, request.getAttribute(LoginWidgetController.ATTR_SUCCESS_URL));
        Assert.assertEquals(ERROR_URL, request.getAttribute(LoginWidgetController.ATTR_ERROR_URL));
        Assert.assertEquals(EXPECTED_URL_PREFIX, request.getAttribute(LoginWidgetController.ATTR_URL_PREFIX));
        Assert.assertEquals(LoginWidgetController.VIEW_LOGIN_FORM, result.getViewName());

        Assert.assertEquals(0, response.getCookies().length);

        Customer customer = SessionHelper.getCustomer(request);
        String webUserName = SessionHelper.getWebUserName(request);
        Assert.assertNull(customer);
        Assert.assertNull(webUserName);

    }

    private void checkLoggedIn(MockHttpServletRequest request, MockHttpServletResponse response, ModelAndView result) {
        Assert.assertEquals(CUSTOMER_USER_NAME, request.getAttribute(LoginWidgetController.ATTR_USER_NAME));
        Assert.assertEquals(CUSTOMER_USER_ID, request.getAttribute(LoginWidgetController.ATTR_USER_ID));
        Assert.assertEquals(EXPECTED_URL_PREFIX, request.getAttribute(LoginWidgetController.ATTR_URL_PREFIX));
        Assert.assertEquals(LOGOUT_URL, request.getAttribute(LoginWidgetController.ATTR_LOGOUT_URL));
        Assert.assertEquals(LoginWidgetController.VIEW_WELCOME, result.getViewName());
        Assert.assertEquals(0, response.getCookies().length);

    }

    private void addCookieToRequest(MockHttpServletRequest request, String sessionKey) {
        Cookie eacCookie = new Cookie(EAC, sessionKey);
        request.setCookies(eacCookie);
    }

    @Test
    public void testNewSessionNoCookie() {
        MockHttpServletResponse response = new MockHttpServletResponse();
        MockHttpServletRequest request = getRequest(true);
        Assert.assertNull(request.getCookies());

        EasyMock.replay(getMocks());

        ModelAndView result = controller.getLoginOrWelcomeJavascript(SUCCESS_URL, ERROR_URL, LOGOUT_URL, request, response);

        EasyMock.verify(getMocks());

        checkLoginPage(request, response, result);

    }

    @Test
    public void testNewSessionInvalidCookie() throws CustomerNotFoundServiceLayerException {
        MockHttpServletRequest request = getRequest(true);
        addCookieToRequest(request, INVALID_SESSION_KEY);
        MockHttpServletResponse response = new MockHttpServletResponse();

        EasyMock.expect(this.loginWidgetCustomerLookup.getAndCacheCustomerFromErightsSessionKey(INVALID_SESSION_KEY)).andReturn(null);

        EasyMock.replay(getMocks());

        ModelAndView result = controller.getLoginOrWelcomeJavascript(SUCCESS_URL, ERROR_URL, LOGOUT_URL, request, response);

        EasyMock.verify(getMocks());

        checkLoginPage(request, response, result);
    }

    @Test
    public void testNewSessionBlankCookie() throws CustomerNotFoundServiceLayerException {
        MockHttpServletRequest request = getRequest(true);

        addCookieToRequest(request, "     ");
        MockHttpServletResponse response = new MockHttpServletResponse();

        EasyMock.replay(getMocks());

        ModelAndView result = controller.getLoginOrWelcomeJavascript(SUCCESS_URL, ERROR_URL, LOGOUT_URL, request, response);

        EasyMock.verify(getMocks());

        checkLoginPage(request, response, result);
    }

    @Test
    public void testNewSessionValidCookie() throws CustomerNotFoundServiceLayerException {
        MockHttpServletRequest request = getRequest(true);

        MockHttpServletResponse response = new MockHttpServletResponse();

        addCookieToRequest(request, SESSION_KEY);

        EasyMock.expect(this.loginWidgetCustomerLookup.getAndCacheCustomerFromErightsSessionKey(SESSION_KEY)).andReturn(webCustomerDto);

        EasyMock.replay(getMocks());

        ModelAndView result = controller.getLoginOrWelcomeJavascript(SUCCESS_URL, ERROR_URL, LOGOUT_URL, request, response);

        EasyMock.verify(getMocks());
        
        checkLoggedIn(request, response, result);

    }

    @Test
    public void testExistingSessionNoCookie() {
        MockHttpServletResponse response = new MockHttpServletResponse();
        MockHttpServletRequest request = getRequest(false);
        Assert.assertNull(request.getCookies());

        EasyMock.replay(getMocks());

        ModelAndView result = controller.getLoginOrWelcomeJavascript(SUCCESS_URL, ERROR_URL, LOGOUT_URL, request, response);

        EasyMock.verify(getMocks());

        checkLoginPage(request, response, result);
    }

    @Test
    public void testExistingSessionInvalidCookie() throws CustomerNotFoundServiceLayerException {
        MockHttpServletRequest request = getRequest(false);
        addCookieToRequest(request, INVALID_SESSION_KEY);
        MockHttpServletResponse response = new MockHttpServletResponse();

        this.loginWidgetCustomerLookup.removeCacheEntryForErightsSessionKey(INVALID_SESSION_KEY);
        EasyMock.expectLastCall();

        EasyMock.expect(this.loginWidgetCustomerLookup.getCustomerFromErightsSessionKey(INVALID_SESSION_KEY)).andReturn(null);

        EasyMock.replay(getMocks());

        ModelAndView result = controller.getLoginOrWelcomeJavascript(SUCCESS_URL, ERROR_URL, LOGOUT_URL, request, response);

        EasyMock.verify(getMocks());

        checkLoginPage(request, response, result);
    }

    @Test
    public void testExistingSessionBlankCookie() throws CustomerNotFoundServiceLayerException {
        MockHttpServletRequest request = getRequest(false);

        addCookieToRequest(request, "     ");
        MockHttpServletResponse response = new MockHttpServletResponse();

        EasyMock.replay(getMocks());

        ModelAndView result = controller.getLoginOrWelcomeJavascript(SUCCESS_URL, ERROR_URL, LOGOUT_URL, request, response);

        EasyMock.verify(getMocks());

        checkLoginPage(request, response, result);
    }

    @Test
    public void testExistingSessionValidCookieCustomerInSession() throws CustomerNotFoundServiceLayerException {
        MockHttpServletRequest request = getRequest(false);
        SessionHelper.setCustomerAndWebUserName(request, customer, webCustomerDto.getWebUserName());

        MockHttpServletResponse response = new MockHttpServletResponse();

        addCookieToRequest(request, SESSION_KEY);

        this.loginWidgetCustomerLookup.removeCacheEntryForErightsSessionKey(SESSION_KEY);
        EasyMock.expectLastCall();

        EasyMock.replay(getMocks());

        ModelAndView result = controller.getLoginOrWelcomeJavascript(SUCCESS_URL, ERROR_URL, LOGOUT_URL, request, response);

        EasyMock.verify(getMocks());

        checkLoggedIn(request, response, result);
    }

    @Test
    public void testExistingSessionValidCookieCustomerNotInSession() throws CustomerNotFoundServiceLayerException {
        MockHttpServletRequest request = getRequest(false);
        SessionHelper.setCustomerAndWebUserName(request, null, null);
        MockHttpServletResponse response = new MockHttpServletResponse();

        addCookieToRequest(request, SESSION_KEY);

        this.loginWidgetCustomerLookup.removeCacheEntryForErightsSessionKey(SESSION_KEY);
        EasyMock.expectLastCall();

        EasyMock.expect(this.loginWidgetCustomerLookup.getCustomerFromErightsSessionKey(SESSION_KEY)).andReturn(webCustomerDto);

        EasyMock.replay(getMocks());

        ModelAndView result = controller.getLoginOrWelcomeJavascript(SUCCESS_URL, ERROR_URL, LOGOUT_URL, request, response);

        EasyMock.verify(getMocks());

        checkLoggedIn(request, response, result);
    }

}
