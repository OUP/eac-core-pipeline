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
public class PasswordResetVelocityTest {

    @Autowired
    @Qualifier("velocityEngine")
    private VelocityEngine ve;
    
    @Autowired
    private MessageSource messageSource;
    
    private Resource expected = new ClassPathResource("com/oup/eac/service/impl/passwordResetExpected.txt");

    private MessageTextSource resource = null;

    public PasswordResetVelocityTest() throws NamingException {
        SimpleNamingContextBuilder builder = SimpleNamingContextBuilder.emptyActivatedContextBuilder();
        builder.bind("java:/Mail", Session.getInstance(new Properties()));
    }
    
    @Before
    public void setup(){
        this.resource = new MessageTextSource(this.messageSource, Locale.ENGLISH);
    }

    @Test
    public void testValidatedRegistrationTemplate() throws Exception {
        Assert.assertNotNull(resource);

        Assert.assertNotNull(ve);
        Template t = ve.getTemplate("com/oup/eac/service/velocity/passwordReset.vm");
        Assert.assertNotNull(t);

        VelocityContext ctx = VelocityUtils.createVelocityContext();

        Customer customer = new Customer();
        customer.setUsername("jsmith");
        
        ctx.put("resource", resource);
        ctx.put("username", customer.getUsername());        
        ctx.put("password", "<NEW PASSWORD>");

        StringWriter writer = new StringWriter();
        t.merge(ctx, writer);

        String result = writer.toString();
        
        String expected = getExpected();
        
        /* show the World */
        System.out.println(result);
        
        Assert.assertEquals(expected, result);
    }

    
    public String getExpected() {
        try {
            InputStream is = this.expected.getInputStream();
            String result = IOUtils.toString(is);
            return result.trim();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
}
