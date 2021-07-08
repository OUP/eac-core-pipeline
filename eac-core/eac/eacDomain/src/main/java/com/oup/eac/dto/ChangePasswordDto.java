/**
 * 
 */
package com.oup.eac.dto;

import java.io.Serializable;

/**
 * @author harlandd
 * 
 */
public class ChangePasswordDto implements Serializable {

    private String username;

    private String newPassword;
    
    private String passwordcheck;

    private String confirmNewPassword;
    
    private String URL;
    
    private String tokenId;

    public ChangePasswordDto() {
		super();
	}

	public ChangePasswordDto(String username, String newPassword,
			String confirmNewPassword) {
		super();
		this.username = username;
		this.newPassword = newPassword;
		this.confirmNewPassword = confirmNewPassword;
	}

	/**
     * @return the username
     */
    public final String getUsername() {
        return username;
    }

    /**
     * @param username
     *            the username to set
     */
    public final void setUsername(final String username) {
        this.username = username;
    }

    /**
     * @return the newPassword
     */
    public final String getNewPassword() {
        return newPassword;
    }

    /**
     * @param newPassword
     *            the newPassword to set
     */
    public final void setNewPassword(final String newPassword) {
        this.newPassword = newPassword;
    }

    /**
     * @return the confirmNewPassword
     */
    public final String getConfirmNewPassword() {
        return confirmNewPassword;
    }

    /**
     * @param confirmNewPassword
     *            the confirmNewPassword to set
     */
    public final void setConfirmNewPassword(final String confirmNewPassword) {
        this.confirmNewPassword = confirmNewPassword;
    }

    public String getPasswordcheck() {
        return passwordcheck;
    }

    public void setPasswordcheck(String passwordcheck) {
        this.passwordcheck = passwordcheck;
        this.setNewPassword(passwordcheck);
    }

	public String getURL() {
		return URL;
	}

	public void setURL(String uRL) {
		URL = uRL;
	}

	/**
	 * @return the tokenId
	 */
	public String getTokenId() {
		return tokenId;
	}

	/**
	 * @param tokenId the tokenId to set
	 */
	public void setTokenId(String tokenId) {
		this.tokenId = tokenId;
	}

}
