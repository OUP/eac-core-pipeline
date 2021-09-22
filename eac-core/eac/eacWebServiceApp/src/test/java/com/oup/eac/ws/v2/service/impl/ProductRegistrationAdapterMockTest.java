package com.oup.eac.ws.v2.service.impl;

import static org.easymock.EasyMock.anyObject;
import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.naming.NamingException;

import junit.framework.Assert;

import org.easymock.EasyMock;
import org.easymock.IArgumentMatcher;
import org.junit.Before;
import org.junit.Test;

import com.oup.eac.domain.Component;
import com.oup.eac.domain.Customer;
import com.oup.eac.domain.Element;
import com.oup.eac.domain.Field;
import com.oup.eac.domain.LinkedProduct;
import com.oup.eac.domain.PageComponent;
import com.oup.eac.domain.Product.ProductState;
import com.oup.eac.domain.ProductRegistration;
import com.oup.eac.domain.Question;
import com.oup.eac.domain.RegisterableProduct;
import com.oup.eac.domain.Registration;
import com.oup.eac.dto.EnforceableProductDto;
import com.oup.eac.service.ProductRegistrationService;
import com.oup.eac.service.ProductService;
import com.oup.eac.service.ServiceLayerException;
import com.oup.eac.service.exceptions.ServiceLayerValidationException;
import com.oup.eac.ws.v2.binding.access.ProductRegistrationRequest;
import com.oup.eac.ws.v2.binding.access.ProductRegistrationResponse;
import com.oup.eac.ws.v2.binding.common.Identifier;
import com.oup.eac.ws.v2.binding.common.RegistrationInformation;
import com.oup.eac.ws.v2.binding.common.WsUserId;
import com.oup.eac.ws.v2.binding.common.types.StatusCode;
import com.oup.eac.ws.v2.ex.WebServiceException;
import com.oup.eac.ws.v2.ex.WebServiceValidationException;
import com.oup.eac.ws.v2.service.WsCustomerLookup;
import com.oup.eac.ws.v2.service.WsProductLookup;
import com.oup.eac.ws.v2.service.utils.IdUtils;

public class ProductRegistrationAdapterMockTest /* extends AbstractMockTest implements WebServiceMessages */ {
	/*
	 * 
	 * private static final String PRODUCT_ID = "PRODUCT_ID"; private static final
	 * String CUSTOMER_ID = "CUSTOMER_ID"; private static final Map<String, String>
	 * EMPTY_REG_INFO = new HashMap<String, String>();
	 * 
	 * private WsCustomerLookup customerLookup; private WsProductLookup
	 * productLookup; private ProductRegistrationService productRegistrationService;
	 * private ProductService productService; private ProductRegistrationAdapterImpl
	 * sut; private Customer customer; private LinkedProduct linkedProduct; private
	 * RegisterableProduct regProduct; private EnforceableProductDto enforceProduct;
	 * private Map<String,String> registrationPageData = new HashMap<String,
	 * String>();
	 * 
	 * @SuppressWarnings("rawtypes") private Registration productRegistration;
	 * 
	 * public ProductRegistrationAdapterMockTest() throws NamingException { super();
	 * }
	 * 
	 * @Before public void setup() { customerLookup =
	 * createMock(WsCustomerLookup.class);
	 * 
	 * productLookup = createMock(WsProductLookup.class);
	 * 
	 * productService = createAMock(ProductService.class);
	 * 
	 * productRegistrationService = createAMock(ProductRegistrationService.class);
	 * 
	 * sut = new ProductRegistrationAdapterImpl(customerLookup, productLookup,
	 * productRegistrationService, productService); setMocks(customerLookup,
	 * productLookup, productRegistrationService); customer = new Customer();
	 * linkedProduct = new LinkedProduct();
	 * 
	 * regProduct = new RegisterableProduct();
	 * regProduct.setProductName("regProductName");
	 * 
	 * enforceProduct = new EnforceableProductDto();
	 * enforceProduct.setState(ProductState.ACTIVE.toString());
	 * 
	 * Set<PageComponent> pageComponents = new HashSet<PageComponent>();
	 * 
	 * PageComponent pcomp1 = new PageComponent();
	 * 
	 * Component comp1 = new Component(); Set<Field> fields = new HashSet<Field>();
	 * Question question = new Question(); Element element = new Element();
	 * element.setQuestion(question); Field f1 = new Field();
	 * f1.setElement(element); question.setDescription("field.one");
	 * f1.setRequired(true);
	 * 
	 * fields.add(f1); comp1.setFields(fields); pcomp1.setComponent(comp1);
	 * pageComponents.add(pcomp1);
	 * 
	 * this.productRegistration = new ProductRegistration(); }
	 * 
	 * private String randomMessage() { return UUID.randomUUID().toString(); }
	 * 
	 * @Test public void testUnknownCustomer() throws WebServiceException {
	 * ProductRegistrationRequest request =
	 * getProductRegistrationRequest(PRODUCT_ID, CUSTOMER_ID, EMPTY_REG_INFO);
	 * 
	 * String message = randomMessage();
	 * expect(this.customerLookup.getCustomerByWsUserId(eqWsUserId(CUSTOMER_ID))).
	 * andThrow(new WebServiceValidationException(message));
	 * expect(this.productLookup.lookupEnforceableProductByIdentifier(eqInteralId(
	 * PRODUCT_ID))).andReturn(enforceProduct); replay(getMocks());
	 * 
	 * ProductRegistrationResponse response =
	 * sut.createProductRegistration(request); // verify(getMocks());
	 * 
	 * Assert.assertEquals(message, response.getErrorStatus().getStatusReason());
	 * Assert.assertEquals(StatusCode.CLIENT_ERROR,
	 * response.getErrorStatus().getStatusCode()); }
	 * 
	 * @Test public void testUnknownProduct() throws
	 * WebServiceException,ServiceLayerException { ProductRegistrationRequest
	 * request = getProductRegistrationRequest(PRODUCT_ID, CUSTOMER_ID,
	 * EMPTY_REG_INFO);
	 * 
	 * String message = randomMessage();
	 * 
	 * expect(this.customerLookup.getCustomerByWsUserId(eqWsUserId(CUSTOMER_ID))).
	 * andReturn(customer);
	 * 
	 * 
	 * expect(this.productLookup.lookupEnforceableProductByIdentifier(eqInteralId(
	 * PRODUCT_ID))).andReturn(enforceProduct);
	 * 
	 * expect(this.productRegistrationService.createProductRegistration(
	 * enforceProduct, customer,
	 * registrationPageData)).andReturn(productRegistration);
	 * 
	 * 
	 * replay(getMocks()); ProductRegistrationResponse response =
	 * sut.createProductRegistration(request);
	 * Assert.assertNull(response.getErrorStatus());
	 * 
	 * }
	 * 
	 * @Test public void testLinkedProduct() throws
	 * WebServiceException,ServiceLayerException { ProductRegistrationRequest
	 * request = getProductRegistrationRequest(PRODUCT_ID, CUSTOMER_ID,
	 * EMPTY_REG_INFO);
	 * 
	 * expect(this.customerLookup.getCustomerByWsUserId(eqWsUserId(CUSTOMER_ID))).
	 * andReturn(customer);
	 * expect(this.productLookup.lookupProductByIdentifier(eqInteralId(PRODUCT_ID)))
	 * .andReturn(linkedProduct); //Registration<?> reg = new
	 * Registration<ProductRegistrationDefinition>();
	 * expect(this.productRegistrationService.createProductRegistration(
	 * enforceProduct, customer,
	 * registrationPageData)).andReturn(productRegistration);
	 * 
	 * expect(this.productLookup.lookupEnforceableProductByIdentifier(eqInteralId(
	 * PRODUCT_ID))).andReturn(enforceProduct); replay(getMocks());
	 * ProductRegistrationResponse response =
	 * sut.createProductRegistration(request);
	 * 
	 * Assert.assertNull(response.getErrorStatus());
	 * 
	 * }
	 * 
	 * @Test public void testCreateProductRegistrationOkay() throws
	 * WebServiceException, ServiceLayerException {
	 * 
	 * ProductRegistrationRequest request =
	 * getProductRegistrationRequest(PRODUCT_ID, CUSTOMER_ID, EMPTY_REG_INFO);
	 * 
	 * expect(this.customerLookup.getCustomerByWsUserId(eqWsUserId(CUSTOMER_ID))).
	 * andReturn(customer);
	 * expect(this.productLookup.lookupProductByIdentifier(eqInteralId(PRODUCT_ID)))
	 * .andReturn(regProduct);
	 * expect(this.productLookup.lookupEnforceableProductByIdentifier(eqInteralId(
	 * PRODUCT_ID))).andReturn(enforceProduct);
	 * expect(this.productRegistrationService.createProductRegistration(eq(
	 * enforceProduct), eq(customer), (Map<String, String>)
	 * anyObject(Map.class))).andReturn(productRegistration);
	 * 
	 * replay(getMocks()); ProductRegistrationResponse response =
	 * sut.createProductRegistration(request); // verify(getMocks());
	 * 
	 * Assert.assertNull(response.getErrorStatus()); }
	 * 
	 * @Test public void
	 * testCreateProductRegistrationServiceLayerValidationException() throws
	 * WebServiceException, ServiceLayerException {
	 * 
	 * ProductRegistrationRequest request =
	 * getProductRegistrationRequest(PRODUCT_ID, CUSTOMER_ID, EMPTY_REG_INFO);
	 * 
	 * expect(this.customerLookup.getCustomerByWsUserId(eqWsUserId(CUSTOMER_ID))).
	 * andReturn(customer);
	 * expect(this.productLookup.lookupProductByIdentifier(eqInteralId(PRODUCT_ID)))
	 * .andReturn(regProduct);
	 * expect(this.productLookup.lookupEnforceableProductByIdentifier(eqInteralId(
	 * PRODUCT_ID))).andReturn(enforceProduct); String randomMessage =
	 * UUID.randomUUID().toString(); ServiceLayerValidationException slve = new
	 * ServiceLayerValidationException(randomMessage);
	 * expect(this.productRegistrationService.createProductRegistration(eq(
	 * enforceProduct), eq(customer), (Map<String, String>)
	 * anyObject(Map.class))).andThrow( slve);
	 * 
	 * replay(getMocks()); ProductRegistrationResponse response =
	 * sut.createProductRegistration(request); // verify(getMocks());
	 * 
	 * Assert.assertEquals(randomMessage,
	 * response.getErrorStatus().getStatusReason());
	 * Assert.assertEquals(StatusCode.CLIENT_ERROR,
	 * response.getErrorStatus().getStatusCode()); }
	 * 
	 * @Test public void testCreateProductRegistrationServiceLayerException() throws
	 * WebServiceException { String randomMessage = UUID.randomUUID().toString();
	 * ServiceLayerException sle = new ServiceLayerException(randomMessage); try {
	 * ProductRegistrationRequest request =
	 * getProductRegistrationRequest(PRODUCT_ID, CUSTOMER_ID, EMPTY_REG_INFO);
	 * 
	 * expect(this.customerLookup.getCustomerByWsUserId(eqWsUserId(CUSTOMER_ID))).
	 * andReturn(customer);
	 * expect(this.productLookup.lookupProductByIdentifier(eqInteralId(PRODUCT_ID)))
	 * .andReturn(regProduct);
	 * 
	 * expect(this.productRegistrationService.createProductRegistration(eq(
	 * enforceProduct), eq(customer), (Map<String, String>) anyObject(Map.class)))
	 * .andThrow(sle);
	 * expect(this.productLookup.lookupEnforceableProductByIdentifier(eqInteralId(
	 * PRODUCT_ID))).andReturn(enforceProduct); replay(getMocks());
	 * 
	 * ProductRegistrationResponse response =
	 * sut.createProductRegistration(request); Assert.fail("exception expected"); }
	 * catch (Exception ex) { Assert.assertEquals(WebServiceException.class,
	 * ex.getClass());
	 * Assert.assertEquals(ERR_PRODUCT_REGISTRATION_CREATION_UNEXPECTED,
	 * ex.getMessage()); Assert.assertEquals(sle,ex.getCause()); } finally {
	 * //verify(getMocks()); } }
	 * 
	 * private ProductRegistrationRequest getProductRegistrationRequest(String
	 * productId, String customerId, Map<String, String> registrationData) {
	 * ProductRegistrationRequest request = new ProductRegistrationRequest();
	 * enforceProduct = new EnforceableProductDto();
	 * enforceProduct.setState(ProductState.ACTIVE.toString());
	 * request.setProductId(IdUtils.getInternalIdentifier(productId));
	 * request.setUserId(IdUtils.getInternalIdentifier(customerId));
	 * RegistrationInformation[] regInfo = new
	 * RegistrationInformation[registrationData.size()]; int idx = 0; for
	 * (Map.Entry<String, String> entry : registrationData.entrySet()) {
	 * RegistrationInformation ri = new RegistrationInformation();
	 * ri.setRegistrationKey(entry.getKey());
	 * ri.setRegistrationValue(entry.getValue()); regInfo[idx] = ri; idx++; }
	 * request.setRegistrationInformation(regInfo);
	 * 
	 * return request; }
	 * 
	 * protected WsUserId eqWsUserId(final String internalId) { IArgumentMatcher
	 * matcher = new IArgumentMatcher() {
	 * 
	 * @Override public boolean matches(Object arg) { if (arg instanceof WsUserId ==
	 * false) { return false; } WsUserId wsUserId = (WsUserId) arg; boolean same =
	 * wsUserId.getUserId() != null && wsUserId.getUserId().getInternalId() != null
	 * && wsUserId.getUserId().getInternalId().getId().equals(internalId); return
	 * same; }
	 * 
	 * @Override public void appendTo(StringBuffer buffer) { // TODO Auto-generated
	 * method stub
	 * 
	 * } }; EasyMock.reportMatcher(matcher); return null; }
	 * 
	 * protected Identifier eqInteralId(final String internalId) { IArgumentMatcher
	 * matcher = new IArgumentMatcher() {
	 * 
	 * @Override public boolean matches(Object arg) { if (arg instanceof Identifier
	 * == false) { return false; } Identifier ident = (Identifier) arg; boolean same
	 * = ident != null && ident.getInternalId() != null &&
	 * ident.getInternalId().getId().equals(internalId); return same; }
	 * 
	 * @Override public void appendTo(StringBuffer buffer) { // TODO Auto-generated
	 * method stub
	 * 
	 * } }; EasyMock.reportMatcher(matcher); return null; }
	 * 
	 */}
