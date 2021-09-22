package com.oup.eac.web.controllers.thirdpartyui;

import static org.easymock.EasyMock.expectLastCall;

import java.util.Locale;
import java.util.UUID;

import javax.naming.NamingException;
import javax.servlet.http.Cookie;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;
import org.springframework.web.servlet.view.RedirectView;

import com.oup.eac.domain.Customer;
import com.oup.eac.domain.Password;
import com.oup.eac.dto.CustomerSessionDto;
import com.oup.eac.integration.facade.exceptions.ErightsException;
import com.oup.eac.integration.facade.exceptions.UserNotFoundException;
import com.oup.eac.service.CustomerService;
import com.oup.eac.service.InvalidCredentialsServiceLayerException;
import com.oup.eac.service.ServiceLayerException;
import com.oup.eac.service.exceptions.AccountLockedServiceLayerException;
import com.oup.eac.service.exceptions.CustomerNotFoundServiceLayerException;
import com.oup.eac.web.controllers.helpers.CookieHelper;
import com.oup.eac.web.controllers.registration.EACViews;

public class BasicLoginControllerMockTest /* extends AbstractMockTest */ {
	/*
	 * 
	 * private static final String USERNAME = "BOB"; private static final String
	 * PASSWORD = "Password1";
	 * 
	 * private static final String ERIGHTS_COOKIE = "EAC"; private static final
	 * String SUCCESS_URL = "http://www.ora.com"; private static final String
	 * ERROR_URL = "http://www.ora.com"; private static final String BAD_ERROR_URL =
	 * "oops"; private static final String SESSION_TOKEN = "12345"; private static
	 * final String EXPECTED_VIEW_NAME =
	 * "redirect:http://www.ora.com?ERSESSION=12345"; private static final String
	 * BASIC_LOGIN_ERROR = "basicLoginError"; private static final String
	 * ENCODED_PASSWORD = "asasdoaodaodiasd"; private static final String
	 * INVALID_SESSION = "101"; private static final String WRONG_PASSWORD =
	 * "Password2";
	 * 
	 * private BasicLoginController basicLoginController; private CustomerService
	 * customerService; private Customer customer; private Customer customer2;
	 * private CustomerSessionDto dto;
	 * 
	 * public BasicLoginControllerMockTest() throws NamingException { super(); }
	 * 
	 * @Before public void setup() {
	 * 
	 * // This helps with the coverage statistics Logger logger =
	 * Logger.getLogger(BasicLoginController.class); logger.setLevel(Level.DEBUG);
	 * 
	 * this.customerService = EasyMock.createMock(CustomerService.class);
	 * 
	 * this.basicLoginController = new BasicLoginController(customerService);
	 * 
	 * setMocks(this.customerService); customer = new Customer();
	 * customer.setId(UUID.randomUUID().toString());
	 * customer.setUsername("bob.builder@test.com"); customer.setPassword(new
	 * Password(ENCODED_PASSWORD, true));
	 * 
	 * customer2 = new Customer(); customer2.setId(UUID.randomUUID().toString());
	 * dto = new CustomerSessionDto(customer, SESSION_TOKEN); }
	 * 
	 * @Test public void testLoginSuccessWithoutLocale() throws
	 * ServiceLayerException, ErightsException { customer.setLocale(null);
	 * MockHttpServletRequest request = new MockHttpServletRequest();
	 * MockHttpServletResponse response = new MockHttpServletResponse();
	 * 
	 * EasyMock.expect(customerService.getCustomerByUsername(USERNAME)).andReturn(
	 * customer);
	 * EasyMock.expect(customerService.getCustomerByUsernameAndPassword(USERNAME,
	 * PASSWORD, false, true)).andReturn(dto);
	 * 
	 * EasyMock.replay(getMocks());
	 * 
	 * ModelAndView result = basicLoginController.basicLogin(USERNAME, PASSWORD,
	 * ERROR_URL, SUCCESS_URL, request, response);
	 * 
	 * EasyMock.verify(getMocks()); Assert.assertEquals(SESSION_TOKEN,
	 * response.getCookie(ERIGHTS_COOKIE).getValue());
	 * Assert.assertEquals(EXPECTED_VIEW_NAME, result.getViewName());
	 * Assert.assertEquals(1, response.getCookies().length); }
	 * 
	 * @Test public void testLoginSuccessWithLocale() throws ServiceLayerException,
	 * ErightsException { customer.setLocale(Locale.FRANCE); MockHttpServletRequest
	 * request = new MockHttpServletRequest(); LocaleResolver localeResolver = new
	 * SessionLocaleResolver();
	 * request.setAttribute(DispatcherServlet.LOCALE_RESOLVER_ATTRIBUTE,
	 * localeResolver); MockHttpServletResponse response = new
	 * MockHttpServletResponse(); localeResolver.setLocale(request, response,
	 * Locale.UK);
	 * EasyMock.expect(customerService.getCustomerByUsername(USERNAME)).andReturn(
	 * customer);
	 * EasyMock.expect(customerService.getCustomerByUsernameAndPassword(USERNAME,
	 * PASSWORD, false, true)).andReturn(dto);
	 * 
	 * EasyMock.replay(getMocks());
	 * 
	 * ModelAndView result = basicLoginController.basicLogin(USERNAME, PASSWORD,
	 * ERROR_URL, SUCCESS_URL, request, response);
	 * 
	 * EasyMock.verify(getMocks());
	 * 
	 * Assert.assertEquals(SESSION_TOKEN,
	 * response.getCookie(ERIGHTS_COOKIE).getValue());
	 * Assert.assertEquals(EXPECTED_VIEW_NAME, result.getViewName());
	 * Assert.assertEquals(1, response.getCookies().length); }
	 * 
	 * @Test public void testBlankUsernameWithRedirect() throws
	 * ServiceLayerException { checkValidationErrorWithRedirect("    ", PASSWORD,
	 * SUCCESS_URL); }
	 * 
	 * @Test public void testBlankPasswordWithRedirect() throws
	 * ServiceLayerException { checkValidationErrorWithRedirect(USERNAME, "    ",
	 * SUCCESS_URL); }
	 * 
	 * @Test public void testInvalidSuccessUrlWithRedirect() throws
	 * ServiceLayerException { checkValidationErrorWithRedirect(USERNAME, PASSWORD,
	 * "htttp://www.oup.com"); }
	 * 
	 * private void checkValidationErrorWithRedirect(String username, String
	 * password, String successUrl) { MockHttpServletRequest request = new
	 * MockHttpServletRequest(); MockHttpServletResponse response = new
	 * MockHttpServletResponse();
	 * 
	 * EasyMock.replay(getMocks());
	 * 
	 * ModelAndView result = basicLoginController.basicLogin(username, password,
	 * ERROR_URL, successUrl, request, response);
	 * 
	 * EasyMock.verify(getMocks());
	 * 
	 * Assert.assertEquals("redirect:" + ERROR_URL, result.getViewName());
	 * Assert.assertEquals(0, response.getCookies().length);
	 * 
	 * }
	 * 
	 * private void checkValidationErrorWithoutRedirect(String username, String
	 * password, String successUrl, String expectedErrorCode) {
	 * MockHttpServletRequest request = new MockHttpServletRequest();
	 * MockHttpServletResponse response = new MockHttpServletResponse();
	 * 
	 * EasyMock.replay(getMocks());
	 * 
	 * ModelAndView result = basicLoginController.basicLogin(username, password,
	 * BAD_ERROR_URL, successUrl, request, response);
	 * 
	 * EasyMock.verify(getMocks());
	 * 
	 * Assert.assertEquals(BASIC_LOGIN_ERROR, result.getViewName());
	 * Assert.assertEquals(expectedErrorCode, request.getAttribute("errorCode"));
	 * Assert.assertEquals(0, response.getCookies().length); }
	 * 
	 * @Test public void testBlankUsernameWithoutRedirect() throws
	 * ServiceLayerException { checkValidationErrorWithoutRedirect("    ", PASSWORD,
	 * SUCCESS_URL, BasicLoginController.ERR_CODE_USER_NAME_BLANK); }
	 * 
	 * @Test public void testBlankPasswordWithoutRedirect() throws
	 * ServiceLayerException { checkValidationErrorWithoutRedirect(USERNAME, "    ",
	 * SUCCESS_URL, BasicLoginController.ERR_CODE_PASSWORD_BLANK); }
	 * 
	 * @Test public void testInvalidSuccessUrlWithoutRedirect() throws
	 * ServiceLayerException { checkValidationErrorWithoutRedirect(USERNAME,
	 * PASSWORD, "htttp://www.oup.com",
	 * BasicLoginController.ERR_CODE_SUCCESS_URL_INVALID); }
	 * 
	 * @Test public void testLoginSuccessWithInvalidEacCookie1() throws
	 * ServiceLayerException, ErightsException { MockHttpServletRequest request =
	 * new MockHttpServletRequest(); MockHttpServletResponse response = new
	 * MockHttpServletResponse();
	 * 
	 * Cookie invalidCookie = CookieHelper.createErightsCookie(INVALID_SESSION);
	 * request.setCookies(invalidCookie);
	 * 
	 * EasyMock.expect(customerService.getCustomerByUsername(USERNAME)).andReturn(
	 * customer);
	 * EasyMock.expect(customerService.getCustomerByUsernameAndPassword(USERNAME,
	 * PASSWORD, false, true)).andReturn(dto);
	 * 
	 * EasyMock.expect(customerService.getCustomerFromSession(INVALID_SESSION)).
	 * andReturn(null);
	 * 
	 * EasyMock.replay(getMocks());
	 * 
	 * ModelAndView result = basicLoginController.basicLogin(USERNAME, PASSWORD,
	 * ERROR_URL, SUCCESS_URL, request, response);
	 * 
	 * EasyMock.verify(getMocks()); Assert.assertEquals(SESSION_TOKEN,
	 * response.getCookie(ERIGHTS_COOKIE).getValue());
	 * Assert.assertEquals(EXPECTED_VIEW_NAME, result.getViewName());
	 * Assert.assertEquals(1, response.getCookies().length); }
	 * 
	 * @Test public void testLoginSuccessWithInvalidEacCookie2() throws
	 * ServiceLayerException, ErightsException { MockHttpServletRequest request =
	 * new MockHttpServletRequest(); MockHttpServletResponse response = new
	 * MockHttpServletResponse();
	 * 
	 * Cookie invalidCookie = CookieHelper.createErightsCookie(INVALID_SESSION);
	 * request.setCookies(invalidCookie);
	 * 
	 * EasyMock.expect(customerService.getCustomerByUsername(USERNAME)).andReturn(
	 * customer);
	 * EasyMock.expect(customerService.getCustomerByUsernameAndPassword(USERNAME,
	 * PASSWORD, false, true)).andReturn(dto);
	 * 
	 * EasyMock.expect(customerService.getCustomerFromSession(INVALID_SESSION)).
	 * andThrow(new CustomerNotFoundServiceLayerException());
	 * 
	 * EasyMock.replay(getMocks());
	 * 
	 * ModelAndView result = basicLoginController.basicLogin(USERNAME, PASSWORD,
	 * ERROR_URL, SUCCESS_URL, request, response);
	 * 
	 * EasyMock.verify(getMocks()); Assert.assertEquals(SESSION_TOKEN,
	 * response.getCookie(ERIGHTS_COOKIE).getValue());
	 * Assert.assertEquals(EXPECTED_VIEW_NAME, result.getViewName());
	 * Assert.assertEquals(1, response.getCookies().length); }
	 * 
	 * @Test public void testLoginSuccessWithValidEacCookieDifferentCustomer()
	 * throws ServiceLayerException, ErightsException { MockHttpServletRequest
	 * request = new MockHttpServletRequest(); MockHttpServletResponse response =
	 * new MockHttpServletResponse();
	 * 
	 * Cookie validCookie = CookieHelper.createErightsCookie(INVALID_SESSION);
	 * request.setCookies(validCookie);
	 * 
	 * 
	 * EasyMock.expect(customerService.getCustomerByUsername(USERNAME)).andReturn(
	 * customer);
	 * EasyMock.expect(customerService.getCustomerByUsernameAndPassword(USERNAME,
	 * PASSWORD, false, true)).andReturn(dto);
	 * 
	 * // EAC cookie is for another customer - log them out
	 * EasyMock.expect(customerService.getCustomerFromSession(INVALID_SESSION)).
	 * andReturn(customer2); customerService.logout(INVALID_SESSION);
	 * expectLastCall();
	 * 
	 * EasyMock.replay(getMocks());
	 * 
	 * ModelAndView result = basicLoginController.basicLogin(USERNAME, PASSWORD,
	 * ERROR_URL, SUCCESS_URL, request, response);
	 * 
	 * EasyMock.verify(getMocks()); Assert.assertEquals(SESSION_TOKEN,
	 * response.getCookie(ERIGHTS_COOKIE).getValue());
	 * Assert.assertEquals(EXPECTED_VIEW_NAME, result.getViewName());
	 * Assert.assertEquals(1, response.getCookies().length); }
	 * 
	 * @Test public void
	 * testLoginSuccessWithValidEacCookieDifferentCustomerAndLogoutFails() throws
	 * ServiceLayerException, ErightsException { MockHttpServletRequest request =
	 * new MockHttpServletRequest(); MockHttpServletResponse response = new
	 * MockHttpServletResponse();
	 * 
	 * Cookie validCookie = CookieHelper.createErightsCookie(INVALID_SESSION);
	 * request.setCookies(validCookie);
	 * 
	 * EasyMock.expect(customerService.getCustomerByUsername(USERNAME)).andReturn(
	 * customer);
	 * EasyMock.expect(customerService.getCustomerByUsernameAndPassword(USERNAME,
	 * PASSWORD, false, true)).andReturn(dto);
	 * 
	 * // EAC cookie is for another customer - log them out
	 * EasyMock.expect(customerService.getCustomerFromSession(INVALID_SESSION)).
	 * andReturn(customer2); customerService.logout(INVALID_SESSION);
	 * expectLastCall().andThrow(new ServiceLayerException());
	 * 
	 * EasyMock.replay(getMocks());
	 * 
	 * ModelAndView result = basicLoginController.basicLogin(USERNAME, PASSWORD,
	 * ERROR_URL, SUCCESS_URL, request, response);
	 * 
	 * EasyMock.verify(getMocks()); Assert.assertEquals(SESSION_TOKEN,
	 * response.getCookie(ERIGHTS_COOKIE).getValue());
	 * Assert.assertEquals(EXPECTED_VIEW_NAME, result.getViewName());
	 * Assert.assertEquals(1, response.getCookies().length); }
	 * 
	 * @Test public void testLoginSuccessWithValidEacCookieSameCustomer() throws
	 * ServiceLayerException, ErightsException { MockHttpServletRequest request =
	 * new MockHttpServletRequest(); MockHttpServletResponse response = new
	 * MockHttpServletResponse();
	 * 
	 * Cookie validCookie = CookieHelper.createErightsCookie(SESSION_TOKEN);
	 * request.setCookies(validCookie);
	 * 
	 * EasyMock.expect(customerService.getCustomerByUsername(USERNAME)).andReturn(
	 * customer);
	 * 
	 * // EAC cookie is for same customer
	 * EasyMock.expect(customerService.getCustomerFromSession(SESSION_TOKEN)).
	 * andReturn(customer);
	 * 
	 * EasyMock.replay(getMocks());
	 * 
	 * ModelAndView result = basicLoginController.basicLogin(USERNAME, PASSWORD,
	 * ERROR_URL, SUCCESS_URL, request, response);
	 * 
	 * EasyMock.verify(getMocks()); Assert.assertEquals(SESSION_TOKEN,
	 * response.getCookie(ERIGHTS_COOKIE).getValue());
	 * Assert.assertEquals(EXPECTED_VIEW_NAME, result.getViewName());
	 * Assert.assertEquals(1, response.getCookies().length); }
	 * 
	 * @Test public void testLoginFailureWrongPassword() throws
	 * ServiceLayerException, ErightsException { MockHttpServletRequest request =
	 * new MockHttpServletRequest(); MockHttpServletResponse response = new
	 * MockHttpServletResponse();
	 * 
	 * Cookie validCookie = CookieHelper.createErightsCookie(SESSION_TOKEN);
	 * request.setCookies(validCookie);
	 * 
	 * EasyMock.expect(customerService.getCustomerFromSession(SESSION_TOKEN)).
	 * andReturn(customer);
	 * EasyMock.expect(customerService.getCustomerByUsername(USERNAME)).andReturn(
	 * customer);
	 * 
	 * EasyMock.expect(customerService.getCustomerByUsernameAndPassword(USERNAME,
	 * WRONG_PASSWORD, false, true)).andThrow(new
	 * InvalidCredentialsServiceLayerException());
	 * 
	 * EasyMock.replay(getMocks());
	 * 
	 * ModelAndView result = basicLoginController.basicLogin(USERNAME,
	 * WRONG_PASSWORD, BAD_ERROR_URL, SUCCESS_URL, request, response);
	 * 
	 * EasyMock.verify(getMocks()); checkForLoginFailure(result, request, response);
	 * }
	 * 
	 * @Test public void testLoginFailureInvalidUser() throws ServiceLayerException,
	 * ErightsException { MockHttpServletRequest request = new
	 * MockHttpServletRequest(); MockHttpServletResponse response = new
	 * MockHttpServletResponse();
	 * 
	 * Cookie validCookie = CookieHelper.createErightsCookie(SESSION_TOKEN);
	 * request.setCookies(validCookie);
	 * 
	 * EasyMock.expect(customerService.getCustomerByUsername(USERNAME)).andThrow(new
	 * UserNotFoundException(""));
	 * 
	 * EasyMock.replay(getMocks());
	 * 
	 * ModelAndView result = basicLoginController.basicLogin(USERNAME, PASSWORD,
	 * BAD_ERROR_URL, SUCCESS_URL, request, response);
	 * 
	 * EasyMock.verify(getMocks()); checkForLoginFailure(result, request, response);
	 * }
	 * 
	 * @Test public void testLoginFailureAuthenticationFailure() throws
	 * ServiceLayerException, ErightsException{ MockHttpServletRequest request = new
	 * MockHttpServletRequest(); MockHttpServletResponse response = new
	 * MockHttpServletResponse();
	 * 
	 * EasyMock.expect(customerService.getCustomerByUsername(USERNAME)).andThrow(new
	 * ErightsException(BASIC_LOGIN_ERROR));
	 * 
	 * EasyMock.replay(getMocks());
	 * 
	 * ModelAndView result = basicLoginController.basicLogin(USERNAME, PASSWORD,
	 * BAD_ERROR_URL, SUCCESS_URL, request, response);
	 * 
	 * EasyMock.verify(getMocks()); checkForLoginFailure(result, request, response);
	 * }
	 * 
	 * @Test public void testLoginFailureAccountLocked() throws
	 * ServiceLayerException, ErightsException { MockHttpServletRequest request =
	 * new MockHttpServletRequest(); MockHttpServletResponse response = new
	 * MockHttpServletResponse();
	 * 
	 * EasyMock.expect(customerService.getCustomerByUsername(USERNAME)).andReturn(
	 * customer);
	 * EasyMock.expect(customerService.getCustomerByUsernameAndPassword(USERNAME,
	 * PASSWORD, false, true)).andThrow(new AccountLockedServiceLayerException());
	 * 
	 * EasyMock.replay(getMocks());
	 * 
	 * ModelAndView result = basicLoginController.basicLogin(USERNAME, PASSWORD,
	 * BAD_ERROR_URL, SUCCESS_URL, request, response);
	 * 
	 * EasyMock.verify(getMocks()); checkForLoginFailure(result, request, response);
	 * }
	 * 
	 * @Test public void testLoginFailureUnknown() throws ServiceLayerException,
	 * ErightsException { MockHttpServletRequest request = new
	 * MockHttpServletRequest(); MockHttpServletResponse response = new
	 * MockHttpServletResponse();
	 * 
	 * EasyMock.expect(customerService.getCustomerByUsername(USERNAME)).andReturn(
	 * customer);
	 * EasyMock.expect(customerService.getCustomerByUsernameAndPassword(USERNAME,
	 * PASSWORD, false, true)).andThrow(new ServiceLayerException());
	 * 
	 * EasyMock.replay(getMocks());
	 * 
	 * 
	 * ModelAndView result = basicLoginController.basicLogin(USERNAME, PASSWORD,
	 * BAD_ERROR_URL, SUCCESS_URL, request, response);
	 * 
	 * EasyMock.verify(getMocks()); checkForLoginFailure(result, request, response);
	 * }
	 * 
	 * @Test public void testLoginSuccessChangePassword() throws
	 * ServiceLayerException, ErightsException { customer.setLocale(null);
	 * customer.setResetPassword(true); MockHttpServletRequest request = new
	 * MockHttpServletRequest(); MockHttpServletResponse response = new
	 * MockHttpServletResponse();
	 * 
	 * EasyMock.expect(customerService.getCustomerByUsername(USERNAME)).andReturn(
	 * customer);
	 * EasyMock.expect(customerService.getCustomerByUsernameAndPassword(USERNAME,
	 * PASSWORD, false, true)).andReturn(dto);
	 * 
	 * EasyMock.replay(getMocks());
	 * 
	 * ModelAndView result = basicLoginController.basicLogin(USERNAME, PASSWORD,
	 * ERROR_URL, SUCCESS_URL, request, response);
	 * 
	 * EasyMock.verify(getMocks()); View view = result.getView();
	 * Assert.assertNotNull("No view specified", view);
	 * Assert.assertTrue("Expected a RedirectView but got '" +
	 * view.getClass().getSimpleName() + "'", view instanceof RedirectView); String
	 * redirectUrl = ((RedirectView) view).getUrl();
	 * Assert.assertTrue("Expected a redirect url of '" +
	 * EACViews.CHANGE_PASSWORD_VIEW + "' but got '" + redirectUrl + "'",
	 * EACViews.CHANGE_PASSWORD_VIEW.equals(redirectUrl)); }
	 * 
	 * private void checkForLoginFailure(ModelAndView result, MockHttpServletRequest
	 * request, MockHttpServletResponse response) {
	 * Assert.assertEquals(BASIC_LOGIN_ERROR, result.getViewName());
	 * Assert.assertEquals(BasicLoginController.ERR_CODE_LOGIN_FAILED,
	 * request.getAttribute("errorCode")); Assert.assertEquals(0,
	 * response.getCookies().length); }
	 * 
	 */}
