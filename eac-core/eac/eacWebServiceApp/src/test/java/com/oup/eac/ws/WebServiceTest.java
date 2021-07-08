package com.oup.eac.ws;

import java.util.Properties;

import javax.mail.Session;
import javax.naming.NamingException;

import org.junit.Test;
import org.springframework.mock.jndi.SimpleNamingContextBuilder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

@ContextConfiguration(locations = {"classpath*:/eac/eac*-beans.xml", "classpath*:/eac/test.eac*-beans.xml", "classpath*:/eac/web.eac*-beans.xml", "classpath*:/eac/web.eac-servlet.xml"})
public class WebServiceTest extends AbstractJUnit4SpringContextTests {

    public WebServiceTest() throws NamingException {
        SimpleNamingContextBuilder builder = SimpleNamingContextBuilder.emptyActivatedContextBuilder();
        builder.bind("java:/Mail", Session.getInstance(new Properties()));
    }
    
    @Test
    public void testAccess() {
        
    }
}
