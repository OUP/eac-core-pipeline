package com.oup.eac.web.controllers.helpers;

import javax.servlet.http.HttpServletRequest;

import junit.framework.Assert;

import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;

public class RequestHelperTest {
	
	@Test
	public void testLogParams(){
		HttpServletRequest request = getRequest("http","localhost",8080,"");
		String params = RequestHelper.getParams(request);
		StringBuilder str = new StringBuilder("\nAuth type: null")
							.append("\nCharacter encoding: null")
							.append("\nContent length: -1")
							.append("\ncontentType: null")
							.append("\ncontextPath: ")
							.append("\nlocalAddr: 127.0.0.1")
							.append("\nlocalName: localhost")
							.append("\nlocalPort: 80")
							.append("\nmethod: ")
							.append("\npathInfo: null")
							.append("\nprotocol: http")
							.append("\nqueryString: null")
							.append("\nremoteAddr: 127.0.0.1")
							.append("\nremoteHost: localhost")
							.append("\nremotePort: 80")
							.append("\nrequestURI: ")
							.append("\nrequestURL: http://localhost:8080")
							.append("\nserverName: localhost")
							.append("\nserverPort: 8080")
							.append("\nservletPath: ");
		Assert.assertEquals(str.toString() ,params);	
	}	
	
	@Test
	public void testNoContextRoot(){
		HttpServletRequest request = getRequest("http","localhost",8080,"");
		String prefix = RequestHelper.getUrlPrefix(request);
		Assert.assertEquals("http://localhost:8080",prefix);		
	}

	@Test
	public void testNoTrailingSlash(){
		HttpServletRequest request = getRequest("http","localhost",8080,"/");
		String prefix = RequestHelper.getUrlPrefix(request);
		Assert.assertEquals("http://localhost:8080",prefix);		
	}

	@Test
	public void testUrlPrefix8080(){
		HttpServletRequest request = getRequest("http","localhost",8080,"/eac");
		String prefix = RequestHelper.getUrlPrefix(request);
		Assert.assertEquals("http://localhost:8080/eac",prefix);		
	}

	@Test
	public void testUrlPrefix80(){
		HttpServletRequest request = getRequest("http","localhost",80,"/eac");
		String prefix = RequestHelper.getUrlPrefix(request);
		Assert.assertEquals("http://localhost/eac",prefix);		
	}

	@Test
	public void testUrlPrefixHttps(){
		HttpServletRequest request = getRequest("https","localhost",443,"/eac");
		String prefix = RequestHelper.getUrlPrefix(request);
		Assert.assertEquals("https://localhost/eac",prefix);		
	}

	private HttpServletRequest getRequest(String protocol, String hostname, int port, String contextRoot) {
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.setScheme(protocol);
		request.setServerName(hostname);
		request.setServerPort(port);
		request.setContextPath(contextRoot);
		return request;
	}
}
