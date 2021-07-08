package com.oup.eac.ws.v2.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.ArrayUtils;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.springframework.context.MessageSource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.Assert;

import com.oup.eac.common.utils.username.UsernameValidator;
import com.oup.eac.domain.Customer;
import com.oup.eac.dto.ExternalIdDto;
import com.oup.eac.integration.facade.exceptions.ErightsException;
import com.oup.eac.service.CustomerService;
import com.oup.eac.service.ExternalIdService;
import com.oup.eac.service.ServiceLayerException;
import com.oup.eac.service.exceptions.ServiceLayerValidationException;
import com.oup.eac.service.exceptions.UsernameExistsException;
import com.oup.eac.ws.v2.binding.access.CreateUserAccountResponse;
import com.oup.eac.ws.v2.binding.access.UserStatusType;
import com.oup.eac.ws.v2.binding.common.CreateUser;
import com.oup.eac.ws.v2.binding.common.Credential;
import com.oup.eac.ws.v2.binding.common.ErrorStatus;
import com.oup.eac.ws.v2.binding.common.ExternalIdentifier;
import com.oup.eac.ws.v2.binding.common.InternalIdentifier;
import com.oup.eac.ws.v2.binding.common.PasswordCredential;
import com.oup.eac.ws.v2.ex.WebServiceValidationException;
import com.oup.eac.ws.v2.service.CreateUserAccountAdapter;
import com.oup.eac.ws.v2.service.entitlements.CustomerConverter;
import com.oup.eac.ws.v2.service.utils.ErrorStatusUtils;
import com.oup.eac.ws.v2.service.utils.IdUtils;

/**
 * The Class CreateUserAccountAdapterImpl.
 */
public class CreateUserAccountAdapterImpl extends BaseUserAdapter implements CreateUserAccountAdapter {

    private static final Logger LOG = Logger.getLogger(CreateUserAccountAdapterImpl.class);

    private final CustomerConverter customerConverter;
    private final ExternalIdService externalIdService;

    public CreateUserAccountAdapterImpl(MessageSource messageSource, CustomerService customerService, CustomerConverter customerConverter,
            ExternalIdService externalIdService, UsernameValidator usernameValidator) {
        super(messageSource,customerService, usernameValidator);
        Assert.notNull(customerConverter);
        this.customerConverter = customerConverter;
        this.externalIdService = externalIdService;
    }

    @Override
    @PreAuthorize("hasRole('ROLE_WS_CREATE_USER_ACCOUNT')")
    public CreateUserAccountResponse createUserAccounts(CreateUser[] users) {

        CreateUserAccountResponse resp = new CreateUserAccountResponse();
        UserStatusType[] status = getResponseData(users);
        resp.setUserStatus(status);
        return resp;
    }

    private UserStatusType[] getResponseData(final CreateUser[] users) {
        if (users == null) {
            return new UserStatusType[0];
        }
        UserStatusType[] result = new UserStatusType[users.length];
        for (int i = 0; i < users.length; i++) {
            UserStatusType us = new UserStatusType();
            result[i] = us;
            CreateUser user = users[i];
            
            try {
                String id = createCustomer(user);
                
                InternalIdentifier userId = new InternalIdentifier(); 
                userId.setId(id); 
                us.setUserId(userId);
                
            } catch (WebServiceValidationException wsve) {                
            	wsve.printStackTrace();
                ErrorStatus errorStatus = ErrorStatusUtils.getClientErrorStatus(wsve.getMessage());
                us.setErrorStatus(errorStatus);
            } catch (ServiceLayerValidationException slve) {                
                ErrorStatus errorStatus = ErrorStatusUtils.getClientErrorStatus(slve.getMessage());
                us.setErrorStatus(errorStatus);
            } catch (ServiceLayerException sle) {   
            	if (sle.getCause() == null) {
            		ErrorStatus errorStatus = ErrorStatusUtils.getServerErrorStatus(sle.getMessage());
	                us.setErrorStatus(errorStatus);
            	}else if(((ErightsException)sle.getCause()).getErrorCode() == 2039){
            		
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
            } catch(ErightsException e) {
            	String msg = e.getMessage();
	        	 ErrorStatus errorStatus = ErrorStatusUtils.getServerErrorStatus(e.getMessage());
	        	 if(e.getErrorCode() != null){
		        	 if(e.getErrorCode()==1096){
		        		 msg = e.getMessage().replace("externalUserId", "externalCustomerId").replace("user", "another customer");
		        		 errorStatus = ErrorStatusUtils.getClientErrorStatus(msg);
		        	 }
		        	 if( e.getErrorCode() == 1044){
		        		 String error = e.getMessage().substring(e.getMessage().lastIndexOf(".") + 1);
		        		 errorStatus = ErrorStatusUtils.getClientErrorStatus(error);
		        	 }
	        	 }
	          	
                 us.setErrorStatus(errorStatus);
            } catch (Exception ex) {
                logUserDataMessage("unexpected exception attempting to create user ", Level.ERROR, user, ex);
                ErrorStatus errorStatus = ErrorStatusUtils.getServerErrorStatus(ex.getMessage());
                us.setErrorStatus(errorStatus);
            } 
            Assert.isTrue(us.getChoiceValue() != null);
        }
        return result;
    }

    private void logUserDataMessage(String prefix, Level level, CreateUser user, Exception ex) {        
        String msg = String.format("%s : username[%s] email[%s]",prefix,user.getCredentials().getPasswordCredential().getUserName(),user.getEmailAddress());
        LOG.log(level, msg, ex);
    }
    
    //Added Erights exception since it calls atypon services
    private String createCustomer(CreateUser user) throws ServiceLayerException, WebServiceValidationException, ErightsException {
        LOG.info("createCustomer start");
    	validateCreateUser(user);
    	LOG.info("user validated");
        Customer newCustomer = customerConverter.convertCreateUserToCustomer(user);
        ExternalIdentifier[] externals = user.getExternal();
        try {
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
        LOG.info("User created.");
        String id = newCustomer.getId();
        
        if(LOG.isDebugEnabled()){
            String msg = String.format("Created User : username[%s] email[%s] id[%s]", newCustomer.getUsername(), newCustomer.getEmailAddress(), id);
            LOG.debug(msg);
        }
        
        return id;
    }

    private void createCustomerWithExternalIds(Customer newCustomer, ExternalIdentifier[] externals) throws WebServiceValidationException,
            ServiceLayerException, ErightsException {
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

    public void validateCreateUser(CreateUser user) throws WebServiceValidationException, ErightsException, ServiceLayerException{

        Credential cred = user.getCredentials();
        validateCredentials(cred);
        PasswordCredential pwd = cred.getPasswordCredential();
        Assert.isTrue(pwd != null);
        String username = pwd.getUserName();
        String password = pwd.getPassword();
        
        validateNewUsername(username);
        validatePassword(password);
        validateEmail(user.getEmailAddress());
        validateLocale(user.getLocale());
        user.setTimeZone(validateTimeZone(user.getTimeZone()));
        List<String> externalIds = new ArrayList<String>() ;
       	for (ExternalIdentifier externalIdentifier : user.getExternal()) {
       		externalIds.add(externalIdentifier.getId()) ;
       	}
       	if (externalIds.size() > 0) {
       		validateExternalCustomerIds(externalIds);
       	}
        if((user.getFirstName()!=null&& !user.getFirstName().isEmpty()) || (user.getLastName() != null && !user.getLastName().isEmpty())) 
        	validateFirstAndLastName(user.getFirstName(),user.getLastName());
    }

	

}
