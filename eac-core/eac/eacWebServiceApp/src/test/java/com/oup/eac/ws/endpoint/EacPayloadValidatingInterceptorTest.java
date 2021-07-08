package com.oup.eac.ws.endpoint;

import org.junit.Ignore;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import com.oup.eac.ws.AbstractWebServiceAuthenticationTest;

@Ignore
public class EacPayloadValidatingInterceptorTest extends AbstractWebServiceAuthenticationTest {

    private Resource request = new ClassPathResource("/soap/eacPayloadValidatingInterceptorRequest.xml");

    private Resource response = new ClassPathResource("/soap/eacPayloadValidatingInterceptorResponse.xml");

    @Override
    protected Resource getExpectedResponse() {
        return response;
    }

    @Override
    protected Resource getRequest() {
        return request;
    }

    @Override
    public boolean isFaultExpectedAfterAuthentication(){
        return true;
    }
}
