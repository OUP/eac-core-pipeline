package com.oup.eac.ws.v2;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import com.oup.eac.ws.AbstractWebServiceAuthenticationTest;


/**
 * Checks that by moving the xmlns:xsi, that authenticateRequest4.xml will work.
 * 
 * @author David Hay
 *
 */
@Component
public class AuthenticateAuth4Test extends AbstractWebServiceAuthenticationTest {

    private Resource request = new ClassPathResource("/soap/v2/authenticateRequest4.xml");

    private Resource response = new ClassPathResource("/soap/v2/authenticateResponse4.xml");

    @Override
    protected Resource getExpectedResponse() {
        return response;
    }

    @Override
    protected Resource getRequest() {
        return request;
    }


}