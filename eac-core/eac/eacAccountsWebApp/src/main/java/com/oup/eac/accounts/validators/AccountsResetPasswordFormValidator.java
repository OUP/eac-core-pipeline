package com.oup.eac.accounts.validators;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.oup.eac.dto.PasswordResetDto;

/**
 * Spring MVC form validator for Reset password.  
 * @author Gaurav Soni
 */

@Component
public class AccountsResetPasswordFormValidator implements Validator{

    @Override
    public boolean supports(Class<?> clazz) {
        final boolean supports = PasswordResetDto.class.isAssignableFrom(clazz);
        return supports;
    }

    @Override
    public void validate(Object target, Errors errors) {
        final PasswordResetDto passwordResetDto = (PasswordResetDto) target;
        if (StringUtils.isBlank(passwordResetDto.getUsername())) {
            errors.rejectValue("username", "error.not-specified", new Object[] { "label.username" }, "Username is required.");
            return;
        }
    }
}
