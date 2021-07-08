package com.oup.eac.domain;

import com.oup.eac.domain.RegistrationActivation.ActivationStrategy;


public class ProgressState {
	private UserState userState;
	private RegistrationState registrationState;
	private ActivationState activationState;
	private TokenState tokenState;
	private RegistrationType registrationType;
	private ActivationStrategy activationStrategy;
	private AccountValidated accountValidated;

	/**
	 * @return the userState
	 */
	public final UserState getUserState() {
		return userState;
	}
	/**
	 * @param userState the userState to set
	 */
	public final void setUserState(UserState userState) {
		this.userState = userState;
	}
	/**
	 * @return the registrationState
	 */
	public final RegistrationState getRegistrationState() {
		return registrationState;
	}
	/**
	 * @param registrationState the registrationState to set
	 */
	public final void setRegistrationState(RegistrationState registrationState) {
		this.registrationState = registrationState;
	}
	/**
	 * @return the activationState
	 */
	public final ActivationState getActivationState() {
		return activationState;
	}
	/**
	 * @param activationState the activationState to set
	 */
	public final void setActivationState(ActivationState activationState) {
		this.activationState = activationState;
	}
	/**
	 * @return the tokenState
	 */
	public final TokenState getTokenState() {
		return tokenState;
	}
	/**
	 * @param tokenState the tokenState to set
	 */
	public final void setTokenState(TokenState tokenState) {
		this.tokenState = tokenState;
	}

	/**
	 * @return the registrationType
	 */
	public final RegistrationType getRegistrationType() {
		return registrationType;
	}
	/**
	 * @param registrationType the registrationType to set
	 */
	public final void setRegistrationType(RegistrationType registrationType) {
		this.registrationType = registrationType;
	}
	/**
	 * @return the activationStrategy
	 */
	public final ActivationStrategy getActivationStrategy() {
		return activationStrategy;
	}
	/**
	 * @param activationStrategy the activationStrategy to set
	 */
	public final void setActivationStrategy(ActivationStrategy activationStrategy) {
		this.activationStrategy = activationStrategy;
	}
	
	/**
     * @return the accountValidated
     */
    public final AccountValidated getAccountValidated() {
        return accountValidated;
    }
    /**
     * @param accountValidated the accountValidated to set
     */
    public final void setAccountValidated(AccountValidated accountValidated) {
        this.accountValidated = accountValidated;
    }
    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "ProgressState ["
                + (userState != null ? "userState=" + userState + ", " : "")
                + (registrationState != null ? "registrationState="
                        + registrationState + ", " : "")
                + (activationState != null ? "activationState="
                        + activationState + ", " : "")
                + (tokenState != null ? "tokenState=" + tokenState + ", " : "")
                + (registrationType != null ? "registrationType="
                        + registrationType + ", " : "")
                + (activationStrategy != null ? "activationStrategy="
                        + activationStrategy + ", " : "")
                + (accountValidated != null ? "accountValidated="
                        + accountValidated : "") + "]";
    }



}
