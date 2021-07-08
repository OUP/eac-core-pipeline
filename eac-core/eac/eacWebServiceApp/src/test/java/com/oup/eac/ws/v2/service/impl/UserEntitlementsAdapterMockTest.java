package com.oup.eac.ws.v2.service.impl;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import javax.naming.NamingException;

import junit.framework.Assert;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;

import com.oup.eac.common.mock.AbstractMockTest;
import com.oup.eac.domain.Customer;
import com.oup.eac.domain.ExternalCustomerId;
import com.oup.eac.domain.ProductRegistrationDefinition;
import com.oup.eac.domain.Registration;
import com.oup.eac.domain.entitlement.ProductEntitlementDto;
import com.oup.eac.domain.entitlement.ProductEntitlementGroupDto;
import com.oup.eac.dto.CustomerRegistrationsDto;
import com.oup.eac.dto.ExternalCustomerIdDto;
import com.oup.eac.dto.LicenceDto;
import com.oup.eac.integration.facade.exceptions.ErightsException;
import com.oup.eac.service.ExternalIdService;
import com.oup.eac.service.RegistrationService;
import com.oup.eac.service.ServiceLayerException;
import com.oup.eac.service.UserEntitlementsService;
import com.oup.eac.ws.v2.binding.access.GetUserEntitlementsResponse;
import com.oup.eac.ws.v2.binding.access.GetUserEntitlementsResponseSequence;
import com.oup.eac.ws.v2.binding.common.ErrorStatus;
import com.oup.eac.ws.v2.binding.common.ProductEntitlement;
import com.oup.eac.ws.v2.binding.common.ProductEntitlementGroup;
import com.oup.eac.ws.v2.binding.common.User;
import com.oup.eac.ws.v2.binding.common.WsUserId;
import com.oup.eac.ws.v2.binding.common.types.StatusCode;
import com.oup.eac.ws.v2.ex.WebServiceException;
import com.oup.eac.ws.v2.ex.WebServiceValidationException;
import com.oup.eac.ws.v2.service.UserEntitlementsAdapter;
import com.oup.eac.ws.v2.service.WsCustomerLookup;
import com.oup.eac.ws.v2.service.WsExternalSystemLookup;
import com.oup.eac.ws.v2.service.entitlements.CustomerConverter;
import com.oup.eac.ws.v2.service.entitlements.EntitlementsAdapterHelper;

/**
 * Mock test of UserEntitlementsAdapter.
 * 
 * @author David Hay
 * 
 */
public class UserEntitlementsAdapterMockTest extends AbstractMockTest {

    public UserEntitlementsAdapterMockTest() throws NamingException {
        super();
    }

    private static final String SYSTEM_ID1 = "SYSTEM_ID1";

    // system under test
    private UserEntitlementsAdapter sut;

    private RegistrationService mRegistrationService;
    private CustomerConverter mCustomerConverter;
    private WsCustomerLookup mCustomerLookup;
    private WsExternalSystemLookup mExternalSystemLookup;

    private Customer customer;
    private ProductEntitlement ent1;
    private ProductEntitlement ent2;
    private ProductEntitlement ent3;
    private User user;
    private WsUserId wsUserId;

    private ExternalIdService mExternalIdService;

    private ExternalCustomerIdDto extCustDto;
    
    private UserEntitlementsService mUserEntitlementsService;

    private EntitlementsAdapterHelper mEntitlementsHelperAdapter;
    
    @Before
    public void setup() {
        this.customer = new Customer();
        this.user = new User();        
        this.mRegistrationService = EasyMock.createMock(RegistrationService.class);
        this.mCustomerConverter = EasyMock.createMock(CustomerConverter.class);
        this.mCustomerLookup = EasyMock.createMock(WsCustomerLookup.class);
        this.mExternalSystemLookup = EasyMock.createMock(WsExternalSystemLookup.class);
        this.mExternalIdService = EasyMock.createMock(ExternalIdService.class);
        this.mUserEntitlementsService = EasyMock.createMock(UserEntitlementsService.class);
        this.mEntitlementsHelperAdapter = EasyMock.createMock(EntitlementsAdapterHelper.class);
        
        this.setMocks(mCustomerLookup, mRegistrationService, 
        		mCustomerConverter, mExternalIdService,
        		mExternalSystemLookup, mUserEntitlementsService,
        		mEntitlementsHelperAdapter);
       
        	this.sut = new UserEntitlementsAdapterImpl(
        		mCustomerLookup, mRegistrationService, 
        		mCustomerConverter, mExternalIdService,
        		mExternalSystemLookup, mUserEntitlementsService,
        		mEntitlementsHelperAdapter);
        	
        this.wsUserId = new WsUserId();
        
        List<ExternalCustomerId> ids = new ArrayList<ExternalCustomerId>();
        this.extCustDto = new ExternalCustomerIdDto(customer, ids);
    }

    /**
     * This test checks that we get a ServiceLayerException if the registrationService throws a ServiceLayerException.
     * @throws ServiceLayerException 
     * 
     * @throws ServiceLayerException
     */
    @Test
    public void testServiceLaterExceptionThrown() throws WebServiceException, ServiceLayerException, ErightsException {

        expect(mCustomerLookup.getCustomerByWsUserId(this.wsUserId)).andReturn(customer);

        expect(mRegistrationService.getEntitlementsForCustomerRegistrations(customer,null,false)).andThrow(new ServiceLayerException("oops1"));

        mExternalSystemLookup.validateExternalSystem(SYSTEM_ID1);
        expectLastCall();

        // no expectations on helper if there's a service layer exception

        replay(getMocks());

        try {
            @SuppressWarnings("unused")
            GetUserEntitlementsResponse result = sut.getUserEntitlementGroups(SYSTEM_ID1, this.wsUserId);
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

        GetUserEntitlementsResponse result = sut.getUserEntitlementGroups(SYSTEM_ID1, this.wsUserId);
        Assert.assertEquals(StatusCode.CLIENT_ERROR, result.getErrorStatus().getStatusCode());
        Assert.assertEquals("oops",result.getErrorStatus().getStatusReason());
            
    }

    @Test
    public void testGroupedProductEntitlementsSingleGroup() throws WebServiceException, ServiceLayerException, ErightsException{
        List<Registration<? extends ProductRegistrationDefinition>> registrations = new ArrayList<Registration<? extends ProductRegistrationDefinition>>();
        List<LicenceDto> licences = new ArrayList<LicenceDto>();

        expect(mCustomerLookup.getCustomerByWsUserId(this.wsUserId)).andReturn(customer);
        CustomerRegistrationsDto custRegDto = new CustomerRegistrationsDto(customer, registrations, licences);
		expect(mRegistrationService.getEntitlementsForCustomerRegistrations(customer,null,false)).andReturn(custRegDto);

        mExternalSystemLookup.validateExternalSystem(SYSTEM_ID1);
        expectLastCall();

		expect(mCustomerConverter.convertCustomerToUser(extCustDto)).andReturn(user);
		
		ProductEntitlementGroupDto group1 = new ProductEntitlementGroupDto();
		ProductEntitlementGroupDto group2 = new ProductEntitlementGroupDto();
		ProductEntitlementGroupDto group3 = new ProductEntitlementGroupDto();
		ProductEntitlementDto g1Ent = new ProductEntitlementDto();
		ProductEntitlementDto g1LinkedEnt1 = new ProductEntitlementDto();
		ProductEntitlementDto g1LinkedEnt2 = new ProductEntitlementDto();
		group1.setEntitlement(g1Ent);
		group1.setLinkedEntitlements(Arrays.asList(g1LinkedEnt1,g1LinkedEnt2));
		
		ProductEntitlementDto g2Ent = new ProductEntitlementDto();
		ProductEntitlementDto g2LinkedEnt1 = new ProductEntitlementDto();
		ProductEntitlementDto g2LinkedEnt2 = new ProductEntitlementDto();
		group2.setEntitlement(g2Ent);
		group2.setLinkedEntitlements(Arrays.asList(g2LinkedEnt1,g2LinkedEnt2));

		ProductEntitlementDto g3Ent = new ProductEntitlementDto();
		ProductEntitlementDto g3LinkedEnt1 = new ProductEntitlementDto();
		ProductEntitlementDto g3LinkedEnt2 = new ProductEntitlementDto();
		group3.setEntitlement(g3Ent);
		group3.setLinkedEntitlements(Arrays.asList(g3LinkedEnt1,g3LinkedEnt2));

		List<ProductEntitlementGroupDto> groupList = Arrays.asList(group1, group2, group3);
		expect(mUserEntitlementsService.getUserEntitlementGroups(custRegDto, SYSTEM_ID1)).andReturn(groupList);
        expect(mExternalIdService.getExternalCustomerIds(customer,SYSTEM_ID1)).andReturn(extCustDto);
        
        ProductEntitlement pe1  = new ProductEntitlement();
        ProductEntitlement pe1Linked1 = new ProductEntitlement();
        ProductEntitlement pe1Linked2 = new ProductEntitlement();
        
        ProductEntitlement pe2  = new ProductEntitlement();
        ProductEntitlement pe2Linked1 = new ProductEntitlement();
        ProductEntitlement pe2Linked2 = new ProductEntitlement();

        ProductEntitlement pe3  = new ProductEntitlement();
        ProductEntitlement pe3Linked1 = new ProductEntitlement();
        ProductEntitlement pe3Linked2 = new ProductEntitlement();
        
        expect(mEntitlementsHelperAdapter.getProductEntitlement(g1Ent)).andReturn(pe1);
		expect(mEntitlementsHelperAdapter.getProductEntitlement(g1LinkedEnt1)).andReturn(pe1Linked1);
		expect(mEntitlementsHelperAdapter.getProductEntitlement(g1LinkedEnt2)).andReturn(pe1Linked2);
		
		expect(mEntitlementsHelperAdapter.getProductEntitlement(g2Ent)).andReturn(pe2);
		expect(mEntitlementsHelperAdapter.getProductEntitlement(g2LinkedEnt1)).andReturn(pe2Linked1);
		expect(mEntitlementsHelperAdapter.getProductEntitlement(g2LinkedEnt2)).andReturn(pe2Linked2);
		
		expect(mEntitlementsHelperAdapter.getProductEntitlement(g3Ent)).andReturn(pe3);
		expect(mEntitlementsHelperAdapter.getProductEntitlement(g3LinkedEnt1)).andReturn(pe3Linked1);
		expect(mEntitlementsHelperAdapter.getProductEntitlement(g3LinkedEnt2)).andReturn(pe3Linked2);
		
        replay(getMocks());

        // CALL THE METHOD WE ARE TESTING
        GetUserEntitlementsResponse result = sut.getUserEntitlementGroups(SYSTEM_ID1, wsUserId);
        verify(getMocks());

        GetUserEntitlementsResponseSequence groupSeq = result.getGetUserEntitlementsResponseSequence();
        ProductEntitlementGroup[] wsGroups = groupSeq.getEntitlementGroup();
        ProductEntitlementGroup wsGroup1 = wsGroups[0];
        ProductEntitlementGroup wsGroup2 = wsGroups[1];
        ProductEntitlementGroup wsGroup3 = wsGroups[2];
        checkGroups(wsGroup1, pe1, pe1Linked1, pe1Linked2);
        checkGroups(wsGroup2, pe2, pe2Linked1, pe2Linked2);
        checkGroups(wsGroup3, pe3, pe3Linked1, pe3Linked2);
    }
    
    private void checkGroups(ProductEntitlementGroup wsGroup,
			ProductEntitlement top, ProductEntitlement... children) {
    	Assert.assertEquals(top, wsGroup.getEntitlement());
		for(int i=0;i<children.length;i++){
			Assert.assertEquals(children[i],wsGroup.getLinkedEntitlement()[i]);
		}
	}

	@Test
    public void testInvalidSystemId() throws WebServiceException, ServiceLayerException, ErightsException{
        expect(mCustomerLookup.getCustomerByWsUserId(this.wsUserId)).andReturn(customer);

        String randomMessage = UUID.randomUUID().toString();
        mExternalSystemLookup.validateExternalSystem(SYSTEM_ID1);
        expectLastCall().andThrow(new WebServiceValidationException(randomMessage));

        replay(getMocks());

        // CALL THE METHOD WE ARE TESTING
        GetUserEntitlementsResponse result = sut.getUserEntitlementGroups(SYSTEM_ID1, wsUserId);
        verify(getMocks());

        ErrorStatus errorStatus = result.getErrorStatus();
        Assert.assertEquals(StatusCode.CLIENT_ERROR,errorStatus.getStatusCode());
        Assert.assertEquals(randomMessage,errorStatus.getStatusReason());
        Assert.assertNull(result.getGetUserEntitlementsResponseSequence());

    }


}
