package com.oup.eac.web.interceptors;

import javax.naming.NamingException;
import javax.servlet.ServletException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockHttpSession;

import com.oup.eac.common.mock.AbstractMockTest;

public class EacWebContentInterceptorTest extends AbstractMockTest {

	public EacWebContentInterceptorTest() throws NamingException {
		super();
	}

	private EacWebContentInterceptor interceptor;
	private MockHttpServletRequest request;
	private MockHttpServletResponse response;
	private MockHttpSession session;
	private Object handler;

	@Before
	public void setup() {
		response = new MockHttpServletResponse();
		session = new MockHttpSession();
		interceptor = new EacWebContentInterceptor();
		interceptor.setCacheSeconds(0);
		interceptor.setUseExpiresHeader(true);
		interceptor.setUseCacheControlHeader(true);
		interceptor.setUseCacheControlNoStore(true);
		
		request = new MockHttpServletRequest();
		request.setSession(session);

	}
	
	private void setupForNullUserAgent() {
		request = new MockHttpServletRequest() {
			public String getHeader(String name) {
				return null;
			}
		};
		request.setSession(session);
	}

	@Test
	public void testNoUserAgent() throws ServletException {
		setupForNullUserAgent();
		boolean canContinue = interceptor.preHandle(request, response, handler);
		Assert.assertTrue(canContinue);
	}
	
	@Test
	public void testBlankUserAgent() throws ServletException {
		request.addHeader(EacWebContentInterceptor.USER_AGENT, "    ");
		boolean canContinue = interceptor.preHandle(request, response, handler);
		Assert.assertTrue(canContinue);
	}
	
	@Test
	public void testIEUserAgent() throws ServletException {
		request.addHeader(EacWebContentInterceptor.USER_AGENT, EacWebContentInterceptor.INTERNET_EXPLORER_CODE);
		boolean canContinue = interceptor.preHandle(request, response, handler);		
		Assert.assertTrue(canContinue);
		Assert.assertEquals(EacWebContentInterceptor.CACHE_CONTROL_MSIE_VALUE, response.getHeader(EacWebContentInterceptor.CACHE_CONTROL));
	}
	
	@Test
	public void testNonIEUserAgent() throws ServletException {
		request.addHeader(EacWebContentInterceptor.USER_AGENT, "bob");
		boolean canContinue = interceptor.preHandle(request, response, handler);		
		Assert.assertTrue(canContinue);
		Assert.assertEquals("no-cache", response.getHeader(EacWebContentInterceptor.CACHE_CONTROL));
	}
}
