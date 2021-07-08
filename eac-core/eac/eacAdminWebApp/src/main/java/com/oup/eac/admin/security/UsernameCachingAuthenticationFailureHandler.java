package com.oup.eac.admin.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Decorates an AuthenticationFailureHandler, caching username before calling through to the underlying
 * handler.
 * 
 * This has been introduced because UsernamePasswordAuthenticationFilter.SPRING_SECURITY_LAST_USERNAME_KEY has been deprecated.
 * 
 * @author Ian Packard
 *
 */
public class UsernameCachingAuthenticationFailureHandler implements AuthenticationFailureHandler {
	
	public static final String SPRING_SECURITY_LAST_USERNAME_KEY = "spring.security.last.username.key";
	
	private AuthenticationFailureHandler authenticationFailureHandler; 
	
	@Override
	public void onAuthenticationFailure(HttpServletRequest request,
		HttpServletResponse response, AuthenticationException exception)
		throws IOException, ServletException {
		
		String username = request.getParameter(UsernamePasswordAuthenticationFilter.SPRING_SECURITY_FORM_USERNAME_KEY);
		
		request.getSession().setAttribute(SPRING_SECURITY_LAST_USERNAME_KEY, username);
		authenticationFailureHandler.onAuthenticationFailure(request, response, exception);
	}
	
	public AuthenticationFailureHandler getAuthenticationFailureHandler() {
		return authenticationFailureHandler;
	}

	public void setAuthenticationFailureHandler(
			AuthenticationFailureHandler authenticationFailureHandler) {
		this.authenticationFailureHandler = authenticationFailureHandler;
	}

}
