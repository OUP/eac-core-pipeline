package com.oup.eac.ws.util;

import java.util.Properties;

import javax.mail.Session;
import javax.naming.NamingException;

import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.mock.jndi.SimpleNamingContextBuilder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.oup.eac.domain.utils.audit.EacApp;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:/eac-web-services-servlet.xml", "classpath*:/eac/test.eac*-beans.xml" })
public class EacAppTest {

    private Logger LOG = Logger.getLogger(EacAppTest.class);
            
	public EacAppTest() {
		SimpleNamingContextBuilder builder;
		try {
			builder = SimpleNamingContextBuilder.emptyActivatedContextBuilder();
			builder.bind("java:/Mail", Session.getInstance(new Properties()));
		} catch (NamingException e) {
			throw new RuntimeException("problem with jndi", e);
		}
	}
	
    @Before
    public void setup() {
        LOG.info("EacAppTest::"+EacApp.getType());
    }

    @Test
    public void testEacApp() {
        boolean isWebServices = EacApp.isWebServices();
        Assert.assertTrue(isWebServices);
    }

    @Test
	public void testEacAppUsingSpel() {
	    ExpressionParser parser = new SpelExpressionParser();
	    Expression exp = parser.parseExpression("T(com.oup.eac.domain.utils.audit.EacApp).isWebServices()");
	    Boolean isWebServices = (Boolean) exp.getValue();
	    Assert.assertTrue(isWebServices);
	}
    
    @Test
    public void testEacAppIsNotAdmin() {
        boolean isAdmin = EacApp.isAdmin();
        Assert.assertFalse(isAdmin);
    }

    @Test
    public void testEacAppIsNotAdminUsingSpel() {
        ExpressionParser parser = new SpelExpressionParser();
        Expression exp = parser.parseExpression("T(com.oup.eac.domain.utils.audit.EacApp).isAdmin()");
        Boolean isAdmin = (Boolean) exp.getValue();
        Assert.assertFalse(isAdmin);
    }
}
