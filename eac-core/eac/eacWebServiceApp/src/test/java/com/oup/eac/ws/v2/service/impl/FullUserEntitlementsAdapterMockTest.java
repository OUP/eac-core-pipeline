package com.oup.eac.ws.v2.service.impl;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import javax.naming.NamingException;

import junit.framework.Assert;

import org.easymock.EasyMock;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.oup.eac.common.mock.AbstractMockTest;
import com.oup.eac.domain.Customer;
import com.oup.eac.domain.Division;
import com.oup.eac.domain.EacGroups;
import com.oup.eac.domain.ExternalCustomerId;
import com.oup.eac.domain.ExternalProductId;
import com.oup.eac.domain.ExternalSystem;
import com.oup.eac.domain.ExternalSystemIdType;
import com.oup.eac.domain.Product;
import com.oup.eac.domain.ProductRegistrationDefinition;
import com.oup.eac.domain.RegisterableProduct;
import com.oup.eac.domain.Registration;
import com.oup.eac.domain.entitlement.ProductDetailsDto;
import com.oup.eac.domain.entitlement.ProductEntitlementDto;
import com.oup.eac.domain.entitlement.ProductEntitlementGroupDto;
import com.oup.eac.dto.CustomerRegistrationsDto;
import com.oup.eac.dto.EnforceableProductDto;
import com.oup.eac.dto.EnforceableProductUrlDto;
import com.oup.eac.dto.ExternalCustomerIdDto;
import com.oup.eac.dto.LicenceDto;
import com.oup.eac.integration.facade.exceptions.ErightsException;
import com.oup.eac.service.ExternalIdService;
import com.oup.eac.service.ProductService;
import com.oup.eac.service.RegistrationService;
import com.oup.eac.service.ServiceLayerException;
import com.oup.eac.service.UserEntitlementsService;
import com.oup.eac.service.entitlements.ExternalProductIdDtoSource;
import com.oup.eac.service.entitlements.ProductEntitlementGroupSource;
import com.oup.eac.service.entitlements.ProductEntitlementInfosSource;
import com.oup.eac.service.impl.UserEntitlementsServiceImpl;
import com.oup.eac.ws.v2.binding.access.GetFullUserEntitlementsResponse;
import com.oup.eac.ws.v2.binding.access.GetFullUserEntitlementsResponseSequence;
import com.oup.eac.ws.v2.binding.common.ErrorStatus;
import com.oup.eac.ws.v2.binding.common.FullProductEntitlement;
import com.oup.eac.ws.v2.binding.common.FullProductEntitlementGroup;
import com.oup.eac.ws.v2.binding.common.User;
import com.oup.eac.ws.v2.binding.common.WsUserId;
import com.oup.eac.ws.v2.binding.common.types.StatusCode;
import com.oup.eac.ws.v2.ex.WebServiceException;
import com.oup.eac.ws.v2.ex.WebServiceValidationException;
import com.oup.eac.ws.v2.service.FullUserEntitlementsAdapter;
import com.oup.eac.ws.v2.service.WsCustomerLookup;
import com.oup.eac.ws.v2.service.WsExternalSystemLookup;
import com.oup.eac.ws.v2.service.WsProductLookup;
import com.oup.eac.ws.v2.service.entitlements.CustomerConverter;
import com.oup.eac.ws.v2.service.entitlements.EntitlementsAdapterHelper;

public class FullUserEntitlementsAdapterMockTest extends AbstractMockTest{

    public FullUserEntitlementsAdapterMockTest() throws NamingException {
        super();
        // TODO Auto-generated constructor stub
    }
    
    private static final Set<String>  SYSTEM_IDS = new HashSet<String>(Arrays.asList("SYSTEM_ID1", "SYSTEM_ID2", "SYSTEM_ID3"));
    
    private static final Set<String>  ORG_UNIT = new HashSet<String>(Arrays.asList("ORG_1", "ORG_2", "ORG_3"));
    
    private static final Set<String>  PRODUCT_SYSTEM_IDS = new HashSet<String>(Arrays.asList("PRODUCT_SYSTEM_ID1", "PRODUCT_SYSTEM_ID2", "PRODUCT_SYSTEM_ID3"));
    
    private static final Set<String>  PRODUCT_ORG_UNITS =new HashSet<String>(Arrays.asList("PRODUCT_ORG_UNIT1", "PRODUCT_ORG_UNIT2", "PRODUCT_ORG_UNIT3"));
    
    private static final String LICENCE_STATE = "ACTIVE";
    
    
    //empty sets
    
    private static final Set<String>  SYSTEM_IDS_EMPTY_SET = new HashSet<String>(Collections.<String>emptyList());
    
    private static final Set<String>  PRODUCT_SYSTEM_IDS_EMPTY_SET = new HashSet<String>(Collections.<String>emptyList());
    
    private static final Set<String>  PRODUCT_ORG_UNITS_EMPTY_SET =new HashSet<String>(Collections.<String>emptyList());
    
    private static final String LICENCE_STATE_NULL = null;
    

    // system under test
    private FullUserEntitlementsAdapter sut;

    private RegistrationService mRegistrationService;
    private CustomerConverter mCustomerConverter;
    private WsCustomerLookup mCustomerLookup;
    private WsExternalSystemLookup mExternalSystemLookup;

    private Customer customer;
    /*private FullProductEntitlement ent1;
    private FullProductEntitlement ent2;
    private FullProductEntitlement ent3;*/
    private User user;
    private WsUserId wsUserId;

    private ExternalIdService mExternalIdService;

    private ExternalCustomerIdDto extCustDto;
    
    private UserEntitlementsService mUserEntitlementsService;

    private EntitlementsAdapterHelper mEntitlementsHelperAdapter;
    
    private WsProductLookup mProductLookup;
    private ExternalProductIdDtoSource externalProductIdDtoSource;
    private ProductEntitlementInfosSource productEntitlementInfosSource;
    private ProductEntitlementGroupSource entitlementGroupSource;
    private ProductService productService;
    
   // private CustomerRegistrationsDto custReg;
    List<LicenceDto> licences = new ArrayList<LicenceDto>();
    private LicenceDto licDto;
    String divType = "division" + System.currentTimeMillis();
    @Before
    public void setup() {
        this.customer = new Customer();
        this.user = new User();        
        this.externalProductIdDtoSource = EasyMock.createMock(ExternalProductIdDtoSource.class); 
        
        this.productEntitlementInfosSource = EasyMock.createMock(ProductEntitlementInfosSource.class); 
        this.entitlementGroupSource = EasyMock.createMock(ProductEntitlementGroupSource.class); 
        this.productService = EasyMock.createMock(ProductService.class); 
        this.mRegistrationService = EasyMock.createMock(RegistrationService.class);
        this.mCustomerConverter = EasyMock.createMock(CustomerConverter.class);
        this.mCustomerLookup = EasyMock.createMock(WsCustomerLookup.class);
        this.mExternalSystemLookup = EasyMock.createMock(WsExternalSystemLookup.class);
        this.mExternalIdService = EasyMock.createMock(ExternalIdService.class);
        this.mUserEntitlementsService = new UserEntitlementsServiceImpl(externalProductIdDtoSource, productEntitlementInfosSource, entitlementGroupSource, productService);
        this.mEntitlementsHelperAdapter = EasyMock.createMock(EntitlementsAdapterHelper.class);
        this.mProductLookup = EasyMock.createMock(WsProductLookup.class);
        this.licDto = EasyMock.createMock(LicenceDto.class);
        
        
       // this.eacGroup=EasyMock.createMock(EacGroups.class);
        licences.add(licDto);
        
       // this.custReg = new CustomerRegistrationsDto(user1, registrations1, licences1);
        this.setMocks(mCustomerLookup, mRegistrationService, 
                mCustomerConverter, mExternalIdService,
                mExternalSystemLookup, 
                mEntitlementsHelperAdapter, mProductLookup, licDto,externalProductIdDtoSource,
                productEntitlementInfosSource,entitlementGroupSource,productService);
       
            this.sut = new FullUserEntitlementsAdapterImpl(
                mCustomerLookup, mRegistrationService, 
                mCustomerConverter, mExternalIdService,
                mExternalSystemLookup, mUserEntitlementsService,
                mEntitlementsHelperAdapter, mProductLookup);
            
        this.wsUserId = new WsUserId(); 
        //this.wsUserId.setUserName(customer.getUsername());
        
        List<ExternalCustomerId> ids = new ArrayList<ExternalCustomerId>();       
        
        this.extCustDto = new ExternalCustomerIdDto(customer, ids);
        
    }
    
    
    /**
     * This test checks that we get a ServiceLayerException if the registrationService throws a ServiceLayerException.
     * @throws ServiceLayerException 
     * 
     * @throws ServiceLayerException
     */
    
    @Ignore
    @Test
    public void testServiceLaterExceptionThrown() throws WebServiceException, ServiceLayerException, ErightsException {

        expect(mCustomerLookup.getCustomerByWsUserId(this.wsUserId)).andReturn(customer);

       // expect(mRegistrationService.getEntitlementsForCustomerRegistrationsFiltered(customer, PRODUCT_SYSTEM_IDS, null, LICENCE_STATE)).andThrow(new ServiceLayerException("oops1"));
        //expect(mDivisionService.getAllDivisions()).andReturn(divisions);
        mExternalSystemLookup.validateMultipleExternalSystem(SYSTEM_IDS);
        mProductLookup.validateProductOrgUnit(ORG_UNIT);
        
        //customer = custRegistrationsDto.getUser();
        //mRegistrationService.getEntitlementsForCustomerRegistrationsFiltered(customer,  PRODUCT_SYSTEM_IDS,  ORG_UNIT, LICENCE_STATE).andReturn(custRegistrationsDto);
        mExternalSystemLookup.validateMultipleExternalSystem(PRODUCT_SYSTEM_IDS);
        mProductLookup.validateLicenceState(LICENCE_STATE);
       // expectLastCall();

        // no expectations on helper if there's a service layer exception

       // replay(getMocks());

        try {
            @SuppressWarnings("unused")
            GetFullUserEntitlementsResponse result = sut.getFullUserEntitlementGroups(this.wsUserId, SYSTEM_IDS, PRODUCT_SYSTEM_IDS,ORG_UNIT, LICENCE_STATE);
            Assert.fail("exception excepted");
        } catch (WebServiceException ex) {
            Assert.assertEquals("oops1",ex.getMessage());
        }

    }

    /**
     * This test checks that we get a ServiceLayerException if getCustomerFromSession throws a ServiceLayerException.
     * 
     * @throws ServiceLayerException
     *             the service layer exception
     * @throws WebServiceException 
     */
    
    @Test
    public void testBadSession() throws ServiceLayerException, WebServiceException, ErightsException {

        expect(this.mCustomerLookup.getCustomerByWsUserId(this.wsUserId)).andThrow(new WebServiceValidationException("oops"));

        replay(getMocks());

        GetFullUserEntitlementsResponse result = sut.getFullUserEntitlementGroups(this.wsUserId, SYSTEM_IDS, PRODUCT_SYSTEM_IDS,ORG_UNIT, LICENCE_STATE);
        Assert.assertEquals(StatusCode.CLIENT_ERROR, result.getErrorStatus().getStatusCode());
        Assert.assertEquals("oops",result.getErrorStatus().getStatusReason());
            
    }
    
    @Ignore
    @Test
    public void testGroupedProductEntitlementsSingleGroup() throws WebServiceException, ServiceLayerException, ErightsException{
        List<Registration<? extends ProductRegistrationDefinition>> registrations = new ArrayList<Registration<? extends ProductRegistrationDefinition>>();
        List<LicenceDto> licences = new ArrayList<LicenceDto>();

        expect(mCustomerLookup.getCustomerByWsUserId(this.wsUserId)).andReturn(customer);
        mExternalSystemLookup.validateMultipleExternalSystem(SYSTEM_IDS_EMPTY_SET);
        mProductLookup.validateProductOrgUnit(ORG_UNIT);
        mExternalSystemLookup.validateMultipleExternalSystem(PRODUCT_SYSTEM_IDS_EMPTY_SET);
        mProductLookup.validateLicenceState(LICENCE_STATE_NULL);
        expectLastCall();        

        CustomerRegistrationsDto custRegDto = new CustomerRegistrationsDto(customer, registrations, licences);
        expect(mRegistrationService.getEntitlementsForCustomerRegistrationsFiltered(customer, PRODUCT_SYSTEM_IDS_EMPTY_SET, PRODUCT_ORG_UNITS_EMPTY_SET, LICENCE_STATE_NULL)).andReturn(custRegDto);
        //expect(mRegistrationService.getEntitlementsForCustomerRegistrationsFiltered(customer, PRODUCT_SYSTEM_IDS_EMPTY_SET, LICENCE_STATE_NULL)).andReturn(custRegDto);
        expect(mExternalIdService.getFullExternalCustomerIdDto(customer)).andReturn(extCustDto);
        expect(mCustomerConverter.convertCustomerToUser(this.extCustDto)).andReturn(user);
        expectLastCall();
        ProductEntitlementGroupDto group1 = new ProductEntitlementGroupDto();
        ProductEntitlementGroupDto group2 = new ProductEntitlementGroupDto();
        ProductEntitlementGroupDto group3 = new ProductEntitlementGroupDto();
        ProductEntitlementDto g1Ent = new ProductEntitlementDto();
        g1Ent.setProductList(new ArrayList<ProductDetailsDto>());
        ProductEntitlementDto g1LinkedEnt1 = new ProductEntitlementDto();
        ProductEntitlementDto g1LinkedEnt2 = new ProductEntitlementDto();
        group1.setEntitlement(g1Ent);
        group1.setLinkedEntitlements(Arrays.asList(g1LinkedEnt1,g1LinkedEnt2));

        ProductEntitlementDto g2Ent = new ProductEntitlementDto();
        g2Ent.setProductList(new ArrayList<ProductDetailsDto>());
        ProductEntitlementDto g2LinkedEnt1 = new ProductEntitlementDto();
        ProductEntitlementDto g2LinkedEnt2 = new ProductEntitlementDto();
        group2.setEntitlement(g2Ent);
        group2.setLinkedEntitlements(Arrays.asList(g2LinkedEnt1,g2LinkedEnt2));

        ProductEntitlementDto g3Ent = new ProductEntitlementDto();
        g3Ent.setProductList(new ArrayList<ProductDetailsDto>());
        ProductEntitlementDto g3LinkedEnt1 = new ProductEntitlementDto();
        ProductEntitlementDto g3LinkedEnt2 = new ProductEntitlementDto();
        group3.setEntitlement(g3Ent);
        group3.setLinkedEntitlements(Arrays.asList(g3LinkedEnt1,g3LinkedEnt2));

        /*List<ProductEntitlementGroupDto> groupList = Arrays.asList(group1, group2, group3);
        expect(mUserEntitlementsService.getFullUserEntitlementGroups(custRegDto)).andReturn(groupList);*/        

        FullProductEntitlement pe1  = new FullProductEntitlement();
        FullProductEntitlement pe1Linked1 = new FullProductEntitlement();
        FullProductEntitlement pe1Linked2 = new FullProductEntitlement();

        FullProductEntitlement pe2  = new FullProductEntitlement();
        FullProductEntitlement pe2Linked1 = new FullProductEntitlement();
        FullProductEntitlement pe2Linked2 = new FullProductEntitlement();

        FullProductEntitlement pe3  = new FullProductEntitlement();
        FullProductEntitlement pe3Linked1 = new FullProductEntitlement();
        FullProductEntitlement pe3Linked2 = new FullProductEntitlement();

        expect(mEntitlementsHelperAdapter.getFullProductEntitlement(g1Ent)).andReturn(pe1);
        expect(mEntitlementsHelperAdapter.getFullProductEntitlement(g1LinkedEnt1)).andReturn(pe1Linked1);
        expect(mEntitlementsHelperAdapter.getFullProductEntitlement(g1LinkedEnt2)).andReturn(pe1Linked2);

        expect(mEntitlementsHelperAdapter.getFullProductEntitlement(g2Ent)).andReturn(pe2);
        expect(mEntitlementsHelperAdapter.getFullProductEntitlement(g2LinkedEnt1)).andReturn(pe2Linked1);
        expect(mEntitlementsHelperAdapter.getFullProductEntitlement(g2LinkedEnt2)).andReturn(pe2Linked2);

        expect(mEntitlementsHelperAdapter.getFullProductEntitlement(g3Ent)).andReturn(pe3);
        expect(mEntitlementsHelperAdapter.getFullProductEntitlement(g3LinkedEnt1)).andReturn(pe3Linked1);
        expect(mEntitlementsHelperAdapter.getFullProductEntitlement(g3LinkedEnt2)).andReturn(pe3Linked2);

        replay(getMocks());

        // CALL THE METHOD WE ARE TESTING
        GetFullUserEntitlementsResponse result = sut.getFullUserEntitlementGroups(this.wsUserId, SYSTEM_IDS_EMPTY_SET, PRODUCT_SYSTEM_IDS_EMPTY_SET,ORG_UNIT, LICENCE_STATE_NULL);
        verify(getMocks());

        GetFullUserEntitlementsResponseSequence groupSeq = result.getGetFullUserEntitlementsResponseSequence();
        FullProductEntitlementGroup[] wsGroups = groupSeq.getEntitlementGroup();
        FullProductEntitlementGroup wsGroup1 = wsGroups[0];
        FullProductEntitlementGroup wsGroup2 = wsGroups[1];
        FullProductEntitlementGroup wsGroup3 = wsGroups[2];
        checkGroups(wsGroup1, pe1, pe1Linked1, pe1Linked2);
        checkGroups(wsGroup2, pe2, pe2Linked1, pe2Linked2);
        checkGroups(wsGroup3, pe3, pe3Linked1, pe3Linked2);
    }

   private void checkGroups(FullProductEntitlementGroup wsGroup,
            FullProductEntitlement top, FullProductEntitlement... children) {
        Assert.assertEquals(top, wsGroup.getEntitlement());
        for(int i=0;i<children.length;i++){
            Assert.assertEquals(children[i],wsGroup.getLinkedEntitlement()[i]);
        }
    }
    
    @Test
    public void testInvalidSystemId() throws WebServiceException, ServiceLayerException, ErightsException{
        expect(mCustomerLookup.getCustomerByWsUserId(this.wsUserId)).andReturn(customer);

        String randomMessage = UUID.randomUUID().toString();
        mExternalSystemLookup.validateMultipleExternalSystem(SYSTEM_IDS);
        expectLastCall().andThrow(new WebServiceValidationException(randomMessage));
        replay(getMocks());

        // CALL THE METHOD WE ARE TESTING
        GetFullUserEntitlementsResponse result = sut.getFullUserEntitlementGroups(this.wsUserId, SYSTEM_IDS, PRODUCT_SYSTEM_IDS,ORG_UNIT, LICENCE_STATE);
        verify(getMocks());

        ErrorStatus errorStatus = result.getErrorStatus();
        Assert.assertEquals(StatusCode.CLIENT_ERROR,errorStatus.getStatusCode());
        Assert.assertEquals(randomMessage,errorStatus.getStatusReason());
        Assert.assertNull(result.getGetFullUserEntitlementsResponseSequence());

    }    

    @Test
    public void testInvalidProductSystemId() throws WebServiceException, ServiceLayerException, ErightsException{
        expect(mCustomerLookup.getCustomerByWsUserId(this.wsUserId)).andReturn(customer);

        String randomMessage = UUID.randomUUID().toString();
        mExternalSystemLookup.validateMultipleExternalSystem(SYSTEM_IDS);
        mExternalSystemLookup.validateMultipleExternalSystem(PRODUCT_SYSTEM_IDS);
        expectLastCall().andThrow(new WebServiceValidationException(randomMessage));
        replay(getMocks());

        // CALL THE METHOD WE ARE TESTING
        GetFullUserEntitlementsResponse result = sut.getFullUserEntitlementGroups(this.wsUserId, SYSTEM_IDS, PRODUCT_SYSTEM_IDS,ORG_UNIT, LICENCE_STATE);
        verify(getMocks());

        ErrorStatus errorStatus = result.getErrorStatus();
        Assert.assertEquals(StatusCode.CLIENT_ERROR,errorStatus.getStatusCode());
        Assert.assertEquals(randomMessage,errorStatus.getStatusReason());
        Assert.assertNull(result.getGetFullUserEntitlementsResponseSequence());
    }    

    @Test
    public void testInvalidProductOrgUnit() throws WebServiceException, ServiceLayerException, ErightsException{
        expect(mCustomerLookup.getCustomerByWsUserId(this.wsUserId)).andReturn(customer);

        String randomMessage = UUID.randomUUID().toString();
        mExternalSystemLookup.validateMultipleExternalSystem(SYSTEM_IDS);
        mExternalSystemLookup.validateMultipleExternalSystem(PRODUCT_SYSTEM_IDS);
        //code TO CHECK 
        expectLastCall().andThrow(new WebServiceValidationException(randomMessage));
        replay(getMocks());

        // CALL THE METHOD WE ARE TESTING
        GetFullUserEntitlementsResponse result = sut.getFullUserEntitlementGroups(this.wsUserId, SYSTEM_IDS, PRODUCT_SYSTEM_IDS,ORG_UNIT, LICENCE_STATE);
        verify(getMocks());

        ErrorStatus errorStatus = result.getErrorStatus();
        Assert.assertEquals(StatusCode.CLIENT_ERROR,errorStatus.getStatusCode());
        Assert.assertEquals(randomMessage,errorStatus.getStatusReason());
        Assert.assertNull(result.getGetFullUserEntitlementsResponseSequence());

    }
    
    @Test
    public void testInvalidLicenceState() throws WebServiceException, ServiceLayerException, ErightsException{
        expect(mCustomerLookup.getCustomerByWsUserId(this.wsUserId)).andReturn(customer);

        String randomMessage = UUID.randomUUID().toString();
        mExternalSystemLookup.validateMultipleExternalSystem(SYSTEM_IDS);
        mExternalSystemLookup.validateMultipleExternalSystem(PRODUCT_SYSTEM_IDS);
        mProductLookup.validateProductOrgUnit(ORG_UNIT);
        mProductLookup.validateLicenceState(LICENCE_STATE);
        expectLastCall().andThrow(new WebServiceValidationException(randomMessage));
        replay(getMocks());

        // CALL THE METHOD WE ARE TESTING
        GetFullUserEntitlementsResponse result = sut.getFullUserEntitlementGroups(this.wsUserId, SYSTEM_IDS, PRODUCT_SYSTEM_IDS,ORG_UNIT, LICENCE_STATE);
        verify(getMocks());

        ErrorStatus errorStatus = result.getErrorStatus();
        Assert.assertEquals(StatusCode.CLIENT_ERROR,errorStatus.getStatusCode());
        Assert.assertEquals(randomMessage,errorStatus.getStatusReason());
        Assert.assertNull(result.getGetFullUserEntitlementsResponseSequence());

    }
    
    @Ignore
    @Test
    public void testSuccessWithOutSearchFilter() throws WebServiceException, ServiceLayerException, ErightsException{
       
        EasyMock.expect(mCustomerLookup.getCustomerByWsUserId(wsUserId)).andReturn(customer);        
        this.mExternalSystemLookup.validateMultipleExternalSystem(SYSTEM_IDS);
        this.mExternalSystemLookup.validateMultipleExternalSystem(PRODUCT_SYSTEM_IDS_EMPTY_SET);
        mProductLookup.validateProductOrgUnit(ORG_UNIT);
        this.mProductLookup.validateLicenceState(LICENCE_STATE_NULL);
        EasyMock.expectLastCall();
        
        List<Registration<? extends ProductRegistrationDefinition>> registrations = new ArrayList<Registration<? extends ProductRegistrationDefinition>>();
        
        CustomerRegistrationsDto custRegDto = new CustomerRegistrationsDto(customer, registrations, licences);
        EasyMock.expect(mRegistrationService.getEntitlementsForCustomerRegistrationsFiltered(customer, PRODUCT_SYSTEM_IDS_EMPTY_SET, PRODUCT_ORG_UNITS_EMPTY_SET, LICENCE_STATE_NULL)).andReturn(custRegDto);
        //expect(mRegistrationService.getEntitlementsForCustomerRegistrationsFiltered(customer, PRODUCT_SYSTEM_IDS_EMPTY_SET, LICENCE_STATE_NULL)).andReturn(custRegDto);
        EasyMock.expect(mExternalIdService.getFullExternalCustomerIdDto(customer)).andReturn(extCustDto);        
        EasyMock.expect(mCustomerConverter.convertCustomerToUser(extCustDto)).andReturn(user);
        
        ProductEntitlementGroupDto group1 = new ProductEntitlementGroupDto();
        ProductEntitlementDto g1Ent = new ProductEntitlementDto();
        ProductEntitlementDto g1LinkedEnt1 = new ProductEntitlementDto();
        group1.setEntitlement(g1Ent);
        group1.setLinkedEntitlements(Arrays.asList(g1LinkedEnt1));
        
        /*List<ProductEntitlementGroupDto> groupList = Arrays.asList(group1);
        expect(mUserEntitlementsService.getFullUserEntitlementGroups(custRegDto)).andReturn(groupList);*/
        
        FullProductEntitlement pe1  = new FullProductEntitlement();
        FullProductEntitlement pe1Linked1 = new FullProductEntitlement();
        expect(mEntitlementsHelperAdapter.getFullProductEntitlement(g1Ent)).andReturn(pe1);
        expect(mEntitlementsHelperAdapter.getFullProductEntitlement(g1LinkedEnt1)).andReturn(pe1Linked1);
        EnforceableProductDto prodDto = new EnforceableProductDto();
        expect(licDto.getProducts()).andStubReturn(prodDto);
        
        expect(licDto.getActivationCode()).andStubReturn(null);
        
        EasyMock.replay(getMocks());   
        
        this.licDto.setProducts(prodDto);
        expectLastCall();
        this.licDto.getProducts().setDivision(new Division());
        expectLastCall();
        this.licDto.getProducts().setDivisionId(12);
        expectLastCall();
        this.licDto.setProducts(prodDto);
        this.licDto.getProducts().setDivision(new Division());
        this.licDto.getProducts().setDivisionId(12);
        GetFullUserEntitlementsResponse result = sut.getFullUserEntitlementGroups(this.wsUserId, SYSTEM_IDS, PRODUCT_SYSTEM_IDS_EMPTY_SET,ORG_UNIT, LICENCE_STATE_NULL);
        
        Assert.assertNotNull(result);
        Assert.assertNull(result.getErrorStatus());        
        EasyMock.verify(getMocks());
        
        GetFullUserEntitlementsResponseSequence groupSeq = result.getGetFullUserEntitlementsResponseSequence();
        FullProductEntitlementGroup[] wsGroups = groupSeq.getEntitlementGroup();
        FullProductEntitlementGroup wsGroup1 = wsGroups[0];
        checkGroups(wsGroup1, pe1, pe1Linked1);
    }
    
    @Ignore
    @Test
    public void testSuccessWithSearchFilterOnProductOrgUnit() throws WebServiceException, ServiceLayerException, ErightsException{
        
        EasyMock.expect(mCustomerLookup.getCustomerByWsUserId(wsUserId)).andReturn(customer);        
        this.mExternalSystemLookup.validateMultipleExternalSystem(SYSTEM_IDS);
        this.mExternalSystemLookup.validateMultipleExternalSystem(PRODUCT_SYSTEM_IDS_EMPTY_SET);
        mProductLookup.validateProductOrgUnit(ORG_UNIT);
        this.mProductLookup.validateLicenceState(LICENCE_STATE_NULL);
        EasyMock.expectLastCall();
        
        List<Registration<? extends ProductRegistrationDefinition>> registrations = new ArrayList<Registration<? extends ProductRegistrationDefinition>>();
        List<LicenceDto> licences = new ArrayList<LicenceDto>();
        CustomerRegistrationsDto custRegDto = new CustomerRegistrationsDto(customer, registrations, licences);
        EasyMock.expect(mRegistrationService.getEntitlementsForCustomerRegistrationsFiltered(customer, PRODUCT_SYSTEM_IDS_EMPTY_SET, PRODUCT_ORG_UNITS_EMPTY_SET, LICENCE_STATE_NULL)).andReturn(custRegDto);
        //expect(mRegistrationService.getEntitlementsForCustomerRegistrationsFiltered(customer, PRODUCT_SYSTEM_IDS_EMPTY_SET, LICENCE_STATE_NULL)).andReturn(custRegDto);
        EasyMock.expect(mExternalIdService.getFullExternalCustomerIdDto(customer)).andReturn(extCustDto);        
        EasyMock.expect(mCustomerConverter.convertCustomerToUser(extCustDto)).andReturn(user);
        
        ProductEntitlementGroupDto group1 = new ProductEntitlementGroupDto();
        ProductEntitlementDto g1Ent = new ProductEntitlementDto();
        ProductEntitlementDto g1LinkedEnt1 = new ProductEntitlementDto();
        
        ProductDetailsDto pdto= new ProductDetailsDto();
        
        RegisterableProduct rPro =new RegisterableProduct();
        
        pdto.setProduct(rPro);        
        List<ProductDetailsDto> pDtoList = new ArrayList<ProductDetailsDto>();
        pDtoList.add(pdto);
        g1Ent.setProductList(pDtoList);
        
        group1.setEntitlement(g1Ent);
        group1.setLinkedEntitlements(Arrays.asList(g1LinkedEnt1));
        
        List<ProductEntitlementGroupDto> groupList = Arrays.asList(group1);
        expect(mUserEntitlementsService.getFullUserEntitlementGroups(custRegDto)).andReturn(groupList);
        
        FullProductEntitlement pe1  = new FullProductEntitlement();
        FullProductEntitlement pe1Linked1 = new FullProductEntitlement();
        expect(mEntitlementsHelperAdapter.getFullProductEntitlement(g1Ent)).andReturn(pe1);
        expect(mEntitlementsHelperAdapter.getFullProductEntitlement(g1LinkedEnt1)).andReturn(pe1Linked1);
        EasyMock.replay(getMocks());       
        
        GetFullUserEntitlementsResponse result = sut.getFullUserEntitlementGroups(this.wsUserId, SYSTEM_IDS, PRODUCT_SYSTEM_IDS_EMPTY_SET,ORG_UNIT, LICENCE_STATE_NULL);
        
        Assert.assertNotNull(result);
        Assert.assertNull(result.getErrorStatus());        
        EasyMock.verify(getMocks());
        
        GetFullUserEntitlementsResponseSequence groupSeq = result.getGetFullUserEntitlementsResponseSequence();
        FullProductEntitlementGroup[] wsGroups = groupSeq.getEntitlementGroup();
        FullProductEntitlementGroup wsGroup1 = wsGroups[0];
        checkGroups(wsGroup1, pe1, pe1Linked1);
    }    
    
    @Ignore
    @Test
    public void testSuccessWithSearchFilterOnProductSysId() throws WebServiceException, ServiceLayerException, ErightsException{
        
     //   EasyMock.expect(mCustomerLookup.getCustomerByWsUserId(wsUserId)).andReturn(customer);        
        this.mExternalSystemLookup.validateMultipleExternalSystem(SYSTEM_IDS);
        this.mExternalSystemLookup.validateMultipleExternalSystem(PRODUCT_SYSTEM_IDS);
        this.mProductLookup.validateProductOrgUnit(ORG_UNIT);
        this.mProductLookup.validateLicenceState(LICENCE_STATE_NULL);
       // EasyMock.expectLastCall();
        
        List<Registration<? extends ProductRegistrationDefinition>> registrations = new ArrayList<Registration<? extends ProductRegistrationDefinition>>();
        List<LicenceDto> licences = new ArrayList<LicenceDto>();
        CustomerRegistrationsDto custRegDto = new CustomerRegistrationsDto(customer, registrations, licences);
       // EasyMock.expect(mRegistrationService.getEntitlementsForCustomerRegistrationsFiltered(customer, PRODUCT_SYSTEM_IDS_EMPTY_SET, PRODUCT_ORG_UNITS_EMPTY_SET, LICENCE_STATE_NULL)).andReturn(custRegDto);
        //expect(mRegistrationService.getEntitlementsForCustomerRegistrationsFiltered(customer, PRODUCT_SYSTEM_IDS, LICENCE_STATE_NULL)).andReturn(custRegDto);
       // EasyMock.expect(mExternalIdService.getFullExternalCustomerIdDto(customer)).andReturn(extCustDto);        
        //EasyMock.expect(mCustomerConverter.convertCustomerToUser(extCustDto)).andReturn(user);
        
        ProductEntitlementGroupDto group1 = new ProductEntitlementGroupDto();
        ProductEntitlementDto g1Ent = new ProductEntitlementDto();
        ProductEntitlementDto g1LinkedEnt1 = new ProductEntitlementDto();
        
        ProductDetailsDto pdto= new ProductDetailsDto();
        
        List<ExternalProductId> extProIds = new ArrayList<ExternalProductId>(Collections.<ExternalProductId>emptyList());
        ExternalProductId ePi= new ExternalProductId();
        ExternalSystemIdType esit=new ExternalSystemIdType();
        ExternalSystem es=new ExternalSystem();
        es.setName("PRODUCT_SYSTEM_ID1");
        esit.setExternalSystem(es);
        ePi.setExternalSystemIdType(esit);
        extProIds.add(ePi);
        pdto.setExternalProductIds(extProIds);
        
        List<ProductDetailsDto> pDtoList = new ArrayList<ProductDetailsDto>();
        pDtoList.add(pdto);
        g1Ent.setProductList(pDtoList);        
        
        group1.setEntitlement(g1Ent);
        group1.setLinkedEntitlements(Arrays.asList(g1LinkedEnt1));
        
        List<ProductEntitlementGroupDto> groupList = Arrays.asList(group1);
        //expect(mUserEntitlementsService.getFullUserEntitlementGroups(custRegDto)).andReturn(groupList);
        
        FullProductEntitlement pe1  = new FullProductEntitlement();
        FullProductEntitlement pe1Linked1 = new FullProductEntitlement();
        //expect(mEntitlementsHelperAdapter.getFullProductEntitlement(g1Ent)).andReturn(pe1);
       
        //expect(mEntitlementsHelperAdapter.getFullProductEntitlement(g1LinkedEnt1)).andReturn(pe1Linked1);
        //EasyMock.replay(getMocks());       
        
        GetFullUserEntitlementsResponse result = sut.getFullUserEntitlementGroups(this.wsUserId, SYSTEM_IDS, PRODUCT_SYSTEM_IDS,ORG_UNIT, LICENCE_STATE_NULL);
        
        Assert.assertNotNull(result);
        Assert.assertNull(result.getErrorStatus());        
        //EasyMock.verify(getMocks());  
        
        GetFullUserEntitlementsResponseSequence groupSeq = result.getGetFullUserEntitlementsResponseSequence();
        FullProductEntitlementGroup[] wsGroups = groupSeq.getEntitlementGroup();
        FullProductEntitlementGroup wsGroup1 = wsGroups[0];
        checkGroups(wsGroup1, pe1, pe1Linked1);
    }   
    
    @Ignore
    @Test
    public void testSuccessWithSearchFilterOnLicenceState() throws WebServiceException, ServiceLayerException, ErightsException{
       
        EasyMock.expect(mCustomerLookup.getCustomerByWsUserId(wsUserId)).andReturn(customer);        
        this.mExternalSystemLookup.validateMultipleExternalSystem(SYSTEM_IDS);
        this.mExternalSystemLookup.validateMultipleExternalSystem(PRODUCT_SYSTEM_IDS_EMPTY_SET);
        this.mProductLookup.validateProductOrgUnit(ORG_UNIT);
        this.mProductLookup.validateLicenceState(LICENCE_STATE);
        EasyMock.expectLastCall();
        
        List<Registration<? extends ProductRegistrationDefinition>> registrations = new ArrayList<Registration<? extends ProductRegistrationDefinition>>();
        List<LicenceDto> licences = new ArrayList<LicenceDto>();
        CustomerRegistrationsDto custRegDto = new CustomerRegistrationsDto(customer, registrations, licences);
        EasyMock.expect(mRegistrationService.getEntitlementsForCustomerRegistrationsFiltered(customer, PRODUCT_SYSTEM_IDS_EMPTY_SET, PRODUCT_ORG_UNITS_EMPTY_SET, "ACTIVE")).andReturn(custRegDto);
        //expect(mRegistrationService.getEntitlementsForCustomerRegistrationsFiltered(customer, PRODUCT_SYSTEM_IDS_EMPTY_SET, LICENCE_STATE)).andReturn(custRegDto);
        EasyMock.expect(mExternalIdService.getFullExternalCustomerIdDto(customer)).andReturn(extCustDto);        
        EasyMock.expect(mCustomerConverter.convertCustomerToUser(extCustDto)).andReturn(user);
        
        ProductEntitlementGroupDto group1 = new ProductEntitlementGroupDto();
        ProductEntitlementDto g1Ent = new ProductEntitlementDto();
        ProductEntitlementDto g1LinkedEnt1 = new ProductEntitlementDto();
        
        int erightId=12345;
        DateTime dt=new DateTime();
        Boolean expired=false;
        Boolean active=true;
        
        LicenceDto lDto=new LicenceDto(String.valueOf(erightId), dt, expired, active,true,false,false);
        g1Ent.setLicence(lDto);        
        
        group1.setEntitlement(g1Ent);
        group1.setLinkedEntitlements(Arrays.asList(g1LinkedEnt1));
        
        List<ProductEntitlementGroupDto> groupList = Arrays.asList(group1);
        expect(mUserEntitlementsService.getFullUserEntitlementGroups(custRegDto)).andReturn(groupList);
        
        FullProductEntitlement pe1  = new FullProductEntitlement();
        FullProductEntitlement pe1Linked1 = new FullProductEntitlement();
        expect(mEntitlementsHelperAdapter.getFullProductEntitlement(g1Ent)).andReturn(pe1);
        expect(mEntitlementsHelperAdapter.getFullProductEntitlement(g1LinkedEnt1)).andReturn(pe1Linked1);
        //mProductLookup.validateLicenceState(LICENCE_STATE);
        EasyMock.replay(getMocks());       
        
        GetFullUserEntitlementsResponse result = sut.getFullUserEntitlementGroups(this.wsUserId, SYSTEM_IDS, PRODUCT_SYSTEM_IDS_EMPTY_SET, ORG_UNIT,"ACTIVE");
        
        Assert.assertNotNull(result);
        Assert.assertNull(result.getErrorStatus());        
        EasyMock.verify(getMocks());
        
        GetFullUserEntitlementsResponseSequence groupSeq = result.getGetFullUserEntitlementsResponseSequence();
        FullProductEntitlementGroup[] wsGroups = groupSeq.getEntitlementGroup();
        FullProductEntitlementGroup wsGroup1 = wsGroups[0];
        checkGroups(wsGroup1, pe1, pe1Linked1);
    }
    
    @Ignore
    @Test
    public void testSuccessWithSearchFilterOnAllFields() throws WebServiceException, ServiceLayerException, ErightsException{        
       
        EasyMock.expect(mCustomerLookup.getCustomerByWsUserId(wsUserId)).andReturn(customer);        
        this.mExternalSystemLookup.validateMultipleExternalSystem(SYSTEM_IDS);
        this.  mProductLookup.validateProductOrgUnit(ORG_UNIT);
        this.mExternalSystemLookup.validateMultipleExternalSystem(PRODUCT_SYSTEM_IDS);
        this.mProductLookup.validateLicenceState(LICENCE_STATE);
        EasyMock.expectLastCall();
        
        List<Registration<? extends ProductRegistrationDefinition>> registrations = new ArrayList<Registration<? extends ProductRegistrationDefinition>>();
        List<LicenceDto> licences = new ArrayList<LicenceDto>();
        CustomerRegistrationsDto custRegDto = new CustomerRegistrationsDto(customer, registrations, licences);
        EasyMock.expect(mRegistrationService.getEntitlementsForCustomerRegistrationsFiltered(customer, PRODUCT_SYSTEM_IDS_EMPTY_SET, PRODUCT_ORG_UNITS_EMPTY_SET, LICENCE_STATE_NULL)).andReturn(custRegDto);
        //expect(mRegistrationService.getEntitlementsForCustomerRegistrationsFiltered(customer, PRODUCT_SYSTEM_IDS, LICENCE_STATE)).andReturn(custRegDto);
        EasyMock.expect(mExternalIdService.getFullExternalCustomerIdDto(customer)).andReturn(extCustDto);        
        EasyMock.expect(mCustomerConverter.convertCustomerToUser(extCustDto)).andReturn(user);
        
        ProductEntitlementGroupDto group1 = new ProductEntitlementGroupDto();
        ProductEntitlementDto g1Ent = new ProductEntitlementDto();
        ProductEntitlementDto g1LinkedEnt1 = new ProductEntitlementDto();        
        
        ProductDetailsDto pdto= new ProductDetailsDto();        
        RegisterableProduct rPro =new RegisterableProduct();
        
        pdto.setProduct(rPro);  
        
        List<ExternalProductId> extProIds = new ArrayList<ExternalProductId>(Collections.<ExternalProductId>emptyList());
        ExternalProductId ePi= new ExternalProductId();
        ExternalSystemIdType esit=new ExternalSystemIdType();
        ExternalSystem es=new ExternalSystem();
        es.setName("PRODUCT_SYSTEM_ID1");
        esit.setExternalSystem(es);
        ePi.setExternalSystemIdType(esit);
        extProIds.add(ePi);
        pdto.setExternalProductIds(extProIds);
        
        List<ProductDetailsDto> pDtoList = new ArrayList<ProductDetailsDto>();
        pDtoList.add(pdto);
        g1Ent.setProductList(pDtoList);
                
        int erightId=12345;
        DateTime dt=new DateTime();
        Boolean expired=false;
        Boolean active=true;
        
        LicenceDto lDto=new LicenceDto(String.valueOf(erightId), dt, expired, active,true,false,false);
        g1Ent.setLicence(lDto);        
        
        group1.setEntitlement(g1Ent);
        group1.setLinkedEntitlements(Arrays.asList(g1LinkedEnt1));
        
        List<ProductEntitlementGroupDto> groupList = Arrays.asList(group1);
        expect(mUserEntitlementsService.getFullUserEntitlementGroups(custRegDto)).andReturn(groupList);
        
        FullProductEntitlement pe1  = new FullProductEntitlement();
        FullProductEntitlement pe1Linked1 = new FullProductEntitlement();
        expect(mEntitlementsHelperAdapter.getFullProductEntitlement(g1Ent)).andReturn(pe1);
        expect(mEntitlementsHelperAdapter.getFullProductEntitlement(g1LinkedEnt1)).andReturn(pe1Linked1);
        EasyMock.expect(mRegistrationService.getEntitlementsForCustomerRegistrationsFiltered(customer, PRODUCT_SYSTEM_IDS_EMPTY_SET, PRODUCT_ORG_UNITS_EMPTY_SET, "ACTIVE")).andReturn(custRegDto);
        EasyMock.replay(getMocks());       
        
        GetFullUserEntitlementsResponse result = sut.getFullUserEntitlementGroups(this.wsUserId, SYSTEM_IDS, PRODUCT_SYSTEM_IDS,ORG_UNIT, LICENCE_STATE);
        Assert.assertNotNull(result);
        Assert.assertNull(result.getErrorStatus());        
        EasyMock.verify(getMocks());

        GetFullUserEntitlementsResponseSequence groupSeq = result.getGetFullUserEntitlementsResponseSequence();
        FullProductEntitlementGroup[] wsGroups = groupSeq.getEntitlementGroup();
        FullProductEntitlementGroup wsGroup1 = wsGroups[0];
        checkGroups(wsGroup1, pe1, pe1Linked1);
        
    }
    
    
}
