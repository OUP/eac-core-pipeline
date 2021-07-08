package com.oup.eac.admin.components;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.event.AbstractAuthenticationEvent;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.authentication.event.InteractiveAuthenticationSuccessEvent;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.oup.eac.domain.AdminUser;
import com.oup.eac.domain.utils.audit.AuditLogger;
import com.oup.eac.service.AdminService;

@Component(value="eacAuthenticationEventListener")
public class EacAuthenticationEventListener implements ApplicationListener<AbstractAuthenticationEvent>{

	@Autowired			
	private AdminService adminService;
	
	@Override
	public void onApplicationEvent(AbstractAuthenticationEvent event) {
		
		if(event instanceof AuthenticationSuccessEvent){
			AuthenticationSuccessEvent evt = (AuthenticationSuccessEvent)event;
			//TODO - Once eacWeb is migrated to spring, this should deal with User rather than AdminUser
			UserDetails userDetails = (UserDetails)evt.getAuthentication().getPrincipal();
			
			if (userDetails instanceof AdminUser) {
				adminService.updateAdminUserLastLoginTime(userDetails.getUsername());
			}
		} else if (event instanceof InteractiveAuthenticationSuccessEvent) {
			InteractiveAuthenticationSuccessEvent evt = (InteractiveAuthenticationSuccessEvent)event;
			AuditLogger.logEvent("Logged In");
		} else if (event instanceof AuthenticationFailureBadCredentialsEvent) {
			AuthenticationFailureBadCredentialsEvent evt = (AuthenticationFailureBadCredentialsEvent)event;
			Object source = evt.getSource();
			if(source instanceof UsernamePasswordAuthenticationToken){
				UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) source;
				AuditLogger.logEvent("Bad Credentials for Admin[" +token.getPrincipal() + "]");	
			}
		} 
	}

}
