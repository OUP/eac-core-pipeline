package com.oup.eac.ws.v2.service.impl;

import java.util.Locale;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Primary;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import com.oup.eac.common.utils.EACSettings;
import com.oup.eac.common.utils.username.UsernameValidator;
import com.oup.eac.domain.Answer;
import com.oup.eac.domain.Customer;
import com.oup.eac.domain.utils.audit.AuditLogger;
import com.oup.eac.dto.ChangePasswordDto;
import com.oup.eac.dto.CustomerSessionDto;
import com.oup.eac.dto.ExternalCustomerIdDto;
import com.oup.eac.service.CustomerService;
import com.oup.eac.service.ExternalIdService;
import com.oup.eac.service.InvalidCredentialsServiceLayerException;
import com.oup.eac.service.ServiceLayerException;
import com.oup.eac.service.exceptions.AccountLockedServiceLayerException;
import com.oup.eac.service.exceptions.CustomerNotFoundServiceLayerException;
import com.oup.eac.service.exceptions.PasswordPolicyViolatedServiceLayerException;
import com.oup.eac.service.exceptions.PasswordSameAsOldServiceLayerException;
import com.oup.eac.service.exceptions.SessionConcurrencyServiceLayerException;
import com.oup.eac.service.exceptions.SessionNotFoundServiceLayerException;
import com.oup.eac.service.exceptions.UserHasNoEmailServiceLayerException;
import com.oup.eac.ws.v2.binding.access.AuthenticateResponse;
import com.oup.eac.ws.v2.binding.access.AuthenticateResponseSequence;
import com.oup.eac.ws.v2.binding.access.ChangePasswordResponse;
import com.oup.eac.ws.v2.binding.access.LogoutResponse;
import com.oup.eac.ws.v2.binding.access.ResetPasswordResponse;
import com.oup.eac.ws.v2.binding.common.ErrorStatus;
import com.oup.eac.ws.v2.binding.common.RegistrationInformation;
import com.oup.eac.ws.v2.binding.common.User;
import com.oup.eac.ws.v2.binding.common.WsUserId;
import com.oup.eac.ws.v2.binding.common.types.StatusCode;
import com.oup.eac.ws.v2.binding.userdata.RegistrationInformationResponse;
import com.oup.eac.ws.v2.binding.userdata.RegistrationInformationResponseSequence;
import com.oup.eac.ws.v2.binding.userdata.UserNameResponse;
import com.oup.eac.ws.v2.ex.WebServiceValidationException;
import com.oup.eac.ws.v2.service.UserServiceAdapter;
import com.oup.eac.ws.v2.service.WsCustomerLookup;
import com.oup.eac.ws.v2.service.entitlements.CustomerConverter;
import com.oup.eac.ws.v2.service.utils.ErrorStatusUtils;

/**
 * User services adapter that provides caching.
 * 
 */
@Component("userServiceAdapterV2")
@Primary
public class CachingUserServiceAdapter extends BaseUserAdapter implements UserServiceAdapter {

    private static final Logger LOG = Logger.getLogger(CachingUserServiceAdapter.class);
    
    private final ExternalIdService externalIdService;
    
    private final CustomerConverter customerConverter;

    private final WsCustomerLookup wsCustomerLookup;
    
    /**
     * Constructor.
     * 
     * @param customerService
     *            customerService
     */
    @Autowired
    public CachingUserServiceAdapter(final CustomerService customerService, final CustomerConverter customerConverter,
            final WsCustomerLookup wsCustomerLookup, final ExternalIdService externalIdService, MessageSource messageSource, UsernameValidator usernameValidator) {
        super(messageSource,customerService, usernameValidator);
        Assert.notNull(customerConverter);
        Assert.notNull(wsCustomerLookup);
        Assert.notNull(externalIdService);
        this.customerConverter = customerConverter;
        this.wsCustomerLookup = wsCustomerLookup;
        this.externalIdService = externalIdService;
    }

    /**
     * {@inheritDoc}
     */
    @PreAuthorize("hasRole('ROLE_WS_USER_NAME')")
    @Override
    public final UserNameResponse getUserName(final WsUserId wsUserId) {
        UserNameResponse response = new UserNameResponse();        
        try{            
            Customer customer = wsCustomerLookup.getCustomerByWsUserId(wsUserId);
            response.setUserName(customer.getFullName());
        }catch(WebServiceValidationException wsve){
            ErrorStatus errorStatus = ErrorStatusUtils.getClientErrorStatus(wsve.getMessage());
            response.setErrorStatus(errorStatus);
        }
        return response;
    }

    /**
     * {@inheritDoc}
     */
    @PreAuthorize("hasRole('ROLE_WS_REGISTRATION_INFORMATION')")
    @Override
    public RegistrationInformationResponse getRegistrationInformation(final String systemId, WsUserId wsUserId) throws ServiceLayerException {
        
        final RegistrationInformationResponse response = new RegistrationInformationResponse();
        
        try{
            final Customer basicCustomer = wsCustomerLookup.getCustomerByWsUserId(wsUserId);
            final Set<Answer> answers = this.wsCustomerLookup.getCustomerWithAnswers(basicCustomer.getId());
       
            final ExternalCustomerIdDto extCustIdsDto = this.externalIdService.getExternalCustomerIds(basicCustomer, systemId);
            final User user = customerConverter.convertCustomerToUser(extCustIdsDto);
            final RegistrationInformationResponseSequence seq = new RegistrationInformationResponseSequence();
            seq.setUser(user);
            //final Set<Answer> answers = customerWithAnswers.getAnswers();
            for (Answer answer : answers) {
                //TODO This must be fixed once a final decision has been made
                //     about the export name.
            	String exportName = answer.getQuestion().getDescription();
                if (StringUtils.isNotBlank(exportName)) {
                     String answerText = answer.getAnswerText();
                     if(StringUtils.isNotBlank(answerText)){
                          RegistrationInformation registrationItem = new RegistrationInformation();
                          registrationItem.setRegistrationKey(exportName);
                          registrationItem.setRegistrationValue(answerText);
                          seq.addRegistrationInformation(registrationItem);
                     } else {
                         if(LOG.isDebugEnabled()){
                              String msg = String.format("Not adding entry for [%s] : it has no answer", answerText); 
                              LOG.debug(msg);
                         }
                     }
                } else {
                    LOG.warn("No export name defined for field id:" + answer.getQuestion().getId());
                }                
            }
            response.setRegistrationInformationResponseSequence(seq);
        }catch(WebServiceValidationException wsve){
            ErrorStatus errorStatus = ErrorStatusUtils.getClientErrorStatus(wsve.getMessage());
            response.setErrorStatus(errorStatus);
        }
        return response;
    }

    /**
     * {@inheritDoc}
     */
	@Override
	@PreAuthorize("hasRole('ROLE_WS_AUTHENTICATE')")
	public final AuthenticateResponse authenticate(final String username, final String password) {
		try {
			String uname = username == null ? "" : username.toLowerCase();
			validateAuthenticate(uname, password);
			LOG.debug("start time :"+ System.currentTimeMillis());
			CustomerSessionDto dto = customerService.getCustomerByUsernameAndPassword(uname, password);
			Customer customer = dto.getCustomer();
			String systemId = null;
			ExternalCustomerIdDto externalCustomerIds = externalIdService.getExternalCustomerIds(customer,systemId);
			LOG.debug("end time :"+ System.currentTimeMillis());
			return getAuthenticateResponse(dto, externalCustomerIds);
		} catch (IllegalArgumentException e) {
			LOG.debug("Validation error", e);
			return getAuthenticateResponse(StatusCode.CLIENT_ERROR, e.getMessage());
		} catch (WebServiceValidationException e) {
            LOG.debug("Web Service Validation error", e);
            return getAuthenticateResponse(StatusCode.CLIENT_ERROR, e.getMessage());
		} catch (CustomerNotFoundServiceLayerException e) {
			LOG.debug("The customer was not found", e);
			return getAuthenticateResponse(StatusCode.CLIENT_ERROR, e.getMessage());
		} catch (AccountLockedServiceLayerException e) {
			LOG.debug("The users account is locked", e);
			return getAuthenticateResponse(StatusCode.CLIENT_ERROR, e.getMessage());
		} catch (SessionConcurrencyServiceLayerException e) {
			LOG.debug("Session concurrency has been exceeded", e);
			return getAuthenticateResponse(StatusCode.CLIENT_ERROR, e.getMessage());
		} catch (InvalidCredentialsServiceLayerException e) {
			LOG.debug("Invalid credentials supplied", e);
			//return getAuthenticateResponse(StatusCode.CLIENT_ERROR, e.getMessage());
			if(e.getCause().getMessage().equals("Internal Server Error Authentication failed because no auth profile matched ")){
				return getAuthenticateResponse(StatusCode.CLIENT_ERROR, ERR_CUSTOMER_NOT_FOUND_FOR_USERNAME + username);
			}
			return getAuthenticateResponse(StatusCode.CLIENT_ERROR, e.getMessage());
		} catch (ServiceLayerException e) {
			if(e.getMessage().equals("Internal Error: Authentication failed because no auth profile matched")){
			return getAuthenticateResponse(StatusCode.CLIENT_ERROR, ERR_CUSTOMER_NOT_FOUND_FOR_USERNAME + username);	
			}
			LOG.error("An erights exception has occurred", e);
			return getAuthenticateResponse(StatusCode.SERVER_ERROR, e.getMessage());
		}
	}
	
    /**
     * {@inheritDoc}
     */
	@Override
	@PreAuthorize("hasRole('ROLE_WS_LOGOUT')")
	public final LogoutResponse logout(final String sessionToken) {
		long startTime = System.currentTimeMillis();
		try {
			validateLogout(sessionToken);
			customerService.logout(sessionToken);
			AuditLogger.logEvent(":: Time to LogoutRequest :: " + (System.currentTimeMillis() - startTime));
			return getLogoutResponse(null);
		} catch (IllegalArgumentException e) {
			LOG.debug(ERR_SESSION_CANNOT_BE_BLANK);
			return getLogoutResponse(StatusCode.CLIENT_ERROR, e.getMessage());
		} catch (SessionNotFoundServiceLayerException e) {
			//Ignore errors such as the session has expired or is not known
			LOG.debug("Ignoring errors such as the session has expired or is not known. Returning success");
			return getLogoutResponse(null);
		} catch (ServiceLayerException e) {
			LOG.error("An erights exception has occurred", e);
			return getLogoutResponse(StatusCode.SERVER_ERROR, e.getMessage());
		}
	}
	
    /**
     * {@inheritDoc}
     * @throws PasswordAlreadyExistsException 
     */
	@Override
	@PreAuthorize("hasRole('ROLE_WS_CHANGE_PASSWORD')")
	public final ChangePasswordResponse changePassword(final WsUserId wsUserId, final String newPassword) {	
		long startTime = System.currentTimeMillis();
	    Customer customer;
	    try{
	        try{
	            customer = validateChangePassword(wsUserId, newPassword);
	        }catch(WebServiceValidationException wsve){
	            return getChangePasswordResponse(StatusCode.CLIENT_ERROR,wsve.getMessage());
	        }
	        String username = customer.getUsername();
			customerService.saveChangeCustomerPassword(new ChangePasswordDto(username, newPassword, newPassword), customer);
			AuditLogger.logEvent(":: Time to ChangePasswordRequest :: " + (System.currentTimeMillis() - startTime));
			return getChangePasswordResponse(null);
		} catch (PasswordPolicyViolatedServiceLayerException e) {
			LOG.debug("Password Policy Violated", e);
			return getChangePasswordResponse(StatusCode.CLIENT_ERROR, e.getMessage());
		} catch (IllegalArgumentException e) {
			LOG.debug("Validation error", e);
			return getChangePasswordResponse(StatusCode.CLIENT_ERROR, e.getMessage());
		}  catch (PasswordSameAsOldServiceLayerException e) {
			LOG.debug("The new password is the same as the old", e);
			return getChangePasswordResponse(StatusCode.CLIENT_ERROR, ERR_OLD_AND_NEW_PASSWORDS_MATCH);
		} catch (ServiceLayerException e) {
			LOG.error("Erights error", e);
			return getChangePasswordResponse(StatusCode.SERVER_ERROR, e.getMessage());
		}
	}
	
    /**
     * {@inheritDoc}
     * @throws PasswordAlreadyExistsException 
     */
	@Override
	@PreAuthorize("hasRole('ROLE_WS_RESET_PASSWORD')")
	public final ResetPasswordResponse resetPassword(final WsUserId wsUserId, final String wsUsername) {
		long startTime = System.currentTimeMillis();
	    Customer customer = null;
		try {
			customer = validateResetPassword(wsUserId);
		} catch (ServiceLayerException sle) {
            return getResetPasswordResponse(StatusCode.SERVER_ERROR, sle.getMessage());
        } catch (WebServiceValidationException wsve) {
            return getResetPasswordResponse(StatusCode.CLIENT_ERROR, wsve.getMessage());
		}
		
		String username = customer.getUsername();
		String baseurl=EACSettings.getProperty(EACSettings.EAC_RESETPASSWORD_WS_BASEURL);
		try{
			//Added wsUsername as part of OLB requirement, need to revert after Live
			customerService.updateResetCustomerPassword(customer, Locale.getDefault(), baseurl, wsUsername);
			AuditLogger.logEvent(":: Time to ResetPasswordRequest :: " + (System.currentTimeMillis() - startTime));
			return getResetPasswordResponse(null);
		} catch (PasswordPolicyViolatedServiceLayerException e) {
			LOG.debug("Password Policy Violated", e);
			return getResetPasswordResponse(StatusCode.CLIENT_ERROR, "There was a problem changing your password. Please contact the system adminsitrator.");
		} catch (UserHasNoEmailServiceLayerException e) {
			LOG.debug("User: " + username + " does not have an email", e);
			return getResetPasswordResponse(StatusCode.CLIENT_ERROR, e.getMessage());
		}  catch (ServiceLayerException e) {
			LOG.error("Erights error", e);
			return getResetPasswordResponse(StatusCode.SERVER_ERROR, e.getMessage());
		}
	}
	
    private final Customer validateResetPassword(final WsUserId wsUserId) throws WebServiceValidationException, ServiceLayerException {
        //validateUsername(username);
        return checkCustomerExists(wsUserId);
    }
    
    /**
     * Checks that the customer exists.
     * @param wsUserId - a wrapper for several types of user identifier.
     * @return the customer associated with the wsUserId
     * @throws WebServiceValidationException if the customer does not exist.
     * @throws ServiceLayerException if there is some other problem.
     */
	private Customer checkCustomerExists(WsUserId wsUserId) throws WebServiceValidationException {
        return this.wsCustomerLookup.getCustomerByWsUserId(wsUserId);
    }

    private final Customer validateChangePassword(final WsUserId wsUserId, final String newPassword) throws WebServiceValidationException, ServiceLayerException {
		validatePassword(newPassword);
		
		Customer customer = checkCustomerExists(wsUserId);
		return customer;
	}

    private Customer checkUsernameExists(final String username) throws WebServiceValidationException {
        WsUserId wsUserId = new WsUserId();
        wsUserId.setUserName(username);
        Customer user = this.wsCustomerLookup.getCustomerByWsUserId(wsUserId);
        return user;
    }
    
	private void validateAuthenticate(final String username, final String password) throws WebServiceValidationException {
		validateNonBlankUsername(username);
		if(StringUtils.isBlank(password)) {
			throw new IllegalArgumentException(ERR_PASSWORD_CANNOT_BE_BLANK);
		}
		//Customer customer = checkUsernameExists(username);
		//return customer;
	}
	
	private final void validateLogout(final String sessionToken) {
		if(StringUtils.isBlank(sessionToken)) {
			throw new IllegalArgumentException(ERR_SESSION_CANNOT_BE_BLANK);
		}
	}
	
	private final ResetPasswordResponse getResetPasswordResponse(final StatusCode statusCode) {
		return getResetPasswordResponse(statusCode, null);
	}	
	
	private final ResetPasswordResponse getResetPasswordResponse(final StatusCode statusCode, final String statusReason) {
		ResetPasswordResponse response = new ResetPasswordResponse();
		if(statusCode != null){
		    response.setErrorStatus(getErrorStatus(statusCode, statusReason));
		}
		return response;
	}	
	
	private final ChangePasswordResponse getChangePasswordResponse(final StatusCode statusCode) {
		return getChangePasswordResponse(statusCode, null);
	}
	
	private final ChangePasswordResponse getChangePasswordResponse(final StatusCode statusCode, final String statusReason) {
		ChangePasswordResponse response = new ChangePasswordResponse();
		if(statusCode != null){
		    response.setErrorStatus(getErrorStatus(statusCode, statusReason));
		}
		return response;
	}	
	
	private final LogoutResponse getLogoutResponse(final StatusCode statusCode) {	    
		return getLogoutResponse(statusCode, null);
	}
	
	private final LogoutResponse getLogoutResponse(final StatusCode statusCode, final String statusReason) {
		LogoutResponse response = new LogoutResponse();
		if(statusCode != null){
		    response.setErrorStatus(getErrorStatus(statusCode, statusReason));
		}
		return response;
	}
	
	private final ErrorStatus getErrorStatus(final StatusCode statusCode, final String statusReason) {
		return ErrorStatusUtils.getErrorStatus(statusCode, statusReason);
	}
	
	private final AuthenticateResponse getAuthenticateResponse(final StatusCode statusCode, final String statusReason) {
		AuthenticateResponse response = new AuthenticateResponse();
		response.setErrorStatus(getErrorStatus(statusCode, statusReason));		
		return response;
	}	
	
	private final AuthenticateResponse getAuthenticateResponse(final CustomerSessionDto customerDto, ExternalCustomerIdDto extCustIdsDto) {
		AuthenticateResponse response = new AuthenticateResponse();
		AuthenticateResponseSequence seq = new AuthenticateResponseSequence();
        response.setAuthenticateResponseSequence(seq);
		seq.setSessionToken(customerDto.getSession());
		seq.setUser(customerConverter.convertCustomerToUser(extCustIdsDto));
		LOG.debug("end time 2 : "+ System.currentTimeMillis());
		return response;
	}
}
