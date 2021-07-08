package com.oup.eac.common.utils.security;

import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.authentication.dao.SaltSource;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.util.Assert;

public class AuthenticationProvider extends AbstractUserDetailsAuthenticationProvider{
	
	private org.springframework.security.authentication.encoding.Md5PasswordEncoder Md5passwordEncoder;
	private org.springframework.security.crypto.password.StandardPasswordEncoder sha256passwordEncoder;
	private SaltSource saltSource;
	private UserDetailsService userDetailsService;

	public AuthenticationProvider() {
		this.sha256passwordEncoder = new StandardPasswordEncoder();
		this.Md5passwordEncoder = new Md5PasswordEncoder();
	}

	protected void additionalAuthenticationChecks(UserDetails userDetails,
			UsernamePasswordAuthenticationToken authentication)
			throws AuthenticationException {
		Object salt = null;

		if (this.saltSource != null) {
			salt = this.saltSource.getSalt(userDetails);
		}

		if (authentication.getCredentials() == null) {
			this.logger.debug("Authentication failed: no credentials provided");

			throw new BadCredentialsException(this.messages.getMessage(
					"AbstractUserDetailsAuthenticationProvider.badCredentials",
					"Bad credentials"), userDetails);
		}

		String presentedPassword = authentication.getCredentials().toString();
		 if(this.sha256passwordEncoder.matches( presentedPassword,userDetails.getPassword())){
		 }else if(this.Md5passwordEncoder.isPasswordValid(userDetails.getPassword(),
				presentedPassword, salt)){
		 }else{
			this.logger
					.debug("Authentication failed: password does not match stored value");

			throw new BadCredentialsException(this.messages.getMessage(
					"AbstractUserDetailsAuthenticationProvider.badCredentials",
					"Bad credentials"), userDetails);
		}
	}

	protected void doAfterPropertiesSet() throws Exception {
		Assert.notNull(this.userDetailsService,
				"A UserDetailsService must be set");
	}

	protected final UserDetails retrieveUser(String username,
			UsernamePasswordAuthenticationToken authentication)
			throws AuthenticationException {
		UserDetails loadedUser;
		try {
			loadedUser = getUserDetailsService().loadUserByUsername(username);
		} catch (UsernameNotFoundException notFound) {
			throw notFound;
		} catch (Exception repositoryProblem) {
			throw new AuthenticationServiceException(
					repositoryProblem.getMessage(), repositoryProblem);
		}

		if (loadedUser == null) {
			throw new AuthenticationServiceException(
					"UserDetailsService returned null, which is an interface contract violation");
		}

		return loadedUser;
	}

	public void setPasswordEncoder(Object passwordEncoder) {
		Assert.notNull(sha256passwordEncoder, "passwordEncoder cannot be null");

		if (passwordEncoder instanceof org.springframework.security.crypto.password.StandardPasswordEncoder) {
			this.sha256passwordEncoder = ((org.springframework.security.crypto.password.StandardPasswordEncoder) sha256passwordEncoder);
			return;
		}
		
		if (passwordEncoder instanceof org.springframework.security.authentication.encoding.Md5PasswordEncoder) {
			this.Md5passwordEncoder = ((org.springframework.security.authentication.encoding.Md5PasswordEncoder) Md5passwordEncoder);
			return;
		}

		/*if (passwordEncoder instanceof org.springframework.security.crypto.password.PasswordEncoder) {
			org.springframework.security.crypto.password.PasswordEncoder delegate = (org.springframework.security.crypto.password.PasswordEncoder) passwordEncoder;

			this.passwordEncoder = new org.springframework.security.authentication.encoding.PasswordEncoder(
					delegate) {
				public String encodePassword(String rawPass, Object salt) {
					checkSalt(salt);
					return this.val$delegate.encode(rawPass);
				}

				public boolean isPasswordValid(String encPass, String rawPass,
						Object salt) {
					checkSalt(salt);
					return this.val$delegate.matches(rawPass, encPass);
				}

				private void checkSalt(Object salt) {
					Assert.isNull(salt,
							"Salt value must be null when used with crypto module PasswordEncoder");
				}
			};
			return;
		}*/

		throw new IllegalArgumentException(
				"passwordEncoder must be a PasswordEncoder instance");
	}

	protected org.springframework.security.crypto.password.StandardPasswordEncoder getPasswordEncoder() {
		return this.sha256passwordEncoder;
	}

	public void setSaltSource(SaltSource saltSource) {
		this.saltSource = saltSource;
	}

	protected SaltSource getSaltSource() {
		return this.saltSource;
	}

	public void setUserDetailsService(UserDetailsService userDetailsService) {
		this.userDetailsService = userDetailsService;
	}

	protected UserDetailsService getUserDetailsService() {
		return this.userDetailsService;
	}
	
}
