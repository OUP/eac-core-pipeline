package com.oup.eac.web.controllers.authentication;

import java.util.HashMap;
import java.util.Map;

import javax.naming.NamingException;

import org.apache.commons.lang.StringUtils;
import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.oup.eac.domain.Customer;
import com.oup.eac.domain.ProductRegistration;
import com.oup.eac.domain.ProductRegistrationDefinition;
import com.oup.eac.domain.RegisterableProduct;
import com.oup.eac.service.RegistrationService;

/**
 * 
 * @author David Hay
 *
 */
public class RegistrationAllowControllerMockTest /* extends AbstractMockTest */ {
	/*
	 * 
	 * private static final String TEST_PRODUCT_NAME = "testProductName"; private
	 * static final String TEST_CUSTOMER_USERNAME = "testCustomerUsername"; private
	 * static final String TEST_PRODUCT_EMAIL = "testProductEmail"; private static
	 * final String TEST_PRODUCT_HOME = "testProductHome"; private static final
	 * String TEST_PRODUCT_LANDING = "testProductLanding"; private static final
	 * String TEST_URL = "http://www.google.com";
	 * 
	 * 
	 * 
	 * public RegistrationAllowControllerMockTest() throws NamingException {
	 * super(); }
	 * 
	 * private RegistrationAllowController sut;
	 * 
	 * private RegistrationService registrationService; private ProductRegistration
	 * registration; private ProductRegistrationDefinition prd = new
	 * ProductRegistrationDefinition(); private Customer customer; private
	 * RegisterableProduct product;
	 * 
	 * 
	 * @Before public void setup() { this.registrationService =
	 * EasyMock.createMock(RegistrationService.class); this.registration = new
	 * ProductRegistration(); this.customer = new Customer(); this.prd = new
	 * ProductRegistrationDefinition(); this.product = new RegisterableProduct();
	 * 
	 * this.registration.setCustomer(customer);
	 * this.registration.setRegistrationDefinition(prd);
	 * this.prd.setProduct(product); this.product.setHomePage(TEST_PRODUCT_HOME);
	 * this.product.setEmail(TEST_PRODUCT_EMAIL);
	 * this.product.setLandingPage(TEST_PRODUCT_LANDING);
	 * 
	 * this.customer.setUsername(TEST_CUSTOMER_USERNAME);
	 * 
	 * setMocks(this.registrationService); sut = new
	 * RegistrationAllowController(registrationService); }
	 * 
	 * @Test public void testNoTokenParameter() throws Exception {
	 * MockHttpServletRequest request = new MockHttpServletRequest();
	 * MockHttpServletResponse response = new MockHttpServletResponse();
	 * 
	 * ModelAndView mav = sut.handleRequest(request, response);
	 * Assert.assertEquals("activationError", mav.getViewName()); }
	 * 
	 * @Test public void testNotActivated() throws Exception { boolean activated =
	 * false; checkAllowLink(activated, TEST_URL); }
	 * 
	 * @Test public void testNotActivatedBlankUrl() throws Exception { boolean
	 * activated = false; checkAllowLink(activated, ""); }
	 * 
	 * @Test public void testActivated() throws Exception { boolean activated =
	 * true; checkAllowLink(activated, TEST_URL); }
	 * 
	 * @Test public void testActivatedBlankUrl() throws Exception { boolean
	 * activated = true; checkAllowLink(activated, ""); }
	 * 
	 * private void checkAllowLink(boolean activated, String originalUrl) throws
	 * Exception { MockHttpServletRequest request = new MockHttpServletRequest();
	 * request.setParameter("token", "tokenValue"); MockHttpServletResponse response
	 * = new MockHttpServletResponse();
	 * 
	 * Map<String,Object> map1 = new HashMap<String,Object>();
	 * map1.put("originalUrl", originalUrl); map1.put("activated", activated);
	 * map1.put("registration", registration);
	 * 
	 * EasyMock.expect(this.registrationService.updateAllowRegistrationFromToken(
	 * "tokenValue", true)).andReturn(map1);
	 * 
	 * replayMocks(); ModelAndView mav = sut.handleRequest(request, response);
	 * RedirectView redirect = (RedirectView)mav.getView();
	 * if(StringUtils.isBlank(originalUrl)){ Assert.assertEquals(null,
	 * redirect.getUrl()); }else{ Assert.assertEquals(originalUrl,
	 * redirect.getUrl()); }
	 * 
	 * Map<String,Object> model = mav.getModel(); Assert.assertEquals(0,
	 * model.size()); verifyMocks(); }
	 * 
	 * 
	 * 
	 */}
