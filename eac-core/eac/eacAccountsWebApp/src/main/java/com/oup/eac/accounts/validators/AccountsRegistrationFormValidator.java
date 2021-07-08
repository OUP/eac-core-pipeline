package com.oup.eac.accounts.validators;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.oup.eac.common.utils.crypto.PasswordUtils;
import com.oup.eac.common.utils.email.InternationalEmailAddress;
import com.oup.eac.common.utils.username.UsernameValidator;
import com.oup.eac.domain.Customer;
import com.oup.eac.dto.AccountRegistrationDto;
import com.oup.eac.integration.facade.exceptions.ErightsException;
import com.oup.eac.service.CustomerService;

/**
 * Spring MVC form validator for Account Registration.  
 * @author Gaurav Soni
 */

@Component
public class AccountsRegistrationFormValidator implements Validator{

    private final CustomerService customerService;
    private final UsernameValidator usernameValidator;
    
    @Autowired
    public AccountsRegistrationFormValidator(CustomerService customerService, UsernameValidator usernameValidator) {
        this.customerService = customerService;
        this.usernameValidator = usernameValidator;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        final boolean supports = AccountRegistrationDto.class.isAssignableFrom(clazz);
        return supports;
    }

    @Override
    public void validate(Object target, Errors errors) {
        AccountRegistrationDto accountRegistrationFbo = (AccountRegistrationDto) target;
        
        if (StringUtils.isBlank(accountRegistrationFbo.getFirstName())) {
            errors.rejectValue("firstName", "error.not-specified", new Object[] { "title.firstname" }, "First Name is required.");
            return;
        }

        if (StringUtils.isBlank(accountRegistrationFbo.getFamilyName())) {
            errors.rejectValue("familyName", "error.not-specified", new Object[] { "title.familyname" }, "Surname is required.");
            return;
        }
        
        if (StringUtils.isBlank(accountRegistrationFbo.getUsername())) {
            errors.rejectValue("username", "error.not-specified", new Object[] { "title.username" }, "Username is required.");
            return;
        } else {
            if (!usernameValidator.isValid(accountRegistrationFbo.getUsername())) {
                errors.rejectValue("username", "error.must.be.valid.username", new Object[] { "title.username" }, "Username must be valid.");
                return;
            } else {
                Customer user = null;
				try {
					user = customerService.getCustomerByUsername(accountRegistrationFbo.getUsername());
				} catch (ErightsException e) {
					// TODO Auto-generated catch block
					//e.printStackTrace();
				}
                if (user != null) {
                    errors.rejectValue("username", "error.username.taken.update", "This username is already taken. Please try another.");
                    return;
                }
            }
        }
        
        
        if (StringUtils.isBlank(accountRegistrationFbo.getEmail())) {
            errors.rejectValue("email", "error.not-specified", new Object[] { "title.email" }, "Email address is required.");
            return;
        } else if (!InternationalEmailAddress.isValid(accountRegistrationFbo.getEmail())) {
            errors.rejectValue("email", "error.must.be.valid.email", new Object[] { "title.email" }, "Email address must be a valid email address.");
            return;
        }
        

        if (StringUtils.isBlank(accountRegistrationFbo.getPassword())) {
            errors.rejectValue("password", "error.not-specified", new Object[] { "title.password" }, "Password is required.");
            return;
        } else {
            boolean valid = PasswordUtils.isPasswordValid(accountRegistrationFbo.getPassword());
            if (!valid) {
                errors.rejectValue("password", PasswordUtils.INVALID_PASSWORD_MSG_CODE, new Object[] { "title.password" }, "Password is too easy.");
                return;
            }
        }
        
        
        if (StringUtils.isBlank(accountRegistrationFbo.getConfirmPassword())) {
            errors.rejectValue("confirmPassword", "error.not-specified", new Object[] { "title.confirmpassword" }, "Confirm Password is required.");
            return;
        } else if (!accountRegistrationFbo.getConfirmPassword().equals(accountRegistrationFbo.getPassword())) {
            errors.reject("error.must.be.same", new Object[] { "title.password", "title.confirmpassword" }, "Password and "
                    + "Confirm Password must be the same");
            return;
        }
        
        if(!accountRegistrationFbo.isReadOnly()){
            errors.rejectValue("readOnly", "error.not-specified", new Object[] { "title.registration.tandc" }, "Terms and Conditions acceptance is required.");
            /*errors.rejectValue("readOnly","error.tandc.not.checked","Please accept terms and conditions.");*/
            return;
        }
    }
}
