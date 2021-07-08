package com.oup.eac.common.utils.security;

import org.apache.log4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

public class SecurityContextUtils {

	public static final Logger LOG = Logger.getLogger(SecurityContextUtils.class);
	
	public static UserDetails getCurrentUser() {
		UserDetails result = null;
		SecurityContext sc = SecurityContextHolder.getContext();
		if (sc != null) {
			Authentication auth = sc.getAuthentication();
			if (auth != null) {
				Object principal = auth.getPrincipal();
				if (principal != null) {
					if (principal instanceof UserDetails) {
						result = (UserDetails) principal;
					} else {
						LOG.warn("logged in user should be of type AdminUser but is : " + principal.getClass().getName());
					}
				}
			}
		}
		return result;
	}
}
