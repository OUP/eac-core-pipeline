package com.oup.eac.ws;

import java.io.InputStream;
import java.io.StringWriter;
import java.util.Properties;

import javax.mail.Session;
import javax.naming.NamingException;
import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import junit.framework.Assert;
import net.javacrumbs.springws.test.helper.MessageValidator;
import net.javacrumbs.springws.test.helper.WsTestHelper;
import net.javacrumbs.springws.test.util.XmlUtil;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.io.Resource;
import org.springframework.mock.jndi.SimpleNamingContextBuilder;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.ws.WebServiceMessage;
import org.springframework.ws.context.MessageContext;
import org.springframework.ws.soap.SoapFault;
import org.springframework.ws.soap.security.wss4j.Wss4jSecurityInterceptor;
import org.w3c.dom.Document;

/**
 * This test checks that the security configuration for the WebServiceApp is
 * working as expected.
 * 
 * During the test, the adminService which is usually used to authenticate users
 * is replaced with a test-only DummyUserService. The security config being
 * tested is otherwise the same as used by eacWebService application.
 * 
 * @author David Hay
 * @see com.oup.eac.data.DummyUser
 * @see com.oup.eac.ws.v2.service.impl.DummyUserDetailServiceImpl
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:/eac-web-services-servlet.xml", "classpath*:/eac/test.eac*-beans.xml" })
public abstract class AbstractWebServiceAuthenticationTest implements ApplicationListener<ApplicationEvent> {

    private static ThreadLocal<UserDetails> TLOCAL = new ThreadLocal<UserDetails>();

    public void onApplicationEvent(ApplicationEvent appEvent) {
        if (appEvent instanceof AuthenticationSuccessEvent) {
            AuthenticationSuccessEvent event = (AuthenticationSuccessEvent) appEvent;
            UserDetails userDetails = (UserDetails) event.getAuthentication().getPrincipal();
            TLOCAL.set(userDetails);
        }
    }

    protected static final String BAD_PASSWORD = "badPassword";

    private static final Logger LOG = Logger.getLogger(AbstractWebServiceAuthenticationTest.class);

    private static final String AUTH_FAILED_MESSAGE = "Authentication of Username Password Token Failed";

    protected static final String ADMIN_USERNAME = "admin";
    protected static final String ADMIN_PASSWORD = "Passw0rd";

    protected static final String EACUSER_USERNAME = "eacuser";
    protected static final String EACUSER_PASSWORD = "P45$w0r6";

    @Autowired
    @Qualifier("secHelper")
    private WsTestHelper helper;

    @Autowired
    @Qualifier("secInterceptor")
    private Wss4jSecurityInterceptor interceptor;

    public AbstractWebServiceAuthenticationTest() {
        SimpleNamingContextBuilder builder;
        try {
            builder = SimpleNamingContextBuilder.emptyActivatedContextBuilder();
            builder.bind("java:/Mail", Session.getInstance(new Properties()));
        } catch (NamingException e) {
            throw new RuntimeException("problem with jndi", e);
        }
    }

    /**
     * This method simulates a web service call into the WebServiceApp. Logs the
     * XML response if debugging is enabled. If there's a WsTextException - make
     * sure it's because of authentication failure. If there's no exception -
     * compare the actual response to the expected response.
     * 
     * @param username
     *            - the username to use
     * @param password
     *            - the password to use
     * @throws Exception
     *             if there is a problem making the web service request
     */
    protected void makeRequest(boolean expectedToAuthenticate, String username, String password, Resource request,
            Resource response) throws Exception {

        String expectedUsername = username;
        boolean authenticatedActual = false;

        interceptor.setSecurementUsername(username);
        interceptor.setSecurementPassword(password);
        System.out.println("hello");
        WebServiceMessage resp = null;
        try {
            // simulates request coming to MessageDispatcherServlet
            MessageContext message = helper.receiveMessage(request);
            // assert that response is not fault and the message is as expected

            resp = message.getResponse();
            if (LOG.isDebugEnabled()) {
                XmlUtil util = helper.getXmlUtil();
                String xml = util.serializeDocument(resp);
                LOG.debug(xml);
            }

            if (isFaultExpectedBeforeAuthentication()) {
                MessageValidator validator = helper.createMessageValidator(resp).assertSoapFault();
                checkExpectedFailure(validator.getSoapFault(), response);
            } else {
                MessageValidator validator = helper.createMessageValidator(resp);
                if (expectedToAuthenticate) {

                    checkLoggedInUser(expectedUsername);
                    authenticatedActual = true;

                    if (isFaultExpectedAfterAuthentication()) {
                        validator.assertSoapFault();
                        checkExpectedFailure(validator.getSoapFault(), response);
                    } else {
                        validator.assertNotSoapFault().compare(response);
                    }
                } else {
                    // authentication error
                    validator.assertSoapFault();
                    checkAuthenticationFailure(validator.getSoapFault());
                }
            }
        } finally {
            checkAuthenticated(expectedToAuthenticate, authenticatedActual);
        }
    }

    private void checkExpectedFailure(SoapFault soapFault, Resource response) throws Exception {
        Document faultDoc = getDocumentFromResponse(response);
        XPath xpath = XPathFactory.newInstance().newXPath();
        XPathExpression faultCodeExpr = xpath.compile("//faultcode[1]");
        XPathExpression faultStringExpr = xpath.compile("//faultstring[1]");
        String faultCode = (String) faultCodeExpr.evaluate(faultDoc, XPathConstants.STRING);
        String faultString = (String) faultStringExpr.evaluate(faultDoc, XPathConstants.STRING);
        QName sfFaultCode = soapFault.getFaultCode();
        String sfReason = soapFault.getFaultStringOrReason();
        Assert.assertEquals(faultString, sfReason);
        Assert.assertEquals(faultCode, sfFaultCode.getPrefix() + ":" + sfFaultCode.getLocalPart());
    }

    private void checkAuthenticationFailure(SoapFault soapFault) throws Exception {
        Source faultSource = soapFault.getSource();
        StringWriter sw = new StringWriter();
        StreamResult streamResult = new StreamResult(sw);
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer serializer = tf.newTransformer();
        serializer.transform(faultSource, streamResult);
        sw.flush();
        sw.close();
        String responseText = sw.toString();
        boolean isAuthFailed = responseText.contains(AUTH_FAILED_MESSAGE);
        if (!isAuthFailed) {
            Assert.fail("was expected authentication faiulure");
        }
    }

    private void checkLoggedInUser(String expectedUsername) {
        Component ans = this.getClass().getAnnotation(Component.class);
        if (ans != null) {
            UserDetails ud = TLOCAL.get();
            Assert.assertEquals(expectedUsername, ud.getUsername());
        } else {
            LOG.warn("The unit test should be marked with @Component");
        }
    }

    protected boolean isFaultExpectedBeforeAuthentication() {
        return false;
    }

    protected boolean isFaultExpectedAfterAuthentication() {
        return false;
    }

    private void makeRequest(boolean okay, String username, String password) throws Exception {
        boolean before = isFaultExpectedBeforeAuthentication();
        boolean after = isFaultExpectedAfterAuthentication();
        if (before && after) {
            throw new IllegalArgumentException(
                    "Cannot have both 'isFaultExpectedBforeAuthenrication' and 'isFaultExpectedAfterAuthentication' as true");
        }
        makeRequest(okay, username, password, getRequest(), getExpectedResponse());
    }

    private final void checkAuthenticated(boolean authExpected, boolean authActual) {
        if (isFaultExpectedBeforeAuthentication()) {
            Assert.assertFalse(authActual);
        } else if (isFaultExpectedAfterAuthentication()) {
            Assert.assertEquals(authExpected, authActual);
        } else {
            Assert.assertEquals(authExpected, authActual);
        }
    }

    /**
     * Checks we can pass authentication with admins's credentials.
     * 
     * @throws Exception
     *             if there's problem
     */
    @Test
    public void testAdminAuthenticationSucess() throws Exception {
        makeRequest(true, ADMIN_USERNAME, ADMIN_PASSWORD);
    }

    /**
     * Checks we can't pass authentication with eacuser/invalid password
     * combination.
     * 
     * @throws Exception
     *             - if there's problem
     */
    @Test
    public void testAdminAuthenticationFailure() throws Exception {
        makeRequest(false, ADMIN_USERNAME, BAD_PASSWORD);
    }

    /**
     * Checks we can pass authentication with eacuser's credentials.
     * 
     * @throws Exception
     *             - if there's problem.
     */
    @Test
    public void testEacuserAuthenticationSucess() throws Exception {
        makeRequest(true, EACUSER_USERNAME, EACUSER_PASSWORD);
    }

    /**
     * Checks we can't pass authentication with eacuser/invalid password
     * combination.
     * 
     * @throws Exception
     *             - if there's problem.
     */
    @Test
    public void testAuthenticationeacuserFailure() throws Exception {
        makeRequest(false, EACUSER_USERNAME, BAD_PASSWORD);
    }

    protected abstract Resource getExpectedResponse();

    protected abstract Resource getRequest();

    public Document getDocumentFromResponse(Resource response) throws Exception {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        InputStream is = response.getInputStream();
        Document result = db.parse(is);
        return result;
    }

}