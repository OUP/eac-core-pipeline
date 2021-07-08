package com.oup.eac.dto;

/**
 * Credential that captures the re-use constraints of hashed passwords.
 * 
 * @author Ian Packard
 *
 */
public class HashedLoginPasswordCredentialDto extends LoginPasswordCredentialDto {

	public HashedLoginPasswordCredentialDto(final String username) {
		super(username, null);
	}
	
	@Override
	public void setPassword(final String password) {
		throw new UnsupportedOperationException("Credential is readonly");
	}
	
	@Override
	public String getPassword() {
		throw new IllegalStateException("Hashed password credential cannot be used.");
	}
}
