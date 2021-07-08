package com.oup.eac.ws.v2;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import com.oup.eac.ws.AbstractWebServiceAuthenticationTest;

@Component
public class ProductRegistrationAuthTest extends AbstractWebServiceAuthenticationTest {

    private Resource request = new ClassPathResource("/soap/v2/productRegistrationRequest1.xml");

    private Resource response = new ClassPathResource("/soap/v2/productRegistrationResponse1.xml");

    @Override
    protected Resource getExpectedResponse() {
        return response;
    }

    @Override
    protected Resource getRequest() {
        return request;
    }

}