package com.oup.eac.common.utils;

import static org.junit.Assert.*;

import java.net.MalformedURLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class UrlMapTest {

    /**
     * Test putting urls into the map.
     * 
     * @throws MalformedURLException
     *             the exception
     */
    @Test
    public final void testPut() throws MalformedURLException {
        UrlMap<String> urlMap = new UrlMap<String>();
        urlMap.put("http://oup.com/elt/book", "value1");
        urlMap.put("http://oup.com/elt", "value2");
        urlMap.put("http://oup.com", "value3");
        urlMap.put("http://oxfordfajar.com.my", "value4");

        assertEquals("value1", urlMap.get("http://oup.com/elt/book"));
        assertEquals("value2", urlMap.get("http://oup.com/elt"));
        assertEquals("value3", urlMap.get("http://oup.com"));
        assertEquals("value4", urlMap.get("http://oxfordfajar.com.my"));
    }

    /**
     * Test constructing the map.
     * 
     * @throws MalformedURLException
     *             the exception
     */
    @Test
    public final void testConstructor() throws MalformedURLException {
        Map<String, String> map = new HashMap<String, String>();
        map.put("http://oup.com/elt/book", "value1");
        map.put("http://oup.com/elt", "value2");
        map.put("http://oup.com", "value3");
        map.put("http://oxfordfajar.com.my", "value4");

        UrlMap<String> urlMap = new UrlMap<String>(map);
        assertEquals("value1", urlMap.get("http://oup.com/elt/book"));
        assertEquals("value2", urlMap.get("http://oup.com/elt"));
        assertEquals("value3", urlMap.get("http://oup.com"));
        assertEquals("value4", urlMap.get("http://oxfordfajar.com.my"));
    }

    /**
     * Test matching sub domain.
     * 
     * @throws MalformedURLException
     *             the exception
     */
    @Test
    public final void testMatchSubDomain() throws MalformedURLException {
        UrlMap<String> mapping = new UrlMap<String>();
        mapping.put("http://oup.com/elt/book", "value1");

        assertEquals("value1", mapping.get("http://oup.com/elt/book"));
        assertEquals("value1", mapping.get("http://oup.com/elt/book/another"));
        assertEquals("value1", mapping.get("http://oup.com/elt/book/another/yetAnother"));
    }

    /**
     * Test matching fall back.
     * 
     * @throws MalformedURLException
     *             the exception
     */
    @Test
    public final void testMatchFallBack() throws MalformedURLException {
        UrlMap<String> mapping = new UrlMap<String>();
        mapping.put("http://oup.com", "value1");
        mapping.put("http://oup.com/elt", "value2");

        assertEquals("value1", mapping.get("http://oup.com/no/value"));
        assertEquals("value2", mapping.get("http://oup.com/elt/value"));
    }

    /**
     * Test no match.
     * 
     * @throws MalformedURLException
     *             the exception
     */
    @Test
    public final void testNoMatch() throws MalformedURLException {
        UrlMap<String> mapping = new UrlMap<String>();
        mapping.put("http://oup.com/", "value1");

        assertNull(mapping.get("http://thumbsupsoftware.com"));
    }

    /**
     * Test matching an invalid url. Expect a MalformedURLException to be thrown.
     */
    @Test
    public final void testMatchInvalidUrl() {
        UrlMap<String> mapping = new UrlMap<String>();
        try {
            mapping.put("rubbish", "value1");
            fail("Exception expected");
        } catch (MalformedURLException e) {
            assertNotNull(e);
        }
    }

    /**
     * Test matching with a trailing slash in the url.
     * 
     * @throws MalformedURLException
     *             the exception
     */
    @Test
    public final void testMatchTrailingSlash() throws MalformedURLException {
        UrlMap<String> mapping = new UrlMap<String>();
        mapping.put("http://oup.com/elt/", "value1");

        assertEquals("value1", mapping.get("http://oup.com/elt"));
        assertEquals("value1", mapping.get("http://oup.com/elt/"));
    }

    /**
     * Test matching a url that is preceded with www.
     * 
     * @throws MalformedURLException
     *             the exception
     */
    @Test
    public final void testMatchPreceedingWWW() throws MalformedURLException {
        UrlMap<String> mapping = new UrlMap<String>();
        mapping.put("http://www.oup.com/elt/", "value1");

        assertEquals("value1", mapping.get("http://oup.com/elt"));
        assertEquals("value1", mapping.get("http://www.oup.com/elt/"));
    }

    /**
     * Test getting a null from the map.
     * 
     * @throws MalformedURLException
     *             the exception
     */
    @Test
    public final void testGetNullValue() throws MalformedURLException {
        UrlMap<String> mapping = new UrlMap<String>();
        mapping.put("http://www.oup.com/elt/", "value1");

        assertEquals(null, mapping.get(null));
        assertEquals("value1", mapping.get("http://www.oup.com/elt/"));
    }

    /**
     * Test getting multiple matches from the map.
     * 
     * @throws MalformedURLException
     *             the exception
     */
    @Test
    public final void testMultipleMatches() throws MalformedURLException {
        UrlMap<String> mapping = new UrlMap<String>();
        mapping.put("http://www.oup.com/elt/book", "value1");
        mapping.put("http://oup.com/elt/book/another", "value2");
        mapping.put("http://oup.com/elt/book/another/yetAnother/", "value3");
        mapping.put("http://oxfordfajar.com.my/", "value4");
     
        Collection<String> values = mapping.getAll("http://oxfordfajar.com.my");        
        assertEquals(1, values.size());
        assertTrue(values.contains("value4"));

        values = mapping.getAll("http://oup.com/elt/book/another/yetAnother");        
        assertEquals(3, values.size());
        assertTrue(values.contains("value1"));
        assertTrue(values.contains("value2"));
        assertTrue(values.contains("value3"));
        
        values = mapping.getAll("http://oup.com/elt/book/another");        
        assertEquals(2, values.size());
        assertTrue(values.contains("value1"));
        assertTrue(values.contains("value2"));
        
        values = mapping.getAll("http://oup.com/elt");        
        assertEquals(0, values.size());
        
    }

    /**
     * Test getting all matches from the map.
     * 
     * @throws MalformedURLException
     *             the exception
     */
    @Test
    public final void testValues() throws MalformedURLException {
        UrlMap<String> mapping = new UrlMap<String>();
        Collection<String> values = mapping.values();        
        assertEquals(0, values.size());
        
        mapping.put("http://www.oup.com/elt/book", "value1");
        mapping.put("http://oup.com/elt/book/another", "value2");
        mapping.put("http://oup.com/elt/book/another/yetAnother/", "value3");
        mapping.put("http://oxfordfajar.com.my/", "value4");

        values = mapping.values();        
        assertEquals(4, values.size());
        assertTrue(values.contains("value1"));
        assertTrue(values.contains("value2"));
        assertTrue(values.contains("value3"));
        assertTrue(values.contains("value4"));

    }
}
