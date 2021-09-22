package com.oup.eac.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.Locale;
import java.util.Properties;

import javax.mail.Session;
import javax.naming.NamingException;

import junit.framework.Assert;

import org.apache.commons.io.IOUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.mock.jndi.SimpleNamingContextBuilder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.oup.eac.common.utils.MessageTextSource;
import com.oup.eac.common.utils.email.VelocityUtils;
import com.oup.eac.domain.Customer;

//@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:/eac/eac*-beans.xml", "classpath*:/eac/test.eac*-beans.xml" })
public class LicenceAcceptedVelocityTest {
	/*
	 * 
	 * @Autowired
	 * 
	 * @Qualifier("velocityEngine") private VelocityEngine ve;
	 * 
	 * @Autowired private MessageSource messageSource;
	 * 
	 * private Resource expected1 = new
	 * ClassPathResource("com/oup/eac/service/impl/licenceAcceptedExpected.txt");
	 * private Resource expected2 = new
	 * ClassPathResource("com/oup/eac/service/impl/licenceAcceptedExpected2.txt");
	 * 
	 * private MessageTextSource resource = null;
	 * 
	 * public LicenceAcceptedVelocityTest() throws NamingException {
	 * SimpleNamingContextBuilder builder =
	 * SimpleNamingContextBuilder.emptyActivatedContextBuilder();
	 * builder.bind("java:/Mail", Session.getInstance(new Properties())); }
	 * 
	 * @Before public void setup(){ this.resource = new
	 * MessageTextSource(this.messageSource, Locale.ENGLISH); }
	 * 
	 * @Test public void testTemplate1() throws Exception {
	 * Assert.assertNotNull(resource);
	 * 
	 * Assert.assertNotNull(ve); Template t =
	 * ve.getTemplate("com/oup/eac/service/velocity/licenceAccepted.vm");
	 * Assert.assertNotNull(t);
	 * 
	 * VelocityContext ctx = VelocityUtils.createVelocityContext();
	 * 
	 * Customer customer = new Customer(); customer.setUsername("jsmith");
	 * 
	 * ctx.put("resource", resource); ctx.put("username", customer.getUsername());
	 * ctx.put("productname", "ProductXYZ"); ctx.put("licenceDescription",
	 * "Start 1 Nov 2012 End 31 Dec 2013"); ctx.put("email",
	 * "product.admin@mailinator.com"); ctx.put("productdivision", "NON-EMAL");
	 * StringWriter writer = new StringWriter(); t.merge(ctx, writer);
	 * 
	 * String result = writer.toString();
	 * 
	 * String expected = getExpected(this.expected1);
	 * 
	 * show the World System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
	 * int maxSize = Math.max(result.length(),expected.length()); for(int i =
	 * 0;i<maxSize;i++){ char c1 = '#'; char c2 = '#'; try{ c1 = expected.charAt(i);
	 * }catch(Exception ex){ c1 ='#'; } try{ c2 = result.charAt(i); }catch(Exception
	 * ex){ c2 ='#'; } System.out.printf("%4d %c(%d) %c(%d)%n",i,c1,(int)c1, c2,
	 * (int)c2); }
	 * 
	 * System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
	 * 
	 * 
	 * System.out.println("------------------------------------");
	 * System.out.println(expected);
	 * System.out.println("------------------------------------");
	 * System.out.println(result);
	 * System.out.println("------------------------------------");
	 * 
	 * //Assert.assertEquals(expected, result); }
	 * 
	 * @Test public void testTemplate2() throws Exception {
	 * Assert.assertNotNull(resource);
	 * 
	 * Assert.assertNotNull(ve); Template t =
	 * ve.getTemplate("com/oup/eac/service/velocity/licenceAccepted.vm");
	 * Assert.assertNotNull(t);
	 * 
	 * VelocityContext ctx = VelocityUtils.createVelocityContext();
	 * 
	 * Customer customer = new Customer(); customer.setUsername("jsmith");
	 * 
	 * ctx.put("resource", resource); ctx.put("username", customer.getUsername());
	 * ctx.put("productname", "ProductXYZ"); ctx.put("licenceDescription",
	 * "Start 1 Nov 2012 End 31 Dec 2013"); ctx.put("productdivision", "NONELT");
	 * StringWriter writer = new StringWriter(); t.merge(ctx, writer);
	 * 
	 * String result = writer.toString();
	 * 
	 * String expected = getExpected(this.expected2);
	 * 
	 * show the World System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
	 * int maxSize = Math.max(result.length(),expected.length()); for(int i =
	 * 0;i<maxSize;i++){ char c1 = '#'; char c2 = '#'; try{ c1 = expected.charAt(i);
	 * }catch(Exception ex){ c1 ='#'; } try{ c2 = result.charAt(i); }catch(Exception
	 * ex){ c2 ='#'; } System.out.printf("%4d %c(%d) %c(%d)%n",i,c1,(int)c1, c2,
	 * (int)c2); }
	 * 
	 * System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
	 * 
	 * 
	 * System.out.println("------------------------------------");
	 * System.out.println(expected);
	 * System.out.println("------------------------------------");
	 * System.out.println(result);
	 * System.out.println("------------------------------------");
	 * 
	 * Assert.assertEquals(expected, result); }
	 * 
	 * public String getExpected(Resource expected) { try { InputStream is =
	 * expected.getInputStream(); String result = IOUtils.toString(is); return
	 * result.trim(); } catch (IOException ex) { throw new RuntimeException(ex); } }
	 */}
