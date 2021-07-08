package com.oup.eac.dto;

public class WsUserIdDto {
	private String userId ;
		
	private String userName ;

	private String sessionToken ;
	
	private ExternalIdDto externalId ;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getSessionToken() {
		return sessionToken;
	}

	public void setSessionToken(String sessionToken) {
		this.sessionToken = sessionToken;
	}

	public ExternalIdDto getExternalId() {
		return externalId;
	}

	public void setExternalId(ExternalIdDto externalId) {
		this.externalId = externalId;
	}
}
