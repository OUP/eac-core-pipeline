package com.oup.eac.ws.v2;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import com.oup.eac.ws.AbstractWebServiceAuthenticationTest;


/**
 * Tests authentication of ValidateActivationCode web service.
 * Tests basic response message of ValidateActivationCode web service - this checks that castor is working.
 * 
 * @author David Hay
 *
 */
@Component
public class ValidateActivationCodeAuthTest extends AbstractWebServiceAuthenticationTest {

    private Resource request = new ClassPathResource("/soap/v2/validateActivationCodeRequest1.xml");

    private Resource response = new ClassPathResource("/soap/v2/validateActivationCodeResponse1.xml");

    @Override
    protected Resource getExpectedResponse() {
        return response;
    }

    @Override
    protected Resource getRequest() {
        return request;
    }


}