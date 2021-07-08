package com.oup.eac.ws.v2.service.impl;

import java.util.List;

import org.apache.commons.lang.ArrayUtils;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.springframework.context.MessageSource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.Assert;

import com.oup.eac.common.utils.username.UsernameValidator;
import com.oup.eac.domain.Customer;
import com.oup.eac.domain.Customer.CustomerType;
import com.oup.eac.dto.ExternalIdDto;
import com.oup.eac.integration.facade.exceptions.ErightsException;
import com.oup.eac.service.CustomerService;
import com.oup.eac.service.ExternalIdService;
import com.oup.eac.service.ServiceLayerException;
import com.oup.eac.service.exceptions.ServiceLayerValidationException;
import com.oup.eac.service.exceptions.UsernameExistsException;
import com.oup.eac.ws.v2.binding.access.CreateUserAccountWithConcurrencyResponse;
import com.oup.eac.ws.v2.binding.access.UserStatusType;
import com.oup.eac.ws.v2.binding.common.CreateUserWithConcurrency;
import com.oup.eac.ws.v2.binding.common.Credential;
import com.oup.eac.ws.v2.binding.common.ErrorStatus;
import com.oup.eac.ws.v2.binding.common.ExternalIdentifier;
import com.oup.eac.ws.v2.binding.common.InternalIdentifier;
import com.oup.eac.ws.v2.binding.common.PasswordCredential;
import com.oup.eac.ws.v2.ex.WebServiceValidationException;
import com.oup.eac.ws.v2.service.CreateUserAccountWithConcurrencyAdapter;
import com.oup.eac.ws.v2.service.entitlements.CustomerConverter;
import com.oup.eac.ws.v2.service.utils.ErrorStatusUtils;
import com.oup.eac.ws.v2.service.utils.IdUtils;

/**
 * The Class CreateUserAccountWithConcurrencyAdapterImpl.
 */

public class CreateUserAccountWithConcurrencyAdapterImpl extends BaseUserAdapter implements CreateUserAccountWithConcurrencyAdapter{
    
    private static final Logger LOG = Logger.getLogger(CreateUserAccountWithConcurrencyAdapterImpl.class);

    private final CustomerConverter customerConverter;
    private final ExternalIdService externalIdService;

    public CreateUserAccountWithConcurrencyAdapterImpl(MessageSource messageSource, CustomerService customerService, CustomerConverter customerConverter,
            ExternalIdService externalIdService, UsernameValidator usernameValidator) {
        super(messageSource,customerService, usernameValidator);
        Assert.notNull(customerConverter);
        this.customerConverter = customerConverter;
        this.externalIdService = externalIdService;
    }

    @Override
    @PreAuthorize("hasRole('ROLE_WS_CREATE_USER_WITH_CONCURRENCY')")
    public CreateUserAccountWithConcurrencyResponse createUserAccountsWithConcurrency(CreateUserWithConcurrency[] users) {
        
        CreateUserAccountWithConcurrencyResponse resp =new CreateUserAccountWithConcurrencyResponse();
                
        UserStatusType[] status = getResponseData(users);
        resp.setUserStatus(status);
        return resp;
    }

    private UserStatusType[] getResponseData(final CreateUserWithConcurrency[] users) {
        if (users == null) {
            return new UserStatusType[0];
        }
        UserStatusType[] result = new UserStatusType[users.length];
       
        for (int i = 0; i < users.length; i++) {
            
            UserStatusType us = new UserStatusType();
            result[i] = us;
            CreateUserWithConcurrency user = users[i];
            
            try {
                String id = createCustomer(user);
                
                InternalIdentifier userId = new InternalIdentifier(); 
                userId.setId(id); 
                us.setUserId(userId);
                
            } catch (WebServiceValidationException wsve) {                
                ErrorStatus errorStatus = ErrorStatusUtils.getClientErrorStatus(wsve.getMessage());
                us.setErrorStatus(errorStatus);
            } catch (ServiceLayerValidationException slve) {                
                ErrorStatus errorStatus = ErrorStatusUtils.getClientErrorStatus(slve.getMessage());
                us.setErrorStatus(errorStatus);
            } catch (ServiceLayerException sle) { 
            	if(sle.getCause()!=null){
            		if(((ErightsException)sle.getCause()).getErrorCode() == 2039){

            			ErrorStatus errorStatus = ErrorStatusUtils.getClientErrorStatus(ERR_USERNAME_ALREADY_TAKEN);
            			us.setErrorStatus(errorStatus);
            		} else if(((ErightsException)sle.getCause()).getErrorCode() == 2019 ){
            			String error = ((ErightsException)sle.getCause()).getMessage().toString() ;
            			String systemId = error.substring(error.indexOf(":")+1, error.indexOf("does not exists")) ;
            			ErrorStatus errorStatus = ErrorStatusUtils.getClientErrorStatus("No system id found for " + systemId);
            			us.setErrorStatus(errorStatus);
            		} else if(((ErightsException)sle.getCause()).getErrorCode() == 2017 ){
            			String error = ((ErightsException)sle.getCause()).getMessage().toString() ;
            			String typeId = error.substring(error.indexOf("System Type Id ")+1, error.indexOf("does not exists")) ;
            			ErrorStatus errorStatus = ErrorStatusUtils.getClientErrorStatus("No system id found for " + typeId);
            			us.setErrorStatus(errorStatus);
            		} else {
                		logUserDataMessage("unexpected service layer exception attempting to create user ", Level.WARN, user, sle);
                		ErrorStatus errorStatus = ErrorStatusUtils.getServerErrorStatus(sle.getMessage());
                		us.setErrorStatus(errorStatus);
                	}
            	} else {
            		//logUserDataMessage("unexpected service layer exception attempting to create user ", Level.WARN, user, sle);
            		ErrorStatus errorStatus = ErrorStatusUtils.getServerErrorStatus(sle.getMessage());
            		us.setErrorStatus(errorStatus);
            	}
            } catch (Exception ex) {
                logUserDataMessage("unexpected exception attempting to create user ", Level.ERROR, user, ex);
                ErrorStatus errorStatus = ErrorStatusUtils.getServerErrorStatus(ex.getMessage());
                us.setErrorStatus(errorStatus);
            } 
            Assert.isTrue(us.getChoiceValue() != null);
        }
        return result;
    }

    private void logUserDataMessage(String prefix, Level level, CreateUserWithConcurrency user, Exception ex) {        
        String msg = String.format("%s : username[%s] email[%s]",prefix,user.getCredentials().getPasswordCredential().getUserName(),user.getEmailAddress());
        LOG.log(level, msg, ex);
    }
        
  //Added Erights exception since it calls atypon services
    private String createCustomer(CreateUserWithConcurrency user) throws ServiceLayerException, WebServiceValidationException, ErightsException {
        validateCreateUser(user);
        
        Customer newCustomer = customerConverter.convertCreateUserWithConcurrencyToCustomer(user);
        
        //set user concurrency
        int userCC;
        CustomerType ct = CustomerType.SPECIFIC_CONCURRENCY;
        userCC = user.getUserConcurrency();
        if(userCC == 0){
            userCC = CustomerType.SELF_SERVICE.getConcurrency();
        }
        if(userCC < CustomerType.SELF_SERVICE.getConcurrency()){
        	 error(ERR_USER_CONCURRENCY_LESS_THAN_DEFAULT); 
        }
        ct.setConcurrency(userCC);
        newCustomer.setCustomerType(ct);
      
        ExternalIdentifier[] externals = user.getExternal();
        try{
	        if (!ArrayUtils.isEmpty(externals)) {
	            createCustomerWithExternalIds(newCustomer, externals);
	        } else {
	            customerService.saveCustomer(newCustomer, false);
	        }
	    } catch (ServiceLayerException e) {
	    	LOG.info("ServiceLayerException : "+e);
			if(e instanceof UsernameExistsException){
				throw new WebServiceValidationException(ERR_USERNAME_ALREADY_TAKEN);
			}
			else{
				throw e;
			}
		}
        String id = newCustomer.getId();
        
        if(LOG.isDebugEnabled()){
            String msg = String.format("Created User : username[%s] email[%s] id[%s]", newCustomer.getUsername(), newCustomer.getEmailAddress(), id);
            LOG.debug(msg);
        }
        
        return id;
    }

    private void createCustomerWithExternalIds(Customer newCustomer, ExternalIdentifier[] externals) throws WebServiceValidationException, ServiceLayerException, ErightsException {
        String systemId = getSystemId(externals);
        IdUtils.validateSetExternalIds(systemId, externals);
        List<ExternalIdDto> externalIdDtos = IdUtils.getExternalIdDtos(externals);
        externalIdService.saveExternalCustomerIdsForSystemCreatingCustomer(newCustomer, systemId, externalIdDtos);
    }

    private String getSystemId(ExternalIdentifier[] externals) {
        String systemId = "";
        for (ExternalIdentifier external : externals) {
            systemId = external.getSystemId();
            break;
        }
        return systemId;
    }

    public void validateCreateUser(CreateUserWithConcurrency user) throws WebServiceValidationException, ErightsException, ServiceLayerException {

        Credential cred = user.getCredentials();
        validateCredentials(cred);
        PasswordCredential pwd = cred.getPasswordCredential();
        Assert.isTrue(pwd != null);
        String username = pwd.getUserName();
        String password = pwd.getPassword();
        
        validateNewUsername(username);//checks that it's a valid username and hasn't been used already
        validatePassword(password);
        validateEmail(user.getEmailAddress());
        validateLocale(user.getLocale());
        user.setTimeZone(validateTimeZone(user.getTimeZone()));
        validateUserConcurrency(user.getUserConcurrency());
        if((user.getFirstName() != null && !user.getFirstName().isEmpty()) || (user.getLastName() != null && !user.getLastName().isEmpty())) {
        	validateFirstAndLastName(user.getFirstName(), user.getLastName());
        }
        	
    }
        
    private void validateUserConcurrency(int userConcurrency) throws WebServiceValidationException {
        if(userConcurrency < 0){
            error(ERR_USER_CONCURRENCY_NEGATIVE + userConcurrency);            
        }

    } 

}