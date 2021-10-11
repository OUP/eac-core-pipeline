package com.oup.eac.ws.v2;

import net.javacrumbs.springws.test.WsTestException;

import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import com.oup.eac.ws.AbstractWebServiceAuthenticationTest;

/**
 * Checks that authenticateRequestPROBLEM doesn't work but should.
 * 
 * @author David Hay
 *
 */
@Component
public class AuthenticateAuthPROBLEMTest/* extends AbstractWebServiceAuthenticationTest */ {
	/*
	 * 
	 * private Resource request = new
	 * ClassPathResource("/soap/v2/authenticateRequestPROBLEM.xml");
	 * 
	 * private Resource response = new
	 * ClassPathResource("/soap/v2/authenticateResponsePROBLEM.xml");
	 * 
	 * @Override protected Resource getExpectedResponse() { return response; }
	 * 
	 * @Override protected Resource getRequest() { return request; }
	 * 
	 * 
	 *//**
		 * THIS DOES NOT WORK due to some bug in Castor/XML
		 */
	/*
	 * @Test(expected=WsTestException.class) public void
	 * testAdminAuthenticationSucess() throws Exception {
	 * super.testAdminAuthenticationSucess(); }
	 * 
	 * @Test public void testAdminAuthenticationFailure() throws Exception {
	 * super.testAdminAuthenticationFailure(); }
	 * 
	 * 
	 *//**
		 * THIS DOES NOT WORK due to some bug in Castor/XML
		 */
	/*
	 * @Test(expected=WsTestException.class) public void
	 * testEacuserAuthenticationSucess() throws Exception {
	 * super.testEacuserAuthenticationSucess(); }
	 * 
	 * @Test public void testAuthenticationeacuserFailure() throws Exception {
	 * super.testAuthenticationeacuserFailure(); }
	 * 
	 * 
	 * 
	 */}