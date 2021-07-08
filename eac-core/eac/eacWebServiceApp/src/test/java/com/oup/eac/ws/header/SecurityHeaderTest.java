package com.oup.eac.ws.header;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.mail.Session;
import javax.naming.NamingException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import junit.framework.Assert;

import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.mock.jndi.SimpleNamingContextBuilder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.ws.context.DefaultMessageContext;
import org.springframework.ws.soap.SoapMessage;
import org.springframework.ws.soap.SoapMessageFactory;
import org.springframework.ws.soap.saaj.SaajSoapMessageFactory;

import com.oup.eac.ws.endpoint.SecurityHeaderEndpointInterceptor;
import com.oup.eac.ws.util.XmlDiff;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:/eac-web-services-servlet.xml", "classpath*:/eac/test.eac*-beans.xml" })
public class SecurityHeaderTest {

    public static final String SOAP_1_BEFORE = "soap/header/soap1WithNoSecurityHeaderInput.xml"; 
    public static final String SOAP_1_AFTER = "soap/header/soap1WithNoSecurityHeaderOutput.xml";
    
    public static final String SOAP_2_BEFORE = "soap/header/soap2WithSecurityHeaderInput.xml"; 
    public static final String SOAP_2_AFTER = "soap/header/soap2WithSecurityHeaderOutput.xml";
    
    public static final String SOAP_3_BEFORE = "soap/header/soap3WithSecurityHeaderInput.xml"; 
    public static final String SOAP_3_AFTER = "soap/header/soap3WithSecurityHeaderOutput.xml";
    
    public static final String SOAP_4_BEFORE = "soap/header/soap4WithSecurityHeaderInput.xml"; 
    public static final String SOAP_4_AFTER = "soap/header/soap4WithSecurityHeaderOutput.xml";
    
    public static final String SOAP_5_BEFORE = "soap/header/soap5WithNoSoapHeaderInput.xml"; 
    public static final String SOAP_5_AFTER = "soap/header/soap5WithNoSoapHeaderOutput.xml";
    
    private SoapMessage soap1;
    private SoapMessage soap2;
    private SoapMessage soap3;
    private SoapMessage soap4;
    private SoapMessage soap5;
    
    private String expected1;
    private String expected2;
    private String expected3;
    private String expected4;
    private String expected5;

    @Autowired
    private SecurityHeaderEndpointInterceptor securityFilter;

    private SoapMessageFactory factory;
    
    public SecurityHeaderTest() {
        SimpleNamingContextBuilder builder;
        try {
            builder = SimpleNamingContextBuilder.emptyActivatedContextBuilder();
            builder.bind("java:/Mail", Session.getInstance(new Properties()));
        } catch (NamingException e) {
            throw new RuntimeException("problem with jndi", e);
        }
    }
    
    @Before
    public void setup() throws IOException{
        SaajSoapMessageFactory fact = new SaajSoapMessageFactory();
        fact.afterPropertiesSet();
        this.factory = fact;
        
        this.soap1 = getSoapMessage(SOAP_1_BEFORE);
        this.expected1 = getExpectedXml(SOAP_1_AFTER);
        
        this.soap2 = getSoapMessage(SOAP_2_BEFORE);
        this.expected2 = getExpectedXml(SOAP_2_AFTER);
        
        this.soap3 = getSoapMessage(SOAP_3_BEFORE);
        this.expected3 = getExpectedXml(SOAP_3_AFTER);
        
        this.soap4 = getSoapMessage(SOAP_4_BEFORE);
        this.expected4 = getExpectedXml(SOAP_4_AFTER);
        
        this.soap5 = getSoapMessage(SOAP_5_BEFORE);
        this.expected5 = getExpectedXml(SOAP_5_AFTER);
    }
    
    private void checkFilter(SoapMessage soapMessage, String expectedXml) throws Exception{
        DefaultMessageContext mc = new DefaultMessageContext(soapMessage, this.factory);
        boolean result = this.securityFilter.handleRequest(mc, null);
        Assert.assertTrue(result);
        String actual = convertSoapMessageToString(soapMessage);
        checkEquals(expectedXml, actual);
    }
    
    private String getExpectedXml(String resourceName) throws IOException {
        Resource res = new ClassPathResource(resourceName);
        String result = IOUtils.toString(res.getInputStream());
        return result;
    }
    private SoapMessage getSoapMessage(String resourceName) throws IOException {
        Resource res = new ClassPathResource(resourceName);
        SoapMessage result = factory.createWebServiceMessage(res.getInputStream());
        return result;
    }
    
    public String convertSoapMessageToString(SoapMessage soap) throws Exception {
        StringWriter sw = new StringWriter();
        StreamResult temp = new StreamResult(sw);
        DOMSource source = new DOMSource(soap.getDocument());
        TransformerFactory tr = TransformerFactory.newInstance();
        Transformer tf = tr.newTransformer();
        tf.transform(source, temp);
        sw.flush();
        String result =  sw.getBuffer().toString();
        return result;
    }
    
    protected void checkEquals(String expectedXml, String actualXml) throws Exception {
        XmlDiff diff = new XmlDiff();
        List<String> diffs = new ArrayList<String>();
        diff.diff(expectedXml, actualXml, diffs);
        String differences = "differences : " + diffs.toString();
        Assert.assertEquals(differences, 0, diffs.size());
    }


    @Test
    public void test1() throws Exception {
        checkFilter(this.soap1, this.expected1);
    }
    
    @Test
    public void test2() throws Exception {
        checkFilter(this.soap2, this.expected2);
    }
    
    @Test
    public void test3() throws Exception {
        checkFilter(this.soap3, this.expected3);
    }
    
    @Test
    public void test4() throws Exception {
        checkFilter(this.soap4, this.expected4);
    }
    
    @Test
    public void test5() throws Exception {
        checkFilter(this.soap5, this.expected5);
    }
}
