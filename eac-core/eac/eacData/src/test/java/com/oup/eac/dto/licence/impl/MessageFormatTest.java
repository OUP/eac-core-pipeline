package com.oup.eac.dto.licence.impl;

import java.text.MessageFormat;
import java.util.Locale;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Used to test cloning of MessageFormat.
 * This technique is used in synyxMessageSource project which is used to create the 3rd part jar - messagesource-0.6.1-OUP-3.jar. 
 * 
 * @author David Hay
 *
 */
public class MessageFormatTest {

    private MessageFormat mf;
    private Object[] params;

    @Before
    public void setup() {
        mf = new MessageFormat("before {0} after");
        params = new Object[] { 1234.56d };
    }

    @Test
    public void testLocales() {
        testLocale("before 1,234.56 after", Locale.UK);
        testLocale("before 1.234,56 after", Locale.GERMANY);
        testLocale("before 1 234,56 after", Locale.FRANCE);
    }

    private void testLocale(String expected, Locale locale) {
        MessageFormat temp = (MessageFormat) mf.clone();
        temp.setLocale(locale);
        String result = temp.format(params);
        Assert.assertEquals(expected, result);
    }

}
