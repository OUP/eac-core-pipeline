package com.oup.eac.ws.v2.service.impl;

import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import javax.naming.NamingException;

import junit.framework.Assert;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;

import com.oup.eac.common.mock.AbstractMockTest;
import com.oup.eac.domain.ExternalSystem;
import com.oup.eac.integration.facade.exceptions.ErightsException;
import com.oup.eac.service.ExternalIdService;
import com.oup.eac.ws.v2.binding.access.KillUserSessionResponse;
import com.oup.eac.ws.v2.ex.WebServiceException;
import com.oup.eac.ws.v2.ex.WebServiceValidationException;

public class WsExternalSystemLookupImplMockTest extends AbstractMockTest{

    public WsExternalSystemLookupImplMockTest() throws NamingException {
        super();
    }
    
    private ExternalIdService externalIdService;
    
    private WsExternalSystemLookupImpl sut;
    
    @Before
    public void setup(){
        this.externalIdService = EasyMock.createMock(ExternalIdService.class);        
        this.sut = new WsExternalSystemLookupImpl(this.externalIdService);
        setMocks(this.externalIdService);
    }
    
    @Test
    public void testNullSystemId() throws WebServiceValidationException,ErightsException {
        replay(getMocks());
        sut.validateExternalSystem(null);
        verify(getMocks());
    }
    
    @Test
    public void testBlankSystemId() throws WebServiceValidationException, ErightsException {
        replay(getMocks()); 
        try{
            sut.validateExternalSystem("     ");
            Assert.fail("exception expected");
        }catch (WebServiceValidationException wse) {
            Assert.assertEquals("systemId cannot be blank",wse.getMessage());
        } finally {
            verify(getMocks());
        }
    }
    
    @Test
    public void testExistingSystemId() throws WebServiceValidationException, ErightsException {
        ExternalSystem es = new ExternalSystem();
        EasyMock.expect(this.externalIdService.getExternalSystemByName("sys1")).andReturn(es);
        replay(getMocks());
        sut.validateExternalSystem("sys1");
        verify(getMocks());
    }
    
    @Test(expected=WebServiceValidationException.class)
    public void testNonExistentSystemId() throws WebServiceValidationException, ErightsException {
        EasyMock.expect(this.externalIdService.getExternalSystemByName("sys1")).andReturn(null);
        replay(getMocks());
        sut.validateExternalSystem("sys1");
        verify(getMocks());
    }
}
