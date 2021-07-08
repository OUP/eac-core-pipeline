package com.oup.eac.ws.rest.v1;

import java.io.Serializable;

public class UpdateUserAuth implements Serializable {
	 
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/**
	 * token details to validate user
	 */
	private String token;
	/**
	 * new userName to update
	 */
	private String newUserName;
	/**
	 * new password to update
	 */
	private String newPassword;
	
	private String userId;
	
	/**
	 * @return
	 */
	public String getToken() {
		return token;
	}
	/**
	 * @param token
	 */
	public void setToken(String token) {
		this.token = token;
	}
	/**
	 * @return
	 */
	public String getNewUserName() {
		return newUserName;
	}
	/**
	 * @param newUserName
	 */
	public void setNewUserName(String newUserName) {
		this.newUserName = newUserName;
	}
	/**
	 * @return
	 */
	public String getNewPassword() {
		return newPassword;
	}
	/**
	 * @param newPassword
	 */
	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}
	/**
	 * 
	 * @return
	 */
	public String getUserId() {
		return userId;
	}
	/**
	 * 
	 * @param userId
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	
	
}
