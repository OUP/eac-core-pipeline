package com.oup.eac.admin.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.springframework.security.core.userdetails.UserDetails;

import com.oup.eac.common.utils.security.SecurityContextUtils;
import com.oup.eac.domain.AdminUser;
import com.oup.eac.domain.utils.audit.AuditLogger;

public class EacLogoutFilter implements Filter {

	@Override
	public void destroy() {
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException,
			ServletException {
		String usernameBefore = null;
		try {
			usernameBefore = getLoggedInUserName();
			filterChain.doFilter(request, response);
		} finally {
			if (usernameBefore != null) {
				String usernameAfter = getLoggedInUserName();
				if (usernameBefore.equals(usernameAfter) == false) {
					AuditLogger.logEvent("Admin[" + usernameBefore + "]", "Logged Out");
				}
			}
		}

	}

	private String getLoggedInUserName() {
		String result = null;
		UserDetails userDetails = SecurityContextUtils.getCurrentUser();
		if (userDetails != null) {
			if (userDetails instanceof AdminUser) {
				AdminUser admin = (AdminUser) userDetails;
				result = admin.getUsername();
			}
		}
		return result;
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}

}
