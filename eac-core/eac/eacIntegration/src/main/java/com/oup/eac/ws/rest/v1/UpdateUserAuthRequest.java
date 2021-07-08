package com.oup.eac.ws.rest.v1;


import java.io.Serializable;

public class UpdateUserAuthRequest implements Serializable {

	 /** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/**
	 * userAuth
	 */
	private UpdateUserAuth user;

	/**
	 * @return
	 */
	public UpdateUserAuth getUser() {
		return user;
	}

	/**
	 * @param user
	 */
	public void setUser(UpdateUserAuth user) {
		this.user = user;
	}
	
	
}
