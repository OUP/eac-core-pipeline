package com.oup.eac.web.controllers.thirdpartyui;

import static org.easymock.EasyMock.expectLastCall;

import java.util.UUID;

import javax.naming.NamingException;
import javax.servlet.http.Cookie;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.web.servlet.ModelAndView;

import com.oup.eac.common.utils.EACSettings;
import com.oup.eac.common.utils.crypto.SimpleCipher;
import com.oup.eac.domain.Customer;
import com.oup.eac.service.CustomerService;
import com.oup.eac.service.ServiceLayerException;
import com.oup.eac.service.exceptions.CustomerNotFoundServiceLayerException;
import com.oup.eac.web.controllers.helpers.CookieHelper;

public class CookieControllerMockTest /* extends AbstractMockTest */ {
	/*
	 * 
	 * private static final String ERIGHTS_COOKIE = "EAC"; private static final
	 * String KNOWN_SESSION_ID = "12345"; private static final String
	 * UNKNOWN_SESSION_ID = "-1"; private static final String SUCCESS_URL =
	 * "http://www.ora.com"; private static final String EXPECTED_VIEW_NAME =
	 * "redirect:http://www.ora.com?ERSESSION=12345"; private static final Object
	 * SESSION_TOKEN_ERROR = "sessionTokenError";
	 * 
	 * private EacCookieController eacCookieController; private CustomerService
	 * customerService; private String sharedSecret; private Customer customer;
	 * 
	 * public CookieControllerMockTest() throws NamingException { super(); }
	 * 
	 * @Before public void setup() {
	 * 
	 * //this helps with coverage statistics Logger log =
	 * Logger.getLogger(EacCookieController.class); log.setLevel(Level.TRACE);
	 * 
	 * this.customerService = EasyMock.createMock(CustomerService.class);
	 * this.eacCookieController = new EacCookieController(customerService);
	 * setMocks(this.customerService); this.sharedSecret =
	 * EACSettings.getRequiredProperty(EACSettings.EAC_SESSION_TOKEN_ENCRYPTION_KEY)
	 * ; customer = new Customer(); customer.setId(UUID.randomUUID().toString());
	 * customer.setUsername("bob.builder@test.com"); }
	 * 
	 * @Test public void testGenerateCookieSuccess() throws Exception {
	 * EasyMock.expect(customerService.getCustomerFromSession(KNOWN_SESSION_ID)).
	 * andReturn(customer); MockHttpServletRequest request = new
	 * MockHttpServletRequest(); MockHttpServletResponse response = new
	 * MockHttpServletResponse(); EasyMock.replay(getMocks());
	 * 
	 * ModelAndView result =
	 * eacCookieController.generateCookieFromSessionToken(encrypt(KNOWN_SESSION_ID),
	 * SUCCESS_URL, request, response);
	 * 
	 * EasyMock.verify(getMocks());
	 * 
	 * checkSuccess(result, response);
	 * 
	 * }
	 * 
	 * @Test public void testGenerateCookieInvalidSession1() throws Exception {
	 * EasyMock.expect(customerService.getCustomerFromSession(UNKNOWN_SESSION_ID)).
	 * andThrow(new CustomerNotFoundServiceLayerException()); MockHttpServletRequest
	 * request = new MockHttpServletRequest(); MockHttpServletResponse response =
	 * new MockHttpServletResponse(); EasyMock.replay(getMocks());
	 * 
	 * ModelAndView result =
	 * eacCookieController.generateCookieFromSessionToken(encrypt(UNKNOWN_SESSION_ID
	 * ), SUCCESS_URL, request, response);
	 * 
	 * checkForError(result, request, response);
	 * 
	 * EasyMock.verify(getMocks()); }
	 * 
	 * @Test public void testGenerateCookieInvalidSession2() throws Exception {
	 * EasyMock.expect(customerService.getCustomerFromSession(UNKNOWN_SESSION_ID)).
	 * andReturn(null); MockHttpServletRequest request = new
	 * MockHttpServletRequest(); MockHttpServletResponse response = new
	 * MockHttpServletResponse(); EasyMock.replay(getMocks());
	 * 
	 * ModelAndView result =
	 * eacCookieController.generateCookieFromSessionToken(encrypt(UNKNOWN_SESSION_ID
	 * ), SUCCESS_URL, request, response);
	 * 
	 * checkForError(result, request, response);
	 * 
	 * EasyMock.verify(getMocks()); }
	 * 
	 * @Test public void testGenerateCookieExistingCookingV1() throws Exception {
	 * checkGenerateCookieExistingCookie(false, UNKNOWN_SESSION_ID); }
	 * 
	 * @Test public void testGenerateCookieExistingCookingV2() throws Exception {
	 * checkGenerateCookieExistingCookie(true, UNKNOWN_SESSION_ID); }
	 * 
	 * @Test public void testGenerateCookieExistingCookingV3() throws Exception {
	 * checkGenerateCookieExistingCookie(true, KNOWN_SESSION_ID); }
	 * 
	 * @Test public void testGenerateCookieExistingCookingV4() throws Exception {
	 * checkGenerateCookieExistingCookie(true, "     "); }
	 * 
	 * @Test public void testTwoWayEncryption() throws Exception { String original =
	 * "the cat sat on the mat"; String encrpyted = encrypt(original); String plain
	 * = decrypt(encrpyted); Assert.assertEquals(original, plain); }
	 * 
	 * @Test public void testBlankSessionToken(){ checkForError("    ",
	 * SUCCESS_URL); }
	 * 
	 * @Test public void testBlankRedirectUrl() throws Exception{
	 * checkForError(encrypt(KNOWN_SESSION_ID), "    "); }
	 * 
	 * @Test public void testDecryptProblem() throws Exception{
	 * checkForError(KNOWN_SESSION_ID, SUCCESS_URL); }
	 * 
	 * @Test public void testInvalidRedirectUrl() throws Exception{
	 * checkForError(encrypt(KNOWN_SESSION_ID), "htttp://www.oup.com"); }
	 * 
	 * private void checkGenerateCookieExistingCookie(boolean logoutOkay, String
	 * cookieSessionId) throws Exception {
	 * EasyMock.expect(customerService.getCustomerFromSession(KNOWN_SESSION_ID)).
	 * andReturn(customer);
	 * 
	 * //we only need to log out if the cookie value and the sessionToken parameter
	 * are the same if(KNOWN_SESSION_ID.equals(cookieSessionId) == false &&
	 * StringUtils.isNotBlank(cookieSessionId)){
	 * customerService.logout(cookieSessionId); if (logoutOkay) { expectLastCall();
	 * } else { expectLastCall().andThrow(new ServiceLayerException()); } }
	 * 
	 * MockHttpServletRequest request = new MockHttpServletRequest(); Cookie
	 * eacCookie = CookieHelper.createErightsCookie(cookieSessionId);
	 * request.setCookies(eacCookie); MockHttpServletResponse response = new
	 * MockHttpServletResponse(); EasyMock.replay(getMocks());
	 * 
	 * ModelAndView result =
	 * eacCookieController.generateCookieFromSessionToken(encrypt(KNOWN_SESSION_ID),
	 * SUCCESS_URL, request, response);
	 * 
	 * EasyMock.verify(getMocks()); checkSuccess(result, response);
	 * 
	 * }
	 * 
	 * private void checkSuccess(ModelAndView result, MockHttpServletResponse
	 * response) { Assert.assertEquals(EXPECTED_VIEW_NAME, result.getViewName());
	 * Cookie eac = response.getCookie(ERIGHTS_COOKIE); Assert.assertNotNull(eac);
	 * Assert.assertEquals(KNOWN_SESSION_ID, eac.getValue()); }
	 * 
	 * private void checkForError(ModelAndView result, MockHttpServletRequest
	 * request, MockHttpServletResponse response) {
	 * Assert.assertNull(response.getCookie(ERIGHTS_COOKIE));
	 * Assert.assertEquals(SESSION_TOKEN_ERROR, result.getViewName());
	 * Assert.assertNotNull(request.getAttribute("errorCode")); }
	 * 
	 * private void checkForError(String encrytedToken, String url) {
	 * MockHttpServletRequest request = new MockHttpServletRequest();
	 * MockHttpServletResponse response = new MockHttpServletResponse();
	 * 
	 * EasyMock.replay(getMocks()); ModelAndView result =
	 * eacCookieController.generateCookieFromSessionToken(encrytedToken, url,
	 * request, response); EasyMock.verify(getMocks()); checkForError(result,
	 * request, response); }
	 * 
	 * private String encrypt(String plain) throws Exception { return
	 * SimpleCipher.encrypt(plain, sharedSecret); }
	 * 
	 * private String decrypt(String coded) throws Exception {
	 * 
	 * return SimpleCipher.decrypt(coded, sharedSecret); }
	 */}
