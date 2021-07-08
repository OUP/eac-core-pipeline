package com.oup.eac.dto;


/**
 * Contains username and password combination. The password
 * should be in an unencrypted/unhashed format.
 *  
 * @author Ian Packard
 *
 */
public class LoginPasswordCredentialDto implements CredentialDto {

	private String username;
	private String password;
	
	public LoginPasswordCredentialDto(String username, String password) {
		this.username = username;
		this.password = password;
	}

	public String getUsername() {
		return username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
}
