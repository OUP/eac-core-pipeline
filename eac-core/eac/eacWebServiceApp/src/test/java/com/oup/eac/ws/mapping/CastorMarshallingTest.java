package com.oup.eac.ws.mapping;

import java.util.Properties;

import javax.mail.Session;
import javax.naming.NamingException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.mock.jndi.SimpleNamingContextBuilder;
import org.springframework.oxm.castor.CastorMarshaller;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.oup.eac.ws.v1.userdata.binding.UserNameResponse;
import com.oup.eac.ws.v2.binding.access.GetUserEntitlementsResponse;
import com.oup.eac.ws.v2.binding.access.RedeemActivationCodeResponse;
import com.oup.eac.ws.v2.binding.common.ErrorStatus;
import com.oup.eac.ws.v2.service.utils.ErrorStatusUtils;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:/eac-web-services-servlet.xml", "classpath*:/eac/test.eac*-beans.xml" })
public class CastorMarshallingTest extends BaseCastorTest {

    private static final String EXP_XML_REDEEM_ACTIVATION_CODES_RESPONSE = "mapping/expectedXmlForRedeemActivationCodesV2.xml";
    private static final String EXP_XML_USER_ENTITLEMENTS_RESPONSE = "mapping/expectedXmlForUserEntitlementsResponseV2.xml";
    private static final String EXP_XML_USER_NAME_RESPONSE_V1 = "mapping/expectedXmlForUserNameResponseV1.xml";
    private static final String EXP_XML_USER_NAME_RESPONSE_V2 = "mapping/expectedXmlForUserNameResponseV1.xml";
    
    public CastorMarshallingTest() {
        SimpleNamingContextBuilder builder;
        try {
            builder = SimpleNamingContextBuilder.emptyActivatedContextBuilder();
            builder.bind("java:/Mail", Session.getInstance(new Properties()));
        } catch (NamingException e) {
            throw new RuntimeException("problem with jndi", e);
        }
    }

    @Autowired
    @Qualifier("castorMarshallerV1")
    private CastorMarshaller castorV1;
    
    @Autowired
    @Qualifier("castorMarshallerV2")
    private CastorMarshaller castorV2;

    @Test
    public void testGetUserEntitlementsResponse() throws Exception {
        GetUserEntitlementsResponse resp = new GetUserEntitlementsResponse();
        String xml = getXml(this.castorV2, resp);
        String expected = getExpectedXml(EXP_XML_USER_ENTITLEMENTS_RESPONSE);
        checkEquals(expected, xml);
    }

    @Test
    public void testRedeeemActivationCodesResponse() throws Exception {
        RedeemActivationCodeResponse resp = new RedeemActivationCodeResponse();
        ErrorStatus respStatus = ErrorStatusUtils.getServerErrorStatus("oops");
        resp.setErrorStatus(respStatus);
        String xml = getXml(this.castorV2, resp);
        String expected = getExpectedXml(EXP_XML_REDEEM_ACTIVATION_CODES_RESPONSE);
        checkEquals(expected, xml);
    }
    
    @Test
    public void testGetUserNameResponseV1() throws Exception {
        UserNameResponse resp = new UserNameResponse();
        String xml = getXml(this.castorV1, resp);
        String expected = getExpectedXml(EXP_XML_USER_NAME_RESPONSE_V1);
        checkEquals(expected, xml);
    }
    
    @Test
    public void testGetUserNameResponseV2() throws Exception {
        com.oup.eac.ws.v2.binding.userdata.UserNameResponse resp = new com.oup.eac.ws.v2.binding.userdata.UserNameResponse();
        resp.setUserName("Mr John Doe1");
        String xml = getXml(this.castorV2,resp);
        String expected = getExpectedXml(EXP_XML_USER_NAME_RESPONSE_V2);
        checkEquals(expected, xml);
    }


}
