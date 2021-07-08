package com.oup.eac.web.validators.login;

import org.apache.commons.lang.StringUtils;
import org.springframework.validation.Errors;

import com.oup.eac.common.utils.crypto.PasswordUtils;
import com.oup.eac.dto.ChangePasswordDto;
import com.oup.eac.web.validators.EACValidator;

/**
 * @author harlandd Password reset form validator
 */
public class ChangePasswordFormValidator extends EACValidator {

    /**
     * @param clazz
     *            the class
     * @return does this validator support this class
     */
    public final boolean supports(final Class clazz) {
        return ChangePasswordDto.class.isAssignableFrom(clazz);
    }

    /**
     * @param obj
     *            the command object
     * @param errors
     *            the errors
     */
    public final void validate(final Object obj, final Errors errors) {
        ChangePasswordDto changePasswordDto = (ChangePasswordDto) obj;

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
