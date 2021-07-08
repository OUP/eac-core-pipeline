package com.oup.eac.service.impl;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.mail.internet.InternetAddress;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.velocity.app.VelocityEngine;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.constants.SearchDomainFields;
import com.oup.eac.cloudSearch.search.impl.AmazonCloudSearchServiceImpl;
import com.oup.eac.cloudSearch.util.OupIdMappingUtil;
import com.oup.eac.common.date.utils.DateUtils;
import com.oup.eac.common.utils.EACSettings;
import com.oup.eac.common.utils.MessageTextSource;
import com.oup.eac.common.utils.crypto.PasswordGenerator;
import com.oup.eac.common.utils.email.EmailUtils;
import com.oup.eac.common.utils.email.VelocityUtils;
import com.oup.eac.common.utils.token.TokenConverter;
import com.oup.eac.data.AnswerDao;
import com.oup.eac.data.CustomerDao;
import com.oup.eac.data.RegistrationDao;
import com.oup.eac.domain.AdminUser;
import com.oup.eac.domain.Answer;
import com.oup.eac.domain.Component;
import com.oup.eac.domain.Customer;
import com.oup.eac.domain.Customer.CustomerType;
import com.oup.eac.domain.DivisionAdminUser;
import com.oup.eac.domain.ExternalCustomerId;
import com.oup.eac.domain.Field;
import com.oup.eac.domain.LinkedRegistration;
import com.oup.eac.domain.MailCriteria;
import com.oup.eac.domain.PageConstant;
import com.oup.eac.domain.Password;
import com.oup.eac.domain.Product;
import com.oup.eac.domain.ProductRegistrationDefinition;
import com.oup.eac.domain.Registration;
import com.oup.eac.domain.User.EmailVerificationState;
import com.oup.eac.domain.User.UserType;
import com.oup.eac.domain.paging.Paging;
import com.oup.eac.domain.paging.PagingCriteria;
import com.oup.eac.domain.search.AmazonSearchFields;
import com.oup.eac.domain.search.AmazonSearchRequest;
import com.oup.eac.domain.search.AmazonSearchResponse;
import com.oup.eac.domain.utils.audit.AuditLogger;
import com.oup.eac.dto.AccountRegistrationDto;
import com.oup.eac.dto.AuthenticationResponseDto;
import com.oup.eac.dto.BasicProfileDto;
import com.oup.eac.dto.ChangePasswordDto;
import com.oup.eac.dto.CustomerDto;
import com.oup.eac.dto.CustomerRegistrationsDto;
import com.oup.eac.dto.CustomerSearchCriteria;
import com.oup.eac.dto.CustomerSessionDto;
import com.oup.eac.dto.CustomerWithLicencesAndProductsDto;
import com.oup.eac.dto.EmailTokenDto;
import com.oup.eac.dto.LicenceDto;
import com.oup.eac.dto.LoginPasswordCredentialDto;
import com.oup.eac.dto.Message;
import com.oup.eac.dto.ResetPasswordTokenDto;
import com.oup.eac.dto.WsUserIdDto;
import com.oup.eac.integration.erights.ExternalIdentifier;
import com.oup.eac.integration.facade.ErightsFacade;
import com.oup.eac.integration.facade.exceptions.ErightsException;
import com.oup.eac.integration.facade.exceptions.ErightsSessionNotFoundException;
import com.oup.eac.integration.facade.exceptions.InvalidCredentialsException;
import com.oup.eac.integration.facade.exceptions.LicenseNotFoundException;
import com.oup.eac.integration.facade.exceptions.PasswordPolicyViolatedException;
import com.oup.eac.integration.facade.exceptions.SessionConcurrencyException;
import com.oup.eac.integration.facade.exceptions.UserAlreadyExistsException;
import com.oup.eac.integration.facade.exceptions.UserLoginCredentialAlreadyExistsException;
import com.oup.eac.integration.facade.exceptions.UserNotFoundException;
import com.oup.eac.service.CustomerService;
import com.oup.eac.service.EmailService;
import com.oup.eac.service.InvalidCredentialsServiceLayerException;
import com.oup.eac.service.LicenceService;
import com.oup.eac.service.ProductService;
import com.oup.eac.service.RegistrationService;
import com.oup.eac.service.ServiceLayerException;
import com.oup.eac.service.exceptions.AccountLockedServiceLayerException;
import com.oup.eac.service.exceptions.CustomerNotFoundServiceLayerException;
import com.oup.eac.service.exceptions.LicenceNotFoundServiceLayerException;
import com.oup.eac.service.exceptions.PasswordPolicyViolatedServiceLayerException;
import com.oup.eac.service.exceptions.SessionConcurrencyServiceLayerException;
import com.oup.eac.service.exceptions.SessionNotFoundServiceLayerException;
import com.oup.eac.service.exceptions.UserHasNoEmailServiceLayerException;
import com.oup.eac.service.exceptions.UsernameExistsException;
import com.oup.eac.service.util.ConvertSearch;

/**
 * Customer service providing customer related business processes.
 * 
 * @author harlandd 
 * @author Ian Packard
 */
@Service("customerService")
public class CustomerServiceImpl implements CustomerService {

    private static final Logger LOGGER = Logger.getLogger(CustomerServiceImpl.class);

    private final EmailService emailService;

    private final VelocityEngine velocityEngine;

    private final CustomerDao customerDao;

    private final AnswerDao answerDao;

    private final ErightsFacade erightsFacade;

    private final RegistrationDao registrationDao;
    
    private final RegistrationService registrationService;
    
    private final MessageSource messageSource;
    
    private final StandardPasswordEncoder passwordEncoder;
    
    private final LicenceService licenceService;
    
    private final ProductService productService;

    private static final int FETCH_ERIGHTS_ID_FROM_OUP_ID = 5 ;
    /**
     * Instantiates a new customer service impl.
     *
     * @param emailService the email service
     * @param velocityEngine the velocity engine
     * @param customerDao the customer dao
     * @param answerDao the answer dao
     * @param erightsFacade the erights facade
     * @param registrationDao the registration dao
     * @param messageSource the message source
     * @param passwordEncoder the password encoder
     * @param licenceService the licence service
     * @param productService the product service
     */
    @Autowired
    public CustomerServiceImpl(final EmailService emailService, final VelocityEngine velocityEngine, final RegistrationService registrationService,
         final AnswerDao answerDao, final ErightsFacade erightsFacade, final CustomerDao customerDao,
            final RegistrationDao registrationDao, final MessageSource messageSource, final StandardPasswordEncoder passwordEncoder, final LicenceService licenceService, final ProductService productService ) {
        super();
        Assert.notNull(emailService);
        Assert.notNull(velocityEngine);
        Assert.notNull(customerDao);
        Assert.notNull(answerDao);
        Assert.notNull(erightsFacade);
        Assert.notNull(registrationDao);
        Assert.notNull(passwordEncoder);
        Assert.notNull(licenceService);
        Assert.notNull(productService);
        this.emailService = emailService;
        this.velocityEngine = velocityEngine;
        this.registrationService = registrationService;
        this.customerDao = customerDao;
        this.answerDao = answerDao;
        this.erightsFacade = erightsFacade;
        this.registrationDao = registrationDao;
        this.messageSource = messageSource;
        this.passwordEncoder = passwordEncoder;
        this.licenceService = licenceService;
        this.productService = productService;
    }

    
    /**
     * @param productDefinition
     *            the productDefinition
     * @param customer
     *            the customer
     * @return Registration
     * @throws ServiceLayerException 
     */
    public final Registration<ProductRegistrationDefinition> getRegistrationByRegistrationDefinitionAndCustomer(final ProductRegistrationDefinition registrationDefinition,
            final Customer customer) throws ServiceLayerException {
    	CustomerRegistrationsDto dto = registrationService.getEntitlementsForCustomerRegistrations(customer, null, true);
    	List<Registration<ProductRegistrationDefinition>> regist = new ArrayList<Registration<ProductRegistrationDefinition>>();
    	for (Registration reg : dto.getRegistrations()){
    		if(reg.getRegistrationDefinition().getProduct().getId()==registrationDefinition.getProduct().getId())
    			regist.add(reg);
    	}
        return getRegistration(regist);
        		//registrationDao.getRegistrationByRegistrationDefinitionAndCustomer(registrationDefinition, customer));
    }
    
    private final Registration<ProductRegistrationDefinition> getRegistration(List<Registration<ProductRegistrationDefinition>> registrations) {
    	if(registrations.size() == 0) {
    		return null;
    	}
    	if(registrations.size() == 1) {
    		return registrations.get(0);
    	}
    	return getIncompleteRegistration(registrations);
    }
    
	private Registration<ProductRegistrationDefinition> getIncompleteRegistration(List<Registration<ProductRegistrationDefinition>> registrations) {
		Registration<ProductRegistrationDefinition> registration = getOldestIncompleteRegistration(registrations);
		if(registration == null) {
			return getMostRecentCompleteRegistration(registrations);
		}
		return registration;
	}
    
	private Registration<ProductRegistrationDefinition> getMostRecentCompleteRegistration(List<Registration<ProductRegistrationDefinition>> registrations) {
		Registration<ProductRegistrationDefinition> registration = null;
    	for(Registration<ProductRegistrationDefinition> reg : registrations) {
    		if(registration == null || reg.getCreatedDate().isAfter(registration.getCreatedDate())) {
    			registration = reg;
    		}
    	}
    	return registration;
	}      
    
	private Registration<ProductRegistrationDefinition> getOldestIncompleteRegistration(List<Registration<ProductRegistrationDefinition>> registrations) {
		Registration<ProductRegistrationDefinition> registration = null;
    	for(Registration<ProductRegistrationDefinition> reg : registrations) {
    		if((registration == null && !reg.isCompleted()) || 
    		   (!reg.isCompleted() && reg.getCreatedDate().isBefore(registration.getCreatedDate()))) {
    			registration = reg;
    		}
    	}
    	return registration;
	}   
    
    /**
     * @param sessionKey
     *            the sessionKey
     * @return Customer
     * @throws ServiceLayerException
     *             the exception
     */	
    public final Customer getCustomerFromSession(final String sessionKey) throws CustomerNotFoundServiceLayerException {
        try {
            Customer customer = getCustomerFromIds(erightsFacade.getCustomerIdsFromSession(sessionKey));            
            
          /*  try {     
                if(null!=customer && customer.getCustomerType() == CustomerType.SPECIFIC_CONCURRENCY){
                    CustomerDto cdto= erightsFacade.getUserAccount(customer.getId());
                    if(cdto != null){
                        CustomerType ct=customer.getCustomerType();
                        ct.setConcurrency(cdto.getConcurrency());
                        customer.setCustomerType(ct);
                    }                
                }           
                
            } catch (UserNotFoundException unfe) {
                String msg = String.format("User not found Exception");
                LOGGER.error(msg, unfe);
                
            } catch (ErightsException ee) { 
                String msg = String.format("unexpected atypon error");
                LOGGER.error(msg, ee);            
            }*/
            return customer;
        } catch (ErightsException e) {
            throw new CustomerNotFoundServiceLayerException("Erights error getting customer ids from session. Sessionkey: "
                    + sessionKey, e);
        }
    }
    
    /**
     * @param sessionKey
     *            the sessionKey
     * @return Customer
     * @throws ServiceLayerException
     *             the exception
     */
    /*private final Set<Answer> getCustomerWithAnswersFromSession(final String sessionKey) throws CustomerNotFoundServiceLayerException {
        try {
            return getCustomerWithAnswersFromIds(erightsFacade.getCustomerIdsFromSession(sessionKey));
        } catch (ErightsException e) {
            throw new CustomerNotFoundServiceLayerException("Erights error getting customer ids from session. Sessionkey: "
                    + sessionKey, e);
        }
    }*/

    /**
     * @param erightsId
     *            the erights id
     * @return the user
     */
    public final Customer getCustomerByErightsId(final String userId) {
    	//List<CustomerType> customerTypes = Arrays.asList(CustomerType.SELF_SERVICE, CustomerType.SHARED, CustomerType.SPECIFIC_CONCURRENCY);
    	Customer customer = new Customer();
    	try {
			customer = convertCustomer(erightsFacade.getUserAccount(userId));
			return customer;
    	} catch (UserNotFoundException unfe) {
            String msg = String.format("User not found Exception");
            LOGGER.error(msg, unfe);
            
        } catch (ErightsException ee) { 
            String msg = String.format("unexpected atypon error");
            LOGGER.error(msg, ee);            
        }
        return customer;
        //return customerDao.getCustomerByErightsId(erightsId, customerTypes);
    	
    }
    
    /**
     * @param erightsId
     *            the erights id
     * @return the user
     */
    private final Customer getCustomerWithAnswersByErightsId(final Integer erightsId) {
        return customerDao.getCustomerWithAnswersByErightsId(erightsId);
    }

    /**
     * Gets the {@link Customer} from the supplied username. The username is not case sensitive.
     * 
     * @param username
     *            the customer's username
     * @return the customer
     * @throws ErightsException 
     */
    public final Customer getCustomerByUsername(final String username) throws UserNotFoundException, ErightsException {
        final String trimmedLowercaseUsername = StringUtils.trim(username).toLowerCase();
        //Customer customer = customerDao.getCustomerByUsername(trimmedLowercaseUsername);
        Customer customer = null;
       /* try*/ try{
    	    /*if(null!=customer && customer.getCustomerType() == CustomerType.SPECIFIC_CONCURRENCY){*/
              //  CustomerDto cdto= erightsFacade.getUserAccount(customer.getErightsId());
            	CustomerDto cdto= erightsFacade.getUserAccountByUsername(trimmedLowercaseUsername);
                //if(cdto != null){
            	customer = convertCustomer(cdto);
            	 /*}                
            }*/           
            
        } catch (UserNotFoundException e) {
        	 throw new UserNotFoundException(e.getMessage()); 
             } 
      return customer;
    }
    
    public String validateUserAccount(WsUserIdDto wsUserIdDto) throws ErightsException{
    	String userId = erightsFacade.validateUserAccount(wsUserIdDto);
    	return userId ;
    }
    
    public void killUserSession(WsUserIdDto wsUserIdDto) throws ErightsException{
    	erightsFacade.killUserSession(wsUserIdDto);
    }
    /*private void setRegistrationInformation(Customer customer) throws ServiceLayerException {
    	List<LicenceDto> licences = licenceService.getLicencesForCustomer(customer);
		List<Registration<? extends ProductRegistrationDefinition>> registrations = new ArrayList<Registration<? extends ProductRegistrationDefinition>>();

		Set<LinkedRegistration> linkedRegistrations = setLinkedRegistration(licences);
	
		for(LicenceDto dto : licences){
			//List<ProductRegistration> productRegistrations = new ArrayList<ProductRegistration>();
			ProductRegistration e = new ProductRegistration();
			for(int productId:dto.getProductIds()){
				try {
					EnforceableProductDto product = erightsFacade.getProduct(productId);
					ProductRegistrationDefinition registrationDefinition = new ProductRegistrationDefinition();
					registrationDefinition.setEnforceableProduct(product);
					
					e.setRegistrationDefinition(registrationDefinition);
				} catch (ErightsException e1) {
					e1.printStackTrace();
				}
			}
			e.setCompleted(dto.isCompleted());
			e.setActivated(dto.isActive());
			e.setAwaitingValidation(dto.isAwaitingValidation());
			e.setDenied(dto.isDenied());
			e.setErightsLicenceId(dto.getErightsId());
			
			e.getLinkedRegistrations().addAll(linkedRegistrations);
			registrations.add(e);
		}
    	customer.getRegistrations().add((Registration<ProductRegistrationDefinition>) registrations);
		
	}*/


	/**
     * convertCustomer 
     * @param cdto
     * @return 
     * Customer 
     * @author Developed by TCS
     * @throws ServiceLayerException 
     */
    private Customer convertCustomer(CustomerDto cdto) {
    	Customer customer = new Customer(); 
    	if(cdto.getConcurrency()==CustomerType.SELF_SERVICE.getConcurrency())
    		customer.setCustomerType(CustomerType.SELF_SERVICE);
    	if(cdto.getConcurrency()==CustomerType.SHARED.getConcurrency())
    		customer.setCustomerType(CustomerType.SHARED);
    	if(cdto.getConcurrency()>CustomerType.SELF_SERVICE.getConcurrency())
    		customer.setCustomerType(CustomerType.SPECIFIC_CONCURRENCY);
    	CustomerType ct=customer.getCustomerType();
    	ct.setConcurrency(cdto.getConcurrency());
    	customer.setUsername(cdto.getUsername());
    	customer.setEmailAddress(cdto.getEmailAddress());
    	customer.setEmailVerificationState(cdto.getEmailVerificationState());
    	customer.setEnabled(!cdto.isSuspended());
    	customer.setFamilyName(cdto.getFamilyName());
    	customer.setFirstName(cdto.getFirstName());
    	customer.setLocale(cdto.getLocale());
    	customer.setTimeZone(cdto.getTimeZone());
    	customer.setResetPassword(cdto.isResetPassword());
    	customer.setExternalIds(cdto.getExternalIds());
    	customer.setId(cdto.getUserId());
    	customer.setLastLoginDateTime(cdto.getLastLoginDateTime());
    	customer.setLocked(cdto.isSuspended());
    	if(cdto.getFailedLoginAttemptes()!=null){
    		customer.setFailedAttempts(cdto.getFailedLoginAttemptes());
    	}
    	customer.setCreatedDate(cdto.getCreatedDateTime());
    	return customer;
    }
    private CustomerDto convertToCustomerDto(Customer customer) {
    	CustomerDto cdto = new CustomerDto(customer); 
    	
    	return cdto;
    }
		
		private  Set<LinkedRegistration> setLinkedRegistration(List<LicenceDto> licences) {
			Set<LinkedRegistration> linkedRegistrations=new HashSet<LinkedRegistration>();
			for(LicenceDto dto : licences){
				LinkedRegistration linkedRegistration = new LinkedRegistration();
				if(dto.getLinkedProductIds()!=null){
					for(String a : dto.getLinkedProductIds()){
						linkedRegistration.setId(a);
					}
					linkedRegistrations = new HashSet<LinkedRegistration>();
					linkedRegistrations.add(linkedRegistration);

				}
			}
			return linkedRegistrations;

		}


	/**
     * {@inheritDoc}
     */
    @Override
    public final void logout(final String session) throws ServiceLayerException {
        logout(null, session);    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public final void logout(final Customer customer, final String session ) throws ServiceLayerException {
        try {
            erightsFacade.logout(session);
            AuditLogger.logEvent(customer, "Logged Out Successfully", "sessionToken:"+session);
            LOGGER.debug("Erights logout was successful for session: " + session);
        } catch (ErightsSessionNotFoundException e) {
            throw new SessionNotFoundServiceLayerException("Erights error.", e, new Message(
                    "error.problem.with.logout", "There was a problem logging out session: " + session));
        } catch (ErightsException e) {
            throw new ServiceLayerException("Erights error.", e, new Message(
                    "error.problem.with.logout", "There was a problem logging out session: " + session));
        }
    }

    /* (non-Javadoc)
     * @see com.oup.eac.service.CustomerService#getCustomerByUsernameAndPassword(java.lang.String, java.lang.String)
     */
    @Override
    public final CustomerSessionDto getCustomerByUsernameAndPassword(final String username, final String plainTextPassword)
            throws ServiceLayerException {
    	boolean checkEacDb = true;
    	boolean checkAtypon = true;
    	CustomerSessionDto customerSessionDto = getCustomerByUsernameAndPassword(username, plainTextPassword, checkEacDb, checkAtypon);
    	return customerSessionDto;
    }
    
    /**
     * @param username
     *            the username
     * @param password
     *            the password
     * @return the user
     * @throws ServiceLayerException
     *             the exception
     */
    @Override
    public final CustomerSessionDto getCustomerByUsernameAndPassword(final String username, final String plainTextPassword, final boolean checkEacDb, final boolean checkAtypon)
    		throws ServiceLayerException {

    	// Always find a user locally before trying to authenticate via erights
    	final String trimmedLowercaseUsername = StringUtils.trim(username).toLowerCase();
    	Customer customer = null ;
    	/* try {
			customer = getCustomerByUsername(trimmedLowercaseUsername);
		}catch (ErightsException e) {
            	AuditLogger.logEvent(customer, "ErightsException", e.getMessage());
            }

        if (customer == null) {
            throw new CustomerNotFoundServiceLayerException("No customer found for username " + username, new Message(
                    "error.problem.with.login.credentials", "There was a problem with your login credentials."));
        } else if (customer.isLocked() && !customer.isResetPassword()) {
            // customers with a locked account must not be allowed to
            // authenticate unless they are attempting to reset their password.
            throw new AccountLockedServiceLayerException("Account locked for " + username + " so unable to authenticate.",
                    new Message("error.account.locked",
                            "The account is locked."));
        } else {
            if (!checkEacDb && !checkAtypon) {
            	throw new IllegalArgumentException("One of checkEacDb or checkAtypon must be true");
            }
    	 */
    	/*
    	 * the eacWebApp                            checks EacDb and Atypon (in a single call)
    	 * the eacWebServiceApp                     checks EacDb and Atypon (in a single call)
    	 * the BasicLogicController for LoginWidget checks EacDb and Atypon (but in separate calls)
    	 */

    	CustomerSessionDto customerSessionDto = new CustomerSessionDto();
    	final String trimmedPassword = StringUtils.trim(plainTextPassword);
    	try {
    		/*if (checkEacDb) {
                    performEacCheck(trimmedPassword, customer);
                }*/
    		//if (checkAtypon) {
    			customer = performAtyponCheck(trimmedLowercaseUsername, trimmedPassword);
    			customerSessionDto.setSession(customer.getSessions().get(0));
    		//}
    		customerSessionDto.setCustomer(customer);
    		return customerSessionDto;
    	} catch (SessionConcurrencyException e) {
    		AuditLogger.logEvent(customer, "Session concurrency exceeded.", e.getMessage());
    		throw new SessionConcurrencyServiceLayerException("Session concurrency exceeded.", e, new Message(
    				"error.problem.session.concurrency", "Sorry. We could not log you in. Please try again later."));           	
    	} catch (InvalidCredentialsException e) {
    		//registerFailedLoginAttempt(username, customer);
    		throw new InvalidCredentialsServiceLayerException("Invalid credentials.", e, new Message(
    				"error.problem.with.login.credentials", "There was a problem with your login credentials."));
    	} catch (ErightsException e) {
    		if(e.getMessage().equals("Your account is locked."))
    			 throw new AccountLockedServiceLayerException("Account locked for " + username + " so unable to authenticate.",
 	                    new Message("error.account.locked",
 	                            "The account is locked."));
    		if(e.getMessage().equals("Internal Error: Authentication failed because no auth profile matched")){
    			throw new ServiceLayerException(e.getMessage());
    		}
    		AuditLogger.logEvent(customer, "ErightsException", e.getMessage());
    		throw new ServiceLayerException("Erights error.", e, new Message(
    				"error.problem.with.login.credentials", "There was a problem with your login credentials."));
    	}
    }

	/*private void performEacCheck(final String plainTextPassword, Customer customer) throws InvalidCredentialsException {
		String hashedPassword = customer.getPassword();
		String candidateHashedPassword = passwordEncoder.encode(plainTextPassword);
		if (!hashedPassword.equals(candidateHashedPassword)) {
		    throw new InvalidCredentialsException("Invalid Credentials (Checked in EAC DB)", "-1");
		}
	}*/
	
	private Customer performAtyponCheck(String username, String plainTextPassword) throws CustomerNotFoundServiceLayerException, SessionConcurrencyException, InvalidCredentialsException, ErightsException {
        AuthenticationResponseDto response = erightsFacade.authenticateUser(new LoginPasswordCredentialDto(username, plainTextPassword));
        List<String> sessionKey = new  ArrayList<String>();
        sessionKey.add(response.getSessionKey());
        Customer customer = convertCustomer(response.getCustomerDto());
        if (customer == null) {
        	// Clean up an orphaned session if one exists
        	//removeOrphanedSession(sessionKey.get(0));
            throw new CustomerNotFoundServiceLayerException(new StringBuilder("No customer found using erights ids ").append(
                    response.getUserIds()).append(". The username was ").append(username).toString(),
                    new Message("error.problem.with.login.credentials",
                            "There was a problem with your login credentials."));
        }
        AuditLogger.logEvent(customer,"Logged In Successfully", "sessionToken:"+sessionKey);
        // On a successful login attempt, we must unlock the user
        //unlockUserFailingSafely(customer);
        customer.setSessions(sessionKey);
        return customer;
	}

	private void removeOrphanedSession(String sessionKey) {
		try {
			logout(sessionKey);
		} catch (Exception e) {
			LOGGER.warn("Error attempting to remove possible orphaned Atypon session (key=" + sessionKey + "): " + e, e);
		}
	}
	
	private void unlockUserFailingSafely(Customer customer) {
		try {
			customer.setLocked(false);
			customer.setLastLoginDateTime(new DateTime());
			customerDao.update(customer);
		} catch (Exception e) {
			// Exceptions are caught and logged - nothing is propagated
			LOGGER.error("Error unlocking user: " + e, e);
		}
	}
	
	

    /**
     * @param ids
     *            the ids
     * @return self service customer otherwise null
     */
    private Customer getCustomerFromIds(final List<String> ids) {
        if(ids != null){           
        	Customer customer = null;
            for (String id : ids) {
            	try {
        			customer = convertCustomer(erightsFacade.getUserAccount(id));
        			return customer;
            	} catch (UserNotFoundException unfe) {
                    String msg = String.format("User not found Exception");
                    LOGGER.error(msg, unfe);
                    
                } catch (ErightsException ee) { 
                    String msg = String.format("unexpected atypon error");
                    LOGGER.error(msg, ee);            
                }
            	//Customer customer = getCustomerByErightsId(id);
                return customer;
            }
        }
        return null;
    }
    
    /**
     * @param ids
     *            the ids
     * @return self service customer otherwise null
     */
    private Set<Answer> getCustomerWithAnswersFromIds(final List<String> ids) {
        if(ids != null) {
            for (String id : ids) {
            	Set<Answer> answer = getCustomerWithAnswersByCustomerId(id);
                if (answer != null) {
                    return answer;
                }
            }
        }
        return null;
    }
    
    
    public final void updateSendEmailValidationEmail(final Customer customer, final Locale locale, final String forwardUrl) throws ServiceLayerException, UserNotFoundException, ErightsException {
        //Customer customer = customerDao.getCustomerByUsername(username);
        customer.setEmailVerificationState(EmailVerificationState.EMAIL_SENT);
        //customerDao.update(customer);
        updateCustomer(customer, false);
        sendEmailValidationEmail(customer, locale, forwardUrl);
    }
    
    public final String updateValidationEmail(final String tokenString) throws Exception {
        EmailTokenDto token = (EmailTokenDto) TokenConverter.decrypt(tokenString, new EmailTokenDto());
       CustomerDto customerDto = erightsFacade.getUserAccount(token.getUserId());
       Customer customer = convertCustomer(customerDto);
        //Customer customer = customerDao.getEntity(token.getUserId());
        customer.setEmailVerificationState(EmailVerificationState.VERIFIED);
        updateCustomer(customer, false);
        //customerDao.update(customer);
       
        return token.getOrignalUrl();
    }
    
    public final void updateResetCustomerPasswordwithUrl(final Customer customer, final Locale locale,final String successUrl,final String failureUrl, String tokenId)
            throws ServiceLayerException, PasswordPolicyViolatedServiceLayerException {

        //Customer customer = customerDao.getCustomerByUsername(username);
       
        if(StringUtils.isBlank(customer.getEmailAddress())) {
            throw new UserHasNoEmailServiceLayerException("Customer does not have a email address. User: " + customer.getUsername(), new Message(
                    "error.changing.customer.password.no.email",
                    "There was a problem changing your password. Please contact the system adminsitrator."));        	
        }
       /* customer.setResetPassword(true);
        customerDao.update(customer);*/

        try {
        	ChangePasswordDto changePasswordDto=new ChangePasswordDto();
        	changePasswordDto.setTokenId(tokenId);
        	String encryptedDto=TokenConverter.encrypt(changePasswordDto);
            erightsFacade.resetPassword(customer.getId(), encryptedDto, successUrl, failureUrl);
            sendResetPasswordEmail(customer, locale, successUrl, failureUrl, encryptedDto);
            AuditLogger.logEvent(customer, "Reset Password Successful");
        } 
        catch (PasswordPolicyViolatedException e) {
            throw new PasswordPolicyViolatedServiceLayerException("Password Policy Violated"
                    + customer.getUsername(), e, new Message("error.password.reset.denied",
                            "There was a problem changing your password. Please contact the system adminsitrator.")
            		);
        }
        catch (ErightsException e) {
            throw new ServiceLayerException("Erights problem changing the customer password.", e, new Message(
                    "error.changing.customer.password",
                    "There was a problem changing the customer password. Please contact the system adminsitrator."));
        } catch (Exception e) {
			//TO DO
			e.printStackTrace();
		} 
    }
    
    /**
     * Resets the users password and emails the password to them.
     * 
     * @param customer
     *            the customer
     * @param locale
     *            the locale
     * @throws ServiceLayerException
     *             service layer exception
     */
    public final void updateResetCustomerPassword(final Customer customer, final Locale locale,final String reqUrl, final String wsUserName)
            throws ServiceLayerException {

    	
        //Customer customer = customerDao.getCustomerByUsername(username);
        int i;
        String baseUrl = null;
        if(reqUrl!=null)
        {
        	i=reqUrl.lastIndexOf("/")+1;
        	baseUrl=reqUrl.substring(0, i);
        }
      /*  else
        {
        	baseUrl=EACSettings.getProperty(EACSettings.EAC_RESETPASSWORD_WS_BASEURL);
        }
        */
        String successUrl=baseUrl+"changePassword.htm";
        String failureUrl=baseUrl+"passwordResetFailure.htm";
        
        /*baseUrl = "http://localhost" ;
        String successUrl=baseUrl+"/changePassword.htm";
        String failureUrl=baseUrl+"/passwordResetFailure.htm";*/
        
        if(StringUtils.isBlank(customer.getEmailAddress())) {
            throw new UserHasNoEmailServiceLayerException("Customer does not have a email address. User: " + customer.getUsername(), new Message(
                    "error.changing.customer.password.no.email",
                    "There was a problem changing your password. Please contact the system adminsitrator."));        	
        }
       // String newPassword = PasswordGenerator.createPassword();//plain text password
       // customer.setPassword(new Password(newPassword, false));
        /*customer.setResetPassword(true);
        customerDao.update(customer);*/
        
      //Added wsUsername as part of OLB requirement, need to revert after Live
        
       String olbLandingPage = null;
       if (wsUserName != null ) {
    	  String[] olbUserNames = EACSettings.getProperty(EACSettings.OLB_WS_USER).split(",");
    	  for(String olbUsername : olbUserNames){
    		  olbLandingPage = wsUserName.equalsIgnoreCase(olbUsername) ? 
    				  EACSettings.getProperty(EACSettings.OLB_LANDING_PAGE_URL) : null;
    		  if(olbLandingPage!=null){
    			  break;
    		  }
    	  }
       }


        try {
        	ChangePasswordDto changePasswordDto=new ChangePasswordDto();
        	changePasswordDto.setUsername(customer.getUsername());
        	changePasswordDto.setURL(olbLandingPage);
        	
        	String encryptedDto=TokenConverter.encrypt(changePasswordDto);
        	erightsFacade.resetPassword(customer.getId(), encryptedDto, successUrl, failureUrl);

            sendResetPasswordEmail(customer, locale, successUrl, failureUrl, encryptedDto);
            AuditLogger.logEvent(customer, "Reset Password Successful");
        } 
        catch (PasswordPolicyViolatedException e) {
            throw new PasswordPolicyViolatedServiceLayerException("Password Policy Violated"
                    + customer.getUsername(), e, new Message("error.password.reset.denied",
                            "There was a problem changing your password. Please contact the system adminsitrator.")
            		);
        }
        catch (ErightsException e) {
            throw new ServiceLayerException("Erights problem changing the customer password.", e, new Message(
                    "error.changing.customer.password",
                    "There was a problem changing the customer password. Please contact the system adminsitrator."));
        } catch (Exception e) {
			throw new ServiceLayerException("Problem changing the customer password.", e, new Message(
                    "error.changing.customer.password",
                    "There was a problem changing the customer password. Please contact the system adminsitrator."));
		} 
    }

   


	@Override
    public final void passwordResetAttemptDenied(Customer customer, Locale locale) throws ServiceLayerException {

    	String msg = String.format("Password reset requested denied. customerType[%s]", customer.getCustomerType());
        AuditLogger.logEvent(customer,msg);

        if(StringUtils.isBlank(customer.getEmailAddress())) {
            throw new UserHasNoEmailServiceLayerException("Customer does not have a email address. User: " + customer.getUsername(), new Message(
                    "error.password.reset.denied",
                    "There was a problem during a Password Reset attempt. Please contact the System Administrator."));        	
        }

        sendPasswordResetAttemptDeniedEmail(customer, locale);

    }
    
    private void sendEmailValidationEmail(Customer customer, Locale locale, String forwardUrl) throws ServiceLayerException {
        try {
            MessageTextSource resource = new MessageTextSource(this.messageSource, locale);
            
            EmailTokenDto token = new EmailTokenDto(customer.getId(), forwardUrl);
            
            String registrationlink = EACSettings.getProperty(EACSettings.EMAIL_VALIDATION_TOKEN_URL) + TokenConverter.encrypt(token);
    
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("username", customer.getUsername());
            map.put("validationlink", registrationlink);
            map.put("resource", resource);
    
            String text = VelocityUtils.mergeTemplateIntoString(velocityEngine, "com/oup/eac/service/velocity/emailValidation.vm", map);
            MailCriteria mailCriteria = new MailCriteria();
            mailCriteria.setSubject(EACSettings.getProperty(EACSettings.EMAIL_VALIDATION));
            mailCriteria.setFrom(EmailUtils.getInternetAddressFrom());
            mailCriteria.addToAddress(customer.getEmailInternetAddress());
            mailCriteria.setText(text);
            emailService.sendMail(mailCriteria);
        } catch (Exception e) {
            throw new ServiceLayerException("Problem emailing the 'you cannot reset this customer's password' message.", e, new Message(
                    "error.password.reset.denied",
                    "There was a problem during a Password Reset attempt. Please contact the System Administrator."));
        } 
    }    
    
    private void sendPasswordResetAttemptDeniedEmail(Customer customer, Locale locale) throws ServiceLayerException {
    	try {
	    	MessageTextSource resource = new MessageTextSource(this.messageSource, locale);
	
	        Map<String, Object> map = new HashMap<String, Object>();
	        map.put("username", customer.getUsername());
	        map.put("password", customer.getPassword());
	        map.put("resource", resource);
	
	        String text = VelocityUtils.mergeTemplateIntoString(velocityEngine,
	                "com/oup/eac/service/velocity/passwordResetDenied.vm", map);
	        MailCriteria mailCriteria = new MailCriteria();
	        mailCriteria.setSubject(EACSettings.getProperty(EACSettings.PASSWORD_RESET_EMAIL_SUBJECT));
	        mailCriteria.setFrom(EmailUtils.getInternetAddressFrom());
	        mailCriteria.addToAddress(customer.getEmailInternetAddress());
	        mailCriteria.setText(text);
	        emailService.sendMail(mailCriteria);
    	} catch (UnsupportedEncodingException e) {
            throw new ServiceLayerException("Problem emailing the 'you cannot reset this customer's password' message.", e, new Message(
                    "error.password.reset.denied",
                    "There was a problem during a Password Reset attempt. Please contact the System Administrator."));
        } 
    }
    
    private void sendResetPasswordEmail(Customer customer, final Locale locale) throws ServiceLayerException {
    	try {
	    	MessageTextSource resource = new MessageTextSource(this.messageSource, locale);

	        Map<String, Object> map = new HashMap<String, Object>();
	        map.put("username", customer.getUsername());
	        map.put("password", customer.getPassword());
	        map.put("resource", resource);

	        String text = VelocityUtils.mergeTemplateIntoString(velocityEngine,
	                "com/oup/eac/service/velocity/passwordReset.vm", map);
	        MailCriteria mailCriteria = new MailCriteria();
	        mailCriteria.setSubject(EACSettings.getProperty(EACSettings.PASSWORD_RESET_EMAIL_SUBJECT));
	        mailCriteria.setFrom(EmailUtils.getInternetAddressFrom());
	        mailCriteria.addToAddress(customer.getEmailInternetAddress());
	        mailCriteria.setText(text);
	        emailService.sendMail(mailCriteria);
    	} catch (UnsupportedEncodingException e) {
            throw new ServiceLayerException("Problem emailing the customers new password.", e, new Message(
                    "error.changing.customer.password",
                    "There was a problem changing the customer password. Please contact the system adminsitrator."));
        } 
    }
    
    private void sendResetPasswordEmail(Customer customer, final Locale locale, String successUrl,
			String failureUrl, String tokenId) throws ServiceLayerException {
    	try {
	    	MessageTextSource resource = new MessageTextSource(this.messageSource, locale);
	    	
	    	ResetPasswordTokenDto resetPasswordTokenDto = new ResetPasswordTokenDto(
	    			customer.getId(), successUrl, failureUrl, tokenId);
	    	
			String encryptedDto = TokenConverter.encrypt(resetPasswordTokenDto);
			
			String url = EACSettings.getProperty(EACSettings.EAC_HOST_URL) 
					+ EACSettings.getProperty(EACSettings.PASSWORD_RESET_EMAIL_URL) + encryptedDto;
			
	        Map<String, Object> map = new HashMap<String, Object>();
	        map.put("username", customer.getUsername());
	        map.put("resetUrl", url);
	        map.put("resource", resource);
	
	        String text = VelocityUtils.mergeTemplateIntoString(velocityEngine,
	                "com/oup/eac/service/velocity/passwordResetNew.vm", map);
	        MailCriteria mailCriteria = new MailCriteria();
	        mailCriteria.setSubject(EACSettings.getProperty(EACSettings.PASSWORD_RESET_EMAIL_SUBJECT_NEW));
	        mailCriteria.setFrom(EmailUtils.getInternetAddressFrom());
	        mailCriteria.addToAddress(customer.getEmailInternetAddress());
	        mailCriteria.setText(text);
	        emailService.sendMail(mailCriteria);
	        } catch (UnsupportedEncodingException e) {
            throw new ServiceLayerException("Problem emailing the customers password reset link.", e, new Message(
                    "error.changing.customer.password",
                    "There was a problem changing the customer password. Please contact the system adminsitrator."));
        } catch (Exception e) {
        	throw new ServiceLayerException("Problem emailing the customers password reset link.", e, new Message(
                    "error.changing.customer.password",
                    "There was a problem changing the customer password. Please contact the system adminsitrator."));
        }
    }
    


    @Override
    public void notifyCustomerAccountNotFound(final String emailAddress, final Locale locale)
            throws ServiceLayerException {
        try {
            MessageTextSource resource = new MessageTextSource(this.messageSource, locale);

            Map<String, Object> map = new HashMap<String, Object>();
            map.put("username", emailAddress);
            map.put("resource", resource);

            String text = VelocityUtils.mergeTemplateIntoString(velocityEngine,
                    "com/oup/eac/service/velocity/noAccountFound.vm", map);
            final MailCriteria mailCriteria = new MailCriteria();
            mailCriteria.setSubject(EACSettings.getProperty(EACSettings.PASSWORD_RESET_EMAIL_SUBJECT));
            mailCriteria.setFrom(EmailUtils.getInternetAddressFrom());
            mailCriteria.addToAddress(new InternetAddress(emailAddress));
            mailCriteria.setText(text);
            emailService.sendMail(mailCriteria);
        } catch (Exception e) {
            throw new ServiceLayerException("Problem notifying customer that their account was not found.", e,
                    new Message("error.notifying.customer.when.no.account",
                            "There was a problem notifying the customer that their account was not found. "
                                    + "Please contact the system adminsitrator."));
        }
    }


    /**
     * @param changePasswordDto
     *            the changePasswordDto
     * @throws ServiceLayerException
     *             the exception
     */
    public final void saveChangeCustomerPassword(final ChangePasswordDto changePasswordDto, Customer customer)
            throws ServiceLayerException, PasswordPolicyViolatedServiceLayerException {

        //Customer customer = customerDao.getCustomerByUsername(changePasswordDto.getUsername());
        
        /*boolean newPasswordIsSameAsExisting = customer.getPassword().equals(passwordEncoder.encodePassword(changePasswordDto.getNewPassword(), null));
        if (newPasswordIsSameAsExisting) {
            if (customer.isResetPassword() == false) {
                String msg = String.format("Customer[%s] change password request was ignored as new password was same as old password",customer.getUsername()); 
                LOGGER.warn(msg);
                return;
            } else {
                throw new PasswordSameAsOldServiceLayerException("Customer is trying to use their old password as a new password."
                     	+ changePasswordDto.getUsername(), new Message(
                       	"error.customer.newpassword.same.as.oldpassword",
                        "Your new password can not be the same as your old password. Please try another."));
            }
        }*/
        try {
        	
            if (customer == null) {
            	customer = getCustomerByUsername(changePasswordDto.getUsername());
                //throw new ServiceLayerException("Failed to lookup customer with username" + changePasswordDto.getUsername());
            }
            if(customer.getCustomerType() == CustomerType.SHARED) {
                throw new ServiceLayerException("Customer is trying to change password on a shared account."
                        + changePasswordDto.getUsername(), new Message("label.change.password.denied",
                        "Unfortunately we cannot change your password as you have a shared account."));        	            	
            }
            erightsFacade.changePasswordByUserId(customer.getId(), changePasswordDto.getNewPassword());
            /*customer.setResetPassword(false);
            customer.setPassword(new Password(changePasswordDto.getNewPassword(), false));
            customerDao.update(customer);*/
            AuditLogger.logEvent(customer, "Change Password Successful");
        } catch (PasswordPolicyViolatedException e) {
        	throw new PasswordPolicyViolatedServiceLayerException(e.getMessage().replace("Password Policy Violated. ", ""));
        }  catch (UserNotFoundException e) {
       	 	throw new ServiceLayerException("No customer found for User Name : "+ changePasswordDto.getUsername()); 
        } catch (ErightsException e) {
            throw new ServiceLayerException("Erights problem changing customer password for : "
                    + changePasswordDto.getUsername(), e, new Message("error.changing.customer.password",
                            "There was a problem changing your password. Please contact the system adminsitrator."));
        } catch (Exception e) {
            throw new ServiceLayerException("Erights problem changing customer password for : "
                    + changePasswordDto.getUsername(), e, new Message("error.changing.customer.password",
                            "There was a problem changing your password. Please contact the system adminsitrator."));
        }
        
    }

    /**
     * @param accountRegistrationDto
     *            account registraion dto
     * @return the CustomerSessionDto
     * @throws ServiceLayerException
     *             the exception
     */
    public final CustomerSessionDto saveCustomerRegistration(final AccountRegistrationDto accountRegistrationDto)
            throws ServiceLayerException {

        try {
            Customer customer = new Customer();
            customer.setUsername(accountRegistrationDto.getUsername());
			customer.setEmailAddress(accountRegistrationDto.getEmail());
            customer.setPassword(new Password(accountRegistrationDto.getPassword(), false));
            customer.setFirstName(accountRegistrationDto.getFirstName());
            customer.setFamilyName(accountRegistrationDto.getFamilyName());            
            customer.setCustomerType(CustomerType.SELF_SERVICE);
            customer.getCustomerType().setConcurrency(CustomerType.SELF_SERVICE.getConcurrency());
            customer.setEmailVerificationState(EmailVerificationState.UNKNOWN);
            customer.setLocked(false);
            
            String timeZoneId = getValidatedTimeZoneId(accountRegistrationDto.getTimeZoneId());
            customer.setTimeZone(timeZoneId);
            customer.setLocale(accountRegistrationDto.getUserLocale());
            
           /* CustomerDto customerDto = new CustomerDto(customer);
            
            customerDto = erightsFacade.createUserAccount(customerDto);
            customer.setErightsId(customerDto.getErightsId());
            customer.setLastLoginDateTime(new DateTime());
            customerDao.save(customer);*/
            CustomerDto customerDto=saveCustomer(customer, false);
            customer.setId(customerDto.getUserId());
            for( Component component : accountRegistrationDto.getComponents()) {
	            for (Field field : component.getFields()) {
	                String answers = accountRegistrationDto.getAnswersForQuestion(field);
	                if(StringUtils.isNotBlank(answers) || field.getElement().getQuestion().getDescription().equalsIgnoreCase(PageConstant.MARKETING_PREF) ) {
	                	LOGGER.debug("Account creation customer Id : " + customer.getId());
	                	Answer answer = new Answer(field.getElement().getQuestion(), customer.getId(), answers);
			            answerDao.save(answer);
	                }
	            }
            }

            CustomerSessionDto customerSessionDto = new CustomerSessionDto();
            
            AuthenticationResponseDto response = erightsFacade.authenticateUser(customerDto.getLoginPasswordCredential());
            customerSessionDto.setSession(response.getSessionKey());
           // customer.setErightsId(customerDto.getErightsId());
            
        	//TODO:Added to display logout button.
            customer.setCreatedDate(new DateTime());
            customerSessionDto.setCustomer(customer);
            return customerSessionDto;

        } catch (InvalidCredentialsException e) {
            throw new ServiceLayerException("Invalid credentials.", e, new Message(
                    "error.problem.with.login.credentials", "There was a problem with your login credentials."));
        } catch (UserAlreadyExistsException e) {
            throw new ServiceLayerException("A user with username: " + accountRegistrationDto.getUsername()
                    + " already exists in erights.", e, new Message("error.username.taken",
                    "This username is already taken. Please try another.", new Object[] { EACSettings.getProperty(EACSettings.EAC_LOGIN_URL) }));
        } catch (ErightsException e) {
            throw new ServiceLayerException(
                    "Erights problem creating customer." + accountRegistrationDto.getUsername(), e, new Message(
                            "error.problem.creating.customer",
                            "There was a problem creating your account. Please contact the system adminsitrator."));
        }
    }

    @Override
    @PreAuthorize("hasRole('ROLE_LIST_CUSTOMER')")
	public Paging<Customer> searchCustomers(final CustomerSearchCriteria customerSearchCriteria, final PagingCriteria pagingCriteria)
			throws ServiceLayerException {
    	AmazonSearchRequest request = new AmazonSearchRequest();
    	request.setResultsPerPage(pagingCriteria.getItemsPerPage());
    	//request.setStartAt(pagingCriteria.getFirstResult());
    	
    	List<AmazonSearchFields> searchFieldsList = new ArrayList<AmazonSearchFields>();
    	List<Customer> customers = null;
    	if(ArrayUtils.isEmpty(customerSearchCriteria.getRegistrationDataKeywords())){
	    	if(customerSearchCriteria.getFirstName()!=null){
	    		AmazonSearchFields searchField = new AmazonSearchFields();
	    		searchField.setName("firstname");
	    		String firstName = customerSearchCriteria.getFirstName().replace("'", "");
	    		searchField.setValue(firstName);
	    		searchField.setType("String");
	    		searchFieldsList.add(searchField);
	    	}
	    	if(customerSearchCriteria.getFamilyName()!=null){
	    		AmazonSearchFields searchField = new AmazonSearchFields();
	    		searchField.setName("lastname");
	    		String familyname = customerSearchCriteria.getFamilyName().replace("'", "");
	    		searchField.setValue(familyname);
	    		searchField.setType("String");
	    		searchFieldsList.add(searchField);
	    	}
	    	if(customerSearchCriteria.getUsername()!=null){
	    		AmazonSearchFields searchField = new AmazonSearchFields();
	    		searchField.setName("username");
	    		String userName = customerSearchCriteria.getUsername().replace("'", "");
	    		searchField.setValue(userName);
	    		searchField.setType("String");
	    		searchFieldsList.add(searchField);
	    		
	    	}
	    	if(customerSearchCriteria.getEmail()!=null){
	    		AmazonSearchFields searchField = new AmazonSearchFields();
	    		searchField.setName("emailaddress");
	    		searchField.setValue(customerSearchCriteria.getEmail());
	    		searchField.setType("String");
	    		searchFieldsList.add(searchField);
	    	}
	    	
	    	if(customerSearchCriteria.getCreatedDateFrom()!=null){
	    		AmazonSearchFields searchField = new AmazonSearchFields();
	    		searchField.setName("createddatefrom");
	    		searchField.setValue(customerSearchCriteria.getCreatedDateFrom().toString());
	    		searchField.setType("datetime");
	    		searchFieldsList.add(searchField);
	    	}
	    	
	    	if(customerSearchCriteria.getCreatedDateTo()!=null){
	    		AmazonSearchFields searchField = new AmazonSearchFields();
	    		searchField.setName("createddateto");
	    		searchField.setValue(customerSearchCriteria.getCreatedDateTo().toString());
	    		searchField.setType("datetime");
	    		searchFieldsList.add(searchField);
	    	}

	    	if(customerSearchCriteria.getExternalId()!=null){
	    		AmazonSearchFields searchField = new AmazonSearchFields();
	    		searchField.setName("externalids");
	    		String externalId = customerSearchCriteria.getExternalId().replace("'", "");
	    		searchField.setValue(externalId);
	    		searchField.setType("String");
	    		searchFieldsList.add(searchField);
	    	}
    	}
    	boolean registrationDataAvailable = false;
    	List<String> customerIds = new ArrayList<String>();
		if (ArrayUtils.isNotEmpty(customerSearchCriteria
				.getRegistrationDataKeywords())) {
			customerIds = (ArrayList<String>) customerDao
					.getCustomerIdsByRegistrationDataSearch(customerSearchCriteria, pagingCriteria);
			
			if (customerIds.size() > 0 && !customerIds.isEmpty()) {
				ArrayList<String> customerErightsId = new ArrayList<String>() ;
				ArrayList<String> oupIds = new ArrayList<String>() ;
				Map<String, String> customerErightsIdMap = new HashMap<String, String>() ;
				int noOfBatch = (int) Math.ceil(Double.valueOf(customerIds.size())/Double.valueOf(FETCH_ERIGHTS_ID_FROM_OUP_ID)) ; 
				for (int i = 0 ; i < noOfBatch ; i ++) {
					if ( i == noOfBatch -1 ) {
						oupIds.addAll(customerIds.subList(i*FETCH_ERIGHTS_ID_FROM_OUP_ID, customerIds.size()));
					} else {
						oupIds.addAll(customerIds.subList(i*FETCH_ERIGHTS_ID_FROM_OUP_ID, (i+1)*FETCH_ERIGHTS_ID_FROM_OUP_ID ));
					}
					/*
					oupIds.add(oupId);
					if (oupIds.size() == FETCH_ERIGHTS_ID_FROM_OUP_ID) {*/
						customerErightsIdMap.putAll( OupIdMappingUtil.getErightsIdsFromOupIds(oupIds, false, OupIdMappingUtil.Entity.USER.toString()) );
						oupIds.clear();
					/*}*/
				}
				customerErightsId.addAll(customerErightsIdMap.values());
				/*if (oupIds.size() > 0 ) {
					customerErightsIdMap.putAll(OupIdMappingUtil.getErightsIdsFromOupIds(oupIds, false, OupIdMappingUtil.Entity.USER.toString())) ;
					customerErightsId.addAll(customerErightsIdMap.values());
					oupIds.clear();
				}*/
				ArrayList<Integer> customerErightsIdOrdered = (ArrayList<Integer>)customerDao.getCustomerIdListByCustomerIdListInSortedOrderOfDateCreated(customerErightsId) ;
				List<Integer> customerErightsIdOrderedPagination = new ArrayList<Integer>() ;
				
				int firstResultIndex = (pagingCriteria.getRequestedPage() - 1) * pagingCriteria.getItemsPerPage() ;
				for (Integer erightsId : customerErightsIdOrdered.subList(firstResultIndex, customerErightsIdOrdered.size())) {
					customerErightsIdOrderedPagination.add(erightsId) ;
				}
				List<String> orderedCustomerIdList = new ArrayList<String>() ;
				
	            for (Integer erightsId : customerErightsIdOrderedPagination) {
	            	for(Map.Entry entry: customerErightsIdMap.entrySet()){
		            	if(erightsId.toString().equals(entry.getValue().toString())){
		            		orderedCustomerIdList.add(entry.getKey().toString());
			                break; 
		            	}
	            	}
	            }
	            
	            
				List<String> custIds = new ArrayList<String>();
				if(orderedCustomerIdList.size()>pagingCriteria.getItemsPerPage()){
					for(String custId : orderedCustomerIdList){
						if(custIds.size()<pagingCriteria.getItemsPerPage())
							custIds.add(custId);
					}
				customerIds = custIds;
				} else {
					customerIds = orderedCustomerIdList;
				}
			}
			
			String[] custList = new String[customerIds.size()];
			int i = 0;
			for (String custId : customerIds) {
				if (!custId.equals("")) {
					custList[i++] = custId;
					registrationDataAvailable = true;
				}
			}
			if(registrationDataAvailable){
				AmazonSearchFields searchField = new AmazonSearchFields();
				searchField.setName(SearchDomainFields.USER_USERID);
				searchField.setValue(Arrays.toString(custList).replace("[", "").replace("]", ""));
				searchField.setType("String");
				searchFieldsList.add(searchField);
			}else{
				customers = new ArrayList<Customer>();
				return Paging.valueOf(pagingCriteria, customers, 0);
			}
		}
    	
    	List<String> searchResultFieldsList = new ArrayList<String>();
		searchResultFieldsList.add("fistname");
		searchResultFieldsList.add("username");
		searchResultFieldsList.add("lastname");
		searchResultFieldsList.add("lastlogin");
		searchResultFieldsList.add("emailaddress");
		searchResultFieldsList.add("createddate");
		searchResultFieldsList.add("userid");
		searchResultFieldsList.add("licensecount");
		//searchResultFieldsList.add("Createddateto");
		
		HashMap<String, String> hMap1 = new HashMap<String, String>();
		hMap1.put(pagingCriteria.getSortColumn(), "asc");
		//hMap1.put("lastname", "desc");
		
		request.setSortFields(hMap1);
		request.setSearchResultFieldsList(searchResultFieldsList);
		
    	request.setSearchFieldsList(searchFieldsList);
    	if(!registrationDataAvailable){
    		LOGGER.debug("start at" + (pagingCriteria.getRequestedPage()-1)*pagingCriteria.getItemsPerPage());
    		request.setStartAt((pagingCriteria.getRequestedPage()-1)*pagingCriteria.getItemsPerPage());
    	} 
    	System.out.println(request);
    	//request.setSortFields(pagingCriteria.getSortColumn());
    	//request.setSearchFieldsList(pagingCriteria.get);
    	
    	AmazonCloudSearchServiceImpl cloudSearch = new AmazonCloudSearchServiceImpl();
    	AmazonSearchResponse response = null;
    	int count = 0;
    	
    	/*if(customerIds.size()==0 || (registrationDataAvailable && customerIds.size()>0)){*/
    		response = cloudSearch.searchUser(request,false, true);
    		ConvertSearch convert = new ConvertSearch();
        	customers = convert.convertCustomer(response);
        	count = response.getResultsFound();
    	/*} else {
    		customers = new ArrayList<Customer>();
    	}*/
    	
    
    	//List<Customer> customers = customerDao.searchCustomers(customerSearchCriteria, pagingCriteria);
		//int count = customerDao.countSearchCustomers(customerSearchCriteria);
    	/*if(.getCreatedDateFrom()!=null){
    	}*/
    	if(customerSearchCriteria.getCreatedDateTo()!=null){
    		
    	}
		AuditLogger.logEvent("Search Customers", "Username:"+customerSearchCriteria.getUsername(), AuditLogger.customerSearchCriteria(customerSearchCriteria));
		//return Paging.valueOf(pagingCriteria, customers, count);
		if(registrationDataAvailable){
			int countResult = customerDao.countSearchCustomers(customerSearchCriteria, pagingCriteria);
			return Paging.valueOf(pagingCriteria, customers, countResult);
		}else{
			return Paging.valueOf(pagingCriteria, customers, count);
		}
    }


    @Override
    public Customer getCustomerById(String id) {

    	Customer customer = null;
    	/*customerDao.getEntity(id);
    	customerDao.refresh(customer);*/
    	List<String> sessions = new ArrayList<String>();

    	try { 
    		CustomerDto cdto= erightsFacade.getUserAccount(id);
    		if(cdto != null){
    			customer = convertCustomer(cdto);
    			sessions = erightsFacade.getSessionsByUserId(customer.getId());
    			customer.setSessions(sessions);
    		}
    	} catch (ErightsSessionNotFoundException e) {
    		sessions.add("-");
    		customer.setSessions(sessions);
    	} catch (ErightsException e) { 
    		sessions.add("-");
    		customer.setSessions(sessions);
    	}

    	return customer;
    }



    /**
     * {@inheritDoc}
     * @throws ErightsException 
     */
    @Override
    @PreAuthorize("hasRole('ROLE_CREATE_CUSTOMER') or T(com.oup.eac.domain.utils.audit.EacApp).isWebServices()")
    public final CustomerDto saveCustomer(final Customer customer, final boolean generatePassword) throws ServiceLayerException, UsernameExistsException, ErightsException {
    	Customer eacCustomer = new Customer();
    	//saveCustomerPart1(eacCustomer, generatePassword);
        CustomerDto customerDto=saveCustomerPart2(eacCustomer, customer, generatePassword);
        AuditLogger.customerSaved(customer);
        return customerDto;
    }
    
    
    /**
     * {@inheritDoc}
     */
    @Override
    @PreAuthorize("hasRole('ROLE_CREATE_CUSTOMER') or T(com.oup.eac.domain.utils.audit.EacApp).isWebServices()")
    public final void saveCustomerPart1(final Customer customer, final boolean generatePassword) throws ServiceLayerException {
        //at this point we expect the customer does NOT have an id
        //Assert.isNull(customer.getId());
                
        if (generatePassword) {
            customer.setPassword(new Password(PasswordGenerator.createPassword(), false));
            customer.setResetPassword(true);
        }
    	customer.setLocked(false);
        //customerDao.save(customer);
    }
    
    /**
     * {@inheritDoc}
     * @throws ErightsException 
     */
    @Override
    @PreAuthorize("hasRole('ROLE_CREATE_CUSTOMER') or T(com.oup.eac.domain.utils.audit.EacApp).isWebServices()")
    public final CustomerDto saveCustomerPart2(final Customer eacCustomer, final Customer customer, final boolean generatePassword) throws ServiceLayerException, ErightsException {
    	CustomerDto customerDto=null;
        try {
        	if ( generatePassword ) {
        		customer.setPassword(new Password(PasswordGenerator.createPassword(), false));
                customer.setResetPassword(true);
        	}
            //at this point we expect the customer does have an id
            //Assert.notNull(eacCustomer.getId());
            
            customer.setLocked(false);
            customerDto = new CustomerDto(customer);
            customerDto = erightsFacade.createUserAccount(customerDto);
            //customer.setErightsId(customerDto.getErightsId());
            //eacCustomer.setErightsId(customerDto.getErightsId());
            //customerDao.update(eacCustomer);
            // Must flush session before e-mailing in case there are any issues (e.g. stale object problem)
            // customerDao.flush();
			
            customer.setId(customerDto.getUserId());
            //save the customer record to db
            eacCustomer.setId(customerDto.getUserId());
            eacCustomer.setCreatedDate(new DateTime());
			//customerDao.save(eacCustomer);
			//customerDao.flush();
            processPasswordAfterSaveOrUpdate(customer, generatePassword);

        } catch (UserAlreadyExistsException e) {
            throw new UsernameExistsException("A user with username: " + customer.getUsername()
                    + " already exists in erights.", e, new Message("error.username.taken",
                    "This username is already taken. Please try another."));
        } catch (ErightsException e) {
        	if(e.getMessage().contains("does not exitsts"))
        		throw new ErightsException(e.getMessage(), e.getCode());
        	if(e.getErrorCode()==1096){
        		throw new ErightsException(e.getMessage(), e.getCode(), e.getErrorCode());
        	} else if(e.getErrorCode()==1044){
        		throw e;
        	} else if (e.getErrorCode() == 2039) {
        		throw new UsernameExistsException("A user with username: " + customer.getUsername()
                        + " already exists in erights.", e, new Message("error.username.taken",
                        "This username is already taken. Please try another."));
        	}
        	else{
        			throw new ServiceLayerException(
        		
                    "Erights problem creating customer." + customer.getUsername(), e, new Message(
                            "error.problem.creating.customer",
                            "There was a problem creating your account. Please contact the system administrator."));
        	}
        }
        return customerDto;	
    }
    
    /**
     * Update an existing customer.
     * 
     * @param customer
     * @param generatePassword
     * @throws ServiceLayerException
     */
    @Override
    @PreAuthorize("hasRole('ROLE_UPDATE_CUSTOMER') or T(com.oup.eac.domain.utils.audit.EacApp).isWebServices()")
    public void updateCustomer(final Customer customer, final boolean generatePassword) throws ServiceLayerException, UsernameExistsException, PasswordPolicyViolatedServiceLayerException , UserLoginCredentialAlreadyExistsException{
    
        try {
            
            CustomerDto customerDto = erightsFacade.getUserAccount(customer.getId());
            if (generatePassword) {
                customer.setPassword(new Password(PasswordGenerator.createPassword(), false));
                customer.setResetPassword(true);
            } 
            
            //boolean customerEmailHasChanged = customerDao.hasCustomerEmailChanged(customerDto.getUsername(), customer.getEmailAddress());
            if(!customer.getEmailAddress().equals(customerDto.getEmailAddress())) {
                customer.setEmailVerificationState(EmailVerificationState.UNKNOWN);
            }
            
            
          //Added to remove External Id
            //Set<ExternalCustomerId> extCustId = customer.getExternalIds();
            //Set<ExternalCustomerId> extCustdto= customerDto.getExternalIds();
            /*for(ExternalCustomerId exId: extCustdto){
            	boolean flag= false;
            	for(ExternalCustomerId extId2 : extCustId){
            		if(extId2.getExternalSystemIdType().getName().equals(exId.getExternalSystemIdType().getName())&& extId2.getExternalSystemIdType().getExternalSystem().getName().equals(exId.getExternalSystemIdType().getExternalSystem().getName())){
            			flag=true;
            		}
            	}
            	if(!flag){
        			erightsFacade.removeUserExternalId(exId.getExternalId(), customer.getId());
        		}
            }*/
            //merge changes from update back into customerDto
            customerDto.mergeCustomerChanges(customer, false);
            //customerDao.update(customer);
            
            
            customerDto.setTimeZone(customer.getTimeZone());
            customerDto.setLocale(customer.getLocale()); 
           
            erightsFacade.updateUserAccount(customerDto);

            //Must flush session before e-mailing in case there are any issues (e.g. stale object problem) 
            //customerDao.flush();
            processPasswordAfterSaveOrUpdate(customer, generatePassword);
        } catch (UserLoginCredentialAlreadyExistsException e) {
            throw new UsernameExistsException("A user with username: " + customer.getUsername()
                    + " already exists in erights.", e, new Message("error.username.taken",
                    "This username is already taken. Please try another."));
        }
        catch (PasswordPolicyViolatedException e) {
        	throw new PasswordPolicyViolatedServiceLayerException(e.getMessage());
        }
        
        catch (ErightsException e) {
        	if (e.getErrorCode().equals(1075)) {
        		processPasswordAfterSaveOrUpdate(customer, generatePassword);
        	} else {
        		throw new ServiceLayerException(
                        "Erights problem updating customer." + customer.getUsername(), e, new Message(
                                "error.problem.updating.customer",
                                "There was a problem updating your account. Please contact the system administrator."));
        	}            
        }
        AuditLogger.customerUpdated(customer);
    }
    
    @Override
    @PreAuthorize("T(com.oup.eac.domain.utils.audit.EacApp).isWebServices()")
    public void updateCustomerFromWS(final Customer customer, String newEmailAddress) throws ServiceLayerException, UsernameExistsException, PasswordPolicyViolatedServiceLayerException , UserLoginCredentialAlreadyExistsException{
    
        try {
            
            CustomerDto customerDto = convertToCustomerDto(customer);
             
            
            //boolean customerEmailHasChanged = customerDao.hasCustomerEmailChanged(customerDto.getUsername(), customer.getEmailAddress());
            if(!customer.getEmailAddress().equals(newEmailAddress)) {
            	customerDto.setEmailVerificationState(EmailVerificationState.UNKNOWN);
            }
            
            
          /*//Added to remove External Id
            Set<ExternalCustomerId> extCustId = customer.getExternalIds();
            Set<ExternalCustomerId> extCustdto= customerDto.getExternalIds();
            for(ExternalCustomerId exId: extCustdto){
            	boolean flag= false;
            	for(ExternalCustomerId extId2 : extCustId){
            		if(extId2.getExternalSystemIdType().getName().equals(exId.getExternalSystemIdType().getName())&& extId2.getExternalSystemIdType().getExternalSystem().getName().equals(exId.getExternalSystemIdType().getExternalSystem().getName())){
            			flag=true;
            		}
            	}
            	if(!flag){
        			erightsFacade.removeUserExternalId(exId.getExternalId(), customer.getId());
        		}
            }
            //merge changes from update back into customerDto
            customerDto.mergeCustomerChanges(customer, false);
            //customerDao.update(customer);
            
            
            customerDto.setTimeZone(customer.getTimeZone());
            customerDto.setLocale(customer.getLocale()); */
           
            erightsFacade.updateUserAccount(customerDto);

            //Must flush session before e-mailing in case there are any issues (e.g. stale object problem) 
            //customerDao.flush();
            //processPasswordAfterSaveOrUpdate(customer, generatePassword);
        } catch (UserLoginCredentialAlreadyExistsException e) {
            throw new UsernameExistsException("A user with username: " + customer.getUsername()
                    + " already exists in erights.", e, new Message("error.username.taken",
                    "This username is already taken. Please try another."));
        }
        catch (PasswordPolicyViolatedException e) {
        	throw new PasswordPolicyViolatedServiceLayerException(e.getMessage());
        }
        
        catch (ErightsException e) {
        	if (e.getErrorCode().equals(1075)) {
        		//processPasswordAfterSaveOrUpdate(customer, generatePassword);
        	} else {
        		throw new ServiceLayerException(
                        "Erights problem updating customer." + customer.getUsername(), e, new Message(
                                "error.problem.updating.customer",
                                "There was a problem updating your account. Please contact the system administrator."));
        	}            
        }
        AuditLogger.customerUpdated(customer);
    }
    
    @Override
    @PreAuthorize("hasRole('ROLE_UPDATE_CUSTOMER') or T(com.oup.eac.domain.utils.audit.EacApp).isWebServices()")
    public void updateCustomerDetails(final Customer customer) throws ServiceLayerException, UsernameExistsException, UserLoginCredentialAlreadyExistsException{
    
        try {
            
            CustomerDto customerDto = erightsFacade.getUserAccount(customer.getId());
                        
            //boolean customerEmailHasChanged = customerDao.hasCustomerEmailChanged(customerDto.getUsername(), customer.getEmailAddress());
            if(!customer.getEmailAddress().equals(customerDto.getEmailAddress())) {
                customer.setEmailVerificationState(EmailVerificationState.UNKNOWN);
            }
            
            customer.setUsername(customerDto.getUsername());
            customer.setPassword(null);
            //merge changes from update back into customerDto
            customerDto.mergeCustomerChanges(customer, false);
            //customerDao.update(customer);
            
            customerDto.setTimeZone(customer.getTimeZone());
            customerDto.setLocale(customer.getLocale());
           
            erightsFacade.updateUserAccount(customerDto);

            //Must flush session before e-mailing in case there are any issues (e.g. stale object problem) 
            //customerDao.flush();
//            processPasswordAfterSaveOrUpdate(customer, generatePassword);
        } catch (UserLoginCredentialAlreadyExistsException e) {
            throw new UsernameExistsException("A user with username: " + customer.getUsername()
                    + " already exists in erights.", e, new Message("error.username.taken",
                    "This username is already taken. Please try another."));
        }
        catch (PasswordPolicyViolatedException e) {
        	throw new PasswordPolicyViolatedServiceLayerException(e.getMessage());
        }
        
        catch (ErightsException e) {
            throw new ServiceLayerException(
                    "Erights problem updating customer." + customer.getUsername(), e, new Message(
                            "error.problem.updating.customer",
                            "There was a problem updating your account. Please contact the system administrator."));
        }
        AuditLogger.customerUpdated(customer);
    }
    
    @Override
    @PreAuthorize("hasRole('ROLE_UPDATE_CUSTOMER') or T(com.oup.eac.domain.utils.audit.EacApp).isWebServices()")
    public void updateCustomerCredentials(final Customer customer, final boolean generatePassword)  throws ServiceLayerException, UserLoginCredentialAlreadyExistsException {
    	
    	if (generatePassword) {
            customer.setPassword(new Password(PasswordGenerator.createPassword(), false));
            customer.setResetPassword(true);
        }
    	
    	try {
    		CustomerDto customerDto = erightsFacade.getUserAccount(customer.getId());
            
            //merge changes from update back into customerDto
            customerDto.mergeCustomerChanges(customer, true);
            erightsFacade.updateUserAccount(customerDto);
            customer.setEmailAddress(customerDto.getEmailAddress());
        	processPasswordAfterSaveOrUpdate(customer, generatePassword);
    	} catch (PasswordPolicyViolatedException p) {
    		throw new PasswordPolicyViolatedServiceLayerException(p.getMessage());
    	} catch (UserLoginCredentialAlreadyExistsException userException) {
    		throw new UserLoginCredentialAlreadyExistsException(userException.getMessage());
    	}
    	catch (ErightsException e) {
            throw new ServiceLayerException(
                    "Erights problem updating customer." + customer.getUsername(), e, new Message(
                            "error.problem.updating.customer",
                            "There was a problem updating your account. Please contact the system administrator."));
        }
    }
    
    
    @Override
    @PreAuthorize("hasRole('ROLE_UPDATE_CUSTOMER') or T(com.oup.eac.domain.utils.audit.EacApp).isWebServices()")
    public void updateExternalIds(final Customer customer)  throws ServiceLayerException, UserLoginCredentialAlreadyExistsException {
    	try {
    		CustomerDto customerDto = erightsFacade.getUserAccount(customer.getId());
    		//Added to remove External Id
            Set<ExternalCustomerId> extCustId = customer.getExternalIds();
            /*Set<ExternalCustomerId> extCustdto= customerDto.getExternalIds();
            for(ExternalCustomerId exId: extCustdto){
            	boolean flag= false;
            	for(ExternalCustomerId extId2 : extCustId){
            		if(extId2.getExternalSystemIdType().getName().equals(exId.getExternalSystemIdType().getName())&& extId2.getExternalSystemIdType().getExternalSystem().getName().equals(exId.getExternalSystemIdType().getExternalSystem().getName())){
            			flag=true;
            		}
            	}
            	if(!flag){
        			erightsFacade.removeUserExternalId(exId.getExternalId(), customer.getId());
        		}
            }*/
            customer.setPassword(null);
            customerDto.setExternalIds(extCustId);
            customer.setUsername(customerDto.getUsername());
            customerDto.resetLoginPassword(customer);
            erightsFacade.updateUserAccount(customerDto);
    	} catch (ErightsException e) {
            throw new ServiceLayerException(
                    "Erights problem updating customer." + customer.getUsername(), e, new Message(
                            "error.problem.updating.customer",
                            "There was a problem updating your account. Please contact the system administrator."));
        }
    }
    
    /**
     * Update an existing customer.
     * 
     * @param customer
     * @param generatePassword
     * @throws ServiceLayerException
     * @throws UserLoginCredentialAlreadyExistsException 
     */
    @Override
    @PreAuthorize("hasRole('ROLE_UPDATE_CUSTOMER')")
    public void updateCustomerProfile(final String customerId, final BasicProfileDto dto, final boolean generatePassword) throws ServiceLayerException, UsernameExistsException, PasswordPolicyViolatedServiceLayerException, UserLoginCredentialAlreadyExistsException {
    
        	Customer customer = getCustomerById(customerId);
            
            customer.setUsername(dto.getUsername());
            customer.setFirstName(dto.getFirstName());
            customer.setFamilyName(dto.getFamilyName());
            if(!customer.getEmailAddress().equals(dto.getEmail())) {
                customer.setEmailVerificationState(EmailVerificationState.UNKNOWN);
            }
            customer.setEmailAddress(dto.getEmail());
            customer.setLocale(dto.getUserLocale());
            customer.setTimeZone(dto.getTimezone());
            
            updateCustomer(customer, generatePassword);
            
    }
    
    /**
     * Shared by saveCustomer and updateCustomer. Get customer into
     * suitable state after save or update.
     * 
     * @param customer
     * @param generatePassword
     * @throws ServiceLayerException
     */
    private void processPasswordAfterSaveOrUpdate(final Customer customer, final boolean generatePassword) throws ServiceLayerException{
    	if (generatePassword) {
        	//sendResetPasswordEmail(customer, customer.getLocale());
        	String url = EACSettings.getProperty(EACSettings.EAC_HOST_URL) + "/eac/" ;
        	updateResetCustomerPassword(customer, customer.getLocale(), url, null);
        }
        
        //Password will be unhashed after creation/generation. It needs to be refereshed
        //to update the session with the hashed version after customerDao.update(customer) 
       /* if (!customer.getWrappedPassword().isHashed()) { 
        	customerDao.flush();
        	customerDao.clear();
        }*/
    }

    @Override
    public Customer getCustomerByExternalCustomerId(String systemId,
			String typeId, String externalId) throws ErightsException {
       //Need to change this after implementation
    	//Customer customer = customerDao.getCustomerByExternalCustomerId(systemId, typeId, externalId);
    	ExternalIdentifier external= new ExternalIdentifier();
    	external.setId(externalId);
    	external.setSystemId(systemId);
    	external.setTypeId(typeId);
    	 Customer customer = null;
        try {     
         /*   if(null!=customer && customer.getCustomerType() == CustomerType.SPECIFIC_CONCURRENCY){*/
                CustomerDto cdto= erightsFacade.getUserAccountByExternalId(external);
                /* if(cdto != null){
                    CustomerType ct=customer.getCustomerType();
                    ct.setConcurrency(cdto.getConcurrency());
                    customer.setCustomerType(ct);
                }               
            }   */
                customer = convertCustomer(cdto);
            
        } catch (ErightsException ee) { 
            String msg = String.format("unexpected atypon error");
            LOGGER.error(msg, ee);
        }
        return customer;
    }

    
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
    @Override
    public CustomerWithLicencesAndProductsDto getCustomerWithLicencesAndProductsByAdminUser(final String id, final AdminUser adminUser) throws ServiceLayerException {
    	Customer customer = getCustomerById(id);
    	List<LicenceDto> licences = licenceService.getLicencesForCustomer(customer);
    	Map<LicenceDto,List<Product>> licenceProducts = new HashMap<LicenceDto, List<Product>>();
    	 
    	//Iterate licences and remove items if no products are accessible
    	Iterator<LicenceDto> licenceDtos = licences.iterator();
    	while (licenceDtos.hasNext()) {
    		LicenceDto licenceDto = licenceDtos.next();
    		List<Product> products = productService.getProductsByErightsIdsAndAdminUser(licenceDto.getProductIds(), adminUser);
    		if (products.size() > 0) {
    			//At least one product is accessible for the current licence
    			licenceProducts.put(licenceDto, products);
    		} else {
    			//No products accessible. Remove from list returned
    			licenceDtos.remove();
    		}
    	}
    	
    	return new CustomerWithLicencesAndProductsDto(customer, licences, licenceProducts);
    }
    
    /**
     * Update an existing customer and their associated erights licences.
     *
     * @param customer the customer
     * @param generatePassword the generate password
     * @param licences the licences
     * @throws ServiceLayerException the service layer exception
     * @throws UsernameExistsException if the username set for the customer already exists.
     * @throws UserLoginCredentialAlreadyExistsException 
     */
     @Override
    public final void updateCustomerAndLicences(final Customer customer, final boolean generatePassword,
            final List<LicenceDto> licences) throws ServiceLayerException, UsernameExistsException, PasswordPolicyViolatedServiceLayerException, UserLoginCredentialAlreadyExistsException {
    	 updateCustomer(customer, generatePassword);
    	 licenceService.updateLicencesForCustomer(customer, licences);
     }
     
     /**
      * Gets the validated time zone id.
      *
      * @param timeZoneId the time zone id
      * @return the validated time zone id
      */
     private String getValidatedTimeZoneId(final String timeZoneId) {
         String result = null;
         if (StringUtils.isNotBlank(timeZoneId)) {
             if (DateUtils.isTimeZoneValid(timeZoneId)) {                 
                 result = timeZoneId;
             } else {
                 String msg = String.format("Unable to convert [%s] to valid TimeZoneId", timeZoneId); 
                 LOGGER.warn(msg);
             }
         }
         return result;
     }

   

    @Override
    @PreAuthorize("hasRole('ROLE_DELETE_CUSTOMER')")
    public void deleteCustomer(String customerId) throws ServiceLayerException {
    	deleteUser(customerId);
    }
    @Override
    @PreAuthorize("hasRole('ROLE_TRUSTED_SYSTEM_MASTER')")
    public void deleteTrustedSystem(String customerId) throws ServiceLayerException {
    	deleteUser(customerId);
    }
    private void deleteUser(String customerId) throws ServiceLayerException{
    	Customer customer = this.getCustomerById(customerId);
        Assert.isTrue(customer.getUserType() == UserType.CUSTOMER);
        String userId = customer.getId();
        String failedMessage = String.format("Failed to delete customer with id[%s]", customerId);
        try {

            customer.getAnswers().clear();
            //customer.getRegistrations().clear();
            //customer.getExternalIds().clear();
            
            //this.customerDao.deleteCustomer(customer);
            this.erightsFacade.deleteUserAccount(userId);

            AuditLogger.logEvent(customer, "Deleted.");
        } catch (UserNotFoundException unfe) {
            String msg = String.format("delete atypon user : eRightsId [%s] : user could not be found", userId);
            LOGGER.error(msg);
            //LOGGER.warn(msg, unfe);
            //throw new ServiceLayerException(failedMessage);
        } catch (ErightsException ex) {
            String msg = String.format("delete atypon user : eRightsId [%s] : unexpected atypon error", userId);
            LOGGER.error(msg, ex);
            throw new ServiceLayerException(failedMessage);
        } catch (RuntimeException rtex) {
            String msg = String.format("delete customer : [%s] : unexpected exception", customerId);
            LOGGER.error(msg, rtex);
            throw new ServiceLayerException(failedMessage);
        }
    }

    @Override
    public Set<Answer> getCustomerWithAnswersByCustomerId(String customerId) {
        Set<Answer> result = this.customerDao.getAnswersByCustomerId(customerId);
        return result;
    }
    
    @Override
    public List<Customer> getCustomerInvalidEmailAddress(){
    	return this.customerDao.getCustomerInvalidEmailAddress();
    }
    
    
    public String saveCleansedEmail(Customer customer){
    	customerDao.update(customer);
    	return "The customer"+customer.getUsername()+" is Saved";
    }
    
    /**
     * get customer sessions
     * 
     * @param userId
     * @return
     * @throws Exception
     */
    public List<String> getSessionsByUserId(Customer customer) throws ErightsSessionNotFoundException, ServiceLayerException {

        //Integer erightsId = customer.getErightsId();        
        //String failedMessage = String.format("Failed to get sessions of customer with EacId [%s] and eRightsId [%d]", customer.getId(),erightsId);
        
        try {
            List<String> sessions = erightsFacade.getSessionsByUserId(customer.getId());
            
            return sessions;
            
        } catch (ErightsSessionNotFoundException esnfe) {
            String msg = String.format("get sessions atypon user : userId [%s] : Session not found error", customer.getId());
            LOGGER.debug(msg, esnfe);
            throw new ErightsSessionNotFoundException(msg,esnfe.getCode());            
        } catch (ErightsException ee) { 
            String msg = String.format("get sessions atypon user : userId [%s] : unexpected atypon error", customer.getId());
            LOGGER.debug(msg, ee);
            throw new ServiceLayerException(msg);
        }
    }

    @Override
    public void checkCustomerInEright(String id) throws CustomerNotFoundServiceLayerException, ServiceLayerException {
    	/*Customer customer = customerDao.getEntity(id);
    	customerDao.refresh(customer);*/
    	try {
    		erightsFacade.getUserAccount(id);
    	} catch (UserNotFoundException e) {
    		String msg = String.format("check customer in atypon user : eRightsId [%s] : user not found atypon error", id);
            LOGGER.debug(msg, e);
			throw new CustomerNotFoundServiceLayerException(msg, e);
		} catch (ErightsException e) {
			String msg = String.format("check customer in atypon user : eRightsId [%s] : unexpected atypon error", id);
			LOGGER.debug(msg, e);
			throw new ServiceLayerException(msg);
		}
	}


	@Override
	public void removeLicence(final String erightsUserId, final String erightsLicenceId) throws LicenceNotFoundServiceLayerException, CustomerNotFoundServiceLayerException, ServiceLayerException {
		try {
			erightsFacade.removeLicence(erightsUserId, erightsLicenceId);
		} catch (LicenseNotFoundException e) {
			String msg = String.format("Remove licence for user : eRightsId [%d] : licence not found atypon error", erightsUserId);
            LOGGER.debug(msg, e);
			throw new LicenceNotFoundServiceLayerException(msg, e);
		} catch (UserNotFoundException e) {
			String msg = String.format("Remove licence for user : eRightsId [%d] : user not found atypon error", erightsUserId);
            LOGGER.debug(msg, e);
			throw new CustomerNotFoundServiceLayerException(msg, e);
		} catch (ErightsException e) {
			String msg = String.format("Remove licence for user : eRightsId [%d] : unexpected atypon error", erightsUserId);
			LOGGER.debug(msg, e);
			throw new ServiceLayerException(msg);
		}
	}

	/*
	 * Gaurav Soni : Performance improvements CR
	 * same as getCustomerById but
	 * extra atypon call for session is removed
	 * */
	@Override
	public Customer getCustomerByIdWs(String id) throws ErightsException{
		//Customer customer = customerDao.getEntity(id);
		Customer customer = null;
			//customerDao.refresh(customer);
			try {     
				/*if(null!=customer && customer.getCustomerType() == CustomerType.SPECIFIC_CONCURRENCY){*/
					CustomerDto cdto= erightsFacade.getUserAccount(id);
					customer = convertCustomer(cdto);
					/*if(cdto != null){
						CustomerType ct=customer.getCustomerType();
						ct.setConcurrency(cdto.getConcurrency());
						customer.setCustomerType(ct);
					}   */             
				/*}*/
			} catch (UserNotFoundException unfe) {
				String msg = String.format("User not found Exception");
				LOGGER.error(msg, unfe);
				throw new ErightsException(unfe.getMessage()) ;
			} catch (ErightsException ee) { 
				String msg = String.format("unexpected atypon error");
				LOGGER.error(msg, ee);   
				throw new ErightsException(ee.getMessage(), ee.getCode().toString(), ee.getErrorCode()) ;
			}
		
		return customer;
	}


	@Override
	public List<Customer> getCustomersByProductOwner(
			Set<DivisionAdminUser> divisionAdminUsers)
			throws ServiceLayerException {
		// TODO Auto-generated method stub
		return null ; //customerDao.getCustomersByProductOwner(getDivisions(divisionAdminUsers));
	}
	/*private List<String> getDivisions(Set<DivisionAdminUser> divisionAdminUsers) {
    	List<String> divisions = new ArrayList<String>();
    	for(DivisionAdminUser divisionAdminUser : divisionAdminUsers) {
    		divisions.add(divisionAdminUser.getDivision().getDivisionType());
    	}
    	return divisions;
    }*/
	
	@Override
    @PreAuthorize("hasRole('ROLE_MERGE_CUSTOMER')")
	public List<Customer> searchCustomersForMerging(final CustomerSearchCriteria customerSearchCriteria)
			throws ServiceLayerException {
    	AmazonSearchRequest request = new AmazonSearchRequest();
    	
    	request.setStartAt(0);
    	request.setResultsPerPage(20);
    	
    	List<AmazonSearchFields> searchFieldsList = new ArrayList<AmazonSearchFields>();
    	List<Customer> customers = null;
    	
	    	
	    	if(customerSearchCriteria.getEmail()!=null){
	    		AmazonSearchFields searchField = new AmazonSearchFields();
	    		searchField.setName("emailaddress");
	    		searchField.setValue(customerSearchCriteria.getEmail());
	    		searchField.setType("String");
	    		searchFieldsList.add(searchField);
	    	}
	    
    	List<String> searchResultFieldsList = new ArrayList<String>();
		searchResultFieldsList.add("fistname");
		searchResultFieldsList.add("username");
		searchResultFieldsList.add("lastname");
		searchResultFieldsList.add("login");
		searchResultFieldsList.add("emailaddress");
		searchResultFieldsList.add("createddate");
		searchResultFieldsList.add("userid");
		searchResultFieldsList.add("licensecount");
		//searchResultFieldsList.add("Createddateto");
		
		HashMap<String, String> hMap1 = new HashMap<String, String>();
		hMap1.put("createddate", "desc");
		//hMap1.put("lastname", "desc");
		
		request.setSortFields(hMap1);
		request.setSearchResultFieldsList(searchResultFieldsList);
		
    	request.setSearchFieldsList(searchFieldsList);
    	
    	System.out.println(request);
    	//request.setSortFields(pagingCriteria.getSortColumn());
    	//request.setSearchFieldsList(pagingCriteria.get);
    	
    	AmazonCloudSearchServiceImpl cloudSearch = new AmazonCloudSearchServiceImpl();
    	AmazonSearchResponse response = null;
    	int count = 0;
    	
    	/*if(customerIds.size()==0 || (registrationDataAvailable && customerIds.size()>0)){*/
    		response = cloudSearch.searchUser(request,false, true);
    		ConvertSearch convert = new ConvertSearch();
        	customers = convert.convertCustomer(response);
        	count = response.getResultsFound();
    	/*} else {
    		customers = new ArrayList<Customer>();
    	}*/
    	
    
		AuditLogger.logEvent("Search Customers for Merge Customers", "Email:"+customerSearchCriteria.getEmail(), AuditLogger.customerSearchCriteria(customerSearchCriteria));
		//return Paging.valueOf(pagingCriteria, customers, count);
		 return customers;
    }
	
	@Override
    @PreAuthorize("hasRole('ROLE_MERGE_CUSTOMER')")
    public void mergeCustomer(String customerId,String email) throws ServiceLayerException {
       

        String failedMessage="failed to merge ";
        try {

            this.erightsFacade.mergeCustomer(customerId, email);
            AuditLogger.logEvent(String.valueOf(customerId), "merged Customer");
        } catch (UserNotFoundException unfe) {
            String msg = String.format("merge atypon user : eRightsId [%d] : user could not be found", customerId);
            LOGGER.error(msg);
            //LOGGER.warn(msg, unfe);
            //throw new ServiceLayerException(failedMessage);
        } catch (ErightsException ex) {
            String msg = String.format("merge atypon user : eRightsId [%d] : unexpected atypon error", customerId);
            LOGGER.error(msg, ex);
            throw new ServiceLayerException(failedMessage);
        } catch (RuntimeException rtex) {
            String msg = String.format("merge customer : [%s] : unexpected exception", customerId);
            LOGGER.error(msg, rtex);
            throw new ServiceLayerException(failedMessage);
        }
    }

}


