package com.oup.eac.ws.rest.v1;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class OupAuthProfileWS implements Serializable {
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 12343L;
	/**
     * 
     */
	private String matchMode;
	private List<OupCredentialWS> credentials;

	public List<OupCredentialWS> getCredentials() {
		if(credentials == null){
			credentials = new ArrayList<OupCredentialWS>();
		}
		return credentials;
	}

	public void setCredentials(List<OupCredentialWS> credentials) {
		this.credentials = credentials;
	}

	public String getMatchMode() {
		return matchMode;
	}

	public void setMatchMode(String matchMode) {
		this.matchMode = matchMode;
	}
	
}
