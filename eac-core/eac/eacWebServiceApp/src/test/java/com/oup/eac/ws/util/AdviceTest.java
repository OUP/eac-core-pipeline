package com.oup.eac.ws.util;

import java.util.HashMap;
import java.util.Properties;

import javax.mail.Session;
import javax.naming.NamingException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.mock.jndi.SimpleNamingContextBuilder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.sun.xml.wss.XWSSecurityException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:/eac-web-services-servlet.xml", "classpath*:/eac/test.eac*-beans.xml" })
public class AdviceTest {

	public AdviceTest() {
		SimpleNamingContextBuilder builder;
		try {
			builder = SimpleNamingContextBuilder.emptyActivatedContextBuilder();
			builder.bind("java:/Mail", Session.getInstance(new Properties()));
		} catch (NamingException e) {
			throw new RuntimeException("problem with jndi", e);
		}
	}

	@Test(expected=com.sun.xml.wss.XWSSecurityException.class)
	public void testOne() throws XWSSecurityException {
		com.sun.xml.wss.impl.misc.DefaultSecurityEnvironmentImpl o = new com.sun.xml.wss.impl.misc.DefaultSecurityEnvironmentImpl(null);
		HashMap<String,String> map = new HashMap<String,String>();
		map.put("hello","world");
		o.authenticateUser(map, "username1", "password1");
	}

}
