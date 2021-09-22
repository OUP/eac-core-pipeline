package com.oup.eac.web.controllers.authentication;

import java.util.ArrayList;
import java.util.List;

import javax.naming.NamingException;
import javax.servlet.http.Cookie;

import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.view.RedirectView;

import com.oup.eac.domain.Customer;
import com.oup.eac.domain.LinkedProduct;
import com.oup.eac.domain.Product;
import com.oup.eac.domain.ProductRegistrationDefinition;
import com.oup.eac.domain.RegisterableProduct;
import com.oup.eac.domain.RegisterableProduct.RegisterableType;
import com.oup.eac.dto.EnforceableProductDto;
import com.oup.eac.dto.EnforceableProductUrlDto;
import com.oup.eac.integration.facade.ErightsFacade;
import com.oup.eac.integration.facade.exceptions.ErightsException;
import com.oup.eac.integration.facade.exceptions.ProductNotFoundException;
import com.oup.eac.service.CustomerService;
import com.oup.eac.service.DomainSkinResolverService;
import com.oup.eac.service.ProductService;
import com.oup.eac.service.RegistrationDefinitionService;
import com.oup.eac.service.ServiceLayerException;
import com.oup.eac.service.WhiteListUrlService;
import com.oup.eac.service.exceptions.NoRegisterableProductFoundException;
import com.oup.eac.web.controllers.context.DirectRequestContextFactory;
import com.oup.eac.web.controllers.helpers.SessionHelper;

public class DirectRegistrationControllerMockTest /* extends AbstractMockTest */ {
	/*
	 * 
	 * public static final String PROTECTED_URL =
	 * "http://test.oup.com/protected/resource.htm"; public static final String
	 * LANDING_PAGE_URL = "http://localhost:8080/protected/landingPage"; public
	 * static final String UNKNOWN_URL = "http://localhost:8080/unknown"; public
	 * static final String ACCOUNT_REGISTRATION_VIEW = "accountRegistration.htm";
	 * public static final String INT_PRODUCT_ID = "abc-def"; public static final
	 * String INT_PRODUCT2_ID = "abc-def2"; public static final String
	 * EXT_PRODUCT_ID = "0321356683"; public static final String TYPE_ID =
	 * "ISBN-10"; public static final String SYSTEM_ID = "AMZ";
	 * 
	 * private DirectRegistrationController sut; private
	 * RegistrationDefinitionService registrationDefinitionService; private
	 * DomainSkinResolverService domainSkinResolverService; private CustomerService
	 * customerService; private ErightsFacade fakeErightsFacade; private
	 * WhiteListUrlService whiteListUrl; private ProductService productService;
	 * 
	 * private RegisterableProduct regProd1, regProd2, regProd3; private
	 * EnforceableProductDto enfoProd, enfoProd2; private LinkedProduct nonRegProd2;
	 * 
	 * private ProductRegistrationDefinition prodRegDef1, prodRegDef2; private
	 * Customer customer1; private List<String> urlList = new ArrayList<String>();
	 * private String WhiteListURL = "http://test.oup.com/protected/resource.htm";
	 * private Product product = new RegisterableProduct(); public
	 * DirectRegistrationControllerMockTest() throws NamingException { super(); }
	 * 
	 * @Before public void setup() { this.registrationDefinitionService =
	 * EasyMock.createMock(RegistrationDefinitionService.class);
	 * this.domainSkinResolverService =
	 * EasyMock.createMock(DomainSkinResolverService.class); this.customerService =
	 * EasyMock.createMock(CustomerService.class); this.fakeErightsFacade =
	 * EasyMock.createMock(ErightsFacade.class); this.whiteListUrl =
	 * EasyMock.createMock(WhiteListUrlService.class); this.productService =
	 * EasyMock.createMock(ProductService.class); product =
	 * EasyMock.createMock(Product.class); whiteListUrl =
	 * EasyMock.createMock(WhiteListUrlService.class); urlList.add(WhiteListURL);
	 * urlList.add(LANDING_PAGE_URL); List<EnforceableProductUrlDto> productUrls =
	 * new ArrayList<EnforceableProductUrlDto>(); EnforceableProductUrlDto urlDto=
	 * new EnforceableProductUrlDto("http", "www.google.com", "test", null, null,
	 * null); productUrls.add(urlDto); product.setProductUrls(productUrls );
	 * DirectRequestContextFactory directRequestContextFactory = new
	 * DirectRequestContextFactory(productService, domainSkinResolverService,
	 * registrationDefinitionService, fakeErightsFacade, whiteListUrl); //
	 * this.licenceService = EasyMock.createMock(LicenceService.class); this.sut =
	 * new DirectRegistrationController(customerService,
	 * directRequestContextFactory, whiteListUrl);
	 * 
	 * setMocks(this.customerService, this.domainSkinResolverService,
	 * this.productService, this.registrationDefinitionService,this.whiteListUrl,
	 * this.fakeErightsFacade);
	 * 
	 * this.regProd1 = new RegisterableProduct(); this.enfoProd = new
	 * EnforceableProductDto();
	 * this.regProd1.setRegisterableType(RegisterableType.SELF_REGISTERABLE);
	 * 
	 * this.regProd1.setLandingPage(LANDING_PAGE_URL); this.prodRegDef1 = new
	 * ProductRegistrationDefinition();
	 * this.enfoProd.setRegisterableType("SELF_REGISTERABLE");
	 * this.enfoProd.setLandingPage(LANDING_PAGE_URL); this.prodRegDef2 = new
	 * ProductRegistrationDefinition(); this.customer1 = new Customer();
	 * this.nonRegProd2 = new LinkedProduct();
	 * 
	 * this.regProd2 = new RegisterableProduct(); this.enfoProd2 = new
	 * EnforceableProductDto();
	 * this.enfoProd2.setRegisterableType("ADMIN_REGISTERABLE");
	 * this.regProd2.setRegisterableType(RegisterableType.SELF_REGISTERABLE);
	 * this.enfoProd2.setLandingPage(LANDING_PAGE_URL);
	 * this.enfoProd2.setProductId("id-of-product-with-Admin-registrable");;
	 * this.enfoProd2.setName("Reg. Prod. No Landing Page");
	 * this.regProd2.setLandingPage("");
	 * this.regProd2.setId("id-of-product-with-no-landing-page");
	 * this.regProd2.setProductName("Reg. Prod. No Landing Page");
	 * 
	 * this.regProd3 = new RegisterableProduct();
	 * this.regProd3.setRegisterableType(RegisterableType.ADMIN_REGISTERABLE);
	 * this.regProd3.setLandingPage("");
	 * this.regProd3.setId("id-of-product-with-no-landing-page");
	 * this.regProd3.setProductName("Reg. Prod. No Landing Page");
	 * 
	 * }
	 * 
	 * @Test public void testHappyPathUrlToProductRegistrationPage() throws
	 * ServiceLayerException, NoRegisterableProductFoundException {
	 * MockHttpServletRequest request = new MockHttpServletRequest();
	 * MockHttpSession session = new MockHttpSession(); request.setSession(session);
	 * MockHttpServletResponse response = new MockHttpServletResponse();
	 * request.setCookies(new Cookie("EAC", "SESSION_ID_123"));
	 * 
	 * request.setParameter("url", PROTECTED_URL);
	 * 
	 * // there's no skin but that's not important
	 * EasyMock.expect(this.domainSkinResolverService.getSkinFromDomain(
	 * PROTECTED_URL)).andReturn(null);
	 * 
	 * // the URL is for a registerable product try {
	 * EasyMock.expect(this.productService.getRegisterableProductByUrl(PROTECTED_URL
	 * )).andReturn(regProd1); } catch (ErightsException e) { // TODO Auto-generated
	 * catch block e.printStackTrace(); }
	 * 
	 * // there is a registration definition
	 * EasyMock.expect(this.registrationDefinitionService.
	 * getProductRegistrationDefinitionByProduct(regProd1)).andReturn(prodRegDef1);
	 * 
	 * // the cookie is valid and this is the customer
	 * EasyMock.expect(this.customerService.getCustomerFromSession("SESSION_ID_123")
	 * ).andReturn(customer1);
	 * EasyMock.expect(whiteListUrl.getUrls()).andReturn(urlList); replayMocks();
	 * ModelAndView result = sut.process(request, response); // verifyMocks();
	 * 
	 * View view = result.getView();
	 * 
	 * RedirectView rv = (RedirectView) view; Assert.assertTrue(view instanceof
	 * RedirectView); int index = rv.getUrl().indexOf(PROTECTED_URL);
	 * Assert.assertEquals(0, index);
	 * 
	 * // we need to check that the Customer and RegisterableProduct are in the //
	 * session for when we come back into Assert.assertEquals(session,
	 * request.getSession());
	 * Assert.assertNotNull(SessionHelper.getCustomer(request));
	 * Assert.assertEquals(regProd1, SessionHelper.getRegisterableProduct(request));
	 * }
	 * 
	 * @Test public void testHappyPathUrlToAccountRegistrationNoEacCookie() throws
	 * ServiceLayerException, NoRegisterableProductFoundException {
	 * MockHttpServletRequest request = new MockHttpServletRequest();
	 * MockHttpServletResponse response = new MockHttpServletResponse();
	 * 
	 * request.setParameter("url", PROTECTED_URL);
	 * 
	 * // there's no skin but that's not important
	 * EasyMock.expect(this.domainSkinResolverService.getSkinFromDomain(
	 * PROTECTED_URL)).andReturn(null);
	 * 
	 * // the URL is for a registerable product try {
	 * EasyMock.expect(this.productService.getRegisterableProductByUrl(PROTECTED_URL
	 * )).andReturn(regProd1); } catch (ErightsException e) { // TODO Auto-generated
	 * catch block e.printStackTrace(); }
	 * 
	 * // there is a registration definition
	 * EasyMock.expect(this.registrationDefinitionService.
	 * getProductRegistrationDefinitionByProduct(regProd1)).andReturn(prodRegDef1);
	 * EasyMock.expect(whiteListUrl.getUrls()).andReturn(urlList); replayMocks();
	 * ModelAndView result = sut.process(request, response); //verifyMocks();
	 * 
	 * View view = result.getView(); Assert.assertTrue(view instanceof
	 * RedirectView); RedirectView rv = (RedirectView) view;
	 * Assert.assertEquals(ACCOUNT_REGISTRATION_VIEW, rv.getUrl());
	 * 
	 * }
	 * 
	 * @Test public void testHappyPathUrlToAccountRegistrationInvalidEacCookie()
	 * throws ServiceLayerException, NoRegisterableProductFoundException {
	 * MockHttpServletRequest request = new MockHttpServletRequest();
	 * MockHttpServletResponse response = new MockHttpServletResponse();
	 * request.setCookies(new Cookie("EAC", "SESSION_ID_123"));
	 * 
	 * request.setParameter("url", PROTECTED_URL);
	 * 
	 * // there's no skin but that's not important
	 * EasyMock.expect(this.domainSkinResolverService.getSkinFromDomain(
	 * PROTECTED_URL)).andReturn(null);
	 * 
	 * // the URL is for a registerable product try {
	 * EasyMock.expect(this.productService.getRegisterableProductByUrl(PROTECTED_URL
	 * )).andReturn(regProd1); } catch (ErightsException e) { // TODO Auto-generated
	 * catch block e.printStackTrace(); }
	 * 
	 * // there is a registration definition
	 * EasyMock.expect(this.registrationDefinitionService.
	 * getProductRegistrationDefinitionByProduct(regProd1)).andReturn(prodRegDef1);
	 * 
	 * // the cookie is not valid!
	 * EasyMock.expect(this.customerService.getCustomerFromSession("SESSION_ID_123")
	 * ).andReturn(null);
	 * EasyMock.expect(whiteListUrl.getUrls()).andReturn(urlList); replayMocks();
	 * ModelAndView result = sut.process(request, response); // verifyMocks();
	 * 
	 * View view = result.getView(); Assert.assertTrue(view instanceof
	 * RedirectView); RedirectView rv = (RedirectView) view;
	 * Assert.assertEquals(ACCOUNT_REGISTRATION_VIEW, rv.getUrl()); }
	 * 
	 * @Test public void testBadRequest() { MockHttpServletRequest request = new
	 * MockHttpServletRequest(); MockHttpServletResponse response = new
	 * MockHttpServletResponse();
	 * EasyMock.expect(whiteListUrl.getUrls()).andReturn(urlList); replayMocks();
	 * ModelAndView result = sut.process(request, response); verifyMocks();
	 * 
	 * Assert.assertEquals("error", result.getViewName()); }
	 * 
	 * @Test public void testErrorPathUrlNotForRegisterableProduct() throws
	 * ServiceLayerException, NoRegisterableProductFoundException {
	 * MockHttpServletRequest request = new MockHttpServletRequest();
	 * MockHttpServletResponse response = new MockHttpServletResponse();
	 * request.setParameter("url", UNKNOWN_URL);
	 * 
	 * // there's no skin but that's not important
	 * EasyMock.expect(this.domainSkinResolverService.getSkinFromDomain(UNKNOWN_URL)
	 * ).andReturn(null);
	 * EasyMock.expect(whiteListUrl.getUrls()).andReturn(urlList); // the URL is NOT
	 * for a registerable product try {
	 * EasyMock.expect(this.productService.getRegisterableProductByUrl(UNKNOWN_URL))
	 * .andThrow(new ServiceLayerException()); } catch (ErightsException e) { //
	 * TODO Auto-generated catch block e.printStackTrace(); }
	 * 
	 * replayMocks(); ModelAndView result = sut.process(request, response);
	 * 
	 * 
	 * Assert.assertEquals("error", result.getViewName()); }
	 * 
	 * @Test public void testErrorUrlForRegProductWithNotRegistrationDefinition()
	 * throws ServiceLayerException, NoRegisterableProductFoundException {
	 * MockHttpServletRequest request = new MockHttpServletRequest();
	 * MockHttpServletResponse response = new MockHttpServletResponse();
	 * request.setParameter("url", PROTECTED_URL);
	 * 
	 * // there's no skin but that's not important
	 * EasyMock.expect(this.domainSkinResolverService.getSkinFromDomain(
	 * PROTECTED_URL)).andReturn(null);
	 * 
	 * // the URL is NOT for a registerable product try {
	 * EasyMock.expect(this.productService.getRegisterableProductByUrl(PROTECTED_URL
	 * )).andReturn(this.regProd1); } catch (ErightsException e) { // TODO
	 * Auto-generated catch block e.printStackTrace(); }
	 * 
	 * EasyMock.expect(this.registrationDefinitionService.
	 * getProductRegistrationDefinitionByProduct(this.regProd1)).andThrow(new
	 * ServiceLayerException());
	 * EasyMock.expect(whiteListUrl.getUrls()).andReturn(urlList); replayMocks();
	 * ModelAndView result = sut.process(request, response); // verifyMocks();
	 * 
	 * Assert.assertEquals("error", result.getViewName()); }
	 * 
	 * @Test public void testHappyPathInternalProductId() throws
	 * ServiceLayerException, ProductNotFoundException, ErightsException,
	 * NoRegisterableProductFoundException { MockHttpServletRequest request = new
	 * MockHttpServletRequest(); MockHttpSession session = new MockHttpSession();
	 * request.setSession(session); MockHttpServletResponse response = new
	 * MockHttpServletResponse(); request.setCookies(new Cookie("EAC",
	 * "SESSION_ID_123"));
	 * 
	 * request.setParameter("prodId", INT_PRODUCT_ID); request.setParameter("url",
	 * LANDING_PAGE_URL);
	 * //EasyMock.expect(this.productService.getProductById(INT_PRODUCT_ID)).
	 * andReturn();
	 * 
	 * // there's no skin but that's not important
	 * EasyMock.expect(this.domainSkinResolverService.getSkinFromDomain(
	 * LANDING_PAGE_URL)).andReturn(null);
	 * EasyMock.expect(this.productService.getRegisterableProductByUrl(
	 * LANDING_PAGE_URL)).andReturn(regProd1); // there is a registration definition
	 * EasyMock.expect(this.registrationDefinitionService.
	 * getProductRegistrationDefinitionByProduct(EasyMock.anyObject(
	 * RegisterableProduct.class))).andReturn(prodRegDef1);
	 * //EasyMock.expect(this.fakeErightsFacade.getProduct("abc-def")).andReturn(
	 * enfoProd); // the cookie is valid and this is the customer
	 * EasyMock.expect(this.customerService.getCustomerFromSession("SESSION_ID_123")
	 * ).andReturn(customer1);
	 * EasyMock.expect(whiteListUrl.getUrls()).andReturn(urlList); replayMocks();
	 * ModelAndView result = sut.process(request, response); verifyMocks();
	 * 
	 * View view = result.getView(); Assert.assertTrue(view instanceof
	 * RedirectView); RedirectView rv = (RedirectView) view; int index =
	 * rv.getUrl().indexOf(LANDING_PAGE_URL); Assert.assertEquals(0, index);
	 * 
	 * // we need to check that the Customer and RegisterableProduct are in the //
	 * session for when we come back into Assert.assertEquals(session,
	 * request.getSession());
	 * Assert.assertNotNull(SessionHelper.getCustomer(request));
	 * Assert.assertEquals(regProd1, SessionHelper.getRegisterableProduct(request));
	 * }
	 * 
	 * @Test public void testHappyPathExternalProductId() throws
	 * ServiceLayerException, NoRegisterableProductFoundException, ErightsException
	 * { MockHttpServletRequest request = new MockHttpServletRequest();
	 * MockHttpSession session = new MockHttpSession(); request.setSession(session);
	 * request.setParameter("url", PROTECTED_URL); MockHttpServletResponse response
	 * = new MockHttpServletResponse(); request.setCookies(new Cookie("EAC",
	 * "SESSION_ID_123"));
	 * 
	 * request.setParameter("prodId", EXT_PRODUCT_ID);
	 * request.setParameter("typeId", TYPE_ID); request.setParameter("systemId",
	 * SYSTEM_ID);
	 * 
	 * //EasyMock.expect(this.productService.getProductByExternalProductId(
	 * SYSTEM_ID, TYPE_ID, EXT_PRODUCT_ID)).andReturn(null);
	 * 
	 * // there's no skin but that's not important
	 * EasyMock.expect(this.domainSkinResolverService.getSkinFromDomain(
	 * PROTECTED_URL)).andReturn(null);
	 * 
	 * // there is a registration definition
	 * EasyMock.expect(this.registrationDefinitionService.
	 * getProductRegistrationDefinitionByProduct(regProd1)).andReturn(prodRegDef1);
	 * EasyMock.expect(this.productService.getRegisterableProductByUrl(PROTECTED_URL
	 * )).andReturn(regProd1); // the cookie is valid and this is the customer
	 * EasyMock.expect(this.customerService.getCustomerFromSession("SESSION_ID_123")
	 * ).andReturn(customer1);
	 * EasyMock.expect(whiteListUrl.getUrls()).andReturn(urlList); replayMocks();
	 * ModelAndView result = sut.process(request, response); verifyMocks();
	 * 
	 * View view = result.getView(); Assert.assertTrue(view instanceof
	 * RedirectView); RedirectView rv = (RedirectView) view; int index =
	 * rv.getUrl().indexOf(PROTECTED_URL); Assert.assertEquals(0, index);
	 * 
	 * // we need to check that the Customer and RegisterableProduct are in the //
	 * session for when we come back into Assert.assertEquals(session,
	 * request.getSession());
	 * Assert.assertNotNull(SessionHelper.getCustomer(request));
	 * Assert.assertEquals(regProd1, SessionHelper.getRegisterableProduct(request));
	 * }
	 * 
	 * 
	 * @Test public void testErrorInternalProductIdNotRegisterable() throws
	 * ServiceLayerException, ProductNotFoundException, ErightsException {
	 * MockHttpServletRequest request = new MockHttpServletRequest();
	 * MockHttpSession session = new MockHttpSession(); request.setSession(session);
	 * MockHttpServletResponse response = new MockHttpServletResponse();
	 * request.setCookies(new Cookie("EAC", "SESSION_ID_123"));
	 * 
	 * request.setParameter("prodId", INT_PRODUCT_ID);
	 * EasyMock.expect(this.domainSkinResolverService.getSkinFromDomain(
	 * LANDING_PAGE_URL)).andReturn(null);
	 * //EasyMock.expect(this.productService.getProductById(INT_PRODUCT_ID)).
	 * andReturn(nonRegProd2);
	 * EasyMock.expect(whiteListUrl.getUrls()).andReturn(urlList);
	 * EasyMock.expect(this.fakeErightsFacade.getProduct("abc-def")).andReturn(
	 * enfoProd); EasyMock.expect(this.registrationDefinitionService.
	 * getProductRegistrationDefinitionByProduct(EasyMock.anyObject(
	 * RegisterableProduct.class))).andReturn(null);
	 * //EasyMock.expect(this.customerService.getCustomerFromSession(
	 * "SESSION_ID_123")).andReturn(customer1); // there is NO registerable product
	 * from which to get a registration // definition replayMocks(); ModelAndView
	 * result = sut.process(request, response); verifyMocks();
	 * 
	 * Assert.assertEquals("error", result.getViewName()); }
	 * 
	 * @Test public void testErrorInternalProductIdNotFound1() throws
	 * ServiceLayerException, ProductNotFoundException, ErightsException {
	 * MockHttpServletRequest request = new MockHttpServletRequest();
	 * MockHttpSession session = new MockHttpSession(); request.setSession(session);
	 * 
	 * MockHttpServletResponse response = new MockHttpServletResponse();
	 * request.setCookies(new Cookie("EAC", "SESSION_ID_123"));
	 * 
	 * request.setParameter("prodId", INT_PRODUCT_ID);
	 * 
	 * //EasyMock.expect(this.productService.getProductById(INT_PRODUCT_ID)).
	 * andReturn(null);
	 * //EasyMock.expect(whiteListUrl.getUrls()).andReturn(urlList);
	 * //EasyMock.expect(this.domainSkinResolverService.getSkinFromDomain(
	 * LANDING_PAGE_URL)).andReturn(null);
	 * //EasyMock.expect(this.productService.getProductById(INT_PRODUCT_ID)).
	 * andReturn(nonRegProd2);
	 * EasyMock.expect(whiteListUrl.getUrls()).andReturn(urlList);
	 * EasyMock.expect(this.fakeErightsFacade.getProduct(INT_PRODUCT_ID)).andThrow(
	 * new ProductNotFoundException(INT_PRODUCT_ID));
	 * //EasyMock.expect(this.registrationDefinitionService.
	 * getProductRegistrationDefinitionByProduct(EasyMock.anyObject(
	 * RegisterableProduct.class))).andReturn(prodRegDef1);
	 * //EasyMock.expect(this.customerService.getCustomerFromSession(
	 * "SESSION_ID_123")).andReturn(customer1);
	 * 
	 * // there is NO registerable product from which to get a registration //
	 * definition replayMocks(); ModelAndView result = sut.process(request,
	 * response); verifyMocks();
	 * 
	 * Assert.assertEquals("error", result.getViewName()); }
	 * 
	 * @Test public void testErrorInternalProductIdNotFound2() throws
	 * ServiceLayerException, ProductNotFoundException, ErightsException {
	 * MockHttpServletRequest request = new MockHttpServletRequest();
	 * MockHttpSession session = new MockHttpSession(); request.setSession(session);
	 * MockHttpServletResponse response = new MockHttpServletResponse();
	 * request.setCookies(new Cookie("EAC", "SESSION_ID_123"));
	 * 
	 * request.setParameter("prodId", INT_PRODUCT_ID);
	 * 
	 * 
	 * //EasyMock.expect(this.domainSkinResolverService.getSkinFromDomain(
	 * LANDING_PAGE_URL)).andReturn(null);
	 * EasyMock.expect(whiteListUrl.getUrls()).andReturn(urlList);
	 * EasyMock.expect(this.fakeErightsFacade.getProduct(INT_PRODUCT_ID)).andThrow(
	 * new ProductNotFoundException(INT_PRODUCT_ID));
	 * //EasyMock.expect(this.registrationDefinitionService.
	 * getProductRegistrationDefinitionByProduct(EasyMock.anyObject(
	 * RegisterableProduct.class))).andReturn(prodRegDef1);
	 * //.expect(this.customerService.getCustomerFromSession("SESSION_ID_123")).
	 * andReturn(customer1); // there is NO registerable product from which to get a
	 * registration // definition replayMocks(); ModelAndView result =
	 * sut.process(request, response); verifyMocks();
	 * 
	 * Assert.assertEquals("error", result.getViewName()); }
	 * 
	 * @Test public void testErrorExternalProductIdNotRegisterable() throws
	 * ServiceLayerException, NoRegisterableProductFoundException, ErightsException
	 * { MockHttpServletRequest request = new MockHttpServletRequest();
	 * MockHttpSession session = new MockHttpSession(); request.setSession(session);
	 * MockHttpServletResponse response = new MockHttpServletResponse();
	 * request.setCookies(new Cookie("EAC", "SESSION_ID_123"));
	 * request.setParameter("url", PROTECTED_URL);
	 * 
	 * request.setParameter("prodId", EXT_PRODUCT_ID);
	 * request.setParameter("typeId", TYPE_ID); request.setParameter("systemId",
	 * SYSTEM_ID);
	 * 
	 * EasyMock.expect(this.registrationDefinitionService.
	 * getProductRegistrationDefinitionByProduct(regProd2)).andReturn(prodRegDef2);
	 * EasyMock.expect(this.domainSkinResolverService.getSkinFromDomain(
	 * PROTECTED_URL)).andReturn(null);
	 * //EasyMock.expect(this.productService.getProductByExternalProductId(
	 * SYSTEM_ID, TYPE_ID, EXT_PRODUCT_ID)).andReturn(enfoProd2);
	 * EasyMock.expect(this.productService.getRegisterableProductByUrl(PROTECTED_URL
	 * )).andStubReturn(regProd2);
	 * EasyMock.expect(this.customerService.getCustomerFromSession("SESSION_ID_123")
	 * ).andReturn(customer1);
	 * EasyMock.expect(whiteListUrl.getUrls()).andReturn(urlList); // there is NO
	 * registerable product from which to get a registration // definition
	 * replayMocks(); ModelAndView result = sut.process(request, response);
	 * verifyMocks();
	 * 
	 * Assert.assertEquals(null, result.getViewName()); }
	 * 
	 * @Test public void testErrorExternalProductIdNotFound1() throws
	 * ServiceLayerException, NoRegisterableProductFoundException, ErightsException
	 * { MockHttpServletRequest request = new MockHttpServletRequest();
	 * MockHttpSession session = new MockHttpSession(); request.setSession(session);
	 * request.setParameter("url", PROTECTED_URL); MockHttpServletResponse response
	 * = new MockHttpServletResponse(); request.setCookies(new Cookie("EAC",
	 * "SESSION_ID_123")); //request.setParameter("url", value);
	 * request.setParameter("prodId", EXT_PRODUCT_ID);
	 * request.setParameter("typeId", TYPE_ID); request.setParameter("systemId",
	 * SYSTEM_ID);
	 * 
	 * EasyMock.expect(this.domainSkinResolverService.getSkinFromDomain(
	 * PROTECTED_URL)).andReturn(null);
	 * EasyMock.expect(this.productService.getProductByExternalProductId(SYSTEM_ID,
	 * TYPE_ID, EXT_PRODUCT_ID)).andStubReturn(enfoProd);
	 * EasyMock.expect(whiteListUrl.getUrls()).andReturn(urlList);
	 * EasyMock.expect(this.productService.getRegisterableProductByUrl(PROTECTED_URL
	 * )).andStubReturn(regProd2);
	 * EasyMock.expect(this.registrationDefinitionService.
	 * getProductRegistrationDefinitionByProduct(regProd2)).andReturn(prodRegDef2);
	 * EasyMock.expect(this.customerService.getCustomerFromSession("SESSION_ID_123")
	 * ).andReturn(customer1); // there is NO registerable product from which to get
	 * a registration // definition replayMocks(); ModelAndView result =
	 * sut.process(request, response); verifyMocks();
	 * 
	 * Assert.assertEquals(null, result.getViewName()); }
	 * 
	 * @Test public void testErrorExternalProductIdNotFound2() throws
	 * ServiceLayerException { MockHttpServletRequest request = new
	 * MockHttpServletRequest(); MockHttpSession session = new MockHttpSession();
	 * request.setSession(session); MockHttpServletResponse response = new
	 * MockHttpServletResponse(); request.setCookies(new Cookie("EAC",
	 * "SESSION_ID_123"));
	 * 
	 * request.setParameter("prodId", INT_PRODUCT_ID);
	 * request.setParameter("systemId", SYSTEM_ID);
	 * EasyMock.expect(whiteListUrl.getUrls()).andReturn(urlList); replayMocks();
	 * ModelAndView result = sut.process(request, response); verifyMocks();
	 * 
	 * Assert.assertEquals("error", result.getViewName()); }
	 * 
	 * @Test public void testErrorExternalProductIdNotFound3() throws
	 * ServiceLayerException { MockHttpServletRequest request = new
	 * MockHttpServletRequest(); MockHttpSession session = new MockHttpSession();
	 * request.setSession(session); MockHttpServletResponse response = new
	 * MockHttpServletResponse(); request.setCookies(new Cookie("EAC",
	 * "SESSION_ID_123"));
	 * 
	 * request.setParameter("prodId", INT_PRODUCT_ID);
	 * request.setParameter("typeId", TYPE_ID);
	 * EasyMock.expect(whiteListUrl.getUrls()).andReturn(urlList); replayMocks();
	 * ModelAndView result = sut.process(request, response); verifyMocks();
	 * 
	 * Assert.assertEquals("error", result.getViewName()); }
	 * 
	 * @Test public void testErrorExternalProductIdNotFound4() throws
	 * ServiceLayerException { MockHttpServletRequest request = new
	 * MockHttpServletRequest(); MockHttpSession session = new MockHttpSession();
	 * request.setSession(session); MockHttpServletResponse response = new
	 * MockHttpServletResponse(); request.setCookies(new Cookie("EAC",
	 * "SESSION_ID_123"));
	 * 
	 * request.setParameter("prodId", INT_PRODUCT_ID);
	 * request.setParameter("systemId", "     "); request.setParameter("typeId",
	 * "     "); EasyMock.expect(whiteListUrl.getUrls()).andReturn(urlList);
	 * replayMocks(); ModelAndView result = sut.process(request, response);
	 * verifyMocks();
	 * 
	 * Assert.assertEquals("error", result.getViewName()); }
	 * 
	 * @Test public void testErrorUrlToProductRegistrationPageWithNoLandingPage()
	 * throws ServiceLayerException, NoRegisterableProductFoundException,
	 * ErightsException { MockHttpServletRequest request = new
	 * MockHttpServletRequest(); MockHttpSession session = new MockHttpSession();
	 * request.setSession(session); MockHttpServletResponse response = new
	 * MockHttpServletResponse(); request.setCookies(new Cookie("EAC",
	 * "SESSION_ID_123")); request.setParameter("url", PROTECTED_URL);
	 * request.setParameter("prodId", INT_PRODUCT_ID); // request.setParameter(name,
	 * value);
	 * //EasyMock.expect(this.productService.getProductById(INT_PRODUCT_ID)).
	 * andReturn(regProd2);
	 * 
	 * // there's no skin but that's not important
	 * //EasyMock.expect(this.domainSkinResolverService.getSkinFromDomain("")).
	 * andReturn(null);
	 * EasyMock.expect(this.productService.getRegisterableProductByUrl(PROTECTED_URL
	 * )).andReturn(regProd2); // there is a registration definition
	 * EasyMock.expect(this.registrationDefinitionService.
	 * getProductRegistrationDefinitionByProduct(regProd2)).andReturn(prodRegDef2);
	 * EasyMock.expect(this.domainSkinResolverService.getSkinFromDomain(
	 * PROTECTED_URL)).andReturn(null); // the cookie is valid and this is the
	 * customer
	 * EasyMock.expect(this.customerService.getCustomerFromSession("SESSION_ID_123")
	 * ).andReturn(customer1);
	 * EasyMock.expect(whiteListUrl.getUrls()).andReturn(urlList); replayMocks();
	 * ModelAndView result = sut.process(request, response); verifyMocks();
	 * 
	 * Assert.assertEquals(null, result.getViewName()); }
	 * 
	 * 
	 * @Test public void testErrorUrlToProductRegistrationPageAdminRegisterable()
	 * throws ServiceLayerException, NoRegisterableProductFoundException,
	 * ProductNotFoundException, ErightsException { MockHttpServletRequest request =
	 * new MockHttpServletRequest(); MockHttpSession session = new
	 * MockHttpSession(); request.setSession(session); MockHttpServletResponse
	 * response = new MockHttpServletResponse(); request.setCookies(new
	 * Cookie("EAC", "SESSION_ID_123"));
	 * 
	 * request.setParameter("prodId", INT_PRODUCT2_ID);
	 * 
	 * EasyMock.expect(this.fakeErightsFacade.getProduct("abc-def2")).andReturn(
	 * enfoProd2);
	 * //EasyMock.expect(this.productService.getProductById(INT_PRODUCT2_ID)).
	 * andReturn(regProd3);
	 * //EasyMock.expect(this.productService.getRegisterableProductByUrl(
	 * LANDING_PAGE_URL)).andReturn(regProd1);
	 * //EasyMock.expect(this.registrationDefinitionService.
	 * getProductRegistrationDefinitionByProduct(EasyMock.anyObject(
	 * RegisterableProduct.class))).andReturn(prodRegDef1); //
	 * EasyMock.expect(this.customerService.getCustomerFromSession("SESSION_ID_123")
	 * ).andReturn(customer1); // there's no skin but that's not important
	 * EasyMock.expect(this.domainSkinResolverService.getSkinFromDomain(
	 * PROTECTED_URL)).andReturn(null);
	 * EasyMock.expect(this.domainSkinResolverService.getSkinFromDomain(
	 * LANDING_PAGE_URL)).andReturn(null);
	 * EasyMock.expect(whiteListUrl.getUrls()).andReturn(urlList); replayMocks();
	 * ModelAndView result = sut.process(request, response); verifyMocks();
	 * 
	 * Assert.assertEquals("error", result.getViewName()); }
	 * 
	 * 
	 */}
