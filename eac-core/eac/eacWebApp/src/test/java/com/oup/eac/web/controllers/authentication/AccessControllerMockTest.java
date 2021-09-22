package com.oup.eac.web.controllers.authentication;

import static org.easymock.EasyMock.anyObject;
import static org.easymock.EasyMock.expect;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import javax.naming.NamingException;
import javax.servlet.http.Cookie;

import junit.framework.Assert;

import org.easymock.EasyMock;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;
import org.springframework.web.servlet.view.RedirectView;

import com.oup.eac.domain.ActivationCode;
import com.oup.eac.domain.ActivationCodeRegistration;
import com.oup.eac.domain.ActivationCodeRegistrationDefinition;
import com.oup.eac.domain.Customer;
import com.oup.eac.domain.Customer.CustomerType;
import com.oup.eac.domain.ErightsDenyReason;
import com.oup.eac.domain.ErightsLicenceDecision;
import com.oup.eac.domain.LinkedRegistration;
import com.oup.eac.domain.Product;
import com.oup.eac.domain.ProductRegistration;
import com.oup.eac.domain.ProductRegistrationDefinition;
import com.oup.eac.domain.RegisterableProduct;
import com.oup.eac.domain.RegisterableProduct.RegisterableType;
import com.oup.eac.domain.Registration;
import com.oup.eac.domain.RegistrationActivation.ActivationStrategy;
import com.oup.eac.domain.RegistrationDefinition;
import com.oup.eac.domain.SelfRegistrationActivation;
import com.oup.eac.domain.UrlSkin;
import com.oup.eac.domain.User.EmailVerificationState;
import com.oup.eac.domain.ValidatedRegistrationActivation;
import com.oup.eac.domain.WhiteListUrl;
import com.oup.eac.dto.CustomerRegistrationsDto;
import com.oup.eac.dto.EnforceableProductDto;
import com.oup.eac.dto.EnforceableProductUrlDto;
import com.oup.eac.dto.LicenceDto;
import com.oup.eac.dto.RegistrationActivationDto;
import com.oup.eac.service.CustomerService;
import com.oup.eac.service.DomainSkinResolverService;
import com.oup.eac.service.LicenceService;
import com.oup.eac.service.ProductService;
import com.oup.eac.service.RegistrationDefinitionService;
import com.oup.eac.service.RegistrationService;
import com.oup.eac.service.ServiceLayerException;
import com.oup.eac.service.WhiteListUrlService;
import com.oup.eac.service.exceptions.CustomerNotFoundServiceLayerException;
import com.oup.eac.service.exceptions.NoRegisterableProductFoundException;
import com.oup.eac.web.controllers.context.RequestContext;
import com.oup.eac.web.controllers.helpers.RegistrationNotAllowedMessageCodeSource;
import com.oup.eac.web.controllers.helpers.SessionHelper;
import com.oup.eac.web.controllers.registration.EACViews;

/**
 * A mock test for the access controller.
 * 
 * @author David Hay
 * @see com.oup.eac.web.controllers.AccessControllerTest
 */
public class AccessControllerMockTest /* extends AbstractMockTest */ {
	/*
	 * 
	 * private static final String PROTECTED_RESOURCE =
	 * "http://test.oup.com/protected/resource.htm"; // private static final String
	 * PROTECTED_RESOURCE = "profile.htm"; private static final String COOKIE_VALUE
	 * = "cookieValue"; private static final String EAC = "EAC"; private static
	 * final String URL = "url"; private static final String DENY_REASON =
	 * "denyReason"; private static final String SUCCESS_VIEW = PROTECTED_RESOURCE +
	 * "?ERSESSION=" + COOKIE_VALUE; private static final String VALIDATOR_EMAIL =
	 * "validator.email@test.com"; private static final String PRODUCT_EMAIL =
	 * "product.email@test.com"; private static final String KEY_PRODUCT_NAME =
	 * "product"; private static final String PRODUCT_NAME = "productName"; private
	 * static final String KEY_EMAIL = "email"; private static final String SLA =
	 * "service level agreement"; private static final String KEY_SLA = "sla";
	 * private static final String REGISTRATION_ID = "ABC123";
	 * 
	 * // mocks private RegistrationDefinitionService registrationDefinitionService;
	 * 
	 * private DomainSkinResolverService domainSkinResolverService; private
	 * CustomerService customerService; private RegistrationService
	 * registrationService; private ProductService productService; private
	 * LicenceService licenceService; private ServletRequestDataBinder binder;
	 * 
	 * // controller private AccessController controller;
	 * 
	 * // test variables private MockHttpServletRequest request; private
	 * MockHttpServletResponse response; private MockHttpSession session; private
	 * Customer customer; private Cookie eacCookie; private RegisterableProduct
	 * regProd; private ProductRegistrationDefinition prd; private
	 * ActivationCodeRegistrationDefinition acRegDef; private ProductRegistration
	 * registration; private ActivationCodeRegistration acRegistration; private
	 * Locale locale; private RegistrationNotAllowedMessageCodeSource
	 * registrationNotAllowedMessageCodeSource; private WhiteListUrlService
	 * whiteListUrl; private List<String> urlList = new ArrayList<String>(); private
	 * String WhiteListURL = "http://test.oup.com/protected/resource.htm"; private
	 * Product product = new RegisterableProduct(); private EnforceableProductDto
	 * enforceableProduct; private RegistrationActivationDto regDto; public
	 * AccessControllerMockTest() throws NamingException { super(); }
	 * 
	 * @Before public void setup() { registrationDefinitionService =
	 * EasyMock.createMock(RegistrationDefinitionService.class);
	 * domainSkinResolverService =
	 * EasyMock.createMock(DomainSkinResolverService.class); customerService =
	 * EasyMock.createMock(CustomerService.class); registrationService =
	 * EasyMock.createMock(RegistrationService.class); productService =
	 * EasyMock.createMock(ProductService.class); licenceService =
	 * EasyMock.createMock(LicenceService.class); binder =
	 * EasyMock.createMock(ServletRequestDataBinder.class); whiteListUrl =
	 * EasyMock.createMock(WhiteListUrlService.class); product =
	 * EasyMock.createMock(Product.class);
	 * 
	 * registrationNotAllowedMessageCodeSource =
	 * EasyMock.createMock(RegistrationNotAllowedMessageCodeSource.class);
	 * WhiteListUrl url = new WhiteListUrl(); url.setUrl(WhiteListURL);
	 * urlList.add(WhiteListURL);
	 * 
	 * 
	 * List<EnforceableProductUrlDto> productUrls = new
	 * ArrayList<EnforceableProductUrlDto>(); EnforceableProductUrlDto urlDto= new
	 * EnforceableProductUrlDto("http", "www.google.com", "test", null, null, null);
	 * productUrls.add(urlDto); product.setProductUrls(productUrls ); controller =
	 * new AccessController(registrationDefinitionService,
	 * domainSkinResolverService, customerService, registrationService,
	 * productService, licenceService,registrationNotAllowedMessageCodeSource,
	 * whiteListUrl);
	 * 
	 * setMocks(registrationDefinitionService, domainSkinResolverService,
	 * customerService, registrationService, productService,
	 * licenceService,whiteListUrl,product); request = new MockHttpServletRequest();
	 * response = new MockHttpServletResponse(); session = new MockHttpSession();
	 * request.setSession(session); enforceableProduct = new
	 * EnforceableProductDto(); eacCookie = new Cookie(EAC, COOKIE_VALUE); customer
	 * = new Customer(); locale = Locale.FRANCE;
	 * 
	 * 
	 * customer.setCustomerType(CustomerType.SELF_SERVICE);
	 * customer.setEmailVerificationState(EmailVerificationState.VERIFIED); prd =
	 * new ProductRegistrationDefinition(); regProd = new RegisterableProduct();
	 * 
	 * SessionLocaleResolver resolver = new SessionLocaleResolver();
	 * request.setAttribute(DispatcherServlet.LOCALE_RESOLVER_ATTRIBUTE, resolver);
	 * resolver.setLocale(request, response, locale); registration = new
	 * ProductRegistration(); ValidatedRegistrationActivation activation = new
	 * ValidatedRegistrationActivation();
	 * activation.setValidatorEmail(VALIDATOR_EMAIL);
	 * prd.setRegistrationActivation(activation); prd.setProduct(regProd);
	 * regProd.setLinkedProducts( new ArrayList<Product>());
	 * regProd.setProductName(PRODUCT_NAME); regProd.setServiceLevelAgreement(SLA);
	 * regProd.setEmail(PRODUCT_EMAIL);
	 * regProd.setRegisterableType(RegisterableType.SELF_REGISTERABLE);
	 * regProd.setId(REGISTRATION_ID);
	 * regProd.setActivationStrategy(ActivationStrategy.VALIDATED.toString());
	 * regProd.setValidatorEmail("kkk@hhh.ccc"); Assert.assertEquals(locale,
	 * SessionHelper.getLocale(request)); registration.setId(REGISTRATION_ID);
	 * registration.setCustomer(customer);
	 * registration.setRegistrationDefinition(prd);
	 * registration.setActivated(false); registration.setAwaitingValidation(false);
	 * registration.setCompleted(false); registration.setDenied(false);
	 * registration.setEnabled(false); registration.setExpired(false);
	 * Set<LinkedRegistration> linkedRegistrations = new
	 * HashSet<LinkedRegistration>();
	 * registration.setLinkedRegistrations(linkedRegistrations); acRegDef = new
	 * ActivationCodeRegistrationDefinition(); acRegistration = new
	 * ActivationCodeRegistration(); regDto = new
	 * RegistrationActivationDto(customer, locale,
	 * "http://test.oup.com/protected/resource.htm", registration, prd);
	 * 
	 * }
	 * 
	 *//**
		 * request invalid --> error
		 * 
		 * @throws Exception
		 */
	/*
	 * @Test public void testErrorInvalidUserRequest() throws Exception {
	 * binder.registerCustomEditor(EasyMock.eq(String.class),
	 * EasyMock.anyObject(StringTrimmerEditor.class)); EasyMock.expectLastCall();
	 * EasyMock.expect(whiteListUrl.getUrls()).andReturn(urlList);
	 * 
	 * List<Registration<? extends ProductRegistrationDefinition>> registrations =
	 * new ArrayList<Registration<? extends ProductRegistrationDefinition>>();
	 * List<LicenceDto> licences = new ArrayList<LicenceDto>();
	 * CustomerRegistrationsDto custRegDto = new CustomerRegistrationsDto(customer,
	 * registrations, licences);
	 * expect(registrationService.getEntitlementsForCustomerRegistrations(customer,
	 * null,true)).andReturn(custRegDto);
	 * 
	 * ProductRegistrationDefinition regDef= new ProductRegistrationDefinition();
	 * regDef.setProduct(regProd);
	 * expect(registrationService.getCompletedRegistrationInformation(regProd,
	 * customer)).andReturn(true); EasyMock.expectLastCall();
	 * EasyMock.replay(getMocks());
	 * 
	 * controller.initBinder(request, binder); ModelAndView result =
	 * controller.handleRequest(request, response);
	 * 
	 * 
	 * String view = getView(result); Assert.assertEquals(EACViews.ERROR_PAGE,
	 * view); }
	 * 
	 *//**
		 * request valid, not cookie present --> login
		 * 
		 * @throws Exception
		 */
	/*
	 * @Test public void testBarrierLoginNoPrimaryDomainCookie() throws Exception {
	 * 
	 * request.setParameter(URL, PROTECTED_RESOURCE);
	 * 
	 * EasyMock.expect(domainSkinResolverService.getSkinFromDomain(
	 * PROTECTED_RESOURCE)).andReturn(null);
	 * EasyMock.expect(productService.getRegisterableProductByUrl(PROTECTED_RESOURCE
	 * )).andThrow(new NoRegisterableProductFoundException());
	 * EasyMock.expect(whiteListUrl.getUrls()).andReturn(urlList);
	 * 
	 * List<Registration<? extends ProductRegistrationDefinition>> registrations =
	 * new ArrayList<Registration<? extends ProductRegistrationDefinition>>();
	 * List<LicenceDto> licences = new ArrayList<LicenceDto>();
	 * CustomerRegistrationsDto custRegDto = new CustomerRegistrationsDto(customer,
	 * registrations, licences);
	 * expect(registrationService.getEntitlementsForCustomerRegistrations(customer,
	 * null,true)).andReturn(custRegDto);
	 * 
	 * ProductRegistrationDefinition regDef= new ProductRegistrationDefinition();
	 * regDef.setProduct(regProd);
	 * expect(registrationService.getCompletedRegistrationInformation(regProd,
	 * customer)).andReturn(true);
	 * 
	 * EasyMock.replay(getMocks()); ModelAndView result =
	 * controller.handleRequest(request, response);
	 * 
	 * String view = getView(result); Assert.assertEquals(PROTECTED_RESOURCE,
	 * SessionHelper.getForwardUrl(request));
	 * Assert.assertEquals(EACViews.LOGIN_VIEW + "?url=" + PROTECTED_RESOURCE,
	 * view); }
	 * 
	 *//**
		 * request valid, cookie present, cookie not valid --> error
		 * 
		 * @throws Exception
		 */
	/*
	 * @Test public void testBarrierLoginInvalidPrimaryDomainCookie() throws
	 * Exception {
	 * 
	 * request.setParameter(URL, PROTECTED_RESOURCE);
	 * 
	 * request.setCookies(eacCookie);
	 * 
	 * EasyMock.expect(customerService.getCustomerFromSession(COOKIE_VALUE)).
	 * andThrow(new CustomerNotFoundServiceLayerException());
	 * 
	 * EasyMock.expect(domainSkinResolverService.getSkinFromDomain(
	 * PROTECTED_RESOURCE)).andReturn(null);
	 * EasyMock.expect(productService.getRegisterableProductByUrl(PROTECTED_RESOURCE
	 * )).andThrow(new NoRegisterableProductFoundException());
	 * EasyMock.expect(whiteListUrl.getUrls()).andReturn(urlList);
	 * 
	 * List<Registration<? extends ProductRegistrationDefinition>> registrations =
	 * new ArrayList<Registration<? extends ProductRegistrationDefinition>>();
	 * List<LicenceDto> licences = new ArrayList<LicenceDto>();
	 * CustomerRegistrationsDto custRegDto = new CustomerRegistrationsDto(customer,
	 * registrations, licences);
	 * expect(registrationService.getEntitlementsForCustomerRegistrations(customer,
	 * null,true)).andReturn(custRegDto);
	 * 
	 * ProductRegistrationDefinition regDef= new ProductRegistrationDefinition();
	 * regDef.setProduct(regProd);
	 * expect(registrationService.getCompletedRegistrationInformation(regProd,
	 * customer)).andReturn(true);
	 * 
	 * EasyMock.replay(getMocks()); ModelAndView result =
	 * controller.handleRequest(request, response);
	 * 
	 * String view = getView(result); Assert.assertEquals(PROTECTED_RESOURCE,
	 * SessionHelper.getForwardUrl(request));
	 * Assert.assertEquals(EACViews.LOGIN_VIEW + "?url=" + PROTECTED_RESOURCE,
	 * view); }
	 * 
	 *//**
		 * request valid, cookie present, cookie valid, no product associated with
		 * request --> error
		 * 
		 * @throws Exception
		 */
	/*
	 * @Test public void testErrorNoProductAssociatedWithRequest1() throws Exception
	 * { request.setParameter(URL, PROTECTED_RESOURCE);
	 * request.setCookies(eacCookie); request.setParameter(DENY_REASON,
	 * Integer.toString(ErightsDenyReason.DENY_ALL_LICENSE_DENIED.getReasonCode()));
	 * 
	 * EasyMock.expect(domainSkinResolverService.getSkinFromDomain(
	 * PROTECTED_RESOURCE)).andReturn(null);
	 * EasyMock.expect(registrationDefinitionService.
	 * getProductRegistrationDefinitionByProduct(null)).andThrow(new
	 * ServiceLayerException());
	 * EasyMock.expect(productService.getRegisterableProductByUrl(PROTECTED_RESOURCE
	 * )).andThrow(new NoRegisterableProductFoundException());
	 * EasyMock.expect(whiteListUrl.getUrls()).andReturn(urlList);
	 * EasyMock.expect(customerService.getCustomerFromSession(COOKIE_VALUE)).
	 * andReturn(customer);
	 * 
	 * 
	 * List<Registration<? extends ProductRegistrationDefinition>> registrations =
	 * new ArrayList<Registration<? extends ProductRegistrationDefinition>>();
	 * List<LicenceDto> licences = new ArrayList<LicenceDto>();
	 * CustomerRegistrationsDto custRegDto = new CustomerRegistrationsDto(customer,
	 * registrations, licences);
	 * expect(registrationService.getEntitlementsForCustomerRegistrations(customer,
	 * null,true)).andReturn(custRegDto);
	 * 
	 * ProductRegistrationDefinition regDef= new ProductRegistrationDefinition();
	 * regDef.setProduct(regProd);
	 * expect(registrationService.getCompletedRegistrationInformation(regProd,
	 * customer)).andReturn(true);
	 * 
	 * EasyMock.replay(getMocks()); ModelAndView result =
	 * controller.handleRequest(request, response);
	 * 
	 * String view = getView(result); Assert.assertEquals(SUCCESS_VIEW,
	 * SessionHelper.getForwardUrl(request));
	 * Assert.assertEquals(EACViews.REGISTRATION_NOT_ALLOWED, view); }
	 * 
	 *//**
		 * request valid, cookie present, cookie valid, no product associated with
		 * request --> error
		 * 
		 * @throws Exception
		 */
	/*
	 * @Test public void testErrorNoProductAssociatedWithRequest2() throws Exception
	 * { request.setParameter(URL, PROTECTED_RESOURCE);
	 * request.setCookies(eacCookie); request.setParameter(DENY_REASON,
	 * Integer.toString(ErightsDenyReason.DENY_ALL_LICENSE_DENIED.getReasonCode()));
	 * EasyMock.expect(whiteListUrl.getUrls()).andReturn(urlList);
	 * EasyMock.expect(domainSkinResolverService.getSkinFromDomain(
	 * PROTECTED_RESOURCE)).andReturn(null);
	 * EasyMock.expect(registrationDefinitionService.
	 * getProductRegistrationDefinitionByProduct(null)).andThrow(new
	 * ServiceLayerException());
	 * EasyMock.expect(productService.getRegisterableProductByUrl(PROTECTED_RESOURCE
	 * )).andThrow(new ServiceLayerException());
	 * 
	 * EasyMock.expect(customerService.getCustomerFromSession(COOKIE_VALUE)).
	 * andReturn(customer);
	 * 
	 * 
	 * List<Registration<? extends ProductRegistrationDefinition>> registrations =
	 * new ArrayList<Registration<? extends ProductRegistrationDefinition>>();
	 * List<LicenceDto> licences = new ArrayList<LicenceDto>();
	 * CustomerRegistrationsDto custRegDto = new CustomerRegistrationsDto(customer,
	 * registrations, licences);
	 * expect(registrationService.getEntitlementsForCustomerRegistrations(customer,
	 * null,true)).andReturn(custRegDto);
	 * 
	 * ProductRegistrationDefinition regDef= new ProductRegistrationDefinition();
	 * regDef.setProduct(regProd);
	 * expect(registrationService.getCompletedRegistrationInformation(regProd,
	 * customer)).andReturn(true);
	 * 
	 * EasyMock.replay(getMocks()); ModelAndView result =
	 * controller.handleRequest(request, response);
	 * 
	 * String view = getView(result); Assert.assertEquals(SUCCESS_VIEW,
	 * SessionHelper.getForwardUrl(request));
	 * Assert.assertEquals(EACViews.REGISTRATION_NOT_ALLOWED, view); }
	 * 
	 *//**
		 * request valid, cookie present, cookie valid, no product associated with
		 * request --> error
		 * 
		 * @throws Exception
		 */
	/*
	 * @Test public void testErrorNoProductAssociatedWithRequest3() throws Exception
	 * { request.setParameter(URL, PROTECTED_RESOURCE);
	 * request.setCookies(eacCookie); request.setParameter(DENY_REASON,
	 * Integer.toString(ErightsDenyReason.DENY_ALL_LICENSE_DENIED.getReasonCode()));
	 * 
	 * EasyMock.expect(domainSkinResolverService.getSkinFromDomain(
	 * PROTECTED_RESOURCE)).andReturn(null);
	 * EasyMock.expect(registrationDefinitionService.
	 * getProductRegistrationDefinitionByProduct(regProd)).andThrow(new
	 * ServiceLayerException());
	 * EasyMock.expect(productService.getRegisterableProductByUrl(PROTECTED_RESOURCE
	 * )).andReturn(regProd);
	 * 
	 * EasyMock.expect(customerService.getCustomerFromSession(COOKIE_VALUE)).
	 * andReturn(customer);
	 * EasyMock.expect(whiteListUrl.getUrls()).andReturn(urlList);
	 * 
	 * List<Registration<? extends ProductRegistrationDefinition>> registrations =
	 * new ArrayList<Registration<? extends ProductRegistrationDefinition>>();
	 * List<LicenceDto> licences = new ArrayList<LicenceDto>();
	 * CustomerRegistrationsDto custRegDto = new CustomerRegistrationsDto(customer,
	 * registrations, licences);
	 * expect(registrationService.getEntitlementsForCustomerRegistrations(customer,
	 * null,true)).andReturn(custRegDto);
	 * 
	 * ProductRegistrationDefinition regDef= new ProductRegistrationDefinition();
	 * regDef.setProduct(regProd);
	 * expect(registrationService.getCompletedRegistrationInformation(regProd,
	 * customer)).andReturn(true);
	 * 
	 * EasyMock.replay(getMocks()); ModelAndView result =
	 * controller.handleRequest(request, response);
	 * 
	 * String view = getView(result); Assert.assertEquals(SUCCESS_VIEW,
	 * SessionHelper.getForwardUrl(request));
	 * Assert.assertEquals(EACViews.REGISTRATION_NOT_ALLOWED, view); }
	 * 
	 *//**
		 * request valid, cookie present, cookie valid, no product associated with
		 * request --> error
		 * 
		 * @throws Exception
		 */
	/*
	 * @Test public void testSuccessNoDenyReasonAvailable1() throws Exception {
	 * 
	 * request.setParameter(URL, PROTECTED_RESOURCE); request.setCookies(eacCookie);
	 * 
	 * EasyMock.expect(domainSkinResolverService.getSkinFromDomain(
	 * PROTECTED_RESOURCE)).andReturn(new UrlSkin());
	 * EasyMock.expect(productService.getRegisterableProductByUrl(PROTECTED_RESOURCE
	 * )).andReturn(regProd);
	 * EasyMock.expect(customerService.getCustomerFromSession(COOKIE_VALUE)).
	 * andReturn(customer);
	 * EasyMock.expect(whiteListUrl.getUrls()).andReturn(urlList);
	 * List<Registration<? extends ProductRegistrationDefinition>> registrations =
	 * new ArrayList<Registration<? extends ProductRegistrationDefinition>>();
	 * List<LicenceDto> licences = new ArrayList<LicenceDto>();
	 * CustomerRegistrationsDto custRegDto = new CustomerRegistrationsDto(customer,
	 * registrations, licences);
	 * expect(registrationService.getEntitlementsForCustomerRegistrations(customer,
	 * null,true)).andReturn(custRegDto);
	 * 
	 * ProductRegistrationDefinition regDef= new ProductRegistrationDefinition();
	 * regDef.setProduct(regProd);
	 * expect(registrationService.getCompletedRegistrationInformation(regProd,
	 * customer)).andReturn(true); EasyMock.replay(getMocks()); ModelAndView result
	 * = controller.handleRequest(request, response);
	 * 
	 * String view = getView(result); Assert.assertEquals(SUCCESS_VIEW,
	 * SessionHelper.getForwardUrl(request)); Assert.assertEquals(SUCCESS_VIEW,
	 * view); }
	 * 
	 * @Test public void
	 * testBarrierProductRegistrationForCustomerNotRegisteredAndNotActivationCode1InvalidRegisterableProduct
	 * () throws Exception {
	 * regProd.setRegisterableType(RegisterableType.ADMIN_REGISTERABLE);
	 * 
	 * request.setParameter(URL, PROTECTED_RESOURCE);
	 * request.setParameter(DENY_REASON,
	 * String.valueOf(ErightsDenyReason.DENY_ALL_LICENSE_DENIED.getReasonCode()));
	 * request.setCookies(eacCookie);
	 * 
	 * EasyMock.expect(domainSkinResolverService.getSkinFromDomain(
	 * PROTECTED_RESOURCE)).andReturn(null);
	 * EasyMock.expect(registrationDefinitionService.
	 * getProductRegistrationDefinitionByProduct(regProd)).andReturn(prd);
	 * EasyMock.expect(productService.getRegisterableProductByUrl(PROTECTED_RESOURCE
	 * )).andReturn(regProd);
	 * EasyMock.expect(customerService.getCustomerFromSession(COOKIE_VALUE)).
	 * andReturn(customer);
	 * EasyMock.expect(whiteListUrl.getUrls()).andReturn(urlList);
	 * 
	 * 
	 * List<Registration<? extends ProductRegistrationDefinition>> registrations =
	 * new ArrayList<Registration<? extends ProductRegistrationDefinition>>();
	 * List<LicenceDto> licences = new ArrayList<LicenceDto>();
	 * CustomerRegistrationsDto custRegDto = new CustomerRegistrationsDto(customer,
	 * registrations, licences);
	 * expect(registrationService.getEntitlementsForCustomerRegistrations(customer,
	 * null,true)).andReturn(custRegDto);
	 * 
	 * ProductRegistrationDefinition regDef= new ProductRegistrationDefinition();
	 * regDef.setProduct(regProd);
	 * expect(registrationService.getCompletedRegistrationInformation(regProd,
	 * customer)).andReturn(true);
	 * 
	 * EasyMock.replay(getMocks()); ModelAndView result =
	 * controller.handleRequest(request, response);
	 * 
	 * String view = getView(result);
	 * 
	 * Assert.assertEquals(EACViews.REGISTRATION_NOT_ALLOWED, view); }
	 * 
	 * @Ignore
	 * 
	 * @Test public void
	 * testBarrierProductRegistrationForCustomerNotRegisteredAndNotActivationCode1()
	 * throws Exception {
	 * 
	 * checkCustomerIsRegisteredAndProcessRegistration(null);
	 * 
	 * EasyMock.expect(registrationService.saveProductRegistration(customer,
	 * prd)).andReturn(registration);
	 * EasyMock.expect(whiteListUrl.getUrls()).andReturn(urlList);
	 * 
	 * List<Registration<? extends ProductRegistrationDefinition>> registrations =
	 * new ArrayList<Registration<? extends ProductRegistrationDefinition>>();
	 * List<LicenceDto> licences = new ArrayList<LicenceDto>();
	 * 
	 * CustomerRegistrationsDto custRegDto = new CustomerRegistrationsDto(customer,
	 * registrations, licences);
	 * expect(registrationService.getEntitlementsForCustomerRegistrations(customer,
	 * null,true)).andReturn(custRegDto);
	 * 
	 * ProductRegistrationDefinition regDef= new ProductRegistrationDefinition();
	 * regDef.setProduct(regProd);
	 * 
	 * expect(registrationService.getCompletedRegistrationInformation(regProd,
	 * customer)).andReturn(true);
	 * expect(productService.getEnforceableProductByErightsId(regProd.getId())).
	 * andReturn(enforceableProduct);
	 * //EasyMock.expect(licenceService.getLicensesForUserProduct(EasyMock.anyObject
	 * (Customer.class), EasyMock.anyObject())).andThrow(new
	 * ServiceLayerException()); //
	 * EasyMock.expect(licenceService.getLicensesForUserProduct(customer,
	 * regProd)).andStubReturn(licences); EasyMock.replay(getMocks()); ModelAndView
	 * result = controller.handleRequest(request, response);
	 * 
	 * 
	 * 
	 * String view = getView(result);
	 * 
	 * Assert.assertEquals(EACViews.PRODUCT_REGISTRATION_VIEW, view);
	 * Assert.assertEquals(REGISTRATION_ID,
	 * SessionHelper.getRegistrationId(request)); }
	 * 
	 * @Ignore
	 * 
	 * @Test public void
	 * testBarrierProductRegistrationForCustomerNotRegisteredAndNotActivationCode2()
	 * throws Exception {
	 * checkCustomerIsRegisteredAndProcessRegistration(registration);
	 * SessionHelper.setReregister(request);
	 * 
	 * EasyMock.expect(registrationService.saveProductRegistration(customer,
	 * prd)).andReturn(registration);
	 * EasyMock.expect(whiteListUrl.getUrls()).andReturn(urlList);
	 * List<Registration<? extends ProductRegistrationDefinition>> registrations =
	 * new ArrayList<Registration<? extends ProductRegistrationDefinition>>(); //
	 * registrations.add(registration); List<LicenceDto> licences = new
	 * ArrayList<LicenceDto>(); CustomerRegistrationsDto custRegDto = new
	 * CustomerRegistrationsDto(customer, registrations, licences);
	 * expect(registrationService.getEntitlementsForCustomerRegistrations(customer,
	 * null,true)).andReturn(custRegDto);
	 * 
	 * ProductRegistrationDefinition regDef= new ProductRegistrationDefinition();
	 * regDef.setProduct(regProd);
	 * expect(registrationService.getCompletedRegistrationInformation(regProd,
	 * customer)).andReturn(true);
	 * expect(productService.getEnforceableProductByErightsId(regProd.getId())).
	 * andReturn(enforceableProduct);
	 * 
	 * 
	 * EasyMock.expectLastCall(); EasyMock.replay(getMocks()); ModelAndView result =
	 * controller.handleRequest(request, response);
	 * 
	 * 
	 * 
	 * String view = getView(result);
	 * 
	 * Assert.assertEquals(EACViews.PRODUCT_REGISTRATION_VIEW, view);
	 * Assert.assertEquals(REGISTRATION_ID,
	 * SessionHelper.getRegistrationId(request)); }
	 * 
	 * @Test public void
	 * testBarrierCustomerIsNotRegisteredAndActivationCodeNotAvailable() throws
	 * Exception {
	 * 
	 * checkCustomerIsRegisteredAndProcessRegistration(acRegistration, acRegDef);
	 * SessionHelper.setReregister(request);
	 * EasyMock.expect(whiteListUrl.getUrls()).andReturn(urlList);
	 * List<Registration<? extends ProductRegistrationDefinition>> registrations =
	 * new ArrayList<Registration<? extends ProductRegistrationDefinition>>();
	 * List<LicenceDto> licences = new ArrayList<LicenceDto>();
	 * CustomerRegistrationsDto custRegDto = new CustomerRegistrationsDto(customer,
	 * registrations, licences);
	 * expect(registrationService.getEntitlementsForCustomerRegistrations(customer,
	 * null,true)).andReturn(custRegDto);
	 * 
	 * ProductRegistrationDefinition regDef= new ProductRegistrationDefinition();
	 * regDef.setProduct(regProd);
	 * expect(registrationService.getCompletedRegistrationInformation(regProd,
	 * customer)).andReturn(true); EasyMock.replay(getMocks()); ModelAndView result
	 * = controller.handleRequest(request, response);
	 * 
	 * 
	 * 
	 * String view = getView(result);
	 * 
	 * Assert.assertEquals(EACViews.INTERNAL_ACTIVATION_CODE_VIEW, view); }
	 * 
	 * @Test public void testSuccessActivationCodeAvailable() throws Exception {
	 * 
	 * ActivationCode activationCode = new ActivationCode();
	 * SessionHelper.setActivationCode(request, activationCode);
	 * checkCustomerIsRegisteredAndProcessRegistration(acRegistration, acRegDef);
	 * SessionHelper.setReregister(request);
	 * EasyMock.expect(whiteListUrl.getUrls()).andReturn(urlList);
	 * EasyMock.expect(registrationService.saveActivationCodeRegistration(
	 * activationCode, customer)).andReturn(acRegistration);
	 * 
	 * List<Registration<? extends ProductRegistrationDefinition>> registrations =
	 * new ArrayList<Registration<? extends ProductRegistrationDefinition>>();
	 * List<LicenceDto> licences = new ArrayList<LicenceDto>();
	 * CustomerRegistrationsDto custRegDto = new CustomerRegistrationsDto(customer,
	 * registrations, licences);
	 * expect(registrationService.getEntitlementsForCustomerRegistrations(customer,
	 * null,true)).andReturn(custRegDto);
	 * 
	 * ProductRegistrationDefinition regDef= new ProductRegistrationDefinition();
	 * regDef.setProduct(regProd);
	 * expect(registrationService.getCompletedRegistrationInformation(regProd,
	 * customer)).andReturn(true); EasyMock.replay(getMocks()); ModelAndView result
	 * = controller.handleRequest(request, response);
	 * 
	 * 
	 * 
	 * String view = getView(result);
	 * 
	 * Assert.assertEquals(SUCCESS_VIEW, view); }
	 * 
	 * @Ignore
	 * 
	 * @Test public void testBarrierProductRegistration() throws Exception {
	 * ProductRegistration registration = new ProductRegistration();
	 * checkCustomerIsRegisteredAndProcessRegistration(registration);
	 * EasyMock.expect(whiteListUrl.getUrls()).andReturn(urlList);
	 * List<Registration<? extends ProductRegistrationDefinition>> registrations =
	 * new ArrayList<Registration<? extends ProductRegistrationDefinition>>();
	 * registration.setRegistrationDefinition(prd); registrations.add(registration);
	 * 
	 * List<LicenceDto> licences = new ArrayList<LicenceDto>();
	 * CustomerRegistrationsDto custRegDto = new CustomerRegistrationsDto(customer,
	 * registrations, licences);
	 * expect(registrationService.getEntitlementsForCustomerRegistrations(customer,
	 * null,true)).andReturn(custRegDto);
	 * 
	 * ProductRegistrationDefinition regDef= new ProductRegistrationDefinition();
	 * regDef.setProduct(regProd);
	 * expect(registrationService.getCompletedRegistrationInformation(regProd,
	 * customer)).andReturn(true);
	 * expect(productService.getEnforceableProductByErightsId(regProd.getId())).
	 * andReturn(enforceableProduct);
	 * 
	 * EasyMock.expect(licenceService.getLicensesForUserProduct(customer,
	 * regProd)).andStubReturn(licences); EasyMock.replay(getMocks()); ModelAndView
	 * result = controller.handleRequest(request, response);
	 * 
	 * 
	 * 
	 * String view = getView(result);
	 * Assert.assertEquals(EACViews.PRODUCT_REGISTRATION_VIEW, view); }
	 * 
	 * @Test public void testSuccessRegistrationIsCompleteAndNotAwaitingValidation()
	 * throws Exception { registration.setCompleted(true);
	 * 
	 * checkCustomerIsRegisteredAndProcessRegistration(registration);
	 * 
	 * registrationService.saveRegistrationActivation(anyObject(
	 * RegistrationActivationDto.class)); EasyMock.expectLastCall();
	 * 
	 * EasyMock.replay(getMocks()); ModelAndView result =
	 * controller.handleRequest(request, response); EasyMock.verify(getMocks());
	 * String view = getView(result); Assert.assertEquals(SUCCESS_VIEW, view); }
	 * 
	 * @Test public void testBarrierLicenceDenied() throws Exception {
	 * registration.setCompleted(true); registration.setAwaitingValidation(true);
	 * registration.setDenied(true);
	 * 
	 * checkCustomerIsRegisteredAndProcessRegistration(registration);
	 * EasyMock.expect(whiteListUrl.getUrls()).andReturn(urlList);
	 * List<Registration<? extends ProductRegistrationDefinition>> registrations =
	 * new ArrayList<Registration<? extends ProductRegistrationDefinition>>();
	 * registrations.add(registration); List<LicenceDto> licences = new
	 * ArrayList<LicenceDto>(); CustomerRegistrationsDto custRegDto = new
	 * CustomerRegistrationsDto(customer, registrations, licences);
	 * expect(registrationService.getEntitlementsForCustomerRegistrations(customer,
	 * null,true)).andReturn(custRegDto);
	 * 
	 * ProductRegistrationDefinition regDef= new ProductRegistrationDefinition();
	 * regDef.setProduct(regProd);
	 * expect(registrationService.getCompletedRegistrationInformation(regProd,
	 * customer)).andReturn(true);
	 * expect(productService.getEnforceableProductByErightsId(regProd.getId())).
	 * andReturn(enforceableProduct);
	 * 
	 * EasyMock.replay(getMocks()); ModelAndView result =
	 * controller.handleRequest(request, response);
	 * 
	 * 
	 * 
	 * 
	 * String view = getView(result);
	 * Assert.assertEquals(EACViews.LICENCE_DENIED_PAGE, view);
	 * Assert.assertEquals(VALIDATOR_EMAIL, result.getModelMap().get(KEY_EMAIL));
	 * Assert.assertEquals(PRODUCT_NAME,
	 * result.getModelMap().get(KEY_PRODUCT_NAME)); }
	 * 
	 * @Test public void testBarrierActivateLicenceForSelfRegistration() throws
	 * Exception { registration.setCompleted(true);
	 * registration.setAwaitingValidation(true); registration.setDenied(false);
	 * this.prd.setRegistrationActivation(new SelfRegistrationActivation());
	 * checkCustomerIsRegisteredAndProcessRegistration(registration);
	 * EasyMock.expect(whiteListUrl.getUrls()).andReturn(urlList);
	 * 
	 * List<Registration<? extends ProductRegistrationDefinition>> registrations =
	 * new ArrayList<Registration<? extends ProductRegistrationDefinition>>();
	 * registrations.add(registration); List<LicenceDto> licences = new
	 * ArrayList<LicenceDto>(); CustomerRegistrationsDto custRegDto = new
	 * CustomerRegistrationsDto(customer, registrations, licences);
	 * expect(registrationService.getEntitlementsForCustomerRegistrations(customer,
	 * null,true)).andReturn(custRegDto);
	 * 
	 * ProductRegistrationDefinition regDef= new ProductRegistrationDefinition();
	 * regDef.setProduct(regProd);
	 * expect(registrationService.getCompletedRegistrationInformation(regProd,
	 * customer)).andReturn(true);
	 * expect(productService.getEnforceableProductByErightsId(regProd.getId())).
	 * andReturn(enforceableProduct); EasyMock.replay(getMocks()); ModelAndView
	 * result = controller.handleRequest(request, response);
	 * 
	 * String view = getView(result);
	 * Assert.assertEquals(EACViews.ACTIVATE_LICENCE_PAGE, view);
	 * Assert.assertEquals(PRODUCT_EMAIL, result.getModelMap().get(KEY_EMAIL));
	 * Assert.assertEquals(PRODUCT_NAME,
	 * result.getModelMap().get(KEY_PRODUCT_NAME)); }
	 * 
	 * @Test public void
	 * testBarrierAwaitingLicenceActivationForNonSelfRegistration() throws Exception
	 * { registration.setCompleted(true); registration.setAwaitingValidation(true);
	 * registration.setDenied(false); ValidatedRegistrationActivation activation =
	 * new ValidatedRegistrationActivation();
	 * activation.setValidatorEmail(VALIDATOR_EMAIL);
	 * this.prd.setRegistrationActivation(activation);
	 * checkCustomerIsRegisteredAndProcessRegistration(registration);
	 * EasyMock.expect(whiteListUrl.getUrls()).andReturn(urlList);
	 * List<Registration<? extends ProductRegistrationDefinition>> registrations =
	 * new ArrayList<Registration<? extends ProductRegistrationDefinition>>();
	 * registrations.add(registration); List<LicenceDto> licences = new
	 * ArrayList<LicenceDto>(); CustomerRegistrationsDto custRegDto = new
	 * CustomerRegistrationsDto(customer, registrations, licences);
	 * expect(registrationService.getEntitlementsForCustomerRegistrations(customer,
	 * null,true)).andReturn(custRegDto);
	 * 
	 * ProductRegistrationDefinition regDef= new ProductRegistrationDefinition();
	 * regDef.setProduct(regProd);
	 * expect(registrationService.getCompletedRegistrationInformation(regProd,
	 * customer)).andReturn(true);
	 * 
	 * expect(productService.getEnforceableProductByErightsId(regProd.getId())).
	 * andReturn(enforceableProduct);
	 * registrationService.saveRegistrationActivation(EasyMock.anyObject(
	 * RegistrationActivationDto.class)
	 * ,EasyMock.anyObject(EnforceableProductDto.class));
	 * 
	 * EasyMock.expectLastCall();
	 * 
	 * EasyMock.replay(getMocks()); ModelAndView result =
	 * controller.handleRequest(request, response);
	 * 
	 * String view = getView(result);
	 * Assert.assertEquals(EACViews.AWAITING_LICENCE_ACTIVATION_PAGE, view);
	 * ModelMap map = result.getModelMap(); Assert.assertEquals(VALIDATOR_EMAIL,
	 * map.get(KEY_EMAIL)); Assert.assertEquals(PRODUCT_NAME,
	 * map.get(KEY_PRODUCT_NAME)); Assert.assertEquals(SLA,
	 * result.getModelMap().get(KEY_SLA)); }
	 * 
	 * @Test public void testBarrierNoActiveLicenceAvailableWhenException() throws
	 * Exception { registration = new ProductRegistration();
	 * registration.setCompleted(true); registration.setAwaitingValidation(true);
	 * registration.setDenied(false); registration.setActivated(true);
	 * 
	 * checkCustomerIsRegisteredAndProcessRegistration(registration);
	 * 
	 * EasyMock.expect(licenceService.getLicensesForUserProduct(customer,
	 * regProd)).andThrow(new ServiceLayerException()); LicenceDto licenceDto = new
	 * LicenceDto("111", new DateTime(), true, false, true, false, false);
	 * 
	 * List<LicenceDto> licences = Arrays.asList(licenceDto);
	 * EasyMock.expect(licenceService.getLicensesForUserProduct(customer,
	 * regProd)).andReturn(licences);
	 * EasyMock.expect(whiteListUrl.getUrls()).andReturn(urlList);
	 * List<Registration<? extends ProductRegistrationDefinition>> registrations =
	 * new ArrayList<Registration<? extends ProductRegistrationDefinition>>();
	 * registration.setRegistrationDefinition(prd); registrations.add(registration);
	 * 
	 * List<LicenceDto> licences1 = new ArrayList<LicenceDto>();
	 * CustomerRegistrationsDto custRegDto = new CustomerRegistrationsDto(customer,
	 * registrations, licences1);
	 * expect(registrationService.getEntitlementsForCustomerRegistrations(customer,
	 * null,true)).andReturn(custRegDto);
	 * 
	 * ProductRegistrationDefinition regDef= new ProductRegistrationDefinition();
	 * regDef.setProduct(regProd);
	 * expect(registrationService.getCompletedRegistrationInformation(regProd,
	 * customer)).andReturn(true);
	 * expect(productService.getEnforceableProductByErightsId(regProd.getId())).
	 * andReturn(enforceableProduct);
	 * 
	 * EasyMock.replay(getMocks()); ModelAndView result =
	 * controller.handleRequest(request, response);
	 * 
	 * 
	 * 
	 * 
	 * String view = getView(result);
	 * Assert.assertEquals(EACViews.NO_ACTIVE_LICENCE, view);
	 * 
	 * }
	 * 
	 * @Test public void testBarrierWhenAllLicencesExpired() throws Exception {
	 * ProductRegistration registration = new ProductRegistration();
	 * 
	 * checkCustomerIsRegisteredAndProcessRegistration(registration);
	 * 
	 * LicenceDto licenceDto = new LicenceDto("111", new DateTime(), true, false,
	 * true, false, false); List<LicenceDto> licences = Arrays.asList(licenceDto);
	 * EasyMock.expect(licenceService.getLicensesForUserProduct(customer,
	 * regProd)).andReturn(licences).times(2); prd.setProduct(regProd);
	 * registration.setRegistrationDefinition(prd);
	 * EasyMock.expect(whiteListUrl.getUrls()).andReturn(urlList);
	 * List<Registration<? extends ProductRegistrationDefinition>> registrations =
	 * new ArrayList<Registration<? extends ProductRegistrationDefinition>>();
	 * registration.setRegistrationDefinition(prd); registrations.add(registration);
	 * List<LicenceDto> licences1 = new ArrayList<LicenceDto>();
	 * CustomerRegistrationsDto custRegDto = new CustomerRegistrationsDto(customer,
	 * registrations, licences1);
	 * expect(registrationService.getEntitlementsForCustomerRegistrations(customer,
	 * null,true)).andReturn(custRegDto);
	 * 
	 * ProductRegistrationDefinition regDef= new ProductRegistrationDefinition();
	 * regDef.setProduct(regProd);
	 * expect(registrationService.getCompletedRegistrationInformation(regProd,
	 * customer)).andReturn(true);
	 * expect(productService.getEnforceableProductByErightsId(regProd.getId())).
	 * andReturn(enforceableProduct);
	 * 
	 * EasyMock.replay(getMocks()); ModelAndView result =
	 * controller.handleRequest(request, response);
	 * 
	 * String view = getView(result);
	 * //Assert.assertTrue(result.getModelMap().containsAttribute("allExpired"));
	 * Assert.assertEquals(EACViews.NO_ACTIVE_LICENCE, view); }
	 * 
	 * @Test public void testBarrierWhenOneLicenceInFutureRestExpired() throws
	 * Exception {
	 * 
	 * checkCustomerIsRegisteredAndProcessRegistration(registration);
	 * 
	 * LicenceDto expiredLicence = new LicenceDto("111", new DateTime(), true,
	 * false, true, false, false); LicenceDto futureLicence = new LicenceDto("111",
	 * new DateTime(System.currentTimeMillis() + 3600000), false, false, true, true,
	 * false); List<LicenceDto> licences = Arrays.asList(expiredLicence,
	 * futureLicence); prd.setProduct(regProd);
	 * registration.setRegistrationDefinition(prd);
	 * EasyMock.expect(licenceService.getLicensesForUserProduct(customer,
	 * regProd)).andReturn(licences).times(2);
	 * EasyMock.expect(whiteListUrl.getUrls()).andReturn(urlList);
	 * List<Registration<? extends ProductRegistrationDefinition>> registrations =
	 * new ArrayList<Registration<? extends ProductRegistrationDefinition>>();
	 * registrations.add(registration); List<LicenceDto> licences1 = new
	 * ArrayList<LicenceDto>(); CustomerRegistrationsDto custRegDto = new
	 * CustomerRegistrationsDto(customer, registrations, licences1);
	 * expect(registrationService.getEntitlementsForCustomerRegistrations(customer,
	 * null,true)).andReturn(custRegDto);
	 * expect(productService.getEnforceableProductByErightsId(regProd.getId())).
	 * andReturn(enforceableProduct); ProductRegistrationDefinition regDef= new
	 * ProductRegistrationDefinition(); regDef.setProduct(regProd);
	 * expect(registrationService.getCompletedRegistrationInformation(regProd,
	 * customer)).andReturn(true);
	 * 
	 * EasyMock.replay(getMocks()); ModelAndView result =
	 * controller.handleRequest(request, response);
	 * 
	 * String view = getView(result);
	 * Assert.assertFalse(result.getModelMap().containsAttribute("allExpired"));
	 * Assert.assertEquals(EACViews.NO_ACTIVE_LICENCE, view); }
	 * 
	 * 
	 * @Ignore
	 * 
	 * @Test public void testBarrierConcurrencyExceeeded() throws Exception {
	 * //registration = new ProductRegistration(); // ProductRegistration
	 * registration = new ProductRegistration(); registration.setCompleted(true);
	 * registration.setAwaitingValidation(true); registration.setDenied(false);
	 * registration.setActivated(true);
	 * 
	 * checkCustomerIsRegisteredAndProcessRegistration(registration);
	 * 
	 * LicenceDto licenceDto = new LicenceDto("111", new DateTime(), false, true,
	 * true, false, false); List<LicenceDto> licences = Arrays.asList(licenceDto);
	 * request.setParameter("licenseDenyTypes",
	 * String.valueOf(ErightsLicenceDecision.DENY_CONCURRENCY.getDecisionCode()));
	 * 
	 * EasyMock.expect(licenceService.getLicensesForUserProduct(customer,
	 * regProd)).andReturn(licences);
	 * EasyMock.expect(whiteListUrl.getUrls()).andReturn(urlList);
	 * List<Registration<? extends ProductRegistrationDefinition>> registrations =
	 * new ArrayList<Registration<? extends ProductRegistrationDefinition>>();
	 * registration.setRegistrationDefinition(prd); registrations.add(registration);
	 * List<LicenceDto> licences1 = new ArrayList<LicenceDto>();
	 * CustomerRegistrationsDto custRegDto = new CustomerRegistrationsDto(customer,
	 * registrations, licences1);
	 * expect(registrationService.getEntitlementsForCustomerRegistrations(customer,
	 * null,true)).andReturn(custRegDto);
	 * expect(productService.getEnforceableProductByErightsId(regProd.getId())).
	 * andReturn(enforceableProduct); ProductRegistrationDefinition regDef= new
	 * ProductRegistrationDefinition(); regDef.setProduct(regProd);
	 * expect(registrationService.getCompletedRegistrationInformation(regProd,
	 * customer)).andReturn(true);
	 * 
	 * EasyMock.replay(getMocks()); ModelAndView result =
	 * controller.handleRequest(request, response);
	 * 
	 * String view = getView(result);
	 * Assert.assertEquals(EACViews.CONCURRENCY_EXCEEDED, view);
	 * 
	 * }
	 * 
	 * @Test public void testErrorCatchAll1() throws Exception {
	 * 
	 * checkCustomerIsRegisteredAndProcessRegistration(registration);
	 * 
	 * LicenceDto licenceDto = new LicenceDto("111", new DateTime(), false, true,
	 * registration.isCompleted(), registration.isAwaitingValidation(),
	 * registration.isDenied()); List<LicenceDto> licences =
	 * Arrays.asList(licenceDto); request.setParameter("licenseDenyTypes",
	 * String.valueOf(ErightsLicenceDecision.DENY_PAUSED.getDecisionCode()));
	 * 
	 * EasyMock.expect(licenceService.getLicensesForUserProduct(customer,
	 * regProd)).andReturn(licences);
	 * EasyMock.expect(whiteListUrl.getUrls()).andReturn(urlList);
	 * List<Registration<? extends ProductRegistrationDefinition>> registrations =
	 * new ArrayList<Registration<? extends ProductRegistrationDefinition>>();
	 * registrations.add(registration); List<LicenceDto> licences1 = new
	 * ArrayList<LicenceDto>(); CustomerRegistrationsDto custRegDto = new
	 * CustomerRegistrationsDto(customer, registrations, licences1);
	 * expect(registrationService.getEntitlementsForCustomerRegistrations(customer,
	 * null,true)).andReturn(custRegDto);
	 * 
	 * ProductRegistrationDefinition regDef= new ProductRegistrationDefinition();
	 * regDef.setProduct(regProd);
	 * expect(registrationService.getCompletedRegistrationInformation(regProd,
	 * customer)).andReturn(true);
	 * expect(productService.getEnforceableProductByErightsId(regProd.getId())).
	 * andReturn(enforceableProduct); EasyMock.replay(getMocks()); ModelAndView
	 * result = controller.handleRequest(request, response);
	 * 
	 * String view = getView(result); Assert.assertEquals(EACViews.ERROR_PAGE,
	 * view);
	 * 
	 * }
	 * 
	 * @Test public void testErrorCatchAll2() throws Exception {
	 * 
	 * checkCustomerIsRegisteredAndProcessRegistration(registration);
	 * 
	 * LicenceDto licenceDto = new LicenceDto("111", new DateTime(), false, true,
	 * registration.isCompleted(), registration.isAwaitingValidation(),
	 * registration.isDenied()); List<LicenceDto> licences =
	 * Arrays.asList(licenceDto); request.setParameter("licenseDenyTypes", " ");
	 * 
	 * EasyMock.expect(licenceService.getLicensesForUserProduct(customer,
	 * regProd)).andReturn(licences);
	 * EasyMock.expect(whiteListUrl.getUrls()).andReturn(urlList);
	 * List<Registration<? extends ProductRegistrationDefinition>> registrations =
	 * new ArrayList<Registration<? extends ProductRegistrationDefinition>>();
	 * registrations.add(registration); List<LicenceDto> licences1 = new
	 * ArrayList<LicenceDto>(); CustomerRegistrationsDto custRegDto = new
	 * CustomerRegistrationsDto(customer, registrations, licences1);
	 * expect(registrationService.getEntitlementsForCustomerRegistrations(customer,
	 * null,true)).andReturn(custRegDto);
	 * 
	 * ProductRegistrationDefinition regDef= new ProductRegistrationDefinition();
	 * regDef.setProduct(regProd);
	 * expect(registrationService.getCompletedRegistrationInformation(regProd,
	 * customer)).andReturn(true);
	 * expect(productService.getEnforceableProductByErightsId(regProd.getId())).
	 * andReturn(enforceableProduct); EasyMock.replay(getMocks()); ModelAndView
	 * result = controller.handleRequest(request, response);
	 * 
	 * String view = getView(result); Assert.assertEquals(EACViews.ERROR_PAGE,
	 * view); }
	 * 
	 * private void checkCustomerIsRegisteredAndProcessRegistration(Registration<?
	 * extends ProductRegistrationDefinition> registration) throws Exception {
	 * checkCustomerIsRegisteredAndProcessRegistration(registration, prd); }
	 * 
	 * private void checkCustomerIsRegisteredAndProcessRegistration(Registration<?
	 * extends ProductRegistrationDefinition> registration,
	 * ProductRegistrationDefinition prd) throws Exception {
	 * request.setParameter(URL, PROTECTED_RESOURCE);
	 * request.setParameter(DENY_REASON,
	 * String.valueOf(ErightsDenyReason.DENY_ALL_LICENSE_DENIED.getReasonCode()));
	 * request.setCookies(eacCookie);
	 * EasyMock.expect(whiteListUrl.getUrls()).andReturn(urlList);
	 * EasyMock.expect(domainSkinResolverService.getSkinFromDomain(
	 * PROTECTED_RESOURCE)).andReturn(null);
	 * EasyMock.expect(registrationDefinitionService.
	 * getProductRegistrationDefinitionByProduct(regProd)).andReturn(prd);
	 * EasyMock.expect(productService.getRegisterableProductByUrl(PROTECTED_RESOURCE
	 * )).andReturn(regProd);
	 * EasyMock.expect(customerService.getCustomerFromSession(COOKIE_VALUE)).
	 * andReturn(customer);
	 * 
	 * EasyMock.expect(customerService.
	 * getRegistrationByRegistrationDefinitionAndCustomer(prd,
	 * customer)).andReturn((Registration)registration);
	 * 
	 * }
	 * 
	 * private String getView(ModelAndView modelAndView) { if
	 * (modelAndView.getView() instanceof RedirectView) { return ((RedirectView)
	 * modelAndView.getView()).getUrl(); } return modelAndView.getViewName(); }
	 */}
