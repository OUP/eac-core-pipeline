package com.oup.eac.web.controllers.thirdpartyui;

import javax.naming.NamingException;
import javax.servlet.http.Cookie;

import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.web.servlet.ModelAndView;

import com.oup.eac.common.mock.AbstractMockTest;
import com.oup.eac.domain.Customer;
import com.oup.eac.domain.Customer.CustomerType;
import com.oup.eac.service.CustomerService;
import com.oup.eac.service.exceptions.CustomerNotFoundServiceLayerException;

public class LoginWidgetControllerMockTest extends AbstractMockTest {

    private static final String CUSTOMER_USER_NAME = "bobbuilder";

    private static final String CUSTOMER_USER_ID = "customerId1";

    private static final String SUCCESS_URL = "http://www.ora.com";
    
    private static final String LOGOUT_URL = "http://www.oup.com";
    
    private static final String ERROR_URL = "http://www.google.com";
    
    private static final String EXPECTED_URL_PREFIX = "http://eac.mock.host/eac";
    
    private static final String SESSION_KEY = "sessionKey123";
    
    private static final String INVALID_SESSION_KEY = "sessionKey1234";

    private static final String EAC = "EAC";
    
    private CustomerService customerService;
    
    private LoginWidgetController controller;
    
    private Customer customer;
    
    
    public LoginWidgetControllerMockTest() throws NamingException {
        super();
    }

    @Before
    public void setup(){
        this.customerService = EasyMock.createMock(CustomerService.class);
        this.controller = new LoginWidgetController(customerService);
        this.customer = new Customer();
        this.customer.setId(CUSTOMER_USER_ID);
        this.customer.setUsername(CUSTOMER_USER_NAME);
        this.customer.setCustomerType(CustomerType.SELF_SERVICE);
        setMocks(customerService);
    }
    
    private MockHttpServletRequest getRequest(){
        return getRequest("http", 80, "eac.mock.host", "/eac");
    }
    
    private MockHttpServletRequest getRequest(String protocol, int port, String server, String context){
        MockHttpServletRequest request =new MockHttpServletRequest();        
        request.setProtocol(protocol);
        request.setServerPort(port);
        request.setServerName(server);
        request.setContextPath(context);
        return request;
    }

    @Test
    public void testNoCookie(){
        MockHttpServletResponse response =new MockHttpServletResponse();
        MockHttpServletRequest request = getRequest();
        Assert.assertNull(request.getCookies());
        
        EasyMock.replay(getMocks());
        
        ModelAndView result = controller.getLoginOrWelcomeJavascript(SUCCESS_URL, ERROR_URL, LOGOUT_URL, request, response);
        
        EasyMock.verify(getMocks());

        checkLoginPage(request, response, result);
    }
    
    private void checkLoginPage(MockHttpServletRequest request, MockHttpServletResponse response, ModelAndView result) {
        Assert.assertEquals(SUCCESS_URL,request.getAttribute(LoginWidgetController.ATTR_SUCCESS_URL));
        Assert.assertEquals(ERROR_URL,request.getAttribute(LoginWidgetController.ATTR_ERROR_URL));
        Assert.assertEquals(EXPECTED_URL_PREFIX,request.getAttribute(LoginWidgetController.ATTR_URL_PREFIX));
        Assert.assertEquals(LoginWidgetController.VIEW_LOGIN_FORM, result.getViewName());
                
        Assert.assertEquals(0,response.getCookies().length);
        
    }

    private void addCookieToRequest(MockHttpServletRequest request, String sessionKey){
        Cookie eacCookie =  new Cookie(EAC,sessionKey);
        request.setCookies(eacCookie);
    }
    
    @Test
    public void testInvalidCookie() throws CustomerNotFoundServiceLayerException{
        MockHttpServletRequest request = getRequest();
        
        addCookieToRequest(request, INVALID_SESSION_KEY);
        MockHttpServletResponse response =new MockHttpServletResponse();        
        
        EasyMock.expect(this.customerService.getCustomerFromSession(INVALID_SESSION_KEY)).andThrow( new CustomerNotFoundServiceLayerException());

        EasyMock.replay(getMocks());
        
        ModelAndView result = controller.getLoginOrWelcomeJavascript(SUCCESS_URL, ERROR_URL, LOGOUT_URL, request, response);
        
        EasyMock.verify(getMocks());
        
        checkLoginPage(request, response, result);
    }
    
    @Test
    public void testBlankCookie() throws CustomerNotFoundServiceLayerException{
        MockHttpServletRequest request = getRequest();
        
        addCookieToRequest(request, "     ");
        MockHttpServletResponse response =new MockHttpServletResponse();        
        
        EasyMock.replay(getMocks());
        
        ModelAndView result = controller.getLoginOrWelcomeJavascript(SUCCESS_URL, ERROR_URL, LOGOUT_URL, request, response);
        
        EasyMock.verify(getMocks());
        
        checkLoginPage(request, response, result);
    }
    
    @Test
    public void testValidCookie() throws CustomerNotFoundServiceLayerException{
        MockHttpServletRequest request =getRequest();
        MockHttpServletResponse response =new MockHttpServletResponse();        
        
        addCookieToRequest(request, SESSION_KEY);
        
        EasyMock.expect(this.customerService.getCustomerFromSession(SESSION_KEY)).andReturn(customer);

        EasyMock.replay(getMocks());
        
        ModelAndView result = controller.getLoginOrWelcomeJavascript(SUCCESS_URL, ERROR_URL, LOGOUT_URL, request, response);
        
        EasyMock.verify(getMocks());
        
        Assert.assertEquals(CUSTOMER_USER_NAME, request.getAttribute(LoginWidgetController.ATTR_USER_NAME));
        Assert.assertEquals(CUSTOMER_USER_ID, request.getAttribute(LoginWidgetController.ATTR_USER_ID));
        Assert.assertEquals(EXPECTED_URL_PREFIX, request.getAttribute(LoginWidgetController.ATTR_URL_PREFIX));
        Assert.assertEquals(LOGOUT_URL, request.getAttribute(LoginWidgetController.ATTR_LOGOUT_URL));
        Assert.assertEquals(LoginWidgetController.VIEW_WELCOME, result.getViewName());
        Assert.assertEquals(0,response.getCookies().length);
    }
}
