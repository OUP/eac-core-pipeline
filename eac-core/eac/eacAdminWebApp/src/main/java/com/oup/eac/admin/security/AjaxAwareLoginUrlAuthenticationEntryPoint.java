package com.oup.eac.admin.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;

/**
 * This specialisation of {@link LoginUrlAuthenticationEntryPoint} gives us the ability to handle ajax requests that are
 * associated with a session that has timed out. In the normal course of events, once a session times out, subsequent
 * requests are intercepted by Spring and redirected back to the login screen. This is not what we want with ajax
 * requests, because otherwise we end up with the login screen rendering in a small window inside a given page.
 * <p>
 * This implementation of LoginUrlAuthenticationEntryPoint allows us to detect that a request is an ajax request and if
 * it is, add the Spring-Redirect-URL header to the response which contains the URL to the login page. Ajax clients can
 * look for this and use it to redirect the whole window to the login page.
 * 
 * @author keelingw
 * 
 */
public class AjaxAwareLoginUrlAuthenticationEntryPoint extends LoginUrlAuthenticationEntryPoint {

	public AjaxAwareLoginUrlAuthenticationEntryPoint(final String loginFormUrl) {
		super(loginFormUrl);
	}

	public void commence(final HttpServletRequest request, final HttpServletResponse response, final AuthenticationException authException) throws IOException,
			ServletException {
		if ("XMLHttpRequest".equals(request.getHeader("X-Requested-With"))) {
			response.setHeader("Spring-Redirect-URL", buildRedirectUrlToLoginPage(request, response, authException));
			response.getOutputStream().close();
		} else {
			super.commence(request, response, authException);
		}
	}
}
