package com.oup.eac.web.validators.login;

import org.apache.commons.lang.StringUtils;
import org.springframework.validation.Errors;

import com.oup.eac.dto.PasswordResetDto;
import com.oup.eac.web.validators.EACValidator;

/**
 * @author harlandd Password reset form validator
 */
public class PasswordResetFormValidator extends EACValidator {

    /**
     * @param clazz
     *            the class
     * @return does this validator support this class
     */
    @Override
    public final boolean supports(final Class clazz) {
        return PasswordResetDto.class.isAssignableFrom(clazz);
    }

    /**
     * @param obj
     *            the command object
     * @param errors
     *            the errors
     */
    @Override
    public final void validate(final Object obj, final Errors errors) {
        final PasswordResetDto passwordResetFbo = (PasswordResetDto) obj;

        if (StringUtils.isBlank(passwordResetFbo.getUsername())) {
            errors.rejectValue("username", "error.not-specified", new Object[] { "label.username" }, "Username is required.");
            return;
        }

    }

}
