package com.oup.eac.ws.v2.service.impl;

import org.apache.commons.lang3.StringEscapeUtils;

import com.oup.eac.common.utils.EACSettings;
import com.oup.eac.service.ServiceLayerException;
import com.oup.eac.ws.v2.binding.access.GetCredentialPolicyRequest;
import com.oup.eac.ws.v2.binding.access.GetCredentialPolicyResponse;
import com.oup.eac.ws.v2.binding.access.GetCredentialPolicyResponseSequence;
import com.oup.eac.ws.v2.binding.common.ErrorStatus;
import com.oup.eac.ws.v2.binding.common.PasswordPolicy;
import com.oup.eac.ws.v2.binding.common.UsernamePolicy;
import com.oup.eac.ws.v2.ex.WebServiceException;
import com.oup.eac.ws.v2.ex.WebServiceValidationException;
import com.oup.eac.ws.v2.service.CredentialPolicyAdapter;
import com.oup.eac.ws.v2.service.utils.ErrorStatusUtils;

public class CredentialPolicyAdapterImpl implements CredentialPolicyAdapter{

	@Override
	public GetCredentialPolicyResponse getCredentialPolicy(GetCredentialPolicyRequest request) throws WebServiceException {
		//Response
		GetCredentialPolicyResponse response = new GetCredentialPolicyResponse();
		
		try {
			String usernameText=EACSettings.getProperty(EACSettings.USERNAME_POLICY_TEXT);
			String usernameRegex=EACSettings.getProperty(EACSettings.USERNAME_POLICY_REGEX);
			String passwordRegex=EACSettings.getProperty(EACSettings.PASSWORD_POLICY_REGEX);
			String passwordText=EACSettings.getProperty(EACSettings.PASSWORD_POLICY_TEXT);
			
			if(checkNull(usernameText, usernameRegex, passwordText, passwordRegex)){
				throw new ServiceLayerException("Policies does not exist.");
			}else{
				//username policies
				UsernamePolicy usernamePolicy =  new UsernamePolicy();
				usernamePolicy.setText(usernameText);
				usernamePolicy.setRegex(usernameRegex);
				
				//password policies
				PasswordPolicy passwordPolicy = new PasswordPolicy();
				passwordPolicy.setText(passwordText);
				passwordPolicy.setRegex(passwordRegex);
				
				//seq
				GetCredentialPolicyResponseSequence seq = new GetCredentialPolicyResponseSequence();
				seq.setUsernamePolicy(usernamePolicy);
				seq.setPasswordPolicy(passwordPolicy);
				
				response.setGetCredentialPolicyResponseSequence(seq);
			}
		}catch (Exception ex) {
			ErrorStatus errorStatus = ErrorStatusUtils.getServerErrorStatus(ex.getMessage());
			response.setErrorStatus(errorStatus);
		}
		return response;
	}
	
	//checking nulls
	private boolean checkNull(String usernameRegex, String usernameText, String passwordRegex, String passwordText){
		boolean isNull = false;
		if(null == usernameText || null == usernameRegex || null == passwordText || null == passwordRegex ){
			isNull = true;
		}
		return isNull;
	}
	

}
