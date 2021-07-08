package com.oup.eac.dto;


public class ResetPasswordTokenDto {

	private String userId;
	private String successUrl;
	private String failureUrl;
	private String clientTokenId;
	private Long expiry;
	
	public ResetPasswordTokenDto() {
	}

	/**
	 * @param userId
	 * @param successUrl
	 * @param failureUrl
	 * @param clientTokenId
	 */
	public ResetPasswordTokenDto(final String userId, final String successUrl, final String failureUrl,
			final String clientTokenId) {
		this.userId = userId;
		this.successUrl = successUrl;
		this.failureUrl = failureUrl;
		this.clientTokenId = clientTokenId;
		this.expiry = System.currentTimeMillis() + 86400000; //For 24 hours
	}
	

	/**
	 * @param userId
	 * @param successUrl
	 * @param failureUrl
	 * @param clientTokenId
	 * @param expiry
	 */
	public ResetPasswordTokenDto(final String userId, final String successUrl, final String failureUrl,
			final String clientTokenId, final Long expiry) {
		this.userId = userId;
		this.successUrl = successUrl;
		this.failureUrl = failureUrl;
		this.clientTokenId = clientTokenId;
		this.expiry = expiry;
	}

	


	/**
	 * @return the userId
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * @param userId the userId to set
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}

	/**
	 * @return the successUrl
	 */
	public String getSuccessUrl() {
		return successUrl;
	}

	/**
	 * @param successUrl the successUrl to set
	 */
	public void setSuccessUrl(String successUrl) {
		this.successUrl = successUrl;
	}

	/**
	 * @return the failureUrl
	 */
	public String getFailureUrl() {
		return failureUrl;
	}

	/**
	 * @param failureUrl the failureUrl to set
	 */
	public void setFailureUrl(String failureUrl) {
		this.failureUrl = failureUrl;
	}

	/**
	 * @return the clientTokenId
	 */
	public String getClientTokenId() {
		return clientTokenId;
	}

	/**
	 * @param clientTokenId the clientTokenId to set
	 */
	public void setClientTokenId(String clientTokenId) {
		this.clientTokenId = clientTokenId;
	}

	/**
	 * @param expiry the expiry to set
	 */
	public void setExpiry(Long expiry) {
		this.expiry = expiry;
	}

	public Long getExpiry() {
		return expiry;
	}

	public boolean isExpired() {
		return (expiry - System.currentTimeMillis()) < 0;
	}
}
