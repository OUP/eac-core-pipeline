package com.oup.eac.ws.v2;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import com.oup.eac.ws.AbstractWebServiceAuthenticationTest;

/**
 * Tests authentication of RedeemActivationCodes web service.
 * Tests basic response message of RedeeemActivationCodes web service - this checks that castor is working.
 * Checks that we get the correct code when the java code throws a ServiceLayerException.
 * @author David Hay
 *
 */
@Component
public class RedeemActivationCodesAuth2Test /* extends AbstractWebServiceAuthenticationTest */ {
	/*
	 * 
	 * private Resource request = new
	 * ClassPathResource("/soap/v2/redeemActivationCodesRequest2.xml");
	 * 
	 * private Resource response = new
	 * ClassPathResource("/soap/v2/redeemActivationCodesResponse2.xml");
	 * 
	 * @Override protected Resource getExpectedResponse() { return response; }
	 * 
	 * @Override protected Resource getRequest() { return request; }
	 * 
	 * 
	 */}