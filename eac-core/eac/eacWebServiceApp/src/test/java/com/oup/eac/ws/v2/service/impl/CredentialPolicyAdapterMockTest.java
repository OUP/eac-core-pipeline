package com.oup.eac.ws.v2.service.impl;
import static org.easymock.EasyMock.expect;

import javax.naming.NamingException;

import junit.framework.Assert;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.oup.eac.ws.v2.binding.access.GetCredentialPolicyRequest;
import com.oup.eac.ws.v2.binding.access.GetCredentialPolicyResponse;
import com.oup.eac.ws.v2.binding.common.types.StatusCode;
import com.oup.eac.ws.v2.ex.WebServiceException;

//@RunWith(PowerMockRunner.class)
//@PrepareForTest(com.oup.eac.common.utils.EACSettings.class)
public class CredentialPolicyAdapterMockTest {
	/*
	 * private static final Logger LOG =
	 * Logger.getLogger(CreateUserAccountAdapterMockTest.class); private
	 * CredentialPolicyAdapterImpl sut;
	 * 
	 * 
	 * public CredentialPolicyAdapterMockTest() throws NamingException { super(); }
	 * 
	 * @Before public void setup() { this.sut= new CredentialPolicyAdapterImpl();
	 * PowerMock.mockStatic(EACSettings.class); }
	 * 
	 * @Test public void testSuccess() throws WebServiceException {
	 * GetCredentialPolicyRequest request = new GetCredentialPolicyRequest();
	 * GetCredentialPolicyResponse response = sut.getCredentialPolicy(request);
	 * Assert.assertNotNull(response); }
	 * 
	 * @SuppressWarnings("null")
	 * 
	 * @Test public void testFailure() { GetCredentialPolicyRequest request = new
	 * GetCredentialPolicyRequest(); String message = "Policies does not exist.";
	 * expect(EACSettings.getProperty(EACSettings.USERNAME_POLICY_REGEX)).andReturn(
	 * null);
	 * expect(EACSettings.getProperty(EACSettings.USERNAME_POLICY_TEXT)).andReturn(
	 * null);
	 * expect(EACSettings.getProperty(EACSettings.PASSWORD_POLICY_REGEX)).andReturn(
	 * null);
	 * expect(EACSettings.getProperty(EACSettings.PASSWORD_POLICY_TEXT)).andReturn(
	 * null); PowerMock.expectLastCall(); PowerMock.replay(EACSettings.class);
	 * 
	 * 
	 * GetCredentialPolicyResponse response=null; try { response =
	 * sut.getCredentialPolicy(request); } catch (WebServiceException e) {
	 * Assert.assertEquals(StatusCode.SERVER_ERROR,
	 * response.getErrorStatus().getStatusCode()); Assert.assertEquals(message,
	 * response.getErrorStatus().getStatusReason()); }
	 * PowerMock.verify(EACSettings.class); }
	 */} 