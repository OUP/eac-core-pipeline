package com.oup.eac.web.interceptors;

import javax.naming.NamingException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import junit.framework.Assert;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpSession;

import com.oup.eac.domain.Customer;
import com.oup.eac.service.CustomerService;
import com.oup.eac.service.exceptions.CustomerNotFoundServiceLayerException;
import com.oup.eac.web.controllers.helpers.SessionHelper;

public class LoggedInCustomerInterceptorMockTest /* extends AbstractMockTest */ {
	/*
	 * 
	 * private LoggedInCustomerInterceptor sut; private CustomerService
	 * customerService; private Object handler; private Customer customer; private
	 * HttpServletResponse response;
	 * 
	 * public LoggedInCustomerInterceptorMockTest() throws NamingException {
	 * super(); }
	 * 
	 * @Before public void setup() { customerService =
	 * EasyMock.createMock(CustomerService.class); sut = new
	 * LoggedInCustomerInterceptor(customerService); response =
	 * EasyMock.createMock(HttpServletResponse.class); setMocks(customerService,
	 * response); customer = new Customer(); }
	 * 
	 * @Test public void testHappyPath1() throws Exception { MockHttpServletRequest
	 * request = new MockHttpServletRequest(); MockHttpSession session = new
	 * MockHttpSession(); request.setSession(session);
	 * 
	 * SessionHelper.setCustomer(request, customer);
	 * 
	 * replayMocks(); boolean canProceed = sut.preHandle(request, response,
	 * handler); Assert.assertTrue(canProceed);
	 * 
	 * Assert.assertEquals(customer, SessionHelper.getCustomer(request));
	 * verifyMocks();
	 * 
	 * }
	 * 
	 * @Test public void testHappyPath2() throws Exception { MockHttpServletRequest
	 * request = new MockHttpServletRequest(); MockHttpSession session = new
	 * MockHttpSession(); request.setSession(session); Cookie cookie = new
	 * Cookie("EAC", "123456"); request.setCookies(cookie);
	 * 
	 * EasyMock.expect(customerService.getCustomerFromSession("123456")).andReturn(
	 * customer); replayMocks(); boolean canProceed = sut.preHandle(request,
	 * response, handler); Assert.assertTrue(canProceed);
	 * 
	 * Assert.assertEquals(customer, SessionHelper.getCustomer(request));
	 * verifyMocks();
	 * 
	 * }
	 * 
	 * @Test public void testUnHappyPath1() throws Exception {
	 * 
	 * MockHttpServletRequest request = new MockHttpServletRequest();
	 * MockHttpSession session = new MockHttpSession(); request.setSession(session);
	 * 
	 * response.sendRedirect("/login.htm"); EasyMock.expectLastCall();
	 * 
	 * replayMocks(); sut.preHandle(request, response, handler);
	 * 
	 * verifyMocks();
	 * 
	 * }
	 * 
	 * @Test public void testUnHappyPath2() throws Exception {
	 * 
	 * MockHttpServletRequest request = new MockHttpServletRequest();
	 * MockHttpSession session = new MockHttpSession(); request.setSession(session);
	 * 
	 * Cookie cookie = new Cookie("EAC", "123456"); request.setCookies(cookie);
	 * 
	 * EasyMock.expect(customerService.getCustomerFromSession("123456")).andReturn(
	 * null);
	 * 
	 * response.sendRedirect("/login.htm"); EasyMock.expectLastCall();
	 * 
	 * replayMocks(); sut.preHandle(request, response, handler); verifyMocks();
	 * 
	 * }
	 * 
	 * @Test public void testUnHappyPath3() throws Exception {
	 * 
	 * MockHttpServletRequest request = new MockHttpServletRequest();
	 * MockHttpSession session = new MockHttpSession(); request.setSession(session);
	 * 
	 * Cookie cookie = new Cookie("EAC", "123456"); request.setCookies(cookie);
	 * 
	 * response.sendRedirect("/login.htm"); EasyMock.expectLastCall();
	 * 
	 * EasyMock.expect(customerService.getCustomerFromSession("123456")).andThrow(
	 * new CustomerNotFoundServiceLayerException());
	 * 
	 * replayMocks(); boolean canProceed = sut.preHandle(request, response,
	 * handler); Assert.assertFalse(canProceed); verifyMocks();
	 * 
	 * }
	 */}
