package com.oup.eac.service.impl;

import java.io.StringWriter;
import java.util.Properties;

import javax.mail.Session;
import javax.naming.NamingException;

import junit.framework.Assert;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.mock.jndi.SimpleNamingContextBuilder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.oup.eac.common.utils.email.VelocityUtils;

//@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:/eac/eac*-beans.xml", "classpath*:/eac/test.eac*-beans.xml" })
public class VelocityUtilsTest {
	/*
	 * 
	 * @Autowired
	 * 
	 * @Qualifier("velocityEngine") private VelocityEngine ve;
	 * 
	 * 
	 * public VelocityUtilsTest() throws NamingException {
	 * SimpleNamingContextBuilder builder =
	 * SimpleNamingContextBuilder.emptyActivatedContextBuilder();
	 * builder.bind("java:/Mail", Session.getInstance(new Properties())); }
	 * 
	 * @Before public void setup() throws ClassNotFoundException{ }
	 * 
	 * @Test public void testTemplate() throws Exception { Assert.assertNotNull(ve);
	 * VelocityContext ctx = VelocityUtils.createVelocityContext(); StringWriter sw2
	 * = new StringWriter();
	 * 
	 * Template t =
	 * ve.getTemplate("com/oup/eac/service/velocity/velocityUtilsTest.vm");
	 * 
	 * t.merge(ctx, sw2);
	 * 
	 * String result = sw2.toString();
	 * 
	 * String expected = String.format("%nline2%n%nline4");
	 * 
	 * show the World System.out.println(result);
	 * 
	 * Assert.assertEquals(expected, result); }
	 * 
	 * 
	 */}
