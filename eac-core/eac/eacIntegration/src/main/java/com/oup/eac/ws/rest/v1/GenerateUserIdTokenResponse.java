package com.oup.eac.ws.rest.v1;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class GenerateUserIdTokenResponse extends AbstractResponse implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<UserTokenDetails> userTokenDetails;

	public List<UserTokenDetails> getUserTokenDetails() {
		if(userTokenDetails == null) userTokenDetails =new ArrayList<UserTokenDetails>();
		return userTokenDetails;
	}

	public void setUserTokenDetails(List<UserTokenDetails> userTokenDetails) {
		this.userTokenDetails = userTokenDetails;
	}


}
