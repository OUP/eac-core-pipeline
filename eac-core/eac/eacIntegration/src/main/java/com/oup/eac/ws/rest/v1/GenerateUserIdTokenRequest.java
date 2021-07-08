package com.oup.eac.ws.rest.v1;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class GenerateUserIdTokenRequest implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private List<UserDetailsGenerateUserToken> userDetails;
	
	/** Generate Flag. */
	private String generateFlag;
    
    /** token type. */
    private String tokenType;

	public List<UserDetailsGenerateUserToken> getUserDetails() {
		if(userDetails == null){
			userDetails = new ArrayList<UserDetailsGenerateUserToken>();
		}
		return userDetails;
	}

	public void setUserDetails(List<UserDetailsGenerateUserToken> userDetails) {
		this.userDetails = userDetails;
	}

	public String getGenerateFlag() {
		return generateFlag;
	}

	public void setGenerateFlag(String generateFlag) {
		this.generateFlag = generateFlag;
	}

	public String getTokenType() {
		return tokenType;
	}

	public void setTokenType(String tokenType) {
		this.tokenType = tokenType;
	}
    
    

}
