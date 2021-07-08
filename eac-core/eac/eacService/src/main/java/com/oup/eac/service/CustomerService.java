package com.oup.eac.service;

import java.util.List;
import java.util.Locale;
import java.util.Set;

import com.oup.eac.domain.AdminUser;
import com.oup.eac.domain.Answer;
import com.oup.eac.domain.Customer;
import com.oup.eac.domain.DivisionAdminUser;
import com.oup.eac.domain.ProductRegistrationDefinition;
import com.oup.eac.domain.Registration;
import com.oup.eac.domain.paging.Paging;
import com.oup.eac.domain.paging.PagingCriteria;
import com.oup.eac.dto.AccountRegistrationDto;
import com.oup.eac.dto.BasicProfileDto;
import com.oup.eac.dto.ChangePasswordDto;
import com.oup.eac.dto.CustomerDto;
import com.oup.eac.dto.CustomerSearchCriteria;
import com.oup.eac.dto.CustomerSessionDto;
import com.oup.eac.dto.CustomerWithLicencesAndProductsDto;
import com.oup.eac.dto.LicenceDto;
import com.oup.eac.dto.WsUserIdDto;
import com.oup.eac.integration.facade.exceptions.ErightsException;
import com.oup.eac.integration.facade.exceptions.ErightsSessionNotFoundException;
import com.oup.eac.integration.facade.exceptions.UserLoginCredentialAlreadyExistsException;
import com.oup.eac.integration.facade.exceptions.UserNotFoundException;
import com.oup.eac.service.exceptions.CustomerNotFoundServiceLayerException;
import com.oup.eac.service.exceptions.LicenceNotFoundServiceLayerException;
import com.oup.eac.service.exceptions.PasswordPolicyViolatedServiceLayerException;
import com.oup.eac.service.exceptions.UsernameExistsException;

/**
 * 
 * @author harlandd
 * 
 */
public interface CustomerService {

	/**
	 * @param productDefinition
	 *            the product definition
	 * @param customer
	 *            the customer
	 * @return the Registration
	 * @throws ServiceLayerException 
	 */
	Registration<ProductRegistrationDefinition> getRegistrationByRegistrationDefinitionAndCustomer(final ProductRegistrationDefinition registrationDefinition, Customer customer) throws ServiceLayerException;

	/**
	 * @param sessionKey
	 *            the session key
	 * @return the customer
	 * @throws ServiceLayerException
	 *             the exception
	 */
	Customer getCustomerFromSession(String sessionKey) throws CustomerNotFoundServiceLayerException;

	/**
	 * Gets the customer with answers by customer id.
	 *
	 * @param customerId the customer id
	 * @return the customer with answers by customer id
	 */
	Set<Answer> getCustomerWithAnswersByCustomerId(String customerId);

	/**
	 * Logout (for the web services application )
	 *
	 * @param session the erights session
	 * @throws ServiceLayerException the service layer exception
	 */
	void logout(String session) throws ServiceLayerException;


	/**
	 * Logout ( for the web application )
	 *
	 * @param customer the customer
	 * @param session the session
	 * @throws ServiceLayerException the service layer exception
	 */
	void logout(Customer customer, String session) throws ServiceLayerException;

	/**
	 * Gets the {@link Customer} from the supplied username. The username is not case sensitive.
	 * 
	 * @param username
	 *            the username
	 * @return the user
	 *             the exception
	 * @throws ErightsException 
	 */
	Customer getCustomerByUsername(final String username) throws ErightsException;

	/**
	 * @param username
	 *            the username
	 * @param plainTextPassword
	 *            the plain text password
	 * @return the user
	 * @throws ServiceLayerException
	 *             the exception
	 */
	CustomerSessionDto getCustomerByUsernameAndPassword(final String username, final String plainTextPassword) throws ServiceLayerException;

	/**
	 * Resets the users password and emails the password to them.
	 * 
	 * @param customer
	 *            the customer
	 * @param locale
	 *            the locale
	 * @throws ServiceLayerException
	 *             service layer exception
	 * @throws PasswordAlreadyExistsException 
	 */
	void updateResetCustomerPassword(final Customer customer, final Locale locale,final String reqtUrl, final String wsUserName) throws ServiceLayerException;

	/**
	 * Notifies the user when an account could not be located using the email
	 * address they entered.
	 * 
	 * @param emailAddress
	 *            The email address (username) the user entered.
	 * @param locale
	 *            The locale
	 * @throws ServiceLayerException
	 *             If there was a problem notifying the user.
	 */
	void notifyCustomerAccountNotFound(final String emailAddress, final Locale locale) throws ServiceLayerException;

	/**
	 * @param accountRegistrationFbo
	 *            account registraion dto
	 * @return the CustomerSessionDto
	 * @throws ServiceLayerException
	 *             the exception
	 */
	CustomerSessionDto saveCustomerRegistration(final AccountRegistrationDto accountRegistrationFbo) throws ServiceLayerException;

	/**
	 * @param changePasswordDto
	 *            the changePasswordDto
	 * @throws ServiceLayerException
	 *             the exception
	 * @throws PasswordAlreadyExistsException 
	 */
	void saveChangeCustomerPassword(final ChangePasswordDto changePasswordDto, final Customer customer) throws  PasswordPolicyViolatedServiceLayerException, ServiceLayerException;

	/**
	 * Search for customer matching the search criteria provided.
	 * 
	 * @param customerSearchCriteria
	 *            The {@link CustomerSearchCriteria}.
	 * @param pagingCriteria
	 *            The {@link PagingCriteria}
	 * @return A {@link Paging} object referencing the Customers found by the search.
	 * @throws ServiceLayerException
	 */
	Paging<Customer> searchCustomers(final CustomerSearchCriteria customerSearchCriteria, final PagingCriteria pagingCriteria)
			throws ServiceLayerException;

	/**
	 * Get a customer by their internal id.
	 * 
	 * @param id
	 * @return
	 */
	
	Customer getCustomerById(final String id);

	/**
	 * Gaurav Soni : Performance improvements CR
	 * Get a customer by their internal id.
	 * This call is specially made for WS calls only
	 * @param id
	 * @return
	 * @throws ErightsException 
	 */
	Customer getCustomerByIdWs(final String id) throws ErightsException;


	/**
	 * Save a customer.
	 *
	 * @param customer the customer
	 * @param generatePassword the generate password
	 * @throws ServiceLayerException the service layer exception
	 * @throws UsernameExistsException the username exists exception
	 * @throws ErightsException 
	 */
	CustomerDto saveCustomer(final Customer customer, final boolean generatePassword) throws ServiceLayerException, UsernameExistsException, ErightsException;

	/**
	 * Save customer part1.
	 *
	 * @param customer the customer
	 * @param generatePassword the generate password
	 * @throws ServiceLayerException the service layer exception
	 */
	/*
	 * Commented as a part of cusomer de-duplication
	 */
	void saveCustomerPart1(Customer customer, boolean generatePassword) throws ServiceLayerException;

	/**
	 * Save customer part2.
	 *
	 * @param customer the customer
	 * @param generatePassword the generate password
	 * @throws ServiceLayerException the service layer exception
	 * @throws ErightsException 
	 */
	CustomerDto saveCustomerPart2(Customer eacCustomer, Customer customer, boolean generatePassword) throws ServiceLayerException, ErightsException;

	/**
	 * Update the customers email verification state
	 * 
	 * @param customer the username
	 * @param emailVerificationState the new email verification state
	 * @throws ErightsException 
	 * @throws UserNotFoundException 
	 */
	void updateSendEmailValidationEmail(Customer customer, Locale locale, String forwardUrl) throws ServiceLayerException, UserNotFoundException, ErightsException;

	/**
	 * Validate the customer email address
	 * 
	 * @param tokenString
	 * @return the forward url
	 * @throws Exception
	 */
	String updateValidationEmail(final String tokenString) throws Exception;

	/**
	 * Update an existing customer.
	 * 
	 * @param customer
	 * @param generatePassword
	 * @throws ServiceLayerException
	 * @throws UserLoginCredentialAlreadyExistsException 
	 */
	void updateCustomer(final Customer customer, final boolean generatePassword) throws ServiceLayerException, UsernameExistsException, PasswordPolicyViolatedServiceLayerException, UserLoginCredentialAlreadyExistsException;
	
	void updateCustomerDetails(final Customer customer) throws ServiceLayerException, UsernameExistsException, UserLoginCredentialAlreadyExistsException;
	
	void updateCustomerCredentials(final Customer customer, final boolean generatePassword) throws ServiceLayerException, UserLoginCredentialAlreadyExistsException;

	void updateCustomerProfile(final String customerId, final BasicProfileDto customerDto, final boolean generatePassword) throws ServiceLayerException, UsernameExistsException, UserLoginCredentialAlreadyExistsException;
	
	public void updateExternalIds(final Customer customer)  throws ServiceLayerException, UserLoginCredentialAlreadyExistsException;

	/**
	 * Gets the customer by external customer id.
	 *
	 * @param systemId the system id
	 * @param typeId the type id
	 * @param externalId the external id
	 * @return the customer by external customer id
	*/
	//customer de-duplication
//	Customer getCustomerByExternalCustomerId(String systemId, String typeId, String externalId);


	/**
	 * Obtain a list of licences for a customer. The licence products are resolved and if the admin user does
	 * not have access to any product in on the licence, the licence will be removed from the results.
	 * 
	 * @param id
	 * 		Erights id of the customer to find.
	 * @param adminUser
	 * 		The admin user requesting data.
	 * @return
	 */
	CustomerWithLicencesAndProductsDto getCustomerWithLicencesAndProductsByAdminUser(final String id, final AdminUser adminUser) throws ServiceLayerException;

	/**
	 * Update an existing customer and their associated erights licences.
	 * 
	 * @param customer
	 * @param generatePassword
	 * @throws ServiceLayerException
	 * @throws UsernameExistsException
	 * @throws UserLoginCredentialAlreadyExistsException 
	 */
	void updateCustomerAndLicences(final Customer customer, final boolean generatePassword, final List<LicenceDto> licences) throws ServiceLayerException, UsernameExistsException, UserLoginCredentialAlreadyExistsException;

	/**
	 * Gets the customer by username and password ( for checking in db only ).
	 *
	 * @param username the username
	 * @param plainTextPassword the plain text password
	 * @param checkEacDb the check eac db
	 * @param checkAtypon the check atypon
	 * @return the customer by username and password
	 * @throws ServiceLayerException the service layer exception
	 */
	CustomerSessionDto getCustomerByUsernameAndPassword(String username,
			String plainTextPassword, boolean checkEacDb, boolean checkAtypon)
					throws ServiceLayerException;

	/**
	 * Password reset attempt denied.
	 * 
	 * @param customer the customer
	 * @param locale the locale
	 * @throws ServiceLayerException the service layer exception
	 */
	void passwordResetAttemptDenied(Customer customer, Locale locale) throws ServiceLayerException;

	/**
	 * Delete customer.
	 *
	 * @param customerId the customer id
	 * @throws ServiceLayerException the service layer exception
	 */
	void deleteCustomer(String customerId) throws ServiceLayerException;

	/**
	 * get all the invalid Email Address in the system
	 */
	List<Customer> getCustomerInvalidEmailAddress() throws ServiceLayerException;

	/**
	 * save the cleansed email address.
	 */
	String saveCleansedEmail(Customer customer) throws ServiceLayerException ;


	/**
	 * get user sessions.
	 */
	List<String> getSessionsByUserId(Customer customer) throws ErightsSessionNotFoundException, ServiceLayerException ;


	/**
	 * Get a customer by their internal id.
	 * 
	 * @param id
	 * @return
	 * @throws ServiceLayerException 
	 */
	void checkCustomerInEright(final String id) throws CustomerNotFoundServiceLayerException, ServiceLayerException;

	/**
	 * remove licence from atypone
	 * 
	 * @param customer the customer
	 * @param licenceID the licence ID in Atypone for customer
	 * @throws ServiceLayerException the service layer exception
	 */
	void removeLicence(final String userID, final String licenceID)  throws LicenceNotFoundServiceLayerException, CustomerNotFoundServiceLayerException, ServiceLayerException;
	
	/**
	 * reset Password from atypon
	 * 
	 * @param username the username
	 * @param erightsLicenceId the licence ID in Atypone for customer
	 * @throws ServiceLayerException the service layer exception
	 */
	void updateResetCustomerPasswordwithUrl(final Customer customer, final Locale locale,final String successUrl,final String failureUrl, String tokenId)
			throws ServiceLayerException, PasswordPolicyViolatedServiceLayerException;

	/**
     * 
     * @param productOwners
     *          the product owners
     * @throws ServiceLayerException
     *              the exception
     */
    List<Customer> getCustomersByProductOwner(Set<DivisionAdminUser> divisionAdminUsers) throws ServiceLayerException;

    /**
     * getCustomerByExternalCustomerId 
     * @param systemId
     * @param typeId
     * @param externalId
     * @return 
     * Customer 
     * @author Developed by TCS
     * @throws ErightsException 
     */
    public Customer getCustomerByExternalCustomerId(String systemId,
			String typeId, String externalId) throws ErightsException;
    
    List<Customer> searchCustomersForMerging(final CustomerSearchCriteria customerSearchCriteria)
			throws ServiceLayerException;
    
    void mergeCustomer(String customerId,String email) throws ServiceLayerException;

	void updateCustomerFromWS(Customer customer, String newEmailAddress)
			throws ServiceLayerException, UsernameExistsException,
			PasswordPolicyViolatedServiceLayerException,
			UserLoginCredentialAlreadyExistsException;
	
	public String validateUserAccount(WsUserIdDto wsUserIdDto) throws ErightsException ;
	
	public void killUserSession(WsUserIdDto wsUserIdDto) throws ErightsException ;

	public void deleteTrustedSystem(String customerId) throws ServiceLayerException;

}

