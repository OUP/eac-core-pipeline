package com.oup.eac.web.controllers.login;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import javax.naming.NamingException;

import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.view.RedirectView;

import com.oup.eac.data.WhiteListUrlDao;
import com.oup.eac.domain.Customer;
import com.oup.eac.domain.User.EmailVerificationState;
import com.oup.eac.domain.WhiteListUrl;
import com.oup.eac.dto.CustomerSessionDto;
import com.oup.eac.dto.LoginDto;
import com.oup.eac.service.CustomerService;
import com.oup.eac.service.DomainSkinResolverService;
import com.oup.eac.service.ProductService;
import com.oup.eac.service.ServiceLayerException;
import com.oup.eac.service.WhiteListUrlService;
import com.oup.eac.web.controllers.helpers.CookieHelper;
import com.oup.eac.web.controllers.registration.EACViews;

/**
 * @author David Hay
 */
public class LoginFormControllerMockTest /* extends AbstractMockTest */ {
	/*
	 * 
	 * private static final String USERNAME = "username";
	 * 
	 * private static final String PASSWORD = "password";
	 * 
	 * private static final String FORM_VIEW = "form";
	 * 
	 * // private static final String ERROR_VIEW = "error";
	 * 
	 * private static final String URL = "http://localhost/index.htm";
	 * 
	 * private static final String URL_PARAM = "url";
	 * 
	 * private static final String ERSESSION = "ERSESSION";
	 * 
	 * private CustomerService customerService;
	 * 
	 * private WhiteListUrlDao whiteListdao;
	 * 
	 * private DomainSkinResolverService domainSkinResolverService;
	 * 
	 * private ProductService productService;
	 * 
	 * private LoginFormController controller;
	 * 
	 * private LoginDto command;
	 * 
	 * private BindException errors;
	 * 
	 * private CustomerSessionDto customerSessionDto;
	 * 
	 * private Customer customer;
	 * 
	 * private LocaleResolver localeResolver;
	 * 
	 * private MockHttpServletRequest request;
	 * 
	 * private MockHttpServletResponse response;
	 * 
	 * private WhiteListUrlService whiteListUrlService;
	 * 
	 * private Locale locale;
	 * 
	 * private String unique;
	 * 
	 * private List<WhiteListUrl> urls = new ArrayList<WhiteListUrl>();
	 * 
	 * private List<String> urlList = new ArrayList<String>();
	 * 
	 * public LoginFormControllerMockTest() throws NamingException { super(); }
	 * 
	 * @Before public void setup() { request = new MockHttpServletRequest();
	 * response = new MockHttpServletResponse(); customerService =
	 * EasyMock.createMock(CustomerService.class); localeResolver =
	 * EasyMock.createMock(LocaleResolver.class); domainSkinResolverService =
	 * EasyMock.createMock(DomainSkinResolverService.class); whiteListUrlService =
	 * EasyMock.createMock(WhiteListUrlService.class); productService =
	 * EasyMock.createMock(ProductService.class); whiteListdao
	 * =EasyMock.createMock(WhiteListUrlDao.class); controller = new
	 * LoginFormController(customerService, domainSkinResolverService,
	 * productService, whiteListUrlService); controller.setFormView(FORM_VIEW);
	 * controller.setCommandClass(LoginDto.class);
	 * request.setAttribute(DispatcherServlet.LOCALE_RESOLVER_ATTRIBUTE,
	 * localeResolver); request.setParameter(URL_PARAM, URL); unique =
	 * UUID.randomUUID().toString().replace("-", ""); setMocks(customerService,
	 * localeResolver, whiteListUrlService, whiteListdao);
	 * 
	 * command = new LoginDto(); command.setUsername(USERNAME);
	 * command.setPassword(PASSWORD); errors = new BindException(command,
	 * "loginDto"); customerSessionDto = new CustomerSessionDto(); customer = new
	 * Customer(); customerSessionDto.setCustomer(customer); urlList.add(URL);
	 * locale = Locale.FRANCE;
	 * customer.setEmailVerificationState(EmailVerificationState.VERIFIED);
	 * customer.setResetPassword(false);
	 * 
	 * }
	 * 
	 * 
	 * @Test public void testHappyPath1() throws Exception {
	 * 
	 * customer.setLocale(locale);
	 * EasyMock.expect(customerService.getCustomerByUsernameAndPassword(USERNAME,
	 * PASSWORD, true, true)).andReturn(customerSessionDto);
	 * localeResolver.setLocale(request, response, locale);
	 * EasyMock.expectLastCall();
	 * 
	 * replayMocks();
	 * 
	 * ModelAndView result = controller.onSubmit(request, response, command,
	 * errors);
	 * 
	 * View view = result.getView(); Assert.assertTrue(view instanceof
	 * RedirectView); RedirectView rv = (RedirectView) view;
	 * Assert.assertEquals(EACViews.PROFILE_VIEW, rv.getUrl());
	 * 
	 * verifyMocks(); }
	 * 
	 * @Test public void testHappyPath2() throws Exception {
	 * customer.setLocale(null);
	 * 
	 * localeResolver.setLocale(request, response, null); EasyMock.expectLastCall();
	 * 
	 * EasyMock.expect(customerService.getCustomerByUsernameAndPassword(USERNAME,
	 * PASSWORD, true, true)).andReturn(customerSessionDto);
	 * 
	 * 
	 * replayMocks();
	 * 
	 * ModelAndView result = controller.onSubmit(request, response, command,
	 * errors);
	 * 
	 * View view = result.getView(); Assert.assertTrue(view instanceof
	 * RedirectView); RedirectView rv = (RedirectView) view;
	 * Assert.assertEquals(EACViews.PROFILE_VIEW, rv.getUrl());
	 * 
	 * verifyMocks(); }
	 * 
	 * @Test public void testPasswordReset() throws Exception {
	 * 
	 * customer.setLocale(locale); customer.setResetPassword(true);
	 * EasyMock.expect(customerService.getCustomerByUsernameAndPassword(USERNAME,
	 * PASSWORD, true, true)).andReturn(customerSessionDto);
	 * localeResolver.setLocale(request, response, locale);
	 * 
	 * replayMocks();
	 * 
	 * ModelAndView result = controller.onSubmit(request, response, command,
	 * errors);
	 * 
	 * View view = result.getView(); Assert.assertTrue(view instanceof
	 * RedirectView); RedirectView rv = (RedirectView) view;
	 * Assert.assertEquals(EACViews.CHANGE_PASSWORD_VIEW, rv.getUrl());
	 * 
	 * verifyMocks(); }
	 * 
	 * @Test public void testServiceLayerException() throws Exception {
	 * 
	 * customer.setResetPassword(true);
	 * EasyMock.expect(whiteListUrlService.getUrls()).andStubReturn(urlList);
	 * EasyMock.expect(customerService.getCustomerByUsernameAndPassword(USERNAME,
	 * PASSWORD, true, true)).andThrow(new ServiceLayerException());
	 * 
	 * replayMocks();
	 * 
	 * ModelAndView result = controller.onSubmit(request, response, command,
	 * errors);
	 * 
	 * Assert.assertEquals(FORM_VIEW, result.getViewName()); verifyMocks(); }
	 * 
	 * @Test public void testDirectLoginUrlNotPassed() throws Exception {
	 * 
	 * request.setParameter("url", ""); request.setMethod("GET");
	 * 
	 * replayMocks();
	 * 
	 * ModelAndView result = controller.handleRequest(request, response);
	 * 
	 * Assert.assertEquals(FORM_VIEW, result.getViewName());
	 * 
	 * verifyMocks(); }
	 * 
	 * @Test public void testDirectLoginUrlPassed() throws Exception {
	 * 
	 * request.setMethod("GET");
	 * EasyMock.expect(whiteListUrlService.getUrls()).andStubReturn(urlList);
	 * replayMocks();
	 * 
	 * ModelAndView result = controller.handleRequest(request, response);
	 * 
	 * Assert.assertEquals(FORM_VIEW, result.getViewName());
	 * 
	 * verifyMocks(); }
	 * 
	 * @Test public void testPrimaryDomainSessionAvailableButInvalid() throws
	 * Exception { String session = "IUHDUYGBSDUDG";
	 * 
	 * request.setMethod("GET");
	 * request.setCookies(CookieHelper.createErightsCookie(session));
	 * EasyMock.expect(whiteListUrlService.getUrls()).andStubReturn(urlList);
	 * EasyMock.expect(customerService.getCustomerFromSession(session)).andReturn(
	 * null);
	 * 
	 * replayMocks();
	 * 
	 * ModelAndView result = controller.handleRequest(request, response);
	 * 
	 * Assert.assertEquals(FORM_VIEW, result.getViewName());
	 * 
	 * verifyMocks(); }
	 * 
	 * @Test public void testPrimaryDomainSessionAvailableAndValid() throws
	 * Exception { String session = "IUHDUYGBSDUDG";
	 * 
	 * request.setMethod("GET");
	 * request.setCookies(CookieHelper.createErightsCookie(session));
	 * EasyMock.expect(whiteListUrlService.getUrls()).andStubReturn(urlList);
	 * EasyMock.expect(customerService.getCustomerFromSession(session)).andReturn(
	 * customer);
	 * 
	 * replayMocks();
	 * 
	 * ModelAndView result = controller.handleRequest(request, response);
	 * 
	 * View view = result.getView(); Assert.assertTrue(view instanceof
	 * RedirectView); RedirectView rv = (RedirectView) view;
	 * Assert.assertEquals(URL+ "?" + ERSESSION + "=" + session, rv.getUrl());
	 * 
	 * verifyMocks(); }
	 * 
	 */}
