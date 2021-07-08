package com.oup.eac.integration.erights;

import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

public class AtyponConnectionIntegrationTest {

    private static final boolean UAT_TEST = Boolean.valueOf(System.getProperty("uat.test", "false"));
    private static String WEB_SERVICE_URL = "https://eac-dev-a-eacerigh-gmycvfloixgj-156402362.eu-west-1.elb.amazonaws.com/acesWebService/soap/services/access-service-v1.0.wsdl";
    private static String WSDL_PATH = "/eac/access-service-v1.0.wsdl";
    /**
     * Test that atypon wsdl does not change unexpectedly. Deals with line breaks which can change in expected file.
     * 
     * @throws Exception
     *             the exception
     */
    
   @Ignore
    @Test
    public final void testWSDLConnection() throws Exception {
        
        if (UAT_TEST) {
            WEB_SERVICE_URL = "https://eac-dev-a-eacerigh-gmycvfloixgj-156402362.eu-west-1.elb.amazonaws.com/acesWebService/soap/services/access-service-v1.0.wsdl";
            WSDL_PATH = "/eac/access-service-v1.0";
        }

        URL url = new URL(WEB_SERVICE_URL);
        
        URLConnection connection = url.openConnection();

        connection.connect();

        FileInputStream fis = new FileInputStream(AtyponConnectionIntegrationTest.class.getClass().getResource(WSDL_PATH).getFile());

        String expected = IOUtils.toString(fis);

        expected = removeWhitespace(expected);

        InputStream is = connection.getInputStream();
        String actual = IOUtils.toString(is);

        actual = removeWhitespace(actual);
        
        IOUtils.closeQuietly(is);
        IOUtils.closeQuietly(fis);

        Assert.assertEquals("Check wsdl returned from webservice is as expected", expected, actual);

    }
    
	private String removeWhitespace(String expected) {
		return expected.replaceAll("\r\n", "")
					   .replaceAll("\n", "")
					   .replaceAll("\t", "")
					   .replaceAll(">\\s+<", "><")
					   .replaceAll("\\s/>", "/>");
	}
}
