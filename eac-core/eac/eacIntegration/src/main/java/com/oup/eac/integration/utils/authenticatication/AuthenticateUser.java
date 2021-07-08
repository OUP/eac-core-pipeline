package com.oup.eac.integration.utils.authenticatication;

import java.util.Date;
import java.util.Map;

import com.oup.eac.common.date.utils.DateUtils;
import com.oup.eac.integration.rest.template.AcesRestTemplate;



public class AuthenticateUser {
	
	
	private static AuthenticateResponse response;
	

	public Map<String,String> getSessionCredentials(UserCredentials credentials) throws Exception{				
		boolean isAlreadyAuthenticated = false;
		if(response!=null && response.getData()!=null && response.getStatus().equalsIgnoreCase("success")){
			Date sessionExpiryTime = DateUtils.fromISO8601UTC(response.getData().get("Expiration"));
			Date currentTime = new Date();
			if(sessionExpiryTime.after(currentTime)){
				isAlreadyAuthenticated = true;
			}
		}
		if(!isAlreadyAuthenticated){
			response = new AcesRestTemplate().postForAuthenticateUser(credentials);
		}
		if(!response.getStatus().equalsIgnoreCase("success")){
			response = new AuthenticateResponse();			
			throw new Exception(response.getMessage());
		}
		return response.getData();
	}
	
}
