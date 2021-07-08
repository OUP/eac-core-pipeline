package com.oup.eac.ws.mapping;

import java.io.IOException;

import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;

import junit.framework.Assert;

import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.oxm.castor.CastorMappingException;
import org.springframework.oxm.castor.CastorMarshaller;

import com.oup.eac.ws.v2.binding.common.CredentialName;
import com.oup.eac.ws.v2.binding.common.ErrorStatus;
import com.oup.eac.ws.v2.binding.common.ExternalIdentifier;
import com.oup.eac.ws.v2.binding.common.Identifiers;
import com.oup.eac.ws.v2.binding.common.RegistrationInformation;
import com.oup.eac.ws.v2.binding.common.User;
import com.oup.eac.ws.v2.binding.userdata.RegistrationInformationResponse;
import com.oup.eac.ws.v2.binding.userdata.RegistrationInformationResponseSequence;
import com.oup.eac.ws.v2.service.utils.ErrorStatusUtils;

public class CastorRegInfoResponseTest extends BaseCastorTest {

    public static final String EXPECTED_REG_INFO_RESPONSE_FAIL = "mapping/expectedRegInfoResponseFAIL.xml";
    public static final String EXPECTED_REG_INFO_RESPONSE_OKAY = "mapping/expectedRegInfoResponseOKAY.xml";

    private CastorMarshaller castor;
    private RegistrationInformationResponse responseFail;
    private RegistrationInformationResponse responseOkay;

    private String xmlFail;
    private String xmlOkay;

    @Before
    public void setup() throws CastorMappingException, IOException {
        this.castor = new CastorMarshaller();
        this.castor.setMappingLocation(new ClassPathResource("mapping/castorMappingRegInfoResponse.xml"));
        this.castor.afterPropertiesSet();
        this.responseFail = getFailResponse();
        this.responseOkay = getOkayResponse();
        this.xmlFail = getExpectedXml(EXPECTED_REG_INFO_RESPONSE_FAIL);
        this.xmlOkay = getExpectedXml(EXPECTED_REG_INFO_RESPONSE_OKAY);
    }

    @Test
    public void testToXmlError() throws Exception {
        String xml = getXml(castor, responseFail);
        checkEquals(this.xmlFail, xml);
    }

    @Test
    public void testToXmlOkay() throws Exception {
        String xml = getXml(this.castor, responseOkay);
        checkEquals(this.xmlOkay, xml);
    }

    @Test
    public void testFromXmlOkay() throws Exception {
        Source src = new StreamSource(IOUtils.toInputStream(this.xmlOkay));
        Object response = this.castor.unmarshal(src);
        Assert.assertTrue(response instanceof RegistrationInformationResponse);
        RegistrationInformationResponse resp = (RegistrationInformationResponse) response;
        Assert.assertNull(resp.getErrorStatus());
        Assert.assertNotNull(resp.getRegistrationInformationResponseSequence());
        RegistrationInformation[] regInfo = resp.getRegistrationInformationResponseSequence().getRegistrationInformation();
        Assert.assertEquals(2, regInfo.length);
        RegistrationInformation regInfo1 = regInfo[0];
        RegistrationInformation regInfo2 = regInfo[1];
        Assert.assertEquals(this.responseOkay.getRegistrationInformationResponseSequence().getRegistrationInformation(0).getRegistrationKey(), regInfo1
                .getRegistrationKey());
        Assert.assertEquals(this.responseOkay.getRegistrationInformationResponseSequence().getRegistrationInformation(0).getRegistrationValue(), regInfo1
                .getRegistrationValue());
        Assert.assertEquals(this.responseOkay.getRegistrationInformationResponseSequence().getRegistrationInformation(1).getRegistrationKey(), regInfo2
                .getRegistrationKey());
        Assert.assertEquals(this.responseOkay.getRegistrationInformationResponseSequence().getRegistrationInformation(1).getRegistrationValue(), regInfo2
                .getRegistrationValue());

        User user = resp.getRegistrationInformationResponseSequence().getUser();
        User expectedUser = this.responseOkay.getRegistrationInformationResponseSequence().getUser();

        Assert.assertEquals(expectedUser.getEmailAddress(), user.getEmailAddress());
        Assert.assertEquals(expectedUser.getFirstName(), user.getFirstName());
        Assert.assertEquals(expectedUser.getLastName(), user.getLastName());
        Assert.assertEquals(expectedUser.getCredentialName().getUserName(), user.getCredentialName().getUserName());
        Assert.assertEquals(expectedUser.getUserIds().getExternal()[0].getSystemId(), user.getUserIds().getExternal()[0].getSystemId());
        Assert.assertEquals(expectedUser.getUserIds().getExternal()[0].getTypeId(), user.getUserIds().getExternal()[0].getTypeId());
        Assert.assertEquals(expectedUser.getUserIds().getExternal()[0].getId(), user.getUserIds().getExternal()[0].getId());
    }

    @Test
    public void testFromXmlFail() throws Exception {
        Source src = new StreamSource(IOUtils.toInputStream(this.xmlFail));
        Object response = this.castor.unmarshal(src);
        Assert.assertTrue(response instanceof RegistrationInformationResponse);
        RegistrationInformationResponse resp = (RegistrationInformationResponse) response;
        Assert.assertNull(resp.getRegistrationInformationResponseSequence());
        ErrorStatus expected = this.responseFail.getErrorStatus();
        ErrorStatus actual = resp.getErrorStatus();
        Assert.assertEquals(expected.getStatusReason(), actual.getStatusReason());
        Assert.assertEquals(expected.getStatusCode(), actual.getStatusCode());
    }

    private RegistrationInformationResponse getFailResponse() {
        RegistrationInformationResponse resp = new RegistrationInformationResponse();
        ErrorStatus errStatus = ErrorStatusUtils.getServerErrorStatus("The error reason");
        resp.setErrorStatus(errStatus);
        return resp;
    }

    private RegistrationInformationResponse getOkayResponse() {
        RegistrationInformationResponse resp = new RegistrationInformationResponse();
        RegistrationInformationResponseSequence seq = new RegistrationInformationResponseSequence();
        User user = new User();
        Identifiers ids = new Identifiers();
        user.setUserIds(ids);
        user.setEmailAddress("david.hay@test.com");
        user.setFirstName("Davidy");
        user.setLastName("Hay");
        CredentialName credName = new CredentialName();
        credName.setUserName("davidhay");
        user.setCredentialName(credName);
        ExternalIdentifier ext = new ExternalIdentifier();
        ext.setId("USER123");
        ext.setTypeId("ISBN");
        ext.setSystemId("SYSTEM123");
        user.getUserIds().setExternal(new ExternalIdentifier[]{ext});
        seq.setUser(user);
        RegistrationInformation[] allRegInfo = new RegistrationInformation[2];
        RegistrationInformation regInfo1 = new RegistrationInformation();
        RegistrationInformation regInfo2 = new RegistrationInformation();
        regInfo1.setRegistrationKey("REGKEY1");
        regInfo1.setRegistrationValue("REGVALUE1");
        regInfo2.setRegistrationKey("REGKEY2");
        regInfo2.setRegistrationValue("REGVALUE2");
        allRegInfo[0] = regInfo1;
        allRegInfo[1] = regInfo2;
        seq.setRegistrationInformation(allRegInfo);
        resp.setRegistrationInformationResponseSequence(seq);
        return resp;
    }

}
