package com.oup.eac.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.joda.time.DateTime;
import org.springframework.util.Assert;

import com.oup.eac.domain.Customer;
import com.oup.eac.domain.Customer.CustomerType;
import com.oup.eac.domain.ExternalCustomerId;
import com.oup.eac.domain.User.EmailVerificationState;
import com.oup.eac.domain.User.UserType;

/**
 * Represents the data transfer between eac and erights and imposes some business
 * rules to enforce consistency of data between both domains.
 * 
 * @author Ian Packard
 * @author Will Keeling
 *
 */
public class CustomerDto implements Serializable {
	
	private List<String> groupIds = new ArrayList<String>();
	
	private Integer concurrency;
	
	private LoginPasswordCredentialDto loginPasswordCredential;
	
	//private Integer erightsId;
	
	private String username;
	
	private boolean suspended;
	
	private UserType userType= UserType.CUSTOMER;
	
	private String userId;
	
	private String emailAddress;
	
	private String firstName;
	
	private String familyName;
	
	private EmailVerificationState emailVerificationState = EmailVerificationState.UNKNOWN;
	
	private String timeZone;
	
	private Locale locale;
	
	private Integer failedLoginAttemptes;
	
	/*Added for externalId with getters and setters*/
	private Set<ExternalCustomerId> externalIds = new HashSet<ExternalCustomerId>();
	
	private boolean resetPassword;
	
	private DateTime lastLoginDateTime;
	
	private DateTime createdDateTime;
	
	private boolean isTncAccepted;
	
	protected boolean privacyPolicyAccepted;
	
	/**
	 * Create a customer dto with a new erights id, using data from
	 * and existing customer dto.
	 * 
	 * @param erightsId
	 * @param customerDto
	 */
	public CustomerDto(final String userId , final CustomerDto customerDto) {
		this(customerDto.username, customerDto.groupIds, customerDto.concurrency, customerDto.loginPasswordCredential, customerDto.suspended, customerDto.userType, userId, customerDto.emailAddress, customerDto.firstName, customerDto.familyName, customerDto.emailVerificationState, customerDto.timeZone, customerDto.locale, customerDto.getExternalIds(), customerDto.isResetPassword());
	}
	
	/**
	 * Create a customer dto with data returned from eRights.
	 * 
	 * @param customer
	 * @param groupIds
	 * @param concurrency
	 */
	/*public CustomerDto(final Integer erightsId, final String username, final List<Integer> groupIds, final Integer concurrency, final LoginPasswordCredentialDto loginPasswordCredential, final boolean suspended) {		
		this.erightsId = erightsId;
		this.groupIds = groupIds;
		this.concurrency = concurrency;
		this.loginPasswordCredential = loginPasswordCredential;
		this.username = username;
		this.suspended = suspended;
	}*/
	
	public CustomerDto(final String username, final List<String> groupIds, final Integer concurrency, final LoginPasswordCredentialDto loginPasswordCredential, final boolean suspended,
			UserType userType,
			String userId,
			String emailAddress,
			String firstName,
			String familyName,
			EmailVerificationState emailVerificationState,
			String timeZone,
			Locale locale, Set<ExternalCustomerId> externalIds,
			boolean resetPassword) {		
		this.groupIds = groupIds;
		this.concurrency = concurrency;
		this.loginPasswordCredential = loginPasswordCredential;
		this.username = username;
		this.suspended = suspended;
		this.userType = userType;
		this.userId = userId;
		this.emailAddress = emailAddress;
		this.firstName = firstName;
		this.familyName = familyName;
		this.emailVerificationState = emailVerificationState;
		this.timeZone = timeZone;
		this.locale = locale;
		this.externalIds = externalIds;
		this.resetPassword = resetPassword;
	}
	
	/**
	 * Create a customer dto with new data from eac and no groups. Not for updating.
	 * 
	 * @param customer
	 * @param concurrency
	 */
	public CustomerDto(final Customer customer) {
		this(customer, null, customer.getCustomerType());
		
	}	
	
	/**
	 * Create a customer dto with new data from eac and no groups. Not for updating.
	 * 
	 * @param customer
	 * @param concurrency
	 */
	public CustomerDto(final Customer customer, final CustomerType customerType){
		this(customer, null, customerType);
	}
	
	/**
	 * Create a customer dto with new data from eac including groups. Not for udpadting
	 * 
	 * @param customer
	 * @param groupIds
	 */
	public CustomerDto(final Customer customer, final List<String> groupIds) {
		this(customer, groupIds, customer.getCustomerType());
	}	
	
	/**
	 * Create a customer dto with data from eac.
	 * 
	 * @param customer
	 * @param groupIds
	 * @param concurrency
	 */
	public CustomerDto(final Customer customer, final List<String> groupIds, final CustomerType customerType) {
		Assert.notNull(customer);
		//this.erightsId = customer.getErightsId();
		this.groupIds = (groupIds != null) ? groupIds : this.groupIds ;
		if(customerType== CustomerType.SPECIFIC_CONCURRENCY)
			this.concurrency = customerType.getConcurrency();
		else if(customerType== CustomerType.SHARED)
			this.concurrency = -1;
		else if(customerType== CustomerType.SELF_SERVICE)
			//this.concurrency = 0;
			this.concurrency = customerType.getConcurrency();
		resetLoginPassword(customer);
		this.username = customer.getUsername();
		this.suspended = customer.isSuspended();
		this.userId=customer.getId();
		
		if(customer.getCustomerUserType().equals(UserType.CUSTOMER))
			this.userType=UserType.CUSTOMER;
		else if(customer.getCustomerUserType().equals(UserType.ADMIN))
			this.userType=UserType.ADMIN;
		
		this.emailAddress=customer.getEmailAddress();
		this.firstName=customer.getFirstName();
		this.familyName=customer.getFamilyName();
		
		if(customer.getEmailVerificationState().equals(EmailVerificationState.UNKNOWN))
			this.emailVerificationState=EmailVerificationState.UNKNOWN;
		else if(customer.getEmailVerificationState().equals(EmailVerificationState.VERIFIED))
			this.emailVerificationState=EmailVerificationState.VERIFIED;
		else if(customer.getEmailVerificationState().equals(EmailVerificationState.EMAIL_SENT))
			this.emailVerificationState=EmailVerificationState.EMAIL_SENT;
		
		this.timeZone=customer.getTimeZone();
		this.locale=customer.getLocale();
		this.resetPassword = customer.isResetPassword();
		//Added for customer ExternalID in atypon
		this.externalIds=customer.getExternalIds();
	}

	/**
	 * If an update is required to groups they can be made by changing this list.
	 *
	 * @return
	 */
	public List<String> getGroupIds() {
		return groupIds;
	}

	/**
	 * The users concurrency.
	 * 
	 * @return
	 */
	public Integer getConcurrency() {
		return concurrency;
	}	
	
	/**
	 * Obtain the login/password credential. If the credential has
	 * been returned from erights it will initially be a HashedLoginPasswordCredential.
	 * 
	 * Any updates to the credential should be performed by first updating the customer
	 * and then passing the customer to mergeCustomerChanges.
	 * 
	 * @return
	 */
	public LoginPasswordCredentialDto getLoginPasswordCredential() {
		return loginPasswordCredential;
	}
	
	/**
	 * Gets the username of the customer in erights.
	 * 
	 * @return
	 */
	public String getUsername() {
		return username;
	}
	
	/**
	 * Identifies if the customer is enabled/disabled. Suspended customers
	 * cannot login to erights. This is synonymous to enabled/disabled in eac.
	 * 
	 * @return
	 */
	public boolean isSuspended() {
		return suspended;
	}
	
	

	public UserType getUserType() {
		return userType;
	}

	public void setUserType(UserType userType) {
		this.userType = userType;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getFamilyName() {
		return familyName;
	}

	public void setFamilyName(String familyName) {
		this.familyName = familyName;
	}

	public EmailVerificationState getEmailVerificationState() {
		return emailVerificationState;
	}

	public void setEmailVerificationState(
			EmailVerificationState emailVerificationState) {
		this.emailVerificationState = emailVerificationState;
	}

	public String getTimeZone() {
		return timeZone;
	}

	public void setTimeZone(String timeZone) {
		this.timeZone = timeZone;
	}

	public Locale getLocale() {
		return locale;
	}

	public void setLocale(Locale locale) {
		this.locale = locale;
	}

	public boolean isResetPassword() {
		return resetPassword;
	}

	public void setResetPassword(boolean resetPassword) {
		this.resetPassword = resetPassword;
	}

	public DateTime getLastLoginDateTime() {
		return lastLoginDateTime;
	}

	public void setLastLoginDateTime(DateTime lastLoginDateTime) {
		this.lastLoginDateTime = lastLoginDateTime;
	}

	/**
	 * @return the externalIds
	 */
	public Set<ExternalCustomerId> getExternalIds() {
		return externalIds;
	}

	/**
	 * @param externalIds the externalIds to set
	 */
	public void setExternalIds(Set<ExternalCustomerId> externalIds) {
		this.externalIds = externalIds;
	}

	/**
	 * Update a the customer dto with username and password of the customer. The underlying credential is replaced.
	 * The empty string is used by erights to determine if a change to password is required.
	 * 
	 * @param customer
	 * @return
	 */
	public LoginPasswordCredentialDto resetLoginPassword(final Customer customer) {
		//Assert.notNull(customer.getWrappedPassword(), "Customer must have a password");
		if(customer.getPassword()!=null){
		String password = (customer.getWrappedPassword().isHashed() ? "" : customer.getPassword());
		//Create credential using existing id if available
		this.loginPasswordCredential = new LoginPasswordCredentialDto(customer.getUsername(), password);
		}
		else
			this.loginPasswordCredential = new LoginPasswordCredentialDto(customer.getUsername(), "");
		return this.loginPasswordCredential;
	}
	
	/**
	 * Merge in changes from a customer to this dto. Also updates the 
	 * login/password credential. If the password has changed that will 
	 * also be updated (customer password is not hashed).
	 * 
	 * @param customer
	 */
	public void mergeCustomerChanges(final Customer customer, boolean updateCreds) {
		
		if (!updateCreds) {
			this.suspended = customer.isLocked();
			this.concurrency = customer.getCustomerType().getConcurrency();
			this.externalIds=customer.getExternalIds();
			this.resetPassword = customer.isResetPassword();
			this.firstName = customer.getFirstName();
			this.familyName = customer.getFamilyName();
			this.emailAddress = customer.getEmailAddress();
			this.emailVerificationState = customer.getEmailVerificationState();
		} else {
			this.username = customer.getUsername();
			this.loginPasswordCredential.setUsername(customer.getUsername());
		}
		resetLoginPassword(customer);
	}

	public Integer getFailedLoginAttemptes() {
		return failedLoginAttemptes;
	}

	public void setFailedLoginAttemptes(Integer failedLoginAttemptes) {
		this.failedLoginAttemptes = failedLoginAttemptes;
	}

	/**
	 * @return the createdDateTime
	 */
	public DateTime getCreatedDateTime() {
		return createdDateTime;
	}

	/**
	 * @param createdDateTime the createdDateTime to set
	 */
	public void setCreatedDateTime(DateTime createdDateTime) {
		this.createdDateTime = createdDateTime;
	}

	public boolean isTncAccepted() {
		return isTncAccepted;
	}

	public void setTncAccepted(boolean isTncAccepted) {
		this.isTncAccepted = isTncAccepted;
	}

	public void setSuspended(boolean suspended) {
		this.suspended = suspended;
	}

	public void setConcurrency(Integer concurrency) {
		this.concurrency = concurrency;
	}

	public void setLoginPasswordCredential(
			LoginPasswordCredentialDto loginPasswordCredential) {
		this.loginPasswordCredential = loginPasswordCredential;
	}

	public boolean isPrivacyPolicyAccepted() {
		return privacyPolicyAccepted;
	}

	public void setPrivacyPolicyAccepted(boolean privacyPolicyAccepted) {
		this.privacyPolicyAccepted = privacyPolicyAccepted;
	}
	
	
	
	
}
