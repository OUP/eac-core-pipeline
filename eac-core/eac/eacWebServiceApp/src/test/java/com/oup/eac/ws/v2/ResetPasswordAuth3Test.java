package com.oup.eac.ws.v2;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import com.oup.eac.ws.AbstractWebServiceAuthenticationTest;


/**
 * Tests authentication of ResetPassword web service.
 * Tests basic response message of ResetPassword web service - this checks that castor is working.
 * 
 * @author David Hay
 *
 */
@Component
public class ResetPasswordAuth3Test extends AbstractWebServiceAuthenticationTest {

    private Resource request = new ClassPathResource("/soap/v2/resetPasswordRequest3.xml");

    private Resource response = new ClassPathResource("/soap/v2/resetPasswordResponse3.xml");

    @Override
    protected Resource getExpectedResponse() {
        return response;
    }

    @Override
    protected Resource getRequest() {
        return request;
    }


}