package com.oup.eac.web.controllers.thirdpartyui.impl;

import javax.naming.NamingException;

import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.oup.eac.domain.Customer;
import com.oup.eac.dto.WebCustomerDto;
import com.oup.eac.service.CustomerService;
import com.oup.eac.service.exceptions.CustomerNotFoundServiceLayerException;
import com.oup.eac.web.utils.WebContentUtils;

public class LoginWidgetCustomerLookupImplMockTest /* extends AbstractMockTest */ {
	/*
	 * 
	 * private static final String SESSION_KEY = "sessionKey"; private static final
	 * String USER_NAME = "Homer Simpson"; private WebContentUtils webContentUtils;
	 * private CustomerService customerService; private
	 * LoginWidgetCustomerLookupImpl sut; private Customer customer;
	 * 
	 * public LoginWidgetCustomerLookupImplMockTest() throws NamingException {
	 * super(); }
	 * 
	 * @Before public void setup() { this.customerService =
	 * EasyMock.createMock(CustomerService.class); this.webContentUtils =
	 * EasyMock.createMock(WebContentUtils.class); sut = new
	 * LoginWidgetCustomerLookupImpl(customerService, webContentUtils); customer =
	 * new Customer(); setMocks(this.customerService, this.webContentUtils); }
	 * 
	 * @Test public void testRemove() { replayMocks();
	 * sut.removeCacheEntryForErightsSessionKey("eRightsSessionKey"); verifyMocks();
	 * }
	 * 
	 * @Test public void testGetAndCacheCustomerSuccess() throws
	 * CustomerNotFoundServiceLayerException { Assert.assertNotNull(customer);
	 * EasyMock.expect(this.customerService.getCustomerFromSession(SESSION_KEY)).
	 * andReturn(customer);
	 * EasyMock.expect(this.webContentUtils.getCustomerName(customer)).andReturn(
	 * USER_NAME); replayMocks(); WebCustomerDto result =
	 * sut.getAndCacheCustomerFromErightsSessionKey(SESSION_KEY);
	 * Assert.assertEquals(customer, result.getCustomer());
	 * Assert.assertEquals(USER_NAME, result.getWebUserName()); verifyMocks(); }
	 * 
	 * @Test public void testGetCustomerSuccess() throws
	 * CustomerNotFoundServiceLayerException { Assert.assertNotNull(customer);
	 * EasyMock.expect(this.customerService.getCustomerFromSession(SESSION_KEY)).
	 * andReturn(customer);
	 * EasyMock.expect(this.webContentUtils.getCustomerName(customer)).andReturn(
	 * USER_NAME); replayMocks(); WebCustomerDto result =
	 * sut.getCustomerFromErightsSessionKey(SESSION_KEY);
	 * Assert.assertEquals(customer, result.getCustomer());
	 * Assert.assertEquals(USER_NAME, result.getWebUserName()); verifyMocks(); }
	 * 
	 * @Test public void testGetAndCacheCustomerFail() throws
	 * CustomerNotFoundServiceLayerException { Assert.assertNotNull(customer);
	 * EasyMock.expect(this.customerService.getCustomerFromSession(SESSION_KEY)).
	 * andThrow(new CustomerNotFoundServiceLayerException());
	 * 
	 * replayMocks(); WebCustomerDto result =
	 * sut.getAndCacheCustomerFromErightsSessionKey(SESSION_KEY);
	 * Assert.assertNull(result); verifyMocks(); }
	 * 
	 * @Test public void testGetCustomerFail() throws
	 * CustomerNotFoundServiceLayerException { Assert.assertNotNull(customer);
	 * EasyMock.expect(this.customerService.getCustomerFromSession(SESSION_KEY)).
	 * andThrow(new CustomerNotFoundServiceLayerException());
	 * 
	 * replayMocks(); WebCustomerDto result =
	 * sut.getCustomerFromErightsSessionKey(SESSION_KEY); Assert.assertNull(result);
	 * verifyMocks(); }
	 * 
	 * @Test public void testGetAndCacheCustomerBlankKey() throws
	 * CustomerNotFoundServiceLayerException { replayMocks(); WebCustomerDto result
	 * = sut.getAndCacheCustomerFromErightsSessionKey("    ");
	 * Assert.assertNull(result); verifyMocks(); }
	 * 
	 * @Test public void testGetCustomerBlankKey() throws
	 * CustomerNotFoundServiceLayerException { replayMocks(); WebCustomerDto result
	 * = sut.getCustomerFromErightsSessionKey("     "); Assert.assertNull(result);
	 * verifyMocks(); }
	 */}
