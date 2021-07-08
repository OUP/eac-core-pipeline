package com.oup.eac.dto;

import java.util.Locale;


/**
 * @author harlandd Account registration dto.
 */
public class AccountRegistrationDto extends RegistrationDto {

    private String username;

    private String confirmPassword;

    private String password;
    
    private String passwordcheck;

    private String firstName;

    private String familyName;

	private String email;

    private String timeZoneId;
    
    private Locale userLocale;
    
    private boolean disableInputFlag;
    
    public boolean getDisableInputFlag() {
		return disableInputFlag;
	}

	public void setDisableInputFlag(boolean region) {
		this.disableInputFlag = region;
	}

	/**
     * Gets the user locale.
     *
     * @return the user locale
     */
    public final Locale getUserLocale() {
        return userLocale;
    }

    /**
     * Sets the user locale.
     *
     * @param userLocaleP the new user locale
     */
    public final void setUserLocale(final Locale userLocaleP) {
        this.userLocale = userLocaleP;
    }

    /**
     * @return the first name
     */
    public final String getFirstName() {
        return firstName;
    }

    /**
     * @param firstNameP
     *            the first name
     */
    public final void setFirstName(final String firstNameP) {
        this.firstName = firstNameP;
    }

    /**
     * @return the family name
     */
    public final String getFamilyName() {
        return familyName;
    }

    /**
     * @param familyNameP
     *            the family name
     */
    public final void setFamilyName(final String familyNameP) {
        this.familyName = familyNameP;
    }

    /**
	 * Gets the email address.
	 * 
	 * @return The email address.
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * Sets the email address.
	 * 
	 * @param emailAddress
	 *            The email address.
	 */
	public void setEmail(String emailAddress) {
		this.email = emailAddress;
	}

	/**
	 * @return the username
	 */
    public final String getUsername() {
        return username;
    }

    /**
     * @param usernameP
     *            the username
     */
    public final void setUsername(final String usernameP) {
        this.username = usernameP;
    }

    /**
     * @return the password
     */
    public final String getPassword() {
        return password;
    }

    /**
     * @param passwordP
     *            the password
     */
    public final void setPassword(final String passwordP) {
        this.password = passwordP;
    }

    /**
     * @return the confirm password
     */
    public final String getConfirmPassword() {
        return confirmPassword;
    }

    /**
     * @param confirmPasswordP
     *            the confirm password
     */
    public final void setConfirmPassword(final String confirmPasswordP) {
        this.confirmPassword = confirmPasswordP;
    }

    /**
     * @return full name
     */
    public final String getFullName() {
        return getFirstName() + " " + getFamilyName();
    }

    /**
     * Gets the time zone id.
     *
     * @return the time zone id
     */
    public final String getTimeZoneId() {
        return timeZoneId;
    }

    /**
     * Sets the time zone id.
     *
     * @param timeZoneIdP the new time zone id
     */
    public final void setTimeZoneId(final String timeZoneIdP) {
        this.timeZoneId = timeZoneIdP;
    }
    
    public String getPasswordcheck() {
        return passwordcheck;
    }

    public void setPasswordcheck(String passwordcheck) {
        this.passwordcheck = passwordcheck;
        this.setPassword(passwordcheck);
    }
}
