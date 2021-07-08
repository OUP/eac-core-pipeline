package com.oup.eac.service;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.mail.MessagingException;

import org.springframework.security.access.AccessDeniedException;

import com.oup.eac.domain.AccountPageDefinition;
import com.oup.eac.domain.AccountRegistrationDefinition;
import com.oup.eac.domain.ActivationCode;
import com.oup.eac.domain.ActivationCodeRegistration;
import com.oup.eac.domain.ActivationCodeRegistrationDefinition;
import com.oup.eac.domain.Customer;
import com.oup.eac.domain.EmailType;
import com.oup.eac.domain.LicenceTemplate;
import com.oup.eac.domain.Product;
import com.oup.eac.domain.ProductPageDefinition;
import com.oup.eac.domain.ProductRegistrationDefinition;
import com.oup.eac.domain.RegisterableProduct;
import com.oup.eac.domain.Registration;
import com.oup.eac.dto.AccountRegistrationDto;
import com.oup.eac.dto.CustomerRegistrationsDto;
import com.oup.eac.dto.EnforceableProductDto;
import com.oup.eac.dto.LicenceDto;
import com.oup.eac.dto.ProductRegistrationDto;
import com.oup.eac.dto.RegistrationActivationDto;
import com.oup.eac.dto.RegistrationDto;
import com.oup.eac.dto.TokenDto;
import com.oup.eac.integration.facade.exceptions.ErightsException;
import com.oup.eac.integration.facade.exceptions.GroupNotFoundException;
import com.oup.eac.integration.facade.exceptions.LicenseNotFoundException;
import com.oup.eac.integration.facade.exceptions.ProductNotFoundException;
import com.oup.eac.integration.facade.exceptions.UserNotFoundException;
import com.oup.eac.service.exceptions.ServiceLayerValidationException;

/**
 * @author harlandd Registration service providing registration related business processes.
 */
public interface RegistrationService {
	

	//Registration<?> getProductRegistration(final String id);
	
	/**
	 * Gets the {@link Registration} with the specified id and ensures that the Registration's object graph is fully
	 * initialised (that child collections are loaded and proxies are replaced with real objects).
	 * 
	 * @param id
	 *            The id (primary key) of the Registration.
	 * @return The fully initialised Registration or null if no Registration was found with the specified id.
	 */
	//Registration<?> getProductRegistrationInitialised(final String id);
	
	/**
	 * Get the activation code registrations by code
	 * 
	 * @param activationCode the activationCode
	 * @return the activation code registrations
	 */
	List<ActivationCodeRegistration> getActivationCodeRegistrationByCode(final ActivationCode activationCode);
    
    /**
     * Get a registration dto based on the product registration definition.
     *  
     * @param productRegistrationDefinition
     * 			the definition identifying the page definition to retrive
     * @param customer
     * 			the customer for which the page should be populated
     * @param locale TODO
     * @return
     */
    ProductRegistrationDto getProductPageDefinitionByRegistrationDefinition(final ProductRegistrationDefinition productRegistrationDefinition, 
            final Customer customer, Locale locale);
    
    /**
     * Get a registration dto based on the account registration definition.
     * @param customer
     * 			the customer for which the page should be populated
     * @param locale TODO
     * @param productRegistrationDefinition
     * 			the definition identifying the page definition to retrive
     *  
     * @return
     */
    RegistrationDto getAccountPageDefinitionByRegistrationDefinition(final AccountRegistrationDefinition accountRegistrationDefinition, 
            final Customer customer, Locale locale);

    /**
     * @param registrationDto
     *            the registrationDto
     * @param customer
     *            the customer
     * @param productDefinition
     *            the productDefinition
     * @param locale
     *            the locale
     * @param orignalUrl
     *            the orignalUrl
     * @throws ServiceLayerException
     *             any service layer exception
     */
    Registration<ProductRegistrationDefinition> saveProductRegistration(final Customer customer, 
    		final ProductRegistrationDefinition registrationDefinition) throws ServiceLayerException;
    
    
    void saveCompleteRegsitration(final RegistrationDto registrationDto, final Customer customer, 
			final String registrationId) throws ServiceLayerException;
    
	void saveProductRegistrationDetails(final RegistrationDto registrationDto,
			final Customer customer, final RegisterableProduct product,
			final String registrationId) throws ServiceLayerException;
    
    ActivationCodeRegistration saveActivationCodeRegistration(final ActivationCode code, final Customer customer) throws ServiceLayerException, ErightsException, Exception;
    
    ActivationCodeRegistration saveActivationCodeRegistrationWithoutActivationCode(final Customer customer, final ActivationCodeRegistrationDefinition activationCodeRegistrationDefinition) throws ServiceLayerException;
    

	String prepareActivationOrAcceptanceEmails(Customer customer, ProductRegistrationDefinition productRegistrationDefinition, LicenceTemplate licenceTemplate, Locale locale, EmailType emailType, LicenceDto licence);

	void emailSelfLicenceActivation(final Customer customer, final ProductRegistrationDefinition registrationDefinition, final Locale locale,
			final TokenDto token) throws ServiceLayerException;
	
	void emailSelfLicenceActivationNew(final Customer customer, final EnforceableProductDto enfoProduct, final Locale locale,
			final TokenDto token) throws ServiceLayerException;

	Map<String, Object> updateAllowRegistrationFromToken(final String tokenString, final boolean sendEmail) throws Exception;
    
	void updateAllowRegistration(final Registration<? extends ProductRegistrationDefinition> registration, final boolean sendEmail, final EnforceableProductDto enfProduct) throws Exception;
    
	Map<String, String> updateDenyRegistrationFromToken(final String tokenString, final boolean sendEmail) throws Exception;
    
	void updateDenyRegistration(final Registration<? extends ProductRegistrationDefinition> registration, final boolean sendEmail, final EnforceableProductDto enfProduct) throws Exception;

    /**
     * Gets the entitlements for customer registrations.
     *
     * @param customer the customer
     * @param licenceId the licence id
     * @return the registrations for customer
     * @throws ServiceLayerException the service layer exception
     */
    CustomerRegistrationsDto getEntitlementsForCustomerRegistrations(Customer customer, String licenceId, boolean editRegistration) throws ServiceLayerException;
    
    
    /**
     * Gaurav Soni : Performance improvements CR
     * Gets the entitlements for customer registrations.
     * 
     * @param customer the customer
     * @param productSystemIdSet the list of product external IDs
     * @param licenceState the state of licence
     * @return the registrations for customer
     * @throws ServiceLayerException the service layer exception
     */
    CustomerRegistrationsDto getEntitlementsForCustomerRegistrationsFiltered(Customer customer, Set<String> productOrgUnitSet, Set<String> productSystemIdSet, String licenceState) throws ServiceLayerException;

    /**
     * Gets the entitlements for a customer registration.
     *
     * @param customer the customer
     * @param systemId the system id
     * @param registration the registration
     * @return the registrations for customer
     * @throws ServiceLayerException the service layer exception
     */
    CustomerRegistrationsDto getEntitlementsForCustomerRegistration(Customer customer, Registration<? extends ProductRegistrationDefinition> registration) throws ServiceLayerException;

    /**
     * Update registration.
     *
     * @param registrationDto the registration dto
     * @param customer the customer
     * @param registration the registration id
     * @throws ServiceLayerException the service layer exception
     */
    void updateRegistration(RegistrationDto registrationDto, Customer customer, Registration registration) throws ServiceLayerException;

    AccountRegistrationDto getAccountRegistrationDto(final AccountPageDefinition pageDefinition, final Locale locale);
    
    ProductRegistrationDto getProductRegistrationDto(final ProductPageDefinition pageDefinition, final Locale locale);
    
 //   ActivationCodeRegistration updateCodeAndCreateRegistrationForProduct(Customer customer, ActivationCode activationCode) throws ServiceLayerException;
    
 //   List<ActivationCodeRegistration> updateCodeAndCreateRegistrationForEacGroup(Customer customer, ActivationCode activationCode) throws ServiceLayerException;

    CustomerRegistrationsDto getEntitlementsForCustomerRegistrationForAfterRedeem(Customer customer, List<Registration<? extends ProductRegistrationDefinition>> registration) throws ServiceLayerException;

 //   void createRegistrationActivation(List<ActivationCodeRegistration> registrationList, Customer customer, Locale locale) throws ServiceLayerException;
    
    ActivationCode incrementActivationCodeUsage(ActivationCode activationCode) throws ServiceLayerException;
    
    List<LicenceDto> createRegistrationAndAddLicence(Customer customer, ActivationCode activationCode, Locale locale) 
    		throws ServiceLayerException, ProductNotFoundException, UserNotFoundException, LicenseNotFoundException, AccessDeniedException, 
    		GroupNotFoundException, ErightsException, Exception ;
    
    List<LicenceDto> createRegistrationAndAddLicenceForEacGroup(Customer customer, ActivationCode activationCode, Locale locale) 
    		throws ServiceLayerException, ProductNotFoundException, UserNotFoundException, LicenseNotFoundException, AccessDeniedException, 
    		GroupNotFoundException, ErightsException;
    
    void decrementUsage(ActivationCode activationCode);

	void saveRegistrationActivation(
			RegistrationActivationDto registrationActivationDto,
			EnforceableProductDto product) throws ServiceLayerException;
	
	
	/**
	 * getProductRegistration 
	 * @param registrationId
	 * @param customerId
	 * @return
	 * @throws ServiceLayerException 
	 * Registration<? extends ProductRegistrationDefinition> 
	 * @author Developed by TCS
	 */
	public Registration<? extends ProductRegistrationDefinition> getProductRegistration(String registrationId, String customerId) throws ServiceLayerException;
	
	public void activateLicense(final String userId, final String licenseId, final String productName) 
			throws UserNotFoundException, LicenseNotFoundException, ErightsException, UnsupportedEncodingException, MessagingException ;
	
	public void deactivateLicense(final String userId, final String licenseId, final String productName) 
			throws UserNotFoundException, LicenseNotFoundException, ErightsException, UnsupportedEncodingException, MessagingException ;

	public boolean getCompletedRegistrationInformation(Product product,
			Customer customer);

	void saveActivationRegistrationActivation(
			RegistrationActivationDto registrationActivationDto,
			EnforceableProductDto enforceableProduct) throws ServiceLayerException;

	void sendRedeemEmailForGroup(List<LicenceDto> licenses, Customer customer,
			Locale locale) throws ServiceLayerValidationException, Exception;

	void updateLicenseModifiedDate(String licenseId);
	
}
