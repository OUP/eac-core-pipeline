/**
 * 
 */
package com.oup.eac.common.utils.url;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author harlandd URLUtis tests
 */
public class URLUtilsTest {

    @Test
    public void testUrlWithNoParams() {
        String url = "http://www.google.com";
        Map<String, String> params = new HashMap<String, String>();
        params.put("param1", "value1");
        String newUrl = URLUtils.appendParams(url, params);
        assertEquals("http://www.google.com?param1=value1", newUrl);
    }

    @Test
    public void testUrlWithParams() {
        String url = "http://www.google.com?param1=value1";
        Map<String, String> params = new HashMap<String, String>();
        params.put("param2", "value2");
        String newUrl = URLUtils.appendParams(url, params);
        assertEquals("http://www.google.com?param1=value1&param2=value2", newUrl);
    }

    //@Test
    public void testUrlWithMultipleParams() {
        String url = "http://www.google.com?param1=value1";
        Map<String, String> params = new HashMap<String, String>();
        params.put("param2", "value2");
        params.put("param3", "value3");
        String newUrl = URLUtils.appendParams(url, params);
        assertEquals("http://www.google.com?param1=value1&param2=value2&param3=value3", newUrl);
    }

    @Test
    public void testSafeEncodeNull() throws MalformedURLException, UnsupportedEncodingException, URISyntaxException {
        assertEquals(null, URLUtils.safeEncode(null));
    }

    @Test
    public void testSafeEncodeNoChange() throws MalformedURLException, UnsupportedEncodingException, URISyntaxException {
        assertEquals("http://www.google.com/wibble.htm", URLUtils.safeEncode("http://www.google.com/wibble.htm"));
    }

    @Test
    public void testSafeEncodeWithSpace() throws MalformedURLException, UnsupportedEncodingException,
            URISyntaxException {
        assertEquals("http://www.google.com/test%20space/wibble.htm",
                URLUtils.safeEncode("http://www.google.com/test space/wibble.htm"));
    }

    @Test
    public void testSafeEncodeAlreadyEncoded() throws MalformedURLException, UnsupportedEncodingException,
            URISyntaxException {
        assertEquals("http://www.google.com/test%20space/wibble.htm",
                URLUtils.safeEncode("http://www.google.com/test%20space/wibble.htm"));
    }

    @Test
    public void testSafeEncodeSpaceInQuery() throws MalformedURLException, UnsupportedEncodingException,
            URISyntaxException {
        assertEquals("http://www.google.com/wibble.htm?a=test%20a",
                URLUtils.safeEncode("http://www.google.com/wibble.htm?a=test a"));
    }

    @Test
    public void testSafeEncodeRef() throws MalformedURLException, UnsupportedEncodingException, URISyntaxException {
        assertEquals("http://www.google.com/wibble.htm#test%20b",
                URLUtils.safeEncode("http://www.google.com/wibble.htm#test b"));
    }

    @Test
    public void testURLInValid1() throws MalformedURLException, UnsupportedEncodingException, URISyntaxException {
        try {
            URLUtils.validateUrl("http%3a%2f%2felt.oup.com%2f%2fgeneral_content%2fglobal%2flogout/option><option%20title=");
            fail("Should have thrown exception");
        } catch (InvalidURLException e) {
            assertEquals(
                    "Invalid character: > found in URL: http://elt.oup.com//general_content/global/logout/option><option title=",
                    e.getMessage());
        }
    }

    @Test
    public void testURLInValid2() throws MalformedURLException, UnsupportedEncodingException, URISyntaxException {
        try {
            URLUtils.validateUrl("http%3a%2f%2felt.oup.com%2f%2fgeneral_content%2fglobal%2flogout/option<option%20title=");
            fail("Should have thrown exception");
        } catch (InvalidURLException e) {
            assertEquals(
                    "Invalid character: < found in URL: http://elt.oup.com//general_content/global/logout/option<option title=",
                    e.getMessage());
        }
    }

    @Test
    public void testURLInValid3() throws MalformedURLException, UnsupportedEncodingException, URISyntaxException {
        try {
            URLUtils.validateUrl("http%3a%2f%2felt.oup.com%2f%2fgeneral_content%2fglobal%2flogout/optionoption%20title=");
        } catch (InvalidURLException e) {
            fail("Should not have thrown exception");
        }
    }

    @Test
    public void testIsRelative() {
        try {
            assertTrue(URLUtils.isRelativeURL("/profile.htm"));
            assertFalse(URLUtils.isRelativeURL("http://test.oup.com/profile.htm"));
        } catch (Exception e) {
            fail("Should not have thrown exception");
        }
    }

    @Test(expected = InvalidURLException.class)
    public void testCheckURISyntaxExceptionV1() throws InvalidURLException {
        String data = "01234567";
        for (int i = 0; i < data.length(); i++) {
            System.out.printf("%d %c%n", i, data.charAt(i));
        }
        URISyntaxException syn = new URISyntaxException(data, "unknown", data.length()-1);
        URLUtils.checkURISyntaxException(syn);
    }
    
    @Test(expected = InvalidURLException.class)
    public void testCheckURISyntaxExceptionV2() throws InvalidURLException {
        String data = "01234567";
        for (int i = 0; i < data.length(); i++) {
            System.out.printf("%d %c%n", i, data.charAt(i));
        }
        URISyntaxException syn = new URISyntaxException(data, "unknown", data.length());
        URLUtils.checkURISyntaxException(syn);
    }
    @Test(expected = InvalidURLException.class)
    public void testCheckURISyntaxExceptionV3() throws InvalidURLException {
        String data = "01234567";
        for (int i = 0; i < data.length(); i++) {
            System.out.printf("%d %c%n", i, data.charAt(i));
        }
        URISyntaxException syn = new URISyntaxException(data, "unknown", -1);
        URLUtils.checkURISyntaxException(syn);
    }
    
    @Test
    public void testIsValidURI(){
        /*Assert.assertFalse(URLUtils.isValidURI(""));
        Assert.assertFalse(URLUtils.isValidURI("",false));
        Assert.assertTrue(URLUtils.isValidURI("",true));
        Assert.assertTrue(URLUtils.isValidURI("//www.google.co.uk/"));
        Assert.assertTrue(URLUtils.isValidURI("//www.google.co.uk/",true));
        Assert.assertFalse(URLUtils.isValidURI("bob",true));
        Assert.assertFalse(URLUtils.isValidURI("bob",false));
        Assert.assertTrue(URLUtils.isValidURI("/bob://??bob",true));
        Assert.assertFalse(URLUtils.isValidURI("/\\:/\\:/bob:\\//??bob",true));*/
    }
    
    @Test
    public void testIsValidURL(){
         /*Assert.assertFalse(URLUtils.isValidURL(""));
        Assert.assertTrue(URLUtils.isValidURL("http://www.google.com"));
        Assert.assertFalse(URLUtils.isValidURL("/\\:/\\:/bob:\\//??bob"));*/
    }
    
    
    @Test
    public void testValidateUrl2() throws UnsupportedEncodingException {
        validateURL("http://www.google.co.uk",null);
        validateURL(" ",InvalidURLException.class);        
    }
    
    private  void validateURL(String url, Class<? extends Exception> clazz){
        try{
           URLUtils.validateUrl(url);
           Assert.assertTrue(clazz == null);
        }catch(Exception ex ){
           Assert.assertEquals(ex.getClass(),clazz); 
        }
    }
    
    @Test
    public void testIsRelativeURL() throws InvalidURLException{
        try{
            URLUtils.isRelativeURL(":::");
            Assert.fail();
        }catch(InvalidURLException ex){
            Assert.assertEquals("Expected scheme name at index 0: :::",ex.getMessage());
        }
        Assert.assertTrue(URLUtils.isRelativeURL("./bob"));
        Assert.assertFalse(URLUtils.isRelativeURL("http://www.google.com"));
    }
}

