package com.oup.eac.ws.v2.service.impl;

import java.util.List;
import java.util.Locale;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTimeZone;
import org.springframework.context.MessageSource;
import org.springframework.util.Assert;

import com.oup.eac.common.date.utils.DateUtils;
import com.oup.eac.common.utils.crypto.PasswordUtils;
import com.oup.eac.common.utils.email.InternationalEmailAddress;
import com.oup.eac.common.utils.username.UsernameValidator;
import com.oup.eac.dto.WsUserIdDto;
import com.oup.eac.integration.facade.exceptions.ErightsException;
import com.oup.eac.integration.facade.exceptions.UserNotFoundException;
import com.oup.eac.service.CustomerService;
import com.oup.eac.service.ServiceLayerException;
import com.oup.eac.ws.v2.binding.common.Credential;
import com.oup.eac.ws.v2.binding.common.LocaleType;
import com.oup.eac.ws.v2.ex.WebServiceValidationException;

public abstract class BaseUserAdapter implements WebServiceMessages {

    protected final CustomerService customerService;
    
    private final MessageSource messageSource;
    
    private final UsernameValidator usernameValidator;
    
    public BaseUserAdapter(MessageSource msgSource, CustomerService customerService, UsernameValidator usernameValidator) {
    	Assert.notNull(msgSource);
    	Assert.notNull(customerService);
        this.messageSource = msgSource;
        this.customerService = customerService;
        this.usernameValidator = usernameValidator;
    }
    
    
    protected void validateUsername(String username) throws WebServiceValidationException {
        validateNonBlankUsername(username);
        if(usernameValidator.isValid(username) == false){
        	error(ERR_USERNAME_IS_INVALID);
        }
    }
    
    protected void validateNonBlankUsername(String username) throws WebServiceValidationException {
        if (StringUtils.isBlank(username)) {
            error(ERR_USERNAME_CANNOT_BE_BLANK);
        }
        
    }

    protected void validatePassword(String password) throws WebServiceValidationException {
        if (StringUtils.isBlank(password)) {
            error(ERR_PASSWORD_CANNOT_BE_BLANK);
        }
        boolean passwordValid = PasswordUtils.isPasswordValid(password);
        if(!passwordValid){
            String msg = PasswordUtils.getInvalidPasswordMessage(this.messageSource);
            error(msg);
        }
    }
    
    protected void validateFirstAndLastName(String firstName,String lastName) throws ServiceLayerException {
		if ( (firstName != null && !firstName.isEmpty() && firstName.length()>255 ) || ( lastName != null && !lastName.isEmpty() && lastName.length() > 255 ) ) {
			throw new ServiceLayerException("Could not execute JDBC batch update");
		}
	}

    protected void validateExternalCustomerIds(List<String> externalIds) throws WebServiceValidationException {
		for (String externalId : externalIds) {
			if (externalId.length() > 255 ) {
				error("Could not execute JDBC batch update");
			}
		}
	}

    protected void validateEmail(String emailAddress) throws WebServiceValidationException {
        if (StringUtils.isBlank(emailAddress)) {
            error(ERR_EMAIL_CANNOT_BE_BLANK);
        }

        boolean isEmailValid = InternationalEmailAddress.isValid(emailAddress);
        if (!isEmailValid) {
            error(ERR_EMAIL_IS_INVALID);
        }
    }

    
    protected void validateCredentials(Credential cred) throws WebServiceValidationException {
        if(cred.getIpCredential() != null){
            error(ERR_IP_CREDENTIALS_UNSUPPORTED);
        }
    }

    protected void validateLocale(LocaleType locale) throws WebServiceValidationException {
        //locale is optional
        if(locale == null){
            return;
        }
        String lang = locale.getLanguage();
        String country = locale.getCountry();
        
        if(StringUtils.isBlank(lang)){
            error(ERR_LOCALE_LANGUAGE_CANNOT_BE_BLANK);
        }
        if(country != null){
            if(StringUtils.isBlank(country)){
                error(ERR_LOCALE_COUNTRY_CANNOT_BE_BLANK);
            }
        }
        
        //check that the language is known
        Locale loc = new Locale(lang);
        if(loc.getDisplayLanguage().equals(lang.toLowerCase())){
            error(ERR_LANGUAGE_INVALID + lang);
        }
        
        //check that the country is known
        if(country != null){
            loc = new Locale(lang,country);            
            if(loc.getDisplayCountry().equals(country.toUpperCase())){
                error(ERR_COUNTRY_INVALID + country);
            }
        }
    }
    
    protected String validateTimeZone(String timeZoneID) throws WebServiceValidationException {
    	for(String tz : DateTimeZone.getAvailableIDs()){
			if(tz.equalsIgnoreCase(timeZoneID)){
				timeZoneID = tz;
				break;
			}
		}
    	boolean isValid = DateUtils.isTimeZoneValid(timeZoneID);
        if(!isValid){
            error(ERR_TIME_ZONE_ID_INVALID + timeZoneID);
        }
        return timeZoneID;
    }
    
    protected void error(String msg) throws WebServiceValidationException {
        throw new WebServiceValidationException(msg);
    }

    public void validateNewUsername(String candidateUsername) throws WebServiceValidationException,UserNotFoundException, ErightsException{
        validateUsername(candidateUsername);
        //Check added for usernameexistence
        String userId = null ;
        WsUserIdDto wsUserIdDto = new WsUserIdDto() ;
        wsUserIdDto.setUserName(candidateUsername);
        try{
        	userId = customerService.validateUserAccount(wsUserIdDto);
        }catch(ErightsException e){
        	//if exception throws then user is not exist
        }
        
        if (userId != null) {
        	error(ERR_USERNAME_ALREADY_TAKEN);
        }
    }
    
}
