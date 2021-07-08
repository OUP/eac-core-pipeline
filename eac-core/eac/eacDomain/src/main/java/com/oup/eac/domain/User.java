package com.oup.eac.domain;

import java.io.UnsupportedEncodingException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import javax.mail.internet.InternetAddress;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.StringUtils;
import org.hibernate.annotations.Index;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Entity
@Table (name="customer")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "USER_TYPE", discriminatorType = DiscriminatorType.STRING)
public abstract class User extends OUPUpdatedAudit implements UserDetails {

    public static enum EmailVerificationState {
        UNKNOWN,
        EMAIL_SENT,
        VERIFIED;
    }
    
    public static enum UserType {
        ADMIN, CUSTOMER;
    }
    
    private String firstName;

    private String familyName;

    @Column(nullable = true, unique = true)
    @Index(name = "customer_username_idx")
    private String username;

    @Type(type = "com.oup.eac.persistence.Sha256PasswordUserType")
    @Column(name="password")
    private Password password; 

    @Index(name = "customer_email_idx")
    private String emailAddress;

    @Transient
    @Enumerated(EnumType.STRING)
    private EmailVerificationState emailVerificationState = EmailVerificationState.UNKNOWN;

    @Column(nullable = true)
    private boolean locked;
    
    //@Transient
    @Column(nullable = true)
    private boolean resetPassword;
    
    //@Transient
    private int failedAttempts;
    
    //@Transient
    @Column(nullable = true)
    private boolean enabled = true;
    
    @Transient
    private Locale locale;
    
    @Transient
    private String timeZone;
    
    /*@Type(type = "org.joda.time.contrib.hibernate.PersistentDateTime")
    @Column(name="last_login")*/
    @Transient
    private DateTime lastLoginDateTime;
    
    @ManyToMany
    @JoinTable(joinColumns = { @JoinColumn(name = "customer_id") }, 
    	inverseJoinColumns = { @JoinColumn(name = "role_id") })
    private Set<Role> roles = new HashSet<Role>();
    
    /**
     * Default constructor.
     */
    public User() {
        super();
    }
    
    public User(final String firstName, final String familyName, final String username, final Password password) {
        this.firstName = firstName;
        this.familyName = familyName;
        this.username = username;
        this.password = password;
    }
    
    /**
     * @return first name
     */
	public String getFirstName() {
        return firstName;
    }

    /**
     * @param firstName
     *            first name
     */
	public void setFirstName(final String firstName) {
        this.firstName = firstName;
    }

    /**
     * @return family name
     */
	public String getFamilyName() {
        return familyName;
    }

    /**
     * @param familyName
     *            family name
     */
	public void setFamilyName(final String familyName) {
        this.familyName = familyName;
    }

    /**
     * @return full name
     */
	public String getFullName() {
        return getFirstName() + " " + getFamilyName();
    }

    /**
     * @return username the username
     */
	public String getUsername() {
        return username;
    }

    /**
     * @param username
     *            the username
     */
	public void setUsername(final String username) {
        //TODO we should revisit this when we store a locale against the user.
        this.username = username == null ? null : username.toLowerCase();        
    }

    /**
     * @return the emailVerificationState
     */
    public final EmailVerificationState getEmailVerificationState() {
        return emailVerificationState;
    }

    /**
     * @param emailVerificationState the emailVerificationState to set
     */
    public final void setEmailVerificationState(final EmailVerificationState emailVerificationState) {
        this.emailVerificationState = emailVerificationState;
    }
    
    /**
     * @return email verified
     */
    public boolean isEmailVerified() {
        return emailVerificationState == EmailVerificationState.VERIFIED;
    }

    /**
     * @return reset password
     */
	public boolean isResetPassword() {
        return resetPassword;
    }

    /**
     * @param resetPassword
     *            reset password
     */
	public void setResetPassword(final boolean resetPassword) {
        this.resetPassword = resetPassword;
    }

    /**
     * @return email address
     */
	public String getEmailAddress() {
        return emailAddress;
    }

    /**
     * @param emailAddress
     *            email address
     */
	public void setEmailAddress(final String emailAddress) {
        this.emailAddress = StringUtils.lowerCase(emailAddress);
    }
    
    /**
     * @return the email internet address
     * @throws UnsupportedEncodingException
     *             if the encoding is wrong
     */
	public InternetAddress getEmailInternetAddress() throws UnsupportedEncodingException {
        return new InternetAddress(getEmailAddress(), getFullName());
    }

	/**
     * Get the number of failed attempts.
     * 
     * @return number of failed attempts
     */
    public int getFailedAttempts() {
        return failedAttempts;
    }

    /**
     * Set the number of failed attempts.
     * 
     * @param failedAttempts
     *            The number of failed attempts to set
     */
    public void setFailedAttempts(int failedAttempts) {
        this.failedAttempts = failedAttempts;
    }

    /**
     * Ask if the customer is locked.
     * 
     * @return true if the customer is locked
     */
    public boolean isLocked() {
        return locked;
    }

    /**
     * Set the customer to be locked or unlocked. When the
     * customer is unlocked, their failed login attempts is reset.
     * 
     * @param locked
     *            The new state, true if customer should be locked
     */
    public void setLocked(boolean locked) {
        this.locked = locked;
        if (!locked) {
            failedAttempts = 0;
        }
    }

    /**
     * Register a failed login attempt. If the number
     * of failed login attempts is greater than or equal to 
     * maxFailedAttempts, the customer should be locked.
     * 
     * @param maxFailedAttempts
     *            The number of failed attempts allowed before a customer is locked
     */
    public void registerFailedAttempt(final int maxFailedAttempts) {
    	if (trackFailedLoginAttempts()) {
    	    failedAttempts++;
    	}
        if (failedAttempts >= maxFailedAttempts ) {
            setLocked(true);
        }
    }
    
    protected boolean trackFailedLoginAttempts() {
    	return true;
    }
    
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !isLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }
    
    public void setEnabled(final boolean enabled) {
    	this.enabled = enabled;
    }
    
    public abstract UserType getUserType();
    
    public Password getWrappedPassword() {
        return this.password;
    }

    @Override
    @Transient
    public String getPassword() {
        if (password == null)
            return null;
        return password.getValue();
    }

    public void setPassword(Password password) {
        this.password = password;
    }
    
    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    public String getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }
    
    public Set<Role> getRoles() {
    	return roles;
    }
    
    public void setRoles(Set<Role> roles) {
    	this.roles = roles;
    }

	public DateTime getLastLoginDateTime() {
		return lastLoginDateTime;
	}

	public void setLastLoginDateTime(DateTime lastLoginDateTime) {
		this.lastLoginDateTime = lastLoginDateTime;
	}
}
