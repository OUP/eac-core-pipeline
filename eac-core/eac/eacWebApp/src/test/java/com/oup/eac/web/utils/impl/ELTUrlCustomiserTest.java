package com.oup.eac.web.utils.impl;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import com.oup.eac.web.controllers.helpers.SessionHelper;

public class ELTUrlCustomiserTest {

    private MockHttpServletRequest request;
    private MockHttpSession session;
    private MockHttpServletResponse response;
    private ELTUrlCustomiser customiser;

    @Before
    public void setup() {
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
        session = new MockHttpSession();
        request.setSession(session);
        SessionLocaleResolver localeResolver = new SessionLocaleResolver();
        request.setAttribute(DispatcherServlet.LOCALE_RESOLVER_ATTRIBUTE, localeResolver);
        customiser = new ELTUrlCustomiser();
    }

    @Test
    public void testMergeLocaleParameters1() {
        SessionHelper.setLocale(request, response, Locale.UK);
        String result = mergeInLocaleParameters(request, "http://news.bbc.co.uk");
        Assert.assertEquals("http://news.bbc.co.uk?selLanguage=en&cc=gb", result);
    }

    @Test
    public void testMergeLocaleParameters2() {
        request.addPreferredLocale(Locale.CANADA_FRENCH);
        SessionHelper.setLocale(request, response, null);
        Assert.assertEquals(Locale.CANADA_FRENCH, SessionHelper.getLocale(request));
        String result = mergeInLocaleParameters(request, "http://news.bbc.co.uk");
        Assert.assertEquals("http://news.bbc.co.uk?selLanguage=fr&cc=ca", result);
    }
    
    @Test
    public void testMergeLocaleParameters3() {
        request.addPreferredLocale(Locale.FRENCH);
        SessionHelper.setLocale(request, response, null);
        Assert.assertEquals(Locale.FRENCH, SessionHelper.getLocale(request));
        String result = mergeInLocaleParameters(request, "http://news.bbc.co.uk");
        Assert.assertEquals("http://news.bbc.co.uk?selLanguage=fr&cc=global", result);
    }

    @Test
    public void testMergeLocaleParameters6() {
        request.addPreferredLocale(Locale.CANADA_FRENCH);
        SessionHelper.setLocale(request, response, null);
        Assert.assertEquals(Locale.CANADA_FRENCH, SessionHelper.getLocale(request));
        String result = mergeInLocaleParameters(request, "http://news.bbc.co.uk?a=b&cc=blah1&selLanguage=blah2");
        Assert.assertEquals("http://news.bbc.co.uk?a=b&selLanguage=fr&cc=ca", result);
    }

    private String mergeInLocaleParameters(HttpServletRequest request, String url){
    	return this.customiser.customiseUrl(url, request);
    }
}
