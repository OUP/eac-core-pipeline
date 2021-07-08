package com.oup.eac.domain.migrationtool;

import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import com.oup.eac.domain.BaseDomainObject;

/**
 * @author Chirag Joshi
 */
@Entity
@Immutable
public class CustomerMigrationData extends BaseDomainObject {
    
    @OneToMany(fetch=FetchType.LAZY, mappedBy="customerMigrationData")
    private Set<RegistrationMigrationData> registrationMigrationData = new HashSet<RegistrationMigrationData>();
   
    @Column(nullable = false)
    private String username;
    
    @Column(nullable = true)
    private String password;
    
    @Column(nullable = true)
    private String firstName;
    
    @Column(nullable = true)
    private String lastName;
    
    @Column(nullable = true)
    private String emailAddress;
    
    @Column(nullable = false)
    private boolean resetPassword;
    
    @Column(nullable = false)
    private int failedAttempts;

    @Column(nullable = false)
    private boolean locked;
    
    @Column(nullable = true)
    private String customerType;
    
    @Column(nullable = true)
    private boolean enabled = true;
    
    @Column(nullable = true)
    private Locale locale;
    
    @Column(name="time_zone")
    private String timeZone;
    
    @Type(type = "org.joda.time.contrib.hibernate.PersistentDateTime")
    @Column(name="last_login")
    private DateTime lastLogin;
    
    @Column(nullable = true)
    private String emailVerificationState;
    
    @Column(nullable = true)
    private String externalId;
    
    @Column(nullable = true)
    private String column1;
    
    @Column(nullable = true)
    private String column2;
    
    @Column(nullable = true)
    private String column3;
    
    @Column(nullable = true)
    private String column4;
    
    @Column(nullable = true)
    private String column5;
    
    @Type(type = "org.joda.time.contrib.hibernate.PersistentDateTime")
    @Column(nullable = false)
    private DateTime createdDate;
    
    @Type(type = "org.joda.time.contrib.hibernate.PersistentDateTime")
    @Column(nullable = true)
    private DateTime updatedDate;
    
    @Override
    public String toString(){
    	return ToStringBuilder.reflectionToString(this);
    }

    public Set<RegistrationMigrationData> getRegistrationMigrationData() {
        return registrationMigrationData;
    }

    public void setRegistrationMigrationData(
            Set<RegistrationMigrationData> registrationMigrationData) {
        this.registrationMigrationData = registrationMigrationData;
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

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public boolean isResetPassword() {
        return resetPassword;
    }

    public void setResetPassword(boolean resetPassword) {
        this.resetPassword = resetPassword;
    }

    public int getFailedAttempts() {
        return failedAttempts;
    }

    public void setFailedAttempts(int failedAttempts) {
        this.failedAttempts = failedAttempts;
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    public String getCustomerType() {
        return customerType;
    }

    public void setCustomerType(String customerType) {
        this.customerType = customerType;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
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

    public DateTime getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(DateTime lastLoginDateTime) {
        this.lastLogin= lastLoginDateTime;
    }

    public String getEmailVerificationState() {
        return emailVerificationState;
    }

    public void setEmailVerificationState(String emailVerificationState) {
        this.emailVerificationState = emailVerificationState;
    }

    public String getExternalId() {
        return externalId;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }

    public String getColumn1() {
        return column1;
    }

    public void setColumn1(String column1) {
        this.column1 = column1;
    }

    public String getColumn2() {
        return column2;
    }

    public void setColumn2(String column2) {
        this.column2 = column2;
    }

    public String getColumn3() {
        return column3;
    }

    public void setColumn3(String column3) {
        this.column3 = column3;
    }

    public String getColumn4() {
        return column4;
    }

    public void setColumn4(String column4) {
        this.column4 = column4;
    }

    public String getColumn5() {
        return column5;
    }

    public void setColumn5(String column5) {
        this.column5 = column5;
    }
    
    public DateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(DateTime createdDate) {
        this.createdDate = createdDate;
    }

    public DateTime getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(DateTime updatedDate) {
        this.updatedDate = updatedDate;
    }
}
