package com.oup.eac.ws.v2.service.impl;

import java.util.Locale;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.context.MessageSource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.Assert;

import com.oup.eac.common.utils.username.UsernameValidator;
import com.oup.eac.domain.Customer;
import com.oup.eac.domain.Customer.CustomerType;
import com.oup.eac.domain.Password;
import com.oup.eac.integration.facade.exceptions.ErightsException;
import com.oup.eac.service.CustomerService;
import com.oup.eac.service.ServiceLayerException;
import com.oup.eac.service.exceptions.PasswordPolicyViolatedServiceLayerException;
import com.oup.eac.service.exceptions.ServiceLayerValidationException;
import com.oup.eac.service.exceptions.UsernameExistsException;
import com.oup.eac.ws.v2.binding.access.UpdateUserAccountWithConcurrencyRequest;
import com.oup.eac.ws.v2.binding.access.UpdateUserAccountWithConcurrencyResponse;
import com.oup.eac.ws.v2.binding.common.ErrorStatus;
import com.oup.eac.ws.v2.binding.common.PasswordCredential;
import com.oup.eac.ws.v2.binding.common.UpdateUserWithConcurrency;
import com.oup.eac.ws.v2.binding.common.WsUserId;
import com.oup.eac.ws.v2.binding.common.types.StatusCode;
import com.oup.eac.ws.v2.ex.WebServiceException;
import com.oup.eac.ws.v2.ex.WebServiceValidationException;
import com.oup.eac.ws.v2.service.UpdateUserAccountWithConcurrencyAdapter;
import com.oup.eac.ws.v2.service.WsCustomerLookup;
import com.oup.eac.ws.v2.service.utils.ErrorStatusUtils;
import com.oup.eac.ws.v2.service.utils.LocaleUtils;

/**
 * The Class UpdateUserAccountWithConcurrencyAdapterImpl.
 */
public class UpdateUserAccountWithConcurrencyAdapterImpl extends BaseUserAdapter implements UpdateUserAccountWithConcurrencyAdapter {

    private static final Logger LOG = Logger.getLogger(UpdateUserAccountWithConcurrencyAdapterImpl.class);
    
    private final WsCustomerLookup customerLookup;
    
    public UpdateUserAccountWithConcurrencyAdapterImpl(MessageSource messageSource, CustomerService customerService, WsCustomerLookup customerLookup, UsernameValidator usernameValidator) {
        super(messageSource,customerService, usernameValidator);
        Assert.notNull(customerLookup);
        this.customerLookup = customerLookup;
    }
    

    @Override
    @PreAuthorize("hasRole('ROLE_WS_UPDATE_USER_WITH_CONCURRENCY')")
    public UpdateUserAccountWithConcurrencyResponse updateUserAccountWithConcurrency(UpdateUserAccountWithConcurrencyRequest request) throws WebServiceException {
        UpdateUserAccountWithConcurrencyResponse response = new UpdateUserAccountWithConcurrencyResponse();
        int userCC;
        try {
            WsUserId wsUserId = request.getWsUserId();
            Customer customer = this.customerLookup.getCustomerByWsUserId(wsUserId);
            String emailAddress = customer.getEmailAddress() ;
            UpdateUserWithConcurrency data = request.getUser();
            validateUpdateUser(customer, data);
            
            Locale locale = LocaleUtils.getLocale(data.getLocale());
            
            customer.setEmailAddress(data.getEmailAddress());//1
            customer.setFirstName(data.getFirstName());//2
            customer.setFamilyName(data.getLastName());//3         
            
            PasswordCredential passwordCredential = data.getCredentials().getPasswordCredential();
            customer.setUsername(passwordCredential.getUserName());
            String password = passwordCredential.getPassword();
            
            //we don't update the password if the password is blank
            if(StringUtils.isNotBlank(password)){
                customer.setPassword(new Password(password,false));
            }
            customer.setLocale(locale);//5
            customer.setTimeZone(data.getTimeZone());//6
            
            //set user concurrency
            CustomerType ct = CustomerType.SPECIFIC_CONCURRENCY;
            userCC = data.getUserConcurrency();
            if(userCC == 0){
                userCC = CustomerType.SELF_SERVICE.getConcurrency();
            }
            if(userCC < CustomerType.SELF_SERVICE.getConcurrency()){
            	 error(ERR_USER_CONCURRENCY_LESS_THAN_DEFAULT); 
            }
            ct.setConcurrency(userCC);
            customer.setCustomerType(ct);
            
            this.customerService.updateCustomerFromWS(customer, emailAddress);
            
            LOG.debug("updated user account " + customer.getId());
            
        } catch (PasswordPolicyViolatedServiceLayerException ex) {
            ErrorStatus status = ErrorStatusUtils.getClientErrorStatus(ex.getMessage());
            response.setErrorStatus(status);
        } catch (WebServiceValidationException ex) {
        	if(ex.getMessage().contains("could not update"))
        	{
        		ErrorStatus status = ErrorStatusUtils.getServerErrorStatus(ex.getMessage());
        		response.setErrorStatus(status);
        	}
        	else
        	{
        		ErrorStatus status = ErrorStatusUtils.getClientErrorStatus(ex.getMessage());
        		response.setErrorStatus(status);
        	}
        } catch (UsernameExistsException ex) {
        	response.setErrorStatus(ErrorStatusUtils.getErrorStatus(StatusCode.CLIENT_ERROR, ERR_USERNAME_ALREADY_TAKEN));
        	//error(ERR_USERNAME_ALREADY_TAKEN);
        } catch (ServiceLayerValidationException slve) {
            ErrorStatus status = ErrorStatusUtils.getClientErrorStatus(slve.getMessage());
            response.setErrorStatus(status);
        } catch (ServiceLayerException sle){
            throw new WebServiceException("problem updating user account", sle);
        } catch (ErightsException e) {
        	ErrorStatus status = ErrorStatusUtils.getClientErrorStatus(e.getMessage());
            response.setErrorStatus(status);
		}
        return response;
    }

    private void validateUpdateUser(Customer existing, UpdateUserWithConcurrency data) throws WebServiceValidationException, ErightsException{
        validateEmail(data.getEmailAddress());
        if(data.getCredentials().getIpCredential() != null){
            error(ERR_IP_CREDENTIALS_UNSUPPORTED);
        }
        PasswordCredential passwordCredential = data.getCredentials().getPasswordCredential();
        String username = passwordCredential.getUserName();
        String password = passwordCredential.getPassword();
        validateUsername(username);
        //we ignore blank passwords when updating users
        if(!StringUtils.isBlank(password)){
            validatePassword(password);
        }
        
        validateLocale(data.getLocale());
        data.setTimeZone(validateTimeZone(data.getTimeZone()));
        
        validateUserConcurrency(data.getUserConcurrency());
        
        
        if(data.getFirstName()!=null && !data.getFirstName().isEmpty())
        	if(data.getFirstName().length()>255)
        		error("could not update: ["+ existing.getClass() +"#"+existing.getId()+"]");
        
        if(data.getLastName()!=null && !data.getLastName().isEmpty())
        	if(data.getLastName().length() > 255)
        		error("could not update: ["+ existing.getClass() +"#"+existing.getId()+"]");
        
    }
    
    private void validateUserConcurrency(int userConcurrency) throws WebServiceValidationException {
        if(userConcurrency < 0){
            error(ERR_USER_CONCURRENCY_NEGATIVE + userConcurrency);            
        }
           
    }
    
}
