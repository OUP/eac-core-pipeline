package com.oup.eac.dto;

import java.io.Serializable;


public class AdminChangePasswordTokenDto implements Serializable {

	private String username;
	private Long expiry;
	
	public AdminChangePasswordTokenDto() {
	}

	/**
	 * Creates a new token for an admin user as specified by the <code>username</code> which will expire at the time
	 * specified by the supplied <code>expiry</code> timestamp.
	 * 
	 * @param username
	 *            The username of the admin user.
	 * @param expiry
	 *            The number of milliseconds in which the token will expire.
	 */
	public AdminChangePasswordTokenDto(final String username, final Long expiry) {
		this.username = username;
		this.expiry = expiry;
	}

	/**
	 * Creates a new token for an admin user as specified by the <code>username</code> which will expire 8 hours
	 * from now.
	 * 
	 * @param username
	 *            The username of the admin user for which the token is for.
	 */
	public AdminChangePasswordTokenDto(final String username) {
		this.username = username;
		this.expiry = System.currentTimeMillis() + 28800000;
	}

	public String getUsername() {
		return username;
	}

	public Long getExpiry() {
		return expiry;
	}

	public boolean isExpired() {
		return (expiry - System.currentTimeMillis()) < 0;
	}
}
