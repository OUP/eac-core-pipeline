package com.oup.eac.web.interceptors;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

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

public class LocaleInterceptorTest {

    private static final Logger LOG = Logger.getLogger(LocaleInterceptorTest.class);
    
    private LocaleInterceptor sut;
    private Object handler = null;
    private MockHttpServletRequest request;
    private MockHttpServletResponse response;
    private SessionLocaleResolver resolver;
    private MockHttpSession session;

    @Before
    public void setup() {
        sut = new LocaleInterceptor();
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
        setLocaleParameter(request, "en", "GB");
        handle();
        checkLocale(request,new Locale("en", "GB"));
    }
    
    private void handle() throws Exception{
    	boolean result = sut.preHandle(request, response, handler);
        Assert.assertTrue(result);
        sut.postHandle(request, response, handler, null);
    }
    
    @Test
    public void testPost1() throws Exception {
        request.setMethod("POST");
        setLocaleParameter(request, "EN", "gb");
        handle();
        checkLocale(request,new Locale("fr", "FR"));
    }

    @Test
    public void testHappyPath2() throws Exception {                
        setLocaleParameter(request, "en", null);
        handle();
        checkLocale(request,new Locale("en"));    
    }
    
    @Test
    public void testHappyPath3() throws Exception {                
        setLocaleParameter(request, "es", null);
        handle();
        checkLocale(request,new Locale("es"));    
    }
    
    @Test
    public void testHappyPath4() throws Exception {                
        setLocaleParameter(request, "es", "ES");
        handle();
        checkLocale(request,new Locale("es","ES"));    
    }
    
    @Test
    public void testHappyPath6() throws Exception {                
        setLocaleParameter(request, null, null);        
        handle();
        checkLocale(request,new Locale("fr","FR"));    
    }
    
    @Test
    public void testResetLocaleToBrowser() throws Exception {
    	Assert.assertEquals(Locale.FRANCE,SessionHelper.getLocale(request));
    	request.addPreferredLocale(Locale.JAPANESE);    	
    	Assert.assertEquals(Locale.JAPANESE,request.getLocale());
    	request.addParameter("locale", "");
    	handle();
        checkLocale(request,new Locale("ja"));    
    }
    
    @Test
    public void testInvalid1() throws Exception {                
        setLocaleParameter(request, "ABC", "DEF");
        handle();
        checkLocale(request,new Locale("fr","FR"));    
    }
    
    @Test
    public void testInvalid2() throws Exception {
        setLocaleParameter(request, "en", "DEF");
        handle();
        checkLocale(request,new Locale("fr","FR"));    
    }

    @Test
    public void testInvalid3() throws Exception {                
        setLocaleParameter(request, "..", "..");
        handle();
        checkLocale(request,new Locale("fr","FR"));    
    }
    
    @Test
    public void testInvalid4() throws Exception {
        Logger log = Logger.getLogger("com.oup.eac.web.interceptors.TeachersClubLocaleInterceptor");
        log.setLevel(Level.INFO);
        setLocaleParameter(request, "..", "..");
        handle();
        checkLocale(request, new Locale("fr","FR"));    
    }
    
    @Test
    public void testInvalid5() throws Exception {
    	Assert.assertEquals(Locale.FRANCE, SessionHelper.getLocale(request));
        Logger log = Logger.getLogger("com.oup.eac.web.interceptors.TeachersClubLocaleInterceptor");
        log.setLevel(Level.INFO);
        setLocaleParameter(request, "en", "GB", "ABC");
        handle();
        checkLocale(request, new Locale("fr","FR"));    
    }
    private void setLocaleParameter(MockHttpServletRequest request, String language, String country) {
    	setLocaleParameter(request, language, country, null);
    }
    private void setLocaleParameter(MockHttpServletRequest request, String language, String country, String variant) {
    	String localeString = "";
        if (StringUtils.isNotBlank(language)) {
            localeString += language;
            if (StringUtils.isNotBlank(country)) {
                localeString += "_"+ country;
                if (StringUtils.isNotBlank(variant)) {
                    localeString += "_"+ variant;
                }
            }
            request.setParameter("locale", localeString);
        }
        
    }

    private void checkLocale(HttpServletRequest request, Locale expected) {
    	Locale actual = SessionHelper.getLocale(request);
        Assert.assertEquals(expected.getLanguage(), actual.getLanguage());
        Assert.assertEquals(expected.getCountry(), actual.getCountry());
        Assert.assertEquals(expected.getVariant(), actual.getVariant());
        Assert.assertEquals(expected, request.getAttribute("locale"));
    }

}


