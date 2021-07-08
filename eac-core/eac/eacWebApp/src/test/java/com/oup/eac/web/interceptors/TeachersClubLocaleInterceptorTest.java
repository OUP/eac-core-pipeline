package com.oup.eac.web.interceptors;

import java.util.Arrays;
import java.util.Locale;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;
import org.springframework.web.servlet.support.RequestContextUtils;

import com.oup.eac.web.controllers.helpers.SessionHelper;

public class TeachersClubLocaleInterceptorTest {

    private static final Logger LOG = Logger.getLogger(TeachersClubLocaleInterceptorTest.class);
    
    private TeachersClubLocaleInterceptor sut;
    private Object handler = null;
    private MockHttpServletRequest request;
    private MockHttpServletResponse response;
    private SessionLocaleResolver resolver;
    private MockHttpSession session;

    @Before
    public void setup() {
        sut = new TeachersClubLocaleInterceptor();
        handler = null;
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
        session = new MockHttpSession();
        request.setSession(session);
        resolver = new SessionLocaleResolver();
        request.setAttribute(DispatcherServlet.LOCALE_RESOLVER_ATTRIBUTE, resolver);
        Assert.assertEquals(resolver, RequestContextUtils.getLocaleResolver(request));
        request.setMethod("GET");
        SessionHelper.setLocale(request, response, Locale.FRANCE);
        Assert.assertEquals(Locale.FRANCE, SessionHelper.getLocale(request));
        LOG.debug("STARTING");
    }

    @Test
    public void testHappyPath1() throws Exception {                
        setRequestURI(request, "EN", "gb");
        boolean result = sut.preHandle(request, response, handler);
        Assert.assertTrue(result);
        checkLocale(new Locale("en", "GB"), SessionHelper.getLocale(request));
    }
    
    
    @Test
    public void testPost1() throws Exception {
        request.setMethod("POST");
        setRequestURI(request, "EN", "gb");
        boolean result = sut.preHandle(request, response, handler);
        Assert.assertTrue(result);
        checkLocale(new Locale("fr", "FR"), SessionHelper.getLocale(request));
    }

    @Test
    public void testHappyPath2() throws Exception {                
        setRequestURI(request, "EN", null);
        boolean result = sut.preHandle(request, response, handler);
        Assert.assertTrue(result);
        checkLocale(new Locale("en"), SessionHelper.getLocale(request));    
    }
    
    @Test
    public void testHappyPath3() throws Exception {                
        setRequestURI(request, "ES", null);
        boolean result = sut.preHandle(request, response, handler);
        Assert.assertTrue(result);
        checkLocale(new Locale("es"), SessionHelper.getLocale(request));    
    }
    
    @Test
    public void testHappyPath4() throws Exception {                
        setRequestURI(request, "ES", "ES");
        boolean result = sut.preHandle(request, response, handler);
        Assert.assertTrue(result);
        checkLocale(new Locale("es", "ES"), SessionHelper.getLocale(request));    
    }
    
    @Test
    public void testHappyPath5() throws Exception {                
        setRequestURI(request, "ES", "global");
        boolean result = sut.preHandle(request, response, handler);
        Assert.assertTrue(result);
        checkLocale(new Locale("es"), SessionHelper.getLocale(request));    
    }
    
    @Test
    public void testHappyPath5B() throws Exception {                
        setRequestURI(request, "ES", "GLOBAL");
        boolean result = sut.preHandle(request, response, handler);
        Assert.assertTrue(result);
        checkLocale(new Locale("es"), SessionHelper.getLocale(request));    
    }
    
    @Test
    public void testHappyPath6() throws Exception {                
        setRequestURI(request, null, null);
        boolean result = sut.preHandle(request, response, handler);
        Assert.assertTrue(result);
        checkLocale(new Locale("fr", "FR"), SessionHelper.getLocale(request));    
    }
    
    @Test
    public void testInvalid1() throws Exception {                
        setRequestURI(request, "ABC", "DEF");
        boolean result = sut.preHandle(request, response, handler);
        Assert.assertTrue(result);
        checkLocale(new Locale("fr", "FR"), SessionHelper.getLocale(request));    
    }
    
    @Test
    public void testInvalid2() throws Exception {                
        setRequestURI(request, "en", "DEF");
        boolean result = sut.preHandle(request, response, handler);
        Assert.assertTrue(result);
        checkLocale(new Locale("en"), SessionHelper.getLocale(request));    
    }

    @Test
    public void testInvalid3() throws Exception {                
        setRequestURI(request, "..", "..");
        boolean result = sut.preHandle(request, response, handler);
        Assert.assertTrue(result);
        checkLocale(new Locale("fr", "FR"), SessionHelper.getLocale(request));    
    }
    
    @Test
    public void testInvalid4() throws Exception {
        Logger log = Logger.getLogger("com.oup.eac.web.interceptors.TeachersClubLocaleInterceptor");
        log.setLevel(Level.INFO);
        setRequestURI(request, "..", "..");
        boolean result = sut.preHandle(request, response, handler);
        Assert.assertTrue(result);
        checkLocale(new Locale("fr", "FR"), SessionHelper.getLocale(request));    
    }
    
    /**
     * Sets the request uri.
     *
     * @param request the request
     * @param language the language
     * @param country the country
     */
    private void setRequestURI(final MockHttpServletRequest request, final String language, final String country) {
        if (StringUtils.isNotBlank(language)) {
            request.setParameter("selLanguage", language);
        }
        if (StringUtils.isNotBlank(country)) {
            request.setParameter("cc", country);
        }
    }

    /**
     * Check locale.
     *
     * @param expected the expected
     * @param actual the actual
     */
    private void checkLocale(final Locale expected, final Locale actual) {
        Assert.assertEquals(expected.getLanguage(), actual.getLanguage());
        Assert.assertEquals(expected.getCountry(), actual.getCountry());
        Assert.assertEquals(expected.getVariant(), actual.getVariant());
    }
    
    @Test
    public void testIgnoreCountryCodes() throws Exception {
        this.sut.setCountryCodesToIgnore(Arrays.asList("GB", null, "fr"));
        //DE
        setRequestURI(request, "de", "DE");
        boolean result = sut.preHandle(request, response, handler);
        Assert.assertTrue(result);
        checkLocale(new Locale("de", "DE"), SessionHelper.getLocale(request));
        //GB
        setRequestURI(request, "en", "GB");
        result = sut.preHandle(request, response, handler);
        Assert.assertTrue(result);
        checkLocale(new Locale("en"), SessionHelper.getLocale(request));
        //FR
        setRequestURI(request, "fr", "FR");
        result = sut.preHandle(request, response, handler);
        Assert.assertTrue(result);
        checkLocale(new Locale("fr"), SessionHelper.getLocale(request));
        
        this.sut.setCountryCodesToIgnore(null);
        
        //FR
        setRequestURI(request, "fr", "FR");
        result = sut.preHandle(request, response, handler);
        Assert.assertTrue(result);
        checkLocale(new Locale("fr", "FR"), SessionHelper.getLocale(request));
    }

}



