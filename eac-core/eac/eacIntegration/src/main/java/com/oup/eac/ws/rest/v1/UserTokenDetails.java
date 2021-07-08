package com.oup.eac.ws.rest.v1;

import java.io.Serializable;

public class UserTokenDetails extends AbstractResponse implements Serializable {	

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String userId;
	
	private String oupId;
	
	private String userToken;
	
	private String userName;	
	
	private String tokenTimeStamp;
	
	private String tokenStatus;
	
	/** The error messages. */
    private String errorMessage;

    /** The error code. */
    private String errorCode;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getOupId() {
		return oupId;
	}

	public void setOupId(String oupId) {
		this.oupId = oupId;
	}

	public String getUserToken() {
		return userToken;
	}

	public void setUserToken(String userToken) {
		this.userToken = userToken;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getTokenTimeStamp() {
		return tokenTimeStamp;
	}

	public void setTokenTimeStamp(String tokenTimeStamp) {
		this.tokenTimeStamp = tokenTimeStamp;
	}

	public String getTokenStatus() {
		return tokenStatus;
	}

	public void setTokenStatus(String tokenStatus) {
		this.tokenStatus = tokenStatus;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
}

