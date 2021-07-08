package com.oup.eac.ws.rest.v1;

import java.io.Serializable;

public class OupCredentialWS implements Serializable {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 12343L;

    /** The login password credential. */
    protected OupCredentialLoginPasswordWS loginPasswordCredential;

	public OupCredentialLoginPasswordWS getLoginPasswordCredential() {
		return loginPasswordCredential;
	}

	public void setLoginPasswordCredential(
			OupCredentialLoginPasswordWS loginPasswordCredential) {
		this.loginPasswordCredential = loginPasswordCredential;
	}
    
    
}
