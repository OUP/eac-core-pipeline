package com.oup.eac.ws.endpoint;

import java.util.List;
import java.util.Properties;

import javax.mail.Session;
import javax.naming.NamingException;

import junit.framework.Assert;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.spi.LoggingEvent;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.jndi.SimpleNamingContextBuilder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.ws.context.DefaultMessageContext;
import org.springframework.ws.context.MessageContext;
import org.springframework.ws.soap.saaj.SaajSoapMessageFactory;
import org.springframework.ws.soap.server.endpoint.SimpleSoapExceptionResolver;

import com.oup.eac.domain.utils.audit.TestingAppender;

/**
 * Tests that when the SimpleSoapExceptionResolver resolves an exception, that it's logged correctly.
 * SimpleSoapExceptionResolver resolves exceptions that are thrown by our own spring-ws interceptors.
 * @see com.oup.eac.ws.endpoint.EacExceptionResolverConfigurer 
 * @author David Hay
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
        "classpath*:/eac-web-services-servlet.xml",
        "classpath*:/eac/test.eac*-beans.xml" })
public class EacExceptionResolverConfigurerTest {

    @Autowired
    private SimpleSoapExceptionResolver resolver;
    private TestingAppender appender;
    private Logger log;
    private Exception ex;
    
    public EacExceptionResolverConfigurerTest() throws NamingException {
        SimpleNamingContextBuilder builder = SimpleNamingContextBuilder.emptyActivatedContextBuilder();
        builder.bind("java:/Mail", Session.getInstance(new Properties()));
    }
    
    @Before
    public void setup(){
        appender = new TestingAppender();
        log = Logger.getLogger(resolver.getClass());
        log.addAppender(appender);
        ex = new RuntimeException();
    }

    @Test
    public void test(){
        SaajSoapMessageFactory mf = new SaajSoapMessageFactory();
        mf.afterPropertiesSet();
        MessageContext mc = new DefaultMessageContext(mf);
        resolver.resolveException(mc, "An endpoint", ex);
        List<LoggingEvent> messages = appender.getMessages();
        Assert.assertEquals(2,messages.size());
        LoggingEvent le1 = messages.get(0);
        LoggingEvent le2 = messages.get(1);
        Assert.assertEquals("Resolving exception from endpoint [An endpoint]: java.lang.RuntimeException",le1.getRenderedMessage());
        Assert.assertEquals("Endpoint execution resulted in exception",le2.getRenderedMessage());
        Assert.assertEquals(Level.DEBUG,le1.getLevel());
        Assert.assertEquals(Level.WARN,le2.getLevel());
        Assert.assertNull(le1.getThrowableInformation());
        Assert.assertEquals(this.ex,le2.getThrowableInformation().getThrowable());
    }
    
    @After
    public void tearDown(){
        log.removeAppender(appender);
    }
    

}

