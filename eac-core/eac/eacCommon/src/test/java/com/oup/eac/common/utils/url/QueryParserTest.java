/**
 * 
 */
package com.oup.eac.common.utils.url;

import static org.junit.Assert.assertEquals;

import java.io.UnsupportedEncodingException;

import org.junit.Test;

/**
 * @author harlandd URLUtis tests
 */
public class QueryParserTest {

    @Test
    public void testUrlWithNoParams() throws UnsupportedEncodingException {
		String query = "hello.htm";
		QueryParser queryParser = new QueryParser(query);
		queryParser.replaceParameter("ersession", "somethingelse");
		assertEquals("hello.htm?ersession=somethingelse", queryParser.getUrl());
    }
    
    @Test
    public void testUrlWithParams() throws UnsupportedEncodingException {
		String query = "hello.htm?name=dave&age=10&children=4&ersession=blahblah";
		QueryParser queryParser = new QueryParser(query);
		queryParser.replaceParameter("ersession", "somethingelse");
		assertEquals("hello.htm?name=dave&age=10&children=4&ersession=somethingelse", queryParser.getUrl());
    }
    
    @Test
    public void testUrlWithMultipuleParams() throws UnsupportedEncodingException {
		String query = "hello.htm?name=dave&age=10&children=1&ersession=blahblah&ersession=blahblah2";
		QueryParser queryParser = new QueryParser(query);
		queryParser.replaceParameter("ersession", "somethingelse");
		assertEquals("hello.htm?name=dave&age=10&children=1&ersession=somethingelse", queryParser.getUrl());
    }
    
    @Test
    public void testUrlWithOneParamNoValue() throws UnsupportedEncodingException {
		String query = "hello.htm?name=dave&age=10&children=&ersession=blahblah";
		QueryParser queryParser = new QueryParser(query);
		queryParser.replaceParameter("ersession", "somethingelse");
		assertEquals("hello.htm?name=dave&age=10&children=&ersession=somethingelse", queryParser.getUrl());
    }

}
