package com.oup.eac.ws.v2.service.util;

import java.util.Locale;

import org.junit.Assert;
import org.junit.Test;

import com.oup.eac.ws.v2.binding.common.LocaleType;
import com.oup.eac.ws.v2.service.utils.LocaleUtils;

public class LocaleUtilsTest {

    @Test
    public void checkBehaviour() {
        Locale loc = Locale.FRENCH;
        Assert.assertEquals("fr", loc.getLanguage());
        Assert.assertEquals("", loc.getCountry());
        Assert.assertEquals("", loc.getVariant());

        Locale loc2 = Locale.FRANCE;
        Assert.assertEquals("fr", loc2.getLanguage());
        Assert.assertEquals("FR", loc2.getCountry());
        Assert.assertEquals("", loc2.getVariant());
    }

    @Test
    public void testFromWsLocaleType1() {

        LocaleType localeInfo = new LocaleType();
        localeInfo.setLanguage("en");
        localeInfo.setCountry("GB");
        localeInfo.setVariant("ESSEX");
        Locale result = LocaleUtils.getLocale(localeInfo);

        Assert.assertEquals("en", result.getLanguage());
        Assert.assertEquals("GB", result.getCountry());
        Assert.assertEquals("ESSEX", result.getVariant());
    }

    @Test
    public void testFromWsLocaleType2() {

        LocaleType localeInfo = new LocaleType();
        localeInfo.setLanguage("en");
        localeInfo.setCountry("GB");
        localeInfo.setVariant(null);
        Locale result = LocaleUtils.getLocale(localeInfo);

        Assert.assertEquals("en", result.getLanguage());
        Assert.assertEquals("GB", result.getCountry());
        Assert.assertEquals("", result.getVariant());

    }

    @Test
    public void testFromWsLocaleType3() {

        LocaleType localeInfo = new LocaleType();
        localeInfo.setLanguage("en");
        localeInfo.setCountry("GB");
        localeInfo.setVariant("");
        Locale result = LocaleUtils.getLocale(localeInfo);

        Assert.assertEquals("en", result.getLanguage());
        Assert.assertEquals("GB", result.getCountry());
        Assert.assertEquals("", result.getVariant());

    }

    @Test
    public void testFromWsLocaleType4() {

        LocaleType localeInfo = new LocaleType();
        localeInfo.setLanguage("en");
        Locale result = LocaleUtils.getLocale(localeInfo);

        Assert.assertEquals("en", result.getLanguage());
        Assert.assertEquals("", result.getCountry());
        Assert.assertEquals("", result.getVariant());

    }

    @Test
    public void testFromLocale1() {
        Locale loc = Locale.FRENCH;
        LocaleType result = LocaleUtils.getLocaleType(loc);
        Assert.assertEquals("fr", result.getLanguage());
        Assert.assertEquals(null, result.getCountry());
        Assert.assertEquals(null, result.getVariant());
    }
    
    @Test
    public void testFromLocale2() {
        Locale loc = Locale.FRANCE;
        LocaleType result = LocaleUtils.getLocaleType(loc);
        Assert.assertEquals("fr", result.getLanguage());
        Assert.assertEquals("FR", result.getCountry());
        Assert.assertEquals(null, result.getVariant());
    }
    
    @Test
    public void testFromLocale3() {
        Locale loc = new Locale("en","GB","ESSEX");
        LocaleType result = LocaleUtils.getLocaleType(loc);
        Assert.assertEquals("en", result.getLanguage());
        Assert.assertEquals("GB", result.getCountry());
        Assert.assertEquals("ESSEX", result.getVariant());
    }


}
