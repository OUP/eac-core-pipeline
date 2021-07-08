package com.oup.eac.ws.v2;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import com.oup.eac.ws.AbstractWebServiceAuthenticationTest;


/**
 * Tests authentication of ChangePassword web service.
 * Tests basic response message of ChangePassword web service - this checks that castor is working.
 * 
 * @author David Hay
 *
 */
@Component
public class ChangePasswordAuth2Test extends AbstractWebServiceAuthenticationTest {

    private Resource request = new ClassPathResource("/soap/v2/changePasswordRequest2.xml");

    private Resource response = new ClassPathResource("/soap/v2/changePasswordResponse2.xml");

    @Override
    protected Resource getExpectedResponse() {
        return response;
    }

    @Override
    protected Resource getRequest() {
        return request;
    }


}