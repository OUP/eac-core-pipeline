package com.oup.eac.web.controllers.helpers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.joda.time.DateTime;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.GenericWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import com.oup.eac.domain.Customer;
import com.oup.eac.web.utils.WebContentUtils;
import com.oup.eac.web.utils.impl.CustomerTimeoutConfigImpl;
import com.oup.eac.web.utils.impl.WebContentUtilsImpl;

public class SessionHelperTest {

    public static final String REDIRECT_URL = "http://www.oup.com";
    public static final String ER_SESSION = "12345";
    public static final String REDIRECT_URL_WITH_ER_SESSION = "http://www.oup.com?ERSESSION=12345";

    @Test
    public void testAppendErSessionToUrlBlankQueryString() {
        String result = SessionHelper.appendErSessionToUrl(REDIRECT_URL, ER_SESSION);
        Assert.assertEquals(REDIRECT_URL_WITH_ER_SESSION, result);
    }

    @Test
    public void testAppendErSessionToUrlExistingQueryString() {
        String result = SessionHelper.appendErSessionToUrl("http://www.ora.com?A=1&ERSESSION=1&B=2", "12345");
        Assert.assertEquals("http://www.ora.com?A=1&B=2&ERSESSION=12345", result);
    }

    @Test
    public void testAppendErSessionToUrlViaUsingHttpSession() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        HttpSession session = request.getSession();
        SessionHelper.setForwardUrl(session, REDIRECT_URL);
        SessionHelper.setErightsSession(session, ER_SESSION);
        String redirectUrl = SessionHelper.getForwardUrl(request);
        Assert.assertEquals(REDIRECT_URL_WITH_ER_SESSION, redirectUrl);
    }

    private HttpServletRequest getRequest() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpSession session = new MockHttpSession();
        request.setSession(session);
        return request;
    }

    public WebApplicationContext getWebAppContext(int timeoutMins){
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();

        BeanDefinition beanDefinition1 = BeanDefinitionBuilder.rootBeanDefinition(WebContentUtilsImpl.class).addConstructorArgValue(false)
                .addConstructorArgValue(-1).getBeanDefinition();
        beanFactory.registerBeanDefinition(SessionHelper.BEAN_NAME_WEB_CONTENT_UTILS, beanDefinition1);
        
        BeanDefinition beanDefinition2 = BeanDefinitionBuilder.rootBeanDefinition(CustomerTimeoutConfigImpl.class).addConstructorArgValue(timeoutMins).getBeanDefinition();
        beanFactory.registerBeanDefinition(SessionHelper.BEAN_NAME_WEB_CUSTOMER_CONFIG, beanDefinition2);
        
        GenericApplicationContext cmdArgCxt = new GenericApplicationContext(beanFactory);
        // Must call refresh to initialize context
        cmdArgCxt.refresh();

        // Create application context, passing command line context as
        // parent
        WebApplicationContext webAppContext = new GenericWebApplicationContext(beanFactory);
        return webAppContext;
    }
    
    private void checkCustomer(HttpServletRequest request, Customer customer, boolean withSpringContext) {
        if (withSpringContext) {

        	WebApplicationContext webAppContext = getWebAppContext(5);
            WebContentUtils utils = webAppContext.getBean(SessionHelper.BEAN_NAME_WEB_CONTENT_UTILS, WebContentUtils.class);
            Assert.assertNotNull(utils);

            request.setAttribute(DispatcherServlet.WEB_APPLICATION_CONTEXT_ATTRIBUTE, webAppContext);
        }

        SessionHelper.setCustomer(request, customer);
        Assert.assertEquals(customer, SessionHelper.getCustomer(request));
    }

    @Test
    public void testNonNullCustomerWithSpringContext() {
        HttpServletRequest request = getRequest();

        Customer customer = new Customer();
        customer.setFirstName("Homer");
        customer.setFamilyName("Simpson");
        checkCustomer(request, customer, true);

        Assert.assertEquals("Homer Simpson", SessionHelper.getWebUserName(request));
    }

    @Test
    public void testNullCustomerWithSpringContext() {
        HttpServletRequest request = getRequest();

        Customer customer = null;
        checkCustomer(request, customer, true);

        Assert.assertNull(SessionHelper.getWebUserName(request));
    }

    @Test
    public void testNonNullCustomerWithoutSpringContext() {
        HttpServletRequest request = getRequest();

        Customer customer = new Customer();
        customer.setFirstName("Homer");
        customer.setFamilyName("Simpson");
        checkCustomer(request, customer, false);

        Assert.assertNull(SessionHelper.getWebUserName(request));
    }

    @Test
    public void testNullCustomerWithoutSpringContext() {
        HttpServletRequest request = getRequest();

        Customer customer = null;
        checkCustomer(request, customer, false);

        Assert.assertNull(SessionHelper.getWebUserName(request));
    }
    
    @Test
    public void testCustomerNotTimedOut(){
    	MockHttpServletRequest request = new MockHttpServletRequest();
    	MockHttpSession session = new MockHttpSession();
    	request.setSession(session);
        request.setAttribute(DispatcherServlet.WEB_APPLICATION_CONTEXT_ATTRIBUTE, getWebAppContext(5));
        Customer customer = new Customer();
        customer.setCreatedDate(new DateTime());
        SessionHelper.setCustomer(request, customer);
        Customer result = SessionHelper.getCustomer(request);
        Assert.assertEquals(result,customer);        
        Assert.assertTrue(SessionHelper.isCustomerLoggedIn(request));
    }
    
    @Test
    public void testCustomerNotTimedOut2(){
    	MockHttpServletRequest request = new MockHttpServletRequest();
    	MockHttpSession session = new MockHttpSession();
    	request.setSession(session);
        request.setAttribute(DispatcherServlet.WEB_APPLICATION_CONTEXT_ATTRIBUTE, getWebAppContext(5));
        Customer customer = new Customer();
        customer.setCreatedDate(new DateTime());
        SessionHelper.setCustomer(request, customer);
        session.setAttribute(SessionHelper.CUSTOMER_SESSION_START, null);
        Customer result = SessionHelper.getCustomer(request);
        Assert.assertEquals(result,customer);
        Assert.assertTrue(SessionHelper.isCustomerLoggedIn(request));
    }
    
    @Test
    public void testCustomerNotTimedOut3(){
    	MockHttpServletRequest request = new MockHttpServletRequest();
    	MockHttpSession session = new MockHttpSession();
    	request.setSession(session);
        request.setAttribute(DispatcherServlet.WEB_APPLICATION_CONTEXT_ATTRIBUTE, getWebAppContext(0));
        Customer customer = new Customer();
        customer.setCreatedDate(new DateTime());
        SessionHelper.setCustomer(request, customer);        
        Customer result = SessionHelper.getCustomer(request);
        Assert.assertEquals(result,customer);
        Assert.assertTrue(SessionHelper.isCustomerLoggedIn(request));
    }

    
    @Test
    public void testCustomerNotTimedOut4() {
    	MockHttpServletRequest request = new MockHttpServletRequest();
    	MockHttpSession session = new MockHttpSession();
    	request.setSession(session);
        request.setAttribute(DispatcherServlet.WEB_APPLICATION_CONTEXT_ATTRIBUTE, getWebAppContext(-1));
        Customer customer = new Customer();
        customer.setCreatedDate(new DateTime());
        SessionHelper.setCustomer(request, customer);        
        Customer result = SessionHelper.getCustomer(request);
        Assert.assertEquals(result,customer);
        Assert.assertTrue(SessionHelper.isCustomerLoggedIn(request));
    }
    
    @Test
    public void testCustomerTimedOut() {
    	MockHttpServletRequest request = new MockHttpServletRequest();
    	MockHttpSession session = new MockHttpSession();
    	request.setSession(session);
        request.setAttribute(DispatcherServlet.WEB_APPLICATION_CONTEXT_ATTRIBUTE, getWebAppContext(1));
        Customer customer = new Customer();
        customer.setCreatedDate(new DateTime());
        SessionHelper.setCustomer(request, customer);
        session.setAttribute(SessionHelper.CUSTOMER_SESSION_START, System.currentTimeMillis() - 70000);
        Customer result = SessionHelper.getCustomer(request);
        Assert.assertNull(result);
        Assert.assertFalse(SessionHelper.isCustomerLoggedIn(request));
    }

    @Test
    public void testCustomerNotLoggedIn1(){
    	MockHttpServletRequest request = new MockHttpServletRequest();
    	MockHttpSession session = new MockHttpSession();
    	request.setSession(session);
        request.setAttribute(DispatcherServlet.WEB_APPLICATION_CONTEXT_ATTRIBUTE, getWebAppContext(1));
        Customer result = SessionHelper.getCustomer(request);
        Assert.assertNull(result);
        Assert.assertFalse(SessionHelper.isCustomerLoggedIn(request));
    }
    
    @Test
    public void testCustomerNotLoggedIn2(){
    	MockHttpServletRequest request = new MockHttpServletRequest();
    	MockHttpSession session = new MockHttpSession();
    	request.setSession(session);
    	Customer customer = new Customer();        
        SessionHelper.setCustomer(request, customer);
        request.setAttribute(DispatcherServlet.WEB_APPLICATION_CONTEXT_ATTRIBUTE, getWebAppContext(1));
        Customer result = SessionHelper.getCustomer(request);
        Assert.assertEquals(customer, result);
        Assert.assertFalse(SessionHelper.isCustomerLoggedIn(request));
    }

}
