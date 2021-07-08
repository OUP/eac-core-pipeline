package com.oup.eac.web.validators.login;

import org.apache.commons.lang.StringUtils;
import org.springframework.validation.Errors;

import com.oup.eac.dto.LoginDto;
import com.oup.eac.web.validators.EACValidator;

/**
 * @author harlandd Login form validator
 */
public class LoginFormValidator extends EACValidator {

    /**
     * @param clazz
     *            the class
     * @return does this validator support this class
     */
    public final boolean supports(final Class clazz) {
        return LoginDto.class.isAssignableFrom(clazz);
    }

    /**
     * @param obj
     *            the command object
     * @param errors
     *            the errors
     */
    public final void validate(final Object obj, final Errors errors) {
        LoginDto loginFbo = (LoginDto) obj;

        if (StringUtils.isBlank(loginFbo.getUsername())) {
            errors.rejectValue("username", "error.not-specified", new Object[] { "label.username" }, "Username is required.");
            return;
        }

        if (StringUtils.isBlank(loginFbo.getPassword())) {
            errors.rejectValue("password", "error.not-specified", new Object[] { "label.password" }, "Password is required.");
            return;
        }

    }

}
