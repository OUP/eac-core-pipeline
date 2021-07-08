package com.oup.eac.ws.v2.service.impl;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.naming.NamingException;

import junit.framework.Assert;

import org.easymock.EasyMock;
import org.easymock.IArgumentMatcher;
import org.junit.Before;
import org.junit.Test;

import com.oup.eac.common.mock.AbstractMockTest;
import com.oup.eac.domain.Customer;
import com.oup.eac.dto.ExternalIdDto;
import com.oup.eac.service.ExternalIdService;
import com.oup.eac.service.ServiceLayerException;
import com.oup.eac.service.exceptions.ServiceLayerValidationException;
import com.oup.eac.ws.v2.binding.access.SetExternalUserIdsRequest;
import com.oup.eac.ws.v2.binding.access.SetExternalUserIdsResponse;
import com.oup.eac.ws.v2.binding.common.ExternalIdentifier;
import com.oup.eac.ws.v2.binding.common.WsUserId;
import com.oup.eac.ws.v2.binding.common.types.StatusCode;
import com.oup.eac.ws.v2.ex.WebServiceException;
import com.oup.eac.ws.v2.ex.WebServiceValidationException;
import com.oup.eac.ws.v2.service.ExternalUserIdsAdapter;
import com.oup.eac.ws.v2.service.WsCustomerLookup;

public class SetExternalUserIdsRequestMockTest extends AbstractMockTest {

    public SetExternalUserIdsRequestMockTest() throws NamingException {
        super();
    }

    private ExternalUserIdsAdapter sut;
    private ExternalIdService externalIdService;
    private WsCustomerLookup customerLookup;
    private String randomMessage;

    @Before
    public void setup() {
        externalIdService = createMock(ExternalIdService.class);
        customerLookup = createMock(WsCustomerLookup.class);
        setMocks(externalIdService, customerLookup);
        this.sut = new ExternalUserIdsAdapterImpl(customerLookup, externalIdService);
        this.randomMessage = UUID.randomUUID().toString();
    }

    @Test
    public void testSuccessRemoveExisting() throws WebServiceException, ServiceLayerException {
        SetExternalUserIdsRequest request = new SetExternalUserIdsRequest();
        WsUserId wsUserId = new WsUserId();
        request.setWsUserId(wsUserId);
        request.setSystemId("SYSTEM1");
        Customer customer = new Customer();
        
        expect(this.customerLookup.getCustomerByWsUserId(wsUserId)).andReturn(customer);
        List<ExternalIdDto> dtos = new ArrayList<ExternalIdDto>();//an empty list
        this.externalIdService.saveExternalCustomerIdsForSystem(eq(customer), eq("SYSTEM1"), eq(dtos));
        expectLastCall();
        
        replay(getMocks());        
        SetExternalUserIdsResponse result = sut.setExternalUserIds(request);        
        verify(getMocks());
        
        Assert.assertNull(result.getErrorStatus());
        
    }
    
    @Test
    public void testFailureUnknownSystem() throws WebServiceException, ServiceLayerException {
        SetExternalUserIdsRequest request = new SetExternalUserIdsRequest();
        WsUserId wsUserId = new WsUserId();
        request.setWsUserId(wsUserId);
        request.setSystemId("SYSTEM1");
        Customer customer = new Customer();
        expect(this.customerLookup.getCustomerByWsUserId(wsUserId)).andReturn(customer);
        List<ExternalIdDto> dtos = new ArrayList<ExternalIdDto>();//an empty list
        this.externalIdService.saveExternalCustomerIdsForSystem(eq(customer), eq("SYSTEM1"), eq(dtos));
        expectLastCall().andThrow(new ServiceLayerValidationException(randomMessage));
        
        replay(getMocks());        
        SetExternalUserIdsResponse result = sut.setExternalUserIds(request);        
        verify(getMocks());
        
        Assert.assertNotNull(result.getErrorStatus());
        Assert.assertEquals(StatusCode.CLIENT_ERROR, result.getErrorStatus().getStatusCode());
        Assert.assertEquals(randomMessage, result.getErrorStatus().getStatusReason());
        
    }
    
    @Test
    public void testFailureServiceLayerException() throws WebServiceException, ServiceLayerException {
        try{
            SetExternalUserIdsRequest request = new SetExternalUserIdsRequest();
            WsUserId wsUserId = new WsUserId();
            request.setWsUserId(wsUserId);
            request.setSystemId("SYSTEM1");
            Customer customer = new Customer();
            expect(this.customerLookup.getCustomerByWsUserId(wsUserId)).andReturn(customer);
            List<ExternalIdDto> dtos = new ArrayList<ExternalIdDto>();//an empty list
            this.externalIdService.saveExternalCustomerIdsForSystem(eq(customer), eq("SYSTEM1"), eq(dtos));
            expectLastCall().andThrow(new ServiceLayerException(randomMessage));
        
            replay(getMocks());        
            @SuppressWarnings("unused")
            SetExternalUserIdsResponse result = sut.setExternalUserIds(request);
            Assert.fail("exception expected");
        
        }catch(WebServiceException wse){
            Assert.assertEquals("problem setting external user ids",wse.getMessage());
            Assert.assertEquals(randomMessage,wse.getCause().getMessage());            
        }finally{
            verify(getMocks());
        }
    }
    
    @Test
    public void testSuccessManyExternalUserIds() throws WebServiceException, ServiceLayerException {
        try{
            SetExternalUserIdsRequest request = new SetExternalUserIdsRequest();
            WsUserId wsUserId = new WsUserId();
            request.setWsUserId(wsUserId);
            request.setSystemId("SYSTEM1");
            ExternalIdentifier extId1 = getExternalId("SYSTEM1","type1","11111");
            ExternalIdentifier extId2 = getExternalId("SYSTEM1","type2","22222");
            ExternalIdentifier extId3 = getExternalId("SYSTEM1","type3","33333");
            ExternalIdentifier[] vExternalArray = { extId1, extId2, extId3 };
            request.setExternal(vExternalArray);
            Customer customer = new Customer();
            expect(this.customerLookup.getCustomerByWsUserId(wsUserId)).andReturn(customer);
            
            this.externalIdService.saveExternalCustomerIdsForSystem(eq(customer), eq("SYSTEM1"), this.eqExternalCustomerDtos(vExternalArray));
            expectLastCall().andThrow(new ServiceLayerException(randomMessage));
        
            replay(getMocks());        
            @SuppressWarnings("unused")
            SetExternalUserIdsResponse result = sut.setExternalUserIds(request);
            Assert.fail("exception expected");
        
        }catch(WebServiceException wse){
            Assert.assertEquals("problem setting external user ids",wse.getMessage());
            Assert.assertEquals(randomMessage,wse.getCause().getMessage());            
        }finally{
            verify(getMocks());
        }
    }
    
    @Test
    public void testFailureBlankExternalIds() throws WebServiceException, ServiceLayerException {
        SetExternalUserIdsRequest request = new SetExternalUserIdsRequest();
        WsUserId wsUserId = new WsUserId();
        request.setWsUserId(wsUserId);
        request.setSystemId("SYSTEM1");
        ExternalIdentifier extId1 = getExternalId("SYSTEM1","type1","    ");
        ExternalIdentifier extId2 = getExternalId("SYSTEM1","type2","22222");
        ExternalIdentifier extId3 = getExternalId("SYSTEM1","type3","33333");
        ExternalIdentifier[] vExternalArray = { extId1, extId2, extId3 };
        request.setExternal(vExternalArray);
        Customer customer = new Customer();
        expect(this.customerLookup.getCustomerByWsUserId(wsUserId)).andReturn(customer);
           
        replay(getMocks());        
        SetExternalUserIdsResponse result = sut.setExternalUserIds(request);
            
        Assert.assertEquals(StatusCode.CLIENT_ERROR, result.getErrorStatus().getStatusCode());
        Assert.assertEquals("The externalIds cannot be blank",result.getErrorStatus().getStatusReason());
            
        verify(getMocks());
    }
    
    @Test
    public void testFailureBlankTypeIds() throws WebServiceException, ServiceLayerException {
        SetExternalUserIdsRequest request = new SetExternalUserIdsRequest();
        WsUserId wsUserId = new WsUserId();
        request.setWsUserId(wsUserId);
        request.setSystemId("SYSTEM1");
        ExternalIdentifier extId1 = getExternalId("SYSTEM1","type1","11111");
        ExternalIdentifier extId2 = getExternalId("SYSTEM1","     ","22222");
        ExternalIdentifier extId3 = getExternalId("SYSTEM1","type3","33333");
        ExternalIdentifier[] vExternalArray = { extId1, extId2, extId3 };
        request.setExternal(vExternalArray);
        Customer customer = new Customer();
        expect(this.customerLookup.getCustomerByWsUserId(wsUserId)).andReturn(customer);
           
        replay(getMocks());        
        SetExternalUserIdsResponse result = sut.setExternalUserIds(request);
            
        Assert.assertEquals(StatusCode.CLIENT_ERROR, result.getErrorStatus().getStatusCode());
        Assert.assertEquals("The typeIds cannot be blank",result.getErrorStatus().getStatusReason());
            
        verify(getMocks());
    }

    private ExternalIdentifier getExternalId(String system, String type, String id) {
        ExternalIdentifier result = new ExternalIdentifier();
        result.setSystemId(system);
        result.setTypeId(type);
        result.setId(id);
        return result;
    }

    @Test
    public void testFailureUnknownCustomer() throws WebServiceException, ServiceLayerException {
        SetExternalUserIdsRequest request = new SetExternalUserIdsRequest();
        WsUserId wsUserId = new WsUserId();
        request.setWsUserId(wsUserId);
        
        expect(this.customerLookup.getCustomerByWsUserId(wsUserId)).andThrow(new WebServiceValidationException(randomMessage));
        
        replay(getMocks());        
        SetExternalUserIdsResponse result = sut.setExternalUserIds(request);        
        verify(getMocks());
        
        Assert.assertNotNull(result.getErrorStatus());
        Assert.assertEquals(StatusCode.CLIENT_ERROR, result.getErrorStatus().getStatusCode());
        Assert.assertEquals(randomMessage, result.getErrorStatus().getStatusReason());
    }
    
    @Test
    public void testFailureSystemNotConsistent() throws WebServiceException, ServiceLayerException {
        SetExternalUserIdsRequest request = new SetExternalUserIdsRequest();
        WsUserId wsUserId = new WsUserId();
        request.setWsUserId(wsUserId);
        request.setSystemId("SYSTEM1");
        ExternalIdentifier extId1 = getExternalId("SYSTEM1","type1","11111");
        ExternalIdentifier extId2 = getExternalId("SYSTEM2","type2","22222");
        ExternalIdentifier extId3 = getExternalId("SYSTEM1","type3","33333");
        ExternalIdentifier[] vExternalArray = { extId1, extId2, extId3 };
        request.setExternal(vExternalArray);
        Customer customer = new Customer();
        expect(this.customerLookup.getCustomerByWsUserId(wsUserId)).andReturn(customer);
        
        replay(getMocks());        
        SetExternalUserIdsResponse result = sut.setExternalUserIds(request);        
        verify(getMocks());
        
        Assert.assertNotNull(result.getErrorStatus());
        Assert.assertEquals(StatusCode.CLIENT_ERROR, result.getErrorStatus().getStatusCode());
        Assert.assertEquals("Different systemIds are present in the request", result.getErrorStatus().getStatusReason());
    }
    
    @Test
    public void testFailureBlankSystemId() throws WebServiceException, ServiceLayerException {
        SetExternalUserIdsRequest request = new SetExternalUserIdsRequest();
        WsUserId wsUserId = new WsUserId();
        request.setWsUserId(wsUserId);
        request.setSystemId("     ");
        Customer customer = new Customer();
        expect(this.customerLookup.getCustomerByWsUserId(wsUserId)).andReturn(customer);
        
        replay(getMocks());        
        SetExternalUserIdsResponse result = sut.setExternalUserIds(request);        
        verify(getMocks());
        
        Assert.assertNotNull(result.getErrorStatus());
        Assert.assertEquals(StatusCode.CLIENT_ERROR, result.getErrorStatus().getStatusCode());
        Assert.assertEquals("The systemId cannot be blank", result.getErrorStatus().getStatusReason());
    }
    
    
    /**
     * EasyMock custom matcher for Customer objects
     * @param username the username
     * @param hashedPassword the hashed password
     * 
     * @see UpdateUserAccountAdapterMockTest
     * @return the customer
     */
    protected List<ExternalIdDto> eqExternalCustomerDtos(final ExternalIdentifier[] ids) {
        IArgumentMatcher matcher = new IArgumentMatcher() {

            private boolean isEq(Object expected, Object actual){
                if(expected == null){
                    return actual == null;
                }else{
                    return expected.equals(actual);
                }
            }
            
            @Override
            public boolean matches(Object arg) {
                if (arg instanceof List<?> == false) {
                    return false;
                }
                @SuppressWarnings("unchecked")
                List<ExternalIdDto> dtos = (List<ExternalIdDto>)arg;
                boolean same = true;
                for(int i=0 ; i<ids.length ; i++){
                    ExternalIdentifier id = ids[i];
                    ExternalIdDto dto = dtos.get(i);
                    same &= checkSame(id,dto);
                }
                return same;
            }

            private boolean checkSame(ExternalIdentifier id, ExternalIdDto dto) {
                boolean ck1 = isEq(id.getId(),dto.getId());
                boolean ck2 = isEq(id.getSystemId(),dto.getSystemId());
                boolean ck3 = isEq(id.getTypeId(),dto.getType());
                boolean result = ck1 && ck2 && ck3;
                return result;
            }

            @Override
            public void appendTo(StringBuffer out) {
                out.append("eqExternalCustomerDtos(");
                out.append(ids);
                out.append(")");
            }
        };
        EasyMock.reportMatcher(matcher);
        return null;
    }

}
