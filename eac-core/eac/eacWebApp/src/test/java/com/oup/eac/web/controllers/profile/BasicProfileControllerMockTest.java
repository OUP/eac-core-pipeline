package com.oup.eac.web.controllers.profile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.naming.NamingException;

import org.easymock.EasyMock;
import org.joda.time.DateTimeZone;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;
import org.springframework.web.servlet.view.RedirectView;

import com.oup.eac.domain.AdminUser;
import com.oup.eac.domain.Customer;
import com.oup.eac.domain.Customer.CustomerType;
import com.oup.eac.dto.BasicProfileDto;
import com.oup.eac.dto.Message;
import com.oup.eac.dto.profile.ProfileRegistrationDto;
import com.oup.eac.integration.facade.exceptions.ErightsException;
import com.oup.eac.integration.facade.exceptions.UserLoginCredentialAlreadyExistsException;
import com.oup.eac.service.AdminService;
import com.oup.eac.service.CustomerService;
import com.oup.eac.service.ServiceLayerException;
import com.oup.eac.service.exceptions.UsernameExistsException;
import com.oup.eac.web.controllers.helpers.SessionHelper;
import com.oup.eac.web.locale.LocaleDropDownSource;
import com.oup.eac.web.profile.CachingProfileRegistrationDtoSource;
import com.oup.eac.web.validators.profile.BasicProfileValidator;

public class BasicProfileControllerMockTest /* extends AbstractMockTest */ {
	/*
	 * 
	 * private BasicProfileController controller; private BasicProfileValidator
	 * validator; private LocaleDropDownSource localeDropDownSource; private
	 * CustomerService customerService; private AdminService adminService; private
	 * Customer customer = null; private Customer freshCustomer = null; private
	 * LocaleResolver localeResolver; private Map<String, String> dropDownValues;
	 * private Locale locale; private MockHttpServletRequest request; private
	 * MockHttpServletResponse response; private MockHttpSession session; private
	 * Customer customer2; private CachingProfileRegistrationDtoSource cache;
	 * private List<ProfileRegistrationDto> profileData;
	 * 
	 * public BasicProfileControllerMockTest() throws NamingException { super(); }
	 * 
	 * @Before public void setup() { validator =
	 * EasyMock.createMock(BasicProfileValidator.class); localeDropDownSource =
	 * EasyMock.createMock(LocaleDropDownSource.class); customerService =
	 * EasyMock.createMock(CustomerService.class); adminService =
	 * EasyMock.createMock(AdminService.class); cache =
	 * EasyMock.createMock(CachingProfileRegistrationDtoSource.class);
	 * this.controller = new BasicProfileController(validator, localeDropDownSource,
	 * customerService, cache, adminService); setMocks(this.validator,
	 * this.localeDropDownSource, this.customerService, this.cache,
	 * this.adminService); this.customer = new Customer();
	 * this.customer.setCustomerType(CustomerType.SELF_SERVICE); this.localeResolver
	 * = new SessionLocaleResolver(); this.dropDownValues = new HashMap<String,
	 * String>(); dropDownValues.put("en", "English"); dropDownValues.put("fr",
	 * "French"); this.request = new MockHttpServletRequest(); this.session = new
	 * MockHttpSession(); this.request.setSession(session); this.response = new
	 * MockHttpServletResponse(); this.locale = Locale.CANADA_FRENCH;
	 * 
	 * localeResolver.setLocale(request, response, locale);
	 * request.setAttribute(DispatcherServlet.LOCALE_RESOLVER_ATTRIBUTE,
	 * this.localeResolver); customer2 = new Customer();
	 * customer2.setId("customer2"); customer.setId("customer");
	 * customer.setFirstName("FIRST"); customer.setFamilyName("LAST");
	 * customer.setEmailAddress("test.user@test.com");
	 * customer.setLocale(Locale.TRADITIONAL_CHINESE);
	 * customer.setTimeZone("Europe/London");
	 * 
	 * freshCustomer = new Customer(); freshCustomer.setId(customer.getId());
	 * 
	 * profileData = new ArrayList<ProfileRegistrationDto>(); }
	 * 
	 * @Test public void testSessionLocale() { Locale sessionLocale = (Locale)
	 * request.getSession().getAttribute(SessionLocaleResolver.
	 * LOCALE_SESSION_ATTRIBUTE_NAME); Assert.assertEquals(locale, sessionLocale);
	 * Locale resolved = localeResolver.resolveLocale(request);
	 * Assert.assertEquals(locale, resolved); }
	 * 
	 *//**
		 * User has Locale.
		 * 
		 * @throws ServiceLayerException
		 */
	/*
	 * @Test public void testGetHappyPath1() throws ServiceLayerException {
	 * request.setMethod("GET");
	 * session.setAttribute(BasicProfileController.KEY_SUPRESS_CACHE_REFRESH,
	 * Boolean.FALSE); SessionHelper.setCustomer(request, customer);
	 * 
	 * EasyMock.expect(localeDropDownSource.getLocaleDropDown(locale,
	 * customer.getLocale())).andReturn(dropDownValues);
	 * 
	 * this.cache.removeFromCache(customer, session); EasyMock.expectLastCall();
	 * 
	 * EasyMock.expect(this.cache.getProfileRegistrationDtos(customer,
	 * session)).andReturn(profileData);
	 * 
	 * EasyMock.replay(getMocks());
	 * 
	 * Map<String, String> dropDown = controller.getLocaleDropDown(request);
	 * Assert.assertEquals(dropDown, dropDownValues);
	 * 
	 * BasicProfileDto dto = controller.getForm(request); WebDataBinder binder = new
	 * WebDataBinder(dto);
	 * 
	 * controller.initBinder(binder);
	 * 
	 * String viewName = controller.processGet(request, response);
	 * 
	 * List<String> timeZone = controller.getTimeZoneDropDown();
	 * Assert.assertEquals(timeZone.size(), DateTimeZone.getAvailableIDs().size());
	 * Assert.assertTrue(timeZone.containsAll(DateTimeZone.getAvailableIDs()));
	 * 
	 * List<ProfileRegistrationDto> data =
	 * controller.getProfileRegistrationDtos(request, session);
	 * Assert.assertEquals(profileData, data);
	 * 
	 * Assert.assertEquals("basicProfile", viewName);
	 * 
	 * Assert.assertEquals(customer.getUsername(), dto.getUsername());
	 * Assert.assertEquals(customer.getFirstName(), dto.getFirstName());
	 * Assert.assertEquals(customer.getFamilyName(), dto.getFamilyName());
	 * Assert.assertEquals(customer.getEmailAddress(), dto.getEmail());
	 * 
	 * Assert.assertEquals(customer.getTimeZone(), dto.getTimezone());
	 * Assert.assertEquals(customer.getLocale(), dto.getUserLocale());
	 * 
	 * EasyMock.verify(getMocks());
	 * Assert.assertNull(session.getAttribute(BasicProfileController.
	 * KEY_SUPRESS_CACHE_REFRESH));
	 * 
	 * }
	 * 
	 *//**
		 * User has Locale.
		 *
		 * @throws ServiceLayerException the service layer exception
		 */
	/*
	 * @Test public void testGetIfCacheReturnsNullList() throws
	 * ServiceLayerException { request.setMethod("GET");
	 * session.setAttribute(BasicProfileController.KEY_SUPRESS_CACHE_REFRESH,
	 * Boolean.FALSE); SessionHelper.setCustomer(request, customer);
	 * 
	 * EasyMock.expect(localeDropDownSource.getLocaleDropDown(locale,
	 * customer.getLocale())).andReturn(dropDownValues);
	 * 
	 * this.cache.removeFromCache(customer, session); EasyMock.expectLastCall();
	 * 
	 * EasyMock.expect(this.cache.getProfileRegistrationDtos(customer,
	 * session)).andReturn(null);
	 * 
	 * EasyMock.replay(getMocks());
	 * 
	 * Map<String, String> dropDown = controller.getLocaleDropDown(request);
	 * Assert.assertEquals(dropDown, dropDownValues);
	 * 
	 * BasicProfileDto dto = controller.getForm(request); WebDataBinder binder = new
	 * WebDataBinder(dto);
	 * 
	 * controller.initBinder(binder);
	 * 
	 * String viewName = controller.processGet(request, response);
	 * 
	 * List<String> timeZone = controller.getTimeZoneDropDown();
	 * Assert.assertEquals(timeZone.size(), DateTimeZone.getAvailableIDs().size());
	 * Assert.assertTrue(timeZone.containsAll(DateTimeZone.getAvailableIDs()));
	 * 
	 * List<ProfileRegistrationDto> data =
	 * controller.getProfileRegistrationDtos(request, session);
	 * Assert.assertTrue(data.isEmpty());
	 * 
	 * Assert.assertEquals("basicProfile", viewName);
	 * 
	 * Assert.assertEquals(customer.getUsername(), dto.getUsername());
	 * Assert.assertEquals(customer.getFirstName(), dto.getFirstName());
	 * Assert.assertEquals(customer.getFamilyName(), dto.getFamilyName());
	 * Assert.assertEquals(customer.getEmailAddress(), dto.getEmail());
	 * 
	 * Assert.assertEquals(customer.getTimeZone(), dto.getTimezone());
	 * Assert.assertEquals(customer.getLocale(), dto.getUserLocale());
	 * 
	 * EasyMock.verify(getMocks());
	 * Assert.assertNull(session.getAttribute(BasicProfileController.
	 * KEY_SUPRESS_CACHE_REFRESH));
	 * 
	 * }
	 * 
	 *//**
		 * @throws ServiceLayerException
		 */
	/*
	 * @Test public void testGetHappyPath3() throws ServiceLayerException {
	 * request.setMethod("GET");
	 * session.setAttribute(BasicProfileController.KEY_SUPRESS_CACHE_REFRESH,
	 * Boolean.TRUE); SessionHelper.setCustomer(request, customer);
	 * 
	 * EasyMock.expect(localeDropDownSource.getLocaleDropDown(locale,
	 * customer.getLocale())).andReturn(dropDownValues);
	 * 
	 * EasyMock.expect(this.cache.getProfileRegistrationDtos(customer,
	 * session)).andReturn(profileData);
	 * 
	 * EasyMock.replay(getMocks());
	 * 
	 * Map<String, String> dropDown = controller.getLocaleDropDown(request);
	 * Assert.assertEquals(dropDown, dropDownValues);
	 * 
	 * BasicProfileDto dto = controller.getForm(request); WebDataBinder binder = new
	 * WebDataBinder(dto);
	 * 
	 * controller.initBinder(binder);
	 * 
	 * String viewName = controller.processGet(request, response);
	 * 
	 * List<String> timeZone = controller.getTimeZoneDropDown();
	 * Assert.assertEquals(timeZone.size(), DateTimeZone.getAvailableIDs().size());
	 * Assert.assertTrue(timeZone.containsAll(DateTimeZone.getAvailableIDs()));
	 * 
	 * List<ProfileRegistrationDto> data =
	 * controller.getProfileRegistrationDtos(request, session);
	 * Assert.assertEquals(profileData, data);
	 * 
	 * Assert.assertEquals("basicProfile", viewName);
	 * 
	 * Assert.assertEquals(customer.getUsername(), dto.getUsername());
	 * Assert.assertEquals(customer.getFirstName(), dto.getFirstName());
	 * Assert.assertEquals(customer.getFamilyName(), dto.getFamilyName());
	 * Assert.assertEquals(customer.getEmailAddress(), dto.getEmail());
	 * 
	 * Assert.assertEquals(customer.getTimeZone(), dto.getTimezone());
	 * Assert.assertEquals(customer.getLocale(), dto.getUserLocale());
	 * 
	 * EasyMock.verify(getMocks());
	 * Assert.assertNull(session.getAttribute(BasicProfileController.
	 * KEY_SUPRESS_CACHE_REFRESH));
	 * 
	 * }
	 *//**
		 * User has no Locale.
		 */
	/*
	 * @Test public void testGetHappyPath2() { request.setMethod("GET");
	 * session.setAttribute(BasicProfileController.KEY_SUPRESS_CACHE_REFRESH, null);
	 * SessionHelper.setCustomer(request, customer); customer.setLocale(null);
	 * 
	 * EasyMock.expect(localeDropDownSource.getLocaleDropDown(locale,
	 * customer.getLocale())).andReturn(dropDownValues);
	 * EasyMock.replay(getMocks()); Map<String, String> dropDown =
	 * controller.getLocaleDropDown(request); Assert.assertEquals(dropDown,
	 * dropDownValues);
	 * 
	 * BasicProfileDto dto = controller.getForm(request); WebDataBinder binder = new
	 * WebDataBinder(dto);
	 * 
	 * controller.initBinder(binder);
	 * 
	 * String viewName = controller.processGet(request, response);
	 * 
	 * Assert.assertEquals("basicProfile", viewName);
	 * 
	 * Assert.assertEquals(customer.getUsername(), dto.getUsername());
	 * Assert.assertEquals(customer.getFirstName(), dto.getFirstName());
	 * Assert.assertEquals(customer.getFamilyName(), dto.getFamilyName());
	 * Assert.assertEquals(customer.getEmailAddress(), dto.getEmail());
	 * 
	 * Assert.assertEquals(customer.getTimeZone(), dto.getTimezone());
	 * Assert.assertEquals(locale, dto.getUserLocale());
	 * 
	 * EasyMock.verify(getMocks());
	 * Assert.assertNull(session.getAttribute(BasicProfileController.
	 * KEY_SUPRESS_CACHE_REFRESH)); }
	 * 
	 *//**
		 * Same username is submitted.
		 */
	/*
	 * @Test public void testPostHappyPath1() throws UsernameExistsException,
	 * ServiceLayerException { request.setMethod("POST");
	 * 
	 * SessionHelper.setCustomer(request, customer);
	 * 
	 * EasyMock.expect(this.cache.getProfileRegistrationDtos(customer,
	 * session)).andReturn(profileData); try {
	 * EasyMock.expect(adminService.getAdminUserByUsername("bob")).andReturn(null);
	 * EasyMock.expect(customerService.getCustomerByUsername("bob")).andReturn(null)
	 * ;
	 * 
	 * EasyMock.expect(customerService.getCustomerById(customer.getId())).andReturn(
	 * freshCustomer);
	 * 
	 * customerService.updateCustomerProfile(EasyMock.eq(customer.getId()),
	 * EasyMock.anyObject(BasicProfileDto.class), EasyMock.eq(false)); } catch
	 * (UserLoginCredentialAlreadyExistsException e) { // TODO Auto-generated catch
	 * block e.printStackTrace(); } catch (ErightsException e) { // TODO
	 * Auto-generated catch block e.printStackTrace(); } EasyMock.expectLastCall();
	 * 
	 * EasyMock.replay(getMocks());
	 * 
	 * List<ProfileRegistrationDto> data =
	 * controller.getProfileRegistrationDtos(request, session);
	 * Assert.assertEquals(profileData, data);
	 * 
	 * BasicProfileDto dto = controller.getForm(request); dto.setUsername("bob");
	 * WebDataBinder binder = new WebDataBinder(dto);
	 * 
	 * controller.initBinder(binder);
	 * 
	 * BindingResult errors = new BindException(dto, "basicProfileDto");
	 * ModelAndView result = controller.processPost(dto, errors, request, response,
	 * session); Assert.assertEquals(0, errors.getErrorCount());
	 * checkRedirect("profile.htm", result); Assert.assertEquals(Boolean.TRUE,
	 * session.getAttribute(BasicProfileController.KEY_SUPRESS_CACHE_REFRESH));
	 * EasyMock.verify(getMocks());
	 * 
	 * 
	 * 
	 * }
	 * 
	 * @Test public void testPostHappyPath2() throws UsernameExistsException,
	 * ServiceLayerException { request.setMethod("POST");
	 * 
	 * SessionHelper.setCustomer(request, customer);
	 * 
	 * EasyMock.expect(adminService.getAdminUserByUsername("bob")).andReturn(null);
	 * try {
	 * EasyMock.expect(customerService.getCustomerByUsername("bob")).andReturn(
	 * customer);
	 * 
	 * 
	 * EasyMock.expect(customerService.getCustomerById(customer.getId())).andReturn(
	 * freshCustomer);
	 * customerService.updateCustomerProfile(EasyMock.eq(customer.getId()),
	 * EasyMock.anyObject(BasicProfileDto.class), EasyMock.eq(false));
	 * EasyMock.expectLastCall(); } catch (ErightsException e) { // TODO
	 * Auto-generated catch block e.printStackTrace(); }
	 * EasyMock.replay(getMocks());
	 * 
	 * BasicProfileDto dto = controller.getForm(request); dto.setUsername("bob");
	 * WebDataBinder binder = new WebDataBinder(dto);
	 * 
	 * controller.initBinder(binder);
	 * 
	 * BindingResult errors = new BindException(dto, "basicProfileDto");
	 * 
	 * 
	 * ModelAndView result = controller.processPost(dto, errors, request, response,
	 * session); Assert.assertEquals(0, errors.getErrorCount());
	 * checkRedirect("profile.htm", result); Assert.assertEquals(Boolean.TRUE,
	 * session.getAttribute(BasicProfileController.KEY_SUPRESS_CACHE_REFRESH));
	 * EasyMock.verify(getMocks());
	 * 
	 * }
	 * 
	 * @Test public void testPostErrorAdminExistsWithUsernameException() throws
	 * ServiceLayerException { request.setMethod("POST");
	 * 
	 * SessionHelper.setCustomer(request, customer);
	 * 
	 * AdminUser admin = new AdminUser();
	 * EasyMock.expect(adminService.getAdminUserByUsername("bob")).andReturn(admin);
	 * 
	 * //EasyMock.expect(customerService.getCustomerById(customer.getId())).
	 * andReturn(freshCustomer);
	 * 
	 * EasyMock.replay(getMocks());
	 * 
	 * BasicProfileDto dto = controller.getForm(request); dto.setUsername("bob");
	 * WebDataBinder binder = new WebDataBinder(dto);
	 * 
	 * controller.initBinder(binder);
	 * 
	 * BindingResult errors = new BindException(dto, "basicProfileDto");
	 * 
	 * ModelAndView result = controller.processPost(dto, errors, request, response,
	 * session); Assert.assertEquals(1, errors.getErrorCount());
	 * checkView("basicProfile", result); EasyMock.verify(getMocks());
	 * 
	 * }
	 * 
	 * @Test public void testPostErrorUserNameExistsException() throws
	 * ServiceLayerException { request.setMethod("POST");
	 * 
	 * SessionHelper.setCustomer(request, customer); try{
	 * EasyMock.expect(adminService.getAdminUserByUsername("bob")).andReturn(null);
	 * EasyMock.expect(customerService.getCustomerByUsername("bob")).andReturn(
	 * customer);
	 * 
	 * //EasyMock.expect(customerService.getCustomerById(customer.getId())).
	 * andReturn(freshCustomer);
	 * customerService.updateCustomerProfile(EasyMock.eq(customer.getId()),
	 * EasyMock.anyObject(BasicProfileDto.class), EasyMock.eq(false)); } catch
	 * (ErightsException e) { // TODO Auto-generated catch block
	 * e.printStackTrace(); }
	 * 
	 * Message msg1 = new Message("error1"); EasyMock.expectLastCall().andThrow(new
	 * UsernameExistsException("oops", msg1));
	 * 
	 * EasyMock.replay(getMocks());
	 * 
	 * BasicProfileDto dto = controller.getForm(request); dto.setUsername("bob");
	 * WebDataBinder binder = new WebDataBinder(dto);
	 * 
	 * controller.initBinder(binder);
	 * 
	 * BindingResult errors = new BindException(dto, "basicProfileDto");
	 * 
	 * ModelAndView result = controller.processPost(dto, errors, request, response,
	 * session); Assert.assertEquals(1, errors.getErrorCount());
	 * checkView("basicProfile", result); EasyMock.verify(getMocks());
	 * 
	 * }
	 * 
	 * @Test public void testPostErrorServiceLayerException() throws
	 * ServiceLayerException { request.setMethod("POST");
	 * 
	 * SessionHelper.setCustomer(request, customer); try{
	 * EasyMock.expect(adminService.getAdminUserByUsername("bob")).andReturn(null);
	 * EasyMock.expect(customerService.getCustomerByUsername("bob")).andReturn(
	 * customer);
	 * 
	 * //EasyMock.expect(customerService.getCustomerById(customer.getId())).
	 * andReturn(freshCustomer);
	 * customerService.updateCustomerProfile(EasyMock.eq(customer.getId()),
	 * EasyMock.anyObject(BasicProfileDto.class), EasyMock.eq(false)); Message msg1
	 * = new Message("error1"); EasyMock.expectLastCall().andThrow(new
	 * ServiceLayerException("oops", msg1)); } catch (ErightsException e) { // TODO
	 * Auto-generated catch block e.printStackTrace(); }
	 * EasyMock.replay(getMocks());
	 * 
	 * BasicProfileDto dto = controller.getForm(request); dto.setUsername("bob");
	 * WebDataBinder binder = new WebDataBinder(dto);
	 * 
	 * controller.initBinder(binder);
	 * 
	 * BindingResult errors = new BindException(dto, "basicProfileDto");
	 * 
	 * ModelAndView result = controller.processPost(dto, errors, request, response,
	 * session); Assert.assertEquals(1, errors.getErrorCount());
	 * checkView("basicProfile", result); EasyMock.verify(getMocks());
	 * 
	 * }
	 * 
	 *//**
		 * Username Exists
		 */
	/*
	 * @Test public void testPostErrorUsernameExists() throws
	 * UsernameExistsException, ServiceLayerException { request.setMethod("POST");
	 * 
	 * SessionHelper.setCustomer(request, customer);
	 * 
	 * //EasyMock.expect(customerService.getCustomerById(customer.getId())).
	 * andReturn(freshCustomer);
	 * 
	 * EasyMock.expect(adminService.getAdminUserByUsername("bob")).andReturn(null);
	 * try {
	 * EasyMock.expect(customerService.getCustomerByUsername("bob")).andReturn(
	 * customer2); } catch (ErightsException e) { // TODO Auto-generated catch block
	 * e.printStackTrace(); }
	 * 
	 * EasyMock.replay(getMocks());
	 * 
	 * BasicProfileDto dto = controller.getForm(request); dto.setUsername("bob");
	 * WebDataBinder binder = new WebDataBinder(dto);
	 * 
	 * controller.initBinder(binder);
	 * 
	 * BindingResult errors = new BindException(dto, "basicProfileDto");
	 * ModelAndView result = controller.processPost(dto, errors, request, response,
	 * session); Assert.assertEquals(1, errors.getErrorCount());
	 * checkView("basicProfile", result); EasyMock.verify(getMocks());
	 * 
	 * }
	 * 
	 *//**
		 * Basic Validation Errors and Existing Customer
		 */
	/*
	 * @Test public void testPostErrorValidationAndExistingUser() {
	 * request.setMethod("POST");
	 * 
	 * SessionHelper.setCustomer(request, customer);
	 * 
	 * //EasyMock.expect(customerService.getCustomerById(customer.getId())).
	 * andReturn(freshCustomer);
	 * 
	 * EasyMock.expect(adminService.getAdminUserByUsername("bob")).andReturn(null);
	 * try {
	 * EasyMock.expect(customerService.getCustomerByUsername("bob")).andReturn(
	 * customer2); } catch (ErightsException e) { // TODO Auto-generated catch block
	 * e.printStackTrace(); }
	 * 
	 * EasyMock.replay(getMocks());
	 * 
	 * BasicProfileDto dto = controller.getForm(request); dto.setUsername("bob");
	 * WebDataBinder binder = new WebDataBinder(dto);
	 * 
	 * controller.initBinder(binder);
	 * 
	 * BindingResult errors = new BindException(dto, "basicProfileDto");
	 * errors.reject("error1"); errors.reject("error2"); int before =
	 * errors.getErrorCount(); ModelAndView result = controller.processPost(dto,
	 * errors, request, response, session); Assert.assertEquals(before + 1,
	 * errors.getErrorCount()); checkView("basicProfile", result);
	 * EasyMock.verify(getMocks());
	 * 
	 * }
	 * 
	 *//**
		 * Basic Validation Errors Only
		 */
	/*
	 * @Test public void testPostErrorValidationOnly1() { request.setMethod("POST");
	 * 
	 * SessionHelper.setCustomer(request, customer);
	 * 
	 * EasyMock.expect(adminService.getAdminUserByUsername("bob")).andReturn(null);
	 * try {
	 * EasyMock.expect(customerService.getCustomerByUsername("bob")).andReturn(null)
	 * ; } catch (ErightsException e) { // TODO Auto-generated catch block
	 * e.printStackTrace(); }
	 * 
	 * //EasyMock.expect(customerService.getCustomerById(customer.getId())).
	 * andReturn(freshCustomer);
	 * 
	 * EasyMock.replay(getMocks());
	 * 
	 * BasicProfileDto dto = controller.getForm(request); dto.setUsername("bob");
	 * WebDataBinder binder = new WebDataBinder(dto);
	 * 
	 * controller.initBinder(binder);
	 * 
	 * BindingResult errors = new BindException(dto, "basicProfileDto");
	 * errors.reject("error1"); errors.reject("error2"); int before =
	 * errors.getErrorCount(); ModelAndView result = controller.processPost(dto,
	 * errors, request, response, session); Assert.assertEquals(before,
	 * errors.getErrorCount()); checkView("basicProfile", result);
	 * EasyMock.verify(getMocks());
	 * 
	 * }
	 * 
	 *//**
		 * Basic Validation Errors Only
		 */
	/*
	 * @Test public void testPostErrorValidationOnly2() { request.setMethod("POST");
	 * 
	 * SessionHelper.setCustomer(request, customer);
	 * 
	 * //EasyMock.expect(customerService.getCustomerById(customer.getId())).
	 * andReturn(freshCustomer);
	 * 
	 * EasyMock.replay(getMocks());
	 * 
	 * BasicProfileDto dto = controller.getForm(request); dto.setUsername("bob");
	 * WebDataBinder binder = new WebDataBinder(dto);
	 * 
	 * controller.initBinder(binder);
	 * 
	 * BindingResult errors = new BindException(dto, "basicProfileDto");
	 * errors.rejectValue("username", "error.code"); int before =
	 * errors.getErrorCount(); ModelAndView result = controller.processPost(dto,
	 * errors, request, response, session); Assert.assertEquals(before,
	 * errors.getErrorCount()); checkView("basicProfile", result);
	 * EasyMock.verify(getMocks());
	 * 
	 * }
	 * 
	 * void checkRedirect(String contextRelativeViewName, ModelAndView mav) { View
	 * view = mav.getView(); if (view == null) { Assert.assertEquals("redirect:/" +
	 * contextRelativeViewName, mav.getViewName()); } else { Assert.assertTrue(view
	 * instanceof RedirectView); RedirectView rv = (RedirectView) view;
	 * Assert.assertEquals(rv.getUrl(), contextRelativeViewName); } }
	 * 
	 * void checkView(String viewName, ModelAndView mav) { View view =
	 * mav.getView(); if (view == null) { Assert.assertEquals(viewName,
	 * mav.getViewName()); } else { Assert.fail("unexpected view type"); } }
	 * 
	 */}
