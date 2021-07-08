package com.oup.eac.ws.v2.service.impl;

import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import java.util.UUID;

import javax.naming.NamingException;

import junit.framework.Assert;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;

import com.oup.eac.common.mock.AbstractMockTest;
import com.oup.eac.domain.Customer;
import com.oup.eac.integration.facade.exceptions.ErightsException;
import com.oup.eac.service.CustomerService;
import com.oup.eac.service.ServiceLayerException;
import com.oup.eac.service.exceptions.CustomerNotFoundServiceLayerException;
import com.oup.eac.ws.v2.binding.common.ExternalIdentifier;
import com.oup.eac.ws.v2.binding.common.Identifier;
import com.oup.eac.ws.v2.binding.common.InternalIdentifier;
import com.oup.eac.ws.v2.binding.common.WsUserId;
import com.oup.eac.ws.v2.ex.WebServiceException;
import com.oup.eac.ws.v2.ex.WebServiceValidationException;

public class WsCustomerLookupImplMockTest extends AbstractMockTest {
    
    private CustomerService mCustomerService;
    private WsCustomerLookupImpl sut;

    private String userid;
    private WsUserId internalUserId;
    private WsUserId sessionId;
    private WsUserId usernameId;
    private Customer customer;
    
    public WsCustomerLookupImplMockTest() throws NamingException {
        super();
    }

    @Before
    public void setup(){
        this.mCustomerService = EasyMock.createMock(CustomerService.class);
        this.sut = new WsCustomerLookupImpl(mCustomerService);
        this.customer = new Customer();
        userid = UUID.randomUUID().toString();   
        this.internalUserId = new WsUserId();
        {
            Identifier id = new Identifier();
            InternalIdentifier internal = new InternalIdentifier(); 
            internal.setId(userid);
            id.setInternalId(internal);
            this.internalUserId.setUserId(id);
        }
        this.sessionId = new WsUserId();
        {
            this.sessionId.setSessionToken(this.userid);
        }
        this.usernameId = new WsUserId();
        {
            this.usernameId.setUserName(this.userid);
        }
        this.setMocks(mCustomerService);
        
    }
 
    @Test
    public void testLookupViaUserIdInternalOkay() throws ServiceLayerException, WebServiceException, ErightsException {
        EasyMock.expect(this.mCustomerService.getCustomerByIdWs(this.userid)).andReturn(customer);
        
        replay(getMocks());
        Customer result = this.sut.getCustomerByWsUserId(this.internalUserId);
        verify(getMocks());
        Assert.assertEquals(customer,result);
    }
    
    @Test
    public void testLookupViaUserIdInternalFail1() throws ServiceLayerException, WebServiceException, ErightsException {
        EasyMock.expect(this.mCustomerService.getCustomerByIdWs(this.userid)).andReturn(null);
        
        replay(getMocks());
        try{
            @SuppressWarnings("unused")
            Customer result = this.sut.getCustomerByWsUserId(this.internalUserId);
            Assert.fail("exception expected");
        }catch(WebServiceValidationException wse){
            check(wse);
        }
        verify(getMocks());
    }
    
    @Test
    public void testLookupViaUserIdInternalFail2() throws WebServiceValidationException, ServiceLayerException, ErightsException  {
        RuntimeException rte = new RuntimeException();
        EasyMock.expect(this.mCustomerService.getCustomerByIdWs(this.userid)).andThrow(rte);
        
        replay(getMocks());
        try{
            @SuppressWarnings("unused")
            Customer result = this.sut.getCustomerByWsUserId(this.internalUserId);
            Assert.fail("exception expected");
        }catch(RuntimeException ex){
            Assert.assertEquals(rte,ex);
        }
        verify(getMocks());
    }

    @Test
    public void testLookupViaUsernameOkay() throws ServiceLayerException, WebServiceException, ErightsException {
        EasyMock.expect(this.mCustomerService.getCustomerByUsername(this.userid)).andReturn(customer);
        
        replay(getMocks());
        Customer result = this.sut.getCustomerByWsUserId(this.usernameId);
        verify(getMocks());
        Assert.assertEquals(customer,result);
    }
    
    @Test
    public void testLookupViaUsernameFailNull() throws ServiceLayerException, WebServiceException, ErightsException {
        EasyMock.expect(this.mCustomerService.getCustomerByUsername(this.userid)).andReturn(null);
        
        replay(getMocks());
        try{
            @SuppressWarnings("unused")
            Customer result = this.sut.getCustomerByWsUserId(this.usernameId);
            Assert.fail("exception expected");
        }catch(WebServiceValidationException wse){
            check(wse);
        }
        verify(getMocks());
    }
    
    @Test
    public void testLookupViaUsernameFailRte() throws ServiceLayerException, WebServiceValidationException, ErightsException {
        RuntimeException rte = new RuntimeException();
        EasyMock.expect(this.mCustomerService.getCustomerByUsername(this.userid)).andThrow(rte);
        
        replay(getMocks());
        try{
            @SuppressWarnings("unused")
            Customer result = this.sut.getCustomerByWsUserId(this.usernameId);
            Assert.fail("exception expected");
        }catch(RuntimeException ex){
            Assert.assertEquals(rte,ex);
        }
        verify(getMocks());
    }
    
    
    @Test
    public void testLookupViaSessionKeyOkay() throws ServiceLayerException, WebServiceException {
        EasyMock.expect(this.mCustomerService.getCustomerFromSession(this.userid)).andReturn(customer);
        
        replay(getMocks());
        Customer result = this.sut.getCustomerByWsUserId(this.sessionId);
        verify(getMocks());
        Assert.assertEquals(customer,result);
    }

    @Test
    public void testLookupViaSessionKeyFail1() throws ServiceLayerException, WebServiceException {
        EasyMock.expect(this.mCustomerService.getCustomerFromSession(this.userid)).andReturn(null);
        
        replay(getMocks());
        try{
            @SuppressWarnings("unused")
            Customer result = this.sut.getCustomerByWsUserId(this.sessionId);            
            Assert.fail("exception expected");
        }catch(WebServiceValidationException wse){
            check(wse);
        }
        verify(getMocks());
    }
    
    @Test
    public void testLookupViaSessionKeyFail2() throws ServiceLayerException, WebServiceException {
        EasyMock.expect(this.mCustomerService.getCustomerFromSession(this.userid)).andThrow(new CustomerNotFoundServiceLayerException());
        
        replay(getMocks());
        try{
            @SuppressWarnings("unused")
            Customer result = this.sut.getCustomerByWsUserId(this.sessionId);            
            Assert.fail("exception expected");
        }catch(WebServiceValidationException wse){
            check(wse);
        }
        verify(getMocks());
    }
    
    @Test
    public void testLookupViaSessionKeyFail3() throws ServiceLayerException, WebServiceException {
        RuntimeException rte = new RuntimeException();
        EasyMock.expect(this.mCustomerService.getCustomerFromSession(this.userid)).andThrow(rte);
        
        replay(getMocks());
        try{
            @SuppressWarnings("unused")
            Customer result = this.sut.getCustomerByWsUserId(this.sessionId);            
            Assert.fail("exception expected");
        }catch(RuntimeException ex){
            Assert.assertEquals(rte,ex);
        }
        verify(getMocks());
    }
    
    private void check(WebServiceException wse){
        Assert.assertTrue(wse.getMessage().toLowerCase().contains("no customer found"));
        Assert.assertTrue(wse.getMessage().contains(this.userid));
    }

    
    @Test
    public void testlookupWithEmpty1() throws ServiceLayerException, WebServiceException{
        WsUserId empty = new WsUserId();
        checkLookupWithEmptyid(empty);
    }
    
    @Test
    public void testlookupWithEmpty2() throws ServiceLayerException, WebServiceException{
        WsUserId empty = null;
        checkLookupWithEmptyid(empty);
    }
    
    @Test
    public void testlookupWithEmpty3() throws ServiceLayerException, WebServiceException{
        WsUserId empty = new WsUserId();
        Identifier id = new Identifier();
        empty.setUserId(id);
        checkLookupWithEmptyid(empty);
    }
    
    @Test
    public void testlookupWithEmpty4() throws ServiceLayerException, WebServiceException{
        WsUserId empty = new WsUserId();
        Identifier id = new Identifier();
        InternalIdentifier internal = new InternalIdentifier();        
        id.setInternalId(internal);
        empty.setUserId(id);
        checkLookupWithEmptyid(empty);
    }
    
    @Test
    public void testlookupWithBlankUserId1() throws ServiceLayerException, WebServiceException{
        WsUserId blank = new WsUserId();
        Identifier id = new Identifier();
        InternalIdentifier internal = new InternalIdentifier();
        internal.setId("");
        id.setInternalId(internal);
        blank.setUserId(id);
        checkLookupWithBlankId("user id",blank);
    }    
    
    @Test
    public void testlookupWithBlankUserId2() throws ServiceLayerException, WebServiceException{
        WsUserId blank = new WsUserId();
        Identifier id = new Identifier();
        InternalIdentifier internal = new InternalIdentifier();
        internal.setId("    ");
        id.setInternalId(internal);
        blank.setUserId(id);
        checkLookupWithBlankId("user id",blank);
    }
    
    @Test
    public void testlookupWithBlankUserName1() throws ServiceLayerException, WebServiceException{
        WsUserId blank = new WsUserId();
        blank.setUserName("");
        checkLookupWithBlankId("username",blank);
    }
    
    @Test
    public void testlookupWithBlankUserName2() throws ServiceLayerException, WebServiceException{
        WsUserId blank = new WsUserId();
        blank.setUserName("    ");
        checkLookupWithBlankId("username",blank);
    }
    
    @Test
    public void testlookupWithBlankSessionToken1() throws ServiceLayerException, WebServiceException{
        WsUserId blank = new WsUserId();
        blank.setSessionToken("");
        checkLookupWithBlankId("session token",blank);
    }
    
    @Test
    public void testlookupWithBlankSessionToken2() throws ServiceLayerException, WebServiceException{
        WsUserId blank = new WsUserId();
        blank.setSessionToken("    ");
        checkLookupWithBlankId("session token",blank);
    }
    
    
    @Test
    public void testlookupWithExternalBlank() throws ServiceLayerException, WebServiceException{
        WsUserId externalId = new WsUserId();
        Identifier id = new Identifier();
        ExternalIdentifier external = new ExternalIdentifier();
        id.setExternalId(external);
        externalId.setUserId(id);
        
        replay(getMocks());
        try{
            @SuppressWarnings("unused")
            Customer result = this.sut.getCustomerByWsUserId(externalId);
            Assert.fail("exception expected");
        }catch(WebServiceException wse){
            Assert.assertTrue(wse.getMessage().toLowerCase().contains("the 3 parts of the external identifer must not be blank"));
        }
        verify(getMocks());
        
    }
    
    @Test
    public void testlookupWithExternalNotFound() throws ServiceLayerException, WebServiceException{
        WsUserId externalId = new WsUserId();
        Identifier id = new Identifier();
        ExternalIdentifier external = new ExternalIdentifier();
        id.setExternalId(external);
        externalId.setUserId(id);
        
        replay(getMocks());
        try{
            @SuppressWarnings("unused")
            Customer result = this.sut.getCustomerByWsUserId(externalId);
            Assert.fail("exception expected");
        }catch(WebServiceException wse){
            Assert.assertTrue(wse.getMessage().toLowerCase().contains("the 3 parts of the external identifer must not be blank"));
        }
        verify(getMocks());
        
    }
    
    @Test
    public void testlookupWithExternal() throws ServiceLayerException, WebServiceException, ErightsException{
        WsUserId externalId = new WsUserId();
        Identifier id = new Identifier();
        
        EasyMock.expect(mCustomerService.getCustomerByExternalCustomerId("SYSTEM1", "TYPE1", "EXTID1")).andReturn(null);
        
        ExternalIdentifier external = new ExternalIdentifier();
        external.setId("EXTID1");
        external.setSystemId("SYSTEM1");
        external.setTypeId("TYPE1");
        
        id.setExternalId(external);
        externalId.setUserId(id);
        
        replay(getMocks());
        
        try{
            @SuppressWarnings("unused")
            Customer result = this.sut.getCustomerByWsUserId(externalId);
            Assert.fail("exception expected");
        }catch(WebServiceException wse){
            Assert.assertTrue(wse.getMessage().equals("No customer found for External Id : systemId[SYSTEM1] typeId[TYPE1] id[EXTID1]"));
        }
        verify(getMocks());
    }
    
    private  void checkLookupWithEmptyid(WsUserId empty) throws ServiceLayerException, WebServiceException {
        
        replay(getMocks());
        try{
            @SuppressWarnings("unused")
            Customer result = this.sut.getCustomerByWsUserId(empty);            
            Assert.fail("exception expected");
        }catch(WebServiceException wse){
            Assert.assertTrue(wse.getMessage().toLowerCase().contains("no customer found"));
        }
        verify(getMocks());
    }
    
    private  void checkLookupWithBlankId(String fragment, WsUserId blank) throws ServiceLayerException, WebServiceException {
        
        replay(getMocks());
        try{
            @SuppressWarnings("unused")
            Customer result = this.sut.getCustomerByWsUserId(blank);
            Assert.fail("exception expected");
        }catch(WebServiceException wse){
            Assert.assertTrue(wse.getMessage().toLowerCase().contains("cannot be blank."));
            Assert.assertTrue(wse.getMessage().toLowerCase().contains(fragment));
        }
        verify(getMocks());
    }

}
