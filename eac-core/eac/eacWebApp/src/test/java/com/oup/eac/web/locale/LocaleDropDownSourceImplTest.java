package com.oup.eac.web.locale;

import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * @author David Hay
 */
public class LocaleDropDownSourceImplTest {

    private LocaleDropDownSourceImpl source;

    @Before
    public void setup() {
        source = new LocaleDropDownSourceImpl();
    }

    @Test
    public void testHappyFrance() {
        Map<String, String> res = source.getLocaleDropDown(Locale.FRANCE, Locale.UK);
        String value1 = res.get("en_GB");
        Assert.assertEquals("anglais (Royaume-Uni)", value1);

        String value2 = res.get("fr_FR");
        Assert.assertEquals("fran\u00E7ais (France)", value2);
    }

    @Test
    public void testHappyEnglish() {
        Map<String, String> res = source.getLocaleDropDown(Locale.ENGLISH, Locale.UK);
        String value1 = res.get("en_GB");
        Assert.assertEquals("English (United Kingdom)", value1);

        String value2 = res.get("fr_FR");
        Assert.assertEquals("French (France)", value2);
    }
    
    @Test
    public void testNonStandardLocale() {
        Locale nonStandard = new Locale("en", "kr");
        Map<String, String> res = source.getLocaleDropDown(nonStandard, Locale.UK);
        String value1 = res.get("en_KR");
        Assert.assertEquals("English (South Korea)", value1);
    }
    
    @Test
    public void testNullUserLocale() {
        Locale nullLocale = null;
        Map<String, String> res = source.getLocaleDropDown(Locale.UK, nullLocale);
        int expected = SimpleDateFormat.getAvailableLocales().length;
        Assert.assertEquals(expected, res.size());
    }
    
    
}
