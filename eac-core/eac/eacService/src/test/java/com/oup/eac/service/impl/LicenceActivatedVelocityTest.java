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

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:/eac/eac*-beans.xml", "classpath*:/eac/test.eac*-beans.xml" })
public class LicenceActivatedVelocityTest {

    @Autowired
    @Qualifier("velocityEngine")
    private VelocityEngine ve;
    
    @Autowired
    private MessageSource messageSource;
    
    private Resource expectedWithDescription = new ClassPathResource("com/oup/eac/service/impl/licenceActivatedExpected.txt");
    private Resource expectedWithoutDescription = new ClassPathResource("com/oup/eac/service/impl/licenceActivatedExpectedNoDescription.txt");

    private MessageTextSource resource = null;

    public LicenceActivatedVelocityTest() throws NamingException {
        SimpleNamingContextBuilder builder = SimpleNamingContextBuilder.emptyActivatedContextBuilder();
        builder.bind("java:/Mail", Session.getInstance(new Properties()));
    }
    
    @Before
    public void setup(){
        this.resource = new MessageTextSource(this.messageSource, Locale.ENGLISH);
    }

    @Test
    public void testValidatedRegistrationTemplateWithDescription() throws Exception {
        String description = "Start 21 Dec 2112";        
        String expected = getExpectedWithDescription();
        checkValidatedRegistrationTemplate(description, expected);
    }
    
    @Test
    public void testValidatedRegistrationTemplateWithoutDescription() throws Exception {
        String description = "";        
        String expected = getExpectedWithoutDescription();
        checkValidatedRegistrationTemplate(description, expected);
    }

    private void checkValidatedRegistrationTemplate(String description, String expected) throws Exception {
        Assert.assertNotNull(resource);

        Assert.assertNotNull(ve);
        Template t = ve.getTemplate("com/oup/eac/service/velocity/licenceActivated.vm");
        Assert.assertNotNull(t);

        VelocityContext ctx = VelocityUtils.createVelocityContext();

        Customer customer = new Customer();
        customer.setUsername("jsmith");
        
        ctx.put("resource", resource);
        ctx.put("username", customer.getUsername());
        ctx.put("productname", "ProductXYZ");
        ctx.put("licenceDescription", description);        
        

        StringWriter writer = new StringWriter();
        t.merge(ctx, writer);

        String result = writer.toString();
        
        /* show the World */
        System.out.println(result);
        
        Assert.assertEquals(expected, result);
    }
    
    public String getExpectedWithDescription() {
        try {
            InputStream is = this.expectedWithDescription.getInputStream();
            String result = IOUtils.toString(is);
            return result.trim();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
    
    public String getExpectedWithoutDescription() {
        try {
            InputStream is = this.expectedWithoutDescription.getInputStream();
            String result = IOUtils.toString(is);
            return result.trim();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
}
