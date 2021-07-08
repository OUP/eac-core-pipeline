package com.oup.eac.ws.rest.v1;

import java.io.Serializable;

public class OupCredentialLoginPasswordWS implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 12343L;

	/** The password. */
	protected String password;

	/** The username. */
	protected String username;

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	
	

}
