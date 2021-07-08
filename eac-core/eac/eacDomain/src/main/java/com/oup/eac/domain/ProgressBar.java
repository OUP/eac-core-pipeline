package com.oup.eac.domain;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;

import org.hibernate.annotations.Sort;
import org.hibernate.annotations.SortType;

import com.oup.eac.domain.RegistrationActivation.ActivationStrategy;

@Entity
public class ProgressBar extends BaseDomainObject {

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "progressBar")
	@Sort(type = SortType.NATURAL)
	private Set<ProgressBarElement> elements = new HashSet<ProgressBarElement>();
	
	@Column(nullable = false)
	private String page;
	
	@Enumerated(EnumType.STRING)
	private UserState userState;
	
	@Enumerated(EnumType.STRING)
	private RegistrationState registrationState;
	
	@Enumerated(EnumType.STRING)
	private ActivationState activationState;
	
	@Enumerated(EnumType.STRING)
	private TokenState tokenState;
	
	@Enumerated(EnumType.STRING)
	private RegistrationType registrationType;
	
	@Enumerated(EnumType.STRING)
	private ActivationStrategy activationStrategy;
	
	@Enumerated(EnumType.STRING)
	private AccountValidated accountValidated;

	public ProgressBar() {
		super();
	}

	public ProgressBar(String page, UserState userState, RegistrationState registrationState, ActivationState activationState, TokenState tokenState, RegistrationType registrationType, ActivationStrategy activationStrategy,
	        AccountValidated accountValidated) {
		super();
		this.page = page;
		this.userState = userState;
		this.registrationState = registrationState;
		this.activationState = activationState;
		this.tokenState = tokenState;
		this.registrationType = registrationType;
		this.activationStrategy = activationStrategy;
		this.accountValidated = accountValidated;
	}

	/**
	 * @return the page
	 */
	public String getPage() {
		return page;
	}

	/**
	 * @param page the page to set
	 */
	public void setPage(final String page) {
		this.page = page;
	}

	/**
	 * @return the userState
	 */
	public UserState getUserState() {
		return userState;
	}

	/**
	 * @param userState the userState to set
	 */
	public void setUserState(final UserState userState) {
		this.userState = userState;
	}

	/**
	 * @return the registrationState
	 */
	public RegistrationState getRegistrationState() {
		return registrationState;
	}

	/**
	 * @param registrationState the registrationState to set
	 */
	public void setRegistrationState(final RegistrationState registrationState) {
		this.registrationState = registrationState;
	}

	/**
	 * @return the activationState
	 */
	public ActivationState getActivationState() {
		return activationState;
	}

	/**
	 * @param activationState the activationState to set
	 */
	public void setActivationState(final ActivationState activationState) {
		this.activationState = activationState;
	}

	/**
	 * @return the tokenState
	 */
	public TokenState getTokenState() {
		return tokenState;
	}

	/**
	 * @param tokenState the tokenState to set
	 */
	public void setTokenState(final TokenState tokenState) {
		this.tokenState = tokenState;
	}

	/**
	 * @return the registrationType
	 */
	public RegistrationType getRegistrationType() {
		return registrationType;
	}

	/**
	 * @param registrationType the registrationType to set
	 */
	public void setRegistrationType(final RegistrationType registrationType) {
		this.registrationType = registrationType;
	}

	/**
	 * @return the activationStrategy
	 */
	public ActivationStrategy getActivationStrategy() {
		return activationStrategy;
	}

	/**
	 * @param activationStrategy the activationStrategy to set
	 */
	public void setActivationStrategy(final ActivationStrategy activationStrategy) {
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

    /**
	 * @return the elements
	 */
	public Set<ProgressBarElement> getElements() {
		return elements;
	}
	
	public void addElement(final ProgressBarElement element) {
		elements.add(element);
	}
}
