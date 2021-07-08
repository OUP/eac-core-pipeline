package com.oup.eac.domain;


public class GenerateUserIdToken  {

	private String userId;
	
	private String userName;
	
	private String tokenType;
	
	private Boolean generateFlag;

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

	public String getTokenType() {
		return tokenType;
	}

	public void setTokenType(String tokenType) {
		this.tokenType = tokenType;
	}

	public Boolean getGenerateFlag() {
		return generateFlag;
	}

	public void setGenerateFlag(Boolean generateFlag) {
		this.generateFlag = generateFlag;
	}

	
}
