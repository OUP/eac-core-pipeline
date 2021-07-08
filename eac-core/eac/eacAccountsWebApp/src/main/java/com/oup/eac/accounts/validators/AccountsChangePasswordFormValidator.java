package com.oup.eac.accounts.validators;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.oup.eac.common.utils.crypto.PasswordUtils;
import com.oup.eac.dto.ChangePasswordDto;

/**
 * Spring MVC form validator for Change password.  
 * @author Gaurav Soni
 */

@Component
public class AccountsChangePasswordFormValidator implements Validator{

    @Override
    public boolean supports(Class<?> clazz) {
        final boolean supports = ChangePasswordDto.class.isAssignableFrom(clazz);
        return supports;
    }

    @Override
    public void validate(Object target, Errors errors) {
        ChangePasswordDto changePasswordDto = (ChangePasswordDto) target;

        if (StringUtils.isBlank(changePasswordDto.getNewPassword())) {
            errors.rejectValue("passwordcheck", "error.not-specified", new Object[] { "label.newpassword" }, "New Password " + "is required.");
            return;
        }
        
        boolean valid = PasswordUtils.isPasswordValid(changePasswordDto.getNewPassword());
        if (!valid) {
            errors.rejectValue("passwordcheck", PasswordUtils.INVALID_PASSWORD_MSG_CODE, new Object[] { "label.password" }, "Password is too easy.");
            return;
        }

        if (StringUtils.isBlank(changePasswordDto.getConfirmNewPassword())) {
            errors.rejectValue("confirmNewPassword", "error.not-specified", new Object[] { "label.confirmnewpassword" }, "Confirm New Password is required.");
            return;
        }

        if (!changePasswordDto.getNewPassword().equals(changePasswordDto.getConfirmNewPassword())) {
            errors.reject("error.must.be.same", new Object[] { "label.newpassword", "label.confirmnewpassword" }, "New Password and "
                    + "Confirm New Password must be the same");
            return;
        }
    }
}
