package com.oup.eac.ws.v2;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import com.oup.eac.ws.AbstractWebServiceAuthenticationTest;


/**
 * Tests authentication of GetUserName web service.
 * Tests basic response message of GetUserName web service - this checks that castor is working.
 * 
 * @author David Hay
 *
 */
@Component
public class GetUserNameAuthenticationTest extends AbstractWebServiceAuthenticationTest{

    private Resource req = new ClassPathResource("/soap/v2/userNameRequest1.xml");
    private Resource res = new ClassPathResource("/soap/v2/userNameResponse1.xml");
    
    @Override
    protected Resource getExpectedResponse() {
        return res;
    }
    @Override
    protected Resource getRequest() {
        return req;
    }
    

}