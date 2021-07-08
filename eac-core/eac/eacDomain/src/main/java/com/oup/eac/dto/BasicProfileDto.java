package com.oup.eac.dto;

import java.util.Locale;

/**
 * Container for contents of Basic Profile form.
 * 
 * @author David Hay
 * 
 * @see com.oup.eac.web.validators.profile.BasicProfileValidator
 * @see com.oup.eac.web.controllers.profile.BasicProfileController
 * 
 */
public class BasicProfileDto {

    private String username;
    private String email;
    private String firstName;
    private String familyName;
    private Locale userLocale;
    private String timezone;
    private boolean readOnly;

    /**
     * @return the username
     */
    public final String getUsername() {
        return username;
    }

    /**
     * @param usernameP the username to set
     */
    public final void setUsername(final String usernameP) {
        this.username = usernameP;
    }

    /**
     * @return the email
     */
    public final String getEmail() {
        return email;
    }

    /**
     * @param emailP the email to set
     */
    public final void setEmail(final String emailP) {
        this.email = emailP;
    }

    /**
     * @return the firstName
     */
    public final String getFirstName() {
        return firstName;
    }

    /**
     * @param firstNameP the firstName to set
     */
    public final void setFirstName(final String firstNameP) {
        this.firstName = firstNameP;
    }

    /**
     * @return the familyName
     */
    public final String getFamilyName() {
        return familyName;
    }

    /**
     * @param familyNameP the familyName to set
     */
    public final void setFamilyName(final String familyNameP) {
        this.familyName = familyNameP;
    }

    /**
     * @return the userLocale
     */
    public final Locale getUserLocale() {
        return userLocale;
    }

    /**
     * @param userLocaleP the userLocale to set
     */
    public final void setUserLocale(final Locale userLocaleP) {
        this.userLocale = userLocaleP;
    }

    /**
     * @return the timezone
     */
    public final String getTimezone() {
        return timezone;
    }

    /**
     * @param timezoneP the timezone to set
     */
    public final void setTimezone(final String timezoneP) {
        this.timezone = timezoneP;
    }

	public boolean isReadOnly() {
		return readOnly;
	}

	public void setReadOnly(boolean readOnly) {
		this.readOnly = readOnly;
	}

    
}
