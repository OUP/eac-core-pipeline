package com.oup.eac.admin.components;

import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpSession;

import org.springframework.context.ApplicationListener;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.web.session.HttpSessionDestroyedEvent;
import org.springframework.stereotype.Component;

import com.oup.eac.domain.AdminUser;
import com.oup.eac.domain.utils.audit.AuditLogger;

@Component(value = "eacSessionTimeoutEventListener")
public class EacSessionTimeoutEventListener implements ApplicationListener<HttpSessionDestroyedEvent> {

	@Override
	public void onApplicationEvent(HttpSessionDestroyedEvent evt) {
		HttpSession session = evt.getSession();
		long timeout = TimeUnit.SECONDS.toMillis(session.getMaxInactiveInterval());
		long timeoutTime = session.getLastAccessedTime() + timeout;
		boolean timedOut = System.currentTimeMillis() > timeoutTime;
		if(!timedOut){
			return;
		}
		SecurityContext sc = getSecurityContextFromEvent(evt);		
		if (sc != null) {
			Object principal = sc.getAuthentication().getPrincipal();
			if (principal instanceof AdminUser) {
				AdminUser admin = (AdminUser) principal;				
				String msg = String.format("Admin[%s] Session Timed Out", admin.getUsername());
				AuditLogger.logEvent(msg);
			}
		}

	}

	private SecurityContext getSecurityContextFromEvent(HttpSessionDestroyedEvent evt) {
		SecurityContext result = null;
		List<SecurityContext> contexts = evt.getSecurityContexts();
		if (contexts != null && contexts.size() > 0) {
			result = contexts.get(0);
		}
		return result;
	}

}
