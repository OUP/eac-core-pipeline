package com.oup.eac.data.message;

import java.util.Locale;
import java.util.Properties;

import javax.mail.Session;

import junit.framework.Assert;

import org.springframework.context.MessageSource;
import org.springframework.mock.jndi.SimpleNamingContextBuilder;

/**
 * Base class for testing the periodic reloading of localized messages from a database.
 * 
 * @author David Hay
 *
 */
public class BaseMessageSourceTest {

    private static final String ERROR_CODE = "error.msg1";
    
    public BaseMessageSourceTest() {
        try {
            SimpleNamingContextBuilder builder = SimpleNamingContextBuilder.emptyActivatedContextBuilder();
            builder.bind("java:/Mail", Session.getInstance(new Properties()));
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail("unexpected exception ");
        }
    }

    public void checkMessageSource(MessageSource msgSource){
        Locale eng = new Locale("en");
        
        Assert.assertEquals(new Locale("fr","CA"),Locale.CANADA_FRENCH);
        
        Assert.assertEquals("en",eng.getLanguage());
        
        check(msgSource, new Locale("en"),"English");
        check(msgSource, new Locale("en","GB"),"English","GB");
        
        check(msgSource, new Locale("en","US"),"English","US");
        check(msgSource, new Locale("es"),"Spanish");
        check(msgSource, new Locale("es","ES"),"Spanish","Spain");
        check(msgSource, new Locale("es","US"),"Spanish","US");
    }
    
    private void check(MessageSource msgSource, Locale loc, String language, String country){
        String actual = msgSource.getMessage(ERROR_CODE, new Object[0], loc);
        String expected = getExpected(language, country);
        Assert.assertEquals(expected, actual);
    }
    private void check(MessageSource msgSource,Locale loc, String language){
        check(msgSource,loc,language,null);
    }
    
    public String getExpected(String language, String country){
        StringBuffer sb = new StringBuffer("error message for ");
        sb.append(language);
        if(country != null){
            sb.append(" ");
            sb.append(country);
        }
        String result = sb.toString();
        return result;
    }
}
