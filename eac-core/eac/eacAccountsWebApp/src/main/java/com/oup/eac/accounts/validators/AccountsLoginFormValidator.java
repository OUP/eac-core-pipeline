package com.oup.eac.accounts.validators;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import com.oup.eac.dto.LoginDto;

/**
 * Spring MVC form validator for Login.  
 * @author Gaurav Soni
 */

@Component
public class AccountsLoginFormValidator implements Validator{

    @Override
    public boolean supports(Class<?> clazz) {
        final boolean supports = LoginDto.class.isAssignableFrom(clazz);
        return supports;
    }

    @Override
    public void validate(Object target, Errors errors) {
        LoginDto loginDto = (LoginDto) target;

        if (StringUtils.isBlank(loginDto.getUsername())) {
            errors.rejectValue("username", "error.not-specified", new Object[] { "label.username" }, "Username is required.");
            return;
        }
        if (StringUtils.isBlank(loginDto.getPassword())) {
            errors.rejectValue("password", "error.not-specified", new Object[] { "label.password" }, "Password is required.");
            return;
        }
    }
}
