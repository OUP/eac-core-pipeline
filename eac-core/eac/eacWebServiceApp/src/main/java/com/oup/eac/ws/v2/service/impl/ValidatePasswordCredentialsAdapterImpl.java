package com.oup.eac.ws.v2.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.MessageSource;
import org.springframework.security.access.prepost.PreAuthorize;

import com.oup.eac.common.utils.username.UsernameValidator;
import com.oup.eac.integration.facade.exceptions.ErightsException;
import com.oup.eac.integration.facade.exceptions.UserNotFoundException;
import com.oup.eac.service.CustomerService;
import com.oup.eac.ws.v2.binding.access.ValidatePasswordCredentialsRequest;
import com.oup.eac.ws.v2.binding.access.ValidatePasswordCredentialsResponse;
import com.oup.eac.ws.v2.binding.access.types.CredentialStatusCode;
import com.oup.eac.ws.v2.binding.common.PasswordCredential;
import com.oup.eac.ws.v2.ex.WebServiceValidationException;
import com.oup.eac.ws.v2.service.ValidatePasswordCredentialsAdapter;

/**
 * The Class ValidatePasswordCredentialsAdapterImpl.
 */
public class ValidatePasswordCredentialsAdapterImpl extends BaseUserAdapter implements ValidatePasswordCredentialsAdapter {

    /**
     * Instantiates a new validate password credentials adapter impl.
     *
     * @param messageSource the message source
     * @param customerService the customer service
     */
    public ValidatePasswordCredentialsAdapterImpl(MessageSource messageSource, CustomerService customerService, UsernameValidator usernameValidator) {
        super(messageSource, customerService, usernameValidator);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @PreAuthorize("hasRole('ROLE_WS_VALIDATE_PASSWORD_CREDENTIALS')")
    public ValidatePasswordCredentialsResponse validate(ValidatePasswordCredentialsRequest request) {
        PasswordCredential cred = request.getCredentials();
        String candidateUsername = cred.getUserName();
        String candidatePassword = cred.getPassword();

        List<String> errors = new ArrayList<String>();
        
        boolean usernameError = validateCandidateUsername(errors, candidateUsername);
        
        boolean passwordError = validateCandidatePassword(errors, candidatePassword);

        CredentialStatusCode statusCode = getStatusCode(usernameError, passwordError);
        
        String[] reasons = getErrorReasons(errors);
        
        ValidatePasswordCredentialsResponse response = new ValidatePasswordCredentialsResponse();
        response.setStatus(statusCode);
        response.setStatusReason(reasons);
        return response;
    }

    
    /**
     * Gets the error reasons.
     *
     * @param errors the errors
     * @return the error reasons
     */
    private String[] getErrorReasons(final List<String> errors) {
        String[] errorReasons = new String[errors.size()];
        errors.toArray(errorReasons);
        return errorReasons;
    }

    /**
     * Validate candidate password.
     *
     * @param errors the errors
     * @param candidatePassword the candidate password
     * @return true, if successful
     */
    private boolean validateCandidatePassword(final List<String> errors, final String candidatePassword) {
        boolean passwordError = false;
        try{
            validatePassword(candidatePassword);
        }catch(WebServiceValidationException wsve){
            passwordError = true;
            errors.add(wsve.getMessage());
        }
        return passwordError;
    }

    
    /**
     * Validate candidate username.
     *
     * @param errors the errors
     * @param candidateUsername the candidate username
     * @return true, if successful
     */
    private boolean validateCandidateUsername(final List<String> errors, final String candidateUsername){
        boolean usernameError = false;
        try {
            validateNewUsername(candidateUsername);//checks that it's a valid user and hasn't been used already
        } catch (WebServiceValidationException wsve) {
            usernameError = true;
            errors.add(wsve.getMessage());
        } catch (UserNotFoundException e) {
        	 usernameError = true;
             errors.add(e.getMessage());
			e.printStackTrace();
		} catch (ErightsException e) {
			 usernameError = true;
	           errors.add(e.getMessage());
			e.printStackTrace();
		}
        return usernameError;
    }

    
    /**
     * Gets the status code.
     *
     * @param usernameError the username error
     * @param passwordError the password error
     * @return the status code
     */
    private CredentialStatusCode getStatusCode(final boolean usernameError, final boolean passwordError){
        final CredentialStatusCode status;

        if (usernameError && passwordError) {
            status = CredentialStatusCode.INVALID_USERNAME_PASSWORD;
        } else {
            if (usernameError) {
                status = CredentialStatusCode.INVALID_USERNAME;
            } else if (passwordError) {
                status = CredentialStatusCode.INVALID_PASSWORD;
            } else {
                status = CredentialStatusCode.VALID;
            }
        }
        return status;
    }
    
}
