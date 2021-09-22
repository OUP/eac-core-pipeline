package com.oup.eac.ws.v2.service.impl;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;

import javax.naming.NamingException;

import junit.framework.Assert;

import org.easymock.EasyMock;
import org.easymock.IArgumentMatcher;
import org.junit.Before;
import org.junit.Test;
import org.springframework.security.access.AccessDeniedException;

import com.oup.eac.common.utils.activationcode.EacActivationCode;
import com.oup.eac.domain.ActivationCode;
import com.oup.eac.domain.ActivationCodeBatch;
import com.oup.eac.domain.ActivationCodeBatch.ActivationCodeFormat;
import com.oup.eac.domain.ActivationCodeRegistration;
import com.oup.eac.domain.ActivationCodeRegistrationDefinition;
import com.oup.eac.domain.ActivationCodeSearchDto;
import com.oup.eac.domain.Customer;
import com.oup.eac.domain.Division;
import com.oup.eac.domain.EacGroups;
import com.oup.eac.domain.ExternalCustomerId;
import com.oup.eac.domain.ExternalProductId;
import com.oup.eac.domain.ExternalSystem;
import com.oup.eac.domain.ExternalSystemIdType;
import com.oup.eac.domain.LicenceTemplate;
import com.oup.eac.domain.Product;
import com.oup.eac.domain.ProductPageDefinition;
import com.oup.eac.domain.ProductRegistrationDefinition;
import com.oup.eac.domain.RegisterableProduct;
import com.oup.eac.domain.RegisterableProduct.RegisterableType;
import com.oup.eac.domain.Registration;
import com.oup.eac.domain.RegistrationActivation;
import com.oup.eac.domain.entitlement.ProductEntitlementInfoDto;
import com.oup.eac.dto.ExternalCustomerIdDto;
import com.oup.eac.dto.ExternalProductIdDto;
import com.oup.eac.dto.GuestRedeemActivationCodeDto;
import com.oup.eac.dto.LicenceDto;
import com.oup.eac.integration.facade.ErightsFacade;
import com.oup.eac.integration.facade.exceptions.ErightsException;
import com.oup.eac.integration.facade.exceptions.GroupNotFoundException;
import com.oup.eac.integration.facade.exceptions.LicenseNotFoundException;
import com.oup.eac.integration.facade.exceptions.ProductNotFoundException;
import com.oup.eac.integration.facade.exceptions.UserNotFoundException;
import com.oup.eac.service.ActivationCodeService;
import com.oup.eac.service.ExternalIdService;
import com.oup.eac.service.LicenceService;
import com.oup.eac.service.ProductService;
import com.oup.eac.service.RegistrationService;
import com.oup.eac.service.ServiceLayerException;
import com.oup.eac.service.UserEntitlementsService;
import com.oup.eac.ws.v2.binding.access.GuestRedeemActivationCodeResponse;
import com.oup.eac.ws.v2.binding.access.RedeemActivationCodeResponse;
import com.oup.eac.ws.v2.binding.access.SearchActivationCodeResponse;
import com.oup.eac.ws.v2.binding.access.ValidateActivationCodeResponse;
import com.oup.eac.ws.v2.binding.access.ValidateActivationCodeResponseSequenceItem;
import com.oup.eac.ws.v2.binding.common.ActivationCodeInfo;
import com.oup.eac.ws.v2.binding.common.ErrorStatus;
import com.oup.eac.ws.v2.binding.common.ExternalIdentifier;
import com.oup.eac.ws.v2.binding.common.GuestProductEntitlement;
import com.oup.eac.ws.v2.binding.common.Identifiers;
import com.oup.eac.ws.v2.binding.common.ProductEntitlement;
import com.oup.eac.ws.v2.binding.common.RedeemedInfo;
import com.oup.eac.ws.v2.binding.common.WsUserId;
import com.oup.eac.ws.v2.binding.common.types.StatusCode;
import com.oup.eac.ws.v2.ex.WebServiceException;
import com.oup.eac.ws.v2.ex.WebServiceValidationException;
import com.oup.eac.ws.v2.service.ActivationCodeAdapter;
import com.oup.eac.ws.v2.service.WsCustomerLookup;
import com.oup.eac.ws.v2.service.WsExternalSystemLookup;
import com.oup.eac.ws.v2.service.entitlements.EntitlementsAdapterHelper;
import com.oup.eac.ws.v2.service.entitlements.ExternalCustomerIdDtoSource;

public class ActivationCodeAdapterImplMockTest /* extends AbstractMockTest */ {
	/*
	 * 
	 * private static final String LANDING_PAGE_URL1 = "LANDING_PAGE_URL1"; private
	 * static final String ACTIVATION_CODE_1 = "AC_CODE_1"; private static final
	 * String ACTIVATION_CODE_WITH_DASHES = "1231-2343-3453"; private static final
	 * String ACTIVATION_CODE_WITHOUT_DASHES = "123123433453"; private static final
	 * String SYSTEM_ID = "SYS_ID1";
	 * 
	 * private ActivationCodeAdapter sut; private RegistrationService
	 * mRegistrationService; private WsCustomerLookup mCustomerLookup; private
	 * WsExternalSystemLookup mExternalSystemLookup; private
	 * ExternalCustomerIdDtoSource externalCustomerIdDtoSource; private
	 * ActivationCodeService activationCodeService; private ProductService
	 * productService; private LicenceService liceseService; private ErightsFacade
	 * fakeErightsFacade;
	 * 
	 * private RegisterableProduct registerableProduct1; private
	 * ActivationCodeRegistrationDefinition regDef1; private ActivationCodeBatch
	 * batch1; private ActivationCode aCode1; private Customer customer; private
	 * Locale locale; private WsUserId wsUserId; private UserEntitlementsService
	 * mUserEntitlementsService; private EntitlementsAdapterHelper
	 * mEntitlementsAdapterHelper; private ExternalIdService mExternalIdService;
	 * 
	 * public ActivationCodeAdapterImplMockTest() throws NamingException { super();
	 * }
	 * 
	 * @Before public void setup() {
	 * 
	 * mRegistrationService = EasyMock.createMock(RegistrationService.class);
	 * mCustomerLookup = EasyMock.createMock(WsCustomerLookup.class);
	 * mExternalSystemLookup = EasyMock.createMock(WsExternalSystemLookup.class);
	 * externalCustomerIdDtoSource =
	 * EasyMock.createMock(ExternalCustomerIdDtoSource.class);
	 * mUserEntitlementsService =
	 * EasyMock.createMock(UserEntitlementsService.class);
	 * mEntitlementsAdapterHelper =
	 * EasyMock.createMock(EntitlementsAdapterHelper.class);
	 * 
	 * activationCodeService = EasyMock.createMock(ActivationCodeService.class);
	 * mExternalIdService = EasyMock.createMock(ExternalIdService.class);
	 * productService = EasyMock.createMock(ProductService.class); liceseService =
	 * EasyMock.createMock(LicenceService.class); fakeErightsFacade =
	 * EasyMock.createMock(ErightsFacade.class); this.sut = new
	 * ActivationCodeAdapterImpl( mCustomerLookup, mRegistrationService,
	 * mExternalSystemLookup, externalCustomerIdDtoSource, activationCodeService,
	 * mUserEntitlementsService, mEntitlementsAdapterHelper, mExternalIdService,
	 * productService, liceseService, fakeErightsFacade); this.registerableProduct1
	 * = new RegisterableProduct();
	 * this.registerableProduct1.setLandingPage(LANDING_PAGE_URL1); this.batch1 =
	 * new ActivationCodeBatch(); this.regDef1 = new
	 * ActivationCodeRegistrationDefinition(); this.wsUserId = new WsUserId();
	 * 
	 * Set<ActivationCodeBatch> batches = new HashSet<ActivationCodeBatch>();
	 * batches.add(batch1);
	 * 
	 * this.regDef1.setActivationCodeBatchs(batches);
	 * this.regDef1.setProduct(this.registerableProduct1);
	 * this.batch1.setActivationCodeRegistrationDefinition(regDef1);
	 * 
	 * this.aCode1 = new ActivationCode();
	 * 
	 * this.aCode1.setCode(ACTIVATION_CODE_1);
	 * 
	 * this.customer = new Customer();
	 * 
	 * this.aCode1.setActivationCodeBatch(batch1);
	 * 
	 * this.setMocks( mRegistrationService, mCustomerLookup, mExternalSystemLookup,
	 * externalCustomerIdDtoSource, activationCodeService, mUserEntitlementsService,
	 * mEntitlementsAdapterHelper, mExternalIdService, productService,
	 * liceseService, fakeErightsFacade );
	 * 
	 * this.locale = Locale.getDefault();
	 * 
	 * ExternalSystem extSys1 = new ExternalSystem(); extSys1.setName(SYSTEM_ID);
	 * 
	 * }
	 * 
	 * @Test public void testRedeeemActivationCodesSuccessForProduct() throws
	 * WebServiceException, ServiceLayerException, ProductNotFoundException,
	 * UserNotFoundException, LicenseNotFoundException, AccessDeniedException,
	 * GroupNotFoundException, ErightsException, Exception { List<LicenceDto>
	 * licenses = new ArrayList<LicenceDto>() ;
	 * 
	 * mExternalSystemLookup.validateExternalSystem(SYSTEM_ID); expectLastCall();
	 * 
	 * expect(mCustomerLookup.getCustomerByWsUserId(this.wsUserId)).andReturn(
	 * customer);
	 * expect(activationCodeService.getActivationCodeWithDetailsWS(ACTIVATION_CODE_1
	 * )).andReturn(aCode1);
	 * 
	 * activationCodeService.validateActivationCode(aCode1); expectLastCall();
	 * expect(mRegistrationService.createRegistrationAndAddLicence(customer, aCode1,
	 * locale)).andReturn(licenses);
	 * 
	 * List<ProductEntitlementInfoDto> ents = new
	 * ArrayList<ProductEntitlementInfoDto>(); ProductEntitlementInfoDto info1 = new
	 * ProductEntitlementInfoDto(); ents.add(info1);
	 * 
	 * ProductEntitlement ent1 = new ProductEntitlement();
	 * expect(mUserEntitlementsService.getProductEntitlementInfo(licenses)).
	 * andReturn(ents);
	 * expect(mEntitlementsAdapterHelper.getProductEntitlement(info1.getEntitlement(
	 * ))).andReturn(ent1);
	 * 
	 * replay(getMocks());
	 * 
	 * // CALL THE METHOD WE ARE TESTING RedeemActivationCodeResponse result =
	 * sut.redeemActivationCode(SYSTEM_ID, this.wsUserId, ACTIVATION_CODE_1,
	 * locale); Assert.assertNotNull(result); ProductEntitlement[] data =
	 * result.getEntitlement(); List<ProductEntitlement> resultEnts =
	 * Arrays.asList(data); Assert.assertTrue(resultEnts.contains(ent1));
	 * Assert.assertNull(result.getErrorStatus());
	 * 
	 * verify(getMocks()); }
	 * 
	 * @Test public void testRedeeemActicationCodesSuccessForProductWithDashes()
	 * throws WebServiceException, ServiceLayerException, ProductNotFoundException,
	 * UserNotFoundException, LicenseNotFoundException, AccessDeniedException,
	 * GroupNotFoundException, ErightsException, Exception { List<LicenceDto>
	 * licenses = new ArrayList<LicenceDto>() ; List<Registration<? extends
	 * ProductRegistrationDefinition>> pRegDefList = new ArrayList<Registration<?
	 * extends ProductRegistrationDefinition>>();
	 * 
	 * mExternalSystemLookup.validateExternalSystem(SYSTEM_ID); expectLastCall();
	 * 
	 * expect(mCustomerLookup.getCustomerByWsUserId(this.wsUserId)).andReturn(
	 * customer); expect(activationCodeService.getActivationCodeWithDetailsWS(
	 * ACTIVATION_CODE_WITHOUT_DASHES)).andReturn(aCode1);
	 * activationCodeService.validateActivationCode(aCode1); expectLastCall();
	 * 
	 * expect(mRegistrationService.createRegistrationAndAddLicence(customer, aCode1,
	 * locale)).andReturn(licenses);
	 * 
	 * List<ProductEntitlementInfoDto> ents = new
	 * ArrayList<ProductEntitlementInfoDto>(); ProductEntitlementInfoDto info1 = new
	 * ProductEntitlementInfoDto(); ents.add(info1);
	 * 
	 * ProductEntitlement ent1 = new ProductEntitlement();
	 * expect(mUserEntitlementsService.getProductEntitlementInfo(licenses)).
	 * andReturn(ents);
	 * expect(mEntitlementsAdapterHelper.getProductEntitlement(info1.getEntitlement(
	 * ))).andReturn(ent1);
	 * 
	 * 
	 * replay(getMocks());
	 * 
	 * // CALL THE METHOD WE ARE TESTING RedeemActivationCodeResponse result =
	 * sut.redeemActivationCode(SYSTEM_ID, this.wsUserId,
	 * ACTIVATION_CODE_WITH_DASHES, locale); Assert.assertNotNull(result);
	 * ProductEntitlement[] data = result.getEntitlement(); List<ProductEntitlement>
	 * resultEnts = Arrays.asList(data);
	 * Assert.assertTrue(resultEnts.contains(ent1));
	 * Assert.assertNull(result.getErrorStatus());
	 * 
	 * verify(getMocks()); }
	 * 
	 * @Test public void testRedeeemActivationCodesSuccessForEacGroup() throws
	 * WebServiceException, ServiceLayerException, ProductNotFoundException,
	 * UserNotFoundException, LicenseNotFoundException, AccessDeniedException,
	 * GroupNotFoundException, ErightsException, Exception { List<LicenceDto>
	 * licenses = new ArrayList<LicenceDto>() ; EacGroups eacGroup = new
	 * EacGroups();
	 * 
	 * mExternalSystemLookup.validateExternalSystem(SYSTEM_ID); expectLastCall();
	 * 
	 * expect(mCustomerLookup.getCustomerByWsUserId(this.wsUserId)).andReturn(
	 * customer);
	 * expect(activationCodeService.getActivationCodeWithDetailsWS(ACTIVATION_CODE_1
	 * )).andReturn(aCode1); activationCodeService.validateActivationCode(aCode1);
	 * expectLastCall();
	 * 
	 * regDef1.setEacGroup(eacGroup); regDef1.setProduct(null);
	 * expect(mRegistrationService.createRegistrationAndAddLicenceForEacGroup(
	 * customer, aCode1, locale)).andReturn(licenses);
	 * List<ProductEntitlementInfoDto> ents = new
	 * ArrayList<ProductEntitlementInfoDto>(); ProductEntitlementInfoDto info1 = new
	 * ProductEntitlementInfoDto(); ProductEntitlementInfoDto info2 = new
	 * ProductEntitlementInfoDto(); ents.add(info1); ents.add(info2);
	 * 
	 * ProductEntitlement ent1 = new ProductEntitlement(); ProductEntitlement ent2 =
	 * new ProductEntitlement();
	 * expect(mUserEntitlementsService.getProductEntitlementInfo(licenses)).
	 * andReturn(ents);
	 * expect(mEntitlementsAdapterHelper.getProductEntitlement(info1.getEntitlement(
	 * ))).andReturn(ent1);
	 * expect(mEntitlementsAdapterHelper.getProductEntitlement(info2.getEntitlement(
	 * ))).andReturn(ent2);
	 * 
	 * replay(getMocks());
	 * 
	 * // CALL THE METHOD WE ARE TESTING RedeemActivationCodeResponse result =
	 * sut.redeemActivationCode(SYSTEM_ID, this.wsUserId, ACTIVATION_CODE_1,
	 * locale); Assert.assertNotNull(result); ProductEntitlement[] data =
	 * result.getEntitlement(); List<ProductEntitlement> resultEnts =
	 * Arrays.asList(data); Assert.assertTrue(resultEnts.contains(ent1));
	 * Assert.assertTrue(resultEnts.contains(ent2));
	 * Assert.assertNull(result.getErrorStatus());
	 * 
	 * verify(getMocks()); }
	 * 
	 * @Test public void testRedeeemActicationCodesSuccessForEacGroupWithDashes()
	 * throws WebServiceException, ServiceLayerException, ProductNotFoundException,
	 * UserNotFoundException, LicenseNotFoundException, AccessDeniedException,
	 * GroupNotFoundException, ErightsException,Exception { List<LicenceDto>
	 * licenses = new ArrayList<LicenceDto>() ; EacGroups eacGroup = new
	 * EacGroups();
	 * 
	 * mExternalSystemLookup.validateExternalSystem(SYSTEM_ID); expectLastCall();
	 * 
	 * expect(mCustomerLookup.getCustomerByWsUserId(this.wsUserId)).andReturn(
	 * customer); expect(activationCodeService.getActivationCodeWithDetailsWS(
	 * ACTIVATION_CODE_WITHOUT_DASHES)).andReturn(aCode1);
	 * activationCodeService.validateActivationCode(aCode1); expectLastCall();
	 * regDef1.setEacGroup(eacGroup); regDef1.setProduct(null);
	 * expect(mRegistrationService.createRegistrationAndAddLicenceForEacGroup(
	 * customer, aCode1, locale)).andReturn(licenses);
	 * List<ProductEntitlementInfoDto> ents = new
	 * ArrayList<ProductEntitlementInfoDto>(); ProductEntitlementInfoDto info1 = new
	 * ProductEntitlementInfoDto(); ProductEntitlementInfoDto info2 = new
	 * ProductEntitlementInfoDto(); ents.add(info1); ents.add(info2);
	 * 
	 * ProductEntitlement ent1 = new ProductEntitlement(); ProductEntitlement ent2 =
	 * new ProductEntitlement();
	 * expect(mUserEntitlementsService.getProductEntitlementInfo(licenses)).
	 * andReturn(ents);
	 * expect(mEntitlementsAdapterHelper.getProductEntitlement(info1.getEntitlement(
	 * ))).andReturn(ent1);
	 * expect(mEntitlementsAdapterHelper.getProductEntitlement(info2.getEntitlement(
	 * ))).andReturn(ent2);
	 * 
	 * replay(getMocks());
	 * 
	 * // CALL THE METHOD WE ARE TESTING RedeemActivationCodeResponse result =
	 * sut.redeemActivationCode(SYSTEM_ID, this.wsUserId,
	 * ACTIVATION_CODE_WITH_DASHES, locale); Assert.assertNotNull(result);
	 * ProductEntitlement[] data = result.getEntitlement(); List<ProductEntitlement>
	 * resultEnts = Arrays.asList(data);
	 * Assert.assertTrue(resultEnts.contains(ent1));
	 * Assert.assertTrue(resultEnts.contains(ent2));
	 * Assert.assertNull(result.getErrorStatus());
	 * 
	 * verify(getMocks()); }
	 * 
	 * @Test public void testRedeeemActicationCodesFailureWithNoCode() throws
	 * WebServiceException, ServiceLayerException, ProductNotFoundException,
	 * UserNotFoundException, LicenseNotFoundException, AccessDeniedException,
	 * GroupNotFoundException, ErightsException { replay(getMocks());
	 * 
	 * RedeemActivationCodeResponse result = sut.redeemActivationCode(SYSTEM_ID,
	 * this.wsUserId, "", locale);
	 * 
	 * Assert.assertNotNull(result); Assert.assertEquals(StatusCode.CLIENT_ERROR,
	 * result.getErrorStatus().getStatusCode());
	 * Assert.assertEquals("No Activation Code supplied",
	 * result.getErrorStatus().getStatusReason()); verify(getMocks()); }
	 * 
	 * @Test public void
	 * testRedeeemActicationCodesFailureResultsInClientErrorResponse() throws
	 * WebServiceException, ServiceLayerException, ProductNotFoundException,
	 * UserNotFoundException, LicenseNotFoundException, AccessDeniedException,
	 * GroupNotFoundException, ErightsException {
	 * expect(mCustomerLookup.getCustomerByWsUserId(this.wsUserId)).andThrow(new
	 * WebServiceValidationException("the customer does not exist"));
	 * mExternalSystemLookup.validateExternalSystem(SYSTEM_ID); expectLastCall();
	 * replay(getMocks());
	 * 
	 * // CALL THE METHOD WE ARE TESTING
	 * 
	 * RedeemActivationCodeResponse result = sut.redeemActivationCode(SYSTEM_ID,
	 * this.wsUserId, ACTIVATION_CODE_1, locale);
	 * 
	 * verify(getMocks());
	 * 
	 * Assert.assertNotNull(result.getErrorStatus());
	 * Assert.assertEquals("the customer does not exist",
	 * result.getErrorStatus().getStatusReason());
	 * Assert.assertEquals(StatusCode.CLIENT_ERROR,
	 * result.getErrorStatus().getStatusCode());
	 * 
	 * }
	 * 
	 * @Test public void testRedeeemActicationCodesFailureWithInvalidCode() throws
	 * WebServiceException, ServiceLayerException, ProductNotFoundException,
	 * UserNotFoundException, LicenseNotFoundException, AccessDeniedException,
	 * GroupNotFoundException, ErightsException {
	 * mExternalSystemLookup.validateExternalSystem(SYSTEM_ID); expectLastCall();
	 * expect(mCustomerLookup.getCustomerByWsUserId(this.wsUserId)).andReturn(
	 * customer);
	 * expect(activationCodeService.getActivationCodeWithDetailsWS(ACTIVATION_CODE_1
	 * )).andReturn(null); replay(getMocks());
	 * 
	 * RedeemActivationCodeResponse result = sut.redeemActivationCode(SYSTEM_ID,
	 * this.wsUserId, ACTIVATION_CODE_1, locale);
	 * 
	 * Assert.assertNotNull(result); Assert.assertEquals(StatusCode.CLIENT_ERROR,
	 * result.getErrorStatus().getStatusCode());
	 * Assert.assertEquals("The activation code  [" + ACTIVATION_CODE_1 +
	 * "] does not exist ", result.getErrorStatus().getStatusReason());
	 * verify(getMocks()); }
	 * 
	 * @Test public void testRedeeemActicationCodesFailureWithCodeHavePageDefInfo()
	 * throws WebServiceException, ServiceLayerException, ProductNotFoundException,
	 * UserNotFoundException, LicenseNotFoundException, AccessDeniedException,
	 * GroupNotFoundException, ErightsException {
	 * mExternalSystemLookup.validateExternalSystem(SYSTEM_ID); expectLastCall();
	 * ProductPageDefinition pageDefinition = new ProductPageDefinition();
	 * aCode1.getActivationCodeBatch().getActivationCodeRegistrationDefinition().
	 * setPageDefinition(pageDefinition);
	 * expect(mCustomerLookup.getCustomerByWsUserId(this.wsUserId)).andReturn(
	 * customer);
	 * expect(activationCodeService.getActivationCodeWithDetailsWS(ACTIVATION_CODE_1
	 * )).andReturn(aCode1);
	 * 
	 * replay(getMocks());
	 * 
	 * RedeemActivationCodeResponse result = sut.redeemActivationCode(SYSTEM_ID,
	 * this.wsUserId, ACTIVATION_CODE_1, locale);
	 * 
	 * Assert.assertNotNull(result); Assert.assertEquals(StatusCode.CLIENT_ERROR,
	 * result.getErrorStatus().getStatusCode()); Assert.
	 * assertEquals("You cannot redeem this type of activation code : it requires the capture of product registration information"
	 * , result.getErrorStatus().getStatusReason()); verify(getMocks()); }
	 * 
	 * @Test public void
	 * testRedeeemActicationCodesFailureWithCodeHasProductAndGroupBoth() throws
	 * WebServiceException, ServiceLayerException, ProductNotFoundException,
	 * UserNotFoundException, LicenseNotFoundException, AccessDeniedException,
	 * GroupNotFoundException, ErightsException { EacGroups eacGroup = new
	 * EacGroups(); mExternalSystemLookup.validateExternalSystem(SYSTEM_ID);
	 * expectLastCall();
	 * expect(mCustomerLookup.getCustomerByWsUserId(this.wsUserId)).andReturn(
	 * customer);
	 * expect(activationCodeService.getActivationCodeWithDetailsWS(ACTIVATION_CODE_1
	 * )).andReturn(aCode1); regDef1.setEacGroup(eacGroup); replay(getMocks());
	 * 
	 * RedeemActivationCodeResponse result = sut.redeemActivationCode(SYSTEM_ID,
	 * this.wsUserId, ACTIVATION_CODE_1, locale);
	 * 
	 * Assert.assertNotNull(result); Assert.assertEquals(StatusCode.CLIENT_ERROR,
	 * result.getErrorStatus().getStatusCode());
	 * Assert.assertEquals("The activation code details are not valid.",
	 * result.getErrorStatus().getStatusReason()); verify(getMocks()); }
	 * 
	 * @Test public void
	 * testRedeeemActicationCodesFailureWithCodeHasNoProductAndGroupBoth() throws
	 * WebServiceException, ServiceLayerException, ProductNotFoundException,
	 * UserNotFoundException, LicenseNotFoundException, AccessDeniedException,
	 * GroupNotFoundException, ErightsException {
	 * mExternalSystemLookup.validateExternalSystem(SYSTEM_ID); expectLastCall();
	 * expect(mCustomerLookup.getCustomerByWsUserId(this.wsUserId)).andReturn(
	 * customer);
	 * expect(activationCodeService.getActivationCodeWithDetailsWS(ACTIVATION_CODE_1
	 * )).andReturn(aCode1); regDef1.setProduct(null); replay(getMocks());
	 * 
	 * RedeemActivationCodeResponse result = sut.redeemActivationCode(SYSTEM_ID,
	 * this.wsUserId, ACTIVATION_CODE_1, locale);
	 * 
	 * Assert.assertNotNull(result); Assert.assertEquals(StatusCode.CLIENT_ERROR,
	 * result.getErrorStatus().getStatusCode());
	 * Assert.assertEquals("The activation code details are not valid.",
	 * result.getErrorStatus().getStatusReason()); verify(getMocks()); }
	 * 
	 * @Test public void testRedeeemActicationCodesWithServiceLayerException()
	 * throws Exception { mExternalSystemLookup.validateExternalSystem(SYSTEM_ID);
	 * expectLastCall();
	 * expect(mCustomerLookup.getCustomerByWsUserId(this.wsUserId)).andReturn(
	 * customer);
	 * expect(activationCodeService.getActivationCodeWithDetailsWS(ACTIVATION_CODE_1
	 * )).andReturn(aCode1); activationCodeService.validateActivationCode(aCode1) ;
	 * expectLastCall(); expect(
	 * mRegistrationService.createRegistrationAndAddLicence(customer,aCode1,locale))
	 * .andThrow(new WebServiceException("Service layer Exception.")) ;
	 * replay(getMocks()); try { RedeemActivationCodeResponse result =
	 * sut.redeemActivationCode(SYSTEM_ID, this.wsUserId, ACTIVATION_CODE_1,
	 * locale); if (result.getEntitlement().length > 0 ){
	 * Assert.fail("exception excepted"); } else { throw new
	 * WebServiceException("Service layer Exception.") ; } } catch
	 * (WebServiceException ex) {
	 * Assert.assertEquals("Service layer Exception.",ex.getMessage()); }
	 * 
	 * verify(getMocks()); }
	 * 
	 * @Test public void
	 * testRedeeemActicationCodesResultsInWebServiceValidationException() throws
	 * WebServiceException, ProductNotFoundException, UserNotFoundException,
	 * LicenseNotFoundException, AccessDeniedException, GroupNotFoundException,
	 * ErightsException { WebServiceValidationException ex = new
	 * WebServiceValidationException("oops1");
	 * expect(mCustomerLookup.getCustomerByWsUserId(this.wsUserId)).andThrow(ex);
	 * mExternalSystemLookup.validateExternalSystem(SYSTEM_ID); expectLastCall();
	 * 
	 * replay(getMocks());
	 * 
	 * RedeemActivationCodeResponse result = sut.redeemActivationCode(SYSTEM_ID,
	 * this.wsUserId, ACTIVATION_CODE_1, locale);
	 * Assert.assertEquals(StatusCode.CLIENT_ERROR,
	 * result.getErrorStatus().getStatusCode()); Assert.assertEquals("oops1",
	 * result.getErrorStatus().getStatusReason());
	 * 
	 * verify(getMocks());
	 * 
	 * }
	 * 
	 * @Test public void testValidateActivationCodeNoCodeSupplied() throws
	 * WebServiceException, ServiceLayerException { replay(getMocks());
	 * ValidateActivationCodeResponse result =
	 * sut.validateActivationCode("systemId", "");
	 * Assert.assertEquals(StatusCode.CLIENT_ERROR,
	 * result.getErrorStatus().getStatusCode());
	 * Assert.assertEquals("No Activation Code supplied",
	 * result.getErrorStatus().getStatusReason());
	 * 
	 * verify(getMocks()); }
	 * 
	 * @Test public void testValidateActivationCodeBadCodeSupplied() throws
	 * WebServiceException, ServiceLayerException, ErightsException { ActivationCode
	 * ac = new ActivationCode(); ActivationCodeBatch acb = new
	 * ActivationCodeBatch(); ac.setActivationCodeBatch(acb);
	 * ActivationCodeRegistrationDefinition acrd = new
	 * ActivationCodeRegistrationDefinition();
	 * acb.setActivationCodeRegistrationDefinition(acrd); RegisterableProduct rp =
	 * new RegisterableProduct(); rp.setProductName("productName");
	 * rp.setId("pro123"); acrd.setProduct(rp);
	 * 
	 * ExternalProductIdDto externalids = new ProductIdDtoBuilder().build();
	 * List<RegisterableProduct> prodList = Arrays.asList(rp);
	 * expect(this.fakeErightsFacade.validateActivationCode(ACTIVATION_CODE_1,
	 * SYSTEM_ID)).andThrow(new
	 * ErightsException("This is not a valid activation code"
	 * ,ActivationCodeResponseSTATUS.ERROR.toString())); //expectLastCall() ;
	 * expect(mExternalIdService.getExternalProductIds(eqProductList(prodList),
	 * EasyMock.eq(SYSTEM_ID))).andReturn(externalids); replay(getMocks());
	 * ValidateActivationCodeResponse result = sut.validateActivationCode(SYSTEM_ID,
	 * ACTIVATION_CODE_1);
	 * 
	 * 
	 * Assert.assertEquals(StatusCode.CLIENT_ERROR,
	 * result.getErrorStatus().getStatusCode());
	 * Assert.assertEquals("This is not a valid activation code",
	 * result.getErrorStatus().getStatusReason()); }
	 * 
	 * @Test public void testValidateActivationCodeValidCodeSupplied() throws
	 * Exception { ActivationCode ac = new ActivationCode(); ActivationCodeBatch acb
	 * = new ActivationCodeBatch(); ac.setActivationCodeBatch(acb);
	 * ActivationCodeRegistrationDefinition acrd = new
	 * ActivationCodeRegistrationDefinition();
	 * acb.setActivationCodeRegistrationDefinition(acrd); RegisterableProduct rp =
	 * new RegisterableProduct(); acrd.setProduct(rp); List<RegisterableProduct>
	 * prodList = Arrays.asList(rp);
	 * expect(this.fakeErightsFacade.validateActivationCode(
	 * ACTIVATION_CODE_WITHOUT_DASHES,SYSTEM_ID)).andReturn(prodList);
	 * expect(this.activationCodeService.getActivationCodeByCode(
	 * ACTIVATION_CODE_WITHOUT_DASHES)).andReturn(ac);
	 * expect(this.activationCodeService.getActivationCodeAndDefinitionByCode(
	 * ACTIVATION_CODE_WITHOUT_DASHES)).andReturn(ac); ExternalProductIdDto
	 * externalids = new ProductIdDtoBuilder().build();
	 * this.activationCodeService.validateActivationCode(ac); expectLastCall();
	 * 
	 * expect(mExternalIdService.getExternalProductIds(eqProductList(prodList),
	 * EasyMock.eq(SYSTEM_ID))).andReturn(externalids);
	 * 
	 * replay(getMocks());
	 * 
	 * ValidateActivationCodeResponse result = sut.validateActivationCode(SYSTEM_ID,
	 * ACTIVATION_CODE_WITHOUT_DASHES);
	 * 
	 * Assert.assertNull(result.getErrorStatus()); }
	 * 
	 * private List<Product> eqProductList(final List<? extends Product> expected) {
	 * EasyMock.reportMatcher(new IArgumentMatcher() {
	 * 
	 * @Override public void appendTo(StringBuffer buffer) {
	 * buffer.append("eqProductList("+expected+")"); }
	 * 
	 * @Override public boolean matches(Object arg) { if (arg instanceof List ) {
	 * 
	 * @SuppressWarnings("unchecked") List<Product> actual = (List<Product>)arg;
	 * return expected.equals(actual); } return false; } }); return null; }
	 * 
	 * 
	 * @Test public void testValidateActivationCodeValidCodeSuppliedWithDashes()
	 * throws Exception { ActivationCode ac = new ActivationCode();
	 * ActivationCodeBatch acb = new ActivationCodeBatch();
	 * ac.setActivationCodeBatch(acb); ActivationCodeRegistrationDefinition acrd =
	 * new ActivationCodeRegistrationDefinition();
	 * acb.setActivationCodeRegistrationDefinition(acrd); RegisterableProduct rp =
	 * new RegisterableProduct(); rp.setProductName("productName");
	 * rp.setId("pro123"); acrd.setProduct(rp); List<RegisterableProduct> prodList =
	 * Arrays.asList(rp); expect(this.fakeErightsFacade.validateActivationCode(
	 * ACTIVATION_CODE_WITHOUT_DASHES,SYSTEM_ID)).andReturn(prodList);
	 * expect(this.activationCodeService.getActivationCodeByCode(
	 * ACTIVATION_CODE_WITH_DASHES)).andReturn(ac);
	 * expect(this.activationCodeService.getActivationCodeAndDefinitionByCode(
	 * ACTIVATION_CODE_WITH_DASHES)).andReturn(ac);
	 * this.activationCodeService.validateActivationCode(ac); expectLastCall();
	 * ExternalProductIdDto externalids = new ProductIdDtoBuilder().build();
	 * 
	 * expect(mExternalIdService.getExternalProductIds(eqProductList(prodList),
	 * EasyMock.eq(SYSTEM_ID))).andReturn(externalids);
	 * 
	 * // external ids replay(getMocks());
	 * 
	 * ValidateActivationCodeResponse result = sut.validateActivationCode(SYSTEM_ID,
	 * ACTIVATION_CODE_WITH_DASHES);
	 * 
	 * ValidateActivationCodeResponseSequenceItem item =
	 * result.getValidateActivationCodeResponseSequence()[0].
	 * getValidateActivationCodeResponseSequenceItem()[0];
	 * Assert.assertEquals(ACTIVATION_CODE_WITH_DASHES, item.getActivationCode());
	 * Assert.assertEquals(rp.getProductName(), item.getProduct().getProductName());
	 * Assert.assertNull(result.getErrorStatus()); }
	 * 
	 * @Test public void testValidateActivationCodeInvalidCodeSupplied() throws
	 * WebServiceException, ServiceLayerException, ErightsException { ActivationCode
	 * ac = new ActivationCode(); ActivationCodeBatch acb = new
	 * ActivationCodeBatch(); ac.setActivationCodeBatch(acb);
	 * ActivationCodeRegistrationDefinition acrd = new
	 * ActivationCodeRegistrationDefinition();
	 * acb.setActivationCodeRegistrationDefinition(acrd); RegisterableProduct rp =
	 * new RegisterableProduct(); rp.setProductName("productName");
	 * rp.setId("pro123"); acrd.setProduct(rp); List<RegisterableProduct> prodList =
	 * Arrays.asList(rp); String randomMessage = UUID.randomUUID().toString();
	 * expect(this.fakeErightsFacade.validateActivationCode(
	 * ACTIVATION_CODE_WITHOUT_DASHES,SYSTEM_ID)).andThrow(new
	 * ErightsException(randomMessage,ActivationCodeResponseSTATUS.ERROR.toString())
	 * ) ; expect(this.activationCodeService.getActivationCodeByCode(
	 * ACTIVATION_CODE_WITHOUT_DASHES)).andReturn(ac);
	 * expect(this.activationCodeService.getActivationCodeAndDefinitionByCode(
	 * ACTIVATION_CODE_WITHOUT_DASHES)).andReturn(ac);
	 * this.activationCodeService.validateActivationCode(ac);
	 * 
	 * expectLastCall().andThrow(new ServiceLayerException(randomMessage));
	 * replay(getMocks());
	 * 
	 * ValidateActivationCodeResponse result = sut.validateActivationCode(SYSTEM_ID,
	 * ACTIVATION_CODE_WITHOUT_DASHES);
	 * 
	 * 
	 * Assert.assertEquals(StatusCode.CLIENT_ERROR,
	 * result.getErrorStatus().getStatusCode()); Assert.assertEquals(randomMessage,
	 * result.getErrorStatus().getStatusReason()); }
	 * 
	 * @Test public void testInvalidSystemId() throws WebServiceException,
	 * ServiceLayerException, ProductNotFoundException, UserNotFoundException,
	 * LicenseNotFoundException, AccessDeniedException, GroupNotFoundException,
	 * ErightsException {
	 * expect(mCustomerLookup.getCustomerByWsUserId(this.wsUserId)).andReturn(
	 * customer); String randomMessage = UUID.randomUUID().toString();
	 * 
	 * mExternalSystemLookup.validateExternalSystem(SYSTEM_ID);
	 * expectLastCall().andThrow(new WebServiceValidationException(randomMessage));
	 * 
	 * replay(getMocks());
	 * 
	 * RedeemActivationCodeResponse result = sut.redeemActivationCode(SYSTEM_ID,
	 * this.wsUserId, ACTIVATION_CODE_1, locale); ErrorStatus errorStatus =
	 * result.getErrorStatus(); Assert.assertEquals(StatusCode.CLIENT_ERROR,
	 * errorStatus.getStatusCode()); Assert.assertEquals(randomMessage,
	 * errorStatus.getStatusReason());
	 * Assert.assertEquals(0,result.getEntitlement().length);
	 * 
	 * }
	 * 
	 * @Test public void testSearchActivationCodeInvalidCode() throws
	 * WebServiceException, ServiceLayerException { replay(getMocks());
	 * SearchActivationCodeResponse result = sut.searchActivationCode(SYSTEM_ID, "",
	 * false); Assert.assertEquals(StatusCode.CLIENT_ERROR,
	 * result.getErrorStatus().getStatusCode());
	 * Assert.assertEquals("No Activation Code supplied",
	 * result.getErrorStatus().getStatusReason());
	 * 
	 * verify(getMocks()); }
	 * 
	 * @Test public void testSearchActivationCodeInvalidSystemId() throws Exception
	 * { String randomMessage = UUID.randomUUID().toString(); LicenceTemplate
	 * licenceTemplate = SampleDataFactory.createStandardLicenceTemplate();
	 * RegistrationActivation registrationActivation =
	 * SampleDataFactory.createInstantRegistrationActivation(); Division division =
	 * SampleDataFactory.createDivision("MALAYSIA"); ProductPageDefinition
	 * pageDefinition = SampleDataFactory.createProductPageDefinition();
	 * RegisterableProduct product =
	 * SampleDataFactory.createRegisterableProduct(1234,"Malaysia",
	 * RegisterableType.SELF_REGISTERABLE); ActivationCodeRegistrationDefinition
	 * acrd = SampleDataFactory.createActivationCodeRegistrationDefinition(product,
	 * licenceTemplate, registrationActivation, pageDefinition); ActivationCodeBatch
	 * acb =
	 * SampleDataFactory.createActivationCodeBatch(ActivationCodeFormat.EAC_NUMERIC,
	 * licenceTemplate, acrd, null, null, null); ActivationCode ac =
	 * SampleDataFactory.createActivationCode(acb, new EacActivationCode());
	 * 
	 * mExternalSystemLookup.validateExternalSystem("");
	 * expectLastCall().andThrow(new WebServiceValidationException(randomMessage));
	 * replay(getMocks()); SearchActivationCodeResponse result =
	 * sut.searchActivationCode("", ac.getCode(), false);
	 * Assert.assertEquals(StatusCode.CLIENT_ERROR,
	 * result.getErrorStatus().getStatusCode()); Assert.assertEquals(randomMessage,
	 * result.getErrorStatus().getStatusReason());
	 * 
	 * verify(getMocks()); }
	 * 
	 * @Test public void testSearchActivationCode() throws Exception {
	 * LicenceTemplate licenceTemplate =
	 * SampleDataFactory.createStandardLicenceTemplate(); RegistrationActivation
	 * registrationActivation =
	 * SampleDataFactory.createInstantRegistrationActivation(); Division division =
	 * SampleDataFactory.createDivision("MALAYSIA"); ProductPageDefinition
	 * pageDefinition = SampleDataFactory.createProductPageDefinition();
	 * RegisterableProduct product =
	 * SampleDataFactory.createRegisterableProduct(1234,"Malaysia",
	 * RegisterableType.SELF_REGISTERABLE); ActivationCodeRegistrationDefinition
	 * acrd = SampleDataFactory.createActivationCodeRegistrationDefinition(product,
	 * licenceTemplate, registrationActivation, pageDefinition); ActivationCodeBatch
	 * acb =
	 * SampleDataFactory.createActivationCodeBatch(ActivationCodeFormat.EAC_NUMERIC,
	 * licenceTemplate, acrd, null, null, null); ActivationCode ac =
	 * SampleDataFactory.createActivationCode(acb, new EacActivationCode());
	 * List<ActivationCode> codes = new ArrayList<ActivationCode>();
	 * List<ActivationCodeSearchDto> acbCodes = new
	 * ArrayList<ActivationCodeSearchDto>() ; ActivationCodeSearchDto acbCode = new
	 * ActivationCodeSearchDto() ; codes.add(ac); ActivationCodeRegistration
	 * registration = SampleDataFactory.createActivationCodeRegistration(customer,
	 * acrd, ac); List<ActivationCodeRegistration> registrations = new
	 * ArrayList<ActivationCodeRegistration>(); registrations.add(registration);
	 * ExternalSystem externalSystem = SampleDataFactory.createExternalSystem("EAC",
	 * "EAC"); ExternalSystemIdType eternalSystemType =
	 * SampleDataFactory.createExternalSystemType(externalSystem, "NAME", "DESC");
	 * ExternalCustomerId externalCustomerId =
	 * SampleDataFactory.createExternalCustomerId(customer, "externalId",
	 * eternalSystemType); ExternalCustomerIdDto externalCustomerIdDto = new
	 * ExternalCustomerIdDto(customer, Arrays.asList(new ExternalCustomerId[]
	 * {externalCustomerId})); List<ExternalCustomerIdDto> externalCustomerIdDtos =
	 * new ArrayList<ExternalCustomerIdDto>();
	 * externalCustomerIdDtos.add(externalCustomerIdDto);
	 * acbCode.setActivationCodeBatch(acb); acbCode.setProduct(product);
	 * acbCode.setActualUsage(ac.getActualUsage());
	 * acbCode.setAllowedUsage(ac.getAllowedUsage()); acbCode.setCode(ac.getCode());
	 * acbCodes.add(acbCode);
	 * mExternalSystemLookup.validateExternalSystem(SYSTEM_ID); expectLastCall();
	 * expect(this.activationCodeService.searchActivationCodeByCode(SYSTEM_ID,ac.
	 * getCode(), false)).andReturn(acbCodes);
	 * expect(this.externalCustomerIdDtoSource.getExternalCustomersId(SYSTEM_ID,
	 * registrations)).andReturn(externalCustomerIdDtos);
	 * expect(this.activationCodeService.getRedeemActivationCodeInfo(acbCode,
	 * SYSTEM_ID)).andReturn(registrations); replay(getMocks());
	 * 
	 * SearchActivationCodeResponse result = sut.searchActivationCode(SYSTEM_ID,
	 * ac.getCode(), false);
	 * 
	 * verify(getMocks());
	 * 
	 * Assert.assertNull(result.getErrorStatus()); Assert.assertEquals(1,
	 * result.getActivationCodeInfoCount());
	 * 
	 * ActivationCodeInfo activationCodeInfo = result.getActivationCodeInfo(0);
	 * 
	 * Assert.assertEquals(ac.getCode(), activationCodeInfo.getActivationCode());
	 * Assert.assertEquals(ac.getActualUsage().intValue(),
	 * activationCodeInfo.getActualUsages());
	 * Assert.assertEquals(ac.getAllowedUsage().intValue(),
	 * activationCodeInfo.getAllowedUsages());
	 * 
	 * Assert.assertEquals(1, activationCodeInfo.getActivationCodeLicenceCount());
	 * 
	 * RedeemedInfo redeemedInfo = activationCodeInfo.getActivationCodeLicence(0);
	 * 
	 * Identifiers identifiers = redeemedInfo.getUserId(); ExternalIdentifier
	 * externalIdentifier = identifiers.getExternal(0);
	 * 
	 * Assert.assertEquals(externalCustomerId.getExternalId(),
	 * externalIdentifier.getId()); }
	 * 
	 * // Guest Redeem Activation code test cases
	 * 
	 * @Test public void testGuestRedeeemActicationCodesSuccess() throws
	 * WebServiceException, ServiceLayerException, ProductNotFoundException,
	 * UserNotFoundException, LicenseNotFoundException, AccessDeniedException,
	 * GroupNotFoundException, ErightsException { List<GuestRedeemActivationCodeDto>
	 * guestRedeemActivationCodeDtos = new
	 * ArrayList<GuestRedeemActivationCodeDto>(); GuestRedeemActivationCodeDto
	 * guestRedeemActivationCodeDto = new GuestRedeemActivationCodeDto() ;
	 * guestRedeemActivationCodeDto.setActivationCode(ACTIVATION_CODE_WITHOUT_DASHES
	 * ); guestRedeemActivationCodeDto.setProductId(registerableProduct1.getId());
	 * guestRedeemActivationCodeDto.setProductName(registerableProduct1.
	 * getProductName()); guestRedeemActivationCodeDto.setExternalProductId(new
	 * ArrayList<ExternalProductId>());
	 * guestRedeemActivationCodeDtos.add(guestRedeemActivationCodeDto);
	 * expect(activationCodeService.guestRedeemActivationCode(
	 * ACTIVATION_CODE_WITHOUT_DASHES)).andReturn(guestRedeemActivationCodeDtos);
	 * replay(getMocks());
	 * 
	 * GuestRedeemActivationCodeResponse result =
	 * sut.guestRedeemActivationCode(ACTIVATION_CODE_WITHOUT_DASHES);
	 * 
	 * Assert.assertNotNull(result); Assert.assertEquals(1,
	 * result.getEntitlementCount()); GuestProductEntitlement[] data =
	 * result.getEntitlement(); List<GuestProductEntitlement> resultEnts =
	 * Arrays.asList(data); Assert.assertNull(result.getErrorStatus());
	 * Assert.assertEquals(registerableProduct1.getId(),
	 * data[0].getProduct()[0].getProductIds().getId());
	 * Assert.assertEquals(registerableProduct1.getProductName(),
	 * data[0].getProduct()[0].getProductName()); verify(getMocks()); }
	 * 
	 * @Test public void testGuestRedeeemActicationCodesWithDashes() throws
	 * WebServiceException, ServiceLayerException, ProductNotFoundException,
	 * UserNotFoundException, LicenseNotFoundException, AccessDeniedException,
	 * GroupNotFoundException, ErightsException { List<GuestRedeemActivationCodeDto>
	 * guestRedeemActivationCodeDtos = new
	 * ArrayList<GuestRedeemActivationCodeDto>(); GuestRedeemActivationCodeDto
	 * guestRedeemActivationCodeDto = new GuestRedeemActivationCodeDto() ;
	 * guestRedeemActivationCodeDto.setActivationCode(ACTIVATION_CODE_WITH_DASHES);
	 * guestRedeemActivationCodeDto.setProductId(registerableProduct1.getId());
	 * guestRedeemActivationCodeDto.setProductName(registerableProduct1.
	 * getProductName()); guestRedeemActivationCodeDto.setExternalProductId(new
	 * ArrayList<ExternalProductId>());
	 * guestRedeemActivationCodeDtos.add(guestRedeemActivationCodeDto);
	 * expect(activationCodeService.guestRedeemActivationCode(
	 * ACTIVATION_CODE_WITH_DASHES)).andReturn(guestRedeemActivationCodeDtos);
	 * replay(getMocks());
	 * 
	 * GuestRedeemActivationCodeResponse result =
	 * sut.guestRedeemActivationCode(ACTIVATION_CODE_WITH_DASHES);
	 * 
	 * Assert.assertNotNull(result); Assert.assertEquals(1,
	 * result.getEntitlementCount()); GuestProductEntitlement[] data =
	 * result.getEntitlement(); List<GuestProductEntitlement> resultEnts =
	 * Arrays.asList(data); Assert.assertNull(result.getErrorStatus());
	 * Assert.assertEquals(registerableProduct1.getId(),
	 * data[0].getProduct()[0].getProductIds().getId());
	 * Assert.assertEquals(registerableProduct1.getProductName(),
	 * data[0].getProduct()[0].getProductName()); verify(getMocks()); }
	 * 
	 * @Test public void testGuestRedeeemActicationCodesWithNoCodeSupplied() throws
	 * WebServiceException, ServiceLayerException, ProductNotFoundException,
	 * UserNotFoundException, LicenseNotFoundException, AccessDeniedException,
	 * GroupNotFoundException, ErightsException { replay(getMocks());
	 * 
	 * GuestRedeemActivationCodeResponse result = sut.guestRedeemActivationCode("");
	 * 
	 * Assert.assertNotNull(result); Assert.assertEquals(StatusCode.CLIENT_ERROR,
	 * result.getErrorStatus().getStatusCode());
	 * Assert.assertEquals("No Activation Code supplied",
	 * result.getErrorStatus().getStatusReason()); verify(getMocks()); }
	 * 
	 * @Test public void testGuestRedeeemActicationCodesWithServiceLayerException()
	 * throws WebServiceException, ServiceLayerException, ProductNotFoundException,
	 * UserNotFoundException, LicenseNotFoundException, AccessDeniedException,
	 * GroupNotFoundException, ErightsException {
	 * 
	 * expect(activationCodeService.guestRedeemActivationCode(ACTIVATION_CODE_1)).
	 * andThrow(new ErightsException("Service layer Exception.",null,0000));
	 * replay(getMocks());
	 * 
	 * GuestRedeemActivationCodeResponse result =
	 * sut.guestRedeemActivationCode(ACTIVATION_CODE_1);
	 * 
	 * Assert.assertNotNull(result); Assert.assertEquals(StatusCode.SERVER_ERROR,
	 * result.getErrorStatus().getStatusCode());
	 * Assert.assertEquals("Service layer Exception.",
	 * result.getErrorStatus().getStatusReason()); verify(getMocks()); }
	 * 
	 * @Test public void testGuestRedeeemActicationCodesWithInvalidCode() throws
	 * WebServiceException, ServiceLayerException, ProductNotFoundException,
	 * UserNotFoundException, LicenseNotFoundException, AccessDeniedException,
	 * GroupNotFoundException, ErightsException {
	 * expect(activationCodeService.guestRedeemActivationCode(ACTIVATION_CODE_1)).
	 * andThrow(new ErightsException("The activation code  ["
	 * +ACTIVATION_CODE_1+"] does not exist.",null,2004)) ; replay(getMocks());
	 * 
	 * 
	 * GuestRedeemActivationCodeResponse result =
	 * sut.guestRedeemActivationCode(ACTIVATION_CODE_1);
	 * 
	 * Assert.assertNotNull(result); Assert.assertEquals(StatusCode.CLIENT_ERROR,
	 * result.getErrorStatus().getStatusCode());
	 * Assert.assertEquals("The activation code  ["
	 * +ACTIVATION_CODE_1+"] does not exist.",
	 * result.getErrorStatus().getStatusReason()); verify(getMocks()); }
	 */}
