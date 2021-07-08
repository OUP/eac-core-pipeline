package com.oup.eac.data.message;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.Properties;

import javax.mail.Session;
import javax.naming.NamingException;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.mock.jndi.SimpleNamingContextBuilder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:/eac/eac*-beans.xml", "classpath*:/eac/test.eac*-beans.xml" })
public class MessageSourceBeanWiringTest {

    @Autowired(required=true)
    @Qualifier("messageSource")
    private MessageSource messageSource;

    @Autowired    
    private DbMessageSourceBeanPostProcessor dbMessageSourceBeanPostProcessor;
    
    public MessageSourceBeanWiringTest() throws NamingException {
    	SimpleNamingContextBuilder builder = SimpleNamingContextBuilder.emptyActivatedContextBuilder();
        builder.bind("java:/Mail", Session.getInstance(new Properties()));
    }
    
    @Test
    public void testMessageSource(){
        Assert.assertNotNull(this.messageSource);
    }
    
    @Test
    public void testBeanPostProcessor(){
        Assert.assertNotNull(dbMessageSourceBeanPostProcessor);
    }
    
    @Test
    public void testMessageWithApostrophe(){
        String message1 = messageSource.getMessage("label.registration.tandc", null, Locale.getDefault());
        System.out.printf("The message is [%s]%n",message1);
        Assert.assertEquals("I have read and agreed to Oxford University Press's Terms and Conditions for Account Creation and Privacy Policy",message1);
        
        String message2 = messageSource.getMessage("label.concurrencyexceeded1", null, Locale.getDefault());
        System.out.printf("The message is [%s]%n",message2);
        Assert.assertEquals("We're sorry. You have reached the maximum number of users who are permitted to access these resources simultaneously using this account. Please try again later.",message2);
        
        String message3 = messageSource.getMessage("label.noactivelicence", null, Locale.getDefault());
        System.out.printf("The message is [%s]%n",message3);
        Assert.assertEquals("We're sorry. You no longer have access to these resources. If you wish to continue to use this web site you will need to",message3);
        
    }
    
    @Test
    public void testMessageFormat(){
        MessageFormat fmt = new MessageFormat("Press''s",Locale.getDefault());
        String result = fmt.format(new Object[0]);
        System.out.println(result);
    }
}
